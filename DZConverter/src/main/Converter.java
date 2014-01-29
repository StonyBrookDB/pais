package main;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Properties;

public class Converter{

    public static Properties property;

    public static String imagexml = "<?xml version=\"1.0\" encoding=\"utf-8\"?><Image TileSize=\"tilesizevalue\" Overlap=\"1\" Format=\"formatvalue\" ServerFormat=\"Default\" xmlns=\"http://schemas.microsoft.com/deepzoom/2009\"><Size Width=\"widthvalue\" Height=\"heightvalue\" /></Image>";
    public static String sparseimage = "<?xml version=\"1.0\"?>\n"+
                                       "<SceneGraph version=\"1\">\n"+
                                       "<AspectRatio>ratiovalue</AspectRatio>\n"+
                                       "<BackgroundColor>000000</BackgroundColor>\n"+
                                       "<SceneNode>\n"+
                                       "<FileName>filepathvalue</FileName>\n"+
                                       "<x>0</x>\n"+
                                       "<y>0</y>\n"+
                                       "<Width>1</Width>\n"+
                                       "<Height>1</Height>\n"+
                                       "<ZOrder>1</ZOrder>\n"+
                                       "</SceneNode>\n"+
                                       "</SceneGraph>";	
   public static String scenexml = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"+
                                   "<m:Ad xmlns=\"http://schemas.microsoft.com/client/2007\" xmlns:x=\"http://schemas.microsoft.com/winfx/2006/xaml\" xmlns:vsm=\"clr-namespace:System.Windows;assembly=System.Windows\" xmlns:m=\"clr-namespace:HeadLight.Player.Model;assembly=HeadLight.Player\" xmlns:v=\"clr-namespace:HeadLight.Player.View;assembly=HeadLight.Player\" xmlns:u=\"clr-namespace:HeadLight.Player.Platform.Menu;assembly=HeadLight.Player\" xmlns:p=\"clr-namespace:HeadLight.Player.Platform;assembly=HeadLight.Player\" xmlns:c=\"clr-namespace:HeadLight.Player.Model.Configuration;assembly=HeadLight.Player\">\n"+
                                   "<m:Ad.MsiData>\n"+
                                   "<m:MsiData Source=\"{x:Null}\" AspectRatio=\"ratiovalue\" Width=\"smallwidthvalue\" Height=\"smallheightvalue\" TileWidth=\"tilesizevalue\" TileHeight=\"tilesizevalue\" TileOverlap=\"1\" />\n"+
                                   "</m:Ad.MsiData>\n"+
                                   "<m:Ad.AdItems>\n"+
                                   "<m:AdItem Title=\"titlevalue\" FilePath=\"filepathvalue\" ZOrder=\"1\" X=\"0\" Y=\"0\" Width=\"1\" Height=\"1\" p:Ref.Key=\"filenamevalue\" />\n"+
                                   "</m:Ad.AdItems>\n";
    public static Properties readProperty(File file)
	{
		Properties property = new Properties();
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
		    String line = null;
		    String kv[];
		    while((line=reader.readLine())!=null)
		    {
		    	kv = line.split("=");
		    	property.put(kv[0], kv[1]);
		    }
		    reader.close();
		    //file.delete();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return property;
		
	}
	
	public static boolean runcmd(String cmd)
	{
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
	
	public static void generateConfig(Properties property,String outputpath)
	{
		try {
			BufferedOutputStream bf = new BufferedOutputStream(new FileOutputStream(outputpath+File.separator+"image.xml"),1024);
		    PrintStream ps = new PrintStream(bf,false);
		    String imagestr = imagexml;
		    imagestr = imagestr.replaceAll("tilesizevalue", property.getProperty("tilesize"));
		    imagestr = imagestr.replaceAll("formatvalue", property.getProperty("format"));
		    imagestr = imagestr.replaceAll("widthvalue", property.getProperty("width"));
		    imagestr = imagestr.replaceAll("heightvalue", property.getProperty("height"));
		    ps.println(imagestr);
		    ps.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			BufferedOutputStream bf = new BufferedOutputStream(new FileOutputStream(outputpath+File.separator+"SparseImageSceneGraph.xml"),1024);
		    PrintStream ps = new PrintStream(bf,false);
		    String sparseimagestr = sparseimage;
		    sparseimagestr = sparseimagestr.replaceAll("filepathvalue", property.getProperty("imagepath"));
		    sparseimagestr = sparseimagestr.replaceAll("ratiovalue", property.getProperty("ratio"));
		    
		    ps.println(sparseimagestr);
		    ps.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			BufferedOutputStream bf = new BufferedOutputStream(new FileOutputStream(outputpath+File.separator+"scene.xml"),1024);
		    PrintStream ps = new PrintStream(bf,false);
		    String scenexmlstr = scenexml;
		    scenexmlstr = scenexmlstr.replaceAll("filepathvalue", property.getProperty("imagepath"));
		    scenexmlstr = scenexmlstr.replaceAll("ratiovalue", property.getProperty("ratio"));
		    scenexmlstr = scenexmlstr.replaceAll("filenamevalue", property.getProperty("imagename"));
		    scenexmlstr = scenexmlstr.replaceAll("titlevalue", property.getProperty("title"));
		    scenexmlstr = scenexmlstr.replaceAll("smallwidthvalue", property.getProperty("smallwidth"));
		    scenexmlstr = scenexmlstr.replaceAll("smallheightvalue", property.getProperty("smallheight"));
		    scenexmlstr = scenexmlstr.replaceAll("tilesizevalue", property.getProperty("tilesize"));
		    ps.print(scenexmlstr);
		    ps.flush();
		    BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream("scene.xml")));
		    String line;
		    int linecount = 0;
		    while((line=reader.readLine())!=null)
		    {
		    	if(linecount++==0)
		    	  ps.println("<m:Ad.Viewer>");
		    	else
		    	  ps.println(line);
		    }
		    reader.close();
		    
		    ps.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	public static boolean generate(String propertyPath)
	{
		File propertyFile = new File(propertyPath);
		File rootFile = propertyFile.getParentFile();
		
		Properties property = readProperty(propertyFile);
		int width = Integer.parseInt(property.getProperty("width"));
		int height = Integer.parseInt(property.getProperty("height"));
		double ratio = (double)(width)/height;
		int smallwidth = width/100;
		int smallheight = height/100;
		
		property.put("smallwidth", String.valueOf(smallwidth));
		property.put("smallheight", String.valueOf(smallheight));
		property.put("ratio", String.valueOf(ratio));

		generateConfig(property,rootFile.getAbsolutePath());
		return true;

	}
	
	public static ArrayList<String> getPropertyList(String rootpath)
	{
		ArrayList<String> result = new ArrayList<String>();
		
		File file = new File(rootpath);
		if(file.isFile()&&file.getName().equals("property"))
			result.add(file.getAbsolutePath());
		else if(file.isDirectory())
		{
			if(file.getName().equalsIgnoreCase("image_files"))
				return result;
			File filelist[] = file.listFiles();
			for(File f:filelist)
			{
				result.addAll(getPropertyList(f.getAbsolutePath()));
			}
		}
		return result;
	}
	
	public static void main(String args[])
	{
		if(args.length<1)
		{
			System.out.println("java -jar rootpath");
			return;
		}
		ArrayList<String> properties = Converter.getPropertyList(args[0]);
		for(String s:properties)
		{
		  generate(s);
		}
	}

}
