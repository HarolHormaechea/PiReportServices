package org.hhg.rpi.reportservice.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Entity used to persist the status of every
 * process managed by the application in the
 * database.
 * 
 * 
 * @author Harold Hormaechea
 *
 */
@Entity
@Table(name="services_status")
public class ServiceStatus {
	
	public enum STATUS{ACTIVE, INACTIVE}
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	
	@Column(name="service_name", length=100)
	private String serviceName;
	
	@Column(name="uuid", length=36)
	private String uuid;
	
	@Column(name="status", length=100)
	private String status;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getServiceName() {
		return serviceName;
	}
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	
}
