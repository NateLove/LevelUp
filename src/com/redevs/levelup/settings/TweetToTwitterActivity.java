package com.redevs.levelup.settings;


import com.redevs.levelup.LoginActivity;
import com.redevs.levelup.R;
import com.redevs.levelup.global.Globals;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

//import android.R;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class TweetToTwitterActivity extends Activity {

	private static final String TAG = "Redevs.twitter";

	/** Name to store the users access token */
	private static final String PREF_ACCESS_TOKEN = "accessToken";
	/** Name to store the users access token secret */
	private static final String PREF_ACCESS_TOKEN_SECRET = "accessTokenSecret";
	/** Consumer Key generated when you registered your app at https://dev.twitter.com/apps/ */
	private static final String CONSUMER_KEY = "KyrXidILXCHM04hT78JDPw";
	/** Consumer Secret generated when you registered your app at https://dev.twitter.com/apps/  */
	private static final String CONSUMER_SECRET = "7qRxW30miSFpXnlXWLm1GpepmoguxFaP7MQzhDiBcQg"; // XXX Encode in your app
	/** The url that Twitter will redirect to after a user log's in - this will be picked up by your app manifest and redirected into this activity */
	private static final String CALLBACK_URL = "tweet-to-twitter-redevs-levelup:///";
	/** Preferences to store a logged in users credentials */
	private SharedPreferences mPrefs;
	/** Twitter4j object */
	private Twitter mTwitter;
	/** The request token signifies the unique ID of the request you are sending to twitter  */
	private RequestToken mReqToken;
	public static final String PREFS_NAME = "MyPrefsFile";

	private Button mLoginButton;
	@SuppressWarnings("unused")
	private Button mTweetButton;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		startLogin();
		
	}

	public void startLogin()
	{
		if(!Globals.hasStarted)
		{
			//open loginActivity
			Intent i = new Intent(TweetToTwitterActivity.this, LoginActivity.class);
			startActivity(i);

		}

		else{


			Log.i(TAG, "Loading TweetToTwitterActivity");
			setContentView(R.layout.main);
			TextView tv = (TextView) findViewById(R.id.loginText);
			tv.setText("Please Log In");

			// Create a new shared preference object to remember if the user has
			// already given us permission
			mPrefs = getSharedPreferences("twitterPrefs", MODE_PRIVATE);
			Log.i(TAG, "Got Preferences");

			// Load the twitter4j helper
			mTwitter = new TwitterFactory().getInstance();
			Log.i(TAG, "Got Twitter4j");

			// Tell twitter4j that we want to use it with our app
			mTwitter.setOAuthConsumer(CONSUMER_KEY, CONSUMER_SECRET);
			Log.i(TAG, "Inflated Twitter4j");

			mLoginButton = (Button) findViewById(R.id.login_button);
			mLoginButton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					buttonLogin(v);
				}
			});
