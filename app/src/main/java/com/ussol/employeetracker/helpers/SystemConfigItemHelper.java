package com.ussol.employeetracker.helpers;

import java.util.Calendar;

import com.ussol.employeetracker.R;
import com.ussol.employeetracker.models.MasterConstants;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.ShareActionProvider;

public  class SystemConfigItemHelper {
	public SharedPreferences settings ;
	private Context ctx;
	
	public  SystemConfigItemHelper(Context context){
		ctx = context;
		settings=readSharedPreferencesSystemConfigItem();
	}
	public  SharedPreferences readSharedPreferencesSystemConfigItem(){
		/** đọc thông tin lưu trữ tại xml */
		
		//settings = ctx.getSharedPreferences(MasterConstants.PRE_SYSTEM_CONFIG_FILE, Context.MODE_PRIVATE);
		settings= PreferenceManager.getDefaultSharedPreferences(ctx);
		return settings;
	}
	
	/** thong tin trình tự sort */
	/*public Boolean getEnableSortAllItem(){
		settings=readSharedPreferencesSystemConfigItem();
		return settings.getBoolean("config_enableSortFirstItem", false);
	}*/
	public String getTheFirstItemSort(){
		settings=readSharedPreferencesSystemConfigItem();
		return settings.getString("config_firstItemSort", "0");//neu khong co thi cho la toan bo
	}
	//config_displayStaffSalaryInList
	public Boolean getcDisplayStaffSalaryInList(){
		settings=readSharedPreferencesSystemConfigItem();
		return settings.getBoolean("config_displayStaffSalaryInList", false);
	}
	/** thong tin trình tự sort */
	public Boolean getNotDisplaySentMessage(){
		settings=readSharedPreferencesSystemConfigItem();
		return settings.getBoolean("config_notDisplaySentMessage", false);
	}
	/** get thong tin gui sms chuc mung sinh nhat tu dong */
	public Boolean getEnableSendSmsBirthdayConfiguration(){
		settings = readSharedPreferencesSystemConfigItem();
		return settings.getBoolean("config_enableSendSmsBirthdayConfiguration", false);
	}
	/** get thong tin gui sms chuc mung sinh nhat tu dong */
	public Boolean getEnableSendSmsYasumiConfiguration(){
		settings = readSharedPreferencesSystemConfigItem();
		return settings.getBoolean("config_enableSendSmsYasumiConfiguration", false);
	}
	/** get thong tin gui sms chuc mung sinh nhat tu dong */
	public Boolean getEnableSendSmsTrainingConfiguration(){
		settings = readSharedPreferencesSystemConfigItem();
		return settings.getBoolean("config_enableSendSmsTrainingConfiguration", false);
	}
	/** Sim dung de gui SMS*/
	public String getSimSendSms(){
		settings=readSharedPreferencesSystemConfigItem();
		return settings.getString("config_SimSendSms", "0967808590");
	}

	/** get thong tin gui sms chuc mung sinh nhat tu dong */
	public Boolean getEmpNameSortEnabled(){
		settings = readSharedPreferencesSystemConfigItem();
		return settings.getBoolean("config_dialogEmpNameSortEnabled", false);
	}
	/** thong tin upload GoogleDrive*/
	public Boolean getEnableGoogleDriveBackup(){
		settings=readSharedPreferencesSystemConfigItem();
		return settings.getBoolean("config_enableGoogleDriveBackup", false);
	}
	/** Hiển thị thông tin mô tả trên biểu đồ */
	public Boolean getDisplayDescriptionOnChart(){
		settings=readSharedPreferencesSystemConfigItem();
		return settings.getBoolean("config_displayDescriptionOnChart", false);
	}
	
	/** Hiển thị thông tin mô tả trên biểu đồ */
	public Boolean getDisplayValueOnClick(){
		settings=readSharedPreferencesSystemConfigItem();
		return settings.getBoolean("config_displayValueOnClick", false);
	}
	/** Năm tài chính */
	public int getYearProcessing(){
		/** Tao array gom 12 thang cua nam hien tai */
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        
		settings=readSharedPreferencesSystemConfigItem();
		return Integer.parseInt(settings.getString("config_YearProcessing", String.valueOf(currentYear)));
	}
	/** Số tháng thâm niên dùng để thống kê */
	public int getKeikenMonthProcessing(){
        int keikenMonth =3;
		settings=readSharedPreferencesSystemConfigItem();
		return Integer.parseInt(settings.getString("config_KeikenMonthProcessing", String.valueOf(keikenMonth)));
	}
	
