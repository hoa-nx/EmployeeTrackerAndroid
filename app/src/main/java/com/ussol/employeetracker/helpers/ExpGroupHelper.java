package com.ussol.employeetracker.helpers;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.content.Context;
import android.util.Xml.Encoding;
import android.widget.Switch;

import com.ussol.employeetracker.EmployeeTrackerApplication;
import com.ussol.employeetracker.models.ChartItem;
import com.ussol.employeetracker.models.IExpGroup;
import com.ussol.employeetracker.models.MasterConstants;
import com.ussol.employeetracker.models.User;
import com.ussol.employeetracker.models.UserHistory;

public class ExpGroupHelper implements IExpGroup<User> {
	private String mTitle;
    private Context ctx ;
    private DatabaseAdapter mDatabaseAdapter; 
    /** chuyển đổi Cursor thành string array */
	ConvertCursorToArrayString mConvertCursorToArrayString;
	ConvertCursorToListString mConvertCursorToListString;
	private eTableUse  mTableUse =eTableUse.FROM_USER_TABLE;
    public ExpGroupHelper(Context context ){
    	ctx = context;
    	mDatabaseAdapter = new DatabaseAdapter(ctx);
    	mConvertCursorToArrayString = new ConvertCursorToArrayString(ctx);
    	mConvertCursorToListString = new ConvertCursorToListString(ctx);
    	mTableUse= eTableUse.FROM_USER_TABLE;
    }
    
    @Override
	public ArrayList<?> getGroupArrayList(int group) {
    	ArrayList<String> list=null;

    	try{
    		mDatabaseAdapter.open();
		switch (group){
			case IExpGroup.EXP_GROUP_BUSINESS_KBN:
				list =mConvertCursorToArrayString.getUserGroup(DatabaseAdapter.KEY_BUSINESS_KBN);
				
				break;
			case IExpGroup.EXP_GROUP_DEPT:
				list =mConvertCursorToArrayString.getUserGroup(DatabaseAdapter.KEY_DEPT,DatabaseAdapter.KEY_DEPT_NAME);
				break;
			case IExpGroup.EXP_GROUP_JAPANESE:
				list =mConvertCursorToArrayString.getUserGroup(DatabaseAdapter.KEY_JAPANESE);

				break;
			case IExpGroup.EXP_GROUP_KEIKEN:
				
				break;
			case IExpGroup.EXP_GROUP_POSITION:
				list =mConvertCursorToArrayString.getUserGroup(DatabaseAdapter.KEY_POSITION,DatabaseAdapter.KEY_POSITION_NAME);

				break;
			case IExpGroup.EXP_GROUP_SEX:
				list =mConvertCursorToArrayString.getUserGroup(DatabaseAdapter.KEY_SEX);

				break;
			case IExpGroup.EXP_GROUP_TEAM:
				list =mConvertCursorToArrayString.getUserGroup(DatabaseAdapter.KEY_TEAM,DatabaseAdapter.KEY_TEAM_NAME);
				break;
				
			case IExpGroup.EXP_GROUP_CUSTOMER:
				//mTableUse= eTableUse.FROM_VIEW;
				list =mConvertCursorToArrayString.getUserGroupFromView(DatabaseAdapter.KEY_CUSTOMER_GROUP_CODE,DatabaseAdapter.KEY_CUSTOMER_GROUP_NAME, DatabaseAdapter.VIEW_M_USER_CUSTOMER_GROUP );
				
				break;
				
			case IExpGroup.EXP_GROUP_YASUMI_YEARMONTH:
				//mTableUse= eTableUse.FROM_VIEW;s
				list =mConvertCursorToArrayString.getUserGroupFromView(DatabaseAdapter.KEY_USER_GROUP_YASUMI_YEARMONTH_SQL,DatabaseAdapter.KEY_USER_GROUP_YASUMI_YEARMONTH_SQL, DatabaseAdapter.VIEW_M_USER_CUSTOMER_GROUP );
				
				break;
			case IExpGroup.EXP_GROUP_STAFF_CONTRACT_STATUS_YEARMONTH:
				//mTableUse= eTableUse.FROM_VIEW;s
				list =mConvertCursorToArrayString.getUserGroupFromView(DatabaseAdapter.KEY_USER_GROUP_CONTRACT_YEARMONTH_SQL,DatabaseAdapter.KEY_USER_GROUP_CONTRACT_YEARMONTH_SQL, DatabaseAdapter.VIEW_M_USER_CUSTOMER_GROUP );
				
				break;
				
		}
		
		mDatabaseAdapter.close();
		
    	}catch (Exception e){
    		mDatabaseAdapter.close();
    	}
    	return list;
	}

