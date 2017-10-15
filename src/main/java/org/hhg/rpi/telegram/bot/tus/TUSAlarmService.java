package org.hhg.rpi.telegram.bot.tus;

import java.util.LinkedList;

import org.hhg.rpi.telegram.model.TelegramAlarm;
import org.hhg.rpi.telegram.tus.model.TUSBusEstimationResponse;
import org.hhg.rpi.telegram.tus.model.TUSEstimationItem;
import org.hhg.rpi.telegram.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class TUSAlarmService implements TUSAlarmServiceInterface{
	private LinkedList<TelegramAlarm> alarmList = new LinkedList<TelegramAlarm>();
	
	@Autowired
	private TUSDataRetrievalServiceInterface dataService;
	
	@Autowired
	private TelegramMessageServiceInterface messageService;
	
	@Override
	public void createAlarm(Long userId, Long chatId, String busLine, Integer busStop, Integer minutes) {
		TelegramAlarm alarm = new TelegramAlarm(userId, chatId, busLine, busStop, minutes);
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
			if(!alarm.isRaised()){
				TUSBusEstimationResponse busInfoResponse = dataService.retrieveBusInformation(alarm.getBusStop());
				for(TUSEstimationItem item : busInfoResponse.getResults()){
					if(item.getBusLine().equals(alarm.getBusLine()) && item.getFirstTime() / 60 <= alarm.getMinutes()){
						messageService.sendMessage(alarm.getChatId(), "Atención! El autobús de línea "+alarm.getBusLine()+" para la parada "+dataService.retrieveBusStopInformation(alarm.getBusStop()).getStationName()+" llegará en "+item.getFirstTime() / 60+" minutos.", "", Object.class);
						alarm.setRaised(true);
					}
				}
			}
		}
	}

}
