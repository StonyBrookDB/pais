package edu.emory.cci.imagedb.dao;

import java.util.List;

import edu.emory.cci.imagedb.ModelBeans.Specimen;

public interface SpecimenDAO {
	
	public void persist(Specimen specimen);
	
	/**
	 * 
	 * @return: all the records from the table SPECIMEN
	 */
	public List<Specimen> loadAll();
	
	public void deleteAll();
	
	/**
	 * Delete from table SPECIMEN all the elements contained in the List specimens.
	 * @param specimens
	 */
	public void deleteAll(List<Specimen> specimens);
	
	public void saveOrUpdateAll(List<Specimen> specimens);
}
