/**
 * Copyright (c) 2012 Fujinet Software Department 5 ( USSOL team)  
 * See the file license.txt for copying permission.
*/  
package com.ussol.employeetracker;

import com.ussol.employeetracker.helpers.ConvertCursorToListString;
import com.ussol.employeetracker.helpers.DatabaseAdapter;
import com.ussol.employeetracker.models.MasterConstants;
import com.ussol.employeetracker.models.Position;
import com.ussol.employeetracker.models.SharedVariables;
import com.ussol.employeetracker.models.Team;
import com.ussol.employeetracker.helpers.AlertDialogRadio;
import com.ussol.employeetracker.helpers.AlertDialogRadio.AlertPositiveListener;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class EditPositionActivity extends Activity  implements OnClickListener ,AlertPositiveListener  {
	/** tag */
	private final static String TAG =EditPositionActivity.class.getName();
	/** chuyển đổi Cursor thành List */
	ConvertCursorToListString mConvertCursorToListString;
	/** Lưu trữ control code nhóm*/
	TextView txtCode ;
	/** Lưu trữ control tên ...nhóm */
	EditText txtName, txtPositionFJNCode, txtMinMonth , txtNote , txtPositionAllowance,txtLevel;
	/** Trị code nhóm  */
	private int nCode = 0;
	/** Lưu các button trên màn hình  */
	Button btnSave, btnCancel , btnPositionFJNCodeSearch, btnMinMonthSearch  ;
	/** Lưu lại vị trí mà đã chọn từ list radio */
	int positionFJNCode = 0;
	int positionMinMonth = 0;
	/** thông tin hiển thị tại màn hình dialog  */
	String[] dataFJNCode = new String[]{"M2","M1","M0","L2","TL2","L1","TL1","SL2","TS2","SL1","TS1"
										,"SS4","SS3","SS2","SS1","JS2","JS1","Trail Staff"};
	String[] dataMinMonth = new String[]{"6","12","18","24","30","36"
										,"42","48","54","60","66","72"
										,"78","84","90","96","102","108"};
	/** Button nào được click */
	private int btnClicked ;
	/** kết nối Database */
	DatabaseAdapter mDatabaseAdapter;
	
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * onCreate
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		try{
			super.onCreate(savedInstanceState);
    		mDatabaseAdapter = new DatabaseAdapter(getApplicationContext());
   		
			/** lưu màn hình ngang hay dọc */
			SharedVariables.CURRENT_ORIENTATION = getResources().getConfiguration().orientation ;
			/** load layout */
			setContentView(R.layout.activity_position_edit);
			/** khoi tao cac event va trị init */ 
			InitializationControl();
			/** Hiển thị data từ code nhóm mà đã truyền qua */
			/** get code nhóm từ intent*/ 
			Intent request = getIntent();
			Bundle param = request.getExtras();
			nCode =param.getInt(DatabaseAdapter.KEY_CODE);
			Position positionCopy = (Position)param.getParcelable(MasterConstants.TAB_POSITION_TAG);

			if (param != null) {
				if (nCode==-1){
					/** trường hợp tạo mới*/
					initDetail();
				}else if (nCode==0){
					/** hiển thị copy data*/
					displayDetail(positionCopy);
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
     * onPause
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    @Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(txtName.getWindowToken(), 0);
		imm.hideSoftInputFromWindow(txtPositionFJNCode.getWindowToken(), 0);
		imm.hideSoftInputFromWindow(txtMinMonth.getWindowToken(), 0);
		imm.hideSoftInputFromWindow(txtLevel.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(txtNote.getWindowToken(), 0);
	}
    
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * get các control trên màn hình
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
	private void InitializationControl() {
		/**thong tin cac item tren man hinh */
		txtCode = (TextView) findViewById(R.id.txtPositionCode);
		txtName= (EditText) findViewById(R.id.txtPositionName);
		txtPositionFJNCode= (EditText) findViewById(R.id.txtPositionFJNCode);
		txtMinMonth= (EditText) findViewById(R.id.txtPositionMinMonth);
		txtLevel= (EditText) findViewById(R.id.txtPositionLevel);
		txtPositionAllowance= (EditText) findViewById(R.id.txtPositionAllowance);
		txtNote= (EditText) findViewById(R.id.txtPositionNote);
		/** button*/
		btnSave = (Button) findViewById(R.id.btnPositionSave);
		btnCancel = (Button) findViewById(R.id.btnPositionCancel);
		btnPositionFJNCodeSearch = (Button) findViewById(R.id.btnPositionFJNCode);
		btnMinMonthSearch= (Button) findViewById(R.id.btnPositionMinMonth);
		/** setting event click cho cac button */
		btnSave.setOnClickListener(this);
		btnCancel.setOnClickListener(this);
		btnPositionFJNCodeSearch.setOnClickListener(this);
		btnMinMonthSearch.setOnClickListener(this);
		
		
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
			txtPositionFJNCode.setText("");
			txtMinMonth.setText("");
			txtLevel.setText("");
			txtPositionAllowance.setText("0");
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
	private void displayDetail(Position item ){
		try{
			/** gán trị cho các item trên màn hình */
			mDatabaseAdapter.open();
			int code = mDatabaseAdapter.getMaxCodeMaster();
    		mDatabaseAdapter.close();
			txtCode.setText(String.valueOf(code));
			txtName.setText(item.name.toString());
			txtPositionFJNCode.setText(item.yobi_text1.toString());
			txtMinMonth.setText(item.yobi_text2.toString());
			txtLevel.setText(String.valueOf(item.yobi_code1)); // muc cua cap bac (cang nho thi chuc cang cao)
			txtPositionAllowance.setText(String.valueOf(item.yobi_real1));
			txtNote.setText(item.note.toString());
		}catch(Exception e){
			Log.v(TAG,e.getMessage());
		}
		
	}
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * hiển thị data lên màn hình
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
	private void displayDetail(int code ){
		try{
			/** khởi tạo đối tượng chức vụ*/
			Position item =null;
			/** chuyển đổi từ Cursor thành list */
			mConvertCursorToListString = new ConvertCursorToListString(getApplicationContext());
			item= mConvertCursorToListString.getPositionByCode(code);
			/** gán trị cho các item trên màn hình */
			txtCode.setText(String.valueOf(item.code));
			txtName.setText(item.name.toString());
			txtPositionFJNCode.setText(item.yobi_text1.toString());
			txtMinMonth.setText(item.yobi_text2.toString());
			txtLevel.setText(String.valueOf(item.yobi_code1)); // muc cua cap bac (cang nho thi chuc cang cao)
			txtPositionAllowance.setText(String.valueOf(item.yobi_real1));
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
			case R.id.btnPositionSave:
				/** lưu thông tin thay đổi vào DB*/
				savePositionToDb();
				/** trả về kết quả*/
				Intent intent = new Intent();
				Bundle bundle = new Bundle();
				bundle.putInt("btn", R.id.btnPositionSave);
				intent.putExtras(bundle);
				setResult(RESULT_OK, intent);
				/** Đóng màn hình và trở về màn hình trước đó*/
				this.finish();
				break;
			case R.id.btnPositionCancel:
				/** Đóng màn hình và trở về màn hình trước đó*/
				this.finish();
				break;
				
			case R.id.btnPositionFJNCode:
				/**hiển thị màn hình để chọn phong ban */
				/** Getting the fragment manager */
				FragmentManager manager = getFragmentManager();
				
				/** Instantiating the DialogFragment class */
				AlertDialogRadio alert = new AlertDialogRadio("",dataFJNCode,R.id.btnPositionFJNCode);
				
				/** Creating a bundle object to store the selected item's index */
				Bundle b  = new Bundle();
				
				/** Storing the selected item's index in the bundle object */
				b.putInt("position", positionFJNCode);
				
				/** Setting the bundle object to the dialog fragment object */
				alert.setArguments(b);
				
				/** Creating the dialog fragment object, which will in turn open the alert dialog window */
				alert.show(manager, MasterConstants.TAB_DIALOG_TAG);	
				break;
				
			case R.id.btnPositionMinMonth:
				/**hiển thị màn hình để chọn người quản lý */
				/** Getting the fragment manager */
				FragmentManager leader = getFragmentManager();
				
				/** Instantiating the DialogFragment class */
				AlertDialogRadio alertLeader = new AlertDialogRadio("",dataMinMonth,R.id.btnPositionMinMonth);
				
				/** Creating a bundle object to store the selected item's index */
				Bundle bun  = new Bundle();
				
				/** Storing the selected item's index in the bundle object */
				bun.putInt("position", positionMinMonth);
				
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
    public void onPositiveClick(int position , int clickBtn, Bundle bundle) {
    	/** Setting the selected android version in the textview */
    	switch (btnClicked) {
    		/** Phân nhánh xử lý tùy theo button mà đã được clicked*/
	    	case R.id.btnPositionFJNCode:
	    		/** gán lại vị trí của item mà đã chọn */
	    		this.positionFJNCode = position;
	    		/** gán tên người đã chọn tại màn hình poup */
	    		txtPositionFJNCode.setText(dataFJNCode[this.positionFJNCode]);    
	    		break;
	    	case R.id.btnPositionMinMonth:
	    		/** gán lại vị trí của item mà đã chọn */
	    		this.positionMinMonth = position;
	    		/** gán tên người đã chọn tại màn hình poup */
	    		txtMinMonth.setText(dataMinMonth[this.positionMinMonth]);    
	    		break;
    	}
    }
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * 
     * lưu thông tin vào DB
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    public void savePositionToDb(){
    	try{

			Position position = new Position();
			mDatabaseAdapter.open();
			/** mã phòng ban sẽ update */
			if (nCode ==-1 ){
				/** tạo mới */
				
			}else{
				/** chỉnh sửa */
				position.code =nCode;
				/** cố định là 1 */
			}
			position.mkbn=MasterConstants.MASTER_MKBN_POSITION;
			/** tên nhóm  */
			position.name = txtName.getText().toString() ;
			/** tên tắt */
			position.ryaku=txtName.getText().toString();
			/** phòng ban  */
			position.yobi_text1=txtPositionFJNCode.getText().toString();
			/**yêu cầu tối thiếu của chức vụ ( số tháng) */
			position.yobi_text2=txtMinMonth.getText().toString();
			/**mức phụ cấp của từng bậc ngạch */
			if(txtPositionAllowance.getText().toString().equals(""))
			{
				position.yobi_real1= Float.parseFloat("0");	
			}else{
				position.yobi_real1= Float.parseFloat(txtPositionAllowance.getText().toString());
			}
			//level cang nho thi chuc vu cang cao
			if(txtLevel.getText().toString().equals(""))
			{
				position.yobi_code1= Integer.parseInt("0");
			}else{
				position.yobi_code1= Integer.parseInt(txtLevel.getText().toString());
			}
			/**ghi chú */
			position.note =txtNote.getText().toString();
			if (nCode ==-1 ){
				/** tạo mới */
				mDatabaseAdapter.insertToMasterTable(position);
			}else{
				/** insert vào DB */			
	    		mDatabaseAdapter.editMasterTable(position);
			}
    		/** đóng connection */
    		mDatabaseAdapter.close();
    		
    	}catch ( Exception e){
    		Log.v(TAG,e.getMessage());
    		/** đóng connection */
    		mDatabaseAdapter.close();
    	}
    }
    
}