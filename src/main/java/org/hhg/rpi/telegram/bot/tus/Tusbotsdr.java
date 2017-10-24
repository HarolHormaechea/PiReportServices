
package org.hhg.rpi.telegram.bot.tus;

import static org.hhg.rpi.telegram.utils.Constants.TUS_UPDATE_TOLERANCE;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.hhg.rpi.reportservice.reporting.LogServiceInterface;
import org.hhg.rpi.telegram.model.TelegramAlarm;
import org.hhg.rpi.telegram.model.TelegramAlarm.ALARM_TYPE;
import org.hhg.rpi.telegram.model.TelegramInlineKeyboardButton;
import org.hhg.rpi.telegram.model.TelegramInlineKeyboardMarkup;
import org.hhg.rpi.telegram.model.TelegramMessage;
import org.hhg.rpi.telegram.model.TelegramResponse;
import org.hhg.rpi.telegram.model.TelegramUpdate;
import org.hhg.rpi.telegram.tus.model.BusStationInfo;
import org.hhg.rpi.telegram.tus.model.TUSBusEstimationResponse;
import org.hhg.rpi.telegram.tus.model.TUSEstimationItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class Tusbotsdr {

	// TODO: Store this in a database.
	private Long lastHandledUpdateId = 0L;

	@Autowired
	private LogServiceInterface logService;
	
	@Autowired
	private TUSDataRetrievalServiceInterface busService;
	
	@Autowired
	private TelegramMessageServiceInterface messageService;
	
	@Autowired
	private TUSAlarmServiceInterface alarmService;
	
	
	public Tusbotsdr() {

	}

	@Scheduled(fixedDelay = 1000)
	public void getUpdates() {
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("offset", lastHandledUpdateId + 1);
		try{
		TelegramResponse item = messageService.getUpdates(lastHandledUpdateId++);
		if (item != null && item.getOk() && item.getResult().size() > 0) {
			for (TelegramUpdate update : item.getResult()) {
				lastHandledUpdateId = update.getUpdateId();
				processUpdate(update);
			}

			logService.info("Processed Telegram Bot Update: "+item.getResult().size()+" messages replied.");
		} 
		
		}catch(Exception ex){

			logService.error("Error retrieving Telegram updates for bot.");
		}
	}

	public void processUpdate(TelegramUpdate update) {
		if (update.getMessage() != null) {
			processMessage(update.getMessage());
		}
		
	}
	
	/**
	 * As I can not retrieve callback data, I use this regex to define what to look for.
	 * @param message
	 * @return
	 */
	private String extractQueryMessage(String message){
		return message.contains(":") ? message.split(":")[0]: message;
	}

	/**
	 * Processes a query message / callback message as a bus arrival message.
	 * 
	 * @param message
	 */
	public void processMessage(TelegramMessage message) {
		String queryText = extractQueryMessage(message.getText());
		MessageResult result = null;
		// First, we will check if we have a /command command. If we don't, we will assume we are asked for stations.
		if(queryText.length() >= 7 && queryText.substring(0, 7).equals("/alarma")){
			result = initSingleAlarm(message);
		}else if(queryText.length() >= 6 && queryText.substring(0, 6).equals("/aviso")){
			result = initRepeatingAlarm(message);
		}else if(queryText.length() >= 7 && queryText.substring(0, 7).equals("/config")){
			result = checkAlarmConfig(message.getFromChat().getId());
		}else if(queryText.contains("🔕 Silenciar alarma")){
			
		}else if(queryText.contains("🔔 Reactivar alarma")){
			
		}else if(queryText.contains("❌ Eliminar alarma")){
			
		}else{
			result = executeQuery(message);
		}
		
		messageService.sendMessage(message.getMessageId(), message.getFromChat().getId(), result.getText(), result.getMarkup(), Object.class);
	}

	/**
	 * Retrieves the list of alarms created in this chat as buttons, so their configuration can be changed.
	 * 
	 * @param id
	 * @param id2
	 * @return
	 */
	private MessageResult checkAlarmConfig(Long chatId) {
		MessageResult result = new MessageResult();
		List<TelegramAlarm> chatAlarms = alarmService.findAlarmsByChat(chatId);
		if(chatAlarms != null && chatAlarms.size() > 0){
			result.setText("Se encontraron varias alarmas configuradas. Puedes usar los botones para revisarlas o eliminarlas usando su numero de alarma.");
			TelegramInlineKeyboardMarkup inlineKeyboard = new TelegramInlineKeyboardMarkup();
			//TelegramReplyKeyboardMarkup keyboard = new TelegramReplyKeyboardMarkup();
			
			int i = 1;
			for(TelegramAlarm alarm : chatAlarms){
				result.setText(result.getText() + "\nAlarma nº "+i+":\n   - Parada: "+busService.retrieveBusStopInformation(alarm.getBusStop()).getStationName()+"\n   - Linea: "+alarm.getBusLine());
				
				
				if(ALARM_TYPE.REPEATING.equals(alarm.getType())){
					result.setText(result.getText() + result.getText() + "\n   - Hora de notificación: "+alarm.getAlarmReferenceTime());
				}
				
				LinkedList<TelegramInlineKeyboardButton> row = new LinkedList<TelegramInlineKeyboardButton>();
				
				TelegramInlineKeyboardButton buttonToggleActivation = new TelegramInlineKeyboardButton();
				buttonToggleActivation.setText(alarm.isActive() ? "🔕 Silenciar alarma "+i : "🔔 Reactivar alarma "+i);
				buttonToggleActivation.setCallbackData(alarm.isActive() ? "/config silenciaralarma "+i : "/config activaralarma "+i);
				
				TelegramInlineKeyboardButton buttonRemove = new TelegramInlineKeyboardButton();
				buttonRemove.setText("❌ Eliminar alarma"+i);
				buttonRemove.setCallbackData("/config eliminaralarma "+i);
				
				row.add(buttonToggleActivation);
				row.add(buttonRemove);
				
				inlineKeyboard.getKeyboard().add(row);
				i++;
			}
			try {
				result.setMarkup(new ObjectMapper().writeValueAsString(inlineKeyboard));
			} catch (JsonProcessingException e) {
				logService.error("Error serializing choice keyboard: "+e.getMessage());
			}
		}else{
			result.setText("No hay alarmas configuradas en este chat.");
		}
		return result;
	}

	
	private MessageResult deactivateAlarm(TelegramMessage message){
		MessageResult result = new MessageResult();
		//alarmService.deactivateUserAlarm(Long userId, Long id);
		return result;
	}
	
	private MessageResult activateAlarm(TelegramMessage message){
		MessageResult result = new MessageResult();
		
		return result;
	}
	
	private MessageResult removeAlarm(TelegramMessage message){
		MessageResult result = new MessageResult();
		
		return result;
	}
	
	
	/**
	 * Initializes a new alarm.
	 * 
	 * The message must be formatted with /alarma {0} {1} {2}
	 * {0}: Bus Stop
	 * {1}: Bus Line 
	 * {2}: Minutes left for the bus
	 * 
	 * @param message
	 * @return
	 */
	private MessageResult initSingleAlarm(TelegramMessage message) {
		MessageResult result = new MessageResult();
		String[] msg = message.getText().split(" ");
		
		try{
			Integer stop = Integer.valueOf(msg[1]);
			Integer minutes = Integer.valueOf(msg[3]);
			String line = msg[2];
			
			alarmService.createAlarm(message.getFromUser().getId(), message.getFromChat().getId(), line, stop, minutes, false, null);
			result.setText("Se ha creado una alarma para el próximo autobús de línea "+line+" y la parada "+busService.retrieveBusStopInformation(stop).getStationName()+". La alarma se enviará cuando falten como máximo "+minutes+" minutos para que llegue.");
		}catch(Exception ex){
			result.setText("No se proveyó un formato correcto. El formato de esta petición debe ser el siguiente:\n/alarma A B C\nA: Parada (el código)\nB: Línea (código)\nC: Minutos que deben faltar para enviar alarma de bus.\n Por ejemplo: /alarma 42 1 5\nGenerará una alarma que avisará con un mensaje del próximo autobús de la línea 1 que pase por la parada 42 al que le falten 5 minutos para llegar.");
		}
		return result;
	}
	
	
	/**
	 * Initializes a new repeating daily alarm, which will warn its user about the next bus of the requested line
	 * arriving at the stop after the given hour.
	 * 
	 * The message must be formatted with /aviso {0} {1} {2}
	 * {0}: Bus Stop (ex. "42")
	 * {1}: Bus Line (ex. "6C1")
	 * {2}: HOUR:TIME (ex. "15:25")
	 * 
	 * @param message
	 * @return
	 */
	private MessageResult initRepeatingAlarm(TelegramMessage message) {
		MessageResult result = new MessageResult();
		String[] msg = message.getText().split(" ");
		
		try{
			Integer stop = Integer.valueOf(msg[1]);
			String line = msg[2];
			String[] refTimeStr = msg[3].split(":");
			LocalTime time = LocalTime.of(Integer.valueOf(refTimeStr[0]), Integer.valueOf(refTimeStr[1]));
			
			alarmService.createAlarm(message.getFromUser().getId(), message.getFromChat().getId(), line, stop, null, true, time);
			result.setText("Se ha creado una alarma con repeticion que avisará del próximo autobús de línea "+line+" que pase por la parada "+busService.retrieveBusStopInformation(stop).getStationName()+" a partir de las "+msg[3]);
		}catch(Exception ex){
			result.setText("No se proveyó un formato correcto. El formato de esta petición debe ser el siguiente:\n/aviso A B C\nA: Parada (el código)\nB: Línea (código)\nC: Hora a la que se debe comprobar los buses (HH:MM).\n Por ejemplo: /aviso 42 1 15:15\nGenerará una alarma que avisará con un mensaje cada día a las 15:15 sobre el próximo autobús de la línea 1 que pase por la parada 42 .");
		}
		return result;
	}


	/**
	 * Executes a bus query
	 * 
	 * @param message
	 * @return
	 */
	private MessageResult executeQuery(TelegramMessage message) {
		MessageResult result = new MessageResult();
		String queryText = extractQueryMessage(message.getText());
		try{
			Integer requestedBusStopId = Integer.parseInt(queryText);
			result.setText(retrieveBusInformation(requestedBusStopId, message.getFromUser().getFirstName()));
		}catch(NumberFormatException ex){
			// If we are not given a proper number as stop ID, we attempt to look for the closest station name
			// for the input text.
			List<BusStationInfo> matchingStops = busService.retrieveBusStopInformation(queryText);
			
			if(matchingStops.size() == 0){
				// If no stations match this given name, meeec...
				result.setText("Disculpa, no se encontró ninguna parada con este nombre.");
			}else if(matchingStops.size() == 1){
				// If our search returns exactly one station by this name, we will use its number as query value.
				result.setText(retrieveBusInformation(matchingStops.get(0).getStationNumber(),  message.getFromUser().getFirstName()));
			}else{
				// If our search returns  more than one stop, we will send back a keyboard with the different options received.
				TelegramInlineKeyboardMarkup inlineKeyboard = new TelegramInlineKeyboardMarkup();
				for(BusStationInfo matchingStation : matchingStops){
					TelegramInlineKeyboardButton button = new TelegramInlineKeyboardButton();
					button.setText(""+matchingStation.getStationNumber() + ": "+matchingStation.getStationName());
					button.setCallbackData(matchingStation.getStationNumber().toString());
					inlineKeyboard.getKeyboard().add(new LinkedList<TelegramInlineKeyboardButton>(Arrays.asList(button)));
				}
				result.setText("Se encontraron varias paradas con nombre similar. Por favor, elige la correcta de entre las opciones.");
				try {
					result.setMarkup(new ObjectMapper().writeValueAsString(inlineKeyboard));
				} catch (JsonProcessingException e) {
					logService.error("Error serializing choice keyboard: "+e.getMessage());
				}
			}
		}
		return result;
	}

	/**
	 * Retrieves information about the next buses due to arrive at the specified bus stop.
	 * 
	 * @param query
	 * @param username
	 * @return
	 */
	private String retrieveBusInformation(Integer query, String username) {
		String responseText = username+", aquí tienes el resultado de tu consulta ";
		try {
			// We retrieve the next buses.
			TUSBusEstimationResponse infoBuses = busService.retrieveBusInformation(query);
			// We extract information about the bus stop, if we have it on cache. If we don't, we ignore it. Only for
			// display purposes on the outgoing message.
			BusStationInfo stop = busService.retrieveBusStopInformation(query);
			String stopName = stop == null ? query.toString() : stop.getStationName();
			responseText += "para la parada "+stopName+":\n";
			if (infoBuses.getSummary().getItemNumber() > 0) {
				GregorianCalendar currentDate = new GregorianCalendar();
				Iterator<TUSEstimationItem> iterator = infoBuses.getResults().iterator();
				while (iterator.hasNext()) {
					TUSEstimationItem estimacion = iterator.next();
					if (ChronoUnit.MINUTES.between(estimacion.getLastUpdated().toInstant(),
							currentDate.toInstant()) < TUS_UPDATE_TOLERANCE) {
						estimacion.getLastUpdated().getTime();
						responseText += " 🚌  Linea " + estimacion.getBusLine() + ": " + estimacion.getFirstTime() / 60+" minutos."
								+ "\n";
					}
				}
			} else {
				responseText = "Disculpa, no se encontraron autobuses para la parada seleccionada.";
			}

		} catch (NumberFormatException ex) {
			responseText = "No fue provisto un numero de parada valido. Hay que introducir solo el numero (por ejemplo: 42)";
		}
		return responseText;
	}
	
	
	class MessageResult{
		private String text;
		private String markup;
		public String getText() {
			return text;
		}
		public void setText(String text) {
			this.text = text;
		}
		public String getMarkup() {
			return markup;
		}
		public void setMarkup(String markup) {
			this.markup = markup;
		}
		
		
	}
}
