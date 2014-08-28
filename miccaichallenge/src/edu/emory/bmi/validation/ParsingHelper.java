/**
 * @author Fusheng Wang
 * @organization Emory University
 */



package edu.emory.bmi.validation;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import static java.nio.file.FileVisitResult.*;

public class ParsingHelper {

	// "/home/miccai/human/1/classification"
	
/*	human/userid/segmentation/timestamp/
	human/userid/classification/timestamp/
	algorithm/userid/classification/timestamp/	
	*/
	
	
	private  static ArrayList<String> getList(String inputFolder, boolean isFolder) {
		
		File folder = new File(inputFolder);
		System.out.println("Input folder: " + folder.getAbsolutePath()  + "\n");
		File[] listOfFiles = folder.listFiles();
		ArrayList<String> list = new ArrayList<String>();
		System.out.println("Number of user folders to process: " + listOfFiles.length);
		
		if (folder.isDirectory()){
			for (int i = 0; i < listOfFiles.length; i++) {
				if (listOfFiles[i].isFile()) {
					if (!isFolder){
						String fileName = listOfFiles[i].getName();
						list.add(fileName);
					}
				} else if (listOfFiles[i].isDirectory()) {
					if (isFolder){
						String folderName = listOfFiles[i].getName();
						list.add(folderName);
					}
				}
			}
		}
		return list;		    
	}
	
	public static ArrayList<String> getFolders(String inputFolder){
		return getList(inputFolder, true);
	}
	
	public static ArrayList<String> getFiles(String inputFolder){
		return getList(inputFolder, false);
	}
	
	
	public static void copyInputStream(InputStream in, OutputStream out)
			throws IOException
			{
		byte[] buffer = new byte[4096];
		int len;

		while((len = in.read(buffer)) >= 0)
			out.write(buffer, 0, len);

		in.close();
		out.close();
	}
	
	public static void copyInputStream(File in, OutputStream out)throws IOException
	{
		copyInputStream(new FileInputStream (in),  out);
	}
	
	public static boolean unzipFile(String inputFile, String outputPath){
	   Enumeration entries;
	    ZipFile zipFile;
	    try {
	      zipFile = new ZipFile(inputFile);

	      entries = zipFile.entries();

	      while(entries.hasMoreElements()) {
	        ZipEntry entry = (ZipEntry)entries.nextElement();

	        if(entry.isDirectory()) {
	          // Assume directories are stored parents first then children.
	          //System.err.println("Extracting directory: " + entry.getName());
	          // This is not robust, just for demonstration purposes.
	          (new File(entry.getName())).mkdir();
	          continue;
	        }

	        //System.err.println("Extracting file: " + entry.getName());
	        copyInputStream(zipFile.getInputStream(entry),
	           new BufferedOutputStream(new FileOutputStream(outputPath + File.separator + entry.getName())));
	      }

	      zipFile.close();
	      return true;
	    } catch (IOException ioe) {
	      System.err.println("Unhandled exception:");
	      ioe.printStackTrace();
	      return false;
	    }
	  }

	public static void deletePath(Path dir){

		try {
			Files.walkFileTree(dir, new SimpleFileVisitor<Path>() {

				@Override
				public FileVisitResult visitFile(Path file,
						BasicFileAttributes attrs) throws IOException {

					//System.out.println("Deleting file: " + file);
					Files.delete(file);
					return CONTINUE;
				}

/*				@Override
				public FileVisitResult postVisitDirectory(Path dir,
						IOException exc) throws IOException {

					System.out.println("Deleting dir: " + dir);
					if (exc == null) {
						Files.delete(dir);
						return CONTINUE;
					} else {
						throw exc;
					}
				}*/

			});
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String getFilePrefix(String inputFile){

		int location = inputFile.lastIndexOf(File.separator);
		int extLocation = inputFile.lastIndexOf(".");
		return inputFile.substring(location+1, extLocation);
	}

	public static String generateImageName(String fileName){
		//path-image-129.mask -> path-image-129
		int location = fileName.lastIndexOf(".mask");
		//System.out.println(fileName);
		return fileName.substring(0, location);
	}
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ParsingHelper parser = new ParsingHelper();
		ArrayList<String> list = parser.getFiles("c:\\temp\\validation\\miccaichallenge");
		//for (int i = 0; i < list.size(); i++ )
		//	System.out.println(list.get(i) );
		String root = "F:\\Projects\\Github\\pais\\miccaichallenge\\test\\human\\user100\\segmentation\\20140610160500\\zip1.zip";
		ParsingHelper helper = new ParsingHelper();
		//helper.unzipFile(root, "c:\\temp\\zip");
	}

}
