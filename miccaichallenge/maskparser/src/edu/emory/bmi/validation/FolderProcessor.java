package edu.emory.bmi.validation;

import java.util.ArrayList;

public class FolderProcessor {
	ArrayList<String> users = new ArrayList<String>();
	public FolderProcessor (String root, String outputRoot){
		ParsingHelper parser = new ParsingHelper();
		users = parser.getFolders(root);	
		processClassification(root, outputRoot);
		processSegmentation(root, outputRoot);
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


//	public boolean processSegmentation(String root){ }
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		String root = "F:\\Projects\\Github\\pais\\miccaichallenge\\test\\human";
		String outputRoot = "c:\\temp";
		FolderProcessor processor = new FolderProcessor(root, outputRoot);
		//processor.processClassification(root);
		

	}

}
