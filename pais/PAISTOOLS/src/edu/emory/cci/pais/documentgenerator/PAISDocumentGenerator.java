package edu.emory.cci.pais.documentgenerator;

import java.io.File;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import edu.emory.cci.pais.PAISIdentifierGenerator.DataGeneratorConfig;
import edu.emory.cci.pais.PAISIdentifierGenerator.PAISIdentifierGenerator;
import edu.emory.cci.pais.documentgenerator.InputDataReader.CalculationMetadata;
import edu.emory.cci.pais.documentgenerator.InputDataReader.ObservationMetadata;
import edu.emory.cci.pais.documentgenerator.ObservationGenerator;
import edu.emory.cci.pais.model.AnatomicEntity;
import edu.emory.cci.pais.model.Annotation;
import edu.emory.cci.pais.model.Calculation;
import edu.emory.cci.pais.model.Collection;
import edu.emory.cci.pais.model.Group;
import edu.emory.cci.pais.model.ImageReference;
import edu.emory.cci.pais.model.Markup;
import edu.emory.cci.pais.model.Ordinal;
import edu.emory.cci.pais.model.Patient;
import edu.emory.cci.pais.model.Polygon;
import edu.emory.cci.pais.model.Project;
import edu.emory.cci.pais.model.Region;
import edu.emory.cci.pais.model.Specimen;
import edu.emory.cci.pais.model.Subject;
import edu.emory.cci.pais.model.User;
import edu.emory.cci.pais.model.WholeSlideImageReference;

public class PAISDocumentGenerator {

	PAISXMLGenerator paisGen = new PAISXMLGenerator();
	InputDataReader reader;
	String fileName;
	String fileNameWOPath;
	String outputFileName;
	PAISIdentifierGenerator idGen;
	List<Markup> markup = new ArrayList<Markup>();
	
	java.util.logging.Logger logger = java.util.logging.Logger.getLogger(this.getClass().getPackage().getName());
	
	public PAISDocumentGenerator(String fileName, String outputFileName, String configFile) {
		this.fileName = fileName;
		//this.fileNameWOPath = new LinkedList<String>(Arrays.asList(fileName.split(File.separator))).getLast();
		//updated to fix bug on Windows, where "\" is forbidden when used as a beginning pattern
		int  idx = fileName.lastIndexOf(File.separator);
		this.fileNameWOPath = fileName.substring(idx+1);
		
		if(this.fileName == null || this.fileName.length() < 1) {
			throw new RuntimeException("Fatal Failure: the name of the file could not be parsed from the absolute path of the file provided. This is a bug in this program. A report of this bug would be greatly appreciated.");
		}
		this.outputFileName = outputFileName;
		idGen = new PAISIdentifierGenerator(new DataGeneratorConfig(configFile));
	}
	
