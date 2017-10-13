package org.hhg.rpi.telegram.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TelegramUpdate {
	@JsonProperty(value = "update_id")
	private long updateId;
	@JsonProperty(value = "message")
	private TelegramMessage message;
	@JsonProperty(value = "callback_query")
	private String callbackQuery;

	public long getUpdateId() {
		return updateId;
	}

	public void setUpdateId(long updateId) {
		this.updateId = updateId;
	}

	public TelegramMessage getMessage() {
		return message;
	}

	public void setMessage(TelegramMessage message) {
		this.message = message;
	}

	public String getCallbackQuery() {
		return callbackQuery;
	}

	public void setCallbackQuery(String callbackQuery) {
		this.callbackQuery = callbackQuery;
	}

}
