package edu.emory.cci.pais.dataloader.dataloadingmanager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingDeque;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

import edu.emory.cci.pais.dataloader.db2helper.DBConfig;
import edu.emory.cci.pais.dataloader.db2helper.PAISDBHelper;
import edu.emory.cci.pais.dataloader.db2helper.QueryGenerator;
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


public class DataLoadingManager extends Thread{
	
	/* Since one PAIS document may be divided into multiple ones, only one metadoc needs to be inserted. To judge if a PAIS metadoc is already there,
	 * we use a static tile_partition_map to keep the current pais UIDs -- first retrieved from metadoc table, then updated when there are new ones coming in.
	*/
	PAISDBHelper db = null;
	PAISParser parser = null;
	/* the blocking queue for producer/consumer*/
	private final BlockingQueue<ArrayList<Object>> queue;

	public int failedCount = 0;
	public int thread_num = 0;
	public static int fetchnum = 40;
	
	

	/*
	 * for the master thread to set & get and return an ArrayList<Object>
	 * and cast them to three data we need.
	*/
	ArrayList<Object> data = null;
	private static final String shutdown_req = "SHUTDOWN";
	private static final String done_req = "DONE";
	
	
	
	private ConcurrentHashMap<String, String> uidMap = new ConcurrentHashMap<String, String>();
	
	//private static String incompleteDocSql ="SELECT * FROM PAIS.STAGINGDOC WHERE STATUS='INCOMPLETE'";
	//private static String getExistingUidsSql = "SELECT uid from PAIS.METADOC";
	// CREATE TABLE PAIS.METADOC(UID VARCHAR(64) NOT NULL, XMLDOC XML, TIME TIMESTAMP, PRIMARY KEY(UID) );
	private static String insertMetaDocSql ="INSERT INTO PAIS.METADOC(UID, XMLDOC, INSERTTIME) VALUES(?, ?, CURRENT_TIMESTAMP) ";	 
	private static String preparedDocCompleteSql =  "UPDATE PAIS.STAGINGDOC SET UID = ?, TILENAME = ?, COMPLETE_COUNT = ?, STATUS = ?, COMPLETE_TIME = CURRENT_TIMESTAMP, " +
    "LOADING_TIME = ? WHERE SEQUENCE_NUMBER = ? AND STATUS = 'INCOMPLETE'" ;
	private static String insertLoadingLogSql = "INSERT INTO PAIS.LOADING_LOG(FAILURE_TIME, FILE_NAME, FAILURE_MESSAGE) VALUES(CURRENT_TIMESTAMP, ?, ?)";

	QueryGenerator queryGenerator = null;
	
	public DataLoadingManager(PAISDBHelper db2, PAISParser parser2, int numOfPartitions, String loadingConfigFile, BlockingQueue<ArrayList<Object>> queue2, ConcurrentHashMap<String, String> MuidMap, int numofthread){
		this.db = db2;
		this.parser = parser2;
		this.queue = queue2;
		this.thread_num = numofthread;
		this.uidMap = MuidMap;
		
		try {
			queryGenerator = QueryGenerator.getQueryGenerator();
		} catch(IllegalStateException e) {
			queryGenerator = QueryGenerator.getQueryGenerator(loadingConfigFile);
		}
		if(numOfPartitions > 0)
			queryGenerator.createPartitonKeyGenerator(db, numOfPartitions);
		

	}
	

	
	
	
	
	public void run(){
		// main codes for each thread
		System.out.println("Thread "+thread_num+" starting......");
		try {
			this.data = queue.take();
			while((String)data.get(0) != shutdown_req)
			{
			
				failedCount = this.batchLoad(data);
				this.data = queue.take();
			}
			
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}

			
		System.out.println("Thread "+thread_num+" ending......");
	}
	

