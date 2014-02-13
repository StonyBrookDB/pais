package edu.emory.cci.imagedb.FileDataCollector;

import java.awt.Dimension;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import org.hibernate.Hibernate;

import edu.emory.cci.imagedb.FileDataCollector.ImageFormatNotDefinedException;
import edu.emory.cci.imagedb.ModelBeans.Dataset;
import edu.emory.cci.imagedb.ModelBeans.Experimentalstudy;
import edu.emory.cci.imagedb.ModelBeans.Experimenter;
import edu.emory.cci.imagedb.ModelBeans.Experimentergroup;
import edu.emory.cci.imagedb.ModelBeans.Location;
import edu.emory.cci.imagedb.ModelBeans.Patient;
import edu.emory.cci.imagedb.ModelBeans.Project;
import edu.emory.cci.imagedb.ModelBeans.Server;
import edu.emory.cci.imagedb.ModelBeans.Specimen;
import edu.emory.cci.imagedb.ModelBeans.Instrument;

import edu.emory.cci.pais.PAISIdentifierGenerator.DataGeneratorConfig;
import edu.emory.cci.pais.PAISIdentifierGenerator.PAISIdentifierGenerator;
import edu.emory.cci.pais.PAISIdentifierGenerator.SpecimenNotFoundException;


public class ImageFileData {
	File imageFile;
	PAISIdentifierGenerator paisIdGenerator;
	File imageThumbnail;
	File bigImageThumbnail;

	static Map<String, Long> imageFormatsMappings = new HashMap<String, Long>();
	
	static {
		imageFormatsMappings.put("ndpi", new Long(1));
		imageFormatsMappings.put("svs", new Long(2));
		imageFormatsMappings.put("jpg", new Long(3));
	}
	
	public File getImageThumbnail() {
		return imageThumbnail;
	}

	public void setImageThumbnail(File imageThumbnail) {
		this.imageThumbnail = imageThumbnail;
	}
	
	public File getBigImageThumbnail() {
		return bigImageThumbnail;
	}

	public void setBigImageThumbnail(File bigImageThumbnail) {
		this.bigImageThumbnail = bigImageThumbnail;
	}

	public File getImageFile() {
		return imageFile;
	}

	public void setImageFile(File imageFile) {
		this.imageFile = imageFile;	
	}
	
	public PAISIdentifierGenerator getPaisIdGenerator() {
		return paisIdGenerator;
	}

	public void setPaisIdGenerator(PAISIdentifierGenerator paisIdGenerator) {
		this.paisIdGenerator = paisIdGenerator;
	}

	public ImageFileData() {
		super();
	}

	public ImageFileData(File file, File paisIdGeneratorConfigFile) {
		this();
		this.imageFile = file;
		this.paisIdGenerator = new PAISIdentifierGenerator(new DataGeneratorConfig(paisIdGeneratorConfigFile));
	}
	
	public long getImageReferenceId() {
		return paisIdGenerator.getImageReferenceId(imageFile.getName()).longValue();
	}
	
	public Date getAcquisitionDate() {
		return new Date(imageFile.lastModified());
	}
	
	public Long getFileSize() {
		return imageFile.length();
	}
	
	public String getName() {
		String[] tokens = imageFile.getAbsolutePath().split(Pattern.quote(File.separator));
		if(tokens.length < 2) {
			System.err.println("The folder structure is not as expected for image file '"+imageFile.getAbsolutePath()+"'.");
			return imageFile.getAbsolutePath();
		}
		return (tokens[tokens.length - 2] + File.separator + tokens[tokens.length - 1]);
	}
	
	public Long getFormat() {
		String[] tokens = imageFile.getName().split("\\.");
		String fileExtension = tokens[tokens.length-1];
		if(!imageFormatsMappings.containsKey(fileExtension)) {
			throw new ImageFormatNotDefinedException("The image format '"+fileExtension+"' is not supported for images. The format must be added in the mapping from formats to Long in "+this.getClass().getName()+".");
		}
		return imageFormatsMappings.get(fileExtension);
	}
	
	public String getResolution() {
		return Integer.toString((int)paisIdGenerator.getImageScanningResolution()) + "X";
	}
	//
	public Dimension getFileDimension(File imageFile)
	{
		String filePath = imageFile.getAbsolutePath();
		
		String tmpPath = System.getProperty("java.io.tmpdir")+File.separator+imageFile.getName();
		OpenSlideToolsBin.getDimension(filePath, tmpPath);
		File tmpfile = new File(tmpPath);
		Dimension result = null;
		if(tmpfile.exists()&&tmpfile.isFile())
		{ 
        try {
        	InputStreamReader read = new InputStreamReader(new FileInputStream(tmpfile));
            BufferedReader bufferedReader = new BufferedReader(read); 
			String lineTxt = bufferedReader.readLine();
			
			read.close();
			String[] dimension= lineTxt.split(" ");
			result = new Dimension(Integer.parseInt(dimension[0]),Integer.parseInt(dimension[2]));
			
		} catch (IOException e) {
			e.printStackTrace();
			result = new Dimension(0,0);
		} 
 		}
		tmpfile.delete();
		return result;
		
	}
	
	public String getImageReferenceUid() {
		return paisIdGenerator.getImageReferenceUid(imageFile.getName());
	}
	
	/*
	 * PatientID comes from a filename convention
	 * i.e. 
	 * TCGA-14-0736-01R-01_01B.svs (WSI)
	 * TCGA-14-0736-01R-01_01B.ndpi-000000000-000000000.tiff (Tiled Image)
	 * PatientID: 0736  
	 */
	
