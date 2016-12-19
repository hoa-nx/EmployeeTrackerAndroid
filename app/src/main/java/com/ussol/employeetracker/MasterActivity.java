/**
 * Copyright (c) 2012 Fujinet Software Department 5 ( USSOL team)  
 * See the file license.txt for copying permission.
*/  
package com.ussol.employeetracker;

import com.ussol.employeetracker.models.MasterConstants;
import com.ussol.employeetracker.models.SharedVariables;
import com.ussol.employeetracker.models.Team;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.StrictMode;
//import android.support.v4.app.FragmentActivity;

public class MasterActivity extends Activity {
	//tag
	private final static String TAG =MasterActivity.class.getName();
	/** Called when the activity is first created. */
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * onCreate
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
	private ActionBar actionBar ;
    @SuppressWarnings("deprecation")
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
       
        actionBar.setDisplayShowTitleEnabled(true);
        
        /** Creating DEPARTMENT Tab */
        Tab tab = actionBar.newTab()
        		.setText("Phòng ban")
        		.setTabListener(new MasterTabListener<DepartmentFragment>(this, MasterConstants.TAB_DEPT_TAG, DepartmentFragment.class))
        		.setIcon(R.drawable.department);
        		
        actionBar.addTab(tab);
        
        /** Creating TEAM Tab */
        tab = actionBar.newTab()
        		.setText("Tổ")
        		.setTabListener(new MasterTabListener<TeamFragment>(this, MasterConstants.TAB_TEAM_TAG, TeamFragment.class))
        		.setIcon(R.drawable.users_group);

        actionBar.addTab(tab);            
        
        /** Creating POSITION Tab */
        tab = actionBar.newTab()
        		.setText("Chức vụ")
        		.setTabListener(new MasterTabListener<PositionFragment>(this, MasterConstants.TAB_POSITION_TAG, PositionFragment.class))
        		.setIcon(R.drawable.position);
        
        actionBar.addTab(tab);
        /** lấy thông tin xem đã gọi từ chức năng nào*/
        int TabSelected;
        if (savedInstanceState == null) {
        	Intent request= getIntent();
            Bundle param = request.getExtras(); 		
            TabSelected = param.getInt(MasterConstants.MAIN_TO_TAB_CALL);
        }else{
        	TabSelected = savedInstanceState.getInt(MasterConstants.MAIN_TO_TAB_CALL);	
        }
        /** setting selected tab*/
        actionBar.setSelectedNavigationItem(TabSelected);
    }
	/**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * onSaveInstanceState
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/   
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		/** lưu lại trạng thái của TAB đang được chọn */
		outState.putInt(MasterConstants.MAIN_TO_TAB_CALL,actionBar.getSelectedNavigationIndex());
	}
	
	/**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * onConfigurationChanged
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		/** lưu màn hình ngang hay dọc */
		SharedVariables.CURRENT_ORIENTATION = getResources().getConfiguration().orientation ;
	}
}
