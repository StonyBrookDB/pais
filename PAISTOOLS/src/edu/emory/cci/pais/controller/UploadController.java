package edu.emory.cci.pais.controller;

import edu.emory.cci.pais.dataloader.db2helper.PAISDBHelper;
import edu.emory.cci.pais.dataloader.documentuploader.DocumentUploader;
import edu.emory.cci.pais.util.TengUtils;

/**
 * @author dejun teng, Center for Comprehensive Informatics, Emory University
 * @version 1.0
 * This class is the controller of document upload panel, the functions realized the upload of original zipped pais markup files
 *
 */
public class UploadController {
	
	
	//use tool DocumentUploader to upload files. db is the database connection, type define the input folder type collection or slide, loadingconfig
	//is the path of loading configure file.
	public void documentUploader(PAISDBHelper db,String filePath,int type,String loadingconfig)
	{
		
		long startCurrentTime = 0;
        long endCurrentTime = 0;
        long totalTime = 0;
        startCurrentTime = System.currentTimeMillis();        
		DocumentUploader uploader = new DocumentUploader(db,loadingconfig);

		if(type == TengUtils.SLIDE)
		 uploader.batchDocUpload(filePath);
		else if(type == TengUtils.COLLECTION )
		 uploader.uploadDataSet(filePath);				

		endCurrentTime = System.currentTimeMillis();
        totalTime = endCurrentTime - startCurrentTime;
        System.out.println("Total time (seconds):" + totalTime/1000.0);	 	
	}
	
	
	

}
