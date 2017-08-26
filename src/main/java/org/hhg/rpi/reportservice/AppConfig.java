package org.hhg.rpi.reportservice;

import org.hhg.rpi.reportservice.reporting.impl.TwitterLogServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

/**
 * Main configuration class for the application, will kickstart initial services
 * and beans.
 * 
 * TODO: Security! Whenever any actual management service is implemented, of course.
 * 
 * @author Harold Hormaechea
 *
 */
// This tells Spring where to look for components. It will
// also scan subpackages.
@ComponentScan(basePackages = "org.hhg.rpi")
// Will allow Spring to link requests to controllers.
@EnableWebMvc
@EnableScheduling
@Configuration
//"reportservice_config" is an environment variable (a.k.a. path variable)
//set to the folder containing the application.properties file. You have
//an example of this file in the main project folder.
@PropertySource(value = "file:${reportservice_config}/application.properties")
public class AppConfig {

	@Bean
	public InternalResourceViewResolver viewResolver() {
		InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
		viewResolver.setPrefix("/WEB-INF/views/");
		viewResolver.setSuffix(".jsp");

		return viewResolver;
	}

	@Bean
	public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
		return new PropertySourcesPlaceholderConfigurer();
	}
	
	@Bean
	public static TwitterLogServiceImpl twitterLogServiceImpl(){
		return new TwitterLogServiceImpl();
	}
	

	
}