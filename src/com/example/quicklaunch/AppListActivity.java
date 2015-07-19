package com.example.quicklaunch;

import java.util.ArrayList;
import java.util.List;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

public class AppListActivity extends ListActivity {

	String gestureName;
	
	Intent intent;

	private PackageManager packageManager = null;
	private List<ApplicationInfo> applist = null;
	private AppListAdapter listadaptor = null;
	String PackageName = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.app_list_view);
		intent = new Intent(getBaseContext(), GestureListActivity.class);
				packageManager = getPackageManager();		
		gestureName = getIntent().getExtras().getString("GestureType");
		new LoadApplications().execute();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		startActivity(intent);
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}


	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);

		ApplicationInfo app = applist.get(position);
		try {
			
			SharedPreferences sharedPref = getApplicationContext().getSharedPreferences("GoQuick", 0);
			SharedPreferences.Editor editor = sharedPref.edit();
			
			editor.putString(gestureName, app.packageName);
			editor.commit();
			
			finish();
			
		} catch (ActivityNotFoundException e) {
			Toast.makeText(AppListActivity.this, e.getMessage(), Toast.LENGTH_LONG)
					.show();
		} catch (Exception e) {
			Toast.makeText(AppListActivity.this, e.getMessage(), Toast.LENGTH_LONG)
					.show();
		}
	}

	private List<ApplicationInfo> checkForLaunchIntent(
			List<ApplicationInfo> list) {
		ArrayList<ApplicationInfo> applist = new ArrayList<ApplicationInfo>();
		for (ApplicationInfo info : list) {
			try {
				if (null != packageManager
						.getLaunchIntentForPackage(info.packageName)) {
					applist.add(info);

				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return applist;
	}

	private class LoadApplications extends AsyncTask<Void, Void, Void> {
		private ProgressDialog progress = null;

		@Override
		protected Void doInBackground(Void... params) {
			applist = checkForLaunchIntent(packageManager
					.getInstalledApplications(PackageManager.GET_META_DATA));
			listadaptor = new AppListAdapter(AppListActivity.this,
					R.layout.app_list_view, applist);

			return null;
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
		}

		@Override
		protected void onPostExecute(Void result) {
			setListAdapter(listadaptor);
			progress.dismiss();
			super.onPostExecute(result);
		}

		@Override
		protected void onPreExecute() {
			progress = ProgressDialog.show(AppListActivity.this, null,
					"Loading application info...");
			super.onPreExecute();
		}

		@Override
		protected void onProgressUpdate(Void... values) {
			super.onProgressUpdate(values);
		}
	}

}
