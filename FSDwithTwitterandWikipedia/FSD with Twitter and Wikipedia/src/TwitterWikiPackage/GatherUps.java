package TwitterWikiPackage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class GatherUps {
	static Properties tweetWikiProperties = new Properties();
	
	public static Properties loadProps(){
		FileInputStream input;
		try {
			input = new FileInputStream("TweetWikiParameters.properties");
			tweetWikiProperties.load(input);
			input.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return tweetWikiProperties;
	}
	

}
