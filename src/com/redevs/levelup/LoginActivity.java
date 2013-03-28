//Login Activity.  This Activity is mostly done.

package com.redevs.levelup;

import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.FacebookError;
import com.facebook.android.Facebook.DialogListener;
import com.redevs.levelup.global.Globals;
import com.redevs.levelup.tabs.TabsActivity;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

public class LoginActivity extends Activity {

	public static final String PREFS_NAME = "MyPrefsFile";
	private Integer displayDisclaimerPreference;
	
    Facebook facebook = new Facebook("163863397043367");
    
    
    private TextView easterEgg;
    
    String FILENAME = "AndroidSSO_data";
    private SharedPreferences mPrefs;
	boolean dialogisOpen;
	private Button facebookAuthButton_;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mPrefs = getSharedPreferences(PREFS_NAME, 0);
		Globals.textColor = mPrefs.getInt("textColor", Globals.defaultTextColor);
		Globals.backgroundColor = mPrefs.getInt("backgroundColor", Globals.defaultBackgroundColor);
		
		Globals.hasStarted = true;
		setContentView(R.layout.login);
		dialogisOpen = false;
		
		easterEgg = (TextView) findViewById(R.id.easterEgg);
		easterEgg.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				AlertDialog.Builder alert = new AlertDialog.Builder(LoginActivity.this);
				alert.setMessage("oh god how did this get here i am not good with computer.");
				alert.setPositiveButton("Okay?", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
					}
				});
				alert.show();
				
			}
			
		});
		
		facebookAuthButton_ = (Button) findViewById(R.id.facebookAuthButton);
		facebookAuthButton_.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
			        if(!facebook.isSessionValid()) {
			            facebook.setAccessToken(null);
			            facebook.setAccessExpires(0);
			            facebook.authorize(LoginActivity.this, new String[] {"publish_stream", "publish_actions", "offline_access"}, new DialogListener() {
			                @Override
			                public void onComplete(Bundle values) {
			                    SharedPreferences.Editor editor = mPrefs.edit();
			                    editor.putString("access_token", facebook.getAccessToken());
			                    editor.putLong("access_expires", facebook.getAccessExpires());
			                    editor.commit();
			                    SessionStore.save(facebook, getBaseContext());
			                    Intent i = new Intent(LoginActivity.this, TabsActivity.class);
			                    startActivity(i);
			                }
			    
			                @Override
			                public void onFacebookError(FacebookError error) {
			                	Log.e("fberror", error.getMessage());
			                }
			    
			                @Override
			                public void onError(DialogError e) {
			                	Log.e("DialogError", e.getMessage());
			                }
			    
			                @Override
			                public void onCancel() {
			                	Log.e("Cancel", "Authorization Cancelled");
			                }
			            });
			        }
			        
			        else {
			        	SessionStore.save(facebook, getBaseContext());
						Intent i = new Intent(LoginActivity.this, TabsActivity.class);
						startActivity(i);
			        }
				
			}
			
		});
		
        mPrefs = getPreferences(MODE_PRIVATE);
        String access_token = mPrefs.getString("access_token", null);
        long expires = mPrefs.getLong("access_expires", 0);
        if(access_token != null) {
            facebook.setAccessToken(access_token);
        }
        if(expires != 0) {
            facebook.setAccessExpires(expires);
        }
        
		//The following Code is for the Shared Preferences and the disclaimer dialog.
		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
		displayDisclaimerPreference = settings.getInt("displayDisclaimerPreference", 0); //0 display, 1 do not
		Log.e("Validation 1", "displayDisclaimerPreference = " + displayDisclaimerPreference);
		final Dialog dialog = new Dialog(this);
		
		dialog.setContentView(R.layout.alert);
		dialog.setTitle("Disclaimer - PLEASE READ");
		
		TextView alertText = (TextView) dialog.findViewById(R.id.alerttext);
		alertText.setText("Level Up! and the REDEVS team does not claim ownership" +
				" of any content accessed through the use of the Level Up! app.  All" +
				" material is copyright of the app's respective owners.");
		final CheckBox dontShowAgain = (CheckBox) dialog.findViewById(R.id.dontShowAgain);
		Button alertButton = (Button) dialog.findViewById(R.id.alertButton);
		
		alertButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				if(dontShowAgain.isChecked()){
					SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
					SharedPreferences.Editor editor = settings.edit();
					editor.putInt("displayDisclaimerPreference", 1);
					editor.commit();
				}
				dialog.dismiss();
			}
			
		});
		
		if(displayDisclaimerPreference == 0){
			dialog.show();
		}
        
        /*
         * Only call authorize if the access_token has expired.
         */
		
        if(facebook.isSessionValid() && displayDisclaimerPreference == 1) {
        	Intent i = new Intent(LoginActivity.this, TabsActivity.class);
        	startActivity(i);
        }
      
       
	

		
	}
	
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        facebook.authorizeCallback(requestCode, resultCode, data);
    }
}
