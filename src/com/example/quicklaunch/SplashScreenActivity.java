package com.example.quicklaunch;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;

public class SplashScreenActivity extends Activity {

	Intent i;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        i=new Intent(getBaseContext(),FirstMainActivity.class);
		new Thread(new Runnable() {
			
			@Override
			public void run() {
			try{
				Thread.sleep(3000);
				startActivity(i);
			}catch (Exception e) {
				e.printStackTrace();
			}
				finish();
			}
		}).start();
    }


    
}
