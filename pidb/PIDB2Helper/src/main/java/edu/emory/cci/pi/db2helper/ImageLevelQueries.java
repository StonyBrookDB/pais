package edu.emory.cci.pi.db2helper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.regex.Pattern;

// file copies
import com.zehon.FileTransferStatus;
import com.zehon.exception.FileTransferException;
import com.zehon.scp.SCP;

/**
 * 
 * @author Steve Oh,
 * Comprehensive Informatics
 * 
 * This class takes care of all image level queries
 *
 */
public class ImageLevelQueries {

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
	
	public ImageLevelQueries(){
		try {
			stmt = myconn.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

/**
 * Return and store image file that has specific imagereferenceUid into writeToLocalFolder
 * @param String imageReferenceUid : PI.IMAGE.imagereference_uid, i.e, "TCGA-06-0648-01Z-00-DX1_40X"
 * @param String writeToLocalFolder: returned image folder path i.e, "/Users/taewooko/Documents/workspace/imagedb/PIDB2Helper/PIDB2Helper"
 * @return File imageFile : image file, i.e, "image.svs" , "image.ndpi"
 */
	public File getImageByImageUID(String imageReferenceUid, String writeToLocalFolder) {
		String psql = "SELECT i.name, l.folder " +
		"FROM PI.IMAGE i, PI.LOCATION l " + 
		"WHERE i.location_id = l.id and " +
		"i.imagereference_uid = " + "'" + imageReferenceUid + "'";
		
		List<String> list = new ArrayList<String>();
	    try {
	        ResultSet rs = stmt.executeQuery(psql);
	        while(rs.next()){
	        	String imageFolderName = rs.getString(1);
	        	String locationName = rs.getString(2);
	        	
	        	if(imageFolderName!=null && locationName!=null)
	        	{
	        		list.add(locationName+imageFolderName);
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
	    
		String scpFolder = getFolderName(list.get(0));
		String nameOfFile = getImageName(list.get(0));
		try {
			int status = SCP.getFile(nameOfFile, scpFolder, host, username, passwd, writeToLocalFolder);
			if(FileTransferStatus.SUCCESS == status){
				System.out.println(nameOfFile + " got downloaded successfully to folder "+writeToLocalFolder);
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
 * Return Image name
 * @param String path : whole file path, i.e, "/Users/taewooko/Documents/workspace/imagedb/PIDB2Helper/TCGA-06-0648-01Z-00-DX1.svs"
 * @return String imageName, i.e, "TCGA-06-0648-01Z-00-DX1.svs"
 */
	public static String getImageName(String path) {
		String[] tokens = path.split(Pattern.quote(File.separator));
		if(tokens.length < 2) {
			System.err.println("The image file path is not correct '"+path+"'.");
			return path;
		}
		
		return (tokens[tokens.length - 1]);
	}
	
/**
 * Return folderName
 * @param String path : whole file path, i.e, "/Users/taewooko/Documents/workspace/imagedb/PIDB2Helper/TCGA-06-0648-01Z-00-DX1.svs"
 * @return String folderName, folder path that contains the image,  i.e, "/Users/taewooko/Documents/workspace/imagedb/PIDB2Helper/"
 */
	public static String getFolderName(String path) {
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
 * 
 * Return ImageThumbnail that has specific imagereference_uid and store into specific location	
 * Constraints: Increase heap size to execute the function (Java -Xmx256m)
 * @param imagereference_uid : PI.IMAGE imagereference_uid
 * @param format : jpg png gif
 * @param writeToLocalFolder: returned image thumbnail folder path i.e "/Users/taewooko/Documents/workspace/imagedb/PIDB2Helper/PIDB2Helper"
 * @return imageThumbnail
 * @throws IOException
 */
	
/*	
	public File getImageThumbnailByImageReferenceUID(String imagereferenceUid, String format, String writeToLocalFolder) throws IOException {
		

		// Default: 4096, 4096
		int width = 4096;
		int height = 4096;
		
		String psql = "SELECT PI.getRegionImage(" + "'" +
		imagereferenceUid +"'" + "," + "'" + format + "'" + "," + "0.0" + "," +
		"0.0" + "," +Double.toString(width) + "," + Double.toString(height) + ")" +
		"FROM SYSIBM.SYSDUMMY1";
		
		// String writeToLocalFolder = "/Users/taewooko/Documents/workspace/imagedb/PIDB2Helper/PIDB2Helper";
		String filePath = null;
		try {

            ResultSet rs = stmt.executeQuery(psql);
            while(rs.next()){
                Blob blob = rs.getBlob(1);
                filePath = writeToLocalFolder+File.separator+imagereferenceUid+"_"+"0.0"+"_"+"0.0"+"_"+Double.toString(width)+"_"+Double.toString(height)+"."+format;
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
*/

/**
 * Return ImageSubRegionThumbnail that has specific imagereference_uid and store into specific location	
 * Constraints: Only SVS image can be readable (ndpi format is not supported yet)
 * @param imagereference_uid : PI.IMAGE imagereference_uid
 * @param format : jpg png gif
 * @param width : image thumbnail width 
 * @param height : image thumbnail height
 * @param writeToLocalFolder: returned image thumbnail folder path i.e "/Users/taewooko/Documents/workspace/imagedb/PIDB2Helper/PIDB2Helper"
 * @return imageThumbnail
 * @throws IOException
 */
	public File getSubRegionImageFromImage(String imagereferenceUid, String format, double X, double Y, double width, double height, String writeToLocalFolder) throws IOException {
	
		String psql = "SELECT PI.getRegionImage(" + "'" +
		imagereferenceUid +"'" + "," + "'" + format + "'" + "," + Double.toString(X) + "," +
		Double.toString(Y) + "," +Double.toString(width) + "," + Double.toString(height) + ")" +
		"FROM SYSIBM.SYSDUMMY1";
		
		String filePath = null;
		try {

            ResultSet rs = stmt.executeQuery(psql);
            while(rs.next()){
                Blob blob = rs.getBlob(1);
                filePath = writeToLocalFolder+File.separator+imagereferenceUid+"_"+"0.0"+"_"+"0.0"+"_"+Double.toString(width)+"_"+Double.toString(height)+"."+format;
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
		
		ImageLevelQueries query = new ImageLevelQueries();		
*/
	
/*		
		File file = query.getImageByImageUID("TCGA-06-0648-01Z-00-DX1_40X", "/Users/taewooko/Documents/workspace/imagedb/PIDB2Helper/PIDB2Helper"); 
		System.out.println("file name: " + file.getName());
*/

/*		
		File file = query.getImageThumbnailByImageReferenceUID("TCGA-08-0358-01Z-00-DX1_40X", "jpg", "/Users/taewooko/Desktop");
		System.out.println("file name: " + file.getName());
*/
		
/*
		File file = query.getSubRegionImageFromImage("TCGA-08-0358-01Z-00-DX1_40X", "jpg", 7706, 1444, 500, 500, "/Users/taewooko/Desktop");
		System.out.println("file name: " + file.getName());
*/
/*	
		long endtime = System.currentTimeMillis();
	    System.out.println(" DB init time = " + (endtime - starttime)/1000.0 + " seconds." );   
		
	}
*/
}
