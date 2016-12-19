package com.ussol.employeetracker.helpers;

import android.content.Context;
import android.content.SharedPreferences;

import com.ussol.employeetracker.models.MasterConstants;

public class GetSearchItemSetting {
	static SharedPreferences pre;
	static Context context;
	private static String _yasumiSQLString ="";
	private static String _contractSQLString ="";
	
	public GetSearchItemSetting(Context ctx ){
		context=ctx;
	}
	/**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * lấy về điều kiện filter dữ liệu
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    public static  String  getWhereSQL(){
    	String xWhere ="";
    	 /** đọc thông tin từ file preferences*/
    	pre = context.getSharedPreferences(MasterConstants.PRE_SEARCH_ITEM_FILE, Context.MODE_PRIVATE);
    	
    	String deptCode = pre.getString("pre_Dept", "");
    	String teamCode = pre.getString("pre_Team", "");
    	String posCode = pre.getString("pre_Position", "");

    	String Japanese = pre.getString("pre_Japanese", "");
		String Allowance_Business = pre.getString("pre_Allowance_Business", "");
		String Business_Kbn = pre.getString("pre_Business_Kbn", "");
		String Yasumi_Kbn = pre.getString("pre_UserOutDate", "");
		
		boolean isCurrentLabour = pre.getBoolean("pre_isCurrentLabour", false);
		boolean isDeleted = pre.getBoolean("pre_UserDelete", false);
		boolean isIncludeTrialStaff = pre.getBoolean("pre_UserIncludeTrialStaff", true);
		//boolean isOutDate = pre.getBoolean("pre_UserOutDate", false);
		boolean enabledJapanese = pre.getString("pre_enabledJapanese", "0").equals("1")?true:false;
		boolean enabledAllowance_Business = pre.getString("pre_enabledAllowance_Business", "0").equals("1")?true:false;
		boolean isChkDept = pre.getBoolean("pre_chkDept", false);
		boolean isChkTeam = pre.getBoolean("pre_chkTeam", false);
		boolean isChkPosition = pre.getBoolean("pre_chkPosition", false);
		boolean isChkBusiness_Kbn = pre.getBoolean("pre_chkBusiness_Kbn", false);
		
		if(isChkDept){
			//xWhere = xWhere + " AND (" + DatabaseAdapter.KEY_DEPT + " IN (" + deptCode + ") OR " + DatabaseAdapter.KEY_DEPT + " IS NULL OR " + DatabaseAdapter.KEY_DEPT + "=0 )";
			xWhere = xWhere + " AND (" + DatabaseAdapter.KEY_DEPT + " IN (" + deptCode + ")) ";
		}
		if(isChkTeam){
			//xWhere = xWhere + " AND (" + DatabaseAdapter.KEY_TEAM + " IN (" + teamCode + ") OR " + DatabaseAdapter.KEY_TEAM + " IS NULL OR " + DatabaseAdapter.KEY_TEAM + "=0)";
			xWhere = xWhere + " AND (" + DatabaseAdapter.KEY_TEAM + " IN (" + teamCode + "))";
		}
		if(isChkPosition){
			//xWhere = xWhere + " AND (" + DatabaseAdapter.KEY_POSITION + " IN (" + posCode + ") OR " + DatabaseAdapter.KEY_POSITION + " IS NULL OR " + DatabaseAdapter.KEY_POSITION + "=0)";
			xWhere = xWhere + " AND (" + DatabaseAdapter.KEY_POSITION + " IN (" + posCode + "))";
		}
		
		if(enabledJapanese){
			/** nếu có chỉ định rõ bằng cấp tiếng Nhật */
			if (!Japanese.equals("")){
				xWhere = xWhere+ " AND " + DatabaseAdapter.KEY_JAPANESE + "='" + Japanese + "'";
			}
		}else{
			/** nếu không có chỉ định rõ bằng cấp tiếng Nhật -> không cần add điều kiện search*/
			
		}
		
		if(enabledAllowance_Business){
			/** nếu có chỉ định rõ bằng mức trợ cấp */
			
			if (!Allowance_Business.equals("")){
				xWhere = xWhere+ " AND " + DatabaseAdapter.KEY_ALLOWANCE_BUSINESS + "='" + Allowance_Business + "'";
			}
		}else{
			/** nếu không  có chỉ định rõ bằng mức trợ cấp */
		}
		
