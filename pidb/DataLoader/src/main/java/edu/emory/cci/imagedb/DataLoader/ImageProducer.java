package edu.emory.cci.imagedb.DataLoader;

import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import edu.emory.cci.imagedb.FileDataCollector.ImageFileData;
import edu.emory.cci.imagedb.FileDataCollector.ImageFormatNotDefinedException;
import edu.emory.cci.imagedb.FileDataCollector.TileFileData;
import edu.emory.cci.imagedb.ModelBeans.Dataset;
import edu.emory.cci.imagedb.ModelBeans.Image;
import edu.emory.cci.imagedb.ModelBeans.Instrument;
import edu.emory.cci.imagedb.ModelBeans.Patient;
import edu.emory.cci.imagedb.ModelBeans.Specimen;
import edu.emory.cci.imagedb.ModelBeans.Tileset;
import edu.emory.cci.imagedb.dao.ImageDAO;

public class ImageProducer implements Runnable, TileFolder {

	private ImageDBDataLoader dataLoader;
	private ImageFileData imageFileData;
	private ImageDAO imageDAO;
	
	public void setDataLoader(ImageDBDataLoader dataLoader) {
		this.dataLoader = dataLoader;
	}
	
	public void setImageDAO(ImageDAO imageDAO) {
		this.imageDAO = imageDAO;
	}
	
	
	@Override
	public void run() {	
		prepareImageQueue(dataLoader.inputFolder);
	}
	
