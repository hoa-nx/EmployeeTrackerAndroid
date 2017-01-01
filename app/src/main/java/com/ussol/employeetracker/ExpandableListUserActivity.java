package com.ussol.employeetracker;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.ussol.employeetracker.helpers.DatabaseAdapter;
import com.ussol.employeetracker.helpers.ExpParent;
import com.ussol.employeetracker.helpers.ExpAdapter;
import com.ussol.employeetracker.helpers.ExpGroupHelper;
import com.ussol.employeetracker.helpers.ExpParentChildInGroup;
import com.ussol.employeetracker.helpers.IconContextMenu;
import com.ussol.employeetracker.helpers.SystemConfigItemHelper;
import com.ussol.employeetracker.models.IExpGroup;
import com.ussol.employeetracker.models.MasterConstants;
import com.ussol.employeetracker.models.User;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ExpandableListActivity;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.DisplayMetrics;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View.OnLongClickListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.view.Window;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.ExpandableListContextMenuInfo;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.LinearLayout;
import android.widget.SearchView;

public class ExpandableListUserActivity extends Activity implements OnChildClickListener , SearchView.OnQueryTextListener, SearchView.OnCloseListener{
	private ExpandableListView mExpandableList;
	private ExpGroupHelper  grp =null;
	private String[] arrGroup=null;
	private List<User> list;
	private DisplayMetrics metrics;
	private int width;
	private static  int currentGroup=IExpGroup.EXP_GROUP_DEPT;
	private User info =null;
	private ExpandableListAdapter mExpandableListAdapter;
	private static final int MENU_DELETE =0;
	private DatabaseAdapter mDatabaseAdapter;
	private ExpAdapter mExpAdapter;
	private static final int MENU_ITEM_ADDCOPY_ACTION = 1;
	private static final int MENU_ITEM_DETAIL_ACTION = 2;
	private static final int MENU_ITEM_EDIT_ACTION = 3;
	private static final int MENU_ITEM_DELETE_ACTION = 4;
	private static final int MENU_ITEM_HISTORY_ACTION = 5;
	private static final int CONTEXT_MENU_ID = 410;
	private IconContextMenu iconContextMenu = null;
	private int groupPosition , childPosition;
	private SearchView search;
	private LinearLayout layoutSearch;
	private SystemConfigItemHelper systemConfig ;
    private int currentYear;
    private int keikenMonthSystem;
	/**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * onCreate
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    @SuppressLint("NewApi")
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.expandable_list_view_user_main);
        //mo ket noi SQLite
        mDatabaseAdapter = new DatabaseAdapter(getBaseContext());
        //get view
        mExpandableList = (ExpandableListView)findViewById(R.id.expandable_list);
        
        systemConfig = new SystemConfigItemHelper(this);
    	currentYear = systemConfig.getYearProcessing();
    	keikenMonthSystem = systemConfig.getKeikenMonthProcessing();
    	if(currentYear==0){
    		currentYear = Calendar.getInstance().get(Calendar.YEAR);
    	}
    	//get param tu man hinh chart
		/** get code user tu intent*/
		Intent request = getIntent();
		Bundle param = request.getExtras();
		int expKey =0;
		if(param!=null){
			expKey = param.getInt(DatabaseAdapter.KEY_EXPANDABLE_GROUP);
		}else{
			expKey =IExpGroup.EXP_GROUP_DEPT;
		}


        //lay kich thuoc
        metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        width = metrics.widthPixels;
        //mExpandableList.setIndicatorBounds(width - GetDipsFromPixel(40), width - GetDipsFromPixel(10));
        int width = metrics.widthPixels; 
        if(android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
        	mExpandableList.setIndicatorBounds(width-GetDipsFromPixel(35), width-GetDipsFromPixel(5));
		} else {
			mExpandableList.setIndicatorBoundsRelative(width-GetDipsFromPixel(35), width-GetDipsFromPixel(5));
        }
        
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        search = (SearchView) findViewById(R.id.search);
        layoutSearch = (LinearLayout) findViewById(R.id.fdLinearLayoutSearch);
        search.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        search.setIconifiedByDefault(false);
        search.setOnQueryTextListener(this);
        search.setOnCloseListener(this);
        
