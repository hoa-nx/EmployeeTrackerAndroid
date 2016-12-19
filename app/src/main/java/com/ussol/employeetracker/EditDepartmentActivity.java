/**
 * Copyright (c) 2012 Fujinet Software Department 5 ( USSOL team)  
 * See the file license.txt for copying permission.
*/  
package com.ussol.employeetracker;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.ussol.employeetracker.EditUserBasic.SelectDateFragment;
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
import com.ussol.employeetracker.utils.DateTimeUtil;
import com.ussol.employeetracker.utils.Utils;
import com.ussol.employeetracker.helpers.AlertDialogRadio;
import com.ussol.employeetracker.helpers.AlertDialogRadio.AlertPositiveListener;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

public class EditDepartmentActivity extends  Activity implements OnClickListener ,AlertPositiveListener , OnTouchListener {
	/** tag */
	private final static String TAG =EditDepartmentActivity.class.getName();
	/** chuyển đổi Cursor thành List */
	ConvertCursorToListString mConvertCursorToListString;
	/** Lưu trữ control code phòng ban */
	TextView txtCode , txtManagerCode , txtViceCode;
	/** Lưu trữ control tên ...phòng ban */
	EditText txtName, txtManager, txtVice , txtNote , txtCreateDate;
	/** Trị code phòng ban  */
	private int nCode = 0;
	/** Lưu các button trên màn hình  */
	Button btnSave, btnCancel , btnDepartmentMSearch, btnDepartmentVSearch,btnDepartmentCreateDateSearch  ;
	/** Lưu lại vị trí mà đã chọn từ list radio */
	int positionManager = 0;
	int positionVice = 0;
	/** thông tin các user hiển thị tại màn hình dialog  */
	String[] dataUser = new String[]{"Nguyễn Minh Hải","Dương Thái Trung"}  ;
	User[] _users;
	List<User> _listUser =null;
	/** thông tin các phòng ban hiển thị tại màn hình dialog  */
	String[] dataDept ;
	Dept[] _depts;
	List<Dept> _listDept =null;
	/** thông tin các nhóm hiển thị tại màn hình dialog  */
	String[] dataTeam ;
	Team[] _teams;
	List<Team> _listTeam =null;
	/** thông tin các chức vụ hiển thị tại màn hình dialog  */
	String[] dataPosition ;
	Position[] _Positions;
	List<Position> _listPosition =null;
	/** chuyển đổi Cursor thành string array */
	ConvertCursorToArrayString mConvertCursorToArrayString;
	/** Button nào được click */
	int btnClicked ;
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
			setContentView(R.layout.activity_department_edit);
			/** khoi tao cac event va trị init */ 
			InitializationControl();
			/** Hiển thị data từ code phòng ban mà đã truyền qua */
			/** get code phòng ban tu intent*/ 
			Intent request = getIntent();
			Bundle param = request.getExtras();
			nCode =param.getInt(DatabaseAdapter.KEY_CODE);
			Dept deptCopy = (Dept)param.getParcelable(MasterConstants.TAB_DEPT_TAG);
			
