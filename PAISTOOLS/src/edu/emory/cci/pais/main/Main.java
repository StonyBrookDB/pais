package edu.emory.cci.pais.main;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.JFrame;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

import edu.emory.cci.pais.controller.ConvertController;
import edu.emory.cci.pais.dataloader.db2helper.DBConfig;
import edu.emory.cci.pais.dataloader.db2helper.PAISDBHelper;
import edu.emory.cci.pais.dataloader.documentuploader.DocumentUploader;
import edu.emory.cci.pais.view.MainPanel;

public class Main {
	//the main window
public static void  main(String[] args){
	    if(args.length==0)
	    {
		JFrame jf = new JFrame("PAIS");
		jf.setBounds(0, 0, 660, 600);
		jf.setLocationRelativeTo(null);
		jf.add(new MainPanel());
		jf.setVisible(true);
		jf.addWindowListener(new WindowAdapter() {
		      public void windowClosing(WindowEvent e) {
		        System.exit(0);
		      }
		    });
       }
       else// command line
       {
    	String work = args[0];
    	String otherargs[] = new String[args.length-1];
    	for(int i=0;i<otherargs.length;i++)
    	{
    		otherargs[i]=args[i+1];
    	}
   		Option help;
   		Options options;
   		CommandLineParser CLIparser;
   		HelpFormatter formatter=new HelpFormatter();
   		CommandLine line ;
   		
   		
   		if(work.equalsIgnoreCase("converter"))
   		{
   			help = new Option("h","help",false,"display this help and exit.");
   			help.setRequired(false);
   			Option inputtype = new Option("it","inputtype",false,"input file type, aperio/fixed/unfixed");
   			inputtype.setRequired(false);
   			inputtype.setArgName("inputtype");
   			
   			Option zip = new Option("z","zip",false,"zip the output file or not");
   			zip.setRequired(false);
   			zip.setArgName("outputtype");
   			
   			Option inputpath = new Option("i","inputpath",false,"input directory or file path");
   			inputpath.setRequired(true);
   			inputpath.setArgName("inputpath");
   			
   			Option outputpath = new Option("o","outputpath",false,"output directory or file path");
   			outputpath.setRequired(true);
   			outputpath.setArgName("outputpath");
   			
   			Option config = new Option("c","config",false,"configuration file path");
   			config.setRequired(true);
   			config.setArgName("config");
   			
   			options = new Options();
   			options.addOption(help);
   			options.addOption(inputtype);
   			options.addOption(zip);
   			options.addOption(inputpath);
   			options.addOption(outputpath);
   			options.addOption(config);

   			CLIparser = new GnuParser();
   	   	    line = null;
   	   		try {
   	   			line = CLIparser.parse(options, args);
   	   			if(line.hasOption("h")) {
   	   				formatter.printHelp("converter", options, true);
   	   				System.exit(0);
   	   			}
   	   		} catch(org.apache.commons.cli.ParseException e) {
   	   			formatter.printHelp("converter", options, true);
   	   			System.exit(1);
   	   		}
   	   		String input = line.getOptionValue("inputpath");
            String output = line.getOptionValue("outputpath");
            String intype = line.getOptionValue("inputtype");
            String zipornot = line.getOptionValue("zip");
            String configfile = line.getOptionValue("config");
            
            if(intype==null||intype.equalsIgnoreCase(""))
            	intype = "aperio";
            if(zipornot==null)
            	zipornot = "unzipped";
            else
            	zipornot = "zipped";
            
            System.out.println("start convert files....");
    	    try {
				new ConvertController().convert(intype,zipornot,input,output,configfile);
				System.out.println("convert successfully!");
			} catch (IOException | InterruptedException e) {
				System.out.println("convert failed!");
				e.printStackTrace();
			}   
   		}
   		else if(work.equalsIgnoreCase("uploader"))
   		{
   			help = new Option("h", "help", false, "display this help and exit.");
   			help.setRequired(false);
   			Option folderType = new Option("ft", "folderType", true, "the type of the Folder. It can be either 'slide' or 'collection'. " +
   					"If it is 'slide', all the zip files contained in the folder will be loaded. If it is 'collection', all the zip " +
   					"files contained in the immediate child subfolders will be uploaded, files contained directly in the folder will be ignored. " +
   					"Notice that both methods don't add zip files in subfolders recursively.");
   			folderType.setRequired(true);
   			folderType.setArgName("folderType");
   			Option folder = new Option("f", "folder", true, "in the case 'slide', folder containing the zipped documents. In the case of 'collection' " +
   					"it's the folder containing the subfolders with zipped documents.");
   			folder.setRequired(true);
   			folder.setArgName("folder");
   			Option dbConfigFile = new Option("dbc", "dbConfigFile", true, "xml file with the configuration of the database.");
   			dbConfigFile.setRequired(true);
   			dbConfigFile.setArgName("dbConfigFile");
   			
   			options = new Options();
   			options.addOption(help);
   			options.addOption(folderType);
   			options.addOption(folder);
   			options.addOption(dbConfigFile);
   			
   			CLIparser = new GnuParser();
   			line = null;
   			try {
   				line = CLIparser.parse(options, args);
   				if(line.hasOption("h")) {
   					formatter.printHelp("uploader", options, true);
   					System.exit(0);
   				}
   			} catch(org.apache.commons.cli.ParseException e) {
   				e.printStackTrace();
   				formatter.printHelp("uploader", options, true);
   				System.exit(1);
   			}	
   			
   			long startCurrentTime = 0;
   	        long endCurrentTime = 0;
   	        long totalTime = 0;
   	        startCurrentTime = System.currentTimeMillis();
   	        
   			PAISDBHelper db = new PAISDBHelper(new DBConfig(new File(line.getOptionValue("dbConfigFile"))));
   	        
   			DocumentUploader uploader = new DocumentUploader(db,"PP");

   			String folderString = line.getOptionValue("folder");
   			if (line.getOptionValue("folderType").trim().toLowerCase().equals("slide")){
   				uploader.batchDocUpload(folderString);
   			} 
   			else if(line.getOptionValue("folderType").trim().toLowerCase().equals("collection")){
   				uploader.uploadDataSet(folderString);				
   			}
   			else {
   				formatter.printHelp("PAISDocumentUploader", options, true);
   				System.exit(1);
   			}

   			endCurrentTime = System.currentTimeMillis();
   	        totalTime = endCurrentTime - startCurrentTime;
   	        System.out.println("Total time (seconds):" + totalTime/1000.0);	 	
	
   		}
   		else
   		{
   		
   			System.out.println("the first argument must be the work type\n e.g. java -jar paistools.jar uploader/converter <otherargs>");
			System.exit(0);
   			
   		}

       }
}
       
}
