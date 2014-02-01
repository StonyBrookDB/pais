package edu.emory.cci.pais.dataloader.dataloadingmanager;


import java.util.ArrayList;


/** 
 * @author Fusheng Wang
 * @version 1.0
 * This class provides queries for uploading meta XML document.
 * */
public class MetaQueries{ 

	public static ArrayList<String> getAllQueries(String uid){
		ArrayList<String> array = new ArrayList<String>();
		
		array.add(getquery_PAIS(uid) );
		array.add(getquery_COLLECTION(uid) );
		array.add(getquery_WHOLESLIDEIMAGEREFERENCE (uid) );
		array.add(getquery_PROJECT(uid) );
		array.add(getquery_GROUP(uid) );
		array.add(getquery_USER(uid) );
		array.add(getquery_PATIENT(uid) );
		array.add(getquery_SPECIMEN(uid) );
		array.add(getquery_ANATOMICENTITY(uid) );
		array.add(getquery_PROVENANCE(uid) );
		array.add(getquery_ALGORITHM(uid) );
		array.add(getquery_PARAMETER(uid) );
		array.add(getquery_INPUTFILEREFERENCE(uid) );		
		return array;
	}
	

	public static String getquery_PAIS(String uid){
		String query =  
		"	  INSERT INTO PAIS.PAIS  " +
		"	  SELECT T.* " +
		"	  FROM  PAIS.METADOC X, XMLTABLE( " +
		"	       XMLNAMESPACES(DEFAULT 'gme://caCORE.caCORE/3.2/edu.emory.cci.pais'), '$m/PAIS' PASSING X.XMLDOC as  \"m\" " +
		"	       COLUMNS	 " +
		"	       	  	PAIS_ID				DECIMAL(30,0)     PATH '@id', " +
		"	 	  		PAIS_UID     		VARCHAR(64)       PATH '@uid',  " +
		"	 	  		NAME				VARCHAR(64)  	  PATH '@name',  	 " +
		"	 	  		DATETIME			VARCHAR(64)       PATH '@dateTime',  " +
		"	 	  		PAISVERSION  		VARCHAR(64)       PATH '@paisVersion', " + 
		"	   	  		CODEVALUE			VARCHAR(64)  	  PATH '@codeValue', " +
		"	   	  		CODEMEANING			VARCHAR(64) 	  PATH '@codeMeaning', " +
		"		  		CODINGSCHEMEDESIGNATOR	VARCHAR(64)   PATH '@codingSchemeDesignator', " +
		"	          	CODINGSCHEMEVERSION		VARCHAR(64)   PATH '@codingSchemeVersion',   " +
		"	          	COMMENT				VARCHAR(128)      PATH '@comment'  	   " +
		"	    ) " +
		"	  AS T " +
		"	  WHERE NOT EXISTS (SELECT * FROM PAIS.PAIS WHERE PAIS_UID = T.PAIS_UID) " +
			" AND X.UID='"  + uid+"'";		
		return query;  
	}
	
	
	public static String getquery_PROJECT(String uid){
		String query =  
		    " INSERT INTO PAIS.PROJECT " +
			" SELECT DISTINCT T.* " +
			" FROM  PAIS.METADOC X, XMLTABLE( " +
			"      XMLNAMESPACES(DEFAULT 'gme://caCORE.caCORE/3.2/edu.emory.cci.pais'), '$m/PAIS/project/Project' PASSING X.XMLDOC as \"m\" " + 
			"      COLUMNS " +
			" 		PROJECT_ID 			DECIMAL(30,0) 	PATH '@id', " + 
			" 		PROJECT_UID			VARCHAR(64)	PATH '@uid', " +
			" 		NAME	 			VARCHAR(64)	PATH '@name', " +
			" 		URI					VARCHAR(64)	PATH '@uri', " +
			" 		PAIS_UID			VARCHAR(64)   	PATH '../../@uid' " +  
			" ) AS T  " +
			" WHERE NOT EXISTS (SELECT * FROM PAIS.PROJECT WHERE PROJECT_ID = T.PROJECT_ID and PAIS_UID = T.PAIS_UID)  " + 			
			" AND X.UID='"  + uid+"'";
		return query;  
	}	
	
