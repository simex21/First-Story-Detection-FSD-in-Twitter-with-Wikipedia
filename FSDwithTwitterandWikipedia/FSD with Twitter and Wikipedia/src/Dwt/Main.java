package Dwt;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.InputMismatchException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import FileSystem.DiskFileExplorer;
import ch.epfl.lis.networks.NetworkException;

import TwitterWikiPackage.GatherUps;
import TwitterWikiPackage.WikiThread;
import TwitterWikiPackage.WikipediaThread;
import TwitterWikiPackage.TwitterToWikipediaEventsComparator;
import TwitterWikiPackage.WikipediaEventDetector;

/**
 * This class is the main class for running EDCoW and a simple modification has been done 
 * by Estifanos Simegnew Getiye and Offorji Godson EKezie
 * @author Yue HE & Falitokiniaina RABEARISON (Original authors)
 * 
 */
public class Main {
	public static HashMap<String, String> mapParam = new HashMap<String, String>();
	public static Set<String> eventKeyWords = new HashSet<String>(); 
	public static Set<String> displayEventKeywords = new HashSet<String>();
	public static Properties properties = new Properties();  
	public static Set<String> wikiEventsToDispaly = new HashSet<String>();
	
/**
 * Method to run the event detection algorithms, both Twitter and Wikipedia
 * @param tweetDate : the chosen date for event detection
 * @throws NetworkException
 * @throws Exception
 */
	public static void edcowMain(String tweetDate) throws NetworkException, Exception{
		//WikipediaThread.wikiThread();
		WikiThread wikiThread = new WikiThread(); //create a new 
		try{
			System.out.println("================Starting Twitter event detection=========== ");
			properties = GatherUps.loadProps();
			TreatSignals(tweetDate);
			Thread.sleep(50);
		}catch(InterruptedException e){
			System.out.println("Twitter event detection thread interrupted.");
		}
		while(wikiThread.isAlive()){
			try{
				Thread.sleep(1000);
			}catch(InterruptedException e){
				e.printStackTrace();
			}
		}
		System.out.println("wikipedia events are: "+WikipediaEventDetector.getWikipediaEvents());
		wikiEventsToDispaly = WikipediaEventDetector.getWikipediaEvents();
		//compare eventKeywords with wikiEventsToDisplay
		
		Set<String> reset = new HashSet<String>();
		WikipediaEventDetector.setWikipediaEvents(reset);
		System.out.println("wikipedia events after resetting are: "+WikipediaEventDetector.getWikipediaEvents());
		System.out.println("Twitter thread exiting.");
		
	}
		