	/**
	 * This method generates a PAIS document. The document is stored in a file with the path and
	 * name provided in the constructor (the second argument, outputFile).
	 * @param compressed: whether the result should be compressed with zip or not.
	 */
	public void generateDocument(boolean compressed) {
		// Read and parse the text file
		reader = new TextFileInputDataReader(fileName);
		
		// Create the PAIS Document
		PAISRoot root;
		
		String paisUid = idGen.getPAISUid(fileNameWOPath);
		BigInteger paisId = idGen.getPAISId(fileNameWOPath);
		String paisVersion = idGen.getPAISVersion();
		
		GregorianCalendar rightNow = new GregorianCalendar();
		// Try to create a PAIS document with the current date. If it doesn't work, create one without date.
		rightNow.setTime(new Date());
		XMLGregorianCalendar date = null;
		try {
			date = DatatypeFactory.newInstance().newXMLGregorianCalendar(rightNow);
			//TODO: name of the document was set to fileName. Get the name from the configuration.
			root = new PAISRoot(paisId, paisUid, paisUid, date, paisVersion, idGen.getPAISDocComment(fileNameWOPath));
		} catch (DatatypeConfigurationException e) {
			root = new PAISRoot(paisId, paisUid, paisVersion);
		}
		
		paisGen.setRoot(root);
		
		// Set the document header (document properties)
		// Most of the attributes are retrieved from the xml config file
		
		Project project = new Project();
		project.setId(idGen.getProjectId(fileNameWOPath));
		project.setUid(idGen.getProjectUid());
		if(!idGen.getProjectName().equals(""))
			project.setName(idGen.getProjectName());
		
		paisGen.setProject(project);
		
		Collection collection = new Collection();
		collection.setId(idGen.getCollectionId(fileNameWOPath));
		collection.setUid(idGen.getCollectionUid(fileNameWOPath));
		if(!idGen.getCollectionName(fileNameWOPath).equals(""))
			collection.setName(idGen.getCollectionName(fileNameWOPath));
		if(!idGen.getMethodName().equals(""))
			collection.setMethodName(idGen.getMethodName());
		if(!idGen.getMethodRole().equals(""))
			collection.setRole(idGen.getMethodRole());
		if(!idGen.getMethodSequenceNumber().equals(""))
			collection.setSequenceNumber(idGen.getMethodSequenceNumber());
		
		ArrayList<Collection> collections = new ArrayList<Collection>();
		collections.add(collection);
		paisGen.setCollections(collections);
		
		Group group = new Group();
		group.setId(idGen.getGroupId(fileNameWOPath));
		group.setUid(idGen.getGroupUid());
		if(!idGen.getGroupName().equals(""))
			group.setName(idGen.getGroupName());
		if(!idGen.getGroupUri().equals(""))
			group.setUri(idGen.getGroupUri());
		
		paisGen.setGroup(group);
		
		User user = new User();
		user.setId(idGen.getUserId(fileNameWOPath));
		if(!idGen.getUserLoginName().equals(""))
			user.setLoginName(idGen.getUserLoginName());
		//TODO user name missing.
		
		paisGen.setUser(user);
		
		Subject subject = new Patient();
		subject.setId(idGen.getSubjectId(fileNameWOPath));
		if(!idGen.getPatientId(fileNameWOPath).equals(""))
			subject.setId(idGen.getSubjectId(fileNameWOPath));
		
		Specimen specimen = new Specimen();
		specimen.setId(idGen.getSpecimenId(fileNameWOPath));
		specimen.setUid(idGen.getSpecimenUid(fileNameWOPath));
		if(!idGen.getSpecimenStain().equals(""))
			specimen.setStain(idGen.getSpecimenStain());
		if(!idGen.getSpecimenType().equals(""))
			specimen.setType(idGen.getSpecimenType());
		
		AnatomicEntity anatomicEntity = new AnatomicEntity();
		anatomicEntity.setId(idGen.getAnatomicEntityId());
		if(!idGen.getAnatomicEntityCodeMeaning().equals(""))
			anatomicEntity.setCodeMeaning(idGen.getAnatomicEntityCodeMeaning());
		if(!idGen.getAnatomicEntityCodeValue().equals(""))
			anatomicEntity.setCodeValue(idGen.getAnatomicEntityCodeValue());
		if(!idGen.getAnatomicEntityCodingSchemeDesignator().equals(""))
			anatomicEntity.setCodingSchemeDesignator(idGen.getAnatomicEntityCodingSchemeDesignator());
		if(!idGen.getAnatomicEntityCodingSchemeVersion().equals(""))
			anatomicEntity.setCodingSchemeVersion(idGen.getAnatomicEntityCodingSchemeVersion());
		
		Region region = new Region();
		region.setId(idGen.getRegionId(fileNameWOPath));
		if(!idGen.getRegionName(fileNameWOPath).equals(""))
			region.setName(idGen.getRegionName(fileNameWOPath));
		try {
			region.setX((double) idGen.getRegionXCoordinate(idGen.getRegionCoordinates(fileNameWOPath)));
			region.setY((double) idGen.getRegionYCoordinate(idGen.getRegionCoordinates(fileNameWOPath)));
		} catch(Exception e) {
			logger.log(Level.WARNING, "X or Y coordinates not available not available in configuration file.");
		}
		
		try {
			region.setWidth((double) idGen.getRegionWidth());
			region.setHeight((double) idGen.getRegionHeight());
		} catch(Exception e) {
			logger.log(Level.WARNING, "Width or height not available in configuration file.");
		}
		
		ImageReference imgRef;
		
		//TODO: add all the possible subtypes of ImageReference or encapsulate this 
		// code in an ImageFileReference factory method
		if(idGen.getImageType().equals("WholeSlideImageReference")) {
			imgRef = new WholeSlideImageReference();
			if(!idGen.getImageFileReference(fileNameWOPath).equals(""))
				((WholeSlideImageReference)imgRef).setFileReference(idGen.getImageFileReference(fileNameWOPath));
			if(!idGen.getImageReferenceUid(fileNameWOPath).equals(""))
				((WholeSlideImageReference)imgRef).setUid(idGen.getImageReferenceUid(fileNameWOPath));
			try {
				((WholeSlideImageReference)imgRef).setResolution(idGen.getImageScanningResolution());
			} catch(Exception e) {
				logger.log(Level.WARNING, "Was not able to set the resolution. Check the attribute 'scanningResolution' " +
						"of the 'image' element in the configuration file.");
			}
		}
		else
			imgRef = new ImageReference();
		
		imgRef.setId(idGen.getImageReferenceId(fileNameWOPath));
		
		ImageReference.Subject imgRefSubject = new ImageReference.Subject();
		imgRefSubject.setSubject(subject);
		
		ImageReference.Specimen imgRefSpecimen = new ImageReference.Specimen();
		imgRefSpecimen.setSpecimen(specimen);
		
		ImageReference.AnatomicEntityCollection anatomicEntityCollection = new ImageReference.AnatomicEntityCollection();
		anatomicEntityCollection.getAnatomicEntity().add(anatomicEntity);
		
		ImageReference.Region imgRefReg = new ImageReference.Region();
		imgRefReg.setRegion(region);
		
		imgRef.setSubject(imgRefSubject);
		imgRef.setSpecimen(imgRefSpecimen);
		imgRef.setAnatomicEntityCollection(anatomicEntityCollection);
		imgRef.setRegion(imgRefReg);
		
		ArrayList<ImageReference> imgReferences = new ArrayList<ImageReference>();
		imgReferences.add(imgRef);
		
		paisGen.setImageReferenceCollection(imgReferences);
		
		addMarkups();
		
		addCalculationsAndObservations();
		
		// Serialize XML
		if(compressed) {
			paisGen.toXMLZip(outputFileName);
		} else {
			paisGen.toXML(outputFileName);
		}
	}	

