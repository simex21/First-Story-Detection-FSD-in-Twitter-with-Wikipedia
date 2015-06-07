package TwitterWikiPackage;

import java.io.IOException;

public class WikipediaThread extends Thread {
	public WikipediaThread(){
		//create a new thread for wikipedia event detection
		super("Wiki Thread");
		System.out.println("Starting Wikipedia Event Detection thread...");
		start(); //start the thread
	}
	//This is the entry point for the Wiki thread
	public void run(){
		try{
			WikipediaEventDetector.wikiEventDetector();
			Thread.sleep(50);
		}catch(IOException e){
			System.out.println("Necessary files were not found.");
		}
		catch(InterruptedException a){
			System.out.println("Wikipedia event detection thread interrupted.");
		}
		System.out.println("Exiting Wikipedia Event detection thread.");
	}
}
