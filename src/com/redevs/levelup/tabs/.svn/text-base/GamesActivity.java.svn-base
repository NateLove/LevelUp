package com.redevs.levelup.tabs;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.impl.client.DefaultHttpClient;

import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;


import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.Facebook;
import com.facebook.android.FacebookError;
import com.facebook.android.Util;

import com.redevs.levelup.BaseRequestListener;
import com.redevs.levelup.R;
import com.redevs.levelup.SessionStore;
import com.redevs.levelup.async.HttpCallback;
import com.redevs.levelup.async.HttpUtils;
import com.redevs.levelup.global.Globals;
import com.redevs.levelup.qsort.MyQsort;
import com.redevs.levelup.settings.SettingsActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Vibrator;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class GamesActivity extends Activity implements OnItemClickListener, OnItemLongClickListener {

	/* whether or not to include system apps */
	private static final boolean INCLUDE_SYSTEM_APPS = false;
	private String TAG = "GamesActivity";
	private ListView mAppsList;
	private AppListAdapter mAdapter;
	private List<App> mApps;
	private boolean refresh_ = true;
	private ProgressBar pb;
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
	@SuppressWarnings("unused")
	private RequestToken mReqToken;
	public static final String PREFS_NAME = "MyPrefsFile";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list);



		
		pb = (ProgressBar) findViewById(R.id.progressBar1);


		refresh();



	}
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


		final App app = (App) parent.getItemAtPosition(position);

		AlertDialog.Builder builder = new AlertDialog.Builder(this);

		String msg = app.getTitle() + "\n\n" + 
		"Version " + app.getVersionName() + " (" +
		app.getVersionCode() + ")" +
		(app.getDescription() != null ? ("\n\n" + app.getDescription()) : "");

		builder.setMessage(msg)
		.setCancelable(true)
		.setTitle(app.getTitle())
		.setIcon(mAdapter.getIcons().get(app.getPackageName()))
		.setPositiveButton("Launch", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				// start the app by invoking its launch intent
				mPrefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);


				SharedPreferences.Editor editor = mPrefs.edit();
				Log.i("Validation 23", "Show sharing preference: " + mPrefs.getBoolean("showSharingPreference", true));

				if(mPrefs.getBoolean("showSharingPreference", true)){

					Intent i = new Intent(getApplicationContext(), SharingPreferencesActivity.class);
					i.putExtra("Package", app.getPackageName());
					startActivity(i);
					@SuppressWarnings("unused")
					boolean launch = mPrefs.getBoolean("launchApp", false);
					//					while(!launch)
					//					{
					//						launch = mPrefs.getBoolean("launchApp", false);
					//					}


				}
				sendPackage(app.getTitle() + "," + app.getPackageName(), "uidpackagepairs");
				//------------POST TO FACEBOOK CODE--------------//

				if(mPrefs.getBoolean("shareFromFacebook", false)){
					Facebook facebook = new Facebook("163863397043367");

					SessionStore.restore(facebook, getBaseContext());

					if(facebook.isSessionValid()) {
						//Post A feed to person's wall.

						Bundle params = new Bundle();

						params.putString("caption","Level Up! is an android app that brings social networking to android gaming.");
						params.putString("link", "http://market.android.com/details?id=" + app.getPackageName()	);
						params.putString("message", "I just played " + app.getTitle() + " via Level Up!  You should " +
						"check it out!");
						params.putString("description", "You ain't got no pancake mix.");
						params.putString("picture", "http://dl.dropbox.com/u/27727908/Android_Market.png");

						// an AsyncRunner to handle the dispatching the post
						AsyncFacebookRunner mAsyncRunner;
						mAsyncRunner = new AsyncFacebookRunner(facebook);
						mAsyncRunner.request("me/feed", params, "POST", new LinkUploadListener(), null);	
					}
				}
				//----------END OF POST TO FACEBOOK CODE----------//

				//---------Start post to Twitter--------------//

				TwitterTweeter tt = new TwitterTweeter();
				tt.execute(app);
				//-----------------END of Twitter--------------//
				mPrefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

				if(!mPrefs.getBoolean("showSharingPreference", true)){
					Intent i = getPackageManager().getLaunchIntentForPackage(app.getPackageName());
					try {
						if (i != null) {
							startActivity(i);
						} else {
							i = new Intent(app.getPackageName());
							startActivity(i);
						}
					} catch (ActivityNotFoundException err) {
						Toast.makeText(GamesActivity.this, "Error launching app", Toast.LENGTH_SHORT).show();
					}
				}
				editor = mPrefs.edit();

				editor.putBoolean("showSharingPreference", false);
				editor.commit();

			}
		})
		.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.cancel();
			}
		});
		AlertDialog dialog = builder.create();
		dialog.show();

	}

	/**
	 * Uses the package manager to query for all currently installed apps which are put into beans and returned
	 * in form of a list.
	 * 
	 * @param includeSysApps whether or not to include system applications
	 * @param result 
	 * @return a list containing an {@code App} bean for each installed application 
	 */
	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View arg1, int position, long id) {
		// TODO Auto-generated method stub
		Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		// Vibrate for 300 milliseconds
		v.vibrate(50);

		final App app = (App) parent.getItemAtPosition(position);
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		String msg = app.getTitle() + "\n\n" + 
		"Version " + app.getVersionName() + " (" +
		app.getVersionCode() + ")" +
		(app.getDescription() != null ? ("\n\n" + app.getDescription()) : "");


		builder.setMessage(msg)
		.setCancelable(true)
		.setTitle(app.getTitle())
		.setIcon(mAdapter.getIcons().get(app.getPackageName()))
		.setPositiveButton("Remove", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				//addToPackageList(app.getPackageName());
				Log.i("Remove", "package: " + app.getPackageName() );
				removeFromPackageList(app.getPackageName());
			}


		})
		.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.cancel();
			}
		});
		AlertDialog dialog = builder.create();
		dialog.show();

		return false;
	}



	private List<App> loadInstalledApps(boolean includeSysApps, ArrayList<String> result) {
		List<App> apps = new ArrayList<App>();

		ArrayList<String> customPKG = getPackagesFromFile();
		// the package manager contains the information about all installed apps
		PackageManager packageManager = getPackageManager();

		List<PackageInfo> packs = packageManager.getInstalledPackages(0); //PackageManager.GET_META_DATA 

		for(int i=0; i < packs.size(); i++) {
			PackageInfo p = packs.get(i);
			ApplicationInfo a = p.applicationInfo;
			// skip system apps if they shall not be included
			if ((!includeSysApps) && ((a.flags & ApplicationInfo.FLAG_SYSTEM) == 1)) {
				continue;
			}
			if(result.contains(p.packageName) || customPKG.contains(p.packageName)){
				App app = new App();
				app.setTitle(p.applicationInfo.loadLabel(packageManager).toString());
				app.setPackageName(p.packageName);
				app.setVersionName(p.versionName);
				app.setVersionCode(p.versionCode);
				CharSequence description = p.applicationInfo.loadDescription(packageManager);
				app.setDescription(description != null ? description.toString() : "");
				apps.add(app);
			}
		}
		pb.setVisibility(View.INVISIBLE);
		apps = MyQsort.sortArrayList(apps);
		
		return apps;
	}

	/**
	 * An asynchronous task to load the icons of the installed applications.
	 */
	private class LoadIconsTask extends AsyncTask<App, Void, Void> {
		@Override
		protected Void doInBackground(App... apps) {

			Map<String, Drawable> icons = new HashMap<String, Drawable>();
			PackageManager manager = getApplicationContext().getPackageManager();

			for (App app : apps) {
				String pkgName = app.getPackageName();
				Drawable ico = null;
				try {
					Intent i = manager.getLaunchIntentForPackage(pkgName);
					if (i != null) {
						ico = manager.getActivityIcon(i);
					}
				} catch (NameNotFoundException e) {
					Log.e("ERROR", "Unable to find icon for package '" + pkgName + "': " + e.getMessage());
				}
				icons.put(app.getPackageName(), ico);
			}
			mAdapter.setIcons(icons);

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			mAdapter.notifyDataSetChanged();
		}
	}




	public void makeToast(String message)
	{
		Toast.makeText(getApplicationContext(), message,
				Toast.LENGTH_SHORT).show();
	}

	class PrintXml extends AsyncTask<HttpResponse, Void, ArrayList<String>>{



		@Override
		protected ArrayList<String> doInBackground(HttpResponse... params) {
			// TODO Auto-generated method stub
			ArrayList<String> data = null;
			try {
				data = printXml(HttpUtils.get().responseToString(params[0]));

			} catch (XmlPullParserException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return data;

		}

		@Override
		protected void onPostExecute(ArrayList<String> result) {
			mAppsList = (ListView) findViewById(R.id.appslist);

			mAppsList.setOnItemClickListener(GamesActivity.this);

			mAppsList.setOnItemLongClickListener(GamesActivity.this);

			mApps = loadInstalledApps(INCLUDE_SYSTEM_APPS, result);

			mAdapter = new AppListAdapter(getApplicationContext());
			mAdapter.setListItems(mApps);
			mAppsList.setAdapter(mAdapter);

			new LoadIconsTask().execute(mApps.toArray(new App[]{}));



		}


		public ArrayList<String> printXml(String xml) throws XmlPullParserException, IOException, InterruptedException
		{

			ArrayList<String> packages = new ArrayList<String>();
			//wait();
			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
			factory.setNamespaceAware(true);
			XmlPullParser xpp = factory.newPullParser();


			xpp.setInput( new StringReader ( xml ) );
			int eventType = xpp.getEventType();




			//while(eventType != XmlPullParser.END_DOCUMENT){

			while(eventType != XmlPullParser.END_DOCUMENT){

				if(eventType == XmlPullParser.START_DOCUMENT) {

					//theView.setText("");//Start document");
				} else if(eventType == XmlPullParser.START_TAG) {

					String name = xpp.getName();
					if(name.equalsIgnoreCase("name"))
					{

						//	Log.e("WOOOOOOOOHOOOOOOOOO", "yep");//xpp.getText());
						xpp.next();
						//temp += xpp.getText() + "\n";
						//Log.e("WOOOOOOOOHOOOOOOOOO", xpp.getText());
						packages.add(xpp.getText());


					}



				}
				eventType = xpp.next();
			}
			return packages;





		}


	}

	//Link Posting Listener for facebook.
	public class LinkUploadListener extends BaseRequestListener {

		@Override
		public void onComplete(final String response, Object state) {
			Log.d("Link", "Response: " + response.toString());
			try {
				// process the response here: (executed in background thread)
				JSONObject json = Util.parseJson(response);
				final String src = json.getString("src");

				// then post the processed result back to the UI thread
				// if we do not do this, an runtime exception will be generated
				// e.g. "CalledFromWrongThreadException: Only the original
				// thread that created a view hierarchy can touch its views."
				GamesActivity.this.runOnUiThread(new Runnable() {
					public void run() {
						// mText
						// .setText("Hello there, photo has been uploaded at \n"
						// + src);
						Log.d("Link Post", src);
					}
				});
			} catch (JSONException e) {
				Log.w("Facebook-Example", "JSON Error in response");
			} catch (FacebookError e) {
				Log.w("Facebook-Example", "Facebook Error: " + e.getMessage());
			}
		}
	}



	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.gamesmenu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		//makeToast(item.getTitle().toString());


		switch (item.getItemId()) {
		case R.id.about:
			showAbout();
			return true;
		case R.id.settings:
			Intent i = new Intent(getApplicationContext(), SettingsActivity.class);
			startActivity(i);
			return true;
		case R.id.addgame:
			addGame();
			setRefresh_(true);

			return true;
		default:
			return super.onOptionsItemSelected(item);
		}

	}

	private void addGame() {
		// TODO Auto-generated method stub
		Intent i = new Intent(getApplicationContext(), AddGameActivity.class);
		startActivity(i);


	}
	private void showAbout(){
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		alert.setTitle("Level Up! v1.0");
		alert.setMessage(Globals.about);
		alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
			}
		});
		alert.show();
	}


	private ArrayList<String> getPackagesFromFile() {
		// TODO Auto-generated method stub
		ArrayList<String> temp = new ArrayList<String>();
		try {
			String path = Environment.getExternalStorageDirectory().getCanonicalPath() + "/data/levelup/";


			//view2_.setText("");
			File root = new File(path);


			root.mkdirs();


			File f = new File(root, "custom.txt");
			if(!f.exists())
			{
				//view2_.setText("now I am here");
				FileOutputStream fos = new FileOutputStream(f);
				fos.close();

			}



			FileReader r = new FileReader(f);
			BufferedReader br = new BufferedReader(r);



			// Open the file that is the first
			// command line parameter


			String strLine;
			// Read File Line By Line



			while ((strLine = br.readLine()) != null) {
				// Print the content on the console
				//String[] allWords;
				if(!strLine.equals(null)) temp.add(strLine);
				Log.i("packages from file", strLine);
			}

			r.close();


		} catch (Exception e) {// Catch exception if any
			//System.err.println("Error: " + e.getMessage());
		}
		return temp;
	}

	@Override
	public void onResume()
	{

		super.onResume();
		Log.i("RESUME", "entering on resume");

		Globals.refreshGames++;
		mPrefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
		Globals.textColor = mPrefs.getInt("textColor", Globals.defaultTextColor);
		Globals.backgroundColor = mPrefs.getInt("backgroundColor", Globals.defaultBackgroundColor);
		Globals.hasStarted = true;
		//Globals.colorsHaveChangedGames = true;

		refresh();
	}


	

	
	private void refresh() {
		// TODO Auto-generated method stubif(refresh_){


		if(getRefresh_() || Globals.refreshGames > 3 || Globals.colorsHaveChangedGames){
			Globals.colorsHaveChangedGames = false;

			LinearLayout ll = (LinearLayout) findViewById(R.id.gameTitleLayout);
			ll.setBackgroundColor(Globals.backgroundColor);
			TextView temp = (TextView) findViewById(R.id.gameTitle);
			temp.setTextColor(Globals.textColor);
			temp.setBackgroundColor(Globals.backgroundColor);
			temp = (TextView) findViewById(R.id.textView2);
			temp.setBackgroundColor(Globals.textColor);
			
			ListView lv = (ListView) findViewById(R.id.appslist);
			lv.setBackgroundColor(Globals.backgroundColor);


			pb.setVisibility(View.VISIBLE);
			setRefresh_(false);
			Globals.refreshGames = 0;
			HttpUtils.get().doGet("http://www.levelupredevs.appspot.com/packages", new HttpCallback() {

				@Override
				public void onResponse(HttpResponse resp) {
					// TODO Auto-generated method stub
					//try {
					Log.i("Entering onResponse", "Its True");

					PrintXml printer = new PrintXml();

					printer.execute(resp);
				}

				@Override
				public void onError(Exception e) {
					// TODO Auto-generated method stub

				}
			});
		}


	}




	public static void doDelete() {
		HttpClient client = new DefaultHttpClient();
		HttpDelete delete = new HttpDelete("http://www.levelupredevs.appspot.com/packages");

		try {
			@SuppressWarnings("unused")
			HttpResponse resp = client.execute(delete);
		} catch(Exception e) {
			e.printStackTrace();
		}


	}


	void removeFromPackageList(String PackageToRemove)
	{

		ArrayList<String> temp = getPackagesFromFile();
		if(!temp.contains(PackageToRemove)) return;

		String path =  Environment.getExternalStorageDirectory() + "/data/levelup/";


		//view2_.setText("");
		File root = new File(path);
		//view2_.setText("I AM HERE ");


		root.mkdirs();

		try {
			File f = new File(root, "custom.txt");
			if(!f.exists())
			{
				//view2_.setText("now I am here");
				FileOutputStream fos;


				fos = new FileOutputStream(f);


				fos.close();



			}

			String packages = "";
			for(String pkg: temp)
			{
				if(!pkg.equalsIgnoreCase(PackageToRemove)){
					packages = packages + pkg + "\n";
				}
			}


			FileWriter writer;

			writer = new FileWriter(f);


			writer.write(packages);

			writer.close();
			setRefresh_(true);
			refresh();



		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	public void setRefresh_(boolean refresh_) {
		this.refresh_ = refresh_;
	}
	public boolean getRefresh_() {
		return refresh_;
	}


	public void setUpTwitter()
	{
		mPrefs = getSharedPreferences("twitterPrefs", MODE_PRIVATE);
		Log.i(TAG, "Got Preferences");

		// Load the twitter4j helper
		mTwitter = new TwitterFactory().getInstance();
		Log.i(TAG, "Got Twitter4j");

		// Tell twitter4j that we want to use it with our app
		mTwitter.setOAuthConsumer(CONSUMER_KEY, CONSUMER_SECRET);
		Log.i(TAG, "Inflated Twitter4j");
		if (mPrefs.contains(PREF_ACCESS_TOKEN)) {
			Log.i(TAG, "Repeat User");
			loginAuthorisedUser();
		}


	}
	public void tweetMessage(String message)
	{
		mPrefs = getSharedPreferences("twitterPrefs", MODE_PRIVATE);
		try {
			if (mPrefs.contains(PREF_ACCESS_TOKEN)) {
				mTwitter.updateStatus(message);
				Log.i("TweetMessage",message);
				//Toast.makeText(this, "Tweet Successful!", Toast.LENGTH_SHORT).show();
				Log.i("TwitterTweeter", "Success");
			}
			else{
				//makeToast("Not signed in to Twitter");
				Log.i("TwitterTweeter", "Not signed in to twitter");

			}
		} catch (TwitterException e) {
			//Toast.makeText(this, "Tweet error, try again later", Toast.LENGTH_SHORT).show();
			Log.i("TwitterTweeter", "FAIL: " + e.getErrorMessage());  

		}
	}
	private void loginAuthorisedUser() {
		String token = mPrefs.getString(PREF_ACCESS_TOKEN, null);
		String secret = mPrefs.getString(PREF_ACCESS_TOKEN_SECRET, null);

		// Create the twitter access token from the credentials we got previously
		AccessToken at = new AccessToken(token, secret);

		mTwitter.setOAuthAccessToken(at);

		//Toast.makeText(this, "Welcome back", Toast.LENGTH_SHORT).show();


	}

	public static String getCallbackUrl() {
		return CALLBACK_URL;
	}

	class TwitterTweeter extends AsyncTask<App, Void, Void>
	{


		@Override
		protected Void doInBackground(App... params) {
			// TODO Auto-generated method stub
			if(mPrefs.getBoolean("shareFromTwitter", false)){
				setUpTwitter();
				tweetMessage("I just played " + params[0].getTitle() + " via @LevelUp_Android .  You should " +
						"check it out!" + "  http://market.android.com/details?id=" + params[0].getPackageName());
			}
			return null;
		}

	}


	private void sendPackage(final String thePackage, String packageUrlType)
	{
		Map<String, String> args_ = new HashMap<String, String>();
		args_.put("app", thePackage);
		args_.put("uid", Globals.uid);

		HttpUtils.get().doPost("http://www.levelupredevs.appspot.com/" + packageUrlType, args_, new HttpCallback() {

			@Override
			public void onResponse(HttpResponse resp) {
				// TODO Auto-generated method stub
				try {
					Log.i("GamesActivity", "Succesful post of " + thePackage + " " + HttpUtils.get().responseToString(resp));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

			@Override
			public void onError(Exception e) {
				// TODO Auto-generated method stub

			}

		});
	}

}

