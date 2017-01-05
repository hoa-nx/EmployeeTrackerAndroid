
package com.ussol.employeetracker;

import java.math.RoundingMode;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.TooManyListenersException;

import com.ussol.employeetracker.EditUserBasic.SelectDateFragment;
import com.ussol.employeetracker.helpers.AlertDialogRadio;
import com.ussol.employeetracker.helpers.CommonClass;
import com.ussol.employeetracker.helpers.ConvertCursorToArrayString;
import com.ussol.employeetracker.helpers.ConvertCursorToListString;
import com.ussol.employeetracker.helpers.DatabaseAdapter;
import com.ussol.employeetracker.helpers.DecodeTask;
import com.ussol.employeetracker.helpers.GetListDataHelper;
import com.ussol.employeetracker.helpers.AlertDialogRadio.AlertPositiveListener;
import com.ussol.employeetracker.helpers.UserDialogAdapter;
import com.ussol.employeetracker.models.Dept;
import com.ussol.employeetracker.models.MasterConstants;
import com.ussol.employeetracker.models.Position;
import com.ussol.employeetracker.models.Team;
import com.ussol.employeetracker.models.User;
import com.ussol.employeetracker.models.UserHistory;
import com.ussol.employeetracker.utils.DateTimeUtil;
import com.ussol.employeetracker.utils.ShowAlertDialog;
import com.ussol.employeetracker.utils.Utils;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
 
public class HisUserOther extends Fragment implements OnClickListener , OnTouchListener,AlertPositiveListener,OnCheckedChangeListener{
	/** tag */
	private final static String TAG =HisUserOther.class.getName();
	static final int DATE_DIALOG_ID = 0;
    //private TextView lblUserCode , lblUserDeptCode , lblUserTeamCode , lblUserPositionCode ;
    private TextView txtUserCode, imgUserFullPath,lblUserHisCurrentSalary;
    private EditText txtUserHisDeptStartDate ,txtUserHisDeptNote,txtUserHisNewJapanese , txtUserHisNewAllowance_Business,txtUserHisNewAllowance_BSE,txtUserHisNewSalary;
    private EditText txtUserFullName,txtUserHisNewSalaryStandard,txtUserHisNewSalaryPercent,txtUserHisNewSalaryActualUp,txtUserHisNewSalaryNextYM;
    private Button btnUserSave , btnUserCancel , btnUserHisNewJapanese , btnUserHisNewAllowance_Business,btnUserHisNewAllowance_BSE,btnUserHisNewSalaryNextYM,btnUserHisDeptStartDate;
    private CheckBox chkUserHisNewJapanese,chkUserHisNewAllowance_Business,chkUserHisNewSalary,chkUserHisNewAllowance_BSE;
	private Bitmap bitmap;
	private ImageView imageView , pic , imgPrev, imgNext;
	private ImageButton imgUserHisDeptList;
	private Uri mImageCaptureUri;
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
	private ConvertCursorToListString mConvertCursorToListString;
	/** đối tượng hiển thị tại các màn hình search*/
	GetListDataHelper mGetListDataHelper;
	/** thông tin các nhóm hiển thị tại màn hình dialog  */
	User[] _Users=null;
	List<User> _listUser =null;
	List<User> _listUserChecked =null;
	/** thông tin bằng cấp tiếng Nhật  */
	String[] dataJapaneseLevel = MasterConstants.JAPANESE_LEVEL ;
	/** thông tin phụ cấp nghiệp vụ : các bậc*/
	String[] dataAllowance_Business=MasterConstants.ALLOWANCE_BUSINESS_LEVEL ;
	/** thông tin phụ cấp BSE : các bậc*/
	String[] dataAllowance_BSE=MasterConstants.ALLOWANCE_BSE_LEVEL;
    /** Lưu lại vị trí mà đã chọn từ list radio */
	int positionJananeseLevel = 0;
	int positionAllowance_Business = 0;
	int positionAllowance_BSE = 0;
	String date_start="";
	String old_date_start="";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	/**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
	 * onCreateView
	 * 
	 ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		ViewGroup root = (ViewGroup) inflater.inflate(R.layout.activity_user_his_other, null);		
		/** setting tab để có thể truy xuất từ các Tab khác*/
		((HisUserMainActivity)getActivity()).setTabFragmentHisOther(getTag());
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
		
		String TabOfFragmentMain =HisUserMainActivity.getTabFragmentHisMain();
		HisUserMain fgUserMain = (HisUserMain)getFragmentManager().findFragmentByTag(TabOfFragmentMain);
		   
		if (fgUserMain!=null){
			/**thông tin của tab main*/
			_listUserChecked = fgUserMain.getCheckedUserList();
			date_start = fgUserMain.getDateFrom();
			old_date_start = fgUserMain.getHisOldStartDate();
			setHisDeptStartDate(date_start);
		}

		nCode=HisUserMainActivity.nCode;
		userInfo =HisUserMainActivity.userInfo;
		userHisInfo =HisUserMainActivity.userHisInfo;
		/** gan init cho list user duoc check khi mo man hinh search */
		/*if(userInfo !=null){
			_listUser.add(userInfo);
		}*/
		/** get các control date và gán sự kiện khi click vào textbox */
		getControl();
		/** gán sự kiện cho các control */ 
		settingListener();
		/** setting không hiển thị keyboard */
		settingInputType();
		/** add event cho cac control */
		addTextChangedListener();
		/** setting init cho cac item check box */
		setHisJapaneseCheck(false);
		/** setting init cho cac item check box */
		setHisAllowance_BusinessCheck(false);
		/** setting init cho cac item check box */
		setHisSalaryCheck(false);
		txtUserHisNewSalaryStandard.setEnabled(false);
		txtUserHisNewSalaryPercent.setEnabled(false);
		txtUserHisNewSalaryActualUp.setEnabled(false);
		txtUserHisNewSalaryNextYM.setEnabled(false);

