package edu.emory.cci.imagedb.FileDataCollector;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import org.hibernate.Hibernate;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;

import java.io.IOException;
import java.io.InputStream;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.emory.cci.imagedb.FileDataCollector.ImageFormatNotDefinedException;
import edu.emory.cci.pais.PAISIdentifierGenerator.DataGeneratorConfig;
import edu.emory.cci.pais.PAISIdentifierGenerator.PAISIdentifierGenerator;

/**
 * This class does not provide a getLocation() method, because it is too expensive to
 * create a Location instance for each tile. Since the Location for images and tiles
 * is the same (the folder hierarchy that differs between images and tiles is included
 * in the getName methods of ImageFileData and TileFileData), the same location
 * returned by getLocation() in ImageFileDate can be used for Tiled images. Example:
 * 
 *  '/home/
 *     user/
 *        images/
 *           EmoryTCGA/
 *              image.svs
 *              20Xtiles/
 *                 dataSet/
 *                    tile.tif
 *                    ...
 *           ...
 * 
 *  location.folder := "/home/user/images/"
 *  imageFileData.name := "EmoryTCGA/image.svs"
 *  tileFileData.name := "EmoryTCGA/20Xtiles/dataSet/tile.tif"
 *  
 * @author Steveoh
 *
 */
public class TileFileData {

	File tileFile;
	PAISIdentifierGenerator paisIdGenerator;
		
	Map<String, Integer> resolutionToHirarchylevelMappings;
	
	static Map<String, Long> imageFormatsMappings = new HashMap<String, Long>();
	
	static {
		imageFormatsMappings.put("tif", new Long(1));
		imageFormatsMappings.put("tiff", new Long(1));
		imageFormatsMappings.put("png", new Long(2));
		imageFormatsMappings.put("jpg", new Long(3));
	}
	
	public Map<String, Integer> getResolutionToHirarchylevelMappings() {
		return resolutionToHirarchylevelMappings;
	}

	/**
	 * The map provided specifies the mappings between resolutions and hierarchy levels.
	 * @param resolutionToHirarchylevelMappings: i.e.: [[40X, 0], [20X, 1], [10X, 2], [5.5X, 3], [1X, 4]]
	 */
	public void setResolutionToHirarchylevelMappings(
			Map<String, Integer> resolutionToHirarchylevelMappings) {
		this.resolutionToHirarchylevelMappings = resolutionToHirarchylevelMappings;
	}

	public TileFileData() {
		super();
	}
	
	public TileFileData(File file, File paisIdGeneratorConfigFile) {
		this();
		this.tileFile = file;
		this.paisIdGenerator = new PAISIdentifierGenerator(new DataGeneratorConfig(paisIdGeneratorConfigFile));
	}

	public File getTileFile() {
		return tileFile;
	}

	public void setTileFile(File tileFile) {
		this.tileFile = tileFile;
	}

	public PAISIdentifierGenerator getPaisIdGenerator() {
		return paisIdGenerator;
	}

	public void setPaisIdGenerator(PAISIdentifierGenerator paisIdGenerator) {
		this.paisIdGenerator = paisIdGenerator;
	}

	public Long getFormat() {
		String tokens[] = tileFile.getName().split("\\.");
		String fileExtension = tokens[tokens.length-1];
		if(!imageFormatsMappings.containsKey(fileExtension)) {
			throw new ImageFormatNotDefinedException("The image format '"+fileExtension+"' is not supported for tiles. The format must be added in the mapping from formats to Long in "+this.getClass().getName()+".");
		}
		return imageFormatsMappings.get(fileExtension);
	}

	public String getName(String datasetAbsolutePath) {
		// Get the folder name of the dataset
		String[] datasetPathTokens = datasetAbsolutePath.split(Pattern.quote(File.separator));
		String dataSet = datasetPathTokens[datasetPathTokens.length - 1];
		
		String[] tileFilePathTokens = tileFile.getAbsolutePath().split(Pattern.quote(File.separator));
		if(tileFilePathTokens.length < 1) {
			System.err.println("The folder structure is not as expected for tile file '"+(tileFile.getAbsolutePath())+"'.");
			return (tileFile.getAbsolutePath());
		}
		
		// Find the index of the dataset folder in the path of the tile file
		int i=tileFilePathTokens.length-1;
		for(;i >= 0;i--) {
			if(tileFilePathTokens[i].equals(dataSet))
				break;
		}
		// Create the relative path from the dataset folder until the tile file
		String relativePath = "";
		for(int j=i;j < tileFilePathTokens.length;j++) {
			relativePath += tileFilePathTokens[j];
			if(j < tileFilePathTokens.length - 1)
				relativePath += File.separator;
		}
		return relativePath;
	}

