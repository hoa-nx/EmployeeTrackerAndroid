/**
 * Copyright (c) 2012 Fujinet Software Department 5 ( USSOL team)  
 * See the file license.txt for copying permission.
*/  
package com.ussol.employeetracker.models;

import com.ussol.employeetracker.EmployeeTrackerApplication;



public class MasterConstants {
	public static final String IMAGE_SMALL_SUFFIX = "_small.png";
	public static final String IMAGE_THUMBNAIL_SUFFIX = "_thumbnail.png";
	public static final String IMAGE_LARGE_SUFFIX = ".png";
	public static final String AUDIO_FILE_SUFFIX = ".amr";
	public static final String ET_FOLDER = "/EmployeeTracker/";
	public static String DIRECTORY = EmployeeTrackerApplication.FILES_DIR + ET_FOLDER;
	public static final String DIRECTORY_AUDIO = "Audio/";
	public static final String DIRECTORY_PICTURE = "Picture/";
	
	/** dùng để close all Activity*/
	public static final int RESULT_CLOSE_ALL = 10;
	/** dùng để gọi Activity*/
	public static final int CALL_USER_ACTIVITY_CODE = 90;
	/** dùng để gọi Activity*/
	public static final int CALL_DEPT_ACTIVITY_CODE = 100;
	/** dùng để gọi Activity*/
	public static final int CALL_TEAM_ACTIVITY_CODE = 110;
	/** dùng để gọi Activity*/
	public static final int CALL_POSITION_ACTIVITY_CODE = 120;
	/** dùng để gọi Activity*/
	public static final int CALL_SEARCH_ITEM_ACTIVITY_CODE = 130;
	/** dùng để gọi Activity*/
	public static final int CALL_SORT_ITEM_ACTIVITY_CODE = 140;
	/** dùng để gọi Activity*/
	public static final int CALL_USER_HIS_ACTIVITY_CODE = 150;
	/** dùng để gọi Activity*/
	public static final int CALL_EXP_USER_HIS_ACTIVITY_CODE = 160;
	/** dùng để gọi Activity*/
	public static final int CALL_CONFIG_ITEM_ACTIVITY_CODE = 170;
	/** dùng để gọi Activity*/
	public static final int CALL_BACKUP_ITEM_ACTIVITY_CODE = 180;
	/** dùng để gọi Activity*/
	public static final int CALL_JOB_SETTING_ACTIVITY_CODE = 190;
	/** dùng để gọi Activity*/
	public static final int CALL_HISTORY_USER_ACTIVITY_CODE = 200;

	/** master phòng ban */
	public static final int MASTER_MKBN_DEPT = 1;
	/** master nhóm */ 
	public static final int MASTER_MKBN_TEAM = 2;
	/** master chức vụ */
	public static final int MASTER_MKBN_POSITION = 3;
	/** master nhóm chức danh */
	public static final int MASTER_MKBN_POSITION_GROUP = 4;
	/** master phan nhom cong viec theo PROCES.S hay he thong cong */
	public static final int MASTER_MKBN_BUSINESS_GROUP = 5;
	/** master các khách hàng */
	public static final int MASTER_MKBN_CUSTOMER_GROUP = 6;
	
	/** chưa delete */ 
	public static final int MASTER_UNDELETED = 0;
	/** đã delete ( về mặt logic) */
	public static final int MASTER_DELETED = 1;
	/** dung de hien thi man hinh chon user de update lich su*/
	public static final int BTN_DIALOG_USER_LIST = 10001;
	
	/** lich su phong ban */
	public static final int MASTER_MKBN_DEPT_HIS = 100;
	/** lich su nhom */
	public static final int MASTER_MKBN_TEAM_HIS = 110;
	/** lich su chuc vu */
	public static final int MASTER_MKBN_POSITION_HIS = 120;
	/** lich su tieng nhat */
	public static final int MASTER_MKBN_JAPANESE_HIS = 130;
	/** lich su tro cap nghiep vu */
	public static final int MASTER_MKBN_ALLOWANCE_BUSINESS_HIS = 140;
	/** lich su xet luong */
	public static final int MASTER_MKBN_SALARY_HIS = 150;
	/** lich su tro cap BSE*/
	public static final int MASTER_MKBN_ALLOWANCE_BSE_HIS = 160;
	/** lich su doi ung khach hang */
	public static final int MASTER_MKBN_CUSTOMER_HIS = 170;
	/** lich su doi ung cong viec */
	public static final int MASTER_MKBN_SHIGOTO_HIS = 180;

	/** List expandable*/
	public static final String EXP_USER_TAG = "ExpUser";
	public static final String EXP_USER_GROUP_TAG = "ExpUserGroup";
	
	/** Search item */
	public static final String SEARCH_ITEM_TAG="SearchItem";
	
	/** Sort list item */
	public static final String SORT_ITEM_TAG="SortItem";
	
	/** thông tin TAB*/
	public static final String TAB_USER_TAG = "User";
	public static final String TAB_USER_HIS_TAG = "UserHis";
	public static final String TAB_DEPT_TAG = "Department";
	public static final String TAB_TEAM_TAG = "Team";
	public static final String TAB_POSITION_TAG = "Position";
	
	public static final String TAB_SEARCH_ITEM_1_TAG = "searchitem1";
	public static final String TAB_SEARCH_ITEM_2_TAG = "searchitem2";
	
