package edu.emory.cci.pais.dataloader.dataloadingmanager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

import edu.emory.cci.pais.dataloader.db2helper.DBConfig;
import edu.emory.cci.pais.dataloader.db2helper.PAISDBHelper;
import edu.emory.cci.pais.util.TengUtils;

/**
 * @author Fusheng Wang, Center for Comprehensive Informatics, Emory University
 * @version 1.0
 * This class provides workflow management on loading data. The steps are as follows:
 * 1. XML ZIP files are stored in a folder organized by document uid on a client machine;
 * 2. A text file containing a list of all folders;
 * 3. DataLoader will load all zipped XML (one zip per tile) into PAIS.XMLDOC table;
 * 4. For each tile XML ZIP with INCOMPLETE status:
 *     5. Unzip it as a temp file;
 *     6. Use PAISParser to parse it and load it into DB. Meanwhile, it generates information:
 *        PAIS_UID, TILENAME, NUM_OF_PROCESSED_OBJECTS, FILENAME
 *     7. Call spatial data converter to generate spatial data;
 *     8. Update PAIS.XMLDOC with STATUS as COMPLETE and OBJECT_COUNT;
 *     9. Update PAIS.METADOC(UID, XMLDOC) with header;      
 */  


public class DataLoadingManager{
	
	/* Since one PAIS document may be divided into multiple ones, only one metadoc needs to be inserted. To judge if a PAIS metadoc is already there,
	 * we use a static tile_partition_map to keep the current pais UIDs -- first retrieved from metadoc table, then updated when there are new ones coming in.
	*/
	
	/* the blocking queue for producer/consumer*/
	public static Queue<ArrayList<Object>> queue = new LinkedList<ArrayList<Object>>();;
	public static HashMap<String, String> uidMap = new HashMap<String, String>();
	
	public static int fetchnum = 40;
	public static final String shutdown_req = "SHUTDOWN";
	public static final String done_req = "DONE";
	public static Integer numOfThreads = 1;
	private static String getExistingUidsSql = "SELECT uid from PAIS.METADOC";
	public static Integer maxUnzipped = 10;
	public static String cachePath = System.getProperty("java.io.tmpdir");

	
	
