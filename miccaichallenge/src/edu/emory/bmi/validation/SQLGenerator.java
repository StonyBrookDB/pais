package edu.emory.bmi.validation;

import java.io.File;

public class SQLGenerator {

/*	LOAD FROM "/home/avpuser/production/miccai/TCGA-02-0006-01Z-00-DX1_mask.map2" OF DEL MODIFIED BY 
	COLDEL,
	METHOD P (1, 2, 3) MESSAGES "/tmp/miccai/TCGA-02-0006-01Z-00-DX1_mask.log" 
	INSERT INTO 
	MICCAI.MASK (IMAGE, X, Y) COPY NO INDEXING MODE AUTOSELECT;	*/
	

/*	Date date = new Date();
	SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy h:mm:ss a");
	String formattedDate = sdf.format(date);
	System.out.println(formattedDate); // 12/01/2011 4:48:16 PM
*/	
	
	public static String humanSegmentationSQLLoader (String maskFilePath){
		String localFileName = "humansegmentationload.log";
		StringBuffer strBuf = new StringBuffer("LOAD FROM \"" + maskFilePath + "\" OF DEL MODIFIED BY COLDEL, \n");
		strBuf.append("METHOD P (1, 2, 3) MESSAGES ");
		strBuf.append("\"" + System.getProperty("java.io.tmpdir") + File.pathSeparator + localFileName + "\"\n");  
		strBuf.append("INSERT INTO MICCAI.HUMANMASK (IMAGE, X, Y) COPY NO INDEXING MODE AUTOSELECT;\n");
		//System.out.println(strBuf.toString());
		return strBuf.toString();
	}
	
	public static String userSegmentationSQLLoader (String maskFilePath){
		String localFileName = "humansegmentationload.log";
		StringBuffer strBuf = new StringBuffer("LOAD FROM \"" + maskFilePath + "\" OF DEL MODIFIED BY COLDEL, \n");
		strBuf.append("METHOD P (1, 2, 3, 4) MESSAGES ");
		strBuf.append("\"" + System.getProperty("java.io.tmpdir") + File.pathSeparator +localFileName + "\"\n");  
		strBuf.append("INSERT INTO MICCAI.USERMASK (USER,IMAGE, X, Y) COPY NO INDEXING MODE AUTOSELECT;\n");
		//System.out.println(strBuf.toString());
		return strBuf.toString();
	}
	

	public static String classificationSQLLoader(String classificationFilePath){
		String localFileName = "humanclassificationload.log";
		StringBuffer strBuf = new StringBuffer("LOAD FROM \"" + classificationFilePath + "\" OF DEL MODIFIED BY COLDEL, \n");
		strBuf.append("METHOD P (1, 2, 3) MESSAGES ");
		strBuf.append("\"" + System.getProperty("java.io.tmpdir") + File.pathSeparator + localFileName + "\"\n");  
		strBuf.append("INSERT INTO MICCAI.CLASSIFICATION (user, image, label) COPY NO INDEXING MODE AUTOSELECT;\n");
		//System.out.println(strBuf.toString());
		return strBuf.toString();		
	}
	
	
	public static String userTimestampSQLLoader(String outRoot){
		String localFileName = "humanclassificationload.log";
		String timestampFilePath = outRoot + File.separator +  "submissiontimestamp.txt";
		StringBuffer strBuf = new StringBuffer("LOAD FROM \"" + timestampFilePath + "\" OF DEL MODIFIED BY COLDEL, \n");
		strBuf.append("METHOD P (1, 2, 3) MESSAGES ");
		strBuf.append("\"" + System.getProperty("java.io.tmpdir") + File.pathSeparator + localFileName + "\"\n");  
		strBuf.append("INSERT INTO MICCAI.submissiontimestamp (user, type, timestamp) COPY NO INDEXING MODE AUTOSELECT;\n");
		//System.out.println(strBuf.toString());
		return strBuf.toString();		
	}	
	
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String maskFilePath = "C:\\Temp\\user100\\segmentation\\TCGA-02-0006-01Z-00-DX1_mask.txt.txt";
		String classificationFilePath = "C:\\Temp\\user100\\classification.txt";
		String outRoot = "c:\\temp";
		SQLGenerator.humanSegmentationSQLLoader(maskFilePath);
		SQLGenerator.classificationSQLLoader(classificationFilePath);
		SQLGenerator.userTimestampSQLLoader(outRoot);
		
	}

}
