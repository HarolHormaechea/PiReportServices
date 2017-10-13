package org.hhg.rpi.telegram.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TelegramCallbackQuery {
	@JsonProperty(value = "id")
	private Long id;
	@JsonProperty(value = "data")
	private String data;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

}
