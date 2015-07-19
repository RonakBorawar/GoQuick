package com.example.quicklaunch;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;

public class ShakeYService extends Service implements SensorEventListener {

	SensorManager sensor_manager;
	Sensor yaxis;
	SharedPreferences shared_preferences;
	
	String packagename1=null;

    @Override
	public IBinder onBind(Intent arg0) {
		return null;
	}
   	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		
		String packagename = intent.getStringExtra("Package");
		packagename1=packagename;
		
		sensor_manager=(SensorManager) getSystemService(SENSOR_SERVICE);
		yaxis = sensor_manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		
		if(packagename1 == null){
			sensor_manager.unregisterListener(this, yaxis);	
		}else{
			sensor_manager.registerListener(this, yaxis,
						SensorManager.SENSOR_DELAY_NORMAL);		
		}
		
		return START_STICKY;
	}

	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {

	}

	@Override
	public void onSensorChanged(SensorEvent arg0) {

		float[] sp = arg0.values;
		if (sp[1] > 14) {
			Intent launchIntent = getPackageManager()
					.getLaunchIntentForPackage(packagename1);
			startActivity(launchIntent);
		}
	}
}
