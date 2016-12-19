/**
 * Copyright (c) 2012 Fujinet Software Department 5 ( USSOL team)  
 * See the file license.txt for copying permission.
*/  
  

package com.ussol.employeetracker;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.ussol.employeetracker.helpers.SystemConfigItemHelper;
import com.ussol.employeetracker.models.MasterConstants;
import com.ussol.employeetracker.models.SelectUser;
import com.ussol.employeetracker.passcodelock.AppLockManager;
import com.ussol.employeetracker.utils.Log;
import com.ussol.employeetracker.utils.Strings;

public class EmployeeTrackerApplication extends Application {
	
	private static Context applicationContext; 
	public static String FILES_DIR;
	public static boolean isInitialized = false;
	public static boolean toSync = false;
	public static ArrayList<SelectUser> googleContactList = new ArrayList<SelectUser>();
	private SystemConfigItemHelper systemConfig ;
    
    @Override
    public void onCreate() {
        super.onCreate();
        applicationContext = this;
        AppLockManager.getInstance().enableDefaultAppLockIfAvailable(this);
        if (AppLockManager.getInstance().isAppLockFeatureEnabled()) {
            AppLockManager.getInstance().getCurrentAppLock().setDisabledActivities(
                    new String[]{"com.ussol.employeetracker.DatabaseToGoogleDriveActivity","com.ussol.employeetracker.DatabaseToolActivity"});       	
        }
    	/*PreferenceManager.setDefaultValues(applicationContext, R.xml.preferences, false);*/
    	setSyncPrefs();
        Initialize();
        
        // Simply add the handler, and that's it! No need to add any code
        // to every activity. Everything is contained in EmployeeTrackerApplicationLifecycleCallbacks
        // with just a few lines of code. Now *that's* nice.
        //registerActivityLifecycleCallbacks(new EmployeeTrackerApplicationLifecycleCallbacks());
        
        systemConfig = new SystemConfigItemHelper(this);
    	
    }
    
    public static void setSyncPrefs() {
    	/*String token = SharedPreferencesHelper.getSharedPreferences().getString(getContext().getString(R.string.pref_key_token), "");
    	if(Strings.notEmpty(token)) {
    		toSync = true;
    	} else {
    		toSync = false;
    	}
    	
    	SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(applicationContext);
    	Log.d(preferences.getString(applicationContext.getString(R.string.pref_key_token), "not found"));
    	Log.d("******************************* Syncing syncing syncing **************************"+EmployeeTrackerApplication.toSync+" token "+token+" key "+applicationContext.getString(R.string.pref_key_token));
    	Log.d("******************************* Syncing syncing syncing **************************"+EmployeeTrackerApplication.toSync+" token "+SharedPreferencesHelper.getSharedPreferences().getString(getContext().getString(R.string.pref_key_sync_email), ""+" key "+applicationContext.getString(R.string.pref_key_token)));*/
    }

	public static void Initialize() {
    	if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
    		FILES_DIR = applicationContext.getExternalFilesDir(null).toString();
    		if(FILES_DIR != null) {
    			MasterConstants.DIRECTORY = FILES_DIR + MasterConstants.ET_FOLDER;
        		File mFile = new File(MasterConstants.DIRECTORY + MasterConstants.DIRECTORY_AUDIO);
        		mFile.mkdirs();
        		mFile = new File(MasterConstants.DIRECTORY + MasterConstants.DIRECTORY_PICTURE);
        		mFile.mkdirs();
        		/*if(!SharedPreferencesHelper.getSharedPreferences().contains(applicationContext.getString(R.string.pref_key_run_first_time))) {
        			File prevVerDir = new File(Environment.getExternalStorageDirectory()+"/EmployeeTracker");
        			if(prevVerDir.exists()) {
        				try {
        					copyDirectory(prevVerDir, new File(FILES_DIR+"/EmployeeTracker"));
        				} catch (IOException e) {
        					e.printStackTrace();
        				}
        				
        				try {
        					deleteDirectory(prevVerDir);
        				} catch (IOException e) {
        					e.printStackTrace();
        				}
        			}
        			SharedPreferencesHelper.setBooleanPrefs(R.string.pref_key_run_first_time, false);
        		}*/
        		isInitialized = true;
    		}
    	} else {
    		Toast.makeText(applicationContext, "sdcard not available", Toast.LENGTH_LONG).show();
    	}
	}

	public static Context getContext() {
    	return applicationContext;
    }
	
	private static void copyDirectory(File sourceLocation , File targetLocation) throws IOException {
        if (sourceLocation.isDirectory()) {
            if (!targetLocation.exists()) {
                targetLocation.mkdir();
            }
            
            String[] children = sourceLocation.list();
            for (int i=0; i<children.length; i++) {
                copyDirectory(new File(sourceLocation, children[i]), new File(targetLocation, children[i]));
            }
        } else {
            
            InputStream in = new FileInputStream(sourceLocation);
            OutputStream out = new FileOutputStream(targetLocation);
            
            // Copy the bits from instream to outstream
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
        }
    }
	
	private static void deleteDirectory(File fileOrDirectory) throws IOException{
	    if (fileOrDirectory.isDirectory()) {
	        for (File child : fileOrDirectory.listFiles()) { deleteDirectory(child);}
	    }
	    fileOrDirectory.delete();
	}

	/**
	 * Lấy năm tài chính setting trong hệ thống
	 * @return Năm tài chính
	 */
	public int getYearProcessing() {
	    int currentYear=0;

		currentYear = systemConfig.getYearProcessing();
    	if(currentYear==0){
    		currentYear = Calendar.getInstance().get(Calendar.YEAR);
    	}
        return currentYear;
    }
	/**
	 * Lấy tháng năm kinh nghiệm setting trong hệ thống
	 * @return
	 */
	public int getKeikenMonthProcessing() {
	    int keikenMonthSystem =3;
    	keikenMonthSystem = systemConfig.getKeikenMonthProcessing();
    	if(keikenMonthSystem==0){
    		keikenMonthSystem = 3;
    	}
        return keikenMonthSystem;
    }
	
}