	public static String getquery_GROUP(String uid){
		String query =  
		    " INSERT INTO PAIS.GROUP " +
			" SELECT DISTINCT T.* " +
			" FROM  PAIS.METADOC X, XMLTABLE( " +
			"      XMLNAMESPACES(DEFAULT 'gme://caCORE.caCORE/3.2/edu.emory.cci.pais'), '$m/PAIS/group/Group' PASSING X.XMLDOC as  \"m\" " + 
			"      COLUMNS " +
			" 	GROUP_ID 			DECIMAL(30,0) 	PATH '@id', " + 
			" 	GROUP_UID			VARCHAR(64)	PATH '@uid', " +
			" 	NAME	 			VARCHAR(64)	PATH '@name', " +
			" 	URI					VARCHAR(64)	PATH '@uri', " +
			" 	PAIS_UID			VARCHAR(64)   	PATH '../../@uid' " +  
			" ) AS T " +
			" WHERE NOT EXISTS (SELECT * FROM PAIS.GROUP WHERE GROUP_ID = T.GROUP_ID and PAIS_UID = T.PAIS_UID) " +			
			" AND X.UID='"  + uid+"'";
		return query;  
	}	
	
	
	public static String getquery_USER(String uid){
		String query =  
		    " INSERT INTO PAIS.USER " +
			" SELECT  DISTINCT T.* " +
			" FROM  PAIS.METADOC X, XMLTABLE( " +
			"      XMLNAMESPACES(DEFAULT 'gme://caCORE.caCORE/3.2/edu.emory.cci.pais'), '$m/PAIS/user/User' PASSING X.XMLDOC as  \"m\" " + 
			"      COLUMNS " +
			" 	USER_ID 			DECIMAL(30,0) 	PATH '@id', " + 
			" 	LOGINNAME			VARCHAR(64)		PATH '@loginName', " +
			" 	NAME	 			VARCHAR(64)		PATH '@name', " +
			" 	NUMBERWITHINROLEOFCLINICALTRIAL INTEGER		PATH '@numberWithinRoleOfClinicalTrial', " +
			" 	ROLEINTRIAL			VARCHAR(64)		PATH '@roleInTrial', " +
			" 	PAIS_UID			VARCHAR(64)   	PATH '../../@uid'   " +
			" ) AS T " +
			" WHERE NOT EXISTS (SELECT * FROM PAIS.USER WHERE USER_ID = T.USER_ID and PAIS_UID = T.PAIS_UID) " +
			" AND X.UID='"  + uid+"'";
		return query;  
	}		
	
	
	
	public static String getquery_COLLECTION(String uid){
		String query =  
			"    INSERT  INTO PAIS.COLLECTION " +
			"    SELECT  DISTINCT T.* " +
			"    FROM  PAIS.METADOC X, XMLTABLE( " +
			"         XMLNAMESPACES(DEFAULT 'gme://caCORE.caCORE/3.2/edu.emory.cci.pais'), '$m/PAIS/collections/Collection' PASSING X.XMLDOC as  \"m\" " +
			"         COLUMNS	 	   " +
			"   	  COLLECTION_ID          DECIMAL(30,0)   PATH  '@id', " +
			"   	  COLLECTION_UID         VARCHAR(64)     PATH  '@uid', " +
			"   	  NAME					VARCHAR(64)      PATH  '@name', " +
			"   	  ROLE					VARCHAR(64)      PATH  '@role', " +
			"   	  METHODNAME			VARCHAR(64)      PATH  '@methodName', " +
			"   	  SEQUENCENUMBER		INTEGER	         PATH  '@sequenceNumber', " +
			"   	  STUDYDATETIME 	    VARCHAR(64)      PATH  '@studyDateTime', " +
			"   	  PAIS_UID     			VARCHAR(64)      PATH  '../../@uid' 	  " +
			"      ) " +
			"    AS T " +
			"    WHERE NOT EXISTS (SELECT * FROM PAIS.COLLECTION WHERE PAIS_UID = T.PAIS_UID and COLLECTION_ID = T.COLLECTION_ID and METHODNAME=T.METHODNAME and SEQUENCENUMBER = T.SEQUENCENUMBER) " +	
			" 	 AND X.UID='"  + uid+"'";
		return query;  
	}

	

