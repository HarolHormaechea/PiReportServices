package org.hhg.rpi.telegram.tus.model;

import java.math.BigDecimal;
import java.util.GregorianCalendar;

import org.hhg.rpi.telegram.tus.utils.TUSDateDeserializer;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TUSStopItem implements Comparable<TUSStopItem> {

	// Station info
	@JsonProperty(value = "ayto:numero")
	private Integer stopId;
	@JsonProperty(value = "ayto:address1")
	private String address;
	@JsonProperty(value = "ayto:parada")
	private String stopName;

	// GEO Info
	@JsonProperty(value = "wgs84_pos:long")
	private BigDecimal wgs84Long;
	@JsonProperty(value = "wgs84_pos:lat")
	private BigDecimal wgs84Lat;
	@JsonProperty(value = "gn:coordX")
	private BigDecimal coordX;
	@JsonProperty(value = "gn:coordY")
	private BigDecimal coordY;

	@JsonProperty(value = "dc:modified")
	@JsonDeserialize(using = TUSDateDeserializer.class)
	private GregorianCalendar modified;

	public Integer getStopId() {
		return stopId;
	}

	public void setStopId(Integer stopId) {
		this.stopId = stopId;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getStopName() {
		return stopName;
	}

	public void setStopName(String stopName) {
		this.stopName = stopName;
	}

	public BigDecimal getWgs84Long() {
		return wgs84Long;
	}

	public void setWgs84Long(BigDecimal wgs84Long) {
		this.wgs84Long = wgs84Long;
	}

	public BigDecimal getWgs84Lat() {
		return wgs84Lat;
	}

	public void setWgs84Lat(BigDecimal wgs84Lat) {
		this.wgs84Lat = wgs84Lat;
	}

	public BigDecimal getCoordX() {
		return coordX;
	}

	public void setCoordX(BigDecimal coordX) {
		this.coordX = coordX;
	}

	public BigDecimal getCoordY() {
		return coordY;
	}

	public void setCoordY(BigDecimal coordY) {
		this.coordY = coordY;
	}

	public GregorianCalendar getModified() {
		return modified;
	}

	public void setModified(GregorianCalendar modified) {
		this.modified = modified;
	}

	@Override
	public int compareTo(TUSStopItem o) {
		return stopId.compareTo(o.stopId);
	}

}