	/** This function searches for all records with INCOMPLETE status in XMLDOC table and process each one of them. 
	 *  After a BLOB XML ZIP is retrieved, it is unzipped as a temporary XML file.
	 *  This temporary XML file will be processed by PAISParser, which will generate result such as uid, tilename, completed object count, and
	 *  metadoc XML file -- if there is no such file available for this PAIS UID. 
	 *  If this metadoc is new, it's inserted into the metadoc table.
	 *  The status of this job is then updated into the stagingdoc table. 
	 *  @return Number of documents failed to upload. 
	 * */
	
	public int batchLoad(ArrayList<Object> array){

		/*
		 * cast each element of ArrayList<Object> array into three different types.
		 * */
		
		String uid = null;
		String xmlFilename = null;
		String tileName = null;		
		
		String fileName = (String)array.get(0);
		xmlFilename = (String)array.get(1);
		Integer seqNum = (Integer)array.get(2);
		
		
		//System.out.println("SeqNum is: "+ seqNum);
				//Unzip and generate the XML file 
		
				LoadResult result = null;
				uid = null;
				tileName = null;
				boolean flag = false;
				try {
					//Parse the XML file and load it into DB
					result = load(xmlFilename, true);
					
					//Get the UID and check if this UID is already in the uidMap for the METADOC table
					if (result != null){
						try {
							setJobCompletionStatus(db, seqNum, "COMPLETE", result);
						}catch(SQLException e) {
							System.out.println("WARNING: the STAGINGDOC table was not updated to 'COMPLETE' for file: '"+fileName+"' but the file was successfuly processed.");
						}
						//System.out.println(seqNum +  " job successful:" + jobSuccess);
						uid = result.getUid();					
						tileName = result.getTileName();
						System.out.println("Tile " + tileName + " successfully uploaded.");
						
						synchronized (uidMap) {	
							
							flag = uidMap.containsKey(uid);
							
							if ( ! flag ){
								System.out.println("Inserting a UID of " + uid + " to MetaDoc table");
								
								/*Since ResultSet is not thread-safe, pass string to method here. */
								insertDocStatus(uid,xmlFilename);
								loadMeta(uid);						
								uidMap.put(uid, uid);
							}
						}
					}
					else {
						throw new Exception("LoadResult is null.");
					}
				} catch(Exception e) {
					try {
						setJobCompletionStatus(db, seqNum, "FAILED", result);
					} catch(SQLException e1) {
						System.out.println("WARNING: the STAGINGDOC table was not updated to 'FAILED' for file: "+fileName);
					}
					failedCount++;
					if(result != null)
						tileName = result.getTileName();
					System.out.println("Failed upload for tile " + tileName+". Exception was: "+e.getMessage());
					e.printStackTrace();
					String failureMessage = "Unknown failure.";
					if(e.getMessage() != null)
						failureMessage = e.getMessage();
					try {
						insertLog(fileName, failureMessage);
					}catch(SQLException e1) {
						System.out.println("Failed to log the failure on the database: "+e1.getMessage());
					}
				} finally {
					File xmlFile = new File(xmlFilename);
					xmlFile.delete();
					File metaFilename = new File(result.getMetaFilename());
					metaFilename.delete();
				}
							
		System.out.println("Thread " + thread_num + " has " + failedCount + " faults");
		return failedCount;
	}
	
	public LoadResult load(String xmlFile, boolean write){
		LoadResult value = parse(xmlFile, write);
		return value;		
	}
	
	public LoadResult parse(String xmlFile, boolean write){
		LoadResult value = new LoadResult();
		//boolean write = true;
		boolean load = true;
		//PAISParser parser = new PAISParser();
		value = parser.parse(xmlFile, this.db, write, load, true);
		return value;
	}
	
