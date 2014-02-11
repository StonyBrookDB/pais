package edu.emory.cci.pais.dataloader.dataloadingmanager;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import edu.emory.cci.pais.dataloader.db2helper.PAISDBHelper;
import edu.emory.cci.pais.dataloader.db2helper.QueryGenerator;

public class Consumer extends Thread{
	
	PAISDBHelper db = null;
	PAISParser parser = null;

	public int failedCount = 0;
	public int thread_num = 0;
	QueryGenerator queryGenerator = null;
	
	private static String insertMetaDocSql ="INSERT INTO PAIS.METADOC(UID, XMLDOC, INSERTTIME) VALUES(?, ?, CURRENT_TIMESTAMP) ";	 
	private static String preparedDocCompleteSql =  "UPDATE PAIS.STAGINGDOC SET UID = ?, TILENAME = ?, COMPLETE_COUNT = ?, STATUS = ?, COMPLETE_TIME = CURRENT_TIMESTAMP, " +
                                                    "LOADING_TIME = ? WHERE SEQUENCE_NUMBER = ? AND STATUS = 'INCOMPLETE'" ;
	private static String insertLoadingLogSql = "INSERT INTO PAIS.LOADING_LOG(FAILURE_TIME, FILE_NAME, FAILURE_MESSAGE) VALUES(CURRENT_TIMESTAMP, ?, ?)";

	
	
	public Consumer(PAISDBHelper db2, int numOfPartitions, String loadingConfigFile, int threadid){
		this.db = db2;
		this.parser = new PAISParser(loadingConfigFile);
		this.thread_num = threadid;
		
		try {
			queryGenerator = QueryGenerator.getQueryGenerator();
		} catch(IllegalStateException e) {
			queryGenerator = QueryGenerator.getQueryGenerator(loadingConfigFile);
		}
		if(numOfPartitions > 0)
			queryGenerator.createPartitonKeyGenerator(db, numOfPartitions);
		

	}
	
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
						
				synchronized (DataLoadingManager.uidMap) {	
                    flag = DataLoadingManager.uidMap.containsKey(uid);
                    if(!flag)
                    {
                    	System.out.println("Inserting a UID of " + uid + " to MetaDoc table");
    					insertDocStatus(uid,xmlFilename);
    					loadMeta(uid);	
                    	DataLoadingManager.uidMap.put(uid, uid);
                    }
				}

			 }
			 else {
				throw new Exception("LoadResult is null.");
			 }
		 }catch(Exception e) {
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
				if(xmlFile.exists())
				   xmlFile.delete();
				File metaFilename = new File(result.getMetaFilename());
				metaFilename.delete();
		}
		if(failedCount!=0)
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
	

	
	
	
	
	public void run(){
		// main codes for each thread
		System.out.println("Thread "+thread_num+" starting......");
		ArrayList<Object> data;

		while(true)
		{
			synchronized(DataLoadingManager.queue)
			{
			  data = DataLoadingManager.queue.poll();
			}
			if(data==null)
			{
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else if((String)data.get(0)==DataLoadingManager.shutdown_req)
			{
				synchronized(DataLoadingManager.queue)
				{
					DataLoadingManager.queue.add(data);
				}
				break;
			}
			else{
				System.out.println("thread "+thread_num+ " is working on "+(String)data.get(0));
				failedCount = this.batchLoad(data);
				
			}
   
		}
		synchronized(DataLoadingManager.numOfThreads)
		{
			DataLoadingManager.numOfThreads--;
		}
		System.out.println("Thread "+thread_num+" ending......");
	}
}
