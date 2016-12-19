/**
 * Copyright (c) 2012 Fujinet Software Department 5 ( USSOL team)  
 * See the file license.txt for copying permission.
*/  

package com.ussol.employeetracker;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.ussol.employeetracker.EditUserWork.SelectDateFragment;
import com.ussol.employeetracker.helpers.AlertDialogRadio;
import com.ussol.employeetracker.helpers.ConvertCursorToArrayString;
import com.ussol.employeetracker.helpers.DatabaseAdapter;
import com.ussol.employeetracker.helpers.GetListDataHelper;
import com.ussol.employeetracker.models.CustomerGroup;
import com.ussol.employeetracker.models.Dept;
import com.ussol.employeetracker.models.MasterConstants;
import com.ussol.employeetracker.models.Position;
import com.ussol.employeetracker.models.PositionGroup;
import com.ussol.employeetracker.models.User;
import com.ussol.employeetracker.models.UserCustomerGroup;
import com.ussol.employeetracker.models.UserPositionGroup;

import android.R.bool;
import android.R.integer;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class EditUserOther extends Fragment implements OnClickListener , OnSeekBarChangeListener {
	
	private int mYear;
    private int mMonth;
    private int mDay;
    static final int DATE_DIALOG_ID = 0;
    private EditText  txtUserJananese , txtUserAllowance_Business,txtUserAllowance_Room ,txtUserPositionGroup, txtUserNote;
    private TextView lblUserProgram,lblUserDetailDesign;
    //private Switch txtUserBusinessKbn ;
    private SeekBar sbaUserProgram,sbaUserDetailDesign;
    private Button btnUserSave , btnUserCancel, btnUserJapaneseLevel , btnUserAllowance_Business,btnUserAllowance_Room, btnUserPositionGroup;
    private ToggleButton tbtUserPositionGroup,tbtUserCustomerGroup;
    private CheckBox chkUserDeveloper, chkUserHonyaku , chkUserKbnOther;
    /** Lưu lại vị trí mà đã chọn từ list radio */
	int positionJananeseLevel = 0;
	int positionAllowance_Business = 0;
	int positionAllowance_Room = 0;
	int positionGroup = 0;
	int positionCustomerGroup = 0;
	/** thông tin các nhóm chức danh hiển thị tại màn hình dialog  */
	String[] dataPosition ;
	PositionGroup[] _PositionGroup=null;
	List<PositionGroup> _listPositionGroup =null;
	
	CustomerGroup[] _CustomerGroup=null;
	List<CustomerGroup> _listCustomerGroup =null;
	
	
	/** chuyển đổi Cursor thành string array */
	ConvertCursorToArrayString mConvertCursorToArrayString;
	/** thông tin bằng cấp tiếng Nhật  */
	String[] dataJapaneseLevel = MasterConstants.JAPANESE_LEVEL ;
	
	/** thông tin phụ cấp nghiệp vụ : các bậc*/
	String[] dataAllowance_Business=MasterConstants.ALLOWANCE_BUSINESS_LEVEL ;
	/** thông tin phụ cấp phòng chuyên biệt*/
	String[] dataAllowance_Room=MasterConstants.ALLOWANCE_ROOM_LEVEL ;

	/** Button nào được click */
	private static  int btnClicked ;
	/** kết nối Database */
	DatabaseAdapter mDatabaseAdapter;
	/** đối tượng hiển thị tại các màn hình search*/
	GetListDataHelper mGetListDataHelper;
	/** đối tượng nhóm chức danh trả vế từ màn hình search*/
	List<UserPositionGroup> userPositionGroupList;
	/** đối tượng nhóm khách hàng trả vế từ màn hình search*/
	List<UserCustomerGroup> userCustomerGroupList;
	
	/** Trị code nhân viên  */
	private int nCode = -1;
	public static User userInfo ;
	private boolean isFirstRun = true;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		isFirstRun=true;
	}

	
	@SuppressWarnings("static-access")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		ViewGroup root = (ViewGroup) inflater.inflate(R.layout.activity_user_edit_other, null);		
		/** setting tab để có thể truy xuất từ các Tab khác*/
		((EditUserMainActivity)getActivity()).setTabFragmentOther(getTag());
		return root;
	}
	/**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
	 * onActivityCreated
	 * 
	 ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		nCode=EditUserMainActivity.nCode;
		userInfo =EditUserMainActivity.userInfo;
		mDatabaseAdapter = new DatabaseAdapter(getActivity());
		/** khởi tạo đối tượng */
		mGetListDataHelper = new GetListDataHelper(getActivity());
		/** khởi tạo list dùng để lưu các item được chọn tại dialog chức danh*/
		if(userPositionGroupList!=null){
			if(userPositionGroupList.size()==0 ){
				userPositionGroupList= new ArrayList<UserPositionGroup>();
				userPositionGroupList =mGetListDataHelper.getListUserPositionGroup(nCode);
			}
		}
		else
		{
			userPositionGroupList= new ArrayList<UserPositionGroup>();
			userPositionGroupList =mGetListDataHelper.getListUserPositionGroup(nCode);
		}
		/** khởi tạo list dùng để lưu các item được chọn tại dialog khách hàng*/
		if(userCustomerGroupList!=null){
			if(userCustomerGroupList.size()==0 ){
				userCustomerGroupList= new ArrayList<UserCustomerGroup>();
				userCustomerGroupList =mGetListDataHelper.getListUserCustomerGroup(nCode);
			}
		}
		else
		{
			userCustomerGroupList= new ArrayList<UserCustomerGroup>();
			userCustomerGroupList =mGetListDataHelper.getListUserCustomerGroup(nCode);
		}
		
		/** get các control date và gán sự kiện khi click vào textbox */
		getControl();
		settingListener();
		settingInputType();
		
		if (nCode==-1){
			/** trường hợp tạo mới*/
			/** setting code user*/
			
		}else if (nCode==0){
			/** hiển thị copy data*/
			if (isFirstRun){
				setValueTabOther(userInfo);
			}
		}else{
			/** hiển thị chi tiết */
			if (isFirstRun){
				setValueTabOther(userInfo);
			}
		}
		isFirstRun = false;
		/** hiển thị lại image */
		String tabOfFragmentBasic=((EditUserMainActivity)getActivity()).getTabFragmentBasic();
		EditUserBasic fgUserBasic= (EditUserBasic)getActivity().getSupportFragmentManager().findFragmentByTag(tabOfFragmentBasic);
		if (fgUserBasic!=null){
			/**dept*/
			fgUserBasic.viewUserImg();
		}
		
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
        String TabOfFragmentWork =EditUserMainActivity.getTabFragmentWork();
		   
    	EditUserWork fgUserWork = (EditUserWork)getFragmentManager().findFragmentByTag(TabOfFragmentWork);
    	if(fgUserWork!=null){
    		fgUserWork.onPause();
    	}
	}
	
	/**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
	 * onClick
	 * 
	 ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
	@Override
	public void onClick(View v) {
		btnClicked = v.getId();
		/** phân nhánh xử lý theo từng button*/
		switch (v.getId()) {
		case R.id.btnUserJapanese:
			/**hiển thị màn hình để chọn phong ban */
			/** Getting the fragment manager */
			FragmentManager manager =getActivity().getFragmentManager();
			
			/** Instantiating the DialogFragment class */
			AlertDialogRadio alert = new AlertDialogRadio("",dataJapaneseLevel,R.id.btnUserJapanese);
			
			/** Creating a bundle object to store the selected item's index */
			Bundle b  = new Bundle();
			
			/** Storing the selected item's index in the bundle object */
			b.putInt("position", positionJananeseLevel);
			
			/** Setting the bundle object to the dialog fragment object */
			alert.setArguments(b);
			
			/** Creating the dialog fragment object, which will in turn open the alert dialog window */
			alert.show(manager, MasterConstants.TAB_DIALOG_TAG);	
			break;
		case R.id.btnUserAllowance_business:
			/**hiển thị màn hình để chọn nhóm*/
			/** Getting the fragment manager */
			FragmentManager managerTeam =getActivity().getFragmentManager();
			
			/** Instantiating the DialogFragment class */
			AlertDialogRadio alertTeam = new AlertDialogRadio("",dataAllowance_Business,R.id.btnUserAllowance_business);
			
			/** Creating a bundle object to store the selected item's index */
			Bundle bTeam  = new Bundle();
			
			/** Storing the selected item's index in the bundle object */
			bTeam.putInt("position", positionAllowance_Business);
			
			/** Setting the bundle object to the dialog fragment object */
			alertTeam.setArguments(bTeam);
			
			/** Creating the dialog fragment object, which will in turn open the alert dialog window */
			alertTeam.show(managerTeam, MasterConstants.TAB_DIALOG_TAG);	
			break;
			
		case R.id.btnUserAllowance_Room:
			/**hiển thị màn hình để chọn phụ cấp phòng chuyên biệt*/
			/** Getting the fragment manager */
			FragmentManager managerAllowance_Room =getActivity().getFragmentManager();
			
			/** Instantiating the DialogFragment class */
			AlertDialogRadio alertAllowance_Room = new AlertDialogRadio("",dataAllowance_Room,R.id.btnUserAllowance_Room);
			
			/** Creating a bundle object to store the selected item's index */
			Bundle bAllowance_Room  = new Bundle();
			
			/** Storing the selected item's index in the bundle object */
			bAllowance_Room.putInt("position", positionAllowance_Room);
			
			/** Setting the bundle object to the dialog fragment object */
			alertAllowance_Room.setArguments(bAllowance_Room);
			
			/** Creating the dialog fragment object, which will in turn open the alert dialog window */
			alertAllowance_Room.show(managerAllowance_Room, MasterConstants.TAB_DIALOG_TAG);	
			break;
			
		case R.id.tbtUserPositionGroup:
			/**hiển thị màn hình để chọn nhóm*/
			/** Getting the fragment manager */
			tbtUserPositionGroup.setChecked(false);
			_PositionGroup  = mGetListDataHelper.getArrayPositionGroup("");
	    	if (_PositionGroup.length==0){
	    		return;
	    	}
	    	
			FragmentManager managerPositionGroup =getActivity().getFragmentManager();
			
			/** Instantiating the DialogFragment class */
			//AlertDialogRadio alertPositionGroup = new AlertDialogRadio("",dataAllowance_Business,R.id.btnUserAllowance_business);
			AlertDialogRadio alertPositionGroup = new AlertDialogRadio("",_PositionGroup,R.id.tbtUserPositionGroup,userPositionGroupList, true);
			/** Creating a bundle object to store the selected item's index */
			Bundle bPositionGroup  = new Bundle();
			
			/** Storing the selected item's index in the bundle object */
			bPositionGroup.putInt("position", positionGroup);
		
			/** Setting the bundle object to the dialog fragment object */
			alertPositionGroup.setArguments(bPositionGroup);
			
			/** Creating the dialog fragment object, which will in turn open the alert dialog window */
			alertPositionGroup.show(managerPositionGroup, MasterConstants.TAB_DIALOG_TAG);	
			break;
			
		case R.id.tbtUserCustomerGroup:
			/**hiển thị màn hình để chọn nhóm*/
			/** Getting the fragment manager */
			tbtUserCustomerGroup.setChecked(false);
			_CustomerGroup  = mGetListDataHelper.getArrayCustomerGroup("");
	    	if (_CustomerGroup.length==0){
	    		return;
	    	}
	    	
			FragmentManager managerCustomerGroup =getActivity().getFragmentManager();
			
			/** Instantiating the DialogFragment class */
			//AlertDialogRadio alertPositionGroup = new AlertDialogRadio("",dataAllowance_Business,R.id.btnUserAllowance_business);
			AlertDialogRadio alertCustomerGroup = new AlertDialogRadio("",_CustomerGroup,R.id.tbtUserCustomerGroup,userCustomerGroupList, true);
			/** Creating a bundle object to store the selected item's index */
			Bundle bCustomerGroup  = new Bundle();
			
			/** Storing the selected item's index in the bundle object */
			bCustomerGroup.putInt("position", positionCustomerGroup);
		
			/** Setting the bundle object to the dialog fragment object */
			alertCustomerGroup.setArguments(bCustomerGroup);
			
			/** Creating the dialog fragment object, which will in turn open the alert dialog window */
			alertCustomerGroup.show(managerCustomerGroup, MasterConstants.TAB_DIALOG_TAG);	
			break;
			
		case R.id.btnUserSave:
			
			break;
		case R.id.btnUserCancel:
			getActivity().finish();
			break;
		}
	}
	 /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * Defining button click listener for the OK button of the alert dialog window 
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    
    public void onPositiveClick(int position, int clickBtn, Bundle bundle) {
    	/** Setting the selected android version in the textview */
    	
    	switch (clickBtn) {
    		/** Phân nhánh xử lý tùy theo button mà đã được clicked*/
	    	case R.id.btnUserJapanese:
	    		/** gán lại vị trí của item mà đã chọn */
	    		this.positionJananeseLevel = position;
	    		/** gán tên người đã chọn tại màn hình poup */
	    		txtUserJananese.setText(dataJapaneseLevel[this.positionJananeseLevel]);    
	    		break;
	    	case R.id.btnUserAllowance_business:
	    		/** gán lại vị trí của item mà đã chọn */
	    		this.positionAllowance_Business = position;
	    		/** gán tên người đã chọn tại màn hình poup */
	    		txtUserAllowance_Business.setText(dataAllowance_Business[this.positionAllowance_Business]);    
	    		break;
	    	case R.id.btnUserAllowance_Room:
	    		/** gán lại vị trí của item mà đã chọn */
	    		this.positionAllowance_Room = position;
	    		/** gán tên người đã chọn tại màn hình poup */
	    		txtUserAllowance_Room.setText(dataAllowance_Room[this.positionAllowance_Room]);    
	    		break;
	    	case R.id.tbtUserPositionGroup:
	    		UserPositionGroup userPositionGroup;
	    		/** lấy data chức danh từ bundle */
	    		PositionGroup[] positionGroup = (PositionGroup[])bundle.getParcelableArray(MasterConstants.TAB_DIALOG_BUNDLE);
	    		tbtUserPositionGroup.setChecked(false);
	    		if (positionGroup.length==0){
	    			userPositionGroupList.clear();
	    		}else{
	    			userPositionGroupList.clear();
	    			/** xác định các code chức danh được check */
	    			for(int i=0 ; i<positionGroup.length;i++ ){
	    				if(positionGroup[i].isselected==true){
	    					tbtUserPositionGroup.setChecked(true);
	    					userPositionGroup = new UserPositionGroup();
	    					userPositionGroup.user_code =0; /** chưa xác định user code*/
	    					userPositionGroup.position_group_code =positionGroup[i].code;
	    					userPositionGroup.name = positionGroup[i].name;
	    					/** add đối tượng vào list */
	    					userPositionGroupList.add(userPositionGroup);
	    				}
	    			}
	    		}
	    		break;
	    		
	    	case R.id.tbtUserCustomerGroup:
	    		UserCustomerGroup userCustomerGroup;
	    		/** lấy data chức danh từ bundle */
	    		CustomerGroup[] customerGroup = (CustomerGroup[])bundle.getParcelableArray(MasterConstants.TAB_DIALOG_BUNDLE);
	    		tbtUserCustomerGroup.setChecked(false);
	    		if (customerGroup.length==0){
	    			userCustomerGroupList.clear();
	    		}else{
	    			userCustomerGroupList.clear();
	    			/** xác định các code chức danh được check */
	    			for(int i=0 ; i<customerGroup.length;i++ ){
	    				if(customerGroup[i].isselected==true){
	    					tbtUserCustomerGroup.setChecked(true);
	    					userCustomerGroup = new UserCustomerGroup();
	    					userCustomerGroup.user_code =0; /** chưa xác định user code*/
	    					userCustomerGroup.customer_group_code =customerGroup[i].code;
	    					userCustomerGroup.name = customerGroup[i].name;
	    					/** add đối tượng vào list */
	    					userCustomerGroupList.add(userCustomerGroup);
	    				}
	    			}
	    		}
	    		break;
	    		
    	}
    }
	
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * 
     * onProgressChanged
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser) {
		float value = (float) (progress / 100.0);
		switch(seekBar.getId()){
			case R.id.sbaUserProgram:
				lblUserProgram.setText(String.valueOf(value));
				break;
			case R.id.sbaUserDetailDesign:
				lblUserDetailDesign.setText(String.valueOf(value));
				break;
		}
	}

    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * 
     * onStartTrackingTouch
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
		
	}

    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * 
     * onStopTrackingTouch
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
		
	}
	
	/**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * get các control trên màn hình
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    private void getControl(){
    	txtUserJananese = (EditText)getView().findViewById(R.id.txtUserJapanese);
    	txtUserAllowance_Business= (EditText)getView().findViewById(R.id.txtUserAllowance_business);
    	txtUserAllowance_Room= (EditText)getView().findViewById(R.id.txtUserAllowance_Room);
    	//txtUserBusinessKbn = (Switch)getView().findViewById(R.id.txtUserBusinessKbn);
    	chkUserDeveloper=(CheckBox)getView().findViewById(R.id.chkUserDeveloper);
    	chkUserHonyaku=(CheckBox)getView().findViewById(R.id.chkUserHonyaku);
		chkUserKbnOther=(CheckBox)getView().findViewById(R.id.chkUserOther);

    	btnUserJapaneseLevel=(Button)getView().findViewById(R.id.btnUserJapanese);
    	btnUserAllowance_Business=(Button)getView().findViewById(R.id.btnUserAllowance_business);
    	btnUserAllowance_Room=(Button)getView().findViewById(R.id.btnUserAllowance_Room);
    	tbtUserPositionGroup=(ToggleButton)getView().findViewById(R.id.tbtUserPositionGroup);
    	tbtUserCustomerGroup=(ToggleButton)getView().findViewById(R.id.tbtUserCustomerGroup);
    	
    	sbaUserProgram =(SeekBar)getView().findViewById(R.id.sbaUserProgram);
    	sbaUserDetailDesign =(SeekBar)getView().findViewById(R.id.sbaUserDetailDesign);
    	lblUserProgram =(TextView)getView().findViewById(R.id.txtUserProgram);
    	lblUserDetailDesign =(TextView)getView().findViewById(R.id.txtUserDetailDesign);
    	txtUserNote = (EditText)getView().findViewById(R.id.txtUserNote);
		/*btnUserSave = (Button)getView().findViewById(R.id.btnUserSave);
		btnUserCancel= (Button)getView().findViewById(R.id.btnUserCancel);*/
    }

    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * gán sự kiện cho các control
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    private void settingListener(){
		
		btnUserJapaneseLevel.setOnClickListener(this);
		btnUserAllowance_Business.setOnClickListener(this);
		btnUserAllowance_Room.setOnClickListener(this);
		tbtUserPositionGroup.setOnClickListener(this);
		tbtUserCustomerGroup.setOnClickListener(this);
		
		sbaUserProgram.setOnSeekBarChangeListener(this);
		sbaUserDetailDesign.setOnSeekBarChangeListener(this);
		/*btnUserSave.setOnClickListener(this);
		btnUserCancel.setOnClickListener(this);*/
		/** xử lý double click để xóa dữ liệu đã input */
	    @SuppressWarnings("deprecation")
		final GestureDetector gestureDetectorJananese = new GestureDetector(new GestureDetector.SimpleOnGestureListener() {
	        public boolean onDoubleTap(MotionEvent e) {
	        	txtUserJananese.setText("");
	            return true;
	        }
	    });
	    txtUserJananese.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				return gestureDetectorJananese.onTouchEvent(event);
			}
		});
	    /** xử lý double click để xóa dữ liệu đã input */
	    @SuppressWarnings("deprecation")
		final GestureDetector gestureDetectorAllowance_Business = new GestureDetector(new GestureDetector.SimpleOnGestureListener() {
	        public boolean onDoubleTap(MotionEvent e) {
	        	txtUserAllowance_Business.setText("");
	            return true;
	        }
	    });
	    
	    @SuppressWarnings("deprecation")
		final GestureDetector gestureDetectorAllowance_Room = new GestureDetector(new GestureDetector.SimpleOnGestureListener() {
	        public boolean onDoubleTap(MotionEvent e) {
	        	txtUserAllowance_Room.setText("");
	            return true;
	        }
	    });
	    
	    txtUserAllowance_Business.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				return gestureDetectorAllowance_Business.onTouchEvent(event);
			}
		});
	    
	    txtUserAllowance_Room.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				return gestureDetectorAllowance_Room.onTouchEvent(event);
			}
		});
	    
    }
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * setting input cho control(không hiển thị bàn phím khi nhận focus)
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    private void settingInputType(){
    	txtUserJananese.setInputType(InputType.TYPE_NULL);
    	txtUserAllowance_Business.setInputType(InputType.TYPE_NULL);
    	txtUserAllowance_Room.setInputType(InputType.TYPE_NULL);
    }
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * setValueTabOther
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    public void setValueTabOther(User item){
		/**thông tin của tab Other*/
		setJapaneseLevel(item.japanese);
		setAllowance_Business(item.allowance_business);
		setAllowance_Room(item.allowance_room);
		setBusinessKbn(String.valueOf(item.business_kbn ));
		setProgram((float)item.program);
		setDetailDesign((float)item.detaildesign);
		setPositionGroupList();
		setCustomerGroupList();
		setNote(item.note);
    }
    
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * set thông tin dung cho cac man hinh search
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    
    public void setPositionGroupList(){
    	/** thuộc nhóm giám đốc*/
    	if(userPositionGroupList==null)
    	{
    		tbtUserPositionGroup.setChecked(false);
    		return;
    	}
		if (userPositionGroupList.size()>0){
			tbtUserPositionGroup.setChecked(true);
		}else{
			tbtUserPositionGroup.setChecked(false);
		}
    }

    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * set thông tin dung cho cac man hinh search(khách hàng)
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    
    public void setCustomerGroupList(){
    	/** thuộc khách hàng nào*/
    	if(userCustomerGroupList==null)
    	{
    		tbtUserCustomerGroup.setChecked(false);
    		return;
    	}
		if (userCustomerGroupList.size()>0){
			tbtUserCustomerGroup.setChecked(true);
		}else{
			tbtUserCustomerGroup.setChecked(false);
		}
    }

    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * lấy thông tin bằng cấp tiếng Nhật
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    
    public String getJapaneseLevel(){
    	return  txtUserJananese.getText().toString();
    }

    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * set thông tin bằng cấp tiếng Nhật
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    
    public void setJapaneseLevel(String value){
    	txtUserJananese.setText(value);
    }
    
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * lấy thông tin phụ cấp nghiệp vụ
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    
    public String getAllowance_Business(){
    	return  txtUserAllowance_Business.getText().toString();
    }

    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * set thông tin phụ cấp nghiệp vụ
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    
    public void setAllowance_Business(String value){
    	txtUserAllowance_Business.setText(value);
    }
    
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * lấy thông tin phụ cấp phòng làm việc
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    
    public String getAllowance_Room(){
    	return  txtUserAllowance_Room.getText().toString();
    }

    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * set thông tin phụ cấp phòng làm việc
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    
    public void setAllowance_Room(String value){
    	txtUserAllowance_Room.setText(value);
    }
    
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * 
     * get thông tin về nghề nghiệp chính
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    public int  getBusinessKbn(){
    	/*return Boolean.toString( txtUserBusinessKbn.isChecked()).equals("true")?1:0;*/
    	if(chkUserDeveloper.isChecked() && chkUserHonyaku.isChecked()&& chkUserKbnOther.isChecked()){
    		return 9; //old = 3
    	}
		if(chkUserDeveloper.isChecked()){
			return 1;
		}else if (chkUserHonyaku.isChecked()){
			return 2;
		}else if (chkUserKbnOther.isChecked()) {
			return 3 ; //tong vu hoac cong viec khac
		}else{
			return 0;
		}
		
    }
    
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * 
     * set thông tin về nghề nghiệp chính
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    public void  setBusinessKbn(String value){
    	/*if (value.equals("0")){
    		txtUserBusinessKbn.setChecked(false);
    	}else{
    		txtUserBusinessKbn.setChecked(true);
    	}*/
    	if (value.equals("9")){
    		chkUserDeveloper.setChecked(true);
    		chkUserHonyaku.setChecked(true);
			chkUserKbnOther.setChecked(true);
    	}else if (value.equals("3")){
			//cong viec khac
    		chkUserDeveloper.setChecked(false);
			chkUserKbnOther.setChecked(false);
			chkUserHonyaku.setChecked(true);
    	}else if (value.equals("2")){
			//phien dich
			chkUserKbnOther.setChecked(false);
    		chkUserDeveloper.setChecked(false);
    		chkUserHonyaku.setChecked(true);
    	}
		else if (value.equals("1")) {
			//lap trinh
			chkUserKbnOther.setChecked(false);
			chkUserDeveloper.setChecked(true);
			chkUserHonyaku.setChecked(false);
		}else {
			chkUserKbnOther.setChecked(false);
    		chkUserDeveloper.setChecked(false);
    		chkUserHonyaku.setChecked(false);
    	}
    	
    }
    
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * 
     * get thông tin về hiệu suất lập trình
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    public float  getProgram(){
    	if (lblUserProgram.getText().toString().equals("")){
    		return  0;
    	}else{
    		return  Float.parseFloat(lblUserProgram.getText().toString());
    	}
    }
    
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * 
     * set thông tin về hiệu suất lập trình
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    public void  setProgram(float value){
    	int progress =(int)(value*100);
    	sbaUserProgram.setProgress(progress);
    	lblUserProgram.setText(String.valueOf(value));
    }    
    
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * 
     * get thông tin về hiệu suất TKCT
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    public float  getDetailDesign(){
    	if (lblUserDetailDesign.getText().toString().equals("")){
    		return  0;
    	}else{
    		return  Float.parseFloat(lblUserDetailDesign.getText().toString());
    	}
    }
    
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * 
     * set thông tin về hiệu suất TKCT
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    public void  setDetailDesign(float value){
    	int progress =(int)(value*100);
    	sbaUserDetailDesign.setProgress(progress);
    	lblUserDetailDesign.setText(String.valueOf(value));
    }    
    
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * 
     * getUserPositionGroupList
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    public List<UserPositionGroup> getUserPositionGroupList(){
    	return userPositionGroupList;
    }
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * 
     * getUserCustomerGroupList
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    public List<UserCustomerGroup> getUserCustomerGroupList(){
    	return userCustomerGroupList;
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
     * lấy thông tin ghi chu
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    
    public String getNote(){
    	return  txtUserNote.getText().toString();
    }

    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * set thông tin ghi chu
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    
    public void setNote(String value){
    	txtUserNote.setText(value);
    }
}
