/**
 * Copyright (c) 2012 Fujinet Software Department 5 ( USSOL team)  
 * See the file license.txt for copying permission.
*/  

package com.ussol.employeetracker.helpers;

import java.io.IOException;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.format.DateUtils;
import android.webkit.WebChromeClient.CustomViewCallback;

import com.ussol.employeetracker.models.ChartItem;
import com.ussol.employeetracker.models.Dept;
import com.ussol.employeetracker.models.Master;
import com.ussol.employeetracker.models.MasterConstants;
import com.ussol.employeetracker.models.MessageTemplate;
import com.ussol.employeetracker.models.User;
import com.ussol.employeetracker.models.UserBusinessGroup;
import com.ussol.employeetracker.models.UserCustomerGroup;
import com.ussol.employeetracker.models.UserHistory;
import com.ussol.employeetracker.models.UserImage;
import com.ussol.employeetracker.models.UserMessageStatus;
import com.ussol.employeetracker.models.UserPositionGroup;
import com.ussol.employeetracker.utils.DateTimeUtil;
import com.ussol.employeetracker.utils.Log;
import com.ussol.employeetracker.utils.Strings;
import com.ussol.employeetracker.utils.Utils;
import com.ussol.employeetracker.R;


/**
 * @author hoa-nx
 * (ALT+Shift+J)
 */
public class DatabaseAdapter {

	// database and table name
	private static int DB_VERSION = 16;//olversion is 1
	
	public static final String DATABASE_NAME = "EmployeeTrackerData";
	private final String TABLE_M_USER = "m_user";
	private final String TABLE_M_USER_POSITION_GROUP = "m_user_position_group";
	private final String TABLE_M_USER_CUSTOMER_GROUP = "m_user_customer_group";
	private final String TABLE_M_USER_BUSINESS = "m_user_business";
	private final String TABLE_M_USER_IMAGE = "m_user_image";
	private final String TABLE_M_USER_HIS = "m_user_his";
	private final String TABLE_M_MESSAGE_TEMPLATE = "m_message_template";
	private final String TABLE_M_USER_MESSAGE_STATUS= "m_user_message_status";
	private final String TABLE_M_USER_SALARY = "m_user_salary";
	private final String TABLE_M_USER_SALARY_SUMMARY = "m_user_salary_summary";
	private final String TABLE_M_MEI = "m_mei";
	private final String TABLE_M_COMPANY = "m_company";
	/**
	 * Doanh thu
	 */
	private final String TABLE_T_REVENUE = "t_revenue";

	//View 
	public static final String VIEW_M_USER_CUSTOMER_GROUP = "view_user_customer_group";
	public static final String VIEW_M_USER_YASUMI_YEARMONTH_GROUP = "not_create_view";//khong tao view nay, chi tao ten de su dung
	
	public static final String VIEW_DEV_TRAINING_COUNT_BY_YEARMONTH = "view_dev_training_count_by_yearmonth";
	public static final String VIEW_DEV_YASUMI_COUNT_BY_YEARMONTH = "view_dev_yasumi_count_by_yearmonth";
	public static final String VIEW_PD_TRAINING_COUNT_BY_YEARMONTH = "view_pd_training_count_by_yearmonth";
	public static final String VIEW_PD_YASUMI_COUNT_BY_YEARMONTH = "view_pd_yasumi_count_by_yearmonth";
	
	private Context context;

	//column m_user_salary

	/**
	 * luong co ban
	 */
	public static final String KEY_SALARY_BASIC = "SALARY_BASIC";
	/**
	 * luong hop dong
	 */
	public static final String KEY_SALARY_CONTRACT = "SALARY_CONTRACT";
	/**
	 * thuong du an
	 */
	public static final String KEY_SALARY_BONUS = "SALARY_BONUS";
	/**
	 * phu cap chuc vu
	 */
	public static final String KEY_SALARY_ALLOWANCE_POSITION = "SALARY_ALLOWANCE_POSITION";
	/**
	 * phu cap nghiep vu
	 */
	public static final String KEY_SALARY_ALLOWANCE_BUSINESS = "SALARY_ALLOWANCE_BUSINESS"; //
	/**
	 * phu cap cong viec
	 */
	public static final String KEY_SALARY_ALLOWANCE_WORK = "SALARY_ALLOWANCE_WORK";//
	/**
	 * phi support
	 */
	public static final String KEY_SALARY_ALLOWANCE_SUPPORT_FREE = "SALARY_ALLOWANCE_SUPPORT_FREE";//
	public static final String KEY_SALARY_ALLOWANCE_BSE = "SALARY_ALLOWANCE_BSE"; //phu cap ngach BSE
	public static final String KEY_SALARY_ALLOWANCE_ROOM = "SALARY_ALLOWANCE_ROOM"; //phong chuyen biet
	public static final String KEY_SALARY_ALLOWANCE_JAPANESE = "SALARY_ALLOWANCE_JAPANESE"; //tieng Nhat
	public static final String KEY_SALARY_ALLOWANCE_OT = "SALARY_ALLOWANCE_OT"; // tien lam ngoai gio
	public static final String KEY_SALARY_ALLOWANCE_MANAGEMENT = "SALARY_ALLOWANCE_MANAGEMENT"; // phu cap quan ly
	public static final String KEY_SALARY_ALLOWANCE_OTHER_1 = "SALARY_ALLOWANCE_OTHER_1"; // phu cap khac
	public static final String KEY_SALARY_ALLOWANCE_OTHER_2 = "SALARY_ALLOWANCE_OTHER_2"; // phu cap khac
	public static final String KEY_SALARY_ALLOWANCE_OTHER_3 = "SALARY_ALLOWANCE_OTHER_3"; // phu cap khac
	public static final String KEY_SALARY_ALLOWANCE_FIX = "SALARY_ALLOWANCE_FIX"; // phu cap co dinh hang thang
	public static final String KEY_SALARY_SUMMARY = "SALARY_SUMMARY"; // Tong luong
	public static final String KEY_SALARY_ESTIMATE = "SALARY_ESTIMATE"; // Diem danh gia
	public static final String KEY_SALARY_EFFORT = "SALARY_EFFORT"; // So MM
	public static final String KEY_SALARY_MINUS_1 = "SALARY_MINUS_1"; // Khoan tru
	public static final String KEY_SALARY_MINUS_2 = "SALARY_MINUS_2"; // Khoan tru
	public static final String KEY_SALARY_MINUS_3 = "SALARY_MINUS_3"; // Khoan tru
	public static final String KEY_SALARY_INCOM_TAX = "SALARY_INCOM_TAX"; // Thue thu nhap ca nha
	public static final String KEY_SALARY_DEPENDENCY_MINUS = "SALARY_DEPENDENCY_MINUS"; // Tong so giam tru gia canh
	// column m_user
	public static final String KEY_ID = "_id";
	public static final String KEY_TAG = "TAG";
	public static final String KEY_CODE="CODE";
	public static final String KEY_GOOGLE_CONTACT_ID="GOOGLE_CONTACT_ID";
	public static final String KEY_FIRST_NAME="FIRST_NAME";
	public static final String KEY_LAST_NAME="LAST_NAME";
	public static final String KEY_FULL_NAME="FULL_NAME";
	public static final String KEY_SEX="SEX";
	public static final String KEY_BIRTHDAY="BIRTHDAY";
	public static final String KEY_ADDRESS="ADDRESS";
	public static final String KEY_HOME_TEL="HOME_TEL";
	public static final String KEY_MOBILE="MOBILE";
	public static final String KEY_FAX="FAX";
	public static final String KEY_POSITION="POSITION";
	public static final String KEY_POSITION_NAME="POSITION_NAME";
	public static final String KEY_DEPT="DEPT";
	public static final String KEY_DEPT_NAME="DEPT_NAME";
	public static final String KEY_TEAM="TEAM";
	public static final String KEY_TEAM_NAME="TEAM_NAME";
	public static final String KEY_TANT="TANT";
	public static final String KEY_TANT_NAME="TANT_NAME";
	public static final String KEY_ESTIMATE_POINT="ESTIMATE_POINT";
	/**
	 * Ngày bắt đầu thử việc
	 */
	public static final String KEY_TRAINING_DATE="TRAINING_DATE";
	/**
	 * Ngày kết thúc thử việc
	 */
	public static final String KEY_TRAINING_DATE_END="TRAINING_DATE_END";
	/**
	 * Ngày bắt đầu học việc
	 */
	public static final String KEY_LEARN_TRAINING_DATE="LEARN_TRAINING_DATE";
	/**
	 * Ngày kết thúc học việc
	 */
	public static final String KEY_LEARN_TRAINING_DATE_END="LEARN_TRAINING_DATE_END";
	/**
	 * Ngày nhận chính thức
	 */
	public static final String KEY_IN_DATE="IN_DATE";
	/**
	 * Ngày gia nhập nhóm labor
	 */
	public static final String KEY_JOIN_DATE="JOIN_DATE";
	/**
	 * Ngày nghỉ việc
	 */
	public static final String KEY_OUT_DATE="OUT_DATE";
	/**
	 * Ngày kết hôn
	 */
	public static final String KEY_MARRIED_DATE="MARRIED_DATE";
	/**
	 * Số năm kinh nghiệm tại công ty khác
	 */
	public static final String KEY_INIT_KEIKEN="INIT_KEIKEN";
	/**
	 * Qui đổi kinh nghiệm(Tháng)
	 */
	public static final String KEY_CONVERT_KEIKEN="CONVERT_KEIKEN";
	public static final String KEY_USER_KBN="USER_KBN";
	public static final String KEY_NOTE="NOTE";
	public static final String KEY_IMG="IMG";
	public static final String KEY_EMAIL="EMAIL";
	/**
	 * Năng lực Nhật ngữ (N1,2...)
	 */
	public static final String KEY_JAPANESE="JAPANESE";
	/**
	 * Phụ cấp nghiệp vụ (bậc 1,2,3,4,5...)
	 */
	public static final String KEY_ALLOWANCE_BUSINESS="ALLOWANCE_BUSINESS";
	/**
	 * Phu cap ngach BSE
	 */
	public static final String KEY_ALLOWANCE_BSE="ALLOWANCE_BSE";

	/**
	 * Loai nhan vien nhan tu training center hay la pv truc tiep
	 */
	public static final String KEY_STAFF_KBN="STAFF_KBN";

	/**
	 * GPA : Diem tot nghiep
	 */
	public static final String KEY_GPA="GPA";

	/**
	 * GPA : Diem tot nghiep bang chữ
	 */
	public static final String KEY_GPA_TEXT="GPA_TEXT";
	/**
	 * Hoc trường nào
	 */
	public static final String KEY_COLLECT_NAME="COLLECT_NAME";
	/**
	 * Người support
	 */
	public static final String KEY_SUPPORTER1="SUPPORTER1";
	/**
	 * Người support
	 */
	public static final String KEY_SUPPORTER2="SUPPORTER2";
	/**
	 * Người support
	 */
	public static final String KEY_SUPPORTER3="SUPPORTER3";
	/**
	 * Người phỏng vấn
	 */
	public static final String KEY_INTERVIEWER1="INTERVIEWER1";
	/**
	 * Người phỏng vấn
	 */
	public static final String KEY_INTERVIEWER2="INTERVIEWER2";

	/**
	 * Người phỏng vấn
	 */
	public static final String KEY_INTERVIEWER3="INTERVIEWER3";

	/**
	 * Ket qua PV
	 */
	public static final String KEY_INTERVIEW_KEKKA="INTERVIEW_KEKKA";


	/**
	 * Phụ cấp phòng chuyên biệt
	 */
	public static final String KEY_ALLOWANCE_ROOM="ALLOWANCE_ROOM";
	public static final String KEY_MARRIED="MARRIED";
	public static final String KEY_SALARY_NOTALOWANCE="SALARY_NOTALOWANCE";/** luong co ban */
	public static final String KEY_SALARY_ALOWANCE="SALARY_ALOWANCE";/**Tong luong */
	public static final String KEY_BSE_LEVEL="BSE_LEVEL";/**Muc BSE dung de tinh tro cap */
	public static final String KEY_TAG1="TAG1";
	public static final String KEY_TAG2="TAG2";
	public static final String KEY_COMPANY_CODE="COMPANY_CODE";
	
	public static final String KEY_IMG_FULLPATH="IMG_FULL_PATH";
	public static final String KEY_ISLABOUR="ISLABOUR";  /** có phải là nhân viên team labour không */
	public static final String KEY_LEARNING_BSE="LEARNING_BSE";  /** hoc BSE/khong hoc BSE/ tam nghi hoc BSE/ */
	
	public static final String KEY_LABOUR_JOIN_DATE="LABOUR_JOIN_DATE";  /** ngay vao team labour  */
	public static final String KEY_LABOUR_OUT_DATE="LABOUR_OUT_DATE";  /** ngay roi team labour  */
	public static final String KEY_BUSINESS_KBN="BUSINESS_KBN"; /** lập trình hay là phiên dịch */
	public static final String KEY_BASIC_DESIGN="BASIC_DESIGN";/** so cong thiet ke co ban */
	public static final String KEY_DETAIL_DESIGN="DETAIL_DESIGN";/** nang suat thiet ke chi tiet */
	public static final String KEY_PROGRAM="PROGRAM";/** so cong lap trinh(nang suat ) */
	//m_user_group_position
	public static final String KEY_USER_CODE="USER_CODE";
	public static final String KEY_POSITION_GROUP_CODE="POSITION_GROUP_CODE";
	//m_user_group_customer
	public static final String KEY_CUSTOMER_GROUP_CODE="CUSTOMER_GROUP_CODE";
	public static final String KEY_CUSTOMER_GROUP_NAME="CUSTOMER_GROUP_NAME";
	public static final String KEY_CUSTOMER_GROUP_RYAKU="CUSTOMER_GROUP_RYAKU";
	//m_user_business
	public static final String KEY_BUSINESS_CODE="BUSINESS_CODE";
	//m_mei
	public static final String KEY_MKBN="MKBN";
	public static final String KEY_NAME="NAME";
	public static final String KEY_RYAKU="RYAKU";
	//m_company
	public static final String KEY_TAX_CODE="TAX_CODE";//Ma so thue
	public static final String KEY_VOUCHER_ADDRESS="VOUCHER_ADDRESS";//Dia chi ghi hoa don do
	public static final String KEY_SHIHON="SHIHON";//Von chu so huu
	public static final String KEY_DIRECTOR="DIRECTOR";//Giam doc
	public static final String KEY_DEPUTY_DIRECTOR="DEPUTY_DIRECTOR";//Pho Giam doc
	public static final String KEY_WEBSITE="WEBSITE";
	
	/** lich su phong ban-nhom-chuc vu*/
	public static final String KEY_DATE_FROM="DATE_FROM";
	public static final String KEY_DATE_TO="DATE_TO";
	public static final String KEY_NEW_DEPT_CODE="NEW_DEPT_CODE";
	public static final String KEY_NEW_TEAM_CODE="NEW_TEAM_CODE";
	public static final String KEY_NEW_POSITION_CODE="NEW_POSITION_CODE";
	public static final String KEY_NEW_JAPANESE="NEW_JAPANESE";
	public static final String KEY_NEW_ALLOWANCE_BUSINESS="NEW_ALLOWANCE_BUSINESS";
	public static final String KEY_NEW_ALLOWANCE_BSE="NEW_ALLOWANCE_BSE";
	public static final String KEY_NEW_SALARY="NEW_SALARY";
	/**
	 * Lan xet luong tiep theo
	 */
	public static final String KEY_SALARY_NEXT_YM="NEXT_YM";
	/**
	 * Ti le nang luong
	 */
	public static final String KEY_SALARY_PERCENT="SALARY_PERCENT";
	/**
	 * Muc tang luong chuan
	 */
	public static final String KEY_SALARY_STANDARD="SALARY_STANDARD";
	/**
	 * Muc tang luong thuc te
	 */
	public static final String KEY_SALARY_ACTUAL_UP="SALARY_ACTUAL_UP";
	
	public static final String KEY_REASON="REASON";
	
	/** template message */
	public static final String KEY_CONTENT="CONTENT";
	public static final String KEY_TELEPHONE="TELEPHONE";
	public static final String KEY_ISVALIDATED="ISVALIDATED";
	/** trạng thái của send message */
	public static final String KEY_SENDER_CODE="SENDER_CODE";
	public static final String KEY_SENDER_PHONE="SENDER_PHONE";
	public static final String KEY_SENDER_MAIL="SENDER_MAIL";
	public static final String KEY_RECEIVER_CODE="RECEIVER_CODE";
	public static final String KEY_RECEIVER_PHONE="RECEIVER_PHONE";
	public static final String KEY_RECEIVER_MAIL="RECEIVER_MAIL";
	public static final String KEY_SEND_PACKAGE="SEND_PACKAGE";
	public static final String KEY_SEND_DATETIME ="SEND_DATETIME";
	public static final String KEY_ISSMS ="ISSMS";
	public static final String KEY_ISEMAIL ="ISEMAIL";
	public static final String KEY_ISACTIVE ="ISACTIVE";
	public static final String KEY_ATTACH_FILE ="ATTACH_FILE";
	public static final String KEY_SEND_STATUS ="SEND_STATUS";
	public static final String KEY_SCHEDULE_SEND_DATETIME="SCHEDULE_SEND_DATETIME";
	public static final String KEY_SCHEDULE_SEND_LOOP="SCHEDULE_SEND_LOOP";
	/** ngay thanh lap*/
	public static final String KEY_CREATE_DATE="CREATE_DATE";

	/** Bang doanh thu theo tung thang , tung khach hang , tung du an */
	public static final String KEY_REVENUE_CUSTOMER_ID="CUSTOMER_ID";
	public static final String KEY_REVENUE_CUSTOMER_NAME="CUSTOMER_NAME";
	public static final String KEY_REVENUE_PJ_ID="PJ_ID";
	public static final String KEY_REVENUE_PJ_NAME="PJ_NAME";
	public static final String KEY_REVENUE_MANAGER="MANAGER";
	public static final String KEY_REVENUE_VICE_MANAGER="VICE_MANAGER";
	public static final String KEY_REVENUE_PM_ID="PM_ID";
	public static final String KEY_REVENUE_PL_ID="PL_ID";
	public static final String KEY_REVENUE_PJ_MEMBER_GROUP="PJ_MEMBER_GROUP";
	public static final String KEY_REVENUE_BASIC_DESIGN="BASIC_DESIGN";
	public static final String KEY_REVENUE_DETAIL_DESIGN="DETAIL_DESIGN";
	public static final String KEY_REVENUE_PG="PG";
	public static final String KEY_REVENUE_UT="UT";
	public static final String KEY_REVENUE_CT="CT";
	public static final String KEY_REVENUE_ST="ST";
	public static final String KEY_REVENUE_MAINTERNANCE="MAINTERNANCE";
	public static final String KEY_REVENUE_START_DATE="START_DATE";
	public static final String KEY_REVENUE_END_DATE="END_DATE";
	public static final String KEY_REVENUE_UNIT_PRICE_CODE="UNIT_PRICE_CODE";
	public static final String KEY_REVENUE_UNIT_PRICE="UNIT_PRICE";
	public static final String KEY_REVENUE_UNIT="UNIT";
	public static final String KEY_REVENUE_RATE_YEN_USD="RATE_YEN_USD";
	public static final String KEY_REVENUE_RATE_YEN_VND="RATE_YEN_VND";
	public static final String KEY_REVENUE_RATE_USD_VND="RATE_USD_VND";
	public static final String KEY_REVENUE_DISCOUNT="DISCOUNT";
	public static final String KEY_REVENUE_MONTHLY_MM="MONTHLY_MM";
	public static final String KEY_REVENUE_ESTIMATE_MM="ESTIMATE_MM";
	public static final String KEY_REVENUE_DEV_MM="DEV_MM";
	public static final String KEY_REVENUE_MANA_MM="MANA_MM";
	public static final String KEY_REVENUE_TRANS_MM="TRANS_MM";
	public static final String KEY_REVENUE_OTHER_MM1="OTHER_MM1";
	public static final String KEY_REVENUE_OTHER_MM2="OTHER_MM2";
	public static final String KEY_REVENUE_MONTHLY_REVENUE="MONTHLY_REVENUE";
	public static final String KEY_REVENUE_MONTHLY_COST="MONTHLY_COST";
	public static final String KEY_REVENUE_MONTHLY_REVENUE_MINUS="MONTHLY_REVENUE_MINUS";

	/** các item dùng chung */
	public static final String KEY_ISDELETED="ISDELETED";
	public static final String KEY_YOBI_CODE1="YOBI_CODE1";
	public static final String KEY_YOBI_CODE2="YOBI_CODE2";
	public static final String KEY_YOBI_CODE3="YOBI_CODE3";
	public static final String KEY_YOBI_CODE4="YOBI_CODE4";
	public static final String KEY_YOBI_CODE5="YOBI_CODE5";
	public static final String KEY_YOBI_TEXT1="YOBI_TEXT1";
	public static final String KEY_YOBI_TEXT2="YOBI_TEXT2";
	public static final String KEY_YOBI_TEXT3="YOBI_TEXT3";
	public static final String KEY_YOBI_TEXT4="YOBI_TEXT4";
	public static final String KEY_YOBI_TEXT5="YOBI_TEXT5";
	public static final String KEY_YOBI_DATE1="YOBI_DATE1";
	public static final String KEY_YOBI_DATE2="YOBI_DATE2";
	public static final String KEY_YOBI_DATE3="YOBI_DATE3";
	public static final String KEY_YOBI_DATE4="YOBI_DATE4";
	public static final String KEY_YOBI_DATE5="YOBI_DATE5";
	public static final String KEY_YOBI_REAL1="YOBI_REAL1";
	public static final String KEY_YOBI_REAL2="YOBI_REAL2";
	public static final String KEY_YOBI_REAL3="YOBI_REAL3";
	public static final String KEY_YOBI_REAL4="YOBI_REAL4";
	public static final String KEY_YOBI_REAL5="YOBI_REAL5";
	public static final String KEY_UP_DATE="UP_DATE";
	public static final String KEY_AD_DATE="AD_DATE";
	public static final String KEY_OPID="OPID";
	public static final String KEY_EXPANDABLE_GROUP="EXPANDABLE_GROUP";

	/**
	 * Chuỗi SQL dùng để get thống kê nghỉ việc theo từng tháng năm
	 */
	public static final String KEY_USER_GROUP_YASUMI_YEARMONTH_SQL="strftime('%m-%Y', "+ KEY_OUT_DATE + ")";
	/**
	 * Chuỗi SQL dùng để get thống kê nghỉ việc theo từng tháng năm
	 */
	public static final String KEY_USER_GROUP_YASUMI_YEARMONTH_SQL_YM="strftime('%Y-%m', "+ KEY_OUT_DATE + ")";
	/**
	 * Chuỗi order by dùng để get thống kê nghỉ việc theo từng tháng năm
	 */
	public static final String KEY_USER_GROUP_YASUMI_YEARMONTH_SORT="strftime('%Y-%m', "+ KEY_OUT_DATE + ")";

	/**
	 * Chuỗi SQL dùng để get thống kê nhận chính thức NV theo từng tháng năm
	 */
	public static final String KEY_USER_GROUP_CONTRACT_YEARMONTH_SQL="strftime('%m-%Y', "+ KEY_IN_DATE + ")";
	/**
	 * Chuỗi SQL dùng để get thống kê nhận chính thức NV theo từng tháng năm
	 */
	public static final String KEY_USER_GROUP_CONTRACT_YEARMONTH_SQL_YM="strftime('%Y-%m', "+ KEY_IN_DATE + ")";
	/**
	 * Chuỗi order by dùng để get thống kê nhận chính thức NV theo từng tháng năm
	 */
	public static final String KEY_USER_GROUP_CONTRACT_YEARMONTH_SORT="strftime('%Y-%m-%d', "+ KEY_IN_DATE + ")";
	
	/**
	 * Chuỗi order by dùng để get thống kê nhận chính thức NV theo từng tháng năm
	 */
	public static final String KEY_USER_GROUP_CONTRACT_YEARMONTH_KEYGROUP="strftime('%m-%Y', "+ KEY_IN_DATE + ")";
	
	public static final String KEY_YEARMONTH="YM";
	public static final String KEY_CNT="CNT";
	public static final String KEY_DATATYPE="DATATYPE";
	public static final String KEY_WHERE_STRING="@WHERE@";
	public static final String KEY_YEARMONTH_STRING="@YEARMONTH@";
	public static final String KEY_MESSAGE_TEMPLATE_CODE="MESSAGE_TEMPLATE_CODE";
	public static final String KEY_MESSAGE_TYPE="MESSAGE_TYPE";
	public static final String KEY_MESSAGE_EMP_CODE="MESSAGE_EMP_CODE";
	
	/**
	 * Loai data su dung de ve chart
	 */
	public enum VIEW_DATATYPE {DEV_YASUMI,PD_YASUMI,DEV_TRAINING,PD_TRAINING,DEV_WORKING,PD_WORKING}
	
	/**
	 * Trang thai cua message
	 */
	public enum MESSAGE_STATUS {NOT_SENT , SENT , SKIP , FAIL}
	
	/** câu lệnh tạo table master nhân viên */
	private final String M_USER_TABLE_CREATE = "create table if not exists "
			+ TABLE_M_USER + "(" 
			+ KEY_CODE 			+ " INTEGER PRIMARY KEY AUTOINCREMENT ,"
			+ KEY_GOOGLE_CONTACT_ID 	+ " TEXT DEFAULT ''		,"
			+ KEY_FIRST_NAME 	+ " TEXT DEFAULT ''		,"
			+ KEY_LAST_NAME 	+ " TEXT DEFAULT ''		,"
			+ KEY_FULL_NAME 	+ " TEXT DEFAULT ''		,"
			+ KEY_SEX 			+ " INTEGER DEFAULT 0	,"
			+ KEY_BIRTHDAY 		+ " TEXT DEFAULT ''		,"
			+ KEY_ADDRESS 		+ " TEXT DEFAULT ''		,"
			+ KEY_HOME_TEL 		+ " TEXT DEFAULT ''		,"
			+ KEY_MOBILE 		+ " TEXT DEFAULT ''		,"
			+ KEY_FAX 			+ " TEXT DEFAULT ''		,"
			+ KEY_POSITION 		+ " INTEGER DEFAULT 0	,"
			+ KEY_POSITION_NAME + " TEXT DEFAULT ''		,"
			+ KEY_DEPT 			+ " INTEGER DEFAULT 0   ,"
			+ KEY_DEPT_NAME 	+ " TEXT DEFAULT ''		,"
			+ KEY_TEAM 			+ " INTEGER DEFAULT 0	,"
			+ KEY_TEAM_NAME 	+ " TEXT DEFAULT ''		,"
			+ KEY_TANT 			+ " INTEGER DEFAULT 0	,"
			+ KEY_TANT_NAME 	+ " TEXT DEFAULT ''		,"
			+ KEY_TRAINING_DATE + " TEXT DEFAULT ''		,"
			+ KEY_TRAINING_DATE_END + " TEXT DEFAULT ''	,"
			+ KEY_IN_DATE 		+ " TEXT DEFAULT ''		,"
			+ KEY_JOIN_DATE 	+ " TEXT DEFAULT ''		,"
			+ KEY_OUT_DATE 		+ " TEXT DEFAULT ''		,"
			+ KEY_MARRIED_DATE	+ " TEXT DEFAULT ''		,"
			+ KEY_INIT_KEIKEN	+ " TEXT DEFAULT ''		,"
			+ KEY_CONVERT_KEIKEN	+ " TEXT DEFAULT ''		,"
			+ KEY_USER_KBN	 	+ " INTEGER DEFAULT 0	,"
			+ KEY_NOTE 			+ " TEXT DEFAULT ''		,"
			+ KEY_IMG 			+ " CLOB,"
			+ KEY_ISDELETED 	+ " INTEGER DEFAULT 0	,"
			
			+ KEY_EMAIL 		+ " TEXT DEFAULT ''		,"
			+ KEY_JAPANESE 		+ " TEXT DEFAULT ''		,"
			+ KEY_ALLOWANCE_BUSINESS 						+ " TEXT DEFAULT ''		,"
			+ KEY_ALLOWANCE_BSE		 						+ " TEXT DEFAULT ''		,"
			+ KEY_ALLOWANCE_ROOM	 						+ " TEXT DEFAULT ''		,"
			+ KEY_MARRIED 									+ " INTEGER DEFAULT 0	,"
			+ KEY_SALARY_NOTALOWANCE 						+ " REAL DEFAULT 0		,"
			+ KEY_SALARY_ALOWANCE 							+ " REAL DEFAULT 0		,"
			+ KEY_TAG1 				+ " TEXT DEFAULT ''	,"
			+ KEY_TAG2 				+ " TEXT DEFAULT ''	,"
			
			+ KEY_IMG_FULLPATH		+ " TEXT DEFAULT ''	,"
			+ KEY_ISLABOUR 			+ " INTEGER DEFAULT 0,"
			+ KEY_LABOUR_JOIN_DATE	+ " TEXT DEFAULT ''	,"
			+ KEY_LABOUR_OUT_DATE	+ " TEXT DEFAULT ''	,"
			+ KEY_BUSINESS_KBN		+ " TEXT DEFAULT '0',"
			+ KEY_BASIC_DESIGN 		+ " REAL DEFAULT 0.0  ,"
			+ KEY_DETAIL_DESIGN 	+ " REAL DEFAULT 0.0  ,"
			+ KEY_PROGRAM 			+ " REAL DEFAULT 0.0  ,"

			+ KEY_GPA 				+ " REAL DEFAULT 0.0  ,"
			+ KEY_GPA_TEXT 			+ " TEXT DEFAULT ''	,"
			+ KEY_COLLECT_NAME		+ " TEXT DEFAULT ''	,"
			+ KEY_STAFF_KBN			+ " TEXT DEFAULT ''	,"

			+ KEY_SUPPORTER1		+ " TEXT DEFAULT ''	,"
			+ KEY_SUPPORTER2		+ " TEXT DEFAULT ''	,"
			+ KEY_SUPPORTER3		+ " TEXT DEFAULT ''	,"

			+ KEY_INTERVIEWER1		+ " TEXT DEFAULT ''	,"
			+ KEY_INTERVIEWER2		+ " TEXT DEFAULT ''	,"
			+ KEY_INTERVIEWER3		+ " TEXT DEFAULT ''	,"
			+ KEY_INTERVIEW_KEKKA	+ " TEXT DEFAULT ''	,"

			+ KEY_YOBI_CODE1 	+ " INTEGER DEFAULT 0 	,"
			+ KEY_YOBI_CODE2 	+ " INTEGER DEFAULT 0	,"
			+ KEY_YOBI_CODE3 	+ " INTEGER DEFAULT 0	,"
			+ KEY_YOBI_CODE4 	+ " INTEGER DEFAULT 0	,"
			+ KEY_YOBI_CODE5 	+ " INTEGER DEFAULT 0	,"
			+ KEY_YOBI_TEXT1 	+ " TEXT DEFAULT ''		,"
			+ KEY_YOBI_TEXT2 	+ " TEXT DEFAULT '' 	,"
			+ KEY_YOBI_TEXT3 	+ " TEXT DEFAULT '' 	,"
			+ KEY_YOBI_TEXT4 	+ " TEXT DEFAULT '' 	,"
			+ KEY_YOBI_TEXT5 	+ " TEXT DEFAULT '' 	,"
			+ KEY_YOBI_DATE1 	+ " TEXT DEFAULT '' 	,"
			+ KEY_YOBI_DATE2 	+ " TEXT DEFAULT '' 	,"
			+ KEY_YOBI_DATE3 	+ " TEXT DEFAULT '' 	,"
			+ KEY_YOBI_DATE4 	+ " TEXT DEFAULT '' 	,"
			+ KEY_YOBI_DATE5 	+ " TEXT DEFAULT '' 	,"
			+ KEY_YOBI_REAL1 	+ " REAL DEFAULT 0  	,"
			+ KEY_YOBI_REAL2 	+ " REAL DEFAULT 0  	,"
			+ KEY_YOBI_REAL3 	+ " REAL DEFAULT 0  	,"
			+ KEY_YOBI_REAL4 	+ " REAL DEFAULT 0  	,"
			+ KEY_YOBI_REAL5 	+ " REAL DEFAULT 0  	,"
			+ KEY_UP_DATE 		+ " TEXT  				,"
			+ KEY_AD_DATE 		+ " TEXT   				,"
			+ KEY_OPID 			+ " TEXT DEFAULT '' "
			+ ")";

