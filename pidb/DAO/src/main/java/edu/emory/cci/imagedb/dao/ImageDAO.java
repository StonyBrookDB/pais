package edu.emory.cci.imagedb.dao;

import java.util.List;

import edu.emory.cci.imagedb.ModelBeans.Image;
import edu.emory.cci.imagedb.ModelBeans.Tileset;
import edu.emory.cci.imagedb.ModelBeans.Tiledimage;

public interface ImageDAO {
	
	public void persist(Image image);
	public void persist(Tileset tileset);
	public void persist(Tiledimage ti);
	/**
	 * 
	 * @return: all the records from the table IMAGE
	 */
	public List<Image> loadAll();
	
	public void deleteAll();
	
	/**
	 * Delete from table IMAGE all the elements contained in the List images.
	 * @param images
	 */
	public void deleteAll(List<Image> images);
	
	public void saveOrUpdate(Image image);
	
	public void saveOrUpdateAll(List<Image> images);
	
	public List<Image> find(String imageName);
}
