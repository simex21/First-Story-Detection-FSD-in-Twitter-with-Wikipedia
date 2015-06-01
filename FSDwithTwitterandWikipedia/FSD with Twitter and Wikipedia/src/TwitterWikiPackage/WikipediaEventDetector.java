package TwitterWikiPackage;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeMap;
import java.lang.Math;

public class WikipediaEventDetector {
	public static HashMap<String, String> mapParam = new HashMap<String, String>();

	static int windowSize = 0;
	static int[] window; 
	static boolean eventDetected;
	static int numberOfWikiEventsDetected = 0;
	static int notevents = 0;
	static int pageViewThreshold = 0;
	static int zScoreThreshold = 0;
	static String folderPath;
	public static Set<String> wikiEvents = new HashSet<String>();
	
	public static void wikiEventDetector()throws IOException{
		System.out.println("================Starting Wikipedia event detection=========== ");
		Map<String,ArrayList<String>> multiMap = new HashMap<String,ArrayList<String>>();
		setWikiParameters();
		window= new int[windowSize];
		multiMap = pageViewFilesReader("./"+folderPath+"/WikiData");
		
		slidingWindowImplementation(multiMap);
		System.out.println(numberOfWikiEventsDetected+" wikipedia events were detected.");
		System.out.println(notevents+" wiki pages were not considered as events.");
		System.out.println("===============Finishing Wikipedia event detection=================");
	}
	
	public static Map<String,ArrayList<String>> pageViewFilesReader(String pathToFolder) throws IOException{
		File folder = new File(pathToFolder);
		System.out.println(pathToFolder);
		File[] listOfFiles = folder.listFiles();
		Map<String,ArrayList<String>> titleCountTimeMap = new HashMap<String,ArrayList<String>>();
		    for (int i = 0; i < listOfFiles.length; i++) {
		      if (listOfFiles[i].isFile()) {
		    	  String fileName = listOfFiles[i].getName();
		    	  BufferedReader br = new BufferedReader(new FileReader(listOfFiles[i]));
		    	  for (String line = br.readLine(); line != null; line = br.readLine()){
		        	if(line.startsWith("en ")){	        		
		        		String [] lineArray = line.split(" ");
		        		String entity = lineArray[1];
		        		if(entity!=null&&!entity.isEmpty()){
		        			char myChar = entity.charAt(0);
		        			boolean isDigit = Character.isDigit(entity.charAt(0));
			        		boolean isAlphabet = Character.isAlphabetic(myChar);
		        		if(lineArray.length==4&&isDigit==false&&isAlphabet==true&&!entity.startsWith("File:")&&!entity.startsWith("Special:")&&!entity.startsWith("Category:")&&!entity.startsWith("Category_talk")&&!entity.startsWith("Talk")&&!entity.startsWith("User:")&&!entity.startsWith("Template_talk")&&!entity.startsWith("User_talk")&&!entity.startsWith("Wikipedia:wikiproject")&&!entity.endsWith(".php")){
			        			String countAndTimeStamp = lineArray[2]+"_"+fileName.replaceAll("pagecounts-", "");
				        		ArrayList<String> values;
				        		if(!titleCountTimeMap.containsKey(entity)){
				        			values = new ArrayList<String>();
				        			values.add(countAndTimeStamp);
				        		}
				        		else{
				        			values = titleCountTimeMap.get(entity);
				        			values.add(countAndTimeStamp);
				        			}
				        		titleCountTimeMap.put(entity, values);
			        		}else{
			        			//System.out.println("nothing to write on the map");
			        		}
		        		}
		        		
		        	}
		        }
		      } 
		    }
		    return titleCountTimeMap;
	}
	
	public static float square(float number){
		return number*number;
	}
	
	public static void slidingWindowImplementation(Map<String,ArrayList<String>> multiMap) throws IOException{
		FileWriter fw = null; 
		BufferedWriter bw = null;
		File file = new File("wikipediaEvents");
		if (!file.exists()) {
			file.createNewFile();						
		}	
		fw = new FileWriter(file.getAbsoluteFile(), true);
		bw = new BufferedWriter(fw);
		
		for(String key:multiMap.keySet()){
			TreeMap<String,Integer> dateValueMap = new TreeMap<String, Integer>();
			ArrayList<String> values = multiMap.get(key);
			ArrayList<Integer> counts = new ArrayList<Integer>();
			for(int elements = 0; elements<values.size(); elements++){
				int count = Integer.parseInt(values.get(elements).split("_")[0]);
				String timeStamp = values.get(elements).split("_")[1];
				dateValueMap.put(timeStamp, count);
				counts.add(count);
			}
			
			for(int i = 0; i<counts.size(); i++){
				eventDetected = false;
				int tempSum = 0;
				if(!checkIfWindowIsFull()){
					window[i] = counts.get(i);
				}else{
					
					for(int j = 0; j<window.length; j++){
						tempSum += window[j];
					}
					float windowAvg = tempSum/windowSize;
					float windowDev = 0;
					for(int k= 0; k<window.length; k++){
						windowDev +=  square(window[k]-windowAvg);
					}
					double standardDeviation = Math.sqrt(windowDev/windowSize);
					double zScore = (counts.get(i)-windowAvg)/standardDeviation;
					//System.out.println("zScore is: "+zScore);
					window[i%windowSize] = counts.get(i);
					eventDetection(counts.get(i), zScore);
					if(eventDetected){
						numberOfWikiEventsDetected ++;
						wikiEvents.add(key);
						bw.write(key);
						bw.newLine();
						//System.out.println(key+"_"+new ArrayList<String>(dateValueMap.keySet()).get(i)+"_"+counts.get(i)+"_"+zScore);//+new ArrayList<String>(dateValueMap.keySet()).get(i));
					}else{
						notevents++;
					}
				}
			}
		}
		bw.close();
		fw.close();
	}
	public static void eventDetection(int pageViews, double zScore){
		if(pageViews>pageViewThreshold){
			if(zScore>zScoreThreshold){
				//System.out.println("event detected.");
				eventDetected = true;
			}
		}
	}
	public static Set<String> getWikiEvents(){
		return wikiEvents;
	}
	
	public static void setWikiParameters(){
		Properties prop = new Properties();
		//InputStream input = null;
		prop = GatherUps.loadProps();
		pageViewThreshold = Integer.parseInt(prop.getProperty("pageCount"));
		zScoreThreshold = Integer.parseInt(prop.getProperty("zScore"));
		windowSize = Integer.parseInt(prop.getProperty("wikiSample"));
		folderPath = prop.getProperty("tweetDate");
	}
	
	public static boolean checkIfWindowIsFull(){
		for(Integer i:window){
			if(i == null) return false;
		}
		return true;
	}

}