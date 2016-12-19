/**
 * Copyright (c) 2012 Fujinet Software Department 5 ( USSOL team)  
 * See the file license.txt for copying permission.
*/  
package com.ussol.employeetracker;

import com.ussol.employeetracker.models.MasterConstants;
import com.ussol.employeetracker.models.SharedVariables;

import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.widget.ShareActionProvider;

public class MasterTabListener<T extends Fragment> implements TabListener {
	private Fragment mFragment;
	private final Activity mActivity;
	private final String mTag;
	private final Class<T> mClass;
	//tag
	private final static String TAG =MasterTabListener.class.getName();
	
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * MasterTabListener
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
	public MasterTabListener(Activity activity, String tag, Class<T> clz){
		mActivity = activity;
		mTag = tag;
		mClass = clz;
		/*if (tag.equals("department")){
			mActivity.setContentView(R.layout.activity_department_fragment);
		}*/
		
		/*mFragment = mActivity.getFragmentManager().findFragmentByTag(mTag);
	    if (mFragment != null && !mFragment.isDetached()) {
	        FragmentTransaction ft = mActivity.getFragmentManager().beginTransaction();
	        ft.detach(mFragment);
	        ft.commit();
	    }*/
	    
	}
	
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * onTabReselected
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		// Nothing special to do here for this application
	}
	
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * onTabSelected
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		if (mTag==MasterConstants.TAB_DEPT_TAG){
			SharedVariables.CURRENT_TAB_INDEX = 0;
		}else if (mTag==MasterConstants.TAB_TEAM_TAG){
			SharedVariables.CURRENT_TAB_INDEX = 1;
		}else if (mTag==MasterConstants.TAB_POSITION_TAG){
			SharedVariables.CURRENT_TAB_INDEX = 2;
		}
		mFragment = mActivity.getFragmentManager().findFragmentByTag(mTag); 
		if(mFragment==null){
			mFragment = Fragment.instantiate(mActivity, mClass.getName());
			
			ft.add(android.R.id.content,mFragment, mTag);

		}else{
			ft.attach(mFragment);

		}
		
	}
	
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * onTabUnselected
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		if(mFragment!=null)
			//ft.detach(mFragment);	
			ft.remove(mFragment);
			//ft.commit();
	}

	
}