	public static final String SEND_SMS_TAG = "Sms";
	
	/**	TAG của màn hình dialog  */
	public static final String TAB_DIALOG_TAG = "Dialog";
	public static final String TAB_DIALOG_BUNDLE = "KeyBundle";
	
	/**	KEY dùng để nhận biết chức năng nào được gọi để setting tab focus*/
	public static final String MAIN_TO_TAB_CALL = "ActionBarFocus";

	/** thông tin trình độ Nhật ngữ*/
	public static final String[] JAPANESE_LEVEL = new String[]{"N1","N2","N3","N4","N5"};
	
	/** thông tin phụ cấp nghiệp vụ */
	public static final String[] ALLOWANCE_BUSINESS_LEVEL = new String[]{"Bậc 1","Bậc 2","Bậc 3","Bậc 4","Bậc 5"};

	/** thông tin phụ cấp ngạch BSE*/
	public static final String[] ALLOWANCE_BSE_LEVEL = new String[]{"Bậc 1","Bậc 2","Bậc 3","Bậc 4","Bậc 5"};

	/** thông tin phụ cấp phòng chuyên biệt*/
	public static final String[] ALLOWANCE_ROOM_LEVEL = new String[]{"Mức 1","Mức 2","Mức 3","Mức 4","Mức 5"};
	
	/** tên file save trình tự sort */
	public static final String PRE_SORT_FILE ="sort_item_preferences";
	
	/** tên file save điều kiện search */
	public static final String PRE_SEARCH_ITEM_FILE ="search_item_preferences";
	
	/** tên file lưu các cấu hình của system*/
	public static final String PRE_SYSTEM_CONFIG_FILE ="system_config_preferences";
	
	/** Ký tự phân cách giữa các ngày tháng năm */
	public static final String DATE_SEPERATE_CHAR="-";
	/** Chuỗi format theo lịch việt */
	public static final String DATE_VN_FORMAT="dd-MM-yyyy";
	/** Chuỗi format theo lịch nhật */
	public static final String DATE_JP_FORMAT="yyyy-MM-dd";
	/** Chuỗi format theo lịch việt */
	public static final String DATETIME_VN_FORMAT="dd-MM-yyyy HH:mm:ss";
	/** Chuỗi format theo lịch nhật */
	public static final String DATETIME_JP_FORMAT="yyyy-MM-dd HH:mm:ss";
	/**tên hiển thị tại group lịch sử phòng ban */
	public static final String HIS_DEPT_NAME="LS phòng ban";
	/**tên hiển thị tại group lịch sử nhóm */
	public static final String HIS_TEAM_NAME="LS nhóm -tổ";
	/**tên hiển thị tại group lịch sử chức vụ */
	public static final String HIS_POSITION_NAME="LS bậc ngạch";
	/**tên hiển thị tại group lịch sử tiếng Nhật */
	public static final String HIS_JAPANESE_NAME="LS tiếng Nhật";
	/**tên hiển thị tại group lịch sử phụ cấp nghiệp vụ */
	public static final String HIS_ALLOWANCE_BUSINESS_NAME="LS phụ cấp nghiệp vụ";
	/**tên hiển thị tại group lịch sử phụ cấp nghiệp vụ */
	public static final String HIS_ALLOWANCE_BSE_NAME="LS phụ cấp BSE";
	/**tên hiển thị tại group lịch sử salary */
	public static final String HIS_SALARY_NAME="LS Salary";
	/**tên hiển thị tại group lịch sử salary */
	public static final String HIS_CUSTOMER_NAME="LS Khách hàng";
	/**tên hiển thị tại group lịch sử salary */
	public static final String HIS_SHIGOTO_NAME="LS Công việc";
	
	/** Loại report */
	public static final int REP_BY_DEPT=0;
	public static final int REP_BY_TEAM=1;
	public static final int REP_BY_POSITION=3;
	public static final int REP_BY_SEX=4;
	public static final int REP_BY_JAPANESE=5;
	public static final int REP_BY_BUSSINESS_KBN=6;
	public static final int REP_BY_USER_DETAIL=7;
	public static final int REP_BY_USER_LIST=8;
	public static final int REP_BY_USER_SALARY=9;
	
	/** Dung de chua vi tri hien tai cua listView khi goi toi UserEdit **/
	public static final String LISTVIEW_CURRENT_POSITION="LISTVIEW_CURRENT_POSITION";
	/**
	 * 
	 * Loại message 
	 * BIRTHDAY : SINH NHAT
	 * SALARY	: TANG LUONG
	 * YASUMI	: NGHI VIEC
	 * TRAIL: DANH GIA THỬ VIỆC
	 * 
	 */

	public static class MESSAGE_TYPE_CONST{
		public static final int BIRTHDAY = 0;
	    public static final int SALARY = 1;
	    public static final int YASUMI = 2;
	    public static final int TRAIL = 3;
	}
	
	public static enum MESSAGE_TYPE {BIRTHDAY,SALARY,YASUMI,TRAIL}
	
	public static enum MESSAGE_SMS_OR_EMAIL {SMS,EMAIL}

	public static  final String REPORT_FOOTER_COMPANY_TEXT ="FUJINET SYSTEMS JSC-DEPT 1";
}
