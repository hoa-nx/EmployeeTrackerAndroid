/**
 * 
 */
package com.ussol.employeetracker;

import com.ussol.employeetracker.models.MasterConstants;
import com.ussol.employeetracker.passcodelock.PasscodeManagePasswordActivity;
import com.ussol.employeetracker.utils.BadgeAction;

import android.annotation.SuppressLint;
import android.app.job.JobScheduler;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.SwitchPreference;
import android.util.Log;

import me.leolin.shortcutbadger.ShortcutBadger;
//import android.support.v4.media.routing.MediaRouterJellybeanMr1.ActiveScanWorkaround;

public class SystemConfigPreferences extends PreferenceFragment {
	SharedPreferences mPrefs;
	public static final String  KEY_TYPE = "type";
	private static final String TAG = "SystemConfigPreferences";
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.system_config_preferences);
		
		SwitchPreference passCodeEnabled = (SwitchPreference) findPreference("config_enablePassCode");
	    SwitchPreference enableBackgroundJob = (SwitchPreference) findPreference("config_enableBackgroundJob");
	    
        /** cau hinh cho phep su dung password **/
        if (passCodeEnabled != null){
            //Update the operations like storing, updating UI etc... on pref change.
    		passCodeEnabled.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
			@Override
			public boolean onPreferenceChange(Preference arg0, Object isPassCodeEnabled) {
			              
				boolean isPassCodeOn = ((Boolean) isPassCodeEnabled).booleanValue();
				Intent passCodeInt = new Intent(getActivity() , PasscodeManagePasswordActivity.class);
				Bundle bundlePassCode = new Bundle();
				
				if(isPassCodeOn)
				{
					//goi toi Intent de setting mat khau
					bundlePassCode.putInt(KEY_TYPE, 0);
	    			passCodeInt.putExtras(bundlePassCode);
					
					startActivity(passCodeInt);
				}else{
					//khong active
					bundlePassCode.putInt(KEY_TYPE, 1);
	    			passCodeInt.putExtras(bundlePassCode);
					
					startActivity(passCodeInt);
				}
				return true;
           }

			
    		});
    	}
        /** cau hinh thuc hien job ngam **/
        if (enableBackgroundJob != null){
            //Update the operations like storing, updating UI etc... on pref change.
        	enableBackgroundJob.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
			@SuppressLint("NewApi")
			@Override
			public boolean onPreferenceChange(Preference arg0, Object isBackgroundJobEnabled) {
				boolean isBackgroundJobOn = ((Boolean) isBackgroundJobEnabled).booleanValue();
				Intent backgroundJobInt = new Intent(getActivity() , AlarmJobServiceActivity.class);
				Bundle bundleBackgroundJob = new Bundle();
				
				/** Tao badge */
    	        //BadgeAction badge = new BadgeAction(getActivity());
    	        //badge.deleteBagde(true);
    	        /** reset icon **/
    	        //badge.clearBadgeIcon();
				ShortcutBadger.removeCount(getActivity()); //for 1.1.4+


				if(isBackgroundJobOn)
				{
					//goi toi Intent de setting thuc thi background jobs
					bundleBackgroundJob.putInt(KEY_TYPE, 0);
					backgroundJobInt.putExtras(bundleBackgroundJob);
					
					//startActivity(backgroundJobInt);
					/**khởi tạo activity dùng để edit  */
            		startActivityForResult(backgroundJobInt , MasterConstants.CALL_JOB_SETTING_ACTIVITY_CODE);
				}else{
					//cancel cac job
					JobScheduler tm = (JobScheduler) getActivity().getSystemService(Context.JOB_SCHEDULER_SERVICE);
				    tm.cancelAll();
				}
				return true;
			}

        	});
    	}
        
	}

	/**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * onActivityResult
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch(requestCode){
			case MasterConstants.CALL_JOB_SETTING_ACTIVITY_CODE:
				if (resultCode==0){
					
					SwitchPreference enableBackgroundJob = (SwitchPreference) findPreference("config_enableBackgroundJob");
					/** cau hinh thuc hien job ngam **/
			        if (enableBackgroundJob != null){
			        	enableBackgroundJob.setChecked(false);
			        }
					//Bundle bundle = data.getExtras();
					//currentGroup = bundle.getInt(MasterConstants.EXP_USER_GROUP_TAG);
					SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
					//SharedPreferences.Editor editor = root.getPreferenceManager().getSharedPreferences().edit();

					//or set the values. 
					SharedPreferences.Editor editor = preferences.edit();
					editor.putBoolean("config_enableBackgroundJob", true); //This is just an example, you could also put boolean, long, int or floats
					editor.commit();
				}
				break;

		}
	}
}
