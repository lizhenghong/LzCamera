﻿package cn.listudio.lzcamera;

import android.app.Activity;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Build;

/**
 * @author lizheng
 * 
 */
public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			Intent intent = new Intent();//(this,CamActivity.class);
			intent.setAction("android.media.action.STILL_IMAGE_CAMERA"); 

			//	CamActivity camActivity = new CamActivity()
			
				startActivity(intent);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);

			Button buttonCamera = (Button) rootView.findViewById(R.id.button_camera);
			Button buttonSetAlarm = (Button) rootView.findViewById(R.id.button_setalarm);
			
			buttonCamera.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					Intent intent = new Intent(getActivity(),CamActivity.class);
					getActivity().startActivity(intent);
				}
			});
			
			buttonSetAlarm.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					Intent intent = new Intent(getActivity(),CamActivity.class);
					//getActivity().startActivity(intent);
					AlarmManager alarmManager = (AlarmManager)getActivity().getSystemService(Service.ALARM_SERVICE);
					PendingIntent pi = PendingIntent.getActivity(
							getActivity(), 0, intent, 0);
					alarmManager.setRepeating( AlarmManager.RTC_WAKEUP, 10000, 6*60000, pi) ;
					
					Toast.makeText(getActivity(), "AlarmManager设置成功啦"
							, Toast.LENGTH_SHORT).show();
				}
			});
			
			
			return rootView;
		}
	}

}
