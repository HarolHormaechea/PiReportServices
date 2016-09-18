package org.hhg.rpi.reportservice.controllers;

import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.hhg.rpi.reportservice.model.ServiceStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Test controller to check if the REST interface 
 * and JPA implementation are working.
 * 
 * 
 * @author Harold Hormaechea
 *
 */
@RestController
@Transactional
public class TestRestController {
	
	@PersistenceContext
	private EntityManager entityManager;
	
	@RequestMapping("/")
	public String welcome() {
		ServiceStatus ss = new ServiceStatus();
		ss.setServiceName("service");
		ss.setUuid(UUID.randomUUID().toString());
		ss.setStatus("PEPE");
		entityManager.persist(ss);
		return "If this message is shown, congratulations! A new item was added to the database.";
	}
}