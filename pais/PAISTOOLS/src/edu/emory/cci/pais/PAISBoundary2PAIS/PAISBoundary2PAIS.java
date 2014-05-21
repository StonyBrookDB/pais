package edu.emory.cci.pais.PAISBoundary2PAIS;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import edu.emory.cci.pais.util.TengUtils;
/**
 * @author dejun teng, Center for Comprehensive Informatics, Emory University
 * @version 1.0
 * This class transfer the Aperio output XML file into fixed point chain file with header
 *
 */
public class PAISBoundary2PAIS {

	private String inpath;
	private String outpath;
	private String tmpdir;
	private String FixerDir=System.getProperty("user.dir")+"/";
	private String prefix = "#CalculationMetadata" + "\n"
	+	"AREA, area, DOUBLE" + "\n"
	+ "PERIMETER, perimeter, DOUBLE" + "\n"
	+ "ECCENTRICITY, eccentricity, DOUBLE" + "\n"
	+ "CIRCULARITY, circularity, DOUBLE" + "\n"
	+ "MAJOR_AXIS, the length of the major axis of the fitted ellipse, DOUBLE" + "\n"
	+ "MINOR_AXIS, the length of the minor axis of the fitted ellipse, DOUBLE" + "\n"
	+ "EXTENT_RATIO, extent ratio, DOUBLE" + "\n"
	+ "MEAN_INTENSITY, mean intensity, DOUBLE" + "\n"
	+ "MAX_INTENSITY, max intensity,  DOUBLE" + "\n"
	+ "MIN_INTENSITY, min intensity, DOUBLE" + "\n"
	+ "STD_INTENSITY, std intensity, DOUBLE" + "\n"
	+ "ENTROPY, entropy,  DOUBLE" + "\n"
	+ "ENERGY, energy, DOUBLE" + "\n"
	+ "SKEWNESS, skewness, DOUBLE" + "\n"
	+ "KURTOSIS, kurtosis, DOUBLE" + "\n"
	+ "MEAN_GRADIENT_MAGNITUDE, mean of magnitude of gradient, DOUBLE" + "\n"
	+ "STD_GRADIENT_MAGNITUDE, std of magnitude of gradient,DOUBLE" + "\n"
	+ "ENTROPY_GRADIENT_MAGNITUDE, entropy of magnitude of gradient, DOUBLE" + "\n"
	+ "ENERGY_GRADIENT_MAGNITUDE, energy of magnitude of gradient, DOUBLE" + "\n"
	+ "SKEWNESS_GRADIENT_MAGNITUDE, skewness of magnitude of gradient, DOUBLE" + "\n"
	+ "KURTOSIS_GRADIENT_MAGNITUDE, kurtosis of magnitude of gradient, DOUBLE" + "\n"
	+ "SUM_CANNY_PIXEL, sum of number of canny pixel, DOUBLE" + "\n"
	+ "MEAN_CANNY_PIXEL, normalized number of canny pixel, DOUBLE" + "\n"
	+ "CYTO_H_MeanIntensity, mean of intensities of pixels in cytoplasmic regions from hematoxylin image component, DOUBLE" + "\n"
	+ "CYTO_H_MeanMedianDifferenceIntensity, the difference between CYTO_H_MeanIntensity and the median of intensities of pixels in cytoplasmic regions from hematoxylin image component, DOUBLE" + "\n"
	+ "CYTO_H_MaxIntensity, max pixel intensity in cytoplasmic regions from hematoxylin image component, DOUBLE" + "\n"
	+ "CYTO_H_MinIntensity, min pixel intensity in cytoplasmic regions from hematoxylin image component, DOUBLE" + "\n"
	+ "CYTO_H_StdIntensity, std pixel intensity in cytoplasmic regions from hematoxylin image component, DOUBLE" + "\n"
	+ "CYTO_H_Entropy, entropy of intensities of pixels in cytoplasmic regions from hematoxylin image component, DOUBLE" + "\n"
	+ "CYTO_H_Energy, energy of intensities of pixels in cytoplasmic regions from hematoxylin image component, DOUBLE" + "\n"
	+ "CYTO_H_Skewness, skewness of intensities of pixels in cytoplasmic regions from hematoxylin image component, DOUBLE" + "\n"
	+ "CYTO_H_Kurtosis, kurtosis of intensities of pixels in cytoplasmic regions from hematoxylin image component, DOUBLE" + "\n"
	+ "CYTO_H_MeanGradMag, mean gradient magnitude of intensities of pixels in cytoplasmic regions from hematoxylin image component, DOUBLE" + "\n"
	+ "CYTO_H_StdGradMag, std gradient magnitude of intensities of pixels in cytoplasmic regions from hematoxylin image component, DOUBLE" + "\n"
	+ "CYTO_H_EntropyGradMag, entropy of gradient magnitude of intensities of pixels in cytoplasmic regions from hematoxylin image component, DOUBLE" + "\n"
	+ "CYTO_H_EnergyGradMag, energy of gradient magnitude of intensities of pixels in cytoplasmic regions from hematoxylin image component, DOUBLE" + "\n"
	+ "CYTO_H_SkewnessGradMag, skewness of gradient magnitude of intensities of pixels in cytoplasmic regions from hematoxylin image component, DOUBLE" + "\n"
	+ "CYTO_H_KurtosisGradMag, kurtosis of gradient magnitude of intensities of pixels in cytoplasmic regions from hematoxylin image component, DOUBLE" + "\n"
	+ "CYTO_H_SumCanny, sum of number of canny pixels in cytoplasmic regions from hematoxylin image component, DOUBLE" + "\n"
	+ "CYTO_H_MeanCanny, normalized number of canny pixels in cytoplasmic regions from hematoxylin image component, DOUBLE" + "\n"
	+ "CYTO_E_MeanIntensity, mean of intensities of pixels in cytoplasmic regions from eosin image component, DOUBLE" + "\n"
	+ "CYTO_E_MeanMedianDifferenceIntensity, the difference between CYTO_E_MeanIntensity and the median of intensities of pixels in cytoplasmic regions from eosin image component, DOUBLE" + "\n"
	+ "CYTO_E_MaxIntensity, max pixel intensity in cytoplasmic regions from eosin image component, DOUBLE" + "\n"
	+ "CYTO_E_MinIntensity, min pixel intensity in cytoplasmic regions from eosin image component, DOUBLE" + "\n"
	+ "CYTO_E_StdIntensity, std pixel intensity in cytoplasmic regions from eosin image component, DOUBLE" + "\n"
	+ "CYTO_E_Entropy, entropy of intensities of pixels in cytoplasmic regions from eosin image component, DOUBLE" + "\n"
	+ "CYTO_E_Energy, energy of intensities of pixels in cytoplasmic regions from eosin image component, DOUBLE" + "\n"
	+ "CYTO_E_Skewness, skewness of intensities of pixels in cytoplasmic regions from eosin image component, DOUBLE" + "\n"
	+ "CYTO_E_Kurtosis, kurtosis of intensities of pixels in cytoplasmic regions from eosin image component, DOUBLE" + "\n"
	+ "CYTO_E_MeanGradMag, mean gradient magnitude of intensities of pixels in cytoplasmic regions from eosin image component, DOUBLE" + "\n"
	+ "CYTO_E_StdGradMag, std gradient magnitude of intensities of pixels in cytoplasmic regions from eosin image component, DOUBLE" + "\n"
	+ "CYTO_E_EntropyGradMag, entropy of gradient magnitude of intensities of pixels in cytoplasmic regions from eosin image component, DOUBLE" + "\n"
	+ "CYTO_E_EnergyGradMag, energy of gradient magnitude of intensities of pixels in cytoplasmic regions from eosin image component, DOUBLE" + "\n"
	+ "CYTO_E_SkewnessGradMag, skewness of gradient magnitude of intensities of pixels in cytoplasmic regions from eosin image component, DOUBLE" + "\n"
	+ "CYTO_E_KurtosisGradMag, kurtosis of gradient magnitude of intensities of pixels in cytoplasmic regions from eosin image component, DOUBLE" + "\n"
	+ "CYTO_E_SumCanny, sum of number of canny pixels in cytoplasmic regions from eosin image component, DOUBLE" + "\n"
	+ "CYTO_E_MeanCanny, normalized number of canny pixels in cytoplasmic regions from eosin image component, DOUBLE" + "\n"
	+ "CYTO_G_MeanIntensity, mean of intensities of pixels in cytoplasmic regions from grayscale image component, DOUBLE" + "\n"
	+ "CYTO_G_MeanMedianDifferenceIntensity, the difference between CYTO_G_MeanIntensity and the median of intensities of pixels in cytoplasmic regions from grayscale image component, DOUBLE" + "\n"
	+ "CYTO_G_MaxIntensity, max pixel intensity in cytoplasmic regions from grayscale image component, DOUBLE" + "\n"
	+ "CYTO_G_MinIntensity, min pixel intensity in cytoplasmic regions from grayscale image component, DOUBLE" + "\n"
	+ "CYTO_G_StdIntensity, std pixel intensity in cytoplasmic regions from grayscale image component, DOUBLE" + "\n"
	+ "CYTO_G_Entropy, entropy of intensities of pixels in cytoplasmic regions from grayscale image component, DOUBLE" + "\n"
	+ "CYTO_G_Energy, energy of intensities of pixels in cytoplasmic regions from grayscale image component, DOUBLE" + "\n"
	+ "CYTO_G_Skewness, skewness of intensities of pixels in cytoplasmic regions from grayscale image component, DOUBLE" + "\n"
	+ "CYTO_G_Kurtosis, kurtosis of intensities of pixels in cytoplasmic regions from grayscale image component, DOUBLE" + "\n"
	+ "CYTO_G_MeanGradMag, mean gradient magnitude of intensities of pixels in cytoplasmic regions from grayscale image component, DOUBLE" + "\n"
	+ "CYTO_G_StdGradMag, std gradient magnitude of intensities of pixels in cytoplasmic regions from grayscale image component, DOUBLE" + "\n"
	+ "CYTO_G_EntropyGradMag, entropy of gradient magnitude of intensities of pixels in cytoplasmic regions from grayscale image component, DOUBLE" + "\n"
	+ "CYTO_G_EnergyGradMag, energy of gradient magnitude of intensities of pixels in cytoplasmic regions from grayscale image component, DOUBLE" + "\n"
	+ "CYTO_G_SkewnessGradMag, skewness of gradient magnitude of intensities of pixels in cytoplasmic regions from grayscale image component, DOUBLE" + "\n"
	+ "CYTO_G_KurtosisGradMag, kurtosis of gradient magnitude of intensities of pixels in cytoplasmic regions from grayscale image component, DOUBLE" + "\n"
	+ "CYTO_G_SumCanny, sum of number of canny pixels in cytoplasmic regions from grayscale image component, DOUBLE" + "\n"
	+ "CYTO_G_MeanCanny, normalized number of canny pixels in cytoplasmic regions from grayscale image component, DOUBLE" + "\n"
	+ "#ObservationMetadata" + "\n"
	+ "Nuclei Score, grade, Ordinal, Integer" + "\n"
	+ "#Markups format=db2";
	
	
	public void fixAndPrefix(String inpath, String outpath) throws IOException, InterruptedException
	{
		
		this.inpath=inpath;
		this.outpath=outpath;
		this.tmpdir=System.getProperty("java.io.tmpdir");
		
		File B2Ptmpdir=new File(this.tmpdir+"/B2Ptmpdir");
		B2Ptmpdir.mkdirs();
		//make tempt directories for fix tools
		File B2Poutput1 = new File(B2Ptmpdir.getAbsolutePath()+"/output1");
		File B2Poutput2 = new File(B2Ptmpdir.getAbsolutePath()+"/output2");
		B2Poutput1.mkdirs();
		B2Poutput2.mkdirs();
		//use fix tools BoundaryFix1 and BoundaryFix2 fix input file
		boundaryFix(B2Poutput1.getAbsolutePath(),B2Poutput2.getAbsolutePath());
		//get all the fixed files
		ArrayList<File> filelist = fetchfiles(B2Poutput2.getAbsolutePath());
		genOutfiles(filelist);
		TengUtils.deleteDir(B2Poutput2);
		TengUtils.deleteDir(B2Poutput1);
		TengUtils.deleteDir(B2Ptmpdir);
		
	}
	
