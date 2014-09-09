package edu.emory.cci.pais.api;

import java.util.HashMap;


public class MICCAIChallengeQueries {
	
	private String[] featureArray = {

			  "AREA",
			  "PERIMETER",
			  "ECCENTRICITY",
			  "CIRCULARITY",
			  "MAJOR_AXIS",
			  "MINOR_AXIS",  
			  "EXTENT_RATIO",
			  "MEAN_INTENSITY",
			  "MAX_INTENSITY ",
			  "MIN_INTENSITY ",
			  "STD_INTENSITY ",
			  "ENTROPY",
			  "ENERGY",
			  "SKEWNESS",
			  "KURTOSIS",
			  "MEAN_GRADIENT_MAGNITUDE",
			  "STD_GRADIENT_MAGNITUDE",   
			  "ENTROPY_GRADIENT_MAGNITUDE",   
			  "ENERGY_GRADIENT_MAGNITUDE",   
			  "SKEWNESS_GRADIENT_MAGNITUDE", 
			  "KURTOSIS_GRADIENT_MAGNITUDE",   
			  "SUM_CANNY_PIXEL",
			  "MEAN_CANNY_PIXEL",
			"CYTO_H_MeanIntensity", 
			"CYTO_H_MeanMedianDifferenceIntensity", 
			"CYTO_H_MaxIntensity", 
			"CYTO_H_MinIntensity", 
			"CYTO_H_StdIntensity",
			"CYTO_H_Entropy", 
			"CYTO_H_Energy", 
			"CYTO_H_Skewness", 
			"CYTO_H_Kurtosis",
			"CYTO_H_MeanGradMag", 
			"CYTO_H_StdGradMag", 
			"CYTO_H_EntropyGradMag", 
			"CYTO_H_EnergyGradMag",
			"CYTO_H_SkewnessGradMag", 
			"CYTO_H_KurtosisGradMag", 
			"CYTO_H_SumCanny", 
			"CYTO_H_MeanCanny",
			"CYTO_E_MeanIntensity", 
			"CYTO_E_MeanMedianDifferenceIntensity", 
			"CYTO_E_MaxIntensity", 
			"CYTO_E_MinIntensity", 
			"CYTO_E_StdIntensity",
			"CYTO_E_Entropy", 
			"CYTO_E_Energy", 
			"CYTO_E_Skewness", 
			"CYTO_E_Kurtosis",
			"CYTO_E_MeanGradMag", 
			"CYTO_E_StdGradMag", 
			"CYTO_E_EntropyGradMag", 
			"CYTO_E_EnergyGradMag",
			"CYTO_E_SkewnessGradMag", 
			"CYTO_E_KurtosisGradMag", 
			"CYTO_E_SumCanny", 
			"CYTO_E_MeanCanny",
			"CYTO_G_MeanIntensity", 
			"CYTO_G_MeanMedianDifferenceIntensity", 
			"CYTO_G_MaxIntensity", 
			"CYTO_G_MinIntensity", 
			"CYTO_G_StdIntensity",
			"CYTO_G_Entropy", 
			"CYTO_G_Energy", 
			"CYTO_G_Skewness", 
			"CYTO_G_Kurtosis",
			"CYTO_G_MeanGradMag", 
			"CYTO_G_StdGradMag", 
			"CYTO_G_EntropyGradMag", 
			"CYTO_G_EnergyGradMag",
			"CYTO_G_SkewnessGradMag", 
			"CYTO_G_KurtosisGradMag", 
			"CYTO_G_SumCanny", 
			"CYTO_G_MeanCanny" };
			

	private static HashMap<String, String> map = new HashMap<String, String>();
	
	String  query = "SELECT distinct imagereference_uid " +
	"FROM pais.wholeslideimagereference";
	
	public void addQuery(String name, String query){
		map.put(query, query);
	}
	
