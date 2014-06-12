package edu.emory.bmi.validation;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class FolderProcessor {
	ArrayList<String> users = new ArrayList<String>();
	public FolderProcessor (String root, String outputRoot){
		ParsingHelper parser = new ParsingHelper();
		users = parser.getFolders(root);	
		processClassification(root, outputRoot);
		processSegmentation(root, outputRoot);
		//SQLGenerator.userTimestampSQLLoader(outputRoot);
		processTimestamp(outputRoot);
	}
	
	public boolean processClassification(String root, String outputRoot){
		for (int i=0; i < users.size(); i ++){
			String user = users.get(i);
			ClassificationParser.processUserClassification(root, user, outputRoot);
		}
		return true;
	}

	
	public boolean processSegmentation(String root, String outputRoot){
		for (int i=0; i < users.size(); i ++){
			String user = users.get(i);
			SegmentationParser.processUserSegmentation(root, user, outputRoot);
		}
		return true;
	}

	
	public boolean processTimestamp(String outputRoot){
		File sqlFile =  new File(outputRoot + File.separator + "timestampload.sql");	
		FileWriter sqlFWriter  = null;
		BufferedWriter sqlBWriter = null;
		String timestampFile = outputRoot + File.separator +   "submissiontimestamp.txt";
		try {
			sqlFWriter = new FileWriter(sqlFile, true);
			sqlBWriter = new BufferedWriter(sqlFWriter);
			String sql = SQLGenerator.userTimestampSQLLoader(outputRoot);
			sqlBWriter.append(sql);			
			sqlBWriter.close();
			sqlFWriter.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return true;
	}
	

//	public boolean processSegmentation(String root){ }
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		String root = "F:\\Projects\\Github\\pais\\miccaichallenge\\test\\human";
		String outputRoot = "c:\\temp\\miccai";
		if (args.length == 3) {
			root = args[1];
			outputRoot = args[2];
		}
		FolderProcessor processor = new FolderProcessor(root, outputRoot);

		

	}

}
