package edu.emory.cci.imagedb.FileNameDataCollector;

import static org.junit.Assert.assertTrue;

import javax.imageio.ImageIO;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.emory.cci.imagedb.FileDataCollector.TileFileData;

public class TileFileDataTest {

	
	//TODO: mavenTestResources shouldn't be necessary. Need to find out why
	//this test is not able to access the resources directly from
	//src/test/resources, like the HibernateDAOSpecimenTest.java test in 
	//the DAO project is able to do.
	static String mavenTestResources = "src"+File.separator+"test"+File.separator+"resources"+File.separator;
	
	static File paisIdGeneratorConfigEmoryTCGA;
	static File tileEmoryTCGA;
	static TileFileData tileFileDataEmoryTCGA;
	
	static File paisIdGeneratorConfigValidationSet;
	static File tileValidationSet;
	static TileFileData tileFileDataValidationSet;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		paisIdGeneratorConfigEmoryTCGA = new File(mavenTestResources+"paisIdGeneratorConfigEmoryTCGA.xml");
		assertTrue("Cannot read the config file for EmoryTCGA.", paisIdGeneratorConfigEmoryTCGA.canRead());
		tileEmoryTCGA = new File(mavenTestResources+"imageDataSets"+File.separator+"EmoryTCGA"+File.separator+"20Xtiles"+File.separator+"TCGA-14-0736-01R-01_2A-tile"+File.separator+"TCGA-14-0736-01R-01_2A.ndpi-0000000000-0000008192.tif");
		assertTrue("Cannot read the tile file for EmoryTCGA.", tileEmoryTCGA.canRead());
		tileFileDataEmoryTCGA = new TileFileData(tileEmoryTCGA, paisIdGeneratorConfigEmoryTCGA);
		
		paisIdGeneratorConfigValidationSet = new File(mavenTestResources+"paisIdGeneratorConfigValidationSet.xml");
		tileValidationSet = new File(mavenTestResources+"imageDataSets"+File.separator+"ValidationSet"+File.separator+"20X_4096x4096_tiles"+File.separator+"astroII.1"+File.separator+"astroII.1.ndpi-0000053248-0000012288.tif");
		tileFileDataValidationSet = new TileFileData(tileValidationSet, paisIdGeneratorConfigValidationSet);
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
	public void testGetName() {
		String expectedName = "EmoryTCGA"+File.separator+"20Xtiles"+File.separator+"TCGA-14-0736-01R-01_2A-tile"+File.separator+"TCGA-14-0736-01R-01_2A.ndpi-0000000000-0000008192.tif";
		String nameFound = tileFileDataEmoryTCGA.getName("/data2/Images/EmoryTCGA");
		System.out.println("testGetName:");
		System.out.println("Expected name: '"+expectedName+"'");
		System.out.println("Name found: '"+nameFound+"'\n");
		
		assertTrue("Name found not as expected. Found: '"+nameFound+"'. Expected: '"+expectedName+"'", expectedName.equals(nameFound));
	}
	
	@Test
	public void testGetName2() {
		String expectedName = "EmoryTCGA"+File.separator+"20Xtiles"+File.separator+"TCGA-14-0736-01R-01_2A-tile"+File.separator+"TCGA-14-0736-01R-01_2A.ndpi-0000000000-0000008192.tif";
		String nameFound = tileFileDataEmoryTCGA.getName("/data2/Images/EmoryTCGA/");   // It includes a / at the end.
		System.out.println("testGetName:");
		System.out.println("Expected name: '"+expectedName+"'");
		System.out.println("Name found: '"+nameFound+"'\n");
		
		assertTrue("Name found not as expected. Found: '"+nameFound+"'. Expected: '"+expectedName+"'", expectedName.equals(nameFound));
	}

	@Test
	public void testGetFormat() {
		Long expectedFormat = new Long(2);
		Long format = tileFileDataEmoryTCGA.getFormat();
		assertTrue("Format found is not as it was expected. Found: '"+format+"'. Expected was: '"+expectedFormat+"'", expectedFormat.equals(format));
	}

	@Test
	public void testGetWidth() {
		
		String[] formatNames = ImageIO.getReaderFormatNames();
		for (int i=0; i<formatNames.length; i++){
			System.out.print("\t" + formatNames[i]+"\t");
		}
		
		Double expectedWidth = new Double(4096);
		Double width = tileFileDataEmoryTCGA.getWidth();
		String format = Double.toString(width);
		String expectedFormat = Double.toString(expectedWidth);
		System.out.println("testGetWidth:");
		System.out.println("Expected width found: '"+format);
		System.out.println("Actual width value from TileImage found: '"+expectedFormat+"'\n");
		assertTrue("Format found is not as it was expected. Found: '"+format+"'. Expected was: '"+expectedFormat+"'", expectedFormat.equals(format));
	}
	
