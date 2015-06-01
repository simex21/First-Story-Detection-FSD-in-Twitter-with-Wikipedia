package TwitterWikiPackage;

import java.io.IOException;

class RunnableDemo implements Runnable {
	   private Thread t;
	   private String threadName;
	   
	   RunnableDemo(){

	   }
	   public void run() {
	      try {
				WikipediaEventDetector.wikiEventDetector();
	    	    Thread.sleep(50);
	     } catch (InterruptedException | IOException e) {
	         System.out.println("Wikipedia event detection interrupted.");
	     }
	   }
	   
	   public void start ()
	   {
	      if (t == null)
	      {
	         t = new Thread (this);
	         t.start ();
	      }
	      System.out.println("thread state is: "+t.getState());
	   }

	}

	public class WikipediaThread {
	   //public static void main(String args[]) {
		public static void wikiThread(){
	   
	      RunnableDemo R1 = new RunnableDemo();
	      R1.start();
	   }   
	}