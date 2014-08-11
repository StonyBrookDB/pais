package edu.emory.cci.pais.dataloader.dataloadingmanager;

/**
 * @author Fusheng Wang, Center for Comprehensive Informatics, Emory University
 * @version 1.0
 */

public class LoadResult{
		String uid;
		String tileName;
		int count;
		String metaFilename;
		double loadingTime;
		public LoadResult(){}
		public LoadResult(String uid, String tileName, int count, String metaFilename, double loadingTime){
			this.uid = uid;
			this.tileName = tileName;
			this.count = count;
			this.metaFilename = metaFilename;
			this.loadingTime = loadingTime;
		}
		public String getUid(){return uid; }
		public String getTileName(){return tileName;}
		public int getCount(){return count; }
		public String getMetaFilename(){return metaFilename;}	
		public double getLoadingTime(){return loadingTime; }
		
		public String toString(){
			return "uid:" + uid + "; tileName:" + tileName + ";count:" + count + ";metaFilename:" + metaFilename +"; loadingTime:" + loadingTime;
		}
		
		public void print(){
			System.out.println("uid:" + uid + "; tileName:" + tileName + ";count:" + count + ";metaFilename:" + metaFilename+"; loadingTime:" + loadingTime);
		}
	}