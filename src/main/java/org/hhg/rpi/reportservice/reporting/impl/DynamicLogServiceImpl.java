package org.hhg.rpi.reportservice.reporting.impl;

import java.util.ArrayList;

import javax.annotation.PostConstruct;

import org.hhg.rpi.reportservice.reporting.LogServiceInterface;
import org.hhg.rpi.reportservice.reporting.ReportingSubServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Will handle log handling across different services, like
 * twitter, local logging, etc from here.
 * 
 * This is the only logging service which should be autowired
 * by other application classes.
 * 
 * 
 * @author Harold Hormaechea
 *
 */
@Service
public class DynamicLogServiceImpl implements LogServiceInterface {
	
	private ArrayList<ReportingSubServiceInterface> servicesList = new ArrayList<ReportingSubServiceInterface>();
	


	/**
	 * TODO: Include other stuff, like file loggers, and smoke
	 * signalers.
	 * 
	 * @param twitter
	 */
	@Autowired
	public DynamicLogServiceImpl(TwitterLogServiceImpl twitter) {
		if(twitter != null){
			servicesList.add(twitter);
		}
			
	}
	
	/**
	 * Will initialize each service after everything
	 * is wired.
	 */
	@PostConstruct
	public void initializeServices(){
		for(ReportingSubServiceInterface rService : servicesList){
			if(rService != null)
				rService.init();
		}
	}

	@Override
	public void debug(String message) {
		for(ReportingSubServiceInterface rService : servicesList)
			rService.debug(message);
	}

	@Override
	public void info(String message) {
		for(ReportingSubServiceInterface rService : servicesList)
			rService.info(message);
	}

	@Override
	public void error(String message) {
		for(ReportingSubServiceInterface rService : servicesList)
			rService.info(message);
	}
}