	@Override
	public String[] getGroup(int group) {
    	ArrayList<String> list;
    	String[] returnArr=null ;
    	try{
    		mDatabaseAdapter.open();
		switch (group){
			case IExpGroup.EXP_GROUP_BUSINESS_KBN:
				list =mConvertCursorToArrayString.getUserGroup(DatabaseAdapter.KEY_BUSINESS_KBN);
				returnArr = new String[list.size()];
				returnArr = list.toArray(returnArr);
				break;

			case IExpGroup.EXP_GROUP_DEPT:
				list =mConvertCursorToArrayString.getUserGroup(DatabaseAdapter.KEY_DEPT,DatabaseAdapter.KEY_DEPT_NAME);
				returnArr = new String[list.size()];
				returnArr = list.toArray(returnArr);
				break;

			case IExpGroup.EXP_GROUP_JAPANESE:
				list =mConvertCursorToArrayString.getUserGroup(DatabaseAdapter.KEY_JAPANESE);
				returnArr = new String[list.size()];
				returnArr = list.toArray(returnArr);
				break;

			case IExpGroup.EXP_GROUP_BUSSINESS_ALLOWANCE:
				list =mConvertCursorToArrayString.getUserGroup(DatabaseAdapter.KEY_ALLOWANCE_BUSINESS);
				returnArr = new String[list.size()];
				returnArr = list.toArray(returnArr);
				break;

			case IExpGroup.EXP_GROUP_KEIKEN:
				/** 2013.09.16 ADD START */
				returnArr = new String[6];
				returnArr[0] ="0";	/** Nhỏ hơn 1 năm */
				returnArr[1] ="1";	/** 1 ～ 2 năm */
				returnArr[2] ="2";	/** 2 ～ 3 năm */
				returnArr[3] ="3";/** 3 ～ 4 năm */
				returnArr[4] ="4";/** 4 ～ 5 năm */
				returnArr[5] ="5";/** Lớn hơn 5 năm */
				/** 2013.09.16 ADD END */
				break;

			case IExpGroup.EXP_GROUP_KEIKEN_LABOR:
				/** 2013.09.16 ADD START */
				returnArr = new String[6];
				returnArr[0] ="0";	/** Nhỏ hơn 1 năm */
				returnArr[1] ="1";	/** 1 ～ 2 năm */
				returnArr[2] ="2";	/** 2 ～ 3 năm */
				returnArr[3] ="3";/** 3 ～ 4 năm */
				returnArr[4] ="4";/** 4 ～ 5 năm */
				returnArr[5] ="5";/** Lớn hơn 5 năm */
				/** 2013.09.16 ADD END */
				break;

			case IExpGroup.EXP_GROUP_POSITION:
				list =mConvertCursorToArrayString.getUserGroup(DatabaseAdapter.KEY_POSITION,DatabaseAdapter.KEY_POSITION_NAME);
				returnArr = new String[list.size()];
				returnArr = list.toArray(returnArr);
				break;
			case IExpGroup.EXP_GROUP_SEX:
				list =mConvertCursorToArrayString.getUserGroup(DatabaseAdapter.KEY_SEX);
				returnArr = new String[list.size()];
				returnArr = list.toArray(returnArr);
				break;
			case IExpGroup.EXP_GROUP_TEAM:
				list =mConvertCursorToArrayString.getUserGroup(DatabaseAdapter.KEY_TEAM,DatabaseAdapter.KEY_TEAM_NAME);
				returnArr = new String[list.size()];
				returnArr = list.toArray(returnArr);
				break;
				/** 2013.09.16 ADD START */
			case IExpGroup.EXP_GROUP_YASUMI:
				returnArr = new String[2];
				returnArr[0] = "";
				returnArr[1] = "1"; /** tri tai table trong trường hợp setting là nghỉ */
				break;
			case IExpGroup.EXP_GROUP_LABOUR_USER:
				list =mConvertCursorToArrayString.getUserGroup(DatabaseAdapter.KEY_ISLABOUR);
				returnArr = new String[list.size()];
				returnArr = list.toArray(returnArr);
				break;	
				/** 2013.09.16 ADD END */
				/** 2013.09.27 ADD START */
			case IExpGroup.EXP_GROUP_SALARY_BASIC:
				returnArr = new String[6];
				returnArr[0] ="0";	/** Nhỏ hơn 300 USD */
				returnArr[1] ="1";	/** 300 ～ 400 USD */
				returnArr[2] ="2";	/** 400 ～ 500 USD */
				returnArr[3] ="3";	/** 500 ～ 600 USD */
				returnArr[4] ="4";	/** 600 ～ 700 USD */
				returnArr[5] ="5";	/** Lớn hơn 700 USD */

				break;
				/** 2013.09.27 ADD END */
			case IExpGroup.EXP_GROUP_CUSTOMER:
				//mTableUse= eTableUse.FROM_VIEW;
				list =mConvertCursorToArrayString.getUserGroupFromView(DatabaseAdapter.KEY_CUSTOMER_GROUP_NAME,DatabaseAdapter.VIEW_M_USER_CUSTOMER_GROUP);
				returnArr = new String[list.size()];
				returnArr = list.toArray(returnArr);
				
				break;
				
			case IExpGroup.EXP_GROUP_YASUMI_YEARMONTH:
				//mTableUse= eTableUse.FROM_VIEW;
				list =mConvertCursorToArrayString.getUserGroupFromView(DatabaseAdapter.KEY_USER_GROUP_YASUMI_YEARMONTH_SQL, DatabaseAdapter.VIEW_M_USER_CUSTOMER_GROUP );
				returnArr = new String[list.size()];
				returnArr = list.toArray(returnArr);
				break;
			case IExpGroup.EXP_GROUP_STAFF_CONTRACT_STATUS_YEARMONTH:
				//mTableUse= eTableUse.FROM_VIEW;
				list =mConvertCursorToArrayString.getUserGroupFromView(DatabaseAdapter.KEY_USER_GROUP_CONTRACT_YEARMONTH_SQL, DatabaseAdapter.VIEW_M_USER_CUSTOMER_GROUP );
				returnArr = new String[list.size()];
				returnArr = list.toArray(returnArr);
				break;
				
			case IExpGroup.EXP_GROUP_TRAINING_YEAR:
				returnArr = new String[1];
				returnArr[0] ="0";	/** số nhân viên thử việc trong năm */
				break;
			case IExpGroup.EXP_GROUP_CONTRACT_YEAR:
				returnArr = new String[2];
				returnArr[0] ="0";	/** số nhân viên ký HD trong năm có thử việc trong năm */
				returnArr[1] ="1";	/** số nhân viên ký HD trong năm nhưng thử việc từ năm trước*/
				break;
			case IExpGroup.EXP_GROUP_NOTCONTRACT_YEAR:
				returnArr = new String[1];
				returnArr[0] ="0";	/** số nhân viên không ký HD sau thử việc */
				break;
			case IExpGroup.EXP_GROUP_CONTRACT_LESS_MONTH:
				returnArr = new String[1];
				returnArr[0] ="0";	/** số nhân viên có thâm niên nhỏ hơn tháng setting trong system*/
				break;
			case IExpGroup.EXP_GROUP_STAFF_CURRENT_POSITION_NOT_SATIFIED:
				returnArr = new String[1];
				returnArr[0] ="0";	/** số nhân viên có thâm niên khong phu hop voi chuc vu*/
				break;
			case IExpGroup.EXP_GROUP_YASUMI_YEAR:
				returnArr = new String[3];
				returnArr[0] ="0";	/** Hai nam truoc*/
				returnArr[1] ="1";	/** Mot nam truoc*/
				returnArr[2] ="2";	/** Nam hien tai duoc setting trong he thong */
		}
		
		mDatabaseAdapter.close();
		
    	}catch (Exception e){
    		mDatabaseAdapter.close();
    	}
    	return returnArr;
	}

