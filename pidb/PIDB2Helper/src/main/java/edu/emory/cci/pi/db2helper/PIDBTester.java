package edu.emory.cci.pi.db2helper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/**
 * 
 * @author Steve Oh,
 * Comprehensive Informatics
 * 
 * This class takes care of database connection to server
 *
 */
public class PIDBTester {
	    private static String DRIVER_NAME = "com.ibm.db2.jcc.DB2Driver";
	    private static String XMLDB_URI_KEY = "jdbc:db2://170.140.186.172:50000";     
	    private String USERNAME = "db2user";
	    private String PASSWORD = "userdb1234";
	    private String db2Collection = "SAMPLE";
	    private String HOSTNAME = "europa.cci.emory.edu";
	    
	    private Connection db2Connection = null;
	    private Statement db2Statement = null;
	   
	   
	    public PIDBTester(){
	        initialize();
	    }
	   
	    public PIDBTester(boolean autoCommit){
	        initialize();
	        setAutoCommit(autoCommit);
	    }
	   
	   
	    public PIDBTester(String host, String port, String username, String passwd, String database){
	        setLogin(host, port, username, passwd, database);
	        initialize();
	        setAutoCommit(true);
	       
	    }
	   
	    private void initialize() {
	        setDBConnection();         
	    }
	   
	   
	    public void setAutoCommit(boolean auto){
	        try {
	            db2Connection.setAutoCommit(auto);
	        } catch (SQLException e1) {
	            e1.printStackTrace();
	        }
	    }
	   
	   
	    public void setLogin(String host, String port, String username, String passwd, String database ){
	        XMLDB_URI_KEY = "jdbc:db2://" + host + ":" + port;
	        USERNAME = username;
	        PASSWORD = passwd;
	        db2Collection = database;
	    }

	    //Initialize database connection. This connection is reused.
	    private void setDBConnection() {
	        String jdbcUrl = XMLDB_URI_KEY + "/" + db2Collection;         
	        try {
	            Class.forName(DRIVER_NAME).newInstance();
	            Properties connectProperties = new Properties();
	            connectProperties.put("user", USERNAME);
	            connectProperties.put("password", PASSWORD);
	            System.out.println("JDBC URL: " + jdbcUrl);
	            db2Connection = DriverManager.getConnection(jdbcUrl, connectProperties);
	            if (db2Connection != null) db2Statement = db2Connection.createStatement();       
	            System.out.println("DB successfully connected.");
	        } catch (InstantiationException e) {
	            e.printStackTrace();
	        } catch (IllegalAccessException e) {
	            e.printStackTrace();
	        } catch (ClassNotFoundException e) {
	            e.printStackTrace();
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }   
	    
	    public void setHostname(String hostName){
	    	HOSTNAME = hostName;
	    }
	    
	    public void setUsername(String userName){
	    	USERNAME = userName;
	    }
	    
	    public void setPassword(String passWord){
	    	PASSWORD = passWord;
	    }
	   
	    public Connection getDBConnection(){
	        return db2Connection;
	    }
	   
	   
	    public Statement getStatement(){
	        return db2Statement;
	   
	    }
	    
	    public String getHostname(){
	    	return HOSTNAME;
	    }
	    
	    public String getUsername(){
	    	return USERNAME;
	    }
	    
	    public String getPassword(){
	    	return HOSTNAME;
	    }
	   
	    public void closeStatement(){
	        if (db2Statement != null)
	            try {
	                db2Statement.close();
	            } catch (SQLException e) {
	                e.printStackTrace();
	            }           
	    }

}
