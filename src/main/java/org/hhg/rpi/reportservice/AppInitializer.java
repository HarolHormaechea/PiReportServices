package org.hhg.rpi.reportservice;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

/**
 * This will initialize our configuration classes. This is the first
 * thing to get loaded. 
 * 
 * @author Harold Hormaechea
 *
 */
public class AppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

	@Override
	protected Class<?>[] getRootConfigClasses() {
		return new Class[] { AppConfig.class, JpaConfig.class};
	}

	@Override
	protected Class<?>[] getServletConfigClasses() {
		return  null;
	}

	@Override
	protected String[] getServletMappings() {
		return new String[] { "/" };
	}

}
