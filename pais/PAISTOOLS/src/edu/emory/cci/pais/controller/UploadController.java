package edu.emory.cci.pais.controller;

import edu.emory.cci.pais.dataloader.db2helper.PAISDBHelper;
import edu.emory.cci.pais.dataloader.documentuploader.DocumentUploader;

/**
 * @author dejun teng, Center for Comprehensive Informatics, Emory University
 * @version 1.0
 * This class is the controller of document upload panel, the functions realized the upload of original zipped pais markup files
 *
 */
public class UploadController {
	
	
	//use tool DocumentUploader to upload files. db is the database connection, type define the input folder type collection or slide, loadingconfig
	//is the path of loading configure file.
	public void documentUploader(PAISDBHelper db,String filePath,String loadingconfig)
	{     
		DocumentUploader uploader = new DocumentUploader(db,loadingconfig);
	
		uploader.batchDocUpload(filePath);
	}
	
	
	

}
