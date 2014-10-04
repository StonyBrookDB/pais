/**
 * For MICCAI, we need to do following things. We assume all data are collected and stored on the server.
You can sudo to miccai user from "fusheng" user to access testing file folders: 
sudo -u miccai -s
cd human

You will see a folder structure:

human/userid/segmentation/timestamp/
human/userid/classification/timestamp/
algorithm/userid/classification/timestamp/
(We either have the algorithm folder to differentiate algorithm from human, or we don't use algorithm folder but know which userid is the domain expert who makes ground truth annotations.

1. What we need to do is that we will create tables (algorithms and human) for storing segmentation results and classification results. We have the tables for segmentation (see examples in github), but not for classification yet. 
We might also need a submission timestamp table to track the timestamps for the last submission of each user.
CREATE TABLE MICCAI.usermask...
CREATE TABLE MICCAI.humanmask...
CREATE TABLE MICCAI.classification (
user,
image,
label...)...
CREATE TABLE MICCAI.submissiontimestamp( user, timestamp)...

Each classification folder will have only a single file, containing a label for each image. e.g.: 
challenge_image1.svs LGG
challenge_image2.svs GBM
challenge_image3.svs GBM
challenge_image4.svs LGG

2. We will need to create a batch loading program to parse all files and have them loaded into tables. We only load results for latest submission (ignore old timestamp folders.)

3. We will need to create WebAPIs to return results and evaluation:
a. Given a user, return the comparison of the segmentation area from the algorithm with human result for each image. See example SQL in github.
We can use SQL to pregeneate the results, and return a table, e.g..:
 challenge_image_name     overlap_percentage  (intersected pixels/total pixels from human mask)   Jaccard_similarity (overlap/union)
challenge_image1.svs  80%    70%
We also need to return the result as a WebAPI, e.g.:
getSegmentationComparison/user=userid;timestamp=

b. WebAPI to sort the results based on overlap_percentage or Jaccard_similarity.

c. Given a user, return the correctness for classification for each image.
e.g.:  image_uid      label correctness
challenge_image1.svs    LGG  true
challenge_image2.svs   GBM  false
A WebAPI like:
getClassificationComparison/user=userid;timestamp=

d. Sort above results based on best correctness.

3. We need to create WebAPI to download original files for each timestamp and user.
e.g.: getSubmissionData/user=userid;timestamp=...


These are top priorities. They want to have this ready early June. I have two travels these days and may not have time on this. 
Once this is working, we can look at mask conversion and have polygons loaded as well. That will be another story.


 * 
 */

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
		boolean ok = new File(outputRoot).mkdir();
		//processClassification(root, outputRoot);
		processSegmentation(root, outputRoot);
		processTimestamp(outputRoot);
		generateLoadSQL(outputRoot);
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

	//Load all commands
	public boolean generateLoadSQL(String outputRoot){
		File sqlFile =  new File(outputRoot + File.separator + "loadallcmd");	
		FileWriter sqlFWriter  = null;
		BufferedWriter sqlBWriter = null;
		String script =
			"# Change  database name 'pais' to real database name; change 'db2inst1' to db2 login with loading permissions\n" + 	
			"db2 connect to pais user db2inst1\n" + 
			"db2 -tf timestampload.sql\n" + 
			"db2 -tf humanclassificationload.sql\n" + 
			"db2 -tf humansegmentationload.sql\n" + 
			"db2 -tf userclassificationload.sql\n" + 
			"db2 -tf usersegmentationload.sql\n";
		try {
			sqlFWriter = new FileWriter(sqlFile, true);
			sqlBWriter = new BufferedWriter(sqlFWriter);
			sqlBWriter.append(script);			
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
		String root = "C:\\Projects\\miccaichallenge\\miccaichallengesampledata";
		String outputRoot = "C:\\Projects\\miccaichallenge\\output";
		if (args.length >= 2) {
			root = args[0];
			outputRoot = args[1];
		}
		else {
			System.out.println("Number of arguments: " + args.length  + " No input/output folders specified."); 
/*			for (int i = 0; i < args.length; i++){
				System.out.println(i + "," + args[i] );
			}
*/		
		}
		
		FolderProcessor processor = new FolderProcessor(root, outputRoot);
	}

}