        grp = new ExpGroupHelper(getApplicationContext());
        //hien thi init group tu theo param
		if(expKey>0){
			getParentChildInGroup(expKey);
		}else{
			getParentChildInGroup(IExpGroup.EXP_GROUP_DEPT);
		}

        mExpandableList.setOnChildClickListener(this);
        mExpandableList.setOnCreateContextMenuListener(this);
        
        /**init the menu*/
        Resources res = getResources();
        iconContextMenu = new IconContextMenu(this, CONTEXT_MENU_ID);
        iconContextMenu.addItem(res, R.string.medit, R.drawable.edit, MENU_ITEM_EDIT_ACTION);
        iconContextMenu.addItem(res, R.string.mshowhistory, R.drawable.history_position, MENU_ITEM_HISTORY_ACTION);
        iconContextMenu.addItem(res, R.string.mdelete, R.drawable.delete, MENU_ITEM_DELETE_ACTION);
 
        //set onclick listener for context menu
        iconContextMenu.setOnClickListener(new IconContextMenu.IconContextMenuOnClickListener() {
            @Override
            public void onClick(int menuId) {
                switch(menuId) {
	                case MENU_ITEM_EDIT_ACTION:
	                	/**  chỉnh sửa */
	            		Intent intent = new Intent(getApplicationContext(), EditUserMainActivity.class);
	            		Bundle bundle = new Bundle();
	            		/**lấy code của user*/
	            		bundle.putInt(DatabaseAdapter.KEY_CODE, info.code);
	            		bundle.putParcelable(MasterConstants.TAB_USER_TAG, info);
	            		/** thông tin lưu trữ nhóm đang chọn*/
	            		bundle.putInt(MasterConstants.EXP_USER_GROUP_TAG,currentGroup);
	            		
	            		/**gán vào bundle để gửi cùng với intent */
	            		intent.putExtras(bundle);
						//intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	            		/**khởi tạo activity dùng để edit  */
						startActivityForResult(intent , MasterConstants.CALL_USER_ACTIVITY_CODE);
	    				break;
	                case MENU_ITEM_HISTORY_ACTION:
	                	/** xem thong tin lich su */
	    				Intent intentHis = new Intent(getApplicationContext(), ExpandableListUserHisActivity.class);
	    				Bundle bundleHis = new Bundle();
	    				/**lấy code của user*/
	    				bundleHis.putInt(DatabaseAdapter.KEY_CODE, info.code);
	    				bundleHis.putParcelable(MasterConstants.TAB_USER_TAG, info);
	    				/**gán vào bundle để gửi cùng với intent */
	    				intentHis.putExtras(bundleHis);

	    				/**khởi tạo activity dùng để edit  */
						startActivityForResult(intentHis , MasterConstants.CALL_HISTORY_USER_ACTIVITY_CODE);
	               	
	    				break;
	    				
	                case MENU_ITEM_DELETE_ACTION:
	                	/** xóa*/
	                	deleteUser(info.code, groupPosition, childPosition);
	                	break;
                }
            }
        });
        
     }
    
    /***
     * 
     */
    @SuppressLint("NewApi")
	@Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int width = metrics.widthPixels; 
        if(android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
        	mExpandableList.setIndicatorBounds(width-GetDipsFromPixel(35), width-GetDipsFromPixel(5));
		} else {
			mExpandableList.setIndicatorBoundsRelative(width-GetDipsFromPixel(35), width-GetDipsFromPixel(5));
        }
    }
    
    @Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		setResult(MasterConstants.RESULT_CLOSE_ALL);
		this.finish();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		//Tam thoi khong closed activity
		//setResult(MasterConstants.RESULT_CLOSE_ALL);
		this.finish();
	}

	/**
     * create context menu
     */
    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == CONTEXT_MENU_ID) {
            return iconContextMenu.createMenu("");
        }
        return super.onCreateDialog(id);
    }
	/**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * onChildClick
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    @Override
	public boolean onChildClick(ExpandableListView parent, View v,
			int groupPosition, int childPosition, long id) {
    	
    	this.groupPosition= groupPosition;
    	this.childPosition =childPosition;
		/** get thông tin của user đang chọn */
		mExpandableListAdapter=mExpandableList.getExpandableListAdapter();
		info = (User)mExpandableListAdapter.getChild(groupPosition, childPosition);

		showDialog(CONTEXT_MENU_ID);

		return true;
	}


	/**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * onActivityResult
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		switch(requestCode){
			case MasterConstants.CALL_USER_ACTIVITY_CODE:
				if (resultCode==RESULT_OK){
					//Bundle bundle = data.getExtras();
					//currentGroup = bundle.getInt(MasterConstants.EXP_USER_GROUP_TAG);
					getParentChildInGroup(currentGroup);
				}
				break;
			case MasterConstants.CALL_SEARCH_ITEM_ACTIVITY_CODE:
				if (resultCode==RESULT_OK){
					getParentChildInGroup(currentGroup);
				}
				break;
			case MasterConstants.CALL_SORT_ITEM_ACTIVITY_CODE:
				if (resultCode==RESULT_OK){
					getParentChildInGroup(currentGroup);
				}
				break;
			case MasterConstants.CALL_HISTORY_USER_ACTIVITY_CODE:
				if (resultCode==RESULT_OK){
					Bundle bundle = data.getExtras();
					if(bundle!=null){
						currentGroup = bundle.getInt(MasterConstants.EXP_USER_GROUP_TAG);
					}else{
						//currentGroup = bundle.getInt(MasterConstants.EXP_USER_GROUP_TAG);
					}
					//get data & display
					getParentChildInGroup(currentGroup);
				}
				break;
		}

	}
	/**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * GetDipsFromPixel
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    public int GetDipsFromPixel(float pixels)
    {
	     // Get the screen's density scale
	     final float scale = getResources().getDisplayMetrics().density;
	     // Convert the dps to pixels, based on density scale
	     return (int) (pixels * scale + 0.5f);
    }
	/**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * tạo menu
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.exp_list_group_menu, menu);
        return true;
    }
	/**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * xử lý của các menu
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
        case R.id.menu_exp_group_dept:
        	getParentChildInGroup(IExpGroup.EXP_GROUP_DEPT);
        	currentGroup = IExpGroup.EXP_GROUP_DEPT;
            return true;
        case R.id.menu_exp_group_team:
        	getParentChildInGroup(IExpGroup.EXP_GROUP_TEAM);
        	currentGroup = IExpGroup.EXP_GROUP_TEAM;
            return true;
        case R.id.menu_exp_group_position:
        	getParentChildInGroup(IExpGroup.EXP_GROUP_POSITION);
        	currentGroup = IExpGroup.EXP_GROUP_POSITION;
            return true;
        case R.id.menu_exp_group_sex:
        	getParentChildInGroup(IExpGroup.EXP_GROUP_SEX);
        	currentGroup = IExpGroup.EXP_GROUP_SEX;
            return true;
        case R.id.menu_exp_group_japanese:
        	getParentChildInGroup(IExpGroup.EXP_GROUP_JAPANESE);
        	currentGroup = IExpGroup.EXP_GROUP_JAPANESE;
            return true;
        case R.id.menu_exp_group_business_kbn:
        	getParentChildInGroup(IExpGroup.EXP_GROUP_BUSINESS_KBN);
        	currentGroup = IExpGroup.EXP_GROUP_BUSINESS_KBN;
            return true;

		case R.id.menu_exp_group_business_allowance:
			getParentChildInGroup(IExpGroup.EXP_GROUP_BUSSINESS_ALLOWANCE);
			currentGroup = IExpGroup.EXP_GROUP_BUSSINESS_ALLOWANCE;
			return true;

            /** 2013.09.16 ADD START */
        case R.id.menu_exp_group_keiken:
        	getParentChildInGroup(IExpGroup.EXP_GROUP_KEIKEN);
        	currentGroup = IExpGroup.EXP_GROUP_KEIKEN;
        	return true;
        case R.id.menu_exp_group_keiken_labor:
        	getParentChildInGroup(IExpGroup.EXP_GROUP_KEIKEN_LABOR);
        	currentGroup = IExpGroup.EXP_GROUP_KEIKEN_LABOR;
        	return true;
        case R.id.menu_exp_group_labour:
        	getParentChildInGroup(IExpGroup.EXP_GROUP_LABOUR_USER);
        	currentGroup = IExpGroup.EXP_GROUP_LABOUR_USER;
        	return true;        	
        case R.id.menu_exp_group_yasumi:
        	getParentChildInGroup(IExpGroup.EXP_GROUP_YASUMI);
        	currentGroup = IExpGroup.EXP_GROUP_YASUMI;
        	return true;
        case R.id.menu_exp_group_yasumi_yearmonth:
        	getParentChildInGroup(IExpGroup.EXP_GROUP_YASUMI_YEARMONTH);
        	currentGroup = IExpGroup.EXP_GROUP_YASUMI_YEARMONTH;
        	return true;
			/** 2013.09.16 ADD END */
        	/** 2013.09.27 ADD START */
        case R.id.menu_exp_group_salary_basic:
        	getParentChildInGroup(IExpGroup.EXP_GROUP_SALARY_BASIC);
        	currentGroup = IExpGroup.EXP_GROUP_SALARY_BASIC;
        	return true;	
        	/** 2013.09.27 ADD END*/
        	
        case R.id.menu_exp_group_customer:
        	getParentChildInGroup(IExpGroup.EXP_GROUP_CUSTOMER);
        	currentGroup = IExpGroup.EXP_GROUP_CUSTOMER;
            return true;
        
        case R.id.menu_exp_group_contract_yearmonth:
        	getParentChildInGroup(IExpGroup.EXP_GROUP_STAFF_CONTRACT_STATUS_YEARMONTH);
        	currentGroup = IExpGroup.EXP_GROUP_STAFF_CONTRACT_STATUS_YEARMONTH;
            return true;
        //2016.12.12    
        case R.id.menu_exp_group_training_year:
        	getParentChildInGroup(IExpGroup.EXP_GROUP_TRAINING_YEAR);
        	currentGroup = IExpGroup.EXP_GROUP_TRAINING_YEAR;
            return true;
        case R.id.menu_exp_group_contract_year:
        	getParentChildInGroup(IExpGroup.EXP_GROUP_CONTRACT_YEAR);
        	currentGroup = IExpGroup.EXP_GROUP_CONTRACT_YEAR;
            return true;
        case R.id.menu_exp_group_notcontract_year:
        	getParentChildInGroup(IExpGroup.EXP_GROUP_NOTCONTRACT_YEAR);
        	currentGroup = IExpGroup.EXP_GROUP_NOTCONTRACT_YEAR;
            return true;
        case R.id.menu_exp_group_contract_less_month:
        	getParentChildInGroup(IExpGroup.EXP_GROUP_CONTRACT_LESS_MONTH);
        	currentGroup = IExpGroup.EXP_GROUP_CONTRACT_LESS_MONTH;
            return true;
		case R.id.menu_exp_group_staff_current_position_not_satified:
			getParentChildInGroup(IExpGroup.EXP_GROUP_STAFF_CURRENT_POSITION_NOT_SATIFIED);
			currentGroup = IExpGroup.EXP_GROUP_STAFF_CURRENT_POSITION_NOT_SATIFIED;
			return true;
		case R.id.menu_exp_group_yasumi_year:
			getParentChildInGroup(IExpGroup.EXP_GROUP_YASUMI_YEAR);
			currentGroup = IExpGroup.EXP_GROUP_YASUMI_YEAR;
			return true;
        case R.id.menu_exp_group_searchitem:
        	Intent intent = new Intent(this, SearchItemMainActivity.class);
			startActivityForResult(intent,MasterConstants.CALL_SEARCH_ITEM_ACTIVITY_CODE);
            return true;
        case R.id.menu_exp_group_sortlist:
        	Intent intSort = new Intent(this, DragNDropListActivity.class);
			startActivityForResult(intSort , MasterConstants.CALL_SORT_ITEM_ACTIVITY_CODE);
            return true;
        case R.id.menu_exp_group_search:
        	if(getSearchViewShowHide(layoutSearch)==View.VISIBLE){
        		setSearchViewShowHide(layoutSearch, View.GONE);
        		setHideKeyboard();
        	}else{
        		setSearchViewShowHide(layoutSearch, View.VISIBLE);
        	}
        	return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }
    
	/**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * Lấy các item trong nhóm
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    public void getParentChildInGroup(int group){

		ArrayList<ExpParent> arrayParents=new ArrayList<ExpParent>() ;
		//get data theo tung group
		arrayParents = ExpParentChildInGroup.getParentChildInGroup(group,getApplicationContext());
		//lay ve cac gia tri
		arrGroup = ExpParentChildInGroup.arrGroupTitle;
		grp = ExpParentChildInGroup.grpExp;
		list = ExpParentChildInGroup.listUser;

		/*
		String[] arrGroupTemp=null;

		arrGroup =grp.getGroup(group);
		arrGroupTemp = copyArray(arrGroup);
		*//** trường hợp là giới tính thì phải setting lại text hiển thị *//*
		if (group == IExpGroup.EXP_GROUP_SEX){
			for (int i=0 ; i<arrGroupTemp.length;i++){
				if(arrGroupTemp[i]==null|| Integer.parseInt( arrGroupTemp[i])==0 ){
					arrGroupTemp[i] ="Nữ";
				}else{
					arrGroupTemp[i] ="Nam";
				}
			}
		}

		*//** trường hợp là nhom labour thì phải setting lại text hiển thị *//*
		if (group == IExpGroup.EXP_GROUP_LABOUR_USER){
			for (int i=0 ; i<arrGroupTemp.length;i++){
				if(arrGroupTemp[i]==null|| Integer.parseInt( arrGroupTemp[i])==0 ){
					arrGroupTemp[i] ="Không thuộc nhóm labor";
				}else{
					arrGroupTemp[i] ="Thành viên nhóm labor";
				}
			}
		}

		*//** trường hợp là nghỉ việc -chưa nghỉ iệc thì phải setting lại text hiển thị *//*
		if (group == IExpGroup.EXP_GROUP_YASUMI){
			for (int i=0 ; i<arrGroupTemp.length;i++){
				if(arrGroupTemp[i]==null|| ( arrGroupTemp[i])=="" ){
					arrGroupTemp[i] ="Đang làm việc";
				}else{
					arrGroupTemp[i] ="Đã nghỉ việc";
				}
			}
		}

		*//** trường hợp là thâm niên thì phải setting lại text hiển thị *//*
		if (group == IExpGroup.EXP_GROUP_KEIKEN){
			for (int i=0 ; i<arrGroupTemp.length;i++){
				if( arrGroupTemp[i]==null|| Integer.parseInt( arrGroupTemp[i])==0){
					arrGroupTemp[i] ="Nhỏ hơn 1 năm";
				}else if (Integer.parseInt(arrGroupTemp[i])==1){
					arrGroupTemp[i] ="1 ～ 2 năm";
				}else if (Integer.parseInt(arrGroupTemp[i])==2){
					arrGroupTemp[i] ="2 ～ 3 năm";
				}else if (Integer.parseInt(arrGroupTemp[i])==3){
					arrGroupTemp[i] ="3 ～4 năm";
				}else if (Integer.parseInt(arrGroupTemp[i])==4){
					arrGroupTemp[i] ="4 ～5 năm";
				}else if (Integer.parseInt(arrGroupTemp[i])==5){
					arrGroupTemp[i] ="Lớn hơn 5 năm";
				}

			}
		}
		*//** trường hợp là thâm niên nhóm labor thì phải setting lại text hiển thị *//*
		if (group == IExpGroup.EXP_GROUP_KEIKEN_LABOR){
			for (int i=0 ; i<arrGroupTemp.length;i++){
				if( arrGroupTemp[i]==null|| Integer.parseInt( arrGroupTemp[i])==0){
					arrGroupTemp[i] ="Nhỏ hơn 1 năm";
				}else if (Integer.parseInt(arrGroupTemp[i])==1){
					arrGroupTemp[i] ="1 ～ 2 năm";
				}else if (Integer.parseInt(arrGroupTemp[i])==2){
					arrGroupTemp[i] ="2 ～ 3 năm";
				}else if (Integer.parseInt(arrGroupTemp[i])==3){
					arrGroupTemp[i] ="3 ～4 năm";
				}else if (Integer.parseInt(arrGroupTemp[i])==4){
					arrGroupTemp[i] ="4 ～5 năm";
				}else if (Integer.parseInt(arrGroupTemp[i])==5){
					arrGroupTemp[i] ="Lớn hơn 5 năm";
				}

			}
		}
		*//** trường hợp là lương thì phải setting lại text hiển thị *//*
		if (group == IExpGroup.EXP_GROUP_SALARY_BASIC){
			for (int i=0 ; i<arrGroupTemp.length;i++){
				if( arrGroupTemp[i]==null|| Integer.parseInt( arrGroupTemp[i])==0){
					arrGroupTemp[i] ="Nhỏ hơn 300$";
				}else if (Integer.parseInt(arrGroupTemp[i])==1){
					arrGroupTemp[i] ="300 ～ 399.9$";
				}else if (Integer.parseInt(arrGroupTemp[i])==2){
					arrGroupTemp[i] ="400 ～ 499.9$";
				}else if (Integer.parseInt(arrGroupTemp[i])==3){
					arrGroupTemp[i] ="500 ～ 599.9$";
				}else if (Integer.parseInt(arrGroupTemp[i])==4){
					arrGroupTemp[i] ="600 ～ 699.9$";
				}else if (Integer.parseInt(arrGroupTemp[i])==5){
					arrGroupTemp[i] ="Lớn hơn 700$";
				}

			}
		}

		*//** trường hợp là chuyên môn thì phải setting lại text hiển thị *//*
		if (group == IExpGroup.EXP_GROUP_BUSINESS_KBN){
			for (int i=0 ; i<arrGroupTemp.length;i++){
				if( arrGroupTemp[i]==null|| Integer.parseInt( arrGroupTemp[i])==0){
					arrGroupTemp[i] ="Chưa chỉ định";
				}else if (Integer.parseInt(arrGroupTemp[i])==1){
					arrGroupTemp[i] ="Lập trình viên";
				}else if (Integer.parseInt(arrGroupTemp[i])==2){
					arrGroupTemp[i] ="Phiên dịch";
				}else if (Integer.parseInt(arrGroupTemp[i])==3){
					arrGroupTemp[i] ="Lập trình viên-Phiên dịch";
				}

			}
		}
		*//** trường hợp số nhân viên thử việc trong năm setting tại system *//*
		if (group == IExpGroup.EXP_GROUP_TRAINING_YEAR){
			for (int i=0 ; i<arrGroupTemp.length;i++){
				if( arrGroupTemp[i]==null|| Integer.parseInt( arrGroupTemp[i])==0){
					arrGroupTemp[i] ="Số LTV thử việc năm " + currentYear;
				}
			}
		}

		*//** trường hợp số nhân viên được nhận chính thức trong năm setting tại system *//*
		if (group == IExpGroup.EXP_GROUP_CONTRACT_YEAR){
			for (int i=0 ; i<arrGroupTemp.length;i++){
				if( arrGroupTemp[i]==null|| Integer.parseInt( arrGroupTemp[i])==0){
					arrGroupTemp[i] ="Số LTV nhận chính thức năm " + currentYear;
				}else if (Integer.parseInt(arrGroupTemp[i])==1){
					arrGroupTemp[i] ="Số LTV nhận chính thức năm " + currentYear+"(thử việc năm trước)";
				}
			}
		}
		*//** trường hợp số nhân viên thử việc nhưng không được nhận*//*
		if (group == IExpGroup.EXP_GROUP_NOTCONTRACT_YEAR){
			for (int i=0 ; i<arrGroupTemp.length;i++){
				if( arrGroupTemp[i]==null|| Integer.parseInt( arrGroupTemp[i])==0){
					arrGroupTemp[i] ="Số LTV không nhận sau thử việc năm " + currentYear;
				}
			}
		}

		*//** trường hợp số nhân viên chính thức có thâm niên nhỏ hơn hoặc bằng N tháng ( setting tại system)*//*
		if (group == IExpGroup.EXP_GROUP_CONTRACT_LESS_MONTH){
			for (int i=0 ; i<arrGroupTemp.length;i++){
				if( arrGroupTemp[i]==null|| Integer.parseInt( arrGroupTemp[i])==0){
					arrGroupTemp[i] ="Số LTV có thâm niên <= " + keikenMonthSystem + " tháng";
				}
			}
		}
		*//** so nhan vien co chuc vu khong phu hop voi tham nien*//*
		if (group == IExpGroup.EXP_GROUP_STAFF_CURRENT_POSITION_NOT_SATIFIED){
			for (int i=0 ; i<arrGroupTemp.length;i++){
				if( arrGroupTemp[i]==null|| Integer.parseInt( arrGroupTemp[i])==0){
					arrGroupTemp[i] ="Số LTV có chức vụ không phù hợp thâm niên";
				}
			}
		}

		*//** trường hợp là thong ke nghi viec theo tung nam *//*
		if (group == IExpGroup.EXP_GROUP_YASUMI_YEAR) {
			for (int i = 0; i < arrGroupTemp.length; i++) {
				if (arrGroupTemp[i] == null || Integer.parseInt(arrGroupTemp[i]) == 0) {
					arrGroupTemp[i] = "Nghỉ việc năm " + (currentYear - 2);
				} else if (Integer.parseInt(arrGroupTemp[i]) == 1) {
					arrGroupTemp[i] = "Nghỉ việc năm " + (currentYear - 1);
				} else if (Integer.parseInt(arrGroupTemp[i]) == 2) {
					arrGroupTemp[i] = "Nghỉ việc năm " + (currentYear);
				}
			}
		}
		ArrayList<ExpParent> arrayParents = new ArrayList<ExpParent>();

		*//** here we set the parents and the children *//*
		for (int i = 0; i < arrGroup.length; i++){
			ArrayList<User> arrayChildren = new ArrayList<User>();
			*//** tạo Object để lưu trữ data tại node cha và con *//*
			ExpParent parent = new ExpParent();
			*//** insert data cho node cha *//*
			if (arrGroupTemp[i]==null){
				if(group == IExpGroup.EXP_GROUP_STAFF_CONTRACT_STATUS_YEARMONTH){
					parent.setTitle("Chưa nhận chính thức");
				}else{
					parent.setTitle("");
				}

			}else{
				parent.setTitle(arrGroupTemp[i].toString());
			}
			*//** insert data cho node con *//*
			if (arrGroupTemp[i]==null){
				list = grp.getChildGroup(group, "");
			}else{
				if( arrGroup[i]==null || arrGroup[i].equals("")){
					list = grp.getChildGroup(group, "");
				}else{
					list = grp.getChildGroup(group, arrGroup[i].toString());
				}

			}

			if (list !=null){
				for(User usr : list){
					arrayChildren.add(usr);
				}
				parent.setArrayChildren(arrayChildren);
				arrayParents.add(parent);
			}
		}*/

		/** gán data */
		mExpAdapter = new ExpAdapter(this,arrayParents);
		mExpandableList.setAdapter(mExpAdapter);
	}
	/**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * copy Array
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    private String[] copyArray(String[] source){
    	String[] des={""};
    	if(source==null){
    		return des;
    	}
    	des=new String[source.length];
    	for(int i=0 ; i<source.length; i++){
    		des[i] = source[i];
    	}
    	return des;
    }
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * onCreateContextMenu
     * xu ly hien thi menu khi long click
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
            ContextMenuInfo menuInfo)  {
    	super.onCreateContextMenu(menu, v, menuInfo);
    	
    	/*ExpandableListView.ExpandableListContextMenuInfo info =(ExpandableListView.ExpandableListContextMenuInfo) menuInfo;
    	int type = ExpandableListView.getPackedPositionType(info.packedPosition);
    	int group = ExpandableListView.getPackedPositionGroup(info.packedPosition);
    	int child = ExpandableListView.getPackedPositionChild(info.packedPosition);
    	*//** tao context menu cho item con trong list *//*
    	if (type==ExpandableListView.PACKED_POSITION_TYPE_CHILD){
    		*//**thong tin cua nhom dang chon *//*
    		//String page = mListStringArray[group][child];
    		menu.setHeaderTitle("");
    	    menu.add(0, MENU_DELETE, 0, getResources().getString(R.string.mdelete));
    	}*/
    }
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * xu ly hien thi menu khi long click
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    @Override
    public boolean onContextItemSelected(MenuItem menuItem) 
    {
      /*ExpandableListContextMenuInfo info = 
        (ExpandableListContextMenuInfo) menuItem.getMenuInfo();

      int groupPos = 0, childPos = 0;

      int type = ExpandableListView.getPackedPositionType(info.packedPosition);
      if (type == ExpandableListView.PACKED_POSITION_TYPE_CHILD) 
      {
        groupPos = ExpandableListView.getPackedPositionGroup(info.packedPosition);
        childPos = ExpandableListView.getPackedPositionChild(info.packedPosition);
      }

      // Pull values from the array we built when we created the list
      String author = mListStringArray[groupPos][0];
      String page = mListStringArray[groupPos][childPos * 3 + 1];
      rowId = Integer.parseInt(mListStringArray[groupPos][childPos * 3 + 3]);

      switch (menuItem.getItemId()) 
      {
      	case MENU_DELETE:
      		deleteUser(0,groupPos,childPos);
      		return true;

        default:
        	return super.onContextItemSelected(menuItem);
      }*/
    	return super.onContextItemSelected(menuItem);
    }
	
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * xóa user dựa vào code user
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
	public void deleteUser(final int code, final int group , final int child){
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
		/** Setting Dialog Title */
        alertDialog.setTitle(getResources().getString(R.string.titleDelete));

        /** Setting Dialog Message */
        alertDialog.setMessage("Bạn có muốn xóa nhân viên này không?");
        
        /** Setting Icon to Dialog*/
        alertDialog.setIcon(R.drawable.ic_button_delete);
        /** Setting Positive "Yes" Button */
        alertDialog.setPositiveButton(getResources().getText(R.string.titleYes), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
            	/** xóa user đã chọn */
            	mDatabaseAdapter.open();
        		mDatabaseAdapter.deleteUserByCode(code);
        		mDatabaseAdapter.close();
        		/** remove ra khỏi list hiện tại */
        		mExpAdapter.removeChild(group, child);
        		mExpAdapter.notifyDataSetChanged();
            }
        });
 
        /** Setting Negative "NO" Button */
        alertDialog.setNegativeButton(getResources().getText(R.string.titleNo), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            dialog.cancel();
            }
        });
 
        /** Showing Alert Message */
        alertDialog.show();
        
	}
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * ẩn/hiển thị search view
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
	private void setSearchViewShowHide(View v , int showHide) {
		layoutSearch.setVisibility(showHide);
	}
	/**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * ẩn bàn phím
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
	private void setHideKeyboard(){
		/*View target = findFocus();
        if (target != null) 
        {
            InputMethodManager mgr = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            mgr.hideSoftInputFromWindow(target.getWindowToken(), 0);
        }*/
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
	}
	
	/**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * nhận về trạng thái của SearchView
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
	private int getSearchViewShowHide(View v ) {
		return layoutSearch.getVisibility();
	}
	
	@Override
	public boolean onClose() {
		// TODO Auto-generated method stub
		mExpAdapter.filterData("");
		return false;
	}

	@Override
	public boolean onQueryTextChange(String newText) {
		// TODO Auto-generated method stub
		mExpAdapter.filterData(newText);
		return false;
	}

	@Override
	public boolean onQueryTextSubmit(String query) {
		// TODO Auto-generated method stub
		return false;
	}
}