	public static String getquery_WHOLESLIDEIMAGEREFERENCE(String uid){
		String query =  
			"	    INSERT INTO PAIS.WHOLESLIDEIMAGEREFERENCE " +
			"	    SELECT DISTINCT T.* " +
			"	    FROM  PAIS.METADOC X, XMLTABLE( " +
			"	         XMLNAMESPACES(DEFAULT 'gme://caCORE.caCORE/3.2/edu.emory.cci.pais'), '$m/PAIS/imageReferenceCollection/ImageReference' PASSING X.XMLDOC as  \"m\" " +
			"	         COLUMNS " +
			"	      	IMAGEREFERENCE_ID		DECIMAL(30,0) 	PATH  '@id', " +
			"	      	IMAGEREFERENCE_UID		VARCHAR(64)		PATH  '@uid',	 " +
			"	      	FILEREFERENCE			VARCHAR(64)		PATH  '@fileReference', " +
			"	      	URI						VARCHAR(64)		PATH  '@uri', " +
			"	      	RESOLUTION				DOUBLE			PATH  '@resolution', " +
			"	      	ZAXISRESOLUTION			DOUBLE			PATH  '@zaxisResolution', " +
			"	      	ZAXISCOORDINTE			DOUBLE			PATH  '@zaxisCoordinate', " +
			"	      	PAIS_UID				VARCHAR(64) 	PATH  '../../@uid'  " +
			"	    ) AS T " +
			"	      WHERE NOT EXISTS (SELECT * FROM PAIS.WHOLESLIDEIMAGEREFERENCE WHERE IMAGEREFERENCE_ID = T.IMAGEREFERENCE_ID ) " +
			" 		AND X.UID='"  + uid+"'";
		return query;  
	}



	public static String getquery_REGION(String uid){
		String query =  
			"    INSERT INTO PAIS.REGION(REGION_ID, NAME, X, Y, HEIGHT, WIDTH, ZOOMRESOLUTION, COORDINATERESOLUTION, COORDINATEREFERENCE, IMAGEREFERENCE_ID, PAIS_UID) " +
			"    SELECT DISTINCT  T.REGION_ID, T.NAME, T.X, T.Y, T.HEIGHT, T.WIDTH, T.ZOOMRESOLUTION, T.COORDINATERESOLUTION, T.COORDINATEREFERENCE, T.IMAGEREFERENCE_ID, T.PAIS_UID " +
			"    FROM  PAIS.METADOC X, XMLTABLE( " +
			"       XMLNAMESPACES(DEFAULT 'gme://caCORE.caCORE/3.2/edu.emory.cci.pais'), '$m/PAIS/imageReferenceCollection/ImageReference/region/Region' PASSING X.XMLDOC as  \"m\" " +
			"       COLUMNS " +
			"  	    REGION_ID				DECIMAL(30,0) 	PATH '@id', " +
			"  	    NAME					VARCHAR(64)		PATH '@name', " +
			"      	X						DOUBLE			PATH '@x', " +
			"      	Y						DOUBLE			PATH '@y', " +
			"      	HEIGHT					DOUBLE			PATH '@height', " +
			"      	WIDTH					DOUBLE			PATH '@width', " +
			"      	ZOOMRESOLUTION			DOUBLE			PATH '@zoomResolution', " +
			"       COORDINATERESOLUTION	DOUBLE			PATH '@coordinateResolution', " +	
			"      	COORDINATEREFERENCE		VARCHAR(64)		PATH '@coordinateReference', " +
			"      	IMAGEREFERENCE_ID		DECIMAL(30,0)   PATH  '../../@id', " +
			"      	PAIS_UID				VARCHAR(64)   	PATH '../../../../@uid'   " +
			"    ) AS T " +
			"	 WHERE X.UID='"  + uid+"'";
		return query;  
	}

	
	
