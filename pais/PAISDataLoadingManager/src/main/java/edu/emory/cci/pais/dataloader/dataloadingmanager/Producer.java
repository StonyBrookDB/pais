package edu.emory.cci.pais.dataloader.dataloadingmanager;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.zip.ZipInputStream;

import edu.emory.cci.pais.dataloader.db2helper.PAISDBHelper;


/*
 * This is the Producer-Consumer pattern of Java multi-thread
 * 
 * Here we have only one Producer thread, get ResultSets from SQL statement and cast them into a object array list and put it to the blocking queue
 * And consumers will take an object array list from the queue and cast it separately.*/


public class Producer extends Thread{

	private static String incompleteDocSql ="SELECT * FROM PAIS.STAGINGDOC WHERE STATUS='INCOMPLETE'";
	private int fetchnum;
	PAISDBHelper db = null;
	PAISParser parser = null;
	ResultSet rs = null;

	
	
	/*
	 * data we want to produce/consume.
	 * cast Object into them separately.
	 * */

	String xmlFilename = null;
	public String fileName = null;
	public Integer seqNum = 0;
	String loadingconfig ;
	
	private static final String SHUTDOWN_REQ = "SHUTDOWN";

 
	
	public Producer(PAISDBHelper db, int fetchnum,String loadingconfig)
	{
		this.db = db;
		this.parser = new PAISParser(loadingconfig);
		this.fetchnum = fetchnum;
		this.loadingconfig = loadingconfig;
	}
	
	
	

	/*thread entrance
	 * produce data to the queue*/
	public void run() {

		ZipInputStream zipIn = null;
		ArrayList<Object> data = null;
			try {
				this.rs = setIncompletedResultSet();

				while(rs.next())
				{
					
					data = new ArrayList<Object>();
					
					zipIn = new ZipInputStream ( rs.getBinaryStream("BLOB") );
					xmlFilename = db.getXmlDocFile(zipIn,DataLoadingManager.cachePath);
					
					seqNum = rs.getInt("SEQUENCE_NUMBER");
					fileName = rs.getString("FILE_NAME");
					data.add((String)this.fileName);
					data.add((String)xmlFilename);
					data.add((Integer)this.seqNum);
				//	System.out.println("SeqNum in Queue: "+this.seqNum);
					synchronized(DataLoadingManager.queue)
					{
					  DataLoadingManager.queue.add(data);
					}
					while(DataLoadingManager.queue.size()>DataLoadingManager.maxUnzipped)
					{
						try {
							Thread.sleep(1000);
							System.out.println("waiting for consumer!");
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}

				}
				System.out.println("Master thread is shutting down......");
				data = new ArrayList<Object>();				
				data.add((String)SHUTDOWN_REQ);
				synchronized(DataLoadingManager.queue)
				{
				   DataLoadingManager.queue.add(data);
				}

				

				
			} catch (SQLException e) {
				e.printStackTrace();
			}

	}
	
	
	public ResultSet setIncompletedResultSet() throws SQLException
	{

		ResultSet rs = null;
		try {
			//fetch the first fetchnum rows whose loading configuration filename is loadingconfig for batch loading
			PreparedStatement ps;
			if(fetchnum!=0)
			{
				ps = db.getPreparedStatement(incompleteDocSql+" AND LOADINGCONFIG=? fetch first "+fetchnum+" row only");
				ps.setString(1, loadingconfig);
			}
			else
				ps = db.getPreparedStatement(incompleteDocSql);	
			
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
