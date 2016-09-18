package org.hhg.rpi.reportservice.utils;

import java.text.SimpleDateFormat;

public abstract class Constants {
	
	public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("HH:mm:ss");
	public static final String SOCIAL_INFO_PREFIX="Info";
	public static final String SOCIAL_DEBUG_PREFIX="Debug";
	public static final String SOCIAL_ERROR_PREFIX="Error";
	public static final String SOCIAL_STARTUP_MESSAGE="Here we are! Service is up!";
	public static final String SOCIAL_SHUTDOWN_MESSAGE="Time to sleep. Goodbye!";
}
