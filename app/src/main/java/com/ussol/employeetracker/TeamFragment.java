/**
 * Copyright (c) 2012 Fujinet Software Department 5 ( USSOL team)  
 * See the file license.txt for copying permission.
*/  
package com.ussol.employeetracker;

import com.ussol.employeetracker.models.SharedVariables;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
//import android.support.v4.app.Fragment;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * @author hoa-nx
 *
 */
public class TeamFragment  extends Fragment{
	//tag
	private final static String TAG =TeamFragment.class.getName();
	View myFragmentView=null;

    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * onCreate
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//Tam thoi comment do khong hoat dong tai Android 5.0 START
		//this.setRetainInstance(true);
		if (savedInstanceState != null)
			return;
	}
	
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * onCreateView
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		try{
			//inflate layout 
			//if(myFragmentView==null){
				myFragmentView = inflater.inflate(R.layout.activity_team_fragment, container, false);
			//}
		} catch (Exception e) {
			Log.v(TAG,e.getMessage());
		}
		return myFragmentView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		//get activity
		 Activity activity = getActivity();
		 //chỉ refesh lại trang trong trường hợp là màn hình dọc
		 if (activity != null && SharedVariables.CURRENT_ORIENTATION == Configuration.ORIENTATION_PORTRAIT) {
			 //lay thong tin cua list fragment
			 ListTeamFragment listFragment = (ListTeamFragment) getFragmentManager()
						.findFragmentById(R.id.listTeamFragment);
			 /*if(listFragment==null)
			 {
				 final String listFragmentTag = ListTeamFragment.class.getName();
				 listFragment = new ListTeamFragment();
				 final FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
			        fragmentTransaction.replace(R.id.listTeamFragment, listFragment, listFragmentTag);
			        fragmentTransaction.commit();
			 }*/
			 //Tam thoi comment do khong hoat dong tai Android 5.0 START
			 //goi lai onResume cua list fragment de refresh lai data tren listview
			 //listFragment.onActivityCreated(savedInstanceState);
		 }
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
		//lưu màn hình ngang hay dọc
		SharedVariables.CURRENT_ORIENTATION = getResources().getConfiguration().orientation ;
	}
}
