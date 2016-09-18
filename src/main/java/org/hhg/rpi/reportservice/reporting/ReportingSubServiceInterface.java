package org.hhg.rpi.reportservice.reporting;


public interface ReportingSubServiceInterface {
	public void debug(String message);
	public void info(String message);
	public void error(String message);
	public void init();
}
