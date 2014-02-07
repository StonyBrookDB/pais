package edu.emory.cci.imagedb.ModelBeans;

// Generated Jul 6, 2011 7:35:37 PM by Hibernate Tools 3.4.0.CR1

import java.util.HashSet;
import java.util.Set;

/**
 * Location generated by hbm2java
 */
public class Location implements java.io.Serializable {

	private long id;
	private Server server;
	private String folder;
	private Set<Image> images = new HashSet<Image>(0);

	public Location() {
	}

	public Location(long id, Server server) {
		this.id = id;
		this.server = server;
	}

	public Location(long id, Server server, String folder, Set<Image> images) {
		this.id = id;
		this.server = server;
		this.folder = folder;
		this.images = images;
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Server getServer() {
		return this.server;
	}

	public void setServer(Server server) {
		this.server = server;
	}

	public String getFolder() {
		return this.folder;
	}

	public void setFolder(String folder) {
		this.folder = folder;
	}

	public Set<Image> getImages() {
		return this.images;
	}

	public void setImages(Set<Image> images) {
		this.images = images;
	}

}
