package com.example.quicklaunch;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

public class FirstMainActivity extends Activity {

	public static String[] Item = { "Proximity", "Shake In X", "Shake In Y", "Shake In Z" };
	public static int[] Images = { R.drawable.soundicon, R.drawable.orientation,
		R.drawable.shakeicon, R.drawable.proximity };
	
	Intent intent, xaxis, yaxis, zaxis, proximity;

	Button reset;
	View v;
	ListView lv;
	Context context;

	public Intent serviceObjectForGestureName(String gesturename, Context context) {
		
		Intent intentobject = null;
		
		if(gesturename.equals("Proximity"))
		{
			intentobject = proximity;
		}
		else if(gesturename.equals("Shake In X"))
		{
			intentobject = xaxis;
		}
		else if(gesturename.equals("Shake In Y"))
		{
			intentobject = yaxis;
		}
		else if(gesturename.equals("Shake In Z"))
		{
			intentobject = zaxis;
		}

		return intentobject;
		
	}
	
	public void stopGestureService(String gesturename, Context context) {
		
		Intent stopintent = serviceObjectForGestureName(gesturename, context);
		context.stopService(stopintent);
	}

	public void startGestureService(String gesturename,String packageName, Context context) {
		
		Intent startIntent = serviceObjectForGestureName(gesturename, context);
		startIntent.putExtra("Package", packageName);
		context.startService(startIntent);
	}
	
	public void startAllGestureServices() {
		
		for (int i = 0; i < Item.length; i++) {
			SharedPreferences sharedPref = getApplicationContext().getSharedPreferences("GoQuick", 0);
			String sharedPreferencePackageName = sharedPref.getString(Item[i],"Default");
			
            if(sharedPreferencePackageName != null && sharedPreferencePackageName.equals("Default") == false){
    			startGestureService(Item[i], sharedPreferencePackageName, getBaseContext());
            }
		}
	}
	public void stopAllGestureServices() {
	
		for (int i = 0; i < Item.length; i++) {
			stopGestureService(Item[i], getBaseContext());
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		stopAllGestureServices();
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_first);
		
		xaxis = new Intent(getBaseContext(), Xaxis.class);
		yaxis = new Intent(getBaseContext(), Yaxis.class);
		zaxis = new Intent(getBaseContext(), Zaxis.class);
		proximity = new Intent(getBaseContext(), Proximity.class);
		
		lv = (ListView) findViewById(R.id.listitem);
		context = this;
		lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

		      @Override
		      public void onItemClick(AdapterView<?> parent, final View view,
		          int position, long id) {
		    	  String gestureName = Item[position];		    	  
					//Toast.makeText(context, "", Toast.LENGTH_LONG).show();
					// TODO Auto-generated method stub
					intent = new Intent(context,ThirdMainActivity.class);
					intent.putExtra("GestureType", gestureName);
					startActivity(intent);
		      }

		    });
		
		reset = (Button) findViewById(R.id.button1);
		
		reset.setOnClickListener(new View.OnClickListener() {
			   @Override
			   public void onClick(View v) {
					SharedPreferences sharedPref = getApplicationContext().getSharedPreferences("GoQuick", 0);		
					SharedPreferences.Editor editor = sharedPref.edit();
					editor.putString("Proximity","Default");
					editor.putString("Shake In X","Default");
					editor.putString("Shake In Y","Default");
					editor.putString("Shake In Z","Default");
					editor.commit();
					
					reloadDataAndServices();
			   }
			  });
		
		startAllGestureServices();
		//Toast.makeText(getBaseContext(), "", Toast.LENGTH_LONG).show();		
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();		
		
		reloadDataAndServices();
	} 
	
	public void reloadDataAndServices(){
		SharedPreferences sharedPref = getApplicationContext().getSharedPreferences("GoQuick", 0);		
		String soundPackageName = sharedPref.getString("Proximity","Default");
		String orientationPackageName = sharedPref.getString("Shake In X","Default");
		String shakePackageName = sharedPref.getString("Shake In Y","Default");
		String proximityPackageName = sharedPref.getString("Shake In Z","Default");
				
		String[] packageNameList = new String[4] ;
		packageNameList[0] = soundPackageName;
		packageNameList[1] = orientationPackageName;
		packageNameList[2] = shakePackageName;
		packageNameList[3] = proximityPackageName;
		lv.setAdapter(new SecondMainActivity(this, Item, Images, packageNameList));
		
		for (int i = 0; i < Item.length; i++) {
			String sharedPreferencePackageName = sharedPref.getString(Item[i],"Default");
			
			stopGestureService(Item[i], getBaseContext());
			
            if(sharedPreferencePackageName != null && sharedPreferencePackageName.equals("Default") == false){
            	startGestureService(Item[i], sharedPreferencePackageName, getBaseContext());
            }
		}
	}
}