	/** câu lệnh tạo table nhóm chức danh*/
	private final String M_USER_GROUP_POSITION_TABLE_CREATE = "create table if not exists "
			+ TABLE_M_USER_POSITION_GROUP + "(" 
			+ KEY_CODE 			+ " INTEGER PRIMARY KEY AUTOINCREMENT ,"
			+ KEY_USER_CODE 	+ " INTEGER NOT NULL 	,"
			+ KEY_POSITION_GROUP_CODE 	+ " INTEGER NOT NULL 	,"
			+ KEY_NAME		 	+ " TEXT DEFAULT ''  	,"
			+ KEY_RYAKU		 	+ " TEXT DEFAULT ''  	,"
			+ KEY_ISDELETED 	+ " INTEGER DEFAULT 0	,"
			+ KEY_NOTE 			+ " TEXT DEFAULT '' 	,"
			+ KEY_YOBI_CODE1 	+ " INTEGER DEFAULT 0 	,"
			+ KEY_YOBI_CODE2 	+ " INTEGER DEFAULT 0	,"
			+ KEY_YOBI_CODE3 	+ " INTEGER DEFAULT 0	,"
			+ KEY_YOBI_CODE4 	+ " INTEGER DEFAULT 0	,"
			+ KEY_YOBI_CODE5 	+ " INTEGER DEFAULT 0	,"
			+ KEY_YOBI_TEXT1 	+ " TEXT DEFAULT ''		,"
			+ KEY_YOBI_TEXT2 	+ " TEXT DEFAULT '' 	,"
			+ KEY_YOBI_TEXT3 	+ " TEXT DEFAULT '' 	,"
			+ KEY_YOBI_TEXT4 	+ " TEXT DEFAULT '' 	,"
			+ KEY_YOBI_TEXT5 	+ " TEXT DEFAULT '' 	,"
			+ KEY_YOBI_DATE1 	+ " TEXT DEFAULT '' 	,"
			+ KEY_YOBI_DATE2 	+ " TEXT DEFAULT '' 	,"
			+ KEY_YOBI_DATE3 	+ " TEXT DEFAULT '' 	,"
			+ KEY_YOBI_DATE4 	+ " TEXT DEFAULT '' 	,"
			+ KEY_YOBI_DATE5 	+ " TEXT DEFAULT '' 	,"
			+ KEY_YOBI_REAL1 	+ " REAL DEFAULT 0  	,"
			+ KEY_YOBI_REAL2 	+ " REAL DEFAULT 0  	,"
			+ KEY_YOBI_REAL3 	+ " REAL DEFAULT 0  	,"
			+ KEY_YOBI_REAL4 	+ " REAL DEFAULT 0  	,"
			+ KEY_YOBI_REAL5 	+ " REAL DEFAULT 0  	,"
			+ KEY_UP_DATE 		+ " TEXT 				,"
			+ KEY_AD_DATE 		+ " TEXT 				,"
			+ KEY_OPID 			+ " TEXT DEFAULT '' "
			+ ")";
	
	/** câu lệnh tạo table khách hàng - user*/
	private final String M_USER_GROUP_CUSTOMER_TABLE_CREATE = "create table if not exists "
			+ TABLE_M_USER_CUSTOMER_GROUP + "(" 
			+ KEY_CODE 			+ " INTEGER PRIMARY KEY AUTOINCREMENT ,"
			+ KEY_USER_CODE 	+ " INTEGER NOT NULL 	,"
			+ KEY_CUSTOMER_GROUP_CODE 	+ " INTEGER NOT NULL 	,"
			+ KEY_NAME		 	+ " TEXT DEFAULT ''  	,"
			+ KEY_RYAKU		 	+ " TEXT DEFAULT ''  	,"
			+ KEY_ISDELETED 	+ " INTEGER DEFAULT 0	,"
			+ KEY_NOTE 			+ " TEXT DEFAULT '' 	,"
			+ KEY_YOBI_CODE1 	+ " INTEGER DEFAULT 0 	,"
			+ KEY_YOBI_CODE2 	+ " INTEGER DEFAULT 0	,"
			+ KEY_YOBI_CODE3 	+ " INTEGER DEFAULT 0	,"
			+ KEY_YOBI_CODE4 	+ " INTEGER DEFAULT 0	,"
			+ KEY_YOBI_CODE5 	+ " INTEGER DEFAULT 0	,"
			+ KEY_YOBI_TEXT1 	+ " TEXT DEFAULT ''		,"
			+ KEY_YOBI_TEXT2 	+ " TEXT DEFAULT '' 	,"
			+ KEY_YOBI_TEXT3 	+ " TEXT DEFAULT '' 	,"
			+ KEY_YOBI_TEXT4 	+ " TEXT DEFAULT '' 	,"
			+ KEY_YOBI_TEXT5 	+ " TEXT DEFAULT '' 	,"
			+ KEY_YOBI_DATE1 	+ " TEXT DEFAULT '' 	,"
			+ KEY_YOBI_DATE2 	+ " TEXT DEFAULT '' 	,"
			+ KEY_YOBI_DATE3 	+ " TEXT DEFAULT '' 	,"
			+ KEY_YOBI_DATE4 	+ " TEXT DEFAULT '' 	,"
			+ KEY_YOBI_DATE5 	+ " TEXT DEFAULT '' 	,"
			+ KEY_YOBI_REAL1 	+ " REAL DEFAULT 0  	,"
			+ KEY_YOBI_REAL2 	+ " REAL DEFAULT 0  	,"
			+ KEY_YOBI_REAL3 	+ " REAL DEFAULT 0  	,"
			+ KEY_YOBI_REAL4 	+ " REAL DEFAULT 0  	,"
			+ KEY_YOBI_REAL5 	+ " REAL DEFAULT 0  	,"
			+ KEY_UP_DATE 		+ " TEXT 				,"
			+ KEY_AD_DATE 		+ " TEXT 				,"
			+ KEY_OPID 			+ " TEXT DEFAULT '' "
			+ ")";
	
	/** câu lệnh tạo table lich su */
	private final String M_USER_HIS_TABLE_CREATE = "create table if not exists "
			+ TABLE_M_USER_HIS + "(" 
			+ KEY_CODE 			+ " INTEGER PRIMARY KEY AUTOINCREMENT ,"
			+ KEY_USER_CODE 	+ " INTEGER NOT NULL 	,"
			+ KEY_MKBN		 	+ " INTEGER NOT NULL 	,"
			+ KEY_NAME		 	+ " TEXT DEFAULT ''  	,"
			+ KEY_RYAKU		 	+ " TEXT DEFAULT ''  	,"
			+ KEY_DATE_FROM		+ " TEXT DEFAULT ''  	,"
			+ KEY_DATE_TO		+ " TEXT DEFAULT ''  	,"
			+ KEY_NEW_DEPT_CODE	+ " INTEGER DEFAULT 0  	,"
			+ KEY_NEW_TEAM_CODE	+ " INTEGER DEFAULT 0  	,"
			+ KEY_NEW_POSITION_CODE		+ " INTEGER DEFAULT 0  	,"
			+ KEY_NEW_JAPANESE	+ " TEXT DEFAULT ''  	,"
			+ KEY_NEW_ALLOWANCE_BUSINESS+ " TEXT DEFAULT ''  	,"
			+ KEY_NEW_ALLOWANCE_BSE+ " TEXT DEFAULT ''  	,"
			+ KEY_REASON		+ " TEXT DEFAULT ''  	,"
			
			+ KEY_NEW_SALARY 		+ "  REAL DEFAULT 0 ,"		
			+ KEY_SALARY_STANDARD 	+ "  REAL DEFAULT 0 ,"							
			+ KEY_SALARY_PERCENT 	+ "  REAL DEFAULT 0 ,"
			+ KEY_SALARY_ACTUAL_UP 	+ "  REAL DEFAULT 0 ,"
			+ KEY_SALARY_NEXT_YM 	+ "  TEXT DEFAULT '',"
							
			+ KEY_ISDELETED 	+ " INTEGER DEFAULT 0	,"
			+ KEY_NOTE 			+ " TEXT DEFAULT '' 	,"
			+ KEY_YOBI_CODE1 	+ " INTEGER DEFAULT 0 	,"
			+ KEY_YOBI_CODE2 	+ " INTEGER DEFAULT 0	,"
			+ KEY_YOBI_CODE3 	+ " INTEGER DEFAULT 0	,"
			+ KEY_YOBI_CODE4 	+ " INTEGER DEFAULT 0	,"
			+ KEY_YOBI_CODE5 	+ " INTEGER DEFAULT 0	,"
			+ KEY_YOBI_TEXT1 	+ " TEXT DEFAULT ''		,"
			+ KEY_YOBI_TEXT2 	+ " TEXT DEFAULT '' 	,"
			+ KEY_YOBI_TEXT3 	+ " TEXT DEFAULT '' 	,"
			+ KEY_YOBI_TEXT4 	+ " TEXT DEFAULT '' 	,"
			+ KEY_YOBI_TEXT5 	+ " TEXT DEFAULT '' 	,"
			+ KEY_YOBI_DATE1 	+ " TEXT DEFAULT '' 	,"
			+ KEY_YOBI_DATE2 	+ " TEXT DEFAULT '' 	,"
			+ KEY_YOBI_DATE3 	+ " TEXT DEFAULT '' 	,"
			+ KEY_YOBI_DATE4 	+ " TEXT DEFAULT '' 	,"
			+ KEY_YOBI_DATE5 	+ " TEXT DEFAULT '' 	,"
			+ KEY_YOBI_REAL1 	+ " REAL DEFAULT 0  	,"
			+ KEY_YOBI_REAL2 	+ " REAL DEFAULT 0  	,"
			+ KEY_YOBI_REAL3 	+ " REAL DEFAULT 0  	,"
			+ KEY_YOBI_REAL4 	+ " REAL DEFAULT 0  	,"
			+ KEY_YOBI_REAL5 	+ " REAL DEFAULT 0  	,"
			+ KEY_UP_DATE 		+ " TEXT 				,"
			+ KEY_AD_DATE 		+ " TEXT 				,"
			+ KEY_OPID 			+ " TEXT DEFAULT '' "
			+ ")";
	
	/** câu lệnh tạo table nhóm chức danh*/
	private final String M_USER_IMAGE_TABLE_CREATE = "create table if not exists "
			+ TABLE_M_USER_IMAGE + "(" 
			+ KEY_CODE 			+ " INTEGER PRIMARY KEY AUTOINCREMENT ,"
			+ KEY_USER_CODE 	+ " INTEGER NOT NULL 	,"
			+ KEY_IMG 			+ " BLOB				,"
			+ KEY_IMG_FULLPATH 	+ " TEXT DEFAULT ''		,"
			+ KEY_NAME		 	+ " TEXT DEFAULT ''  	,"
			+ KEY_RYAKU		 	+ " TEXT DEFAULT ''  	,"
			+ KEY_ISDELETED 	+ " INTEGER DEFAULT 0	,"
			+ KEY_NOTE 			+ " TEXT DEFAULT '' 	,"
			+ KEY_YOBI_CODE1 	+ " INTEGER DEFAULT 0 	,"
			+ KEY_YOBI_CODE2 	+ " INTEGER DEFAULT 0	,"
			+ KEY_YOBI_CODE3 	+ " INTEGER DEFAULT 0	,"
			+ KEY_YOBI_CODE4 	+ " INTEGER DEFAULT 0	,"
			+ KEY_YOBI_CODE5 	+ " INTEGER DEFAULT 0	,"
			+ KEY_YOBI_TEXT1 	+ " TEXT DEFAULT ''		,"
			+ KEY_YOBI_TEXT2 	+ " TEXT DEFAULT '' 	,"
			+ KEY_YOBI_TEXT3 	+ " TEXT DEFAULT '' 	,"
			+ KEY_YOBI_TEXT4 	+ " TEXT DEFAULT '' 	,"
			+ KEY_YOBI_TEXT5 	+ " TEXT DEFAULT '' 	,"
			+ KEY_YOBI_DATE1 	+ " TEXT DEFAULT '' 	,"
			+ KEY_YOBI_DATE2 	+ " TEXT DEFAULT '' 	,"
			+ KEY_YOBI_DATE3 	+ " TEXT DEFAULT '' 	,"
			+ KEY_YOBI_DATE4 	+ " TEXT DEFAULT '' 	,"
			+ KEY_YOBI_DATE5 	+ " TEXT DEFAULT '' 	,"
			+ KEY_YOBI_REAL1 	+ " REAL DEFAULT 0  	,"
			+ KEY_YOBI_REAL2 	+ " REAL DEFAULT 0  	,"
			+ KEY_YOBI_REAL3 	+ " REAL DEFAULT 0  	,"
			+ KEY_YOBI_REAL4 	+ " REAL DEFAULT 0  	,"
			+ KEY_YOBI_REAL5 	+ " REAL DEFAULT 0  	,"
			+ KEY_UP_DATE 		+ " TEXT 				,"
			+ KEY_AD_DATE 		+ " TEXT 				,"
			+ KEY_OPID 			+ " TEXT DEFAULT '' "
			+ ")";
	
	/** câu lệnh tạo table phan loai nghiep vu PROCES.S , he thong cong, */
	private final String M_USER_BUSINESS_TABLE_CREATE = "create table if not exists "
			+ TABLE_M_USER_BUSINESS + "(" 
			+ KEY_CODE 					+ " INTEGER PRIMARY KEY AUTOINCREMENT ,"
			+ KEY_USER_CODE 		+ " INTEGER NOT NULL 	,"
			+ KEY_BUSINESS_CODE 	+ " INTEGER NOT NULL 	,"
			+ KEY_NAME		 	+ " TEXT DEFAULT ''  	,"
			+ KEY_RYAKU		 	+ " TEXT DEFAULT ''  	,"
			+ KEY_ISDELETED 	+ " INTEGER DEFAULT 0	,"
			+ KEY_NOTE 			+ " TEXT DEFAULT '' 	,"
			+ KEY_YOBI_CODE1 	+ " INTEGER DEFAULT 0 	,"
			+ KEY_YOBI_CODE2 	+ " INTEGER DEFAULT 0	,"
			+ KEY_YOBI_CODE3 	+ " INTEGER DEFAULT 0	,"
			+ KEY_YOBI_CODE4 	+ " INTEGER DEFAULT 0	,"
			+ KEY_YOBI_CODE5 	+ " INTEGER DEFAULT 0	,"
			+ KEY_YOBI_TEXT1 	+ " TEXT DEFAULT ''		,"
			+ KEY_YOBI_TEXT2 	+ " TEXT DEFAULT '' 	,"
			+ KEY_YOBI_TEXT3 	+ " TEXT DEFAULT '' 	,"
			+ KEY_YOBI_TEXT4 	+ " TEXT DEFAULT '' 	,"
			+ KEY_YOBI_TEXT5 	+ " TEXT DEFAULT '' 	,"
			+ KEY_YOBI_DATE1 	+ " TEXT DEFAULT '' 	,"
			+ KEY_YOBI_DATE2 	+ " TEXT DEFAULT '' 	,"
			+ KEY_YOBI_DATE3 	+ " TEXT DEFAULT '' 	,"
			+ KEY_YOBI_DATE4 	+ " TEXT DEFAULT '' 	,"
			+ KEY_YOBI_DATE5 	+ " TEXT DEFAULT '' 	,"
			+ KEY_YOBI_REAL1 	+ " REAL DEFAULT 0  	,"
			+ KEY_YOBI_REAL2 	+ " REAL DEFAULT 0  	,"
			+ KEY_YOBI_REAL3 	+ " REAL DEFAULT 0  	,"
			+ KEY_YOBI_REAL4 	+ " REAL DEFAULT 0  	,"
			+ KEY_YOBI_REAL5 	+ " REAL DEFAULT 0  	,"
			+ KEY_UP_DATE 		+ " TEXT 				,"
			+ KEY_AD_DATE 		+ " TEXT 				,"
			+ KEY_OPID 			+ " TEXT DEFAULT '' "
			+ ")";
	
	/** câu lệnh tạo table master dùng chung*/
	private final String M_MEI_TABLE_CREATE = "create table if not exists "
			+ TABLE_M_MEI + "(" 
			+ KEY_CODE 			+ " INTEGER PRIMARY KEY AUTOINCREMENT ,"
			+ KEY_MKBN			+ " INTEGER NOT NULL 	,"
			+ KEY_NAME		 	+ " TEXT DEFAULT ''  	,"
			+ KEY_RYAKU		 	+ " TEXT DEFAULT ''  	,"
			+ KEY_CREATE_DATE 	+ " TEXT DEFAULT ''		,"
			+ KEY_ISDELETED 	+ " INTEGER DEFAULT 0	,"
			+ KEY_NOTE 			+ " TEXT DEFAULT '' 	,"
			+ KEY_YOBI_CODE1 	+ " INTEGER DEFAULT 0 	,"
			+ KEY_YOBI_CODE2 	+ " INTEGER DEFAULT 0	,"
			+ KEY_YOBI_CODE3 	+ " INTEGER DEFAULT 0	,"
			+ KEY_YOBI_CODE4 	+ " INTEGER DEFAULT 0	,"
			+ KEY_YOBI_CODE5 	+ " INTEGER DEFAULT 0	,"
			+ KEY_YOBI_TEXT1 	+ " TEXT DEFAULT ''		,"
			+ KEY_YOBI_TEXT2 	+ " TEXT DEFAULT '' 	,"
			+ KEY_YOBI_TEXT3 	+ " TEXT DEFAULT '' 	,"
			+ KEY_YOBI_TEXT4 	+ " TEXT DEFAULT '' 	,"
			+ KEY_YOBI_TEXT5 	+ " TEXT DEFAULT '' 	,"
			+ KEY_YOBI_DATE1 	+ " TEXT DEFAULT '' 	,"
			+ KEY_YOBI_DATE2 	+ " TEXT DEFAULT '' 	,"
			+ KEY_YOBI_DATE3 	+ " TEXT DEFAULT '' 	,"
			+ KEY_YOBI_DATE4 	+ " TEXT DEFAULT '' 	,"
			+ KEY_YOBI_DATE5 	+ " TEXT DEFAULT '' 	,"
			+ KEY_YOBI_REAL1 	+ " REAL DEFAULT 0  	,"
			+ KEY_YOBI_REAL2 	+ " REAL DEFAULT 0  	,"
			+ KEY_YOBI_REAL3 	+ " REAL DEFAULT 0  	,"
			+ KEY_YOBI_REAL4 	+ " REAL DEFAULT 0  	,"
			+ KEY_YOBI_REAL5 	+ " REAL DEFAULT 0  	,"
			+ KEY_UP_DATE 		+ " TEXT  ,"
			+ KEY_AD_DATE 		+ " TEXT  ,"
			+ KEY_OPID 			+ " TEXT DEFAULT '' "
			+ ")";
	/** câu lệnh tạo table master thong tin cong ty*/
	private final String M_COMPANY_TABLE_CREATE = "create table if not exists "
			+ TABLE_M_COMPANY + "(" 
			+ KEY_CODE 			+ " INTEGER PRIMARY KEY AUTOINCREMENT ,"
			+ KEY_MKBN			+ " INTEGER NOT NULL 	,"
			+ KEY_NAME		 	+ " TEXT DEFAULT ''  	,"
			+ KEY_RYAKU		 	+ " TEXT DEFAULT ''  	,"
			+ KEY_CREATE_DATE 	+ " TEXT DEFAULT '' 	,"
			+ KEY_SHIHON 		+ " REAL DEFAULT 0 		,"
			+ KEY_TAX_CODE 		+ " TEXT DEFAULT ''		,"
			+ KEY_ADDRESS 		+ " TEXT DEFAULT ''		,"
			+ KEY_HOME_TEL 		+ " TEXT DEFAULT ''		,"
			+ KEY_MOBILE 		+ " TEXT DEFAULT ''		,"
			+ KEY_FAX 			+ " TEXT DEFAULT ''		,"
			+ KEY_VOUCHER_ADDRESS 		+ " TEXT DEFAULT ''		,"
			+ KEY_WEBSITE	 	+ " TEXT DEFAULT ''  	,"
			+ KEY_ISDELETED 	+ " INTEGER DEFAULT 0	,"
			+ KEY_NOTE 			+ " TEXT DEFAULT '' 	,"
			+ KEY_YOBI_CODE1 	+ " INTEGER DEFAULT 0 	,"
			+ KEY_YOBI_CODE2 	+ " INTEGER DEFAULT 0	,"
			+ KEY_YOBI_CODE3 	+ " INTEGER DEFAULT 0	,"
			+ KEY_YOBI_CODE4 	+ " INTEGER DEFAULT 0	,"
			+ KEY_YOBI_CODE5 	+ " INTEGER DEFAULT 0	,"
			+ KEY_YOBI_TEXT1 	+ " TEXT DEFAULT ''		,"
			+ KEY_YOBI_TEXT2 	+ " TEXT DEFAULT '' 	,"
			+ KEY_YOBI_TEXT3 	+ " TEXT DEFAULT '' 	,"
			+ KEY_YOBI_TEXT4 	+ " TEXT DEFAULT '' 	,"
			+ KEY_YOBI_TEXT5 	+ " TEXT DEFAULT '' 	,"
			+ KEY_YOBI_DATE1 	+ " TEXT DEFAULT '' 	,"
			+ KEY_YOBI_DATE2 	+ " TEXT DEFAULT '' 	,"
			+ KEY_YOBI_DATE3 	+ " TEXT DEFAULT '' 	,"
			+ KEY_YOBI_DATE4 	+ " TEXT DEFAULT '' 	,"
			+ KEY_YOBI_DATE5 	+ " TEXT DEFAULT '' 	,"
			+ KEY_YOBI_REAL1 	+ " REAL DEFAULT 0  	,"
			+ KEY_YOBI_REAL2 	+ " REAL DEFAULT 0  	,"
			+ KEY_YOBI_REAL3 	+ " REAL DEFAULT 0  	,"
			+ KEY_YOBI_REAL4 	+ " REAL DEFAULT 0  	,"
			+ KEY_YOBI_REAL5 	+ " REAL DEFAULT 0  	,"
			+ KEY_UP_DATE 		+ " TEXT  ,"
			+ KEY_AD_DATE 		+ " TEXT  ,"
			+ KEY_OPID 			+ " TEXT DEFAULT '' "
			+ ")";

	/** câu lệnh tạo table master message*/
	private final String M_MESSAGE_TEMPLATE_TABLE_CREATE = "create table if not exists "
			+ TABLE_M_MESSAGE_TEMPLATE + "(" 
			+ KEY_CODE 			+ " INTEGER PRIMARY KEY AUTOINCREMENT ,"
			+ KEY_MKBN			+ " INTEGER NOT NULL 	,"
			+ KEY_NAME		 	+ " TEXT DEFAULT ''  	,"
			+ KEY_RYAKU		 	+ " TEXT DEFAULT ''  	,"
			+ KEY_CONTENT	 	+ " TEXT DEFAULT ''  	,"
			+ KEY_TELEPHONE	 	+ " TEXT DEFAULT ''  	,"
			+ KEY_EMAIL		 	+ " TEXT DEFAULT ''  	,"
			+ KEY_ISDELETED 	+ " INTEGER DEFAULT 0	,"
			+ KEY_NOTE 			+ " TEXT DEFAULT '' 	,"
			+ KEY_YOBI_CODE1 	+ " INTEGER DEFAULT 0 	,"
			+ KEY_YOBI_CODE2 	+ " INTEGER DEFAULT 0	,"
			+ KEY_YOBI_CODE3 	+ " INTEGER DEFAULT 0	,"
			+ KEY_YOBI_CODE4 	+ " INTEGER DEFAULT 0	,"
			+ KEY_YOBI_CODE5 	+ " INTEGER DEFAULT 0	,"
			+ KEY_YOBI_TEXT1 	+ " TEXT DEFAULT ''		,"
			+ KEY_YOBI_TEXT2 	+ " TEXT DEFAULT '' 	,"
			+ KEY_YOBI_TEXT3 	+ " TEXT DEFAULT '' 	,"
			+ KEY_YOBI_TEXT4 	+ " TEXT DEFAULT '' 	,"
			+ KEY_YOBI_TEXT5 	+ " TEXT DEFAULT '' 	,"
			+ KEY_YOBI_DATE1 	+ " TEXT DEFAULT '' 	,"
			+ KEY_YOBI_DATE2 	+ " TEXT DEFAULT '' 	,"
			+ KEY_YOBI_DATE3 	+ " TEXT DEFAULT '' 	,"
			+ KEY_YOBI_DATE4 	+ " TEXT DEFAULT '' 	,"
			+ KEY_YOBI_DATE5 	+ " TEXT DEFAULT '' 	,"
			+ KEY_YOBI_REAL1 	+ " REAL DEFAULT 0  	,"
			+ KEY_YOBI_REAL2 	+ " REAL DEFAULT 0  	,"
			+ KEY_YOBI_REAL3 	+ " REAL DEFAULT 0  	,"
			+ KEY_YOBI_REAL4 	+ " REAL DEFAULT 0  	,"
			+ KEY_YOBI_REAL5 	+ " REAL DEFAULT 0  	,"
			+ KEY_UP_DATE 		+ " TEXT  ,"
			+ KEY_AD_DATE 		+ " TEXT  ,"
			+ KEY_OPID 			+ " TEXT DEFAULT '' "
			+ ")";
	
	/** câu lệnh tạo table lưu trữ các trạng thái của message */
	private final String M_USER_MESSAGE_STATUS_TABLE_CREATE = "create table if not exists "
			+ TABLE_M_USER_MESSAGE_STATUS + "(" 
			+ KEY_CODE 			+ " INTEGER PRIMARY KEY AUTOINCREMENT ,"
			+ KEY_MESSAGE_TEMPLATE_CODE + "  INTEGER DEFAULT 0 , "
			+ KEY_MESSAGE_EMP_CODE + "  INTEGER DEFAULT 0 , "
			+ KEY_MESSAGE_TYPE + "  INTEGER DEFAULT 0 , "
			+ KEY_SENDER_CODE 	+ " INTEGER DEFAULT 0 	,"
			+ KEY_SENDER_PHONE 	+ " TEXT DEFAULT ''  	,"
			+ KEY_SENDER_MAIL 	+ " TEXT DEFAULT ''  	,"
			+ KEY_RECEIVER_CODE	+ " INTEGER DEFAULT 0 	,"
			+ KEY_RECEIVER_PHONE 	+ " TEXT DEFAULT ''  	,"
			+ KEY_RECEIVER_MAIL 	+ " TEXT DEFAULT ''  	,"
			+ KEY_SEND_PACKAGE	+ " TEXT DEFAULT ''  	,"
			+ KEY_SEND_DATETIME	+ " TEXT DEFAULT ''  	,"
			+ KEY_ISSMS			+ " INTEGER DEFAULT 0 	,"
			+ KEY_ISEMAIL		+ " INTEGER DEFAULT 0 	,"
			+ KEY_ISACTIVE		+ " INTEGER DEFAULT 0 	,"
			+ KEY_ATTACH_FILE	+ " TEXT DEFAULT ''  	,"
			+ KEY_SEND_STATUS	+ " INTEGER DEFAULT 0 	,"
			+ KEY_SCHEDULE_SEND_DATETIME	+ " TEXT DEFAULT ''  	,"
			+ KEY_SCHEDULE_SEND_LOOP		+ " TEXT DEFAULT ''  	,"
			
			+ KEY_NAME		 	+ " TEXT DEFAULT ''  	,"
			+ KEY_RYAKU		 	+ " TEXT DEFAULT ''  	,"
			+ KEY_ISDELETED 	+ " INTEGER DEFAULT 0	,"
			+ KEY_NOTE 			+ " TEXT DEFAULT '' 	,"
			+ KEY_YOBI_CODE1 	+ " INTEGER DEFAULT 0 	,"
			+ KEY_YOBI_CODE2 	+ " INTEGER DEFAULT 0	,"
			+ KEY_YOBI_CODE3 	+ " INTEGER DEFAULT 0	,"
			+ KEY_YOBI_CODE4 	+ " INTEGER DEFAULT 0	,"
			+ KEY_YOBI_CODE5 	+ " INTEGER DEFAULT 0	,"
			+ KEY_YOBI_TEXT1 	+ " TEXT DEFAULT ''		,"
			+ KEY_YOBI_TEXT2 	+ " TEXT DEFAULT '' 	,"
			+ KEY_YOBI_TEXT3 	+ " TEXT DEFAULT '' 	,"
			+ KEY_YOBI_TEXT4 	+ " TEXT DEFAULT '' 	,"
			+ KEY_YOBI_TEXT5 	+ " TEXT DEFAULT '' 	,"
			+ KEY_YOBI_DATE1 	+ " TEXT DEFAULT '' 	,"
			+ KEY_YOBI_DATE2 	+ " TEXT DEFAULT '' 	,"
			+ KEY_YOBI_DATE3 	+ " TEXT DEFAULT '' 	,"
			+ KEY_YOBI_DATE4 	+ " TEXT DEFAULT '' 	,"
			+ KEY_YOBI_DATE5 	+ " TEXT DEFAULT '' 	,"
			+ KEY_YOBI_REAL1 	+ " REAL DEFAULT 0  	,"
			+ KEY_YOBI_REAL2 	+ " REAL DEFAULT 0  	,"
			+ KEY_YOBI_REAL3 	+ " REAL DEFAULT 0  	,"
			+ KEY_YOBI_REAL4 	+ " REAL DEFAULT 0  	,"
			+ KEY_YOBI_REAL5 	+ " REAL DEFAULT 0  	,"
			+ KEY_UP_DATE 		+ " TEXT 				,"
			+ KEY_AD_DATE 		+ " TEXT 				,"
			+ KEY_OPID 			+ " TEXT DEFAULT '' "
			+ ")";


	/** câu lệnh tạo table lưu trữ các trạng thái của message */
	private final String M_USER_SALARY_TABLE_CREATE = "create table if not exists "
			+ TABLE_M_USER_SALARY 				+ "("
			+ KEY_CODE 							+ " INTEGER PRIMARY KEY AUTOINCREMENT ,"
			+ KEY_USER_CODE 					+ " INTEGER NOT NULL 	,"
			+ KEY_NAME		 					+ " TEXT DEFAULT ''  	,"
			+ KEY_RYAKU		 					+ " TEXT DEFAULT ''  	,"

