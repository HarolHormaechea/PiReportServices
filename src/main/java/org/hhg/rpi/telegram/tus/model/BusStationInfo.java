package org.hhg.rpi.telegram.tus.model;

public class BusStationInfo {
	private Integer stationNumber;
	private String stationName;
	private String stationAddress;
	private String encodedImage;
	
	public BusStationInfo(){
		super();
	}
	
	public BusStationInfo(Integer stationNumber, String stationName, String stationAddress) {
		this();
		this.stationNumber = stationNumber;
		this.stationName = stationName;
		this.stationAddress = stationAddress;
	}


	public BusStationInfo(Integer stationNumber, String stationName, String stationAddress, String encodedImage) {
		this(stationNumber, stationName, stationAddress);
		this.encodedImage = encodedImage;
	}




	public Integer getStationNumber() {
		return stationNumber;
	}

	public void setStationNumber(Integer stationNumber) {
		this.stationNumber = stationNumber;
	}

	public String getStationName() {
		return stationName;
	}

	public void setStationName(String stationName) {
		this.stationName = stationName;
	}

	public String getStationAddress() {
		return stationAddress;
	}

	public void setStationAddress(String stationAddress) {
		this.stationAddress = stationAddress;
	}

	public String getEncodedImage() {
		return encodedImage;
	}

	public void setEncodedImage(String encodedImage) {
		this.encodedImage = encodedImage;
	}

}