			if (param != null) {
				if (nCode==-1){
					/** trường hợp tạo mới*/
					initDetail();
				}else if (nCode==0){
					/** hiển thị copy data*/
					displayDetail(deptCopy);
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
		txtCode = (TextView) findViewById(R.id.txtDepartmentCode);
		txtName= (EditText) findViewById(R.id.txtDepartmentName);
		txtManagerCode= (TextView) findViewById(R.id.txtDepartmentManagerCode);
		txtManager= (EditText) findViewById(R.id.txtDepartmentManager);
		txtVice= (EditText) findViewById(R.id.txtDepartmentVice);
		txtViceCode= (TextView) findViewById(R.id.txtDepartmentViceCode);
		txtCreateDate= (EditText) findViewById(R.id.txtDepartmentCreateDate);
		txtNote= (EditText) findViewById(R.id.txtDepartmentNote);
		/** button*/
		btnSave = (Button) findViewById(R.id.btnDeptSave);
		btnCancel = (Button) findViewById(R.id.btnDeptCancel);
		btnDepartmentMSearch = (Button) findViewById(R.id.btnDepartmentManager);
		btnDepartmentVSearch = (Button) findViewById(R.id.btnDepartmentVice);
		btnDepartmentCreateDateSearch= (Button) findViewById(R.id.btnCreateDate);
		/** setting event click cho cac button */
		btnSave.setOnClickListener(this);
		btnCancel.setOnClickListener(this);
		btnDepartmentMSearch.setOnClickListener(this);
		btnDepartmentVSearch.setOnClickListener(this);
		btnDepartmentCreateDateSearch.setOnClickListener(this);
		
		txtCreateDate.setOnClickListener(this);
		txtCreateDate.setOnTouchListener(this);
		txtCreateDate.setOnClickListener(this);
    	
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
			txtManager.setText("");
			txtManagerCode.setText("");
			txtVice.setText("");
			txtViceCode.setText("");
			txtCreateDate.setText("");
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
			/** khởi tạo đối tượng phòng ban*/
			Dept item =null;
			/** chuyển đổi từ Cursor thành list */
			mConvertCursorToListString = new ConvertCursorToListString(getApplicationContext());
			item= mConvertCursorToListString.getDeptByCode(code);
			/** gán trị cho các item trên màn hình */
			txtCode.setText(String.valueOf(item.code));
			txtName.setText(item.name.toString());
			txtManager.setText(item.yobi_text1.toString());/** tên trưởng phòng */
			txtManagerCode.setText(String.valueOf(item.yobi_code1)); /** code trưởng phòng */
			txtVice.setText(item.yobi_text2.toString());/** tên phó phòng */
			txtViceCode.setText(String.valueOf(item.yobi_code2)); /** code phó phòng */
			boolean isDate=DateTimeUtil.isDate(String.valueOf(item.create_date), MasterConstants.DATE_VN_FORMAT);
			if(isDate){
				txtCreateDate.setText(String.valueOf(item.create_date)); /** ngay thanh lap bo phan */				
			}else{
				txtCreateDate.setText(""); /** ngay thanh lap bo phan */
			}
			
			txtNote.setText(item.note.toString());
		}catch(Exception e){
			Log.v(TAG,e.getMessage());
		}
		
	}
	
	/**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * hiển thị data lên màn hình
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
	private void displayDetail(Dept  item){
		try{
			/** gán trị cho các item trên màn hình */
			mDatabaseAdapter.open();
			int code = mDatabaseAdapter.getMaxCodeMaster();
    		mDatabaseAdapter.close();
			txtCode.setText(String.valueOf(code));
			txtName.setText(item.name.toString());
			txtManager.setText(item.yobi_text1.toString());
			txtManagerCode.setText(String.valueOf(item.yobi_code1)); /** code trưởng phòng */
			txtVice.setText(item.yobi_text2.toString());
			txtViceCode.setText(String.valueOf(item.yobi_code2)); /** code phó phòng */
			txtCreateDate.setText(item.create_date.toString());/** ngay thanh lap */
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
			case R.id.btnDeptSave:
				/** lưu thông tin thay đổi vào DB*/
				saveDeptToDb();
				/** trả về kết quả*/
				Intent intent = new Intent();
				Bundle bundle = new Bundle();
				bundle.putInt("btn", R.id.btnDeptSave);
				intent.putExtras(bundle);
				setResult(RESULT_OK, intent);
				/** Đóng màn hình và trở về màn hình trước đó*/
				this.finish();
				break;
			case R.id.btnDeptCancel:
				/** Đóng màn hình và trở về màn hình trước đó*/
				this.finish();
				
				break;
				
			case R.id.btnDepartmentManager:
				/**hiển thị màn hình để chọn người quản lý */
				/*_listUser =getListUser("");
		    	
				_users = new User[_listUser.size()];
		    	int indexUser=0;
		    	for (User li : _listUser){
		    		_users[indexUser] = li;
		    		indexUser++;
		    	}*/
		    	