			+KEY_SALARY_BASIC 					+ " REAL DEFAULT 0  	,"
			+KEY_SALARY_CONTRACT				+ " REAL DEFAULT 0  	,"
			+KEY_SALARY_BONUS 					+ " REAL DEFAULT 0  	,"
			+KEY_SALARY_ALLOWANCE_POSITION 		+ " REAL DEFAULT 0  	,"
			+KEY_SALARY_ALLOWANCE_BUSINESS 		+ " REAL DEFAULT 0  	,"
			+KEY_SALARY_ALLOWANCE_WORK 			+ " REAL DEFAULT 0  	,"
			+KEY_SALARY_ALLOWANCE_SUPPORT_FREE 	+ " REAL DEFAULT 0  	,"
			+KEY_SALARY_ALLOWANCE_BSE			+ " REAL DEFAULT 0  	,"
			+KEY_SALARY_ALLOWANCE_ROOM 			+ " REAL DEFAULT 0  	,"
			+KEY_SALARY_ALLOWANCE_JAPANESE 		+ " REAL DEFAULT 0  	,"
			+KEY_SALARY_ALLOWANCE_OT 			+ " REAL DEFAULT 0  	,"
			+KEY_SALARY_ALLOWANCE_MANAGEMENT 	+ " REAL DEFAULT 0  	,"
			+KEY_SALARY_ALLOWANCE_OTHER_1 		+ " REAL DEFAULT 0  	,"
			+KEY_SALARY_ALLOWANCE_OTHER_2 		+ " REAL DEFAULT 0  	,"
			+KEY_SALARY_ALLOWANCE_OTHER_3 		+ " REAL DEFAULT 0  	,"
			+KEY_SALARY_ALLOWANCE_FIX			+ " REAL DEFAULT 0  	,"
			+KEY_SALARY_SUMMARY					+ " REAL DEFAULT 0  	,"
			+KEY_SALARY_ESTIMATE				+ " REAL DEFAULT 0  	,"
			+KEY_SALARY_EFFORT					+ " REAL DEFAULT 0  	,"
			+KEY_SALARY_MINUS_1					+ " REAL DEFAULT 0  	,"
			+KEY_SALARY_MINUS_2					+ " REAL DEFAULT 0  	,"
			+KEY_SALARY_MINUS_3					+ " REAL DEFAULT 0  	,"
			+KEY_SALARY_INCOM_TAX				+ " REAL DEFAULT 0  	,"
			+KEY_SALARY_DEPENDENCY_MINUS		+ " REAL DEFAULT 0  	,"

			+ KEY_ISDELETED 	+ " INTEGER DEFAULT 0	,"
			+ KEY_NOTE 			+ " TEXT DEFAULT '' 	,"
			+ KEY_YOBI_CODE1 	+ " INTEGER DEFAULT 0 	,"
			+ KEY_YOBI_CODE2 	+ " INTEGER DEFAULT 0	,"
			+ KEY_YOBI_CODE3 	+ " INTEGER DEFAULT 0	,"
			+ KEY_YOBI_CODE4 	+ " INTEGER DEFAULT 0	,"
			+ KEY_YOBI_CODE5 	+ " INTEGER DEFAULT 0	,"
			+ KEY_YOBI_TEXT1 	+ " TEXT DEFAULT ''		,"
			+ KEY_YOBI_TEXT2 	+ " TEXT DEFAULT '' 	,"
			+ KEY_YOBI_TEXT3 	+ " TEXT DEFAULT '' 	,"
			+ KEY_YOBI_TEXT4 	+ " TEXT DEFAULT '' 	,"
			+ KEY_YOBI_TEXT5 	+ " TEXT DEFAULT '' 	,"
			+ KEY_YOBI_DATE1 	+ " TEXT DEFAULT '' 	,"
			+ KEY_YOBI_DATE2 	+ " TEXT DEFAULT '' 	,"
			+ KEY_YOBI_DATE3 	+ " TEXT DEFAULT '' 	,"
			+ KEY_YOBI_DATE4 	+ " TEXT DEFAULT '' 	,"
			+ KEY_YOBI_DATE5 	+ " TEXT DEFAULT '' 	,"
			+ KEY_YOBI_REAL1 	+ " REAL DEFAULT 0  	,"
			+ KEY_YOBI_REAL2 	+ " REAL DEFAULT 0  	,"
			+ KEY_YOBI_REAL3 	+ " REAL DEFAULT 0  	,"
			+ KEY_YOBI_REAL4 	+ " REAL DEFAULT 0  	,"
			+ KEY_YOBI_REAL5 	+ " REAL DEFAULT 0  	,"
			+ KEY_UP_DATE 		+ " TEXT 				,"
			+ KEY_AD_DATE 		+ " TEXT 				,"
			+ KEY_OPID 			+ " TEXT DEFAULT '' "
			+ ")";

	/** câu lệnh tạo table lưu trữ doanh thu*/
	private final String T_REVENUE_TABLE_CREATE = "create table if not exists "
			+ TABLE_T_REVENUE				+ "("
			+ KEY_CODE 							+ " INTEGER PRIMARY KEY AUTOINCREMENT ,"

			+KEY_REVENUE_CUSTOMER_NAME + " TEXT DEFAULT ''    ,"
			+KEY_REVENUE_PJ_ID + " TEXT DEFAULT ''  ,"
			+KEY_REVENUE_PJ_NAME + " TEXT DEFAULT ''   ,"
			+KEY_REVENUE_MANAGER + " TEXT DEFAULT ''   ,"
			+KEY_REVENUE_VICE_MANAGER + " TEXT DEFAULT ''   ,"
			+KEY_REVENUE_PM_ID + " TEXT DEFAULT ''   ,"
			+KEY_REVENUE_PL_ID + " TEXT DEFAULT ''  ,"
			+KEY_REVENUE_PJ_MEMBER_GROUP + " TEXT DEFAULT ''   ,"
			+KEY_REVENUE_BASIC_DESIGN + " TEXT DEFAULT ''   ,"
			+KEY_REVENUE_DETAIL_DESIGN + " TEXT DEFAULT ''    ,"
			+KEY_REVENUE_PG + " TEXT DEFAULT ''    ,"
			+KEY_REVENUE_UT + " TEXT DEFAULT ''    ,"
			+KEY_REVENUE_CT + " TEXT DEFAULT ''    ,"
			+KEY_REVENUE_ST + " TEXT DEFAULT ''    ,"
			+KEY_REVENUE_MAINTERNANCE + " TEXT DEFAULT ''  ,"
			+KEY_REVENUE_START_DATE + " TEXT DEFAULT ''    ,"
			+KEY_REVENUE_END_DATE + " TEXT DEFAULT ''    ,"
			+KEY_REVENUE_UNIT_PRICE_CODE + " TEXT DEFAULT ''    ,"
			+KEY_REVENUE_UNIT_PRICE + " REAL DEFAULT 0   ,"
			+KEY_REVENUE_UNIT + " TEXT DEFAULT ''    ,"
			+KEY_REVENUE_RATE_YEN_USD + " REAL DEFAULT 0   ,"
			+KEY_REVENUE_RATE_YEN_VND + " REAL DEFAULT 0   ,"
			+KEY_REVENUE_RATE_USD_VND + " REAL DEFAULT 0   ,"
			+KEY_REVENUE_DISCOUNT + " REAL DEFAULT 0   ,"
			+KEY_REVENUE_MONTHLY_MM + " REAL DEFAULT 0   ,"
			+KEY_REVENUE_ESTIMATE_MM + " REAL DEFAULT 0   ,"
			+KEY_REVENUE_DEV_MM + " REAL DEFAULT 0   ,"
			+KEY_REVENUE_MANA_MM + " REAL DEFAULT 0   ,"
			+KEY_REVENUE_TRANS_MM + " REAL DEFAULT 0   ,"
			+KEY_REVENUE_OTHER_MM1 + " REAL DEFAULT 0   ,"
			+KEY_REVENUE_OTHER_MM2 + " REAL DEFAULT 0   ,"
			+KEY_REVENUE_MONTHLY_REVENUE + " REAL DEFAULT 0   ,"
			+KEY_REVENUE_MONTHLY_COST + " REAL DEFAULT 0   ,"
			+KEY_REVENUE_MONTHLY_REVENUE_MINUS + " REAL DEFAULT 0   ,"

			+ KEY_ISDELETED 	+ " INTEGER DEFAULT 0	,"
			+ KEY_NOTE 			+ " TEXT DEFAULT '' 	,"
			+ KEY_YOBI_CODE1 	+ " INTEGER DEFAULT 0 	,"
			+ KEY_YOBI_CODE2 	+ " INTEGER DEFAULT 0	,"
			+ KEY_YOBI_CODE3 	+ " INTEGER DEFAULT 0	,"
			+ KEY_YOBI_CODE4 	+ " INTEGER DEFAULT 0	,"
			+ KEY_YOBI_CODE5 	+ " INTEGER DEFAULT 0	,"
			+ KEY_YOBI_TEXT1 	+ " TEXT DEFAULT ''		,"
			+ KEY_YOBI_TEXT2 	+ " TEXT DEFAULT '' 	,"
			+ KEY_YOBI_TEXT3 	+ " TEXT DEFAULT '' 	,"
			+ KEY_YOBI_TEXT4 	+ " TEXT DEFAULT '' 	,"
			+ KEY_YOBI_TEXT5 	+ " TEXT DEFAULT '' 	,"
			+ KEY_YOBI_DATE1 	+ " TEXT DEFAULT '' 	,"
			+ KEY_YOBI_DATE2 	+ " TEXT DEFAULT '' 	,"
			+ KEY_YOBI_DATE3 	+ " TEXT DEFAULT '' 	,"
			+ KEY_YOBI_DATE4 	+ " TEXT DEFAULT '' 	,"
			+ KEY_YOBI_DATE5 	+ " TEXT DEFAULT '' 	,"
			+ KEY_YOBI_REAL1 	+ " REAL DEFAULT 0  	,"
			+ KEY_YOBI_REAL2 	+ " REAL DEFAULT 0  	,"
			+ KEY_YOBI_REAL3 	+ " REAL DEFAULT 0  	,"
			+ KEY_YOBI_REAL4 	+ " REAL DEFAULT 0  	,"
			+ KEY_YOBI_REAL5 	+ " REAL DEFAULT 0  	,"
			+ KEY_UP_DATE 		+ " TEXT 				,"
			+ KEY_AD_DATE 		+ " TEXT 				,"
			+ KEY_OPID 			+ " TEXT DEFAULT '' "
			+ ")";
	/** câu lệnh tạo view  user customer group */
	private final String VIEW_M_USER_CUSTOMER_GROUP_VIEW_CREATE = "create view if not exists "
			+ VIEW_M_USER_CUSTOMER_GROUP + " AS "
			+" SELECT DISTINCT " 
			+"		USR.*" 
		    +"	,	GRP." + KEY_CUSTOMER_GROUP_CODE + "" 
     		+"	,	GRP." + KEY_NAME + " AS " + KEY_CUSTOMER_GROUP_NAME + " " 
			+"	FROM " + TABLE_M_USER + " USR LEFT OUTER JOIN " + TABLE_M_USER_CUSTOMER_GROUP + " GRP" 
			+" 		ON USR." + KEY_CODE + "=GRP." + KEY_USER_CODE + ""; 

	/** tao view lay so lap trinh vien nghi viec theo tung thang */
	private final String VIEW_DEV_YASUMI_COUNT_BY_YEARMONTH_SQL = ""
			+ " SELECT " 
			+ "		strftime('%Y-%m'," + KEY_OUT_DATE + ") " +  KEY_YEARMONTH 
			+ " ,	count(*) " 	+ KEY_CNT 
			+ " ,	" + VIEW_DATATYPE.DEV_YASUMI.ordinal() +	" "	+ KEY_DATATYPE 
			+ "	FROM " + TABLE_M_USER  
			+ "	where out_date <> '' "
			+ "	and isdeleted =0 " + System.getProperty("line.separator")
			+ "	and business_kbn <> 2 --loai tru phien dich " + System.getProperty("line.separator")
			+ "	---and ( out_date ='' or (out_date <>'' and strftime('%Y-%m',out_date) >'2015-10' )) "+ System.getProperty("line.separator")
			+ " " + KEY_WHERE_STRING 
			+ "	group by strftime('%Y-%m'," + KEY_OUT_DATE + ") ";
	
	/** tao view lay so phien dich nghi viec trong tung thang */
	private final String VIEW_PD_YASUMI_COUNT_BY_YEARMONTH_SQL = ""
			+ " SELECT "
			+ "		strftime('%Y-%m'," + KEY_OUT_DATE + ") " +  KEY_YEARMONTH
			+ " ,	count(*) " 	+ KEY_CNT
			+ " ,	" + VIEW_DATATYPE.PD_YASUMI.ordinal() +	" "	+ KEY_DATATYPE 
			+ "	FROM " + TABLE_M_USER 
			+ "	where out_date <> '' "
			+ "	and isdeleted =0 " + System.getProperty("line.separator")
			+ "	and business_kbn = 2 --loai tru phien dich  \r\n" + System.getProperty("line.separator")
			+ "	---and ( out_date ='' or (out_date <>'' and strftime('%Y-%m',out_date) >'2015-10' )) " + System.getProperty("line.separator")
			+ " " + KEY_WHERE_STRING 
			+ "	group by strftime('%Y-%m'," + KEY_OUT_DATE + ") ";
	
	/** tao view lay so lap trinh vien thu viec theo tung thang*/
	private final String VIEW_DEV_TRAINING_COUNT_BY_YEARMONTH_SQL = ""
			+ " SELECT "
			+	KEY_YEARMONTH
			+ " , SUM(" + KEY_CNT + ")" +  KEY_CNT
			+ " , " + VIEW_DATATYPE.DEV_TRAINING.ordinal() + "  " +  KEY_DATATYPE
			+ " FROM "
			+ " ( "
			+ " SELECT "
			+ "		'" + KEY_YEARMONTH_STRING + "' " + KEY_YEARMONTH+ System.getProperty("line.separator")
			+ " ,	count(*) " 	+ KEY_CNT
			+ " ,	" + VIEW_DATATYPE.DEV_TRAINING.ordinal() +	" "	+ KEY_DATATYPE 
			+ "	FROM " + TABLE_M_USER 
			+ "	where  "
			+ "	isdeleted =0 " + System.getProperty("line.separator")
			+ "	and business_kbn <> 2 --loai tru phien dich " + System.getProperty("line.separator")
			+ " and ( " + KEY_OUT_DATE + " ='' or (" + KEY_OUT_DATE + " <>'' and strftime('%Y-%m'," + KEY_OUT_DATE + ") >='"+ KEY_YEARMONTH_STRING + "' ))  "+ System.getProperty("line.separator")
			+ "	and (strftime('%Y-%m'," + KEY_TRAINING_DATE + ") ='"+ KEY_YEARMONTH_STRING +"' and ( strftime('%Y-%m'," + KEY_TRAINING_DATE_END + ") >='"+ KEY_YEARMONTH_STRING +"' or "+ KEY_TRAINING_DATE_END + " =''  ))"
			+ "	and (" + KEY_IN_DATE + " ='' or strftime('%Y-%m'," + KEY_IN_DATE + ") >='" + KEY_YEARMONTH_STRING + "')"
			+ " " + KEY_WHERE_STRING 
			+ "	group by strftime('%Y-%m'," + KEY_TRAINING_DATE + ") "
			+ " ) " ;
	
	/** tao view lay so lap trinh vien chinh thuc theo tung thang*/
	private final String VIEW_DEV_CONTRACT_COUNT_BY_YEARMONTH_SQL = ""
					+ " SELECT "
					+	KEY_YEARMONTH
					+ " , SUM(" + KEY_CNT + ")" +  KEY_CNT
					+ " , " + VIEW_DATATYPE.DEV_WORKING.ordinal() + "  " +  KEY_DATATYPE
					+ " FROM "
					+ " ( "+ System.getProperty("line.separator")
					+ " --lay so nhan vien lap trinh chinh thuc "+ System.getProperty("line.separator")

					+ " SELECT '" + KEY_YEARMONTH_STRING + "' " + KEY_YEARMONTH + System.getProperty("line.separator")
					+ " , COUNT(*) " + KEY_CNT + "  "+ System.getProperty("line.separator")
					+ " from " + TABLE_M_USER +  System.getProperty("line.separator")
					+ " where strftime('%Y-%m', " + KEY_IN_DATE + ") ='" + KEY_YEARMONTH_STRING + "'"+ System.getProperty("line.separator")
					+ " and isdeleted =0  "+ System.getProperty("line.separator")
					+ " and business_kbn <> 2 "+ System.getProperty("line.separator")
					//+ " and ( " + KEY_OUT_DATE + " ='' or (" + KEY_OUT_DATE + " <>'' and strftime('%Y-%m'," + KEY_OUT_DATE + ") >'"+ KEY_YEARMONTH_STRING + "' ))  "+ System.getProperty("line.separator")
					+ " " + KEY_WHERE_STRING
					+ " group by strftime('%Y-%m',"+ KEY_IN_DATE + ") "
					+ " ) ";
	
	/** tao view lay so phien dich thu viec theo tung thang*/
	private final String VIEW_PD_TRAINING_COUNT_BY_YEARMONTH_SQL = ""
			+ " SELECT "
			+	KEY_YEARMONTH
			+ " , SUM(" + KEY_CNT + ")" +  KEY_CNT
			+ " , " + VIEW_DATATYPE.DEV_TRAINING.ordinal() + "  " +  KEY_DATATYPE
			+ " FROM "
			+ " ( "
			+ " SELECT "
			+ "		'" + KEY_YEARMONTH_STRING + "' " + KEY_YEARMONTH+ System.getProperty("line.separator")
			+ " ,	count(*) " 	+ KEY_CNT
			+ " ,	" + VIEW_DATATYPE.DEV_TRAINING.ordinal() +	" "	+ KEY_DATATYPE 
			+ "	FROM " + TABLE_M_USER 
			+ "	where  "
			+ "	isdeleted =0 " + System.getProperty("line.separator")
			+ "	and business_kbn = 2 --loai tru phien dich " + System.getProperty("line.separator")
			+ " and ( " + KEY_OUT_DATE + " ='' or (" + KEY_OUT_DATE + " <>'' and strftime('%Y-%m'," + KEY_OUT_DATE + ") >'"+ KEY_YEARMONTH_STRING + "' ))  "+ System.getProperty("line.separator")
			+ "	and (strftime('%Y-%m'," + KEY_TRAINING_DATE + ") <='"+ KEY_YEARMONTH_STRING +"' and strftime('%Y-%m'," + KEY_TRAINING_DATE_END + ") >='"+ KEY_YEARMONTH_STRING +"' )"
			+ "	and (" + KEY_IN_DATE + " ='' or strftime('%Y-%m'," + KEY_IN_DATE + ") >'" + KEY_YEARMONTH_STRING + "')"
			+ " " + KEY_WHERE_STRING 
			+ "	group by strftime('%Y-%m'," + KEY_TRAINING_DATE + ") "
			+ " ) " ;


	/** tao view lay ve LTV co ngach khong tuong xung voi tham nien*/
	public static final String VIEW_USER_LIST_CURRENT_POSITION_NOT_SATIFIED_SQL = ""
			+ "	(SELECT USR.* , TMP.NEW_POSITION_CODE, TMP.NEW_POSITION_RYAKU ,POSITION_LEVEL ,  NEW_POSITION_LEVEL"
			+ "	FROM"
			+ "	("
			+ "		SELECT"
			+ "			CODE, MAX(CV) , NEW_POSITION_CODE ,NEW_POSITION_RYAKU,POSITION_LEVEL ,  NEW_POSITION_LEVEL"
			+ "		FROM "
			+ "		("
			+ "			SELECT"
			+ "				A.*"
			+ "			,	(A.YOBI_REAL1*12)				KEIKEN"
			+ "			,	MEI.YOBI_CODE1					POSITION_LEVEL"
			+ "			,	CAST(B.YOBI_TEXT2 AS DECIMAL)	CV"
			+ "			,	B.CODE							NEW_POSITION_CODE"
			+ "			,	B.YOBI_TEXT1					NEW_POSITION_RYAKU"
			+ "			,	B.YOBI_CODE1					NEW_POSITION_LEVEL"
			+ "			FROM"
			+ "				M_USER A"
			+ "				LEFT OUTER JOIN ( SELECT * FROM M_MEI WHERE MKBN=3 AND CAST(YOBI_TEXT2 AS DECIMAL) >0 ) MEI"
			+ "				ON (A.POSITION  =  MEI.CODE)	"
			+ "				CROSS JOIN ( SELECT * FROM M_MEI WHERE MKBN=3 AND CAST(YOBI_TEXT2 AS DECIMAL) >0 ) B"
			+ "				ON (A.YOBI_REAL1*12)  > CAST(B.YOBI_TEXT2 AS DECIMAL)	"
			+ "			WHERE"
			+ "				MEI.YOBI_CODE1 < B.YOBI_CODE1"
			+ "		) TMP"

			+ "		GROUP BY TMP.CODE"
			+ "	) TMP LEFT OUTER JOIN M_USER USR ON TMP.CODE = USR.CODE WHERE POSITION_NAME <> NEW_POSITION_RYAKU )";


	private SQLiteDatabase db;
	private MyCreateOpenHelper createOpenHelper;
	
	/**
	 * 
	 * @param context
	 */
	
	public DatabaseAdapter(Context context) {
		this.context = context;
		createOpenHelper = new MyCreateOpenHelper(context);
	}
	/**
	 * 
	 * @return
	 * @throws SQLException
	 */
	public DatabaseAdapter open() throws SQLException {
		db = createOpenHelper.getWritableDatabase();
		return this;
	}
	/**
	 * 
	 */
	public void close() {
		db.close();
	}
	/**
	 * 
	 */
	protected void dropM_UserTable() {
		db.execSQL("drop table " + TABLE_M_USER);
	}
	/**
	 * 
	 */
	protected void dropM_MeiTable() {
		db.execSQL("drop table " + TABLE_M_MEI);
	}
	
