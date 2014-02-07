package edu.emory.cci.imagedb.FileNameDataCollector;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.Date;
import java.util.Iterator;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.emory.cci.imagedb.FileDataCollector.ImageFileData;
import edu.emory.cci.imagedb.ModelBeans.Dataset;
import edu.emory.cci.imagedb.ModelBeans.Experimentalstudy;
import edu.emory.cci.imagedb.ModelBeans.Location;
import edu.emory.cci.imagedb.ModelBeans.Server;
import edu.emory.cci.imagedb.ModelBeans.Specimen;
import edu.emory.cci.imagedb.utils.BeansComparators;
import edu.emory.cci.pais.PAISIdentifierGenerator.DataGeneratorConfig;
import edu.emory.cci.pais.PAISIdentifierGenerator.PAISIdentifierGenerator;

public class ImageFileDataTest {

	//TODO: mavenTestResources shouldn't be necessary. Need to find out why
	//this test is not able to access the resources directly from
	//src/test/resources, like the HibernateDAOSpecimenTest.java test in 
	//the DAO project is able to do.
	static String mavenTestResources = "src"+File.separator+"test"+File.separator+"resources"+File.separator;
	
	static File paisIdGeneratorConfigEmoryTCGA;
	static File imageEmoryTCGA;
	static ImageFileData imageFileDataEmoryTCGA;
	
	static File paisIdGeneratorConfigValidationSet;
	static File imageValidationSet;
	static ImageFileData imageFileDataValidationSet;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		paisIdGeneratorConfigEmoryTCGA = new File(mavenTestResources+"paisIdGeneratorConfigEmoryTCGA.xml");
		assertTrue("Cannot read the config file for EmoryTCGA.", paisIdGeneratorConfigEmoryTCGA.canRead());
		imageEmoryTCGA = new File(mavenTestResources+"imageDataSets"+File.separator+"EmoryTCGA"+File.separator+"TCGA-14-0736-01R-01_2A.ndpi");
		assertTrue("Cannot read the image file for EmoryTCGA.", imageEmoryTCGA.canRead());
		imageFileDataEmoryTCGA = new ImageFileData(imageEmoryTCGA, paisIdGeneratorConfigEmoryTCGA);
		
		paisIdGeneratorConfigValidationSet = new File(mavenTestResources+"paisIdGeneratorConfigValidationSet.xml");
		imageValidationSet = new File(mavenTestResources+"imageDataSets"+File.separator+"ValidationSet"+File.separator+"astroll.1.ndpi");
		imageFileDataValidationSet = new ImageFileData(imageValidationSet, paisIdGeneratorConfigValidationSet);
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetId() {
		long id = imageFileDataEmoryTCGA.getImageReferenceId();
		assertTrue("Invalid id.", id > 0);
		id = imageFileDataValidationSet.getImageReferenceId();
		assertTrue("Invalid id.", id > 0);
	}
	
	@Test
	public void testGetFormat() {
		Long expectedFormat = new Long(1);
		Long format = imageFileDataEmoryTCGA.getFormat();
		assertTrue("Format found is not as it was expected. Found: '"+format+"'. Expected was: '"+expectedFormat+"'", expectedFormat.equals(format));
	}

	@Test
	public void testGetAcquisitionDate() {
		Date acquisitionDateTestEmoryTCGA = imageFileDataEmoryTCGA.getAcquisitionDate();
		Date expectedDateEmoryTCGA = new Date();
		System.out.println(acquisitionDateTestEmoryTCGA);
		fail("Not yet implemented");
	}

	@Test
	public void testGetFileSize() {
		Long testFileSize = imageFileDataEmoryTCGA.getFileSize();
		Long originalFileSize = imageEmoryTCGA.length();
		assertTrue("Original file size: '"+originalFileSize+"'. Expected was: '"+testFileSize+"'.", testFileSize.equals(originalFileSize));
	}

	@Test
	public void testGetResolution() {
		String EmoryTCGAResolution = imageFileDataEmoryTCGA.getResolution();
		assertTrue("Image resolution found: '"+EmoryTCGAResolution+"'. Expected: 40X", EmoryTCGAResolution.equals("40X"));
		String ValidationSetResolution = imageFileDataValidationSet.getResolution();
		assertTrue("Image resolution found: '"+ValidationSetResolution+"'. Expected: 40X", ValidationSetResolution.equals("40X"));
	}

