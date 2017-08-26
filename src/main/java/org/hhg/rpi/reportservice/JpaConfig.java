package org.hhg.rpi.reportservice;

import java.util.Properties;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;


/**
 * Configuration class for persistence beans.
 * 
 * 
 * @author Harold Hormaechea
 *
 */
@Configuration
@EnableTransactionManagement
public class JpaConfig {
	@Value("${rpi.reporting.database.url}")
	private String databaseUrl;
	@Value("${rpi.reporting.database.user}")
	private String databaseUser;
	@Value("${rpi.reporting.database.password}")
	private String databasePassword;

	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
		LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
		em.setDataSource(dataSource());

		// Define la ubicación de los datos a persistir
		// como entidades.
		em.setPackagesToScan(new String[] { "org.hhg.rpi.reportservice.model" });

		JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
		em.setJpaVendorAdapter(vendorAdapter);
		em.setJpaProperties(additionalProperties());

		return em;
	}

	/**
	 * Datasource configuration bean.
	 * 
	 * @return
	 */
	@Bean
	public DataSource dataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName("com.mysql.jdbc.Driver");
		dataSource.setUrl(databaseUrl);
		dataSource.setUsername("rpistatusdb");
		dataSource.setPassword("rpistatusdb");
		return dataSource;
	}

	/**
	 * We want transactions, and we will have them.
	 * 
	 * @param emf
	 * @return
	 */
	@Bean
	public PlatformTransactionManager transactionManager(EntityManagerFactory emf) {
		JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory(emf);
		return transactionManager;
	}

	@Bean
	public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
		return new PersistenceExceptionTranslationPostProcessor();
	}

	/**
	 * Utility method to configure the custom properties which may be
	 * required for correct hibernate behaviour.
	 * 
	 * @return
	 */
	Properties additionalProperties() {
		Properties properties = new Properties();
		properties.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL5Dialect");
		
		//In hibernate 5.0, this property was defaulted to true. Which
		//for now is inconvenient until proper sequence generators
		//are created for this application.
		properties.setProperty("hibernate.id.new_generator_mappings", "false"); 
		return properties;
	}
}
