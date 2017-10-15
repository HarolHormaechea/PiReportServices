package org.hhg.rpi.telegram.bot.tus;

public interface TUSAlarmServiceInterface {

	public void createAlarm(Long userId, Long chatId, String busLine, Integer busStop, Integer minutes);

}
