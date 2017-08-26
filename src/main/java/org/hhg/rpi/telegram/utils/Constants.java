package org.hhg.rpi.telegram.utils;

public final class Constants {
	private Constants(){
		throw new UnsupportedOperationException("Not instantiable.");
	}
	
	public static final String TELEGRAM_URL_PREFIX ="https://api.telegram.org/bot";
	public static final String TELEGRAM_GET_UPDATES_SUFFIX = "/getUpdates?offset={offset}";
	public static final String TELEGRAM_SEND_MESSAGE_SUFFIX = "/sendMessage?chat_id={chat_id}&text={text}";
	

	public static final String TUS_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
	public static final String TUS_QUERY_URL = "http://datos.santander.es/api/rest/datasets/control_flotas_estimaciones.json?items=100&query=ayto\\:paradaId:{idParada}";

	
	/**
	 * This represents the amount of minutes which can have passed since last bus line update before we drop it from the bus list.
	 * 
	 * If there has been a longer period between current time and last time/position update for a bus, we will drop it.
	 */
	public static final Long TUS_UPDATE_TOLERANCE = 130L;

}
