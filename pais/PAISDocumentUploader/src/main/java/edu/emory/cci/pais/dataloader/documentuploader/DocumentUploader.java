package edu.emory.cci.pais.dataloader.documentuploader;


import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

import edu.emory.cci.pais.dataloader.db2helper.DBConfig;
import edu.emory.cci.pais.dataloader.db2helper.PAISDBHelper;


/**
 * 
 * @author Fusheng Wang
 * 
 * This class uploads a set of zipped XML to the database. 
 * Initially, all uploaded document records will have an INCOMPLETE status, also an INSERTIME. 
 */


public class DocumentUploader {	

	private PAISDBHelper db = null;
	private String loadingconfig;
	public static int failedCount=0;
	public static ArrayList<String> failedlist;
	//GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1, NO CACHE), 
	 
	private static String preparedDocUploadSql =  "INSERT INTO PAIS.STAGINGDOC(BLOB, STATUS, INSERT_TIME, FILE_NAME,LOADINGCONFIG) VALUES (?, 'INCOMPLETE', CURRENT_TIMESTAMP, ?,?) " ;

	public DocumentUploader(PAISDBHelper db,String loadingconfig) {
		this.db = db;
		this.loadingconfig=loadingconfig;
		failedlist = new ArrayList<String>();
	}
	
	
	/** Batch upload zip files in a folder */
	public boolean batchDocUpload(String rootPath){
		File folderFile = new File(rootPath);
		if(folderFile.isFile()&&folderFile.getName().toLowerCase().endsWith("zip"))
		{
		  boolean result = docUpload(folderFile.getAbsolutePath());
		  if (result == false) 
		  {
			  failedCount++;
			  failedlist.add(folderFile.getAbsolutePath());
			  System.out.println("File " + folderFile.getName() + "  upload failed.");
		  }
		  else
		     System.out.println("File " + folderFile.getName() + "  upload complete.");
		}
		else if(folderFile.isDirectory())
		{
		  File files[] = folderFile.listFiles();
		  for (File f:files){
				batchDocUpload(f.getAbsolutePath());
		  }
		}
		
		return true;	
	}
	
	/** Submit zip based XML to the database as BLOB. */
	public boolean docUpload(String zipFilePath){
		PreparedStatement pstmt = null; 
		File blobFile = new File(zipFilePath);
		String sql = preparedDocUploadSql;
		//INSERT INTO PAIS.STAGINGDOC(BLOB, STATUS, INSERT_TIME,LOADINGCONFIG) VALUES (?, 'INCOMPLETE', CURRENT_TIMESTAMP,?) 
		try {
			pstmt = db.getDBConnection().prepareStatement(sql);
			InputStream inBlob = new FileInputStream(blobFile);	
			pstmt.setBinaryStream(1, inBlob, inBlob.available());
			pstmt.setString(2, zipFilePath);
			pstmt.setString(3, loadingconfig);
			pstmt.executeUpdate();	
			pstmt.close();
			inBlob.close();
		} catch (Exception e) {
			e.printStackTrace();
			try {
				if (pstmt != null) pstmt.close();
			} catch (SQLException e1) {
			}
			return false;
		}		
		return true;	
	}
	
	public static void main(String[] args) {
		documentUploadermain(args);	
	}
	
	public static void documentUploadermain(String[] args)
	{
		GnuParser CLIparser = new GnuParser();
	    	HelpFormatter formatter = new HelpFormatter();
	    	CommandLine line = null;
	    	
			Option help = new Option("h", "help", false, "display this help and exit.");
			help.setRequired(false);
			Option folder = new Option("i", "input", true, "the path to the input folder or file, all zipped files under this path will be uploaded recursively");
			folder.setRequired(true);
			folder.setArgName("input");
			Option dbConfigFile = new Option("dbc", "dbConfigFile", true, "xml file with the configuration of the database.");
			dbConfigFile.setRequired(true);
			dbConfigFile.setArgName("dbConfigFile");
			
			Options options = new Options();
			options.addOption(help);
			options.addOption(folder);
			options.addOption(dbConfigFile);
			
			CLIparser = new GnuParser();
			line = null;
			try {
				line = CLIparser.parse(options, args);
				if(line.hasOption("h")) {
					formatter.printHelp("uploader", options, true);
					System.exit(0);
				}
			} catch(org.apache.commons.cli.ParseException e) {
				e.printStackTrace();
				formatter.printHelp("uploader", options, true);
				System.exit(1);
			}	
			
			long startCurrentTime = 0;
	        long endCurrentTime = 0;
	        long totalTime = 0;
	        startCurrentTime = System.currentTimeMillis();
	        
			PAISDBHelper db = new PAISDBHelper(new DBConfig(new File(line.getOptionValue("dbConfigFile"))));
	        
			DocumentUploader uploader = new DocumentUploader(db,"cell");

			String folderString = line.getOptionValue("input");
			uploader.batchDocUpload(folderString);	

			endCurrentTime = System.currentTimeMillis();
	        totalTime = endCurrentTime - startCurrentTime;
	        System.out.println("Total time (seconds):" + totalTime/1000.0);	

			if(failedCount!=0)
			{
				System.out.println("totally "+failedCount+" files upload failed!");
				for(String s:failedlist)
				{
					System.out.println(s+" failed");
				}
			}
	}

}
	
