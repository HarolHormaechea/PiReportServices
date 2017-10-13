package org.hhg.rpi.telegram.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TelegramInlineKeyboardButton implements Serializable{
	
	private static final long serialVersionUID = -5478003764352240590L;
	@JsonProperty(value = "text")
	private String text;
	@JsonProperty(value = "callback_data")
	private String callbackData;

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getCallbackData() {
		return callbackData;
	}

	public void setCallbackData(String callbackData) {
		this.callbackData = callbackData;
	}

}
