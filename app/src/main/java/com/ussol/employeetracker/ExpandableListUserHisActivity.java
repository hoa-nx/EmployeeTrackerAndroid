package com.ussol.employeetracker;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.ussol.employeetracker.helpers.ConvertCursorToListString;
import com.ussol.employeetracker.helpers.DatabaseAdapter;
import com.ussol.employeetracker.helpers.ExpGroupUserHisHelper;
import com.ussol.employeetracker.helpers.ExpParent;
import com.ussol.employeetracker.helpers.ExpAdapter;
import com.ussol.employeetracker.helpers.ExpGroupHelper;
import com.ussol.employeetracker.helpers.ExpUserHisAdapter;
import com.ussol.employeetracker.helpers.IconContextMenu;
import com.ussol.employeetracker.models.Dept;
import com.ussol.employeetracker.models.IExpGroup;
import com.ussol.employeetracker.models.MasterConstants;
import com.ussol.employeetracker.models.Position;
import com.ussol.employeetracker.models.Team;
import com.ussol.employeetracker.models.User;
import com.ussol.employeetracker.models.UserHistory;
import com.ussol.employeetracker.utils.DateTimeUtil;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ExpandableListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.Window;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;

public class ExpandableListUserHisActivity extends Activity implements OnChildClickListener  {
	private DatabaseAdapter mDatabaseAdapter;
	private ExpandableListView mExpandableList;
	private ExpGroupUserHisHelper  grp =null;
	private String[] arrGroup=null;
	private List<UserHistory> list;
	private DisplayMetrics metrics;
	private int width;
	private static  int currentGroup=IExpGroup.EXP_GROUP_HISTORY;
	private UserHistory info =null;
	private int user_code=0;
	private ExpandableListAdapter mExpandableListAdapter;
	private ExpUserHisAdapter mExpUserHisAdapter;
	private ConvertCursorToListString mConvertCursorToListString;
	private User usr=null;
	private static final int MENU_ITEM_ADDCOPY_ACTION = 1;
	private static final int MENU_ITEM_DETAIL_ACTION = 2;
	private static final int MENU_ITEM_EDIT_ACTION = 3;
	private static final int MENU_ITEM_DELETE_ACTION = 4;
	private static final int MENU_ITEM_HISTORY_ACTION = 5;
	private static final int CONTEXT_MENU_ID = 410;
	private IconContextMenu iconContextMenu = null;
	private int groupPosition , childPosition;
	
	/**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * onCreate
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    @SuppressLint("NewApi")
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //mo ket noi SQLite
        mDatabaseAdapter = new DatabaseAdapter(getBaseContext());
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        mConvertCursorToListString = new ConvertCursorToListString(this);
        setContentView(R.layout.expandable_list_view_user_main);
        mExpandableList = (ExpandableListView)findViewById(R.id.expandable_list);
        metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        int width = metrics.widthPixels; 
        if(android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
        	mExpandableList.setIndicatorBounds(width-GetDipsFromPixel(35), width-GetDipsFromPixel(5));
		} else {
			mExpandableList.setIndicatorBoundsRelative(width-GetDipsFromPixel(35), width-GetDipsFromPixel(5));
        }
        
        /** get user code từ màn hình nguồn gọi */
        Intent request = getIntent();
  		Bundle param = request.getExtras();
  		if (param != null) {
			user_code =param.getInt(DatabaseAdapter.KEY_CODE);
			List<User> listUser = mConvertCursorToListString.getUserList(" AND " + DatabaseAdapter.KEY_CODE+" = " + user_code);
			if(listUser.size()>0){
				usr= listUser.get(0);
				usr.isselected=true;
				setTitle(usr.full_name);
			}
  		}
        grp = new ExpGroupUserHisHelper(getApplicationContext());
        getParentChildInGroup(IExpGroup.EXP_GROUP_HISTORY);
        
