/*
 * To add:
 * nathan is infinite gay.
 * Retrieve list of last played games / User IDs
 * Add "- <GAME>" to applicable user ids that have played something.
 */
package com.redevs.levelup.tabs;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.HttpResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.Facebook;
import com.facebook.android.FacebookError;
import com.facebook.android.Util;
import com.redevs.levelup.BaseRequestListener;
import com.redevs.levelup.R;
import com.redevs.levelup.SessionStore;
import com.redevs.levelup.async.HttpCallback;
import com.redevs.levelup.async.HttpUtils;
import com.redevs.levelup.global.Friend;
import com.redevs.levelup.global.Globals;
import com.redevs.levelup.qsort.MyQsort;
import com.redevs.levelup.settings.SettingsActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

public class FriendsActivity extends Activity implements HttpCallback, OnItemClickListener {

	public static final String PREFS_NAME = "MyPrefsFile";
	
    private ArrayList<Friend> friends = new ArrayList<Friend>();
    private ArrayList<Friend> appFriends = new ArrayList<Friend>();
	private TextView friendsView, addFriendText, topSep, botSep;
	private ImageView profilePhoto, addFriendPhoto;
	AsyncFacebookRunner mAsyncRunner;
	private String profileId;
	private Facebook facebook;
	private int addFriendSelected;
	private int alreadySentId;
	private ProgressDialog progressDialog;
	private HashMap<String, String> putMap, ParsedXML;
	private RelativeLayout friendsRelativeLayout_;
	private LinearLayout friendsLayout_, addFriendLayout_;
	private SharedPreferences mPrefs;
	
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.friends);
		
		SharedPreferences friendSettings = getSharedPreferences(PREFS_NAME, 0);
		alreadySentId = friendSettings.getInt("LastFriendIdPreference", -1);
		
		Log.e("id", alreadySentId + " :: AlreadySentId");
		
		profileId = "";
		
		friendsRelativeLayout_ = (RelativeLayout) findViewById(R.id.FriendsRelativeLayout);
		
		friendsLayout_ = (LinearLayout) findViewById(R.id.friendsLayout);
		
		friendsView = (TextView) findViewById(R.id.friendText);
		addFriendText = (TextView) findViewById(R.id.Test);
		topSep = (TextView) findViewById(R.id.profileSeperator);
		botSep = (TextView) findViewById(R.id.addFriendSeperator);
		
		addFriendPhoto = (ImageView) findViewById(R.id.addFriendPicture);
	    addFriendPhoto.setAdjustViewBounds(true);
	    addFriendPhoto.setMaxHeight(50);
	    addFriendPhoto.setMaxWidth(50);
		
	    profilePhoto = (ImageView) findViewById(R.id.profilePicture);
	    profilePhoto.setAdjustViewBounds(true);
	    profilePhoto.setMaxHeight(50);
	    profilePhoto.setMaxWidth(50);    
	    
	    Intent intent = getIntent();
	    addFriendSelected = intent.getIntExtra("selected", -1);
	    
	    //COLOR SHIT GOES HERE.
	    //Background
	    friendsRelativeLayout_.setBackgroundColor(Globals.backgroundColor);
	    friendsView.setTextColor(Globals.textColor);
	    addFriendText.setTextColor(Globals.textColor);
	    topSep.setBackgroundColor(Globals.textColor);
	    botSep.setBackgroundColor(Globals.textColor);
	    //END COLOR SHIT.
	    
	    addFriendLayout_ = (LinearLayout) findViewById(R.id.addFriendLayout);
	    addFriendLayout_.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {

				ArrayList<String> friendsStringList = new ArrayList<String>();
            	

				for(int i=0; i<friends.size(); i++){
					//Log.e("Friends:", Integer.toString(friends.size()) + " " + friends.get(i).name);
					String s = friends.get(i).name;
					friendsStringList.add(s);
				}
			
                Intent i = new Intent(FriendsActivity.this, AddFriendActivity.class);
                i.putStringArrayListExtra("asdf", friendsStringList);
                startActivity(i);
				
			}
	    	
	    });
	    
	    facebook = new Facebook("163863397043367");
	    
	    SessionStore.restore(facebook, this);
	    
	    if(facebook.isSessionValid()) {
	    	friendsView.setText("");
	    }
	    else {
	    	friendsView.setText("Session is not valid.");
	    }
	    

	    progressDialog = new ProgressDialog(this);
	    progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
	    progressDialog.setMessage("Retrieving Profile Information...");
	    progressDialog.setCancelable(false);
	    progressDialog.show();
	    
	    
	    mAsyncRunner = new AsyncFacebookRunner(facebook);
	    mAsyncRunner.request("me", new UserRequestListener());
	    mAsyncRunner.request("me/friends", new FriendRequestListener());
	}
	
	@Override
	public void onResume()
	{
		super.onResume();
		mPrefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
		Globals.textColor = mPrefs.getInt("textColor", Globals.defaultTextColor);
		Globals.backgroundColor = mPrefs.getInt("backgroundColor", Globals.defaultBackgroundColor);
		Globals.hasStarted = true;
		Log.i("Friends", "Entered onResume");
	    friendsRelativeLayout_.setBackgroundColor(Globals.backgroundColor);
	    friendsView.setTextColor(Globals.textColor);
	    addFriendText.setTextColor(Globals.textColor);
	    topSep.setBackgroundColor(Globals.textColor);
	    botSep.setBackgroundColor(Globals.textColor);
	    if(Globals.colorsHaveChangedFriends == true) {
	    	friendsLayout_.removeAllViews();
	    	parseAppFriends();
	    }
	    Globals.colorsHaveChangedFriends = false;
	}
	
	
	
    public class UserRequestListener extends BaseRequestListener {

        public void onComplete(final String response, final Object state) {
            try {
                // process the response here: executed in background thread
                Log.d("User Request", "Response: " + response.toString());
                JSONObject json = Util.parseJson(response);
                final String name = json.getString("name");
                profileId = json.getString("id");
                Globals.uid = profileId;
          
        	    URL img_value = null;
        	    img_value = new URL("http://graph.facebook.com/"+profileId+"/picture?type=square");
        	    final Bitmap mIcon1 = BitmapFactory.decodeStream(img_value.openConnection().getInputStream());
        	   
        	    
                FriendsActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        friendsView.setText(name);
                        profilePhoto.setImageBitmap(mIcon1);
                        
                	    progressDialog.setProgress(33);
                	    progressDialog.setMessage("Retrieving Friend Information...");
                    	
                        
                	    doFQLMultiquery();
                    }
                });
                

                
            } catch (JSONException e) {
                Log.w("Facebook-Example", "JSON Error in response");
            } catch (FacebookError e) {
                Log.w("Facebook-Example", "Facebook Error: " + e.getMessage());
            } catch (MalformedURLException e) {
				Log.w("Facebook-Example", "URL Error: " + e.getMessage());
			} catch (IOException e) {
				Log.w("Facebook-Example", "IO Error: " + e.getMessage());
			}
        }


    }
    
    public class FriendRequestListener extends BaseRequestListener {

    	@Override
    	public void onComplete(String response, Object state) {
    		try {
    			friends.clear();
    			// process the response here: executed in background thread
    			Log.d("Facebook-Example-Friends Request", "response.length(): " + 
                                                            response.length());
    			Log.d("Facebook-Example-Friends Request", "Response: " + response);

    			final JSONObject json = new JSONObject(response);
    			JSONArray d = json.getJSONArray("data");
    			int l = (d != null ? d.length() : 0);
    			Log.d("Facebook-Example-Friends Request", "d.length(): " + l);

    			for (int i=0; i<l; i++) {
    				JSONObject o = d.getJSONObject(i);
    				String n = o.getString("name");
        		    String id = o.getString("id");
        		    Friend f = new Friend();
        		    f.id = id;
        		    f.name = n;
        		    //Log.d("hey", n);
        		    friends.add(f);
    			}
            	friends = MyQsort.sortArrayList(friends);

    			
    			sendFriendRequest();
    			
    		} catch (JSONException e) {
    			Log.w("Facebook-Example", "JSON Error in response");
    			progressDialog.dismiss();
    		}
    	}
    }
    
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
				FriendsActivity.this.runOnUiThread(new Runnable() {
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

    @Override
	public void onResponse(HttpResponse resp) {
    	
    	progressDialog.setProgress(66);
    	progressDialog.setMessage("Retrieving Icons and Games...");
		
		friendsLayout_.removeAllViews();
		try {
			appFriends.clear();
			String response = HttpUtils.get().responseToString(resp);
	        JSONArray json;
			json = new JSONArray(response);
			JSONObject d = json.getJSONObject(1);
			JSONArray json2 = d.getJSONArray("fql_result_set");
				
			int l = (json2 != null ? json2.length() : 0);
			Log.d("Facebook-Example-Friends Request", "d.length(): " + l);

			for (int i=0; i<l; i++) {
				JSONObject o = json2.getJSONObject(i);
				String n = o.getString("name");
	    		String id = o.getString("id");
	    		String pic = o.getString("pic"); 
	    		Log.e("hey", n);
    		    Friend f = new Friend();
    		    f.id = id;
    		    f.name = n;
    		    f.pictureURL = pic;
    		    Log.d("Friends", n);
    		    appFriends.add(f);	
			}
			
			appFriends = MyQsort.sortArrayList(appFriends);
			
			putMap = new HashMap<String, String>();
			
			for(int i=0; i<appFriends.size(); i++){
				putMap.put("uid"+Integer.toString(i+1), appFriends.get(i).id);
			}
			
			HttpUtils.get().doPut(Globals.uidPackagePairsUrl, putMap, new HttpCallback(){

				@Override
				public void onResponse(HttpResponse resp) {
					
					try {
						String response = HttpUtils.get().responseToString(resp);
						ParsedXML = XMLParser.parseUidPackagePairsXML(response);
						Log.e("asdfasdf", response);
						friendsLayout_.removeAllViews();
						parseAppFriends();
						progressDialog.dismiss();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						progressDialog.dismiss();
					}
					
				}

				@Override
				public void onError(Exception e) {
					//Log.e("Appengine error", e.printStackTrace());
					
				}
				
			});

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
    
	//creates options menu
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.friendsmenu, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.about:
			showAbout();
			return true;
		case R.id.settings:
			Intent i = new Intent(getApplicationContext(), SettingsActivity.class);
			startActivity(i);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
		
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

	@Override
	public void onError(Exception e) {
		// TODO Auto-generated method stub
		
	}
	
	public void showToast(String message) {
		Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		
	}
	
	public void parseAppFriends() {
		for(int i=0; i<appFriends.size(); i++) {
			LinearLayout appFriendLayout_ = new LinearLayout(getBaseContext());
    	    URL img_value = null;
    	    try {
				img_value = new URL("http://graph.facebook.com/"+appFriends.get(i).id+"/picture?type=square");
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	    Bitmap mIcon1 = null;
			try {
				mIcon1 = BitmapFactory.decodeStream(img_value.openConnection().getInputStream());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	    LinearLayout.LayoutParams hlp = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    	    hlp.setMargins(0, 0, 10, 0);
    	    ImageView photo = new ImageView(getBaseContext());
    	    photo.setImageBitmap(mIcon1);
    	    photo.setLayoutParams(hlp);
    	    appFriendLayout_.addView(photo);
			TextView t = new TextView(getBaseContext());
			if(!ParsedXML.get(appFriends.get(i).id).equals("")){
				t.setText(appFriends.get(i).name + " - " + ParsedXML.get(appFriends.get(i).id));
			}
			else {
				t.setText(appFriends.get(i).name);
			}
			t.setTextColor(Globals.textColor);
			t.setTextSize(18);
			LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			llp.setMargins(0, 0, 0, 0);
			t.setLayoutParams(llp);
			appFriendLayout_.addView(t);
			LinearLayout.LayoutParams plp = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			plp.setMargins(0, 10, 0, 10);
			appFriendLayout_.setLayoutParams(plp);
			friendsLayout_.addView(appFriendLayout_);
		}
	}
	
	public void sendFriendRequest() {
		if(alreadySentId != addFriendSelected){
	    	if(addFriendSelected >= 0) {
	    		while(friends.size() == 0) {}
	    		//Do post of link.
	    		//showToast(friends.get(addFriendSelected).name + "has been sent a request!");
			
	    		Bundle params = new Bundle();
            
				params.putString("caption","Level Up! is an android app that brings social networking to android gaming.");
				params.putString("link", "https://www.facebook.com/apps/application.php?id=163863397043367");
				params.putString("message", "Hey, check out Level Up!");
				params.putString("description", "This link was sent using the Level Up! for Android app.");
				params.putString("picture", "https://fbcdn-profile-a.akamaihd.net/hprofile-ak-ash2/372851_163863397043367_832581163_n.jpg");
			                
				// an AsyncRunner to handle the dispatching the post
				mAsyncRunner.request(friends.get(addFriendSelected).id + "/feed", params, "POST", new LinkUploadListener(), null);
				alreadySentId = addFriendSelected;
				
				SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
				SharedPreferences.Editor editor = settings.edit();
				editor.putInt("LastFriendIdPreference", alreadySentId);
				editor.commit();
			}
		}
	}
	
	public void doFQLMultiquery() {
		
		
	    String fql_multiquery = "%7B%22query1%22%3A%22SELECT+uid+FROM+user+WHERE+uid+IN+%28SELECT+uid2+FROM+friend+WHERE+uid1+%3D+" + profileId + "%29+AND+is_app_user+%3D+1%22%2C%22query2%22%3A%22SELECT+name%2C+id%2C+pic+FROM+profile+WHERE+id+IN+%28SELECT+uid+FROM+%23query1%29%22%7D&access_token=" + facebook.getAccessToken() + "&format=json";
	    String url_ = "https://api.facebook.com/method/fql.multiquery?queries="+ fql_multiquery;
		HttpUtils.get().doGet(url_, FriendsActivity.this);
	}
}