package com.redevs.levelup.settings;

import com.redevs.levelup.R;
import com.redevs.levelup.global.Globals;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

public class ColorsActivity extends Activity {

	private Spinner mText;
	private Spinner mBackground;
	private Button mSave;
	private Button mDefault;
	public static final String PREFS_NAME = "MyPrefsFile";
	private SharedPreferences mPrefs;
	private ArrayAdapter<String> adapter_ = null;



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.colors);

		mText = (Spinner) findViewById(R.id.textSpinner);
		mBackground = (Spinner) findViewById(R.id.backgroundSpinner);
		final String[] colors = {"Select a Color", "Red", "Blue", "Green", "Orange", "Purple", "Yellow", "Black", "White", "Grey", "Maroon"};
		final int[] colorCodes = {-1, 0xffff0000, 0xff0000ff,0xff008000, 0xffff9900, 0xff800080, 0xffffff00, 0xff000000, 0xffffffff, 0xff808080, 0xff800000};
		adapter_ = new ArrayAdapter<String>(ColorsActivity.this,android.R.layout.simple_spinner_item, colors);
		adapter_.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mText.setAdapter(adapter_);  
		mBackground.setAdapter(adapter_);


		mSave = (Button) findViewById(R.id.save);
		mDefault = (Button) findViewById(R.id.defaultButton);

		mSave.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				int Color = 0;
				String selectedColor = mText.getSelectedItem().toString();
				for(int i = 1; i < colors.length; i++)
				{
					if(selectedColor.equals(colors[i]))
					{
						Globals.colorsHaveChangedFriends = true;
						Globals.colorsHaveChangedGames = true;
						Color = colorCodes[i];
						Globals.textColor = colorCodes[i];
						mPrefs = getSharedPreferences(PREFS_NAME, 0);
						SharedPreferences.Editor editor = mPrefs.edit();
						editor.putInt("textColor", Color);
						
						editor.commit();
						Log.i("TEST", "text color is " + mPrefs.getInt("textColor", Globals.textColor));
					}
					
				}
				
				Color = 0;
				selectedColor = mBackground.getSelectedItem().toString();
				for(int i = 1; i < colors.length; i++)
				{
					if(selectedColor.equals(colors[i]))
					{
						Globals.colorsHaveChangedFriends = true;
						Globals.colorsHaveChangedGames = true;

						Color = colorCodes[i];
						Globals.backgroundColor = colorCodes[i];
						mPrefs = getSharedPreferences(PREFS_NAME, 0);
						SharedPreferences.Editor editor = mPrefs.edit();
						editor.putInt("backgroundColor", Color);
						
						editor.commit();
						Log.i("TEST", "background color is " + mPrefs.getInt("backgroundColor", Globals.backgroundColor));
					}
					
				}
				makeToast("Settings Saved");
				finish();
				
			}
			
		});


		mDefault.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Globals.colorsHaveChangedFriends = true;
				Globals.colorsHaveChangedGames = true;
				Globals.backgroundColor = Globals.defaultBackgroundColor;
				Globals.textColor = Globals.defaultTextColor;
				mPrefs = getSharedPreferences(PREFS_NAME, 0);
				SharedPreferences.Editor editor = mPrefs.edit();
				editor.putInt("textColor", Globals.defaultTextColor);
				editor.putInt("backgroundColor", Globals.defaultBackgroundColor);
				editor.commit();
				Log.i("TEST", "background color is " + mPrefs.getInt("backgroundColor", Globals.backgroundColor));
				Log.i("TEST", "text color is " + mPrefs.getInt("textColor", Globals.textColor));
				makeToast("Colors Returned to Default");
				finish();
			}
		});



	}

	public void makeToast(String message)
	{
		Toast.makeText(getApplicationContext(), message,
				Toast.LENGTH_SHORT).show();
	}



}
