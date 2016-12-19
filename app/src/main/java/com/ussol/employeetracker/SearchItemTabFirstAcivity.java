package com.ussol.employeetracker;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

import com.google.gson.Gson;
import com.ussol.employeetracker.helpers.AlertDialogRadio;
import com.ussol.employeetracker.helpers.AlertDialogRadio.AlertPositiveListener;
import com.ussol.employeetracker.helpers.ConvertCursorToArrayString;
import com.ussol.employeetracker.helpers.ConvertCursorToListString;
import com.ussol.employeetracker.helpers.DatabaseAdapter;
import com.ussol.employeetracker.helpers.GetListDataHelper;
import com.ussol.employeetracker.helpers.SegmentedGroup;
import com.ussol.employeetracker.models.Dept;
import com.ussol.employeetracker.models.MasterConstants;
import com.ussol.employeetracker.models.Position;
import com.ussol.employeetracker.models.PositionGroup;
import com.ussol.employeetracker.models.Team;
import com.ussol.employeetracker.models.User;
import com.ussol.employeetracker.models.UserPositionGroup;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.LocalActivityManager;
import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;

public class SearchItemTabFirstAcivity  extends Activity implements OnClickListener,AlertPositiveListener , OnCheckedChangeListener,RadioGroup.OnCheckedChangeListener{
	static final int DATE_DIALOG_ID = 0;
    private Button btnSearchItemSave , btnSearchItemCancel;
    private ToggleButton tbtSearchItemDept ,tbtSearchItemTeam,tbtSearchItemPosition;
    private CheckBox chkUserDelete, chkUserIncludeTrialStaff , chkUserOutDate , chkIsLabour,chkSearchItemDept,chkSearchItemTeam,chkSearchItemPosition;
    private SharedPreferences settings;
    private SegmentedGroup segUserYasumiKbn;
    /** Lưu lại vị trí mà đã chọn từ list radio */
	int positionDept = 0;
	int positionPos = 0;
	int positionTeam= 0;
	/** chuyển đổi Cursor thành string array */
	ConvertCursorToArrayString mConvertCursorToArrayString;
	ConvertCursorToListString mConvertCursorToListString;
	/** thông tin các phòng ban hiển thị tại màn hình dialog  */
	Dept[] _depts=null;
	List<Dept> _listDept =null;
	/** thông tin các nhóm hiển thị tại màn hình dialog  */
	Team[] _teams=null;
	List<Team> _listTeam =null;
	/** thông tin các chức vụ hiển thị tại màn hình dialog  */
	Position[] _Positions=null;
	List<Position> _listPosition =null;
	
