package org.hhg.rpi.telegram.bot.tus;

import static org.hhg.rpi.telegram.utils.Constants.TELEGRAM_GET_UPDATES_SUFFIX;
import static org.hhg.rpi.telegram.utils.Constants.TELEGRAM_SEND_MESSAGE_SUFFIX;
import static org.hhg.rpi.telegram.utils.Constants.TELEGRAM_URL_PREFIX;

import java.util.Arrays;
import java.util.HashMap;

import org.hhg.rpi.telegram.model.TelegramResponse;
import org.hhg.rpi.telegram.utils.CustomHttpRequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class TelegramMessageService implements TelegramMessageServiceInterface {
	
	@Value("${rpi.telegram.bot.id}")
	private String botApiKey;

	private RestTemplate restTemplate = new RestTemplate();
	{
		restTemplate.setInterceptors(
				Arrays.asList(new CustomHttpRequestInterceptor[] { new CustomHttpRequestInterceptor() }));
	}

	
	@Override
	public TelegramResponse getUpdates(Long lastHandledUpdateId){
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("offset", lastHandledUpdateId + 1);
		return restTemplate.getForObject(TELEGRAM_URL_PREFIX + botApiKey + TELEGRAM_GET_UPDATES_SUFFIX,
				TelegramResponse.class, params);
	}

	@Override
	public <T> T sendMessage(Long repliedMessageId, Long chatId, String text, String markup, Class<T> clazz) {
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("chat_id", chatId);
		params.put("text", text);
		params.put("markup", markup);
		params.put("replyId", repliedMessageId);
		return (T) restTemplate.postForObject(TELEGRAM_URL_PREFIX + botApiKey + TELEGRAM_SEND_MESSAGE_SUFFIX,
				null, clazz, params);
	}
}