	/* Set complete status and update complete count and timestamp for the current job (tile loading). */
	public void setJobCompletionStatus(PAISDBHelper myDb, int seqNum, String completionStatus, LoadResult result) throws SQLException{
		PreparedStatement pstmt = myDb.getCompleteJobPstmt(preparedDocCompleteSql);
		if(result != null) {
			pstmt.setString(1, result.getUid() );
			pstmt.setString(2, result.getTileName() );
			pstmt.setInt(3, result.getCount() );
			pstmt.setDouble(5, result.getLoadingTime() );
		} else {
			pstmt.setNull(1, java.sql.Types.VARCHAR);
			pstmt.setNull(2, java.sql.Types.VARCHAR);
			pstmt.setNull(3, java.sql.Types.INTEGER);
			pstmt.setNull(5, java.sql.Types.DECIMAL);
		}
		pstmt.setInt(6, seqNum);
		pstmt.setString(4, completionStatus);
		pstmt.executeUpdate();	
		myDb.getDBConnection().commit();			
	}		

	/* For each completed pais XML document, update the job status - a job can be a fragment PAIS document. */
	public boolean insertDocStatus(String uid, String xmlFilename){
		PreparedStatement pstmt = db.getPreparedStatement(insertMetaDocSql);
		try {
//			String xmlFilename = value.getMetaFilename();
			File xmlFile = new File(xmlFilename);
			pstmt.setString(1, uid);

					
			InputStream inXml =  new FileInputStream(xmlFile);	

			
			pstmt.setBinaryStream(2, inXml, (int) xmlFile.length()); 
			pstmt.execute();
			db.commit();
			pstmt.close();
			db.deleteFile(xmlFilename);
		} catch (Exception e) {
			e.printStackTrace();
			try {
				if (pstmt != null) pstmt.close();
			} catch (SQLException e1) {
			}
			return false;
		}
		return true;
	}
	