	public static String getquery_PATIENT(String uid){
		String query =   
	"	    INSERT INTO PAIS.PATIENT " +
	"	    SELECT DISTINCT T.* " +
	"	    FROM  PAIS.METADOC X, XMLTABLE( " +
	"	         XMLNAMESPACES(DEFAULT 'gme://caCORE.caCORE/3.2/edu.emory.cci.pais'), '$m/PAIS/imageReferenceCollection/ImageReference/subject/Subject' PASSING X.XMLDOC as  \"m\" " + 
	"	         COLUMNS " +
	"	    	SUBJECT_ID 			DECIMAL(30,0)	PATH '@id', " +
	"	    	PATIENTID 			VARCHAR(64)		PATH '@patientID', " +
	"	    	SEX					VARCHAR(64)		PATH '@sex', " +
	"	    	BIRTHDATE			VARCHAR(64)		PATH '@birthdate', " +
	"	    	ETHNICGROUP			VARCHAR(64)		PATH '@ethnicGroup', " +
	"	      	IMAGEREFERENCE_ID	DECIMAL(30,0)   PATH  '../../@id', " +
	"	      	PAIS_UID			VARCHAR(64)   	PATH '../../../../@uid'   " +
	"	    ) AS T " +
	"	    WHERE NOT EXISTS (SELECT * FROM PAIS.PATIENT WHERE SUBJECT_ID = T.SUBJECT_ID and PAIS_UID = T.PAIS_UID) " +			
	" 		AND X.UID='"  + uid+"'";
		return query;  
	}

	
	public static String getquery_SPECIMEN(String uid){
		String query =  
	"		  INSERT INTO PAIS.SPECIMEN " +
	"		    SELECT DISTINCT T.* " +
	"		    FROM  PAIS.METADOC X, XMLTABLE( " +
	"		         XMLNAMESPACES(DEFAULT 'gme://caCORE.caCORE/3.2/edu.emory.cci.pais'), '$m/PAIS/imageReferenceCollection/ImageReference/specimen/Specimen' PASSING X.XMLDOC as  \"m\" " + 
	"		         COLUMNS " +
	"		     	SPECIMEN_ID			DECIMAL(30,0) 	PATH '@id', " +
	"		     	SPECIMEN_UID		VARCHAR(64)     PATH '@uid', " +
	"		  		TYPE				VARCHAR(64)     PATH '@type', " +
	"		     	STAIN				VARCHAR(64)     PATH '@stain', " +
	"		     	IMAGEREFERENCE_ID	DECIMAL(30,0)   PATH '../../@id', " +
	"		      	PAIS_UID			VARCHAR(64)   	PATH '../../../../@uid'   " +
	"		    ) AS T " +
	"		    WHERE NOT EXISTS (SELECT * FROM PAIS.SPECIMEN WHERE SPECIMEN_ID = T.SPECIMEN_ID and PAIS_UID = T.PAIS_UID) " +			
	" 			AND X.UID='"  + uid+"'";
		return query;  
	}
	
	
	public static String getquery_ANATOMICENTITY(String uid){
		String query =  
	"		  INSERT INTO PAIS.ANATOMICENTITY " +
	"		    SELECT DISTINCT T.* " +
	"		    FROM  PAIS.METADOC X, XMLTABLE( " +
	"		      XMLNAMESPACES(DEFAULT 'gme://caCORE.caCORE/3.2/edu.emory.cci.pais'), '$m/PAIS/imageReferenceCollection/ImageReference/anatomicEntityCollection/AnatomicEntity' PASSING X.XMLDOC as  \"m\" " + 
	"		      COLUMNS  	 " +
	"		     	ANATOMICENTITY_ID		DECIMAL(30,0) 	PATH '@id', " +
	"		     	CODEVALUE				VARCHAR(64)		PATH '@codeValue', " +
	"		     	CODEMEANING				VARCHAR(64)		PATH '@codeMeaning', " +
	"		     	CODINGSCHEMEDESIGNATOR	VARCHAR(64)		PATH '@codingSchemeDesignator', " +
	"		     	CODINGSCHEMEVERSION		VARCHAR(64)		PATH '@codingSchemeVersion', " +
	"		     	IMAGEREFERENCE_ID		DECIMAL(30,0)   PATH '../../@id', " +
	"		      	PAIS_UID				VARCHAR(64)   	PATH '../../../../@uid'   " +
	"		    ) AS T " +
	"		  WHERE NOT EXISTS (SELECT * FROM PAIS.ANATOMICENTITY WHERE ANATOMICENTITY_ID = T.ANATOMICENTITY_ID and PAIS_UID = T.PAIS_UID) " +			
	" 		  AND X.UID='"  + uid+"'";
		return query;  
	}
	
	

	
	
