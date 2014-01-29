package edu.emory.cci.pais.controller;

import java.io.File;
import java.io.IOException;
import java.util.AbstractMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.regex.Pattern;

import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import edu.emory.cci.pais.AperioXMLParser.AperioXMLParser;
import edu.emory.cci.pais.PAISBoundary2PAIS.PAISBoundary2PAIS;
import edu.emory.cci.pais.documentgenerator.PAISDocumentGenerator;
import edu.emory.cci.pais.util.TengUtils;
/**
 * @author Dejun Teng, Center for Comprehensive Informatics, Emory University
 * @version 1.0
 * This class provides the operation of transferpanel, transfer Aperio output XML file into zipped or unzipped PAIS recognizable file
 *
 */
public class ConvertController{
	public static String tmpdir = System.getProperty("java.io.tmpdir");

	//public static String slash="\\";
	public ConvertController()
	{
		
	}
	
	public void convert(String inputType, String outputType,String inputPath,String outputPath,String configFilePath) throws IOException, InterruptedException
	{
		boolean zipornot = outputType.equalsIgnoreCase("zipped PAIS")||outputType.equalsIgnoreCase("zipped")?true:false;
		
	    String aperioOutput = tmpdir+File.separator+"aperioOutput";
	    String fixerOutput = tmpdir+File.separator+"fixerOutput";
	    new File(aperioOutput).mkdirs();
	    new File(fixerOutput).mkdirs();
	    //
		if(inputType.equalsIgnoreCase("Aperio XML File")||inputType.equalsIgnoreCase("aperio"))
		{
		aperioXmlParser(inputPath,aperioOutput,null,"");
		paisBoundary2PAIS(aperioOutput,fixerOutput);
		paisDocumentGenerator(fixerOutput,outputPath, configFilePath,zipornot);
		}
		else if(inputType.equalsIgnoreCase("unfixed points")||inputType.equalsIgnoreCase("unfixed"))
		{
			paisBoundary2PAIS(inputPath,fixerOutput);
			paisDocumentGenerator(fixerOutput,outputPath, configFilePath,zipornot);	
		}
		else if(inputType.equalsIgnoreCase("fixed points")||inputType.equalsIgnoreCase("fixed"))
		{

			System.out.println(inputPath);
			paisDocumentGenerator(inputPath,outputPath, configFilePath,zipornot);	
			
		}
		TengUtils.deleteDir(new File(aperioOutput));
		TengUtils.deleteDir(new File(fixerOutput));
		
	}
	
	
	public static void main(String[] args)
	{
	    try {
			new ConvertController().convert("","","/home/db2inst1/Dropbox/test/input","/home/db2inst1/Dropbox/test/output","/home/db2inst1/Dropbox/test/config/docgeneratorconfig_liver.xml");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
  	}
	
	public void aperioXmlParser(String inFolder,String targetFolder,String filterObject, String filterlocation)
	{
		long startCurrentTime = 0;
	    long endCurrentTime = 0;
	    long totalTime = 0;
	    startCurrentTime = System.currentTimeMillis();
	    if (targetFolder == null || "".equals(targetFolder) )   	targetFolder = null;
	    
		AperioXMLParser parser = new AperioXMLParser(filterObject,filterlocation);
		File inputFile = new File(inFolder);
		if(inputFile.exists()==false)
		{
			System.out.println("input file or folder not exists!");
			return;
		}
		
	    if (inputFile.isDirectory()) parser.batchConvert(inFolder, targetFolder);			
	    else parser.parse(inFolder, targetFolder);
	    
	    endCurrentTime = System.currentTimeMillis();
	    
        totalTime = endCurrentTime - startCurrentTime;
        System.out.println("Total time (seconds):" + totalTime/1000.0);	 	
	}
	
	public void paisBoundary2PAIS(String inpath,String outpath) throws IOException, InterruptedException
	{
			new PAISBoundary2PAIS(inpath,outpath);
	}
	
	public void paisDocumentGenerator(String inputFilePath,String outputFilePath, String configFilePath,boolean dontzipresults) {
		
		File input = new File(inputFilePath);
		File output = new File(outputFilePath);
		
		FileFilter zipFilter = new FileNameExtensionFilter("zip File", "zip");
		FileFilter txtFilter = new FileNameExtensionFilter("txt File", "txt");
		System.out.println(inputFilePath+" "+outputFilePath+" "+configFilePath);
		if(!input.isDirectory()) {
			PAISDocumentGenerator PAISGenerator = new PAISDocumentGenerator(inputFilePath, outputFilePath, configFilePath);
			PAISGenerator.generateDocument(zipFilter.accept(output));
		} else {
			// Process all the files in the folder, including those in sub-folders.
			output.mkdirs();
			Queue<Map.Entry<File, String>> filesQueue = new LinkedList<Map.Entry<File, String>>();
			
			filesQueue.add(new AbstractMap.SimpleEntry<File, String>(input, File.separator));
			
			while(!filesQueue.isEmpty()) {
				Map.Entry<File, String> fileWithPath = filesQueue.remove(); 
				File file = fileWithPath.getKey();
				if(file.isDirectory()) {
					for(File f: file.listFiles()) {
						if(f.getAbsolutePath().equals(output.getAbsolutePath()))
							continue;
						String path = fileWithPath.getValue();
						if(!file.getAbsolutePath().equals(input.getAbsolutePath())) {
							path += file.getName() + File.separator;
						}
						filesQueue.add(new AbstractMap.SimpleEntry<File, String>(f, path));
					}
					continue;
				} else {
					try {
						
						if(!txtFilter.accept(file))
							continue;
						File createOutFolder = new File(output.getAbsolutePath() + fileWithPath.getValue());
						createOutFolder.mkdirs();
						String outputFilename = output.getAbsolutePath() + fileWithPath.getValue() + file.getName();
						// Change the extension of the file to xml
						String[] tokens = outputFilename.split(Pattern.quote("."));
						outputFilename = "";
						for(int i=0;i < (tokens.length - 1);i++) {
							outputFilename += tokens[i] + ".";
						}
						outputFilename += "xml";
						PAISDocumentGenerator PAISGenerator = new PAISDocumentGenerator(file.getAbsolutePath(), outputFilename , configFilePath);
						PAISGenerator.generateDocument(dontzipresults);
						
					} catch(Exception e) {
						System.err.print("Ignoring file: '"+file.getAbsolutePath()+"'. Exception was: '"+e.getMessage()+"'\n");
					}
				}
			}
		}
	}

}
