package TwitterWikiPackage;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class TwitterToWikipediaEventsComparator {
	static boolean result = false;
	static boolean finalResult = false;
	
	public static boolean compare(String tweetEvent, String wikipediaEventFilePath, boolean direct) throws Exception{
		if(direct == true){
			FileInputStream fstream2 = new FileInputStream(wikipediaEventFilePath);
			BufferedReader br2 = new BufferedReader(new InputStreamReader(fstream2));
			String wikiLine;
			while ((wikiLine = br2.readLine()) != null)   {
				if(sendGet(tweetEvent, wikiLine)){
					finalResult = true;
					break;
				}
			}
		}
		return finalResult;
	}
	
	public static String replaceSpace(String str){
		StringBuffer strBuffer = new StringBuffer();
		for (int i = 0; i < str.length(); i++) {
			if (str.charAt(i) == ' ') {
				strBuffer.append("%20");
			}else {
				strBuffer.append(str.charAt(i));
			}
		}
		return strBuffer.toString();
	}
	
	public static boolean sendGet(String text1, String text2) throws Exception {
		
		String phrase1 = replaceSpace(text1);
		String phrase2 = replaceSpace(text2);
		String url = "http://swoogle.umbc.edu/StsService/GetStsSim?operation=api&phrase1="+phrase1+"&phrase2="+phrase2;

		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		con.setRequestMethod("GET");
 
		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();
 
		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
		if(Float.valueOf(response.toString()) >= 0.5){
			System.out.println("After comparison detected event is: "+text1);
			result = true;
		}
		return result;
	}

}
