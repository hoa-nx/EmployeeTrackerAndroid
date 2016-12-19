
package com.ussol.employeetracker;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TooManyListenersException;

import com.ussol.employeetracker.EditUserBasic.SelectDateFragment;
import com.ussol.employeetracker.helpers.AlertDialogRadio;
import com.ussol.employeetracker.helpers.ConvertCursorToArrayString;
import com.ussol.employeetracker.helpers.ConvertCursorToListString;
import com.ussol.employeetracker.helpers.DatabaseAdapter;
import com.ussol.employeetracker.helpers.GetListDataHelper;
import com.ussol.employeetracker.helpers.AlertDialogRadio.AlertPositiveListener;
import com.ussol.employeetracker.helpers.UserDialogAdapter;
import com.ussol.employeetracker.models.Dept;
import com.ussol.employeetracker.models.MasterConstants;
import com.ussol.employeetracker.models.Position;
import com.ussol.employeetracker.models.Team;
import com.ussol.employeetracker.models.User;
import com.ussol.employeetracker.utils.DateTimeUtil;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class EditUserWork extends Fragment implements OnClickListener , OnTouchListener, OnCheckedChangeListener {

	static final int DATE_DIALOG_ID = 0;
    private TextView  txtUserDeptCode , txtUserTeamCode , txtUserPositionCode ;
    private EditText  txtUserDeptName , txtUserTeamName , txtUserPositionName, txtUserKeikenConvert ;
    private EditText txtUserTraining_date,txtUserTraining_dateEnd , txtUserJoin_date,txtUserIn_date, txtUserOut_date, txtUserLabour_Out_date ;
    private EditText txtUserLearnTraining_date,txtUserLearnTraining_dateEnd ;
    private Button btnUserSave , btnUserCancel, btnUserDeptName , btnUserTeamName , btnUserPositionName;
    private Button btnUserTraining_date,btnUserTraining_dateEnd ,btnUserIn_date,btnUserOut_date,btnUserJoin_date,btnUserLabour_Out_date; 
    private Button btnUserLearnTraining_date,btnUserLearnTraining_dateEnd ;
    private CheckBox chkUserOut_date; 
    private ToggleButton tbtIsLabour ;
    /** Lưu lại vị trí mà đã chọn từ list radio */
	int positionDept = 0;
	int positionPos = 0;
	int positionTeam= 0;
	/** chuyển đổi Cursor thành string array */
	ConvertCursorToArrayString mConvertCursorToArrayString;
	ConvertCursorToListString mConvertCursorToListString;
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
	
	/** Button nào được click */
	private static  int btnClicked ;
	/** kết nối Database */
	DatabaseAdapter mDatabaseAdapter;
	/** đối tượng hiển thị tại các màn hình search*/
	GetListDataHelper mGetListDataHelper;
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

	/**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
	 * onCreateView
	 * 
	 ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		ViewGroup root = (ViewGroup) inflater.inflate(R.layout.activity_user_edit_work, null);		
		/** setting tab để có thể truy xuất từ các Tab khác*/
		((EditUserMainActivity)getActivity()).setTabFragmentWork(getTag());
		return root;
	}
		
	/**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
	 * onActivityCreated
	 * 
	 ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mDatabaseAdapter = new DatabaseAdapter(getActivity());
		/** khởi tạo đối tượng */
		mGetListDataHelper = new GetListDataHelper(getActivity());
		/** get code và thông tin nhân viên*/
		nCode=EditUserMainActivity.nCode;
		userInfo =EditUserMainActivity.userInfo;
		/** get các control date và gán sự kiện khi click vào textbox */
		getControl();
		settingListener();
		settingInputType();
		/*
		*//** get thông tin phòng ban dùng cho search dialog*//*
		mConvertCursorToArrayString = new ConvertCursorToArrayString(getActivity());
		dataDept = mConvertCursorToArrayString.getDeptList();
		
		*//** get thông tin nhóm dùng cho search dialog*//*
		dataTeam = mConvertCursorToArrayString.getTeamList();
		
		*//** get thông tin chức vụ dùng cho search dialog*//*
		dataPosition = mConvertCursorToArrayString.getPositionList();*/
		
		if (nCode==-1){
			/** trường hợp tạo mới*/
			/** setting code user*/
			
		}else if (nCode==0){
			/** hiển thị copy data*/
			if (isFirstRun){
				setValueTabWork(userInfo);
			}
		}else{
			/** hiển thị chi tiết */
			if (isFirstRun){
				
				setValueTabWork(userInfo);
				
			}
		}
		isFirstRun = false;
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
		case R.id.txtUserTraining_date:
			//selectDate(v);
			break;
		case R.id.txtUserTraining_dateEnd:
			//selectDate(v);
			break;
		case R.id.btnUserDeptName:
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
	    	/*
	    	final Dialog dialog;

            dialog = new Dialog(getActivity());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_user_list);
            dialog.setTitle("");
			ListView lst = (ListView) dialog.findViewById(R.id.list);
			ListAdapter adapter = new DialogAdapter( getActivity(),_users);
			lst.setAdapter(adapter);

			lst.setOnItemClickListener(new OnItemClickListener() {
				@Override
                public void onItemClick(AdapterView<?> arg0, View arg1,
                        int arg2, long arg3) {
					dialog.dismiss();
                }
			});

			dialog.show();*/

			/**hiển thị màn hình để chọn phong ban */
			/** Getting the fragment manager */
			FragmentManager manager =getActivity().getFragmentManager();
			
			/** Instantiating the DialogFragment class */
			//AlertDialogRadio alert = new AlertDialogRadio("",dataDept,R.id.btnUserDeptName);
			AlertDialogRadio alert = new AlertDialogRadio("",_depts,R.id.btnUserDeptName);

			/** Creating a bundle object to store the selected item's index */
			Bundle b  = new Bundle();
			
			/** Storing the selected item's index in the bundle object */
			b.putInt("position", positionDept);
			
			/** Setting the bundle object to the dialog fragment object */
			alert.setArguments(b);
			
			/** Creating the dialog fragment object, which will in turn open the alert dialog window */
			alert.show(manager, MasterConstants.TAB_DIALOG_TAG);	
			break;
			
			
		case R.id.btnUserTeamName:
			/*_listTeam =getListTeam("");
	    	
			_teams = new Team[_listTeam.size()];
	    	int indexTeam=0;
	    	for (Team li : _listTeam){
	    		_teams[indexTeam] = li;
	    		indexTeam++;
	    	}*/
			if (getDeptCode()!=0){
				_teams  = mGetListDataHelper.getArrayTeam(" AND " + DatabaseAdapter.KEY_YOBI_CODE1 + " =" + getDeptCode());	
			}else{
				_teams  = mGetListDataHelper.getArrayTeam("");
			}
			
	    	if (_teams.length==0){
	    		return;
	    	}
			/**hiển thị màn hình để chọn nhóm*/
			/** Getting the fragment manager */
			FragmentManager managerTeam =getActivity().getFragmentManager();
			
			/** Instantiating the DialogFragment class */
			//AlertDialogRadio alertTeam = new AlertDialogRadio("",dataTeam, R.id.btnUserTeamName);
			AlertDialogRadio alertTeam = new AlertDialogRadio("",_teams,R.id.btnUserTeamName);
			/** Creating a bundle object to store the selected item's index */
			Bundle bTeam  = new Bundle();
			
			/** Luu giu tri cua code phong ban da chon */
			bTeam.putInt("dept", getDeptCode());
			
			/** Storing the selected item's index in the bundle object */
			bTeam.putInt("position", positionTeam);
			
			/** Setting the bundle object to the dialog fragment object */
			alertTeam.setArguments(bTeam);
			
			/** Creating the dialog fragment object, which will in turn open the alert dialog window */
			alertTeam.show(managerTeam, MasterConstants.TAB_DIALOG_TAG);	
			break;
		case R.id.btnUserPositionName:
			/*_listPosition =getListPosition("");
	    	
			_Positions= new Position[_listPosition.size()];
			int indexPosition=0;
	    	for (Position li : _listPosition){
	    		_Positions[indexPosition] = li;
	    		indexPosition++;
	    	}*/
			_Positions  = mGetListDataHelper.getArrayPosition("");
	    	if (_Positions.length==0){
	    		return;
	    	}
	    	
			/**hiển thị màn hình để chọn nhóm*/
			/** Getting the fragment manager */
			FragmentManager managerPosition =getActivity().getFragmentManager();
			
			/** Instantiating the DialogFragment class */
			//AlertDialogRadio alertPosition = new AlertDialogRadio("",dataPosition , R.id.btnUserPositionName);
			AlertDialogRadio alertPosition = new AlertDialogRadio("",_Positions,R.id.btnUserPositionName);
			/** Creating a bundle object to store the selected item's index */
			Bundle bPosition  = new Bundle();
			
			/** Storing the selected item's index in the bundle object */
			bPosition.putInt("position", positionPos);
			
			/** Setting the bundle object to the dialog fragment object */
			alertPosition.setArguments(bPosition);
			
			/** Creating the dialog fragment object, which will in turn open the alert dialog window */
			alertPosition.show(managerPosition, MasterConstants.TAB_DIALOG_TAG);	
			break;
		case R.id.txtUserIn_date:
			//selectDate(v);
			break;
			
		case R.id.txtUserJoin_date:
			//selectDate(v);
			break;

		case R.id.txtUserOut_date:
			//selectDate(v);
			break;

		case R.id.txtUserLabour_Out_date:
			//selectDate(v);
			break;
			
		case R.id.btnUserSave:
			
			break;
		case R.id.btnUserCancel:
			getActivity().finish();
			break;
		case R.id.btnUserTraining_date:
			txtUserTraining_date.setText("");
			break;
		case R.id.btnUserTraining_dateEnd:
			txtUserTraining_dateEnd.setText("");
			break;
		case R.id.btnUserLearnTraining_date:
			txtUserLearnTraining_date.setText("");
			break;
		case R.id.btnUserLearnTraining_dateEnd:
			txtUserLearnTraining_dateEnd.setText("");
			break;
		case R.id.btnUserIn_date:
			txtUserIn_date.setText("");
			break;
		case R.id.btnUserOut_date:
			txtUserOut_date.setText("");
			break;
		case R.id.btnUserJoin_date:
			txtUserJoin_date.setText("");
			break;
		case R.id.btnUserLabour_Out_date:
			txtUserLabour_Out_date.setText("");
			break;
		}
	}
	 /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * Defining button click listener for the OK button of the alert dialog window 
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    
    public void onPositiveClick(int position, int clickBtn , Bundle bundle) {
    	/** Setting the selected android version in the textview */
    	switch (clickBtn) {
    		/** Phân nhánh xử lý tùy theo button mà đã được clicked*/
	    	case R.id.btnUserDeptName:
	    		/** gán lại vị trí của item mà đã chọn */
	    		this.positionDept = position;
	    		/** gán tên người đã chọn tại màn hình poup */
	    		txtUserDeptName.setText(_depts[position].ryaku);    
	    		txtUserDeptCode.setText(String.valueOf(_depts[position].code));
	    		break;
	    	case R.id.btnUserTeamName:
	    		/** gán lại vị trí của item mà đã chọn */
	    		this.positionTeam = position;
	    		/** gán tên người đã chọn tại màn hình poup */
	    		//txtUserTeamName.setText(dataTeam[this.positionTeam]);    
	    		txtUserTeamName.setText(_teams[position].ryaku);    
	    		txtUserTeamCode.setText(String.valueOf(_teams[position].code));
	    		break;
	    	case R.id.btnUserPositionName:
	    		/** gán lại vị trí của item mà đã chọn */
	    		this.positionPos = position;
	    		/** gán tên người đã chọn tại màn hình poup */
	    		//txtUserPositionName.setText(dataPosition[this.positionPos]);
	    		txtUserPositionName.setText(_Positions[position].yobi_text1);    
	    		txtUserPositionCode.setText(String.valueOf(_Positions[position].code));
	    		break;
    	}
    }
	/**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
	 * selectDate
	 * 
	 ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
	public void selectDate(View view) {
	        DialogFragment newFragment = new SelectDateFragment(view.getId());
	        newFragment.show(getActivity().getFragmentManager(),String.valueOf( view.getId()));
	 }
	/**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
	 * populateSetDate
	 * 
	 ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    public void populateSetDate(int control,int year, int month, int day) {
    	//mDateDisplay = (EditText)findViewById(R.id.editText1);
    	switch (control){
    			
    		case R.id.txtUserTraining_date:
    			txtUserTraining_date.setText(day + MasterConstants.DATE_SEPERATE_CHAR + month+ MasterConstants.DATE_SEPERATE_CHAR +year);
    			break;
    			
    		case R.id.txtUserTraining_dateEnd:
    			txtUserTraining_dateEnd.setText(day + MasterConstants.DATE_SEPERATE_CHAR + month+ MasterConstants.DATE_SEPERATE_CHAR +year);
    			break;
    			
    		case R.id.txtUserLearnTraining_date:
    			txtUserLearnTraining_date.setText(day + MasterConstants.DATE_SEPERATE_CHAR + month+ MasterConstants.DATE_SEPERATE_CHAR +year);
    			break;
    			
    		case R.id.txtUserLearnTraining_dateEnd:
    			txtUserLearnTraining_dateEnd.setText(day + MasterConstants.DATE_SEPERATE_CHAR + month+ MasterConstants.DATE_SEPERATE_CHAR +year);
    			break;
    				
    		case R.id.txtUserIn_date:
    			txtUserIn_date.setText(day + MasterConstants.DATE_SEPERATE_CHAR + month+ MasterConstants.DATE_SEPERATE_CHAR +year);
    			break;
    			
    		case R.id.txtUserJoin_date:
    			txtUserJoin_date.setText(day + MasterConstants.DATE_SEPERATE_CHAR + month+ MasterConstants.DATE_SEPERATE_CHAR +year);
    			break;

    		case R.id.txtUserOut_date:
    			txtUserOut_date.setText(day + MasterConstants.DATE_SEPERATE_CHAR + month+ MasterConstants.DATE_SEPERATE_CHAR +year);
    			break;
    		case R.id.txtUserLabour_Out_date:
    			txtUserLabour_Out_date.setText(day + MasterConstants.DATE_SEPERATE_CHAR + month+ MasterConstants.DATE_SEPERATE_CHAR +year);
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
    	@SuppressWarnings("deprecation")
		@Override
    	public Dialog onCreateDialog(Bundle savedInstanceState) {
    		String date =null;
    		switch (mControl){
	    		case R.id.txtUserTraining_date:
	    			date = txtUserTraining_date.getText().toString();
	    			break;
	    		case R.id.txtUserTraining_dateEnd:
	    			date = txtUserTraining_dateEnd.getText().toString();
	    			break;
	    		case R.id.txtUserLearnTraining_date:
	    			date = txtUserLearnTraining_date.getText().toString();
	    			break;
	    		case R.id.txtUserLearnTraining_dateEnd:
	    			date = txtUserLearnTraining_dateEnd.getText().toString();
	    			break;
	    		case R.id.txtUserIn_date:
	    			date = txtUserIn_date.getText().toString();
	    			break;
	    			
	    		case R.id.txtUserJoin_date:
	    			date = txtUserJoin_date.getText().toString();
	    			break;
	
	    		case R.id.txtUserOut_date:
	    			date = txtUserOut_date.getText().toString();
	    			break;
	    		case R.id.txtUserLabour_Out_date:
	    			date = txtUserLabour_Out_date.getText().toString();
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
    			Date datefmt = DateTimeUtil.convertStringToDate(date,  MasterConstants.DATE_VN_FORMAT);
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
    	txtUserDeptCode = (TextView)getView().findViewById(R.id.txtUserDeptCode);
    	txtUserTeamCode = (TextView)getView().findViewById(R.id.txtUserTeamCode);
    	txtUserPositionCode = (TextView)getView().findViewById(R.id.txtUserPositionCode);
    	
    	txtUserDeptName= (EditText)getView().findViewById(R.id.txtUserDeptName);
    	txtUserTeamName= (EditText)getView().findViewById(R.id.txtUserTeamName);
    	txtUserPositionName= (EditText)getView().findViewById(R.id.txtUserPositionName);

    	txtUserTraining_date = (EditText)getView().findViewById(R.id.txtUserTraining_date);
    	txtUserTraining_dateEnd = (EditText)getView().findViewById(R.id.txtUserTraining_dateEnd);
    	
    	txtUserLearnTraining_date = (EditText)getView().findViewById(R.id.txtUserLearnTraining_date);
    	txtUserLearnTraining_dateEnd = (EditText)getView().findViewById(R.id.txtUserLearnTraining_dateEnd);
    	
    	txtUserKeikenConvert= (EditText)getView().findViewById(R.id.txtUserKeikenConvertFJN);
    	
    	txtUserIn_date = (EditText)getView().findViewById(R.id.txtUserIn_date);
		txtUserJoin_date = (EditText)getView().findViewById(R.id.txtUserJoin_date);
		txtUserOut_date = (EditText)getView().findViewById(R.id.txtUserOut_date);
		chkUserOut_date = (CheckBox)getView().findViewById(R.id.chkUserOut_date);
		txtUserLabour_Out_date= (EditText)getView().findViewById(R.id.txtUserLabour_Out_date);
		
		tbtIsLabour = (ToggleButton)getView().findViewById(R.id.tbtUserIsLabour);
		
		btnUserDeptName= (Button)getView().findViewById(R.id.btnUserDeptName);
		btnUserTeamName= (Button)getView().findViewById(R.id.btnUserTeamName);
		btnUserPositionName= (Button)getView().findViewById(R.id.btnUserPositionName);
    	
		btnUserTraining_date= (Button)getView().findViewById(R.id.btnUserTraining_date);
		btnUserTraining_dateEnd= (Button)getView().findViewById(R.id.btnUserTraining_dateEnd);
		
		btnUserLearnTraining_date= (Button)getView().findViewById(R.id.btnUserLearnTraining_date);
		btnUserLearnTraining_dateEnd= (Button)getView().findViewById(R.id.btnUserLearnTraining_dateEnd);
		
		btnUserIn_date= (Button)getView().findViewById(R.id.btnUserIn_date);
		btnUserJoin_date= (Button)getView().findViewById(R.id.btnUserJoin_date);
		btnUserOut_date= (Button)getView().findViewById(R.id.btnUserOut_date);
		btnUserLabour_Out_date= (Button)getView().findViewById(R.id.btnUserLabour_Out_date);
		/*btnUserSave = (Button)getView().findViewById(R.id.btnUserSave);
		btnUserCancel= (Button)getView().findViewById(R.id.btnUserCancel);*/
    }

    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * gán sự kiện cho các control
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    private void settingListener(){
		txtUserTraining_date.setOnClickListener(this);
		txtUserTraining_dateEnd.setOnClickListener(this);
		txtUserLearnTraining_date.setOnClickListener(this);
		txtUserLearnTraining_dateEnd.setOnClickListener(this);
		txtUserIn_date.setOnClickListener(this);
		txtUserJoin_date.setOnClickListener(this);
		txtUserOut_date.setOnClickListener(this);
		txtUserLabour_Out_date.setOnClickListener(this);
		
		txtUserTraining_date.setOnTouchListener(this);
		txtUserTraining_dateEnd.setOnTouchListener(this);
		txtUserLearnTraining_date.setOnTouchListener(this);
		txtUserLearnTraining_dateEnd.setOnTouchListener(this);
		txtUserIn_date.setOnTouchListener(this);
		txtUserJoin_date.setOnTouchListener(this);
		txtUserOut_date.setOnTouchListener(this);
		txtUserLabour_Out_date.setOnTouchListener(this);
		
		btnUserDeptName.setOnClickListener(this);
		btnUserTeamName.setOnClickListener(this);
		btnUserPositionName.setOnClickListener(this);
		
		btnUserTraining_date.setOnClickListener(this);
		btnUserTraining_dateEnd.setOnClickListener(this);
		btnUserLearnTraining_date.setOnClickListener(this);
		btnUserLearnTraining_dateEnd.setOnClickListener(this);
		btnUserIn_date.setOnClickListener(this);
		btnUserJoin_date.setOnClickListener(this);
		btnUserOut_date.setOnClickListener(this);
		btnUserLabour_Out_date.setOnClickListener(this);
		
		chkUserOut_date.setOnCheckedChangeListener(this);
		/*btnUserSave.setOnClickListener(this);
		btnUserCancel.setOnClickListener(this);*/
		
		/** xử lý double click để xóa dữ liệu đã input phòng ban*/
	    @SuppressWarnings("deprecation")
		final GestureDetector gestureDetectorDept = new GestureDetector(new GestureDetector.SimpleOnGestureListener() {
	        public boolean onDoubleTap(MotionEvent e) {
	        	txtUserDeptName.setText("");
	        	txtUserDeptCode.setText("");
	            return true;
	        }
	    });
	    txtUserDeptName.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				return gestureDetectorDept.onTouchEvent(event);
			}
		});
	    
	    /** xử lý double click để xóa dữ liệu đã input nhóm*/
	    @SuppressWarnings("deprecation")
		final GestureDetector gestureDetectorTeam = new GestureDetector(new GestureDetector.SimpleOnGestureListener() {
	        public boolean onDoubleTap(MotionEvent e) {
	        	txtUserTeamName.setText("");
	        	txtUserTeamCode.setText("");
	            return true;
	        }
	    });
	    txtUserTeamName.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				return gestureDetectorTeam.onTouchEvent(event);
			}
		});
	    
	    /** xử lý double click để xóa dữ liệu đã input nhóm*/
	    @SuppressWarnings("deprecation")
		final GestureDetector gestureDetectorPosition = new GestureDetector(new GestureDetector.SimpleOnGestureListener() {
	        public boolean onDoubleTap(MotionEvent e) {
	        	txtUserPositionName.setText("");
	        	txtUserPositionCode.setText("");
	            return true;
	        }
	    });
	    txtUserPositionName.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				return gestureDetectorPosition.onTouchEvent(event);
			}
		});
	    
	    /** xử lý double click để xóa dữ liệu đã input tai ngay vao cong ty*/
	    @SuppressWarnings("deprecation")
		final GestureDetector gestureDetectorInDate = new GestureDetector(new GestureDetector.SimpleOnGestureListener() {
	        public boolean onDoubleTap(MotionEvent e) {
	        	txtUserIn_date.setText("");
	            return true;
	        }
	    });
	       
    }
    
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * setting input cho control(không hiển thị bàn phím khi nhận focus)
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    private void settingInputType(){
        txtUserTraining_date.setInputType(InputType.TYPE_NULL);
        txtUserTraining_dateEnd.setInputType(InputType.TYPE_NULL);
        txtUserLearnTraining_date.setInputType(InputType.TYPE_NULL);
        txtUserLearnTraining_dateEnd.setInputType(InputType.TYPE_NULL);
        txtUserIn_date.setInputType(InputType.TYPE_NULL);
		txtUserJoin_date.setInputType(InputType.TYPE_NULL);
		txtUserOut_date.setInputType(InputType.TYPE_NULL);
		txtUserLabour_Out_date.setInputType(InputType.TYPE_NULL);
    }
    
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * xử lý thay đổi item màn hình theo check box nghỉ việc
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		switch(buttonView.getId()){
			case R.id.chkUserOut_date:
				txtUserOut_date.setEnabled(isChecked);
				if(!isChecked){
					/** xoa tri neu nhu chuyen tu nghih viec sang chua nghi viec */
					setOut_date("");
				}
				break;
		}
	}
	
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * hiển thị dialog date khi chạm vào màn hình
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    @Override
    public boolean onTouch (View v, MotionEvent event){
    	/** phân nhánh xử lý theo từng button*/
		switch (v.getId()) {
			case R.id.txtUserTraining_date:
				if(event.getAction()==MotionEvent.ACTION_UP){
					selectDate(v);
				}
				break;
		
			case R.id.txtUserTraining_dateEnd:
				if(event.getAction()==MotionEvent.ACTION_UP){
					selectDate(v);
				}
				break;
			case R.id.txtUserLearnTraining_date:
				if(event.getAction()==MotionEvent.ACTION_UP){
					selectDate(v);
				}
				break;
		
			case R.id.txtUserLearnTraining_dateEnd:
				if(event.getAction()==MotionEvent.ACTION_UP){
					selectDate(v);
				}
				break;
			case R.id.txtUserIn_date:
				if(event.getAction()==MotionEvent.ACTION_UP){
					selectDate(v);
				}
				break;
				
			case R.id.txtUserJoin_date:
				if(event.getAction()==MotionEvent.ACTION_UP){
					selectDate(v);
				}
				break;

			case R.id.txtUserOut_date:
				if(event.getAction()==MotionEvent.ACTION_UP){
					selectDate(v);
				}
				break;

			case R.id.txtUserLabour_Out_date:
				if(event.getAction()==MotionEvent.ACTION_UP){
					selectDate(v);
				}
				break;
		}
    	return true; 
    }
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * hiển thị giá trị lên màn hình
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    public void setValueTabWork(User item){
		/**thông tin của tab Work*/
		setDeptCode(String.valueOf(item.dept));
		setDeptName(item.dept_name);
		setTeamCode(String.valueOf(item.team));
		setTeamName(item.team_name);
		setPositionCode(String.valueOf(item.position));
		setPositionName(item.position_name);
		
		setTraining_date(item.training_date);
		setTraining_dateEnd(item.training_dateEnd);
		
		setLearnTraining_date(item.learn_training_date);
		setLearnTraining_dateEnd(item.learn_training_dateEnd);
		
		setKeikenConvert(item.convert_keiken);
		
		setIn_date(item.in_date);
		setOut_date(item.out_date);
		setJoin_date(item.join_date);
		setLabourOut_date(item.labour_out_date);
		setChkOut_date(item.out_date);
		setIsCurrentLabour(String.valueOf(item.isLabour));
		
    }
    
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * lấy thông tin phòng ban
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    
    public int getDeptCode(){
    	try{
    		return Integer.parseInt( txtUserDeptCode.getText().toString());	
    	}
    	catch(Exception e)
    	{
    		return 0;
    	}
    }

    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * set code phòng ban
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    
    public void setDeptCode(String value){
    	txtUserDeptCode.setText(value.toString());
    }
    
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * lấy thông tin phòng ban
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    
    public String getDeptName(){
    	return txtUserDeptName.getText().toString();
    }
    
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * set thông tin phòng ban
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    
    public void setDeptName(String value){
    		
    	txtUserDeptName.setText(value);
    }
    
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * lấy thông tin nhóm
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    
    public int getTeamCode(){
    	try{
    		return Integer.parseInt( txtUserTeamCode.getText().toString());	
    	}
    	catch(Exception e)
    	{
			return 0;
    	}
    }

    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * set code nhóm
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    
    public void setTeamCode(String value){
    	txtUserTeamCode.setText(value);
    }
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * lấy tên nhóm
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    
    public String getTeamName(){
    	return txtUserTeamName.getText().toString();
    }
    
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * set tên nhóm
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    
    public void setTeamName(String value){
    		
    	txtUserTeamName.setText(value);
    }
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * lấy thông tin  chức vụ
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    
    public int getPositionCode(){
    	try{
    		return Integer.parseInt( txtUserPositionCode.getText().toString());
    	}
    	catch(Exception e )
    	{
    		return 0;
    	}
    }

    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * set code chức vụ
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    
    public void setPositionCode(String value){
    	txtUserPositionCode.setText(value);
    }
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * lấy tên  chức vụ
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    
    public String getPositionName(){
    	return txtUserPositionName.getText().toString();
    }
    
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * set tên  chức vụ
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    
    public void setPositionName(String value){
    		
    	txtUserPositionName.setText(value);
    }

	/**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * lấy ngày nhận vào thử việc
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    
    public String getTraining_date(){
    	return txtUserTraining_date.getText().toString();
    }
    
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * set ngày nhận vào thử việc
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    
    public void setTraining_date(String value){
    		
    	txtUserTraining_date.setText(value);
    }
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * lấy ngày hoan thanh thử việc
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    
    public String getTraining_dateEnd(){
    	return txtUserTraining_dateEnd.getText().toString();
    }
    
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * set ngày hoan thanh thử việc
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    
    public void setTraining_dateEnd(String value){
    		
    	txtUserTraining_dateEnd.setText(value);
    }
    
    
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * lấy ngày nhận vào học việc
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    
    public String getLearnTraining_date(){
    	return txtUserLearnTraining_date.getText().toString();
    }
    
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * set ngày nhận vào học việc
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    
    public void setLearnTraining_date(String value){
    		
    	txtUserLearnTraining_date.setText(value);
    }
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * lấy ngày hoan thanh học việc
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    
    public String getLearnTraining_dateEnd(){
    	return txtUserLearnTraining_dateEnd.getText().toString();
    }
    
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * set ngày hoan thanh học việc
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    
    public void setLearnTraining_dateEnd(String value){
    		
    	txtUserLearnTraining_dateEnd.setText(value);
    }
    
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * lấy ngày nhận vào làm việc
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    
    public String getIn_date(){
    	return txtUserIn_date.getText().toString();
    }
    
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * set ngày nhận vào làm việc
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    
    public void setIn_date(String value){
    		
    	txtUserIn_date.setText(value);
    }
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * lấy ngày nhận vào làm việc
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    
    public String getJoin_date(){
    	return txtUserJoin_date.getText().toString();
    }
    
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * set ngày nhận vào làm việc
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    
    public void setJoin_date(String value){
    		
    	txtUserJoin_date.setText(value);
    }
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * lấy ngày nghỉ việc
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    
    public String getOut_date(){
    	return txtUserOut_date.getText().toString();
    }
    
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * set ngày nghỉ việc
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    
    public void setOut_date(String value){
    		
    	txtUserOut_date.setText(value);
    }
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * set checkbox ngày nghỉ việc
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    
    public void setChkOut_date(String value){
    	if(value==null || value.equals("")){
    		chkUserOut_date.setChecked(false);
    		txtUserOut_date.setEnabled(false);
    	}else{
    		chkUserOut_date.setChecked(true);
    		txtUserOut_date.setEnabled(true);
    	}
    }
    
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * lấy ngày ra khỏi nhóm labour
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    
    public String getLabourOut_date(){
    	return txtUserLabour_Out_date.getText().toString();
    }
    
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * set ngày  ra khỏi nhóm labour
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    
    public void setLabourOut_date(String value){
    		
    	txtUserLabour_Out_date.setText(value);
    }
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * 
     * có thuộc nhóm labour hiện tại
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    public int  getIsCurrentLabour(){
    	return Boolean.toString(tbtIsLabour.isChecked()).equals("true")?1:0;
    }
    
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * 
     * có thuộc nhóm labour hiện tại
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    public void  setIsCurrentLabour(String value){
    	if (value.equals("0")){
    		tbtIsLabour.setChecked(false);
    	}else{
    		tbtIsLabour.setChecked(true);
    	}
    }
    
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * lấy thông tin qui đổi kinh nghiệm
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    
    public String getKeikenConvert(){
    	return txtUserKeikenConvert.getText().toString();
    }
    
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * set thông tin qui đổi kinh nghiệm
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    
    public void setKeikenConvert(String value){
    		
    	txtUserKeikenConvert.setText(value);
    }
    
    
  	/**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * get data cho list
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
	public List<User> getListUser(String xWhere){
		
		/** chuyển đổi từ Cursor thành List */
		mConvertCursorToListString = new ConvertCursorToListString(getActivity());
		return mConvertCursorToListString.getUserList(xWhere);
	}

	/**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * get list dept 
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
	public List<Dept> getListDept(String xWhere){
		
		/** chuyển đổi từ Cursor thành List */
		mConvertCursorToListString = new ConvertCursorToListString(getActivity());
		return mConvertCursorToListString.getDeptList(xWhere);
	}
	
	/**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * get list Team 
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
	public List<Team> getListTeam(String xWhere){
		
		/** chuyển đổi từ Cursor thành List */
		mConvertCursorToListString = new ConvertCursorToListString(getActivity());
		return mConvertCursorToListString.getTeamList(xWhere);
	}
	
	/**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * get list position 
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
	public List<Position> getListPosition(String xWhere){
		
		/** chuyển đổi từ Cursor thành List */
		mConvertCursorToListString = new ConvertCursorToListString(getActivity());
		return mConvertCursorToListString.getPositionList(xWhere);
	}
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * 
     * setIsFirstRun
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    public void setIsFirstRun(boolean value){
    	this.isFirstRun=value;
    }


}
