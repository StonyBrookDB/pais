/**
 * @author Fusheng Wang
 * @organization Emory University
 * This class will provide parser of input "ppm" files into point based representation.
 * The parsed files will be loaded into tables. Human table will include (image, x, y), and user/algorithm table will include (user,image,x,y).
 * There are four parameters: user, inputfile, outputfile, isHuman.
 * For example, to convert an algorithm result, run: 
 * java MaskParser fwang C:\Users\fwang\Downloads\TCGA-02-0006-01Z-00-DX1_mask.ppm C:\Users\fwang\Downloads\TCGA-02-0006-01Z-00-DX1_mask.map false
 * To convert human results, run:   
 * java MaskParser human C:\Users\fwang\Downloads\TCGA-02-0006-01Z-00-DX1_mask.ppm C:\Users\fwang\Downloads\TCGA-02-0006-01Z-00-DX1_mask.map2 true
 * 
 * The *_mask.txt files are the masks for each image tile. These are text files. Each file has the following format: 
 *	Tile_xdim Tile_ydim // N M
 *	Pixel_region_id     // pixel (0,0)
 *	Pixel_region_id     // pixel (0,1)
 *	Pixel_region_id	 // pixel (0,2)
 *	бн
 *	Pixel_region_id    // pixel (N-1,M-1)
 * e.g.: 
 *
 * 2726 2020
 * 2
 * 2
 * 2
 * 2
 * 2
 * 2
 * 2
 * 2
 * 2
 * 2
 * 2
 * 2
 * ...

 * Example tables:
 *  CREATE TABLE MICCAI.usermask(
 *  user VARCHAR(16) NOT NULL,
 *  image VARCHAR(32) NOT NULL,
 *  x INT NOT NULL,
 *  y INT NOT NULL,
 *  PRIMARY KEY (user, image, x, y)
 * )
 * COMPRESS YES
 * IN SPATIALTBS32K;
 * 
 * 
 * CREATE TABLE MICCAI.humanmask(
 *  image VARCHAR(32) NOT NULL,
 *  x INT NOT NULL,
 *  y INT NOT NULL,
 *  PRIMARY KEY (image, x, y)
 * )
 * COMPRESS YES
 * IN SPATIALTBS32K;

 */
package edu.emory.bmi.validation;

import java.io.*;
import java.util.StringTokenizer;
public class MaskParser {

	public static String getFilePrefix(String inputFile){
		
		int location = inputFile.lastIndexOf(File.separator);
		int extLocation = inputFile.lastIndexOf(".");
		return inputFile.substring(location+1, extLocation);
	}
	
	public static void parseMask (String user, String inputFile, String outputFile, boolean isHuman){
		BufferedReader br;
		int width;
		int height;
		String token = " ";
		try {
			br = new BufferedReader(new FileReader(inputFile));
			String strLine = "";
			StringTokenizer st = null;
			strLine =  br.readLine();
			st = new StringTokenizer(strLine, token);
			//System.out.println(strLine);
			width = Integer.parseInt( st.nextToken(token).trim() );
			height = Integer.parseInt( st.nextToken(token).trim() );
			System.out.println(width + ", " + height);
			long count = 0;
			BufferedWriter bw = new BufferedWriter(new FileWriter(outputFile));
			int classification = 0;
			while ((strLine = br.readLine()) != null) {
				classification = Integer.parseInt(strLine);
				//System.out.println( Integer.parseInt(strLine) );
				long x = count/height; 
				long y = count%height;
				//System.out.println( x + ", " + y + ": " +  classification );
				if (classification == 2){
					String imageName = getFilePrefix(inputFile);
					if (!isHuman)
						bw.write(user + "," +  imageName + "," +  x + "," + y + "\n");
					else //human annotations will not include userid
						bw.write(imageName + "," +  x + "," + y + "\n");
				}
				count++;
			}
			bw.close();
			br.close();
		} catch (Exception e){
			e.printStackTrace();
		}
		
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String user = "", inputFile ="", outputFile = "";	
		boolean isHuman = false;
		if (args.length >= 2){
			user = args[0];
			inputFile = args[1];
			outputFile = args[2];
			isHuman = Boolean.parseBoolean( args[3] );
		}
		//System.out.println(inputFile + " " + outputFile);
		MaskParser parser = new MaskParser();
		//inputFile ="C:\\Users\\fwang\\Downloads\\TCGA-02-0006-01Z-00-DX1_mask.ppm";
		//outputFile ="C:\\Users\\fwang\\Downloads\\TCGA-02-0006-01Z-00-DX1_mask.map";
		//user ="human";
		parser.parseMask(user, inputFile, outputFile, isHuman);
		//System.out.println(parser.getFilePrefix(inputFile) );
		
	}

}
