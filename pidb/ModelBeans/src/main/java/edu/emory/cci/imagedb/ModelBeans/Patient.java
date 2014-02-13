package edu.emory.cci.imagedb.ModelBeans;

// Generated Jul 6, 2011 7:35:37 PM by Hibernate Tools 3.4.0.CR1

import java.util.HashSet;
import java.util.Set;

/**
 * Patient generated by hbm2java
 */
public class Patient implements java.io.Serializable {

	private long id;
	private String name;
	private String sex;
	private String birthdate;
	private String ethnicgroup;
	private String patientid;
	private Set<Image> images = new HashSet<Image>(0);

	public Patient() {
	}

	public Patient(long id) {
		this.id = id;
	}

	public Patient(long id, String name, String sex, String birthdate,
			String ethnicgroup, String patientid, Set<Image> images) {
		this.id = id;
		this.name = name;
		this.sex = sex;
		this.birthdate = birthdate;
		this.ethnicgroup = ethnicgroup;
		this.patientid = patientid;
		this.images = images;
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

	public String getSex() {
		return this.sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getBirthdate() {
		return this.birthdate;
	}

	public void setBirthdate(String birthdate) {
		this.birthdate = birthdate;
	}

	public String getEthnicgroup() {
		return this.ethnicgroup;
	}

	public void setEthnicgroup(String ethnicgroup) {
		this.ethnicgroup = ethnicgroup;
	}

	public String getPatientid() {
		return this.patientid;
	}

	public void setPatientid(String patientid) {
		this.patientid = patientid;
	}

	public Set<Image> getImages() {
		return this.images;
	}

	public void setImages(Set<Image> images) {
		this.images = images;
	}

}