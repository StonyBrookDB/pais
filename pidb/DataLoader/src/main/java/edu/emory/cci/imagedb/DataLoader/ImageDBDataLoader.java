package edu.emory.cci.imagedb.DataLoader;

import java.io.File;
import java.util.concurrent.BlockingQueue;
import java.lang.Integer;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import edu.emory.cci.imagedb.ModelBeans.Image;

public class ImageDBDataLoader {
	
	long imagesProduced = 0;
	long tilesProduced = 0;
	long imagesInserted = 0;
	BlockingQueue<Image> imagesQueue;
	File inputFolder;
	File dbConfigurationFile;
	File paisIdConfiguration;
	static ApplicationContext context;
	
	// default value(0) No thumbnail images generation 
	static int thumbnailCheck = 0;
	static int wtCheck = 0;
	static int threadNumber = 1;
	
	
	public void setImagesQueue(BlockingQueue<Image> imagesQueue) {
		this.imagesQueue = imagesQueue;
	}
	
	public void setInputFolder(File inputFolder) {
		this.inputFolder = inputFolder;
	}

	public void setDbConfigurationFile(File dbConfigurationFile) {
		this.dbConfigurationFile = dbConfigurationFile;
	}

	public void setPaisIdGeneratorFile(File paisIdGeneratorFile) {
		this.paisIdConfiguration = paisIdGeneratorFile;
	}
	
	public void setThumbnail(int thumbnailCheck) {
		ImageDBDataLoader.thumbnailCheck = thumbnailCheck;
	}
	
	public ImageDBDataLoader() {}
	
