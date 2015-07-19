package com.example.quicklaunch;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class GestureListAdapter extends BaseAdapter {
	String[] result;
	Context context;
	int[] imageId;
	private static LayoutInflater inflater = null;
	Intent intent;
	public static TextView tv1, tv2;
	public static ImageView img1, img2;
	SharedPreferences shared_preferences1, shared_preferences2,
			shared_preferences3, shared_preferences4;
	SharedPreferences.Editor shared_preferences_editor1,
			shared_preferences_editor2, shared_preferences_editor3,
			shared_preferences_editor4;

	public static String[] Item = { "SetApp1", "SetApp2", "SetApp3", "SetApp4" };
	public static int[] Images = { R.drawable.app_default_icon, R.drawable.app_default_icon,
			R.drawable.app_default_icon, R.drawable.app_default_icon };
	public static String[] packageNameList;
		
	//HashMap<String, String> object = new HashMap<String, String>();
	
	public GestureListAdapter(GestureListActivity mainActivity,
			String[] prgmNameList, int[] prgmImages, String[] lpackageNameList) {
		// TODO Auto-generated constructor stub
		packageNameList = lpackageNameList;
		result = prgmNameList;
		context = mainActivity;
		imageId = prgmImages;
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return result.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub

		convertView = inflater.inflate(R.layout.activity_second, null);
		tv1 = (TextView) convertView.findViewById(R.id.textView1);
		img1 = (ImageView) convertView.findViewById(R.id.thumb);
		tv1.setText(result[position]);
		img1.setImageResource(imageId[position]);

		tv2 = (TextView) convertView.findViewById(R.id.textView2);
		img2 = (ImageView) convertView.findViewById(R.id.ImageView01);
		
		String packageName = packageNameList[position];
		if(packageName.equals("Default")){
			tv2.setText("Select App");
			img2.setBackgroundResource(R.drawable.app_default_icon);
		}else{
			PackageManager packageManager = context.getPackageManager();
			ApplicationInfo app = null;
			
			if (null != packageManager
					.getLaunchIntentForPackage(packageName)) {
				try {
					app = packageManager.getApplicationInfo(packageName, 0);
				} catch (NameNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				String appName = (String) packageManager.getApplicationLabel(app);		
				Drawable appIcon = packageManager.getApplicationIcon(app);
				
				tv2.setText(appName);
				img2.setImageDrawable(appIcon);
			}else{
				tv2.setText("Select app");
				img2.setBackgroundResource(R.drawable.app_default_icon);
			}
		}
		return convertView;
		
	}

}