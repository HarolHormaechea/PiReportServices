package org.hhg.rpi.telegram.bot.tus;

import java.time.LocalTime;
import java.util.List;

import org.hhg.rpi.telegram.model.TelegramAlarm;

public interface TUSAlarmServiceInterface {

	public void createAlarm(Long userId, Long chatId, String busLine, Integer busStop, Integer minutes,
			boolean repeating, LocalTime refTime);

	public List<TelegramAlarm> findAlarmsByChat(Long chatId);

}
