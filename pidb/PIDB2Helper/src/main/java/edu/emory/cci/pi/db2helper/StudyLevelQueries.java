package edu.emory.cci.pi.db2helper;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @author Steve Oh,
 * Comprehensive Informatics Center:
 * 
 * StudyLevelQueries class take care of all study level queries 
 */
public class StudyLevelQueries {
	
	/**  
	 * DB connection information come from either a configuration xml file or default values from PIDBTester.java
	 */
	public String file = "conf/dbconfig.xml";
	public DBConfig config = new DBConfig(new File(file) );
	public Properties props = config.getProperties();		
	public String host = props.getProperty("host");
	public String port = props.getProperty("port");
	public String username = props.getProperty("username");
	public String passwd = props.getProperty("passwd");
	public String database = props.getProperty("database");
	
	public PIDBTester db = new PIDBTester(host, port, username, passwd, database);
	public Connection myconn = db.getDBConnection();
	public static Statement stmt = null;
	
	public StudyLevelQueries(){
		try {
			stmt = myconn.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

/**
 * Return ImageReferenceUIDs that related to specific patientID
 * 
 * @param String PI.PATIENT patientID 
 * 
 * @return List<String> ImageReferenceUIDs 
 */
	public List <String> getImageReferenceUIDsByPatientID(String patientid) {
		String psql = "SELECT i.imagereference_uid " +
		"FROM PI.IMAGE i, PI.PATIENT p " + 
		"WHERE i.patient_id = p.id and " +
		"p.patientid = " + "'" + patientid + "'";
		 
		List<String> list = new ArrayList<String>();
	    try {
	        ResultSet rs = stmt.executeQuery(psql);
	        while(rs.next()){
	        	String tmp = rs.getString(1);
	        	if(tmp!=null)
	        	{
	        		list.add(tmp);
	        	}
	        } // end of while        
	    } catch (SQLException e) {
	        // TODO Auto-generated catch block
	    	e.getMessage();
	    	e.getMessage();
	    } catch (NullPointerException e) {
	    	e.printStackTrace();
	    	e.getMessage();
	    }
		return list;	
	}
/*
 * To Do: There is no data under name file in ExperimentalStudy table yet. 
 * So, test is required. 
 */
	
/**
 * Return ImageReferenceUIDs that related to specific study name
 * 
 * @param String PI.EXPERIMENTALSTUDY name 
 * 
 * @return List<String> ImageReferenceUIDs 
 */	
	public List <String> getImageReferenceUIDsByStudyName(String studyName) {
		String psql = "SELECT i.imagereference_uid " +
		"FROM PI.IMAGE i, PI.DATASET ds, PI.EXPERIMENTALSTUDY estudy " + 
		"WHERE i.dataset_id = ds.id and " +
		"estudy.dataset_id = ds.id and " +
		"estudy.name = " + "'" + studyName + "'";
		 
		List<String> list = new ArrayList<String>();
	    try {
	        ResultSet rs = stmt.executeQuery(psql);
	        while(rs.next()){
	        	String tmp = rs.getString(1);
	        	if(tmp!=null)
	        	{
	        		list.add(tmp);
	        	}
	        } // end of while        
	    } catch (SQLException e) {
	        // TODO Auto-generated catch block
	    	e.getMessage();
	    	e.getMessage();
	    } catch (NullPointerException e) {
	    	e.printStackTrace();
	    	e.getMessage();
	    }
		return list;	
	}
	
/*
	public static void main(String[] args) throws IOException {
	
	long starttime = System.currentTimeMillis() ;
	StudyLevelQueries query = new StudyLevelQueries();
	
	List<String> test = query.getImageReferenceUIDsByPatientID("0518");
	System.out.println(test.get(1));
	
    long endtime = System.currentTimeMillis();
    System.out.println(" DB init time = " + (endtime - starttime)/1000.0 + " seconds." );   
	
	}
*/	
}
