package edu.emory.cci.pais.api;

import java.io.File;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.Properties;
import java.util.StringTokenizer;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.MatrixParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.codehaus.jettison.json.JSONObject;

import edu.emory.cci.imagedb.FileDataCollector.OpenSlideToolsBin;
import edu.emory.cci.pais.dataloader.db2helper.PAISDBHelper;

//Sets the path to base URL 
@Path("/")
public class WebAPI {
	
	static PAISDBHelper db =  new PAISDBHelper("connection.properties");
    static String thumbnailDir = System.getProperty("java.io.tmpdir")+File.separator+"Thumbnails"+File.separator;
	CachingManager manager = new CachingManager(db);
	Queries queries = new Queries();
	/*
	 * PAIS Queries
	 */
	
	// e.g.: /pais/list
	String query_getPaisUids = queries.getQuery("getPaisUids");
	String query_getImageReferenceUIDs = queries.getQuery("getImageReferenceUIDs");
	String query_getSlideUids = queries.getQuery("getSlideUids");
	String query_getPatientUIDs = queries.getQuery("getPatientUIDs");
	String query_getRegionsfromPAISUID = queries.getQuery("regionsfromPAISUID");
	
	// e.g.: /pais/markups/density
	String query_getNuclearDensityByTile = queries.getQuery("getNuclearDensityByTile");
	String query_getNuclearDensityByImage = queries.getQuery("getNuclearDensityByImage");
	String query_getNuclearDensityByDoc = queries.getQuery("getNuclearDensityByDoc");
	String query_getNuclearDensityGroupByImageuid = queries.getQuery("getNuclearDensityGroupByImageuid");
	String query_getNuclearDensityGroupByDoc = queries.getQuery("getNuclearDensityGroupByDoc");
	
	
	// e.g.: /pais/markups/boundaries
	String query_getBoundariesFromTile = queries.getQuery("getBoundariesFromTile");
	String query_getBoundariesFromRectangleFromTile = queries.getQuery("getBoundariesFromRectangleFromTile");
	String query_getBoundariesFromPolygon = queries.getQuery("getBoundariesFromPolygon");
	String query_getBoundariesOfPointFromTile = queries.getQuery("getBoundariesOfPointFromTile");
	String query_getIntersectionRatio = queries.getQuery("getIntersectionRatio");
	
	String query_getAnnotationByAnnotationID = queries.getQuery("annotationByID");
	String query_getAnnotationsByImageID = queries.getQuery("annotationsByimageID");
	String query_postHumanAnnotation = queries.getQuery("insertHumanAnnotation");
	
	// e.g.: /pais/markups/centroids
	String query_getPolygonCentroidsFromTile = queries.getQuery("getPolygonCentroidsFromTile");
	String query_getPolygonCentroidsFromWindow = queries.getQuery("getPolygonCentroidsFromWindow");
	String query_getCentroidsFromPolygon = queries.getQuery("getCentroidsFromPolygon");
	//String query_getPolygonCentroidsFromPaisuid = queries.getQuery("getPolygonCentroidsFromPaisuid");
	
	// e.g.: /pais/documents
	String query_getPaisDocFromTile = queries.getQuery("getPaisDocFromTile");
	
	// e.g.: /pais/features
	String query_getFeaturesFromTile = queries.getQuery("getFeaturesFromTile");
	String query_getFeaturesFromRectangleOrPolygon = queries.getQuery("getFeaturesFromRectangleOrPolygon");
	String query_getFeatureHistogram = queries.getQuery("getFeatureHistogram");
	
	// e.g.: /pais/features/aggregation
	String query_getMeanFeatureVectorByPAISUID = queries.getQuery("getMeanFeatureVectorByPAISUID");
	String query_getMeanFeatureVectorForAllPAISUID = queries.getQuery("getMeanFeatureVectorForAllPAISUID");
	//String query_getMeanFeatureVectorAsTableByPAISUID = queries.getQuery("getMeanFeatureVectorAsTableByPAISUID");
	String query_getPreMeanFeatureVectorByPAISUID = queries.getQuery("getPreMeanFeatureVectorByPAISUID");
	String query_getPreMeanFeatureVectorForImage = queries.getQuery("getPreMeanFeatureVectorForImage");
	String query_getPreMeanFeatureVectorForPatient = queries.getQuery("getPreMeanFeatureVectorForPatient");
	
	/*
	 * IMAGE Queries
	 */
	
	// e.g.: /images/list
	String query_getImageUids = queries.getQuery("getImageUids");
	String query_getImageUidsByPatientId = queries.getQuery("getImageUidsByPatientId");
	String query_getImageUidsByStudyName = queries.getQuery("getImageUidsByStudyName");
	String query_getImageDetails = queries.getQuery("getImageDetails");
	String query_getTileNames = queries.getQuery("getTileNames");
	String query_getImagePatientUids = queries.getQuery("getImagePatientUids");
	String query_getTileNameOfPoint = queries.getQuery("getTileNameOfPoint");
	
	// e.g.: /images/image
	//String query_getSubRegionTileImage = queries.getQuery("getSubRegionTileImage");
	String query_getSubRegionImage = queries.getQuery("getSubRegionImage");
	String query_getSubRegionImageFromPAISUID = queries.getQuery("getSubRegionImageFromPAISUID");
	String query_getImageLocationPath = queries.getQuery("getImageLocationPath");
	String query_getImageLocationPathBypaisuid = queries.getQuery("getImageLocationPathByPAISUID");
	
	// e.g.: /images/thumbnail
	String query_getThumbnailImageByTilename = queries.getQuery("getThumbnailImageByTilename");
	String query_getThumbnailImageByImageUid = queries.getQuery("getThumbnailImageByImageUid");

	
	// e.g.: /images/similarity
	String query_getSimilarNucleiIcons = queries.getQuery("getSimilarNucleiIcons");
	
	
	// Additional Queries
	String query_getBoundariesFromPaisUidFromTile = queries.getQuery("getBoundariesFromPaisUidFromTile");
	// String query_getBoundariesFromPaisUidFromTileWithParam = queries.getQuery("getBoundariesFromPaisUidFromTileWithParam");
	//String query_getRegionImageDummy = queries.getQuery("getRegionImageDummy");
	
	
	String query_getTiledImageLocationPath = queries.getQuery("getTiledImageLocationPath");
	String query_getTileNamesByImageUid = queries.getQuery("getTileNamesByImageUid");
	String query_getTileListsByTilesetName = queries.getQuery("getTileListsByTilesetName");
	
	//patient queries
	String query_patientlist=queries.getQuery("getPatientBarcode");
	String query_patientchar=queries.getQuery("getPatientChar");
	String query_patientidfrompaisuid=queries.getQuery("patientidfrompaisuid");
	String query_patientfeatures=queries.getQuery("patientfeatures");
	String query_patientfeatureBybarcode=queries.getQuery("patientfeatureBybarcode");

/*******************************************************************************************/
/************************************PAIS list queries**************************************/
/*******************************************************************************************/
		
	// e.g.: /pais/list/paisuids
	// e.g.: /pais/list/paisuids;format=html
	// e.g.: /pais/list/paisuids;role=human;format=html
	// e.g.: /pais/list/paisuids;role=algorithm;format=html
	@GET
	@Path("/pais/list/paisuids")
	public Response getPaisUids(@DefaultValue("algorithm") @MatrixParam("role") String role, @DefaultValue("html") @MatrixParam("format") String format){
		Properties props = new Properties();
		props.put("1", role);
		PreparedStatement pstmt = manager.setPreparedStatementAndParams("getPaisUids", query_getPaisUids, props);
		ResultSet rs = APIHelper.getResultSetFromPreparedStatement(pstmt);
		return APIHelper.setResponseByFormat(format, rs);
	}	
	//  e.g.: /pais/list/imageuids
	//  e.g.: /pais/list/imageuids;format=html
	@GET
	@Path("/pais/list/imageuids")
	public Response getImageReferenceUIDs(@DefaultValue("html") @MatrixParam("format") String format) {
		ResultSet rs = APIHelper.getResultSet(db, query_getImageReferenceUIDs); 				
		//String queryResult = APIHelper.resultSet2html(rs);		
		return APIHelper.setResponseByFormat(format, rs);
	}
	
	// e.g.: /pais/list/slideuids	
	// e.g.: /pais/list/slideuids;format=html
	@GET
	@Path("/pais/list/slideuids")
	public Response getSlideUids(@DefaultValue("html") @MatrixParam("format") String format){
		ResultSet rs = APIHelper.getResultSet(db, query_getSlideUids);
		return APIHelper.setResponseByFormat(format, rs);
	}
	
	// e.g.: /pais/list/patientids	
	// e.g.: /pais/list/patientuids;format=html
	@GET
	@Path("/pais/list/patientuids")
	public Response getPatientUids(@DefaultValue("html") @MatrixParam("format") String format){
		ResultSet rs = APIHelper.getResultSet(db, query_getPatientUIDs);
		return APIHelper.setResponseByFormat(format, rs);
	}
	
	
	// e.g.: /pais/list/summary	
	@GET
	@Path("/pais/list/summary")
	public Response getSummary(){
		    
		String imageCountQuery="select count(ID) from pi.image";
		String patientCountQuery = "select count(distinct patientid) from pi.patient";
		String algrithmdocumentCountQuery= "select count(distinct pais_uid) from pais.collection where role='algorithm'";
		String humandocumentCountQuery= "select count(distinct pais_uid) from pais.collection where role='human'";

		//statement=con1.createStatement(); 
		Connection con = db.getDBConnection();
		ResultSet rs1;
		StringBuffer content=new StringBuffer();
		content.append("<result>");
		try {
			rs1 = con.createStatement().executeQuery(imageCountQuery);
			rs1.next();
			content.append("<row images=\"" + rs1.getString(1) + "\"/>"); 
			ResultSet rs2=con.createStatement().executeQuery(patientCountQuery);
			rs2.next();
			content.append("<row patients=\"" + rs2.getString(1) + "\"/>"); 
			ResultSet rs3=con.createStatement().executeQuery(algrithmdocumentCountQuery);
			rs3.next();
			content.append("<row algorithm=\"" + rs3.getString(1) + "\"/>"); 
			ResultSet rs4=con.createStatement().executeQuery(humandocumentCountQuery);
			rs4.next();
			content.append("<row human=\"" + rs4.getString(1) + "\"/>"); 
			
		} catch (SQLException e) {
		}
		
		content.append("</result>");
		return Response.ok(content.toString()).type(MediaType.APPLICATION_XML).build();
	}
	
	// e.g.: /pais/list/tile/regions;paisuid=TCGA-06-0122-01Z-00-DX1_20x_20x_NS-MORPH_1;format=json	
		@GET
		@Path("/pais/list/tile/regions")
		public Response gettileRegions(@DefaultValue("xml") @MatrixParam("format") String format,
				@MatrixParam("paisuid") String paisuid
				){
			if(paisuid == null)
			{
				String content = "paisuid parameter is mandatory" +"\n" + "Ex, /pais/list/tile/regions;paisuid=TCGA-27-1836-01Z-DX2-0000004096-0000016384";
				return Response.ok(content).type(MediaType.TEXT_PLAIN).build();
			}
			else {	
			Properties props = new Properties();
			props.put("1", paisuid);		
			PreparedStatement pstmt = manager.setPreparedStatementAndParams("regionsfromPAISUID", query_getRegionsfromPAISUID, props);
			ResultSet rs = APIHelper.getResultSetFromPreparedStatement(pstmt);
			return APIHelper.setResponseByFormat(format, rs);
			}
		}
	
