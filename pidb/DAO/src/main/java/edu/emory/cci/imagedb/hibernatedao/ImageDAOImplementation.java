package edu.emory.cci.imagedb.hibernatedao;

import java.util.List;
import java.util.HashSet;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.transaction.annotation.Transactional;
//import org.springframework.dao.DataIntegrityViolationException;
//import org.hibernate.exception.ConstraintViolationException;
//import org.hibernate.util.JDBCExceptionReporter;
import org.hibernate.*;

import edu.emory.cci.imagedb.ModelBeans.Image;
import edu.emory.cci.imagedb.ModelBeans.Tileset;
import edu.emory.cci.imagedb.ModelBeans.Tiledimage;
import edu.emory.cci.imagedb.ModelBeans.Experimentalstudy;

import edu.emory.cci.imagedb.dao.ImageDAO;


public class ImageDAOImplementation extends HibernateDaoSupport implements ImageDAO {

	@Override
	@Transactional(rollbackFor={Exception.class})
	public void persist(Image image) {
		try {
			getHibernateTemplate().persist(image);
		} catch (RuntimeException e){
			String SQLstr1 = "SELECT ID AS IMAGEID FROM PI.IMAGE WHERE NAME='" + image.getName() + "'"; 
			Session ses = getSession(false);
			ses.clear();
			Object result = ses.createSQLQuery(SQLstr1).addScalar("imageid", Hibernate.LONG).uniqueResult();
			image.setId(((Long)result).longValue());
			ses.clear();
			image.setActivitystatuses(null);
			image.setTileset(null);
			image.getLocation().setImages(null);
			image.getLocation().getServer().setLocations(null);
			image.getInstrument().setImages(null);
			image.getLocation().setImages(null);
			for (Experimentalstudy es: image.getDataset().getExperimentalstudies()) {
				es.setExperiments(null);
				es.getProject().setExperimentalstudies(null);
			}
			image.getDataset().setExperimentalstudies(image.getDataset().getExperimentalstudies());
			image.getDataset().setImages(null);
			image.getPatient().setImages(null);
			image.getSpecimen().setImages(null);
			
			ses.update(image);
			image.setTileset(new HashSet<Tileset>(0));
		} 
	}
	
	@Override
	@Transactional(rollbackFor={Exception.class})
	public void persist(Tileset tileSet) {
		try {
			getHibernateTemplate().persist(tileSet);
		} catch (RuntimeException e) {
			String SQLstr2 = "SELECT ID AS TILESETID FROM PI.TILESET WHERE NAME='" + tileSet.getName() + "'";
			Session ses = getSession(false);
			ses.clear();
			Object result = ses.createSQLQuery(SQLstr2).addScalar("tilesetid", Hibernate.LONG).uniqueResult();
			tileSet.setId(((Long)result).longValue());
			ses.clear();
			tileSet.setTiledimages(null);
			
			ses.update(tileSet);
		} 
	}
	
	@Override
	@Transactional(rollbackFor={Exception.class})
	public void persist(Tiledimage td) {
		try {
			getHibernateTemplate().persist(td);
		} catch (RuntimeException e) {
			String SQLstr3 = "SELECT ID AS TILEID FROM PI.TILEDIMAGE WHERE NAME='" + td.getName() + "'";
			Session ses = getSession(false);
			ses.clear();
			Object result = ses.createSQLQuery(SQLstr3).addScalar("tileid", Hibernate.LONG).uniqueResult();
			td.setId(((Long)result).longValue());
			ses.clear();
			
			ses.update(td);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Image> loadAll() {
		return (List<Image>) getHibernateTemplate().loadAll(Image.class);
	}
	
	/**
	 * The id's of the images must all be 0.
	 * @param images
	 */
	@Override
	@Transactional(readOnly=false)
	public void saveOrUpdate(final Image image) {
			getHibernateTemplate().saveOrUpdate(image);
	}
	

	
	@Override
	@Transactional(readOnly=false)
	public void deleteAll() {
		getHibernateTemplate().deleteAll(getHibernateTemplate().loadAll(Image.class));
	}
	
	@Override
	@Transactional(readOnly=false)
	public void deleteAll(List<Image> imagesToDelete) {
		getHibernateTemplate().deleteAll(imagesToDelete);
	}

	@Override
	public void saveOrUpdateAll(List<Image> images) {
		for(Image image: images)
			saveOrUpdate(image);
	}
	
	@Override
	@Transactional(readOnly=false)
	public List<Image> find(String imageName) {
		String queryString = "from Image image where image.name = ?";
		@SuppressWarnings("unchecked")
		List <Image> image = getHibernateTemplate().find(queryString,imageName);
		return image;
	}

	
}
