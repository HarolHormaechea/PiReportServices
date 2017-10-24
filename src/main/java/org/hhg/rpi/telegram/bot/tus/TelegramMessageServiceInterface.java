package org.hhg.rpi.telegram.bot.tus;

import org.hhg.rpi.telegram.model.TelegramResponse;

public interface TelegramMessageServiceInterface {

	public TelegramResponse getUpdates(Long lastHandledUpdateId);

	public <T> T sendMessage(Long repliedMessageId, Long chatId, String text, String markup, Class<T> clazz);

}
