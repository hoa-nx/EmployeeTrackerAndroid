/**
 * Copyright (c) 2012 Fujinet Software Department 5 ( USSOL team)  
 * See the file license.txt for copying permission.
*/  

package com.ussol.employeetracker.helpers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.ussol.employeetracker.models.MasterConstants;
import com.ussol.employeetracker.models.User;
import com.ussol.employeetracker.utils.DateTimeUtil;

import android.content.Context;
import android.os.Environment;

public class CsvHelper {
	User user ;
	DatabaseAdapter mDatabaseAdapter ;
	public CsvHelper(Context context){
		mDatabaseAdapter = new DatabaseAdapter(context);
	}
	/** */
	public void readUserFromCsv(String file) throws IOException
	{

	    /*File sdcard = Environment.getExternalStorageDirectory();
	    File file = new File(sdcard,"csvtest.csv");*/

	    //BufferedReader in = new BufferedReader(new FileReader(file));
		InputStreamReader iReader = new InputStreamReader(new FileInputStream(file), "UTF8");
		//iReader.getEncoding();
	    BufferedReader in = new BufferedReader(iReader);
	    String reader = "";
	    mDatabaseAdapter.open();
	    
	    while ((reader = in.readLine()) != null)
	    {
	        //String[] RowData = reader.split(","); /** ngăn cách bằng dấu ,*/
	    	String[] RowData = reader.split("\t"); /** ngăn cách bằng phím TAB */
	    	if (RowData.length == 18){
	    		 user = new User();
	 	        /** tên nhân viên */
	 	        user.first_name = RowData[0];
	 	        user.last_name = RowData[1];
	 	        user.full_name = RowData[2];
	 	        /** giới tính */
	 	        //user.sex =Integer.parseInt( RowData[3]);
	 	        /** ngày tháng năm sinh */
	 	        String birthday ="";
	 	        if(RowData[4]==""){
	 	        	birthday="";
	 	        }else if (DateTimeUtil.isDate(RowData[4].replace("/", MasterConstants.DATE_SEPERATE_CHAR), MasterConstants.DATE_VN_FORMAT)){
	 	        	/** format theo lich viet */
	 	        	birthday = DateTimeUtil.formatDate2String(RowData[4].replace("/", MasterConstants.DATE_SEPERATE_CHAR), MasterConstants.DATE_VN_FORMAT, MasterConstants.DATE_JP_FORMAT);
	 	        }else if (DateTimeUtil.isDate(RowData[4].replace("/", MasterConstants.DATE_SEPERATE_CHAR), MasterConstants.DATE_JP_FORMAT)){
	 	        	/** format theo lich nhật */
	 	        	birthday=RowData[4];
	 	        }
	 	        user.birthday = RowData[4].replace("/", MasterConstants.DATE_SEPERATE_CHAR);
	 	        /** địa chỉ */
	 	        user.address = RowData[5];
	 	        /** điện thoại  */
	 	        user.mobile = RowData[6];
	 	        /** email */
	 	        user.email = RowData[7];
	 	        /** phòng ban */
	 	        //user.dept = Integer.parseInt( RowData[8]);
	 	        /** tên phòng ban */
	 	        user.dept_name =RowData[9];
	 	        /** mã nhóm */
	 	        //user.team = Integer.parseInt( RowData[10]);
	 	        /** tên nhóm */
	 	        user.team_name = RowData[11];
	 	        /** chức vụ */
	 	        //user.position = Integer.parseInt( RowData[12]);
	 	        /** ten chức vụ */
	 	        user.position_name = RowData[13];
	 	        /** ngày vào công ty */
	 	       String in_date ="";
	 	        if(RowData[14]==""){
	 	        	in_date="";
	 	        }else if (DateTimeUtil.isDate(RowData[14].replace("/", MasterConstants.DATE_SEPERATE_CHAR), MasterConstants.DATE_VN_FORMAT)){
	 	        	/** format theo lich viet */
	 	        	in_date = DateTimeUtil.formatDate2String(RowData[14].replace("/", MasterConstants.DATE_SEPERATE_CHAR), MasterConstants.DATE_VN_FORMAT, MasterConstants.DATE_JP_FORMAT);
	 	        }else if (DateTimeUtil.isDate(RowData[14].replace("/", MasterConstants.DATE_SEPERATE_CHAR), MasterConstants.DATE_JP_FORMAT)){
	 	        	/** format theo lich nhật */
	 	        	in_date=RowData[14];
	 	        }
	 	        user.in_date =  RowData[14].replace("/", MasterConstants.DATE_SEPERATE_CHAR);
	 	        /** ngày vào USSOL */
	 	       String join_date ="";
	 	        if(RowData[15]==""){
	 	        	join_date="";
	 	        }else if (DateTimeUtil.isDate(RowData[15].replace("/", MasterConstants.DATE_SEPERATE_CHAR), MasterConstants.DATE_VN_FORMAT)){
	 	        	/** format theo lich viet */
	 	        	join_date = DateTimeUtil.formatDate2String(RowData[15].replace("/", MasterConstants.DATE_SEPERATE_CHAR), MasterConstants.DATE_VN_FORMAT, MasterConstants.DATE_JP_FORMAT);
	 	        }else if (DateTimeUtil.isDate(RowData[15].replace("/", MasterConstants.DATE_SEPERATE_CHAR), MasterConstants.DATE_JP_FORMAT)){
	 	        	/** format theo lich nhật */
	 	        	join_date=RowData[15];
	 	        }
	 	        user.join_date = RowData[15].replace("/", MasterConstants.DATE_SEPERATE_CHAR);
	 	        /** trình độ tiếng Nhật*/
	 	        //user.japanese = RowData[16];
	 	        /** phụ cấp nghiệp vụ*/
	 	        //user.allowance_business = RowData[17];
	 	        
	 	        mDatabaseAdapter.insertToUserTable(user);
	    	}
	    	
	    }
	    mDatabaseAdapter.close();
	    in.close();
	}
	
}