	@Test
	public void testGetImageReferenceUid() {
		String uidEmoryTCGA = imageFileDataEmoryTCGA.getImageReferenceUid();
		assertTrue("getImageReferenceUid() returned null.", uidEmoryTCGA != null);
		assertTrue("getImageReferenceUid() returned an empty String.", uidEmoryTCGA.length() > 0);
		
		String uidValidationSet = imageFileDataValidationSet.getImageReferenceUid();
		assertTrue("getImageReferenceUid() returned null.", uidValidationSet != null);
		assertTrue("getImageReferenceUid() returned an empty String.", uidValidationSet.length() > 0);
	}
	
	@Test
	public void testGetSpecimen() {
		Specimen expectedSpecimen = new Specimen();
		expectedSpecimen.setType("tissue slide");
		expectedSpecimen.setStain("hematoxylin stain");
		PAISIdentifierGenerator paisIdentifierGenerator = new PAISIdentifierGenerator(new DataGeneratorConfig(paisIdGeneratorConfigEmoryTCGA));
		expectedSpecimen.setSpecimenUid(paisIdentifierGenerator.getSpecimenUid(imageEmoryTCGA.getName()));
		expectedSpecimen.setSliceId("01_2A");
		expectedSpecimen.setTissueId("01R");
		
		Specimen specimenFound = imageFileDataEmoryTCGA.getSpecimen();
		
		assertTrue("The specimen found is not as expected.", BeansComparators.specimenEquals(expectedSpecimen, specimenFound));
	}
	
	@Test
	public void testGetName() {
		String expectedName = "EmoryTCGA" + File.separator + "TCGA-14-0736-01R-01_2A.ndpi";
		String nameFound = imageFileDataEmoryTCGA.getName();
		System.out.println("testGetName:");
		System.out.println("Expected name: '"+expectedName+"'");
		System.out.println("Name found: '"+nameFound+"'\n");
		
		assertTrue("Name found not as expected. Found: '"+nameFound+"'. Expected: '"+expectedName+"'", expectedName.equals(nameFound));
	}
	
	@Test
	public void testGetLocation() {
		Location expectedLocation = new Location();
		expectedLocation.setFolder(imageEmoryTCGA.getParentFile().getParentFile().getAbsolutePath() + File.separator);
		System.out.println("testGetLocation:");
		System.out.println("Expected folder: '"+expectedLocation.getFolder()+"'");
		
		Server expectedServer = new Server();
		expectedServer.setName("Europa");
		expectedServer.setCapacity("");
		expectedServer.setHostname("europa.cci.emory.edu");
		expectedServer.setIpaddress("170.140.186.172");
		expectedServer.setPort(new Integer(50000));
		
		expectedLocation.setServer(expectedServer);
		
		Location locationFound = imageFileDataEmoryTCGA.getLocation("/data2/Images");
		System.out.println("Folder in location found: '"+locationFound.getFolder()+"'\n");
		assertTrue("Location found not as expected.", BeansComparators.locationEquals(expectedLocation, locationFound));
	}
	
	@Test
	public void testGetLocation2() {
		Location expectedLocation = new Location();
		expectedLocation.setFolder(imageEmoryTCGA.getParentFile().getParentFile().getAbsolutePath() + File.separator);
		System.out.println("testGetLocation:");
		System.out.println("Expected folder: '"+expectedLocation.getFolder()+"'");
		
		Server expectedServer = new Server();
		expectedServer.setName("Europa");
		expectedServer.setCapacity("");
		expectedServer.setHostname("europa.cci.emory.edu");
		expectedServer.setIpaddress("170.140.186.172");
		expectedServer.setPort(new Integer(50000));
		
		expectedLocation.setServer(expectedServer);
		
		Location locationFound = imageFileDataEmoryTCGA.getLocation("/data2/Images/");  // It ends with a /
		System.out.println("Folder in location found: '"+locationFound.getFolder()+"'\n");
		assertTrue("Location found not as expected.", BeansComparators.locationEquals(expectedLocation, locationFound));
	}
	