	/** Button nào được click */
	private static  int btnClicked ;
	/** kết nối Database */
	DatabaseAdapter mDatabaseAdapter;
	/** đối tượng hiển thị tại các màn hình search*/
	GetListDataHelper mGetListDataHelper;
	/** Trị code nhân viên  */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_search_item_tabfirst);
		mDatabaseAdapter = new DatabaseAdapter(getBaseContext());
		/** khởi tạo đối tượng */
		mGetListDataHelper = new GetListDataHelper(getBaseContext());
		/** get các control date và gán sự kiện khi click vào textbox */
		getControl();
		settingListener();
		settingInputType();
		
		_listDept= new ArrayList<Dept>();
		_listTeam= new ArrayList<Team>();
		_listPosition= new ArrayList<Position>();
		/** đọc thông tin lưu trữ tại xml */
		settings = getSharedPreferences(MasterConstants.PRE_SEARCH_ITEM_FILE, Context.MODE_PRIVATE);
		if (settings==null){
		}else{
			/** */
			readPreferencesAndDisplay(settings);
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
		case R.id.tbtSearchItemDept:
			/**hiển thị màn hình để chọn phòng ban*/
			/** Getting the fragment manager */
			tbtSearchItemDept.setChecked(false);
			_depts  = mGetListDataHelper.getArrayDept("");
	    	if (_depts.length==0){
	    		return;
	    	}
	    	
			FragmentManager managerSearchItemDept =getFragmentManager();
			
			/** Instantiating the DialogFragment class */
			//AlertDialogRadio alertPositionGroup = new AlertDialogRadio("",dataAllowance_Business,R.id.btnUserAllowance_business);
			AlertDialogRadio alertDept = new AlertDialogRadio("",_depts,R.id.tbtSearchItemDept,_listDept, true);
			/** Creating a bundle object to store the selected item's index */
			Bundle bDept  = new Bundle();
			
			/** Storing the selected item's index in the bundle object */
			bDept.putInt("position", 0);
		
			/** Setting the bundle object to the dialog fragment object */
			alertDept.setArguments(bDept);
			
			/** Creating the dialog fragment object, which will in turn open the alert dialog window */
			alertDept.show(managerSearchItemDept, MasterConstants.TAB_DIALOG_TAG);	
			break;
		case R.id.tbtSearchItemTeam:
			/**hiển thị màn hình để chọn nhóm*/
			/** Getting the fragment manager */
			tbtSearchItemTeam.setChecked(false);
			_teams = mGetListDataHelper.getArrayTeam("");
	    	if (_teams.length==0){
	    		return;
	    	}
	    	
			FragmentManager managerSearchItemTeam =getFragmentManager();
			
			/** Instantiating the DialogFragment class */
			//AlertDialogRadio alertPositionGroup = new AlertDialogRadio("",dataAllowance_Business,R.id.btnUserAllowance_business);
			AlertDialogRadio alertTeam = new AlertDialogRadio("",_teams,R.id.tbtSearchItemTeam,_listTeam, true);
			/** Creating a bundle object to store the selected item's index */
			Bundle bTeam  = new Bundle();
			
			/** Storing the selected item's index in the bundle object */
			bTeam.putInt("position", 0);
		
			/** Setting the bundle object to the dialog fragment object */
			alertTeam.setArguments(bTeam);
			
			/** Creating the dialog fragment object, which will in turn open the alert dialog window */
			alertTeam.show(managerSearchItemTeam, MasterConstants.TAB_DIALOG_TAG);	
			break;
		case R.id.tbtSearchItemPosition:
			/**hiển thị màn hình để chọn chức vụ*/
			/** Getting the fragment manager */
			tbtSearchItemPosition.setChecked(false);
			_Positions = mGetListDataHelper.getArrayPosition("");
	    	if (_Positions.length==0){
	    		return;
	    	}
	    	
			FragmentManager managerSearchItemPosition =getFragmentManager();
			
			/** Instantiating the DialogFragment class */
			//AlertDialogRadio alertPositionGroup = new AlertDialogRadio("",dataAllowance_Business,R.id.btnUserAllowance_business);
			AlertDialogRadio alertPosition = new AlertDialogRadio("",_Positions,R.id.tbtSearchItemPosition,_listPosition, true);
			/** Creating a bundle object to store the selected item's index */
			Bundle bPosition  = new Bundle();
			
			/** Storing the selected item's index in the bundle object */
			bPosition.putInt("position", 0);
		
			/** Setting the bundle object to the dialog fragment object */
			alertPosition.setArguments(bPosition);
			
			/** Creating the dialog fragment object, which will in turn open the alert dialog window */
			alertPosition.show(managerSearchItemPosition, MasterConstants.TAB_DIALOG_TAG);	
			break;
		case R.id.btnSearchItemSetting:
			/** lưu vào file setting */
			saveToPreferences(settings);
			/** trả về kết quả*/
			Intent intent = new Intent();
			Bundle bundle = new Bundle();
			intent.putExtras(bundle);
			
			if (getParent() == null) {
			    setResult(Activity.RESULT_OK, intent);
			}
			else {
			    getParent().setResult(Activity.RESULT_OK, intent);
			}
			finish();
			break;
		case R.id.btnSearchItemSettingCancel:
			setResult(Activity.RESULT_CANCELED);
			finish();
			break;
		}
	}
	
	/**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
	 * onCheckedChanged(RadioGroup)
	 *
	 ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
			case R.id.radOnlyYamsumi:
				//Toast.makeText(this, "One", Toast.LENGTH_SHORT).show();
				return;
			case R.id.radWorking:
				//Toast.makeText(this, "Two", Toast.LENGTH_SHORT).show();
				return;
			case R.id.radAllUser:
				//Toast.makeText(this, "One", Toast.LENGTH_SHORT).show();
				return;
			default:
				// Nothing to do
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
	    	case R.id.tbtSearchItemDept:
	    		Dept dept;
	    		/** lấy data phòng ban từ bundle */
	    		Dept[] deptArr = (Dept[])bundle.getParcelableArray(MasterConstants.TAB_DIALOG_BUNDLE);
	    		//tbtSearchItemDept.setChecked(false);
	    		if (deptArr.length==0){
	    			_listDept.clear();
	    			tbtSearchItemDept.setChecked(false);
	    		}else{
	    			_listDept.clear();
	    			/** xác định các code phòng ban được check */
	    			for(int i=0 ; i<deptArr.length;i++ ){
	    				if(deptArr[i].isselected==true){
	    					tbtSearchItemDept.setChecked(true);
	    					dept = new Dept();
	    					dept.code =deptArr[i].code;
	    					dept.name = deptArr[i].name;
	    					/** add đối tượng vào list */
	    					_listDept.add(dept);
	    				}
	    			}
	    		}
	    		break;
	    	case R.id.tbtSearchItemTeam:
	    		Team team;
	    		/** lấy data phòng ban từ bundle */
	    		Team[] teamArr = (Team[])bundle.getParcelableArray(MasterConstants.TAB_DIALOG_BUNDLE);
	    		//tbtSearchItemDept.setChecked(false);
	    		if (teamArr.length==0){
	    			_listTeam.clear();
	    			tbtSearchItemTeam.setChecked(false);
	    		}else{
	    			_listTeam.clear();
	    			/** xác định các code phòng ban được check */
	    			for(int i=0 ; i<teamArr.length;i++ ){
	    				if(teamArr[i].isselected==true){
	    					tbtSearchItemTeam.setChecked(true);
	    					team = new Team();
	    					team.code =teamArr[i].code;
	    					team.name = teamArr[i].name;
	    					/** add đối tượng vào list */
	    					_listTeam.add(team);
	    				}
	    			}
	    		}
	    		break;
	    	case R.id.tbtSearchItemPosition:
	    		Position position2;
	    		/** lấy data phòng ban từ bundle */
	    		Position[] positionArr = (Position[])bundle.getParcelableArray(MasterConstants.TAB_DIALOG_BUNDLE);
	    		//tbtSearchItemDept.setChecked(false);
	    		if (positionArr.length==0){
	    			_listPosition.clear();
	    			tbtSearchItemPosition.setChecked(false);
	    		}else{
	    			_listPosition.clear();
	    			/** xác định các code phòng ban được check */
	    			for(int i=0 ; i<positionArr.length;i++ ){
	    				if(positionArr[i].isselected==true){
	    					tbtSearchItemPosition.setChecked(true);
	    					position2 = new Position();
	    					position2.code =positionArr[i].code;
	    					position2.name = positionArr[i].name;
	    					/** add đối tượng vào list */
	    					_listPosition.add(position2);
	    				}
	    			}
	    		}
	    		break;
    	}
    }
    
	/**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * get các control trên màn hình
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    private void getControl(){
    	tbtSearchItemDept = (ToggleButton)findViewById(R.id.tbtSearchItemDept);
    	tbtSearchItemTeam= (ToggleButton)findViewById(R.id.tbtSearchItemTeam);
    	tbtSearchItemPosition = (ToggleButton)findViewById(R.id.tbtSearchItemPosition);
    	chkIsLabour= (CheckBox)findViewById(R.id.chkSearchItemCurrentLabour);
    	chkUserDelete= (CheckBox)findViewById(R.id.chkSearchItemDelete);
    	chkUserIncludeTrialStaff= (CheckBox)findViewById(R.id.chkSearchItemIncludeTrialStaff);
    	//chkUserOutDate= (CheckBox)findViewById(R.id.chkSearchItemOutDate);
    	chkSearchItemDept=(CheckBox)findViewById(R.id.chkSearchItemDept);
    	chkSearchItemTeam=(CheckBox)findViewById(R.id.chkSearchItemTeam);
    	chkSearchItemPosition=(CheckBox)findViewById(R.id.chkSearchItemPosition);
		btnSearchItemSave = (Button)findViewById(R.id.btnSearchItemSetting);
		btnSearchItemCancel= (Button)findViewById(R.id.btnSearchItemSettingCancel);
		segUserYasumiKbn = (SegmentedGroup) findViewById(R.id.segUserYasumiKbn);
    }

    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * gán sự kiện cho các control
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    private void settingListener(){

		btnSearchItemSave.setOnClickListener(this);
		btnSearchItemCancel.setOnClickListener(this);
		
		tbtSearchItemDept.setOnClickListener(this);
		tbtSearchItemTeam.setOnClickListener(this);
		tbtSearchItemPosition.setOnClickListener(this);
		
		chkSearchItemDept.setOnCheckedChangeListener(this);
		chkSearchItemTeam.setOnCheckedChangeListener(this);
		chkSearchItemPosition.setOnCheckedChangeListener(this);
		
		segUserYasumiKbn.setOnCheckedChangeListener(this);
    }
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * setting input cho control(không hiển thị bàn phím khi nhận focus)
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    private void settingInputType(){
    	/*txtUserDeptName.setInputType(InputType.TYPE_NULL);
    	txtUserTeamName.setInputType(InputType.TYPE_NULL);
    	btnUserTeamName.setInputType(InputType.TYPE_NULL);*/
    }
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * Lưu thông tin trên màn hình xuống file
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    public void saveToPreferences(SharedPreferences pre){   	
    	try {
			storeDeptInSharedPreferences(pre);
			storeTeamInSharedPreferences(pre);
			storePositionInSharedPreferences(pre);
			storeCurrentLabourInSharedPreferences(pre);
			storeUserDeleteInSharedPreferences(pre);
			storeUserIncludeTrialStaffInSharedPreferences(pre);
			storeUserOutDateInSharedPreferences(pre);
			storeUserChkDeptInSharedPreferences(pre);
			storeUserChkTeamInSharedPreferences(pre);
			storeUserChkPositionInSharedPreferences(pre);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * Đọc thông tin từ file và hiển thị lên màn hình
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    private void readPreferencesAndDisplay(SharedPreferences pre){
    	
    	try {
			getDeptInSharedPreferences(pre);
			getTeamInSharedPreferences(pre);
			getPositionInSharedPreferences(pre);
			getCurrentLabourInSharedPreferences(pre);
			getUserDeleteInSharedPreferences(pre);
			getUserIncludeTrialStaffInSharedPreferences(pre);
			getUserOutDateInSharedPreferences(pre);
			getUserChkDeptInSharedPreferences(pre);
			getUserChkTeamInSharedPreferences(pre);
			getUserChkPositionInSharedPreferences(pre);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * lưu  thông tin phòng ban
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲
     * @throws JSONException */
    public void storeDeptInSharedPreferences(SharedPreferences pre) throws JSONException{
    	Editor edit = pre.edit();
    	String deptCode = "";
    	int i=0;
    	
    	/*JSONArray jsonArr = new JSONArray();
    	
    	for (Dept item : _listDept){
    		jsonArr.put(i, item);
    		i++;
    	}
    	
    	edit.putString("pre_Dept", jsonArr.toString());*/
    	for (Dept item : _listDept){
    		if(i==0){
    			deptCode=String.valueOf(item.code);
    		}else{
    			deptCode=deptCode + "," + String.valueOf(item.code);
    		}
    		i++;
    	}
    	
    	edit.putString("pre_Dept", deptCode.toString());
    	edit.commit();
    }

    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * get thông tin phòng ban
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲
     * @throws JSONException */
    public void  getDeptInSharedPreferences(SharedPreferences pre) throws JSONException{
    	/*Gson gson = new Gson();
    	String 	json = pre.getString("pre_Dept", "");
    	Dept getDept = (Dept)gson.fromJson(json, Dept.class);*/
    	_listDept.clear();
    	/*JSONArray jsonArr=new JSONArray(settings.getString("pre_Dept", "[]"));
    	for(int i=0; i<jsonArr.length();i++){
    		Dept dept =(Dept)jsonArr.get(i);

    		if (dept!=null){
    			_listDept.add(dept);
    		}
    	}*/
    	String deptCode = pre.getString("pre_Dept", "") ;
    	_listDept  = mGetListDataHelper.getListDept(" AND " + DatabaseAdapter.KEY_CODE + " IN (" + deptCode + ")");
    	if(_listDept.size()>0){
    		tbtSearchItemDept.setChecked(true);
    	}else{
    		tbtSearchItemDept.setChecked(false);
    	}

    }
    
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * lưu  thông tin phòng ban
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲
     * @throws JSONException */
    public void storeTeamInSharedPreferences(SharedPreferences pre) throws JSONException{
    	Editor edit = pre.edit();
    	String teamCode = "";
    	int i=0;
    	
    	/*JSONArray jsonArr = new JSONArray();
    	
    	for (Dept item : _listDept){
    		jsonArr.put(i, item);
    		i++;
    	}
    	
    	edit.putString("pre_Dept", jsonArr.toString());*/
    	for (Team item : _listTeam){
    		if(i==0){
    			teamCode=String.valueOf(item.code);
    		}else{
    			teamCode=teamCode + "," + String.valueOf(item.code);
    		}
    		i++;
    	}
    	
    	edit.putString("pre_Team", teamCode.toString());
    	edit.commit();
    }

    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * get thông tin phòng ban
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲
     * @throws JSONException */
    public void  getTeamInSharedPreferences(SharedPreferences pre) throws JSONException{
    	/*Gson gson = new Gson();
    	String 	json = pre.getString("pre_Dept", "");
    	Dept getDept = (Dept)gson.fromJson(json, Dept.class);*/
    	_listTeam.clear();
    	/*JSONArray jsonArr=new JSONArray(settings.getString("pre_Dept", "[]"));
    	for(int i=0; i<jsonArr.length();i++){
    		Dept dept =(Dept)jsonArr.get(i);

    		if (dept!=null){
    			_listDept.add(dept);
    		}
    	}*/
    	String teamCode = pre.getString("pre_Team", "") ;
    	_listTeam  = mGetListDataHelper.getListTeam(" AND " + DatabaseAdapter.KEY_CODE + " IN (" + teamCode + ")");
    	if(_listTeam.size()>0){
    		tbtSearchItemTeam.setChecked(true);
    	}else{
    		tbtSearchItemTeam.setChecked(false);
    	}

    }
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * lưu  thông tin chức vụ
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲
     * @throws JSONException */
    public void storePositionInSharedPreferences(SharedPreferences pre) throws JSONException{
    	Editor edit = pre.edit();
    	String positionCode = "";
    	int i=0;
    	
    	/*JSONArray jsonArr = new JSONArray();
    	
    	for (Dept item : _listDept){
    		jsonArr.put(i, item);
    		i++;
    	}
    	
    	edit.putString("pre_Dept", jsonArr.toString());*/
    	for (Position item : _listPosition){
    		if(i==0){
    			positionCode=String.valueOf(item.code);
    		}else{
    			positionCode=positionCode + "," + String.valueOf(item.code);
    		}
    		i++;
    	}
    	
    	edit.putString("pre_Position", positionCode.toString());
    	edit.commit();
    }

    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * get thông tin chức vụ
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲
     * @throws JSONException */
    public void  getPositionInSharedPreferences(SharedPreferences pre) throws JSONException{
    	/*Gson gson = new Gson();
    	String 	json = pre.getString("pre_Dept", "");
    	Dept getDept = (Dept)gson.fromJson(json, Dept.class);*/
    	_listPosition.clear();
    	/*JSONArray jsonArr=new JSONArray(settings.getString("pre_Dept", "[]"));
    	for(int i=0; i<jsonArr.length();i++){
    		Dept dept =(Dept)jsonArr.get(i);

    		if (dept!=null){
    			_listDept.add(dept);
    		}
    	}*/
    	String positionCode = pre.getString("pre_Position", "") ;
    	_listPosition  = mGetListDataHelper.getListPosition(" AND " + DatabaseAdapter.KEY_CODE + " IN (" + positionCode + ")");
    	if(_listPosition.size()>0){
    		tbtSearchItemPosition.setChecked(true);
    	}else{
    		tbtSearchItemPosition.setChecked(false);
    	}

    }
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * luu thông tin nhóm labour 
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/   
    public void storeCurrentLabourInSharedPreferences(SharedPreferences pre){
    	Editor edit = pre.edit();
    	edit.putBoolean("pre_isCurrentLabour", chkIsLabour.isChecked());
    	edit.commit();
    }
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * get thông tin nhóm labour 
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    public void getCurrentLabourInSharedPreferences(SharedPreferences pre){
    	boolean isCurrentLabour = pre.getBoolean("pre_isCurrentLabour", false);
    	chkIsLabour.setChecked(isCurrentLabour);
    }
    
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * luu thông tin nhan vien da xoa
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/   
    public void storeUserOutDateInSharedPreferences(SharedPreferences pre){
    	Editor edit = pre.edit();
    	edit.putBoolean("pre_UserDelete", chkUserDelete.isChecked());
    	edit.commit();
    }
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * get thông tin user da xoa
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    public void getUserOutDateInSharedPreferences(SharedPreferences pre){
    	boolean isCurrentLabour = pre.getBoolean("pre_UserDelete", false);
    	chkUserDelete.setChecked(isCurrentLabour);
    }
    
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * luu thông tin nhan vien thu viec
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/   
    public void storeUserIncludeTrialStaffInSharedPreferences(SharedPreferences pre){
    	Editor edit = pre.edit();
    	edit.putBoolean("pre_UserIncludeTrialStaff", chkUserIncludeTrialStaff.isChecked());
    	edit.commit();
    }
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * get thông tin nhan vien thu viec
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    public void getUserIncludeTrialStaffInSharedPreferences(SharedPreferences pre){
    	boolean isCurrentLabour = pre.getBoolean("pre_UserIncludeTrialStaff", false);
    	chkUserIncludeTrialStaff.setChecked(isCurrentLabour);
    }
    
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * luu thông tin nhan vien da nghi viec 
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/   
    public void storeUserDeleteInSharedPreferences(SharedPreferences pre){
    	Editor edit = pre.edit();
    	//edit.putBoolean("pre_UserOutDate", chkUserOutDate.isChecked());
    	edit.putString("pre_UserOutDate", String.valueOf( getYasumiKbnRadio()));
    	edit.commit();
    }
    
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * get thông tin  nhan vien da nghi viec 
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    public void getUserDeleteInSharedPreferences(SharedPreferences pre){
    	//boolean isCurrentLabour = pre.getBoolean("pre_UserOutDate", false);
    	//chkUserOutDate.setChecked(isCurrentLabour);
    	String Yasumi_Kbn = pre.getString("pre_UserOutDate", "9");
    	setYasumiKbnRadio(Yasumi_Kbn);
    }
    
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
	 *
	 * get thông tin về nghỉ việc
	 *
	 ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
	public int  getYasumiKbnRadio(){
		//return Boolean.toString( txtUserBusinessKbn.isChecked()).equals("true")?1:0;
		int radCheckedId = segUserYasumiKbn.getCheckedRadioButtonId();
		switch ((radCheckedId))
		{
			case R.id.radOnlyYamsumi:
				return 0;
			case R.id.radWorking:
				return 1;
			case R.id.radAllUser:
				return 9;
			default:
				//nothing
		}
		return 9;
	}
	/**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
	 *
	 * set thông tin về nghỉ việc
	 *
	 ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
	public void  setYasumiKbnRadio(String value){
		if (value.equals("0")) {
			segUserYasumiKbn.check(R.id.radOnlyYamsumi);
		}
		else if (value.equals("1")){
			segUserYasumiKbn.check(R.id.radWorking);
		}else{
			segUserYasumiKbn.check(R.id.radAllUser);
		}
	}
	
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * luu thông tin check cua phong ban 
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/   
    public void storeUserChkDeptInSharedPreferences(SharedPreferences pre){
    	Editor edit = pre.edit();
    	edit.putBoolean("pre_chkDept", chkSearchItemDept.isChecked());
    	edit.commit();
    }
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * get thông tin check cua phong ban 
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    public void getUserChkDeptInSharedPreferences(SharedPreferences pre){
    	boolean isChkDept = pre.getBoolean("pre_chkDept", false);
    	chkSearchItemDept.setChecked(isChkDept);
    }
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * luu thông tin check cua nhom 
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/   
    public void storeUserChkTeamInSharedPreferences(SharedPreferences pre){
    	Editor edit = pre.edit();
    	edit.putBoolean("pre_chkTeam", chkSearchItemTeam.isChecked());
    	edit.commit();
    }
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * get thông tin check cua nhom
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    public void getUserChkTeamInSharedPreferences(SharedPreferences pre){
    	boolean isChkTeam = pre.getBoolean("pre_chkTeam", false);
    	chkSearchItemTeam.setChecked(isChkTeam);
    }
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * luu thông tin check cua chuc vu 
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/   
    public void storeUserChkPositionInSharedPreferences(SharedPreferences pre){
    	Editor edit = pre.edit();
    	edit.putBoolean("pre_chkPosition", chkSearchItemPosition.isChecked());
    	edit.commit();
    }
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * get thông tin check cua chuc vu
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    public void getUserChkPositionInSharedPreferences(SharedPreferences pre){
    	boolean isChkPosition = pre.getBoolean("pre_chkPosition", false);
    	chkSearchItemPosition.setChecked(isChkPosition);
    }
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * setting su dung -khong su dung tuy theo item co duoc check hay khong
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		// TODO Auto-generated method stub
		switch (buttonView.getId()){
		case R.id.chkSearchItemDept:
			if(isChecked){
				tbtSearchItemDept.setEnabled(true);
			}else{
				tbtSearchItemDept.setEnabled(false);
			}
			break;
		case R.id.chkSearchItemTeam:
			if(isChecked){
				tbtSearchItemTeam.setEnabled(true);
			}else{
				tbtSearchItemTeam.setEnabled(false);
			}
			break;
		case R.id.chkSearchItemPosition:
			if(isChecked){
				tbtSearchItemPosition.setEnabled(true);
			}else{
				tbtSearchItemPosition.setEnabled(false);
			}
			break;
		}
		
	}

}
