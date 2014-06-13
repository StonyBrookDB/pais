package edu.emory.bmi.validation;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.StringTokenizer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class SegmentationParser {
		
		//public static String outputRoot = ""
		public static String cachePath = System.getProperty("java.io.tmpdir");
		public static String classificationFileName = "classification.txt";
		public static String token = " ";

		//Human markups are in the same root folder, but comes with a user name "human". 
		public static boolean processUserSegmentation(String root, String userId, String outputRoot){
			boolean isHuman = false;		
			if ("human".equals(userId) ) {
				isHuman = true;
				System.out.println("Human results");
			}
			File sqlFile =  null;
			if (isHuman)
				sqlFile = new File(outputRoot + File.separator + "humansegmentationload.sql");
			else 
				sqlFile = new File(outputRoot + File.separator + "usersegmentationload.sql");
			
			FileWriter sqlFWriter  = null;
			BufferedWriter sqlBWriter = null;
			try {
				sqlFWriter = new FileWriter(sqlFile, true);
				sqlBWriter = new BufferedWriter(sqlFWriter);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			
			
			ParsingHelper parser = new ParsingHelper();
			File folder = new File(root + File.separator + userId + File.separator + "segmentation");
			File[] listOfFiles = folder.listFiles();		
			//  .../userid/classification/timestamp/; for human markups: /human/classification/timestamp/
			
			//Get timestamps, and pick the latest one
			long latestTimestamp = 0; 
			try {
				boolean ok = new File(outputRoot + File.separator + userId).mkdir();
				File timestampFile = new File(outputRoot + File.separator +  "submissiontimestamp.txt");
				FileWriter fw = new FileWriter(timestampFile,true);
				BufferedWriter bw = new BufferedWriter(fw);
				if(!timestampFile.exists()){
					timestampFile.createNewFile();
				}

				for (int i = 0; i < listOfFiles.length; i++) {
					String fileName = listOfFiles[i].getName();
					//Folders are numeric. Skip string based folders, like images, masks for human annotations.
					if (fileName.substring(0,1).matches("[0-9]") ) {
						long timestamp = Long.parseLong(fileName);
						if (timestamp >latestTimestamp ){
							latestTimestamp = timestamp; 
						}
						bw.write(userId +"," + "segmentation," + latestTimestamp + "\n");
					}
				}
				bw.close();
				fw.close();
			}catch (IOException e) {
				e.printStackTrace();
			}	
			
			//System.out.println(userId +"," + "segmentation," + latestTimestamp);
			
			String maskPath =  root + File.separator + userId + File.separator + "segmentation" + File.separator + latestTimestamp;
			File maskFolder = new File(maskPath);
			//System.out.println(maskPath);
			//File[] listOfMaskFiles = folder.listFiles();
			File[] files = maskFolder.listFiles(new FilenameFilter() {
				public boolean accept(File dir, String name) {
					return name.toLowerCase().endsWith(".zip");
				}
			});
					
			System.out.println(files.length);
			
			//For each zip file, unzip it to a temporary location, and process each file inside...

			File tempDir = null; 
			System.out.println(System.getProperty("java.io.tmpdir"));
			try {
				tempDir = Files.createTempDirectory(userId).toFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			for (int i=0; i < files.length; i++){
				File zipFile = files[i];
				ParsingHelper.unzipFile(zipFile.getAbsolutePath(), tempDir.getAbsolutePath() );		
			}
			File [] segmentationFiles = tempDir.listFiles();
			//System.out.println(segmentationFiles.length);
			String segmentationFolder = outputRoot + File.separator + userId + File.separator + "segmentation";
			//Parser the mask files
			try {
				boolean ok = new File(segmentationFolder).mkdir();
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
			
			for (int i = 0; i < segmentationFiles.length; i++){
				String newSegmentationFile = segmentationFiles[i].getName() + ".txt";
				String fullPath = segmentationFolder + File.separator + newSegmentationFile;
				MaskParser.parseMask (userId, segmentationFiles[i].getAbsolutePath(), fullPath, isHuman);
				//Generate SQL script; seperate SQL files for human and users.
				String sql = null;
				if (isHuman) sql = SQLGenerator.humanSegmentationSQLLoader(fullPath);
				else  sql = SQLGenerator.userSegmentationSQLLoader(fullPath);
				try {
					sqlBWriter.write(sql);
				} catch (IOException e) {					
					e.printStackTrace();
					return false;
				}				
			}
			try {
			sqlBWriter.close();
			sqlFWriter.close();
			} catch (IOException e) {					
				e.printStackTrace();
				return false;
			}		
			
			
			//remove tempDir
			
			Path tempPath = Paths.get(tempDir.getAbsolutePath());
			ParsingHelper.deletePath(tempPath);
/*			try {
				Files.delete(tempPath);
			} catch (IOException e) {
				e.printStackTrace();
			}*/
			
			
			return true;
			
		}
		
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
