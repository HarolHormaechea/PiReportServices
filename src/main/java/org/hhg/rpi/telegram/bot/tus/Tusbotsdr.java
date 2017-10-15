
package org.hhg.rpi.telegram.bot.tus;

import static org.hhg.rpi.telegram.utils.Constants.TUS_UPDATE_TOLERANCE;

import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.hhg.rpi.reportservice.reporting.LogServiceInterface;
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
		if(queryText.length() > 6 && queryText.substring(0, 7).equals("/alarma")){
			result = initAlarm(message);
		}else{
			result = executeQuery(message);
			
		}
		
		messageService.sendMessage(message.getFromChat().getId(), result.getText(), result.getMarkup(), Object.class);
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
	private MessageResult initAlarm(TelegramMessage message) {
		MessageResult result = new MessageResult();
		String[] msg = message.getText().split(" ");
		
		try{
			Integer stop = Integer.valueOf(msg[1]);
			Integer minutes = Integer.valueOf(msg[3]);
			String line = msg[2];
			
			alarmService.createAlarm(message.getFromUser().getId(), message.getFromChat().getId(), line, stop, minutes);
			result.setText("Se ha creado una alarma para el próximo autobús de línea "+line+" y la parada "+busService.retrieveBusStopInformation(stop).getStationName()+". La alarma se enviará cuando falten como máximo "+minutes+" minutos para que llegue.");
		}catch(Exception ex){
			result.setText("No se proveyó un formato correcto. El formato de esta petición debe ser el siguiente:\n/alarma A B C\nA: Parada (el código)\nB: Línea (código)\nC: Minutos que deben faltar para enviar alarma de bus.\n Por ejemplo: /alarma 42 1 5\nGenerará una alarma que avisará con un mensaje del próximo autobús de la línea 1 que pase por la parada 42 al que le falten 5 minutos para llegar.");
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
						responseText += " - Linea " + estimacion.getBusLine() + ": " + estimacion.getFirstTime() / 60
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
