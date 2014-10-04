package edu.emory.bmi.validation;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.StringTokenizer;

public class ClassificationParser {
	static String classificationFileName = "classification_test.txt";
	static String token = " ";

	
	
	
	
	public static boolean processUserClassification(String root, String userId, String outputRoot){
		ParsingHelper parser = new ParsingHelper();
		File folder = new File(root + File.separator + userId + File.separator + "classification");
		File[] listOfFiles = folder.listFiles();		
		//  human/userid/classification/timestamp/
		
		boolean isHuman = false;		
		if ("human".equals(userId) ) {
			isHuman = true;
			System.out.println("Human results");
		}		
		File sqlFile =  null;
		if (isHuman)
			sqlFile = new File(outputRoot + File.separator + "humanclassificationload.sql");
		else 
			sqlFile = new File(outputRoot + File.separator + "userclassificationload.sql");
		
		FileWriter sqlFWriter  = null;
		BufferedWriter sqlBWriter = null;
		try {
			sqlFWriter = new FileWriter(sqlFile, true);
			sqlBWriter = new BufferedWriter(sqlFWriter);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		//Get timestamps, and pick the latest one
		long latestTimestamp = 0; 
		//Write timestamp to a file in outputRoot folder.
		try {
			boolean ok = new File(outputRoot + File.separator + userId).mkdir();
			File timestampFile = new File(outputRoot + File.separator +   "submissiontimestamp.txt");
			FileWriter fw = new FileWriter(timestampFile,true);
			BufferedWriter bw = new BufferedWriter(fw);
			if(!timestampFile.exists()){
				timestampFile.createNewFile();
			}

			for (int i = 0; i < listOfFiles.length; i++) {
				String fileName = listOfFiles[i].getName();
				long timestamp = Long.parseLong(fileName);
				if (timestamp >latestTimestamp ){
					latestTimestamp = timestamp; 
				}
				bw.write(userId +"," + "classification," + latestTimestamp + "\n");
			}
			bw.close();
			fw.close();
		}catch (IOException e) {
			e.printStackTrace();
		}		

	
		//classification.txt
		File classificationFile = new File(root + File.separator + userId + File.separator + "classification" + File.separator + latestTimestamp + File.separator + classificationFileName );
		String newClassificaitonFileName = null; 
		try {
			boolean ok = new File(outputRoot + File.separator + userId + File.separator + "classification").mkdir();
			BufferedReader br = new BufferedReader(new FileReader(classificationFile));
			String strLine = "";
			StringTokenizer st = null;
			newClassificaitonFileName = outputRoot + File.separator + userId + File.separator + "classification" + 
					File.separator + "classification.txt";			
			File newClassificationFile = new File(newClassificaitonFileName);
			BufferedWriter bw = new BufferedWriter(new FileWriter(newClassificationFile) );
			
			while ((strLine = br.readLine()) != null) {
				st = new StringTokenizer(strLine, token);
				String image = st.nextToken(token).trim();
				//image = ParsingHelper.generateImageName(image);
				String label = st.nextToken(token).trim();				
				//System.out.println(userId + "," + image + "," + label);
				bw.write(userId + "," + image + "," + label + "\n");
			}
			bw.close();
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
		String sql = SQLGenerator.classificationSQLLoader(newClassificaitonFileName);
		try {
			sqlBWriter.write(sql);
			sqlBWriter.close();
			sqlFWriter.close();
		} catch (IOException e) {					
			e.printStackTrace();
			return false;
		}					
		return true;		
	}
	
	
	
	
	
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ClassificationParser.processUserClassification("F:\\Projects\\Github\\pais\\miccaichallenge\\test\\human",
				"user100", "c:\\temp");
	}

}
