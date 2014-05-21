/**
 * 
 */
package edu.emory.bmi.validation;

import java.io.*;
import java.util.StringTokenizer;

/**
 * @author Fusheng Wang
 *
 */
public class MaskParser {

	
	public void parseMask (String user, String inputFile, String outputFile){
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
				if (classification == 2)
					bw.write(user + "," + x + "," + y + "\n");
				count++;

			}
			bw.close();
		} catch (Exception e){
			e.printStackTrace();
		}
		
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		MaskParser parser = new MaskParser();
		String inputFile ="C:\\Users\\fwang\\Downloads\\TCGA-02-0006-01Z-00-DX1_mask.ppm";
		String outputFile ="C:\\Users\\fwang\\Downloads\\TCGA-02-0006-01Z-00-DX1_mask.map";
		String user ="human";
		parser.parseMask(user, inputFile, outputFile);
		
	}

}