	public Double getWidth(){
		Double width = new Double(0.0);
		try {
			Iterator<ImageReader> readers = ImageIO.getImageReadersByFormatName("tif");
			ImageReader reader = (ImageReader)readers.next();
			ImageInputStream iis = ImageIO.createImageInputStream(tileFile);
			reader.setInput(iis,true);
			width = Double.parseDouble(Integer.toString(reader.getWidth(0)));
			return width;	
		} catch (IOException e) {
			e.getMessage();
			return new Double(1.0);
		}
		catch (RuntimeException r) {
			r.getMessage();
			return new Double(2.0);
		}
	}

	public Double getHeight(){
		Double height = new Double(0.0);
		try {
			Iterator<ImageReader> readers = ImageIO.getImageReadersByFormatName("tif");
			ImageReader reader = (ImageReader)readers.next();
			ImageInputStream iis = ImageIO.createImageInputStream(tileFile);
			reader.setInput(iis,true);
			height = Double.parseDouble(Integer.toString(reader.getHeight(0)));
			return height;	
		} catch (IOException e) {
			e.getMessage();
			return new Double(1.0);
		}
		catch (RuntimeException r) {
			r.getMessage();
			return new Double(2.0);
		}
	}

	public String getTilename() {
		return paisIdGenerator.getRegionName(tileFile.getName());
	}
	
	public Double getX() {
		return new Double(paisIdGenerator.getRegionXCoordinate(paisIdGenerator.getRegionCoordinates(tileFile.getName())));
	}
	
	public Double getY() {
		return new Double(paisIdGenerator.getRegionYCoordinate(paisIdGenerator.getRegionCoordinates(tileFile.getName())));
	}
	
	public Long getFileSize() {
		return tileFile.length();
	}
	
	public String getResolution() {
		// Try to get the resolution from the folder name according to the convention
		String folderName = getAllTilesFolder(tileFile.getAbsolutePath());
		Pattern pattern = Pattern.compile("[0-9]{1,2}((\\.){1}[0-9]{1,2})?X");
		Matcher matcher = pattern.matcher(folderName);
		if(matcher.find())
			return folderName.substring(matcher.start(), matcher.end());
		// Try to get the resolution from any folder name in the absolute path of the tile file
		matcher.reset(tileFile.getAbsolutePath());
		if(matcher.find()) 
			return tileFile.getAbsolutePath().substring(matcher.start(), matcher.end());
		// Nothing worked, returning the resolution from the configuration
		return paisIdGenerator.getCoordinateResolution();
	}
	
	private String getAllTilesFolder(String tileAbsolutePath) {
		String[] tokens = tileAbsolutePath.split(Pattern.quote(File.separator));
		return tokens[tokens.length - 3];
	}

	public Date getTiledTime() {
		return new Date(tileFile.lastModified());
	}
	
	public long getTileId() {
		return paisIdGenerator.getRegionId(tileFile.getName()).longValue();
	}
	
	public Integer getHierarchylevel() {
		return resolutionToHirarchylevelMappings.get(this.getResolution());
	}
	
	public String getTilesetName() {
		String[] tokens = tileFile.getAbsolutePath().split(Pattern.quote(File.separator));
		return tokens[tokens.length-2].trim().replace("-tile", "");
	}
	
	public java.sql.Blob getThumbnail() throws IOException {
		// convert tif to png
		String filePath1 = tileFile.getParent() + File.separator + "tile_thumbnail";
		File thumbnailFolder = new File(filePath1);
		if (!thumbnailFolder.exists())
			thumbnailFolder.mkdir();
		String filePath = thumbnailFolder.getAbsolutePath() + File.separator + tileFile.getName() + "-tn.jpg";
		String convertCommand = "convert " + tileFile.getAbsolutePath() + " -resize 200x200 " + filePath;
		
		try {
			Process proc = Runtime.getRuntime().exec(convertCommand); 
			int exitVal = proc.waitFor();
		}
		catch (Throwable t) {
			t.printStackTrace();
		}

		File thumbnailfile = new File(filePath);
		// convert jpg to byte
	    InputStream is = new FileInputStream(thumbnailfile);
			
	    // Get the size of the file
	    long length = thumbnailfile.length();
	
	    // to ensure that file is not larger than Integer.MAX_VALUE.
	    if (length > Integer.MAX_VALUE) {
	        // File is too large
	    }
	
	    // Create the byte array to hold the data
	    byte[] bytes = new byte[(int)length];
	
	    // Read in the bytes
	    int offset = 0;
	    int numRead = 0;
	    
		while (offset < bytes.length
		       && (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0) {
		    offset += numRead;
		}
	
	    // Ensure all the bytes have been read in
	    if (offset < bytes.length) {
			throw new IOException("Could not completely read file "+thumbnailfile.getName());
	    }
	
	    // Close the input stream and return bytes
		is.close();
	    thumbnailfile.delete();
	    
	    return Hibernate.createBlob(bytes);
	}
}
