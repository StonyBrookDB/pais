package edu.emory.cci.pais.dataloader.dataloadingmanager;

import java.io.File;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.zip.ZipInputStream;

import edu.emory.cci.pais.dataloader.db2helper.PAISDBHelper;
import edu.emory.cci.pais.dataloader.db2helper.DBConfig;
import edu.emory.cci.pais.dataloader.db2helper.QueryGenerator;


/*
 * This is the Producer-Consumer pattern of Java multi-thread
 * 
 * Here we have only one Producer thread, get ResultSets from SQL statement and cast them into a object array list and put it to the blocking queue
 * And consumers will take an object array list from the queue and cast it separately.*/


public class MasterThread extends Thread{

	public BlockingQueue<ArrayList<Object>> queue;
	private static String incompleteDocSql ="SELECT * FROM PAIS.STAGINGDOC WHERE STATUS='INCOMPLETE' AND LOADINGCONFIG=?";
	private static String getExistingUidsSql = "SELECT uid from PAIS.METADOC";
	private int fetchnum;
	PAISDBHelper db = null;
	PAISParser parser = null;
	ResultSet rs = null;
	public HashMap<String, String> uidMap = new HashMap<String, String>();
	
	
	/*
	 * data we want to produce/consume.
	 * cast Object into them separately.
	 * */

	String xmlFilename = null;
	public String fileName = null;
	public Integer seqNum = 0;
	int numofthread = 0;
	String loadingconfig ;
	
	private static final String SHUTDOWN_REQ = "SHUTDOWN";

 
	
	public MasterThread(PAISDBHelper db, PAISParser parser, BlockingQueue<ArrayList<Object>> queue, int num,int fetchnum,String loadingconfig)
	{
		this.queue = queue;
		this.db = db;
		this.parser = parser;
		this.numofthread = num;
		this.fetchnum = fetchnum;
		this.loadingconfig = loadingconfig;
	}
	
	
	/*for the master thread, set the shared uidmap*/
	public void setUidMap() {
		HashMap<String, String> map = getExistingUidMap(db);
		if (map != null) uidMap.putAll( map );		
	}
	
	/*for the master thread, get the shared uidmap and pass it to every slave threads*/
	public HashMap<String, String> getUidMap() {
		if(uidMap == null)
			uidMap.put(null, null);
		return uidMap;		
	}
	
	
	
	//get this shared UID map and pass it to every slave threads.
	//Retrieve PAIS UID already inserted in metadoc table
	public static HashMap <String, String> getExistingUidMap(PAISDBHelper db){
		HashMap <String, String> uidMap = new HashMap <String, String>();
		ResultSet rs = null;
		try {
			db.getSqlQueryResult(getExistingUidsSql);
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

	/*thread entrance
	 * produce data to the queue*/
	public void run() {

		int i=0;
		ZipInputStream zipIn = null;
		ArrayList<Object> data = null;
			try {
				this.rs = setIncompletedResultSet();

				while(rs.next())
				{
					data = new ArrayList<Object>();
					
					zipIn = new ZipInputStream ( rs.getBinaryStream("BLOB") );
					xmlFilename = db.getXmlDocFile(zipIn);
					
					seqNum = rs.getInt("SEQUENCE_NUMBER");
					fileName = rs.getString("FILE_NAME");
					
					data.add((String)this.fileName);

					data.add((String)xmlFilename);
					data.add((Integer)this.seqNum);
				//	System.out.println("SeqNum in Queue: "+this.seqNum);
					
					queue.put(data);

				}
				System.out.println("Master thread is shutting down......");
				for (i=0;i<numofthread;i++)
				{
					data = new ArrayList<Object>();				
					data.add((String)SHUTDOWN_REQ);
					queue.put(data);
					//rs.close();
				}
				

				
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			} 

	}
	
	
	public ResultSet setIncompletedResultSet() throws SQLException
	{

		ResultSet rs = null;
		try {
			//fetch the first fetchnum rows whose loading configuration filename is loadingconfig for batch loading
			PreparedStatement ps = db.getPreparedStatement(incompleteDocSql+" fetch first "+fetchnum+" row only");
			ps.setString(1, loadingconfig);
			rs = ps.executeQuery();
			} catch (Exception e1) {
			e1.printStackTrace();
			System.out.println("Failed to retrieve INCOMPLETE documents.");
			return null;
		}

		System.out.println("Master Thread is getting incompleted ResultSet.......................");

		return rs;		
	}
	
}
