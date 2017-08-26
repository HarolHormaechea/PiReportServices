package org.hhg.rpi.telegram.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TelegramUser {
	@JsonProperty(value="id")
	private Long id;
	@JsonProperty(value="is_bot")
	private Boolean isBot;
	@JsonProperty(value="first_name")
	private String firstName;
	@JsonProperty(value="language_code")
	private String languageCode;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Boolean getIsBot() {
		return isBot;
	}

	public void setIsBot(Boolean isBot) {
		this.isBot = isBot;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLanguageCode() {
		return languageCode;
	}

	public void setLanguageCode(String languageCode) {
		this.languageCode = languageCode;
	}

}
