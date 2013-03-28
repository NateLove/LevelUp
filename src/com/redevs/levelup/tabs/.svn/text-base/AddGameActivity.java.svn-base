package com.redevs.levelup.tabs;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;

import com.redevs.levelup.R;
import com.redevs.levelup.async.HttpCallback;
import com.redevs.levelup.async.HttpUtils;
import com.redevs.levelup.global.Globals;
import com.redevs.levelup.qsort.MyQsort;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;



public class AddGameActivity extends Activity implements OnItemClickListener {

	/* whether or not to include system apps */
	private static final boolean INCLUDE_SYSTEM_APPS = false;

	private ListView mAppsList;
	private AppListAdapter mAdapter;
	private List<App> mApps;
	private ProgressBar pb;
	private ProgressDialog pd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list);

		TextView temp = (TextView) findViewById(R.id.gameTitle);
		temp.setTextColor(Globals.textColor);
		temp.setBackgroundColor(Globals.backgroundColor);
		temp = (TextView) findViewById(R.id.textView2);
		temp.setBackgroundColor(Globals.textColor);
		LinearLayout ll = (LinearLayout) findViewById(R.id.gameTitleLayout);
		ll.setBackgroundColor(Globals.backgroundColor);
		ListView lv = (ListView) findViewById(R.id.appslist);
		lv.setBackgroundColor(Globals.backgroundColor);

		pd = ProgressDialog.show(this, "", 
				"Loading Apps. Please wait...", true);
		pb = (ProgressBar) findViewById(R.id.progressBar1);


		mAppsList = (ListView) findViewById(R.id.appslist);
		mAppsList.setOnItemClickListener(this);

		mApps = loadInstalledApps(INCLUDE_SYSTEM_APPS);

		mAdapter = new AppListAdapter(getApplicationContext());
		mAdapter.setListItems(mApps);
		mAppsList.setAdapter(mAdapter);

		new LoadIconsTask().execute(mApps.toArray(new App[]{}));
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

		final App app = (App) parent.getItemAtPosition(position);

		AlertDialog.Builder builder = new AlertDialog.Builder(this);

		@SuppressWarnings("unused")
		String pack = app.getPackageName();
		//		Toast.makeText(getApplicationContext(), pack,
		//		Toast.LENGTH_SHORT).show();
		//Map<String, String> args_ = new HashMap<String, String>();
		//args_.put("app", pack);
		//
		//
		//
		//HttpUtils.get().doPost("http://www.levelupredevs.appspot.com/packages", args_, new HttpCallback() {
		//
		//	@Override
		//	public void onResponse(HttpResponse resp) {
		//		// TODO Auto-generated method stub
		//
		//	}
		//
		//	@Override
		//	public void onError(Exception e) {
		//		// TODO Auto-generated method stub
		//		Log.e("DFASDFASDFASDFASDFASDF", "LIES" + e.getMessage());
		//
		//	}
		//});
		//
		//
		//Log.e("HTTP DUMBNESS", pack);



		String msg = app.getTitle() + "\n\n" + 
		"Version " + app.getVersionName() + " (" +
		app.getVersionCode() + ")" +
		(app.getDescription() != null ? ("\n\n" + app.getDescription()) : "");


		builder.setMessage(msg)
		.setCancelable(true)
		.setTitle(app.getTitle())
		.setIcon(mAdapter.getIcons().get(app.getPackageName()))
		.setPositiveButton("Add", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				addToPackageList(app.getPackageName());
				Log.i("add package", "package " + app.getPackageName() + " was added to file" );
				String pack = app.getPackageName();
				Map<String, String> args_ = new HashMap<String, String>();
				args_.put("app", pack);



				HttpUtils.get().doPost("http://www.levelupredevs.appspot.com/potentialpackages", args_, new HttpCallback() {

					@Override
					public void onResponse(HttpResponse resp) {
						// TODO Auto-generated method stub
						Log.i("HttpPost", "Success");

					}

					@Override
					public void onError(Exception e) {
						// TODO Auto-generated method stub
						Log.e("HttpPost", "LIES " + e.getMessage());

					}

					
				});


				Log.e("HTTP DUMBNESS", pack);
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

	void addToPackageList(String PackageToAdd)
	{


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
			ArrayList<String> temp = getPackagesFromFile();
			String packages = "";
			for(String pkg: temp)
			{
				packages = packages + pkg + "\n";
			}


			FileWriter writer;

			writer = new FileWriter(f);


			writer.write(packages + PackageToAdd);

			writer.close();



		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * Uses the package manager to query for all currently installed apps which are put into beans and returned
	 * in form of a list.
	 * 
	 * @param includeSysApps whether or not to include system applications
	 * @return a list containing an {@code App} bean for each installed application 
	 */
	private List<App> loadInstalledApps(boolean includeSysApps) {
		List<App> apps = new ArrayList<App>();

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
			App app = new App();
			app.setTitle(p.applicationInfo.loadLabel(packageManager).toString());
			app.setPackageName(p.packageName);
			app.setVersionName(p.versionName);
			app.setVersionCode(p.versionCode);
			CharSequence description = p.applicationInfo.loadDescription(packageManager);
			app.setDescription(description != null ? description.toString() : "");
			apps.add(app);
		}
		pb.setVisibility(View.INVISIBLE);
		//	pd.dismiss();
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
			pd.dismiss();
		}
	}

}