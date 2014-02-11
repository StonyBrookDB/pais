package edu.emory.cci.pais.converter;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;
import java.util.zip.ZipInputStream;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;




/**
 * @author Fusheng Wang, Center for Comprehensive Informatics, Emory University
 * @version 1.0
 * This class provides functions to parse XML data
 *
 */
public class AperioXMLParser {


	private String filterAttrName = "Name";
	private String filterAttrValue = "Pseudopalisade"; 
	private boolean annotation = true;
	private boolean description = true;

	private XMLInputFactory xmlif = null ;
	private XMLStreamReader xmlr  = null;
	
	
	
	public AperioXMLParser(){
	}

	public AperioXMLParser(String filter,String filterLocation){
		setFilterAttr(filter);
		if(filterLocation.equalsIgnoreCase("a"))
			setDescription(false);
		else if(filterLocation.equalsIgnoreCase("d"))
			setAnnotation(false);
	}	
	
	
	public boolean isAnnotation() {
		return annotation;
	}

	public void setAnnotation(boolean annotation) {
		this.annotation = annotation;
	}

	public boolean isDescription() {
		return description;
	}

	public void setDescription(boolean description) {
		this.description = description;
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
	
	public void setFilterAttr(String attrValue){
		filterAttrValue = attrValue;
	}

	/**
	 * @param filename XML file to be parsed
	 *  */
	public boolean parse(String filename, String targetFolder) {

		long starttime = System.currentTimeMillis();
     
		File targetFolderFile = null;
		BufferedWriter writer = null;


		String outFilename;

		if (targetFolder == null ){
			outFilename = StAXUtils.getTargetFilename(filename, "txt");
			System.out.println(outFilename);	
		} 		
		else{		
			targetFolderFile = new File(targetFolder);
			if ( !targetFolderFile.exists() ){
				targetFolderFile.mkdir();
			}
			outFilename = StAXUtils.getTargetFilename(filename, 0, targetFolder);
			System.out.println(outFilename);		
		}		
		
		writer =StAXUtils.getBufWtr(outFilename);

			
		setXMLInputFactory(filename);
        XMLStreamReader xmlr = getXmlStreamReader();
        

        int idx = 1;
        
        try{          
        	
        	//  /Annotations/Annotation/Regions/Region/Vertices
        	while(xmlr.hasNext()){
        		xmlr.next();
        		if ( StAXUtils.isStartElement("Annotations", xmlr) ) {
        			while(xmlr.hasNext()){
        				xmlr.next();
        	       		if ( StAXUtils.isEndElement("Annotations", xmlr) ) break;
        	       		
        				if ( StAXUtils.isStartElement("Annotation", xmlr) ) {
        					//System.out.println("Start ant: ");
        					
        				boolean checkElem = false;
        				
        				if ( (filterAttrValue== null) || (StAXUtils.hasAttribute("Name", filterAttrValue, xmlr )&&annotation==true) )
                		 checkElem = true;
                		
        				
        					while(xmlr.hasNext()){
        						try{
        						xmlr.next();          					
        						if ( StAXUtils.isEndElement("Annotation", xmlr) )  { break; } //System.out.println("End ann");
        						
                                if(StAXUtils.isStartElement("Attribute", xmlr) )
                                {
                                	if ( (filterAttrValue== null) || (StAXUtils.hasAttribute("Value", filterAttrValue, xmlr )&&description==true ))
                                		checkElem = true;                                		
                                }
        						if ( StAXUtils.isStartElement("Region", xmlr)&&checkElem){  
        							//System.out.println("Start Region:"); 
        								StringBuffer buf = new StringBuffer(idx + "; ");
        								idx ++;
        								buf.append( StAXUtils.getRegionCoords(xmlr) );
        								buf.append("\n");
        								//System.out.println(buf);
        								try {
        									 writer.write(buf.toString() );
        									
        								} catch (IOException e) {
        									// TODO Auto-generated catch block
        									e.printStackTrace();
        								}
        						
        						}//start Region
        						//if ( StAXUtils.isEndElement("Region", xmlr) )  {System.out.println("End Region");  }
        						} catch (Exception e) {
                					System.out.println("parse failed!");
                					e.printStackTrace();
                					return false;
                				}
        						
        					}
        				
        				}//start Annotation
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
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
        }

        long endtime = System.currentTimeMillis();
		double loadingTime = (endtime - starttime)/1000.0;
		//System.out.println("Total Parsing and Loading Time = " + loadingTime + " seconds." );	
      //Close new files
		try {
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return true;
    }
	
	
	/** Batch convert Aperio files in a folder */
	public boolean batchConvert(String rootPath, String targetFolder){
		File folderFile = new File(rootPath);
		FilenameFilter zipOnly = new OnlyExt("zip");
		File zipfiles[] = folderFile.listFiles(zipOnly);
		ZipInputStream zipIn;
		for(int i = 0;i<zipfiles.length;i++)
		{
			try {
				unzipFile(zipfiles[i].getCanonicalPath());
				
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		FilenameFilter xmlOnly = new OnlyExt("xml");
		String files[] = folderFile.list(xmlOnly);
		int failedCount = 0;
		System.out.println("# of files: " + files.length);
		for (int i = 0; i < files.length; i++){
			String file = files[i];
			String zipFilePath = rootPath + File.separatorChar + file;
			boolean result;
			result = parse(zipFilePath, targetFolder);
			if (result == false) failedCount++;			
			
		}
		System.out.println( failedCount +  " of files failed. ");
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
	
	
	
	public void unzipFile(String pathfile) {
		
		try {
			ZipInputStream zipIn = new ZipInputStream(new FileInputStream(pathfile));
			//tempFile = new File("/media/LINUXBUS/tmp/"+prefix+".xml");
		if(new File(pathfile.substring(0, pathfile.lastIndexOf("."))).exists()==true)
            {
			return;
			    
            }
		OutputStream outStream = new FileOutputStream(pathfile.substring(0, pathfile.lastIndexOf(".")));
        byte[] buffer = new byte[2000000];
        int nrBytesRead;

        //Get next zip entry and start reading data
        if (zipIn.getNextEntry() != null) {
            while ((nrBytesRead = zipIn.read(buffer)) > 0) {
                outStream.write(buffer, 0, nrBytesRead);
            }
        }
        //Finish off by closing the streams
        outStream.close();
        zipIn.close();
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
}

}


