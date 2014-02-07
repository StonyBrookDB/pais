package edu.emory.cci.imagedb.utils;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import edu.emory.cci.imagedb.dao.ImageDAO;


public class CleanImageDB {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ApplicationContext context = new FileSystemXmlApplicationContext(new String[] {"classpath:conf/spring/applicationContext.xml", "conf/dbConfiguration/EuropaNonPooledDataSource.xml"});
		ImageDAO imageDAO = (ImageDAO) context.getBean("hibernateImageDAO");
		imageDAO.deleteAll();
	}

}
