package edu.emory.bmi.validation;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;
import java.util.StringTokenizer;

public class MaskXMLParser {

	
	public static String xml2svg (String xmlFileName){
		StringBuffer svg = new StringBuffer("<svg xmlns=\"http://www.w3.org/2000/svg\" version=\"1.1\">\n<polygon points=\"");
		String style = "style=\"fill:rgba(255,255,0,0.3); fill-opacity:0.5; stroke:rgb(0,128,0);stroke-width:2\"";
		try {
			BufferedReader br = new BufferedReader(new FileReader(xmlFileName));
			String strLine = "";
			String token = "";
			StringTokenizer st = null;
			double x = 0, y = 0;
			//StringBuffer svg = new StringBuffer("<svg> <polygon points=\"");
			while ((strLine = br.readLine()) != null) {
				if (strLine.contains("Point ")){
					int xStart = strLine.indexOf('\"');
					int xEnd = strLine.indexOf("\" Y");
					int yStart = strLine.indexOf("Y=\"");
					int yEnd = strLine.indexOf("\" />");
					x = Double.parseDouble(strLine.substring(xStart+1, xEnd) );
					y = Double.parseDouble(strLine.substring(yStart+3, yEnd) );			
					svg.append( String.valueOf ( Math.round(x) ) +","  +  String.valueOf ( Math.round(y) ) +" ");					
				}
			}
		} catch (IOException e) {					
			e.printStackTrace();
		}	
		svg.append("\" ");
		svg.append(style + " /> \n");
		svg.append("</svg> \n");
		return svg.toString();
	}


	public static String getMaskPath (String annotationPath) {        
        Properties properties = new Properties();
        try {
                properties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream(annotationPath));
        } catch (IOException e1) {
          System.out.println("Load stream Error");
          e1.printStackTrace();
        }
        String path = properties.getProperty("mask.path");
        return path;
}
	
	public static String getAnnotationPath (String annotationPath) {        
        Properties properties = new Properties();
        try {
                properties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream(annotationPath));
        } catch (IOException e1) {
          System.out.println("Load stream Error");
          e1.printStackTrace();
        }
        String path = properties.getProperty("annotation.path");
        return path;
}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String fileName = "F:\\Projects\\miccaichallengesampledata\\human\\segmentation\\xml\\TCGA-02-0006-01Z-00-DX1_data.xml";
		String svg = xml2svg(fileName);
		System.out.println(svg);
	}

}
