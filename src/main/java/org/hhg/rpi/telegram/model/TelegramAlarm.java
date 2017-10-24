package org.hhg.rpi.telegram.model;

import java.time.LocalDateTime;
import java.time.LocalTime;

public class TelegramAlarm {
	/**
	 * Defines the type of alarm.
	 * 
	 * A "single" alarm will only notify the user once. A "repeating" alarm will
	 * keep alerting it's user.
	 * 
	 * @author Harold Hormaechea
	 *
	 */
	public enum ALARM_TYPE {
		SINGLE, REPEATING
	}

	private Long userId;
	private Long chatId;
	private String busLine;
	private Integer busStop;
	private Integer minutes;
	private ALARM_TYPE type;
	private LocalTime alarmReferenceTime;
	private boolean raised;
	private boolean active;
	private LocalDateTime lastRaiseTime;

	public TelegramAlarm() {
		active = true;
	}

	public TelegramAlarm(Long userId, Long chatId, String busLine, Integer busStop, Integer minutes, ALARM_TYPE type) {
		this();
		this.userId = userId;
		this.chatId = chatId;
		this.busLine = busLine;
		this.busStop = busStop;
		this.minutes = minutes;
		this.type = type;
	}

	public TelegramAlarm(Long userId, Long chatId, String busLine, Integer busStop, Integer minutes, ALARM_TYPE type,
			LocalTime referenceTime) {
		this(userId, chatId, busLine, busStop, minutes, type);
		this.alarmReferenceTime = referenceTime;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public LocalDateTime getLastRaiseTime() {
		return lastRaiseTime;
	}

	public void setLastRaiseTime(LocalDateTime lasRaiseTime) {
		this.lastRaiseTime = lasRaiseTime;
	}

	public LocalTime getAlarmReferenceTime() {
		return alarmReferenceTime;
	}

	public void setAlarmReferenceTime(LocalTime alarmReferenceTime) {
		this.alarmReferenceTime = alarmReferenceTime;
	}

	public ALARM_TYPE getType() {
		return type;
	}

	public void setType(ALARM_TYPE type) {
		this.type = type;
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
