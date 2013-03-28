package com.redevs.levelup.settings;

import com.redevs.levelup.LoginActivity;
import com.redevs.levelup.R;
import com.redevs.levelup.tabs.TabsActivity;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.TabActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

@SuppressWarnings("unused")
public class SettingsActivity extends Activity {

	private TextView mManageAccounts;
	private TextView mFacebook;
	private TextView mTwitter;
	private TextView mColor;
	private TextView mDisclaimer;
	/** Name to store the users access token */
	private static final String PREF_ACCESS_TOKEN = "accessToken";
	public static final String PREFS_NAME = "MyPrefsFile";
	private SharedPreferences mPrefs;
	private CheckBox mCheckBoxFacebook;
	private CheckBox mCheckBoxTwitter;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings);
		mManageAccounts = (TextView) findViewById(R.id.accounts);
		mDisclaimer = (TextView) findViewById(R.id.disclaimer);
		mFacebook = (TextView) findViewById(R.id.autoFacebook);
		mTwitter = (TextView) findViewById(R.id.autoTwitter);
		mColor = (TextView) findViewById(R.id.color);
		mPrefs = getSharedPreferences(PREFS_NAME, 0);
		mCheckBoxFacebook = (CheckBox) findViewById(R.id.checkBox1);
		Log.i("TEST", "shareFromFacebook is " + mPrefs.getBoolean("shareFromFacebook", false));
		if(mPrefs.getBoolean("shareFromFacebook", false))
		{
			mCheckBoxFacebook.setChecked(true);
		}
		mCheckBoxFacebook.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(mCheckBoxFacebook.isChecked())
				{
					mPrefs = getSharedPreferences(PREFS_NAME, 0);
					Log.i("TEST", "true and it says" + mPrefs.getBoolean("shareFromFacebook", false));
					SharedPreferences.Editor editor = mPrefs.edit();
					editor.putBoolean("shareFromFacebook", true);
					editor.commit();
					Log.i("TEST", "true and it says" + mPrefs.getBoolean("shareFromFacebook", false));
				}
				if(!mCheckBoxFacebook.isChecked()){

					mPrefs = getSharedPreferences(PREFS_NAME, 0);
					Log.i("TEST", "false and it says" + mPrefs.getBoolean("shareFromFacebook", false));

					//	Log.i("TEST", "false");
					SharedPreferences.Editor editor = mPrefs.edit();
					editor.putBoolean("shareFromFacebook", false);
					editor.commit();
					Log.i("TEST", "false and it says" + mPrefs.getBoolean("shareFromFacebook", false));

				}

			}
		});


		mDisclaimer.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AlertDialog.Builder alert = new AlertDialog.Builder(SettingsActivity.this);
				alert.setTitle("Disclaimer");
				alert.setMessage("Level Up! and the REDEVS team does not claim ownership" + 
										" of any content accessed through the use of the Level Up! app.  All" + 
										" material is copyright of the app's respective owners.");
				alert.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
					}
				});
				alert.show();
			}
			
		});


		mCheckBoxTwitter = (CheckBox) findViewById(R.id.twitterBox);
		mPrefs = getSharedPreferences("twitterPrefs", MODE_PRIVATE);
		//mCheckBoxTwitter.setClickable(false);
		Log.i("SettingsActivity", "twitterSetup: " + mPrefs.getBoolean("twitterSetup", false));
		if(!mPrefs.getBoolean("twitterSetup", false)){
			mCheckBoxTwitter.setEnabled(false);
			Log.i("SettingsActivity", "just set clickable to false");

		}
		mPrefs = getSharedPreferences(PREFS_NAME, 0);
		if(mPrefs.getBoolean("shareFromTwitter", false))
		{
			mCheckBoxTwitter.setChecked(true);
		}
		mCheckBoxTwitter.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if(mCheckBoxTwitter.isChecked())
				{
					mPrefs = getSharedPreferences(PREFS_NAME, 0);
					SharedPreferences.Editor editor = mPrefs.edit();
					editor.putBoolean("shareFromTwitter", true);
					editor.commit();
				}
				else{
					mPrefs = getSharedPreferences(PREFS_NAME, 0);
					SharedPreferences.Editor editor = mPrefs.edit();
					editor.putBoolean("shareFromTwitter", false);
					editor.commit();
				}

			}
		});

		mManageAccounts.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				twitterGO();

			}
		});

		mFacebook.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(mCheckBoxFacebook.isChecked())
				{
					mCheckBoxFacebook.setChecked(false);
				}
				else{
					mCheckBoxFacebook.setChecked(true);
				}
				
				
				if(mCheckBoxFacebook.isChecked())
				{
					mPrefs = getSharedPreferences(PREFS_NAME, 0);
					Log.i("TEST", "true and it says" + mPrefs.getBoolean("shareFromFacebook", false));
					SharedPreferences.Editor editor = mPrefs.edit();
					editor.putBoolean("shareFromFacebook", true);
					editor.commit();
					Log.i("TEST", "true and it says" + mPrefs.getBoolean("shareFromFacebook", false));
				}
				if(!mCheckBoxFacebook.isChecked()){
					
					mPrefs = getSharedPreferences(PREFS_NAME, 0);
					Log.i("TEST", "false and it says" + mPrefs.getBoolean("shareFromFacebook", false));

				//	Log.i("TEST", "false");
					SharedPreferences.Editor editor = mPrefs.edit();
					editor.putBoolean("shareFromFacebook", false);
					editor.commit();
					Log.i("TEST", "false and it says" + mPrefs.getBoolean("shareFromFacebook", false));

				}

			}
		});
		mTwitter.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mPrefs = getSharedPreferences("twitterPrefs", MODE_PRIVATE);
				if(mPrefs.getBoolean("twitterSetup", false)){
					if(mCheckBoxTwitter.isChecked())
					{
						mCheckBoxTwitter.setChecked(false);
					}
					else{
						mCheckBoxTwitter.setChecked(true);
					}
					
					Log.i("TEST", "Twitter checked is " + mCheckBoxTwitter.isChecked());
					

					if(mCheckBoxTwitter.isChecked())
					{
						mPrefs = getSharedPreferences(PREFS_NAME, 0);
						SharedPreferences.Editor editor = mPrefs.edit();
						editor.putBoolean("shareFromTwitter", true);
						editor.commit();
					}
					else{
						mPrefs = getSharedPreferences(PREFS_NAME, 0);
						SharedPreferences.Editor editor = mPrefs.edit();
						editor.putBoolean("shareFromTwitter", false);
						editor.commit();
					}

				}
			}
		});

		mColor.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(getApplicationContext(), ColorsActivity.class);
				startActivity(i);
			}
		});

	}



	//	@Override
	//	public void onBackPressed() {
	//		super.finish();
	//	   return;
	//	}

	private void twitterGO() {
		// TODO Auto-generated method stub
		Intent i = new Intent(getApplicationContext(), TweetToTwitterActivity.class);
		startActivity(i);

	}

	@Override
	public void onResume()
	{
		super.onResume();
		mPrefs = getSharedPreferences("twitterPrefs", MODE_PRIVATE);
		Log.i("SettingsActivity", "twitterSetup is " + mPrefs.getBoolean("twitterSetup", false));
		if(mPrefs.getBoolean("twitterSetup", false)){
			mCheckBoxTwitter.setEnabled(true);
			Log.i("SettingsActivity", "just set clickable to true");

		}

	}

}

