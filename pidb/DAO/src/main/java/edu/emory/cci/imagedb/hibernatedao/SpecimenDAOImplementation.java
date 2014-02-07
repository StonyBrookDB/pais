package edu.emory.cci.imagedb.hibernatedao;

import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.transaction.annotation.Transactional;

import edu.emory.cci.imagedb.ModelBeans.Specimen;
import edu.emory.cci.imagedb.dao.SpecimenDAO;

public class SpecimenDAOImplementation extends HibernateDaoSupport implements SpecimenDAO {

	@Override
	@Transactional(readOnly=false)
	public void persist(Specimen specimen) {
		getHibernateTemplate().persist(specimen);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Specimen> loadAll() {
		return (List<Specimen>) getHibernateTemplate().loadAll(Specimen.class);
	}
	
	@Override
	@Transactional(readOnly=false)
	public void deleteAll() {
		getHibernateTemplate().bulkUpdate("delete from Specimen");
	}
	
	/**
	 * The id's of the specimens must all be 0.
	 * @param specimens
	 */
	@Override
	@Transactional(readOnly=false)
	public void saveOrUpdateAll(List<Specimen> specimens) {
		getHibernateTemplate().saveOrUpdateAll(specimens);
	}

	@Override
	@Transactional(readOnly=false)
	public void deleteAll(List<Specimen> specimens) {
		getHibernateTemplate().deleteAll(specimens);
	}
}