        mExpandableList.setOnChildClickListener(this);
        mExpandableList.setOnCreateContextMenuListener(this);
        /**init the menu*/
        Resources res = getResources();
        iconContextMenu = new IconContextMenu(this, CONTEXT_MENU_ID);
        iconContextMenu.addItem(res, R.string.medit, R.drawable.edit, MENU_ITEM_EDIT_ACTION);
        iconContextMenu.addItem(res, R.string.mdelete, R.drawable.delete, MENU_ITEM_DELETE_ACTION);
 
        //set onclick listener for context menu
        iconContextMenu.setOnClickListener(new IconContextMenu.IconContextMenuOnClickListener() {
            @Override
            public void onClick(int menuId) {
                switch(menuId) {
	                case MENU_ITEM_EDIT_ACTION:

	            		/**  chỉnh sửa */
	            		Intent intent = new Intent(getApplicationContext(), HisUserMainActivity.class);
	            		Bundle bundle = new Bundle();
	            		/** get thông tin của user đang chọn */
	            		mExpandableListAdapter=mExpandableList.getExpandableListAdapter();
	            		info = (UserHistory)mExpandableListAdapter.getChild(groupPosition, childPosition);
	            		/**lấy code của user*/
	            		bundle.putInt(DatabaseAdapter.KEY_CODE, info.code);
	            		/** get thong tin cua user */
	            		/*List<User> listUser = mConvertCursorToListString.getUserList(" AND " + DatabaseAdapter.KEY_CODE+" = " + info.user_code);
	            		if(listUser.size()>0){
	            			usr= listUser.get(0);
	            			usr.isselected=true;
	            		}*/
	            		bundle.putParcelable(MasterConstants.TAB_USER_TAG, usr);
	            		bundle.putParcelable(MasterConstants.TAB_USER_HIS_TAG, info);
	            		/** thông tin lưu trữ nhóm đang chọn*/
	            		bundle.putInt(MasterConstants.EXP_USER_GROUP_TAG,IExpGroup.EXP_GROUP_HISTORY);
	            		
	            		/**gán vào bundle để gửi cùng với intent */
	            		intent.putExtras(bundle);
	            		
	            		/**khởi tạo activity dùng để edit  */
	            		startActivityForResult(intent , MasterConstants.CALL_USER_HIS_ACTIVITY_CODE);
	            		
	    				break;
	                case MENU_ITEM_DELETE_ACTION:
	                	/** xóa*/
	                	/** get thông tin của user đang chọn */
	            		mExpandableListAdapter=mExpandableList.getExpandableListAdapter();
	            		info = (UserHistory)mExpandableListAdapter.getChild(groupPosition, childPosition);
	                	deleteHisInfo(info.user_code,info.code, groupPosition, childPosition);
	                	break;
                }
            }
        });
        
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
    @SuppressWarnings("deprecation")
	@Override
	public boolean onChildClick(ExpandableListView parent, View v,
			int groupPosition, int childPosition, long id) {
    	/*get vi tri group */
    	this.groupPosition= groupPosition;
    	/*get vi tri item child trong group*/
    	this.childPosition =childPosition;
    	/*
    	*//**  chỉnh sửa *//*
		Intent intent = new Intent(getApplicationContext(), HisUserMainActivity.class);
		Bundle bundle = new Bundle();
		
		*//** get thông tin của user đang chọn *//*
		mExpandableListAdapter=mExpandableList.getExpandableListAdapter();
		info = (UserHistory)mExpandableListAdapter.getChild(groupPosition, childPosition);
		*//**lấy code của user*//*
		bundle.putInt(DatabaseAdapter.KEY_CODE, info.code);
		*//** get thong tin cua user *//*
		*//**List<User> listUser = mConvertCursorToListString.getUserList(" AND " + DatabaseAdapter.KEY_CODE+" = " + info.user_code);
		if(listUser.size()>0){
			usr= listUser.get(0);
			usr.isselected=true;
		}*//*
		bundle.putParcelable(MasterConstants.TAB_USER_TAG, usr);
		bundle.putParcelable(MasterConstants.TAB_USER_HIS_TAG, info);
		*//** thông tin lưu trữ nhóm đang chọn*//*
		bundle.putInt(MasterConstants.EXP_USER_GROUP_TAG,IExpGroup.EXP_GROUP_HISTORY);
		
		*//**gán vào bundle để gửi cùng với intent *//*
		intent.putExtras(bundle);
		
		*//**khởi tạo activity dùng để edit  *//*
		startActivityForResult(intent , MasterConstants.CALL_USER_HIS_ACTIVITY_CODE);
		*/
    	/** get thông tin của user đang chọn */
		mExpandableListAdapter=mExpandableList.getExpandableListAdapter();
		info = (UserHistory)mExpandableListAdapter.getChild(groupPosition, childPosition);

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
			case MasterConstants.CALL_USER_HIS_ACTIVITY_CODE:
				if (resultCode==RESULT_OK){
					Bundle bundle = data.getExtras();
					currentGroup = bundle.getInt(MasterConstants.EXP_USER_GROUP_TAG);
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
        inflater.inflate(R.menu.exp_list_user_his_group_menu, menu);
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
        case R.id.menu_exp_group_searchitem:
        	Intent intent = new Intent(this, SearchItemMainActivity.class);
			startActivityForResult(intent,MasterConstants.CALL_SEARCH_ITEM_ACTIVITY_CODE);
            return true;
        case R.id.menu_exp_group_sortlist:
        	Intent intSort = new Intent(this, DragNDropListActivity.class);
			startActivityForResult(intSort , MasterConstants.CALL_SORT_ITEM_ACTIVITY_CODE);
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
    	String[] arrGroupTemp=null;
    	
    	arrGroup =grp.getGroup(group);
    	arrGroupTemp = copyArray(arrGroup);
    	
        ArrayList<ExpParent> arrayParents = new ArrayList<ExpParent>();
        
        /** here we set the parents and the children */
        for (int i = 0; i < arrGroup.length; i++){
    		ArrayList<UserHistory> arrayChildren = new ArrayList<UserHistory>();
    		/** tạo Object để lưu trữ data tại node cha và con */
    		ExpParent parent = new ExpParent();
    		/** insert data cho node cha */
    		if (arrGroupTemp[i]==null){
    			parent.setTitle("");
    		}else{
    			parent.setTitle(arrGroupTemp[i].toString());
    		}
    		/** insert data cho node con */
    		int tmpGroup=-1 ;
    		if(arrGroup[i]==MasterConstants.HIS_DEPT_NAME){
    			tmpGroup= MasterConstants.MASTER_MKBN_DEPT_HIS;
    		}else if(arrGroup[i]==MasterConstants.HIS_TEAM_NAME){
    			tmpGroup= MasterConstants.MASTER_MKBN_TEAM_HIS;
    		}else if(arrGroup[i]==MasterConstants.HIS_POSITION_NAME){
    			tmpGroup= MasterConstants.MASTER_MKBN_POSITION_HIS;
    		}else if(arrGroup[i]==MasterConstants.HIS_JAPANESE_NAME){
    			tmpGroup= MasterConstants.MASTER_MKBN_JAPANESE_HIS;
    		}else if(arrGroup[i]==MasterConstants.HIS_ALLOWANCE_BUSINESS_NAME){
    			tmpGroup= MasterConstants.MASTER_MKBN_ALLOWANCE_BUSINESS_HIS;
    		}else if(arrGroup[i]==MasterConstants.HIS_SALARY_NAME){
    			tmpGroup= MasterConstants.MASTER_MKBN_SALARY_HIS;
    		}
    		
    		list = grp.getChildUserHisGroup(tmpGroup, String.valueOf( user_code));
			if (list !=null){
            	for(UserHistory usr : list){
	            	arrayChildren.add(usr);
	            }	            
        	parent.setArrayChildrenUserHis(arrayChildren);
        	arrayParents.add(parent);
            }
        }
        
        /** gán data */
        /*mExpandableList.setAdapter(new ExpUserHisAdapter(this,arrayParents));*/
        mExpUserHisAdapter = new ExpUserHisAdapter(this,arrayParents);
        mExpandableList.setAdapter(mExpUserHisAdapter);
    }
	/**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * copy Array
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    private String[] copyArray(String[] source){
    	String[] des;
    	des=new String[source.length];
    	for(int i=0 ; i<source.length; i++){
    		des[i] = source[i];
    	}
    	return des;
    }
    
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * xóa thong tin lich su 
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
	public void deleteHisInfo(final int user_code ,final int code, final int group , final int child){
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
		/** Setting Dialog Title */
        alertDialog.setTitle(getResources().getString(R.string.titleDelete));

        /** Setting Dialog Message */
        alertDialog.setMessage("Bạn có muốn xóa thông tin này không?");
        
        /** Setting Icon to Dialog*/
        alertDialog.setIcon(R.drawable.ic_button_delete);
        /** Setting Positive "Yes" Button */
        alertDialog.setPositiveButton(getResources().getText(R.string.titleYes), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
            	/** xóa thong tin lich su đã chọn */
            	mDatabaseAdapter.open();
        		mDatabaseAdapter.deleteUserHisByCodeRecord(code);
        		mDatabaseAdapter.close();
        		
        		/** chinh sua lai data lich su*/
        		switch (groupPosition){
        		case 0:/** lich su dept */
        			/** chinh sua lai data lich su phong ban*/
            		correctHisData(MasterConstants.MASTER_MKBN_DEPT_HIS, code);
        			break;
        		case 1:/** lich su nhom */
        			/** chinh sua lai data lich su team -nhom */
            		correctHisData(MasterConstants.MASTER_MKBN_TEAM_HIS, code);
        			break;
        		case 2:/** lich su team */
        			/** chinh sua lai data lich su chuc vu*/
            		correctHisData(MasterConstants.MASTER_MKBN_POSITION_HIS, code);
        			break;
        		case 3:/** lich su tieng nhat */
        			/** chinh sua lai data lich su tieng Nhat*/
            		correctHisData(MasterConstants.MASTER_MKBN_JAPANESE_HIS, code);
        			break;
        		case 4:/** lich su tro cap nghiep vu */
        			/** chinh sua lai data lich su tro cap nghiep vu*/
            		correctHisData(MasterConstants.MASTER_MKBN_ALLOWANCE_BUSINESS_HIS, code);
        			break;
        		case 5:/** lich su salary */
        			/** chinh sua lai data lich su salary*/
            		correctHisData(MasterConstants.MASTER_MKBN_SALARY_HIS, code);
        			break;
        		}
        		
        		/** update lai data cua master nhan vien m_user de cap nhat cac thay doi moi nhat tu table lich su */
        		updateUserMaster(user_code,groupPosition);
        		/** remove ra khỏi list hiện tại */
        		mExpUserHisAdapter.removeChild(group, child);
        		mExpUserHisAdapter.notifyDataSetChanged();
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
     * 
     * cập nhật lại thông tin cho master nhân viên ( can thiet gom lai vi nhieu man hinh su dung)
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    public void updateUserMaster(int user_code , int group_update){
    	/** cap nhat lai cac thong tin phong ban -team-chuc vu moi nhat cho user */
    	try{
    		List<User> userList = mConvertCursorToListString.getUserList(" AND " + DatabaseAdapter.KEY_CODE + "=" + user_code);
        	if(userList.size()>0){
        		User usr = userList.get(0);
        		/** chinh sua lai data lich su*/
        		switch (group_update){
        		case 0:/** lich su dept */
        			/** update */
            		/**update phong ban */
        			Dept dept = getNewestUserHisDeptInfo(user_code);
        			usr.dept =dept.code;
            		usr.dept_name = dept.name;
        			break;
        		case 1:/** lich su nhom */
        			/**update team nhom*/
        			Team team = getNewestUserHisTeamInfo(user_code);
        			usr.team = team.code;
            		usr.team_name = team.name;
        			
        			break;
        		case 2:/** lich su team */
        			/**update chuc vu*/
        			Position position= getNewestUserHisPositionInfo(user_code);
        			usr.position = position.code;
            		//usr.position_name = position.name;
            		usr.position_name = position.ryaku;
            		        		       					
        			break;
        		case 3:/** lich su tieng nhat */
        			/**update chung chi tieng Nhat */
    				String japaneseNew = getNewestUserHisJapaneseInfo(user_code);
            		usr.japanese = japaneseNew;
        			break;
        		case 4:/** lich su tro cap nghiep vu */
        			/**update phu cap nghiep vu */
            		String allowanceNew = getNewestUserHisAllowanceBusinessInfo(user_code);
            		usr.allowance_business = allowanceNew;
            		break;	
        		case 5:/** lich su salary*/
        			/**update salary */
            		float salaryNew = getNewestUserHisSalaryInfo(user_code);
            		usr.salary_notallowance = salaryNew;
            		break;	
        			}
        		/** update xuong DB */
        		mDatabaseAdapter.open();
        		mDatabaseAdapter.editToUserTable(usr);
        		mDatabaseAdapter.close();
        		}
       		
        	}
    	catch(Exception e){
    		/*Dong ket noi*/
    		mDatabaseAdapter.close();
    	}
    }
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * 
     * get thông tin về code phòng ban mới nhất của nhân viên
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    public  Dept getNewestUserHisDeptInfo(int user_code ){
    	String xWhere ="";
    	String xOrderBy ="";
    	Dept dept=null;
    	dept= new Dept();
    	
    	xWhere = " AND " + DatabaseAdapter.KEY_USER_CODE + " = " + user_code; 
    	xOrderBy = DatabaseAdapter.KEY_DATE_FROM + " DESC ";
    	/** get danh sách các data lịch sử của user */
    	List<UserHistory> userhis = mConvertCursorToListString.getUserHisList(MasterConstants.MASTER_MKBN_DEPT_HIS, xWhere,xOrderBy);
    	if(userhis.size()==0){
    	}else{
    		dept.code = userhis.get(0).new_dept_code;
    		dept.name = userhis.get(0).new_dept_name;
    	}
    	
    	return dept;
    }
    
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * 
     * get thông tin về code nhóm mới nhất của nhân viên
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    public Team getNewestUserHisTeamInfo(int user_code ){
    	String xWhere ="";
    	String xOrderBy ="";
    	Team team=null;
    	team= new Team();
    	
    	xWhere = " AND " + DatabaseAdapter.KEY_USER_CODE + " = " + user_code; 
    	xOrderBy = DatabaseAdapter.KEY_DATE_FROM + " DESC ";
    	/** get danh sách các data lịch sử của user */
    	List<UserHistory> userhis = mConvertCursorToListString.getUserHisList(MasterConstants.MASTER_MKBN_TEAM_HIS, xWhere,xOrderBy);
    	if(userhis.size()==0){
    	}else{
    		team.code = userhis.get(0).new_team_code;
    		team.name = userhis.get(0).new_team_name;
    	}
    	
    	return team;
    }
    
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * 
     * get thông tin về code chức vụ mới nhất của nhân viên
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    public Position getNewestUserHisPositionInfo(int user_code ){
    	String xWhere ="";
    	String xOrderBy ="";
    	Position position=null;
    	position= new Position();
    	
    	xWhere = " AND " + DatabaseAdapter.KEY_USER_CODE + " = " + user_code; 
    	xOrderBy = DatabaseAdapter.KEY_DATE_FROM + " DESC ";
    	/** get danh sách các data lịch sử của user */
    	List<UserHistory> userhis = mConvertCursorToListString.getUserHisList(MasterConstants.MASTER_MKBN_POSITION_HIS, xWhere,xOrderBy);
    	if(userhis.size()==0){
    	}else{
    		position.code = userhis.get(0).new_position_code;
    		position.name = userhis.get(0).new_position_name;
    	}
    	
    	return position;
    }
    
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * 
     * get thông tin về bang cap tieng Nhat mới nhất của nhân viên
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    public String getNewestUserHisJapaneseInfo(int user_code ){
    	String xWhere ="";
    	String xOrderBy ="";
    	String kekka =""; 
    	
    	xWhere = " AND " + DatabaseAdapter.KEY_USER_CODE + " = " + user_code; 
    	xOrderBy = DatabaseAdapter.KEY_DATE_FROM + " DESC ";
    	/** get danh sách các data lịch sử của user */
    	List<UserHistory> userhis = mConvertCursorToListString.getUserHisList(MasterConstants.MASTER_MKBN_JAPANESE_HIS, xWhere,xOrderBy);
    	if(userhis.size()==0){
    	}else{
    		kekka = userhis.get(0).new_japanese;
    	}
    	
    	return kekka;
    }
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * 
     * get thông tin về phu cap nghiep vu mới nhất của nhân viên
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    public String getNewestUserHisAllowanceBusinessInfo(int user_code ){
    	String xWhere ="";
    	String xOrderBy ="";
    	String kekka =""; 
    	
    	xWhere = " AND " + DatabaseAdapter.KEY_USER_CODE + " = " + user_code; 
    	xOrderBy = DatabaseAdapter.KEY_DATE_FROM + " DESC ";
    	/** get danh sách các data lịch sử của user */
    	List<UserHistory> userhis = mConvertCursorToListString.getUserHisList(MasterConstants.MASTER_MKBN_ALLOWANCE_BUSINESS_HIS, xWhere,xOrderBy);
    	if(userhis.size()==0){
    	}else{
    		kekka = userhis.get(0).new_allowance_business;
    	}
    	
    	return kekka;
    }
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * 
     * get thông tin về salary mới nhất của nhân viên
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    public float getNewestUserHisSalaryInfo(int user_code ){
    	String xWhere ="";
    	String xOrderBy ="";
    	float kekka =0; 
    	
    	xWhere = " AND " + DatabaseAdapter.KEY_USER_CODE + " = " + user_code; 
    	xOrderBy = DatabaseAdapter.KEY_DATE_FROM + " DESC ";
    	/** get danh sách các data lịch sử của user */
    	List<UserHistory> userhis = mConvertCursorToListString.getUserHisList(MasterConstants.MASTER_MKBN_SALARY_HIS, xWhere,xOrderBy);
    	if(userhis.size()==0){
    	}else{
    		kekka = userhis.get(0).yobi_real1;
    	}
    	
    	return kekka;
    }
	/**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * 
     * chinh sua lai data cho dung theo nhu trinh tu cua thang nam thay doi
     * (Copy tu xu ly tai man hinh update thong tin lich su cua nhan vien )
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    @SuppressWarnings("deprecation")
	public void correctHisData(int type,int user_code){
    	String xWhere="";
    	String xOrderBy="";
    	
    	xWhere = " AND " + DatabaseAdapter.KEY_USER_CODE + " = " + user_code; 
    	xOrderBy = DatabaseAdapter.KEY_DATE_FROM + " ASC ";
    	/** get danh sách các data lịch sử của user */
    	List<UserHistory> userhis = mConvertCursorToListString.getUserHisList(type, xWhere,xOrderBy);
    	if(userhis.size()==0){
    		//khong co du lieu
    		
    	} else if(userhis.size()==1){
    		//chi co 1 dong nên không cần update lại ngày end
    		
    	} else if(userhis.size()> 1){
    		UserHistory hisnext;
    		for(int i=0 ; i<userhis.size();i++){
    			if(i+1 <userhis.size()){
    				hisnext = userhis.get(i+1);
    			}else{
    				hisnext=null;
    			}
    			if(hisnext==null){
    				userhis.get(i).date_to ="";
    			}else{
    				Date dt = DateTimeUtil.convertStringToDate(hisnext.date_from, MasterConstants.DATE_VN_FORMAT);
    				Calendar cal = Calendar.getInstance(); 
    				cal.set(dt.getYear()+ 1900, dt.getMonth()+1, dt.getDate());
    				
    				cal.add(Calendar.DATE, -1);
    				String date =DateTimeUtil.convertDateToString(cal.getTime(),MasterConstants.DATE_VN_FORMAT);
    				
    				userhis.get(i).date_to =hisnext.date_from;
    			}
    			try{
    				mDatabaseAdapter.open();
        			mDatabaseAdapter.editToUserHisTable(userhis.get(i));
        			mDatabaseAdapter.close();
    			}catch(Exception e){
    				mDatabaseAdapter.close();
    			}
    		}
    	}
    }
}
