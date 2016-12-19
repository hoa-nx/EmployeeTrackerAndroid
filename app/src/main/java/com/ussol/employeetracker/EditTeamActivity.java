/**
 * Copyright (c) 2012 Fujinet Software Department 5 ( USSOL team)  
 * See the file license.txt for copying permission.
*/  
package com.ussol.employeetracker;

import java.util.List;

import com.ussol.employeetracker.helpers.ConvertCursorToArrayString;
import com.ussol.employeetracker.helpers.ConvertCursorToListString;
import com.ussol.employeetracker.helpers.DatabaseAdapter;
import com.ussol.employeetracker.helpers.GetListDataHelper;
import com.ussol.employeetracker.models.Dept;
import com.ussol.employeetracker.models.MasterConstants;
import com.ussol.employeetracker.models.Position;
import com.ussol.employeetracker.models.SharedVariables;
import com.ussol.employeetracker.models.Team;
import com.ussol.employeetracker.models.User;
import com.ussol.employeetracker.helpers.AlertDialogRadio;
import com.ussol.employeetracker.helpers.AlertDialogRadio.AlertPositiveListener;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class EditTeamActivity extends Activity  implements OnClickListener ,AlertPositiveListener  {
	/** tag */
	private final static String TAG =EditTeamActivity.class.getName();
	/** chuyển đổi Cursor thành List */
	ConvertCursorToListString mConvertCursorToListString;
	/** chuyển đổi Cursor thành string array */
	ConvertCursorToArrayString mConvertCursorToArrayString;
	/** Lưu trữ control code nhóm*/
	TextView txtCode , txtTeamLeaderCode, txtDeptCode ;
	/** Lưu trữ control tên ...nhóm */
	EditText txtName, txtTeamLeader, txtDept , txtNote ;
	/** Trị code nhóm  */
	private int nCode = 0;
	/** Lưu các button trên màn hình  */
	Button btnSave, btnCancel , btnDeptSearch, btnLeaderSearch  ;
	/** Lưu lại vị trí mà đã chọn từ list radio */
	int positionDept = 0;
	int positionTeamLeader = 0;
	/** thông tin các user hiển thị tại màn hình dialog  */
	String[] dataUser = new String[]{"Nguyễn Minh Hải","Dương Thái Trung"}  ;
	User[] _users=null;
	List<User> _listUser =null;
	/** thông tin các phòng ban hiển thị tại màn hình dialog  */
	String[] dataDept ;
	Dept[] _depts=null;
	List<Dept> _listDept =null;
	/** thông tin các nhóm hiển thị tại màn hình dialog  */
	String[] dataTeam ;
	Team[] _teams=null;
	List<Team> _listTeam =null;
	/** thông tin các chức vụ hiển thị tại màn hình dialog  */
	String[] dataPosition ;
	Position[] _positions;
	List<Position> _listPosition =null;
	
	/** Button nào được click */
	private int btnClicked ;
	/** kết nối Database */
	DatabaseAdapter mDatabaseAdapter;
	/** đối tượng hiển thị tại các màn hình search*/
	GetListDataHelper mGetListDataHelper;
	
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * onCreate
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		try{
			super.onCreate(savedInstanceState);
    		mDatabaseAdapter = new DatabaseAdapter(getApplicationContext());
    		/** khởi tạo đối tượng */
    		mGetListDataHelper = new GetListDataHelper(getApplicationContext());
			/** lưu màn hình ngang hay dọc */
			SharedVariables.CURRENT_ORIENTATION = getResources().getConfiguration().orientation ;
			/** load layout */
			setContentView(R.layout.activity_team_edit);
			/** khoi tao cac event va trị init */ 
			InitializationControl();
			/** get thông tin phòng ban dùng cho search dialog*/
			mConvertCursorToArrayString = new ConvertCursorToArrayString(getApplicationContext());
			dataDept = mConvertCursorToArrayString.getDeptList();
			
			/** Hiển thị data từ code nhóm mà đã truyền qua */
			/** get code nhóm từ intent*/ 
			Intent request = getIntent();
			Bundle param = request.getExtras();
			nCode =param.getInt(DatabaseAdapter.KEY_CODE);
			Team teamCopy = (Team)param.getParcelable(MasterConstants.TAB_TEAM_TAG);

			if (param != null) {
				if (nCode==-1){
					/** trường hợp tạo mới*/
					initDetail();
				}else if (nCode==0){
					/** hiển thị copy data*/
					displayDetail(teamCopy);
				}else{
					/** hiển thị chi tiết */
					displayDetail(nCode);
				}
			}
		}catch (Exception e){
			Log.v(TAG,e.getMessage());
		}
	}
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * gán lại biến số hướng của màn hình dọc hay ngang
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		/** lưu màn hình ngang hay dọc */
		SharedVariables.CURRENT_ORIENTATION = getResources().getConfiguration().orientation ;
	}
	
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * get các control trên màn hình
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
	private void InitializationControl() {
		/**thong tin cac item tren man hinh */
		txtCode = (TextView) findViewById(R.id.txtTeamCode);
		txtName= (EditText) findViewById(R.id.txtTeamName);
		txtDept= (EditText) findViewById(R.id.txtTeamDept);
		txtDeptCode = (TextView) findViewById(R.id.txtTeamDeptCode);
		txtTeamLeader= (EditText) findViewById(R.id.txtTeamLeader);
		txtTeamLeaderCode = (TextView) findViewById(R.id.txtTeamLeaderCode);
		txtNote= (EditText) findViewById(R.id.txtTeamNote);
		/** button*/
		btnSave = (Button) findViewById(R.id.btnTeamSave);
		btnCancel = (Button) findViewById(R.id.btnTeamCancel);
		btnDeptSearch = (Button) findViewById(R.id.btnTeamDept);
		btnLeaderSearch= (Button) findViewById(R.id.btnTeamLeader);
		/** setting event click cho cac button */
		btnSave.setOnClickListener(this);
		btnCancel.setOnClickListener(this);
		btnDeptSearch.setOnClickListener(this);
		btnLeaderSearch.setOnClickListener(this);

	}
	/**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * hiển thị data lên màn hình
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
	private void initDetail(){
		try{
			/** gán trị cho các item trên màn hình */
    		mDatabaseAdapter.open();
    		int code = mDatabaseAdapter.getMaxCodeMaster();
    		mDatabaseAdapter.close();
			txtCode.setText(String.valueOf(code));
			txtName.setText("");
			txtDept.setText("");
			txtDeptCode.setText("");
			txtTeamLeader.setText("");
			txtTeamLeaderCode.setText("");
			txtNote.setText("");
		}catch(Exception e){
			mDatabaseAdapter.close();
			Log.v(TAG,e.getMessage());
		}
		
	}	
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * hiển thị data lên màn hình
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
	private void displayDetail(int code ){
		try{
			/** khởi tạo đối tượng nhóm*/
			Team item =null;
			/** chuyển đổi từ Cursor thành list */
			mConvertCursorToListString = new ConvertCursorToListString(getApplicationContext());
			item= mConvertCursorToListString.getTeamByCode(code);
			/** gán trị cho các item trên màn hình */
			txtCode.setText(String.valueOf(item.code));
			txtName.setText(item.name.toString());
			txtDept.setText(item.yobi_text1.toString());
			txtDeptCode.setText(String.valueOf(item.yobi_code1));
			txtTeamLeader.setText(item.yobi_text2.toString());
			txtTeamLeaderCode.setText(String.valueOf(item.yobi_code2));
			txtNote.setText(item.note.toString());
		}catch(Exception e){
			Log.v(TAG,e.getMessage());
		}
		
	}
	/**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * hiển thị data lên màn hình
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
	private void displayDetail(Team item ){
		try{
			/** gán trị cho các item trên màn hình */
			mDatabaseAdapter.open();
			int code = mDatabaseAdapter.getMaxCodeMaster();
    		mDatabaseAdapter.close();
			txtCode.setText(String.valueOf(code));
			txtName.setText(item.name.toString());
			txtDept.setText(item.yobi_text1.toString());
			txtDeptCode.setText(String.valueOf(item.yobi_code1));
			txtTeamLeader.setText(item.yobi_text2.toString());
			txtTeamLeaderCode.setText(String.valueOf(item.yobi_code2));
			txtNote.setText(item.note.toString());
		}catch(Exception e){
			Log.v(TAG,e.getMessage());
		}
		
	}
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * xử lý cho các button trên màn hình 
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
	@Override
	public void onClick(View v) {
		/** get id của button */
		btnClicked =v.getId();
		/** phân nhánh xử lý theo từng button*/
		switch (v.getId()) {
			case R.id.btnTeamSave:
				/** lưu thông tin thay đổi vào DB*/
				saveTeamToDb();
				/** trả về kết quả*/
				Intent intent = new Intent();
				Bundle bundle = new Bundle();
				bundle.putInt("btn", R.id.btnTeamSave);
				intent.putExtras(bundle);
				setResult(RESULT_OK, intent);
				/** Đóng màn hình và trở về màn hình trước đó*/
				this.finish();
				break;
			case R.id.btnTeamCancel:
				/** Đóng màn hình và trở về màn hình trước đó*/
				this.finish();
				break;
				
			case R.id.btnTeamDept:
				/**hiển thị màn hình để chọn phong ban */
				/*_listDept=getListDept("");
				_depts = new Dept[_listDept.size()];
		    	int indexDept=0;
		    	for (Dept li : _listDept){
		    		_depts[indexDept] = li;
		    		indexDept++;
		    	}*/
		    	_depts  = mGetListDataHelper.getArrayDept("");
		    	
		    	if (_depts.length==0){
		    		return;
		    	}
		    	
				/** Getting the fragment manager */
				FragmentManager manager = getFragmentManager();
				
				/** Instantiating the DialogFragment class */
				//AlertDialogRadio alert = new AlertDialogRadio("",dataDept,R.id.btnTeamDept);
				AlertDialogRadio alert = new AlertDialogRadio("",_depts,R.id.btnTeamDept);
				
				/** Creating a bundle object to store the selected item's index */
				Bundle b  = new Bundle();
				
				/** Storing the selected item's index in the bundle object */
				b.putInt("position", positionDept);
				
				/** Setting the bundle object to the dialog fragment object */
				alert.setArguments(b);
				
				/** Creating the dialog fragment object, which will in turn open the alert dialog window */
				alert.show(manager, MasterConstants.TAB_DIALOG_TAG);	
				break;
				
			case R.id.btnTeamLeader:
				/**hiển thị màn hình để chọn người quản lý */
				/*_listUser =getListUser("");
		    	
				_users = new User[_listUser.size()];
		    	int indexUser=0;
		    	for (User li : _listUser){
		    		_users[indexUser] = li;
		    		indexUser++;
		    	}
		    	*/
				_users  = mGetListDataHelper.getArrayUser("");
		    	
		    	if (_users.length==0){
		    		return;
		    	}
				/** Getting the fragment manager */
				FragmentManager leader = getFragmentManager();
				
				/** Instantiating the DialogFragment class */
				//AlertDialogRadio alertLeader = new AlertDialogRadio("",dataUser,R.id.btnTeamLeader);
				AlertDialogRadio alertLeader = new AlertDialogRadio("",_users,R.id.btnTeamLeader);
				/** Creating a bundle object to store the selected item's index */
				Bundle bun  = new Bundle();
				
				/** Storing the selected item's index in the bundle object */
				bun.putInt("position", positionTeamLeader);
				
				/** Setting the bundle object to the dialog fragment object */
				alertLeader.setArguments(bun);
				
				/** Creating the dialog fragment object, which will in turn open the alert dialog window */
				alertLeader.show(leader, MasterConstants.TAB_DIALOG_TAG);	
				break;
			}
	}
	
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * Defining button click listener for the OK button of the alert dialog window 
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    @Override
    public void onPositiveClick(int position, int clickBtn, Bundle bundle) {
    	/** Setting the selected android version in the textview */
    	switch (btnClicked) {
    		/** Phân nhánh xử lý tùy theo button mà đã được clicked*/
	    	case R.id.btnTeamDept:
	    		/** gán lại vị trí của item mà đã chọn */
	    		this.positionDept = position;
	    		/** gán tên người đã chọn tại màn hình poup */
	    		//txtDept.setText(dataDept[this.positionDept]);    
	    		txtDept.setText(_depts[position].name);
	    		txtDeptCode.setText(String.valueOf(_depts[position].code));
	    		break;
	    	case R.id.btnTeamLeader:
	    		/** gán lại vị trí của item mà đã chọn */
	    		this.positionTeamLeader = position;
	    		/** gán tên người đã chọn tại màn hình poup */
	    		//txtTeamLeader.setText(dataUser[this.positionTeamLeader]);    
	    		txtTeamLeader.setText(_users[position].full_name);
	    		txtTeamLeaderCode.setText(String.valueOf(_users[position].code));
	    		break;
    	}
    }
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * 
     * lưu thông tin vào DB
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    public void saveTeamToDb(){
    	try{

			Team team = new Team();
			mDatabaseAdapter.open();
			/** mã phòng ban sẽ update */
			if (nCode ==-1 ){
				/** tạo mới */
				
			}else{
				/** chỉnh sửa */
				team.code =nCode;
				/** cố định là 1 */
			}
			team.mkbn=MasterConstants.MASTER_MKBN_TEAM;
			/** tên nhóm  */
			team.name = txtName.getText().toString() ;
			/** tên tắt */
			team.ryaku=txtName.getText().toString();
			/** phòng ban  */
			team.yobi_text1=txtDept.getText().toString();
			/** code phòng ban  */
			if(txtDeptCode.getText().toString()==null || txtDeptCode.getText().toString().equals("")){
				team.yobi_code1=0;
			}else{
				team.yobi_code1=Integer.parseInt(txtDeptCode.getText().toString());
			}
			
			/** leader */
			team.yobi_text2=txtTeamLeader.getText().toString();
			/** code leader*/
			if(txtTeamLeaderCode.getText().toString()==null || txtTeamLeaderCode.getText().toString().equals("")){
				team.yobi_code2=0;
			}else{
				team.yobi_code2=Integer.parseInt(txtTeamLeaderCode.getText().toString());
			}
			
			/**ghi chú */
			team.note =txtNote.getText().toString();
			if (nCode ==-1 ){
				/** tạo mới */
				mDatabaseAdapter.insertToMasterTable(team);
			}else{
				/** insert vào DB */			
	    		mDatabaseAdapter.editMasterTable(team);
			}
    		/** đóng connection */
    		mDatabaseAdapter.close();
    		
    	}catch ( Exception e){
    		Log.v(TAG,e.getMessage());
    		/** đóng connection */
    		mDatabaseAdapter.close();
    	}
    }

    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * get data cho list
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
	public List<User> getListUser(String xWhere){
		
		/** chuyển đổi từ Cursor thành List */
		mConvertCursorToListString = new ConvertCursorToListString(this);
		return mConvertCursorToListString.getUserList(xWhere);
	}

	/**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * get list dept 
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
	public List<Dept> getListDept(String xWhere){
		
		/** chuyển đổi từ Cursor thành List */
		mConvertCursorToListString = new ConvertCursorToListString(this);
		return mConvertCursorToListString.getDeptList(xWhere);
	}
	
	/**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * get list Team 
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
	public List<Team> getListTeam(String xWhere){
		
		/** chuyển đổi từ Cursor thành List */
		mConvertCursorToListString = new ConvertCursorToListString(this);
		return mConvertCursorToListString.getTeamList(xWhere);
	}
	
	/**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * get list Team 
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
	public List<Position> getListPosition(String xWhere){
		
		/** chuyển đổi từ Cursor thành List */
		mConvertCursorToListString = new ConvertCursorToListString(this);
		return mConvertCursorToListString.getPositionList(xWhere);
	}
	
}