	public Patient getPatient() {
		Patient patient = new Patient();
		// get Id
		String[] tokens = imageFile.getName().split("-");
		if(tokens.length == 5 || tokens.length == 6) {
			patient.setPatientid(tokens[2]);
		}
		
		// get ethnicGroup
		patient.setEthnicgroup(paisIdGenerator.getPatientEthnicGroup());
		return patient;
	}
		
	public Specimen getSpecimen() {
		Specimen specimen = new Specimen();
		
		// get slice Id, Tissue Id
		String[] tokens = imageFile.getName().split("-");
		if(tokens.length == 5) {
			specimen.setSliceId(tokens[4].replaceAll("\\..*", ""));
			specimen.setTissueId(tokens[3]);
		}
		if(tokens.length == 6) {
			specimen.setSliceId(tokens[4]);
			specimen.setTissueId(tokens[3]);
		}
		try {
			specimen.setSpecimenUid(paisIdGenerator.getSpecimenUid(imageFile.getName()));
		} catch(SpecimenNotFoundException e) {
			// Specimen not found. Leave it blank.
			return null;
		}
		specimen.setType(paisIdGenerator.getSpecimenType());
		specimen.setStain(paisIdGenerator.getSpecimenStain());
		
		return specimen;
	}
	
	/*
	 * To Do: add manufacturerModelName, softwareVersion from a configration file
	 */
	public Instrument getInstrument() {
		Instrument instrument = new Instrument();
		
		// get manufacturer, manufacturerModelName & softwareVersion
		instrument.setManufacturer(paisIdGenerator.getEquipmentManufacturer());
		return instrument;
	}

	/*
	 * To Do: Add attributes dataset_name, image_format 
	 */
	public Dataset getDataset() {
		Dataset dataset = new Dataset();
		dataset.setImage_format(getFormat());
		
		String datasetName = getName();
		String[] datasetPathTokens = datasetName.split(Pattern.quote(File.separator));
		datasetName = datasetPathTokens[0];
		dataset.setDataset_name(datasetName);
		
		return dataset;
	}

	public Experimentalstudy getExperimentalstudy() {
		Experimentalstudy estudy = new Experimentalstudy();
		
		estudy.setName(paisIdGenerator.getStudyName());
		
		Experimenter experimenter = new Experimenter();
		experimenter.setUserUid(paisIdGenerator.getUserUid());
		experimenter.setLoginname(paisIdGenerator.getUserLoginName());
		experimenter.setName(paisIdGenerator.getUserName());
		
		Experimentergroup egroup = new Experimentergroup();
		egroup.setName(paisIdGenerator.getGroupName());
		egroup.setGroupUid(paisIdGenerator.getGroupUid());
		egroup.setUri(paisIdGenerator.getGroupUri());
		
		experimenter.getExperimentergroups().add(egroup);
		estudy.setExperimenter(experimenter);
		
		Project project = new Project();
		project.setName(paisIdGenerator.getProjectName());
		project.setUri(paisIdGenerator.getProjectUri());
		
		estudy.setProject(project);
		return estudy;
	}
	
	public Location getLocation(String folder) {
		Location location = new Location();
		if(!folder.endsWith(File.separator))
			folder += File.separator;
		location.setFolder(folder);
		
		Server server = new Server();
		
		server.setName(paisIdGenerator.getServerName());
		server.setCapacity(paisIdGenerator.getServerCapacity());
		server.setHostname(paisIdGenerator.getServerHostname());
		server.setIpaddress(paisIdGenerator.getServerIpaddress());
		try {
			server.setPort(new Integer(Integer.parseInt(paisIdGenerator.getServerPort())));
		} catch(NumberFormatException e) {
			// Port not available in configuration
		}
		location.setServer(server);
		
		return location;
	}
	
	public java.sql.Blob getThumbnail() throws IOException {
		String tbFileName = System.getProperty("java.io.tmpdir")+File.separator+"tempimg.png";
		//System.out.println("imageThumbnail: " +tbFileName);
		OpenSlideToolsBin.getThumbnailWSI(imageFile.getAbsolutePath(), tbFileName, 200);
		File thumbnailfile = new File(tbFileName);
		
	    InputStream is = new FileInputStream(thumbnailfile);
			
	    // Get the size of the file
	    long length = thumbnailfile.length();
	
	    // to ensure that file is not larger than Integer.MAX_VALUE.
	    if (length > Integer.MAX_VALUE) {
	    	System.out.println("file is too large");
	    	is.close();
	        return null;// File is too large
	    }
	
	    // Create the byte array to hold the data
	    byte[] bytes = new byte[(int)length];
	
	    // Read in the bytes
	    int offset = 0;
	    int numRead = 0;
	    
		while (offset < bytes.length
		       && (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0) {
		    offset += numRead;
		}
	
	    // Ensure all the bytes have been read in
	    if (offset < bytes.length) {
	    	is.close();
			throw new IOException("Could not completely read file "+thumbnailfile.getName());
	    }
	
	    // Close the input stream and return bytes
		is.close();
	    thumbnailfile.delete();
	    
	    return Hibernate.createBlob(bytes);
	}
	
	public byte[] getBigThumbnail() {
		return readFile(bigImageThumbnail);
	}
	
	private byte[] readFile(File file) {
		byte[] result = new byte[(int)imageThumbnail.length()];
		FileInputStream in = null;
		try {
			in = new FileInputStream(imageThumbnail);
			int bytesRead = in.read(result);
			if(bytesRead != imageThumbnail.length()) {
				System.err.println("Reading thumbnail imageEmoryTCGA from imageEmoryTCGA '"+imageFile+"': The amount bytes read is not equal the size of the file.");
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
public static void main(String[] args)
{
	new ImageFileData().getFileDimension(new File("/home/db2inst1/Dropbox/TCGA-06-0173-01Z-00-DX1.svs"));
}

	

}
