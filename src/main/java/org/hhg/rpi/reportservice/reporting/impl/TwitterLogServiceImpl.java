package org.hhg.rpi.reportservice.reporting.impl;

import org.hhg.rpi.reportservice.reporting.ReportingSubServiceInterface;
import org.hhg.rpi.reportservice.utils.Constants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.RequestToken;
import twitter4j.conf.ConfigurationBuilder;

/**
 * Twitter log service. 
 * 
 * Will send status updates through Twitter using the
 * configured credentials.
 * 
 * @author Harold Hormaechea
 *
 */
@Component
public class TwitterLogServiceImpl implements ReportingSubServiceInterface {
	@Value("${rpi.twitter.numThreads}")
	private Integer numThreads;
	@Value("${rpi.twitter.maxCharsPerUpdate}")
	private Integer maxCharsPerUpdate;
	@Value("${rpi.twitter.maxRetryAttempts}")
	private Integer maxRetryAttempts;
	@Value("${rpi.twitter.secondsBetweenAttempts}")
	private Integer secondsBetweenAttempts;
	@Value("${rpi.twitter.consumerKey}")
	private String consumerKey;
	@Value("${rpi.twitter.consumerSecret}")
	private String consumerSecret;
	@Value("${rpi.twitter.accessToken}")
	private String accessToken;
	@Value("${rpi.twitter.accessSecret}")
	private String accessSecret;

	private Twitter twitter;

	public TwitterLogServiceImpl() {
	}

	@Override
	public void init() {
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true)
			.setAsyncNumThreads(numThreads)
			.setHttpRetryCount(maxRetryAttempts)
			.setHttpRetryIntervalSeconds(secondsBetweenAttempts)
			.setOAuthConsumerKey(consumerKey)
			.setOAuthConsumerSecret(consumerSecret)
			.setOAuthAccessToken(accessToken)
			.setOAuthAccessTokenSecret(accessSecret);
		
		
		TwitterFactory tf = new TwitterFactory(cb.build());
		twitter = tf.getInstance();
		try {
			RequestToken requestToken = twitter.getOAuthRequestToken();
		} catch (TwitterException | IllegalStateException ie) {

			if (!twitter.getAuthorization().isEnabled()) {
				System.err.println("OAuth consumer key/secret is not set.");
			}
		}
	}

	@Override
	public void debug(String message) {
		try {
			twitter.updateStatus(generateMessage(Constants.SOCIAL_DEBUG_PREFIX, message));
		} catch (TwitterException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void info(String message) {
		try {
			twitter.updateStatus(generateMessage(Constants.SOCIAL_INFO_PREFIX, message));
		} catch (TwitterException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void error(String message) {
		try {
			twitter.updateStatus(generateMessage(Constants.SOCIAL_ERROR_PREFIX, message));
		} catch (TwitterException e) {
			e.printStackTrace();
		}
	}

	private String generateMessage(String prefix, String messageToSend) {
		String newMessage = prefix + ": " + messageToSend;
		if (newMessage.length() > maxCharsPerUpdate)
			newMessage = newMessage.substring(0, maxCharsPerUpdate);

		return newMessage;
	}

}