		/** chuyên môn */
		/*if (isChkBusiness_Kbn){
			if(Business_Kbn.equals("0")){
				*//** phien dich *//*
				Business_Kbn="2";
			}
			xWhere = xWhere+ " AND " + DatabaseAdapter.KEY_BUSINESS_KBN + "='" + Business_Kbn + "'";
		}
		*/
		if(Business_Kbn.equals("2")){
			/** phien dich */
			Business_Kbn="2";
			xWhere = xWhere+ " AND " + DatabaseAdapter.KEY_BUSINESS_KBN + "='" + Business_Kbn + "'";
		}
		if(Business_Kbn.equals("1")){
			/** la[ trinh */
			Business_Kbn="1";
			xWhere = xWhere+ " AND " + DatabaseAdapter.KEY_BUSINESS_KBN + "='" + Business_Kbn + "'";
		}
		if(Business_Kbn.equals("3")){
			/** cong viec khac nhu tong vu */
			Business_Kbn="3";
			xWhere = xWhere+ " AND " + DatabaseAdapter.KEY_BUSINESS_KBN + "='" + Business_Kbn + "'";
		}
		if(Business_Kbn.equals("9")||Business_Kbn.equals("")){
			/** get toan bo */
			Business_Kbn="";
		}

		/** user đã xóa */
		if (isDeleted){
			xWhere = xWhere+ " AND " + DatabaseAdapter.KEY_ISDELETED + "=1" ;
		}else{
			xWhere = xWhere+ " AND " + DatabaseAdapter.KEY_ISDELETED + "=0" ;
		}
		
		/** user đã nghỉ việc */
		/*
		if (isOutDate){
			xWhere = xWhere+ " AND TRIM(" + DatabaseAdapter.KEY_OUT_DATE + ") <> ''" ;
			//xWhere = xWhere+ " AND "+ DatabaseAdapter.KEY_OUT_DATE  + " < date('now'))";
			 
		}else{
			xWhere = xWhere+ " AND (TRIM(" + DatabaseAdapter.KEY_OUT_DATE + ") = ''" ;
			xWhere = xWhere+ " OR "+ DatabaseAdapter.KEY_OUT_DATE  + " > date('now'))";
		}*/
		if(Yasumi_Kbn.equals("0")){
			/** chi nhan vien nghi viec */
			xWhere = xWhere+ " AND TRIM(" + DatabaseAdapter.KEY_OUT_DATE + ") <> ''" ;
			_yasumiSQLString = " AND TRIM(" + DatabaseAdapter.KEY_OUT_DATE + ") <> ''" ;
		}
		if(Yasumi_Kbn.equals("1")){
			/**chua nghi viec cho toi hien tai*/
			xWhere = xWhere+ " AND (TRIM(" + DatabaseAdapter.KEY_OUT_DATE + ") = ''" ;
			xWhere = xWhere+ " OR "+ DatabaseAdapter.KEY_OUT_DATE  + " >= date('now'))";
			
			_yasumiSQLString = " AND (TRIM(" + DatabaseAdapter.KEY_OUT_DATE + ") = ''" ;
			_yasumiSQLString = _yasumiSQLString+ " OR "+ DatabaseAdapter.KEY_OUT_DATE  + " >= date('now'))";
		}
		if(Yasumi_Kbn.equals("9")||Yasumi_Kbn.equals("")){
			/** get toan bo ca nghi viec cung nhu chua nghi viec */
			Business_Kbn="";
			_yasumiSQLString ="@";
		}
				
		/** user thuộc nhóm labour */
		if (isCurrentLabour){
			xWhere = xWhere+ " AND " + DatabaseAdapter.KEY_ISLABOUR + "=1" ;
		}
		//bao gom ca nhan vien thu viec ( Chua co ngay ky HD hoac title la TrialStaff )
		if(isIncludeTrialStaff){
			//none 
		}else{
			//Chi lay cac nhan vien da ky HD
			xWhere = xWhere+ " AND TRIM(" + DatabaseAdapter.KEY_IN_DATE + ") <> ''" ;
		}
		//else{
		//	xWhere = xWhere+ " AND " + DatabaseAdapter.KEY_ISLABOUR + "<>1" ;
		//}
    	return xWhere;
    }
    
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * get trạng thái của item xóa hay chưa xóa nhân viên
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    public static boolean getIsDeleted(){
    	/** đọc thông tin từ file preferences*/
    	pre = context.getSharedPreferences(MasterConstants.PRE_SEARCH_ITEM_FILE, Context.MODE_PRIVATE);
    	boolean isDeleted = pre.getBoolean("pre_UserDelete", false);
    	return isDeleted;
    }
    /**
     * Chuỗi SQL liên quan đến SQL dùng để get nhân viên nghỉ /chưa nghỉ
     * Cần phải lấy SQL này để sử dụng trong trường hợp thống kê nhân
     * viên nghỉ theo tháng năm
     * @return
     */
    public static String getYasumiSQLString(){
    	getWhereSQL();
    	return _yasumiSQLString;
    }
}
