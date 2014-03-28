package edu.emory.cci.imagedb.FileDataCollector;

import java.io.IOException;

public class OpenSlideToolsBin{
	
	public static String toolPath = "openslidetools ";
	
	
	public static boolean getThumbnailWSI(String WSIFile, String tbFile, int length)
	{
		
		String cmd = toolPath;
		return runcmd(cmd+WSIFile+"  "+tbFile+" "+length+" 1");
	}
	public static boolean getDimension(String WSIFile, String tmpFile)
	{
		String cmd = toolPath;
		return runcmd(cmd+WSIFile+" "+tmpFile+" 2");
	}
	public static boolean getRegionImage(String inFile, String outFile, String formate, int coordinateSX, int coordinateSY, int imageWidth, int imageHeight)
	{
		String cmd = toolPath;
		return runcmd(cmd+ inFile+" "+outFile+" "+formate+" "+coordinateSX+" "+ coordinateSY+" "+imageWidth+" "+imageHeight+" 3");
	}
	
	public static boolean getThumbnailTiled(String inFile, String outFile, int length, int coordinateSX, int coordinateSY, int imageWidth, int imageHeight)
	{
		String cmd = toolPath;
		return runcmd(cmd+ inFile+" "+outFile+" "+length+" "+coordinateSX+" "+ coordinateSY+" "+imageWidth+" "+imageHeight+" 4");
	}
	
	public static boolean runcmd(String cmd)
	{
		System.out.println(cmd);
		Process pro;
		try {
			pro = Runtime.getRuntime().exec(cmd);
			pro.waitFor();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} catch (InterruptedException e) {
			e.printStackTrace();
			return false;
		}
		
		
		return true;
		
	}
	
	public static void main(String[] args)
	{
		//System.out.println(System.getProperty("user.dir")+File.separator+"region ");
		//OpenSlideToolsBin.getDimension("/home/db2inst1/image/TCGA-06-0644-01Z-00-DX1.svs","/home/db2inst1/Dropbox/dimention.txt");
		OpenSlideToolsBin.getThumbnailTiled("/home/db2inst1/image/TCGA-06-0644-01Z-00-DX1.svs","/home/db2inst1/Dropbox/test400.jpg",100,0,0,4096,4096);		
		//OpenSlideToolsBin.getRegionImage("/home/db2inst1/image/TCGA-06-0644-01Z-00-DX1.svs","/home/db2inst1/Dropbox/test.jpg","jpeg",1111,1111,1111,1111);		
	}
	
}