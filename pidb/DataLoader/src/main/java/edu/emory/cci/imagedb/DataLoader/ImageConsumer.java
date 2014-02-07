package edu.emory.cci.imagedb.DataLoader;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import edu.emory.cci.imagedb.ModelBeans.Image;
import edu.emory.cci.imagedb.dao.ImageDAO;
import edu.emory.cci.imagedb.ModelBeans.Tileset;
import edu.emory.cci.imagedb.ModelBeans.Tiledimage;
import edu.emory.cci.imagedb.FileDataCollector.TileFileData;
import edu.emory.cci.pais.PAISIdentifierGenerator.UnableToGetRegionNameException;

public class ImageConsumer implements Runnable {

	private ImageDBDataLoader dataLoader;
	private ImageDAO imageDAO;
	
	public void setDataLoader(ImageDBDataLoader dataLoader) {
		this.dataLoader = dataLoader;
	}

	public void setImageDAO(ImageDAO imageDAO) {
		this.imageDAO = imageDAO;
	}

	@Override
	public void run() {
		while (true) {
			Image image = null;
			try {
				image = dataLoader.imagesQueue.poll(10, TimeUnit.SECONDS); //wait for 10 seconds, if no element retrieved, end of the queue;				
				if (image==null) 
					{
					System.out.println("I need to go!");
					   break;
					}
				
				String imageName = image.getName();
				for (Tileset ts: image.getTileset()) 
							{
					  produceTiles(ts, ts.getAbsolutePath(), imageName);  
					  
							}
				
				
				System.out.println("Successfuly inserted image: '"+imageName+"'");
				
				dataLoader.imagesInserted++;
				if(dataLoader.imagesInserted % 10 == 0)
				System.out.println(dataLoader.imagesInserted + " images inserted in the db.");
			} catch(Exception e) {
				e.printStackTrace();
				if(image != null)
					if(image.getName() != null)
						System.out.println("\nCould not insert image: '"+image.getName()+"'");
			}
		}
	}
	
	private void produceTiles (Tileset tileSet, String tileFolderPath, String imageName) {
		FileFilter tifFileFilter = new FileNameExtensionFilter("tif", "tif");
		FileFilter tiffFileFilter = new FileNameExtensionFilter("tiff", "tiff");
		FileFilter pngFileFilter = new FileNameExtensionFilter("png", "png");
		FileFilter jpgFileFilter = new FileNameExtensionFilter("jpg", "jpg");
		File tileFolder = new File(tileFolderPath);
		
		for(File tileFile: tileFolder.listFiles()) {
			if(!tifFileFilter.accept(tileFile) && !tiffFileFilter.accept(tileFile)
					&& !pngFileFilter.accept(tileFile) && !jpgFileFilter.accept(tileFile)) {
				continue;
			}
			if(tileFile.isDirectory()) {
			  continue;
			}
			TileFileData tileFileData = new TileFileData(tileFile, dataLoader.paisIdConfiguration);
			Tiledimage tile = new Tiledimage();

			//try {
			tile.setFormat(tileFileData.getFormat());
			//} catch(ImageFormatNotDefinedException e) {
				//TODO: log a warning
			//}

			tile.setName(tileFileData.getName(dataLoader.inputFolder.getAbsolutePath()));
			
			/*
			 * compare the first token of imageName and tileName, and setName according the result  
			 */
			
			String[] imageNameTokens = imageName.split(Pattern.quote(File.separator));
			String[] tileNameTokens = tileFileData.getName(dataLoader.inputFolder.getAbsolutePath()).split(Pattern.quote(File.separator));
			
			int tileLen = tileNameTokens.length;
			
			
			if(imageNameTokens[0].equals(tileNameTokens[0]))
			{
				tile.setName(tileFileData.getName(dataLoader.inputFolder.getAbsolutePath()));
				// System.out.println("tileFileData name: "+tileFileData.getName(dataLoader.inputFolder.getAbsolutePath())+"\n");		
			}
			else
			{
				String tileName = null;
				tileName = tileNameTokens[tileLen -3] + File.separator + tileNameTokens[tileLen-2] + File.separator + tileNameTokens[tileLen-1];
				tile.setName(tileName);
				// System.out.println(tileName+"\n");
			}
		
			try {
				tile.setTilename(tileFileData.getTilename());
			} catch(UnableToGetRegionNameException e) {
				System.out.println("Fail to set tile name");
			}
					
			tile.setX(tileFileData.getX());
			tile.setY(tileFileData.getY());
			tile.setWidth(tileFileData.getWidth());
			tile.setHeight(tileFileData.getHeight());
			
			tile.setFilesize(tileFileData.getFileSize());
			tile.setResolution(tileFileData.getResolution());
			tile.setTiledtime(tileFileData.getTiledTime());
			//tile.setLocation(image.getLocation());
			
			try {
				if(ImageDBDataLoader.thumbnailCheck==1)
					tile.setThumbnail(tileFileData.getThumbnail());
			} catch (IOException e) {
				System.out.println("IO Exception");
				e.printStackTrace();
			}
			
			//tile.(tileFileData.getHierarchylevel());
			if(tileSet != null) {
				tile.setTileset(tileSet);
			}
			
			imageDAO.persist(tile);
			
			dataLoader.tilesProduced++;
			if(dataLoader.tilesProduced % 1000 == 0)
				System.out.println(dataLoader.tilesProduced + " tiles read.");
		}
	}
}