	/**
	 * Get group dung cho bieu do
	 * @param group
	 * @return
	 * ArrayList<ChartItem>
	 */
	public ArrayList<ChartItem> getGroupChartItem(int group) {
    	ArrayList<ChartItem> list= new ArrayList<ChartItem>();
    	int currentYear =0;
    	try{
    		mDatabaseAdapter.open();
    		SystemConfigItemHelper systemConfig = new SystemConfigItemHelper(ctx);
        	currentYear = systemConfig.getYearProcessing();
        	if(currentYear==0){
        		currentYear = Calendar.getInstance().get(Calendar.YEAR);
        	}
		switch (group){
			case IExpGroup.EXP_GROUP_YASUMI_YEARMONTH:
				//mTableUse= eTableUse.FROM_VIEW;s
				list =mConvertCursorToArrayString.getUserGroupFromViewChartItem(DatabaseAdapter.KEY_USER_GROUP_YASUMI_YEARMONTH_SQL_YM, DatabaseAdapter.VIEW_M_USER_CUSTOMER_GROUP );
				break;
			case IExpGroup.EXP_GROUP_STAFF_STATUS_YEARMONTH:
				list =mConvertCursorToArrayString.getDevWorkingByTreeYearMonthChartItem(currentYear);
				break;
			case IExpGroup.EXP_GROUP_STAFF_STATUS_TRAINING_YEAR:
				/* lay so LTV thu viec trong cac nam */
				list = mConvertCursorToArrayString.getDevTrainingByThreeYearMonthChartItem(currentYear);
				break;
			case IExpGroup.EXP_GROUP_STAFF_STATUS_CONTRACT_YEAR:
				/* lay so LTV chinh thuc trong cac nam */
				list = mConvertCursorToArrayString.getDevContractByThreeYearMonthChartItem(currentYear);
				break;
		}
		
		mDatabaseAdapter.close();
		
    	}catch (Exception e){
    		mDatabaseAdapter.close();
    	}
    	return list;
	}
	
