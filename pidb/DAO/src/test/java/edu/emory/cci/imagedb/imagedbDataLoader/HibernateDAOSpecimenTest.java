package edu.emory.cci.imagedb.imagedbDataLoader;


import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import edu.emory.cci.imagedb.ModelBeans.Specimen;
import edu.emory.cci.imagedb.dao.SpecimenDAO;

public class HibernateDAOSpecimenTest {

	static SpecimenDAO specimenDAO;
	
	List<Specimen> backup;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		File dir = new File("");
		String currentDir = dir.getAbsolutePath() + File.separator + "applicationContext.xml";
		// System.out.println(currentDir);
		
		ApplicationContext context = new ClassPathXmlApplicationContext(currentDir);
		specimenDAO = (SpecimenDAO) context.getBean("hibernateSpecimenDAO");
		
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		backupSpecimenTable();
		specimenDAO.deleteAll();
	}

	@After
	public void tearDown() throws Exception {
		restoreSpecimenTable();
	}

	@Test
	public void testInsertSpeciment() {
		Specimen specimen1 = new Specimen();
        specimen1.setType("Specimen1 type");
        specimen1.setStain("Specimen1 stain");
        specimen1.setSpecimenUid("Specimen1UID");
        
        Specimen specimen2 = new Specimen();
        specimen2.setType("Specimen2 type");
        specimen2.setStain("Specimen2 stain");
        specimen2.setSpecimenUid("Specimen2UID");
        
        specimenDAO.persist(specimen1);
        specimenDAO.persist(specimen2);
        
		// Check that the specimens are in the table and that the amount of
		// returned records is 2
        List<Specimen> test = specimenDAO.loadAll();
        assertTrue("Unexpected amount of records in table SPECIMEN. Expected was 2, found: "+test.size()+".", test.size() == 2);
        /*
        equals(Object o) must be implemented in Specimen.java 
         
        assertTrue("specimen1 not found in table SPECIMEN", test.contains(specimen1));
        assertTrue("specimen2 not found in table SPECIMEN", test.contains(specimen2));
        */
	}
	
	void backupSpecimenTable() {
		backup = new ArrayList<Specimen>();
		backup.addAll(specimenDAO.loadAll());
	}
	
	void restoreSpecimenTable() {
		specimenDAO.deleteAll();
		setIDsTo0(backup);
		specimenDAO.saveOrUpdateAll(backup);
	}
	
	void setIDsTo0(List<Specimen> specimens) {
		for(Specimen s: specimens) {
			s.setId(0);
		}
	}
}