	public void fixOnly(String inpath, String outpath) throws IOException, InterruptedException
	{
		this.inpath=inpath;
		this.outpath=outpath;
		this.tmpdir=System.getProperty("java.io.tmpdir");
		
		File B2Ptmpdir=new File(this.tmpdir+"/B2Ptmpdir");
		B2Ptmpdir.mkdirs();
		//make tempt directories for fix tools
		File B2Poutput1 = new File(B2Ptmpdir.getAbsolutePath()+"/output1");
		File B2Poutput2 = new File(outpath);
		B2Poutput1.mkdirs();
		B2Poutput2.mkdirs();
		//use fix tools BoundaryFix1 and BoundaryFix2 fix input file
		boundaryFix(B2Poutput1.getAbsolutePath(),B2Poutput2.getAbsolutePath());

		TengUtils.deleteDir(B2Poutput1);
		TengUtils.deleteDir(B2Ptmpdir);
		
	}
	
	public void prefixOnly(String inpath, String outpath) throws IOException, InterruptedException
	{
		this.inpath=inpath;
		this.outpath=outpath;
		this.tmpdir=System.getProperty("java.io.tmpdir");
		ArrayList<File> filelist = fetchfiles(inpath);
		genOutfiles(filelist);
	}
	
	
	//use fix tools fix input files
	public void boundaryFix(String tmpdir1,String tmpdir2) throws IOException, InterruptedException
	{
		String cmd1[] = new String[3];
		String cmd2[] = new String[3];
		cmd1[0]=FixerDir+"BoundaryFix";
		cmd1[1]=inpath+"/";
		cmd1[2]=tmpdir1+"/";
		cmd2[0]=FixerDir+"BoundaryFix2";
		cmd2[1]=tmpdir1+"/";
		cmd2[2]=tmpdir2+"/";		
	
		Process pro = Runtime.getRuntime().exec(cmd1);
		pro.waitFor();
		pro = Runtime.getRuntime().exec(cmd2);
		pro.waitFor();
	}
	
   
	public ArrayList<File> fetchfiles(String path)
	{
		System.out.println(path);
		ArrayList<File> filelist = new ArrayList<File>();
		File file = new File(path);
		if(file.isFile()&&file.getName().endsWith(".txt"))
		{
			filelist.add(file);
		}
		else if(file.isDirectory())
		{
			for(File f:file.listFiles())
			{
				filelist.addAll(fetchfiles(f.getAbsolutePath()));
			}
		}
		return filelist;
	}
	//add header to each file in the files array
	public void genOutfiles(ArrayList<File> filelist)
	{
		for(File file:filelist)
		{
			
			try {
				BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(outpath+"/"+file.getName().substring(0,file.getName().lastIndexOf(".txt"))+".svs-0000000000-0000000000.ppm.grid4.mat.grid4.txt"))));
				writer.write(prefix);
				BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file.getAbsoluteFile())));
				String line;
				String truncline;
					
				 while((line = reader.readLine())!=null)
				 {
						truncline=line.substring(line.indexOf(";")+1);
						writer.newLine();
						writer.write(truncline);
                 }
				 reader.close();
				 writer.close();
				 
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					System.out.println("read file "+file.getName()+" failed!");
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	
			 System.out.println(file.getName()+" done");
    		}
	}
	
	public static void main(String[] args)
	{
	    try {
			new PAISBoundary2PAIS().prefixOnly("/home/db2inst1/Dropbox/project/LoadData/TOOLS/Data/output1","/home/db2inst1/Dropbox/project/LoadData/TOOLS/Data/output3");;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	

	
	
}
