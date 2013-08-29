package uk.ac.ncl.fanyaoxia.monitor;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class StoredArticle {
	private int popularity;
	private String storedArticleName;
	private String initialTime;
	private LinkedHashMap<String,Integer> dateAndPopularityArray;


	public StoredArticle(String title,String initialTime) {
		storedArticleName = title;
		this.initialTime=initialTime;
		popularity=0;
		dateAndPopularityArray=new LinkedHashMap<String,Integer>();
	}

	public int getPopularity() {
		return popularity;
	}

	public String getStoredTitle() {
		return storedArticleName;
	}
	public String getInitialTime(){
		return initialTime;
	}
	/**
	 * If no updates for a particular page, the value of popularity increase one. If it has 
	 * updates, the value stay zero.
	 */
	public void noUpdates(){
		popularity=popularity+1;
	}
	public void emptyTime(){
		initialTime=null;
	}
	public void addTimeAndPopularity(String key,int value ){
		dateAndPopularityArray.put(key,value);
	}
	public Map<String,Integer> getTimeAndPopularity(){
		return dateAndPopularityArray;
	}
}
