/**
 * Copyright (c) 2012 Fujinet Software Department 5 ( USSOL team)  
 * See the file license.txt for copying permission.
*/  
package com.ussol.employeetracker;

import java.util.ArrayList;
import java.util.List;

import com.ussol.employeetracker.helpers.ConvertCursorToListString;
import com.ussol.employeetracker.helpers.DatabaseAdapter;
import com.ussol.employeetracker.models.Dept;
import com.ussol.employeetracker.models.MasterConstants;
import com.ussol.employeetracker.models.SharedVariables;
import com.ussol.employeetracker.models.Team;

import android.app.AlertDialog;
import android.app.ListFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.AdapterView.AdapterContextMenuInfo;


public class ListTeamFragment extends ListFragment {
	View view =null;
	/** DB adapter */
	DatabaseAdapter mDatabaseAdapter;
	TeamAdapter adapter ;
	/** List  data  */
	List<Team> list;
	ConvertCursorToListString mConvertCursorToListString;
	/** tag */
	private final static String TAG =ListTeamFragment.class.getName();
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * onCreate
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/** gán data cho list*/
		mDatabaseAdapter = new DatabaseAdapter(getActivity());
		bindData();
		/** lưu trạng thái của Fragment */
		//Tam thoi comment do khong hoat dong tai Android 5.0 START
		//setRetainInstance(true);
	}

	/**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * onConfigurationChanged
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
		//lưu màn hình ngang hay dọc
		SharedVariables.CURRENT_ORIENTATION = getResources().getConfiguration().orientation ;
	}
	/**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * onActivityCreated
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		if(savedInstanceState!=null)
		{
			super.onSaveInstanceState(savedInstanceState);
		}
		SharedVariables.CURRENT_ORIENTATION = getResources().getConfiguration().orientation ;

    	/**  Add new button event */
        
		Button btn = (Button)getView().findViewById(R.id.btnTeamAdd);
		
		btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
					/** tạo mới nhóm */
					Intent intentNew = new Intent(getActivity(), EditTeamActivity.class);
					Bundle bundleNew = new Bundle();
					/**lấy code của nhóm */
					bundleNew.putInt(DatabaseAdapter.KEY_CODE, -1);
					/**gán vào bundle để gửi cùng với intent */
					intentNew.putExtras(bundleNew);
					/**khởi tạo activity dùng để edit  */
					startActivityForResult(intentNew , MasterConstants.CALL_TEAM_ACTIVITY_CODE);
				
			}
		});
	
		if (getListView().getCount()>0){
			getListView().setSelection(0);
			if (SharedVariables.CURRENT_ORIENTATION==Configuration.ORIENTATION_LANDSCAPE){
				int first_pos =getListView().getFirstVisiblePosition();
				Team item = (Team) getListAdapter().getItem(first_pos);
				setListviewSelection(getListView(), first_pos);
				updateDetail(item);
			}
		}
		
		/** đăng ký menu context vào list*/
        registerForContextMenu(getListView());
      
	}
	
	/**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * onCreateView
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		try{
			View v = getView();
			//if (v==null){
				view = inflater.inflate(R.layout.fragment_teamlist_list,container, false);
			//}

			
		}catch(Exception e ){
			Log.v("ListDepartmentFragment",e.getMessage());
		}
	
		return view;
	}
	
	/**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * onListItemClick
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		Team item = (Team) getListAdapter().getItem(position);
		
		updateDetail(item);
	}
	/**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * binding data cho list
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
	public void bindData(){
		/** khởi tạo list */
		list = new ArrayList<Team>();
		/** chuyển đổi từ Cursor thành List */
		mConvertCursorToListString = new ConvertCursorToListString(getActivity());
		list = mConvertCursorToListString.getTeamList("");
		/** tạo adapter để hiển thị list data  */
		adapter = new TeamAdapter(getActivity(),android.R.layout.simple_list_item_1, list);
		/** gán data */
		setListAdapter(adapter);
	}
	/**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * cập nhật data cho màn hình chi tiết
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
	private void updateDetail(Team item){
		try{
			DetailTeamFragment fragment = (DetailTeamFragment) getFragmentManager()
					.findFragmentById(R.id.detailTeamFragment);
			if (fragment != null  && fragment.isInLayout()) {
				fragment.setTeamCode(String.valueOf(item.code));
				fragment.setTeamName(item.name);
				fragment.setTeamDept(item.yobi_text1);
				fragment.setTeamLeader(item.yobi_text2);
				fragment.setTeamNote(item.note);
			}else {
				Intent intent = new Intent(getActivity().getApplicationContext(),DetailTeamActivity.class);
				Bundle bundle = new Bundle();
				bundle.putInt(DatabaseAdapter.KEY_CODE, item.code);
				intent.putExtras(bundle);
				startActivity(intent);
			}
			
		}catch (Exception e){
			Log.v("updateDetail",e.getMessage());
		}
		
	}
	/**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * hiển thị data tại chi tiết 
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
	@Override
	public void onResume() {
		super.onResume();
		SharedVariables.CURRENT_ORIENTATION = getResources().getConfiguration().orientation ;
		if (getListView().getCount()>0){
			getListView().setSelection(0);
			if (SharedVariables.CURRENT_ORIENTATION==Configuration.ORIENTATION_LANDSCAPE){
				int first_pos =getListView().getFirstVisiblePosition();
				Team item = (Team) getListAdapter().getItem(first_pos);
				setListviewSelection(getListView(), first_pos);
				updateDetail(item);
			}
		}
	}
	/**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * setting màu cho item đã được selected
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
	private void setListviewSelection(final ListView list, final int pos) {
		list.post(new Runnable() 
		   {
		    @Override
		    public void run() 
		      {
		        list.setSelection(pos);
		        View v = list.getChildAt(pos);
		        if (v != null) 
		        {
		            v.requestFocus();
		            //setting màu
		            v.setBackgroundResource(R.drawable.list_item_selector);
		        }
		    }
		});
		}
	/**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * onCreateContextMenu
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,   ContextMenuInfo menuInfo){
		if (v.getId()==android.R.id.list) {
			super.onCreateContextMenu(menu, v, menuInfo);
		    //AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
		    //menu.setHeaderTitle("Phòng ban");
		    MenuInflater inflater = getActivity().getMenuInflater();
		    inflater.inflate(R.menu.team_context_menu, menu);
		  }
	}
	
	/**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * onContextItemSelected
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		/** vị trí của item trên list*/
		Team detail = (Team) getListAdapter().getItem(info.position);
		
		switch (item.getItemId()) {
		  case R.id.teamaddcopy:
			  /** tạo mới team từ team hiện tại*/
			  /**  chỉnh sửa */
				Intent intentNew = new Intent(getActivity(), EditTeamActivity.class);
				Bundle bundleNew = new Bundle();
				/**lấy code của phòng ban */
				bundleNew.putInt(DatabaseAdapter.KEY_CODE, 0);
				bundleNew.putParcelable(MasterConstants.TAB_TEAM_TAG, detail);
				/**gán vào bundle để gửi cùng với intent */
				intentNew.putExtras(bundleNew);
				/**khởi tạo activity dùng để edit  */
				startActivityForResult(intentNew , MasterConstants.CALL_TEAM_ACTIVITY_CODE);
				break;
		  case R.id.teamdetail:
			  /** xem thông tin chi tiết */
			  /**  chỉnh sửa */
				Intent intentDetail = new Intent(getActivity(), DetailTeamActivity.class);
				Bundle bundleDetail = new Bundle();
				/**lấy code của phòng ban */
				bundleDetail.putInt(DatabaseAdapter.KEY_CODE, detail.code);
				/**gán vào bundle để gửi cùng với intent */
				intentDetail.putExtras(bundleDetail);
				/**khởi tạo activity dùng để edit  */
				startActivity(intentDetail);
				break;
		  case R.id.teamedit:
			  /**  chỉnh sửa */
				Intent intent = new Intent(getActivity(), EditTeamActivity.class);
				Bundle bundle = new Bundle();
				/**lấy code của phòng ban */
				bundle.putInt(DatabaseAdapter.KEY_CODE, detail.code);
				/**gán vào bundle để gửi cùng với intent */
				intent.putExtras(bundle);
				/**khởi tạo activity dùng để edit  */
				startActivityForResult(intent , MasterConstants.CALL_TEAM_ACTIVITY_CODE);
				break;
		  case R.id.teamdelete:
			  /** xóa */
			  deleteData(detail.code);
			  break;
		  default:
		    return super.onContextItemSelected(item);
		}
		return true;
	}

	/**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * onActivityResult
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
	@SuppressWarnings("static-access")
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch(requestCode){
			case MasterConstants.CALL_TEAM_ACTIVITY_CODE:
				if (resultCode==getActivity().RESULT_OK){
					/** refresh lai data*/
					bindData();
				}
		}
	}	
	/**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * xóa phòng ban dựa vào code
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/	
	private void deleteData(final int  code ){
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
		/** Setting Dialog Title */
        alertDialog.setTitle(getResources().getString(R.string.titleDelete));

        /** Setting Dialog Message */
        alertDialog.setMessage("Bạn có muốn xóa nhóm này không?");
        
        /** Setting Icon to Dialog*/
        alertDialog.setIcon(R.drawable.ic_button_delete);
        /** Setting Positive "Yes" Button */
        alertDialog.setPositiveButton(getResources().getText(R.string.titleYes), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
            	mDatabaseAdapter.open();
            	mDatabaseAdapter.deleteTeamByCode(code);
            	bindData();
            	mDatabaseAdapter.close();
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
	
	
}