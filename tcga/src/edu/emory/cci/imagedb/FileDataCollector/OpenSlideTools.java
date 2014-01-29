package edu.emory.cci.imagedb.FileDataCollector;

public class OpenSlideTools {
	
	public native static void getThumbnailWSI(String WSIFile, String tbFile, int length);
	public native static void getDimension(String WSIFile, String tmpFile);
	public native static void getRegionImage(String inFile, String outFile, String formate, double coordinateSX, double coordinateSY, double imageWidth, double imageHeight);
	public native static void getThumbnailTiled(String inFile,String outFile,int length, int x, int y, int w, int h);
	static{
	   System.load("/tmp/libopenslidetools.so");
		//System.load("/tmp/libopenslidetools.so");
	  //System.loadLibrary("openSlideTools");
	}
	private OpenSlideTools(){};
	
	private final static OpenSlideTools instance = new OpenSlideTools();

	public static OpenSlideTools getInstance()
	{
		return instance;
	}
	public static void main(String[] args)
	{
		//System.out.println(System.getProperty("user.dir"));
		OpenSlideTools.getInstance().getThumbnailTiled("/home/db2inst1/image/TCGA-06-0644-01Z-00-DX1.svs", "/home/db2inst1/Dropbox/TCGA-06-0644-01Z-00-DX1.PNG", 300,1000, 600, 1000, 1000);
		//OpenSlideTools.getInstance().getRegionImage("/home/db2inst1/image/TCGA-06-0644-01Z-00-DX1.svs", "/tmp/TCGA-06-0644-01Z-00-DX1.PNG", "PNG", 100, 600, 1000, 200);
	}
	
}