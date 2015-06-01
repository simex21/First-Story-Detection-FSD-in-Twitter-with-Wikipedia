package TwitterWikiPackage;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import twitter4j.FilterQuery;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.Twitter;
import twitter4j.TwitterBase;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.ConfigurationBuilder;


/**
 * Receives tweets from Twitter streaming API.
 * You need to pass the right parameters in the config.properties file.
 *
 */

public class TweetsFetcher {

	static int firstCreatedAt;
	static int numberOfTweets;
	static int tweetSample;
	static int duration;
	static long startTime; 
	static long endTime;
	static String tweetDate;

	public static void fetchTweets() throws TwitterException, IOException {
		FileInputStream input = new FileInputStream("TweetWikiParameters.properties");
		Properties tweetWikiProperties = new Properties();
		tweetWikiProperties.load(input);
		duration = Integer.parseInt(tweetWikiProperties.getProperty("fetchDuration"));
		numberOfTweets = duration*5000/3;
		tweetSample = Integer.parseInt(tweetWikiProperties.getProperty("tweetSample"));
		tweetDate = tweetWikiProperties.getProperty("tweetDate");
		input.close();
		startTime = System.currentTimeMillis();
		endTime   = startTime+duration*60000;

		StatusListener listener = new StatusListener() {
			int tweetCount = 0;

			public void onStatus(Status status) {
				String lang = status.getUser().getLang();
				
				if (tweetCount < numberOfTweets&&System.currentTimeMillis()<=endTime) {
						if (lang.equals("en")&&!status.isRetweet()) {
							storeInFile(status);
							tweetCount++;
							if(tweetCount == 1){
								firstCreatedAt = status.getCreatedAt().getHours()*3600+status.getCreatedAt().getMinutes()*60+status.getCreatedAt().getSeconds();
							}
						}
				} else {
					System.out.println("Finished fetching tweets!");
					try {
						TimeUnit.SECONDS.sleep(2);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					System.exit(0);
				}
			}

			private void storeInFile(Status status) {
				FileWriter fw2 = null;
				BufferedWriter bw2 = null;
				try {
					int createdAt = status.getCreatedAt().getHours()*3600+status.getCreatedAt().getMinutes()*60+status.getCreatedAt().getSeconds();
					float timeDifference = (createdAt-firstCreatedAt)/tweetSample;
					int integerTime = (int)timeDifference;
					int starting = integerTime*tweetSample+firstCreatedAt;
					int ending = starting+tweetSample;
					String timeStamp = "./"+tweetDate+"/"+String.valueOf(starting)+"_"+String.valueOf(ending)+".txt";
					File file = new File(timeStamp);
					if (!file.exists()) {
						file.createNewFile();						
					}	
					fw2 = new FileWriter(file.getAbsoluteFile(), true);
					bw2 = new BufferedWriter(fw2);
					//bw.write(DataObjectFactory.getRawJSON(status));
					bw2.write(status.getText());
					bw2.newLine();
				} catch (Exception e) {
					e.toString();
				}
				try {
					bw2.flush();
					bw2.close();
					fw2.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			public void onDeletionNotice(
					StatusDeletionNotice statusDeletionNotice) {
			}

			public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
			}

			public void onException(Exception ex) {
				ex.toString();
			}

			public void onScrubGeo(long userId, long upToStatusId) {
			}

			public void onStallWarning(StallWarning warning) {
			}
		};
		
		Properties prop = new Properties();
		try {
			InputStream in = TweetsFetcher.class.getClassLoader()
					.getResourceAsStream("twitter4j.properties");
			prop.load(in);
		} catch (IOException e) {
			e.toString();
		}

		ConfigurationBuilder twitterConf = new ConfigurationBuilder();
		twitterConf.setIncludeEntitiesEnabled(true);
		twitterConf.setDebugEnabled(Boolean.valueOf(prop.getProperty("debug")));
		twitterConf.setOAuthAccessToken(prop.getProperty("oauth.accessToken"));
		twitterConf.setOAuthAccessTokenSecret(prop
				.getProperty("oauth.accessTokenSecret"));
		twitterConf.setOAuthConsumerKey(prop.getProperty("oauth.consumerKey"));
		twitterConf.setOAuthConsumerSecret(prop
				.getProperty("oauth.consumerSecret"));
		twitterConf.setJSONStoreEnabled(true);

		TwitterStream twitterStream = new TwitterStreamFactory(
				twitterConf.build()).getInstance();
		twitterStream.addListener(listener);

		twitterStream.sample();
	}

}
