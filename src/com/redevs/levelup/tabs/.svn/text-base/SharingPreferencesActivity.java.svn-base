package com.redevs.levelup.tabs;

import com.redevs.levelup.R;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

public class SharingPreferencesActivity extends Activity {
	
	@SuppressWarnings("unused")
	private static final String PREF_ACCESS_TOKEN = "accessToken";
	public static final String PREFS_NAME = "MyPrefsFile";
	private SharedPreferences mPrefs;
	private CheckBox mCheckBoxFacebook;
	private CheckBox mCheckBoxTwitter;
	private Button mOK;
	private Button mCancel;
	private boolean mFacebookIsChecked = false;
	private boolean mTwitterIsChecked = false;
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sharing_preferences);
		
		mPrefs = getSharedPreferences(PREFS_NAME, 0);
		mCheckBoxFacebook = (CheckBox) findViewById(R.id.facebook);
		if(mPrefs.getBoolean("shareFromFacebook", false))
		{
			mFacebookIsChecked = true;
			mCheckBoxFacebook.setChecked(true);
		}
		mCheckBoxFacebook.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(mCheckBoxFacebook.isChecked())
				{
					
					mFacebookIsChecked = true;

				}
				else{
					mFacebookIsChecked = false;

//					SharedPreferences.Editor editor = mPrefs.edit();
//					editor.putBoolean("shareFromFacebook", false);
//					editor.commit();
				}

			}
		});





		mCheckBoxTwitter = (CheckBox) findViewById(R.id.twitter);
		mPrefs = getSharedPreferences("twitterPrefs", MODE_PRIVATE);
		if(!mPrefs.getBoolean("twitterSetup", false)){
			mCheckBoxTwitter.setEnabled(false);
		}
		mPrefs = getSharedPreferences(PREFS_NAME, 0);
		if(mPrefs.getBoolean("shareFromTwitter", false))
		{
			mTwitterIsChecked = true;
			mCheckBoxTwitter.setChecked(true);
		}
		mCheckBoxTwitter.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(mCheckBoxTwitter.isChecked())
				{
					mTwitterIsChecked = true;

				}
				else{
					
					mTwitterIsChecked = false;
//					SharedPreferences.Editor editor = mPrefs.edit();
//					editor.putBoolean("shareFromTwitter", false);
//					editor.commit();
				}

			}
		});
		
		mOK = (Button) findViewById(R.id.ok);
		mOK.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mPrefs = getSharedPreferences(PREFS_NAME, 0);
				SharedPreferences.Editor editor = mPrefs.edit();
				
				editor.putBoolean("shareFromFacebook", mFacebookIsChecked);
				editor.putBoolean("launchApp", true);
				editor.putBoolean("shareFromTwitter", mTwitterIsChecked);
				
				editor.commit();
				launchApp();
				finish();
				
			}

			
		});
		
		mCancel = (Button) findViewById(R.id.cancel);
		mCancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				SharedPreferences.Editor editor = mPrefs.edit();
				
				
				editor.putBoolean("launchApp", true);
				editor = mPrefs.edit();

				editor.putBoolean("showSharingPreference", true);
				editor.commit();
				finish();
				
			}
		});
	}
	private void launchApp() {
		// TODO Auto-generated method stub
		Intent i  = getIntent();
		String pack = i.getStringExtra("Package");
		i = getPackageManager().getLaunchIntentForPackage(pack);
		try {
			if (i != null) {
				startActivity(i);
			} else {
				i = new Intent(pack);
				startActivity(i);
			}
		} catch (ActivityNotFoundException err) {
			Toast.makeText(SharingPreferencesActivity.this, "Error launching app", Toast.LENGTH_SHORT).show();
		}
		
	}
	
}