		    	_users  = mGetListDataHelper.getArrayUser("");
		    	if (_users.length==0){
		    		return;
		    	}
				
				/** Getting the fragment manager */
				FragmentManager manager = getFragmentManager();
				
				/** Instantiating the DialogFragment class */
				//AlertDialogRadio alert = new AlertDialogRadio("",dataUser,R.id.btnDepartmentManager);
				AlertDialogRadio alert = new AlertDialogRadio("",_users,R.id.btnDepartmentManager);
				/** Creating a bundle object to store the selected item's index */
				Bundle b  = new Bundle();
				
				/** Storing the selected item's index in the bundle object */
				b.putInt("position", positionManager);
				
				/** Setting the bundle object to the dialog fragment object */
				alert.setArguments(b);
				
				/** Creating the dialog fragment object, which will in turn open the alert dialog window */
				alert.show(manager, "alert_dialog_radio");	
				break;
				
			case R.id.btnDepartmentVice:
				/**hiển thị màn hình để chọn người quản lý */
				/*_listUser =getListUser("");
		    	
				_users = new User[_listUser.size()];
		    	indexUser=0;
		    	for (User li : _listUser){
		    		_users[indexUser] = li;
		    		indexUser++;
		    	}*/
		    	
		    	_users  = mGetListDataHelper.getArrayUser("");
		    	if (_users.length==0){
		    		return;
		    	}
				/** Getting the fragment manager */
				FragmentManager vice = getFragmentManager();
				
				/** Instantiating the DialogFragment class */
				//AlertDialogRadio alertVice = new AlertDialogRadio("",dataUser,R.id.btnDepartmentManager);
				AlertDialogRadio alertVice = new AlertDialogRadio("",_users,R.id.btnDepartmentManager);
				/** Creating a bundle object to store the selected item's index */
				Bundle bun  = new Bundle();
				
				/** Storing the selected item's index in the bundle object */
				bun.putInt("position", positionVice);
				
				/** Setting the bundle object to the dialog fragment object */
				alertVice.setArguments(bun);
				
				/** Creating the dialog fragment object, which will in turn open the alert dialog window */
				alertVice.show(vice, MasterConstants.TAB_DIALOG_TAG);	
				break;
				
