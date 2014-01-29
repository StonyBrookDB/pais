package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CacheThumbnail {
	public static String cachepath = "../Thumbs/name.svs.dzi.tif.thumb.jpg";
	//public static String cachepath = "f:\\thumb"+File.separator+"name.svs.dzi.tif.thumb.jpg";

	public static void generate(Connection conn)
	{

		String filename=null;
		String name;
		ArrayList<String> namelist = new ArrayList<String>();
		try {
			System.out.println("updating directories");
			ResultSet namers = conn.createStatement().executeQuery("select name from pi.image");

			while(namers.next())
			{	
				name = namers.getString(1);
				filename = getFilename(name);
				File dir = new File(filename).getParentFile();
				if(dir.exists()==false)
				    {
					   dir.mkdirs();
					   System.out.println(dir.getPath()+"  is created");
				    }
				namelist.add(name);
			}
			
			System.out.println("directories updated successfully!");
			int count = 1;
            
			ResultSet rs = conn.createStatement().executeQuery("select name,thumbnail from pi.image");
			System.out.println("thumnails selected successfully!");
			while(rs.next())
			{
		    count++;
			filename = getFilename(rs.getString(1));
            File newfile = new File(filename);
		    if(newfile.exists()==false)
			{
				  if(newfile.getParentFile().exists()==false)
					  newfile.getParentFile().mkdirs();
				     System.out.println(filename+" is added");
				     saveBlob(rs.getBlob(2),newfile);
			 }
		     else
		     {
		    	   System.out.println(filename+" already exists.");
		     }
			}
			
			System.out.println(count);
		}catch (SQLException e){
			e.printStackTrace();
		}
	}
	
	public static String getFilename(String name)
	{
		String filename = cachepath.replaceAll("name", name);
		String system = System.getProperty("os.name");
		if(system.startsWith("Windows"))
		{
		  if(filename.contains("/"))
		  filename = filename.replace('/', '\\');
		}
		else if(filename.contains("\\"))
		  filename = filename.replace('\\', File.separatorChar);
		
		return filename;
	}
	
	public static void saveBlob(Blob blob,File file)
	{
		if(blob==null)return;
		try {
			     InputStream is = blob.getBinaryStream();   
                 FileOutputStream os = new FileOutputStream(file);   
                 int b;   
                 byte[] buffer = new byte[1024];   
                 while( (b=is.read(buffer)) != -1){   
                 os.write(buffer,0,b);
             }   
             is.close();   
             os.close();   
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	
	public static void main(String[] args) throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException
	{
		Class.forName("com.ibm.db2.jcc.DB2Driver").newInstance();
		String url = "jdbc:db2://europa.cci.emory.edu:50001/tcga";
		String username = "tcgauser";
		String passwd = "userdb4321";
		Connection con1 = DriverManager.getConnection(url, username, passwd);
		System.out.println("connected successfully!");
		CacheThumbnail.generate(con1);
	}
	
}
