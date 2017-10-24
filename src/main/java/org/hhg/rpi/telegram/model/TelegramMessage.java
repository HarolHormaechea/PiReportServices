package org.hhg.rpi.telegram.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TelegramMessage {
	@JsonProperty(value = "message_id")
	private Long messageId;
	@JsonProperty(value = "from")
	private TelegramUser fromUser;
	@JsonProperty(value = "chat")
	private TelegramChat fromChat;
	@JsonProperty(value = "date")
	private Date date;
	@JsonProperty(value = "text")
	private String text;
	@JsonProperty(value = "callback_query")
	private TelegramCallbackQuery callback;
	@JsonProperty(value = "inline_query")
	private String inline_query;
	@JsonProperty(value = "chosen_inline_result")
	private String chosen_inline_result;
	@JsonProperty(value = "reply_to_message_id")
	private Long idMessageReplied;

	public Long getIdMessageReplied() {
		return idMessageReplied;
	}

	public void setIdMessageReplied(Long idMessageReplied) {
		this.idMessageReplied = idMessageReplied;
	}

	public String getInline_query() {
		return inline_query;
	}

	public void setInline_query(String inline_query) {
		this.inline_query = inline_query;
	}

	public String getChosen_inline_result() {
		return chosen_inline_result;
	}

	public void setChosen_inline_result(String chosen_inline_result) {
		this.chosen_inline_result = chosen_inline_result;
	}

	public Long getMessageId() {
		return messageId;
	}

	public TelegramCallbackQuery getCallback() {
		return callback;
	}

	public void setCallback(TelegramCallbackQuery callback) {
		this.callback = callback;
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
