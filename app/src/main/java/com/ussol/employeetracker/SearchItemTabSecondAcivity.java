package com.ussol.employeetracker;

import com.ussol.employeetracker.helpers.AlertDialogRadio;
import com.ussol.employeetracker.helpers.AlertDialogRadio.AlertPositiveListener;
import com.ussol.employeetracker.helpers.ConvertCursorToArrayString;
import com.ussol.employeetracker.helpers.DatabaseAdapter;
import com.ussol.employeetracker.helpers.SegmentedGroup;
import com.ussol.employeetracker.models.MasterConstants;
import com.ussol.employeetracker.models.User;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.Toast;

public class SearchItemTabSecondAcivity extends Activity implements OnClickListener ,AlertPositiveListener, OnCheckedChangeListener,RadioGroup.OnCheckedChangeListener {
	private int mYear;
    private int mMonth;
    private int mDay;
    static final int DATE_DIALOG_ID = 0;
    private EditText  txtUserJananese , txtUserAllowance_Business ;
    //private Switch txtUserBusinessKbn ;
    private Button btnSearchItemSave , btnSearchItemCancel, btnUserJapaneseLevel , btnUserAllowance_Business;
    private CheckBox  chkUserJananese , chkUserAllowance_Business ;//,chkBusiness_Kbn;
	private SegmentedGroup segBusiness_Kbn;
    /** Lưu lại vị trí mà đã chọn từ list radio */
	int positionJananeseLevel = 0;
	int positionAllowance_Business = 0;
	
	/** chuyển đổi Cursor thành string array */
	ConvertCursorToArrayString mConvertCursorToArrayString;
	/** thông tin bằng cấp tiếng Nhật  */
	String[] dataJapaneseLevel = MasterConstants.JAPANESE_LEVEL ;
	/** thông tin phụ cấp nghiệp vụ : các bậc*/
	String[] dataAllowance_Business=MasterConstants.ALLOWANCE_BUSINESS_LEVEL ;