	/*******************************************************************************************/
	/************************************PAIS markup queries************************************/
	/*******************************************************************************************/	
	
	// e.g.: /pais/markups/density/tile;tilename=TCGA-27-1836-01Z-DX2-0000004096-0000016384 
	@GET
	@Path("/pais/markups/density/tile")
	public Response getNuclearDensityByTile(
			@MatrixParam("tilename") String tilename, 
			@DefaultValue("html") @MatrixParam("format") String format) {
		if(tilename == null)
		{
			String content = "tilename parameter is mandatory" +"\n" + "Ex, /pais/markups/density/tile;tilename=TCGA-27-1836-01Z-DX2-0000004096-0000016384";
			return Response.ok(content).type(MediaType.TEXT_PLAIN).build();
		}
		else {	
			Properties props = new Properties();
			props.put("1", tilename);		
			PreparedStatement pstmt = manager.setPreparedStatementAndParams("getNuclearDensityByTile", query_getNuclearDensityByTile, props);
			ResultSet rs = APIHelper.getResultSetFromPreparedStatement(pstmt);
			return APIHelper.setResponseByFormat(format, rs);
		}
	}
	
	// e.g.: /pais/markups/density/image;imageuid=TCGA-27-1836-01Z-DX2_20x;method=NS-MORPH;seqno=1
	@GET
	@Path("/pais/markups/density/image")
	public Response getNuclearDensityByImage(
			@MatrixParam("imageuid") String imageuid, 
    		@DefaultValue("NS-MORPH") @MatrixParam("method") String methodname, 
    		@DefaultValue("1") @MatrixParam("seqno") String seqno, 
			@DefaultValue("html") @MatrixParam("format") String format) {
		if(imageuid == null)
		{
			String content = "imageuid parameter is mandatory" +"\n" + "Ex, /pais/markups/density/image;imageuid=TCGA-27-1836-01Z-DX2_20x";
			return Response.ok(content).type(MediaType.TEXT_PLAIN).build();
		}
		else {
			Properties props = new Properties();
			props.put("1", imageuid);
			props.put("2", methodname);
			props.put("3", seqno);
			PreparedStatement pstmt = manager.setPreparedStatementAndParams("getNuclearDensityByImage", query_getNuclearDensityByImage, props);
			ResultSet rs = APIHelper.getResultSetFromPreparedStatement(pstmt);
			return APIHelper.setResponseByFormat(format, rs);
		}
	}
	
	// e.g.: /pais/markups/density/document;paisuid=TCGA-27-1836-01Z-DX2_20x_20x_NS-MORPH_1;format=html
	@GET
	@Path("/pais/markups/density/document")
	public Response getNuclearDensityByPAISUID(
			@MatrixParam("paisuid") String paisuid, 
			@DefaultValue("html") @MatrixParam("format") String format) {	
		if(paisuid == null)
		{
			String content = "paisuid parameter is mandatory" +"\n" + "Ex, /pais/markups/density/document;paisuid=TCGA-27-1836-01Z-DX2_20x_20x_NS-MORPH_1";
			return Response.ok(content).type(MediaType.TEXT_PLAIN).build();
		}
		else {
			Properties props = new Properties();
			props.put("1", paisuid);		
			PreparedStatement pstmt = manager.setPreparedStatementAndParams("getNuclearDensityByDoc", query_getNuclearDensityByDoc, props);
			ResultSet rs = APIHelper.getResultSetFromPreparedStatement(pstmt);
			return APIHelper.setResponseByFormat(format, rs);
		}		
	}
	
	//  e.g.: /pais/markups/density/groupbyimage
	//  e.g.: /pais/markups/density/groupbyimage;method=NS-MORPH;seqno=1;format=html
	@GET
    @Path("/pais/markups/density/groupbyimage")
    public Response getNuclearDensityGroupByImageuid(
    		@DefaultValue("NS-MORPH") @MatrixParam("method") String methodname, 
    		@DefaultValue("1") @MatrixParam("seqno") String seqno, @DefaultValue("html") @MatrixParam("format") String format){
    	
		//System.out.println("image uid: " + imageuid);
		
    	Properties props = new Properties();
    	props.put("1", methodname);
    	props.put("2",seqno);
    	PreparedStatement pstmt = manager.setPreparedStatementAndParams("getNuclearDensityGroupByImageuid", query_getNuclearDensityGroupByImageuid, props);				
		ResultSet rs = APIHelper.getResultSetFromPreparedStatement(pstmt); 		
		return APIHelper.setResponseByFormat(format, rs);	
    }
	
	//  e.g.: /pais/markups/density/groupbydocument;format=html
	@GET
    @Path("/pais/markups/density/groupbydocument")
    public Response getNuclearDensityGroupByDoc(
    		@DefaultValue("html") @MatrixParam("format") String format){
    	
		//System.out.println("");
		
		PreparedStatement pstmt = manager.setPreparedStatementAndParams("getNuclearDensityGroupByDoc", query_getNuclearDensityGroupByDoc, null);
		ResultSet rs = APIHelper.getResultSetFromPreparedStatement(pstmt); 		
		return APIHelper.setResponseByFormat(format, rs);	
    }

	
	// e.g.: /pais/markups/boundaries/tile;paisuid=TCGA-27-1836-01Z-DX2_20x_20x_NS-MORPH_1;tilename=TCGA-27-1836-01Z-DX2-0000004096-0000016384
	// e.g.: /pais/markups/boundaries/tile;paisuid=TCGA-27-1836-01Z-DX2_20x_20x_NS-MORPH_1;tilename=TCGA-27-1836-01Z-DX2-0000004096-0000016384;samplingrate=1
	// e.g.: /pais/markups/boundaries/tile;paisuid=TCGA-27-1836-01Z-DX2_20x_20x_NS-MORPH_1;tilename=TCGA-27-1836-01Z-DX2-0000004096-0000016384;samplingrate=1;format=html
	@GET
	@Path("/pais/markups/boundaries/tile")
	public Response getBoundariesFromTile(@MatrixParam("paisuid") String pais_uid, 
			@MatrixParam("tilename") String tilename, 
			@DefaultValue("1") @MatrixParam("samplingrate") int samplingRate,  
			@DefaultValue("html") @MatrixParam("format") String format) {
		if(pais_uid == null || tilename==null)
		{
			String content = "paisuid and tilename parameters are mandatory" +"\n" + "Ex, /pais/markups/boundaries/tile;paisuid=TCGA-27-1836-01Z-DX2_20x_20x_NS-MORPH_1;tilename=TCGA-27-1836-01Z-DX2-0000004096-0000016384";
			return Response.ok(content).type(MediaType.TEXT_PLAIN).build();
		}
		else {
			Properties props = new Properties();
			props.put("1", pais_uid);	
			props.put("2", tilename);	
			// System.out.println(pais_uid + ", " +  tilename);
			// System.out.println("samplerate:" + samplingrate);
			PreparedStatement pstmt = manager.setPreparedStatementAndParams("getBoundariesFromTile", query_getBoundariesFromTile, props);			
			ResultSet rs = APIHelper.getResultSetFromPreparedStatement(pstmt); 
			Properties props1 = new Properties();
			props1.put("1", tilename);	
			pstmt = manager.setPreparedStatementAndParams("getTilePosition", queries.getQuery("getTilePosition"), props1);
			ResultSet rs1 = APIHelper.getResultSetFromPreparedStatement(pstmt);
			String[] rst = APIHelper.getSingleRecordFromResultSet(rs1);
			if ("svg".equalsIgnoreCase(format) )
				return APIHelper.setSVGResponse(rs, Math.round(Float.parseFloat(rst[0])), Math.round(Float.parseFloat(rst[1])), samplingRate );
			else return APIHelper.setResponseByFormat(format, rs);
		}
	} 
		
	// e.g.: /pais/markups/boundaries/window;paisuid=TCGA-27-1836-01Z-DX2_20x;x=4096;y=16384;w=300;h=300;samplingrate=3;format=svg
	@GET
	@Path("/pais/markups/boundaries/window")
	public Response getBoundariesFromRectangleFromTile(
			@MatrixParam("paisuid") String paisuid,
			@MatrixParam("x") String x, @MatrixParam("y") String y,
			@MatrixParam("h") String h,  @MatrixParam("w") String w,
			@DefaultValue("1") @MatrixParam("samplingrate") int samplingRate, 
			@DefaultValue("html") @MatrixParam("format") String format){
		
		if(paisuid==null || x==null || y==null || h==null || w==null)
		{
			String content = "paisuid, tilename and window coordinates parameters are mandatory" +"\n" + "Ex, /pais/markups/boundaries/window;paisuid=TCGA-27-1836-01Z-DX2_20x_20x_NS-MORPH_1;tilename=TCGA-27-1836-01Z-DX2-0000004096-0000016384;x=4096;y=16384;w=300;h=300";
			return Response.ok(content).type(MediaType.TEXT_PLAIN).build();
		}
		
		String x_w = String.valueOf(Integer.parseInt(x) + Integer.parseInt(w) ); 
		String y_h = String.valueOf(Integer.parseInt(y) + Integer.parseInt(h) ); 
		
		StringBuffer buf = new StringBuffer("polygon((");
		buf.append(x + " " + y + ",");
		buf.append(x_w + " " + y + ",");
		buf.append(x_w + " " + y_h + ",");
		buf.append(x + " " + y_h + ",");
		buf.append(x + " " + y + "))");
				
		Properties props = new Properties();
		props.put("1", paisuid);		
		props.put("2", buf.toString() );
		PreparedStatement pstmt = manager.setPreparedStatementAndParams("getBoundariesFromRectangleFromTile", query_getBoundariesFromRectangleFromTile, props);			
        System.out.println(paisuid+" "+buf.toString());
		ResultSet rs = APIHelper.getResultSetFromPreparedStatement(pstmt); 	
		if ("svg".equalsIgnoreCase(format) )
			return APIHelper.setSVGResponse(rs, Integer.parseInt(x), Integer.parseInt(y), samplingRate );		
		else return APIHelper.setResponseByFormat(format, rs);			
	}	
	
