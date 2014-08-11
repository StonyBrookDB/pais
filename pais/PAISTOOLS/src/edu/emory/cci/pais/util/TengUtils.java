package edu.emory.cci.pais.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
/**
 * @author dejun teng, Center for Comprehensive Informatics, Emory University
 * @version 1.0
 * This class provide some useful static functions and values
 *
 */
public class TengUtils {
	
	
	public static final int SLIDE = 1;
	public static final int COLLECTION = 2;
	public static final int EDITPANEL = 1;
	public static final int CHOOSEPANEL = 2;
	public static final int DLMWORKING=3;
	public static final int DLMNOTWORKING=1;
	public static final int DLMDIED=2;
	
	public static final int RETRYTIME = 3;
	public static final String INSERTTEMPLATELABEL = "--INSERTTEMPLATESQL";
	public static final String INSERTFEATURELABEL = "--INSERTFEATURESQL";
	public static final String INSERTTFRLLABEL = "--INSERTTFRL";
	public static final String DROPTABLELABEL = "--NEWDROPTABLE";
	public static final String MARKUPTABLELABEL="--NEWMARKUPTABLE";
	public static final String NEWINDEXLABEL = "--NEWINDEXES";
	public static final String NEWKEYLABEL = "--NEWKEYS";

	public static final String INSERTTEMPLATESQL = "INSERT INTO PAIS.TEMPLATE(TEMPLATENAME,DESCRIPTION) VALUES('templatenamevalue','descriptionvalue');\n";
	public static final String INSERTFEATURESQL = "INSERT INTO PAIS.FEATURES(FEATURENAME,TYPE) VALUES('featurenamevalue','typevalue');\n";
	public static final String INSERTTFRLSQL = "INSERT INTO PAIS.TEMPLATEFEATURESRL(templatename,featurename) VALUES('templatenamevalue','featurenamevalue');\n";
	public static final String KEYSQL = "ALTER TABLE PAIS.MARKUP_templatename ADD PRIMARY KEY(PAIS_UID, MARKUP_ID);\n";
	public static final String INDEXSQL = "CREATE INDEX PAIS.MAPIDX ON PAIS.MARKUP_templatename(POLYGON) EXTEND USING DB2GSE.SPATIAL_INDEX (15, 30, 110);\n" +
			"RUNSTATS ON TABLE PAIS.MARKUP_templatename with distribution AND sampled detailed INDEXES ALL;\n";
	public static final String DROPTABLESQL = "DROP TABLE PAIS.MARKUP_tablename;\n";
    public static final String MARKUPTABLESQL = "CREATE TABLE PAIS.MARKUP_tablename (\n" +
			"MARKUP_ID			DECIMAL(30,0) NOT NULL ,  \n" +//
			"NAME				VARCHAR(64),\n" +
			" GEOMETRICSHAPE_ID		DECIMAL(30,0)   NOT NULL ,\n" +//
			"ANNOTATION_ID		DECIMAL(30,0)   NOT NULL ,\n" +//
			"POLYGON  			DB2GSE.ST_POLYGON INLINE LENGTH 30000,\n" +
			"TILENAME			VARCHAR(64)   NOT NULL ,\n " +//
			"PAIS_UID 			VARCHAR(64)   NOT NULL ,\n" +//
			"CENTROID_X			DOUBLE,\n" +
			"CENTROID_Y			DOUBLE\n" +
			")\n" +
			"COMPRESS YES\n" +
			"IN spatialtbs32k;\n";
	
	public static final String lcxml = 
			"<loadingconfig>\n" +
					"<largeboundary>true</largeboundary>\n" +
					"<tables>\n" +
						"<table name=\"regionTable\" tableName=\"PAIS.REGION\">\n" +
						"<attribute path=\"Region/@id\"            name=\"REGION_ID\"             type=\"DECIMAL\" />\n" +
						"<attribute path=\"Region/@name\"          name=\"NAME\"              type=\"VARCHAR\" />\n" +
						"<attribute path=\"Region/@x\"             name=\"X\"                 type=\"DOUBLE\" />\n" +
						"<attribute path=\"Region/@y\"             name=\"Y\"                 type=\"DOUBLE\" />\n" +
						"<attribute path=\"Region/@height\"        name=\"HEIGHT\"                type=\"DOUBLE\" />\n" +
						"<attribute path=\"Region/@width\"             name=\"WIDTH\"                 type=\"DOUBLE\" />\n" +
						"<attribute path=\"Region/@zoomResolution\" name=\"ZOOMRESOLUTION\"                   type=\"DOUBLE\" />\n" +
						"<attribute path=\"Region/@coordinateResolution\" name=\"COORDINATERESOLUTION\"       type=\"DOUBLE\" />\n" +
						"<attribute path=\"Region/@coordinateReference\"  name=\"COORDINATEREFERENCE\"        type=\"VARCHAR\" />\n" +
						"<attribute path=\"ImageReference/@id\"          name=\"IMAGEREFERENCE_ID\"           type=\"DECIMAL\" />\n" +
						"<attribute path=\"PAIS/@uid\"                   name=\"PAIS_UID\"        type=\"VARCHAR\" />\n" +
						"</table>\n" +
						