			case R.id.btnCreateDate:
				txtCreateDate.setText("");//xoa ngay thanh lap
				break;
			}
	}
	/**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
	 * selectDate
	 * 
	 ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
	public void selectDate(View view) {
	        DialogFragment newFragment = new SelectDateFragment(view.getId());
	        newFragment.show(getFragmentManager(), "Create Date");
	 }
	/**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
	 * populateSetDate
	 * 
	 ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    public void populateSetDate(int control,int year, int month, int day) {
    	//mDateDisplay = (EditText)findViewById(R.id.editText1);
    	switch (control){
    		case R.id.txtDepartmentCreateDate:
    			txtCreateDate.setText(day + MasterConstants.DATE_SEPERATE_CHAR + month+ MasterConstants.DATE_SEPERATE_CHAR +year);
    			break;
    	}
    	
    }
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * SelectDateFragment
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    public class SelectDateFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    	private int mControl ;
    	public SelectDateFragment(int control){
    		mControl = control;
    	}
    	@Override
    	public Dialog onCreateDialog(Bundle savedInstanceState) {
			/*final Calendar calendar = Calendar.getInstance();
			int yy = calendar.get(Calendar.YEAR);
			int mm = calendar.get(Calendar.MONTH);
			int dd = calendar.get(Calendar.DAY_OF_MONTH);
			return new DatePickerDialog(getActivity(), this, yy, mm, dd);*/
    		String date =null;
    		switch (mControl){
	    		case R.id.txtDepartmentCreateDate:
	    			date = txtCreateDate.getText().toString();
	    			break;
    		}
    		int yy;
    		int mm;
    		int dd;
    		/** trường hợp mà chưa có input thì sẽ là ngày hiện tại */
    		if (date ==null || date.equals("")){
    			final Calendar calendar = Calendar.getInstance();
				yy = calendar.get(Calendar.YEAR);
				mm = calendar.get(Calendar.MONTH);
				dd = calendar.get(Calendar.DAY_OF_MONTH);
    		}else{
    			/** hiển thị ngày mà đã chọn trước đó */
    			
    			//Date datefmt = DateTime.convertStringToDate(date, "dd/MM/yyyy");
    			Date datefmt = DateTimeUtil.convertStringToDate(date, MasterConstants.DATE_VN_FORMAT);
    			yy = datefmt.getYear() + 1900;
				mm = datefmt.getMonth();
				dd = datefmt.getDate();
    		}
			return new DatePickerDialog(getActivity(), this, yy, mm, dd);
	
    	}
    	
    	
    	public void onDateSet(DatePicker view, int yy, int mm, int dd) {
    		populateSetDate(mControl,yy, mm+1, dd);
    	}
    }
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * Defining button click listener for the OK button of the alert dialog window 
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    @Override
    public void onPositiveClick(int position , int clickBtn, Bundle bundle) {
    	/** Setting the selected android version in the textview */
    	switch (btnClicked) {
    		/** Phân nhánh xử lý tùy theo button mà đã được clicked*/
	    	case R.id.btnDepartmentManager:
	    		/** gán lại vị trí của item mà đã chọn */
	    		this.positionManager = position;
	    		/** gán tên người đã chọn tại màn hình poup */
	    		//txtManager.setText(dataUser[this.positionManager]);    
	    		txtManager.setText(_users[position].full_name);
	    		txtManagerCode.setText(String.valueOf(_users[position].code));
	    		break;
	    	case R.id.btnDepartmentVice:
	    		/** gán lại vị trí của item mà đã chọn */
	    		this.positionVice = position;
	    		/** gán tên người đã chọn tại màn hình poup */
	    		//txtVice.setText(dataUser[this.positionVice]);   
	    		txtVice.setText(_users[position].full_name);
	    		txtViceCode.setText(String.valueOf(_users[position].code));
	    		break;
    	}
    }
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * 
     * lưu thông tin vào DB
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    public void saveDeptToDb(){
    	try{

			Dept dept = new Dept();
			mDatabaseAdapter.open();
			/** mã phòng ban sẽ update */
			if (nCode ==-1 ){
				/** tạo mới */
				
			}else{
				/** chỉnh sửa */
				dept.code =nCode;
				/** cố định là 1 */
			}
			dept.mkbn=MasterConstants.MASTER_MKBN_DEPT;
			/** tên phòng ban  */
			dept.name = txtName.getText().toString() ;
			/** tên tắt phòng ban  */
			dept.ryaku=txtName.getText().toString();
			/**trưởng phòng  */
			dept.yobi_text1=txtManager.getText().toString();
			/** code trưởng phòng  */
			if(txtManagerCode.getText().toString()==null || txtManagerCode.getText().toString().equals("")){
				dept.yobi_code1=0;
			}else{
				dept.yobi_code1=Integer.parseInt(txtManagerCode.getText().toString());
			}
			
			/**phó phòng  */
			dept.yobi_text2=txtVice.getText().toString();
			/** code phó phòng  */
			if(txtViceCode.getText().toString()==null || txtViceCode.getText().toString().equals("")){
				dept.yobi_code2=0;
			}else{
				dept.yobi_code2=Integer.parseInt(txtViceCode.getText().toString());
			}
			/** ngay thanh lap */
			dept.create_date=txtCreateDate.getText().toString();
			
			/**ghi chú */
			dept.note =txtNote.getText().toString();
			if (nCode ==-1 ){
				/** tạo mới */
				mDatabaseAdapter.insertToMasterTable(dept);
			}else{
				/** insert vào DB */			
	    		mDatabaseAdapter.editMasterTable(dept);
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
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		/** phân nhánh xử lý theo từng button*/
		switch (v.getId()) {
			case R.id.txtDepartmentCreateDate:
				if(event.getAction()==MotionEvent.ACTION_UP){
					selectDate(v);
				}
				
			break;
		}
    	return true; 
	}
}