	/**
	 * Treat the whole signals (nt, nwt) by splitting them into windows. The length of the window is an input from the user, defined in the 'param' file.
	 * - That is read the files,
	 * - Put them in the memory,
	 * - affect to a thread to treat a window
	 * - process the threads selected by the user after prompt
	 * - put in 'events' all the events detected in each window
	 * If debug mode is enabled, all the files (detected events, detected keywords) are stored in the folder 'results'.
	 * @throws InterruptedException
	 * @throws IOException
	 */
	private static void TreatSignals(String tweetDate) throws InterruptedException, IOException {
		// TODO Auto-generated method stub
		int nbWindow=0, nb = 0;
		LinkedList<KeyWord> longKeyWords = new LinkedList<>();
		LinkedList<Event> events = new LinkedList<>();
		String ntFilePath=null;
		String nwtFolderPath=null;
		if(tweetDate.equalsIgnoreCase("")){
			ntFilePath = "";
			nwtFolderPath = "";
		}
		else{
			ntFilePath = "./"+tweetDate+"/nt/tweets.nt";
			nwtFolderPath = "./"+tweetDate+"/nwt/";
		}
		if(ntFilePath.equalsIgnoreCase(""))	ntFilePath = "./Twitter-data/Cen/Cen.nt";	
		KeyWord.setLongNt(ntFilePath);
		System.out.println("There are " + KeyWord.longNt.length + " signal points.");		
		
		nbWindow = (int)KeyWord.longNt.length/Integer.valueOf(properties.get("sizeWindow").toString());
		if(nwtFolderPath.equalsIgnoreCase(""))	nwtFolderPath = "./Twitter-data/Cen";
		//create the folder "results"
		File fb = new File(nwtFolderPath+"/results"); 
		fb.mkdirs();
		fb = new File(nwtFolderPath+"/results/Jmod"); 
		fb.mkdirs();
		
		DiskFileExplorer expl = new DiskFileExplorer(nwtFolderPath, false);
		File[] filesNWT = expl.FilesNWT();			
				
		if (filesNWT != null && filesNWT.length!=0) {
			System.out.println("There are "+filesNWT.length+1+" keywords found in the folder.");
			System.out.println("Storing the signals in memory ...");
			for(int i=0; i<filesNWT.length; i++){											
				String filePath = filesNWT[i].getPath();
				String fileName = filesNWT[i].getName();
				
				FileReader fileReadear = new FileReader(filePath);
				BufferedReader buff = new BufferedReader(fileReadear);	
				String firstLine = buff.readLine();
				if(Character.isDigit(firstLine.charAt(0))){
					StringTokenizer st = new StringTokenizer(firstLine, ";");		
					nb = 0;
					double[] nwt = new double[st.countTokens()];
					while(st.hasMoreTokens()){				
						nwt[nb] = Double.parseDouble(st.nextToken());
						nb++;
					}
					buff.close();
					fileReadear.close();
					//computations();
					longKeyWords.add(new KeyWord(filePath, fileName, nwt, Integer.valueOf(properties.get("delta").toString())));
				}					
			}
		}
		else{
			System.out.println("There is no files in the specified folder.");
		}
				
		ProcessWindow[] windows = new ProcessWindow[nbWindow];
		Thread[] threads = new Thread[nbWindow];
		
		for(int i=0; i<nbWindow; i++){											
			ProcessWindow window = new ProcessWindow(i,Integer.valueOf(properties.get("sizeWindow").toString()), longKeyWords, Integer.valueOf(properties.get("delta").toString()), Integer.valueOf(properties.get("gamma1").toString()), Integer.valueOf(properties.get("gamma2").toString()),  Double.valueOf(properties.get("thresholdE").toString()), Boolean.valueOf(properties.get("debugMode").toString()),nwtFolderPath);	
			windows[i] = window;
			threads[i] = new Thread(window);				
		}
		
		String enteredWindows="";
		for(int i=0; i<windows.length; i++){
			
			enteredWindows = enteredWindows+String.valueOf(i);
			if(i!=windows.length-1) enteredWindows = enteredWindows+",";
		}
		System.out.println("Launching Thread Treating window(s) ...");
		System.out.println("enteredWindows is: "+enteredWindows);
		String[] tab = enteredWindows.split(",");
		for(int i=0; i<tab.length; i++){			
			threads[Integer.valueOf(tab[i])].start();
			threads[Integer.valueOf(tab[i])].join();			
		}
//		for(Thread t : threads){								
//			t.start();
//			t.join();				
//		}			
		System.out.println("END of window(s) treatment.");		
		for(int i=0; i<tab.length; i++){
			int y = Integer.valueOf(tab[i]);				
			events.addAll(windows[y].getEvents());			
		}
		
//		for(ProcessWindow pw : windows){
//			events.addAll(pw.getEvents());
//		}
		
		System.out.println("\n\n====  Detected Event(s) : ======");		
		
		if(events.size()>0){
			String snippetResult = "";
			int multiplier = 1;
			
			for(Event ev : events){		
				///////////////////NEXT TWO LINES TO BE INSERTED FOR EXTRACTING TWEETS FROM KEYWORDS///////////////////////
				//System.out.println(ev.toString().replaceAll(".nwt", ""));
				//fromKeywordsToTweets(ev.keywords.toString().replaceAll(".nwt", ""));
				fromKeywordsToTweets(ev.keywords.toString().replaceAll(".nwt", ""), tweetDate);
				StringTokenizer stringToks = new StringTokenizer(ev.getKeywordsAsString(), " ");
				System.out.println("keyword no: "+stringToks.countTokens());
				int count = 0;
				while(stringToks.hasMoreTokens()){				
					eventKeyWords.add(stringToks.nextToken().replaceAll(".nwt", ""));
					count++;
				}
				
				multiplier = multiplier*count;		
				if(snippetResult.equalsIgnoreCase(""))snippetResult = ev.toString().replaceAll(".nwt", "");
				else snippetResult = snippetResult+"\n"+ev.toString().replaceAll(".nwt", "");
				
				if(Boolean.valueOf(properties.get("debugMode").toString())){//////////////////////////////////////DEBUG MODE////////////////////////////////////////////					
					//Write in files SW1 and SW2 of Keywords of the events
					for(KeyWord kw : longKeyWords){
						for(String kwString : ev.getKeywords()){
							if(kw.getKeyWord().equalsIgnoreCase(kwString)){																
								kw.computations();								
								try {
									PrintWriter writer1 = new PrintWriter(nwtFolderPath+"/results/"+kwString+"_SW1_SW2.csv", "UTF-8");																											
									writer1.println(kwString +":" );
									writer1.println("SW1 : " + kw.getSW1ToString());
									writer1.println("SW2 : " + kw.getSW2ToString());
									writer1.close();
								} catch (FileNotFoundException | UnsupportedEncodingException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						}					
																
					}
				} ////////////////////////////////////// END DEBUG MODE////////////////////////////////////////////				
			}
			
			if(Boolean.valueOf(properties.get("debugMode").toString())){//////////////////////////////////////DEBUG MODE////////////////////////////////////////////
			//writing information of the events in a file
				try {
					PrintWriter writer1 = new PrintWriter(nwtFolderPath+"/results/Events_"+properties.get("delta").toString()+"_"+properties.get("gamma1").toString()+"_"+properties.get("gamma2").toString()+".txt", "UTF-8");																											
					writer1.println(snippetResult);
					writer1.close();
				} catch (FileNotFoundException | UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}																					
		}
		else
			System.out.println("Clusters filtered with Epsilon, There is no twitter event detected");
	} //END TreatSignals() 
	
	public static Set<String> compareKeywordsWithWikiEvents(Set<String> keyWordEvents, Set<String> wikiEvents){
		for(String keyword:keyWordEvents){
			for(String wikiEvent:wikiEvents){
				try {
					if(TwitterToWikipediaEventsComparator.sendGet(keyword, wikiEvent)){
						displayEventKeywords.add(keyword);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return displayEventKeywords;
	}
	
	public static void fromKeywordsToTweets(String eventString, String tweetDate) throws IOException{
		Pattern pattern = Pattern.compile("\\w+");
		Matcher matcher = pattern.matcher(eventString);
		HashSet<String> eventSet = new HashSet<String>();
		HashSet<String> eventSet2 = new HashSet<String>();
		HashSet<String> keyWordsSet = new HashSet<String>();
		while (matcher.find()) {
			keyWordsSet.add(matcher.group());
		}
		
		File dir1 = new File("./"+tweetDate+"/");
		File[] directoryListing1 = dir1.listFiles();		
		BufferedReader br = null;
		int increment = 0;
		if(directoryListing1 != null){
			for(File myFile:directoryListing1){
				if(!myFile.isDirectory()){
					br = new BufferedReader(new FileReader(myFile));
					String tweetLine;
					while ((tweetLine = br.readLine()) != null){
						int i = 0;
						for(String myKeyWord:keyWordsSet){
							if(containsKeyword(tweetLine,myKeyWord)){
								i++;
								eventSet2.add(tweetLine);
							}
						}
						if(i==keyWordsSet.size()){
							eventSet.add(tweetLine);
						}
					}
				}
			}
			for(String stringEvent:eventSet){
				System.out.println("Twitter event is: "+stringEvent);
			}
		}
	}
	
	public static boolean keywordswithwiki(String keyword){
		boolean myResult = false;
		try {
			if(TwitterToWikipediaEventsComparator.compare(keyword, "wikipediaEvents", true)) myResult = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return myResult;
	}
	
	public static boolean containsKeyword(String lineString, String keyWord){
		boolean result = false;
		StringTokenizer tokenizedLine = new StringTokenizer(lineString);
		while(tokenizedLine.hasMoreTokens()){
			if(tokenizedLine.nextToken().equalsIgnoreCase(keyWord)){
				result = true;
			}
		}
		return result;
	}
	
	public static Set<String> getEventKeyWords(){
		return eventKeyWords;
	}
	
	public static Set<String> getwikiEventsToDispaly(){
		return wikiEventsToDispaly;
	}
	
	public static Set<String> getdisplayEventKeywords(){
		return displayEventKeywords;
	}
}