	@Test	
	public void testGetHeight() {
		
		Double expectedHeight = new Double(4096);
		Double height = tileFileDataEmoryTCGA.getHeight();
		String format = Double.toString(height);
		String expectedFormat = Double.toString(expectedHeight);
		System.out.println("testGetHeight:");
		System.out.println("Expected height found: '"+format);
		System.out.println("Real height found: '"+expectedFormat+"'\n");
		assertTrue("Format found is not as it was expected. Found: '"+format+"'. Expected was: '"+expectedFormat+"'", expectedFormat.equals(format));
	}
/*
	@Test	
	public void testGetTileThumbnail() {
		
		long expectedImageSize = 335454;
		long ImageSize = tileFileDataEmoryTCGA.getTileThumbnail().length();
		
		String format = Long.toString(ImageSize);
		String expectedFormat = Long.toString(expectedImageSize);
		
		System.out.println("testGetTileThumbnail:");
		System.out.println("Expected Image Size found: '"+format);
		System.out.println("Actual Image Size Value found: '"+expectedFormat+"'\n");
		assertTrue("Format found is not as it was expected. Found: '"+format+"'. Expected was: '"+expectedFormat+"'", expectedFormat.equals(format));
	}
	
*/	
	@Test
	public void testGetResolution() {
		
		String expectedResolution = "20X";
		String resolutionFound = tileFileDataEmoryTCGA.getResolution();
		System.out.println("testGetResolution:");
		System.out.println("Expected resolution: '"+expectedResolution+"'");
		System.out.println("Resolution found: '"+resolutionFound+"'\n");
		
		assertTrue("Resolution found not as expected. Found: '"+resolutionFound+"'. Expected: '"+expectedResolution+"'", expectedResolution.equals(resolutionFound));
	}
	
	@Test
	public void testGetResolution1() {
		File tileEmoryTCGAGetResolution1;
		TileFileData tileFileDataEmoryTCGAGetResolution1;
		
		// test 1, 5.5, 20, 20.5, 20.25		
		String expectedResolution[] = {"1X", "5.5X", "20X", "20.5X", "20.25X"};
		int index = 4;
		tileEmoryTCGAGetResolution1 = new File(mavenTestResources+"imageDataSets"+File.separator+"EmoryTCGA"+File.separator+"test1"+File.separator+expectedResolution[index]+"tiles"+File.separator+"TCGA-14-0736-01R-01_2A-tile"+File.separator+"TCGA-14-0736-01R-01_2A.ndpi-0000000000-0000008192.tif");
		tileFileDataEmoryTCGAGetResolution1 = new TileFileData(tileEmoryTCGAGetResolution1, paisIdGeneratorConfigEmoryTCGA);
		
		
		String resolutionFound = tileFileDataEmoryTCGAGetResolution1.getResolution();
		System.out.println("testGetResolution1:");
		System.out.println("Expected resolution: '"+expectedResolution[index]+"'");
		System.out.println("Resolution found: '"+resolutionFound+"'\n");
		
		assertTrue("Resolution found not as expected. Found: '"+resolutionFound+"'. Expected: '"+expectedResolution[index]+"'", expectedResolution[index].equals(resolutionFound));
	}
	
	@Test
	public void testGetResolution2() {
		File tileEmoryTCGAGetResolution2;
		TileFileData tileFileDataEmoryTCGAGetResolution2;
		
		// test 1, 5.5, 20, 20.5, 20.25		
		String expectedResolution[] = {"1X", "5.5X", "20X", "20.5X", "20.25X"};
		int index = 2;
		tileEmoryTCGAGetResolution2 = new File(mavenTestResources+"imageDataSets"+File.separator+"EmoryTCGA"+File.separator+"test2"+File.separator+"tile"+File.separator+"TCGA-14-0736-01R-01_2A-tile"+File.separator+"TCGA-14-0736-01R-01_2A.ndpi-0000000000-0000008192.tif");
		tileFileDataEmoryTCGAGetResolution2 = new TileFileData(tileEmoryTCGAGetResolution2, paisIdGeneratorConfigEmoryTCGA);
		
		String resolutionFound = tileFileDataEmoryTCGAGetResolution2.getResolution();
		System.out.println("testGetResolution2:");
		System.out.println("Expected resolution: '"+expectedResolution[index]+"'");
		System.out.println("Resolution found: '"+resolutionFound+"'\n");
		
		assertTrue("Resolution found not as expected. Found: '"+resolutionFound+"'. Expected: '"+expectedResolution[index]+"'", expectedResolution[index].equals(resolutionFound));
	}
	
	@Test
	public void testGetHierarchylevel() {
		Integer expectedHierarchylevel = new Integer(1);
		Map<String, Integer> resolutionToHierarchylevelMappings = new HashMap<String,Integer>();
		resolutionToHierarchylevelMappings.put("40X", new Integer(0));
		resolutionToHierarchylevelMappings.put("20X", new Integer(1));
		resolutionToHierarchylevelMappings.put("10X", new Integer(2));
		resolutionToHierarchylevelMappings.put("5.5X", new Integer(3));
		resolutionToHierarchylevelMappings.put("1X", new Integer(4));
		tileFileDataEmoryTCGA.setResolutionToHirarchylevelMappings(resolutionToHierarchylevelMappings);
		Integer hierarchylevelFound = tileFileDataEmoryTCGA.getHierarchylevel();
		
		assertTrue("Hierarchy level not as expected. Found: '"+hierarchylevelFound+"'. Expected was: '"+expectedHierarchylevel+"'. Resolution of the tile: '"+tileFileDataEmoryTCGA.getResolution()+"'", expectedHierarchylevel.equals(hierarchylevelFound));
	}
	
	@Test
	public void testGetTilesetName() {
		String expectedTilesetName = "TCGA-14-0736-01R-01_2A";
		String tilesetNameFound = tileFileDataEmoryTCGA.getTilesetName();
		
		assertTrue("Tileset name is not as expected. Found: '"+tilesetNameFound+"'. Expected was: '"+expectedTilesetName+"'", expectedTilesetName.equals(tilesetNameFound));
	}
	
}
