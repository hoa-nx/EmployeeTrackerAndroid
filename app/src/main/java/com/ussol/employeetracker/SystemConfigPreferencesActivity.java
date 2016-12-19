package com.ussol.employeetracker;

import android.app.Activity;
import android.os.Bundle;
import android.preference.Preference;

public class SystemConfigPreferencesActivity extends Activity {
	
	@Override
	 protected void onCreate(Bundle savedInstanceState) {
	  // TODO Auto-generated method stub
	  super.onCreate(savedInstanceState);
	  
	  getFragmentManager().beginTransaction().replace(android.R.id.content,
	                new SystemConfigPreferences()).commit();
	 }
	
	
}