//			mTweetButton = (Button) findViewById(R.id.tweet_button);
//			mTweetButton.setOnClickListener(new OnClickListener() {
//
//				@Override
//				public void onClick(View v) {
//					// TODO Auto-generated method stub
//					buttonTweet(v);
//				}
//			});
			if (mPrefs.contains(PREF_ACCESS_TOKEN)) {
				Log.i(TAG, "Repeat User");
				loginAuthorisedUser();
			}
		}
	}
	/**
	 * Button clickables are declared in XML as this projects min SDK is 1.6</br> </br>
	 * Checks if the user has given this app permission to use twitter
	 * before</br> If so login and enable tweeting</br>
	 * Otherwise redirect to Twitter for permission
	 *
	 * @param v the clicked button
	 */
	public void buttonLogin(View v) {
		Log.i(TAG, "Login Pressed");
		if (mPrefs.contains(PREF_ACCESS_TOKEN)) {
			Log.i(TAG, "Repeat User");
			loginAuthorisedUser();
		} else {
			Log.i(TAG, "New User");
			loginNewUser();
		}
	}

	/**
	 * Button clickables are declared in XML as this projects min SDK is 1.6</br> </br>
	 *
	 * @param v the clicked button
	 */
	public void buttonTweet(View v) {
		Log.i(TAG, "Tweet Pressed");
		tweetMessage();
	}

	/**
	 * Create a request that is sent to Twitter asking 'can our app have permission to use Twitter for this user'</br>
	 * We are given back the {@link mReqToken}
	 * that is a unique indetifier to this request</br>
	 * The browser then pops up on the twitter website and the user logins in ( we never see this informaton
	 * )</br> Twitter then redirects us to {@link CALLBACK_URL} if the login was a success</br>
	 *
	 */
	private void loginNewUser() {
		try {
			Editor editor = mPrefs.edit();
			editor.putBoolean("twitterSetup", true);
			editor.commit();
			Log.i(TAG, "Request App Authentication");
			mReqToken = mTwitter.getOAuthRequestToken(CALLBACK_URL);

			Log.i(TAG, "Starting Webview to login to twitter");
			MyWebView twitterSite = new MyWebView(this);
			twitterSite.loadUrl(mReqToken.getAuthenticationURL());
			twitterSite.requestFocus(View.FOCUS_DOWN);
			twitterSite.setOnTouchListener(new View.OnTouchListener() {
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN:
					case MotionEvent.ACTION_UP:
						if (!v.hasFocus()) {
							v.requestFocus();
						}
						break;
					}
					return false;
				}


			});

			setContentView(twitterSite);
			enableTweetButton();
			
		} catch (TwitterException e) {
			Toast.makeText(this, "Twitter Login error, try again later", Toast.LENGTH_SHORT).show();
		}
		
	}

	/**
	 * The user had previously given our app permission to use Twitter</br>
	 * Therefore we retrieve these credentials and fill out the Twitter4j helper
	 */
	private void loginAuthorisedUser() {
		String token = mPrefs.getString(PREF_ACCESS_TOKEN, null);
		String secret = mPrefs.getString(PREF_ACCESS_TOKEN_SECRET, null);

		// Create the twitter access token from the credentials we got previously
		AccessToken at = new AccessToken(token, secret);

		mTwitter.setOAuthAccessToken(at);

		Toast.makeText(this, "Welcome back", Toast.LENGTH_SHORT).show();
		TextView tv = (TextView) findViewById(R.id.loginText);
		tv.setText("Logged In");
		
		enableTweetButton();
	}

	/**
	 * Catch when Twitter redirects back to our {@link CALLBACK_URL}</br>
	 * We use onNewIntent as in our manifest we have singleInstance="true" if we did not the
	 * getOAuthAccessToken() call would fail
	 */
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		Log.i(TAG, "New Intent Arrived");
		dealWithTwitterResponse(intent);
	}

	@Override
	protected void onResume() {
		super.onResume();
		mPrefs = getSharedPreferences("twitterPrefs", MODE_PRIVATE);
		Log.i(TAG, "Arrived at onResume");
		if (mPrefs.contains(PREF_ACCESS_TOKEN)) {
			Log.i(TAG, "Repeat User");
			startLogin();
		} 
	}

	/**
	 * Twitter has sent us back into our app</br>
	 * Within the intent it set back we have a 'key' we can use to authenticate the user
	 *
	 * @param intent
	 */
	private void dealWithTwitterResponse(Intent intent) {
		Uri uri = intent.getData();
		if (uri != null && uri.toString().startsWith(CALLBACK_URL)) { // If the user has just logged in
			String oauthVerifier = uri.getQueryParameter("oauth_verifier");

			authoriseNewUser(oauthVerifier);
		}
	}

	/**
	 * Create an access token for this new user</br>
	 * Fill out the Twitter4j helper</br>
	 * And save these credentials so we can log the user straight in next time
	 *
	 * @param oauthVerifier
	 */
	private void authoriseNewUser(String oauthVerifier) {
		try {
			AccessToken at = mTwitter.getOAuthAccessToken(mReqToken, oauthVerifier);
			mTwitter.setOAuthAccessToken(at);

			saveAccessToken(at);
			Editor editor = mPrefs.edit();
			editor.putBoolean("twitterSetup", true);
			editor.commit();
			
			// Set the content view back after we changed to a webview
			setContentView(R.layout.main);

			enableTweetButton();
		} catch (TwitterException e) {
			Toast.makeText(this, "Twitter auth error x01, try again later", Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * Allow the user to Tweet
	 */
	private void enableTweetButton() {
		Log.i(TAG, "User logged in - allowing to tweet");
		mLoginButton.setEnabled(false);

//		mTweetButton.setEnabled(true);
	}

	/**
	 * Send a tweet on your timeline, with a Toast msg for success or failure
	 */
	private void tweetMessage() {
		try {
			mTwitter.updateStatus("Test - Tweeting with LevelUp! for #Android and @Twitter4j");
			Log.i("TweetMessage"," Made it here nothing to it");
			Toast.makeText(this, "Tweet Successful!", Toast.LENGTH_SHORT).show();
		} catch (TwitterException e) {
			Toast.makeText(this, "Tweet error, try again later", Toast.LENGTH_SHORT).show();
		}
	}

	private void saveAccessToken(AccessToken at) {
		String token = at.getToken();
		String secret = at.getTokenSecret();
		Editor editor = mPrefs.edit();
		editor.putString(PREF_ACCESS_TOKEN, token);
		editor.putString(PREF_ACCESS_TOKEN_SECRET, secret);
		editor.commit();
	}


	class MyWebView extends WebView {

		public MyWebView(Context context) {
			super(context);
		}

		@Override
		public boolean onCheckIsTextEditor() {
			return true; 
		}

		@Override
		public boolean onKeyDown(int keyCode, KeyEvent event) {
			if ((keyCode == KeyEvent.KEYCODE_BACK) && this.canGoBack()) {
				this.goBack();
				return true;
			}
			return super.onKeyDown(keyCode, event);
		}
	}
}