	@Test
	public void testGetThumbnail() {
		File testThumbnailPng = new File(mavenTestResources+"imageDataSets"+File.separator+"Image-Thumbnails"+File.separator+"bcrTCGA-FS"+File.separator+"TCGA-02-0002-01A-01-BS1-thumbnail.png");
		assertTrue("Cannot read testThumbnail.", testThumbnailPng.canRead());
		imageFileDataEmoryTCGA.setImageThumbnail(testThumbnailPng);
		File getThumbnailTestResultPng = new File("getThumbnailTestResult.png");
		FileOutputStream out = null;
		try {
			getThumbnailTestResultPng.createNewFile();
			assertTrue("Cannot write to result file: '"+getThumbnailTestResultPng.getAbsolutePath()+"'.", getThumbnailTestResultPng.canWrite());
			out = new FileOutputStream(getThumbnailTestResultPng);
			try {
				out.write(imageFileDataEmoryTCGA.getThumbnail().getBytes(0, (int) imageFileDataEmoryTCGA.getThumbnail().length()));
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			fail("File '"+getThumbnailTestResultPng.getAbsolutePath()+"' was not found.");
		} catch (IOException e) {
			e.printStackTrace();
			fail("Exception while trying to write to '"+getThumbnailTestResultPng.getAbsolutePath()+"': "+e.getMessage());
		} finally {
			try {
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
				fail("Failed to close the stream that was writing to '"+getThumbnailTestResultPng.getAbsolutePath()+"': "+e.getMessage());
			}
		}
		
		// Compare the result with the original
		String originalThumbnailPngMd5 = new String(createChecksum(testThumbnailPng));
		String getThumbnailTestResultPngMd5 = new String(createChecksum(getThumbnailTestResultPng));
		
		assertTrue("The original png Thumbnail differs from the result png thumbnail.", originalThumbnailPngMd5.equals(getThumbnailTestResultPngMd5));
	}
	
	@Test
	public void testExperimentalstudy() {
				
		Experimentalstudy estudy = new Experimentalstudy();
		estudy = imageFileDataEmoryTCGA.getExperimentalstudy();
		
		System.out.println("Experimenter user uid: " + estudy.getExperimenter().getUserUid());
		System.out.println("Experimenter login name: " + estudy.getExperimenter().getLoginname());
		System.out.println("Experimenter name: " + estudy.getExperimenter().getName());
		System.out.println("Project name: " + estudy.getProject().getName());
		System.out.println("Project uri: " + estudy.getProject().getUri());
		
		int numExperimentergroup = 0;
		numExperimentergroup = estudy.getExperimenter().getExperimentergroups().size();
		System.out.println("ExperimenterGroup size: " + Integer.toString(numExperimentergroup));
	}
	
	@Test
	public void testDataset() {
		
		Dataset dataset = imageFileDataEmoryTCGA.getDataset();
		dataset.getExperimentalstudies().add(imageFileDataEmoryTCGA.getExperimentalstudy());
		int numSize = 0;
		numSize = dataset.getExperimentalstudies().size();
		System.out.println("Number of dataset size: "+Integer.toString(numSize));
		
	}
	
	

	static byte[] createChecksum(File file) {
		byte[] buffer = new byte[1024];
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			fail("Unable to create MessageDigest with MD5: "+e.getMessage());
		}
		int numRead;
		InputStream fis = null;
		try {
			fis = new FileInputStream(file);
			do {
				numRead = fis.read(buffer);
				if (numRead > 0) {
					md.update(buffer, 0, numRead);
				}
			} while (numRead != -1);
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			fail("File '"+file.getAbsolutePath()+"' was not found.");
		} catch (IOException e) {
			e.printStackTrace();
			fail("Exception while trying to write to '"+file.getAbsolutePath()+"': "+e.getMessage());
		} finally {
			try {
				fis.close();
			} catch(IOException e) {
				e.printStackTrace();
				fail("Failed to close the stream that was writing to '"+file.getAbsolutePath()+"': "+e.getMessage());
			}
		}
		
		return md.digest();
	}
	
}