	//get this shared UID map and pass it to every slave threads.
	//Retrieve PAIS UID already inserted in metadoc table
	public static HashMap <String, String> getExistingUidMap(PAISDBHelper db){
		HashMap <String, String> uidMap = new HashMap <String, String>();
		ResultSet rs = null;
		try {
			rs = db.getSqlQueryResult(getExistingUidsSql);

			if(rs == null) return null;
			while (rs.next()){
				String uid = rs.getString(1);
				uidMap.put(uid, uid);
			}
		} catch (Exception e) {
			e.printStackTrace();
			try {
				if (rs != null) rs.close();
				Statement stmt = rs.getStatement();
				if (stmt != null) stmt.close();
			}
			catch (SQLException e1) { System.out.println("Closing result set failed."); } 
			return null;
		}
		try {
			if(rs != null) {
				rs.close();
				Statement stmt = rs.getStatement();
				if (stmt != null) stmt.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return uidMap;
	}

/*for the master thread, set the shared uidmap*/
public static void initUidMap(PAISDBHelper db) {
	DataLoadingManager.uidMap = getExistingUidMap(db);	
}

	/** This function searches for all records with INCOMPLETE status in XMLDOC table and process each one of them. 
	 *  After a BLOB XML ZIP is retrieved, it is unzipped as a temporary XML file.
	 *  This temporary XML file will be processed by PAISParser, which will generate result such as uid, tilename, completed object count, and
	 *  metadoc XML file -- if there is no such file available for this PAIS UID. 
	 *  If this metadoc is new, it's inserted into the metadoc table.
	 *  The status of this job is then updated into the stagingdoc table. 
	 *  @return Number of documents failed to upload. 
	 * */
	
	
	public static void main(String[] args){
		loadmanagermain(args);

	}
	
	public static void loadmanagermain(String[] args){
		Option help = new Option("h", "help", false, "display this help and exit.");
		help.setRequired(false);
		Option loadingConfigFile = new Option("lc", "loadingConfigFile", true, "xml file with the data loading configuration.");
		loadingConfigFile.setRequired(true);
		loadingConfigFile.setArgName("loadingConfigFile");
		Option dbConfigFile = new Option("dbc", "dbConfigFile", true, "xml file with the configuration of the database.");
		dbConfigFile.setRequired(true);
		dbConfigFile.setArgName("dbConfigFile");
		Option numOfPartitionsOption = new Option("p", "numberOfPartitions", true, "the number of partitions, if using database partitioning. If the db is not partitioned, this option can be omitted or set to 0.");
		numOfPartitionsOption.setRequired(false);
		numOfPartitionsOption.setArgName("numberOfPartitions");
		
		//by Zhengwen to use Multi-Threading load
		Option numOfThreadOption = new Option("t", "numberOfThreads", true, "number of threads");
		numOfThreadOption.setRequired(false);
		numOfThreadOption.setArgName("number of threads");
		
		Option cachePathOption = new Option("cp", "pathofCache", true, "the path of temporary folder you want to put the cache files to, default /tmp");
		cachePathOption.setRequired(false);
		cachePathOption.setArgName("path of cache");
		
		Option cacheSize = new Option("cs", "sizeofCache", true, "number of xml files be cached in the temporary folder, default 5");
		cacheSize.setRequired(false);
		cacheSize.setArgName("size of cache");
		//by Zhengwen to calculate the centroid of a polygon first.
		// MARKUP_POLYGON table needs to append 2 attributes -- CENTROID_X, CENTROID_Y (Modified in the "create_tables_dpf_withCentroid.sql" SQL script).
		
		Option CentFlag = new Option("c", "CentroidCalculationFlag", true, "Flag to calculate Centroid");
		CentFlag.setRequired(false);
		CentFlag.setArgName("Flag to calculate Centroid");

		
		
		Options options = new Options();
		options.addOption(help);
		options.addOption(loadingConfigFile);
		options.addOption(dbConfigFile);
		options.addOption(numOfPartitionsOption);	
		//Zhengwen
		options.addOption(numOfThreadOption);
		options.addOption(CentFlag);
		options.addOption(cachePathOption);
		options.addOption(cacheSize);
		
		CommandLineParser CLIparser = new GnuParser();
		HelpFormatter formatter = new HelpFormatter();
		CommandLine line = null;

		int numOfPartitions = 0;
		int CentroidFlag = 0;
		
		try {
			line = CLIparser.parse(options, args);
			if(line.hasOption("h")) {
				formatter.printHelp("DataLoadingManager", options, true);
				System.exit(0);
			}
			if(line.hasOption("p")) {
				numOfPartitions = Integer.parseInt(line.getOptionValue("numberOfPartitions"));
				System.out.println("The number of Partitions is : " + numOfPartitions);
			} else {
				numOfPartitions = 0;
			}
			if(line.hasOption("t")){
				numOfThreads = Integer.parseInt(line.getOptionValue("numberOfThreads"));
				System.out.println("The number of Threads is: " + numOfThreads);				
			} else {
				numOfThreads = 1;
			}
			if(line.hasOption("c")){
				CentroidFlag = Integer.parseInt(line.getOptionValue("CentroidCalculationFlag"));
				if (CentroidFlag == 1)
					System.out.println("Calculation Centroids of polygons ... " );				
			} else {
				CentroidFlag = 0;
			}
			if(line.hasOption("cp"))
			{
				String tmppath = line.getOptionValue("pathofCache");
				if(cachePath==null)
					cachePath="/tmp";
				if(tmppath!=null)
					cachePath = tmppath;
			}
			if(line.hasOption("cs"))
			{
				try
				{
				int size = Integer.parseInt(line.getOptionValue("sizeofCache"));
				if(size>0)
					maxUnzipped = size;
				}
				catch(Exception e)
				{
				}
			}

			
		} catch(NumberFormatException e) {
			System.err.println("The partition number must be an integer number.\n");
			formatter.printHelp("DataLoadingManager", options, true);
			System.exit(1);
		} catch(org.apache.commons.cli.ParseException e) {
			formatter.printHelp("DataLoadingManager", options, true);
			System.exit(1);
		}	

		long startTime = 0;
		long endTime = 0;
		int num = 0;
		

		DBConfig dbconfig =new DBConfig(line.getOptionValue("dbConfigFile"));
		String loadingconfig = line.getOptionValue("loadingConfigFile");		
		startTime = System.currentTimeMillis() ; 		
		/*Start the Producer*/
		PAISDBHelper db = new PAISDBHelper(dbconfig);
		initUidMap(db);	
		Producer producer = new Producer(db,0,loadingconfig);
		producer.start();
			
		/*Start the slave threads of consumers*/
		for(num=0;num<numOfThreads;num++)
		{
				PAISDBHelper consumerdb = new PAISDBHelper(dbconfig);
				/*Check if need to calculate the centroid of polygon*/
				if (CentroidFlag == 1)
					consumerdb.setCentroidCalulationFlag();
				
				new Consumer(consumerdb, numOfPartitions, loadingconfig, num+1).start();
		}
		
		while(true)
		{
			int threadnum = 0;
			synchronized(numOfThreads)
			{
				threadnum = numOfThreads;
			}
			if(threadnum==0)
				break;
			else{
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		endTime = System.currentTimeMillis();
		
		System.out.println("Totally takes： " + (endTime - startTime)/1000.0 + " seconds." );

	}
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	
	public static boolean getLCFilebyName(String filePath,String lcname,PAISDBHelper db){  
		Writer writer = null;  
	    try{  
	    ResultSet rs = db.getSqlQueryResult("select f.FEATURENAME,f.type from pais.templatefeaturesrl t,pais.features f where t.TEMPLATENAME='"+lcname+"' and t.featurename=f.featurename");
	    HashMap<String,String[]> featuresMap = new HashMap<String,String[]>();
	    while(rs.next())
	    {
	    	featuresMap.put(rs.getString(1), new String[]{rs.getString(2),"selected"});
	    }
	   
	    File file = new File(filePath);  
	    writer = new OutputStreamWriter(new FileOutputStream(file));  
	    writer.write( TengUtils.generateloadingConfigXML(lcname, featuresMap) );  
	    }catch(Exception e)
	    {
	    	e.printStackTrace();
	    	return false;
	    }
	    finally{  
	       if(writer!=null){  
	                try {
						writer.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						return false;
					}  
	            }      
	    }  
	    return true; 
	}  
		public static void main2(String[] args) throws SQLException {
			
			
			Option help = new Option("h", "help", false, "display this help and exit.");
			help.setRequired(false);
			
			Option dbConfigFile = new Option("dbc", "dbConfigFile", true, "xml file with the configuration of the database.");
			dbConfigFile.setRequired(true);
			dbConfigFile.setArgName("dbConfigFile");
			Options options = new Options();
			options.addOption(help);
			options.addOption(dbConfigFile);
				
			CommandLineParser CLIparser = new GnuParser();
	   		HelpFormatter formatter = new HelpFormatter();
	   		CommandLine line = null;
	   		try {
	   			line = CLIparser.parse(options, args);
	   			if(line.hasOption("h")) {
	   				formatter.printHelp("PAIS tools", options, true);
	   				System.exit(0);
	   			}
	   		} catch(org.apache.commons.cli.ParseException e) {
	   			formatter.printHelp("PAIS tools", options, true);
	   			System.exit(1);
	   		}
			PAISDBHelper db = new PAISDBHelper(new DBConfig(line.getOptionValue("dbConfigFile")));
			
			
	        //each time, program fetch one line whose status='INCOMPLETE'  and read its loading configuration file name, then read this file out and fetch fetchnum
			//rows of data to unzip and analyze
			boolean secondcheck = false;
			while(true)
			{
			//each time start one batch of data, set lastheartbeat to current time
			heartbeat(db);
			String tmpconfigfile = System.getProperty("java.io.tmpdir");
			if(tmpconfigfile.endsWith(File.separator))
				tmpconfigfile=tmpconfigfile+"tmpconfigfile";
			else
				tmpconfigfile=tmpconfigfile+File.separator+"tmpconfigfile";
			ResultSet loadconfigrs = db.getPreparedStatement("select loadingconfig from pais.stagingdoc where status='INCOMPLETE' fetch first row only").executeQuery();
			String loadingconfig = null;
			if(loadconfigrs.next())//there is still some incomplete file
			{
				secondcheck=false;
				loadingconfig = loadconfigrs.getString(1);
				if(getLCFilebyName(tmpconfigfile,loadingconfig,db)==false)//get loading configure file from table template, if failed break;
					break;
			}
			else 
				{
				   if(secondcheck)//have waited 5 second and there is no new input file break
				       break;
				   else//else wait for 5 second and make a second check
				   {
					   secondcheck=true;
					   try {
						heartbeat(db);
						Thread.sleep(5000);
						continue;
					    } catch (InterruptedException e) {
					    	continue;
					    }
				   }
				}
			
			initUidMap(db);
			/*Start the producer*/
			Producer producer = new Producer(db,fetchnum,loadingconfig);
	        producer.run();
			/*Start the consumer*/
			
			Consumer consumer = new Consumer(db, 0, tmpconfigfile, 1);
			consumer.run();
			new File(tmpconfigfile).delete();//delete the tempt configuration file
		    
			}
			//set workstatus to 1, means there is no data loading manager is running
			try{
			db.getPreparedStatement("update pais.DLMSTATUS set workstatus=1").execute();
			}catch(Exception e)
			{
				System.out.println("reset work status failed!");
			}
		}
		
		public static void heartbeat(PAISDBHelper db)
		{
			try{
				db.getPreparedStatement("update pais.DLMSTATUS set lastheartbeat=current_timestamp").execute();
				System.out.println("heart beat successfully!");
				}catch(Exception e)
				{
					System.out.println("update heart beat failed!");
				}
		}

}


