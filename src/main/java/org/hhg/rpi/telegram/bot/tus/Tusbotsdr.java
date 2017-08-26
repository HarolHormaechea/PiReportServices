
package org.hhg.rpi.telegram.bot.tus;

import static org.hhg.rpi.telegram.utils.Constants.TELEGRAM_GET_UPDATES_SUFFIX;
import static org.hhg.rpi.telegram.utils.Constants.TELEGRAM_SEND_MESSAGE_SUFFIX;
import static org.hhg.rpi.telegram.utils.Constants.TELEGRAM_URL_PREFIX;
import static org.hhg.rpi.telegram.utils.Constants.TUS_QUERY_URL;
import static org.hhg.rpi.telegram.utils.Constants.TUS_UPDATE_TOLERANCE;

import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;

import org.hhg.rpi.reportservice.reporting.LogServiceInterface;
import org.hhg.rpi.telegram.model.TelegramMessage;
import org.hhg.rpi.telegram.model.TelegramResponse;
import org.hhg.rpi.telegram.model.TelegramUpdate;
import org.hhg.rpi.telegram.tus.model.TUSEstimationResponse;
import org.hhg.rpi.telegram.tus.model.TUSResponse;
import org.hhg.rpi.telegram.utils.CustomHttpRequestInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class Tusbotsdr {

	// TODO: Store this in a database.
	private Long lastHandledUpdateId = 0L;

	@Autowired
	private LogServiceInterface logService;

	@Value("${rpi.telegram.bot.id}")
	private String botApiKey;

	private RestTemplate restTemplate = new RestTemplate();
	{
		restTemplate.setInterceptors(
				Arrays.asList(new CustomHttpRequestInterceptor[] { new CustomHttpRequestInterceptor() }));
	}

	public Tusbotsdr() {

	}

	@Scheduled(fixedDelay = 1000)
	public void getUpdates() {
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("offset", lastHandledUpdateId + 1);
		try{
		TelegramResponse item = restTemplate.getForObject(TELEGRAM_URL_PREFIX + botApiKey + TELEGRAM_GET_UPDATES_SUFFIX,
				TelegramResponse.class, params);
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

	public void processMessage(TelegramMessage message) {
		String responseText = retrieveBusInformation(message.getText());
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("chat_id", message.getFromChat().getId());
		params.put("text", responseText);
		Object response = restTemplate.postForObject(TELEGRAM_URL_PREFIX + botApiKey + TELEGRAM_SEND_MESSAGE_SUFFIX,
				null, Object.class, params);
		response.toString();
	}

	private String retrieveBusInformation(String query) {
		String responseText = "Resultado de la consulta:\n";
		try {
			Integer numParada = Integer.parseUnsignedInt(query);
			HashMap<String, Object> params = new HashMap<String, Object>();
			params.put("idParada", numParada);
			TUSResponse infoBuses = restTemplate.getForObject(TUS_QUERY_URL, TUSResponse.class, params);
			if (infoBuses.getSummary().getItemNumber() > 0) {
				GregorianCalendar currentDate = new GregorianCalendar();
				Iterator<TUSEstimationResponse> iterator = infoBuses.getResults().iterator();
				while (iterator.hasNext()) {
					TUSEstimationResponse estimacion = iterator.next();
					if (ChronoUnit.MINUTES.between(estimacion.getLastUpdated().toInstant(),
							currentDate.toInstant()) < TUS_UPDATE_TOLERANCE) {
						estimacion.getLastUpdated().getTime();
						responseText += " - Linea " + estimacion.getBusLine() + ": " + estimacion.getFirstTime() / 60
								+ "\n";
					}
				}
			} else {
				responseText = "No se encontraron autobuses para la parada seleccionada.";
			}

		} catch (NumberFormatException ex) {
			responseText = "No fue provisto un numero de parada valido. Hay que introducir solo el numero (por ejemplo: 42)";
		}
		return responseText;
	}
}
