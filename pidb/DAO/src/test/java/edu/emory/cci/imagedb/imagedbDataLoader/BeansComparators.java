package edu.emory.cci.imagedb.imagedbDataLoader;

import edu.emory.cci.imagedb.ModelBeans.Location;
import edu.emory.cci.imagedb.ModelBeans.Patient;
import edu.emory.cci.imagedb.ModelBeans.Server;
import edu.emory.cci.imagedb.ModelBeans.Specimen;
import edu.emory.cci.imagedb.ModelBeans.Tiledimage;

public class BeansComparators {
	
	public static boolean locationEquals(Location a, Location b) {
		return a.getFolder().equals(b.getFolder()) &&
		serverEquals(a.getServer(), b.getServer());
	}
	
	public static boolean serverEquals(Server a, Server b) {
		return a.getName().equals(b.getName()) &&
		a.getCapacity().equals(b.getCapacity()) &&
		a.getHostname().equals(b.getHostname()) &&
		a.getIpaddress().equals(b.getIpaddress()) &&
		a.getPort().equals(b.getPort());
	}
	
	public static boolean specimenEquals(Specimen a, Specimen b) {
		return a.getType().equals(b.getType()) &&
		a.getStain().equals(b.getStain()) &&
		a.getSpecimenUid().equals(b.getSpecimenUid()) &&
		a.getSliceId().equals(b.getSliceId()) &&
		a.getTissueId().equals(b.getTissueId());
	}
	
	public static boolean patientEquals (Patient a, Patient b) {
		return a.getBirthdate().equals(b.getBirthdate()) &&
		a.getEthnicgroup().equals(b.getEthnicgroup()) &&
		a.getName().equals(b.getName()) &&
		a.getPatientid().equals(b.getPatientid()) &&
		a.getSex().equals(b.getSex());
	}
	
	public static boolean tiledimageEquals (Tiledimage a, Tiledimage b) {
		return a.getFilesize() == b.getFilesize() &&
		a.getWidth() == b.getWidth() &&
		a.getHeight() == b.getHeight() &&
		a.getX() == b.getY() &&
		a.getHierarchylevel() == b.getHierarchylevel() &&
		a.getFormat().equals(b.getFormat()) &&
		a.getResolution().equals(b.getResolution()) &&
		a.getTilename().equals(b.getTilename()) &&
		locationEquals(a.getLocation(), b.getLocation());		
	}
}