	public void insertLog(String fileName, String message) throws SQLException {
		PreparedStatement pstmt = db.getPreparedStatement(insertLoadingLogSql);
		try {
			pstmt.setString(1, fileName);
			pstmt.setString(2, message);
			
			pstmt.execute();
			db.commit();
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			try {
				pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public boolean loadMeta(String uid){
		ArrayList<String> queries = MetaQueries.getAllQueries(uid);
		PreparedStatement pstmt = null; 

		Connection con = db.getDBConnection();
		for (int i = 0; i < queries.size(); i++ ){ //Process each mapping SQL 		
			//System.out.println(queries.get(i).trim());
			try{
				pstmt = con.prepareStatement(queries.get(i));
				pstmt.executeUpdate();
				pstmt.close();
			} catch (Exception e) {
				System.out.println("Failed metadoc mapping for uid:  " + uid);
				System.out.println(queries.get(i).trim());
				try {if (pstmt != null) pstmt.close(); }
				catch (SQLException e1) { System.out.println("Closing statement failed."); } 
				e.printStackTrace();
				//return false;
			}
		}
		try {
			con.commit();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
		
		
	}
/*
 * By Alex
 * This function has already been moved to the MasterThread class, for master thread to set/get shared uidMap and pass it to every slave thread.
 * 
 * 
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
*/
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

	public static void main(String[] args) throws SQLException {
		
		
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
		
		PAISParser parser = new PAISParser(tmpconfigfile);	
		DataLoadingManager loader = null;
		HashMap<String, String> uidMap = new HashMap<String, String>();
		ConcurrentHashMap<String, String> conUidMap;

        BlockingQueue<ArrayList<Object>> queue = new LinkedBlockingDeque<ArrayList<Object>>();
		/*Start the producer*/
		MasterThread producer = new MasterThread(db, parser, queue, 1,fetchnum,loadingconfig);
		producer.setUidMap();
		uidMap = producer.getUidMap();
        conUidMap = new ConcurrentHashMap<String, String>(uidMap);
        producer.run();
		/*Start the consumer*/
		
		loader = new DataLoadingManager(db, parser, 0, tmpconfigfile, queue, conUidMap, 1);
		loader.run();
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
	
	
	
	public static void main1(String[] args) throws SQLException {
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
		
		CommandLineParser CLIparser = new GnuParser();
		HelpFormatter formatter = new HelpFormatter();
		CommandLine line = null;

		int numOfThreads = 0;	
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
			
		} catch(NumberFormatException e) {
			System.err.println("The partition number must be an integer number.\n");
			formatter.printHelp("DataLoadingManager", options, true);
			System.exit(1);
		} catch(org.apache.commons.cli.ParseException e) {
			formatter.printHelp("DataLoadingManager", options, true);
			System.exit(1);
		}	
		
		
		
		
		
		
		
/*		String metaFilename = "C:\\temp\\validation\\oligoIII.2.ndpi-0000073728-0000024576.tif.grid4.mat_0.xml";
		String uid = "oligoIII.2_20x_20x_NS-MORPH_2";*/
		long startTime = 0;
		long endTime = 0;
		int num = 0;
		
		startTime = System.currentTimeMillis() ;	//Initialization starts

		PAISDBHelper db = new PAISDBHelper(new DBConfig(line.getOptionValue("dbConfigFile")));
		PAISParser parser = new PAISParser(line.getOptionValue("loadingConfigFile"));
		
		
		endTime = System.currentTimeMillis();
		System.out.println("DB initization time = " + (endTime - startTime)/1000.0 + " seconds." );	
		

		startTime = System.currentTimeMillis() ; 		//Multi-thread starts
		
		
		
		DataLoadingManager loader = null;
		HashMap<String, String> uidMap = new HashMap<String, String>();
		ConcurrentHashMap<String, String> conUidMap;

		if(numOfThreads == 1)
		{

			BlockingQueue<ArrayList<Object>> queue = new LinkedBlockingDeque<ArrayList<Object>>();
			/*Start the producer*/
			db = new PAISDBHelper(new DBConfig(line.getOptionValue("dbConfigFile")));
			parser = new PAISParser(line.getOptionValue("loadingConfigFile"));
			MasterThread producer = new MasterThread(db, parser, queue, numOfThreads,fetchnum,"");
			producer.setUidMap();
			uidMap = producer.getUidMap();

			conUidMap = new ConcurrentHashMap<String, String>(uidMap);
            
			producer.run();
			/*Start the consumer*/
			for(num=0;num<numOfThreads;num++)
			{
				db = new PAISDBHelper(new DBConfig(line.getOptionValue("dbConfigFile")));
				/*Check if need to calculate the centroid of polygon*/
				if (CentroidFlag == 1)
					db.setCentroidCalulationFlag();
				parser = new PAISParser(line.getOptionValue("loadingConfigFile"));
				loader = new DataLoadingManager(db, parser, numOfPartitions, line.getOptionValue("loadingConfigFile"), queue, conUidMap, num+1);
				loader.run();
			}
		}
		else {	
			
			/*produce/consume data in FIFO order, until there are END Singles of the number of partitions/consumers*/
			BlockingQueue<ArrayList<Object>> queue = new ArrayBlockingQueue<ArrayList<Object>>(50, true);
			
			/*Start the Master Thread of producer*/
			db = new PAISDBHelper(new DBConfig(line.getOptionValue("dbConfigFile")));			
			parser = new PAISParser(line.getOptionValue("loadingConfigFile"));
			MasterThread producer = new MasterThread(db, parser, queue, numOfThreads,fetchnum,"");
			producer.setUidMap();
			uidMap = producer.getUidMap();
			conUidMap = new ConcurrentHashMap<String, String>(uidMap);
			
			producer.start();
			
			/*Start the slave threads of consumers*/
			/*give uidMap to every slave thread and synchronize it*/
			for(num=0;num<numOfThreads;num++)
			{
				db = new PAISDBHelper(new DBConfig(line.getOptionValue("dbConfigFile")));
				/*Check if need to calculate the centroid of polygon*/
				if (CentroidFlag == 1)
					db.setCentroidCalulationFlag();
				parser = new PAISParser(line.getOptionValue("loadingConfigFile"));
				loader = new DataLoadingManager(db, parser, numOfPartitions, line.getOptionValue("loadingConfigFile"),queue, conUidMap, num+1);
				loader.start();
			}
		}
		

	}
}