		if (savedInstanceState!=null){
			
		}else{
			if (nCode==-1){
				/** trường hợp tạo mới*/

			}else{
				/** hiển thị chi tiết */
				setUserCode(userHisInfo.user_code);
				setValueTabOther(userHisInfo);
				
				
			}
		}
		/** hien thi hinh anh nhan vien */
        pic=(ImageView)getView().findViewById(R.id.imgUser);  
        pic.setOnClickListener(this);  
        
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
			case R.id.btnUserHisNewJapanese:
				/**hiển thị màn hình để chọn phong ban */
				/** Getting the fragment manager */
				FragmentManager managerJapanese =getActivity().getFragmentManager();
				
				/** Instantiating the DialogFragment class */
				AlertDialogRadio alertJapanese = new AlertDialogRadio("",dataJapaneseLevel,R.id.btnUserHisNewJapanese);
				
				/** Creating a bundle object to store the selected item's index */
				Bundle bJapanese  = new Bundle();
				
				/** Storing the selected item's index in the bundle object */
				bJapanese.putInt("position", positionJananeseLevel);
				
				/** Setting the bundle object to the dialog fragment object */
				alertJapanese.setArguments(bJapanese);
				
				/** Creating the dialog fragment object, which will in turn open the alert dialog window */
				alertJapanese.show(managerJapanese, MasterConstants.TAB_DIALOG_TAG);
				break;
				
				
			case R.id.btnUserHisNewAllowance_Business:
				/**hiển thị màn hình để chọn nhóm*/
				/** Getting the fragment manager */
				FragmentManager managerBusiness =getActivity().getFragmentManager();
				
				/** Instantiating the DialogFragment class */
				AlertDialogRadio alertBusiness = new AlertDialogRadio("",dataAllowance_Business,R.id.btnUserHisNewAllowance_Business);
				
				/** Creating a bundle object to store the selected item's index */
				Bundle bBusiness  = new Bundle();
				
				/** Storing the selected item's index in the bundle object */
				bBusiness.putInt("position", positionAllowance_Business);
				
				/** Setting the bundle object to the dialog fragment object */
				alertBusiness.setArguments(bBusiness);
				
				/** Creating the dialog fragment object, which will in turn open the alert dialog window */
				alertBusiness.show(managerBusiness, MasterConstants.TAB_DIALOG_TAG);
				break;

			case R.id.btnUserHisNewAllowance_BSE:
				/**hiển thị màn hình để chọn loai BSE*/
				/** Getting the fragment manager */
				FragmentManager managerBSELevel =getActivity().getFragmentManager();

				/** Instantiating the DialogFragment class */
				AlertDialogRadio alertBSELevel = new AlertDialogRadio("",dataAllowance_BSE,R.id.btnUserHisNewAllowance_BSE);

				/** Creating a bundle object to store the selected item's index */
				Bundle bBSE  = new Bundle();

				/** Storing the selected item's index in the bundle object */
				bBSE.putInt("position", positionAllowance_BSE);

				/** Setting the bundle object to the dialog fragment object */
				alertBSELevel.setArguments(bBSE);

				/** Creating the dialog fragment object, which will in turn open the alert dialog window */
				alertBSELevel.show(managerBSELevel, MasterConstants.TAB_DIALOG_TAG);
				break;

			case R.id.btnUserHisDeptStartDate:
				txtUserHisDeptStartDate.setText("");
				break;
				
