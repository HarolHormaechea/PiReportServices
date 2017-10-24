package org.hhg.rpi.telegram.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TelegramKeyboardButton {
	@JsonProperty(value = "text")
	private String text;
	@JsonProperty(value = "request_contact")
	private boolean requestContact;
	@JsonProperty(value = "request_location")
	private boolean requestLocation;
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public boolean isRequestContact() {
		return requestContact;
	}
	public void setRequestContact(boolean requestContact) {
		this.requestContact = requestContact;
	}
	public boolean isRequestLocation() {
		return requestLocation;
	}
	public void setRequestLocation(boolean requestLocation) {
		this.requestLocation = requestLocation;
	}
	
	
}