	protected void addMarkups() {

		List<Markup> markupList = new ArrayList<Markup>();
		
		Iterator<String> resultMarkupsIterator = reader.getMarkupIterator();
		int markupCounter = 1;
		while(resultMarkupsIterator.hasNext()) {
			BigInteger markupId = idGen.getMarkupId(fileNameWOPath, markupCounter);
			BigInteger polygonId = idGen.getgeometricShapeId(fileNameWOPath, markupCounter);
			BigInteger imageReferenceId = idGen.getImageReferenceId(fileNameWOPath);
			BigInteger annotationId = idGen.getAnnotationId(fileNameWOPath, markupCounter);
			String markupUid = idGen.getMarkupUid(fileNameWOPath, markupCounter);
			String annotationUid = idGen.getAnnotationUid(fileNameWOPath, markupCounter);
			
			Polygon polygon = new Polygon();
			if(!((TextFileInputDataReader)reader).getMarkupFormat().equals(""))
				polygon.setFormat(((TextFileInputDataReader)reader).getMarkupFormat());
			else
				polygon.setFormat(idGen.getMarkupFormat());
			
			String resultMarkup = resultMarkupsIterator.next();
			polygon.setId(polygonId);
			polygon.setPoints(resultMarkup);
			
			Annotation annotation = new Annotation();
			annotation.setId(annotationId);
			annotation.setUid(annotationUid);
			
			ImageReference imageReference = new ImageReference();
			imageReference.setId(imageReferenceId);
			
			Markup documentMarkup = new Markup();
			documentMarkup.setId(markupId);
			documentMarkup.setUid(markupUid);
			documentMarkup.setGeometricShape(new Markup.GeometricShape());
			Markup.GeometricShape markupShape = documentMarkup.getGeometricShape();
			markupShape.setGeometricShape(polygon);
			documentMarkup.setAnnotation(new Markup.Annotation());
			documentMarkup.getAnnotation().setAnnotation(annotation);
			documentMarkup.setImageReferenceCollection(new Markup.ImageReferenceCollection());
			documentMarkup.getImageReferenceCollection().getImageReference().add(imageReference);
			
			markupList.add(documentMarkup);
			this.markup.add(documentMarkup);
			markupCounter++;
		}
		paisGen.setMarkupCollection((ArrayList<Markup>)markupList);

	}
	
