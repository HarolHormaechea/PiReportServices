package org.hhg.rpi.telegram.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TelegramMessage {
	@JsonProperty(value="message_id")
	private Long messageId;
	@JsonProperty(value="from")
	private TelegramUser fromUser;
	@JsonProperty(value="chat")
	private TelegramChat fromChat;
	@JsonProperty(value="date")
	private Date date;
	@JsonProperty(value="text")
	private String text;

	public Long getMessageId() {
		return messageId;
	}

	public void setMessageId(Long messageId) {
		this.messageId = messageId;
	}

	public TelegramUser getFromUser() {
		return fromUser;
	}

	public void setFromUser(TelegramUser fromUser) {
		this.fromUser = fromUser;
	}

	public TelegramChat getFromChat() {
		return fromChat;
	}

	public void setFromChat(TelegramChat fromChat) {
		this.fromChat = fromChat;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

}