	@Override
	public List<User> getChildGroup(int group) {
		
		return null;
	}

	@Override
	public List<User> getChildGroup(int group ,String groupValue) {
		String xWhere="";
		List<User>  list=null;
		int yearProcessing; 
		float keikenMonthProcessing ;
		try{
    		mDatabaseAdapter.open();
    		mTableUse= eTableUse.FROM_USER_TABLE;
    		
    		yearProcessing= ((EmployeeTrackerApplication)ctx.getApplicationContext()).getYearProcessing();
    		keikenMonthProcessing=(float) ((EmployeeTrackerApplication)ctx.getApplicationContext()).getKeikenMonthProcessing();
    		
		switch (group){
			case IExpGroup.EXP_GROUP_BUSINESS_KBN:
				if (groupValue.equals("") || groupValue==null){
					xWhere =" AND (" + DatabaseAdapter.KEY_BUSINESS_KBN + " IS NULL OR "+DatabaseAdapter.KEY_BUSINESS_KBN + " ='')";
				}else{
					xWhere =" AND " + DatabaseAdapter.KEY_BUSINESS_KBN + " = '" + groupValue +"'";
				}
				
				break;
			case IExpGroup.EXP_GROUP_DEPT:
				if (groupValue.equals("") || groupValue==null){
					xWhere =" AND (" + DatabaseAdapter.KEY_DEPT_NAME + " IS NULL OR "+DatabaseAdapter.KEY_DEPT_NAME + " ='')";
				}else{
					xWhere =" AND " + DatabaseAdapter.KEY_DEPT_NAME + " = '" + groupValue + "'";
				}
				
				break;
			case IExpGroup.EXP_GROUP_JAPANESE:
				if (groupValue.equals("") || groupValue==null){
					xWhere =" AND (" + DatabaseAdapter.KEY_JAPANESE + " IS NULL OR "+DatabaseAdapter.KEY_JAPANESE + " ='')";
				}else{
					xWhere =" AND " + DatabaseAdapter.KEY_JAPANESE + " = '" + groupValue  + "'";
				}
				
				break;
			case IExpGroup.EXP_GROUP_BUSSINESS_ALLOWANCE:
				if (groupValue.equals("") || groupValue==null){
					xWhere =" AND (" + DatabaseAdapter.KEY_ALLOWANCE_BUSINESS+ " IS NULL OR "+DatabaseAdapter.KEY_ALLOWANCE_BUSINESS + " ='')";
				}else{
					xWhere =" AND " + DatabaseAdapter.KEY_ALLOWANCE_BUSINESS + " = '" + groupValue  + "'";
				}

				break;
			case IExpGroup.EXP_GROUP_KEIKEN:
				if (groupValue.equals("") || groupValue==null || groupValue.equals("0")){
					/** 2013.09.16 ADD START */
					/** từ 0 - 12 tháng */
					xWhere =" AND (" + DatabaseAdapter.KEY_YOBI_REAL1 + " IS NULL OR "+DatabaseAdapter.KEY_YOBI_REAL1 +" <1.0 "+" OR "+DatabaseAdapter.KEY_YOBI_REAL1 + " ='')";
					/** 2013.09.16 ADD END */
				}else{
					/** 2013.09.16 ADD START */
					switch(Integer.parseInt(groupValue)){
					case 1:
						xWhere =" AND " + DatabaseAdapter.KEY_YOBI_REAL1 + " BETWEEN 1.0 AND 1.9 ";
						break;
					case 2:
						xWhere =" AND " + DatabaseAdapter.KEY_YOBI_REAL1 + " BETWEEN 2.0 AND 2.9 ";
						break;
					case 3:
						xWhere =" AND " + DatabaseAdapter.KEY_YOBI_REAL1 + " BETWEEN 3.0 AND 3.9 ";
						break;
						/** 2013.09.16 ADD END */
					case 4:
						xWhere =" AND " + DatabaseAdapter.KEY_YOBI_REAL1 + " BETWEEN 4.0 AND 4.9 ";
						break;
						/** 2013.09.16 ADD END */
					case 5:
						xWhere =" AND " + DatabaseAdapter.KEY_YOBI_REAL1 + " >= 5.0 ";
						break;
						/** 2013.09.16 ADD END */
					}
				}
				break;
			case IExpGroup.EXP_GROUP_KEIKEN_LABOR:
				if (groupValue.equals("") || groupValue==null || groupValue.equals("0")){
					/** 2013.09.16 ADD START */
					/** từ 0 - 12 tháng */
					xWhere =" AND (" + DatabaseAdapter.KEY_YOBI_REAL2 + " IS NULL OR "+DatabaseAdapter.KEY_YOBI_REAL2 +" <1.0 "+" OR "+DatabaseAdapter.KEY_YOBI_REAL2 + " ='')";
					/** 2013.09.16 ADD END */
				}else{
					/** 2013.09.16 ADD START */
					switch(Integer.parseInt(groupValue)){
					case 1:
						xWhere =" AND " + DatabaseAdapter.KEY_YOBI_REAL2 + " BETWEEN 1.0 AND 1.9 ";
						break;
					case 2:
						xWhere =" AND " + DatabaseAdapter.KEY_YOBI_REAL2 + " BETWEEN 2.0 AND 2.9 ";
						break;
					case 3:
						xWhere =" AND " + DatabaseAdapter.KEY_YOBI_REAL2 + " BETWEEN 3.0 AND 3.9 ";
						break;
						/** 2013.09.16 ADD END */
					case 4:
						xWhere =" AND " + DatabaseAdapter.KEY_YOBI_REAL2 + " BETWEEN 4.0 AND 4.9 ";
						break;
						/** 2013.09.16 ADD END */
					case 5:
						xWhere =" AND " + DatabaseAdapter.KEY_YOBI_REAL2 + " >= 5.0 ";
						break;
						/** 2013.09.16 ADD END */
					}
				}
				break;
			case IExpGroup.EXP_GROUP_POSITION:
				if (groupValue.equals("") || groupValue==null){
					xWhere =" AND (" + DatabaseAdapter.KEY_POSITION_NAME + " IS NULL OR "+DatabaseAdapter.KEY_POSITION_NAME + " ='')";
				}else{
					xWhere =" AND " + DatabaseAdapter.KEY_POSITION_NAME + " = '" + groupValue  + "'";
				}
				
				break;
			case IExpGroup.EXP_GROUP_SEX:
				if (groupValue.equals("") || groupValue==null){
					xWhere =" AND (" + DatabaseAdapter.KEY_SEX + " IS NULL OR "+DatabaseAdapter.KEY_SEX + " =0)";
				}else{
					xWhere =" AND " + DatabaseAdapter.KEY_SEX + " = " + groupValue;
				}
				
				break;
			case IExpGroup.EXP_GROUP_TEAM:
				if (groupValue.equals("") || groupValue==null){
					xWhere =" AND (" + DatabaseAdapter.KEY_TEAM_NAME + " IS NULL OR "+DatabaseAdapter.KEY_TEAM_NAME + " ='')";
				}else{
					xWhere =" AND " + DatabaseAdapter.KEY_TEAM_NAME + " = '" + groupValue  + "'";
				}
				
			break;
			/** 2013.09.16 ADD START */
			case IExpGroup.EXP_GROUP_LABOUR_USER:
				if (groupValue.equals("") || groupValue.equals("0") || groupValue==null){
					xWhere =" AND (" + DatabaseAdapter.KEY_ISLABOUR + " IS NULL OR "+DatabaseAdapter.KEY_ISLABOUR + " =0)";
				}else{
					xWhere =" AND " + DatabaseAdapter.KEY_ISLABOUR + " = " + groupValue;
				}
				break;
				
			case IExpGroup.EXP_GROUP_YASUMI:
				if (groupValue.equals("") || groupValue.equals("0") || groupValue==null){
					xWhere =" AND (" + DatabaseAdapter.KEY_OUT_DATE + " IS NULL OR "+DatabaseAdapter.KEY_OUT_DATE + " ='')";
				}else{
					xWhere =" AND " + DatabaseAdapter.KEY_OUT_DATE + " <> '' ";
				}
				break;
			/** 2013.09.16 ADD END */
			
			/** 2013.09.27 ADD START */
			case IExpGroup.EXP_GROUP_SALARY_BASIC:
				if (groupValue.equals("") || groupValue==null || groupValue.equals("0")){
					/** từ 0 - 300 USD */
					xWhere =" AND (" + DatabaseAdapter.KEY_SALARY_NOTALOWANCE + " IS NULL OR "+DatabaseAdapter.KEY_SALARY_NOTALOWANCE +" <300.0 "+" OR "+DatabaseAdapter.KEY_SALARY_NOTALOWANCE + " ='')";
				}else{
					switch(Integer.parseInt(groupValue)){
					case 1:
						xWhere =" AND " + DatabaseAdapter.KEY_SALARY_NOTALOWANCE + " BETWEEN 300.0 AND 399.9 ";
						break;
					case 2:
						xWhere =" AND " + DatabaseAdapter.KEY_SALARY_NOTALOWANCE + " BETWEEN 400.0 AND 499.9 ";
						break;
					case 3:
						xWhere =" AND " + DatabaseAdapter.KEY_SALARY_NOTALOWANCE + " BETWEEN 500.0 AND 599.9 ";
						break;
					case 4:
						xWhere =" AND " + DatabaseAdapter.KEY_SALARY_NOTALOWANCE + " BETWEEN 600.0 AND 699.9 ";
						break;
					case 5:
						xWhere =" AND " + DatabaseAdapter.KEY_SALARY_NOTALOWANCE + " >= 700.0 ";
						break;
						
					}
				}
				break;				
				/** 2013.09.27 ADD END */
				
			case IExpGroup.EXP_GROUP_CUSTOMER:
				if (groupValue.equals("") || groupValue==null){
					xWhere =" AND (" + DatabaseAdapter.KEY_CUSTOMER_GROUP_NAME + " IS NULL OR "+DatabaseAdapter.KEY_CUSTOMER_GROUP_NAME + " ='')";
				}else{
					xWhere =" AND " + DatabaseAdapter.KEY_CUSTOMER_GROUP_NAME + " = '" + groupValue  + "'";
				}
				mTableUse= eTableUse.FROM_VIEW;
				break;
			case IExpGroup.EXP_GROUP_YASUMI_YEARMONTH:
				if (groupValue.equals("") || groupValue==null){
					xWhere =" AND (" + DatabaseAdapter.KEY_USER_GROUP_YASUMI_YEARMONTH_SQL + " IS NULL OR "+DatabaseAdapter.KEY_USER_GROUP_YASUMI_YEARMONTH_SQL + " ='')";
				}else{
					xWhere =" AND " + DatabaseAdapter.KEY_USER_GROUP_YASUMI_YEARMONTH_SQL + " = '" + groupValue  + "'";
				}
				//mTableUse= eTableUse.FROM_VIEW;
				break;
			case IExpGroup.EXP_GROUP_STAFF_CONTRACT_STATUS_YEARMONTH:
				if (groupValue.equals("") || groupValue==null){
					xWhere =" AND (" + DatabaseAdapter.KEY_USER_GROUP_CONTRACT_YEARMONTH_SQL + " IS NULL OR "+DatabaseAdapter.KEY_USER_GROUP_CONTRACT_YEARMONTH_SQL + " ='')";
				}else{
					xWhere =" AND " + DatabaseAdapter.KEY_USER_GROUP_CONTRACT_YEARMONTH_SQL + " = '" + groupValue  + "'";
				}
				//mTableUse= eTableUse.FROM_VIEW;
				break;
				
			/** Số nhân viên thử việc trong năm*/	
			case IExpGroup.EXP_GROUP_TRAINING_YEAR:
				if (groupValue.equals("") || groupValue==null||groupValue.equals("0")){
					xWhere =" AND (strftime('%Y'," + DatabaseAdapter.KEY_TRAINING_DATE + ")= '" + yearProcessing +"' )";
				}else{
					//xWhere =" AND (strftime('%Y'," + DatabaseAdapter.KEY_TRAINING_DATE + ")= '" + yearProcessing +"' )";
				}
				//mTableUse= eTableUse.FROM_VIEW;
				break;
				/** Số nhân viên nhận chính thức trong năm*/	
			case IExpGroup.EXP_GROUP_CONTRACT_YEAR:
				if (groupValue.equals("") || groupValue==null||groupValue.equals("0")){
					xWhere =" AND (strftime('%Y'," + DatabaseAdapter.KEY_IN_DATE + ")= '" + yearProcessing +"' ) ";
					xWhere +=" AND (strftime('%Y'," + DatabaseAdapter.KEY_TRAINING_DATE + ")= '" + yearProcessing +"' ) ";
				}else{
					xWhere =" AND (strftime('%Y'," + DatabaseAdapter.KEY_IN_DATE + ")= '" + yearProcessing +"' ) ";
					xWhere +=" AND (strftime('%Y'," + DatabaseAdapter.KEY_TRAINING_DATE + ")= '" + (yearProcessing -1) +"' ) ";
				}
				//mTableUse= eTableUse.FROM_VIEW;
				break;
				/** Số nhân viên không nhận sau khi thử việc*/	
			case IExpGroup.EXP_GROUP_NOTCONTRACT_YEAR:
				if (groupValue.equals("") || groupValue==null||groupValue.equals("0")){
					xWhere =" AND (strftime('%Y'," + DatabaseAdapter.KEY_TRAINING_DATE + ")= '" + yearProcessing +"' ) ";
					xWhere +=" AND (" +DatabaseAdapter.KEY_OUT_DATE + " <>'')";
				}else{
					//xWhere =" AND (strftime('%Y'," + DatabaseAdapter.KEY_TRAINING_DATE + ")= '" + yearProcessing +"' ) ";
					//xWhere +=" AND (" + DatabaseAdapter.KEY_OUT_DATE + " IS NOT NULL OR "+DatabaseAdapter.KEY_OUT_DATE + " <>'')";
				}
				//mTableUse= eTableUse.FROM_VIEW;
				break;
			case IExpGroup.EXP_GROUP_CONTRACT_LESS_MONTH:
				if (groupValue.equals("") || groupValue==null || groupValue.equals("0")){
					/** từ N tháng */
					xWhere =" AND (" + DatabaseAdapter.KEY_YOBI_REAL1 + " IS NULL OR "+DatabaseAdapter.KEY_YOBI_REAL1 +" < "+ (float)(keikenMonthProcessing/12.0) +" OR "+DatabaseAdapter.KEY_YOBI_REAL1 + " ='')";
					xWhere +=" AND (" +DatabaseAdapter.KEY_IN_DATE + " <>'')"; // da nhan chinh thuc
				}
				break;
			case IExpGroup.EXP_GROUP_STAFF_CURRENT_POSITION_NOT_SATIFIED:
				mTableUse = eTableUse.FROM_VIEW;
				break;
			case IExpGroup.EXP_GROUP_YASUMI_YEAR:
				switch(Integer.parseInt(groupValue)){
					case 0:
						//2 nam truoc
						xWhere =" AND STRFTIME('%Y'," + DatabaseAdapter.KEY_OUT_DATE + ") = '" + (yearProcessing -2)   +"' ";
						break;
					case 1:
						//1 nam truoc
						xWhere =" AND STRFTIME('%Y'," + DatabaseAdapter.KEY_OUT_DATE + ") = '" + (yearProcessing -1)   +"' ";
						break;
					case 2:
						//nam setting trong he thong
						xWhere =" AND STRFTIME('%Y'," + DatabaseAdapter.KEY_OUT_DATE + ") = '" + (yearProcessing )   +"' ";
						break;
				}
				break;
		}
		if(mTableUse==eTableUse.FROM_VIEW && group == IExpGroup.EXP_GROUP_CUSTOMER){
			list = mConvertCursorToListString.getUserListFromView(group,xWhere,DatabaseAdapter.VIEW_M_USER_YASUMI_YEARMONTH_GROUP);
		}else if(mTableUse==eTableUse.FROM_VIEW && group == IExpGroup.EXP_GROUP_STAFF_CURRENT_POSITION_NOT_SATIFIED){
				list = mConvertCursorToListString.getUserListFromView(group,xWhere,DatabaseAdapter.VIEW_USER_LIST_CURRENT_POSITION_NOT_SATIFIED_SQL);
		}else if (mTableUse==eTableUse.FROM_USER_TABLE){
				list = mConvertCursorToListString.getUserList(xWhere);
		}
		mDatabaseAdapter.close();
		
    	}catch (Exception e){
    		mDatabaseAdapter.close();
    	}
				
		return list;
	}

	@Override
	public List<User> getChildUserHisGroup(int group, String groupValue) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