	// e.g.: /pais/markups/boundaries/polygon;paisuid=TCGA-27-1836-01Z-DX2_20x_20x_NS-MORPH_1;tilename=TCGA-27-1836-01Z-DX2-0000004096-0000016384;polygon="4096 16384,4496 16384,4496 20384,4096 20384,4096 16384";samplingrate=1;format=html
	@GET
	@Path("/pais/markups/boundaries/polygon")
	public Response getBoundariesFromPolygonFromTile(
			@MatrixParam("paisuid") String pais_uid, @MatrixParam("tilename") String tilename,
			@MatrixParam("polygon") String polygon, 
			@DefaultValue("1") @QueryParam("samplingrate") int samplingRate, @DefaultValue("html") @MatrixParam("format") String format){

		if(pais_uid == null || tilename==null || polygon==null)
		{
			String content = "paisuid, tilename and polygon parameters are mandatory" +"\n" + "Ex, /pais/markups/boundaries/polygon;paisuid=TCGA-27-1836-01Z-DX2_20x_20x_NS-MORPH_1;tilename=TCGA-27-1836-01Z-DX2-0000004096-0000016384;polygon=\"4096 16384,4496 16384,4496 20384,4096 20384,4096 16384\"";
			return Response.ok(content).type(MediaType.TEXT_PLAIN).build();
		}
			// update polygon format to match query expression 
			String updatePolygon = polygon.replace('"', ' ');
			String updatePolygon1 = "polygon" + "((" + updatePolygon + "))";
			// System.out.println("updated polygon: " + updatePolygon); 
	        // polygon((4096 16384,4496 16384,4496 20384,4096 20384,4096 16384))
			Properties props = new Properties();
			props.put("1", pais_uid);	
			props.put("2", tilename);	
			props.put("3", updatePolygon1);	
			// System.out.println(pais_uid + ", " +  tilename);
			PreparedStatement pstmt = manager.setPreparedStatementAndParams("getBoundariesFromPolygon", query_getBoundariesFromPolygon, props);			
			ResultSet rs = APIHelper.getResultSetFromPreparedStatement(pstmt);  
			StringTokenizer st = new StringTokenizer(updatePolygon,",");
			StringTokenizer st1 = new StringTokenizer(st.nextToken()," ");
			String x = st1.nextToken();
			String y = st1.nextToken();
			while (st.hasMoreTokens()) {
				st1 = new StringTokenizer(st.nextToken()," ");
				String x1 = st1.nextToken();
				String y1 = st1.nextToken();
				if (Integer.parseInt(x) > Integer.parseInt(x1))
					x = x1;
				if (Integer.parseInt(y) > Integer.parseInt(y1))
					y = y1;	
			}
				
			
			if ("svg".equalsIgnoreCase(format) )
				return APIHelper.setSVGResponse(rs, Integer.parseInt(x), Integer.parseInt(y), samplingRate );	
	    	return APIHelper.setResponseByFormat(format, rs);
	}
	
	// e.g.: /pais/markups/boundaries/containingpoint;imageuid=TCGA-27-1836-01Z-DX2_20x;x=4096;y=16384
	@GET
	@Path("/pais/markups/boundaries/containingpoint")
	public Response getBoundariesOfPonitFromTile(
			@MatrixParam("imageuid") String imageuid, 
			@MatrixParam("x") String x, @MatrixParam("y") String y,
			@DefaultValue("NS-MORPH") @MatrixParam("method") String methodname, 
			@DefaultValue("1") @QueryParam("seqno") int seqno, 
			@DefaultValue("1") @MatrixParam("samplingrate") int samplingRate, 
			@DefaultValue("html") @MatrixParam("format") String format){
		
		if(imageuid==null || x==null || y==null)
		{
			String content = "imageuid and point coordinate parameters are mandatory" +"\n" + "Ex, /pais/markups/boundaries/containingpoint;imageuid=TCGA-27-1836-01Z-DX2_20x;x=300;y=300;";
			return Response.ok(content).type(MediaType.TEXT_PLAIN).build();
		}
		StringBuffer buf = new StringBuffer("point(");
		buf.append(x + " " + y + ")");
		
		//System.out.println(buf);
		
		Properties props = new Properties();
		props.put("1", imageuid);	
		props.put("2", methodname);
		props.put("3", seqno);	
		props.put("4", buf.toString());
		
		PreparedStatement pstmt = manager.setPreparedStatementAndParams("getBoundariesOfPointFromTile", query_getBoundariesOfPointFromTile, props);			
		//System.out.println(query_getBoundariesOfPointFromTile);
		ResultSet rs = APIHelper.getResultSetFromPreparedStatement(pstmt); 	
				
		return APIHelper.setResponseByFormat(format, rs);			
	}	

	// e.g.: /pais/markups/boundaries/intersection;tilename=gbm1.1-0000040960-0000040960;paisuid1=gbm1.1_40x_20x_NS-MORPH_1;paisuid2=gbm1.1_40x_20x_NS-MORPH_2
	@GET
	@Path("/pais/markups/boundaries/intersection")
	public Response getIntersectionRatioCentriodDistance(
			@MatrixParam("tilename") String tilename, 
			@MatrixParam("paisuid1") String paisuid1, @MatrixParam("paisuid2") String paisuid2,
			@DefaultValue("html") @MatrixParam("format") String format){
		if(paisuid1 == null || paisuid2==null || tilename==null)
		{
			String content = "tilename, paisuid1 and paisuid2 parameters are mandatory" +"\n" + "Ex, /pais/markups/boundaries/intersection;tilename=gbm1.1-0000040960-0000040960;paisuid1=gbm1.1_40x_20x_NS-MORPH_1;paisuid2=gbm1.1_40x_20x_NS-MORPH_2";
			return Response.ok(content).type(MediaType.TEXT_PLAIN).build();
		}
		else {
			Properties props = new Properties();
			props.put("1", tilename);	
			props.put("2", paisuid1);
			props.put("3", tilename);
			props.put("4", paisuid2);
		//System.out.println(pais_uid1 + ", " +  tilename);
			PreparedStatement pstmt = manager.setPreparedStatementAndParams("getIntersectionRatio", query_getIntersectionRatio, props);			
			ResultSet rs = APIHelper.getResultSetFromPreparedStatement(pstmt); 	
			return APIHelper.setResponseByFormat(format, rs);
		}		
	}
	
	// e.g. : /pais/markups/centroids/tile;tilename=TCGA-27-1836-01Z-DX2-0000004096-0000016384
	// e.g. : /pais/markups/centroids/tile;tilename=TCGA-27-1836-01Z-DX2-0000004096-0000016384;methodname=NS-MORPH
	// e.g. : /pais/markups/centroids/tile;tilename=TCGA-27-1836-01Z-DX2-0000004096-0000016384;methodname=NS-MORPH;seqno=1;format=html
	@GET
	@Path("/pais/markups/centroids/tile")
	public Response getPolygonCentroidsFromTile(
			@MatrixParam("tilename") String tilename, 
			@DefaultValue("NS-MORPH") @MatrixParam("methodname") String methodname, 
			@DefaultValue("1") @MatrixParam("seqno") String seqno, 
			@DefaultValue("html") @MatrixParam("format") String format) {
		if (tilename == null)
		{
			String content = "Tilename parameter is mandatory" +"\n" + "Ex, /pais/markups/centroids/tile;tilename=TCGA-27-1836-01Z-DX2-0000004096-0000016384";
			return Response.ok(content).type(MediaType.TEXT_PLAIN).build();
		}
		Properties props = new Properties();
		props.put("1", tilename);	
		props.put("2", methodname);	
		props.put("3", seqno);

		PreparedStatement pstmt = manager.setPreparedStatementAndParams("getPolygonCentroidsFromTile", query_getPolygonCentroidsFromTile, props);			
		ResultSet rs = APIHelper.getResultSetFromPreparedStatement(pstmt); 				
    	return APIHelper.setResponseByFormat(format, rs);		
	}
	
	// e.g. : /pais/markups/centroids/window;imageuid=TCGA-27-1836-01Z-DX2_20x;x=4096;y=16384;w=300;h=300
	@GET
	@Path("/pais/markups/centroids/window")
	public Response getCentroidsFromRectangleFromTile(
			@MatrixParam("imageuid") String imageuid, 
			@MatrixParam("x") String x, @MatrixParam("y") String y,
			@MatrixParam("h") String h,  @MatrixParam("w") String w, 
			@DefaultValue("html") @MatrixParam("format") String format){
		
		if (imageuid == null || x == null || y==null || h==null || w==null)
		{
			String content = "Imageuid and window position parameters are mandatory" +"\n" + "Ex, /pais/markups/centroids/window;imageuid=TCGA-27-1836-01Z-DX2_20x;x=4096;y=4096;w=300;h=300";
			return Response.ok(content).type(MediaType.TEXT_PLAIN).build();
		}
		
		String x_w = String.valueOf(Integer.parseInt(x) + Integer.parseInt(w) ); 
		String y_h = String.valueOf(Integer.parseInt(y) + Integer.parseInt(h) ); 
		
		StringBuffer buf = new StringBuffer("polygon((");
		buf.append(x + " " + y + ",");
		buf.append(x_w + " " + y + ",");
		buf.append(x_w + " " + y_h + ",");
		buf.append(x + " " + y_h + ",");
		buf.append(x + " " + y + "))");
		
		//System.out.println(buf);
		
		Properties props = new Properties();
	
		props.put("1", imageuid);	
		props.put("2", buf.toString() );	
		//System.out.println(pais_uid + ", " +  tilename);
		PreparedStatement pstmt = manager.setPreparedStatementAndParams("getPolygonCentroidsFromWindow", query_getPolygonCentroidsFromWindow, props);			
		ResultSet rs = APIHelper.getResultSetFromPreparedStatement(pstmt); 	
		
		return APIHelper.setResponseByFormat(format, rs);			
	}
	
	// e.g. : /pais/markups/centroids/polygon;paisuid=TCGA-27-1836-01Z-DX2_20x_20x_NS-MORPH_1;tilename=TCGA-27-1836-01Z-DX2-0000004096-0000016384;polygon="4096 16384,4496 16384,4496 20384,4096 20384,4096 16384"
	@GET
	@Path("/pais/markups/centroids/polygon")
	public Response getCentroidsFromPolygonFromTile(
			@MatrixParam("paisuid") String pais_uid, @MatrixParam("tilename") String tilename,
			@MatrixParam("polygon") String polygon, 
			@DefaultValue("html") @QueryParam("format") String format){

		if (polygon == null || tilename == null || pais_uid==null)
		{
			String content = "Polygon, tilename and PAIS document uid parameters are mandatory" +"\n" + "Ex, /pais/markups/centroids/polygon;paisuid=TCGA-27-1836-01Z-DX2_20x_20x_NS-MORPH_1;tilename=TCGA-27-1836-01Z-DX2-0000004096-0000016384;polygon=\"4096 16384,4496 16384,4496 20384,4096 20384,4096 16384\"";
			return Response.ok(content).type(MediaType.TEXT_PLAIN).build();
		}
			// update polygon format to match query expression 
			String updatePolygon = polygon.replace('"', ' ');
			updatePolygon = "polygon" + "((" + updatePolygon + "))";
		
			Properties props = new Properties();
			props.put("1", pais_uid);	
			props.put("2", tilename);	
			props.put("3", updatePolygon);	
			//System.out.println(query_getCentroidsFromPolygon);
			PreparedStatement pstmt = manager.setPreparedStatementAndParams("getCentroidsFromPolygon", query_getCentroidsFromPolygon, props);	
			
			ResultSet rs = APIHelper.getResultSetFromPreparedStatement(pstmt); 				
	    	return APIHelper.setResponseByFormat(format, rs);
	}
	
