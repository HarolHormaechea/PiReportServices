package org.hhg.rpi.telegram.model;

public class TelegramAlarm {
	private Long userId;
	private Long chatId;
	private String busLine;
	private Integer busStop;
	private Integer minutes;
	private boolean raised;

	public TelegramAlarm() {
	}

	public TelegramAlarm(Long userId, Long chatId, String busLine, Integer busStop, Integer minutes) {
		super();
		this.userId = userId;
		this.chatId = chatId;
		this.busLine = busLine;
		this.busStop = busStop;
		this.minutes = minutes;
	}

	public Integer getMinutes() {
		return minutes;
	}

	public void setMinutes(Integer minutes) {
		this.minutes = minutes;
	}

	public boolean isRaised() {
		return raised;
	}

	public void setRaised(boolean raised) {
		this.raised = raised;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getChatId() {
		return chatId;
	}

	public void setChatId(Long chatId) {
		this.chatId = chatId;
	}

	public String getBusLine() {
		return busLine;
	}

	public void setBusLine(String busLine) {
		this.busLine = busLine;
	}

	public Integer getBusStop() {
		return busStop;
	}

	public void setBusStop(Integer busStop) {
		this.busStop = busStop;
	}

}
