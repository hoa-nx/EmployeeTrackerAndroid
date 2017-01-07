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

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

public class EditUserMainActivity extends FragmentActivity implements AlertPositiveListener {
	ViewPager mViewPager;
	EditUserMainAdapter mTabsAdapter;
	public static int nCode =-1; /** init xem như tạo mới nhân viên*/
	static String TabFragmentBasic;
	static String TabFragmentWork;
	static String TabFragmentOther;
	public static User userInfo ;
	public static String listViewCurrentPosition ="0";
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * setTabFragmentBasic
     * 
     *▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
	public static void setTabFragmentBasic(String t){
	  TabFragmentBasic = t;
	}
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * getTabFragmentBasic 
     * 
     *▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
	public static String getTabFragmentBasic(){
	  return TabFragmentBasic;
	}
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * setTabFragmentWork
     * 
     *▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
	public  static void setTabFragmentWork(String t){
		  TabFragmentWork = t;
	}
	 
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * getTabFragmentWork
     * 
     *▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
	public static String getTabFragmentWork(){
	  return TabFragmentWork;
	}
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * setTabFragmentOther
     * 
     *▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
	public static void setTabFragmentOther(String t){
		  TabFragmentOther = t;
	}
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * getTabFragmentOther
     * 
     *▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/ 
	public static String getTabFragmentOther(){
	  return TabFragmentOther;
	}
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * onCreate
     * 
     *▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/ 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        mViewPager = new ViewPager(this);
        mViewPager.setId(R.id.pager);
        //Boolean customTitleSupported = requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(mViewPager);
        /*if (customTitleSupported) {
            getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,R.layout.user_cus_title);
            TextView tv = (TextView) findViewById(R.id.txtUserTitle);
            tv.setText("Custom");
        } */            
       
        final ActionBar bar = getActionBar();
		//final ActionBar bar = ((AppCompatActivity) ((Activity) this)).getSupportActionBar();
        bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        bar.setDisplayOptions(ActionBar.DISPLAY_SHOW_TITLE, ActionBar.DISPLAY_SHOW_TITLE);
        bar.setDisplayShowHomeEnabled(false);
        bar.setDisplayShowTitleEnabled(false);
        
        /** get code user tu intent*/ 
  		Intent request = getIntent();
  		Bundle param = request.getExtras();
      		
        mTabsAdapter = new EditUserMainAdapter(this, mViewPager);
        mTabsAdapter.addTab(bar.newTab().setText(R.string.userTitleTabBasic),EditUserBasic.class, param);
        mTabsAdapter.addTab(bar.newTab().setText(R.string.userTitleTabWork),EditUserWork.class, param);
        mTabsAdapter.addTab(bar.newTab().setText(R.string.userTitleTabOther),EditUserOther.class, param);

        if (savedInstanceState != null) {
            bar.setSelectedNavigationItem(savedInstanceState.getInt("tab", 0));
        }

		if (param != null) {
			nCode =param.getInt(DatabaseAdapter.KEY_CODE);
			userInfo = (User)param.getParcelable(MasterConstants.TAB_USER_TAG);
			listViewCurrentPosition =param.getString(MasterConstants.LISTVIEW_CURRENT_POSITION);
			/*if (nCode==-1){
				*//** trường hợp tạo mới*//*
				//initDetail();
			}else if (nCode==0){
				*//** hiển thị copy data*//*
				setValueTabBasic(userInfo);
				setValueTabWork(userInfo);
				setValueTabOther(userInfo);
			}else{
				*//** hiển thị chi tiết *//*
				setValueTabBasic(userInfo);
				setValueTabWork(userInfo);
				setValueTabOther(userInfo);
			}*/
		}else{
			nCode =-1;
			userInfo=null;
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
    	/** tab thông tin cơ bản ... */
    	/*String TabOfFragmentBasic =getTabFragmentWork();
		EditUserBasic fgUserBasic = (EditUserBasic)getSupportFragmentManager().findFragmentByTag(TabOfFragmentBasic);
		if (fgUserBasic!=null){
			fgUserBasic.onPositiveClick(position);
		
		}*/
    	/** tab thông tin về phòng ban ... */
    	String TabOfFragmentWork =getTabFragmentWork();
		EditUserWork fgUserWork = (EditUserWork)getSupportFragmentManager().findFragmentByTag(TabOfFragmentWork);
		if (fgUserWork!=null){
			fgUserWork.onPositiveClick(position,clickBtn,bundle);
		
		}
    	/** tab thông tin về khác... */
    	String TabOfFragmentOther =getTabFragmentOther();
		EditUserOther fgUserOther = (EditUserOther)getSupportFragmentManager().findFragmentByTag(TabOfFragmentOther);
		if (fgUserOther!=null){
			fgUserOther.onPositiveClick(position,clickBtn,bundle);
		
		}
    }
    