	//e.g. : /pais/markups/humanannotation/annotation/get;imagereference_uid=TCGA-27-1836-01Z-DX2_20x;minx=0;miny=0;maxx=100;maxy=100;
	//e.g. : /pais/markups/humanannotation/annotation/get;annotation_id=001;
	@GET
	@Path("/pais/markups/humanannotation/annotation/get")
	public Response getHuamnAnnotation(
			@MatrixParam("imagereference_uid") String imageuid, 
			@MatrixParam("minx") String minx, @MatrixParam("miny") String miny,
			@MatrixParam("maxx") String maxx,  @MatrixParam("maxy") String maxy, 
			@MatrixParam("annotation_id") String annotation_id,
			@DefaultValue("xml") @MatrixParam("format") String format)
	{
		if(annotation_id!=null)
		{
			Properties props = new Properties();
			props.put("1", annotation_id);	
			
            PreparedStatement pstmt = manager.setPreparedStatementAndParams("annotationByID", this.query_getAnnotationByAnnotationID, props);	
			ResultSet rs = APIHelper.getResultSetFromPreparedStatement(pstmt); 				
	    	return APIHelper.setResponseByFormat(format, rs);
		}
		else if(imageuid!=null)
		{
			if(minx==null||miny==null||maxx==null||maxy==null)
			 {
				String content = "minx,miny,maxx,maxy are mandentary if imagereference_uid is defined!";
				return Response.ok(content).type(MediaType.TEXT_PLAIN).build();
			 }
			StringBuffer buf = new StringBuffer("polygon((");
			buf.append(minx + " " + miny + ",");
			buf.append(maxx + " " + miny + ",");
			buf.append(maxx + " " + maxy + ",");
			buf.append(minx + " " + maxy + ",");
			buf.append(minx + " " + miny + "))");
			String winbox = buf.toString();
			Properties props = new Properties();
			props.put("1", imageuid);
			props.put("2", winbox);	
			
            PreparedStatement pstmt = manager.setPreparedStatementAndParams("annotationsByimageID", this.query_getAnnotationsByImageID, props);	
			ResultSet rs = APIHelper.getResultSetFromPreparedStatement(pstmt); 				
	    	return APIHelper.setResponseByFormat(format, rs);
		}
		else
		{
			String content = "imagereference_id or annotation_id should be defined" +"\n" + 
		     "Ex, /pais/markups/humanannotation/annotation/get;imagereference_uid=TCGA-27-1836-01Z-DX2_20x;minx=0;miny=0;maxx=100;maxy=100;\n"+
			 "/pais/markups/humanannotation/annotation;annotation_id=001";
			return Response.ok(content).type(MediaType.TEXT_PLAIN).build();
		}

	}
	
