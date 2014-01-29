package edu.emory.cci.pais.dataloader.dataloadingmanager;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.HashMap;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import edu.emory.cci.pais.dataloader.db2helper.PAISDBHelper;
import edu.emory.cci.pais.dataloader.db2helper.QueryGenerator;


/**
 * @author Fusheng Wang, Center for Comprehensive Informatics, Emory University
 * @version 1.0
 * This class provides functions and constructs for uploading data directly by parsing XML data
 *
 */
public class PAISParser {


	private static final int BATCH_SIZE = 1000;
	private XMLInputFactory xmlif = null ;
	private XMLStreamReader xmlr  = null;
	private String loadingConfigFile;
	
	
	public PAISParser(String loadingConfigFile){
		this.loadingConfigFile = loadingConfigFile;
	}
	

	public boolean setXMLInputFactory(String filename){
        try{
            xmlif = XMLInputFactory.newInstance();
            xmlif.setProperty(XMLInputFactory.IS_REPLACING_ENTITY_REFERENCES,Boolean.TRUE);
            xmlif.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES,Boolean.FALSE);        
            xmlif.setProperty(XMLInputFactory.IS_COALESCING , Boolean.FALSE);            
            xmlr = xmlif.createXMLStreamReader(filename, new FileInputStream(filename));
        }catch(Exception ex){
            ex.printStackTrace();
            return false;
        }
        return true;
	}
	
	public XMLStreamReader getXmlStreamReader(){
		return xmlr;
	}
	

	/**
	 * @param filename XML file to be parsed
	 * @param partionSize Not used
	 * @param targetFolder Folder for the metadata of the document
	 * @param PAISDBHelper DB to be used
	 * @param write  If metadata needs to be written
	 * @param load If data needs to be loaded into DB
	 * @param uniqueFile If use the same filename for writing metadoc file
	 * @return Metadata filename
	 *  */
	public LoadResult parse(String filename, int partionSize, String targetFolder, PAISDBHelper db, boolean write, boolean load, boolean uniqueFile)  {

		QueryGenerator queryGenerator;
		try {
			queryGenerator = QueryGenerator.getQueryGenerator();
		} catch(IllegalStateException e) {
			queryGenerator = QueryGenerator.getQueryGenerator(loadingConfigFile);
		}
		LoadResult result = null;
		long starttime = System.currentTimeMillis();
		//ParseLoader loader = new ParseLoader();        
		File targetFolderFile = null;
		BufferedWriter writer = null;
		String metaFilename = null;
		File tempFile = null; 
		int resultCount = 0;
		String uid = null;
		String tileName = null;  

/*		String markupSQL = QueryGenerator.generatePreparedMarkupSQL();
		String regionSQL = QueryGenerator.generatePreparedRegionSQL();
		String ordinalObsSQL = QueryGenerator.generatePareparedObservationSQL("Ordinal");
		String nominalObsSQL = QueryGenerator.generatePareparedObservationSQL("Nominal");
		String annotationMarkupSQL = QueryGenerator.generatePreparedAnnotationMarkupSQL();
		PreparedStatement markupPstmt = db.getPareparedStatement(markupSQL);
		PreparedStatement regionPstmt = db.getPareparedStatement(regionSQL);		
		PreparedStatement ordinalObservationPstmt = db.getPareparedStatement(ordinalObsSQL);
		PreparedStatement nominalObservationPstmt = db.getPareparedStatement(nominalObsSQL);
		PreparedStatement annotationMarkupPstmt = db.getPareparedStatement(annotationMarkupSQL);*/

		db.initLoadingPreparedStatements(loadingConfigFile);		
		
		if(write == true && uniqueFile == false) {
			if (targetFolder != null){
				targetFolderFile = new File(targetFolder);
				if ( !targetFolderFile.exists() ){
					targetFolderFile.mkdir();
				}
				metaFilename = StAXUtils.getTargetFilename(filename, 0, targetFolder);
			}
			else {
				try {
					int sepPos = filename.lastIndexOf(File.separatorChar);
					tempFile = File.createTempFile(filename.substring(sepPos +1) + "_meta",  ".xml");
					metaFilename = tempFile.getAbsolutePath();
				} catch (IOException e){
					e.printStackTrace();
					throw new RuntimeException(e);
				}
			}
			//System.out.println(metaFilename);
			writer =StAXUtils.getBufWtr(metaFilename);
		}//end non-unique file

		setXMLInputFactory(filename);
        XMLStreamReader xmlr = getXmlStreamReader();
        
        StringBuffer headerBuf = new StringBuffer("");
        StringBuffer footerBuf = new StringBuffer("");

        HashMap <String, String> rootMap = new HashMap <String, String> ();
        HashMap <String, String> imageReferenceCollectionMap = new HashMap <String, String> ();
        HashMap <String, String> markupMap = null; //new HashMap <String, String> ();
        HashMap <String, String> annotationMap = null; //new HashMap <String, String> ();

        int  globalNum = 0; 
        try{                
        	while(xmlr.hasNext()){
        		xmlr.next();
        		if ( StAXUtils.isStartElement("PAIS", xmlr) )	     {
        			rootMap = StAXUtils.printMapStartElement(xmlr, headerBuf);
        			uid = rootMap.get("PAIS/@uid");
        			
        			//Create write for unique filename case. The file will be stored in temp folder with name "uid"_meta.xml.
        			if(write == true && uniqueFile == true) {
        				if (targetFolder != null){
        					targetFolderFile = new File(targetFolder);
        					if ( !targetFolderFile.exists() ){
        						targetFolderFile.mkdir();
        					}
        					metaFilename = targetFolder + File.separator + "_meta.xml";
        				}
        				else {
        					try {
        						String tempDir = System.getProperty("java.io.tmpdir");
        						if (tempDir.endsWith(File.separator) )
        							tempFile = new File(tempDir +  uid +"_meta.xml");        						
        						else
        							tempFile = new File(System.getProperty("java.io.tmpdir") + File.separator + uid +"_meta.xml");
        						metaFilename = tempFile.getAbsolutePath();
        					} catch (Exception e){
        						e.printStackTrace();
        						throw new RuntimeException(e);
        					}
        				}
        				File metaFilenameFile = new File(metaFilename); 
        				if(!metaFilenameFile.exists()) {
        					try {
        						metaFilenameFile.getParentFile().mkdirs();
								metaFilenameFile.createNewFile();
							} catch (IOException e) {
								e.printStackTrace();
								throw new RuntimeException(e);
							}
        				}
        				writer =StAXUtils.getBufWtr(metaFilename);
        			}//end non-unique file     			
        		}
        		if ( StAXUtils.isStartElement("project", xmlr) )	 StAXUtils.getElement("project", headerBuf, xmlr);
        		if ( StAXUtils.isStartElement("collections", xmlr) ) StAXUtils.getElement("collections", headerBuf, xmlr);                	
        		if ( StAXUtils.isStartElement("group", xmlr) ) 		 StAXUtils.getElement("group", headerBuf, xmlr);
        		if ( StAXUtils.isStartElement("user", xmlr) ) 		 StAXUtils.getElement("user", headerBuf, xmlr);
        		if ( StAXUtils.isStartElement("imageReferenceCollection", xmlr) ) {
        			imageReferenceCollectionMap = StAXUtils.mapElement("imageReferenceCollection", headerBuf, xmlr);
        			//System.out.println(headerBuf);
        			StAXUtils.addPaisUid(imageReferenceCollectionMap, rootMap);
        			tileName = imageReferenceCollectionMap.get("Region/@name");
        			//StAXUtils.printMap(imageReferenceCollectionMap);
        			queryGenerator.addBatchRegionQuery(db.getLoadingPreparedStatement("regionPstmt"), imageReferenceCollectionMap);
					db.executeBatch(db.getLoadingPreparedStatement("regionPstmt"));
        		} 

        		//Starting of markupCollection
        		if ( StAXUtils.isStartElement("markupCollection", xmlr) ) {
        			int num = 0; 
        			//StringBuffer queryBuf = new StringBuffer();
        			while(xmlr.hasNext()){
        				xmlr.next();
        				
        				//If ending of markupCollection, break
        				if ( StAXUtils.isEndElement("markupCollection", xmlr) ) {
        					//While end of markupCollection reaches, append ending tag for each file.
        					if ( num == 0) globalNum--;
        					break;
        				}
        				
        				//For each Markup element, print it
        				if ( StAXUtils.isStartElement("Markup", xmlr) )	{
        					//System.out.println("Num:" + num);
/*        					if (num == 0 && globalNum == 0) {	
        						try {     							 
        							if (write == true)
        								writer.write( headerBuf.toString() );
								} catch (IOException e) {								
									System.out.println("Failed to write to metadata file.");
									e.printStackTrace();									
									return null;
								}
        					}
*/        					markupMap = StAXUtils.mapElement("Markup", xmlr);
        					markupMap = StAXUtils.addMap(markupMap, rootMap, imageReferenceCollectionMap);        					  
        					resultCount++;
        					if(load == true) { 
        						queryGenerator.addBatchMarkupQuery(db.getLoadingPreparedStatement("markupPstmt"), markupMap);
        						db.executeBatch(db.getLoadingPreparedStatement("markupPstmt"));
        					}
        					
        					num ++;
        					if (num == partionSize ) {
        						globalNum ++;
        						num =0;
        						//System.out.println("Num:" + globalNum);
        					}
        				}
        			}
        		} //end markupCollection
        		
        		
        		//Starting of markupCollection
        		if ( StAXUtils.isStartElement("annotationCollection", xmlr) ) {
        			globalNum = 0;
        			int num = 0; 
        			resultCount = 0;
        			while(xmlr.hasNext()){
        				xmlr.next();
        				
        				//If ending of annotationCollection, break
        				if ( StAXUtils.isEndElement("annotationCollection", xmlr) ) {
        					if ( num == 0) globalNum--;
        					break;
        				}
        				        				
        				//For each Annotation element, print it
        				if ( StAXUtils.isStartElement("Annotation", xmlr) )	{
        					annotationMap = StAXUtils.mapAnnotationElement("Annotation", xmlr);
        					annotationMap = StAXUtils.addMap(annotationMap, rootMap, imageReferenceCollectionMap);
        					if (load == true) {        						
        						if (resultCount == 0){//Prepared SQL needs to find name/values to generate columns
        							 String calculationFlatSQL = queryGenerator.generatePreparedFlatCalculationSQL(annotationMap);
        							 //System.out.println(calculationFlatSQL);
        							//calculationFlatPstmt = db.createPreparedStatement(calculationFlatSQL);
        							 db.setLoadingPreparedStatement("calculationFlatPstmt", calculationFlatSQL);
        						}
        						queryGenerator.addBatchAnnotationMarkupQuery(db.getLoadingPreparedStatement("annotationMarkupPstmt"), annotationMap);
        						db.executeBatch( db.getLoadingPreparedStatement("annotationMarkupPstmt") );
        						queryGenerator.addBatchFlatCalculationQuery(db.getLoadingPreparedStatement("calculationFlatPstmt"), annotationMap);
        						db.executeBatch(db.getLoadingPreparedStatement("calculationFlatPstmt"));
        						queryGenerator.addBatchObservationQueries(db.getLoadingPreparedStatement("ordinalObservationPstmt"), db.getLoadingPreparedStatement("nominalObservationPstmt"), annotationMap);        						
        						db.executeBatch(db.getLoadingPreparedStatement("ordinalObservationPstmt"));
        						db.executeBatch(db.getLoadingPreparedStatement("nominalObservationPstmt"));
        						resultCount++;
        					}
        					num ++;
        					if (num ==  partionSize ) {
        						globalNum ++;
        						num =0;
        						//System.out.println("Num:" + globalNum);
        					}
        				}
        			}
        		} //end annotationCollection

        		if ( StAXUtils.isStartElement("provenanceCollection", xmlr) )	 
        			StAXUtils.getElement("provenanceCollection", footerBuf, xmlr);

        		if ( StAXUtils.isEndElement("PAIS", xmlr) )	{
        			StAXUtils.printEndElement(xmlr, footerBuf);
						try {
							if (write == true)
								writer.write( headerBuf.toString() );
								writer.write( footerBuf.toString() );
						} catch (IOException e) {
							System.out.println("Failed to write to metadata file.");
							e.printStackTrace();
							//Close new files
							try {
								if (write == true) writer.close();
							} catch (IOException e2) {
								e2.printStackTrace();
							}
							return null;
						}
        		}
        	}//end while
        }catch(XMLStreamException ex){
        	System.out.println(ex.getMessage());
        	if(ex.getNestedException() != null)ex.getNestedException().printStackTrace();
        	throw new RuntimeException(ex);
        }finally {  
	        //Close input file
	        try {
				xmlr.close();
			} catch (XMLStreamException e1) {
				e1.printStackTrace(); 
			}
	        
			//Close new files
			try {
				if (write == true) writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
        }
		//set commit
		if (db != null && load == true){
			db.finalizeLoadingStatements();
			//db.commit();
/*			try{
				if (markupPstmt!=null) markupPstmt.close();
				if(regionPstmt!=null) regionPstmt.close();	
				if(ordinalObservationPstmt!=null) ordinalObservationPstmt.close();
				if(nominalObservationPstmt!=null) nominalObservationPstmt.close();
				if(annotationMarkupPstmt != null) annotationMarkupPstmt.close();
			} catch (Exception e){
				e.printStackTrace();
			}	*/
		}
		long endtime = System.currentTimeMillis();
		double loadingTime = (endtime - starttime)/1000.0;
		//System.out.println("Total Parsing and Loading Time = " + loadingTime + " seconds." );	
        result = new LoadResult(uid, tileName, resultCount, metaFilename, loadingTime);
      //Close new files
		try {
			if (write == true) writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
    }
	
	
	public LoadResult parse(String filename, PAISDBHelper db, boolean write, boolean load, boolean uniqueFile)  {
		return parse(filename, BATCH_SIZE, null, db, write, load, uniqueFile); 
	}
	
	/* Filename filter based on extension. Used for returning zipped XML files. */
	class OnlyExt implements FilenameFilter{
		String ext;
		public OnlyExt(String ext){		  
			this.ext="." + ext;
		}
		public boolean accept(File dir,String name){		  
			return name.endsWith(ext);
		}
	}	

	
	public static void main(String[] args) {
		long startTime = 0;
		long endTime = 0;
		//String filename = "C:\\temp\\validation\\template.xml";
		//String filename = "/tmp/validation/oligoIII.2.ndpi/split3/oligoIII.2.ndpi-0000073728-0000024576.tif.grid4.mat.xml";
		//String filename = "C:\\temp\\validation\\oligoIII.2.ndpi-0000094208-0000012288.tif.grid4.mat.xml";
		String filename = "/develop/imageanalysis/GBMvalidation/validation_paisXML/astroII.1.xml";
		//String targetFolder = "C:\\temp\\validation";

/*		
		String filename = "/tmp/validation/oligoIII.2.ndpi/oligoIII.2.ndpi-0000073728-0000024576.tif.grid4.mat.xml"; 
		String targetFolder = "/tmp/validation";
		*/
		
		int partionSize = 1000;		
    
		startTime = System.currentTimeMillis() ;

		PAISParser loader = new PAISParser("conf" + File.pathSeparator + "loadingconfig.xml");
		//PAISDBHelper db = new PAISDBHelper(false);
		PAISDBHelper db = new PAISDBHelper("europa.cci.emory.edu","50000", "db2user", "userdb1234", "devdb");
		endTime = System.currentTimeMillis();
		System.out.println("DB initization time = " + (endTime - startTime)/1000.0 + " seconds." );
		
		startTime = System.currentTimeMillis() ;

		//LoadResult result = loader.parse(filename, partionSize, null, db, true, true, false);
		LoadResult result = loader.parse(filename, partionSize, null, db, true, false, false);
		//System.out.println(result.getMetaFilename() );
		//splitter.splitFolder(folderName, partionSize, targetFolder);  
		endTime = System.currentTimeMillis();
		System.out.println("Total Parsing and Loading Time = " + (endTime - startTime)/1000.0 + " seconds." );
		
		startTime = System.currentTimeMillis() ;
		db.close();
		endTime = System.currentTimeMillis();
		System.out.println("DB Closing Time = " + (endTime - startTime)/1000.0 + " seconds." );
	}

}


