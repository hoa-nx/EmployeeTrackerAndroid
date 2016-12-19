/**
 * Copyright (c) 2012 Fujinet Software Department 5 ( USSOL team)  
 * See the file license.txt for copying permission.
*/  

package com.ussol.employeetracker;

import com.ussol.employeetracker.helpers.AlertDialogRadio.AlertPositiveListener;
import com.ussol.employeetracker.helpers.DatabaseAdapter;
import com.ussol.employeetracker.models.Dept;
import com.ussol.employeetracker.models.MasterConstants;
import com.ussol.employeetracker.models.User;
import com.ussol.employeetracker.models.UserHistory;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

public class HisUserMainActivity extends FragmentActivity implements AlertPositiveListener {
	ViewPager mViewPager;
	EditUserMainAdapter mTabsAdapter;
	public static int nCode =-1; /** init xem như tạo mới nhân viên*/
	static String TabFragmentHisMain;
	static String TabFragmentHisOther;
	public static User userInfo ;
	public static UserHistory userHisInfo ;
	public static int currentGroup;
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * setTabFragmentBasic
     * 
     *▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
	public static void setTabFragmentHisMain(String t){
		TabFragmentHisMain = t;
	}
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * getTabFragmentBasic 
     * 
     *▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
	public static String getTabFragmentHisMain(){
	  return TabFragmentHisMain;
	}
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * setTabFragmentWork
     * 
     *▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
	public  static void setTabFragmentHisOther(String t){
		TabFragmentHisOther = t;
	}
	 
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * getTabFragmentWork
     * 
     *▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
	public static String getTabFragmentHisOther(){
	  return TabFragmentHisOther;
	}
    
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * onCreate
     * 
     *▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/ 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        mViewPager = new ViewPager(this);
        mViewPager.setId(R.id.pagerhis);
        //Boolean customTitleSupported = requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(mViewPager);
        /*if (customTitleSupported) {
            getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,R.layout.user_cus_title);
            TextView tv = (TextView) findViewById(R.id.txtUserTitle);
            tv.setText("Custom");
        } */            
       
        final ActionBar bar = getActionBar();
        bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        bar.setDisplayOptions(ActionBar.DISPLAY_SHOW_TITLE, ActionBar.DISPLAY_SHOW_TITLE);
        bar.setDisplayShowHomeEnabled(false);
        bar.setDisplayShowTitleEnabled(false);
        
        /** get code user tu intent*/ 
  		Intent request = getIntent();
  		Bundle param = request.getExtras();
      		
        mTabsAdapter = new EditUserMainAdapter(this, mViewPager);
        mTabsAdapter.addTab(bar.newTab().setText(R.string.userhisdeptTitleTabMain),HisUserMain.class, param);
        mTabsAdapter.addTab(bar.newTab().setText(R.string.userhisposTitleTabOther),HisUserOther.class, param);

        if (savedInstanceState != null) {
            bar.setSelectedNavigationItem(savedInstanceState.getInt("tab", 0));
        }

		if (param != null) {
			nCode =param.getInt(DatabaseAdapter.KEY_CODE);
			currentGroup = param.getInt(MasterConstants.EXP_USER_GROUP_TAG);
			userInfo = (User)param.getParcelable(MasterConstants.TAB_USER_TAG);
			userHisInfo = (UserHistory)param.getParcelable(MasterConstants.TAB_USER_HIS_TAG);	
		}else{
			nCode =-1;
			userInfo=null;
			userHisInfo = null;
		}
    }
    
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * onSaveInstanceState
     * 
     *▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
	 @Override
	 protected void onSaveInstanceState(Bundle outState) {
	  //super.onSaveInstanceState(outState);
	  outState.putInt("tab", getActionBar().getSelectedNavigationIndex());
	 }
		
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * Defining button click listener for the OK button of the alert dialog window 
     * 
     *▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    @Override
    public void onPositiveClick(int position,int clickBtn, Bundle bundle) {
    	/** tab thông tin phòng ban, nhóm,chức vụ ... */
    	String TabOfFragmentMain =getTabFragmentHisMain();
		HisUserMain fgUserMain = (HisUserMain)getSupportFragmentManager().findFragmentByTag(TabOfFragmentMain);
		if (fgUserMain!=null){
			fgUserMain.onPositiveClick(position,clickBtn, bundle);
		
		}
    	/** tab thông tin về tiếng Nhật và trợ cấp nghiệp vụ... */
    	String TabOfFragmentOther =getTabFragmentHisOther();
    	HisUserOther fgUserOther = (HisUserOther)getSupportFragmentManager().findFragmentByTag(TabOfFragmentOther);
		if (fgUserOther!=null){
			fgUserOther.onPositiveClick(position,clickBtn,bundle);
		
		}
    }
}