	void processImages() {
		Thread producer = null;
		Thread[] consumer = new Thread[threadNumber];
		
		if(inputFolder == null) {
			throw new IllegalStateException(this.getClass().getName()+" not properly initialized. Input folder of images is null.");
		}
		if(dbConfigurationFile == null) {
			throw new IllegalStateException(this.getClass().getName()+" not properly initialized. The database configuration file is null.");
		}
		if(paisIdConfiguration == null) {
			throw new IllegalStateException(this.getClass().getName()+" not properly initialized. The PAIS ID generator's configuration file is null.");
		}
			
		producer = new Thread((Runnable) context.getBean("imageProducer"));
		producer.start();
		
		/* Creating 1 producer and n consumers
		 *	Consumers are responsible for thumbnail generating, very timeconsuming 
		 */
		/*
		for (int i=0; i<threadNumber; i++) { 
			consumer[i] = new Thread((Runnable) context.getBean("imageConsumer"));
			consumer[i].start();
		}
		*/
		
		try {
			producer.join();
			//for (int j=0; j<threadNumber; j++)
				//consumer[j].join();
		}catch(InterruptedException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	
    public static void main(String[] args) {
    	
    	Option helpOption = new Option("h", "help", false, "display this help and exit.");
    	helpOption.setRequired(false);
    	
    	/*
    	 * Add option 't' in the command options
    	 * If user specify parameter, -t, then it generates a thumbnail file under TILEDIMAGE folder 
    	*/
    	
    	Option thumbnailOption = new Option("t","thumbnail", false, "generate thumbnail for tiledimages.");
    	thumbnailOption.setRequired(false);
    	Option imageThumbnailOption = new Option("wt","imagethumbnail", false, "generate thumbnail for whole slide images.");
    	thumbnailOption.setRequired(false);
		Option threadOption = new Option("n", "threads", true, "specify the number of image processing threads based on the host capacity.");
		threadOption.setRequired(false); 
  	
    	Option inputFolderOption = new Option("i", "inputFolder", true, "folder that contains the images to process.");
    	inputFolderOption.setRequired(true);
    	inputFolderOption.setArgName("folder");
    	Option dbConfigurationOption = new Option("dbc", "dbConfigurationFile", true, "xml configuration file for the database parameters.");
    	dbConfigurationOption.setRequired(true);
    	dbConfigurationOption.setArgName("xmlFile");
    	Option paisIdGeneratorConfigurationOption = new Option("pc", "paisIdConfigurationFile", true, "xml configuration file for the pais ID generation.");
    	paisIdGeneratorConfigurationOption.setRequired(true);
    	paisIdGeneratorConfigurationOption.setArgName("xmlFile");
    	
    	Options options = new Options();
    	options.addOption(helpOption);
    	options.addOption(thumbnailOption);
    	options.addOption(imageThumbnailOption);
		options.addOption(threadOption);
    	options.addOption(inputFolderOption);
    	options.addOption(dbConfigurationOption);
    	options.addOption(paisIdGeneratorConfigurationOption);
    	
    	CommandLineParser parser = new GnuParser();
    	HelpFormatter formatter = new HelpFormatter();
    	CommandLine line = null;
    	try {
    		line = parser.parse(options, args);
    		if(line.hasOption("h")) {
    			formatter.printHelp("ImageDBDataLoader", options, true);
    			System.exit(0);
    		}
    		if(line.hasOption("t")){
        		thumbnailCheck = 1;
        		System.out.println("Thumbnails for tiles will be generated.");
        	}
    		if(line.hasOption("wt")){
        		wtCheck = 1;
        		System.out.println("Thumbnails for whole slide images will be generated.");
        	}
			if(line.hasOption("n")) {
				threadNumber = Integer.parseInt(line.getOptionValue("n"));
				if (threadNumber<=0) {
					System.out.println("The number of threads should be a positive integer.");
					System.exit(1);
				}
				System.out.println(threadNumber + "threads will be running to generate thumbnails.");
			}
    	} catch(ParseException pe) {
    		formatter.printHelp("ImageDBDataLoader", options, true);
    		System.exit(1);
    	}
		 catch(NumberFormatException nfe) {
			formatter.printHelp("ImageDBDataLoader", options, true);
    		System.exit(1);
		 }
    	
    	File inputFolder = new File(line.getOptionValue("inputFolder"));
    	if(!inputFolder.canRead()) {
    		System.out.println("Cannot read input folder '"+inputFolder.getAbsolutePath()+"'.");
    		System.exit(1);
    	}
        File dbConfiguration = new File(line.getOptionValue("dbConfigurationFile"));
        if(!dbConfiguration.canRead()) {
        	System.out.println("Cannot read database configuration file '"+dbConfiguration.getAbsolutePath()+"'.");
        	System.exit(1);
        }
        File paisIdGeneratorConfiguration = new File(line.getOptionValue("paisIdConfigurationFile"));
        if(!paisIdGeneratorConfiguration.canRead()) {
        	System.out.println("Cannot read pais Id generator file '"+paisIdGeneratorConfiguration.getAbsolutePath()+"'.");
        	System.exit(1);
        }
        
        long starttime = System.currentTimeMillis();
        context = new FileSystemXmlApplicationContext(new String[] {"classpath:resources/conf/spring/applicationContext.xml", dbConfiguration.getPath()});
        //context = new FileSystemXmlApplicationContext(new String[] {"applicationContext.xml", dbConfiguration.getPath()});
        ImageDBDataLoader dataLoader = (ImageDBDataLoader) context.getBean("imageDBDataLoader");
        dataLoader.setInputFolder(inputFolder);
        dataLoader.setDbConfigurationFile(dbConfiguration);
        dataLoader.setPaisIdGeneratorFile(paisIdGeneratorConfiguration);
        dataLoader.setThumbnail(thumbnailCheck);
        
        dataLoader.processImages();
        System.out.println("Done.");

		System.out.println("Amount of images read: "+new Long(dataLoader.imagesProduced).toString());
		System.out.println("Amount of tiles read: "+new Long(dataLoader.tilesProduced).toString());
		System.out.println("Amount of images inserted: "+new Long(dataLoader.imagesInserted).toString());
		
		long endtime = System.currentTimeMillis();

		System.out.println("Image Loading Time: " + Double.toString( (endtime - starttime)/1000.0 ));
    }

}