	public String getQuery(String name){
		return map.get(name);
	}
	
	
	public MICCAIChallengeQueries(){
		addAllQueries();		
	}
	

	
	private void addAllQueries(){
		String name ="";
		String query ="";
	
		name = "getValidationSegmentation";
		//query = "SELECT * FROM MICCAI.maskintersection where user=? ORDER BY image";
		query = "SELECT a.user, a.image, a.intersection/b.union AS ratio " +
				"FROM MICCAI.maskintersection a, MICCAI.maskunion b " +
				"WHERE a.user = b.user AND " +
				" a.image = b.image AND" +
				" a.user = ? " + 
				" order by a.user";		
		map.put(name, query);

		name = "getValidationSegmentationbyuserandimageid";
		//query = "SELECT * FROM MICCAI.maskintersection where user=? ORDER BY image";
		/*query = "SELECT a.intersection/b.union AS ratio " +
				"FROM MICCAI.maskintersection a, MICCAI.maskunion b " +
				"WHERE a.user = b.user AND " +
				" a.image = b.image AND " +
				" a.image = ? AND " +
				" a.user = ?";*/
		query = "WITH NECROSIS AS( " +
				"SELECT i.user, u.image, " + 
				"CASE i.image " +
					"WHEN  NULL THEN 0 " +
					"ELSE i.intersection " +
				"END INTERSECTION, " +
				"CASE u.image " +
					"WHEN  NULL THEN 100 " +
					"ELSE u.UNION " +
				"END UNION " +
				"FROM MICCAI.humanmaskdimension d LEFT OUTER JOIN MICCAI.maskintersection i ON d.image = i.image  LEFT OUTER JOIN MICCAI.maskunion u ON u.image = d.image " +
				"WHERE " + 
				   "d.necrosis = 1 AND " +
				   "i.user = u.user " +
				"), " +
				"NONNECROSIS AS( " +
				"SELECT i.user, u.image, " + 
				"CASE i.image " +
					"WHEN  NULL THEN 0 " +
					"ELSE d.x*d.y - u.union " +
				"END INTERSECTION, " +
				"CASE u.image " +
					"WHEN  NULL THEN 100 " +
					"ELSE d.x*d.y - i.intersection " +
				"END UNION " +
				"FROM MICCAI.humanmaskdimension d LEFT OUTER JOIN MICCAI.maskintersection i ON d.image = i.image  LEFT OUTER JOIN MICCAI.maskunion u ON u.image = d.image " +
				"WHERE " + 
				   "d.necrosis = 0 AND " +
				   "i.user = u.user " +
				"), " +
				"J AS( " +
				"SELECT y.user, y.image, y.intersection/union as jc FROM NECROSIS y " +
				"UNION " +
				"SELECT n.user, n.image, n.intersection/union as jc FROM NONNECROSIS n " +
				") " +
				"SELECT j.user, j.jc as jaccard_coefficient " +
				"FROM j " +
				"WHERE j.user = ? AND j.image = ? ";
		map.put(name, query);
		
		
		
		
		name = "getValidationSegmentationWithTimestamp";
		//query = "SELECT * FROM MICCAI.maskintersection where user=? ORDER BY image";
		query = "SELECT a.user, a.image, a.intersection/b.union AS ratio " + 
				"FROM MICCAI.maskintersection a, MICCAI.maskunion b  " +
				"WHERE a.user = b.user AND " +
				"	 a.image = b.image AND " +
				"	 a.user = ?  AND " +
				"     ? = ( " +
				"       SELECT max(timestamp) FROM miccai.submissiontimestamp t " +
				"       WHERE t.user= ?  AND t.type = 'segmentation' " +
				")";
		map.put(name, query);
		
	
	name = "getValidationSegmentationAll";
	/*query = "SELECT a.user, SUM(a.intersection/b.union) AS TotalRatio " +
			"FROM MICCAI.maskintersection a, MICCAI.maskunion b " +
			"WHERE a.user = b.user AND" +
			"      a.image = b.image AND " +
			"      a.user <> 'human' " +
			"GROUP BY a.user " +
			"ORDER BY TotalRatio DESC";
	map.put(name, query);
    */
	query = "WITH NECROSIS AS( " +
			"SELECT i.user, u.image, " + 
			"CASE i.image " +
				"WHEN  NULL THEN 0 " +
				"ELSE i.intersection " +
			"END INTERSECTION, " +
			"CASE u.image " +
				"WHEN  NULL THEN 100 " +
				"ELSE u.UNION " +
			"END UNION " +
			"FROM MICCAI.humanmaskdimension d LEFT OUTER JOIN MICCAI.maskintersection i ON d.image = i.image  LEFT OUTER JOIN MICCAI.maskunion u ON u.image = d.image " +
			"WHERE " + 
			   "d.necrosis = 1 AND " +
			   "i.user = u.user " +
			"), " +
			"NONNECROSIS AS( " +
			"SELECT i.user, u.image, " + 
			"CASE i.image " +
				"WHEN  NULL THEN 0 " +
				"ELSE d.x*d.y - u.union " +
			"END INTERSECTION, " +
			"CASE u.image " +
				"WHEN  NULL THEN 100 " +
				"ELSE d.x*d.y - i.intersection " +
			"END UNION " +
			"FROM MICCAI.humanmaskdimension d LEFT OUTER JOIN MICCAI.maskintersection i ON d.image = i.image  LEFT OUTER JOIN MICCAI.maskunion u ON u.image = d.image " +
			"WHERE " + 
			   "d.necrosis = 0 AND " +
			   "i.user = u.user " +
			"), " +
			"J AS( " +
			"SELECT y.user, y.image, y.intersection/union as jc FROM NECROSIS y " +
			"UNION " +
			"SELECT n.user, n.image, n.intersection/union as jc FROM NONNECROSIS n " +
			") " +
			"SELECT j.user, avg (j.jc) as avg_jaccard " +
			"FROM j " +
			"GROUP BY j.user " +
			"ORDER BY avg_jaccard DESC ";
	map.put(name, query);
	
	
	name = "getValidationClassificationWithTimestamp";
	//query = "SELECT * FROM MICCAI.maskintersection where user=? ORDER BY image";
	query = 
			"SELECT a.user, a.image, a.label, b.label, " +
			"CASE a.label " +
			"	WHEN  b.label THEN 'TRUE' " +
			"	ELSE 'FALSE' " +
			"END CORRECTNESS " +
			"FROM   MICCAI.classification a, MICCAI.classification b " +
			"WHERE  a.image = b.image AND b.user = 'human' AND a.user <> 'human' AND " +       
			"       a.user = ?  " +
			"AND " +
		    "   ?  = ( " +
			"	SELECT max(timestamp) FROM miccai.submissiontimestamp t " +
		    "    WHERE t.user = ?  AND t.type = 'classification')";					
	map.put(name, query);
	
	
	name = "getValidationClassification";
	//query = "SELECT * FROM MICCAI.maskintersection where user=? ORDER BY image";
	query = 
			"SELECT a.user, a.image, a.label as UserLabel, b.label as HumanLabel, " +
			"CASE a.label " +
			"	WHEN  b.label THEN 'TRUE' " +
			"	ELSE 'FALSE' " +
			"END CORRECTNESS " +
			"FROM   MICCAI.classification a, MICCAI.classification b " +
			"WHERE  a.image = b.image AND b.user = 'human' AND a.user <> 'human' AND " +       
			"       a.user = ?  ";					
	map.put(name, query);
	
	name = "getValidationClassifiationAll";	
	query = "SELECT a.user, " + 
			"CAST( " +
			"count(*)*1.0/ " + 
			"( " +
			"SELECT count(*) AS TOTAL_COUNT " +
			"FROM MICCAI.classification b " +
			"WHERE b.user='human' " +
			") " + 
			"AS DECIMAL(5, 3) ) " +
			"AS CORRECT_COUNT_RATIO " + 
			"FROM   MICCAI.classification b, MICCAI.classification a " +
			"WHERE  b.user = 'human' AND  a.user != 'human' AND " +
			       "a.image = b.image AND " +
			       "UPPER(a.label) = UPPER(b.label) " +  
			"GROUP BY a.user " +
			"ORDER BY CORRECT_COUNT_RATIO DESC ";
			
	/*query = 
			"SELECT a.user, count(*) AS CORRECT_COUNT " +
			"FROM   MICCAI.classification a, MICCAI.classification b " + 
			"WHERE  a.image = b.image AND b.user = 'human' AND a.user <> 'human' AND " +
			"       UPPER(a.label) = UPPER(b.label) " +
			"GROUP BY a.user " +
			"ORDER BY CORRECT_COUNT DESC";		
			*/
	map.put(name, query);
	}	
}
