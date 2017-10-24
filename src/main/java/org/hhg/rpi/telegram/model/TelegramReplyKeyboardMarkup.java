package org.hhg.rpi.telegram.model;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TelegramReplyKeyboardMarkup extends TelegramMarkup implements Serializable{
	private static final long serialVersionUID = -8346343024615178420L;
	
	@JsonProperty(value = "keyboard")
	private List<List<TelegramKeyboardButton>> buttonList = new LinkedList<List<TelegramKeyboardButton>>();
	@JsonProperty(value = "resize_keyboard")
	private boolean resize;
	@JsonProperty(value = "one_time_keyboard")
	private boolean oneTime;
	@JsonProperty(value = "selective")
	private boolean selective;

	public List<List<TelegramKeyboardButton>> getButtonList() {
		return buttonList;
	}

	public void setButtonList(List<List<TelegramKeyboardButton>> buttonList) {
		this.buttonList = buttonList;
	}

	public boolean isOneTime() {
		return oneTime;
	}

	public void setOneTime(boolean oneTime) {
		this.oneTime = oneTime;
	}

	public boolean isSelective() {
		return selective;
	}

	public void setSelective(boolean selective) {
		this.selective = selective;
	}

	public boolean isResize() {
		return resize;
	}

	public void setResize(boolean resize) {
		this.resize = resize;
	}

}
