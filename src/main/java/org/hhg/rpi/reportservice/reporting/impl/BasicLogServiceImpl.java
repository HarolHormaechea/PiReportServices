package org.hhg.rpi.reportservice.reporting.impl;

import org.hhg.rpi.reportservice.reporting.ReportingSubServiceInterface;
import org.springframework.stereotype.Service;

@Service
public class BasicLogServiceImpl implements  ReportingSubServiceInterface{

	@Override
	public void debug(String message) {
		System.out.println(message);
	}

	@Override
	public void info(String message) {
		System.out.println(message);
	}

	@Override
	public void error(String message) {
		System.err.print(message);
	}

	@Override
	public void init() {
		// NO-OP
	}
	
	
	
}