	@POST
    @Path("/pais/markups/humanannotation/annotation/add")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response insertHumanAnnotation(final String data) { 

		String tmp = data;
		tmp = "{\""+tmp.replace("=", "\":\"").replace("&", "\",\"").replace("+", " ").replace("%2C", ",")+"\"}";
		
		System.out.println(tmp);

		JSONObject json;
		String imageuid;
		String boundary;
		String description;
		String windowbox;
		int minx = Integer.MAX_VALUE;
		int miny = Integer.MAX_VALUE;
		int maxx = 0;
		int maxy = 0;
		
		try {
			json = new JSONObject(tmp);
			imageuid = (String) json.get("imagereference_uid");
			boundary = (String) json.get("boundary");
			description = (String) json.getString("description");
			
			String points = boundary.replace("(", "").replace(")", "");
			String point[];
			for(String xy:points.split(","))
			{
				point = xy.split(" ");
				if(Integer.parseInt(point[0])<minx)
					minx = Integer.parseInt(point[0]);
				if(Integer.parseInt(point[0])>maxx)
					maxx = Integer.parseInt(point[0]);
				if(Integer.parseInt(point[1])<miny)
					miny = Integer.parseInt(point[1]);
				if(Integer.parseInt(point[1])>maxy)
					maxy = Integer.parseInt(point[1]);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(201).entity("Error data format: "+data).build(); 
		}
		
	    //windowbox = "polygon(("+minx+" "+miny+","+maxx+" "+miny+","+maxx+" "+maxy+","+minx+" "+maxy+","+minx+" "+miny+"))";
	    StringBuffer buf = new StringBuffer("polygon((");
		buf.append(minx + " " + miny + ",");
		buf.append(maxx + " " + miny + ",");
		buf.append(maxx + " " + maxy + ",");
		buf.append(minx + " " + maxy + ",");
		buf.append(minx + " " + miny + "))");
	    windowbox = buf.toString();
	    
		Properties props = new Properties();
		props.put("1", imageuid);	
		props.put("2", boundary);
		props.put("3", description);
		props.put("4", windowbox);
		
		System.out.println(this.query_postHumanAnnotation);
		
        PreparedStatement pstmt = manager.setPreparedStatementAndParams("insertHumanAnnotation", this.query_postHumanAnnotation, props);	
        
		try {
			pstmt.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		} 				
		
        String result = "data post: "+data;
        
        return Response.status(201).entity(result).build(); 
    }
	
	
	// e.g. : /pais/markups/centroids/paisuid;paisuid=TCGA-27-1836-01Z-DX2_20x_20x_NS-MORPH_1
	// e.g. : /pais/markups/centroids/paisuid;paisuid=TCGA-27-1836-01Z-DX2_20x_20x_NS-MORPH_1;methodname=NS-MORPH
	// e.g. : /pais/markups/centroids/paisuid;paisuid=TCGA-27-1836-01Z-DX2_20x_20x_NS-MORPH_1;methodname=NS-MORPH;seqno=1;format=html
	/**
	@GET
	@Path("/pais/markups/centroids/paisuid")
	public Response getPolygonCentroidsFromPaisuid(
			@MatrixParam("paisuid") String uid, 
			@DefaultValue("NS-MORPH") @MatrixParam("methodname") String methodname, 
			@DefaultValue("1") @MatrixParam("seqno") String seqno, 
			@DefaultValue("html") @MatrixParam("format") String format) {

		Properties props = new Properties();
		props.put("1", uid);	
		props.put("2", methodname);	
		props.put("3", seqno);

		PreparedStatement pstmt = manager.setPreparedStatementAndParams("getPolygonCentroidsFromPaisuid", query_getPolygonCentroidsFromPaisuid, props);			
		ResultSet rs = APIHelper.getResultSetFromPreparedStatement(pstmt); 				
    	return APIHelper.setResponseByFormat(format, rs);		
	}
	*/

	
	/*******************************************************************************************/
	/************************************PAIS document queries************************************/
	/*******************************************************************************************/	
	
	// e.g.: /pais/documents/tile;paisuid=TCGA-27-1836-01Z-DX2_20x_20x_NS-MORPH_1;tilename=TCGA-27-1836-01Z-DX2-0000004096-0000016384
	@GET
	@Path("/pais/documents/tile")
	public Response getPaisDocFromTile(
			@MatrixParam("paisuid") String paisuid,
			@MatrixParam("tilename") String tilename) {
		
		if (tilename == null || paisuid==null)
		{
			String content = "Tilename and PAIS document uid parameters are mandatory" +"\n" + "Ex, /pais/documents/tile;paisuid=TCGA-27-1836-01Z-DX2_20x_20x_NS-MORPH_1;tilename=TCGA-27-1836-01Z-DX2-0000004096-0000016384";
			return Response.ok(content).type(MediaType.TEXT_PLAIN).build();
		}
		
		Properties props = new Properties();
		props.put("1", paisuid);	
		props.put("2", tilename);	

		String docName = paisuid + "_" + tilename;
		PreparedStatement pstmt = manager.setPreparedStatementAndParams("getPaisDocFromTile", query_getPaisDocFromTile, props);			 
		Blob blob = APIHelper.getBlobFromPreparedStatement(pstmt);
		if (blob==null) {
			String content = "No PAIS Document found.";
			return Response.ok(content).type(MediaType.TEXT_PLAIN).build();
		}
		return APIHelper.getImageBlobResponse(blob, docName, "zip");
	}
	 
	
	
	/*******************************************************************************************/
	/************************************PAIS feature queries***********************************/
	/*******************************************************************************************/	

	// e.g. : /pais/features/tile;paisuid=TCGA-15-1448-01Z-00-DX1_20x_20x_NS-MORPH_1;tilename=TCGA-15-1448-01Z-00-DX1-0000040960-0000032768
	@GET
	@Path("/pais/features/tile")
	public Response getFeaturesFromTile( 
			@MatrixParam("paisuid") String pais_uid, 
			@MatrixParam("tilename") String tilename, 
			@DefaultValue("html") @MatrixParam("format") String format){

		if(pais_uid == null || (tilename == null))
		{
			String content = "Tilename and paisuid parameters are mandatory" +"\n" + "Ex, /pais/features/tile;tilename=TCGA-15-1448-01Z-00-DX1-0000040960-0000032768;paisuid=TCGA-15-1448-01Z-00-DX1_20x_20x_NS-MORPH_1;";
			return Response.ok(content).type(MediaType.TEXT_PLAIN).build();
		}
		
		Properties props = new Properties();
		ResultSet rs = null; 
		
		props.put("1", pais_uid);
		props.put("2", tilename);
		
		PreparedStatement pstmt = manager.setPreparedStatementAndParams("getFeaturesFromPAISUID", query_getFeaturesFromTile, props);
		rs = APIHelper.getResultSetFromPreparedStatement(pstmt);
		return APIHelper.setResponseByFormat(format, rs);
	}
	
	//e.g. /pais/features/window;tilename=TCGA-15-1448-01Z-00-DX1-0000040960-0000032768;paisuid=TCGA-15-1448-01Z-00-DX1_20x_20x_NS-MORPH_1;x=40960;y=32768;w=300;h=300	
	@GET
	@Path("/pais/features/window")
	public Response getFeaturesFromWindow( 
			@MatrixParam("paisuid") String pais_uid, 
			@MatrixParam("tilename") String tilename, 
			@MatrixParam("x") String x, @MatrixParam("y") String y,
			@MatrixParam("h") String h,  @MatrixParam("w") String w,
			@DefaultValue("html") @MatrixParam("format") String format){

		if(pais_uid == null || tilename == null || x==null || y==null ||w==null ||h==null)
		{
			String content = "Tilename, paisuid parameters and window position parameters are mandatory" +"\n" + "Ex, /pais/features/window;tilename=TCGA-15-1448-01Z-00-DX1-0000040960-0000032768;paisuid=TCGA-15-1448-01Z-00-DX1_20x_20x_NS-MORPH_1;x=40960;y=32768;w=300;h=300";
			return Response.ok(content).type(MediaType.TEXT_PLAIN).build();
		}
		String x_w = String.valueOf(Integer.parseInt(x) + Integer.parseInt(w) ); 
		String y_h = String.valueOf(Integer.parseInt(y) + Integer.parseInt(h) ); 
		
		StringBuffer buf = new StringBuffer("polygon((");
		buf.append(x + " " + y + ",");
		buf.append(x_w + " " + y + ",");
		buf.append(x_w + " " + y_h + ",");
		buf.append(x + " " + y_h + ",");
		buf.append(x + " " + y + "))");
		
		
		Properties props = new Properties();
		ResultSet rs = null; 
		
		props.put("1", pais_uid);
		props.put("2", tilename);
		props.put("3", buf.toString());
		
		PreparedStatement pstmt = manager.setPreparedStatementAndParams("getFeaturesFromRectangleOrPolygon", query_getFeaturesFromRectangleOrPolygon, props);
		rs = APIHelper.getResultSetFromPreparedStatement(pstmt);
		return APIHelper.setResponseByFormat(format, rs);
	}
	
	// e.g. /pais/features/polygon;paisuid=TCGA-27-1836-01Z-DX2_20x_20x_NS-MORPH_1;tilename=TCGA-27-1836-01Z-DX2-0000004096-0000016384;polygon="4096 16384,4496 16384,4496 20384,4096 20384,4096 16384"
	@GET
	@Path("/pais/features/polygon")
	public Response getFeaturesFromPolygon( 
			@MatrixParam("paisuid") String pais_uid, 
			@MatrixParam("tilename") String tilename, 
			@MatrixParam("polygon") String polygon,
			@DefaultValue("html") @MatrixParam("format") String format){

		if (polygon == null || tilename == null || pais_uid==null)
		{
			String content = "Polygon, tilename and PAIS document uid parameters are mandatory" +"\n" + "Ex, /pais/features/polygon;paisuid=TCGA-27-1836-01Z-DX2_20x_20x_NS-MORPH_1;tilename=TCGA-27-1836-01Z-DX2-0000004096-0000016384;polygon=\"4096 16384,4496 16384,4496 20384,4096 20384,4096 16384\"";
			return Response.ok(content).type(MediaType.TEXT_PLAIN).build();
		}
		String updatePolygon = polygon.replace('"', ' ');
		updatePolygon = "polygon" + "((" + updatePolygon + "))";
	
		Properties props = new Properties();
		props.put("1", pais_uid);	
		props.put("2", tilename);	
		props.put("3", updatePolygon);;
		
		PreparedStatement pstmt = manager.setPreparedStatementAndParams("getFeaturesFromRectangleOrPolygon", query_getFeaturesFromRectangleOrPolygon, props);
		ResultSet rs = APIHelper.getResultSetFromPreparedStatement(pstmt);
		return APIHelper.setResponseByFormat(format, rs);
	}
	
	// e.g. : /pais/features/aggregation/document
	// e.g. : /pais/features/aggregation/document;format=html
	// e.g. : /pais/features/aggregation/document;paisuid=TCGA-27-1836-01Z-DX2_20x_20x_NS-MORPH_1;format=html
	@GET
	@Path("/pais/features/aggregation/document")
	public Response getMeanFeatureVector(
			@MatrixParam("paisuid") String pais_uid,
			@MatrixParam("table") String tableView,
			@DefaultValue("html") @MatrixParam("format") String format){
		Properties props = new Properties();
		ResultSet rs = null; 
		
		if(pais_uid == null){
			rs = APIHelper.getResultSet(db, query_getMeanFeatureVectorForAllPAISUID );
		}
		/*else if (tableView != null ) {
			props.put("1", pais_uid);
			PreparedStatement pstmt = manager.setPreparedStatementAndParams("getMeanFeatureVectorAsTableByPAISUID", 
					query_getMeanFeatureVectorAsTableByPAISUID, props);
			rs = APIHelper.getResultSetFromPreparedStatement(pstmt);	
		}*/
		else {
			props.put("1", pais_uid);
			PreparedStatement pstmt = manager.setPreparedStatementAndParams("getPreMeanFeatureVectorByPAISUID", query_getPreMeanFeatureVectorByPAISUID, props);
			rs = APIHelper.getResultSetFromPreparedStatement(pstmt);
		}
		return APIHelper.setResponseByFormat(format, rs);
	}

	// e.g. : /pais/features/aggregation2/document
	// e.g. : /pais/features/aggregation2/document;format=html
	// e.g. : /pais/features/aggregation2/document;paisuid=TCGA-27-1836-01Z-DX2_20x_20x_NS-MORPH_1;format=html
/*	@GET
	@Path("/pais/features/aggregation2/document")
	public Response getMeanFeatureVector2(
			@MatrixParam("paisuid") String pais_uid,
			@MatrixParam("table") String tableView,
			@DefaultValue("html") @MatrixParam("format") String format){
		Properties props = new Properties();
		ResultSet rs = null; 
		
		if(pais_uid == null){
			rs = APIHelper.getResultSet(db, query_getMeanFeatureVectorForAllPAISUID);
			return APIHelper.setResponseByFormat(format, rs);
		}
		else if (tableView != null ) {
			props.put("1", pais_uid);
			PreparedStatement pstmt = manager.setPreparedStatementAndParams("getMeanFeatureVectorAsTableByPAISUID", 
					queries.getQuery("getMeanFeatureVectorAsTableByPAISUID"), props);
			rs = APIHelper.getResultSetFromPreparedStatement(pstmt);
			return APIHelper.setResponseByFormat(format, rs);
		}
		else {
			
			props.put("1", pais_uid);
			PreparedStatement pstmt = manager.setPreparedStatementAndParams("getMeanFeatureVectorByPAISUID", query_getMeanFeatureVectorByPAISUID, props);
			rs = APIHelper.getResultSetFromPreparedStatement(pstmt);
			return APIHelper.setResponseByFormat(format, rs);
		}
	}	
	*/
	// e.g. /pais/features/aggregation/image;imageuid=TCGA-06-0152-01Z-00-DX6_20x
	@GET
	@Path("/pais/features/aggregation/image")
	public Response getAggregationFeatureOfImage(
			@MatrixParam("imageuid") String imageuid, 
    		@DefaultValue("NS-MORPH") @MatrixParam("method") String methodname, 
    		@DefaultValue("1") @MatrixParam("seqno") String seqno,
			@DefaultValue("html") @MatrixParam("format") String format){ 
		
		if(imageuid == null){
			String content = "imageuid parameter is mandatory" +"\n" + "Ex, /pais/features/aggregation/image;imageuid=TCGA-06-0152-01Z-00-DX6_20x";
			return Response.ok(content).type(MediaType.TEXT_PLAIN).build();
		}
		
		Properties props = new Properties();
		ResultSet rs = null; 
		
		props.put("1", imageuid);
		props.put("2", methodname);
		props.put("3", seqno);
		
		PreparedStatement pstmt = manager.setPreparedStatementAndParams("getPreMeanFeatureVectorForImage", query_getPreMeanFeatureVectorForImage, props);
		rs = APIHelper.getResultSetFromPreparedStatement(pstmt);
	
		return APIHelper.setResponseByFormat(format, rs);
	}
	
	//e.g. /pais/features/aggregation/patient;patientuid=0187
	@GET
	@Path("/pais/features/aggregation/patient")
	public Response getAggregationFeatureOfPatient(
			@MatrixParam("patientuid") String patientuid, 
    		@DefaultValue("NS-MORPH") @MatrixParam("method") String methodname, 
    		@DefaultValue("1") @MatrixParam("seqno") String seqno,
			@DefaultValue("html") @MatrixParam("format") String format){ 
		
		if(patientuid == null){
			String content = "patientuid parameter is mandatory" +"\n" + "Ex, /pais/features/aggregation/patient;patientuid=0187";
			return Response.ok(content).type(MediaType.TEXT_PLAIN).build();
		}
		
		Properties props = new Properties();
		ResultSet rs = null; 
		
		props.put("1", patientuid);
		props.put("2", methodname);
		props.put("3", seqno);
		
		PreparedStatement pstmt = manager.setPreparedStatementAndParams("getPreMeanFeatureVectorForPatient", query_getPreMeanFeatureVectorForPatient, props);
		rs = APIHelper.getResultSetFromPreparedStatement(pstmt);
	
		return APIHelper.setResponseByFormat(format, rs);
	}
	
	// e.g.: /pais/features/histogram;paisuid=TCGA-02-0001-01Z-00-DX1_20x_20x_NS-MORPH_1;feature=area;width=500;height=300;format=PNG
	@GET
	@Path("/pais/features/histogram")
	public Response getFeatureHistogram(
			@MatrixParam("paisuid") String pais_uid,  
			@MatrixParam("feature") String feature, 
			//@MatrixParam("title") String title,
			@MatrixParam("width") int width,
			@MatrixParam("height") int height, 
			@DefaultValue("PNG") @MatrixParam("format") String format){
		
		    if (pais_uid==null || feature==null) {
		    	String content = "paisuid and feature parameters are mandatory" +"\n" + "Ex, /pais/features/histogram;paisuid=TCGA-02-0001-01Z-00-DX1_20x_20x_NS-MORPH_1;feature=area;width=500;height=300;format=PNG";
				return Response.ok(content).type(MediaType.TEXT_PLAIN).build();
		    }
			String title = "Histogram of " + feature + " for document " + pais_uid; 

			Properties props1 = new Properties();
			props1.put("1", pais_uid);	
			props1.put("2", feature.toLowerCase());	
			
			PreparedStatement pstmt = manager.setPreparedStatementAndParams("getFeatureHistogram", queries.getQuery("getFeatureHistogram"), props1);			
			ResultSet rs1 = APIHelper.getResultSetFromPreparedStatement(pstmt);

			
			Properties props2 = new Properties();
			props2.put("1", pais_uid);	
			PreparedStatement pstmt2 = manager.setPreparedStatementAndParams("getFeatureMean", queries.getQuery("getFeatureMean"), props2);			
			ResultSet rs2 = APIHelper.getResultSetFromPreparedStatement(pstmt2); 	
			//String[] rst = APIHelper.getSingleRecordFromResultSet(rs2);		
			//System.out.println(rst[1]);

			
			DecimalFormat dFormat = new DecimalFormat("0.0000");
			String subTitle = "";
			try{
			if(rs2.next())
			{
			if (rs2.getDouble("AVG_"+feature.toUpperCase()) >= 1)
				dFormat = new DecimalFormat("0.00");
			subTitle = "mean=" + dFormat.format(rs2.getDouble("AVG_"+feature.toUpperCase())) + "; stddev=" + dFormat.format(rs2.getDouble("STD_"+feature.toUpperCase()));
			}
			}catch(Exception e)
			{
				e.printStackTrace();
				subTitle="cannot get mean and standard variance";
			}
	
//			String subTitle ="test subtitle";
			return APIHelper.setHistogramResponse(rs1, title, subTitle, width, height, format);
	}
		
	
	/*******************************************************************************************/
	/************************************PIDB image queries*************************************/
	/*******************************************************************************************/
	
	// e.g. : /images/list/imageuids
	// e.g. : /images/list/imageuids;format=html;details=true
	// e.g. : /images/list/imageuids;patientid=0152;format=html
	// e.g. : /images/list/imageuids;studyname=avp;format=html (NOT TEST)

	@GET
	@Path("/images/list/imageuids")
	public Response getImageUids(
			@MatrixParam("patientid") String patientid, 
			@MatrixParam("studyname") String studyname, 
			@MatrixParam("details") String details,
			@DefaultValue("html") @MatrixParam("format") String format){
		
		Properties props = new Properties();
		ResultSet rs = null; 
		
		if((patientid == null) && (studyname == null)){
			if(details==null||!details.equalsIgnoreCase("true"))
			{
			rs = APIHelper.getResultSet(db, query_getImageUids);
			return APIHelper.setResponseByFormat(format, rs);
			}
			else{
					rs = APIHelper.getResultSet(db, query_getImageDetails);
					return APIHelper.setResponseByFormat(format, rs);
				}
		}
		else if(patientid != null){
			props.put("1", patientid);
			PreparedStatement pstmt = manager.setPreparedStatementAndParams("getImageUidsByPatientId", query_getImageUidsByPatientId, props);
			rs = APIHelper.getResultSetFromPreparedStatement(pstmt);
			return APIHelper.setResponseByFormat(format, rs);
		}
		else {
			props.put("1", studyname);
			PreparedStatement pstmt = manager.setPreparedStatementAndParams("getImageUidsByStudyName", query_getImageUidsByStudyName, props);
		
			rs = APIHelper.getResultSetFromPreparedStatement(pstmt);
			return APIHelper.setResponseByFormat(format, rs);
		}
		
	}
	
	// e.g. : /images/list/tilenames
	// e.g. : /images/list/tilenames;imageuid=TCGA-06-0152-01Z-00-DX6_20x
	// e.g. : /images/list/tilenames;imageuid=TCGA-06-0152-01Z-00-DX6_20x;format=html
	// e.g. : /images/list/tilenames;tilset=1;format=html
	@GET
	@Path("/images/list/tilenames")
	public Response getTileNamesByImageUid(
			@MatrixParam("imageuid") String imagereference_uid, 
			@DefaultValue("1") @MatrixParam("tileset") String tileset,
			@DefaultValue("html") @MatrixParam("format") String format){

		Properties props = new Properties();
		ResultSet rs = null; 

		if(imagereference_uid != null){
			props.put("1", imagereference_uid);
			PreparedStatement pstmt = manager.setPreparedStatementAndParams("getTileNamesByImageUid", query_getTileNamesByImageUid, props);
			rs = APIHelper.getResultSetFromPreparedStatement(pstmt);
			return APIHelper.setResponseByFormat(format, rs);
		} 
		else {
			props = new Properties();
			props.put("1", tileset);
			PreparedStatement pstmt = manager.setPreparedStatementAndParams("getTileListsByTilesetName", query_getTileListsByTilesetName, props);
			rs = APIHelper.getResultSetFromPreparedStatement(pstmt);
			return APIHelper.setResponseByFormat(format, rs);
		}
	}

	// e.g.: /images/list/patients	
	// e.g.: /iamages/list/patients;format=html
	@GET
	@Path("/images/list/patients")
	public Response getImagePatientUids(@DefaultValue("html") @MatrixParam("format") String format){
		ResultSet rs = APIHelper.getResultSet(db, query_getImagePatientUids);
		return APIHelper.setResponseByFormat(format, rs);
	}
	
	// e.g.: /images/list/tilenameofpoint;imageuid=TCGA-06-0152-01Z-00-DX6_20x;x=12289;y=5000;format=html
	@GET
	@Path("/images/list/tilenameofpoint")
	public Response getTileNameOfPoint(
		 @MatrixParam("imageuid") String imagereference_uid, 
		 @MatrixParam("x") String x, 
		 @MatrixParam("y") String y,
		 @DefaultValue("html") @MatrixParam("format") String format) {
		if(imagereference_uid == null || x==null || y==null)
		{
			String content = "Imageuid and point coordinate parameters are mandatory" +"\n" + "Ex, /images/list/tilenameofpoint;imageuid=TCGA-06-0152-01Z-00-DX6_20x;x=12289;y=5000";
			return Response.ok(content).type(MediaType.TEXT_PLAIN).build();
		}
		Properties props = new Properties();
		props.put("1", imagereference_uid);	
		props.put("2", x);	
		props.put("3", y);	
		PreparedStatement pstmt = manager.setPreparedStatementAndParams("getTileNameOfPoint", query_getTileNameOfPoint, props);
		ResultSet rs = APIHelper.getResultSetFromPreparedStatement(pstmt); 				
    	return APIHelper.setResponseByFormat(format, rs);		
	}	
	
	
	// e.g.: /images/image/tile;tilename=TCGA-06-0152-01Z-00-DX6-0000016384-0000004096;format=tif
	@GET
	@Path("/images/image/tile")
	public Response getTileImage(  
			@MatrixParam("tilename") String tilename,
			@DefaultValue("1") @MatrixParam("tilesetuid") String tilesetuid,
			@DefaultValue("20x") @MatrixParam("resolution") String resolution,
			@DefaultValue("tif") @MatrixParam("format") String format)  {
			
		if(tilename == null)
		{
			String content = "Tilename parameter is mandatory" +"\n" + "Ex, /images/image/tile;tilename=TCGA-15-1448-01Z-00-DX1-0000040960-0000032768";
			return Response.ok(content).type(MediaType.TEXT_PLAIN).build();
		}
		Properties props = new Properties();
		props.put("1", tilename);	
		//props.put("2", tilesetuid);
		props.put("2", resolution);
		PreparedStatement pstmt = manager.setPreparedStatementAndParams("getTiledImageLocationPath", query_getTiledImageLocationPath, props);			
		ResultSet rs = APIHelper.getResultSetFromPreparedStatement(pstmt);
		System.out.println("resultset"+rs.toString());
		String filePath = APIHelper.getSingleValueFromResultSet(rs);
		if (filePath==null) {
			String errContent = "Requested tile doesn't exist. Please check the tilename parameter.";
			return Response.ok(errContent).type(MediaType.TEXT_PLAIN).build();
		}
			
		if (filePath.endsWith("tif") || filePath.endsWith("tiff") )
			format = "tif";
		return APIHelper.getImageFileResponse(filePath, format);
	
	}

	// e.g.: /images/image/window;imageuid=TCGA-06-0644-01Z-00-DX1_20x;x=7706;y=1444;w=500;h=500;format=PNG
	// e.g.: /images/image/window;paisuid=TCGA-08-0525-01Z-00-DX1_20x_20x_NS-MORPH_1;x=4096;y=16384;w=2000;h=2000;format=PNG;attachment=true
	@GET
	@Path("/images/image/window")
	public Response getRegionImage(
			@MatrixParam("imageuid") String uid,
			@MatrixParam("paisuid") String puid,			
			@MatrixParam("x") String x,@MatrixParam("y") String y,
			@MatrixParam("w") String w,@MatrixParam("h") String h,
			@DefaultValue("PNG") @MatrixParam("format") String format,
			@DefaultValue("false") @MatrixParam("attachment") String attachment) {
		
		if(x == null || y==null || w==null ||h==null)
		{
			String content = "Window position parameters are mandatory" +"\n" + "Ex, /images/image/window;imageuid=TCGA-27-1836-01Z-DX2_20x;x=7706;y=1444;w=500;h=500;format=PNG";
			return Response.ok(content).type(MediaType.TEXT_PLAIN).build();
		}
		
		if (uid==null && puid==null)
		{
			String content = "One of imageuid and paisuid parameters must be definded \n";
			return Response.ok(content).type(MediaType.TEXT_PLAIN).build();
		}
		Properties props = new Properties();
		PreparedStatement pstmt=null;
		//imageuid as parameter
		String id = null;
		if (uid != null ){
			id = uid;
		props.put("1", uid);		
	    pstmt = manager.setPreparedStatementAndParams("getImageLocationPath", query_getImageLocationPath, props);	
		}
		else { // puid != null
	    id = puid;
		props.put("1", puid);
	    pstmt = manager.setPreparedStatementAndParams("getImageLocationPathbypaisuid", query_getImageLocationPathBypaisuid, props);	
	    }
		
		String filepath=null;
		try{
		ResultSet rs = pstmt.executeQuery();
		if(rs.next())
			filepath = rs.getString(1);
		rs.close();
		}catch(Exception e)
		{
			return Response.ok("image path can not get correctlly found!").type(MediaType.TEXT_PLAIN).build();
		}
		if(filepath==null||new File(filepath).exists()==false)
			return Response.ok("image "+filepath+" not found!").type(MediaType.TEXT_PLAIN).build();
		
		String tmpfilename = System.getProperty("java.io.tmpdir")+File.separator+id+"_"+x+"_"+y+"_"+w+"_"+h+"."+format;
		int dx ;
		int dy ;
		int dw ;
		int dh ;
		try{
		dx = Integer.parseInt(x);
		dy = Integer.parseInt(y);
		dw = Integer.parseInt(w);
		dh = Integer.parseInt(h);
		}
		catch(Exception e)
		{
			return getErrPath();
		}
		OpenSlideToolsBin.getRegionImage(filepath, tmpfilename, format, dx, dy, dw,dh);
		//OpenSlideTools.getInstance().getRegionImage(filepath, tmpfilename, format, dx, dy, dw,dh);
		Response rs;
		if(attachment.equalsIgnoreCase("true"))
		{
			rs = APIHelper.getImageFileResponse(tmpfilename, format);
		}
		else{
			rs = APIHelper.getImageStreamResponse(tmpfilename, format);
		}
		//File tmpfile = new File(tmpfilename);
		//if(tmpfile.exists())
		  //tmpfile.delete();
		
		return rs;
	}
	
	// e.g.: /images/image/wsi;imageuid=TCGA-02-0010-01Z-00-DX3_20x
	@GET
	@Path("/images/image/wsi")
	public Response getWholeSlideImage(  
			@MatrixParam("imageuid") String imageuid)  {
			
		if(imageuid == null)
		{
			String content = "Imageuid parameter is mandatory" +"\n" + "Ex, /images/image/wsi;imageuid=TCGA-02-0010-01Z-00-DX3_20x";
			return Response.ok(content).type(MediaType.TEXT_PLAIN).build();
		}
		Properties props = new Properties();
		props.put("1", imageuid);	
		PreparedStatement pstmt = manager.setPreparedStatementAndParams("getImageLocationPath", query_getImageLocationPath, props);			
		ResultSet rs = APIHelper.getResultSetFromPreparedStatement(pstmt); 
		String filePath = APIHelper.getSingleValueFromResultSet(rs);
		
		return APIHelper.getImageFileResponse(filePath, "zip");
	
	}
	
	// e.g.: /images/thumbnail/tile;tilename=TCGA-06-0152-01Z-00-DX6-0000016384-0000004096
	// e.g.: /images/thumbnail/tile;tilename=TCGA-06-0152-01Z-00-DX6-0000016384-0000004096;tilesetuid=1;resolution=20x
	@GET
	@Path("/images/thumbnail/tile")
	public Response getThumbnailImageByTilename(
			@MatrixParam("tilename") String tilename, @DefaultValue("1") @MatrixParam("tilesetuid") String tilesetuid, 
			@DefaultValue("20x") @MatrixParam("resolution") String resolution, @DefaultValue("PNG") @MatrixParam("format") String format) {
		
		if (tilename == null)
		{
			String content = "tilename parameter is mandatory" +"\n" + "Ex, /images/thumbnail/tile;tilename=TCGA-06-0152-01Z-00-DX6-0000016384-0000004096";
			return Response.ok(content).type(MediaType.TEXT_PLAIN).build();
		}
		Properties props = new Properties();
		props.put("1", tilename);	

		String imageName = tilename +"_" + "thumbnail";
		PreparedStatement pstmt = manager.setPreparedStatementAndParams("getThumbnailImageByTilename", query_getThumbnailImageByTilename, props);			 
		Blob blob = APIHelper.getBlobFromPreparedStatement(pstmt);
		if (blob==null) {
			String errContent = "Thumbnail hasn't been generated.";
			return Response.ok(errContent).type(MediaType.TEXT_PLAIN).build();
		}
			
		return APIHelper.getImageBlobResponse(blob, imageName, format);
	}
	/*
	// e.g. /images/thumbnail/wsi;imageuid=TCGA-02-0010-01Z-00-DX3_20x
	@GET
	@Path("/images/thumbnail/wsi")
	public Response getThumbnailImageByImageUid(
			@MatrixParam("imageuid") String imageuid,  
			@DefaultValue("PNG") @MatrixParam("format") String format) {
		
		if (imageuid == null)
		{
			String content = "Imageuid parameter is mandatory" +"\n" + "Ex, /images/thumbnail/wsi;imageuid=TCGA-02-0010-01Z-00-DX3_20x";
			return Response.ok(content).type(MediaType.TEXT_PLAIN).build();
		}
		Properties props = new Properties();
		props.put("1", imageuid);	

		String imageName = imageuid +"_" + "thumbnail";
		PreparedStatement pstmt = manager.setPreparedStatementAndParams("getThumbnailImageByImageUid", query_getThumbnailImageByImageUid, props);			 
		Blob blob = APIHelper.getBlobFromPreparedStatement(pstmt);
		if (blob==null) {
			String errContent = "Thumbnail hasn't been generated.";
			return Response.ok(errContent).type(MediaType.TEXT_PLAIN).build();
		}
		return APIHelper.getImageBlobResponse(blob, imageName, format);
	} 
	*/
	// e.g. /images/thumbnail/wsi;imageuid=TCGA-06-0644-01Z-00-DX1_20x;format=PNG;length=400
		@GET
		@Path("/images/thumbnail/wsi")
		public Response getThumbnailImageByImageUid(
				@MatrixParam("imageuid") String imageuid,  
				@DefaultValue("PNG") @MatrixParam("format") String format,
				@DefaultValue("100") @MatrixParam("length") String lengthstr) {
			
			if (imageuid == null)
			{
				String content = "Imageuid parameter is mandatory" +"\n" + "Ex, /images/thumbnail/new;imageuid=TCGA-06-0644-01Z-00-DX1_20x;format=PNG;length=400";
				return Response.ok(content).type(MediaType.TEXT_PLAIN).build();
			}
			Properties props = new Properties();
			props.put("1", imageuid);	

			String imageName = imageuid +"_"+lengthstr+"."+format;
			File wsithumbdir = new File(thumbnailDir+"wsi");
			if(wsithumbdir.exists()==false)
			{
				wsithumbdir.mkdirs();
				System.out.println("mkdirs "+wsithumbdir.getAbsolutePath());
			}
			String tmpfilename = thumbnailDir+"wsi"+File.separator+imageName;
			File tmpfile = new File(tmpfilename);
			
			if(tmpfile.exists()==false)
			{
			PreparedStatement pstmt = manager.setPreparedStatementAndParams("getImageLocationPath", query_getImageLocationPath, props);	
			String filepath=null;
			try{
			ResultSet rs = pstmt.executeQuery();
			if(rs.next())
				filepath = rs.getString(1);
			rs.close();
			}catch(Exception e)
			{
				return null;
			}
			if(filepath==null||new File(filepath).exists()==false)
				return Response.ok(filepath+" is not found!").type(MediaType.TEXT_PLAIN).build();;	
			OpenSlideToolsBin.getThumbnailWSI(filepath, tmpfilename, Integer.parseInt(lengthstr));
			//OpenSlideTools.getInstance().getThumbnailWSI(filepath, tmpfilename, Integer.parseInt(lengthstr));
			}
			Response rs =  APIHelper.getImageStreamResponse(tmpfilename, format);
			return rs;
		} 
		// e.g. /images/thumbnail/tiledimage;paisuid=TCGA-08-0525-01Z-00-DX1_20x_20x_NS-MORPH_1;format=PNG;length=100;x=0;y=0;w=4096;h=4096
				@GET
				@Path("/images/thumbnail/tiledimage")
				public Response getThumbnailImageByTiledImage(
						@MatrixParam("paisuid") String imageuid,  
						@DefaultValue("png") @MatrixParam("format") String format,
						@DefaultValue("100") @MatrixParam("length") String lengthstr,
						@MatrixParam("x") String x,
						@MatrixParam("y") String y,
						@MatrixParam("w") String w,
						@MatrixParam("h") String h) {
					if (imageuid == null)
					{
						String content = "Imageuid parameter is mandatory" +"\n" + "Ex, /images/thumbnail/tiledimage;imageuid=TCGA-06-0644-01Z-00-DX1_20x;format=PNG;length=400;x=0;y=0;w=4096;h=4096;";
						return Response.ok(content).type(MediaType.TEXT_PLAIN).build();
					}
					String tmpfiledir = thumbnailDir+imageuid;
					if(new File(tmpfiledir).exists()==false)
						new File(tmpfiledir).mkdirs();
					String tmpfilename = tmpfiledir +File.separator+"tiledimage_" +x+"_"+y+"_"+w+"_"+h+"_"+lengthstr+"."+format;
					File tmpfile = new File(tmpfilename);
					if(tmpfile.exists()==false)
					{
						
					Properties props = new Properties();
					props.put("1", imageuid);	

					
					PreparedStatement pstmt = manager.setPreparedStatementAndParams("getImageLocationPathbypaisuid", query_getImageLocationPathBypaisuid, props);	
					String filepath=null;
					
					try{
					ResultSet rs = pstmt.executeQuery();
					if(rs.next())
						filepath = rs.getString(1);
					    rs.close();
					}catch(Exception e)
					{
						return null;
					}
					if(filepath==null||new File(filepath).exists()==false)
						return Response.ok("file "+filepath+" dose not exist").type(MediaType.TEXT_PLAIN).build();	
					
					OpenSlideToolsBin.getThumbnailTiled(filepath, tmpfilename, Integer.parseInt(lengthstr),Integer.parseInt(x),Integer.parseInt(y),Integer.parseInt(w),Integer.parseInt(h));

					//OpenSlideTools.getInstance().getThumbnailTiled(filepath, tmpfilename, Integer.parseInt(lengthstr),Integer.parseInt(x),Integer.parseInt(y),Integer.parseInt(w),Integer.parseInt(h));
					}
					Response rs =  APIHelper.getImageStreamResponse(tmpfilename, format);
					return rs;

				} 
	

	@GET
	@Path("/images/overlay/tile")
	@Produces(MediaType.TEXT_HTML)
	public String getOverlayTile(
			@MatrixParam("paisuid") String paisuid,
			@MatrixParam("tilename") String tilename, 
			@DefaultValue("1") @MatrixParam("samplingrate") int samplingRate, 
			@DefaultValue("tif") @MatrixParam("format") String format) {
			
		if (paisuid==null || tilename==null)
		{
			String html = "<html><body> " + "<p>Paisuid and tilename parameters are mandatory" +"</p>" + "<p>Ex, /images/overlay/tile;paisuid=TCGA-27-1836-01Z-DX2_20x_20x_NS-MORPH_1;tilename=TCGA-27-1836-01Z-DX2-0000004096-0000016384</p>"+"</body></html>";
			return html;
		}
		String imgpath = "/images/image/tile;tilename=" + tilename + ";format=" + format;  
		
		String svgpath = "/pais/markups/boundaries/tile;paisuid=" + paisuid + ";tilename=" + tilename + ";samplingrate=" + samplingRate  + ";format=svg"; 
		///pais/markups/boundaries/window;paisuid=TCGA-27-1836-01Z-DX2_20x_20x_NS-MORPH_1;tilename=TCGA-27-1836-01Z-DX2-0000004096-0000016384;format=svg

		String html = "<html><body> " + 
		"<div style=\"background:url(../.." + imgpath  + ");position:absolute; left:0px; top:0px;\">" +
		"<object height=\"100%\" width=\"100%\" type=\"image/svg+xml\" data=\"" + "../.." + svgpath +  "\" style=\"position:absolute; left:0px; top:0px;\" >" + 
		"</div> </body> </html>";
				
		return html;	 
	}	
	
	
// /images/overlay/window;paisuid=TCGA-27-1836-01Z-DX2_20x_20x_NS-MORPH_1;x=4096;y=16384;w=300;h=300;samplingrate=3;format=jpg	
	@GET
	@Path("/images/overlay/window")
	@Produces(MediaType.TEXT_HTML)
	public String getOverlayWindow(
			@MatrixParam("paisuid") String paisuid,
			@MatrixParam("x") String x, @MatrixParam("y") String y,
			@MatrixParam("h") String h,  @MatrixParam("w") String w,
			@DefaultValue("1") @MatrixParam("samplingrate") int samplingRate, 
			@DefaultValue("JPG") @MatrixParam("format") String format) {
			
		if (paisuid==null || x==null || y==null || h==null || w==null)
		{
			String content = "<html><body>"+"<p>paisuid and window position parameters are mandatory" +"</p>" + "<p>Ex, /images/overlay/window;paisuid=TCGA-27-1836-01Z-DX2_20x_20x_NS-MORPH_1;x=4096;y=16384;w=300;h=300</p>" + "</body></html>";
			return content;
		}
		String imgpath = "/images/image/window;paisuid=" + paisuid  + ";x=" + x +";y=" + y +";w=" + w +";h=" + h + ";format=" + format;  
		///images/image/region;paisuid=TCGA-27-1836-01Z-DX2_20x_20x_NS-MORPH_1;x=4096;y=16384;w=2000;h=2000;format=JPG
		String svgpath = "/pais/markups/boundaries/window;paisuid=" + paisuid  + ";x=" + x +";y=" + y +";w=" + w +";h=" + h + ";samplingrate=" + samplingRate  + ";format=svg"; 
		///pais/markups/boundaries/window;paisuid=TCGA-27-1836-01Z-DX2_20x_20x_NS-MORPH_1;tilename=TCGA-27-1836-01Z-DX2-0000004096-0000016384;x=4096;y=16384;w=300;h=300;format=svg

		int width = Integer.parseInt(w);
		int height = Integer.parseInt(h);
		String html = "<html><body> " + 
		"<div style=\"background:url(../.." + imgpath  + "); width:"+ width +"px; height:"+ height +"px; position:absolute; left:0px; top:0px;\">" +
		"<object height=\"100%\" width=\"100%\" type=\"image/svg+xml\" data=\"" + "../.." + svgpath +  "\" style=\"position:absolute; left:0px; top:0px;\" >" + 
		"</div> </body> </html>";
		
/*		"<div style=\"background:url(../.." + imgpath  + "); width:"+ width +"px; height:"+ height +"px; position:absolute; left:0px; top:0px;\">" +
		"<object type=\"image/svg+xml\" data=\"" + "../.." + svgpath +  "\" style=\"overflow=hidden;position:absolute; left:0px; top:0px; width:"+ width +"px; height:"+ height +"px;\" >" + 
		"</div> </body> </html>";		
*/		
		return html;	 
	}	
	
	

	// /images/similarity/feature;paisuid=TCGA-06-0195-01Z-00-DX5_20x_20x_NS-MORPH_1;rowsize=5;format=png 
	// /images/similarity/feature;rowsize=5;format=png
	@GET
	@Path("/images/similarity/feature")
	public Response  getSimilarNucleiByFeature(
			@DefaultValue("5") @MatrixParam("rowsize") int rowSize,
			@MatrixParam("paisuid") String paisuid,
			@DefaultValue("PNG") @MatrixParam("format") String imgformat,
			@DefaultValue("HTML") @MatrixParam("content") String content) {
		Properties props = new Properties();
		
		if (paisuid == null) {
			PreparedStatement pstmt = manager.setPreparedStatementAndParams("getSimilarNucleiIconsAll", queries.getQuery("getSimilarNucleiIconsAll"), null);			 
			ResultSet rs = APIHelper.getResultSetFromPreparedStatement(pstmt);
			return  APIHelper.getIconImageResponse(rs, rowSize, imgformat, content);
		} 
		else {
		props.put("1", paisuid);
		String query = queries.getQuery("getSimilarNucleiIcons");
		//System.out.println(query);
		//System.out.println("paisuid: " + paisuid);
		PreparedStatement pstmt = manager.setPreparedStatementAndParams("getSimilarNucleiIcons", query, props);			 
		ResultSet rs = APIHelper.getResultSetFromPreparedStatement(pstmt);
		return  APIHelper.getIconImageResponse(rs, rowSize, imgformat, content);
		}
	}
	
	
//	/images/similarity/cluster;imageuid=TCGA-02-0010-01Z-00-DX3_20x;rowsize=10;format=png
// /images/similarity/cluster;imageuid=TCGA-02-0010-01Z-00-DX3_20x;label=2;rowsize=10;format=png	
			
	@GET
	@Path("/images/similarity/cluster")
	public Response getSimilarNucleiByCluster(
			@DefaultValue("10") @MatrixParam("rowsize") int rowSize,
			@MatrixParam("imageuid") String imageuid,
			@DefaultValue("0") @MatrixParam("label") String label,
			@DefaultValue("PNG") @MatrixParam("format") String imgformat,
			@DefaultValue("HTML") @MatrixParam("content") String content) {
		if (imageuid==null) {
			String errMsg = "Imageuid parameter is mandatory \n" + "Ex, /images/similarity/cluster/imageuid=TCGA-02-0010-01Z-00-DX3_20x;label=2";
			return Response.ok(errMsg).type(MediaType.TEXT_PLAIN).build();
		}
		Properties props = new Properties();
		props.put("1", imageuid);
		props.put("2", label);
		PreparedStatement pstmt = manager.setPreparedStatementAndParams("getSimilarNucleiByCluster", queries.getQuery("getSimilarNucleiByCluster"), props);			 
		ResultSet rs = APIHelper.getResultSetFromPreparedStatement(pstmt);
		return  APIHelper.getIconImageResponse(rs, rowSize, imgformat, content);
	}	
	// e.g.: /images/similarity/getImageFromFile;name="C:/Users/wangfsh/AppData/Local/Temp/1323456717848-0/TCGA-06-0168-01Z-00-DX2_20x_20x_NS-MORPH_1/1.PNG";format=PNG
	// /getImageFromFile;name=433a2f55736572732f77616e676673682f417070446174612f4c6f63616c2f54656d702f313332333435363731373834382d302f544347412d30362d303136382d30315a2d30302d4458325f3230785f3230785f4e532d4d4f5250485f312f312e504e47;;format=PNG

	@GET
	@Path("/images/similarity/getImageFromFile")
	public Response getImageFromFile(
			@MatrixParam("name") String filepath,
			@DefaultValue("PNG") @MatrixParam("format") String format) {
		
		String decodedPath = APIHelper.decodePath(filepath );
		File file = new File(decodedPath);
		file.length();
		//System.out.println(decodedPath + " " + file.length() );
		
		ResponseBuilder response = Response.ok((Object) file);
		response.header("Content-type", APIHelper.getContentType(format) );
		return response.build();
	}

	


	// e.g : /images/image/dimension;imagereferenceuid=TCGA-06-0644-01Z-00-DX1_20x;format=html
	@GET
	@Path("/images/image/dimension")
	public Response getImageDimension(@MatrixParam("imagereferenceuid") String imagereference_uid, @MatrixParam("format") String format ){			
		Properties props = new Properties();
		props.put("1", imagereference_uid);
		PreparedStatement pstmt = manager.setPreparedStatementAndParams("getImageDimension", queries.getQuery("getImageDimension"), props);
		ResultSet rs = APIHelper.getResultSetFromPreparedStatement(pstmt);
		return APIHelper.setResponseByFormat(format, rs);
	}
	
	// e.g : /images/image/imagereferences;imagereferenceuid=TCGA-06-0644-01Z-00-DX1_20x;format=xml
		@GET
		@Path("/images/image/imagereferences")
		public Response getImageReferences(@MatrixParam("imagereferenceuid") String imagereference_uid, @MatrixParam("format") String format ){			
			Properties props = new Properties();
			props.put("1", imagereference_uid);
			PreparedStatement pstmt = manager.setPreparedStatementAndParams("paisuidsFromImageReferenceUID", queries.getQuery("paisuidsFromImageReferenceUID"), props);
			ResultSet rs = APIHelper.getResultSetFromPreparedStatement(pstmt);
			return APIHelper.setResponseByFormat(format, rs);
		}

	
	


	// e.g : /images/image/tiling/positions;imagereferenceuid=TCGA-06-0152-01Z-00-DX6_20x;width=4096;height=4096;format=csv
	@GET
	@Path("/images/image/tiling/positions")
	public Response getImageTilingPositions(@MatrixParam("imagereferenceuid") String imagereference_uid, @MatrixParam("width") int width, @MatrixParam("height") int height, @DefaultValue("csv") @MatrixParam("format") String format ){			
		Properties props = new Properties();
		props.put("1", imagereference_uid);
		PreparedStatement pstmt = manager.setPreparedStatementAndParams("getImageDimension", queries.getQuery("getImageDimension"), props);
		ResultSet rs = APIHelper.getResultSetFromPreparedStatement(pstmt);
		return APIHelper.setTilePositionsResponse(rs, width, height, format);
		//return APIHelper.setResponseByFormat(format, rs);
	}

	/*
	 * Additional Queries

	
	// e.g : /image/path;imagereferenceuid=TCGA-08-0358-01Z-00-DX1_40X;format=html
	@GET
	@Path("/image/path")
	public Response getImageLocationPath(@MatrixParam("imagereferenceuid") String imagereference_uid, @MatrixParam("format") String format ){
				
		Properties props = new Properties();
		props.put("1", imagereference_uid);
		PreparedStatement pstmt = manager.setPreparedStatementAndParams("getImageLocationPath", query_getImageLocationPath, props);
		System.out.println("imageref=" + imagereference_uid);		
		ResultSet rs = APIHelper.getResultSetFromPreparedStatement(pstmt);
		try {
			System.out.println(rs.getFetchSize() );
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return APIHelper.setResponseByFormat(format, rs);
	}
	
	// e.g : /tiledimage/path/TCGA-06-0152-01Z-00-DX6-0000016384-0000004096;format=html
	@GET
	@Path("/tiledimage/path/{tilename}")
	public Response getTiledImageLocationPath(@PathParam("tilename") String tilename, @MatrixParam("format") String format){
				
		Properties props = new Properties();
		props.put("1", tilename);
		PreparedStatement pstmt = manager.setPreparedStatementAndParams("getTiledImageLocationPath", query_getTiledImageLocationPath, props);
		
		ResultSet rs = APIHelper.getResultSetFromPreparedStatement(pstmt);
		return APIHelper.setResponseByFormat(format, rs);
	}
	

	

	
	//patient queries
	
		//list patient barcode: /patient/list
		@GET
		@Path("/patient/list")
		public Response listPatient(@DefaultValue("html") @MatrixParam("format") String format){
			Properties props = new Properties();
			PreparedStatement pstmt = manager.setPreparedStatementAndParams("getPatientList", query_patientlist, props);
			ResultSet rs = APIHelper.getResultSetFromPreparedStatement(pstmt);
			return APIHelper.setResponseByFormat(format, rs);
		}
		
		//get patient characteristic from barcode: /patient/char;patientid=TCGA-02-0011
		@GET
		@Path("/patient/char")
		public Response getPatientChar(@MatrixParam("patientid") String patientid, @DefaultValue("html") @MatrixParam("format") String format){
			Properties props = new Properties();
			props.put("1", patientid);
			PreparedStatement pstmt = manager.setPreparedStatementAndParams("getPatientChar", query_patientchar, props);
			ResultSet rs = APIHelper.getResultSetFromPreparedStatement(pstmt);
			return APIHelper.setResponseByFormat(format, rs);
		}
		
		//get patient barcode from paisuid: /patient/barcode;paisuid=TCGA-02-0011-01B-01-TS1_20x_20x_RG-HUMAN_1
		@GET
		@Path("/patient/barcode")
		public Response getPatientIDfromUID(@MatrixParam("paisuid") String paisuid, @DefaultValue("html") @MatrixParam("format") String format){
			Properties props = new Properties();
			props.put("1", paisuid);
			PreparedStatement pstmt = manager.setPreparedStatementAndParams("getPatientIDfromUID", query_patientidfrompaisuid, props);
			ResultSet rs = APIHelper.getResultSetFromPreparedStatement(pstmt);
			return APIHelper.setResponseByFormat(format, rs);
		}
		*/
	       //get patient features from patient id, or all patients without patient id
	       //   /patient/features;barcode=TCGA-02-0001
			@GET
			@Path("/patient/features")
			public Response getPatientfeaturesfromPID(@DefaultValue("xml") @MatrixParam("format") String format,
					@MatrixParam("patientid") String patientid,
					@MatrixParam("barcode") String barcode
					){
				Properties props = new Properties();
				PreparedStatement pstmt=null;
				if(barcode==null)
				{
				pstmt = manager.setPreparedStatementAndParams("patientfeatures", query_patientfeatures, props);
				}
				else{
				props.put("1", barcode);
				pstmt = manager.setPreparedStatementAndParams("patientfeatureBybarcode", query_patientfeatureBybarcode, props);
				}
				ResultSet rs = APIHelper.getResultSetFromPreparedStatement(pstmt);
				return APIHelper.setResponseByFormat(format, rs);
			}

		@GET
		@Path("/errpath")
    	public Response getErrPath(){	
			String content = "The URL couldn't be linked to any exisiting resources." +"\n" + "Please check your query path.";
			return Response.ok(content).type(MediaType.TEXT_PLAIN).build();
			
		}
	
}