			case R.id.btnUserHisNewSalaryNextYM:
				txtUserHisNewSalaryNextYM.setText("");
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
		/** khi input vao muc luong chuan thi se tinh toan lai muc tang luong */
		txtUserHisNewSalaryStandard.addTextChangedListener(new TextWatcher() {
			
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
				double ActualUp = calSalaryUp(getHisNewSalaryStandardNotCheckValue(), getHisNewSalaryPercentNotCheckValue());
				txtUserHisNewSalaryActualUp.setText(String.valueOf(ActualUp));
				setHisNewSalary((float) calNewSalary(getHisNewSalaryActualUpNotCheckValue(),getHisCurrentSalary()));
			}
		});
		
		
		/** khi input vao ti le nang luong thi se tinh toan lai muc tang luong */
		txtUserHisNewSalaryPercent.addTextChangedListener(new TextWatcher() {
			
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
				double ActualUp = calSalaryUp(getHisNewSalaryStandardNotCheckValue(), getHisNewSalaryPercentNotCheckValue());
				txtUserHisNewSalaryActualUp.setText(String.valueOf(ActualUp));
				setHisNewSalary((float) calNewSalary(getHisNewSalaryActualUpNotCheckValue(),getHisCurrentSalary()));
			}
		});
				
				
	}
	
	/**
	 * Tinh ra muc luong tang thuc te 
	 * @param stdSalary
	 * @param actualPercent
	 * @return
	 * Muc tang thuc te dua tren ti le nang luong
	 */
	private double calSalaryUp(double stdSalary , double actualPercent){
		return Utils.Round((stdSalary*actualPercent)/100,2,RoundingMode.HALF_UP);
		
	}
	/**
	 * Tinh ra muc luong moi
	 * @param curSalary
	 * @param actualUp
	 * @return
	 */
	private double calNewSalary(double curSalary , double actualUp){
		return Utils.Round(curSalary+actualUp,2,RoundingMode.HALF_UP);
		
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
    		case R.id.txtUserHisNewSalaryNextYM:
    			//truong hop la xet luong tiep theo thi co dinh la ngay 1 (Theo thang nam)
    			txtUserHisNewSalaryNextYM.setText(1 + MasterConstants.DATE_SEPERATE_CHAR + month+ MasterConstants.DATE_SEPERATE_CHAR +year);
    			break;
    	}
    	
    }
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * SelectDateFragment
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    @SuppressLint("ValidFragment")
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
	    		case R.id.txtUserHisNewSalaryNextYM:
	    			date = txtUserHisNewSalaryNextYM.getText().toString();
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
    	lblUserHisCurrentSalary = (TextView)getView().findViewById(R.id.lblUserHisCurrentSalary);
    	txtUserFullName= (EditText)getView().findViewById(R.id.txtUserHisName);
    	txtUserHisDeptStartDate = (EditText) getView().findViewById(R.id.txtUserHisDeptStartDate);
    	txtUserHisNewJapanese= (EditText)getView().findViewById(R.id.txtUserHisNewJapanese);
    	txtUserHisNewAllowance_Business= (EditText)getView().findViewById(R.id.txtUserHisNewAllowance_Business);
		txtUserHisNewAllowance_BSE= (EditText)getView().findViewById(R.id.txtUserHisNewAllowance_BSE);
    	txtUserHisNewSalary= (EditText)getView().findViewById(R.id.txtUserHisNewSalary);
    	txtUserHisNewSalaryStandard= (EditText)getView().findViewById(R.id.txtUserHisNewSalaryStandard);
    	txtUserHisNewSalaryPercent= (EditText)getView().findViewById(R.id.txtUserHisNewSalaryPercent);
    	txtUserHisNewSalaryActualUp= (EditText)getView().findViewById(R.id.txtUserHisNewSalaryActualUp);
    	txtUserHisNewSalaryNextYM= (EditText)getView().findViewById(R.id.txtUserHisNewSalaryNextYM);
    	
    	txtUserHisDeptNote=(EditText)getView().findViewById(R.id.txtUserHisDeptNote);
    	
    	btnUserHisNewJapanese = (Button)getView().findViewById(R.id.btnUserHisNewJapanese);
    	btnUserHisNewAllowance_Business = (Button)getView().findViewById(R.id.btnUserHisNewAllowance_Business);
		btnUserHisNewAllowance_BSE = (Button)getView().findViewById(R.id.btnUserHisNewAllowance_BSE);
    	btnUserHisNewSalaryNextYM = (Button)getView().findViewById(R.id.btnUserHisNewSalaryNextYM);
    	btnUserHisDeptStartDate= (Button)getView().findViewById(R.id.btnUserHisDeptStartDate);
    	
    	chkUserHisNewJapanese =(CheckBox)getView().findViewById(R.id.chkUserHisNewJapanese);
    	chkUserHisNewAllowance_Business=(CheckBox)getView().findViewById(R.id.chkUserHisNewAllowance_Business);
		chkUserHisNewAllowance_BSE=(CheckBox)getView().findViewById(R.id.chkUserHisNewAllowance_BSE);
    	chkUserHisNewSalary =(CheckBox)getView().findViewById(R.id.chkUserHisNewSalary);
    	
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
    	txtUserHisNewSalaryNextYM.setOnTouchListener(this);
    	
    	btnUserHisNewJapanese.setOnClickListener(this);
    	btnUserHisNewAllowance_Business.setOnClickListener(this);
		btnUserHisNewAllowance_BSE.setOnClickListener(this);
    	btnUserHisNewSalaryNextYM.setOnClickListener(this);
    	btnUserHisDeptStartDate.setOnClickListener(this);
    	
    	chkUserHisNewJapanese.setOnCheckedChangeListener(this);
    	chkUserHisNewAllowance_Business.setOnCheckedChangeListener(this);
		chkUserHisNewAllowance_BSE.setOnCheckedChangeListener(this);
    	chkUserHisNewSalary.setOnCheckedChangeListener(this);
    	
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
    	txtUserHisNewSalaryNextYM.setInputType(InputType.TYPE_NULL);
    	txtUserHisNewSalaryActualUp.setInputType(InputType.TYPE_NULL);
    	//txtUserHisNewSalary.setInputType(InputType.TYPE_NULL);
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
			case R.id.txtUserHisNewSalaryNextYM:
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
    public void saveUserToDb(List<User> userList , String date_from){
    	try{

			UserHistory userInsert ;
			setHisDeptStartDate(date_from);
			/** get danh sách các user được chọn */
			_listUserChecked = userList;

			String  currentStringValue="";
			float currentFloatValue=0;
			/*
			*//** cap nhat cho truong hop la tieng nhat thay doi *//*
			if(getHisJapaneseCheck()){
				*//** xử lý cho từng user được chọn *//*
				for(User usr: _listUserChecked){
					if(userHisInfo!=null){
						*//** nếu như có thay đổi ngày tháng năm thì sẽ xóa data cũ *//*
						if(!date_from.equals(userHisInfo.date_from)){
							deleteUserHisByDate(userHisInfo.date_from,usr.code,MasterConstants.MASTER_MKBN_JAPANESE_HIS);
						}
					}
					*//** xoa neu nhu da co data tuong ung voi ngay thang nam tren man hinh*//*
					deleteUserHisByDate(date_from,usr.code,MasterConstants.MASTER_MKBN_JAPANESE_HIS);
					*//** tạo đối tượng dùng để update*//*
					userInsert = getUserHistory(MasterConstants.MASTER_MKBN_JAPANESE_HIS,usr.code);
					*//** thực thi update *//*
					mDatabaseAdapter.open();
					mDatabaseAdapter.insertToUserHisTable(userInsert);
					mDatabaseAdapter.close();
					*//** chỉnh sửa lại ngày tháng năm start -end cho đúng *//*
					correctHisData(MasterConstants.MASTER_MKBN_JAPANESE_HIS,userInsert.user_code);
				}
			}
			
			*//** cap nhat cho truong hop la trợ cấp nghiệp vụ thay doi *//*
			if(getHisAllowance_BusinessCheck()){
				*//** xử lý cho từng user được chọn *//*
				for(User usr: _listUserChecked){
					if(userHisInfo!=null){
						*//** nếu như có thay đổi ngày tháng năm thì sẽ xóa data cũ *//*
						if(!date_from.equals(userHisInfo.date_from)){
							deleteUserHisByDate(userHisInfo.date_from,usr.code,MasterConstants.MASTER_MKBN_ALLOWANCE_BUSINESS_HIS);
						}
					}
					*//** xoa neu nhu da co data tuong ung voi ngay thang nam tren man hinh*//*
					deleteUserHisByDate(date_from,usr.code,MasterConstants.MASTER_MKBN_ALLOWANCE_BUSINESS_HIS);
					*//** tạo đối tượng dùng để update*//*
					userInsert = getUserHistory(MasterConstants.MASTER_MKBN_ALLOWANCE_BUSINESS_HIS,usr.code);
					
					*//** thực thi update *//*
					mDatabaseAdapter.open();
					mDatabaseAdapter.insertToUserHisTable(userInsert);
					mDatabaseAdapter.close();
					*//** chỉnh sửa lại ngày tháng năm start -end cho đúng *//*
					correctHisData(MasterConstants.MASTER_MKBN_ALLOWANCE_BUSINESS_HIS,userInsert.user_code);
				}
			}
			*//** cap nhat cho truong hop la salary thay doi *//*
			if(getHisSalaryCheck()){
				*//** xử lý cho từng user được chọn *//*
				for(User usr: _listUserChecked){
					if(userHisInfo!=null){
						*//** nếu như có thay đổi ngày tháng năm thì sẽ xóa data cũ *//*
						if(!date_from.equals(userHisInfo.date_from)){
							deleteUserHisByDate(userHisInfo.date_from,usr.code,MasterConstants.MASTER_MKBN_SALARY_HIS);
						}
					}
					*//** xoa neu nhu da co data tuong ung voi ngay thang nam tren man hinh*//*
					deleteUserHisByDate(date_from,usr.code,MasterConstants.MASTER_MKBN_SALARY_HIS);
					*//** tạo đối tượng dùng để update*//*
					userInsert = getUserHistory(MasterConstants.MASTER_MKBN_SALARY_HIS,usr.code);
					
					*//** thực thi update *//*
					mDatabaseAdapter.open();
					mDatabaseAdapter.insertToUserHisTable(userInsert);
					mDatabaseAdapter.close();
					*//** chỉnh sửa lại ngày tháng năm start -end cho đúng *//*
					correctHisData(MasterConstants.MASTER_MKBN_SALARY_HIS,userInsert.user_code);
				}
			}*/

			currentStringValue="";
			/** trình độ nhật ngữ*/
			boolean japaneseCheck =getHisJapaneseCheck();
			/** phu cap nghiep vu */
			boolean allowanceCheck =getHisAllowance_BusinessCheck();
			/** phu cap BSE*/
			boolean allowanceBSECheck =getHisAllowance_BSECheck();
			/** salary*/
			/** salary*/
			boolean salaryCheck =getHisSalaryCheck() ;

			String japaneseNew = getHisNewJapanese();
			String allowanceNew = getHisNewAllowance_Business();
			String allowanceBSENew = getHisNewAllowance_BSE();
			float salaryNew = getHisNewSalary();

			/** cap nhat lich su cho truong hop la chung chi tieng Nhat thay doi */
			if(japaneseCheck && !getHisDeptStartDate().equals("")){
				/** xử lý cho từng user được chọn */
				for(User usr: _listUserChecked){
					/** kiem tra xem tri co thay doi so voi master User khong ? Neu khong thi khong update */
					currentStringValue = String.valueOf(usr.japanese) ;
					if(!currentStringValue.equals(japaneseNew)|| !old_date_start.equals(getHisDeptStartDate()) ){
						if(userHisInfo!=null){
								/* truong hop la edit thi se xoa neu du lieu cua ngay cu */
							if(!old_date_start.equals("")){
								deleteUserHisByDate(old_date_start,usr.code,MasterConstants.MASTER_MKBN_JAPANESE_HIS);
							}
							/** nếu như có thay đổi ngày tháng năm thì sẽ xóa data cũ */
							if(!getHisDeptStartDate().equals(userHisInfo.date_from)){
								deleteUserHisByDate(userHisInfo.date_from,usr.code,MasterConstants.MASTER_MKBN_JAPANESE_HIS);
							}
						}
						/** xoa neu nhu da co data tuong ung voi ngay thang nam tren man hinh*/
						deleteUserHisByDate(getHisDeptStartDate(),usr.code,MasterConstants.MASTER_MKBN_JAPANESE_HIS);
						/** tạo đối tượng dùng để update*/
						userInsert = getUserHistory(MasterConstants.MASTER_MKBN_JAPANESE_HIS,usr.code);
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

					if(comparison!=0 || !old_date_start.equals(getHisDeptStartDate()) ){
						if(userHisInfo!=null){
								/* truong hop la edit thi se xoa neu du lieu cua ngay cu */
							if(!old_date_start.equals("")){
								deleteUserHisByDate(old_date_start,usr.code,MasterConstants.MASTER_MKBN_ALLOWANCE_BUSINESS_HIS);
							}
							/** nếu như có thay đổi ngày tháng năm thì sẽ xóa data cũ */
							if(!getHisDeptStartDate().equals(userHisInfo.date_from)){
								deleteUserHisByDate(userHisInfo.date_from,usr.code,MasterConstants.MASTER_MKBN_ALLOWANCE_BUSINESS_HIS);
							}
						}
						/** xoa neu nhu da co data tuong ung voi ngay thang nam tren man hinh*/
						deleteUserHisByDate(getHisDeptStartDate(),usr.code,MasterConstants.MASTER_MKBN_ALLOWANCE_BUSINESS_HIS);
						/** tạo đối tượng dùng để update*/
						userInsert = getUserHistory(MasterConstants.MASTER_MKBN_ALLOWANCE_BUSINESS_HIS,usr.code);
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
			currentStringValue ="";
			/** cap nhat lich su cho truong hop la tro cap BSE thay doi */
			if(allowanceBSECheck && !getHisDeptStartDate().equals("")){
				/** xử lý cho từng user được chọn */
				for(User usr: _listUserChecked){
					/** kiem tra xem tri co thay doi so voi master User khong ? Neu khong thi khong update */
					currentStringValue = String.valueOf(usr.allowance_bse) ;
					/**Neu nhu co su thay doi */
					Collator compare = Collator.getInstance(new Locale("vi", "vn"));
					int comparison = compare.compare(currentStringValue, allowanceBSENew);

					if(comparison!=0 || !old_date_start.equals(getHisDeptStartDate()) ){
						if(userHisInfo!=null){
								/* truong hop la edit thi se xoa neu du lieu cua ngay cu */
							if(!old_date_start.equals("")){
								deleteUserHisByDate(old_date_start,usr.code,MasterConstants.MASTER_MKBN_ALLOWANCE_BSE_HIS);
							}
							/** nếu như có thay đổi ngày tháng năm thì sẽ xóa data cũ */
							if(!getHisDeptStartDate().equals(userHisInfo.date_from)){
								deleteUserHisByDate(userHisInfo.date_from,usr.code,MasterConstants.MASTER_MKBN_ALLOWANCE_BSE_HIS);
							}
						}
						/** xoa neu nhu da co data tuong ung voi ngay thang nam tren man hinh*/
						deleteUserHisByDate(getHisDeptStartDate(),usr.code,MasterConstants.MASTER_MKBN_ALLOWANCE_BSE_HIS);
						/** tạo đối tượng dùng để update*/
						userInsert = getUserHistory(MasterConstants.MASTER_MKBN_ALLOWANCE_BSE_HIS,usr.code);
						/** thực thi update */
						mDatabaseAdapter.open();
						mDatabaseAdapter.insertToUserHisTable(userInsert);
						mDatabaseAdapter.close();
						/** chỉnh sửa lại ngày tháng năm start -end cho đúng */
						correctHisData(MasterConstants.MASTER_MKBN_ALLOWANCE_BSE_HIS,userInsert.user_code);
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
					if(currentFloatValue!=salaryNew || !old_date_start.equals(getHisDeptStartDate()) ){
						if(userHisInfo!=null){
								/* truong hop la edit thi se xoa neu du lieu cua ngay cu */
							if(!old_date_start.equals("")){
								deleteUserHisByDate(old_date_start,usr.code,MasterConstants.MASTER_MKBN_SALARY_HIS);
							}
							/** nếu như có thay đổi ngày tháng năm thì sẽ xóa data cũ */
							if(!getHisDeptStartDate().equals(userHisInfo.date_from)){
								deleteUserHisByDate(userHisInfo.date_from,usr.code,MasterConstants.MASTER_MKBN_SALARY_HIS);
							}
						}
						/** xoa neu nhu da co data tuong ung voi ngay thang nam tren man hinh*/
						deleteUserHisByDate(getHisDeptStartDate(),usr.code,MasterConstants.MASTER_MKBN_SALARY_HIS);
						/** tạo đối tượng dùng để update*/
						userInsert = getUserHistory(MasterConstants.MASTER_MKBN_SALARY_HIS,usr.code);
						/** thực thi update */
						mDatabaseAdapter.open();
						mDatabaseAdapter.insertToUserHisTable(userInsert);
						mDatabaseAdapter.close();
						/** chỉnh sửa lại ngày tháng năm start -end cho đúng */
						correctHisData(MasterConstants.MASTER_MKBN_SALARY_HIS,userInsert.user_code);
					}

				}
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
     * get thông tin lịch sử dựa vào code nhân viên và ngày tháng năm hữu hiệu.
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    public UserHistory getUserHistory(int mkbn, int user_code){
		UserHistory userInsert; 
		userInsert = new UserHistory();
		
		userInsert.mkbn = mkbn;
		userInsert.user_code = user_code;
		userInsert.date_from = getHisDeptStartDate();
		userInsert.new_japanese = getHisNewJapanese();
		userInsert.new_allowance_business = getHisNewAllowance_Business();
		userInsert.new_allowance_bse = getHisNewAllowance_BSE();

		userInsert.yobi_real1 = getHisNewSalary();//muc luong moi
		userInsert.new_salary = getHisNewSalary();//muc luong moi

		userInsert.new_salary_standard = getHisNewSalaryStandard();
		userInsert.new_salary_percent = getHisNewSalaryPercent();
		userInsert.new_salary_actual_up = getHisNewSalaryActualUp();
		userInsert.new_salary_next_ym = getHisNewSalaryNextYM();
		
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
				case MasterConstants.MASTER_MKBN_JAPANESE_HIS:
					mDatabaseAdapter.deleteUserHisJapaneseByCode(xWhere);
					break;
				case MasterConstants.MASTER_MKBN_ALLOWANCE_BUSINESS_HIS:
					mDatabaseAdapter.deleteUserHisAllowance_BusinessByCode(xWhere);
					break;
				case MasterConstants.MASTER_MKBN_SALARY_HIS:
					mDatabaseAdapter.deleteUserHisSalaryByCode(xWhere);
					break;
				case MasterConstants.MASTER_MKBN_ALLOWANCE_BSE_HIS:
					mDatabaseAdapter.deleteUserHisAllowance_BSEByCode(xWhere);
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
    	xOrderBy = "date(" + DatabaseAdapter.KEY_DATE_FROM + ") ASC ";
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
        		if(getHisJapaneseCheck()){
        			String jp = getNewestUserHisJapaneseInfo(user_code);
            		usr.japanese= jp;
        		}
        		if(getHisAllowance_BusinessCheck()){
        			String al= getNewestUserHisAllowance_BusinessInfo(user_code);
        			usr.allowance_business= al;
        		}
        		if(getHisSalaryCheck()){
        			float salary= getNewestUserHisSalaryInfo(user_code);
        			usr.salary_notallowance= salary;
        		}
        		mDatabaseAdapter.open();
        		mDatabaseAdapter.editToUserTable(usr);
        		mDatabaseAdapter.close();
        	}
    	}catch(Exception e){
    		mDatabaseAdapter.close();
    	}
    }
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * 
     * get thông tin về chứng chỉ tiếng Nhật mới nhất của nhân viên
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    public String  getNewestUserHisJapaneseInfo(int user_code ){
    	String xWhere ="";
    	String xOrderBy ="";
    	String jp="";
    	
    	xWhere = " AND " + DatabaseAdapter.KEY_USER_CODE + " = " + user_code; 
    	xOrderBy = "date(" + DatabaseAdapter.KEY_DATE_FROM + ") DESC ";
    	/** get danh sách các data lịch sử của user */
    	List<UserHistory> userhis = mConvertCursorToListString.getUserHisList(MasterConstants.MASTER_MKBN_JAPANESE_HIS, xWhere,xOrderBy);
    	if(userhis.size()==0){
    	}else{
    		jp= userhis.get(0).new_japanese;
    	}
    	return jp;
    }
    
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * 
     * get thông tin về code phòng ban mới nhất của nhân viên
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    public String  getNewestUserHisAllowance_BusinessInfo(int user_code ){
    	String xWhere ="";
    	String xOrderBy ="";
    	String al="";
    	
    	
    	xWhere = " AND " + DatabaseAdapter.KEY_USER_CODE + " = " + user_code; 
    	xOrderBy = "date(" + DatabaseAdapter.KEY_DATE_FROM + ") DESC ";
    	/** get danh sách các data lịch sử của user */
    	List<UserHistory> userhis = mConvertCursorToListString.getUserHisList(MasterConstants.MASTER_MKBN_ALLOWANCE_BUSINESS_HIS, xWhere,xOrderBy);
    	if(userhis.size()==0){
    	}else{
    		al= userhis.get(0).new_allowance_business;
    	}
    	return al;
    }
    
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * 
     * get thông tin về salary mới nhất của nhân viên
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    public float  getNewestUserHisSalaryInfo(int user_code ){
    	String xWhere ="";
    	String xOrderBy ="";
    	float salary=0;
    	
    	
    	xWhere = " AND " + DatabaseAdapter.KEY_USER_CODE + " = " + user_code; 
    	xOrderBy = "date(" + DatabaseAdapter.KEY_DATE_FROM + ") DESC ";
    	/** get danh sách các data lịch sử của user */
    	List<UserHistory> userhis = mConvertCursorToListString.getUserHisList(MasterConstants.MASTER_MKBN_SALARY_HIS, xWhere,xOrderBy);
    	if(userhis.size()==0){
    	}else{
    		salary= userhis.get(0).yobi_real1;
    		//salary= userhis.get(0).new_salary;
    	}
    	return salary;
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
    public void setValueTabOther(UserHistory item){
		/**thông tin của tab phong ban-nhom to*/
		setUserCode(item.user_code);
		setFullName(item.full_name);
		setHisDeptStartDate(item.date_from);
		setHisNewJapanese(item.new_japanese);
		setHisNewAllowance_Business(item.new_allowance_business);
		setHisNewSalary(item.yobi_real1);
		//setHisNewSalary(item.new_salary);
		setHisNewSalaryStandard(item.new_salary_standard);
		setHisNewSalaryPercent(item.new_salary_percent);
		setHisNewSalaryActualUp(item.new_salary_actual_up);
		setHisNewSalaryNextYM(item.new_salary_next_ym);
		
		//getCurrentSalary
		float currentSalary=getCurrentSalary(item.user_code);
		setHisCurrentSalary(currentSalary);
		setHisNewSalary(item.new_salary);
		
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
			/**thông tin của tab chuc vu*/
			
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
	    		}
	    		break;
	    	case R.id.btnUserHisNewJapanese:
	    		/** gán lại vị trí của item mà đã chọn */
	    		this.positionJananeseLevel = position;
	    		/** gán tên người đã chọn tại màn hình poup */
	    		txtUserHisNewJapanese.setText(dataJapaneseLevel[this.positionJananeseLevel]);    
	    		break;
	    	case R.id.btnUserHisNewAllowance_Business:
	    		/** gán lại vị trí của item mà đã chọn */
	    		this.positionAllowance_Business = position;
	    		/** gán tên người đã chọn tại màn hình poup */
	    		txtUserHisNewAllowance_Business.setText(dataAllowance_Business[this.positionAllowance_Business]);    
	    		break;
			case R.id.btnUserHisNewAllowance_BSE:
				/** gán lại vị trí của item mà đã chọn */
				this.positionAllowance_BSE = position;
				/** gán tên người đã chọn tại màn hình poup */
				txtUserHisNewAllowance_BSE.setText(dataAllowance_BSE[this.positionAllowance_BSE]);
				break;
    	}
    }
    
    @Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
    	switch (buttonView.getId()){
		case R.id.chkUserHisNewJapanese:
			if(isChecked){
				txtUserHisNewJapanese.setEnabled(true);
				btnUserHisNewJapanese.setEnabled(true);
			}else{
				txtUserHisNewJapanese.setEnabled(false);
				btnUserHisNewJapanese.setEnabled(false);
			}
			break;
		case R.id.chkUserHisNewAllowance_Business:
			if(isChecked){
				txtUserHisNewAllowance_Business.setEnabled(true);
				btnUserHisNewAllowance_Business.setEnabled(true);
			}else{
				txtUserHisNewAllowance_Business.setEnabled(false);
				btnUserHisNewAllowance_Business.setEnabled(false);
			}
			break;

		case R.id.chkUserHisNewAllowance_BSE:
			if(isChecked){
				txtUserHisNewAllowance_BSE.setEnabled(true);
				btnUserHisNewAllowance_BSE.setEnabled(true);
			}else{
				txtUserHisNewAllowance_BSE.setEnabled(false);
				btnUserHisNewAllowance_BSE.setEnabled(false);
			}
			break;

		case R.id.chkUserHisNewSalary:
			if(isChecked){
				txtUserHisNewSalary.setEnabled(true);
				txtUserHisNewSalaryStandard.setEnabled(true);
				txtUserHisNewSalaryPercent.setEnabled(true);
				txtUserHisNewSalaryActualUp.setEnabled(true);
				txtUserHisNewSalaryNextYM.setEnabled(true);
			}else{
				txtUserHisNewSalary.setEnabled(false);
				txtUserHisNewSalaryStandard.setEnabled(false);
				txtUserHisNewSalaryPercent.setEnabled(false);
				txtUserHisNewSalaryActualUp.setEnabled(false);
				txtUserHisNewSalaryNextYM.setEnabled(false);
			}
			break;
    	}
    }
    
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * 
     * Lấy về chứng chỉ tiếng nhật hiện tại ở master user
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    public String  getCurrentJapanese(int user_code){
    	String  value="";
    	List<User> userList = mConvertCursorToListString.getUserList(" AND " + DatabaseAdapter.KEY_CODE + "=" + user_code);
    	if(userList.size()>0){
    		User usr = userList.get(0);
    		value = usr.japanese;
    	}else{
    		
    	}
    	return value;
    }
    
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * 
     * Lấy về chứng trợ cấp nghiệp vụ hiện tại ở master user
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    public String  getCurrentAllowance_Business(int user_code){
    	String  value="";
    	List<User> userList = mConvertCursorToListString.getUserList(" AND " + DatabaseAdapter.KEY_CODE + "=" + user_code);
    	if(userList.size()>0){
    		User usr = userList.get(0);
    		value = usr.allowance_business;
    	}else{
    		
    	}
    	return value;
    }
    
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * 
     * Lấy về salary hiện tại ở master user
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    public float  getCurrentSalary(int user_code){
    	float  value=0;
    	List<User> userList = mConvertCursorToListString.getUserList(" AND " + DatabaseAdapter.KEY_CODE + "=" + user_code);
    	if(userList.size()>0){
    		User usr = userList.get(0);
    		value = usr.salary_notallowance;
    	}else{
    		
    	}
    	return value;
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
    	date_start = value;
    }
    
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * 
     * getHisDeptStartDate
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    public String   getHisDeptStartDate(){
    	return date_start;
    	//return txtUserHisDeptStartDate.getText().toString();
    }

    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * 
     * setHisNewJapanese
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    public void  setHisNewJapanese(String value){
    	txtUserHisNewJapanese.setText(value );
    }
    
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * 
     * getHisNewJapanese
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    public String   getHisNewJapanese(){
    	if(!getHisJapaneseCheck()){
    		return "";
    	}
    	return txtUserHisNewJapanese.getText().toString();
    }
    
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * 
     * setHisNewAllowance_Business
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    public void  setHisNewAllowance_Business(String value){
    	txtUserHisNewAllowance_Business.setText(value );
    }

    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * 
     * getHisNewAllowance_Business
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    public String   getHisNewAllowance_Business(){
    	if(!getHisAllowance_BusinessCheck()){
    		return "";
    	}
    	return txtUserHisNewAllowance_Business.getText().toString();
    }
	/**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
	 *
	 * setHisNewAllowance_BSE
	 *
	 ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
	public void  setHisNewAllowance_BSE(String value){
		txtUserHisNewAllowance_BSE.setText(value );
	}

	/**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
	 *
	 * getHisNewAllowance_BSE
	 *
	 ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
	public String   getHisNewAllowance_BSE(){
		if(!getHisAllowance_BSECheck()){
			return "";
		}
		return txtUserHisNewAllowance_BSE.getText().toString();
	}
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * 
     * setHisNewSalary
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    public void  setHisNewSalary(float value){
    	txtUserHisNewSalary.setText(String.valueOf( value ));
    }
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * 
     * getHisNewSalary
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    public float   getHisNewSalary(){
    	if(!getHisSalaryCheck()){
    		return 0;
    	}
    	String value =txtUserHisNewSalary.getText().toString();
    	if(value==null || value.equals("")){
    		value="0";
    	}
    	return Float.parseFloat( value);
    	
    }
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * 
     * setHisCurrentSalary
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    public void  setHisCurrentSalary(float value){
    	lblUserHisCurrentSalary.setText(String.valueOf( value ));
    }
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * 
     * getHisCurrentSalary
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    public float   getHisCurrentSalary(){
    	
    	String value =lblUserHisCurrentSalary.getText().toString();
    	if(value==null || value.equals("")){
    		value="0";
    	}
    	return Float.parseFloat( value);
    	
    }
    
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * 
     * setHisNewSalaryStandard
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    public void  setHisNewSalaryStandard(float value){
    	txtUserHisNewSalaryStandard.setText(String.valueOf( value ));
    }
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * 
     * getHisNewSalaryStandard
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    public float   getHisNewSalaryStandard(){
    	if(!getHisSalaryCheck()){
    		return 0;
    	}
    	String value =txtUserHisNewSalaryStandard.getText().toString();
    	if(value==null || value.equals("")){
    		value="0";
    	}
    	return Float.parseFloat( value);
    	
    }
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * 
     * getHisNewSalaryStandardNotCheckValue
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    public float   getHisNewSalaryStandardNotCheckValue(){

    	String value =txtUserHisNewSalaryStandard.getText().toString();
    	if(value==null || value.equals("")){
    		value="0";
    	}
    	return Float.parseFloat( value);
    	
    }
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * 
     * setHisNewSalaryPercent
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    public void  setHisNewSalaryPercent(float value){
    	txtUserHisNewSalaryPercent.setText(String.valueOf( value ));
    }
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * 
     * getHisNewSalaryPercent
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    public float   getHisNewSalaryPercent(){
    	if(!getHisSalaryCheck()){
    		return 0;
    	}
    	String value =txtUserHisNewSalaryPercent.getText().toString();
    	if(value==null || value.equals("")){
    		value="0";
    	}
    	return Float.parseFloat( value);
    	
    }
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * 
     * getHisNewSalaryPercentNotCheckValue
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    public float   getHisNewSalaryPercentNotCheckValue(){
    	String value =txtUserHisNewSalaryPercent.getText().toString();
    	if(value==null || value.equals("")){
    		value="0";
    	}
    	return Float.parseFloat( value);
    	
    }
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * 
     * setHisNewSalaryActualUp
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    public void  setHisNewSalaryActualUp(float value){
    	txtUserHisNewSalaryActualUp.setText(String.valueOf( value ));
    }
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * 
     * getHisNewSalaryActualUp
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    public float   getHisNewSalaryActualUp(){
    	if(!getHisSalaryCheck()){
    		return 0;
    	}
    	String value =txtUserHisNewSalaryActualUp.getText().toString();
    	if(value==null || value.equals("")){
    		value="0";
    	}
    	return Float.parseFloat( value);
    	
    }
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * 
     * getHisNewSalaryActualUpNotCheckValue
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    public float   getHisNewSalaryActualUpNotCheckValue(){
    	String value =txtUserHisNewSalaryActualUp.getText().toString();
    	if(value==null || value.equals("")){
    		value="0";
    	}
    	return Float.parseFloat( value);
    	
    }
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * 
     * setHisNewSalaryNextYM
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    public void  setHisNewSalaryNextYM(String value){
    	txtUserHisNewSalaryNextYM.setText(value);
    }
    
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * 
     * getHisNewSalaryNextYM
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    public String   getHisNewSalaryNextYM(){
    	return txtUserHisNewSalaryNextYM.getText().toString();
    }
    
    
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * 
     * getHisJapaneseCheck
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    public boolean getHisJapaneseCheck(){
    	return chkUserHisNewJapanese.isChecked();
    }
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * 
     * setHisJapaneseCheck
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    public void  setHisJapaneseCheck(boolean value){
    	chkUserHisNewJapanese.setChecked(value );
    }
    
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * 
     * getHisAllowance_BusinessCheck
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    public boolean getHisAllowance_BusinessCheck(){
    	return chkUserHisNewAllowance_Business.isChecked();
    }
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * 
     * setHisAllowance_BusinessCheck
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    public void  setHisAllowance_BusinessCheck(boolean value){
    	chkUserHisNewAllowance_Business.setChecked(value );
    }

	/**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
	 *
	 * getHisAllowance_BSECheck
	 *
	 ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
	public boolean getHisAllowance_BSECheck(){
		return chkUserHisNewAllowance_BSE.isChecked();
	}
	/**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
	 *
	 * setHisAllowance_BSECheck
	 *
	 ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
	public void  setHisAllowance_BSECheck(boolean value){
		chkUserHisNewAllowance_BSE.setChecked(value );
	}

    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * 
     * getHisSalaryCheck
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    public boolean getHisSalaryCheck(){
    	return chkUserHisNewSalary.isChecked();
    }
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * 
     * setHisSalaryCheck
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    public void  setHisSalaryCheck(boolean value){
    	chkUserHisNewSalary.setChecked(value );
    }
    
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * 
     * setHisSalaryCheckStatus
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    public void  setHisSalaryCheckStatus(boolean enable){
		setHisSalaryCheck(false); //luon setting init la false
    	chkUserHisNewSalary.setEnabled(enable);
    	setHisNewSalaryStandard(0);
    	setHisNewSalaryActualUp(0);
    	setHisNewSalaryPercent(0);
    	setHisNewSalaryNextYM("");
    	setHisNewSalary(0);
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
	
}