	/** phu cap tieng Nhat N1 */
	public float getJapaneseAllowanceN1(){
		settings=readSharedPreferencesSystemConfigItem();
		return Float.parseFloat(settings.getString("config_JapaneseN1", "0"));
	}
	/** phu cap tieng Nhat N2 */
	public float getJapaneseAllowanceN2(){
		settings=readSharedPreferencesSystemConfigItem();
		return Float.parseFloat(settings.getString("config_JapaneseN2", "0"));
	}
	/** phu cap tieng Nhat N3 */
	public float getJapaneseAllowanceN3(){
		settings=readSharedPreferencesSystemConfigItem();
		return Float.parseFloat(settings.getString("config_JapaneseN3", "0"));
	}
	/** phu cap tieng Nhat N4 */
	public float getJapaneseAllowanceN4(){
		settings=readSharedPreferencesSystemConfigItem();
		return Float.parseFloat(settings.getString("config_JapaneseN4", "0"));
	}
	/** phu cap tieng Nhat N5 */
	public float getJapaneseAllowanceN5(){
		settings=readSharedPreferencesSystemConfigItem();
		return Float.parseFloat(settings.getString("config_JapaneseN5", "0"));
	}
	/** phu cap nghiep vu bac 1 */
	public float getBusinessAllowanceLevel1(){
		settings=readSharedPreferencesSystemConfigItem();
		return Float.parseFloat(settings.getString("config_BusinessLevel1", "0"));
	}
	/** phu cap nghiep vu bac 2 */
	public float getBusinessAllowanceLevel2(){
		settings=readSharedPreferencesSystemConfigItem();
		return Float.parseFloat(settings.getString("config_BusinessLevel2", "0"));
	}
	/** phu cap nghiep vu bac 3 */
	public float getBusinessAllowanceLevel3(){
		settings=readSharedPreferencesSystemConfigItem();
		return Float.parseFloat(settings.getString("config_BusinessLevel3", "0"));
	}
	/** phu cap nghiep vu bac 4 */
	public float getBusinessAllowanceLevel4(){
		settings=readSharedPreferencesSystemConfigItem();
		return Float.parseFloat(settings.getString("config_BusinessLevel4", "0"));
	}
	/** phu cap nghiep vu bac 5 */
	public float getBusinessAllowanceLevel5(){
		settings=readSharedPreferencesSystemConfigItem();
		return Float.parseFloat(settings.getString("config_BusinessLevel5", "0"));
	}
	
	/** phu cap phòng làm việc 1 */
	public float getRoomAllowanceLevel1(){
		settings=readSharedPreferencesSystemConfigItem();
		return Float.parseFloat(settings.getString("config_RoomLevel1", "0"));
	}
	
	/** phu cap phòng làm việc 2 */
	public float getRoomAllowanceLevel2(){
		settings=readSharedPreferencesSystemConfigItem();
		return Float.parseFloat(settings.getString("config_RoomLevel2", "0"));
	}
	/** phu cap phòng làm việc 3 */
	public float getRoomAllowanceLevel3(){
		settings=readSharedPreferencesSystemConfigItem();
		return Float.parseFloat(settings.getString("config_RoomLevel3", "0"));
	}
	/** phu cap phòng làm việc 4 */
	public float getRoomAllowanceLevel4(){
		settings=readSharedPreferencesSystemConfigItem();
		return Float.parseFloat(settings.getString("config_RoomLevel4", "0"));
	}
	/** phu cap phòng làm việc 5 */
	public float getRoomAllowanceLevel5(){
		settings=readSharedPreferencesSystemConfigItem();
		return Float.parseFloat(settings.getString("config_RoomLevel5", "0"));
	}
	
	
	/** thong tin mat ma */
	public String getPassword(){
		settings=readSharedPreferencesSystemConfigItem();
		return settings.getString("config_PassLogin", "12345");
	}
	
	/** thong tin user cua gmail*/
	public String getEmailUser(){
		settings=readSharedPreferencesSystemConfigItem();
		return settings.getString("config_EmailUser", "xuanhoa97");
	}
	
	/** thong tin mat ma cua gmail*/
	public String getEmailPassWord(){
		settings=readSharedPreferencesSystemConfigItem();
		return settings.getString("config_EmailPass", "12345");
	}
	
	/** thong tin message id -sinh nhat*/
	public String getBirthdayMsgCode(){
		settings=readSharedPreferencesSystemConfigItem();
		return settings.getString("config_birthdayMsgCode", "1");
	}
	/** thong tin message id -nghi viec*/
	public String getYasumiMsgCode(){
		settings=readSharedPreferencesSystemConfigItem();
		return settings.getString("config_yasumiMsgCode", "2");
	}
	
	/** thong tin message id -training*/
	public String getTrainingMsgCode(){
		settings=readSharedPreferencesSystemConfigItem();
		return settings.getString("config_trainingMsgCode", "3");
	}
	/** thong tin message id -training*/
	public String getYasumiMsgReceiverPhone(){
		settings=readSharedPreferencesSystemConfigItem();
		return settings.getString("config_yasumiMsgReceiverPhone", "");
	}
	/** list so dien thoai nhan duoc in nha*/
	public String getTrainingMsgReceiverPhone(){
		settings=readSharedPreferencesSystemConfigItem();
		return settings.getString("config_trainingMsgReceiverPhone", "");
	}	
}