	protected void addCalculationsAndObservations() {
		List<Annotation> annotationList = new ArrayList<Annotation>();
		
		ArrayList<CalculationMetadata> calculationMetadata = reader.getCalculationMetadata();
		Iterator<String[]> calculationsIterator = reader.getCalculationIterator();
		ArrayList<ObservationMetadata> observationMetadata = reader.getObservationMetadata();
		Iterator<String[]> observationsIterator = reader.getObservationIterator();
		int annotationCounter = 1;
		int observationCounter = 1;
		while(calculationsIterator.hasNext()) {
			String[] calculations = calculationsIterator.next();
			String[] observations = null;
			if(observationsIterator.hasNext())
				observations = observationsIterator.next();
			
			BigInteger annotationId = idGen.getAnnotationId(fileNameWOPath, annotationCounter);
			AnnotationGenerator annotGen = new AnnotationGenerator(annotationId);
			
			String[] calculationTypes = idGen.getCalculationType().split(";");
			((TextFileInputDataReader) reader).trimAll(calculationTypes);
			// Add calculations
			for(int i=0;i < calculations.length;i++) {
				String calculationValue = calculations[i].trim();
				CalculationMetadata calcType;
				if(calculationMetadata.size() == 1) {
					// We have the same calculation metadata for all calculations
					calcType = calculationMetadata.get(0);
				} else {
					try {
						calcType = calculationMetadata.get(i);
					} catch(IndexOutOfBoundsException e) {
						// We have less metadata than calculations, use the first one
						calcType = calculationMetadata.get(0);
						logger.log(Level.WARNING, "All calculations don't use the same metadata, but there are not enough calculation metadata records for all calculations. Will use the first one.");
					}
				}
				String calculationName = calcType.getName();
				String calculationDataType = calcType.getDataType();
				String calculationDescription = calcType.getdescription();
				BigInteger calculationId = idGen.getCalculationId(fileNameWOPath, annotationCounter, i);
				// calculationResultId = "calculationId" + "i" (String concatenation)
				BigInteger calculationResultId = new BigInteger(new String(calculationId.toString() + BigInteger.valueOf(i).toString()));
				
				CalculationGenerator calcGen = new CalculationGenerator(calculationId, calculationName, calculationDescription);
				Calculation calculation = null;
				// All calculation are scalar if the property is missing in the configuration file
				if(idGen.getCalculationType().equals("")) {
					calculation = calcGen.createScalarCalculation(calculationResultId, calculationDataType, Double.parseDouble(calculationValue));
				}
				else if(calculationTypes[i].toLowerCase().equals("scalar"))
					calculation = calcGen.createScalarCalculation(calculationResultId, calculationDataType, Double.parseDouble(calculationValue));
				else if(calculationTypes[i].toLowerCase().equals("histogram")) {
					calculation = calcGen.createHistogramCalculation(calculationResultId, calculationDataType, calculationValue);
				} else {
					throw new RuntimeException(new IllegalArgumentException("The type of calculation is not valid."));
				}
				
				annotGen.addCalculation(calculation);				
			}
			
			if(observations != null) {
				// Add Observations
				for(int i=0;i < observations.length;i++) {
					BigInteger observationId = new BigInteger(new String(annotationId.toString() + BigInteger.valueOf(i).toString()));
					BigInteger quantificationId = observationId.add(BigInteger.valueOf(1));
					
					Ordinal quantification = new Ordinal();
					
					ObservationMetadata metadata;
					if(observationMetadata.size() == 1) {
						// We have the same metadata for all observations
						metadata = observationMetadata.get(0);
					} else {
						try {
							metadata = observationMetadata.get(i); // observationCounter starts from 1
						} catch(IndexOutOfBoundsException e) {
							// We have less metadata than observations, use the first one
							metadata = observationMetadata.get(0);
							logger.log(Level.WARNING, "All observations don't use the same metadata, but there are not enough observation metadata records for all observations. Will use the first one.");
						}
					}
					quantification.setId(quantificationId);
					quantification.setName(metadata.getQuantificationName());
					((Ordinal)quantification).setDataType(metadata.getDataType());
					((Ordinal)quantification).setValue(Double.parseDouble(observations[i]));
					
					ObservationGenerator obsGen = new ObservationGenerator(observationId, metadata.getName(), quantificationId, null, null, null, null, null, null, metadata.getQuantificationType());
					obsGen.setOrdinalQuantification(quantification);
					
					annotGen.addObservation(obsGen.getObservation());
				}
				
				observationCounter++;
			}
			
			// Add markup
			Markup markupRef = new Markup();
			try {
				markupRef.setId(markup.get(annotationCounter - 1).getId());
			} catch(IndexOutOfBoundsException e) {
				throw new RuntimeException("There are more annotations than markups. Make sure you have the same amount of lines describing markups and annotations.", e);
			}
			
			annotGen.addMarkup(markupRef);
			// Done with one annotation
			annotationList.add(annotGen.getAnnotation());
			annotationCounter++;
		}
		paisGen.setAnnotationCollection((ArrayList<Annotation>)annotationList);
	}
	
	
}
