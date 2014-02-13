package edu.emory.cci.imagedb.ModelBeans;

// Generated Jul 6, 2011 7:35:37 PM by Hibernate Tools 3.4.0.CR1

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Experimentalstudy generated by hbm2java
 */
public class Experimentalstudy implements java.io.Serializable {

	private long id;
	private Project project;
	private Dataset dataset;
	private Experimenter experimenter;
	private Date activedaterange;
	private String description;
	private String designtype;
	private String name;
	private String typecode;
	private Set<Experiment> experiments = new HashSet<Experiment>(0);

	public Experimentalstudy() {
	}

	public Experimentalstudy(long id) {
		this.id = id;
	}

	public Experimentalstudy(long id, Project project, Dataset dataset,
			Experimenter experimenter, Date activedaterange,
			String description, String designtype, String name,
			String typecode, Set<Experiment> experiments) {
		this.id = id;
		this.project = project;
		this.dataset = dataset;
		this.experimenter = experimenter;
		this.activedaterange = activedaterange;
		this.description = description;
		this.designtype = designtype;
		this.name = name;
		this.typecode = typecode;
		this.experiments = experiments;
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Project getProject() {
		return this.project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public Dataset getDataset() {
		return this.dataset;
	}

	public void setDataset(Dataset dataset) {
		this.dataset = dataset;
	}

	public Experimenter getExperimenter() {
		return this.experimenter;
	}

	public void setExperimenter(Experimenter experimenter) {
		this.experimenter = experimenter;
	}

	public Date getActivedaterange() {
		return this.activedaterange;
	}

	public void setActivedaterange(Date activedaterange) {
		this.activedaterange = activedaterange;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDesigntype() {
		return this.designtype;
	}

	public void setDesigntype(String designtype) {
		this.designtype = designtype;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTypecode() {
		return this.typecode;
	}

	public void setTypecode(String typecode) {
		this.typecode = typecode;
	}

	public Set<Experiment> getExperiments() {
		return this.experiments;
	}

	public void setExperiments(Set<Experiment> experiments) {
		this.experiments = experiments;
	}

}