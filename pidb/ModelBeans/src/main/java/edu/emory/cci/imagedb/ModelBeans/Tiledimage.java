package edu.emory.cci.imagedb.ModelBeans;

// Generated Jul 6, 2011 7:35:37 PM by Hibernate Tools 3.4.0.CR1

import java.sql.Blob;
import java.util.Date;

/**
 * Tiledimage generated by hbm2java
 */

public class Tiledimage implements java.io.Serializable {

	private long id;
	private Tileset tileset;
	private String name;
	private Double x;
	private Double y;
	private Double height;
	private Long filesize;
	private Double width;
	private String resolution;
	private Date tiledtime;
	private Long format;
	private Character hierarchylevel;
	private Blob thumbnail;
	private String tilename;
	private Location location;

	public Tiledimage() {
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Tileset getTileset() {
		return this.tileset;
	}

	public void setTileset(Tileset tileset) {
		this.tileset = tileset;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Double getX() {
		return this.x;
	}

	public void setX(Double x) {
		this.x = x;
	}

	public Double getY() {
		return this.y;
	}

	public void setY(Double y) {
		this.y = y;
	}

	public Double getHeight() {
		return this.height;
	}

	public void setHeight(Double height) {
		this.height = height;
	}

	public Long getFilesize() {
		return this.filesize;
	}

	public void setFilesize(Long filesize) {
		this.filesize = filesize;
	}

	public Double getWidth() {
		return this.width;
	}

	public void setWidth(Double width) {
		this.width = width;
	}

	public String getResolution() {
		return this.resolution;
	}

	public void setResolution(String resolution) {
		this.resolution = resolution;
	}

	public Date getTiledtime() {
		return this.tiledtime;
	}

	public void setTiledtime(Date tiledtime) {
		this.tiledtime = tiledtime;
	}

	public Long getFormat() {
		return this.format;
	}

	public void setFormat(Long format) {
		this.format = format;
	}

	public Character getHierarchylevel() {
		return this.hierarchylevel;
	}

	public void setHierarchylevel(Character hierarchylevel) {
		this.hierarchylevel = hierarchylevel;
	}

	public Blob getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(Blob thumbnail) {
		this.thumbnail = thumbnail;
	}

	public String getTilename() {
		return this.tilename;
	}

	public void setTilename(String tilename) {
		this.tilename = tilename;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}
}
