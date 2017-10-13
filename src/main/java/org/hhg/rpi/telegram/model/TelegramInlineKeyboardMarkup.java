package org.hhg.rpi.telegram.model;

import java.io.Serializable;
import java.util.LinkedList;

public class TelegramInlineKeyboardMarkup extends TelegramMarkup implements Serializable{

	private static final long serialVersionUID = -1535322265848411181L;
	
	private LinkedList<LinkedList<TelegramInlineKeyboardButton>> keyboard = new LinkedList<LinkedList<TelegramInlineKeyboardButton>>();

	public LinkedList<LinkedList<TelegramInlineKeyboardButton>> getKeyboard() {
		return keyboard;
	}

	public void setKeyboard(LinkedList<LinkedList<TelegramInlineKeyboardButton>> keyboard) {
		this.keyboard = keyboard;
	}


}
