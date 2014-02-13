package edu.emory.cci.imagedb.ModelBeans;

// Generated Jul 6, 2011 7:35:37 PM by Hibernate Tools 3.4.0.CR1

import java.util.HashSet;
import java.util.Set;

/**
 * Server generated by hbm2java
 */
public class Server implements java.io.Serializable {

	private long id;
	private String name;
	private String capacity;
	private String hostname;
	private String ipaddress;
	private Integer port;
	private Set<Location> locations = new HashSet<Location>(0);

	public Server() {
	}

	public Server(long id) {
		this.id = id;
	}

	public Server(long id, String name, String capacity, String hostname,
			String ipaddress, Integer port, Set<Location> locations) {
		this.id = id;
		this.name = name;
		this.capacity = capacity;
		this.hostname = hostname;
		this.ipaddress = ipaddress;
		this.port = port;
		this.locations = locations;
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCapacity() {
		return this.capacity;
	}

	public void setCapacity(String capacity) {
		this.capacity = capacity;
	}

	public String getHostname() {
		return this.hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	public String getIpaddress() {
		return this.ipaddress;
	}

	public void setIpaddress(String ipaddress) {
		this.ipaddress = ipaddress;
	}

	public Integer getPort() {
		return this.port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

	public Set<Location> getLocations() {
		return this.locations;
	}

	public void setLocations(Set<Location> locations) {
		this.locations = locations;
	}

}