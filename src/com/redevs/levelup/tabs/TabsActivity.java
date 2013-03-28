package com.redevs.levelup.tabs;



import com.redevs.levelup.R;
import com.redevs.levelup.global.Globals;

import android.app.TabActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Display;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;


public class TabsActivity extends TabActivity {

    TabHost.TabSpec spec;
        
    @Override
        public void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.tab);
                
//                Intent i = getIntent();
//                int position = i.getIntExtra("selected", -1);
//                
//                TabHost tabHost = getTabHost(); 
//                Intent intent1 = new Intent().setClass(this, FriendsActivity.class);
//                Intent intent2 = new Intent().setClass(this, GamesActivity.class);
//
//                intent1.putExtra("selected", position);
//                
//                setupTab(new TextView(this), "Friends", intent1, tabHost);
//                setupTab(new TextView(this), "Games", intent2, tabHost);
//
//
//        }
//        
//        private static View createTabView(final Context context, final String text) {
//                View view = LayoutInflater.from(context).inflate(R.layout.tabs_bg, null);
//                TextView tv = (TextView) view.findViewById(R.id.tabsText);
//                tv.setText(text);
//                return view;
//        }
//        
//        private void setupTab(final View view, final String tag, Intent intent, TabHost tabHost) {
//                View tabview = createTabView(tabHost.getContext(), tag);
//                spec = tabHost.newTabSpec("hours").setIndicator(tabview).setContent(intent);
//                tabHost.addTab(spec);
//        }
                
                /***************************
                 * Start different code
                 */
                
                Display display = getWindowManager().getDefaultDisplay();
        		int width = display.getWidth();

        		//setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

                Intent i = getIntent();
                int position = i.getIntExtra("selected", -1);
        		
        		TabHost tabHost = getTabHost();  // The activity TabHost
        		TabHost.TabSpec spec;  // Resusable TabSpec for each tab
        		Intent intent;  // Reusable Intent for each tab

        		// Create an Intent to launch an Activity for the tab (to be reused)
        		intent = new Intent().setClass(this, FriendsActivity.class);

        		intent.putExtra("selected", position);
        		
        		// Initialize a TabSpec for each tab and add it to the TabHost
        		//	    TextView title = (TextView) tabHost.getTabWidget().getChildAt(1).findViewById(android.R.id.title);
        		//	    title.setSingleLine(false);
        		spec = tabHost.newTabSpec("Friends").setIndicator("Friends").setContent(intent);
        	


        		tabHost.addTab(spec);

        		intent = new Intent().setClass(this, GamesActivity.class);
        		spec = tabHost.newTabSpec("Games").setIndicator("Games").setContent(intent);
        		tabHost.addTab(spec);
        		
        		intent = new Intent().setClass(this, PromotedGamesActivity.class);
        		spec = tabHost.newTabSpec("Suggested").setIndicator("Suggested").setContent(intent);
        		tabHost.addTab(spec);

        		
        	
        		TabHost tabhost = getTabHost();
        	    for(int j=0 ; j < tabhost.getTabWidget().getChildCount() ; j++) 
        	    {
        	        TextView tv = (TextView) tabhost.getTabWidget().getChildAt(j).findViewById(android.R.id.title);
        	        tv.setTextColor(Globals.textColor);
        	    } 

        		tabHost.getTabWidget().getChildAt(0).setLayoutParams(new
        				LinearLayout.LayoutParams((width/3)-2,75));
        		tabHost.getTabWidget().getChildAt(1).setLayoutParams(new
        				LinearLayout.LayoutParams((width/3)-2,75));
        		tabHost.getTabWidget().getChildAt(2).setLayoutParams(new
        				LinearLayout.LayoutParams((width/3)-2,75));
        		

        		//	    
        		//	    intent = new Intent().setClass(this, BluetoothChat.class);
        		//	    spec = tabHost.newTabSpec("Bus Chat").setIndicator("Chat").setContent(intent);
        		//	    tabHost.addTab(spec);

        		//tabHost.setCurrentTab(1);

        		tabHost.setCurrentTab(0);

                
                
                
    }
    
    @Override
    public void onResume()
    {
    	
    	super.onResume();
    	final String PREFS_NAME = "MyPrefsFile";
    	SharedPreferences mPrefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
    	Globals.textColor = mPrefs.getInt("textColor", Globals.defaultTextColor);
		Globals.backgroundColor = mPrefs.getInt("backgroundColor", Globals.defaultBackgroundColor);
		Globals.hasStarted = true;
    	TabHost tabhost = getTabHost();
	    for(int j=0 ; j < tabhost.getTabWidget().getChildCount() ; j++) 
	    {
	        TextView tv = (TextView) tabhost.getTabWidget().getChildAt(j).findViewById(android.R.id.title);
	        tv.setTextColor(Globals.textColor);
	    } 
    }
}