						"<table name=\"flatCalculationTable\" tableName=\"PAIS.CALCULATION_FLAT\">\n" +
						"<attribute path=\"Markup/@id\"           name=\"MARKUP_ID\"          type=\"DECIMAL\" />\n" +
						"<attribute path=\"Annotation/@id\"       name=\"ANNOTATION_ID\"          type=\"DECIMAL\" />\n" +
						"<attribute path=\"Region/@name\"             name=\"TILENAME\"           type=\"VARCHAR\" />\n" +
						"<attribute path=\"PAIS/@uid\"            name=\"PAIS_UID\"           type=\"VARCHAR\" />\n" +
						"<attribute path=\"Provenance/@id\"       name=\"PROVENANCE_ID\"          type=\"DECIMAL\" />\n" +
						"<attribute path=\"Annotation/@name\"         name=\"ANNOTATION_NAME\"            type=\"VARCHAR\" />\n" +
						"</table>\n" +
						
						"<table name=\"featureTable\" tableName=\"PAIS.CALCULATION_FLAT\">\n" +
                        "tableofflat \n"+
                        "</table>\n" +

						"<table name=\"annotationMarkupTable\" tableName=\"PAIS.ANNOTATION_MARKUP\">\n" +
						"<attribute path=\"Markup/@id\"           name=\"MARKUP_ID\"          type=\"DECIMAL\" />\n" +
						"<attribute path=\"Annotation/@id\"       name=\"ANNOTATION_ID\"          type=\"DECIMAL\" />\n" +
						"<attribute path=\"PAIS/@uid\"            name=\"PAIS_UID\"           type=\"VARCHAR\" />\n" +
						"</table>\n" +
						
						"<table name=\"observationOrdinalMultiTable\" tableName=\"PAIS.OBSERVATION_QUANTIFICATION_ORDINAL\">\n" +
						"<attribute path=\"Observation/@id\"              name=\"OBSERVATION_ID\"         type=\"DECIMAL\" />\n" +
						"<attribute path=\"Observation/@name\"            name=\"OBSERVATION_NAME\"   type=\"VARCHAR\" />\n" +
						"<attribute path=\"Observation/@annotatorConfidence\"     name=\"ANNOTATORCONFIDENCE\"    type=\"DOUBLE\" />\n" +
						"<attribute path=\"Quantification/@id\"           name=\"QUANTIFICATION_ID\"  type=\"DECIMAL\" />\n" +
						"<attribute path=\"Quantification/@name\"             name=\"QUANTIFICATION_NAME\"    type=\"VARCHAR\"  />\n" +
						"<attribute path=\"Quantification/@value\"        name=\"QUANTIFICATION_VALUE\"   type=\"DOUBLE\" />\n" +
						"<attribute path=\"Quantification/@dataType\"         name=\"QUANTIFICATION_DATATYPE\"    type=\"VARCHAR\" />\n" +
						"</table>\n" +
						
						"<table name=\"observationNominalMultiTable\" tableName=\"PAIS.OBSERVATION_QUANTIFICATION_NOMINAL\">\n" +
						"<attribute path=\"Observation/@id\"                  name=\"OBSERVATION_ID\"         type=\"DECIMAL\" />\n" +
						"<attribute path=\"Observation/@name\"                name=\"OBSERVATION_NAME\"       type=\"VARCHAR\" />\n" +
						"<attribute path=\"Observation/@annotatorConfidence\"         name=\"ANNOTATORCONFIDENCE\"    type=\"DOUBLE\" />\n" +
						"<attribute path=\"Quantification/@id\"               name=\"QUANTIFICATION_ID\"  type=\"DECIMAL\" />\n" +
						"<attribute path=\"Quantification/@name\"             name=\"QUANTIFICATION_NAME\"    type=\"VARCHAR\" />\n" +
						"<attribute path=\"Quantification/@value\"                name=\"QUANTIFICATION_VALUE\"   type=\"VARCHAR\" />\n" +
						"</table>\n" +
						
