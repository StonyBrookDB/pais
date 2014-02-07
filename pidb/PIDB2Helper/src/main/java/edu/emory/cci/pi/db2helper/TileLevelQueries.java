package edu.emory.cci.pi.db2helper;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import java.awt.geom.*;
import java.io.*;
import java.sql.Blob;

import com.zehon.FileTransferStatus;
import com.zehon.exception.FileTransferException;
import com.zehon.scp.SCP;

/**
 * 
 * @author Steve Oh,
 * Comprehensive Informatics Center
 * 
 * This class takes care of all tile level queries 
 */
public class TileLevelQueries {
	// DB connection
	public String file = "conf/dbconfig.xml";
	public DBConfig config = new DBConfig(new File(file) );
	public Properties props = config.getProperties();		
	public String host = props.getProperty("host");
	public String port = props.getProperty("port");
	public String username = props.getProperty("username");
	public String passwd = props.getProperty("passwd");
	public String database = props.getProperty("database");
	
	public PIDBTester db = new PIDBTester(host, port, username, passwd, database);
	public Connection myconn = db.getDBConnection();
	public static Statement stmt = null;
	
	public TileLevelQueries(){
		try {
			stmt = myconn.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
	
/**
 * Return and store tile image file into specific location
 * @param tilename PI.TILEDIMAGE tilename, i.e "TCGA-06-0152-01Z-00-DX6-0000016384-0000004096"
 * @param tilesetName : PI.TILESET name i.e "TCGA-06-0152-01Z-00-DX6.svs"
 * @param writeToLocalFolder: returned image folder path i.e "/Users/taewooko/Documents/workspace/imagedb/PIDB2Helper/PIDB2Helper"
 * @return tileImageFile : tile image file that has specific tile name and tileset name
 */
	public File getTileImage(String tilename, String tilesetName, String writeToLocalFolder){
		String psql = "SELECT ti.name, l.folder " +
		"FROM PI.TILEDIMAGE ti, PI.TILESET ts, PI.LOCATION l " + 
		"WHERE ti.tileset_id = ts.id and ti.location_id = l.id and " +
		"ti.tilename = " + "'" + tilename + "'" + " and " +
		"ts.name = " + "'" + tilesetName + "'";
		
		List<String> list = new ArrayList<String>();
	    try {
	        ResultSet rs = stmt.executeQuery(psql);
	        while(rs.next()){
	        	String tileImageFolderName = rs.getString(1);
	        	String locationName = rs.getString(2);
	        	
	        	if(tileImageFolderName!=null && locationName!=null)
	        	{
	        		list.add(locationName+tileImageFolderName);
	        	}
	        } // end of while        
	    } catch (SQLException e) {
	        // TODO Auto-generated catch block
	    	e.getMessage();
	    	e.getMessage();
	    } catch (NullPointerException e) {
	    	e.printStackTrace();
	    	e.getMessage();
	    }
	    
		String scpFolder = getTileFolderName(list.get(0));
		String nameOfFile = getTileImageName(list.get(0));

		try {
			int status = SCP.getFile(nameOfFile, scpFolder, host, username, passwd, writeToLocalFolder);
			if(FileTransferStatus.SUCCESS == status){
				System.out.println(nameOfFile + " got downloaded successfully to  folder "+writeToLocalFolder);
			}
			else if(FileTransferStatus.FAILURE == status){
				System.out.println("Fail to download  to  folder "+writeToLocalFolder);
			}
		} catch (FileTransferException e) {
			e.printStackTrace();
		}
		File file = new File(writeToLocalFolder + File.separator + nameOfFile);
		return file;	
	}
	
/**
 * Return and store tile image file into specific location. 
 * If it has multiple image files with different tileset then return first element
 * @param tilename : PI.TILEDIMAGE tilename i.e "TCGA-06-0152-01Z-00-DX6-0000016384-0000004096"
 * @param writeToLocalFolder: returned image folder path i.e "/Users/taewooko/Documents/workspace/imagedb/PIDB2Helper/PIDB2Helper"
 * @return tileImageFile : tile image file that has specific tile name
 */
	public File getTileImage(String tilename, String writeToLocalFolder){
		String psql = "SELECT ti.name, l.folder " +
		"FROM PI.TILEDIMAGE ti, PI.LOCATION l " + 
		"WHERE ti.location_id = l.id and " +
		"ti.tilename = " + "'" + tilename + "'";
	
		List<String> list = new ArrayList<String>();
	    try {
	        ResultSet rs = stmt.executeQuery(psql);
	        while(rs.next()){
	        	String tileImageFolderName = rs.getString(1);
	        	String locationName = rs.getString(2);
	        	
	        	if(tileImageFolderName!=null && locationName!=null)
	        	{
	        		list.add(locationName+tileImageFolderName);
	        	}
	        } // end of while        
	    } catch (SQLException e) {
	        // TODO Auto-generated catch block
	    	e.getMessage();
	    	e.getMessage();
	    } catch (NullPointerException e) {
	    	e.printStackTrace();
	    	e.getMessage();
	    }
	    
		if(list.size() > 1) {
			System.out.println("Multiple tilename exist with different tileset name. Please, specify tilesetName");
		}
		
		String scpFolder = getTileFolderName(list.get(0));
		String nameOfFile = getTileImageName(list.get(0));

		try {
			int status = SCP.getFile(nameOfFile, scpFolder, host, username, passwd, writeToLocalFolder);
			if(FileTransferStatus.SUCCESS == status){
				System.out.println(nameOfFile + " got downloaded successfully to  folder "+writeToLocalFolder);
			}
			else if(FileTransferStatus.FAILURE == status){
				System.out.println("Fail to download  to  folder "+writeToLocalFolder);
			}
		} catch (FileTransferException e) {
			e.printStackTrace();
		}
		File file = new File(writeToLocalFolder + File.separator + nameOfFile);
		return file;	
	}
	
/**
 * Return Tile Image name
 * @param path : whole file path, i.e "20Xtiles/TCGA-06-0152-01Z-00-DX6.svs-tile/TCGA-06-0152-01Z-00-DX6.svs-0000020480-0000004096.tiff"
 * @return tileImageName i.e "TCGA-06-0152-01Z-00-DX6.svs-0000020480-0000004096.tiff"
 */
	public String getTileImageName(String path) {
		String[] tokens = path.split(Pattern.quote(File.separator));
		if(tokens.length < 2) {
			System.err.println("The image file path is not correct '"+path+"'.");
			return path;
		}
		return (tokens[tokens.length - 1]);
	}
	
/**
 * Return tileFolderName
 * @param path : whole file path, i.e "/mnt/data2/test/20Xtiles/TCGA-06-0152-01Z-00-DX6.svs-tile/TCGA-06-0152-01Z-00-DX6.svs-0000020480-0000004096.tiff"
 * @return tileFolderName folder path that contains the tile image  i.e "/mnt/data2/test/20Xtiles/TCGA-06-0152-01Z-00-DX6.svs-tile/"
 */
	public String getTileFolderName(String path) {
		String[] tokens = path.split(Pattern.quote(File.separator));
		String folderName = File.separator;
		if(tokens.length < 2) {
			System.err.println("The image file path is not correct '"+path+"'.");
			return path;
		}
		for(int i=0;i<tokens.length-1;i++)
		{
			folderName = folderName+ tokens[i] + File.separator;
		
		}
		return (folderName.substring(1));
	}
	
/**
 * Return tileThumbnail that has specific tilename and tilesetname with specific format and store into specific location	
 * @param tilename : PI.TILEDIMAGE tilename
 * @param tilesetName : PI.TILESET name
 * @param format : jpg png gif
 * @param width : cropped image width 
 * @param height : cropped image height
 * @param writeToLocalFolder: returned tile thumbnail folder path i.e "/Users/taewooko/Documents/workspace/imagedb/PIDB2Helper/PIDB2Helper"
 * @return tileThumbnail
 * @throws IOException
 */
	public File getTileThumbnail(String tilename, String tilesetName, String format, int width, int height, String writeToLocalFolder) throws IOException {
		File tifFile = getTileImage(tilename, tilesetName, writeToLocalFolder);
		// convert tif to jpg
		// jpg, png, jpeg, gif 
        String outputFileName = writeToLocalFolder+File.separator+tilename+"."+format;
        File outputFile = new File(outputFileName);
       
        // Image resize
        BufferedImage src = ImageIO.read(tifFile);
        BufferedImage dest = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = dest.createGraphics();
        AffineTransform at = AffineTransform.getScaleInstance(
        (double)width/src.getWidth(), (double)height/src.getHeight());
        g.drawRenderedImage(src, at);
        ImageIO.write(dest, format, outputFile);
        tifFile.delete();
        return outputFile;
	}
/**
 * Return tile image lists that has same image referenceUid
 * @param imageReferenceUid : PI.TILEDIMAGE imagereference_uid
 * @param tilesetName : PI.TILESET name
 * @return tileNameLists Return null if there is no data
 */
	public List <String> getTileNameListOfImage(String imageReferenceUid, String tilesetName){
		String psql = "SELECT TI.NAME " +
		"FROM PI.TILEDIMAGE TI, PI.IMAGE I, PI.TILESET TS " + 
		"WHERE TI.IMAGE_ID = I.ID AND " +
		"TI.TILESET_ID = TS.ID AND " + 
		"I.IMAGEREFERENCE_UID = " + "'" + imageReferenceUid + "'" + " AND " +
		"TS.NAME = " + "'" + tilesetName + "'";
		
		List<String> list = new ArrayList<String>();
	    try {
	        ResultSet rs = stmt.executeQuery(psql);
	        while(rs.next()){
	        	String tileImageName = rs.getString(1);
	        	if(tileImageName!=null)
	        	{
	        		list.add(tileImageName);
	        	}
	        } // end of while        
	    } catch (SQLException e) {
	        // TODO Auto-generated catch block
	    	e.getMessage();
	    } catch (NullPointerException e) {
	    	e.printStackTrace();
	    	System.out.println("There is no data that matches up imageReferenceUid ");
	    	return null;
	    }
		return list;
	}
/**
 * Return tileName that contains specific point. 
 * If multiple tile names exist, then it return first element.
 * @param imageReferenceUid : PI.IMAGE imagereference_uid
 * @param tilesetName : PI.TILESET name
 * @param x : starting point of x coordinate
 * @param y : starting point of y coordinate
 * @return tileName that contains specific point i.e "TCGA-06-0152-01Z-00-DX6.svs-0000004096-0000004096.tiff" Return null if there is no tile name or input coordinate is out of bound
 */
	public String getTileNameFromPoint(String imageReferenceUid, String tilesetName, double x, double y){
		
		List <String> tileNameLists = getTileNameListOfImage(imageReferenceUid, tilesetName);
		String tileImageName = null;
		
		double tileWidth = getTileImageWidth(tileNameLists.get(0));
		double tileHeight = getTileImageHeight(tileNameLists.get(0));
		
		/** Local Coordinate Check
		 * Input (local coordinate x) should less than WIDTH value of PI.TILEDIMAGE 
		 * Input (local coordinate y) should less than HEIGHT value of PI.TILEDIMAGE
		 * 
		 * if coordinate values do not satisfy assumption, then it returns 'null' value
		 */
		if((x < 0) ||
		(x >= tileWidth) ||
		(y < 0) ||
		(y >= tileHeight))
		{
			System.out.println("Input Coordinate is out of bound. Please, insert correct coordinate.");
			return null;
		}

		for(int i=0; i<tileNameLists.size(); i++){
			tileImageName = getTileImageName(tileNameLists.get(i));
			// System.out.println("tileImageName: "+tileImageName+"\n");
			String[] tokens = tileImageName.split(Pattern.quote("-"));
			String coordinateX = tokens[tokens.length-2];
			String coordinateY = tokens[tokens.length-1].substring(0, tokens[tokens.length-1].length()-5);
			// System.out.println("cX: " + coordinateX + "\t cY: " + coordinateY + "\n");
			Double cX = Double.parseDouble(coordinateX);
			Double cY = Double.parseDouble(coordinateY);
			// System.out.println("cX: " + cX.toString() + "\t cY: " + cY.toString() + "\n");

			
			if((cX-tileWidth <= x) && (x <= cX) && (cY-tileHeight <= y) && (y <= cY)){
				return tileImageName;
			}
		}
		return tileImageName;
	}
/**
 * Return image width of specific tile image	
 * @param name : PI.TILEDIMAGE name
 * @return tileImageWidth
 */
	public double getTileImageWidth(String name){
		String psql = "SELECT TI.WIDTH " +
		"FROM PI.TILEDIMAGE TI " + 
		"WHERE TI.NAME = " + "'" + name + "'";
		
		Double tileImageWidth = null;
	    try {
	        ResultSet rs = stmt.executeQuery(psql);
	        while(rs.next()){
	        	tileImageWidth = rs.getDouble(1);
	        
	        } // end of while        
	    } catch (SQLException e) {
	        // TODO Auto-generated catch block
	    	e.getMessage();
	    	e.getMessage();
	    } catch (NullPointerException e) {
	    	e.printStackTrace();
	    	e.getMessage();
	    }
	    return tileImageWidth;
	}
	
/**
 * Return image height of specific tile image	
 * @param name : PI.TILEDIMAGE name
 * @return tileImageHeight
 */	
	public double getTileImageHeight(String name){
		String psql = "SELECT TI.HEIGHT " +
		"FROM PI.TILEDIMAGE TI " + 
		"WHERE TI.NAME = " + "'" + name + "'";

		Double tileImageHeight = null;
	    try {
	        ResultSet rs = stmt.executeQuery(psql);
	        while(rs.next()){
	        	tileImageHeight = rs.getDouble(1);
	        
	        } // end of while        
	    } catch (SQLException e) {
	        // TODO Auto-generated catch block
	    	e.getMessage();
	    	e.getMessage();
	    } catch (NullPointerException e) {
	    	e.printStackTrace();
	    	e.getMessage();
	    }
	    return tileImageHeight;
	}

/**
 * Returns tileImage that contains specific point and has specific imagereference_uid and stores the tileImage into specific location 
 * @param imageReferenceUid : PI.TILEDIMAGE imagereference_uid
 * @param tilesetName : PI.TILESET name
 * @param writeToLocalFolder, tileImageLocationFolder i.e "/Users/taewooko/Documents/workspace/imagedb/PIDB2Helper/PIDB2Helper"
 * @param x : Starting point of coordinate X
 * @param y : Starting point of coordinate Y
 * @return tileImageFile
 */

	public File getTileImageFromPoint (String imageReferenceUid, String tilesetName, String writeToLocalFolder, double x, double y){
		
		String name = getTileNameFromPoint(imageReferenceUid, tilesetName, x, y);
		String psql = "SELECT ti.name, l.folder " +
		"FROM PI.TILEDIMAGE ti, PI.LOCATION l " + 
		"WHERE ti.location_id = l.id and " +
		"ti.name like " + "'%" + name + "%'";

		List<String> list = new ArrayList<String>();
	    try {
	        ResultSet rs = stmt.executeQuery(psql);
	        while(rs.next()){
	        	String tileImageName = rs.getString(1);
	        	String locationName = rs.getString(2);
	        	
	        	if(tileImageName!=null && locationName!=null)
	        	{
	        		list.add(locationName+tileImageName);
	        	}
	        } // end of while        
	    } catch (SQLException e) {
	        // TODO Auto-generated catch block
	    	e.getMessage();
	    	e.getMessage();
	    } catch (NullPointerException e) {
	    	e.printStackTrace();
	    	e.getMessage();
	    }
	    
		if(list.size() > 1) {
			System.out.println("Multiple tilename exist. The function returns first element");
		}
		
		String scpFolder = getTileFolderName(list.get(0));
		String nameOfFile = getTileImageName(list.get(0));

		try {
			int status = SCP.getFile(nameOfFile, scpFolder, host, username, passwd, writeToLocalFolder);
			if(FileTransferStatus.SUCCESS == status){
				System.out.println(nameOfFile + " got downloaded successfully to  folder "+writeToLocalFolder);
			}
			else if(FileTransferStatus.FAILURE == status){
				System.out.println("Fail to download  to  folder "+writeToLocalFolder);
			}
		} catch (FileTransferException e) {
			e.printStackTrace();
		}
		File file = new File(writeToLocalFolder + File.separator + nameOfFile);
		return file;	
		
	}


/**
 * Returns tileNameLists that contain specific tilesetName
 * @param tilesetName : PI.TILESET name i.e "TCGA-06-0152-01Z-00-DX6.svs"
 * @return tileNameLists
 */
	public List<String> getTileNameListByTileset (String tilesetName){
		String psql = "SELECT ti.name " +
		"FROM PI.TILEDIMAGE ti, PI.TILESET ts " + 
		"WHERE ti.tileset_id = ts.id and " +
		"ts.name = " + "'" + tilesetName + "'";
		
		List<String> tileNameLists = new ArrayList<String>();
	    try {
	        ResultSet rs = stmt.executeQuery(psql);
	        while(rs.next()){
	        	String tileImageName = rs.getString(1);
	        	
	        	if(tileImageName!=null)
	        	{
	        		tileNameLists.add(tileImageName);
	        	}
	        } // end of while        
	    } catch (SQLException e) {
	        // TODO Auto-generated catch block
	    	e.getMessage();
	    	e.getMessage();
	    } catch (NullPointerException e) {
	    	e.printStackTrace();
	    	e.getMessage();
	    }
	    return tileNameLists;
	}
	
/**
 * Return subRegionTileImage that contains specific tile name, format and coordinates X Y Width and Height 
 * @param tilename : i.e "TCGA-06-0152-01Z-00-DX6-0000016384-0000004096"
 * @param format : i.e "jpg, jpeg, png, gif"
 * @param x : local starting point X coordinate
 * @param y : local starting point Y coordinate 
 * @param width : the width of cropping region
 * @param height : the height of cropping region
 * @return first file of subReginoTileImages return null if either there is no return file or input coordinate is out of bound   
 */
	public File getSubRegionImageFromTile(String tilename, String format, double x, double y, double width, double height){
		
		/** Local Coordinate Check
		 * Input (local coordinate x) should less than WIDTH value of PI.TILEDIMAGE 
		 * Input (local coordinate y) should less than HEIGHT value of PI.TILEDIMAGE
		 * 
		 * if coordinate values do not satisfy assumption, then it returns 'null' value
		 */
		String coordinateChkSQL = "SELECT WIDTH, HEIGHT " +
		"FROM PI.TILEDIMAGE " +
		"WHERE TILENAME = " + "'" + tilename + "'";
		try {
			ResultSet rsChk = stmt.executeQuery(coordinateChkSQL);
			while(rsChk.next()){	
				double widthFromTable = rsChk.getDouble(1);
				double heightFromTable = rsChk.getDouble(2);
				
				if((x < 0) ||
				(x >= widthFromTable) ||
				(y < 0) ||
				(y >= heightFromTable) || 
				(width > widthFromTable) ||
				(height > heightFromTable))
				{
					System.out.println("Input Coordinate is out of bound. Please, insert correct coordinate.");
					return null;
				}
			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		String psql = "SELECT getRegionImageTile(" + "'" +
		tilename +"'" + "," + "'" + format + "'" + "," + Double.toString(x) + "," +
		Double.toString(y) + "," +Double.toString(width) + "," + Double.toString(height) + ")" +
		"FROM SYSIBM.SYSDUMMY1";
		
		String writeToLocalFolder = "/Users/taewooko/Documents/workspace/imagedb/PIDB2Helper/PIDB2Helper";
		String filePath = null;
		int cnt = 1;
		try {

            ResultSet rs = stmt.executeQuery(psql);
            while(rs.next()){
                Blob blob = rs.getBlob(1);
                filePath = writeToLocalFolder+File.separator+"blobImages"+File.separator+tilename+"_"+Double.toString(x)+"_"+Double.toString(y)+"_"+Double.toString(width)+"_"+Double.toString(height)+"_"+Integer.toString(cnt)+"."+format;
                byte[] array = blob.getBytes(1, (int) blob.length());
                FileOutputStream out = new FileOutputStream(new File(filePath));
                try {
					out.write(array);
					File file = new File(filePath);
			        return file;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
           
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
	}
	
/**
 * Return subGlobalRegionTileImage that contains specific tile name, format and coordinates X Y Width and Height 
 * @param tilename : i.e "TCGA-06-0152-01Z-00-DX6-0000016384-0000004096"
 * @param format : i.e "jpg, jpeg, png, gif"
 * @param x : Global starting point X coordinate
 * @param y : Global starting point Y coordinate 
 * @param width : the width of cropping region
 * @param height : the height of cropping region
 * @return first file of GlobalsubReginoTileImages Return null if either there is no return file or input coordinate is out of bound  
 */

	public File getGlobalSubRegionImageFromTile(String tilename, String format, double x, double y, double width, double height){
		/** Global Coordinate Check
		 * Input (Global coordinate x) should bigger than X value of PI.TILEDIMAGE 
		 * Input (Global coordinate y) should bigger than Y value of PI.TILEDIMAGE
		 * 
		 * if coordinate values do not satisfy assumption, then it returns 'null' value
		 */
		
		String coordinateChkSQL = "SELECT WIDTH, HEIGHT, X, Y " +
		"FROM PI.TILEDIMAGE " +
		"WHERE TILENAME = " + "'" + tilename + "'";
		
		try {
			ResultSet rsChk = stmt.executeQuery(coordinateChkSQL);
			while(rsChk.next()){	
				double widthFromTable = rsChk.getDouble(1);
				double heightFromTable = rsChk.getDouble(2);
				double xFromTable = rsChk.getDouble(3);
				double yFromTable = rsChk.getDouble(4);
				
				if(((x-xFromTable) < 0) ||
				((x-xFromTable) >= widthFromTable) ||
				((y-yFromTable) < 0) ||
				((y-yFromTable) >= heightFromTable) ||
				(width > widthFromTable) ||
				(height > heightFromTable))
				{
					System.out.println("Input Coordinate is out of bound. Please, insert correct coordinate.");
					return null;
				}
			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		String psql = "SELECT getGlobalRegionImageTile(" + "'" +
		tilename +"'" + "," + "'" + format + "'" + "," + Double.toString(x) + "," +
		Double.toString(y) + "," +Double.toString(width) + "," + Double.toString(height) + ")" +
		"FROM SYSIBM.SYSDUMMY1";
		
		String writeToLocalFolder = "/Users/taewooko/Documents/workspace/imagedb/PIDB2Helper/PIDB2Helper";
		String filePath = null;
		int cnt = 1;
		try {
            ResultSet rs = stmt.executeQuery(psql);
            while(rs.next()){
                Blob blob = rs.getBlob(1);
                filePath = writeToLocalFolder+File.separator+"globalBlobImages"+File.separator+tilename+"_"+Double.toString(x)+"_"+Double.toString(y)+"_"+Double.toString(width)+"_"+Double.toString(height)+"_"+Integer.toString(cnt)+"."+format;
                byte[] array = blob.getBytes(1, (int) blob.length());
                FileOutputStream out = new FileOutputStream(new File(filePath));
                try {
					out.write(array);
					File file = new File(filePath);
			        return file;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
           
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
	}
/*
	public static void main(String[] args) throws IOException {
	
	long starttime = System.currentTimeMillis() ;
*/
	// File file = getTileImage("TCGA-06-0152-01Z-00-DX6-0000016384-0000004096", "TCGA-06-0152-01Z-00-DX6.svs", "/Users/taewooko/Documents/workspace/imagedb/PIDB2Helper/PIDB2Helper");
	// File file = getTileImage("TCGA-06-0152-01Z-00-DX6-0000016384-0000004096", "/Users/taewooko/Documents/workspace/imagedb/PIDB2Helper/PIDB2Helper");
	// File file = getTileThumbnail("TCGA-06-0152-01Z-00-DX6-0000016384-0000004096", "TCGA-06-0152-01Z-00-DX6.svs","png", "/Users/taewooko/Documents/workspace/imagedb/PIDB2Helper/PIDB2Helper");
/*
	TileLevelQueries query = new TileLevelQueries();
	// File file = query.getTileThumbnail("TCGA-06-0152-01Z-00-DX6-0000016384-0000004096", "TCGA-06-0152-01Z-00-DX6.svs","png", "/Users/taewooko/Documents/workspace/imagedb/PIDB2Helper/PIDB2Helper");
	File file = query.getTileImage("TCGA-06-0152-01Z-00-DX6-0000016384-0000004096", "/Users/taewooko/Documents/workspace/imagedb/PIDB2Helper/PIDB2Helper");
	System.out.println("file name: "+file.getName());
*/
	/*
	 * name: 20Xtiles/TCGA-06-0152-01Z-00-DX6.svs-tile/TCGA-06-0152-01Z-00-DX6.svs-0000020480-0000004096.tiff
	 * 
	List <String> tmp = getTileNameListOfImage("TCGA-06-0152-01Z-00-DX6_40X", "TCGA-06-0152-01Z-00-DX6.svs");

	for(int i=0; i<tmp.size(); i++){
		System.out.println("tile image list: " + tmp.get(i) + "\n");
	}
	*/
	/*
	TileLevelQueries query = new TileLevelQueries();
	String tileName = query.getTileNameFromPoint("TCGA-06-0152-01Z-00-DX6_40X", "TCGA-06-0152-01Z-00-DX6.svs", 100.0, 200.0);
	System.out.println("tile name: "+tileName);
	*/
	/*
	TileLevelQueries query = new TileLevelQueries();
	File file = query.getTileImageFromPoint ("TCGA-06-0152-01Z-00-DX6_40X", "TCGA-06-0152-01Z-00-DX6.svs", "/Users/taewooko/Documents/workspace/imagedb/PIDB2Helper/PIDB2Helper", 100.0 , 500.0);
	*/
	/*
	List <String> tmp = query.getTileNameListByTileset("TCGA-06-0152-01Z-00-DX6.svs");
	for(int i=0; i<tmp.size(); i++){
		System.out.println("tile image list: " + tmp.get(i) + "\n");
	}
	*/

	/*
	TileLevelQueries query = new TileLevelQueries();
	File file = query.getSubRegionImageFromTile("TCGA-06-0152-01Z-00-DX6-0000016384-0000004096", "gif", 10000.0, 100.0, 100.0, 100.0);	
	System.out.println("file name: "+file.getName());
	 */

	/*
	TileLevelQueries query = new TileLevelQueries();
	File file = query.getGlobalSubRegionImageFromTile("TCGA-06-0152-01Z-00-DX6-0000016384-0000004096", "jpg", 30000.0, 6000.0, 200.0, 200.0);	
	System.out.println("file name: "+file.getName());
	*/

/*
	long endtime = System.currentTimeMillis();
    System.out.println(" DB init time = " + (endtime - starttime)/1000.0 + " seconds." );   
	
	}
*/
	
}