    public void setValueTabBasic(User item){
    	String TabOfFragmentBasic =getTabFragmentBasic();
		   
    	EditUserBasic fgUserBasic = (EditUserBasic)getSupportFragmentManager().findFragmentByTag(TabOfFragmentBasic);
		   
		if (fgUserBasic!=null){
			/**thông tin của tab Basic*/
			fgUserBasic.setUserCode(item.code);
			fgUserBasic.setFullName(item.full_name);
			fgUserBasic.setAddress(item.address);
			fgUserBasic.setBirthday(item.birthday);
			fgUserBasic.setMarriedday(item.married_date);
			fgUserBasic.setEmail(item.email);
			fgUserBasic.setImgFullPath(item.img_fullpath);
			fgUserBasic.setMobile(item.mobile);
			fgUserBasic.setSex(String.valueOf(item.sex ));
		}
    }
    
    public void setValueTabWork(User item){
    	String TabOfFragmentWork =getTabFragmentBasic();
		   
    	EditUserWork fgUserWork = (EditUserWork)getSupportFragmentManager().findFragmentByTag(TabOfFragmentWork);
		   
		if (fgUserWork!=null){
			/**thông tin của tab Work*/
			fgUserWork.setDeptCode(String.valueOf(item.dept));
			fgUserWork.setDeptName(item.dept_name);
			fgUserWork.setTeamCode(String.valueOf(item.team));
			fgUserWork.setTeamName(item.team_name);
			fgUserWork.setPositionCode(String.valueOf(item.position));
			fgUserWork.setPositionName(item.position_name);
			
			fgUserWork.setTraining_date(item.training_date);
			fgUserWork.setTraining_dateEnd(item.training_dateEnd);
			
			fgUserWork.setLearnTraining_date(item.learn_training_date);
			fgUserWork.setLearnTraining_dateEnd(item.learn_training_dateEnd);
			
			fgUserWork.setKeikenConvert(item.convert_keiken);
			
			fgUserWork.setIn_date(item.in_date);
			fgUserWork.setOut_date(item.out_date);
			fgUserWork.setJoin_date(item.join_date);
			fgUserWork.setLabourOut_date(item.labour_out_date);
			
			fgUserWork.setIsCurrentLabour(String.valueOf(item.isLabour));
			
		}
    }
    
    public void setValueTabOther(User item){
    	String TabOfFragmentOther =getTabFragmentOther();
		   
    	EditUserOther fgUserOther = (EditUserOther)getSupportFragmentManager().findFragmentByTag(TabOfFragmentOther);
		   
		if (fgUserOther!=null){
			/**thông tin của tab Other*/
			fgUserOther.setJapaneseLevel(item.japanese);
			fgUserOther.setAllowance_Business(item.allowance_business);
			fgUserOther.setAllowance_BSE(item.allowance_bse);
			fgUserOther.setAllowance_Room(item.allowance_room);
			fgUserOther.setBusinessKbn(String.valueOf(item.business_kbn ));
			fgUserOther.setProgram((float)item.program);
			fgUserOther.setDetailDesign((float)item.detaildesign);
			fgUserOther.setPositionGroupList();
			fgUserOther.setCustomerGroupList();
		}
    }
    
}