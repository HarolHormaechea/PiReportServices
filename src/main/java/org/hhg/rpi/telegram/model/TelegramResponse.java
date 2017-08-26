package org.hhg.rpi.telegram.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TelegramResponse {
	private Boolean ok;
	private List<TelegramUpdate> result;

	public Boolean getOk() {
		return ok;
	}

	public void setOk(Boolean ok) {
		this.ok = ok;
	}

	public List<TelegramUpdate> getResult() {
		return result;
	}

	public void setResult(List<TelegramUpdate> result) {
		this.result = result;
	}

}