						"<table name=\"observationSingleTable\" tableName=\"\">\n" +
						"<attribute path=\"Provenance/@id\"                       name=\"PROVENANCE_ID\"          type=\"DECIMAL\" />\n" +
						"<attribute path=\"Markup/@id\"                           name=\"MARKUP_ID\"              type=\"DECIMAL\" />\n" +
						"<attribute path=\"Annotation/@id\"                       name=\"ANNOTATION_ID\"          type=\"DECIMAL\" />\n" +
						"<attribute path=\"Region/@name\"                         name=\"TILENAME\"               type=\"VARCHAR\" />\n" +
						"<attribute path=\"PAIS/@uid\"                            name=\"PAIS_UID\"               type=\"VARCHAR\" />\n" +
						"</table>\n" +
						"<table name=\"markupPolygonTable\" tableName=\"PAIS.MARKUP_nameoftable\">\n" +
						"<attribute path=\"ImageReference/@name\" 	name=\"NAME\" 					type=\"VARCHAR\" />\n" +
						"<attribute path=\"GeometricShape/@id\"   name=\"GEOMETRICSHAPE_ID\"          type=\"DECIMAL\" />\n" +
						"<attribute path=\"Markup/@id\"           name=\"MARKUP_ID\"          type=\"DECIMAL\" />\n" +
						"<attribute path=\"Annotation/@id\"       name=\"ANNOTATION_ID\"          type=\"DECIMAL\" />\n" +
						"<attribute path=\"GeometricShape/@points\" name=\"POLYGON\"              type=\"VARCHAR\" />\n" +
						"<attribute path=\"Provenance@id\"        name=\"PROVENANCE_ID\"          type=\"DECIMAL\" />\n" +
						"<attribute path=\"Region/@name\"         name=\"TILENAME\"           type=\"VARCHAR\" />\n" +
						"<attribute path=\"PAIS/@uid\"            name=\"PAIS_UID\"           type=\"VARCHAR\" />\n" +
						"</table>\n" +
						"</tables>\n" +
				"</loadingconfig>";
	
	 
	
	public static String attribute = "<attribute name=\"featurename\"   type=\"featuretype\" />";
	//delete one directory
	public static boolean deleteDir(File Dir)
	{
		if(!Dir.exists())return false;
		if(Dir.isFile())
			{
			   Dir.delete();
			   return true;
			}
		if(Dir.isDirectory())
		{
			for(File f:Dir.listFiles())
			{
				deleteDir(f);
			}
			Dir.delete();
			return true;
		}
		
		return false;
	}
	
	public static void openDir(String strPath) {
	    String[] execString = new String[2];
	    String filePath = null;
	        String osName = System.getProperty("os.name");
	    if (osName.toLowerCase().startsWith("windows")) {
	        // Window System;
	        execString[0] = "explorer";
	        try {
	        filePath = strPath.replace("/", "\\");
	        } catch (Exception ex) {
	        filePath = strPath;
	        }
	    } else {
	        // Unix or Linux;
	        execString[0] = "nautilus";
	        filePath = strPath;
	    }
	 
	    execString[1] = filePath;
	    try {
	        Runtime.getRuntime().exec(execString);
	    } catch (Exception ex) {
	        ex.printStackTrace();
	        
	    }
	    }
	
	public static String generateloadingConfigXML(String tablename,HashMap<String,String[]> featuresMap)
	{
		StringBuffer sb = new StringBuffer();
		for(String key:featuresMap.keySet())
		{
			if(featuresMap.get(key)[1].equalsIgnoreCase("selected"))
			{
				sb.append(attribute.replaceAll("featurename", key).replaceAll("featuretype", featuresMap.get(key)[0]));	
			}
		}
		return lcxml.replaceAll("nameoftable", tablename).replaceAll("tableofflat", sb.toString());
	}
	
	public static void main(String[] args)
	{
		System.out.println(readFileWithReturn("create_tables_base.sql"));
		/*
		HashMap<String,String[]> featuresMap = new HashMap<String,String[]>();
		featuresMap.put("AREA", new String[]{"DOUBLE","selected"});
		featuresMap.put("PERIMETER", new String[]{"DOUBLE","selected"});
		System.out.println(generateloadingConfigXML("terry",featuresMap));
		*/
	}
	
	public static String readFileWithReturn(String file)
	{
		StringBuffer sb = new StringBuffer();
		try {
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			while(br.ready())
			{
				sb.append(br.readLine());
				sb.append("\n");	
			}
			br.close();
			fr.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sb.toString();
	}
	

}
