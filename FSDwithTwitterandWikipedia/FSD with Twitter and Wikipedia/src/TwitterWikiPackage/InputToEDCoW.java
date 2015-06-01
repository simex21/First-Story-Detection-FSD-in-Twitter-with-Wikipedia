package TwitterWikiPackage;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InputToEDCoW {

	static Pattern Contractions;
	Matcher matches;
	static HashSet<String> dictionary = new HashSet<String>();
	
	public static ArrayList<String> wordsList = new ArrayList<String>();
	public static String[] stopWordsofwordnet = {
			"without", "see", "unless", "due", "also", "must", "might", "like", "]", "[", "}", "{", "<", ">", "?", "\"", "\\", "/", ")", "(", "will", "may", "can", "much", "every", "the", "in", "other", "this", "the", "many", "any", "an", "or", "for", "in", "an", "an ", "is", "a", "about", "above", "after", "again", "against", "all", "am", "an", "and", "any", "are", "aren’t", "as", "at", "be", "because", "been", "before", "being", "below", "between", "both", "but", "by", "can’t", "cannot", "could",
			"couldn’t", "did", "didn’t", "do", "does", "doesn’t", "doing", "don’t", "down", "during", "each", "few", "for", "from", "further", "had", "hadn’t", "has", "hasn’t", "have", "haven’t", "having",
			"he", "he’d", "he’ll", "he’s", "her", "here", "here’s", "hers", "herself", "him", "himself", "his", "how", "how’s", "i ", " i", "i’d", "i’ll", "i’m", "i’ve", "if", "in", "into", "is",
			"isn’t", "it", "it’s", "its", "itself", "let’s", "me", "more", "most", "mustn’t", "my", "myself", "no", "nor", "not", "of", "off", "on", "once", "only", "ought", "our", "ours", "ourselves",
			"out", "over", "own", "con", "aux", "same", "shan’t", "she", "she’d", "she’ll", "she’s", "should", "shouldn’t", "so", "some", "such", "than",
			"that", "that’s", "their", "theirs", "them", "themselves", "then", "there", "there’s", "these", "they", "they’d", "they’ll", "they’re", "they’ve",
			"this", "those", "through", "to", "too", "under", "until", "up", "very", "was", "wasn’t", "we", "we’d", "we’ll", "we’re", "we’ve",
			"were", "weren’t", "what", "what’s", "when", "when’s", "where", "where’s", "which", "while", "who", "who’s", "whom",
			"why", "why’s", "with", "won’t", "would", "wouldn’t", "you", "you’d", "you’ll", "you’re", "you’ve", "your", "yours", "yourself", "yourselves",
			"Without", "See", "Unless", "Due", "Also", "Must", "Might", "Like", "Will", "May", "Can", "Much", "Every", "The", "In", "Other", "This", "The", "Many", "Any", "An", "Or", "For", "In", "An", "An ", "Is", "A", "About", "Above", "After", "Again", "Against", "All", "Am", "An", "And", "Any", "Are", "Aren’t", "As", "At", "Be", "Because", "Been", "Before", "Being", "Below", "Between", "Both", "But", "By", "Can’t", "Cannot", "Could",
			"Couldn’t", "Did", "Didn’t", "Do", "Does", "Doesn’t", "Doing", "Don’t", "Down", "During", "Each", "Few", "For", "From", "Further", "Had", "Hadn’t", "Has", "Hasn’t", "Have", "Haven’t", "Having",
			"He", "He’d", "He’ll", "He’s", "Her", "Here", "Here’s", "Hers", "Herself", "Him", "Himself", "His", "How", "How’s", "I ", " I", "I’d", "I’ll", "I’m", "I’ve", "If", "In", "Into", "Is",
			"Isn’t", "It", "It’s", "Its", "Itself", "Let’s", "Me", "More", "Most", "Mustn’t", "My", "Myself", "No", "Nor", "Not", "Of", "Off", "On", "Once", "Only", "Ought", "Our", "Ours", "Ourselves",
			"Out", "Over", "Own", "Same", "Shan’t", "She", "She’d", "She’ll", "She’s", "Should", "Shouldn’t", "So", "Some", "Such", "Than",
			"That", "That’s", "Their", "Theirs", "Them", "Themselves", "Then", "There", "There’s", "These", "They", "They’d", "They’ll", "They’re", "They’ve",
			"This", "Those", "Through", "To", "Too", "Under", "Until", "Up", "Very", "Was", "Wasn’t", "We", "We’d", "We’ll", "We’re", "We’ve",
			"Were", "Weren’t", "What", "What’s", "When", "When’s", "Where", "Where’s", "Which", "While", "Who", "Who’s", "Whom",
			"Why", "Why’s", "With", "Won’t", "Would", "Wouldn’t", "You", "You’d", "You’ll", "You’re", "You’ve", "Your", "Yours", "Yourself", "Yourselves"
			};
	static String folderPath;
	public static void inputToEdcow() throws IOException{
		int i=0;
		dictionaryToHashSet();
		HashSet<String> wordset = new HashSet<String>();
		FileInputStream input = new FileInputStream("TweetWikiParameters.properties");
		Properties tweetWikiProperties = new Properties();
		tweetWikiProperties.load(input);
	
		folderPath = "./"+tweetWikiProperties.getProperty("tweetDate")+"/";
		
		File dir = new File(folderPath);
		File[] directoryListing = dir.listFiles();
		if (directoryListing != null) {
			for (File child : directoryListing) {
				if(!child.isDirectory()){
					FileInputStream filestream = new FileInputStream(child);
					BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(filestream));

					String stringLine;

					while ((stringLine = bufferedreader.readLine()) != null)   {
						String stringLine2 = stringLine.replaceAll("[^a-zA-Z0-9]", " ");
						List<String> toks = Twokenize.tokenizeRawTweetText(stringLine2);
						
						for(String words:toks){
							wordset.add(words);
						}
					}
					bufferedreader.close();
				}
		    }
		} else {
			System.out.println("Error input is directory? "+dir.isDirectory());
		}
		 
		Set<String> stopWords = new HashSet<String>();
		for(int k=0; k<stopWordsofwordnet.length; k++){
			stopWords.add(stopWordsofwordnet[k]);
		}

		Iterator<String> iter = wordset.iterator();
		while(iter.hasNext()){
			String iterstr = iter.next();
			boolean isStopWord = false;
			for(String keyword:stopWords){
				if(iterstr.equalsIgnoreCase(keyword)){
					isStopWord = true;
				}
			}
			if(iterstr.length()<3||isStopWord||!dictionary.contains(iterstr)){
				iter.remove();
			}
		}
		File ntFolder = new File(folderPath+"nt");
		File nwtFolder = new File(folderPath+"nwt");
		if (!ntFolder.exists()) ntFolder.mkdir(); 
		if (!nwtFolder.exists()) nwtFolder.mkdir(); 
		System.out.println(wordset.size());
		createnumberoftweetntfile();
		for(String keyword:wordset){
			createKeywordnwtFiles(keyword);
		}
	}
	
	public static void dictionaryToHashSet() throws IOException{
		FileInputStream filestream = new FileInputStream("C:/Users/Mihret/TwitterProject/Dictionary.txt");
		BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(filestream));
		String stringLine;
		while ((stringLine = bufferedreader.readLine()) != null)   {
			dictionary.add(stringLine);
		}
		bufferedreader.close();
	}
	
	public static void createKeywordnwtFiles(String keyword) throws IOException{
		File dir1 = new File(folderPath);
		File[] directoryListing1 = dir1.listFiles();
		File file = new File(folderPath+"nwt/"+keyword+".nwt");
		FileWriter fw = null;
		BufferedWriter bw = null;
		if (!file.exists()) {
			file.createNewFile();
		}	
		fw = new FileWriter(file.getAbsoluteFile(), true);
		bw = new BufferedWriter(fw);
		
		BufferedReader bufferedreader = null;
		int increment = 0;
		if(directoryListing1 != null){
			for(File myFile:directoryListing1){
				if(!myFile.isDirectory()){
					bufferedreader = new BufferedReader(new FileReader(myFile));
					String stringLine;
					increment++;
					int number = 0;

					while ((stringLine = bufferedreader.readLine()) != null)   {
						if(containsKeyword(stringLine,keyword)){
						  number++;
					  }
					}
					bw.write(String.valueOf(number+1));
					if(increment!=directoryListing1.length-2){
						bw.write(";");
					}
					bufferedreader.close();
				}
			}
		}
		bw.close();
		fw.close();	
	}
	public static void createnumberoftweetntfile() throws IOException{
		File dir2 = new File(folderPath);
		File[] directoryListing2 = dir2.listFiles();
		File file = new File(folderPath+"nt/"+"tweets.nt");
		FileWriter fw = null;
		BufferedWriter bw = null;
		if (!file.exists()) {
			file.createNewFile();
		}	
		fw = new FileWriter(file.getAbsoluteFile(), true);
		bw = new BufferedWriter(fw);
		FileInputStream filestream = null; 
		BufferedReader bufferedreader = null;
		int increment = 0;
		if(directoryListing2 != null){
			for(File myFile:directoryListing2){
				if(!myFile.isDirectory()){
					bufferedreader = new BufferedReader(new FileReader(myFile));
					String stringLine;
					increment++;
					int number = 0;

					while ((stringLine = bufferedreader.readLine()) != null)   {
						  number++;
					}
					//bw.write(String.valueOf(number));
					bw.write(String.valueOf(number+1));
					if(increment!=directoryListing2.length-2){
						bw.write(";");
					}
					bufferedreader.close();
				}
			}
			bw.close();
			fw.close();	
			}
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
}
