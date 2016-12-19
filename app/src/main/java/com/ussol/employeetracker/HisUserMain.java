/**
 * Copyright (c) 2012 Fujinet Software Department 5 ( USSOL team)  
 * See the file license.txt for copying permission.
*/  

package com.ussol.employeetracker;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.mail.Quota.Resource;

import com.ussol.employeetracker.helpers.AlertDialogRadio;
import com.ussol.employeetracker.helpers.CommonClass;
import com.ussol.employeetracker.helpers.ConvertCursorToListString;
import com.ussol.employeetracker.helpers.DatabaseAdapter;
import com.ussol.employeetracker.helpers.DecodeTask;
import com.ussol.employeetracker.helpers.FileHelper;
import com.ussol.employeetracker.helpers.GetListDataHelper;
import com.ussol.employeetracker.helpers.AlertDialogRadio.AlertPositiveListener;
import com.ussol.employeetracker.models.Dept;
import com.ussol.employeetracker.models.MasterConstants;
import com.ussol.employeetracker.models.Position;
import com.ussol.employeetracker.models.Team;
import com.ussol.employeetracker.models.User;
import com.ussol.employeetracker.models.UserHistory;
import com.ussol.employeetracker.models.UserPositionGroup;
import com.ussol.employeetracker.utils.DateTimeUtil;
import com.ussol.employeetracker.utils.ShowAlertDialog;
import com.ussol.employeetracker.utils.Utils;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.GetChars;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class HisUserMain extends Fragment  implements  OnClickListener , OnTouchListener,AlertPositiveListener,OnCheckedChangeListener{
	/** tag */
	private final static String TAG =HisUserMain.class.getName();
    static final int DATE_DIALOG_ID = 0;
    //private TextView lblUserCode , lblUserDeptCode , lblUserTeamCode , lblUserPositionCode ;
    private TextView txtUserCode, imgUserFullPath,txtUserHisDeptNewDeptCode , txtUserHisDeptNewTeamCode,txtUserHisDeptNewPositionCode ,txtUserHisDept_count ;
    private EditText txtUserHisDeptStartDate ,txtUserHisDeptNote,txtUserHisDeptNewDept , txtUserHisDeptNewTeam,txtUserHisDeptNewPosition;
    private EditText txtUserFullName;
    private Button btnUserSave , btnUserCancel , btnUserHisDeptNewDept , btnUserHisDeptNewTeam, btnUserHisDeptNewPosition;
    private  CheckBox chkUserHisDeptNewDept,chkUserHisDeptNewTeam,chkUserHisDeptNewPosition;
	private Bitmap bitmap;
	private ImageView imageView , pic , imgPrev, imgNext;
	private ImageButton imgUserHisDeptList;
	private Uri mImageCaptureUri;
	CommonClass commClass;
	private boolean isFirstRun = true;
    String fileManagerString,imagePath;  
    String selectedImagePath="";  
    private int columnIndex;  
	/** kết nối Database */
	DatabaseAdapter mDatabaseAdapter;
	/** Trị code nhân viên  */
	private int nCode = -1;
	
	public static UserHistory userHisInfo ;
	public static User userInfo ;
	public static String imgPathSave=""; 
	/** chuyển đổi Cursor thành List */
	private  ConvertCursorToListString mConvertCursorToListString;
	/** đối tượng hiển thị tại các màn hình search*/
	GetListDataHelper mGetListDataHelper;
	/** thông tin các nhóm hiển thị tại màn hình dialog  */
	User[] _Users=null;
	List<User> _listUser =null;
	List<User> _listUserChecked =null;
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
	Position[] _Positions=null;
	List<Position> _listPosition =null;
    /** Lưu lại vị trí mà đã chọn từ list radio */
	int positionDept = 0;
	int positionPos = 0;
	int positionTeam= 0;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		isFirstRun = true;
	}
	/**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
	 * onCreateView
	 * 
	 ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		ViewGroup root = (ViewGroup) inflater.inflate(R.layout.activity_user_his_main, null);	
		/** setting tab để có thể truy xuất từ các Tab khác*/
		((HisUserMainActivity)getActivity()).setTabFragmentHisMain(getTag());
		return root;
	}
	
	/**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
	 * onActivityCreated
	 * 
	 ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
        
		mDatabaseAdapter = new DatabaseAdapter(getActivity().getApplicationContext());
		mConvertCursorToListString = new ConvertCursorToListString(getActivity().getApplicationContext());
		/** khởi tạo đối tượng */
		mGetListDataHelper = new GetListDataHelper(getActivity().getApplicationContext());
		_listUser= new ArrayList<User>();
		_listUserChecked= new ArrayList<User>();
		
		nCode=HisUserMainActivity.nCode;
		userInfo =HisUserMainActivity.userInfo;
		userHisInfo =HisUserMainActivity.userHisInfo;
		/** gan init cho list user duoc check khi mo man hinh search */
		if(userInfo !=null){
			_listUser.add(userInfo);
		}
		/** get các control date và gán sự kiện khi click vào textbox */
		getControl();
		/** gán sự kiện cho các control */ 
		settingListener();
		/** setting không hiển thị keyboard */
		settingInputType();
		/** add event cho cac control */
		addTextChangedListener();
		/** setting init cho cac item check box */
		setHisDeptCheck(false);
		setHisTeamCheck(false);
		setHisPositionCheck(false);
				
		if (savedInstanceState!=null){
			
		}else{
			if (nCode==-1){
				/** trường hợp tạo mới*/

			}else{
				/** hiển thị chi tiết */
				setUserCode(userHisInfo.user_code);
				/** setting thong tin cua user */
				setValueTabMain(userHisInfo);
				/** setting code max cua user : chua su dung*/
				setUserCode(1);
				/** setting so user duoc chon */
				setUserCount(1);
			}
		}
		isFirstRun = false;
		/** hien thi hinh anh nhan vien */
        pic=(ImageView)getView().findViewById(R.id.imgUser);  
        pic.setOnClickListener(this);  
        
	}
	/**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
	 * onPause
	 * 
	 ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		View target = getView().findFocus();
        if (target != null) 
        {
            InputMethodManager mgr = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            mgr.hideSoftInputFromWindow(target.getWindowToken(), 0);
        }
        String TabOfFragmentPosition =HisUserMainActivity.getTabFragmentHisOther();
		   
        HisUserOther fgUserPosition = (HisUserOther)getFragmentManager().findFragmentByTag(TabOfFragmentPosition);
    	if(fgUserPosition!=null){
    		fgUserPosition.onPause();
    	}
	}
	
	/**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
	 * onClick
	 * 
	 ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
	@Override
	public void onClick(View v) {
		
		/** phân nhánh xử lý theo từng button*/
		switch (v.getId()) {
			case R.id.imgUserHisDeptList:
				/** hien thi list user de chon*/
				showDialogUser();
				break;
			case R.id.btnUserHisDeptNewDept:
		    	_depts  = mGetListDataHelper.getArrayDept("");
		    	
		    	if (_depts.length==0){
		    		return;
		    	}
				/**hiển thị màn hình để chọn phong ban */
				/** Getting the fragment manager */
				FragmentManager manager =getActivity().getFragmentManager();
				
				/** Instantiating the DialogFragment class */
				AlertDialogRadio alert = new AlertDialogRadio("",_depts,R.id.btnUserHisDeptNewDept);

				/** Creating a bundle object to store the selected item's index */
				Bundle b  = new Bundle();
				
				/** Storing the selected item's index in the bundle object */
				b.putInt("position", positionDept);
				
				/** Setting the bundle object to the dialog fragment object */
				alert.setArguments(b);
				
				/** Creating the dialog fragment object, which will in turn open the alert dialog window */
				alert.show(manager, MasterConstants.TAB_DIALOG_TAG);	
				break;
				
				
			case R.id.btnUserHisDeptNewTeam:
				_teams  = mGetListDataHelper.getArrayTeam("");
		    	if (_teams.length==0){
		    		return;
		    	}
				/**hiển thị màn hình để chọn nhóm*/
				/** Getting the fragment manager */
				FragmentManager managerTeam =getActivity().getFragmentManager();
				
				/** Instantiating the DialogFragment class */
				AlertDialogRadio alertTeam = new AlertDialogRadio("",_teams,R.id.btnUserHisDeptNewTeam);
				/** Creating a bundle object to store the selected item's index */
				Bundle bTeam  = new Bundle();
				
				/** Storing the selected item's index in the bundle object */
				bTeam.putInt("position", positionTeam);
				
				/** Setting the bundle object to the dialog fragment object */
				alertTeam.setArguments(bTeam);
				
				/** Creating the dialog fragment object, which will in turn open the alert dialog window */
				alertTeam.show(managerTeam, MasterConstants.TAB_DIALOG_TAG);	
				break;
			case R.id.btnUserHisDeptNewPosition:
				_Positions  = mGetListDataHelper.getArrayPosition("");
		    	if (_Positions.length==0){
		    		return;
		    	}
		    	
				/**hiển thị màn hình để chọn nhóm*/
				/** Getting the fragment manager */
				FragmentManager managerPosition =getActivity().getFragmentManager();
				
				/** Instantiating the DialogFragment class */
				AlertDialogRadio alertPosition = new AlertDialogRadio("",_Positions,R.id.btnUserHisDeptNewPosition);
				/** Creating a bundle object to store the selected item's index */
				Bundle bPosition  = new Bundle();
				
				/** Storing the selected item's index in the bundle object */
				bPosition.putInt("position", positionPos);
				
				/** Setting the bundle object to the dialog fragment object */
				alertPosition.setArguments(bPosition);
				
				/** Creating the dialog fragment object, which will in turn open the alert dialog window */
				alertPosition.show(managerPosition, MasterConstants.TAB_DIALOG_TAG);	
				break;
				
			case R.id.btnUserSave:
				if(checkUserListChecked()){
					/** kiểm tra xem  đã có input ngày chưa */
					if(getHisDeptStartDate().equals("")){
						ShowAlertDialog.showTitleAndMessageDialog(getActivity(), "Cập nhật dữ liệu", "Chưa nhập ngày tháng năm hiệu lực.");
						break;
					}
					/** neu nhu co ton tai user muon cap nhat*/
					/** Lưu vào DB */
					SaveDbEntryPoint();
					//saveUserToDb();
					/** cập nhật dữ liệu cho tab 2 */
					/** Tạm thời comment vì đã có xử lý trong hàm saveUserToDb */
					/*
					String TabOfFragmentOther =HisUserMainActivity.getTabFragmentHisOther();
			    	HisUserOther fgUserOther = (HisUserOther)getFragmentManager().findFragmentByTag(TabOfFragmentOther);
			    	if (fgUserOther!=null){
						//thông tin của tab chuc vu
			    		fgUserOther.saveUserToDb(_listUserChecked, getHisDeptStartDate());
					}
			    	*/
			    	
					/** trả về kết quả*/
					/*
					Intent intent = new Intent();
					Bundle bundle = new Bundle();
					bundle.putInt("btn", R.id.btnUserSave);
					bundle.putInt(MasterConstants.TAB_USER_HIS_TAG, 0);
					bundle.putInt(MasterConstants.EXP_USER_GROUP_TAG, HisUserMainActivity.currentGroup);
					intent.putExtras(bundle);
					getActivity().setResult(getActivity().RESULT_OK, intent);
					getActivity().finish();
					*/
					
				}else{
					ShowAlertDialog.showTitleAndMessageDialog(getActivity(), "Cập nhật dữ liệu", "Không có nhân viên nào được chọn.");
					break;
				}
				
				break;
			case R.id.btnUserCancel:
				getActivity().finish();
				break;
			
		}
	}
	/**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
	 * checkUserListChecked
	 * 
	 ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
	public boolean checkUserListChecked(){
		boolean isExisted = false;
		for(User usr : _listUser){
			if(usr.isselected){
				isExisted = true;
				break;
			}
		}
		return isExisted;
	}
	
	/**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
	 * getUserListDeptChecked
	 * 
	 ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
	public List<User> getUserListChecked(List<User> list){
		List<User> _list= new ArrayList<User>();
		for(User usr : list){
			if(usr.isselected){
				_list.add(usr);
				
			}
		}
		return _list;
	}
	
	/**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
	 * addTextChangedListener
	 * 
	 ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
	public void addTextChangedListener(){
		txtUserFullName.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				
			}
		});
	}
	/**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
	 * selectDate
	 * 
	 ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
	public void selectDate(View view) {
	        DialogFragment newFragment = new SelectDateFragment(view.getId());
	        newFragment.show(getActivity().getFragmentManager(), "StartDate");
	 }
	/**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
	 * populateSetDate
	 * 
	 ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    public void populateSetDate(int control,int year, int month, int day) {
    	//mDateDisplay = (EditText)findViewById(R.id.editText1);
    	switch (control){
    		case R.id.txtUserHisDeptStartDate:
    			txtUserHisDeptStartDate.setText(day + MasterConstants.DATE_SEPERATE_CHAR + month+ MasterConstants.DATE_SEPERATE_CHAR +year);
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
    		String date =null;
    		switch (mControl){
	    		case R.id.txtUserHisDeptStartDate:
	    			date = txtUserHisDeptStartDate.getText().toString();
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
     * get các control trên màn hình
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    private void getControl(){
    	txtUserCode = (TextView)getView().findViewById(R.id.txtUserHisCode);
    	if(imgUserFullPath==null){
    		imgUserFullPath = (TextView)getView().findViewById(R.id.txtImgFullPath);
    	}
    	txtUserFullName= (EditText)getView().findViewById(R.id.txtUserHisName);
    	txtUserHisDeptStartDate = (EditText) getView().findViewById(R.id.txtUserHisDeptStartDate);
    	txtUserHisDeptNewDept= (EditText)getView().findViewById(R.id.txtUserHisDeptNewDept);
    	txtUserHisDeptNewTeam= (EditText)getView().findViewById(R.id.txtUserHisDeptNewTeam);
    	txtUserHisDeptNewPosition= (EditText)getView().findViewById(R.id.txtUserHisDeptNewPosition);
    	
    	txtUserHisDeptNewDeptCode= (TextView)getView().findViewById(R.id.txtUserHisDeptNewDeptCode);
    	txtUserHisDeptNewTeamCode= (TextView)getView().findViewById(R.id.txtUserHisDeptNewTeamCode);
    	txtUserHisDeptNewPositionCode= (TextView)getView().findViewById(R.id.txtUserHisDeptNewPositionCode);
    	
    	txtUserHisDeptNote=(EditText)getView().findViewById(R.id.txtUserHisDeptNote);
    	
    	btnUserHisDeptNewDept = (Button)getView().findViewById(R.id.btnUserHisDeptNewDept);
    	btnUserHisDeptNewTeam = (Button)getView().findViewById(R.id.btnUserHisDeptNewTeam);
    	btnUserHisDeptNewPosition= (Button)getView().findViewById(R.id.btnUserHisDeptNewPosition);
    	
    	chkUserHisDeptNewDept =(CheckBox)getView().findViewById(R.id.chkUserHisDeptNewDept);
    	chkUserHisDeptNewTeam =(CheckBox)getView().findViewById(R.id.chkUserHisDeptNewTeam);
    	chkUserHisDeptNewPosition =(CheckBox)getView().findViewById(R.id.chkUserHisDeptNewPosition);
    	
    	txtUserHisDept_count = ( TextView)getView().findViewById(R.id.txtUserHisDept_count);
    	imgUserHisDeptList = (ImageButton)getView().findViewById(R.id.imgUserHisDeptList);
		btnUserSave = (Button)getView().findViewById(R.id.btnUserSave);
		btnUserCancel= (Button)getView().findViewById(R.id.btnUserCancel);
		
    }
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * gán sự kiện cho các control
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    private void settingListener(){
    	txtUserHisDeptStartDate.setOnTouchListener(this);
    	
    	btnUserHisDeptNewDept.setOnClickListener(this);
    	btnUserHisDeptNewTeam.setOnClickListener(this);
    	btnUserHisDeptNewPosition.setOnClickListener(this);
    	
    	chkUserHisDeptNewDept.setOnCheckedChangeListener(this);
    	chkUserHisDeptNewTeam.setOnCheckedChangeListener(this);
    	chkUserHisDeptNewPosition.setOnCheckedChangeListener(this);
		
    	btnUserSave.setOnClickListener(this);
		btnUserCancel.setOnClickListener(this);
		imgUserHisDeptList.setOnClickListener(this);
    }
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * setting input cho control(không hiển thị bàn phím khi nhận focus)
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    private void settingInputType(){
    	txtUserHisDeptStartDate.setInputType(InputType.TYPE_NULL);

    }
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * hiển thị dialog date khi chạm vào màn hình
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    @Override
    public boolean onTouch (View v, MotionEvent event){
    	/** phân nhánh xử lý theo từng button*/
		switch (v.getId()) {
			case R.id.txtUserHisDeptStartDate:
				if(event.getAction()==MotionEvent.ACTION_UP){
					selectDate(v);
				}
			break;
		}
    	return true; 
    }
            
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * 
     * lưu thông tin vào DB
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    public void saveUserToDb(){
    	try{
    		int currentCodeValue =0;
    		String  currentStringValue="";
    		float currentFloatValue=0;
			UserHistory userInsert ;
			/** get danh sách các user được chọn */
			_listUserChecked = getUserListChecked(_listUser);
			
			/** cap nhat cho truong hop la phong ban thay doi */
			if(getHisDeptCheck() && !getHisDeptStartDate().equals("")){
				/** xử lý cho từng user được chọn */
				for(User usr: _listUserChecked){
					/** kiem tra xem tri co thay doi so voi master User khong ? Neu khong thi khong update */
					currentStringValue = String.valueOf(usr.dept) ;
					if(!currentStringValue.equals(getHisDeptNewDept())){
						if(userHisInfo!=null){
							/** nếu như có thay đổi ngày tháng năm thì sẽ xóa data cũ */
							if(!getHisDeptStartDate().equals(userHisInfo.date_from)){
								deleteUserHisByDate(userHisInfo.date_from,usr.code,MasterConstants.MASTER_MKBN_DEPT_HIS);
							}
						}
						/** xoa neu nhu da co data tuong ung voi ngay thang nam tren man hinh*/
						deleteUserHisByDate(getHisDeptStartDate(),usr.code,MasterConstants.MASTER_MKBN_DEPT_HIS);
						/** tạo đối tượng dùng để update*/
						userInsert = getUserHistory(MasterConstants.MASTER_MKBN_DEPT_HIS,usr.code);
						/** thực thi update */
						mDatabaseAdapter.open();
						mDatabaseAdapter.insertToUserHisTable(userInsert);
						mDatabaseAdapter.close();
						/** chỉnh sửa lại ngày tháng năm start -end cho đúng */
						correctHisData(MasterConstants.MASTER_MKBN_DEPT_HIS,userInsert.user_code);
					}
					
				}
			}
			/*---------------------------------------------*/			
			currentStringValue="";
			/** cap nhat cho truong hop la nhom thay doi */
			if(getHisTeamCheck() && !getHisDeptStartDate().equals("")){
				/** xử lý cho từng user được chọn */
				for(User usr: _listUserChecked){
					/** kiem tra xem tri co thay doi so voi master User khong ? Neu khong thi khong update */
					currentStringValue = String.valueOf(usr.team) ;
					if(!currentStringValue.equals(getHisDeptNewTeam())){
						if(userHisInfo!=null){
							/** nếu như có thay đổi ngày tháng năm thì sẽ xóa data cũ */
							if(!getHisDeptStartDate().equals(userHisInfo.date_from)){
								deleteUserHisByDate(userHisInfo.date_from,usr.code,MasterConstants.MASTER_MKBN_TEAM_HIS);
							}
						}
						/** xoa neu nhu da co data tuong ung voi ngay thang nam tren man hinh*/
						deleteUserHisByDate(getHisDeptStartDate(),usr.code,MasterConstants.MASTER_MKBN_TEAM_HIS);
						/** tạo đối tượng dùng để update*/
						userInsert = getUserHistory(MasterConstants.MASTER_MKBN_TEAM_HIS,usr.code);
						
						/** thực thi update */
						mDatabaseAdapter.open();
						mDatabaseAdapter.insertToUserHisTable(userInsert);
						mDatabaseAdapter.close();
						/** chỉnh sửa lại ngày tháng năm start -end cho đúng */
						correctHisData(MasterConstants.MASTER_MKBN_TEAM_HIS,userInsert.user_code);
					}
					
				}
			}
			/*---------------------------------------------*/	
			currentStringValue="";
			/** cap nhat cho truong hop la cấp bậc thay doi */
			if(getHisPositionCheck() && !getHisDeptStartDate().equals("")){
				/** xử lý cho từng user được chọn */
				for(User usr: _listUserChecked){
					/** kiem tra xem tri co thay doi so voi master User khong ? Neu khong thi khong update */
					currentStringValue = String.valueOf(usr.position) ;
					if(!currentStringValue.equals(getHisDeptNewPositionCode())){
						if(userHisInfo!=null){
							/** nếu như có thay đổi ngày tháng năm thì sẽ xóa data cũ */
							if(!getHisDeptStartDate().equals(userHisInfo.date_from)){
								deleteUserHisByDate(userHisInfo.date_from,usr.code,MasterConstants.MASTER_MKBN_POSITION_HIS);
							}
						}
						/** xoa neu nhu da co data tuong ung voi ngay thang nam tren man hinh*/
						deleteUserHisByDate(getHisDeptStartDate(),usr.code,MasterConstants.MASTER_MKBN_POSITION_HIS);
						/** tạo đối tượng dùng để update*/
						userInsert = getUserHistory(MasterConstants.MASTER_MKBN_POSITION_HIS,usr.code);
						/** thực thi update */
						mDatabaseAdapter.open();
						mDatabaseAdapter.insertToUserHisTable(userInsert);
						mDatabaseAdapter.close();
						/** chỉnh sửa lại ngày tháng năm start -end cho đúng */
						correctHisData(MasterConstants.MASTER_MKBN_POSITION_HIS,userInsert.user_code);
					}
				}
			}
			/*---------------------------------------------*/	
			currentStringValue="";
			/** cap nhat cho truong hop la chung chi tieng Nhat thay doi */
			/** get Tag cua tab Other ( tieng nhat - phu cap nghiep vu)*/
			String TabOfFragmentOther =((HisUserMainActivity)getActivity()).getTabFragmentHisOther();
			/** get TAB tieng nhat - nghiep vu */
			HisUserOther fgHisUserOther= (HisUserOther)getActivity().getSupportFragmentManager().findFragmentByTag(TabOfFragmentOther);
			/** Neu co ton tai */
			if (fgHisUserOther!=null){
				/** trình độ nhật ngữ*/
				boolean japaneseCheck =fgHisUserOther.getHisJapaneseCheck();
				/** phu cap nghiep vu */
				boolean allowanceCheck =fgHisUserOther.getHisAllowance_BusinessCheck();
				/** salary*/
				boolean salaryCheck =fgHisUserOther.getHisSalaryCheck() ;
				
				String japaneseNew = fgHisUserOther.getHisNewJapanese();
				String allowanceNew = fgHisUserOther.getHisNewAllowance_Business();
				float salaryNew = fgHisUserOther.getHisNewSalary();
				
				/** cap nhat lich su cho truong hop la chung chi tieng Nhat thay doi */
				if(japaneseCheck && !getHisDeptStartDate().equals("")){
					/** xử lý cho từng user được chọn */
					for(User usr: _listUserChecked){
						/** kiem tra xem tri co thay doi so voi master User khong ? Neu khong thi khong update */
						currentStringValue = String.valueOf(usr.japanese) ;
						if(!currentStringValue.equals(japaneseNew)){
							if(userHisInfo!=null){
								/** nếu như có thay đổi ngày tháng năm thì sẽ xóa data cũ */
								if(!getHisDeptStartDate().equals(userHisInfo.date_from)){
									deleteUserHisByDate(userHisInfo.date_from,usr.code,MasterConstants.MASTER_MKBN_JAPANESE_HIS);
								}
							}
							/** xoa neu nhu da co data tuong ung voi ngay thang nam tren man hinh*/
							deleteUserHisByDate(getHisDeptStartDate(),usr.code,MasterConstants.MASTER_MKBN_JAPANESE_HIS);
							/** tạo đối tượng dùng để update*/
							userInsert = fgHisUserOther.getUserHistory(MasterConstants.MASTER_MKBN_JAPANESE_HIS,usr.code);
							/** thực thi update */
							mDatabaseAdapter.open();
							mDatabaseAdapter.insertToUserHisTable(userInsert);
							mDatabaseAdapter.close();
							/** chỉnh sửa lại ngày tháng năm start -end cho đúng */
							correctHisData(MasterConstants.MASTER_MKBN_JAPANESE_HIS,userInsert.user_code);
						}
						
					}
				}
				/*---------------------------------------------*/	
				currentStringValue ="";
				/** cap nhat lich su cho truong hop la tro cap nghiep vu thay doi */
				if(allowanceCheck && !getHisDeptStartDate().equals("")){
					/** xử lý cho từng user được chọn */
					for(User usr: _listUserChecked){
						/** kiem tra xem tri co thay doi so voi master User khong ? Neu khong thi khong update */
						currentStringValue = String.valueOf(usr.allowance_business) ;
						/**Neu nhu co su thay doi */
						Collator compare = Collator.getInstance(new Locale("vi", "vn"));
						int comparison = compare.compare(currentStringValue, allowanceNew);
						
						if(comparison!=0){
							if(userHisInfo!=null){
								/** nếu như có thay đổi ngày tháng năm thì sẽ xóa data cũ */
								if(!getHisDeptStartDate().equals(userHisInfo.date_from)){
									deleteUserHisByDate(userHisInfo.date_from,usr.code,MasterConstants.MASTER_MKBN_ALLOWANCE_BUSINESS_HIS);
								}
							}
							/** xoa neu nhu da co data tuong ung voi ngay thang nam tren man hinh*/
							deleteUserHisByDate(getHisDeptStartDate(),usr.code,MasterConstants.MASTER_MKBN_ALLOWANCE_BUSINESS_HIS);
							/** tạo đối tượng dùng để update*/
							userInsert = fgHisUserOther.getUserHistory(MasterConstants.MASTER_MKBN_ALLOWANCE_BUSINESS_HIS,usr.code);
							/** thực thi update */
							mDatabaseAdapter.open();
							mDatabaseAdapter.insertToUserHisTable(userInsert);
							mDatabaseAdapter.close();
							/** chỉnh sửa lại ngày tháng năm start -end cho đúng */
							correctHisData(MasterConstants.MASTER_MKBN_ALLOWANCE_BUSINESS_HIS,userInsert.user_code);
						}
						
					}
				}
				/*---------------------------------------------*/	
				currentFloatValue =0;
				/** cap nhat lich su cho truong hop la salary thay doi */
				if(salaryCheck && !getHisDeptStartDate().equals("")){
					/** xử lý cho từng user được chọn */
					for(User usr: _listUserChecked){
						/** kiem tra xem tri co thay doi so voi master User khong ? Neu khong thi khong update */
						currentFloatValue = usr.salary_notallowance;
						if(currentFloatValue!=salaryNew){
							if(userHisInfo!=null){
								/** nếu như có thay đổi ngày tháng năm thì sẽ xóa data cũ */
								if(!getHisDeptStartDate().equals(userHisInfo.date_from)){
									deleteUserHisByDate(userHisInfo.date_from,usr.code,MasterConstants.MASTER_MKBN_SALARY_HIS);
								}
							}
							/** xoa neu nhu da co data tuong ung voi ngay thang nam tren man hinh*/
							deleteUserHisByDate(getHisDeptStartDate(),usr.code,MasterConstants.MASTER_MKBN_SALARY_HIS);
							/** tạo đối tượng dùng để update*/
							userInsert = fgHisUserOther.getUserHistory(MasterConstants.MASTER_MKBN_SALARY_HIS,usr.code);
							/** thực thi update */
							mDatabaseAdapter.open();
							mDatabaseAdapter.insertToUserHisTable(userInsert);
							mDatabaseAdapter.close();
							/** chỉnh sửa lại ngày tháng năm start -end cho đúng */
							correctHisData(MasterConstants.MASTER_MKBN_SALARY_HIS,userInsert.user_code);
						}
						
					}
				}
				
				
				/*---------------------------------------------*/	
			}
			/** cap nhat lai tri moi nhat cua phong ban ---cho nhan vien */
			for(User usr: _listUserChecked){
				updateUserMaster(usr.code);
			}
    		
    	}catch ( Exception e){
    		Log.v(TAG,e.getMessage());
    		/** đóng connection */
    		mDatabaseAdapter.close();
    	}
    }
    
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * 
     * cap nhat cac thong tin tren man hinh
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    public UserHistory getUserHistory(int mkbn, int user_code){
		UserHistory userInsert; 
		userInsert = new UserHistory();
		
		userInsert.mkbn = mkbn;
		userInsert.user_code = user_code;
		userInsert.date_from = getHisDeptStartDate();
		userInsert.new_dept_code = getHisDeptNewDeptCode();
		userInsert.new_dept_name = getHisDeptNewDept();
		userInsert.new_team_code = getHisDeptNewTeamCode();
		userInsert.new_team_name = getHisDeptNewTeam();
		userInsert.new_position_code = getHisDeptNewPositionCode();
		userInsert.new_position_name = getHisDeptNewPosition();		
		userInsert.note=getHisNote(); 
		return userInsert;
	}
    
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * 
     * xóa thông tin lịch sử dựa vào code nhân viên và ngày tháng năm hữu hiệu.
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/ 
    public void deleteUserHisByDate(String datefrom, int user_code, int hisType){
    	String xWhere ="";
		xWhere =xWhere	+	" AND " + DatabaseAdapter.KEY_USER_CODE + " = " + user_code;
		xWhere =xWhere  + 	" AND " + DatabaseAdapter.KEY_DATE_FROM + " = '" + DateTimeUtil.formatDate2String(datefrom , MasterConstants.DATE_VN_FORMAT, MasterConstants.DATE_JP_FORMAT) + "'";
		try{
			mDatabaseAdapter.open();
			switch(hisType){
				case MasterConstants.MASTER_MKBN_DEPT_HIS:
					mDatabaseAdapter.deleteUserHisDeptByCode(xWhere);
					break;
				case MasterConstants.MASTER_MKBN_TEAM_HIS:
					mDatabaseAdapter.deleteUserHisTeamByCode(xWhere);
					break;
				case MasterConstants.MASTER_MKBN_POSITION_HIS:
					mDatabaseAdapter.deleteUserHisPositionByCode(xWhere);
					break;
			}
			
			mDatabaseAdapter.close();
		}catch(Exception e ){
			mDatabaseAdapter.close();
		}
    }
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * 
     * chinh sua lai data cho dung theo nhu trinh tu cua thang nam thay doi
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    @SuppressWarnings("deprecation")
	public void correctHisData(int type,int user_code){
    	String xWhere="";
    	String xOrderBy="";
    	
    	xWhere = " AND " + DatabaseAdapter.KEY_USER_CODE + " = " + user_code; 
    	xOrderBy = "date("+DatabaseAdapter.KEY_DATE_FROM + ") ASC ";
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
    				cal.set(dt.getYear()+ 1900, dt.getMonth(), dt.getDate());
    				
    				cal.add(Calendar.DATE, -1);
    				String date =DateTimeUtil.convertDateToString(cal.getTime(),MasterConstants.DATE_VN_FORMAT);
    				userhis.get(i).date_to =date;
    				//userhis.get(i).date_to =hisnext.date_from;
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
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * 
     * cập nhật lại thông tin cho master nhân viên
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    public void updateUserMaster(int user_code){
    	/** cap nhat lai cac thong tin phong ban -team-chuc vu moi nhat cho user */
    	try{
    		List<User> userList = mConvertCursorToListString.getUserList(" AND " + DatabaseAdapter.KEY_CODE + "=" + user_code);
        	if(userList.size()>0){
        		/** update */
        		User usr = userList.get(0);
        		/*kiem tra xem co check phong ban khong ?*/
        		if(getHisDeptCheck()){
        			Dept dept = getNewestUserHisDeptInfo(user_code);
        			usr.dept =dept.code;
            		usr.dept_name = dept.name;
        		}
        		/*kiem tra xem co check team nhom khong ?*/
        		if(getHisTeamCheck()){
        			Team team = getNewestUserHisTeamInfo(user_code);
        			usr.team = team.code;
            		usr.team_name = team.name;
        		}
        		/*kiem tra xem co check chuc vu khong ?*/
        		if(getHisPositionCheck()){
        			Position position= getNewestUserHisPositionInfo(user_code);
        			usr.position = position.code;
            		usr.position_name = position.name;//PositionFJNCode
        		}
        		
        		/** get Tag cua tab Other ( tieng nhat - phu cap nghiep vu)*/
    			String TabOfFragmentOther =((HisUserMainActivity)getActivity()).getTabFragmentHisOther();
    			/** get TAB tieng nhat - nghiep vu */
    			HisUserOther fgHisUserOther= (HisUserOther)getActivity().getSupportFragmentManager().findFragmentByTag(TabOfFragmentOther);
    			/** Neu co ton tai */
    			if (fgHisUserOther!=null){
    				/** trình độ nhật ngữ*/
    				boolean japaneseCheck =fgHisUserOther.getHisJapaneseCheck();
    				/** phu cap nghiep vu */
    				boolean allowanceCheck =fgHisUserOther.getHisAllowance_BusinessCheck();
    				/** salary*/
    				boolean salaryCheck =fgHisUserOther.getHisSalaryCheck() ;
    				
    				String japaneseNew = fgHisUserOther.getHisNewJapanese();
    				String allowanceNew = fgHisUserOther.getHisNewAllowance_Business();
    				float salaryNew = fgHisUserOther.getHisNewSalary();
    				
    				/*kiem tra xem co check chung chi tieng Nhat khong ?*/
            		if(japaneseCheck){
            			usr.japanese = japaneseNew;
            		}
            		/*kiem tra xem co check  phu cap nghiep vu khong ?*/
            		if(allowanceCheck){
            			usr.allowance_business = allowanceNew;
            		}
            		/*kiem tra xem co check  salary khong ?*/
            		if(salaryCheck){
            			usr.salary_notallowance = salaryNew;
            		}
    			}
    			
        		/** update xuong DB */
        		mDatabaseAdapter.open();
        		mDatabaseAdapter.editToUserTable(usr);
        		mDatabaseAdapter.close();
        	}
    	}catch(Exception e){
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
    	xOrderBy = "date(" + DatabaseAdapter.KEY_DATE_FROM + ") DESC ";
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
    	xOrderBy = "date(" + DatabaseAdapter.KEY_DATE_FROM + ") DESC ";
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
    	xOrderBy = "date(" + DatabaseAdapter.KEY_DATE_FROM + ") DESC ";
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
    	xOrderBy = "date(" + DatabaseAdapter.KEY_DATE_FROM + ") DESC ";
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
    	xOrderBy = "date(" + DatabaseAdapter.KEY_DATE_FROM + ") DESC ";
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
     * getUserCode
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    public int  getUserCode(int userCode){
    	int code ;
    	if (userCode==-1){
    		/** tạo mới */
    		mDatabaseAdapter.open();
    		code= mDatabaseAdapter.getMaxCodeUser();
    		mDatabaseAdapter.close();
    	}else{
    		/** chỉnh sửa */
    		code =userCode;
    	}
    	return code;
    }
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * 
     * setValueTabDeptTeam
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    public void setValueTabMain(UserHistory item){
		/**thông tin của tab phong ban-nhom to*/
		setUserCode(item.user_code);
		setFullName(item.full_name);
		setHisDeptStartDate(item.date_from);
		setHisDeptNewDeptCode(item.new_dept_code);
		setHisDeptNewDept(item.new_dept_name);
		setHisDeptNewTeamCode(item.new_team_code);
		setHisDeptNewTeam(item.new_team_name);
		setHisDeptNewPositionCode(item.new_position_code);
		setHisDeptNewPosition(item.new_position_name);
		setHisNote(item.note);
    }
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * 
     * hiển thị image 
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    public void viewUserImg(){
    	imageView = (ImageView)getView().findViewById(R.id.imgUser);
    	/** nếu không có hình */
    	if (getImgFullPath()==null || getImgFullPath().equals("")){
    		imageView.setImageResource(R.drawable.user);
    		return;
    	}
    	DecodeTask task = new DecodeTask(imageView);
        task.execute(getImgFullPath());
    }
    
    public void setValueTabOther(User item){
    	String TabOfFragmentOther =HisUserMainActivity.getTabFragmentHisOther();
    	HisUserOther fgUserOther = (HisUserOther)getFragmentManager().findFragmentByTag(TabOfFragmentOther);
    	if (fgUserOther!=null){
			/**thông tin của tab tieng nhat - phu cap nghiep vu*/
			
		}
    }
    
    public void showDialogUser(){
		/** Getting the fragment manager */
		_Users = mGetListDataHelper.getArrayUser("");
    	if (_Users.length==0){
    		return ;
    	}
    	
		FragmentManager managerSearchItemUser =getActivity().getFragmentManager();
		
		/** Instantiating the DialogFragment class */
		//AlertDialogRadio alertPositionGroup = new AlertDialogRadio("",dataAllowance_Business,R.id.btnUserAllowance_business);
		AlertDialogRadio alertUser = new AlertDialogRadio("",_Users,MasterConstants.BTN_DIALOG_USER_LIST,_listUser, true);
		/** Creating a bundle object to store the selected item's index */
		Bundle bUser  = new Bundle();
		
		/** Storing the selected item's index in the bundle object */
		bUser.putInt("position", 0);
	
		/** Setting the bundle object to the dialog fragment object */
		alertUser.setArguments(bUser);
		
		/** Creating the dialog fragment object, which will in turn open the alert dialog window */
		alertUser.show(managerSearchItemUser, MasterConstants.TAB_DIALOG_TAG);	
		
    }
    
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * Defining button click listener for the OK button of the alert dialog window 
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    public void onPositiveClick(int position, int clickBtn, Bundle bundle) {
    	/** Setting the selected android version in the textview */
    	switch (clickBtn) {
    		/** Phân nhánh xử lý tùy theo button mà đã được clicked*/
	    	case MasterConstants.BTN_DIALOG_USER_LIST:
	    		/** lấy data phòng ban từ bundle */
	    		User[] userArr = (User[])bundle.getParcelableArray(MasterConstants.TAB_DIALOG_BUNDLE);
	    		//tbtSearchItemDept.setChecked(false);
	    		if (userArr.length==0){
	    			_listUser.clear();
	    		}else{
	    			_listUser.clear();
	    			/** xác định các code phòng ban được check */
	    			for(int i=0 ; i<userArr.length;i++ ){
	    				if(userArr[i].isselected==true){
	    					/** add đối tượng vào list */
	    					_listUser.add((User)userArr[i]);
	    				}
	    			}
	    			setUserCount(_listUser.size());
	    			/** Kiem tra xem neu chon chi 1 nguoi thi co the cho edit thong tin luong
	    			 * nguoc lai thi khong the edit luong
	    			 */
	    			/** get Tag cua tab Other ( tieng nhat - phu cap nghiep vu)*/
	    			String TabOfFragmentOther =((HisUserMainActivity)getActivity()).getTabFragmentHisOther();
	    			/** get TAB tieng nhat - nghiep vu */
	    			HisUserOther fgHisUserOther= (HisUserOther)getActivity().getSupportFragmentManager().findFragmentByTag(TabOfFragmentOther);
	    			/** Neu co ton tai */
	    			if (fgHisUserOther!=null){
    					if(_listUser.size()==1){
    						fgHisUserOther.setHisSalaryCheckStatus(true);
    						/** get cac thong tin lien quan */
		    			}else{
		    				fgHisUserOther.setHisSalaryCheckStatus(false);
		    			}
	    			}
	    			
	    		}
	    		break;
	    	case R.id.btnUserHisDeptNewDept:
	    		/** gán lại vị trí của item mà đã chọn */
	    		this.positionDept = position;
	    		/** gán tên người đã chọn tại màn hình poup */
	    		txtUserHisDeptNewDept.setText(_depts[position].ryaku);    
	    		txtUserHisDeptNewDeptCode.setText(String.valueOf(_depts[position].code));
	    		break;
	    	case R.id.btnUserHisDeptNewTeam:
	    		/** gán lại vị trí của item mà đã chọn */
	    		this.positionTeam = position;
	    		/** gán tên người đã chọn tại màn hình poup */
	    		txtUserHisDeptNewTeam.setText(_teams[position].ryaku);    
	    		txtUserHisDeptNewTeamCode.setText(String.valueOf(_teams[position].code));
	    		break;
	    	case R.id.btnUserHisDeptNewPosition:
	    		/** gán lại vị trí của item mà đã chọn */
	    		this.positionPos = position;
	    		/** gán chức vụ mới chọn tại màn hình poup */
	    		txtUserHisDeptNewPosition.setText(_Positions[position].yobi_text1);	//PositionFJNCode
	    		txtUserHisDeptNewPositionCode.setText(String.valueOf(_Positions[position].code));
	    		break;
    	}
    }
    
    @Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
    	switch (buttonView.getId()){
		case R.id.chkUserHisDeptNewDept:
			if(isChecked){
				txtUserHisDeptNewDept.setEnabled(true);
				btnUserHisDeptNewDept.setEnabled(true);
			}else{
				txtUserHisDeptNewDept.setEnabled(false);
				btnUserHisDeptNewDept.setEnabled(false);
			}
			break;
		case R.id.chkUserHisDeptNewTeam:
			if(isChecked){
				txtUserHisDeptNewTeam.setEnabled(true);
				btnUserHisDeptNewTeam.setEnabled(true);
			}else{
				txtUserHisDeptNewTeam.setEnabled(false);
				btnUserHisDeptNewTeam.setEnabled(false);
			}
			break;
		case R.id.chkUserHisDeptNewPosition:
			if(isChecked){
				txtUserHisDeptNewPosition.setEnabled(true);
				btnUserHisDeptNewPosition.setEnabled(true);
			}else{
				txtUserHisDeptNewPosition.setEnabled(false);
				btnUserHisDeptNewPosition.setEnabled(false);
			}
			break;
    	}
		
	}
    
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * 
     * Lấy về code phòng ban hiện tại ở master user
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    public int  getCurrentDeptCode(int user_code){
    	int code=0;
    	List<User> userList = mConvertCursorToListString.getUserList(" AND " + DatabaseAdapter.KEY_CODE + "=" + user_code);
    	if(userList.size()>0){
    		User usr = userList.get(0);
    		code = usr.dept;
    	}else{
    		
    	}
    	return code;
    }
    
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * 
     * Lấy về code nhóm hiện tại ở master user
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    public int  getCurrentTeamCode(int user_code){
    	int code=0;
    	List<User> userList = mConvertCursorToListString.getUserList(" AND " + DatabaseAdapter.KEY_CODE + "=" + user_code);
    	if(userList.size()>0){
    		User usr = userList.get(0);
    		code = usr.team;
    	}else{
    		
    	}
    	return code;
    }
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * 
     * Lấy về code chức vụ hiện tại ở master user
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    public int  getCurrentPositionCode(int user_code){
    	int code=0;
    	List<User> userList = mConvertCursorToListString.getUserList(" AND " + DatabaseAdapter.KEY_CODE + "=" + user_code);
    	if(userList.size()>0){
    		User usr = userList.get(0);
    		code = usr.position;
    	}else{
    		
    	}
    	return code;
    }
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * 
     * setUserCode
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    public void  setUserCode(int userCode){
    	txtUserCode.setText(String.valueOf(userCode) );
    }
    
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * 
     * getUserCode
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    public int getUserCode(){
    	if(txtUserCode.getText().equals("")){
    		return 0;
    	}else{
    		return Integer.parseInt(txtUserCode.getText().toString());
    	}
    }
    
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * 
     * lấy path của image
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    public String   getImgFullPath(){
    	return imgUserFullPath.getText().toString();
    }
    
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * 
     * setting path của image
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    public void  setImgFullPath(String value){
    	imgUserFullPath.setText(value );
    	imgPathSave = value;
    	if(userHisInfo!=null){
    		userHisInfo.img_fullpath =value;
    	}
    }
    
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * 
     * getFullName
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    public String   getFullName(){
    	return txtUserFullName.getText().toString();
    }
    
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * 
     * setFullName
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    public void  setFullName(String value){
    	txtUserFullName.setText(value );
    	getActivity().setTitle(value);
    }
    
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * 
     * setHisDeptStartDate
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    public void  setHisDeptStartDate(String value){
    	txtUserHisDeptStartDate.setText(value );
    }
    
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * 
     * getHisDeptStartDate
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    public String   getHisDeptStartDate(){
    	return txtUserHisDeptStartDate.getText().toString();
    }

    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * 
     * setHisDeptNewDept
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    public void  setHisDeptNewDept(String value){
    	txtUserHisDeptNewDept.setText(value );
    }
    
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * 
     * getHisDeptNewDept
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    public String   getHisDeptNewDept(){
    	if(!getHisDeptCheck()){
    		return "";
    	}
    	return txtUserHisDeptNewDept.getText().toString();
    }
    
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * 
     * setHisDeptNewDeptCode
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    public void  setHisDeptNewDeptCode(int value){
    	txtUserHisDeptNewDeptCode.setText(String.valueOf(value));
    }
    
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * 
     * getHisDeptNewDeptCode
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    public int   getHisDeptNewDeptCode(){
    	/*if(!getHisDeptCheck()){
    		return 0;
    	}*/
    	if(txtUserHisDeptNewDeptCode.getText()==null || txtUserHisDeptNewDeptCode.getText().equals("")){
    		return 0;
    	}else{
    		return Integer.parseInt(txtUserHisDeptNewDeptCode.getText().toString());
    	}
    	
    }
    
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * 
     * setHisDeptNewTeam
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    public void  setHisDeptNewTeam(String value){
    	txtUserHisDeptNewTeam.setText(value );
    }
    
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * 
     * getHisDeptNewTeam
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    public String   getHisDeptNewTeam(){
    	/*if(!getHisTeamCheck()){
    		return "";
    	}*/
    	return txtUserHisDeptNewTeam.getText().toString();
    }
    
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * 
     * setHisDeptNewTeamCode
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    public void  setHisDeptNewTeamCode(int value){
    	txtUserHisDeptNewTeamCode.setText( String.valueOf( value ));
    }
    
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * 
     * getHisDeptNewTeamCode
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    public int   getHisDeptNewTeamCode(){
    	/*if(!getHisTeamCheck()){
    		return 0;
    	}*/
    	if(txtUserHisDeptNewTeamCode.getText()==null || txtUserHisDeptNewTeamCode.getText().equals("")){
    		return 0;
    	}else{
    		return Integer.parseInt(txtUserHisDeptNewTeamCode.getText().toString());
    	}
    	
    }
    
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * 
     * setHisDeptNewPosition
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    public void  setHisDeptNewPosition(String value){
    	txtUserHisDeptNewPosition.setText(value );
    }
    
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * 
     * getHisDeptNewPosition
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    public String   getHisDeptNewPosition(){
    	/*if(!getHisPositionCheck()){
    		return "";
    	}*/
    	return txtUserHisDeptNewPosition.getText().toString();
    }
    
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * 
     * setHisDeptNewPositionCode
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    public void  setHisDeptNewPositionCode(int value){
    	txtUserHisDeptNewPositionCode.setText( String.valueOf( value ));
    }
    
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * 
     * getHisDeptNewPositionCode
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    public int   getHisDeptNewPositionCode(){
    	/*if(!getHisPositionCheck()){
    		return 0;
    	}*/
    	if(txtUserHisDeptNewPositionCode.getText()==null || txtUserHisDeptNewPositionCode.getText().equals("")){
    		return 0;
    	}else{
    		return Integer.parseInt(txtUserHisDeptNewPositionCode.getText().toString());
    	}
    }
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * 
     * getHisDeptCheck
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    public boolean getHisDeptCheck(){
    	return chkUserHisDeptNewDept.isChecked();
    }
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * 
     * setHisDeptCheck
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    public void  setHisDeptCheck(boolean value){
    	chkUserHisDeptNewDept.setChecked(value );
    }
    
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * 
     * getHisTeamCheck
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    public boolean getHisTeamCheck(){
    	return chkUserHisDeptNewTeam.isChecked();
    }
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * 
     * setHisTeamCheck
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    public void  setHisTeamCheck(boolean value){
    	chkUserHisDeptNewTeam.setChecked(value );
    }
    
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * 
     * getHisPositionCheck
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    public boolean getHisPositionCheck(){
    	return chkUserHisDeptNewPosition.isChecked();
    }
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * 
     * setHisPositionCheck
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    public void  setHisPositionCheck(boolean value){
    	chkUserHisDeptNewPosition.setChecked(value );
    }
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * 
     * setHisNote
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    public void  setHisNote(String value){
    	txtUserHisDeptNote.setText(value );
    }
    
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * 
     * getHisNote
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    public String   getHisNote(){
    	return txtUserHisDeptNote.getText().toString();
    }
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * 
     * gan so user duoc chon 
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    public void setUserCount(int value){
    	txtUserHisDept_count.setText(String.valueOf(value));
    }
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * 
     * setIsFirstRun
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    public void setIsFirstRun(boolean value){
    	this.isFirstRun=value;
    }
	
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * 
     * get ngày tháng năm hiệu lực
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    public String getDateFrom(){
    	return getHisDeptStartDate();
    }
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * 
     * get list user được check
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    public List<User> getCheckedUserList(){
    	return getUserListChecked(_listUser);
    }
    
    
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * save thong tin user da input 
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
	public void SaveDbEntryPoint(){
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(this.getActivity());
		/** Setting Dialog Title */
        alertDialog.setTitle(getResources().getString(R.string.titleSave));

        /** Setting Dialog Message */
        alertDialog.setMessage("Bạn có cập nhật thông tin nhân viên này không?");
        
        /** Setting Icon to Dialog*/
        alertDialog.setIcon(R.drawable.save_back);
        /** Setting Positive "Yes" Button */
        alertDialog.setPositiveButton(getResources().getText(R.string.titleYes), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
            	/** Lưu vào DB */
            	saveHisUserToDbTask();
            }
        });
 
        /** Setting Negative "NO" Button */
        alertDialog.setNegativeButton(getResources().getText(R.string.titleNo), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            dialog.cancel();
            }
        });
 
        /** Showing Alert Message */
        alertDialog.create().show();
        
	}
	
	private void saveHisUserToDbTask(){
		new AsyncTask<Void, Void, Boolean>()
	    {
	        ProgressDialog progressDialog;
	
	        @Override
			protected void onPreExecute() {
				progressDialog = new ProgressDialog(getActivity());
				progressDialog.setCancelable(false);
				progressDialog.setTitle("Lưu dữ liệu");
				progressDialog.setMessage("Xin vui lòng chờ...");
				progressDialog.show();
			}
	        
	        @Override
	        protected Boolean doInBackground(Void... params)
	        {
	            try
	            {
	            	
	            	/** Lưu vào DB */
					saveUserToDb();
					/** trả về kết quả*/
					Intent intent = new Intent();
					Bundle bundle = new Bundle();
					bundle.putInt("btn", R.id.btnUserSave);
					bundle.putInt(MasterConstants.TAB_USER_HIS_TAG, 0);
					bundle.putInt(MasterConstants.EXP_USER_GROUP_TAG, HisUserMainActivity.currentGroup);
					intent.putExtras(bundle);
					getActivity().setResult(getActivity().RESULT_OK, intent);
					getActivity().finish();
					
	            }
	            catch (Exception e)
	            {
	            	return false;
	            }
	            return true;
	        }
	
	        @Override
	        protected void onPostExecute(Boolean result)
	        {
	        	progressDialog.cancel();
	        	String message="";
				if (result==true){
					message="Lưu thành công.";
				}else{
					message="Lưu thất bại.";
				}
	        	AlertDialog.Builder b = new AlertDialog.Builder(getActivity());
	            b.setTitle("Lưu dữ liệu");
	
	                b.setMessage(message);
	             
	                b.setPositiveButton(getString(android.R.string.ok),
	                        new DialogInterface.OnClickListener()
	                        {
	
	                            @Override
	                            public void onClick(DialogInterface dlg, int arg1)
	                            {
	                                dlg.dismiss();
	                            }
	                        });
	                b.create().show();
	            }
	        }.execute();
	}
}
