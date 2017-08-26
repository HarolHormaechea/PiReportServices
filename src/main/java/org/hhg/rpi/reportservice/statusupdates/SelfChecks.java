package org.hhg.rpi.reportservice.statusupdates;

import org.hhg.rpi.reportservice.reporting.LogServiceInterface;
import org.hhg.rpi.reportservice.utils.Constants;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * Tasked to report on the application status itself.
 * 
 * @author Harold Hormaechea
 *
 */
@Component
public class SelfChecks implements ApplicationListener<ApplicationEvent>, ApplicationContextAware {

	private ApplicationContext appContext;

	@Autowired
	private LogServiceInterface logService;

	public SelfChecks() {
	}

	@Override
	public void onApplicationEvent(ApplicationEvent applicationEvent) {
		// We only care about notifications coming from the root
		// application context.
		if (applicationEvent.getSource() != appContext)
			return;
		
		if (applicationEvent instanceof ContextRefreshedEvent) {
			logService.info(Constants.SOCIAL_STARTUP_MESSAGE);
		} else if (applicationEvent instanceof ContextClosedEvent) {
			logService.info(Constants.SOCIAL_SHUTDOWN_MESSAGE);
		}
	}

	@Override
	public void setApplicationContext(ApplicationContext appContext) throws BeansException {
		this.appContext = appContext;
	}

}
