package org.hhg.rpi.telegram.bot.tus;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import org.hhg.rpi.telegram.model.TelegramAlarm;
import org.hhg.rpi.telegram.model.TelegramAlarm.ALARM_TYPE;
import org.hhg.rpi.telegram.tus.model.TUSBusEstimationResponse;
import org.hhg.rpi.telegram.tus.model.TUSEstimationItem;
import org.hhg.rpi.telegram.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class TUSAlarmService implements TUSAlarmServiceInterface {
	private LinkedList<TelegramAlarm> alarmList = new LinkedList<TelegramAlarm>();

	@Autowired
	private TUSDataRetrievalServiceInterface dataService;

	@Autowired
	private TelegramMessageServiceInterface messageService;

	@Override
	public void createAlarm(Long userId, Long chatId, String busLine, Integer busStop, Integer minutes,
			boolean repeating, LocalTime refTime) {
		TelegramAlarm alarm = new TelegramAlarm(userId, chatId, busLine, busStop, minutes,
				repeating ? ALARM_TYPE.REPEATING : ALARM_TYPE.SINGLE, refTime);
		alarmList.addFirst(alarm);
	}

	/**
	 * Method to check if any alarms should be raised for buses due to arrival.
	 */
	@Scheduled(fixedDelay = Constants.TUS_ALARM_CHECK_RATE)
	public void checkAlarms(){
		for(TelegramAlarm alarm : alarmList){
			// For every alarm, we will check if it has been raised. If it has not, we will check if the bus
			// is due to arrival in that time.
			// If no buses for that line are due to arrival at the specified bus station, this service will
			// reply the user with that information and mark the alarm as "raised". Otherwise, it'll wait, and
			// only raise an alarm when the requested time is under the specified limit.
			if(checkAlarm(alarm)){
				LinkedList<TUSEstimationItem> matchingItems = new LinkedList<TUSEstimationItem>();
				TUSBusEstimationResponse busInfoResponse = dataService.retrieveBusInformation(alarm.getBusStop());
				for(TUSEstimationItem item : busInfoResponse.getResults()){
					if(item.getBusLine().equals(alarm.getBusLine()) && (alarm.getMinutes() == null || (item.getFirstTime() / 60 <= alarm.getMinutes()))){
						matchingItems.add(item);
						alarm.setRaised(true);
						alarm.setLastRaiseTime(LocalDateTime.now());
					}
				}
				String message = null;
				if (matchingItems.size() == 0) {
					message = "Atención! No hay autobuses que cumplan los criterios de la alarma configurada para las "+alarm.getAlarmReferenceTime()+", linea "+alarm.getBusLine() +" y parada "+alarm.getBusStop();
				} else if (matchingItems.size() == 1) {
					message = "Atención! El autobús de línea "+alarm.getBusLine()+" para la parada "+dataService.retrieveBusStopInformation(alarm.getBusStop()).getStationName()+" llegará en "+matchingItems.getFirst().getFirstTime() / 60+" minutos.";
				} else {
					message = "Atención! Se han encontrado varios autobuses de la linea "+alarm.getBusLine()+" acercándose a la parada "+dataService.retrieveBusStopInformation(alarm.getBusStop()).getStationName();
					for(TUSEstimationItem item : matchingItems){
						message+="\n 🚌 "+item.getFirstTime() / 60+" minutos.";
					}
				}
				messageService.sendMessage(null, alarm.getChatId(), message, "", Object.class);
			}
		}

	}

	private boolean checkAlarm(TelegramAlarm alarm) {
		boolean result = false;
		if (alarm.isActive()&& ALARM_TYPE.SINGLE.equals(alarm.getType())) {
			result = !alarm.isRaised();
		} else if ( alarm.isActive() && ALARM_TYPE.REPEATING.equals(alarm.getType())) {
			LocalDateTime currentDateTime = LocalDateTime.now();
			// We extract how many minutes are between both dates
			long deltaTime = ChronoUnit.MINUTES.between(currentDateTime.toLocalTime(), alarm.getAlarmReferenceTime());
			// Checks if this alarm last trigger time is at least the day
			// before.
			boolean isTriggable = alarm.getLastRaiseTime() == null
					|| ChronoUnit.HOURS.between(alarm.getLastRaiseTime(), currentDateTime) >= 20;
			result = isTriggable && Math.abs(deltaTime) <= Constants.TUS_MAX_ALARM_TIME_DEVIATION;
		}
		return result;
	}

	@Override
	public List<TelegramAlarm> findAlarmsByChat(Long chatId){
		return alarmList.stream().filter(p -> p.getChatId().equals(chatId)).collect(Collectors.toList());
	}
	
	
	public void disableAlarm(long chatId, int alarmPositionInList){
		List<TelegramAlarm> chatAlarms = alarmList.stream().filter(p -> p.getChatId().equals(chatId)).collect(Collectors.toList());
		chatAlarms.get(alarmPositionInList -1).setActive(false);
	}
	
	public void enableAlarm(long chatId, int alarmPositionInList){
		List<TelegramAlarm> chatAlarms = alarmList.stream().filter(p -> p.getChatId().equals(chatId)).collect(Collectors.toList());
		chatAlarms.get(alarmPositionInList -1).setActive(true);
	}
}
