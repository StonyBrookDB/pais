package edu.emory.cci.pais.dataloader.documentuploader;


import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
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

	//GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1, NO CACHE), 
	 
	private static String preparedDocUploadSql =  "INSERT INTO PAIS.STAGINGDOC(BLOB, STATUS, INSERT_TIME, FILE_NAME,LOADINGCONFIG) VALUES (?, 'INCOMPLETE', CURRENT_TIMESTAMP, ?,?) " ;

	public DocumentUploader(PAISDBHelper db,String loadingconfig) {
		this.db = db;
		this.loadingconfig=loadingconfig;

	}
	
	/** Upload all documents of a set, e.g., a validation set. Assume documents are stored in a root folder, which 
	 * contains folders for multiple PAIS documents. e.g., root folder "/GBMvalidation/SR20_AR20_NS-MORPH_Sq01", with subfolders
	 * astroII.1, ..., oligoIII.2.ndpi... */
	public boolean uploadDataSet(String rootPath){
		File rootDir = new File(rootPath);
		FileFilter fileFilter = new FileFilter() { public boolean accept(File file) { return file.isDirectory(); } }; 
		File[] childDirs = rootDir.listFiles(fileFilter); 
		boolean success = true;
		if (childDirs == null){
			System.out.println("No folder to upload.");
			return false;
		}
		for (int i = 0; i < childDirs.length; i++){
			String file = childDirs[i].getAbsolutePath();
			success = success && batchDocUpload(file);			
		}
		if (success == true) return true;
		else {
			System.out.println("Data set loaded with errors.");
			return false;
		}
	}
	
	/** Batch upload zip files in a folder */
	public boolean batchDocUpload(String rootPath){
		File folderFile = new File(rootPath);
		FilenameFilter zipOnly = new OnlyExt("zip");
		String files[] = folderFile.list(zipOnly);
		int failedCount = 0;
		for (int i = 0; i < files.length; i++){
			String file = files[i];
			String zipFilePath = rootPath + File.separatorChar + file;
			boolean result = docUpload(zipFilePath);
			if (result == false) failedCount++;
			System.out.println("File " + file + "  upload complete.");
		}
		System.out.println(failedCount + " failes upload failed.");
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

	class OnlyExt implements FilenameFilter{
		String ext;
		public OnlyExt(String ext){		  
			this.ext="." + ext;
		}

		public boolean accept(File dir,String name){		  
			return name.endsWith(ext);
		}
	}

}
	
