package edu.emory.cci.imagedb.ModelBeans;

// Generated Jul 6, 2011 7:35:37 PM by Hibernate Tools 3.4.0.CR1

import java.util.HashSet;
import java.util.Set;

/**
 * Tileset generated by hbm2java
 */
public class Tileset implements java.io.Serializable {

	private long id;
	private String name;
	private String absolutePath;
	private String type;
	private String description;
	private Image image;
	private Set<Tiledimage> tiledimages = new HashSet<Tiledimage>(0);

	public Tileset() {
	}

	public Tileset(long id) {
		this.id = id;
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

	public String getAbsolutePath() {
		return this.absolutePath;
	}

	public void setAbsolutePath(String absolutePath) {
		this.absolutePath = absolutePath;
	}
	
	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public Image getImage() {
		return this.image;
	}

	public void setImage(Image image) {
		this.image = image;
	}

	public Set<Tiledimage> getTiledimages() {
		return this.tiledimages;
	}

	public void setTiledimages(Set<Tiledimage> tiledimages) {
		this.tiledimages = tiledimages;
	}

}