	/**
	 * 
	 * @param list tại mới
	 * @return
	 */
	public long insertToMasterTable(Master list) {
		try{
			ContentValues contentValues = new ContentValues();
			//contentValues.put(KEY_ID, list.id); tạm thời không sử dụng item này 
			contentValues.put(KEY_MKBN, list.mkbn);
			//contentValues.put(KEY_CODE, list.code);
			contentValues.put(KEY_NAME, list.name);
			contentValues.put(KEY_RYAKU, list.ryaku);
			if (list.create_date != null) {
				contentValues.put(KEY_CREATE_DATE, DateTimeUtil.formatDate2String(list.create_date , MasterConstants.DATE_VN_FORMAT, MasterConstants.DATE_JP_FORMAT));
			}
			else {
				contentValues.put(KEY_CREATE_DATE, list.create_date);
			}

			contentValues.put(KEY_ISDELETED, list.isdeleted);
			contentValues.put(KEY_NOTE, list.note);
			
			contentValues.put(KEY_YOBI_CODE1, list.yobi_code1);
			contentValues.put(KEY_YOBI_CODE2, list.yobi_code2);
			contentValues.put(KEY_YOBI_CODE3, list.yobi_code3);
			contentValues.put(KEY_YOBI_CODE4, list.yobi_code4);
			contentValues.put(KEY_YOBI_CODE5, list.yobi_code5);
			contentValues.put(KEY_YOBI_TEXT1, list.yobi_text1);
			contentValues.put(KEY_YOBI_TEXT2, list.yobi_text2);
			contentValues.put(KEY_YOBI_TEXT3, list.yobi_text3);
			contentValues.put(KEY_YOBI_TEXT4, list.yobi_text4);
			contentValues.put(KEY_YOBI_TEXT5, list.yobi_text5);
			contentValues.put(KEY_YOBI_DATE1, list.yobi_date1);
			contentValues.put(KEY_YOBI_DATE2, list.yobi_date2);
			contentValues.put(KEY_YOBI_DATE3, list.yobi_date3);
			contentValues.put(KEY_YOBI_DATE4, list.yobi_date4);
			contentValues.put(KEY_YOBI_DATE5, list.yobi_date5);
			contentValues.put(KEY_YOBI_REAL1, list.yobi_real1);
			contentValues.put(KEY_YOBI_REAL2, list.yobi_real2);
			contentValues.put(KEY_YOBI_REAL3, list.yobi_real3);
			contentValues.put(KEY_YOBI_REAL4, list.yobi_real4);
			contentValues.put(KEY_YOBI_REAL5, list.yobi_real5);
			contentValues.put(KEY_UP_DATE, Utils.getCurrentDateTimeToString());
			contentValues.put(KEY_AD_DATE, Utils.getCurrentDateTimeToString());
			contentValues.put(KEY_OPID, list.opid);

			Log.d("TABLE_M_MEI TRYING");
			//long id = db.insert(TABLE_M_MEI, null, contentValues);
			long id = db.insertOrThrow(TABLE_M_MEI, null, contentValues);
			Log.d("TABLE_M_MEI ADDED");
			return id;
		}catch (Exception e){
			e.getMessage();
			return -1;//error
		}
		
	}
	/**
	 * 
	 * @param list : cập nhận thông tin 
	 * @return
	 */
	public boolean editMasterTable(Master list) {
		ContentValues contentValues = new ContentValues();
		contentValues.put(KEY_MKBN, list.mkbn);
		if (list.name != null){
			contentValues.put(KEY_NAME, list.name);
		}
		if (list.ryaku != null){
			contentValues.put(KEY_RYAKU, list.ryaku);
		}
		/** delete hay chưa */
		if (String.valueOf(list.isdeleted) != null){
			contentValues.put(KEY_ISDELETED, list.isdeleted);
		}
		if (list.create_date != null){
			contentValues.put(KEY_CREATE_DATE, DateTimeUtil.formatDate2String(list.create_date , MasterConstants.DATE_VN_FORMAT, MasterConstants.DATE_JP_FORMAT));
		}
		if (list.note != null){
			contentValues.put(KEY_NOTE, list.note);
		}
		/*contentValues.put(KEY_YOBI_CODE1, list.yobi_code1);
		contentValues.put(KEY_YOBI_CODE2, list.yobi_code2);
		contentValues.put(KEY_YOBI_CODE3, list.yobi_code3);
		contentValues.put(KEY_YOBI_CODE4, list.yobi_code4);
		contentValues.put(KEY_YOBI_CODE5, list.yobi_code5);
		contentValues.put(KEY_YOBI_TEXT1, list.yobi_text1);
		contentValues.put(KEY_YOBI_TEXT2, list.yobi_text2);
		contentValues.put(KEY_YOBI_TEXT3, list.yobi_text3);
		contentValues.put(KEY_YOBI_TEXT4, list.yobi_text4);
		contentValues.put(KEY_YOBI_TEXT5, list.yobi_text5);
		contentValues.put(KEY_YOBI_DATE1, list.yobi_date1);
		contentValues.put(KEY_YOBI_DATE2, list.yobi_date2);
		contentValues.put(KEY_YOBI_DATE3, list.yobi_date3);
		contentValues.put(KEY_YOBI_DATE4, list.yobi_date4);
		contentValues.put(KEY_YOBI_DATE5, list.yobi_date5);
		contentValues.put(KEY_YOBI_REAL1, list.yobi_real1);
		contentValues.put(KEY_YOBI_REAL2, list.yobi_real2);
		contentValues.put(KEY_YOBI_REAL3, list.yobi_real3);
		contentValues.put(KEY_YOBI_REAL4, list.yobi_real4);
		contentValues.put(KEY_YOBI_REAL5, list.yobi_real5);
		contentValues.put(KEY_UP_DATE, list.up_date);
		contentValues.put(KEY_AD_DATE, list.ad_date);
		contentValues.put(KEY_OPID, list.opid);*/
		
		if (String.valueOf(list.yobi_code1) != null){
			contentValues.put(KEY_YOBI_CODE1, list.yobi_code1);
		}
		
		if (String.valueOf(list.yobi_code2) != null){
			contentValues.put(KEY_YOBI_CODE2, list.yobi_code2);
		}
		
		if (String.valueOf(list.yobi_code3) != null){
			contentValues.put(KEY_YOBI_CODE3, list.yobi_code3);
		}
		
		if (String.valueOf(list.yobi_code4) != null){
			contentValues.put(KEY_YOBI_CODE4, list.yobi_code4);
		}
		
		if (String.valueOf(list.yobi_code5 )!= null){
			contentValues.put(KEY_YOBI_CODE5, list.yobi_code5);
		}
		
		if (list.yobi_text1 != null){
			contentValues.put(KEY_YOBI_TEXT1, list.yobi_text1);
		}

		if (list.yobi_text2 != null){
			contentValues.put(KEY_YOBI_TEXT2, list.yobi_text2);
		}
		
		if (list.yobi_text3 != null){
			contentValues.put(KEY_YOBI_TEXT3, list.yobi_text3);
		}

		if (list.yobi_text4 != null){
			contentValues.put(KEY_YOBI_TEXT4, list.yobi_text4);
		}

		if (list.yobi_text5 != null){
			contentValues.put(KEY_YOBI_TEXT5, list.yobi_text5);
		}

		if (list.yobi_date1 != null){
			contentValues.put(KEY_YOBI_DATE1, list.yobi_date1);
		}

		if (list.yobi_date2 != null){
			contentValues.put(KEY_YOBI_DATE2, list.yobi_date2);
		}

		if (list.yobi_date3 != null){
			contentValues.put(KEY_YOBI_DATE3, list.yobi_date3);
		}

		if (list.yobi_date4 != null){
			contentValues.put(KEY_YOBI_DATE4, list.yobi_date4);
		}

		if (list.yobi_date5 != null){
			contentValues.put(KEY_YOBI_DATE5, list.yobi_date5);
		}

		if (String.valueOf(list.yobi_real1) != null){
			contentValues.put(KEY_YOBI_REAL1, list.yobi_real1);
		}

		if (String.valueOf(list.yobi_real2) != null){
			contentValues.put(KEY_YOBI_REAL2, list.yobi_real2);
		}

		if (String.valueOf(list.yobi_real3) != null){
			contentValues.put(KEY_YOBI_REAL3, list.yobi_real3);
		}

		if (String.valueOf(list.yobi_real4) != null){
			contentValues.put(KEY_YOBI_REAL4, list.yobi_real4);
		}
		
		
		if (String.valueOf(list.yobi_real5 )!= null){
			contentValues.put(KEY_YOBI_REAL5, list.yobi_real5);
		}
		
		//if (list.up_date != null){
			contentValues.put(KEY_UP_DATE, Utils.getCurrentDateTimeToString());
		//}

		if (list.opid != null){
			contentValues.put(KEY_OPID, list.opid);
		}
		/** điều kiện để update */
		String where = KEY_CODE + "=" + list.code + " AND " + KEY_MKBN + " = " + list.mkbn ;
		try {
			Log.d("EDITING");
			db.update(TABLE_M_MEI, contentValues, where, null);
			Log.d("EDITED");
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * Insert data vao table message 
	 * @param list tại mới
	 * @return
	 */
	public long insertToMessageTemplateTable(MessageTemplate list) {
		try{
			ContentValues contentValues = new ContentValues();
			//contentValues.put(KEY_ID, list.id); tạm thời không sử dụng item này 
			contentValues.put(KEY_MKBN, list.mkbn);
			//contentValues.put(KEY_CODE, list.code);
			contentValues.put(KEY_NAME, list.name);
			contentValues.put(KEY_RYAKU, list.ryaku);
			contentValues.put(KEY_CONTENT, list.content);
			contentValues.put(KEY_TELEPHONE, list.telephone);
			contentValues.put(KEY_EMAIL, list.email);

			contentValues.put(KEY_ISDELETED, list.isdeleted);
			contentValues.put(KEY_NOTE, list.note);
			
			contentValues.put(KEY_YOBI_CODE1, list.yobi_code1);
			contentValues.put(KEY_YOBI_CODE2, list.yobi_code2);
			contentValues.put(KEY_YOBI_CODE3, list.yobi_code3);
			contentValues.put(KEY_YOBI_CODE4, list.yobi_code4);
			contentValues.put(KEY_YOBI_CODE5, list.yobi_code5);
			contentValues.put(KEY_YOBI_TEXT1, list.yobi_text1);
			contentValues.put(KEY_YOBI_TEXT2, list.yobi_text2);
			contentValues.put(KEY_YOBI_TEXT3, list.yobi_text3);
			contentValues.put(KEY_YOBI_TEXT4, list.yobi_text4);
			contentValues.put(KEY_YOBI_TEXT5, list.yobi_text5);
			contentValues.put(KEY_YOBI_DATE1, list.yobi_date1);
			contentValues.put(KEY_YOBI_DATE2, list.yobi_date2);
			contentValues.put(KEY_YOBI_DATE3, list.yobi_date3);
			contentValues.put(KEY_YOBI_DATE4, list.yobi_date4);
			contentValues.put(KEY_YOBI_DATE5, list.yobi_date5);
			contentValues.put(KEY_YOBI_REAL1, list.yobi_real1);
			contentValues.put(KEY_YOBI_REAL2, list.yobi_real2);
			contentValues.put(KEY_YOBI_REAL3, list.yobi_real3);
			contentValues.put(KEY_YOBI_REAL4, list.yobi_real4);
			contentValues.put(KEY_YOBI_REAL5, list.yobi_real5);
			contentValues.put(KEY_UP_DATE, Utils.getCurrentDateTimeToString());
			contentValues.put(KEY_AD_DATE, Utils.getCurrentDateTimeToString());
			contentValues.put(KEY_OPID, list.opid);

			Log.d("TABLE_M_MESSAGE_TEMPLATE TRYING");
			//long id = db.insert(TABLE_M_MEI, null, contentValues);
			long id = db.insertOrThrow(TABLE_M_MESSAGE_TEMPLATE, null, contentValues);
			Log.d("TABLE_M_MESSAGE_TEMPLATE ADDED");
			return id;
		}catch (Exception e){
			e.getMessage();
			return -1;//error
		}
		
	}
	
	/**
	 * Cap nhat message template
	 * @param list : cập nhận thông tin 
	 * @return
	 */
	public boolean editMessageTemplateTable(MessageTemplate list) {
		ContentValues contentValues = new ContentValues();
		contentValues.put(KEY_MKBN, list.mkbn);
		if (list.name != null){
			contentValues.put(KEY_NAME, list.name);
		}
		if (list.ryaku != null){
			contentValues.put(KEY_RYAKU, list.ryaku);
		}
		if (list.content!= null){
			contentValues.put(KEY_CONTENT, list.content);
		}
		
		if (list.telephone!= null){
			contentValues.put(KEY_TELEPHONE, list.telephone);
		}
		if (list.email!= null){
			contentValues.put(KEY_EMAIL, list.email);
		}
		/** delete hay chưa */
		if (String.valueOf(list.isdeleted) != null){
			contentValues.put(KEY_ISDELETED, list.isdeleted);
		}
		if (list.note != null){
			contentValues.put(KEY_NOTE, list.note);
		}
		/*contentValues.put(KEY_YOBI_CODE1, list.yobi_code1);
		contentValues.put(KEY_YOBI_CODE2, list.yobi_code2);
		contentValues.put(KEY_YOBI_CODE3, list.yobi_code3);
		contentValues.put(KEY_YOBI_CODE4, list.yobi_code4);
		contentValues.put(KEY_YOBI_CODE5, list.yobi_code5);
		contentValues.put(KEY_YOBI_TEXT1, list.yobi_text1);
		contentValues.put(KEY_YOBI_TEXT2, list.yobi_text2);
		contentValues.put(KEY_YOBI_TEXT3, list.yobi_text3);
		contentValues.put(KEY_YOBI_TEXT4, list.yobi_text4);
		contentValues.put(KEY_YOBI_TEXT5, list.yobi_text5);
		contentValues.put(KEY_YOBI_DATE1, list.yobi_date1);
		contentValues.put(KEY_YOBI_DATE2, list.yobi_date2);
		contentValues.put(KEY_YOBI_DATE3, list.yobi_date3);
		contentValues.put(KEY_YOBI_DATE4, list.yobi_date4);
		contentValues.put(KEY_YOBI_DATE5, list.yobi_date5);
		contentValues.put(KEY_YOBI_REAL1, list.yobi_real1);
		contentValues.put(KEY_YOBI_REAL2, list.yobi_real2);
		contentValues.put(KEY_YOBI_REAL3, list.yobi_real3);
		contentValues.put(KEY_YOBI_REAL4, list.yobi_real4);
		contentValues.put(KEY_YOBI_REAL5, list.yobi_real5);
		contentValues.put(KEY_UP_DATE, list.up_date);
		contentValues.put(KEY_AD_DATE, list.ad_date);
		contentValues.put(KEY_OPID, list.opid);*/
		
		if (String.valueOf(list.yobi_code1) != null){
			contentValues.put(KEY_YOBI_CODE1, list.yobi_code1);
		}
		
		if (String.valueOf(list.yobi_code2) != null){
			contentValues.put(KEY_YOBI_CODE2, list.yobi_code2);
		}
		
		if (String.valueOf(list.yobi_code3) != null){
			contentValues.put(KEY_YOBI_CODE3, list.yobi_code3);
		}
		
		if (String.valueOf(list.yobi_code4) != null){
			contentValues.put(KEY_YOBI_CODE4, list.yobi_code4);
		}
		
		if (String.valueOf(list.yobi_code5 )!= null){
			contentValues.put(KEY_YOBI_CODE5, list.yobi_code5);
		}
		
		if (list.yobi_text1 != null){
			contentValues.put(KEY_YOBI_TEXT1, list.yobi_text1);
		}

		if (list.yobi_text2 != null){
			contentValues.put(KEY_YOBI_TEXT2, list.yobi_text2);
		}
		
		if (list.yobi_text3 != null){
			contentValues.put(KEY_YOBI_TEXT3, list.yobi_text3);
		}

		if (list.yobi_text4 != null){
			contentValues.put(KEY_YOBI_TEXT4, list.yobi_text4);
		}

		if (list.yobi_text5 != null){
			contentValues.put(KEY_YOBI_TEXT5, list.yobi_text5);
		}

		if (list.yobi_date1 != null){
			contentValues.put(KEY_YOBI_DATE1, list.yobi_date1);
		}

		if (list.yobi_date2 != null){
			contentValues.put(KEY_YOBI_DATE2, list.yobi_date2);
		}

		if (list.yobi_date3 != null){
			contentValues.put(KEY_YOBI_DATE3, list.yobi_date3);
		}

		if (list.yobi_date4 != null){
			contentValues.put(KEY_YOBI_DATE4, list.yobi_date4);
		}

		if (list.yobi_date5 != null){
			contentValues.put(KEY_YOBI_DATE5, list.yobi_date5);
		}

		if (String.valueOf(list.yobi_real1) != null){
			contentValues.put(KEY_YOBI_REAL1, list.yobi_real1);
		}

		if (String.valueOf(list.yobi_real2) != null){
			contentValues.put(KEY_YOBI_REAL2, list.yobi_real2);
		}

		if (String.valueOf(list.yobi_real3) != null){
			contentValues.put(KEY_YOBI_REAL3, list.yobi_real3);
		}

		if (String.valueOf(list.yobi_real4) != null){
			contentValues.put(KEY_YOBI_REAL4, list.yobi_real4);
		}
		
		
		if (String.valueOf(list.yobi_real5 )!= null){
			contentValues.put(KEY_YOBI_REAL5, list.yobi_real5);
		}
		
		//if (list.up_date != null){
			contentValues.put(KEY_UP_DATE, Utils.getCurrentDateTimeToString());
		//}

		if (list.opid != null){
			contentValues.put(KEY_OPID, list.opid);
		}
		/** điều kiện để update */
		String where = KEY_CODE + "=" + list.code ;
		try {
			Log.d("EDITING");
			db.update(TABLE_M_MESSAGE_TEMPLATE, contentValues, where, null);
			Log.d("EDITED");
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * Insert data vao table message trang thai
	 * @param list tại mới
	 * @return
	 */
	public long insertToMessageStatusTable(UserMessageStatus list) {
		try{
			ContentValues contentValues = new ContentValues();
			//contentValues.put(KEY_ID, list.id); tạm thời không sử dụng item này 
			contentValues.put(KEY_SENDER_CODE, list.sender_code);
			contentValues.put(KEY_MESSAGE_EMP_CODE, list.message_emp_code);
			contentValues.put(KEY_MESSAGE_TEMPLATE_CODE, list.message_template_code);
			contentValues.put(KEY_MESSAGE_TYPE, list.message_type);
			contentValues.put(KEY_SENDER_PHONE, list.sender_phone);
			contentValues.put(KEY_SENDER_MAIL, list.sender_mail);
			contentValues.put(KEY_RECEIVER_CODE, list.receiver_code);
			contentValues.put(KEY_RECEIVER_PHONE, list.receiver_phone);
			contentValues.put(KEY_RECEIVER_MAIL, list.receiver_mail);
			contentValues.put(KEY_SEND_PACKAGE, list.send_package);
			contentValues.put(KEY_SEND_DATETIME, list.send_datetime);
			contentValues.put(KEY_ISSMS, list.issms);
			contentValues.put(KEY_ISEMAIL, list.isemail);
			contentValues.put(KEY_ISACTIVE, list.isactive);
			contentValues.put(KEY_ATTACH_FILE, list.attach_file);
			contentValues.put(KEY_SEND_STATUS, list.send_status);
			contentValues.put(KEY_SCHEDULE_SEND_DATETIME, list.schedule_send_datetime);
			contentValues.put(KEY_SCHEDULE_SEND_LOOP, list.schedule_send_loop);
			//contentValues.put(KEY_CODE, list.code);
			contentValues.put(KEY_NAME, list.name);
			contentValues.put(KEY_RYAKU, list.ryaku);
			
			contentValues.put(KEY_ISDELETED, list.isdeleted);
			contentValues.put(KEY_NOTE, list.note);
			
			contentValues.put(KEY_YOBI_CODE1, list.yobi_code1);
			contentValues.put(KEY_YOBI_CODE2, list.yobi_code2);
			contentValues.put(KEY_YOBI_CODE3, list.yobi_code3);
			contentValues.put(KEY_YOBI_CODE4, list.yobi_code4);
			contentValues.put(KEY_YOBI_CODE5, list.yobi_code5);
			contentValues.put(KEY_YOBI_TEXT1, list.yobi_text1);
			contentValues.put(KEY_YOBI_TEXT2, list.yobi_text2);
			contentValues.put(KEY_YOBI_TEXT3, list.yobi_text3);
			contentValues.put(KEY_YOBI_TEXT4, list.yobi_text4);
			contentValues.put(KEY_YOBI_TEXT5, list.yobi_text5);
			contentValues.put(KEY_YOBI_DATE1, list.yobi_date1);
			contentValues.put(KEY_YOBI_DATE2, list.yobi_date2);
			contentValues.put(KEY_YOBI_DATE3, list.yobi_date3);
			contentValues.put(KEY_YOBI_DATE4, list.yobi_date4);
			contentValues.put(KEY_YOBI_DATE5, list.yobi_date5);
			contentValues.put(KEY_YOBI_REAL1, list.yobi_real1);
			contentValues.put(KEY_YOBI_REAL2, list.yobi_real2);
			contentValues.put(KEY_YOBI_REAL3, list.yobi_real3);
			contentValues.put(KEY_YOBI_REAL4, list.yobi_real4);
			contentValues.put(KEY_YOBI_REAL5, list.yobi_real5);
			contentValues.put(KEY_UP_DATE, Utils.getCurrentDateTimeToString());
			contentValues.put(KEY_AD_DATE, Utils.getCurrentDateTimeToString());
			contentValues.put(KEY_OPID, list.opid);

			Log.d("TABLE_M_MEI TRYING");
			//long id = db.insert(TABLE_M_MEI, null, contentValues);
			long id = db.insertOrThrow(TABLE_M_USER_MESSAGE_STATUS, null, contentValues);
			Log.d("TABLE_M_MEI ADDED");
			return id;
		}catch (Exception e){
			e.getMessage();
			return -1;//error
		}
		
	}
	
	/**
	 * Cap nhat message template
	 * @param list : cập nhận thông tin 
	 * @return
	 */
	public boolean editMessageStatusTable(UserMessageStatus list) {
		ContentValues contentValues = new ContentValues();

		if(String.valueOf(list.message_template_code) != null){
			contentValues.put(KEY_MESSAGE_TEMPLATE_CODE, list.message_template_code);
		}
		if(String.valueOf(list.message_emp_code) != null){
			contentValues.put(KEY_MESSAGE_EMP_CODE, list.message_emp_code);
		}
		
		if(String.valueOf(list.message_type) != null){
			contentValues.put(KEY_MESSAGE_TYPE, list.message_type);
		}
		if (list.sender_phone != null){
			contentValues.put(KEY_SENDER_PHONE, list.sender_phone);
		}
		if (list.sender_mail != null){
			contentValues.put(KEY_SENDER_MAIL, list.sender_mail);
		}
		if(String.valueOf(list.receiver_code) != null){
			contentValues.put(KEY_RECEIVER_CODE, list.receiver_code);
		}
		if (list.receiver_phone != null){
			contentValues.put(KEY_RECEIVER_PHONE, list.receiver_phone);
		}
		if (list.receiver_mail != null){
			contentValues.put(KEY_RECEIVER_MAIL, list.receiver_mail);
		}
		if (list.send_package != null){
			contentValues.put(KEY_SEND_PACKAGE, list.send_package);
		}
		if (list.send_datetime != null){
			contentValues.put(KEY_SEND_DATETIME, list.send_datetime);
		}
		if (String.valueOf(list.issms) != null){
			contentValues.put(KEY_ISSMS, list.issms);
		}
		if (String.valueOf(list.isemail) != null){
			contentValues.put(KEY_ISEMAIL, list.isemail);
		}
		if (String.valueOf(list.isactive) != null){
			contentValues.put(KEY_ISACTIVE, list.isactive);
		}
		if (list.attach_file != null){
			contentValues.put(KEY_ATTACH_FILE, list.attach_file);
		}
		if(String.valueOf(list.send_status) != null){
			contentValues.put(KEY_SEND_STATUS, list.send_status);
		}
		if (list.schedule_send_datetime != null){
			contentValues.put(KEY_SCHEDULE_SEND_DATETIME, list.schedule_send_datetime);
		}
		if (list.schedule_send_loop != null){
			contentValues.put(KEY_SCHEDULE_SEND_LOOP, list.schedule_send_loop);
		}
				
		if (list.name != null){
			contentValues.put(KEY_NAME, list.name);
		}
		if (list.ryaku != null){
			contentValues.put(KEY_RYAKU, list.ryaku);
		}
		
		/** delete hay chưa */
		if (String.valueOf(list.isdeleted) != null){
			contentValues.put(KEY_ISDELETED, list.isdeleted);
		}

		if (list.note != null){
			contentValues.put(KEY_NOTE, list.note);
		}
		/*contentValues.put(KEY_YOBI_CODE1, list.yobi_code1);
		contentValues.put(KEY_YOBI_CODE2, list.yobi_code2);
		contentValues.put(KEY_YOBI_CODE3, list.yobi_code3);
		contentValues.put(KEY_YOBI_CODE4, list.yobi_code4);
		contentValues.put(KEY_YOBI_CODE5, list.yobi_code5);
		contentValues.put(KEY_YOBI_TEXT1, list.yobi_text1);
		contentValues.put(KEY_YOBI_TEXT2, list.yobi_text2);
		contentValues.put(KEY_YOBI_TEXT3, list.yobi_text3);
		contentValues.put(KEY_YOBI_TEXT4, list.yobi_text4);
		contentValues.put(KEY_YOBI_TEXT5, list.yobi_text5);
		contentValues.put(KEY_YOBI_DATE1, list.yobi_date1);
		contentValues.put(KEY_YOBI_DATE2, list.yobi_date2);
		contentValues.put(KEY_YOBI_DATE3, list.yobi_date3);
		contentValues.put(KEY_YOBI_DATE4, list.yobi_date4);
		contentValues.put(KEY_YOBI_DATE5, list.yobi_date5);
		contentValues.put(KEY_YOBI_REAL1, list.yobi_real1);
		contentValues.put(KEY_YOBI_REAL2, list.yobi_real2);
		contentValues.put(KEY_YOBI_REAL3, list.yobi_real3);
		contentValues.put(KEY_YOBI_REAL4, list.yobi_real4);
		contentValues.put(KEY_YOBI_REAL5, list.yobi_real5);
		contentValues.put(KEY_UP_DATE, list.up_date);
		contentValues.put(KEY_AD_DATE, list.ad_date);
		contentValues.put(KEY_OPID, list.opid);*/
		
		if (String.valueOf(list.yobi_code1) != null){
			contentValues.put(KEY_YOBI_CODE1, list.yobi_code1);
		}
		
		if (String.valueOf(list.yobi_code2) != null){
			contentValues.put(KEY_YOBI_CODE2, list.yobi_code2);
		}
		
		if (String.valueOf(list.yobi_code3) != null){
			contentValues.put(KEY_YOBI_CODE3, list.yobi_code3);
		}
		
		if (String.valueOf(list.yobi_code4) != null){
			contentValues.put(KEY_YOBI_CODE4, list.yobi_code4);
		}
		
		if (String.valueOf(list.yobi_code5 )!= null){
			contentValues.put(KEY_YOBI_CODE5, list.yobi_code5);
		}
		
		if (list.yobi_text1 != null){
			contentValues.put(KEY_YOBI_TEXT1, list.yobi_text1);
		}

		if (list.yobi_text2 != null){
			contentValues.put(KEY_YOBI_TEXT2, list.yobi_text2);
		}
		
		if (list.yobi_text3 != null){
			contentValues.put(KEY_YOBI_TEXT3, list.yobi_text3);
		}

		if (list.yobi_text4 != null){
			contentValues.put(KEY_YOBI_TEXT4, list.yobi_text4);
		}

		if (list.yobi_text5 != null){
			contentValues.put(KEY_YOBI_TEXT5, list.yobi_text5);
		}

		if (list.yobi_date1 != null){
			contentValues.put(KEY_YOBI_DATE1, list.yobi_date1);
		}

		if (list.yobi_date2 != null){
			contentValues.put(KEY_YOBI_DATE2, list.yobi_date2);
		}

		if (list.yobi_date3 != null){
			contentValues.put(KEY_YOBI_DATE3, list.yobi_date3);
		}

		if (list.yobi_date4 != null){
			contentValues.put(KEY_YOBI_DATE4, list.yobi_date4);
		}

		if (list.yobi_date5 != null){
			contentValues.put(KEY_YOBI_DATE5, list.yobi_date5);
		}

		if (String.valueOf(list.yobi_real1) != null){
			contentValues.put(KEY_YOBI_REAL1, list.yobi_real1);
		}

		if (String.valueOf(list.yobi_real2) != null){
			contentValues.put(KEY_YOBI_REAL2, list.yobi_real2);
		}

		if (String.valueOf(list.yobi_real3) != null){
			contentValues.put(KEY_YOBI_REAL3, list.yobi_real3);
		}

		if (String.valueOf(list.yobi_real4) != null){
			contentValues.put(KEY_YOBI_REAL4, list.yobi_real4);
		}
		
		
		if (String.valueOf(list.yobi_real5 )!= null){
			contentValues.put(KEY_YOBI_REAL5, list.yobi_real5);
		}
		
		//if (list.up_date != null){
			contentValues.put(KEY_UP_DATE, Utils.getCurrentDateTimeToString());
		//}

		if (list.opid != null){
			contentValues.put(KEY_OPID, list.opid);
		}
		/** điều kiện để update */
		String where = KEY_CODE + "=" + list.code ;
		try {
			Log.d("EDITING");
			db.update(TABLE_M_USER_MESSAGE_STATUS, contentValues, where, null);
			Log.d("EDITED");
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * 
	 * @param list
	 * @return
	 */
	public long insertToUserTable(User list) {
		try{
			ContentValues contentValues = new ContentValues();
			
			contentValues.put(KEY_GOOGLE_CONTACT_ID, list.google_id);
			contentValues.put(KEY_FIRST_NAME, list.first_name);
			contentValues.put(KEY_LAST_NAME, list.last_name);
			contentValues.put(KEY_FULL_NAME, list.full_name);
			
			contentValues.put(KEY_SEX, list.sex);
			contentValues.put(KEY_BIRTHDAY, DateTimeUtil.formatDate2String(list.birthday , MasterConstants.DATE_VN_FORMAT, MasterConstants.DATE_JP_FORMAT));
			contentValues.put(KEY_ADDRESS, list.address);
			contentValues.put(KEY_HOME_TEL, list.home_tel);
			contentValues.put(KEY_MOBILE, list.mobile);
			contentValues.put(KEY_FAX, list.fax);
			contentValues.put(KEY_POSITION, list.position);
			contentValues.put(KEY_POSITION_NAME, list.position_name);
			contentValues.put(KEY_DEPT, list.dept);
			contentValues.put(KEY_DEPT_NAME, list.dept_name);
			contentValues.put(KEY_TEAM, list.team);
			contentValues.put(KEY_TEAM_NAME, list.team_name);
			contentValues.put(KEY_TANT, list.tant);
			contentValues.put(KEY_TANT_NAME, list.tant_name);
			contentValues.put(KEY_TRAINING_DATE, DateTimeUtil.formatDate2String(list.training_date , MasterConstants.DATE_VN_FORMAT, MasterConstants.DATE_JP_FORMAT));
			contentValues.put(KEY_TRAINING_DATE_END, DateTimeUtil.formatDate2String(list.training_dateEnd , MasterConstants.DATE_VN_FORMAT, MasterConstants.DATE_JP_FORMAT));
			
			contentValues.put(KEY_LEARN_TRAINING_DATE, DateTimeUtil.formatDate2String(list.learn_training_dateEnd , MasterConstants.DATE_VN_FORMAT, MasterConstants.DATE_JP_FORMAT));
			contentValues.put(KEY_LEARN_TRAINING_DATE_END, DateTimeUtil.formatDate2String(list.learn_training_dateEnd , MasterConstants.DATE_VN_FORMAT, MasterConstants.DATE_JP_FORMAT));
			
			contentValues.put(KEY_IN_DATE, DateTimeUtil.formatDate2String(list.in_date , MasterConstants.DATE_VN_FORMAT, MasterConstants.DATE_JP_FORMAT));
			
			contentValues.put(KEY_JOIN_DATE, DateTimeUtil.formatDate2String(list.join_date , MasterConstants.DATE_VN_FORMAT, MasterConstants.DATE_JP_FORMAT));
			contentValues.put(KEY_OUT_DATE, DateTimeUtil.formatDate2String(list.out_date , MasterConstants.DATE_VN_FORMAT, MasterConstants.DATE_JP_FORMAT));
			contentValues.put(KEY_MARRIED_DATE, DateTimeUtil.formatDate2String(list.married_date , MasterConstants.DATE_VN_FORMAT, MasterConstants.DATE_JP_FORMAT));

			contentValues.put(KEY_INIT_KEIKEN, list.init_keiken);
			contentValues.put(KEY_CONVERT_KEIKEN, list.convert_keiken);
			contentValues.put(KEY_USER_KBN, list.user_kbn);
			
			contentValues.put(KEY_NOTE, list.note);
			//contentValues.put(KEY_IMG, list.img);
			contentValues.put(KEY_ISDELETED, list.isdeleted);
			
	
			contentValues.put(KEY_EMAIL, list.email);
			contentValues.put(KEY_JAPANESE, list.japanese);
			contentValues.put(KEY_ALLOWANCE_BUSINESS, list.allowance_business);
			contentValues.put(KEY_ALLOWANCE_BSE, list.allowance_bse);
			contentValues.put(KEY_ALLOWANCE_ROOM, list.allowance_room);
			contentValues.put(KEY_MARRIED, list.married);
			contentValues.put(KEY_SALARY_NOTALOWANCE, list.salary_notallowance);
			contentValues.put(KEY_SALARY_ALOWANCE, list.salary_allowance);
			contentValues.put(KEY_ESTIMATE_POINT, list.estimate_point);
			contentValues.put(KEY_TAG1, list.tag1);
			contentValues.put(KEY_TAG2, list.tag2);
			
			contentValues.put(KEY_IMG_FULLPATH, list.img_fullpath);
			contentValues.put(KEY_ISLABOUR, list.isLabour);
			contentValues.put(KEY_LABOUR_JOIN_DATE, DateTimeUtil.formatDate2String(list.labour_join_date , MasterConstants.DATE_VN_FORMAT, MasterConstants.DATE_JP_FORMAT));
			contentValues.put(KEY_LABOUR_OUT_DATE, DateTimeUtil.formatDate2String(list.labour_out_date , MasterConstants.DATE_VN_FORMAT, MasterConstants.DATE_JP_FORMAT));
			contentValues.put(KEY_BUSINESS_KBN, list.business_kbn);
			contentValues.put(KEY_BASIC_DESIGN, list.basicdesign);
			contentValues.put(KEY_DETAIL_DESIGN, list.detaildesign);
			contentValues.put(KEY_PROGRAM, list.program);

			contentValues.put(KEY_SUPPORTER1, list.supporter1);
			contentValues.put(KEY_SUPPORTER2, list.supporter2);
			contentValues.put(KEY_SUPPORTER3, list.supporter3);

			contentValues.put(KEY_INTERVIEWER1, list.interviewer1);
			contentValues.put(KEY_INTERVIEWER2, list.interviewer2);
			contentValues.put(KEY_INTERVIEWER3, list.interviewer3);

			contentValues.put(KEY_STAFF_KBN, list.staff_kbn);
			contentValues.put(KEY_GPA, list.gpa);
			contentValues.put(KEY_GPA_TEXT, list.gpa_text);
			contentValues.put(KEY_COLLECT_NAME, list.collect_name);
			contentValues.put(KEY_INTERVIEW_KEKKA, list.interview_kekka);

			contentValues.put(KEY_YOBI_CODE1, list.yobi_code1);
			contentValues.put(KEY_YOBI_CODE2, list.yobi_code2);
			contentValues.put(KEY_YOBI_CODE3, list.yobi_code3);
			contentValues.put(KEY_YOBI_CODE4, list.yobi_code4);
			contentValues.put(KEY_YOBI_CODE5, list.yobi_code5);
			contentValues.put(KEY_YOBI_TEXT1, list.yobi_text1);
			contentValues.put(KEY_YOBI_TEXT2, list.yobi_text2);
			contentValues.put(KEY_YOBI_TEXT3, list.yobi_text3);
			contentValues.put(KEY_YOBI_TEXT4, list.yobi_text4);
			contentValues.put(KEY_YOBI_TEXT5, list.yobi_text5);
			contentValues.put(KEY_YOBI_DATE1, list.yobi_date1);
			contentValues.put(KEY_YOBI_DATE2, list.yobi_date2);
			contentValues.put(KEY_YOBI_DATE3, list.yobi_date3);
			contentValues.put(KEY_YOBI_DATE4, list.yobi_date4);
			contentValues.put(KEY_YOBI_DATE5, list.yobi_date5);
			contentValues.put(KEY_YOBI_REAL1, list.yobi_real1);
			contentValues.put(KEY_YOBI_REAL2, list.yobi_real2);
			contentValues.put(KEY_YOBI_REAL3, list.yobi_real3);
			contentValues.put(KEY_YOBI_REAL4, list.yobi_real4);
			contentValues.put(KEY_YOBI_REAL5, list.yobi_real5);
			contentValues.put(KEY_UP_DATE, Utils.getCurrentDateTimeToString());
			contentValues.put(KEY_AD_DATE, Utils.getCurrentDateTimeToString());
			contentValues.put(KEY_OPID, list.opid);

			Log.d("TABLE_M_USER TRYING");
			long id = db.insert(TABLE_M_USER, null, contentValues);
			Log.d("TABLE_M_USER ADDED");
			return id;
		}catch (Exception e){
			return -1;//error
		}
		
	}
	
	/**
	 * 
	 * @param list
	 * @return
	 */
	public long editToUserTable(User list) {
		try{
			ContentValues contentValues = new ContentValues();
			//contentValues.put(KEY_CODE, list.code);
			
			if (list.google_id != null){
				contentValues.put(KEY_GOOGLE_CONTACT_ID, list.google_id);
			}
			
			if (list.first_name != null){
				contentValues.put(KEY_FIRST_NAME, list.first_name);
			}
			if (list.last_name != null){
				contentValues.put(KEY_LAST_NAME, list.last_name);
			}
			if (list.full_name != null){
				contentValues.put(KEY_FULL_NAME, list.full_name);
			}
			
			if (String.valueOf(list.sex) != null){
				contentValues.put(KEY_SEX, list.sex);
			}
			
			if (list.birthday != null){
				contentValues.put(KEY_BIRTHDAY, DateTimeUtil.formatDate2String(list.birthday , MasterConstants.DATE_VN_FORMAT, MasterConstants.DATE_JP_FORMAT));
			}

			if (list.address != null){
				contentValues.put(KEY_ADDRESS, list.address);
			}

			
			if (list.home_tel != null){
				contentValues.put(KEY_HOME_TEL, list.home_tel);
			}

			if (list.mobile != null){
				contentValues.put(KEY_MOBILE, list.mobile);
			}

			if (list.fax != null){
				contentValues.put(KEY_FAX, list.fax);
			}

			if (String.valueOf(list.position) != null){
				contentValues.put(KEY_POSITION, list.position);
			}

			if (list.position_name != null){
				contentValues.put(KEY_POSITION_NAME, list.position_name);
			}
			
			if (String.valueOf(list.dept) != null){
				contentValues.put(KEY_DEPT, list.dept);
			}

			if (list.dept_name != null){
				contentValues.put(KEY_DEPT_NAME, list.dept_name);
			}

			if (String.valueOf(list.team) != null){
				contentValues.put(KEY_TEAM, list.team);
			}

			if (list.team_name != null){
				contentValues.put(KEY_TEAM_NAME, list.team_name);
			}

			if (String.valueOf(list.tant) != null){
				contentValues.put(KEY_TANT, list.tant);
			}

			if (list.tant_name != null){
				contentValues.put(KEY_TANT_NAME, list.tant_name);
			}

			if (list.training_date != null){
				contentValues.put(KEY_TRAINING_DATE, DateTimeUtil.formatDate2String(list.training_date , MasterConstants.DATE_VN_FORMAT, MasterConstants.DATE_JP_FORMAT));
			}
			
			if (list.training_dateEnd != null){
				contentValues.put(KEY_TRAINING_DATE_END, DateTimeUtil.formatDate2String(list.training_dateEnd , MasterConstants.DATE_VN_FORMAT, MasterConstants.DATE_JP_FORMAT));
			}
			if (list.learn_training_date != null){
				contentValues.put(KEY_LEARN_TRAINING_DATE, DateTimeUtil.formatDate2String(list.learn_training_date , MasterConstants.DATE_VN_FORMAT, MasterConstants.DATE_JP_FORMAT));
			}
			
			if (list.learn_training_dateEnd != null){
				contentValues.put(KEY_LEARN_TRAINING_DATE_END, DateTimeUtil.formatDate2String(list.learn_training_dateEnd , MasterConstants.DATE_VN_FORMAT, MasterConstants.DATE_JP_FORMAT));
			}
			if (list.in_date != null){
				contentValues.put(KEY_IN_DATE, DateTimeUtil.formatDate2String(list.in_date , MasterConstants.DATE_VN_FORMAT, MasterConstants.DATE_JP_FORMAT));
			}

			if (list.join_date != null){
				contentValues.put(KEY_JOIN_DATE, DateTimeUtil.formatDate2String(list.join_date , MasterConstants.DATE_VN_FORMAT, MasterConstants.DATE_JP_FORMAT));
			}

			if (list.out_date != null){
				contentValues.put(KEY_OUT_DATE, DateTimeUtil.formatDate2String(list.out_date , MasterConstants.DATE_VN_FORMAT, MasterConstants.DATE_JP_FORMAT));
			}

			if (list.married_date!= null){
				contentValues.put(KEY_MARRIED_DATE, DateTimeUtil.formatDate2String(list.married_date , MasterConstants.DATE_VN_FORMAT, MasterConstants.DATE_JP_FORMAT));
			}

			if (list.init_keiken != null){
				contentValues.put(KEY_INIT_KEIKEN, list.init_keiken);
			}
			
			if (list.convert_keiken != null){
				contentValues.put(KEY_CONVERT_KEIKEN, list.convert_keiken);
			}
			
			if (String.valueOf(list.user_kbn) != null){
				contentValues.put(KEY_USER_KBN, list.user_kbn);
			}
			
			if (list.note != null){
				contentValues.put(KEY_NOTE, list.note);
			}
			
			//contentValues.put(KEY_IMG, list.img);
			if (String.valueOf(list.isdeleted) != null){
				contentValues.put(KEY_ISDELETED, list.isdeleted);
			}

			if (list.email != null){
				contentValues.put(KEY_EMAIL, list.email);
			}

			if (list.japanese != null){
				contentValues.put(KEY_JAPANESE, list.japanese);
			}

			if (list.allowance_business != null){
				contentValues.put(KEY_ALLOWANCE_BUSINESS, list.allowance_business);
			}

			if (list.allowance_bse!= null){
				contentValues.put(KEY_ALLOWANCE_BSE, list.allowance_bse);
			}

			if (list.allowance_room != null){
				contentValues.put(KEY_ALLOWANCE_ROOM, list.allowance_room);
			}
			if (String.valueOf(list.married )!= null){
				contentValues.put(KEY_MARRIED, list.married);
			}

			if (String.valueOf(list.salary_notallowance) != null){
				contentValues.put(KEY_SALARY_NOTALOWANCE, list.salary_notallowance);
			}

			if (String.valueOf(list.salary_allowance )!= null){
				contentValues.put(KEY_SALARY_ALOWANCE, list.salary_allowance);
			}

			if (String.valueOf(list.estimate_point)!= null){
				contentValues.put(KEY_ESTIMATE_POINT, list.estimate_point);
			}

			if (list.tag1 != null){
				contentValues.put(KEY_TAG1, list.tag1);
			}

			if (list.tag2 != null){
				contentValues.put(KEY_TAG2, list.tag2);
			}

			if (list.img_fullpath != null){
				contentValues.put(KEY_IMG_FULLPATH, list.img_fullpath);
			}

			if (String.valueOf(list.isLabour) != null){
				contentValues.put(KEY_ISLABOUR, list.isLabour);
			}

			if (list.labour_join_date != null){
				contentValues.put(KEY_LABOUR_JOIN_DATE, DateTimeUtil.formatDate2String(list.labour_join_date , MasterConstants.DATE_VN_FORMAT, MasterConstants.DATE_JP_FORMAT));
			}

			if (list.labour_out_date != null){
				contentValues.put(KEY_LABOUR_OUT_DATE, DateTimeUtil.formatDate2String(list.labour_out_date , MasterConstants.DATE_VN_FORMAT, MasterConstants.DATE_JP_FORMAT));
			}

			if (list.business_kbn != null){
				contentValues.put(KEY_BUSINESS_KBN, list.business_kbn);
			}

			if (String.valueOf(list.basicdesign) != null){
				contentValues.put(KEY_BASIC_DESIGN, list.basicdesign);
			}

			if (String.valueOf(list.detaildesign) != null){
				contentValues.put(KEY_DETAIL_DESIGN, list.detaildesign);
			}

			if (String.valueOf(list.program) != null){
				contentValues.put(KEY_PROGRAM, list.program);
			}

			if (list.staff_kbn!= null){
				contentValues.put(KEY_STAFF_KBN, list.staff_kbn);
			}

			if (list.supporter1!= null){
				contentValues.put(KEY_SUPPORTER1, list.supporter1);
			}

			if (list.supporter2!= null){
				contentValues.put(KEY_SUPPORTER2, list.supporter2);
			}

			if (list.supporter3!= null){
				contentValues.put(KEY_SUPPORTER3, list.supporter3);
			}

			if (list.interviewer1!= null){
				contentValues.put(KEY_INTERVIEWER1, list.interviewer1);
			}

			if (list.interviewer2!= null){
				contentValues.put(KEY_INTERVIEWER2, list.interviewer2);
			}

			if (list.interviewer3!= null){
				contentValues.put(KEY_INTERVIEWER3, list.interviewer3);
			}

			if (String.valueOf(list.gpa) != null){
				contentValues.put(KEY_GPA, list.gpa);
			}

			if (list.gpa_text!= null){
				contentValues.put(KEY_GPA_TEXT, list.gpa_text);
			}

			if (list.collect_name!= null){
				contentValues.put(KEY_COLLECT_NAME, list.collect_name);
			}

			if (list.interview_kekka!= null){
				contentValues.put(KEY_INTERVIEW_KEKKA, list.interview_kekka);
			}

			if (String.valueOf(list.yobi_code1) != null){
				contentValues.put(KEY_YOBI_CODE1, list.yobi_code1);
			}
			
			if (String.valueOf(list.yobi_code2) != null){
				contentValues.put(KEY_YOBI_CODE2, list.yobi_code2);
			}
			
			if (String.valueOf(list.yobi_code3) != null){
				contentValues.put(KEY_YOBI_CODE3, list.yobi_code3);
			}
			
			if (String.valueOf(list.yobi_code4) != null){
				contentValues.put(KEY_YOBI_CODE4, list.yobi_code4);
			}
			
			if (String.valueOf(list.yobi_code5 )!= null){
				contentValues.put(KEY_YOBI_CODE5, list.yobi_code5);
			}
			
			if (list.yobi_text1 != null){
				contentValues.put(KEY_YOBI_TEXT1, list.yobi_text1);
			}

			if (list.yobi_text2 != null){
				contentValues.put(KEY_YOBI_TEXT2, list.yobi_text2);
			}
			
			if (list.yobi_text3 != null){
				contentValues.put(KEY_YOBI_TEXT3, list.yobi_text3);
			}

			if (list.yobi_text4 != null){
				contentValues.put(KEY_YOBI_TEXT4, list.yobi_text4);
			}

			if (list.yobi_text5 != null){
				contentValues.put(KEY_YOBI_TEXT5, list.yobi_text5);
			}

			if (list.yobi_date1 != null){
				contentValues.put(KEY_YOBI_DATE1, list.yobi_date1);
			}

			if (list.yobi_date2 != null){
				contentValues.put(KEY_YOBI_DATE2, list.yobi_date2);
			}

			if (list.yobi_date3 != null){
				contentValues.put(KEY_YOBI_DATE3, list.yobi_date3);
			}

			if (list.yobi_date4 != null){
				contentValues.put(KEY_YOBI_DATE4, list.yobi_date4);
			}

			if (list.yobi_date5 != null){
				contentValues.put(KEY_YOBI_DATE5, list.yobi_date5);
			}

			if (String.valueOf(list.yobi_real1) != null){
				contentValues.put(KEY_YOBI_REAL1, list.yobi_real1);
			}

			if (String.valueOf(list.yobi_real2) != null){
				contentValues.put(KEY_YOBI_REAL2, list.yobi_real2);
			}

			if (String.valueOf(list.yobi_real3) != null){
				contentValues.put(KEY_YOBI_REAL3, list.yobi_real3);
			}

			if (String.valueOf(list.yobi_real4) != null){
				contentValues.put(KEY_YOBI_REAL4, list.yobi_real4);
			}
			
			
			if (String.valueOf(list.yobi_real5 )!= null){
				contentValues.put(KEY_YOBI_REAL5, list.yobi_real5);
			}
			
			//if (list.up_date != null){
				contentValues.put(KEY_UP_DATE, Utils.getCurrentDateTimeToString());
			//}

			if (list.opid != null){
				contentValues.put(KEY_OPID, list.opid);
			}

			String where = KEY_CODE + "=" + list.code  ;
			
			Log.d("TABLE_M_USER TRYING");
			long id = db.update(TABLE_M_USER,  contentValues, where, null);
			Log.d("TABLE_M_USER ADDED");
			return id;
		}catch (Exception e){
			return -1;//error
		}
		
	}
	
	/**
	 * 
	 * @param list
	 * @return
	 */
	public long insertToUserHisTable(UserHistory list) {
		try{
			ContentValues contentValues = new ContentValues();
			contentValues.put(KEY_USER_CODE, list.user_code);
			contentValues.put(KEY_MKBN, list.mkbn);
			contentValues.put(KEY_NAME, list.name);
			contentValues.put(KEY_RYAKU, list.ryaku);
			contentValues.put(KEY_DATE_FROM, DateTimeUtil.formatDate2String(list.date_from , MasterConstants.DATE_VN_FORMAT, MasterConstants.DATE_JP_FORMAT));
			contentValues.put(KEY_DATE_TO, DateTimeUtil.formatDate2String(list.date_to, MasterConstants.DATE_VN_FORMAT, MasterConstants.DATE_JP_FORMAT));
			contentValues.put(KEY_NEW_DEPT_CODE, list.new_dept_code);
			contentValues.put(KEY_NEW_TEAM_CODE, list.new_team_code);
			contentValues.put(KEY_NEW_POSITION_CODE, list.new_position_code);
			
			contentValues.put(KEY_NEW_JAPANESE, list.new_japanese);
			contentValues.put(KEY_NEW_ALLOWANCE_BUSINESS, list.new_allowance_business);
			contentValues.put(KEY_NEW_ALLOWANCE_BSE, list.new_allowance_bse);
			contentValues.put(KEY_SALARY_STANDARD, list.new_salary_standard);
			contentValues.put(KEY_SALARY_PERCENT, list.new_salary_percent);
			contentValues.put(KEY_SALARY_ACTUAL_UP, list.new_salary_actual_up);
			contentValues.put(KEY_SALARY_NEXT_YM, list.new_salary_next_ym);
			contentValues.put(KEY_NEW_SALARY, list.new_salary);
			
			contentValues.put(KEY_NOTE, list.note);
			contentValues.put(KEY_ISDELETED, list.isdeleted);
			
			contentValues.put(KEY_YOBI_CODE1, list.yobi_code1);
			contentValues.put(KEY_YOBI_CODE2, list.yobi_code2);
			contentValues.put(KEY_YOBI_CODE3, list.yobi_code3);
			contentValues.put(KEY_YOBI_CODE4, list.yobi_code4);
			contentValues.put(KEY_YOBI_CODE5, list.yobi_code5);
			contentValues.put(KEY_YOBI_TEXT1, list.yobi_text1);
			contentValues.put(KEY_YOBI_TEXT2, list.yobi_text2);
			contentValues.put(KEY_YOBI_TEXT3, list.yobi_text3);
			contentValues.put(KEY_YOBI_TEXT4, list.yobi_text4);
			contentValues.put(KEY_YOBI_TEXT5, list.yobi_text5);
			contentValues.put(KEY_YOBI_DATE1, list.yobi_date1);
			contentValues.put(KEY_YOBI_DATE2, list.yobi_date2);
			contentValues.put(KEY_YOBI_DATE3, list.yobi_date3);
			contentValues.put(KEY_YOBI_DATE4, list.yobi_date4);
			contentValues.put(KEY_YOBI_DATE5, list.yobi_date5);
			contentValues.put(KEY_YOBI_REAL1, list.yobi_real1);
			contentValues.put(KEY_YOBI_REAL2, list.yobi_real2);
			contentValues.put(KEY_YOBI_REAL3, list.yobi_real3);
			contentValues.put(KEY_YOBI_REAL4, list.yobi_real4);
			contentValues.put(KEY_YOBI_REAL5, list.yobi_real5);
			contentValues.put(KEY_UP_DATE, Utils.getCurrentDateTimeToString());
			contentValues.put(KEY_AD_DATE, Utils.getCurrentDateTimeToString());
			contentValues.put(KEY_OPID, list.opid);

			Log.d("TABLE_M_USER_HIS TRYING");
			long id = db.insert(TABLE_M_USER_HIS, null, contentValues);
			Log.d("TABLE_M_USER_HIS ADDED");
			return id;
		}catch (Exception e){
			return -1;//error
		}
		
	}
	
	/**
	 * 
	 * @param list
	 * @return
	 */
	public long editToUserHisTable(UserHistory list) {
		try{
			ContentValues contentValues = new ContentValues();

			if (list.name != null){
				contentValues.put(KEY_NAME, list.name);
			}
			if (list.ryaku != null){
				contentValues.put(KEY_RYAKU, list.ryaku);
			}
			if (String.valueOf(list.mkbn) != null){
				contentValues.put(KEY_MKBN, list.mkbn);
			}
		
			if (list.date_from != null){
				contentValues.put(KEY_DATE_FROM, DateTimeUtil.formatDate2String(list.date_from , MasterConstants.DATE_VN_FORMAT, MasterConstants.DATE_JP_FORMAT));
			}
			
			if (list.date_to != null){
				contentValues.put(KEY_DATE_TO, DateTimeUtil.formatDate2String(list.date_to , MasterConstants.DATE_VN_FORMAT, MasterConstants.DATE_JP_FORMAT));
			}
			
			if (String.valueOf(list.new_dept_code) != null){
				contentValues.put(KEY_NEW_DEPT_CODE, list.new_dept_code);
			}
			
			if (String.valueOf(list.new_team_code) != null){
				contentValues.put(KEY_NEW_TEAM_CODE, list.new_team_code);
			}
			
			if (String.valueOf(list.new_position_code) != null){
				contentValues.put(KEY_NEW_POSITION_CODE, list.new_position_code);
			}
			if (list.new_japanese != null){
				contentValues.put(KEY_NEW_JAPANESE, list.new_japanese);
			}
			if (list.new_allowance_business != null){
				contentValues.put(KEY_NEW_ALLOWANCE_BUSINESS, list.new_allowance_business);
			}
			if (list.new_allowance_bse != null){
				contentValues.put(KEY_NEW_ALLOWANCE_BSE, list.new_allowance_bse);
			}
			if (String.valueOf(list.new_salary_standard) != null){
				contentValues.put(KEY_SALARY_STANDARD, list.new_salary_standard);
			}
			
			
			if (String.valueOf(list.new_salary_percent) != null){
				contentValues.put(KEY_SALARY_PERCENT, list.new_salary_percent);
			}
			
			if (String.valueOf(list.new_salary_actual_up) != null){
				contentValues.put(KEY_SALARY_ACTUAL_UP, list.new_salary_actual_up);
			}
			
			if (String.valueOf(list.new_salary_next_ym) != null){
				contentValues.put(KEY_SALARY_NEXT_YM, list.new_salary_next_ym);
			}
			
			if (String.valueOf(list.new_salary) != null){
				contentValues.put(KEY_NEW_SALARY, list.new_salary);
			}
			
			if (list.reason != null){
				contentValues.put(KEY_REASON, list.reason);
			}
			
			if (list.note != null){
				contentValues.put(KEY_NOTE, list.note);
			}
			
			if (String.valueOf(list.yobi_code1) != null){
				contentValues.put(KEY_YOBI_CODE1, list.yobi_code1);
			}
			
			if (String.valueOf(list.yobi_code2) != null){
				contentValues.put(KEY_YOBI_CODE2, list.yobi_code2);
			}
			
			if (String.valueOf(list.yobi_code3) != null){
				contentValues.put(KEY_YOBI_CODE3, list.yobi_code3);
			}
			
			if (String.valueOf(list.yobi_code4) != null){
				contentValues.put(KEY_YOBI_CODE4, list.yobi_code4);
			}
			
			if (String.valueOf(list.yobi_code5 )!= null){
				contentValues.put(KEY_YOBI_CODE5, list.yobi_code5);
			}
			
			if (list.yobi_text1 != null){
				contentValues.put(KEY_YOBI_TEXT1, list.yobi_text1);
			}

			if (list.yobi_text2 != null){
				contentValues.put(KEY_YOBI_TEXT2, list.yobi_text2);
			}
			
			if (list.yobi_text3 != null){
				contentValues.put(KEY_YOBI_TEXT3, list.yobi_text3);
			}

			if (list.yobi_text4 != null){
				contentValues.put(KEY_YOBI_TEXT4, list.yobi_text4);
			}

			if (list.yobi_text5 != null){
				contentValues.put(KEY_YOBI_TEXT5, list.yobi_text5);
			}

			if (list.yobi_date1 != null){
				contentValues.put(KEY_YOBI_DATE1, list.yobi_date1);
			}

			if (list.yobi_date2 != null){
				contentValues.put(KEY_YOBI_DATE2, list.yobi_date2);
			}

			if (list.yobi_date3 != null){
				contentValues.put(KEY_YOBI_DATE3, list.yobi_date3);
			}

			if (list.yobi_date4 != null){
				contentValues.put(KEY_YOBI_DATE4, list.yobi_date4);
			}

			if (list.yobi_date5 != null){
				contentValues.put(KEY_YOBI_DATE5, list.yobi_date5);
			}

			if (String.valueOf(list.yobi_real1) != null){
				contentValues.put(KEY_YOBI_REAL1, list.yobi_real1);
			}

			if (String.valueOf(list.yobi_real2) != null){
				contentValues.put(KEY_YOBI_REAL2, list.yobi_real2);
			}

			if (String.valueOf(list.yobi_real3) != null){
				contentValues.put(KEY_YOBI_REAL3, list.yobi_real3);
			}

			if (String.valueOf(list.yobi_real4) != null){
				contentValues.put(KEY_YOBI_REAL4, list.yobi_real4);
			}
			
			
			if (String.valueOf(list.yobi_real5 )!= null){
				contentValues.put(KEY_YOBI_REAL5, list.yobi_real5);
			}
			
			//if (list.up_date != null){
				contentValues.put(KEY_UP_DATE, Utils.getCurrentDateTimeToString());
			//}

			if (list.opid != null){
				contentValues.put(KEY_OPID, list.opid);
			}

			String where = KEY_CODE + "=" + list.code + " AND " + KEY_MKBN + " = " + list.mkbn ;
			
			Log.d("TABLE_M_USER_HIS TRYING");
			long id = db.update(TABLE_M_USER_HIS,  contentValues, where, null);
			Log.d("TABLE_M_USER_HIS ADDED");
			return id;
		}catch (Exception e){
			return -1;//error
		}
		
	}
	/**
	 * 
	 * @param list
	 * @return
	 */
	public long insertToUserPositionGroupTable(UserPositionGroup list) {
		try{
			ContentValues contentValues = new ContentValues();
			contentValues.put(KEY_USER_CODE, list.user_code);
			contentValues.put(KEY_POSITION_GROUP_CODE, list.position_group_code);
			contentValues.put(KEY_NAME, list.name);
			contentValues.put(KEY_RYAKU, list.ryaku);
			contentValues.put(KEY_NOTE, list.note);
			contentValues.put(KEY_ISDELETED, list.isdeleted);

			contentValues.put(KEY_YOBI_CODE1, list.yobi_code1);
			contentValues.put(KEY_YOBI_CODE2, list.yobi_code2);
			contentValues.put(KEY_YOBI_CODE3, list.yobi_code3);
			contentValues.put(KEY_YOBI_CODE4, list.yobi_code4);
			contentValues.put(KEY_YOBI_CODE5, list.yobi_code5);
			contentValues.put(KEY_YOBI_TEXT1, list.yobi_text1);
			contentValues.put(KEY_YOBI_TEXT2, list.yobi_text2);
			contentValues.put(KEY_YOBI_TEXT3, list.yobi_text3);
			contentValues.put(KEY_YOBI_TEXT4, list.yobi_text4);
			contentValues.put(KEY_YOBI_TEXT5, list.yobi_text5);
			contentValues.put(KEY_YOBI_DATE1, list.yobi_date1);
			contentValues.put(KEY_YOBI_DATE2, list.yobi_date2);
			contentValues.put(KEY_YOBI_DATE3, list.yobi_date3);
			contentValues.put(KEY_YOBI_DATE4, list.yobi_date4);
			contentValues.put(KEY_YOBI_DATE5, list.yobi_date5);
			contentValues.put(KEY_YOBI_REAL1, list.yobi_real1);
			contentValues.put(KEY_YOBI_REAL2, list.yobi_real2);
			contentValues.put(KEY_YOBI_REAL3, list.yobi_real3);
			contentValues.put(KEY_YOBI_REAL4, list.yobi_real4);
			contentValues.put(KEY_YOBI_REAL5, list.yobi_real5);
			contentValues.put(KEY_UP_DATE, Utils.getCurrentDateTimeToString());
			contentValues.put(KEY_AD_DATE, Utils.getCurrentDateTimeToString());
			contentValues.put(KEY_OPID, list.opid);

			Log.d("TABLE_M_USER_POSITION_GROUP TRYING");
			long id = db.insert(TABLE_M_USER_POSITION_GROUP, null, contentValues);
			Log.d("TABLE_M_USER_POSITION_GROUP ADDED");
			return id;
		}catch (Exception e){
			return -1;//error
		}
		
	}
	
	/**
	 * 
	 * @param list
	 * @return
	 */
	public long insertToUserCustomerGroupTable(UserCustomerGroup list) {
		try{
			ContentValues contentValues = new ContentValues();
			contentValues.put(KEY_USER_CODE, list.user_code);
			contentValues.put(KEY_CUSTOMER_GROUP_CODE, list.customer_group_code);
			contentValues.put(KEY_NAME, list.name);
			contentValues.put(KEY_RYAKU, list.ryaku);
			contentValues.put(KEY_NOTE, list.note);
			contentValues.put(KEY_ISDELETED, list.isdeleted);

			contentValues.put(KEY_YOBI_CODE1, list.yobi_code1);
			contentValues.put(KEY_YOBI_CODE2, list.yobi_code2);
			contentValues.put(KEY_YOBI_CODE3, list.yobi_code3);
			contentValues.put(KEY_YOBI_CODE4, list.yobi_code4);
			contentValues.put(KEY_YOBI_CODE5, list.yobi_code5);
			contentValues.put(KEY_YOBI_TEXT1, list.yobi_text1);
			contentValues.put(KEY_YOBI_TEXT2, list.yobi_text2);
			contentValues.put(KEY_YOBI_TEXT3, list.yobi_text3);
			contentValues.put(KEY_YOBI_TEXT4, list.yobi_text4);
			contentValues.put(KEY_YOBI_TEXT5, list.yobi_text5);
			contentValues.put(KEY_YOBI_DATE1, list.yobi_date1);
			contentValues.put(KEY_YOBI_DATE2, list.yobi_date2);
			contentValues.put(KEY_YOBI_DATE3, list.yobi_date3);
			contentValues.put(KEY_YOBI_DATE4, list.yobi_date4);
			contentValues.put(KEY_YOBI_DATE5, list.yobi_date5);
			contentValues.put(KEY_YOBI_REAL1, list.yobi_real1);
			contentValues.put(KEY_YOBI_REAL2, list.yobi_real2);
			contentValues.put(KEY_YOBI_REAL3, list.yobi_real3);
			contentValues.put(KEY_YOBI_REAL4, list.yobi_real4);
			contentValues.put(KEY_YOBI_REAL5, list.yobi_real5);
			contentValues.put(KEY_UP_DATE, Utils.getCurrentDateTimeToString());
			contentValues.put(KEY_AD_DATE, Utils.getCurrentDateTimeToString());
			contentValues.put(KEY_OPID, list.opid);

			Log.d("TABLE_M_USER_CUSTOMER_GROUP TRYING");
			long id = db.insert(TABLE_M_USER_CUSTOMER_GROUP, null, contentValues);
			Log.d("TABLE_M_USER_CUSTOMER_GROUP ADDED");
			return id;
		}catch (Exception e){
			return -1;//error
		}
		
	}
	
	/**
	 * 
	 * @param list
	 * @return
	 */
	public long insertToUserBusinessGroupTable(UserBusinessGroup list) {
		try{
			ContentValues contentValues = new ContentValues();
			contentValues.put(KEY_USER_CODE, list.user_code);
			contentValues.put(KEY_BUSINESS_CODE, list.business_group_code);
			contentValues.put(KEY_NAME, list.name);
			contentValues.put(KEY_RYAKU, list.ryaku);
			contentValues.put(KEY_NOTE, list.note);
			contentValues.put(KEY_ISDELETED, list.isdeleted);

			contentValues.put(KEY_YOBI_CODE1, list.yobi_code1);
			contentValues.put(KEY_YOBI_CODE2, list.yobi_code2);
			contentValues.put(KEY_YOBI_CODE3, list.yobi_code3);
			contentValues.put(KEY_YOBI_CODE4, list.yobi_code4);
			contentValues.put(KEY_YOBI_CODE5, list.yobi_code5);
			contentValues.put(KEY_YOBI_TEXT1, list.yobi_text1);
			contentValues.put(KEY_YOBI_TEXT2, list.yobi_text2);
			contentValues.put(KEY_YOBI_TEXT3, list.yobi_text3);
			contentValues.put(KEY_YOBI_TEXT4, list.yobi_text4);
			contentValues.put(KEY_YOBI_TEXT5, list.yobi_text5);
			contentValues.put(KEY_YOBI_DATE1, list.yobi_date1);
			contentValues.put(KEY_YOBI_DATE2, list.yobi_date2);
			contentValues.put(KEY_YOBI_DATE3, list.yobi_date3);
			contentValues.put(KEY_YOBI_DATE4, list.yobi_date4);
			contentValues.put(KEY_YOBI_DATE5, list.yobi_date5);
			contentValues.put(KEY_YOBI_REAL1, list.yobi_real1);
			contentValues.put(KEY_YOBI_REAL2, list.yobi_real2);
			contentValues.put(KEY_YOBI_REAL3, list.yobi_real3);
			contentValues.put(KEY_YOBI_REAL4, list.yobi_real4);
			contentValues.put(KEY_YOBI_REAL5, list.yobi_real5);
			contentValues.put(KEY_UP_DATE, Utils.getCurrentDateTimeToString());
			contentValues.put(KEY_AD_DATE, Utils.getCurrentDateTimeToString());
			contentValues.put(KEY_OPID, list.opid);

			Log.d("TABLE_M_USER_BUSINESS_GROUP TRYING");
			long id = db.insert(TABLE_M_USER_BUSINESS, null, contentValues);
			Log.d("TABLE_M_USER_BUSINESS_GROUP ADDED");
			return id;
		}catch (Exception e){
			return -1;//error
		}
		
	}
	
	/**
	 * 
	 * @param list
	 * @return
	 */
	public long insertToUserImageTable(UserImage list) {
		try{
			ContentValues contentValues = new ContentValues();
			contentValues.put(KEY_USER_CODE, list.getUser_code());
			contentValues.put(KEY_IMG, list.getImg());
			contentValues.put(KEY_IMG_FULLPATH, list.getImgFullPath());
			contentValues.put(KEY_NAME, list.getName());
			contentValues.put(KEY_RYAKU, list.getRyaku());
			contentValues.put(KEY_NOTE, list.getNote());
			contentValues.put(KEY_ISDELETED, list.getIsdeleted());

			contentValues.put(KEY_YOBI_CODE1, list.getYobi_code1());
			contentValues.put(KEY_YOBI_CODE2, list.getYobi_code2());
			contentValues.put(KEY_YOBI_CODE3, list.getYobi_code3());
			contentValues.put(KEY_YOBI_CODE4, list.getYobi_code4());
			contentValues.put(KEY_YOBI_CODE5, list.getYobi_code5());
			contentValues.put(KEY_YOBI_TEXT1, list.getYobi_text1());
			contentValues.put(KEY_YOBI_TEXT2, list.getYobi_text2());
			contentValues.put(KEY_YOBI_TEXT3, list.getYobi_text3());
			contentValues.put(KEY_YOBI_TEXT4, list.getYobi_text4());
			contentValues.put(KEY_YOBI_TEXT5, list.getYobi_text5());
			contentValues.put(KEY_YOBI_DATE1, list.getYobi_date1());
			contentValues.put(KEY_YOBI_DATE2, list.getYobi_date2());
			contentValues.put(KEY_YOBI_DATE3, list.getYobi_date3());
			contentValues.put(KEY_YOBI_DATE4, list.getYobi_date4());
			contentValues.put(KEY_YOBI_DATE5, list.getYobi_date5());
			contentValues.put(KEY_YOBI_REAL1, list.getYobi_real1());
			contentValues.put(KEY_YOBI_REAL2, list.getYobi_real2());
			contentValues.put(KEY_YOBI_REAL3, list.getYobi_real3());
			contentValues.put(KEY_YOBI_REAL4, list.getYobi_real4());
			contentValues.put(KEY_YOBI_REAL5, list.getYobi_real5());
			contentValues.put(KEY_UP_DATE, list.getUp_date());
			contentValues.put(KEY_AD_DATE, list.getAd_date());
			contentValues.put(KEY_OPID, list.getOpid());

			Log.d("TABLE_M_USER_IMAGE TRYING");
			long id = db.insert(TABLE_M_USER_IMAGE, null, contentValues);
			Log.d("TABLE_M_USER_IMAGE ADDED");
			return id;
		}catch (Exception e){
			return -1;//error
		}
		
	}
	
	/**
	 * 
	 * @param code : mã message cần delete
	 * @return
	 */
	public boolean deleteMessageTemplateByCode(Integer code) {
		String xWhere ="";
		//xWhere = KEY_MKBN + "=" + MasterConstants.MASTER_MKBN_TEAM;
		xWhere = xWhere + " AND " + KEY_CODE+ "=" + code;
		try {
			ContentValues contentValues = new ContentValues();
			contentValues.put(KEY_ISDELETED, 1);
			
			contentValues.put(KEY_UP_DATE, Utils.getCurrentDateTimeToString());
			
			Log.d("Deleting");
			db.delete(TABLE_M_MESSAGE_TEMPLATE, xWhere, null);
			Log.d("Deleted");
		} catch (SQLiteException e) {
			return false;
		}
		return true;
	}
	/**
	 * 
	 * Xóa tất cả message
	 * @return
	 */
	public boolean deleteMessageTemplate() {
		String xWhere ="";
		//xWhere = KEY_MKBN + "=" + MasterConstants.MASTER_MKBN_DEPT;
		
		try {
			Log.d("Deleting");
			ContentValues contentValues = new ContentValues();
			contentValues.put(KEY_ISDELETED, 1);
			
			contentValues.put(KEY_UP_DATE, Utils.getCurrentDateTimeToString());
			
			Log.d("EDITING");
			db.update(TABLE_M_MESSAGE_TEMPLATE, contentValues, xWhere, null);
			Log.d("EDITED");
			
			Log.d("Deleted");
		} catch (SQLiteException e) {
			return false;
		}
		return true;
	}
	/**
	 * 
	 * @param code : mã phòng ban cần get
	 * @return
	 */
	public boolean deleteDeptByCode(int code ) {
		String xWhere ="";
		xWhere = KEY_MKBN + "=" + MasterConstants.MASTER_MKBN_DEPT;
		xWhere = xWhere + " AND " + KEY_CODE + "=" + code;
		try {
			Log.d("Deleting");
			ContentValues contentValues = new ContentValues();
			contentValues.put(KEY_ISDELETED, 1);
			
			contentValues.put(KEY_UP_DATE, Utils.getCurrentDateTimeToString());
			
			Log.d("EDITING");
			db.update(TABLE_M_MEI, contentValues, xWhere, null);
			Log.d("EDITED");
			
			Log.d("Deleted");
		} catch (SQLiteException e) {
			return false;
		}
		return true;
	}

	/**
	 * 
	 * Xóa tất cả phòng ban
	 * @return
	 */
	public boolean deleteDept() {
		String xWhere ="";
		xWhere = KEY_MKBN + "=" + MasterConstants.MASTER_MKBN_DEPT;
		
		try {
			Log.d("Deleting");
			ContentValues contentValues = new ContentValues();
			contentValues.put(KEY_ISDELETED, 1);
			
			contentValues.put(KEY_UP_DATE, Utils.getCurrentDateTimeToString());
			
			Log.d("EDITING");
			db.update(TABLE_M_MEI, contentValues, xWhere, null);
			Log.d("EDITED");
			
			Log.d("Deleted");
		} catch (SQLiteException e) {
			return false;
		}
		return true;
	}
	
	/**
	 * 
	 * @param code : mã tổ nhóm cần delete
	 * @return
	 */
	public boolean deleteTeamByCode(Integer code) {
		String xWhere ="";
		xWhere = KEY_MKBN + "=" + MasterConstants.MASTER_MKBN_TEAM;
		xWhere = xWhere + " AND " + KEY_CODE+ "=" + code;
		try {
			ContentValues contentValues = new ContentValues();
			contentValues.put(KEY_ISDELETED, 1);
			
			contentValues.put(KEY_UP_DATE, Utils.getCurrentDateTimeToString());
			
			Log.d("Deleting");
			db.delete(TABLE_M_MEI, xWhere, null);
			Log.d("Deleted");
		} catch (SQLiteException e) {
			return false;
		}
		return true;
	}

	/**
	 * Xóa team
	 * @return
     */
	public boolean deleteTeam() {
		String xWhere ="";
		xWhere = KEY_MKBN + "=" + MasterConstants.MASTER_MKBN_TEAM;
		try {
			ContentValues contentValues = new ContentValues();
			contentValues.put(KEY_ISDELETED, 1);
			
			contentValues.put(KEY_UP_DATE, Utils.getCurrentDateTimeToString());
			
			Log.d("Deleting");
			db.delete(TABLE_M_MEI, xWhere, null);
			Log.d("Deleted");
		} catch (SQLiteException e) {
			return false;
		}
		return true;
	}
	/**
	 * 
	 * @param code : mã chức vụ cần delete
	 * @return
	 */
	public boolean deletePositionByCode(Integer code) {
		String xWhere ="";
		xWhere = KEY_MKBN + "=" + MasterConstants.MASTER_MKBN_POSITION;
		xWhere = xWhere + " AND " + KEY_CODE+ "=" + code;
		try {
			ContentValues contentValues = new ContentValues();
			contentValues.put(KEY_ISDELETED, 1);
			
			contentValues.put(KEY_UP_DATE, Utils.getCurrentDateTimeToString());
			
			Log.d("Deleting");
			db.delete(TABLE_M_MEI, xWhere, null);
			Log.d("Deleted");
		} catch (SQLiteException e) {
			return false;
		}
		return true;
	}

	/**
	 * 
	 * xóa tất cả chức vụ 
	 * 
	 */
	public boolean deletePosition() {
		String xWhere ="";
		xWhere = KEY_MKBN + "=" + MasterConstants.MASTER_MKBN_POSITION;
		try {
			ContentValues contentValues = new ContentValues();
			contentValues.put(KEY_ISDELETED, 1);
			
			contentValues.put(KEY_UP_DATE, Utils.getCurrentDateTimeToString());
			
			Log.d("Deleting");
			db.delete(TABLE_M_MEI, xWhere, null);
			Log.d("Deleted");
		} catch (SQLiteException e) {
			return false;
		}
		return true;
	}
	
	/**
	 * 
	 * @param code
	 * @return
	 */
	public boolean deleteUserHisDeptByCode(Integer code) {
		String xWhere ="";
		xWhere = KEY_MKBN + "=" + MasterConstants.MASTER_MKBN_DEPT_HIS;
		xWhere = xWhere + " AND " + KEY_CODE+ "=" + code;
		try {
			ContentValues contentValues = new ContentValues();
			contentValues.put(KEY_ISDELETED, 1);
			
			contentValues.put(KEY_UP_DATE, Utils.getCurrentDateTimeToString());
			
			Log.d("Deleting");
			db.delete(TABLE_M_USER_HIS, xWhere, null);
			Log.d("Deleted");
		} catch (SQLiteException e) {
			return false;
		}
		return true;
	}

	/**
	 * 
	 * @param xWhereAdd
	 * @return
	 */
	public boolean deleteUserHisDeptByCode(String xWhereAdd) {
		String xWhere ="";
		xWhere = KEY_MKBN + "=" + MasterConstants.MASTER_MKBN_DEPT_HIS;
		xWhere = xWhere + " " + xWhereAdd;
		try {
			Log.d("Deleting");
			db.delete(TABLE_M_USER_HIS, xWhere, null);
			Log.d("Deleted");
		} catch (SQLiteException e) {
			return false;
		}
		return true;
	}
	
	/**
	 * 
	 * @param xWhereAdd
	 * @return
	 */
	public boolean deleteUserHisTeamByCode(String xWhereAdd) {
		String xWhere ="";
		xWhere = KEY_MKBN + "=" + MasterConstants.MASTER_MKBN_TEAM_HIS;
		xWhere = xWhere + " " + xWhereAdd;
		try {
			Log.d("Deleting");
			db.delete(TABLE_M_USER_HIS, xWhere, null);
			Log.d("Deleted");
		} catch (SQLiteException e) {
			return false;
		}
		return true;
	}
	
	/**
	 * 
	 * @param xWhereAdd
	 * @return
	 */
	public boolean deleteUserHisPositionByCode(String xWhereAdd) {
		String xWhere ="";
		xWhere = KEY_MKBN + "=" + MasterConstants.MASTER_MKBN_POSITION_HIS;
		xWhere = xWhere + " " + xWhereAdd;
		try {
			Log.d("Deleting");
			db.delete(TABLE_M_USER_HIS, xWhere, null);
			Log.d("Deleted");
		} catch (SQLiteException e) {
			return false;
		}
		return true;
	}
	
	/**
	 * 
	 * @param xWhereAdd
	 * @return
	 */
	public boolean deleteUserHisJapaneseByCode(String xWhereAdd) {
		String xWhere ="";
		xWhere = KEY_MKBN + "=" + MasterConstants.MASTER_MKBN_JAPANESE_HIS;
		xWhere = xWhere + " " + xWhereAdd;
		try {
			Log.d("Deleting");
			db.delete(TABLE_M_USER_HIS, xWhere, null);
			Log.d("Deleted");
		} catch (SQLiteException e) {
			return false;
		}
		return true;
	}
	
	/**
	 * 
	 * @param xWhereAdd
	 * @return
	 */
	public boolean deleteUserHisAllowance_BusinessByCode(String xWhereAdd) {
		String xWhere ="";
		xWhere = KEY_MKBN + "=" + MasterConstants.MASTER_MKBN_ALLOWANCE_BUSINESS_HIS;
		xWhere = xWhere + " " + xWhereAdd;
		try {
			Log.d("Deleting");
			db.delete(TABLE_M_USER_HIS, xWhere, null);
			Log.d("Deleted");
		} catch (SQLiteException e) {
			return false;
		}
		return true;
	}

	/**
	 *
	 * @param xWhereAdd
	 * @return
	 */
	public boolean deleteUserHisAllowance_BSEByCode(String xWhereAdd) {
		String xWhere ="";
		xWhere = KEY_MKBN + "=" + MasterConstants.MASTER_MKBN_ALLOWANCE_BSE_HIS;
		xWhere = xWhere + " " + xWhereAdd;
		try {
			Log.d("Deleting");
			db.delete(TABLE_M_USER_HIS, xWhere, null);
			Log.d("Deleted");
		} catch (SQLiteException e) {
			return false;
		}
		return true;
	}
	/**
	 * Xoa lich su xet luong
	 * @param xWhereAdd
	 * @return
	 */
	public boolean deleteUserHisAllowance_SalaryByCode(String xWhereAdd) {
		String xWhere ="";
		xWhere = KEY_MKBN + "=" + MasterConstants.MASTER_MKBN_SALARY_HIS;
		xWhere = xWhere + " " + xWhereAdd;
		try {
			Log.d("Deleting");
			db.delete(TABLE_M_USER_HIS, xWhere, null);
			Log.d("Deleted");
		} catch (SQLiteException e) {
			return false;
		}
		return true;
	}
	/**
	 * 
	 * @param xWhereAdd
	 * @return
	 */
	public boolean deleteUserHisSalaryByCode(String xWhereAdd) {
		String xWhere ="";
		xWhere = KEY_MKBN + "=" + MasterConstants.MASTER_MKBN_SALARY_HIS;
		xWhere = xWhere + " " + xWhereAdd;
		try {
			Log.d("Deleting");
			db.delete(TABLE_M_USER_HIS, xWhere, null);
			Log.d("Deleted");
		} catch (SQLiteException e) {
			return false;
		}
		return true;
	}
	
	
	/**
	 * 
	 * @param codeRecord
	 * @return
	 */
	public boolean deleteUserHisByCodeRecord(int codeRecord) {
		String xWhere ="";
		xWhere = KEY_CODE + "=" + codeRecord;
		//xWhere = xWhere + " " + xWhereAdd;
		try {
			Log.d("Deleting");
			db.delete(TABLE_M_USER_HIS, xWhere, null);
			Log.d("Deleted");
		} catch (SQLiteException e) {
			return false;
		}
		return true;
	}
	
	/**
	 * 
	 * @param code : mã nhóm chức danh cần delete
	 * @return
	 */
	public boolean deletePositionGroupByCode(Integer code) {
		String xWhere ="";
		xWhere = KEY_MKBN + "=" + MasterConstants.MASTER_MKBN_POSITION_GROUP;
		xWhere = xWhere + " AND " + KEY_CODE+ "=" + code;
		try {
			ContentValues contentValues = new ContentValues();
			contentValues.put(KEY_ISDELETED, 1);
			
			Log.d("Deleting");
			db.delete(TABLE_M_MEI, xWhere, null);
			Log.d("Deleted");
		} catch (SQLiteException e) {
			return false;
		}
		return true;
	}

	/**
	 * 
	 * xóa tất cả nhóm chức danh 
	 * 
	 */
	public boolean deletePositionGroup() {
		String xWhere ="";
		xWhere = KEY_MKBN + "=" + MasterConstants.MASTER_MKBN_POSITION_GROUP;
		try {
			ContentValues contentValues = new ContentValues();
			contentValues.put(KEY_ISDELETED, 1);
			
			Log.d("Deleting");
			db.delete(TABLE_M_MEI, xWhere, null);
			Log.d("Deleted");
		} catch (SQLiteException e) {
			return false;
		}
		return true;
	}


	/**
	 * 
	 * xóa tất cả nhóm khách hàng 
	 * 
	 */
	public boolean deleteCustomerGroup() {
		String xWhere ="";
		xWhere = KEY_MKBN + "=" + MasterConstants.MASTER_MKBN_CUSTOMER_GROUP;
		try {
			ContentValues contentValues = new ContentValues();
			contentValues.put(KEY_ISDELETED, 1);
			
			Log.d("Deleting");
			db.delete(TABLE_M_MEI, xWhere, null);
			Log.d("Deleted");
		} catch (SQLiteException e) {
			return false;
		}
		return true;
	}
	
	/**
	 * 
	 * xóa tất cả chức danh của user 
	 * 
	 */
	public boolean deleteUserPositionGroup(int user_code) {
		String xWhere ="";
		xWhere = KEY_USER_CODE + "=" + user_code;
		try {
			Log.d("Deleting");
			db.delete(TABLE_M_USER_POSITION_GROUP, xWhere, null);
			Log.d("Deleted");
		} catch (SQLiteException e) {
			return false;
		}
		return true;
	}
	
	/**
	 * 
	 * xóa tất cả khach hang của user 
	 * 
	 */
	public boolean deleteUserCustomerGroup(int user_code) {
		String xWhere ="";
		xWhere = KEY_USER_CODE + "=" + user_code;
		try {
			Log.d("Deleting");
			
			db.delete(TABLE_M_USER_CUSTOMER_GROUP, xWhere, null);
			Log.d("Deleted");
		} catch (SQLiteException e) {
			return false;
		}
		return true;
	}
	/**
	 * 
	 * @param code : mã nhân viên cần delete
	 * @return
	 */
	public boolean deleteUserByCode(int code ) {
		String xWhere ="";
		xWhere = KEY_CODE + "=" + code;
		try {
			Log.d("Deleting");
			ContentValues contentValues = new ContentValues();
			contentValues.put(KEY_ISDELETED, 1);
			
			Log.d("EDITING");
			db.update(TABLE_M_USER, contentValues, xWhere, null);
			Log.d("EDITED");
			
			Log.d("Deleted");
		} catch (SQLiteException e) {
			return false;
		}
		return true;
	}
	
	/**
	 * 
	 * @param code : mã nhân viên cần delete
	 * @return
	 */
	public boolean deletePernamentUserByCode(int code ) {
		String xWhere ="";
		xWhere = KEY_CODE + "=" + code;
		try {
			
			Log.d("DELETE");
			db.delete(TABLE_M_USER, xWhere, null);
			Log.d("DELETED");
			
			Log.d("Deleted");
		} catch (SQLiteException e) {
			return false;
		}
		return true;
	}
	
	/**
	 * 
	 * @param listCode : mã nhân viên cần delete
	 * @return
	 */
	public boolean deleteUserByCode(String  listCode ) {
		String xWhere ="";
		xWhere = KEY_CODE + " IN (" + listCode + ")";
		try {
			Log.d("Deleting");
			ContentValues contentValues = new ContentValues();
			contentValues.put(KEY_ISDELETED, 1);
			
			Log.d("EDITING");
			db.update(TABLE_M_USER, contentValues, xWhere, null);
			Log.d("EDITED");
			
			Log.d("Deleted");
		} catch (SQLiteException e) {
			return false;
		}
		return true;
	}

	/**
	 * 
	 * @param listCode : mã nhân viên cần delete
	 * @return
	 */
	public boolean deletePernamentUserByCode(String  listCode ) {
		String xWhere ="";
		xWhere = KEY_CODE + " IN (" + listCode + ")";
		try {
			Log.d("DELETING");
			db.delete(TABLE_M_USER, xWhere, null);
			Log.d("DELETED");
			
			Log.d("Deleted");
		} catch (SQLiteException e) {
			return false;
		}
		return true;
	}
	
	/**
	 * 
	 * @param listCode : mã nhân viên cần phục hồi undelete
	 * @return
	 */
	public boolean unDeleteUserByCode(String  listCode ) {
		String xWhere ="";
		xWhere = KEY_CODE + " IN (" + listCode + ")";
		try {
			Log.d("UnDeleting");
			ContentValues contentValues = new ContentValues();
			contentValues.put(KEY_ISDELETED, 0);
			
			Log.d("EDITING");
			db.update(TABLE_M_USER, contentValues, xWhere, null);
			Log.d("EDITED");
			
			Log.d("UnDelete");
		} catch (SQLiteException e) {
			return false;
		}
		return true;
	}
	
	/**
	 * 
	 * xóa tất cả hinh anh user 
	 * 
	 */
	public boolean deleteUserImage(int user_code) {
		String xWhere ="";
		if (user_code!=0){
			xWhere = KEY_USER_CODE + "=" + user_code;
		}

		try {
			Log.d("Deleting");
			db.delete(TABLE_M_USER_IMAGE, xWhere, null);
			Log.d("Deleted");
		} catch (SQLiteException e) {
			return false;
		}
		return true;
	}
	
	/**
	 * 
	 * @return Cursor (danh sách các phòng ban )
	 */
	public Cursor getDeptList(String xWhereAdd) {
		String xWhere ="";
		String xOrder = "ORDER BY " + KEY_CODE + " ASC" ;
		//chỉ định điều kiện trích xuất theo class phòng ban
		xWhere = KEY_MKBN + "=" + MasterConstants.MASTER_MKBN_DEPT;
		xWhere =xWhere + "  " + xWhereAdd;
		xWhere =xWhere + "  " + xOrder;
		return db.query(TABLE_M_MEI, null, xWhere, null, null, null, null);
	}
	/**
	 * 
	 * @param code (code phòng ban đối tượng)
	 * @return Cursor
	 */
	public Cursor getDeptByCode(Integer code ) {
		String xWhere ="";
		
		xWhere = KEY_MKBN + "=" + MasterConstants.MASTER_MKBN_DEPT;
		xWhere = xWhere +  " AND " + KEY_CODE + "=" + code;
		
		return db.query(TABLE_M_MEI, null, xWhere, null, null, null, null);
	}
	/**
	 * 
	 * @return Cursor (danh sách các team)
	 */
	public Cursor getTeamList(String xWhereAdd) {
		String xWhere ="";
		//chỉ định điều kiện trích xuất theo class phòng ban
		xWhere = KEY_MKBN + "=" + MasterConstants.MASTER_MKBN_TEAM;
		xWhere =xWhere + xWhereAdd;
		
		return db.query(TABLE_M_MEI, null, xWhere, null, null, null, null);
	}
	/**
	 * 
	 * @param code (code team đối tượng)
	 * @return Cursor
	 */
	public Cursor getTeamByCode(Integer code ) {
		String xWhere ="";
		
		xWhere = KEY_MKBN + "=" + MasterConstants.MASTER_MKBN_TEAM;
		xWhere = xWhere +  " AND " + KEY_CODE + "=" + code;
		
		return db.query(TABLE_M_MEI, null, xWhere, null, null, null, null);
	}
	/**
	 * 
	 * @return Cursor (danh sách các chức vụ trong công ty)
	 */
	public Cursor getPositionList(String xWhereAdd) {
		String xWhere ="";
		//chỉ định điều kiện trích xuất theo class phòng ban
		xWhere = KEY_MKBN + "=" + MasterConstants.MASTER_MKBN_POSITION;
		xWhere =xWhere + xWhereAdd;
		
		return db.query(TABLE_M_MEI, null, xWhere, null, null, null, null);
	}
	/**
	 * 
	 * @param code (code chức vụ đối tượng)
	 * @return Cursor
	 */
	public Cursor getPositionByCode(Integer code ) {
		String xWhere ="";
		
		xWhere = KEY_MKBN + "=" + MasterConstants.MASTER_MKBN_POSITION;
		xWhere = xWhere +  " AND " + KEY_CODE + "=" + code;
		
		return db.query(TABLE_M_MEI, null, xWhere, null, null, null, null);
	}
	/**
	 * 
	 * @return Cursor (danh sách các nhóm chức danh trong công ty)
	 */
	public Cursor getPositionGroupList(String xWhereAdd) {
		String xWhere ="";
		//chỉ định điều kiện trích xuất theo class nhóm chức danh
		xWhere = KEY_MKBN + "=" + MasterConstants.MASTER_MKBN_POSITION_GROUP;
		xWhere =xWhere + xWhereAdd;
		
		return db.query(TABLE_M_MEI, null, xWhere, null, null, null, null);
	}
	
	/**
	 * 
	 * @param code (code nhan vien)
	 * @return Cursor
	 */
	public Cursor getPositionGroupByCode(Integer code ) {
		String xWhere ="";
		
		xWhere = KEY_MKBN + "=" + MasterConstants.MASTER_MKBN_POSITION_GROUP;
		xWhere = xWhere +  " AND " + KEY_CODE + "=" + code;
		
		return db.query(TABLE_M_MEI, null, xWhere, null, null, null, null);
	}
	
	/**
	 * 
	 * @return Cursor (danh sách các khách hàng trong công ty)
	 */
	public Cursor getCustomerGroupList(String xWhereAdd) {
		String xWhere ="";
		//chỉ định điều kiện trích xuất theo class khach hàng
		xWhere = KEY_MKBN + "=" + MasterConstants.MASTER_MKBN_CUSTOMER_GROUP;
		xWhere =xWhere + xWhereAdd;
		
		return db.query(TABLE_M_MEI, null, xWhere, null, null, null, null);
	}
	
	/**
	 * 
	 * @param code (code nhan vien)
	 * @return Cursor
	 */
	public Cursor getCustomerGroupByCode(Integer code ) {
		String xWhere ="";
		
		xWhere = KEY_MKBN + "=" + MasterConstants.MASTER_MKBN_CUSTOMER_GROUP;
		xWhere = xWhere +  " AND " + KEY_CODE + "=" + code;
		
		return db.query(TABLE_M_MEI, null, xWhere, null, null, null, null);
	}
	
	/**
	 * 
	 * @return Cursor (danh sách các nhóm chức danh trong công ty)
	 */
	public Cursor getBusinessGroupList(String xWhereAdd) {
		String xWhere ="";
		//chỉ định điều kiện trích xuất theo class nhóm chức danh
		xWhere = KEY_MKBN + "=" + MasterConstants.MASTER_MKBN_BUSINESS_GROUP;
		xWhere =xWhere + xWhereAdd;
		
		return db.query(TABLE_M_MEI, null, xWhere, null, null, null, null);
	}
	
	/**
	 * 
	 * @param code (code nhan vien)
	 * @return Cursor
	 */
	public Cursor getBusinessGroupByCode(Integer code ) {
		String xWhere ="";
		
		xWhere = KEY_MKBN + "=" + MasterConstants.MASTER_MKBN_BUSINESS_GROUP;
		xWhere = xWhere +  " AND " + KEY_CODE + "=" + code;
		
		return db.query(TABLE_M_MEI, null, xWhere, null, null, null, null);
	}
	/**
	 * @param xWhere : chỉ định điều kiện giới hạn data
	 * @return Cursor (danh sách các nhân viên)
	 */
	public Cursor getUserList(String xWhere , String xOrderBy) {
		String xSql ="";
		if(xOrderBy==null || xOrderBy.equals("")){
			xSql = " SELECT * FROM " +  TABLE_M_USER + " WHERE 1=1 AND " + xWhere ;
		}else{
			xSql = " SELECT * FROM " +  TABLE_M_USER + " WHERE 1=1 AND " + xWhere + " ORDER BY " + xOrderBy;
		}
				
		return db.rawQuery(xSql, null);
		//return db.query(TABLE_M_USER, null, xWhere, null, null, null, null);
	}
	
	/**
	 * Get danh sach nhan vien tu VIEW DB
	 * @param xWhere : chỉ định điều kiện giới hạn data
	 * @return Cursor (danh sách các nhân viên)
	 */
	public Cursor getUserListFromView(String xWhere , String xOrderBy,String ViewName) {
		String xSql ="";
		if(ViewName==null|| ViewName.equals("")){
			if(xOrderBy==null || xOrderBy.equals("")){
				xSql = " SELECT * FROM " +  TABLE_M_USER + " WHERE 1=1 AND " + xWhere ;
			}else{
				xSql = " SELECT * FROM " +  TABLE_M_USER + " WHERE 1=1 AND " + xWhere + " ORDER BY " + xOrderBy;
			}	
		}else
		{
			if(xOrderBy==null || xOrderBy.equals("")){
				xSql = " SELECT * FROM " +  ViewName + " WHERE 1=1 AND " + xWhere ;
			}else{
				xSql = " SELECT * FROM " +  ViewName + " WHERE 1=1 AND " + xWhere + " ORDER BY " + xOrderBy;
			}	
		}
		
				
		return db.rawQuery(xSql, null);
		//return db.query(TABLE_M_USER, null, xWhere, null, null, null, null);
	}
	
	
	/**
	 * @param xKeyGroup : chỉ định cột cần group
	 * @return Cursor (danh sách các nhân viên)
	 */
	public Cursor getUserGroupByWithWhere(String xKeyGroup, String xWhere) {
		String xSql ="";
		/** chi get cac nhan vien chua bi delete */
		xSql = " SELECT " + xKeyGroup + " AS GRP ,COUNT(*) CNT  FROM " + TABLE_M_USER
				+ " WHERE 1=1 " + xWhere
				+ " GROUP BY " + xKeyGroup + " ORDER BY " + xKeyGroup + " ASC";   
		
		return db.rawQuery(xSql, null);
		
	}
	
	/**
	 * @param xKeyGroup : chỉ định cột cần group
	 * @param columnReturn : chỉ định tên cột cần get về
	 * @return Cursor (danh sách các nhân viên)
	 */
	public Cursor getUserGroupBy(String xKeyGroup, String columnReturn) {
		String xSql ="";
		/** chi get cac nhan vien chua bi delete */
		xSql = " SELECT MAX(" + columnReturn + ") AS GRP ,COUNT(*) CNT FROM " + TABLE_M_USER 
				+ " WHERE " + KEY_ISDELETED + " <> 1 "
				+ " GROUP BY " + xKeyGroup + " ORDER BY " + xKeyGroup + " ASC";  
		
		return db.rawQuery(xSql, null);
		
	}

	/**
	 * @param xKeyGroup : chỉ định cột cần group
	 * @param columnReturn : chỉ định tên cột cần get về
	 * @param xWhere : chỉ định điều kiện lọc data
	 * @return Cursor (danh sách các nhân viên)
	 */
	public Cursor getUserGroupBy(String xKeyGroup, String columnReturn, String xWhere) {
		String xSql ="";
		/** chi get cac nhan vien chua bi delete */
		xSql = " SELECT MAX(" + columnReturn + ") AS GRP,COUNT(*) CNT  FROM " + TABLE_M_USER 
				+ " WHERE 1=1 " + xWhere
				+ " GROUP BY " + xKeyGroup + " ORDER BY " + xKeyGroup + " ASC";  
		
		return db.rawQuery(xSql, null);
		
	}
	
	/**
	 * @param xKeyGroup : chỉ định cột cần group
	 * @return Cursor (danh sách các nhân viên)
	 */
	public Cursor getUserGroupByFromViewWithWhere(String xKeyGroup, String ViewName, String xWhere) {
		String xSql ="";
		String xSort="";
		
		if(xKeyGroup.equals(DatabaseAdapter.KEY_USER_GROUP_YASUMI_YEARMONTH_SQL)
				|| xKeyGroup.equals(DatabaseAdapter.KEY_USER_GROUP_YASUMI_YEARMONTH_SQL_YM)){
			xSort = " " + DatabaseAdapter.KEY_USER_GROUP_YASUMI_YEARMONTH_SORT ;
			xWhere = xWhere.replace(new GetSearchItemSetting(context).getYasumiSQLString(), " AND " + DatabaseAdapter.KEY_OUT_DATE + "<>''");
			//xWhere +=" AND " + DatabaseAdapter.KEY_OUT_DATE + "<>''" ;
			
			/** chi get cac nhan vien chua bi delete-get tu M_USER thay cho VIEW de TRANH truong hop record trung nhau */
			xSql = " SELECT " + xKeyGroup + " AS GRP ,COUNT(*) CNT FROM " + TABLE_M_USER
					+ " WHERE 1=1 " + xWhere
					+ " GROUP BY " + xKeyGroup + " ORDER BY " + xSort + " ASC";   
			/** sql get thong ke nhan vien theo NHAN CHINH THUC */
		}else if (xKeyGroup.equals(DatabaseAdapter.KEY_USER_GROUP_CONTRACT_YEARMONTH_KEYGROUP)){
			xSort = " " + DatabaseAdapter.KEY_USER_GROUP_CONTRACT_YEARMONTH_SORT ;	
			/** chi get cac nhan vien chua bi delete */
			xSql = " SELECT " + xKeyGroup + " AS GRP ,COUNT(*) CNT FROM " + ViewName
					+ " WHERE 1=1 " + xWhere
					+ " GROUP BY " + xKeyGroup + " ORDER BY " + xSort + " ASC"; 
		}
		else
		{
			xSort=xKeyGroup;
			
			/** chi get cac nhan vien chua bi delete */
			xSql = " SELECT " + xKeyGroup + " AS GRP ,COUNT(*) CNT FROM " + ViewName
					+ " WHERE 1=1 " + xWhere
					+ " GROUP BY " + xKeyGroup + " ORDER BY " + xSort + " ASC";   
			
		}
		
		return db.rawQuery(xSql, null);
		
	}
	
	/**
	 * @param xKeyGroup : chỉ định cột cần group
	 * @param columnReturn : chỉ định tên cột cần get về
	 * @return Cursor (danh sách các nhân viên)
	 */
	public Cursor getUserGroupByFromView1(String xKeyGroup, String columnReturn, String ViewName) {
		String xSql ="";
		/** chi get cac nhan vien chua bi delete */
		xSql = " SELECT MAX(" + columnReturn + ") AS GRP ,COUNT(*) CNT  FROM " + ViewName 
				+ " WHERE " + KEY_ISDELETED + " <> 1 "
				+ " GROUP BY " + xKeyGroup + " ORDER BY " + xKeyGroup + " ASC";  
		
		return db.rawQuery(xSql, null);
		
	}

	/**
	 * @param xKeyGroup : chỉ định cột cần group
	 * @param columnReturn : chỉ định tên cột cần get về
	 * @param xWhere : chỉ định điều kiện lọc data
	 * @return Cursor (danh sách các nhân viên)
	 */
	public Cursor getUserGroupByFromView(String xKeyGroup, String columnReturn, String xWhere, String ViewName) {
		String xSql ="";
		String xSort="";
		
		if(xKeyGroup.equals(DatabaseAdapter.KEY_USER_GROUP_YASUMI_YEARMONTH_SQL)
				|| xKeyGroup.equals(DatabaseAdapter.KEY_USER_GROUP_YASUMI_YEARMONTH_SQL_YM)){
			xSort = " " + DatabaseAdapter.KEY_USER_GROUP_YASUMI_YEARMONTH_SORT ;	
			xWhere = xWhere.replace(new GetSearchItemSetting(context).getYasumiSQLString(), " AND " + DatabaseAdapter.KEY_OUT_DATE + "<>''");
			//xWhere +=" AND " + DatabaseAdapter.KEY_OUT_DATE + "<>''" ;
		}else if(xKeyGroup.equals(DatabaseAdapter.KEY_USER_GROUP_CONTRACT_YEARMONTH_SQL)
				|| xKeyGroup.equals(DatabaseAdapter.KEY_USER_GROUP_CONTRACT_YEARMONTH_SQL_YM)){
			xSort = " " + DatabaseAdapter.KEY_USER_GROUP_CONTRACT_YEARMONTH_SORT ;	
			//xWhere +=" AND " + DatabaseAdapter.KEY_OUT_DATE + "<>''" ;
		}else
			
		{
			xSort=xKeyGroup;
		}
		
		/** chi get cac nhan vien chua bi delete */
		xSql = " SELECT MAX(" + columnReturn + ") AS GRP ,COUNT(*) CNT  FROM " + ViewName 
				+ " WHERE 1=1 " + xWhere
				+ " GROUP BY " + xKeyGroup + " ORDER BY " + xSort + " ASC";  
		
		return db.rawQuery(xSql, null);
		
	}
	
	/**
	 * 
	 * @return Cursor (danh sách các nhóm chức danh trong công ty)
	 */
	public Cursor getUserPositionGroupList(int user_code , String xWhereAdd) {
		String xWhere ="";
		//chỉ định điều kiện trích xuất theo class nhóm chức danh
		xWhere = KEY_USER_CODE + "=" + user_code;
		xWhere =xWhere + xWhereAdd;
		
		return db.query(TABLE_M_USER_POSITION_GROUP, null, xWhere, null, null, null, null);
	}
	
	/**
	 * 
	 * @return Cursor (danh sách các khách hàng trong công ty)
	 */
	public Cursor getUserCustomerGroupList(int user_code , String xWhereAdd) {
		String xWhere ="";
		//chỉ định điều kiện trích xuất theo class nhóm chức danh
		xWhere = KEY_USER_CODE + "=" + user_code;
		xWhere =xWhere + xWhereAdd;
		
		return db.query(TABLE_M_USER_CUSTOMER_GROUP, null, xWhere, null, null, null, null);
	}
	
	/**
	 * 
	 * @return Cursor (danh sách các hinh anh nhan vien )
	 */
	public Cursor getUserImageList(String xWhereAdd) {
		String xWhere ="";
		String xOrder = "ORDER BY " + KEY_USER_CODE + " ASC" ;
		xWhere =xWhere + " 1=1 " + xWhereAdd;

		return db.query(TABLE_M_USER_IMAGE, null, xWhere, null, null, null,  null);
	}
	
	/**
	 * 
	 * @return true nếu tồn tại data tương ứng,false nếu không tồn tại
	 */
	public boolean isSonzaiUserPositionGroup(int user_code) {
		String xWhere ="";
		//chỉ định điều kiện trích xuất theo class nhóm chức danh
		xWhere = KEY_USER_CODE + "=" + user_code;
	
		Cursor cursor= db.query(TABLE_M_USER_POSITION_GROUP, null, xWhere, null, null, null, null);
		if (cursor.getCount()>0){
			return true;
		}else{
			return false;
		}
	}
	/**
	 * 
	 * @return true nếu tồn tại data tương ứng,false nếu không tồn tại
	 */
	public boolean isSonzaiUserCustomerGroup(int user_code) {
		String xWhere ="";
		//chỉ định điều kiện trích xuất theo class nhóm chức danh
		xWhere = KEY_USER_CODE + "=" + user_code;
	
		Cursor cursor= db.query(TABLE_M_USER_CUSTOMER_GROUP, null, xWhere, null, null, null, null);
		if (cursor.getCount()>0){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * 
	 * @return Cursor (danh sách phan loai nghiep vu)
	 */
	public Cursor getUserBusinessGroupList(int user_code , String xWhereAdd) {
		String xWhere ="";
		//chỉ định điều kiện trích xuất theo class nhóm chức danh
		xWhere = KEY_USER_CODE + "=" + user_code;
		xWhere =xWhere + xWhereAdd;
		
		return db.query(TABLE_M_USER_BUSINESS, null, xWhere, null, null, null, null);
	}
	
	/**
	 * 
	 * @return true nếu tồn tại data tương ứng,false nếu không tồn tại
	 */
	public boolean isSonzaiUserBusinessGroup(int user_code) {
		String xWhere ="";
		//chỉ định điều kiện trích xuất theo class nhóm chức danh
		xWhere = KEY_USER_CODE + "=" + user_code;
	
		Cursor cursor= db.query(TABLE_M_USER_BUSINESS, null, xWhere, null, null, null, null);
		if (cursor.getCount()>0){
			return true;
		}else{
			return false;
		}
			
	}
	
	/**
	 * 
	 * @return Cursor (danh sách lich su)
	 */
	public Cursor getUserHisDeptList(String xWhereAdd , String xOrderBy) {
		String xSelect ="";
		String xWhere ="";
		
		xSelect = xSelect + " SELECT ";
		xSelect = xSelect + " 		HIS.*";
		xSelect = xSelect + ", 		USR." + KEY_FULL_NAME;
		xSelect = xSelect + ", 		USR." + KEY_IMG_FULLPATH;
		xSelect = xSelect + ", 		MEI." + KEY_NAME 	+ " AS " + KEY_DEPT_NAME;
		xSelect = xSelect + ", 		MET." + KEY_NAME	+ " AS " + KEY_TEAM_NAME;
		xSelect = xSelect + ", 		MEP." + KEY_YOBI_TEXT1	+ " AS " + KEY_POSITION_NAME;
		xSelect = xSelect + " FROM ";
		xSelect = xSelect + " 		" + TABLE_M_USER_HIS + " AS  HIS ";
		xSelect = xSelect + " LEFT OUTER JOIN  " + TABLE_M_USER + " AS USR";
		xSelect = xSelect + " ON HIS." + KEY_USER_CODE + " = USR." + KEY_CODE;
		xSelect = xSelect + " LEFT OUTER JOIN  " + TABLE_M_MEI + " AS MEI";
		xSelect = xSelect + " ON HIS." + KEY_NEW_DEPT_CODE + " = MEI." + KEY_CODE;
		xSelect = xSelect + " AND MEI." + KEY_MKBN + " = " + MasterConstants.MASTER_MKBN_DEPT;
		xSelect = xSelect + " LEFT OUTER JOIN  " + TABLE_M_MEI + " AS MET";
		xSelect = xSelect + " ON HIS." + KEY_NEW_TEAM_CODE + " = MET." + KEY_CODE;
		xSelect = xSelect + " AND MET." + KEY_MKBN + " = " + MasterConstants.MASTER_MKBN_TEAM;
		xSelect = xSelect + " LEFT OUTER JOIN  " + TABLE_M_MEI + " AS MEP";
		xSelect = xSelect + " ON HIS." + KEY_NEW_POSITION_CODE + " = MEP." + KEY_CODE;
		xSelect = xSelect + " AND MEP." + KEY_MKBN + " = " + MasterConstants.MASTER_MKBN_POSITION;
		
		xSelect = xSelect + " WHERE ";
		xSelect = xSelect + " 1=1 ";
		
		xSelect = xSelect + " AND HIS." + KEY_USER_CODE + " IS NOT NULL";
		xSelect = xSelect + " AND HIS." + KEY_MKBN + " = " + MasterConstants.MASTER_MKBN_DEPT_HIS;
		xWhere =xWhere + xWhereAdd;
		if (xOrderBy.equals("")|| xOrderBy==null){
			xSelect = xSelect + xWhere;
		}else{
			xSelect = xSelect + xWhere + " ORDER BY " + xOrderBy;
		}
		return db.rawQuery(xSelect , null);
		
		//return db.query(TABLE_M_USER_HIS, null, xWhere, null, null, null, xOrderBy);
	}
	/**
	 * 
	 * @param code (code đối tượng)
	 * @return Cursor
	 */
	public Cursor getUserHisDeptByCode(Integer code ) {
		String xWhere ="";
		
		xWhere = KEY_MKBN + "=" + MasterConstants.MASTER_MKBN_DEPT_HIS;
		xWhere = xWhere +  " AND " + KEY_CODE + "=" + code;
		
		return db.query(TABLE_M_USER_HIS, null, xWhere, null, null, null, null);
	}
	
	/**
	 * 
	 * @return Cursor (danh sách lich su)
	 */
	public Cursor getUserHisTeamList(String xWhereAdd, String xOrderBy) {
		String xSelect ="";
		String xWhere ="";
		
		xSelect = xSelect + " SELECT ";
		xSelect = xSelect + " 		HIS.*";
		xSelect = xSelect + ", 		USR." + KEY_FULL_NAME;
		xSelect = xSelect + ", 		USR." + KEY_IMG_FULLPATH;
		xSelect = xSelect + ", 		MEI." + KEY_NAME 	+ " AS " + KEY_DEPT_NAME;
		xSelect = xSelect + ", 		MET." + KEY_NAME	+ " AS " + KEY_TEAM_NAME;
		xSelect = xSelect + ", 		MEP." + KEY_YOBI_TEXT1	+ " AS " + KEY_POSITION_NAME;
		xSelect = xSelect + " FROM ";
		xSelect = xSelect + " 		" + TABLE_M_USER_HIS + " AS  HIS ";
		xSelect = xSelect + " LEFT OUTER JOIN  " + TABLE_M_USER + " AS USR";
		xSelect = xSelect + " ON HIS." + KEY_USER_CODE + " = USR." + KEY_CODE;
		xSelect = xSelect + " LEFT OUTER JOIN  " + TABLE_M_MEI + " AS MEI";
		xSelect = xSelect + " ON HIS." + KEY_NEW_DEPT_CODE + " = MEI." + KEY_CODE;
		xSelect = xSelect + " AND MEI." + KEY_MKBN + " = " + MasterConstants.MASTER_MKBN_DEPT;
		xSelect = xSelect + " LEFT OUTER JOIN  " + TABLE_M_MEI + " AS MET";
		xSelect = xSelect + " ON HIS." + KEY_NEW_TEAM_CODE + " = MET." + KEY_CODE;
		xSelect = xSelect + " AND MET." + KEY_MKBN + " = " + MasterConstants.MASTER_MKBN_TEAM;
		xSelect = xSelect + " LEFT OUTER JOIN  " + TABLE_M_MEI + " AS MEP";
		xSelect = xSelect + " ON HIS." + KEY_NEW_POSITION_CODE + " = MEP." + KEY_CODE;
		xSelect = xSelect + " AND MEP." + KEY_MKBN + " = " + MasterConstants.MASTER_MKBN_POSITION;
		
		xSelect = xSelect + " WHERE ";
		xSelect = xSelect + " 1=1 ";
		xSelect = xSelect + " AND HIS." + KEY_USER_CODE + " IS NOT NULL";
		xSelect = xSelect + " AND HIS." + KEY_MKBN + " = " + MasterConstants.MASTER_MKBN_TEAM_HIS;
		xWhere =xWhere + xWhereAdd;
		if (xOrderBy.equals("")|| xOrderBy==null){
			xSelect = xSelect + xWhere;
		}else{
			xSelect = xSelect + xWhere + " ORDER BY " + xOrderBy;
		}
		return db.rawQuery(xSelect , null);
		
		
	}
	/**
	 * 
	 * @param code (code đối tượng)
	 * @return Cursor
	 */
	public Cursor getUserHisTeamByCode(Integer code ) {
		String xWhere ="";
		
		xWhere = KEY_MKBN + "=" + MasterConstants.MASTER_MKBN_TEAM_HIS;
		xWhere = xWhere +  " AND " + KEY_CODE + "=" + code;
		
		return db.query(TABLE_M_USER_HIS, null, xWhere, null, null, null, null);
	}
	
	/**
	 * 
	 * @return Cursor (danh sách lich su)
	 */
	public Cursor getUserHisPositionList(String xWhereAdd, String xOrderBy) {
		String xSelect ="";
		String xWhere ="";
		
		xSelect = xSelect + " SELECT ";
		xSelect = xSelect + " 		HIS.*";
		xSelect = xSelect + ", 		USR." + KEY_FULL_NAME;
		xSelect = xSelect + ", 		USR." + KEY_IMG_FULLPATH;
		xSelect = xSelect + ", 		MEI." + KEY_NAME 	+ " AS " + KEY_DEPT_NAME;
		xSelect = xSelect + ", 		MET." + KEY_NAME	+ " AS " + KEY_TEAM_NAME;
		xSelect = xSelect + ", 		MEP." + KEY_YOBI_TEXT1	+ " AS " + KEY_POSITION_NAME;
		xSelect = xSelect + " FROM ";
		xSelect = xSelect + " 		" + TABLE_M_USER_HIS + " AS  HIS ";
		xSelect = xSelect + " LEFT OUTER JOIN  " + TABLE_M_USER + " AS USR";
		xSelect = xSelect + " ON HIS." + KEY_USER_CODE + " = USR." + KEY_CODE;
		
		xSelect = xSelect + " LEFT OUTER JOIN  " + TABLE_M_MEI + " AS MEI";
		xSelect = xSelect + " ON HIS." + KEY_NEW_DEPT_CODE + " = MEI." + KEY_CODE;
		xSelect = xSelect + " AND MEI." + KEY_MKBN + " = " + MasterConstants.MASTER_MKBN_DEPT;
		xSelect = xSelect + " LEFT OUTER JOIN  " + TABLE_M_MEI + " AS MET";
		xSelect = xSelect + " ON HIS." + KEY_NEW_TEAM_CODE + " = MET." + KEY_CODE;
		xSelect = xSelect + " AND MET." + KEY_MKBN + " = " + MasterConstants.MASTER_MKBN_TEAM;
		xSelect = xSelect + " LEFT OUTER JOIN  " + TABLE_M_MEI + " AS MEP";
		xSelect = xSelect + " ON HIS." + KEY_NEW_POSITION_CODE + " = MEP." + KEY_CODE;
		xSelect = xSelect + " AND MEP." + KEY_MKBN + " = " + MasterConstants.MASTER_MKBN_POSITION;
		
		xSelect = xSelect + " WHERE ";
		xSelect = xSelect + " 1=1 ";
		
		xSelect = xSelect + " AND HIS." + KEY_USER_CODE + " IS NOT NULL";
		xSelect = xSelect + " AND HIS." + KEY_MKBN + " = " + MasterConstants.MASTER_MKBN_POSITION_HIS;
		xWhere =xWhere + xWhereAdd;
		if (xOrderBy.equals("")|| xOrderBy==null){
			xSelect = xSelect + xWhere;
		}else{
			xSelect = xSelect + xWhere + " ORDER BY " + xOrderBy;
		}
		return db.rawQuery(xSelect , null);
		
		
	}
	/**
	 * 
	 * @param code (code đối tượng)
	 * @return Cursor
	 */
	public Cursor getUserPositionTeamByCode(Integer code ) {
		String xWhere ="";
		
		xWhere = KEY_MKBN + "=" + MasterConstants.MASTER_MKBN_POSITION_HIS;
		xWhere = xWhere +  " AND " + KEY_CODE + "=" + code;
		
		return db.query(TABLE_M_USER_HIS, null, xWhere, null, null, null, null);
	}
	
	/**
	 * 
	 * @return Cursor (danh sách lich su)
	 */
	public Cursor getUserHisJapaneseList(String xWhereAdd, String xOrderBy) {
		String xSelect ="";
		String xWhere ="";
		
		xSelect = xSelect + " SELECT ";
		xSelect = xSelect + " 		HIS.*";
		xSelect = xSelect + ", 		USR." + KEY_FULL_NAME;
		xSelect = xSelect + ", 		USR." + KEY_IMG_FULLPATH;
		xSelect = xSelect + ", 		MEI." + KEY_NAME 	+ " AS " + KEY_DEPT_NAME;
		xSelect = xSelect + ", 		MET." + KEY_NAME	+ " AS " + KEY_TEAM_NAME;
		xSelect = xSelect + ", 		MEP." + KEY_YOBI_TEXT1	+ " AS " + KEY_POSITION_NAME;
		xSelect = xSelect + " FROM ";
		xSelect = xSelect + " 		" + TABLE_M_USER_HIS + " AS  HIS ";
		xSelect = xSelect + " LEFT OUTER JOIN  " + TABLE_M_USER + " AS USR";
		xSelect = xSelect + " ON HIS." + KEY_USER_CODE + " = USR." + KEY_CODE;
		
		xSelect = xSelect + " LEFT OUTER JOIN  " + TABLE_M_MEI + " AS MEI";
		xSelect = xSelect + " ON HIS." + KEY_NEW_DEPT_CODE + " = MEI." + KEY_CODE;
		xSelect = xSelect + " AND MEI." + KEY_MKBN + " = " + MasterConstants.MASTER_MKBN_DEPT;
		xSelect = xSelect + " LEFT OUTER JOIN  " + TABLE_M_MEI + " AS MET";
		xSelect = xSelect + " ON HIS." + KEY_NEW_TEAM_CODE + " = MET." + KEY_CODE;
		xSelect = xSelect + " AND MET." + KEY_MKBN + " = " + MasterConstants.MASTER_MKBN_TEAM;
		xSelect = xSelect + " LEFT OUTER JOIN  " + TABLE_M_MEI + " AS MEP";
		xSelect = xSelect + " ON HIS." + KEY_NEW_POSITION_CODE + " = MEP." + KEY_CODE;
		xSelect = xSelect + " AND MEP." + KEY_MKBN + " = " + MasterConstants.MASTER_MKBN_POSITION;
		
		xSelect = xSelect + " WHERE ";
		xSelect = xSelect + " 1=1 ";
		xSelect = xSelect + " AND HIS." + KEY_USER_CODE + " IS NOT NULL";
		xSelect = xSelect + " AND HIS." + KEY_MKBN + " = " + MasterConstants.MASTER_MKBN_JAPANESE_HIS;
		xWhere =xWhere + xWhereAdd;
		if (xOrderBy.equals("")|| xOrderBy==null){
			xSelect = xSelect + xWhere;
		}else{
			xSelect = xSelect + xWhere + " ORDER BY " + xOrderBy;
		}
		return db.rawQuery(xSelect , null);
		
		
	}
	
	/**
	 * 
	 * @return Cursor (danh sách lich su)
	 */
	public Cursor getUserHisAllowance_BusinessList(String xWhereAdd, String xOrderBy) {
		String xSelect ="";
		String xWhere ="";
		
		xSelect = xSelect + " SELECT ";
		xSelect = xSelect + " 		HIS.*";
		xSelect = xSelect + ", 		USR." + KEY_FULL_NAME;
		xSelect = xSelect + ", 		USR." + KEY_IMG_FULLPATH;
		xSelect = xSelect + ", 		MEI." + KEY_NAME 	+ " AS " + KEY_DEPT_NAME;
		xSelect = xSelect + ", 		MET." + KEY_NAME	+ " AS " + KEY_TEAM_NAME;
		xSelect = xSelect + ", 		MEP." + KEY_YOBI_TEXT1	+ " AS " + KEY_POSITION_NAME;
		xSelect = xSelect + " FROM ";
		xSelect = xSelect + " 		" + TABLE_M_USER_HIS + " AS  HIS ";
		xSelect = xSelect + " LEFT OUTER JOIN  " + TABLE_M_USER + " AS USR";
		xSelect = xSelect + " ON HIS." + KEY_USER_CODE + " = USR." + KEY_CODE;
		
		xSelect = xSelect + " LEFT OUTER JOIN  " + TABLE_M_MEI + " AS MEI";
		xSelect = xSelect + " ON HIS." + KEY_NEW_DEPT_CODE + " = MEI." + KEY_CODE;
		xSelect = xSelect + " AND MEI." + KEY_MKBN + " = " + MasterConstants.MASTER_MKBN_DEPT;
		xSelect = xSelect + " LEFT OUTER JOIN  " + TABLE_M_MEI + " AS MET";
		xSelect = xSelect + " ON HIS." + KEY_NEW_TEAM_CODE + " = MET." + KEY_CODE;
		xSelect = xSelect + " AND MET." + KEY_MKBN + " = " + MasterConstants.MASTER_MKBN_TEAM;
		xSelect = xSelect + " LEFT OUTER JOIN  " + TABLE_M_MEI + " AS MEP";
		xSelect = xSelect + " ON HIS." + KEY_NEW_POSITION_CODE + " = MEP." + KEY_CODE;
		xSelect = xSelect + " AND MEP." + KEY_MKBN + " = " + MasterConstants.MASTER_MKBN_POSITION;
		
		xSelect = xSelect + " WHERE ";
		xSelect = xSelect + " 1=1 ";
		xSelect = xSelect + " AND HIS." + KEY_USER_CODE + " IS NOT NULL";
		xSelect = xSelect + " AND HIS." + KEY_MKBN + " = " + MasterConstants.MASTER_MKBN_ALLOWANCE_BUSINESS_HIS;
		xWhere =xWhere + xWhereAdd;
		if (xOrderBy.equals("")|| xOrderBy==null){
			xSelect = xSelect + xWhere;
		}else{
			xSelect = xSelect + xWhere + " ORDER BY " + xOrderBy;
		}
		return db.rawQuery(xSelect , null);
		
		
	}

	/**
	 *
	 * @return Cursor (danh sách lich su)
	 */
	public Cursor getUserHisAllowance_BSEList(String xWhereAdd, String xOrderBy) {
		String xSelect ="";
		String xWhere ="";

		xSelect = xSelect + " SELECT ";
		xSelect = xSelect + " 		HIS.*";
		xSelect = xSelect + ", 		USR." + KEY_FULL_NAME;
		xSelect = xSelect + ", 		USR." + KEY_IMG_FULLPATH;
		xSelect = xSelect + ", 		MEI." + KEY_NAME 	+ " AS " + KEY_DEPT_NAME;
		xSelect = xSelect + ", 		MET." + KEY_NAME	+ " AS " + KEY_TEAM_NAME;
		xSelect = xSelect + ", 		MEP." + KEY_YOBI_TEXT1	+ " AS " + KEY_POSITION_NAME;
		xSelect = xSelect + " FROM ";
		xSelect = xSelect + " 		" + TABLE_M_USER_HIS + " AS  HIS ";
		xSelect = xSelect + " LEFT OUTER JOIN  " + TABLE_M_USER + " AS USR";
		xSelect = xSelect + " ON HIS." + KEY_USER_CODE + " = USR." + KEY_CODE;

		xSelect = xSelect + " LEFT OUTER JOIN  " + TABLE_M_MEI + " AS MEI";
		xSelect = xSelect + " ON HIS." + KEY_NEW_DEPT_CODE + " = MEI." + KEY_CODE;
		xSelect = xSelect + " AND MEI." + KEY_MKBN + " = " + MasterConstants.MASTER_MKBN_DEPT;
		xSelect = xSelect + " LEFT OUTER JOIN  " + TABLE_M_MEI + " AS MET";
		xSelect = xSelect + " ON HIS." + KEY_NEW_TEAM_CODE + " = MET." + KEY_CODE;
		xSelect = xSelect + " AND MET." + KEY_MKBN + " = " + MasterConstants.MASTER_MKBN_TEAM;
		xSelect = xSelect + " LEFT OUTER JOIN  " + TABLE_M_MEI + " AS MEP";
		xSelect = xSelect + " ON HIS." + KEY_NEW_POSITION_CODE + " = MEP." + KEY_CODE;
		xSelect = xSelect + " AND MEP." + KEY_MKBN + " = " + MasterConstants.MASTER_MKBN_POSITION;

		xSelect = xSelect + " WHERE ";
		xSelect = xSelect + " 1=1 ";
		xSelect = xSelect + " AND HIS." + KEY_USER_CODE + " IS NOT NULL";
		xSelect = xSelect + " AND HIS." + KEY_MKBN + " = " + MasterConstants.MASTER_MKBN_ALLOWANCE_BSE_HIS;
		xWhere =xWhere + xWhereAdd;
		if (xOrderBy.equals("")|| xOrderBy==null){
			xSelect = xSelect + xWhere;
		}else{
			xSelect = xSelect + xWhere + " ORDER BY " + xOrderBy;
		}
		return db.rawQuery(xSelect , null);


	}

	/**
	 * 
	 * @return Cursor (danh sách lich su)
	 */
	public Cursor getUserHisSalaryList(String xWhereAdd, String xOrderBy) {
		String xSelect ="";
		String xWhere ="";
		
		xSelect = xSelect + " SELECT ";
		xSelect = xSelect + " 		HIS.*";
		xSelect = xSelect + ", 		USR." + KEY_FULL_NAME;
		xSelect = xSelect + ", 		USR." + KEY_IMG_FULLPATH;
		xSelect = xSelect + ", 		MEI." + KEY_NAME 	+ " AS " + KEY_DEPT_NAME;
		xSelect = xSelect + ", 		MET." + KEY_NAME	+ " AS " + KEY_TEAM_NAME;
		xSelect = xSelect + ", 		MEP." + KEY_YOBI_TEXT1	+ " AS " + KEY_POSITION_NAME;
		xSelect = xSelect + " FROM ";
		xSelect = xSelect + " 		" + TABLE_M_USER_HIS + " AS  HIS ";
		xSelect = xSelect + " LEFT OUTER JOIN  " + TABLE_M_USER + " AS USR";
		xSelect = xSelect + " ON HIS." + KEY_USER_CODE + " = USR." + KEY_CODE;
		
		xSelect = xSelect + " LEFT OUTER JOIN  " + TABLE_M_MEI + " AS MEI";
		xSelect = xSelect + " ON HIS." + KEY_NEW_DEPT_CODE + " = MEI." + KEY_CODE;
		xSelect = xSelect + " AND MEI." + KEY_MKBN + " = " + MasterConstants.MASTER_MKBN_DEPT;
		xSelect = xSelect + " LEFT OUTER JOIN  " + TABLE_M_MEI + " AS MET";
		xSelect = xSelect + " ON HIS." + KEY_NEW_TEAM_CODE + " = MET." + KEY_CODE;
		xSelect = xSelect + " AND MET." + KEY_MKBN + " = " + MasterConstants.MASTER_MKBN_TEAM;
		xSelect = xSelect + " LEFT OUTER JOIN  " + TABLE_M_MEI + " AS MEP";
		xSelect = xSelect + " ON HIS." + KEY_NEW_POSITION_CODE + " = MEP." + KEY_CODE;
		xSelect = xSelect + " AND MEP." + KEY_MKBN + " = " + MasterConstants.MASTER_MKBN_POSITION;
		
		xSelect = xSelect + " WHERE ";
		xSelect = xSelect + " 1=1 ";
		xSelect = xSelect + " AND HIS." + KEY_USER_CODE + " IS NOT NULL";
		xSelect = xSelect + " AND HIS." + KEY_MKBN + " = " + MasterConstants.MASTER_MKBN_SALARY_HIS;
		xWhere =xWhere + xWhereAdd;
		if (xOrderBy.equals("")|| xOrderBy==null){
			xSelect = xSelect + xWhere;
		}else{
			xSelect = xSelect + xWhere + " ORDER BY " + xOrderBy;
		}
		return db.rawQuery(xSelect , null);
		
		
	}
	
	/**
	 * 
	 * @return lấy trị MAX của code phòng ban +1 
	 */
	public int getMaxCodeMaster(){
		int code =0;
		String xSql = "";
		try{
			xSql = "SELECT MAX( " + KEY_CODE + ") AS " + KEY_CODE + " FROM " + TABLE_M_MEI ;
			//xSql =xSql+ " WHERE " + KEY_MKBN + " = " + MasterConstants.MASTER_MKBN_DEPT;
			
			Cursor cur = db.rawQuery(xSql, null);
			if(cur.getCount()>0){
				cur.moveToFirst();
				do {
					code = cur.getInt(cur.getColumnIndex(DatabaseAdapter.KEY_CODE));
					cur.moveToNext();
				} while (!cur.isAfterLast());
			}
			cur.close();
			
		}catch(Exception e){
			return -1;
		}
		return ++code;
	}
	
	/**
	 * 
	 * @return lấy trị MAX của code user 
	 */
	public int getMaxCodeUser(){
		int code =0;
		String xSql = "";
		try{
			xSql = "SELECT MAX( " + KEY_CODE + ") AS " + KEY_CODE + " FROM " + TABLE_M_USER ;
			//xSql =xSql+ " WHERE " + KEY_MKBN + " = " + MasterConstants.MASTER_MKBN_DEPT;
			
			Cursor cur = db.rawQuery(xSql, null);
			if(cur.getCount()>0){
				cur.moveToFirst();
				do {
					code = cur.getInt(cur.getColumnIndex(DatabaseAdapter.KEY_CODE));
					cur.moveToNext();
				} while (!cur.isAfterLast());
			}
			cur.close();
			
		}catch(Exception e){
			return -1;
		}
		return ++code;
	}
	
	/**
	 * 
	 * @return lấy trị nhỏ hơn code user truyền vào 
	 */
	public int getPrevCodeUser(int usercode){
		int code =0;
		String xSql = "";
		try{
			
			xSql = "SELECT MAX( " + KEY_CODE + ") AS " + KEY_CODE + " FROM " 
					+ TABLE_M_USER + " WHERE 1=1 AND " + KEY_CODE + " < " + usercode;
			//xSql =xSql+ " WHERE " + KEY_MKBN + " = " + MasterConstants.MASTER_MKBN_DEPT;
			
			Cursor cur = db.rawQuery(xSql, null);
			if(cur.getCount()>0){
				cur.moveToFirst();
				do {
					code = cur.getInt(cur.getColumnIndex(DatabaseAdapter.KEY_CODE));
					cur.moveToNext();
				} while (!cur.isAfterLast());
			}
			cur.close();
			
		}catch(Exception e){
			return -1;
		}
		return code;
	}
	/**
	 * 
	 * @return lấy trị tiếp theo của code user truyền vào 
	 */
	public int getNextCodeUser(int usercode){
		int code =0;
		String xSql = "";
		try{
			
			xSql = "SELECT MIN( " + KEY_CODE + ") AS " + KEY_CODE + " FROM " 
					+ TABLE_M_USER + " WHERE 1=1 AND " + KEY_CODE + "> " + usercode;
			//xSql =xSql+ " WHERE " + KEY_MKBN + " = " + MasterConstants.MASTER_MKBN_DEPT;
			
			Cursor cur = db.rawQuery(xSql, null);
			if(cur.getCount()>0){
				cur.moveToFirst();
				do {
					code = cur.getInt(cur.getColumnIndex(DatabaseAdapter.KEY_CODE));
					cur.moveToNext();
				} while (!cur.isAfterLast());
			}
			cur.close();
			
		}catch(Exception e){
			return -1;
		}
		return code;
	}
	
	/** cap nhat lai so nam kinh nghiem lam viec , so nam kinh nghiem labour */
	public void updateKeikenByYear(){
		String xSql , xSqlUpdate="";
		int code=0;
		/** ngày vào công ty*/
        Date in_Date=null;
        String in_DateString ="";
        
        /** ngày tham gia nhom labour*/
        Date join_Date=null;
        String join_DateString ="";
        
        /** lấy ngày hiện tại */
        Date dateto =DateTimeUtil.getCurrentDate(MasterConstants.DATE_VN_FORMAT);
        
		try{
			xSql  = " SELECT * FROM " + TABLE_M_USER + ";" ;
			Cursor cur = db.rawQuery(xSql, null);
			if(cur.getCount()>0){
				cur.moveToFirst();
				do{
					in_Date=null;
					join_Date=null;
					/** lay code nhan vien */
					code = cur.getInt(cur.getColumnIndex(DatabaseAdapter.KEY_CODE));
					/** lay ngay vao cong ty */
					//in_DateString = cur.getString(cur.getColumnIndex(DatabaseAdapter.KEY_IN_DATE));
					in_DateString = DateTimeUtil.formatDate2String( cur.getString(cur.getColumnIndex(DatabaseAdapter.KEY_IN_DATE)),MasterConstants.DATE_JP_FORMAT,MasterConstants.DATE_VN_FORMAT);
					
					if (in_DateString!=null && in_DateString!=""){
						if (DateTimeUtil.isDate(in_DateString ,MasterConstants.DATE_VN_FORMAT)){
							in_Date = DateTimeUtil.convertStringToDate( in_DateString ,MasterConstants.DATE_VN_FORMAT);
			        	}
					}
					/** lay ngay gia nhap nhom labour*/
					join_DateString = DateTimeUtil.formatDate2String( cur.getString(cur.getColumnIndex(DatabaseAdapter.KEY_JOIN_DATE)),MasterConstants.DATE_JP_FORMAT,MasterConstants.DATE_VN_FORMAT);
					if (join_DateString!=null && join_DateString!=""){
						if (DateTimeUtil.isDate(join_DateString ,MasterConstants.DATE_VN_FORMAT)){
							join_Date = DateTimeUtil.convertStringToDate( join_DateString ,MasterConstants.DATE_VN_FORMAT);
			        	}
					}
					
					double keikenYear = 0;
					double keikenLabourYear =0;
					if(in_Date!=null){
						keikenYear = Utils.Round((DateTimeUtil.getFullMonthDiff(in_Date, dateto)/12.0),1,RoundingMode.HALF_UP);	
					}
					if(join_Date!=null){
						keikenLabourYear= Utils.Round((DateTimeUtil.getFullMonthDiff(join_Date, dateto)/12.0),1,RoundingMode.HALF_UP);	
					}
					 					
					/** cap nhat lai data User */
					//xSqlUpdate =" UPDATE " + TABLE_M_USER + " SET " + KEY_YOBI_REAL1 + " = " + keikenYear + " WHERE " + KEY_CODE + " = " + code;
					
					ContentValues contentValues = new ContentValues();
					/** So nam kinh nghiem tu ngay vao cong ty */
					if (String.valueOf(keikenYear) != null){
						contentValues.put(KEY_YOBI_REAL1, keikenYear);
					}
					/** So nam kinh nghiem nhom labour */
					if (String.valueOf(keikenLabourYear) != null){
						contentValues.put(KEY_YOBI_REAL2, keikenLabourYear);
					}
					
					String where = KEY_CODE + "=" + code  ;
					
					Log.d("TABLE_M_USER TRYING");
					long id = db.update(TABLE_M_USER,  contentValues, where, null);
					Log.d("TABLE_M_USER ADDED");
					
					/** di chuyen con tro */
					cur.moveToNext();
				} while(!cur.isLast());
			}
			cur.close();
		}catch (Exception e){

		}
	}
	
	/**
	 * Get nhan vien PD nghi viec theo tung thang nam
	 * @param xWhere
	 * @return
	 */
	public Cursor getPDYasumiByYearMonth(String xWhere){
		String sql =VIEW_PD_YASUMI_COUNT_BY_YEARMONTH_SQL;
		
		//xWhere = xWhere.replace(new GetSearchItemSetting(context).getYasumiSQLString(), "");
		sql = sql.replace(DatabaseAdapter.KEY_WHERE_STRING, xWhere);
		
		return db.rawQuery(sql , null);
		
	}
	
	/**
	 * Get nhan vien nghi viec theo tung thang nam
	 * @param xWhere
	 * @return
	 */
	public Cursor getDevYasumiByYearMonth(String xWhere){
		String sql =VIEW_DEV_YASUMI_COUNT_BY_YEARMONTH_SQL;
		
		//xWhere = xWhere.replace(new GetSearchItemSetting(context).getYasumiSQLString(), "");
		sql = sql.replace(DatabaseAdapter.KEY_WHERE_STRING, xWhere);
		
		return db.rawQuery(sql , null);
		
	}
	/**
	 * Get nhan vien PD thu viec theo tung thang nam
	 * @param xWhere
	 * @return
	 */
	public Cursor getPDTrainingByYearMonth(int year,String xWhere){
		String sql ="";
		int i =0;		
		
		//xWhere = xWhere.replace(new GetSearchItemSetting(context).getYasumiSQLString(), "");

		ArrayList<ChartItem> list12Month= DateTimeUtil.get12MonthFromYear(year);
		
		for(ChartItem item : list12Month){
			if(i==0)
			{
				sql = VIEW_PD_TRAINING_COUNT_BY_YEARMONTH_SQL.replace(KEY_YEARMONTH_STRING, item.KeyItem) ;
			}else{
				sql = sql + System.getProperty("line.separator") + " UNION ALL " + VIEW_PD_TRAINING_COUNT_BY_YEARMONTH_SQL.replace(KEY_YEARMONTH_STRING, item.KeyItem);
			}
			i++;
		}
		//sql 
		sql = sql.replace(DatabaseAdapter.KEY_WHERE_STRING, xWhere);
		
		return db.rawQuery(sql , null);
		
	}
	
	/**
	 * Get nhan vien thu viec theo tung thang nam
	 * @param xWhere
	 * @return
	 */
	public Cursor getDevTrainingByYearMonth(int year,String xWhere){
		String sql ="";
		int i =0;		
		
		//xWhere = xWhere.replace(new GetSearchItemSetting(context).getYasumiSQLString(), "");

		ArrayList<ChartItem> list12Month= DateTimeUtil.get12MonthFromYear(year);
		
		for(ChartItem item : list12Month){
			if(i==0)
			{
				sql = VIEW_DEV_TRAINING_COUNT_BY_YEARMONTH_SQL.replace(KEY_YEARMONTH_STRING, item.KeyItem) ;
			}else{
				sql = sql + System.getProperty("line.separator") + " UNION ALL " + VIEW_DEV_TRAINING_COUNT_BY_YEARMONTH_SQL.replace(KEY_YEARMONTH_STRING, item.KeyItem);
			}
			i++;
		}
		//sql 
		sql = sql.replace(DatabaseAdapter.KEY_WHERE_STRING, xWhere);
		
		return db.rawQuery(sql , null);
		
		
	}
	/**
	 * Get so nhan vien dang lam viec chinh thuc theo tung thang nam
	 * @param year
	 * @return
	 */
	public Cursor getDevWorkingByYearMonth(int year, String xWhere){
		String sql ="";
		int i =0;
		
		ArrayList<ChartItem> list12Month= DateTimeUtil.get12MonthFromYear(year);
		
		for(ChartItem item : list12Month){
			if(i==0)
			{
				sql = getSqlDevWorkingByOneMonth(item.KeyItem, xWhere);
			}else{
				sql = sql + System.getProperty("line.separator") + " UNION ALL " + getSqlDevWorkingByOneMonth(item.KeyItem, xWhere);
			}
			i++;
		}
		//sql 
		return db.rawQuery(sql , null);
	}
	/**
	 * Get so nhan vien dang lam viec chinh thuc theo tung thang nam
	 * @param year
	 * @return
	 */
	public Cursor getDevWorkingByTreeYearMonth(int year, String xWhere){
		String sql ="";
		int i =0;
		//---------nam hien tai
		ArrayList<ChartItem> list12Month= DateTimeUtil.get12MonthFromYear(year);
		
		for(ChartItem item : list12Month){
			if(i==0)
			{
				sql = getSqlDevWorkingByOneMonth(item.KeyItem, xWhere);
			}else{
				sql = sql + System.getProperty("line.separator") + " UNION ALL " + getSqlDevWorkingByOneMonth(item.KeyItem, xWhere);
			}
			i++;
		}
		//---------2 nam thu truoc
		list12Month= DateTimeUtil.get12MonthFromYear(year-1);
		
		for(ChartItem item : list12Month){
			sql = sql + System.getProperty("line.separator") + " UNION ALL " + getSqlDevWorkingByOneMonth(item.KeyItem, xWhere);
		}
		//---------3 nam thu truoc
		list12Month= DateTimeUtil.get12MonthFromYear(year-2);
		
		for(ChartItem item : list12Month){
			sql = sql + System.getProperty("line.separator") + " UNION ALL " + getSqlDevWorkingByOneMonth(item.KeyItem, xWhere);
		}
		//sql 
		return db.rawQuery(sql , null);
	}
	
	/**
	 * 2016.12.10
	 * Get so nhan vien thu viec trong ca nam 
	 * @param year
	 * @return
	 */
	public Cursor getDevTrainingByThreeYearMonth(int year, String xWhere){
		String sql ="";
		int i =0;
		//---------nam hien tai
		ArrayList<ChartItem> list12Month= DateTimeUtil.get12MonthFromYear(year);
		
		for(ChartItem item : list12Month){
			if(i==0)
			{
				sql = getSqlDevTrainingByOneMonth(item.KeyItem, xWhere);
			}else{
				sql = sql + System.getProperty("line.separator") + " UNION ALL " + getSqlDevTrainingByOneMonth(item.KeyItem, xWhere);
			}
			i++;
		}
		//---------2 nam thu truoc
		list12Month= DateTimeUtil.get12MonthFromYear(year-1);
		
		for(ChartItem item : list12Month){
			sql = sql + System.getProperty("line.separator") + " UNION ALL " + getSqlDevTrainingByOneMonth(item.KeyItem, xWhere);
		}
		//---------3 nam thu truoc
		list12Month= DateTimeUtil.get12MonthFromYear(year-2);
		
		for(ChartItem item : list12Month){
			sql = sql + System.getProperty("line.separator") + " UNION ALL " + getSqlDevTrainingByOneMonth(item.KeyItem, xWhere);
		}
		//sql 
		return db.rawQuery(sql , null);
	}
	
	/**
	 * 2016.12.10
	 * Get so nhan vien nhan chính thuc trong ca nam 
	 * @param year
	 * @return
	 */
	public Cursor getDevContractByThreeYearMonth(int year, String xWhere){
		String sql ="";
		int i =0;
		//---------nam hien tai
		ArrayList<ChartItem> list12Month= DateTimeUtil.get12MonthFromYear(year);
		
		for(ChartItem item : list12Month){
			if(i==0)
			{
				sql = getSqlDevContractByOneMonth(item.KeyItem, xWhere);
			}else{
				sql = sql + System.getProperty("line.separator") + " UNION ALL " + getSqlDevContractByOneMonth(item.KeyItem, xWhere);
			}
			i++;
		}
		//---------2 nam thu truoc
		list12Month= DateTimeUtil.get12MonthFromYear(year-1);
		
		for(ChartItem item : list12Month){
			sql = sql + System.getProperty("line.separator") + " UNION ALL " + getSqlDevContractByOneMonth(item.KeyItem, xWhere);
		}
		//---------3 nam thu truoc
		list12Month= DateTimeUtil.get12MonthFromYear(year-2);
		
		for(ChartItem item : list12Month){
			sql = sql + System.getProperty("line.separator") + " UNION ALL " + getSqlDevContractByOneMonth(item.KeyItem, xWhere);
		}
		//sql 
		return db.rawQuery(sql , null);
	}
	
	public Cursor getPDWorkingByYearMonth(int year , String xWhere){
		String sql ="";
		int i =0;
		
		ArrayList<ChartItem> list12Month= DateTimeUtil.get12MonthFromYear(year);
		
		for(ChartItem item : list12Month){
			if(i==0)
			{
				sql = getSqlPDWorkingByOneMonth(item.KeyItem, xWhere);
			}else{
				sql = sql + System.getProperty("line.separator") + " UNION ALL "  + getSqlPDWorkingByOneMonth(item.KeyItem, xWhere);
			}
			i++;
		}
		//sql 
		return db.rawQuery(sql , null);
	}
	
	
	private String getSqlDevWorkingByOneMonth(String ym , String xWhere){
		String sql ="";
		
		//xWhere = xWhere.replace(new GetSearchItemSetting(context).getYasumiSQLString(), "");
		
		sql = " SELECT "
		+	KEY_YEARMONTH
		+ " , SUM(" + KEY_CNT + ")" +  KEY_CNT
		+ " , " + VIEW_DATATYPE.DEV_WORKING.ordinal() + "  " +  KEY_DATATYPE
		+ " FROM "
		+ " ( "+ System.getProperty("line.separator")
		+ " --lay so nhan vien lap trinh chinh thuc "+ System.getProperty("line.separator")

		+ " SELECT '" + ym + "' " + KEY_YEARMONTH + System.getProperty("line.separator")
		+ " , COUNT(*) " + KEY_CNT + "  "+ System.getProperty("line.separator")
		+ " from " + TABLE_M_USER +  System.getProperty("line.separator")
		+ " where strftime('%Y-%m', " + KEY_IN_DATE + ") <='" + ym + "'"+ System.getProperty("line.separator")
		+ " and isdeleted =0  "+ System.getProperty("line.separator")
		+ " and business_kbn <> 2 "+ System.getProperty("line.separator")
		+ " and ( " + KEY_OUT_DATE + " ='' or (" + KEY_OUT_DATE + " <>'' and strftime('%Y-%m'," + KEY_OUT_DATE + ") >'"+ ym + "' ))  "+ System.getProperty("line.separator")
		+ xWhere
		+ " group by strftime('%Y-%m',"+ KEY_IN_DATE + ") "
		+ " ) ";
		
		return sql;
	}
	/**
	 * 2016.12.10
	 * Lay so nhan vien thu viec trong tung thang
	 * @param ym
	 * @param xWhere
	 * @return
	 */
	private String getSqlDevTrainingByOneMonth(String ym , String xWhere){
		String sql ="";
		
		//xWhere = xWhere.replace(new GetSearchItemSetting(context).getYasumiSQLString(), "");
		sql = VIEW_DEV_TRAINING_COUNT_BY_YEARMONTH_SQL;
		sql = sql.replace(KEY_YEARMONTH_STRING, ym);
		sql = sql.replace(KEY_WHERE_STRING, xWhere);
				
		return sql;
	}
	
	/**
	 * 2016.12.10
	 * Lay so nhan vien nhan chinh thuc trong tung thang
	 * @param ym
	 * @param xWhere
	 * @return
	 */
	private String getSqlDevContractByOneMonth(String ym , String xWhere){
		String sql ="";
		
		//xWhere = xWhere.replace(new GetSearchItemSetting(context).getYasumiSQLString(), "");
		sql = VIEW_DEV_CONTRACT_COUNT_BY_YEARMONTH_SQL;
		sql = sql.replace(KEY_YEARMONTH_STRING, ym);
		sql = sql.replace(KEY_WHERE_STRING, xWhere);
				
		return sql;
	}
	
	private String getSqlPDWorkingByOneMonth(String ym , String xWhere){
		String sql ="";
		
		//xWhere = xWhere.replace(new GetSearchItemSetting(context).getYasumiSQLString(), "");
		
		sql = " SELECT "
		+	KEY_YEARMONTH
		+ " , SUM(" + KEY_CNT + ")" +  KEY_CNT
		+ " , " + VIEW_DATATYPE.DEV_WORKING.ordinal() + "  " +  KEY_DATATYPE
		+ " FROM "
		+ " ( "+ System.getProperty("line.separator")
		+ " --lay so nhan vien lap trinh chinh thuc "+ System.getProperty("line.separator")

		+ " SELECT '" + ym + "' " + KEY_YEARMONTH+ System.getProperty("line.separator")
		+ " , COUNT(*) " + KEY_CNT + "  "+ System.getProperty("line.separator")
		+ " from " + TABLE_M_USER +  System.getProperty("line.separator")
		+ " where strftime('%Y-%m', " + KEY_IN_DATE + ") <='" + ym + "'"+ System.getProperty("line.separator")
		+ " and isdeleted =0  "+ System.getProperty("line.separator")
		+ " and business_kbn = 2 "+ System.getProperty("line.separator")
		+ " and ( " + KEY_OUT_DATE + " ='' or (" + KEY_OUT_DATE + " <>'' and strftime('%Y-%m'," + KEY_OUT_DATE + ") >'"+ ym + "' ))  "+ System.getProperty("line.separator")
		+ xWhere
		+ " group by strftime('%Y-%m',"+ KEY_IN_DATE + ") "
		+ " ) ";
		
		return sql;
	}
	
	
	/**
	 * 
	 * @return Cursor (danh sách message)
	 * Khong get cac message da sent
	 */
	public Cursor getMessageList(String xWhereAdd) {
		String xWhere ="";
		String xOrder = "ORDER BY " + KEY_CODE + " DESC ," + KEY_MESSAGE_TYPE + " ASC" ;
		 
		xWhere =xWhere + " 1=1 " ;
		xWhere =xWhere + "  " + xWhereAdd;
		xWhere =xWhere + "  " + xOrder;
		return db.query(TABLE_M_USER_MESSAGE_STATUS, null, xWhere, null, null, null, null);
	}
	
	/**
	 * 
	 * @return Cursor (danh sách message template)
	 * 
	 */
	public Cursor getMessageTemplateList(String xWhereAdd ,String[] columns , boolean isRawSql ) {
	
		String xWhere ="";
		String xOrder = "ORDER BY " + KEY_CODE + " ASC" ;
		xWhere =xWhere + "  " + KEY_ISDELETED + "=0";
		xWhere =xWhere + "  " + xWhereAdd;
		xWhere =xWhere + "  " + xOrder;
		String xSql="SELECT " + KEY_CODE + " AS _id , t.* FROM " + TABLE_M_MESSAGE_TEMPLATE + " t WHERE 1=1 " ;
		xSql += " AND "+ xWhere;
		
		Cursor cursor ;
		if(isRawSql){
			cursor = db.rawQuery(xSql, null);
		}else{
			if(columns==null || columns.length==0){
				cursor = db.query(TABLE_M_MESSAGE_TEMPLATE, null, xWhere, null, null, null, null);
			}else
			{
				cursor = db.query(TABLE_M_MESSAGE_TEMPLATE, columns, xWhere, null, null, null, null);
			}
		}
		
		
		if (cursor != null) {
			cursor.moveToFirst();
		}
		return cursor;
	}
	
	/**
	 * 
	 * @return true nếu tồn tại data tương ứng,false nếu không tồn tại
	 */
	public boolean isSonzaiUserMessageStatus(int user_code , String dt , int typeMsg) {
		String xWhere ="";
		//chỉ định điều kiện trích xuất theo class nhóm chức danh
		xWhere = KEY_MESSAGE_EMP_CODE + "=" + user_code;
		xWhere += " AND " + KEY_SEND_DATETIME + "='" + dt + "'"  ;
		xWhere += " AND " + KEY_MESSAGE_TYPE + "=" + typeMsg ;
		
		Cursor cursor= db.query(TABLE_M_USER_MESSAGE_STATUS, null, xWhere, null, null, null, null);
		if (cursor.getCount()>0){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * 
	 * @param code : id cần delete
	 * @return
	 */
	public boolean deletePernamentUserMessageStatus(int code ) {
		String xWhere ="";
		xWhere = KEY_CODE + "=" + code;
		try {
			
			Log.d("DELETE");
			db.delete(TABLE_M_USER_MESSAGE_STATUS, xWhere, null);
			Log.d("DELETED");
			
			Log.d("Deleted");
		} catch (SQLiteException e) {
			return false;
		}
		return true;
	}
	
	/**
	 * 
	 * @author hoa-nx
	 *
	 */
	private class MyCreateOpenHelper extends SQLiteOpenHelper {
		/**
		 * 
		 * @param context
		 */
		public MyCreateOpenHelper(Context context) {
			
			super(context, DATABASE_NAME, null, DB_VERSION);
		}
		/**
		 * @param database
		 */
		@Override
		public void onCreate(SQLiteDatabase database) {
			database.execSQL(M_MEI_TABLE_CREATE);
			database.execSQL(M_USER_TABLE_CREATE);
			database.execSQL(M_USER_GROUP_POSITION_TABLE_CREATE);
			database.execSQL(M_USER_BUSINESS_TABLE_CREATE);
			database.execSQL(M_USER_IMAGE_TABLE_CREATE);
			database.execSQL(M_USER_HIS_TABLE_CREATE);
			database.execSQL(M_MESSAGE_TEMPLATE_TABLE_CREATE);
			database.execSQL(M_USER_MESSAGE_STATUS_TABLE_CREATE);
			
			database.execSQL(M_USER_GROUP_CUSTOMER_TABLE_CREATE);
			database.execSQL(VIEW_M_USER_CUSTOMER_GROUP_VIEW_CREATE);

			database.execSQL(M_USER_SALARY_TABLE_CREATE);
			
		}
		/**
		 * @param db
		 * 
		 */
		@Override
		public void onOpen(SQLiteDatabase db) {
			super.onOpen(db);
		}
		
		/**
		 * 
		 */
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			/*
			if(prevVersion == 1) {
				db.execSQL("ALTER TABLE " + PREVIOUS_VERSION_ENTRY_TABLE +" RENAME TO "+ENTRY_TABLE);
			}
			*/
			/*{
				db.execSQL("DROP TABLE IF EXISTS " + M_MEI_TABLE_CREATE);
				db.execSQL("DROP TABLE IF EXISTS " + M_USER_TABLE_CREATE);
				db.execSQL("DROP TABLE IF EXISTS " + M_USER_GROUP_POSITION_TABLE_CREATE);
				db.execSQL("DROP TABLE IF EXISTS " + M_USER_BUSINESS_TABLE_CREATE);
				db.execSQL("DROP TABLE IF EXISTS " + M_USER_IMAGE_TABLE_CREATE);
				db.execSQL("DROP TABLE IF EXISTS " + M_USER_HIS_TABLE_CREATE);
				db.execSQL("DROP TABLE IF EXISTS " + M_MESSAGE_TEMPLATE_TABLE_CREATE);
				db.execSQL("DROP TABLE IF EXISTS " + M_USER_MESSAGE_STATUS_TABLE_CREATE);
				onCreate(db);
			}*/
			try {
				String xSQLAlter ="";
				switch (newVersion)
	            {
				/**version up to 2*/
	                case 2:
						if(newVersion>oldVersion){
							//xSQLAlter="ALTER TABLE " + TABLE_M_USER + " ADD COLUMN " + KEY_NEW_SALARY + " REAL DEFAULT 0";
							//db.execSQL(xSQLAlter);
							//chuyen len create nen khong de o day 
							//xSQLAlter="ALTER TABLE " + TABLE_M_MEI + " ADD COLUMN " + KEY_CREATE_DATE + " TEXT DEFAULT ''";
							//db.execSQL(xSQLAlter);
							//end
							
							//xSQLAlter="ALTER TABLE " + TABLE_M_USER + " ADD COLUMN " + KEY_COMPANY_CODE + " TEXT DEFAULT ''";
							//db.execSQL(xSQLAlter);
							//xSQLAlter="ALTER TABLE " + TABLE_M_USER + " ADD COLUMN " + KEY_BSE_LEVEL + " TEXT DEFAULT ''";
							//db.execSQL(xSQLAlter);
							//xSQLAlter="ALTER TABLE " + TABLE_M_USER + " ADD COLUMN " + KEY_TAX_CODE + " TEXT DEFAULT ''";
							//db.execSQL(xSQLAlter);
							//db.execSQL(M_COMPANY_TABLE_CREATE);
						}

	                    break;
	                case 3:
	                	//comment do khong can thiet
	                	//if(newVersion>oldVersion){
	                	//	db.execSQL(M_USER_GROUP_CUSTOMER_TABLE_CREATE);
	                	//}
	                	//end
	                	
	                    break;
	                case 4:
	                	//add them table  
	                	//comment do khong can thiet
	                	if(newVersion>oldVersion){
	                		db.execSQL("drop table " + TABLE_M_USER_CUSTOMER_GROUP);
	                		db.execSQL(M_USER_GROUP_CUSTOMER_TABLE_CREATE);
	                	}
	                	//end
	                    break;
	                case 5:
	                	//add view 
	                	//add them table  
	                	if(newVersion>oldVersion){
	                		//db.execSQL("drop view " + VIEW_M_USER_CUSTOMER_GROUP);
	                		xSQLAlter = "CREATE VIEW " + VIEW_M_USER_CUSTOMER_GROUP + " AS ";
	                		xSQLAlter +=" SELECT DISTINCT " +
	                					"	USR.*" +
	                				    ",	GRP." + KEY_CUSTOMER_GROUP_CODE + "" +
            				     		",	GRP." + KEY_NAME + " AS " + KEY_CUSTOMER_GROUP_NAME + " " +
    				     				"FROM " + TABLE_M_USER + " USR LEFT OUTER JOIN " + TABLE_M_USER_CUSTOMER_GROUP + " GRP" +
				     					" ON USR." + KEY_CODE + "=GRP." + KEY_USER_CODE + ""; 
	                		//xSQLAlter += " LEFT OUTER JOIN " + TABLE_M_MEI + " MEI";
	                		//xSQLAlter += " ON GRP." + KEY_CUSTOMER_GROUP_CODE + "=MEI." + KEY_CODE + " AND MEI." + KEY_MKBN + " =" + MasterConstants.MASTER_MKBN_CUSTOMER_GROUP ; 
	                		
	                		db.execSQL(xSQLAlter);
	                	}
	                	//end
	                    break;
	                    
	                case 6:
	                	if(newVersion>oldVersion){
	                		
							xSQLAlter="ALTER TABLE " + TABLE_M_USER + " ADD COLUMN " + KEY_TRAINING_DATE_END + " TEXT DEFAULT ''";
							db.execSQL(xSQLAlter);
						}
	                	break;
	                case 7:
	                	if(newVersion>oldVersion){
	                		
	                		xSQLAlter="ALTER TABLE " + TABLE_M_USER_HIS + " ADD COLUMN " + KEY_SALARY_STANDARD + "  REAL DEFAULT 0 ";							
							db.execSQL(xSQLAlter);
							xSQLAlter="ALTER TABLE " + TABLE_M_USER_HIS + " ADD COLUMN " + KEY_SALARY_PERCENT + "  REAL DEFAULT 0 ";
							db.execSQL(xSQLAlter);
							xSQLAlter="ALTER TABLE " + TABLE_M_USER_HIS + " ADD COLUMN " + KEY_SALARY_ACTUAL_UP + "  REAL DEFAULT 0 ";
							db.execSQL(xSQLAlter);
							xSQLAlter="ALTER TABLE " + TABLE_M_USER_HIS + " ADD COLUMN " + KEY_SALARY_NEXT_YM + "  TEXT DEFAULT '' ";
							db.execSQL(xSQLAlter);
						}
	                	break;   
	                case 8:
	                	if(newVersion>oldVersion)
	                	{
	                		//da di chuyen len tren
	                		xSQLAlter="ALTER TABLE " + TABLE_M_USER_HIS + " ADD COLUMN " + KEY_NEW_SALARY + "  REAL DEFAULT 0 ";							
							db.execSQL(xSQLAlter);
	                		
	                	}
	                	break;
	                case 9:
	                	if(newVersion>oldVersion)
	                	{
	                		//da di chuyen len tren
	                		xSQLAlter="ALTER TABLE " + TABLE_M_USER + " ADD COLUMN " + KEY_ALLOWANCE_ROOM + "  TEXT DEFAULT ''	";							
							db.execSQL(xSQLAlter);
	                		
	                	}
	                	break;
	                case 10:
	                	if(newVersion>oldVersion)
	                	{
	                		//da di chuyen len tren
	                		xSQLAlter="ALTER TABLE " + TABLE_M_USER + " ADD COLUMN " + KEY_GOOGLE_CONTACT_ID + "  TEXT DEFAULT ''	";							
							db.execSQL(xSQLAlter);
	                		
	                	}
	                	break;
	                case 11:
	                	if(newVersion>oldVersion)
	                	{
	                		//da di chuyen len tren
	                		xSQLAlter="ALTER TABLE " + TABLE_M_USER_MESSAGE_STATUS + " ADD COLUMN " + KEY_MESSAGE_TEMPLATE_CODE + "  INTEGER DEFAULT 0	";							
							db.execSQL(xSQLAlter);
	                		
	                	}
	                	break;
	                case 12:
	                	if(newVersion>oldVersion)
	                	{
	                		//da di chuyen len tren
	                		xSQLAlter="ALTER TABLE " + TABLE_M_USER_MESSAGE_STATUS + " ADD COLUMN " + KEY_MESSAGE_EMP_CODE + "  INTEGER DEFAULT 0	";							
							db.execSQL(xSQLAlter);
							xSQLAlter="ALTER TABLE " + TABLE_M_USER_MESSAGE_STATUS + " ADD COLUMN " + KEY_MESSAGE_TYPE + "  INTEGER DEFAULT 0	";							
							db.execSQL(xSQLAlter);
	                	}
	                	break;
	                case 13:
	                	if(newVersion>oldVersion)
	                	{
	                		//da di chuyen len tren
	                		xSQLAlter="ALTER TABLE " + TABLE_M_USER + " ADD COLUMN " + KEY_LEARN_TRAINING_DATE + " TEXT DEFAULT ''";							
							db.execSQL(xSQLAlter);
							xSQLAlter="ALTER TABLE " + TABLE_M_USER + " ADD COLUMN " + KEY_LEARN_TRAINING_DATE_END + " TEXT DEFAULT ''";					
							db.execSQL(xSQLAlter);
	                	}
	                	break;

	                case 15://2017.01.05 add
	                	if(newVersion > oldVersion)
	                	{
							//table lich su nhan vien
	                		xSQLAlter="ALTER TABLE " + TABLE_M_USER_HIS + " ADD COLUMN " + KEY_ALLOWANCE_BSE + " TEXT DEFAULT '' ";
	                		db.execSQL(xSQLAlter);
	                	}
						if(newVersion>oldVersion){
							//table nhan vien
							xSQLAlter="ALTER TABLE " + TABLE_M_USER + " ADD COLUMN " + KEY_GPA + "  REAL DEFAULT 0 ";
							db.execSQL(xSQLAlter);
							xSQLAlter="ALTER TABLE " + TABLE_M_USER + " ADD COLUMN " + KEY_GPA_TEXT + "  TEXT DEFAULT ''  ";
							db.execSQL(xSQLAlter);
							xSQLAlter="ALTER TABLE " + TABLE_M_USER + " ADD COLUMN " + KEY_COLLECT_NAME + "  TEXT DEFAULT '' ";
							db.execSQL(xSQLAlter);
							xSQLAlter="ALTER TABLE " + TABLE_M_USER + " ADD COLUMN " + KEY_ALLOWANCE_BSE + "  TEXT DEFAULT '' ";
							db.execSQL(xSQLAlter);
							xSQLAlter="ALTER TABLE " + TABLE_M_USER + " ADD COLUMN " + KEY_STAFF_KBN + "  TEXT DEFAULT '' ";
							db.execSQL(xSQLAlter);
							xSQLAlter="ALTER TABLE " + TABLE_M_USER + " ADD COLUMN " + KEY_MARRIED_DATE + "  TEXT DEFAULT '' ";
							db.execSQL(xSQLAlter);
							xSQLAlter="ALTER TABLE " + TABLE_M_USER + " ADD COLUMN " + KEY_SUPPORTER1 + "  TEXT DEFAULT '' ";
							db.execSQL(xSQLAlter);
							xSQLAlter="ALTER TABLE " + TABLE_M_USER + " ADD COLUMN " + KEY_SUPPORTER2 + "  TEXT DEFAULT '' ";
							db.execSQL(xSQLAlter);
							xSQLAlter="ALTER TABLE " + TABLE_M_USER + " ADD COLUMN " + KEY_SUPPORTER3 + "  TEXT DEFAULT '' ";
							db.execSQL(xSQLAlter);
							xSQLAlter="ALTER TABLE " + TABLE_M_USER + " ADD COLUMN " + KEY_INTERVIEWER1 + "  TEXT DEFAULT '' ";
							db.execSQL(xSQLAlter);
							xSQLAlter="ALTER TABLE " + TABLE_M_USER + " ADD COLUMN " + KEY_INTERVIEWER2 + "  TEXT DEFAULT '' ";
							db.execSQL(xSQLAlter);
							xSQLAlter="ALTER TABLE " + TABLE_M_USER + " ADD COLUMN " + KEY_INTERVIEWER3 + "  TEXT DEFAULT '' ";
							db.execSQL(xSQLAlter);
							xSQLAlter="ALTER TABLE " + TABLE_M_USER + " ADD COLUMN " + KEY_INTERVIEW_KEKKA + "  TEXT DEFAULT '' ";
							db.execSQL(xSQLAlter);
							//tao table quan ly thong tin luong nhan vien
							db.execSQL(M_USER_SALARY_TABLE_CREATE);
						}
						break;
					case 16://2017.01.05 add
						if(newVersion > oldVersion)
						{
							//Do version 15 dang add cot khong dung ten nen add them cot khac
							xSQLAlter="ALTER TABLE " + TABLE_M_USER_HIS + " ADD COLUMN " + KEY_NEW_ALLOWANCE_BSE + " TEXT DEFAULT '' ";
							db.execSQL(xSQLAlter);
						}
						break;
					case 17://2017.01.07 add
						if(newVersion > oldVersion)
						{
							//TAO TABLE DOANH THU
							db.execSQL(T_REVENUE_TABLE_CREATE);
						}
						break;
				}
	        } catch( Exception exception ) {
	            throw new RuntimeException("Database upgrade failed", exception );
	        }
		}
		
	}

}