	public static String getquery_PROVENANCE(String uid){
		String query =  
	"		INSERT INTO PAIS.PROVENANCE " +
	"		  SELECT DISTINCT T.* " +
	"		  FROM  PAIS.METADOC X, XMLTABLE( " +  
	"		  	XMLNAMESPACES(DEFAULT 'gme://caCORE.caCORE/3.2/edu.emory.cci.pais'), '$m/PAIS/provenanceCollection/Provenance' PASSING X.XMLDOC as  \"m\" " +   
	"		        COLUMNS " +
	"		  	PROVENANCE_ID		DECIMAL(30,0)   PATH '@id', " + 
	"		  	SCOPE				VARCHAR(64)     PATH '@scope',   " +
	"		  	PAIS_UID 			VARCHAR(64)     PATH '../../@uid' " +
	"		) AS T " +
	"		WHERE NOT EXISTS (SELECT * FROM PAIS.PROVENANCE WHERE PROVENANCE_ID = T.PROVENANCE_ID and PAIS_UID = T.PAIS_UID) " +			
	" 		AND X.UID='"  + uid+"'";
		return query;  
	}
	
	
	public static String getquery_ALGORITHM(String uid){
		String query =
	"		INSERT INTO PAIS.ALGORITHM " +
	"		  SELECT DISTINCT T.* " +
	"		  FROM  PAIS.METADOC X, XMLTABLE(   " +
	"		  	XMLNAMESPACES(DEFAULT 'gme://caCORE.caCORE/3.2/edu.emory.cci.pais'), '$m/PAIS/provenanceCollection/Provenance/algorithm/Algorithm' PASSING X.XMLDOC as  \"m\" " +   
	"		        COLUMNS " +
	"		  ALGORITHM_ID		DECIMAL(30,0) 		PATH '@id', " +
	"		  NAME				VARCHAR(64)			PATH '@name', " +
	"		  VERSION			VARCHAR(64)			PATH '@version', " +
	"		  URI				VARCHAR(64)			PATH '@uri', " +
	"		  PROVENANCE_ID		DECIMAL(30,0) 		PATH '../../@id', " +
	"		  PAIS_UID 			VARCHAR(64)   		PATH '../../../../@uid'   " +
	"		) AS T " +
	"		WHERE NOT EXISTS ( SELECT * FROM PAIS.ALGORITHM WHERE ALGORITHM_ID = T.ALGORITHM_ID and  PAIS_UID = T.PAIS_UID) " +			
	" 		AND X.UID='"  + uid+"'";
		return query;  
	}
	
	
	public static String getquery_PARAMETER(String uid){
		String query =  
	"		INSERT INTO PAIS.PARAMETER  " +
	"		  SELECT  DISTINCT T.* " +
	"		  FROM  PAIS.METADOC X, XMLTABLE( " +  
	"		  	XMLNAMESPACES(DEFAULT 'gme://caCORE.caCORE/3.2/edu.emory.cci.pais'), '$m/PAIS/provenanceCollection/Provenance/parameterCollection/Parameter' PASSING X.XMLDOC as  \"m\" " +   
	"		        COLUMNS " +
	"		  PARAMETER_ID			DECIMAL(30,0) 		PATH '@id', " +    
	"		  NAME					VARCHAR(64)			PATH '@name', " +
	"		  VALUE					VARCHAR(64)			PATH '@value', " +
	"		  DATATYPE				VARCHAR(64)			PATH '@dataType', " +
	"		  PROVENANCE_ID			DECIMAL(30,0) 		PATH '../../@id', " +
	"		  ALGORITHM_ID			DECIMAL(30,0) 		PATH '../../algorithm/Algorithm/@id', " +
	"		  PAIS_UID 				VARCHAR(64)   		PATH '../../../../@uid'   " +
	"		) AS T " +
	"		WHERE NOT EXISTS ( SELECT * FROM PAIS.PARAMETER WHERE PARAMETER_ID = T.PARAMETER_ID and  PROVENANCE_ID = T.PROVENANCE_ID  and PAIS_UID = T.PAIS_UID) " + 			
	" 		AND X.UID='"  + uid+"'";
		return query;  
	}
	
	
	public static String getquery_INPUTFILEREFERENCE(String uid){
		String query =  
	"		INSERT INTO PAIS.INPUTFILEREFERENCE " +
	"		  SELECT  DISTINCT  T.* " +
	"		  FROM  PAIS.METADOC X, XMLTABLE( " +  
	"		  	XMLNAMESPACES(DEFAULT 'gme://caCORE.caCORE/3.2/edu.emory.cci.pais'), '$m/PAIS/provenanceCollection/Provenance/inputFileReferenceCollection/InputFileReference' PASSING X.XMLDOC as  \"m\" " +   
	"		        COLUMNS " +
	"		  INPUTFILEREFERENCE_ID DECIMAL(30,0) 		PATH '@id', " +
	"		  FILEREFERENCE			VARCHAR(64)			PATH '@fielReference', " +
	"		  URI					VARCHAR(64)			PATH '@uri', " +
	"		  PROVENANCE_ID			DECIMAL(30,0) 		PATH '../../@id', " +
	"		  ALGORITHM_ID			DECIMAL(30,0) 		PATH '../../algorithm/Algorithm/@id', " +	
	"		  PAIS_UID 				VARCHAR(64)   		PATH '../../../../@uid' " +
	"		) AS T " +
	"		WHERE NOT EXISTS ( SELECT * FROM PAIS.INPUTFILEREFERENCE WHERE INPUTFILEREFERENCE_ID = T.INPUTFILEREFERENCE_ID and  PROVENANCE_ID = T.PROVENANCE_ID  and PAIS_UID = T.PAIS_UID) " +			
	" 		AND X.UID='"  + uid+"'";
		return query;  
	}
	
	
}
