package org.hhg.rpi.telegram.tus.model;

import java.util.GregorianCalendar;

import org.hhg.rpi.telegram.tus.utils.TUSDateDeserializer;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TUSEstimationResponse implements Comparable<TUSEstimationResponse> {

	@JsonProperty(value="ayto:tiempo1")
	private Integer firstTime;
	@JsonProperty(value="ayto:distancia1")
	private Integer firstDistance;
	@JsonProperty(value="ayto:destino1")
	private String firstDestination;

	@JsonProperty(value="ayto:tiempo2")
	private Integer secondTime;
	@JsonProperty(value="ayto:distancia2")
	private Integer secondDistance;
	@JsonProperty(value="ayto:destino2")
	private String secondDestination;

	@JsonProperty(value="ayto:paradaId")
	private Integer stopId;
	@JsonProperty(value="ayto:etiqLinea")
	private String busLine;
	
	@JsonProperty(value="ayto:fechActual")
	@JsonDeserialize(using=TUSDateDeserializer.class)
	private GregorianCalendar lastUpdated;

	public Integer getFirstTime() {
		return firstTime;
	}

	public void setFirstTime(Integer firstTime) {
		this.firstTime = firstTime;
	}

	public Integer getFirstDistance() {
		return firstDistance;
	}

	public void setFirstDistance(Integer firstDistance) {
		this.firstDistance = firstDistance;
	}

	public String getFirstDestination() {
		return firstDestination;
	}

	public void setFirstDestination(String firstDestination) {
		this.firstDestination = firstDestination;
	}

	public Integer getSecondTime() {
		return secondTime;
	}

	public void setSecondTime(Integer secondTime) {
		this.secondTime = secondTime;
	}

	public Integer getSecondDistance() {
		return secondDistance;
	}

	public void setSecondDistance(Integer secondDistance) {
		this.secondDistance = secondDistance;
	}

	public String getSecondDestination() {
		return secondDestination;
	}

	public void setSecondDestination(String secondDestination) {
		this.secondDestination = secondDestination;
	}

	public Integer getStopId() {
		return stopId;
	}

	public void setStopId(Integer stopId) {
		this.stopId = stopId;
	}

	public String getBusLine() {
		return busLine;
	}

	public void setBusLine(String busLine) {
		this.busLine = busLine;
	}

	public GregorianCalendar getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(GregorianCalendar lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

	@Override
	public int compareTo(TUSEstimationResponse o) {
		return this.getFirstTime().compareTo(o.getFirstTime());
	}
}