	private void prepareImageQueue(File recursiveFolder) {
		FileFilter ndpiFileFilter = new FileNameExtensionFilter("ndpi", "ndpi");
		FileFilter svsFileFilter = new FileNameExtensionFilter("svs", "svs");
		for (File file: recursiveFolder.listFiles()) {
			if(file.isFile() && (ndpiFileFilter.accept(file) || svsFileFilter.accept(file))) {
				try {
					System.out.println("start inserting "+file);
					Image image1 = produceImage(file);
					System.out.println("finished inserting "+file);
					try {
						dataLoader.imagesQueue.put(image1);
					} catch (InterruptedException e) {
						e.printStackTrace();
						System.exit(1);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			else if (file.isDirectory())
				prepareImageQueue(file);
		}
	}
	
	private Image produceImage(File imageFile) {
		imageFileData = new ImageFileData(imageFile, dataLoader.paisIdConfiguration);
		Image result = new Image();
		result.setAcquisitiondate(imageFileData.getAcquisitionDate());
		//result.setImagereferenceUid(imageFileData.getImageReferenceUid());
		result.setFilesize(imageFileData.getFileSize());
		
		result.setName(imageFileData.getName());
		try {
			result.setFormat(imageFileData.getFormat());
		} catch(ImageFormatNotDefinedException e) {
			// TODO: log a warning
		}
		result.setResolution(imageFileData.getResolution());
		
		Instrument instrument = imageFileData.getInstrument();
		result.setInstrument(instrument);
		
		Patient patient = imageFileData.getPatient();
		// if(patient.getPatientid() != null)
		result.setPatient(patient);
			
		Specimen specimen = imageFileData.getSpecimen();
		if(specimen != null)
			result.setSpecimen(specimen);
		
		Dataset dataset = imageFileData.getDataset();
		dataset.getExperimentalstudies().add(imageFileData.getExperimentalstudy());
		result.setDataset(dataset);
		
		result.setLocation(imageFileData.getLocation(imageFile.getParentFile().getParentFile().getAbsolutePath()));
		result.setImagereferenceUid(imageFileData.getImageReferenceUid());
		
		if (ImageDBDataLoader.wtCheck==1) {
		  try {
		      result.setThumbnail(imageFileData.getThumbnail()); 

		  } catch (IOException e) {
			  System.out.println("IO Exception");
			  e.printStackTrace();
		  }
		}

		Dimension dimension = imageFileData.getFileDimension(imageFile);
		if(dimension!=null)
		{
		result.setWidth(dimension.width);
		result.setHeight(dimension.height);	
		}
		else
		{
			System.out.println("dimension gaining failed!");
		}
		
		imageDAO.persist(result);
		List<File> tilesetFolders = getTileFolders(imageFile);
		
		// System.out.println("TilsetFolders name: "+test.getAbsolutePath()+"\n");
		
		for(File tilesetFolder: tilesetFolders) {
			File tileFolder = getTileFolder(tilesetFolder, imageFile);
			if(tileFolder.isDirectory())
				addTileset(result, imageFile.getName(), tileFolder);
		}
		
		dataLoader.imagesProduced++;
		if(dataLoader.imagesProduced % 10 == 0)
			System.out.println(dataLoader.imagesProduced + " images processed.");
			
		return result;
	}	
	
	private void addTileset(Image image, String imageName, File tileFolder) {		
		Tileset tileSet = null;
		try {
			tileSet = new Tileset();
			File anyTile = null;
			for(File file: tileFolder.listFiles()) {
				if(file.getName().contains(imageName)) {
					anyTile = file;				
					break;
				}
			}
			if(anyTile == null) //add a filter here, whether it is an image format
				anyTile = tileFolder.listFiles()[0];
			
			TileFileData tempTileFileData = new TileFileData(anyTile, dataLoader.paisIdConfiguration);
			tileSet.setName(tempTileFileData.getTilesetName());
			tileSet.setImage(image);
			tileSet.setAbsolutePath(tileFolder.getAbsolutePath());
			image.getTileset().add(tileSet);
		} catch(Exception e) {
			System.out.println(imageName + " tileset Exception");
			e.printStackTrace();
			System.exit(1);		
		}
		
		if(tileSet != null)
			imageDAO.persist(tileSet);		
	
	}

	private String removeExtension(String file) {
		String result = "";
		String[] tokens = file.split("\\.");
		for(int i=0;i < tokens.length - 1;i++) {
			result += tokens[i];
			if(i < tokens.length - 2)
				result += ".";
		}
		
		return result;
	}
	
	@Override
	public File getTileFolder(File tilesetFolder, File imageFile) {
		String imageName = imageFile.getName();
		String imageNameWithoutExtension = null;
		if(imageFile.getParentFile().equals(dataLoader.inputFolder))
		{
			imageNameWithoutExtension = removeExtension(imageName);
		}
		else if(imageFile.getParentFile().getParentFile().equals(dataLoader.inputFolder))
		{
			imageNameWithoutExtension = imageName;
		}
		// System.out.println("imageName wihtoutExtension: "+imageNameWithoutExtension);
		
		String tileFolderNameSuffix = "";
		
		// if at least one folder complies to the convention, use only folders that comply to it
		for(File file: tilesetFolder.listFiles()) {
			if(file.isDirectory() && file.getName().endsWith("-tile")) {
				tileFolderNameSuffix = "-tile";
				break;
			}
		}
		
		File tileFolder = new File(tilesetFolder, imageNameWithoutExtension + tileFolderNameSuffix);
		// System.out.println("imageName wihtoutExtension + tileFolderNameSuffix "+imageNameWithoutExtension+tileFolderNameSuffix);
		return tileFolder;
	}	
	
	@Override
	public List<File> getTileFolders(File imageFile) {
		List<File> result = new ArrayList<File>();
		
		if (imageFile.getParentFile().equals(dataLoader.inputFolder))
		{
			try{
				result.addAll(Arrays.asList(dataLoader.inputFolder.listFiles()));
				Iterator<File> iterator = result.iterator();
				while(iterator.hasNext()) {
					File file = iterator.next();
					if(!file.isDirectory())
						iterator.remove();
				}
			}catch(NullPointerException e){
				System.out.println("NullPointer Exception");
				e.printStackTrace();
			}			
		}
		else if(imageFile.getParentFile().getParentFile().equals(dataLoader.inputFolder))
		{
			try{
				result.addAll(Arrays.asList(dataLoader.inputFolder.listFiles()));
				Iterator<File> iterator = result.iterator();
				while(iterator.hasNext()) {
					File file = iterator.next();
					if(!file.isDirectory()){
						iterator.remove();
					}
					else if(!file.getName().endsWith("tiles"))
					{
						iterator.remove();
					}
						
				}
			}catch(NullPointerException e){
				System.out.println("NullPointer Exception");
				e.printStackTrace();
			}
			
		}
		return result; //contains directories under inputfolder
	}
}