	/** Button nào được click */
	private static  int btnClicked ;
	/** kết nối Database */
	DatabaseAdapter mDatabaseAdapter;
	/** Trị code nhân viên  */
	private int nCode = -1;
	public static User userInfo ;
	SharedPreferences settings;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_search_item_tabsecond);
		settings = getSharedPreferences(MasterConstants.PRE_SEARCH_ITEM_FILE, Context.MODE_PRIVATE);
		mDatabaseAdapter = new DatabaseAdapter(getBaseContext());
		/** get các control date và gán sự kiện khi click vào textbox */
		getControl();
		settingListener();
		settingInputType();
		readPreferencesAndDisplay(settings);
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
			FragmentManager manager =getFragmentManager();
			
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
			FragmentManager managerTeam =getFragmentManager();
			
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
		case R.id.btnSearchItemSetting:
			saveToPreferences(settings);
			SearchItemTabFirstAcivity activity1 =(SearchItemTabFirstAcivity) ((TabActivity)getParent()).getLocalActivityManager().getActivity(MasterConstants.TAB_SEARCH_ITEM_1_TAG);
			if (activity1!=null){
		    	activity1.saveToPreferences(settings);
		    }
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
			case R.id.radDev:
				//Toast.makeText(this, "One", Toast.LENGTH_SHORT).show();
				return;
			case R.id.radPD:
				//Toast.makeText(this, "Two", Toast.LENGTH_SHORT).show();
				return;
			case R.id.radOther:
				return;
			case R.id.radAll:
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
    	}
    }
    
	/**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * get các control trên màn hình
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    private void getControl(){
    	txtUserJananese = (EditText)findViewById(R.id.txtUserJapanese);
    	txtUserAllowance_Business= (EditText)findViewById(R.id.txtUserAllowance_business);
    	//txtUserBusinessKbn = (Switch)findViewById(R.id.txtUserBusinessKbn);
    	
    	chkUserJananese = (CheckBox)findViewById(R.id.chkUserJapanese);
    	chkUserAllowance_Business= (CheckBox)findViewById(R.id.chkUserAllowance_business);
    	//chkBusiness_Kbn= (CheckBox)findViewById(R.id.chkUserBusinessKbn);
    	btnUserJapaneseLevel=(Button)findViewById(R.id.btnUserJapanese);
    	btnUserAllowance_Business=(Button)findViewById(R.id.btnUserAllowance_business);
		segBusiness_Kbn = (SegmentedGroup) findViewById(R.id.segUserBusinessKbn);

    	btnSearchItemSave = (Button)findViewById(R.id.btnSearchItemSetting);
    	btnSearchItemCancel= (Button)findViewById(R.id.btnSearchItemSettingCancel);
    }

    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * gán sự kiện cho các control
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    private void settingListener(){
		
		btnUserJapaneseLevel.setOnClickListener(this);
		btnUserAllowance_Business.setOnClickListener(this);
				
		btnSearchItemSave.setOnClickListener(this);
		btnSearchItemCancel.setOnClickListener(this);
		
		chkUserJananese.setOnCheckedChangeListener(this);
		chkUserAllowance_Business.setOnCheckedChangeListener(this);
		//chkBusiness_Kbn.setOnCheckedChangeListener(this);
		segBusiness_Kbn.setOnCheckedChangeListener(this);
    }
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * setting input cho control(không hiển thị bàn phím khi nhận focus)
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    private void settingInputType(){
    	txtUserJananese.setInputType(InputType.TYPE_NULL);
    	txtUserAllowance_Business.setInputType(InputType.TYPE_NULL);
    }
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * setValueTabOther
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    public void setValueTabOther(User item){
		/**thông tin của tab Other*/
		setJapaneseLevel(item.japanese);
		setAllowance_Business(item.allowance_business);
		setBusinessKbnRadio(String.valueOf(item.business_kbn));
		
    }
    
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * Lưu thông tin trên màn hình xuống file
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    public void saveToPreferences(SharedPreferences pre){
    	Editor edit = pre.edit();
    	edit.putString("pre_Japanese", String.valueOf( getJapaneseLevel()));
    	edit.putString("pre_Allowance_Business", String.valueOf( getAllowance_Business()));
    	//edit.putString("pre_Business_Kbn", String.valueOf( getBusinessKbn()));
		edit.putString("pre_Business_Kbn", String.valueOf( getBusinessKbnRadio()));
    	edit.putString("pre_enabledJapanese", String.valueOf( getCheckJapaneseLevel()));
    	edit.putString("pre_enabledAllowance_Business", String.valueOf(getCheckAllowance_Business()));
    	//edit.putBoolean("pre_chkBusiness_Kbn", chkBusiness_Kbn.isChecked());
    	edit.commit();
    }
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * Đọc thông tin từ file và hiển thị lên màn hình
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    public void readPreferencesAndDisplay(SharedPreferences pre){
    	String Japanese = pre.getString("pre_Japanese", "");
    	setJapaneseLevel(Japanese);
    	String Allowance_Business = pre.getString("pre_Allowance_Business", "");
    	setAllowance_Business(Allowance_Business);
    	String Business_Kbn = pre.getString("pre_Business_Kbn", "");
    	//setBusinessKbn(Business_Kbn);
		setBusinessKbnRadio(Business_Kbn);
    	String enabledJapanese = pre.getString("pre_enabledJapanese", "0");
    	setCheckJapaneseLevel(enabledJapanese);
    	String enabledAllowance_Business = pre.getString("pre_enabledAllowance_Business", "0");
    	setCheckAllowance_Business(enabledAllowance_Business);
    	getUserChkBusiness_KbnInSharedPreferences(pre);
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
     * 
     * get thông tin về nghề nghiệp chính
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    /*public int  getBusinessKbn(){
    	return Boolean.toString( txtUserBusinessKbn.isChecked()).equals("true")?1:0;
    }*/
    
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * 
     * set thông tin về nghề nghiệp chính
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    /*public void  setBusinessKbn(String value){
    	if (value.equals("0")){
    		txtUserBusinessKbn.setChecked(false);
    	}else{
    		txtUserBusinessKbn.setChecked(true);
    	}
    }*/

	/**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
	 *
	 * get thông tin về nghề nghiệp chính
	 *
	 ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
	public int  getBusinessKbnRadio(){
		//return Boolean.toString( txtUserBusinessKbn.isChecked()).equals("true")?1:0;
		int radCheckedId = segBusiness_Kbn.getCheckedRadioButtonId();
		switch ((radCheckedId))
		{
			case R.id.radDev:
				return 1; // 0-> 1 cho trung voi tri luu xuong -- LTV
			case R.id.radPD:
				return 2; // 1-> 2 Phien dich
			case R.id.radOther:
				return 3;//cong viec khac chang han nhu tong vu...
			case R.id.radAll:
				return 9;
			default:
				//nothing
		}
		return 9;
	}
	/**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
	 *
	 * set thông tin về nghề nghiệp chính
	 *
	 ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
	public void  setBusinessKbnRadio(String value){
		if (value.equals("1")) {
			segBusiness_Kbn.check(R.id.radDev);
		}
		else if (value.equals("2")){
			segBusiness_Kbn.check(R.id.radPD);
		}else if (value.equals("3")){
			segBusiness_Kbn.check(R.id.radOther);
		}else
		{//=9 -1
			segBusiness_Kbn.check(R.id.radAll);
		}
	}

    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * 
     * get thông tin về trạng thái của tiếng Nhật
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    public int  getCheckJapaneseLevel(){
    	return Boolean.toString( chkUserJananese.isChecked()).equals("true")?1:0;
    }
    
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * 
     * set thông tin về trạng thái của tiếng Nhật
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    public void  setCheckJapaneseLevel(String value){
    	if (value.equals("0")){
    		chkUserJananese.setChecked(false);
    	}else{
    		chkUserJananese.setChecked(true);
    	}
    }
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * 
     * get thông tin về trạng thái của trợ cấp nghiệp vụ
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    public int  getCheckAllowance_Business(){
    	return Boolean.toString( chkUserAllowance_Business.isChecked()).equals("true")?1:0;
    }
    
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * 
     * set thông tin về trạng thái của trợ cấp nghiệp vụ
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    public void  setCheckAllowance_Business(String value){
    	if (value.equals("0")){
    		chkUserAllowance_Business.setChecked(false);
    	}else{
    		chkUserAllowance_Business.setChecked(true);
    	}
    }
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * luu thông tin check cua phong ban 
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/   
    public void storeUserChkBusiness_KbnInSharedPreferences(SharedPreferences pre){
    	Editor edit = pre.edit();
    	//edit.putBoolean("pre_chkBusiness_Kbn", chkBusiness_Kbn.isChecked());
    	edit.commit();
    }
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * get thông tin check cua nghề nghiệp
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    public void getUserChkBusiness_KbnInSharedPreferences(SharedPreferences pre){
    	boolean isChkDept = pre.getBoolean("pre_chkBusiness_Kbn", false);
    	//chkBusiness_Kbn.setChecked(isChkDept);

    }
    
	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		
		switch (buttonView.getId()){
		case R.id.chkUserJapanese:
			if(isChecked){
				txtUserJananese.setEnabled(true);
				btnUserJapaneseLevel.setEnabled(true);
			}else{
				txtUserJananese.setEnabled(false);
				btnUserJapaneseLevel.setEnabled(false);
			}
			break;
		case R.id.chkUserAllowance_business:
			if(isChecked){
				txtUserAllowance_Business.setEnabled(true);
				btnUserAllowance_Business.setEnabled(true);
			}else{
				txtUserAllowance_Business.setEnabled(false);
				btnUserAllowance_Business.setEnabled(false);
			}
			break;
		/*case R.id.chkUserBusinessKbn:
			if(isChecked){
				txtUserBusinessKbn.setEnabled(true);
			}else{
				txtUserBusinessKbn.setEnabled(false);
			}
			break;
			*/
		}
		
	}
    
}
