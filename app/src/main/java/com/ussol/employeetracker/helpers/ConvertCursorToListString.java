/**
 * Copyright (c) 2012 Fujinet Software Department 5 ( USSOL team)  
 * See the file license.txt for copying permission.
*/  

package com.ussol.employeetracker.helpers;

import java.util.ArrayList;
import java.util.List;

import com.ussol.employeetracker.helpers.DatabaseAdapter.MESSAGE_STATUS;
import com.ussol.employeetracker.models.CustomerGroup;
import com.ussol.employeetracker.models.Dept;
import com.ussol.employeetracker.models.IExpGroup;
import com.ussol.employeetracker.models.MasterConstants;
import com.ussol.employeetracker.models.MasterConstants.MESSAGE_TYPE;
import com.ussol.employeetracker.models.MasterConstants.MESSAGE_TYPE_CONST;
import com.ussol.employeetracker.models.MessageTemplate;
import com.ussol.employeetracker.models.Position;
import com.ussol.employeetracker.models.PositionGroup;
import com.ussol.employeetracker.models.Team;
import com.ussol.employeetracker.models.User;
import com.ussol.employeetracker.models.UserCustomerGroup;
import com.ussol.employeetracker.models.UserHistory;
import com.ussol.employeetracker.models.UserMessageStatus;
import com.ussol.employeetracker.models.UserPositionGroup;
import com.ussol.employeetracker.utils.DateTimeUtil;

import com.ussol.employeetracker.R;
import android.content.Context;
import android.database.Cursor;

/**
 * @author hoa-nx
 *
 */
public class ConvertCursorToListString {

	private Context context;
	private DatabaseAdapter adapter;
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * ConvertCursorToListString
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
	public ConvertCursorToListString(Context context) {
		this.context = context;
		adapter = new DatabaseAdapter(context);
	}
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * getDeptList
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
	public List<Dept> getDeptList(String xWhere) {
		adapter.open();
		return getDeptList(adapter.getDeptList( " AND " + DatabaseAdapter.KEY_ISDELETED + " = 0 "+ xWhere) );
	}
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * getDeptList
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
	private List<Dept> getDeptList(Cursor cursor) {
		List<Dept> mainlist = new ArrayList<Dept>();
		Dept listDept;
		if (cursor.getCount() >= 1) {
			cursor.moveToFirst();
			do {
				listDept = new Dept();
				//listDept.id =Integer.parseInt(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_ID)));
				/** code  phòng ban */
				listDept.code = Integer.parseInt(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_CODE)));
				/** tên phòng ban */
				listDept.name = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_NAME)) 
								+ "(" + cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_TEXT1)) + ")";
				/** tên tắt phòng ban */
				listDept.ryaku = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_RYAKU));
				/** trưởng phòng*/
				listDept.yobi_text1 = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_TEXT1));
				/** phó phòng*/
				listDept.yobi_text2= cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_TEXT2));
				/** ghi chú */
				listDept.note = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_NOTE));
			
				listDept.yobi_code1 = Integer.parseInt(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_CODE1)));
				listDept.yobi_code2 = Integer.parseInt(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_CODE2)));
				listDept.yobi_code3 = Integer.parseInt(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_CODE3)));
				listDept.yobi_code4 = Integer.parseInt(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_CODE4)));
				listDept.yobi_code5 = Integer.parseInt(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_CODE5)));
				listDept.yobi_text1   = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_TEXT1));
				listDept.yobi_text2 = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_TEXT2));
				listDept.yobi_text3 = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_TEXT3));
				listDept.yobi_text4 = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_TEXT4));
				listDept.yobi_text5 = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_TEXT5));
				listDept.yobi_date1   = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_DATE1));
				listDept.yobi_date2 = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_DATE2));
				listDept.yobi_date3 = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_DATE3));
				listDept.yobi_date4 = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_DATE4));
				listDept.yobi_date5 = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_DATE5));
				listDept.yobi_real1 = Float.parseFloat(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_REAL1)));
				listDept.yobi_real2 = Float.parseFloat(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_REAL2)));
				listDept.yobi_real3 = Float.parseFloat(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_REAL3)));
				listDept.yobi_real4 = Float.parseFloat(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_REAL4)));
				listDept.yobi_real5 = Float.parseFloat(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_REAL5)));
				listDept.up_date = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_UP_DATE));
				listDept.ad_date = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_AD_DATE));
				listDept.opid = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_OPID));
				
				mainlist.add(listDept);
				cursor.moveToNext();
			} while (!cursor.isAfterLast());
		}
		cursor.close();
		adapter.close();
		
		return mainlist;
	}
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * getDeptByCode
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
	public Dept getDeptByCode(Integer code) {
		adapter.open();
		return getDeptByCode(adapter.getDeptByCode(code));
	}
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * getDeptByCode
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
	private Dept getDeptByCode(Cursor cursor) {
		Dept listDept=null;
		if (cursor.getCount() >= 1) {
			cursor.moveToFirst();
			do {
				listDept = new Dept();
				//listDept.id =Integer.parseInt(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_ID)));
				listDept.code = Integer.parseInt(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_CODE)));
				/** tên phòng ban */
				listDept.name = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_NAME));
				/** tên tắt */
				listDept.ryaku = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_RYAKU));
				/** trưởng phòng*/
				listDept.yobi_text1 = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_TEXT1));
				/** phó phòng*/
				listDept.yobi_text2= cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_TEXT2));
				/** ghi chú*/
				listDept.note = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_NOTE));

				listDept.yobi_code1 = Integer.parseInt(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_CODE1)));
				listDept.yobi_code2 = Integer.parseInt(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_CODE2)));
				listDept.yobi_code3 = Integer.parseInt(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_CODE3)));
				listDept.yobi_code4 = Integer.parseInt(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_CODE4)));
				listDept.yobi_code5 = Integer.parseInt(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_CODE5)));
				listDept.yobi_text1   = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_TEXT1));
				listDept.yobi_text2 = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_TEXT2));
				listDept.yobi_text3 = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_TEXT3));
				listDept.yobi_text4 = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_TEXT4));
				listDept.yobi_text5 = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_TEXT5));
				listDept.yobi_date1   = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_DATE1));
				listDept.yobi_date2 = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_DATE2));
				listDept.yobi_date3 = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_DATE3));
				listDept.yobi_date4 = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_DATE4));
				listDept.yobi_date5 = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_DATE5));
				listDept.yobi_real1 = Float.parseFloat(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_REAL1)));
				listDept.yobi_real2 = Float.parseFloat(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_REAL2)));
				listDept.yobi_real3 = Float.parseFloat(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_REAL3)));
				listDept.yobi_real4 = Float.parseFloat(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_REAL4)));
				listDept.yobi_real5 = Float.parseFloat(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_REAL5)));
				listDept.up_date = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_UP_DATE));
				listDept.ad_date = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_AD_DATE));
				listDept.opid = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_OPID));
				
				cursor.moveToNext();
			} while (!cursor.isAfterLast());
		}
		cursor.close();
		adapter.close();
		
		return listDept;
	}
	
	
	/**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * getTeamList
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
	public List<Team> getTeamList(String xWhere) {
		adapter.open();
		return getTeamList(adapter.getTeamList( " AND " + DatabaseAdapter.KEY_ISDELETED + " = 0 " + xWhere));
	}
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * getTeamList
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
	private List<Team> getTeamList(Cursor cursor) {
		List<Team> mainlist = new ArrayList<Team>();
		Team listTeam;
		if (cursor.getCount() >= 1) {
			cursor.moveToFirst();
			do {
				listTeam = new Team();
				//listDept.id =Integer.parseInt(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_ID)));
				/** code  nhóm */
				listTeam.code = Integer.parseInt(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_CODE)));
				/** tên nhóm */
				listTeam.name = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_NAME)) 
								+ "(" + cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_TEXT2)) + ")";
				/** tên tắt nhóm */
				listTeam.ryaku = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_RYAKU));
				/** phòng ban */
				listTeam.yobi_text1 = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_TEXT1));
				/** trưởng nhóm*/
				listTeam.yobi_text2= cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_TEXT2));
				/** ghi chú */
				listTeam.note = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_NOTE));
				
				listTeam.yobi_code1 = Integer.parseInt(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_CODE1)));
				listTeam.yobi_code2 = Integer.parseInt(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_CODE2)));
				listTeam.yobi_code3 = Integer.parseInt(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_CODE3)));
				listTeam.yobi_code4 = Integer.parseInt(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_CODE4)));
				listTeam.yobi_code5 = Integer.parseInt(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_CODE5)));
				listTeam.yobi_text1   = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_TEXT1));
				listTeam.yobi_text2 = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_TEXT2));
				listTeam.yobi_text3 = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_TEXT3));
				listTeam.yobi_text4 = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_TEXT4));
				listTeam.yobi_text5 = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_TEXT5));
				listTeam.yobi_date1   = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_DATE1));
				listTeam.yobi_date2 = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_DATE2));
				listTeam.yobi_date3 = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_DATE3));
				listTeam.yobi_date4 = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_DATE4));
				listTeam.yobi_date5 = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_DATE5));
				listTeam.yobi_real1 = Float.parseFloat(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_REAL1)));
				listTeam.yobi_real2 = Float.parseFloat(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_REAL2)));
				listTeam.yobi_real3 = Float.parseFloat(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_REAL3)));
				listTeam.yobi_real4 = Float.parseFloat(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_REAL4)));
				listTeam.yobi_real5 = Float.parseFloat(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_REAL5)));
				listTeam.up_date = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_UP_DATE));
				listTeam.ad_date = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_AD_DATE));
				listTeam.opid = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_OPID));
				
				mainlist.add(listTeam);
				cursor.moveToNext();
			} while (!cursor.isAfterLast());
		}
		cursor.close();
		adapter.close();
		
		return mainlist;
	}
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * getTeamByCode
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
	public Team getTeamByCode(Integer code) {
		adapter.open();
		return getTeamByCode(adapter.getTeamByCode(code));
	}
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * getDeptByCode
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
	private Team getTeamByCode(Cursor cursor) {
		Team listTeam=null;
		if (cursor.getCount() >= 1) {
			cursor.moveToFirst();
			do {
				listTeam = new Team();
				//listDept.id =Integer.parseInt(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_ID)));
				/** code  nhóm */
				listTeam.code = Integer.parseInt(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_CODE)));
				/** tên nhóm */
				listTeam.name = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_NAME));
				/** tên tắt nhóm */
				listTeam.ryaku = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_RYAKU));
				/** phòng ban */
				listTeam.yobi_text1 = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_TEXT1));
				/** trưởng nhóm*/
				listTeam.yobi_text2= cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_TEXT2));
				/** ghi chú */
				listTeam.note = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_NOTE));
			
				listTeam.yobi_code1 = Integer.parseInt(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_CODE1)));
				listTeam.yobi_code2 = Integer.parseInt(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_CODE2)));
				listTeam.yobi_code3 = Integer.parseInt(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_CODE3)));
				listTeam.yobi_code4 = Integer.parseInt(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_CODE4)));
				listTeam.yobi_code5 = Integer.parseInt(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_CODE5)));
				listTeam.yobi_text1   = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_TEXT1));
				listTeam.yobi_text2 = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_TEXT2));
				listTeam.yobi_text3 = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_TEXT3));
				listTeam.yobi_text4 = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_TEXT4));
				listTeam.yobi_text5 = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_TEXT5));
				listTeam.yobi_date1   = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_DATE1));
				listTeam.yobi_date2 = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_DATE2));
				listTeam.yobi_date3 = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_DATE3));
				listTeam.yobi_date4 = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_DATE4));
				listTeam.yobi_date5 = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_DATE5));
				listTeam.yobi_real1 = Float.parseFloat(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_REAL1)));
				listTeam.yobi_real2 = Float.parseFloat(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_REAL2)));
				listTeam.yobi_real3 = Float.parseFloat(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_REAL3)));
				listTeam.yobi_real4 = Float.parseFloat(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_REAL4)));
				listTeam.yobi_real5 = Float.parseFloat(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_REAL5)));
				listTeam.up_date = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_UP_DATE));
				listTeam.ad_date = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_AD_DATE));
				listTeam.opid = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_OPID));
				
				
				cursor.moveToNext();
			} while (!cursor.isAfterLast());
		}
		cursor.close();
		adapter.close();
		
		return listTeam;
	}


	/**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * getPositionList
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
	public List<Position> getPositionList(String xWhere) {
		adapter.open();
		return getPositionList(adapter.getPositionList( " AND " + DatabaseAdapter.KEY_ISDELETED + " = 0 " + xWhere));
	}
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * getPositionList
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
	private List<Position> getPositionList(Cursor cursor) {
		List<Position> mainlist = new ArrayList<Position>();
		Position listPosition;
		if (cursor.getCount() >= 1) {
			cursor.moveToFirst();
			do {
				listPosition = new Position();
				//listDept.id =Integer.parseInt(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_ID)));
				/** code chức vụ */
				listPosition.code = Integer.parseInt(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_CODE)));
				/** tên chức vụ */
				listPosition.name = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_NAME)) 
								+ "(" + cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_TEXT1)) + ")";
				/** tên tắt */
				listPosition.ryaku = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_RYAKU));
				/** mã tại FJN */
				listPosition.yobi_text1 = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_TEXT1));
				/** ghi chú */
				listPosition.note = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_NOTE));
			
				listPosition.yobi_code1 = Integer.parseInt(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_CODE1)));
				listPosition.yobi_code2 = Integer.parseInt(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_CODE2)));
				listPosition.yobi_code3 = Integer.parseInt(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_CODE3)));
				listPosition.yobi_code4 = Integer.parseInt(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_CODE4)));
				listPosition.yobi_code5 = Integer.parseInt(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_CODE5)));
				listPosition.yobi_text1   = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_TEXT1));
				listPosition.yobi_text2 = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_TEXT2));
				listPosition.yobi_text3 = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_TEXT3));
				listPosition.yobi_text4 = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_TEXT4));
				listPosition.yobi_text5 = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_TEXT5));
				listPosition.yobi_date1   = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_DATE1));
				listPosition.yobi_date2 = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_DATE2));
				listPosition.yobi_date3 = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_DATE3));
				listPosition.yobi_date4 = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_DATE4));
				listPosition.yobi_date5 = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_DATE5));
				listPosition.yobi_real1 = Float.parseFloat(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_REAL1)));
				listPosition.yobi_real2 = Float.parseFloat(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_REAL2)));
				listPosition.yobi_real3 = Float.parseFloat(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_REAL3)));
				listPosition.yobi_real4 = Float.parseFloat(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_REAL4)));
				listPosition.yobi_real5 = Float.parseFloat(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_REAL5)));
				listPosition.up_date = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_UP_DATE));
				listPosition.ad_date = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_AD_DATE));
				listPosition.opid = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_OPID));
				
				
				mainlist.add(listPosition);
				cursor.moveToNext();
			} while (!cursor.isAfterLast());
		}
		cursor.close();
		adapter.close();
		
		return mainlist;
	}
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * getPositionByCode
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
	public Position getPositionByCode(Integer code) {
		adapter.open();
		return getPositionByCode(adapter.getPositionByCode(code));
	}
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * getDeptByCode
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
	private Position getPositionByCode(Cursor cursor) {
		Position listPosition=null;
		if (cursor.getCount() >= 1) {
			cursor.moveToFirst();
			do {
				listPosition = new Position();
				//listDept.id =Integer.parseInt(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_ID)));
				/** code  chức vụ */
				listPosition.code = Integer.parseInt(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_CODE)));
				/** tên  chức vụ */
				listPosition.name = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_NAME));
				/** tên tắt  */
				listPosition.ryaku = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_RYAKU));
				/** mã FJN */
				listPosition.yobi_text1 = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_TEXT1));
				/** ghi chú */
				listPosition.note = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_NOTE));
			
				listPosition.yobi_code1 = Integer.parseInt(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_CODE1)));
				listPosition.yobi_code2 = Integer.parseInt(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_CODE2)));
				listPosition.yobi_code3 = Integer.parseInt(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_CODE3)));
				listPosition.yobi_code4 = Integer.parseInt(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_CODE4)));
				listPosition.yobi_code5 = Integer.parseInt(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_CODE5)));
				listPosition.yobi_text1   = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_TEXT1));
				listPosition.yobi_text2 = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_TEXT2));
				listPosition.yobi_text3 = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_TEXT3));
				listPosition.yobi_text4 = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_TEXT4));
				listPosition.yobi_text5 = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_TEXT5));
				listPosition.yobi_date1   = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_DATE1));
				listPosition.yobi_date2 = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_DATE2));
				listPosition.yobi_date3 = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_DATE3));
				listPosition.yobi_date4 = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_DATE4));
				listPosition.yobi_date5 = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_DATE5));
				listPosition.yobi_real1 = Float.parseFloat(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_REAL1)));
				listPosition.yobi_real2 = Float.parseFloat(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_REAL2)));
				listPosition.yobi_real3 = Float.parseFloat(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_REAL3)));
				listPosition.yobi_real4 = Float.parseFloat(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_REAL4)));
				listPosition.yobi_real5 = Float.parseFloat(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_REAL5)));
				listPosition.up_date = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_UP_DATE));
				listPosition.ad_date = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_AD_DATE));
				listPosition.opid = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_OPID));
				
				
				cursor.moveToNext();
			} while (!cursor.isAfterLast());
		}
		cursor.close();
		adapter.close();
		
		return listPosition;
	}
	
	
	/**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * getPositionGroupList
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
	public List<PositionGroup> getPositionGroupList(String xWhere) {
		adapter.open();
		return getPositionGroupList(adapter.getPositionGroupList(" AND " + DatabaseAdapter.KEY_ISDELETED + " = 0 " + xWhere));
	}
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * getPositionGroupList
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
	private List<PositionGroup> getPositionGroupList(Cursor cursor) {
		List<PositionGroup> mainlist = new ArrayList<PositionGroup>();
		PositionGroup listPosition;
		if (cursor.getCount() >= 1) {
			cursor.moveToFirst();
			do {
				listPosition = new PositionGroup();
				/** code chức danh */
				listPosition.code = Integer.parseInt(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_CODE)));
				/** tên chức danh */
				listPosition.name = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_NAME));
				/** tên tắt */
				listPosition.ryaku = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_RYAKU));
				/** ghi chú */
				listPosition.note = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_NOTE));
			
				listPosition.yobi_code1 = Integer.parseInt(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_CODE1)));
				listPosition.yobi_code2 = Integer.parseInt(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_CODE2)));
				listPosition.yobi_code3 = Integer.parseInt(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_CODE3)));
				listPosition.yobi_code4 = Integer.parseInt(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_CODE4)));
				listPosition.yobi_code5 = Integer.parseInt(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_CODE5)));
				listPosition.yobi_text1   = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_TEXT1));
				listPosition.yobi_text2 = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_TEXT2));
				listPosition.yobi_text3 = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_TEXT3));
				listPosition.yobi_text4 = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_TEXT4));
				listPosition.yobi_text5 = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_TEXT5));
				listPosition.yobi_date1   = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_DATE1));
				listPosition.yobi_date2 = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_DATE2));
				listPosition.yobi_date3 = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_DATE3));
				listPosition.yobi_date4 = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_DATE4));
				listPosition.yobi_date5 = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_DATE5));
				listPosition.yobi_real1 = Float.parseFloat(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_REAL1)));
				listPosition.yobi_real2 = Float.parseFloat(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_REAL2)));
				listPosition.yobi_real3 = Float.parseFloat(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_REAL3)));
				listPosition.yobi_real4 = Float.parseFloat(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_REAL4)));
				listPosition.yobi_real5 = Float.parseFloat(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_REAL5)));
				listPosition.up_date = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_UP_DATE));
				listPosition.ad_date = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_AD_DATE));
				listPosition.opid = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_OPID));
				
				
				mainlist.add(listPosition);
				cursor.moveToNext();
			} while (!cursor.isAfterLast());
		}
		cursor.close();
		adapter.close();
		
		return mainlist;
	}
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * getPositionGroupByCode
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
	public PositionGroup getPositionGroupByCode(Integer code) {
		adapter.open();
		return getPositionGroupByCode(adapter.getPositionGroupByCode(code));
	}
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * getPositionGroupByCode
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
	private PositionGroup getPositionGroupByCode(Cursor cursor) {
		PositionGroup listPosition=null;
		if (cursor.getCount() >= 1) {
			cursor.moveToFirst();
			do {
				listPosition = new PositionGroup();
				/** code chức danh */
				listPosition.code = Integer.parseInt(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_CODE)));
				/** tên chức danh */
				listPosition.name = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_NAME));
				/** tên tắt */
				listPosition.ryaku = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_RYAKU));
				/** ghi chú */
				listPosition.note = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_NOTE));
			
				listPosition.yobi_code1 = Integer.parseInt(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_CODE1)));
				listPosition.yobi_code2 = Integer.parseInt(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_CODE2)));
				listPosition.yobi_code3 = Integer.parseInt(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_CODE3)));
				listPosition.yobi_code4 = Integer.parseInt(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_CODE4)));
				listPosition.yobi_code5 = Integer.parseInt(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_CODE5)));
				listPosition.yobi_text1   = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_TEXT1));
				listPosition.yobi_text2 = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_TEXT2));
				listPosition.yobi_text3 = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_TEXT3));
				listPosition.yobi_text4 = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_TEXT4));
				listPosition.yobi_text5 = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_TEXT5));
				listPosition.yobi_date1   = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_DATE1));
				listPosition.yobi_date2 = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_DATE2));
				listPosition.yobi_date3 = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_DATE3));
				listPosition.yobi_date4 = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_DATE4));
				listPosition.yobi_date5 = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_DATE5));
				listPosition.yobi_real1 = Float.parseFloat(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_REAL1)));
				listPosition.yobi_real2 = Float.parseFloat(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_REAL2)));
				listPosition.yobi_real3 = Float.parseFloat(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_REAL3)));
				listPosition.yobi_real4 = Float.parseFloat(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_REAL4)));
				listPosition.yobi_real5 = Float.parseFloat(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_REAL5)));
				listPosition.up_date = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_UP_DATE));
				listPosition.ad_date = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_AD_DATE));
				listPosition.opid = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_OPID));
				
				cursor.moveToNext();
			} while (!cursor.isAfterLast());
		}
		cursor.close();
		adapter.close();
		
		return listPosition;
	}
	/**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * getUSerPositionGroupList
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
	public List<UserPositionGroup> getUSerPositionGroupList(int user_code) {
		adapter.open();
		List<UserPositionGroup> mainlist= getUSerPositionGroupList(adapter.getUserPositionGroupList(user_code, ""));
		adapter.close();
		return mainlist;
	}
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * getPositionGroupList
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
	private List<UserPositionGroup> getUSerPositionGroupList(Cursor cursor) {
		List<UserPositionGroup> mainlist = new ArrayList<UserPositionGroup>();
		UserPositionGroup listPosition;
		if (cursor.getCount() >= 1) {
			cursor.moveToFirst();
			do {
				listPosition = new UserPositionGroup();
				/** code user nhóm chức danh */
				listPosition.code = Integer.parseInt(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_CODE)));
				/** code user */
				listPosition.user_code = Integer.parseInt(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_USER_CODE)));
				/** code nhóm chức danh */
				listPosition.position_group_code = Integer.parseInt(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_POSITION_GROUP_CODE)));
				/** tên nhóm chức danh */
				listPosition.name = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_NAME));
				/** tên tắt */
				listPosition.ryaku = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_RYAKU));
				/** ghi chú */
				listPosition.note = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_NOTE));
			
				listPosition.yobi_code1 = Integer.parseInt(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_CODE1)));
				listPosition.yobi_code2 = Integer.parseInt(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_CODE2)));
				listPosition.yobi_code3 = Integer.parseInt(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_CODE3)));
				listPosition.yobi_code4 = Integer.parseInt(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_CODE4)));
				listPosition.yobi_code5 = Integer.parseInt(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_CODE5)));
				listPosition.yobi_text1   = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_TEXT1));
				listPosition.yobi_text2 = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_TEXT2));
				listPosition.yobi_text3 = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_TEXT3));
				listPosition.yobi_text4 = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_TEXT4));
				listPosition.yobi_text5 = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_TEXT5));
				listPosition.yobi_date1   = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_DATE1));
				listPosition.yobi_date2 = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_DATE2));
				listPosition.yobi_date3 = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_DATE3));
				listPosition.yobi_date4 = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_DATE4));
				listPosition.yobi_date5 = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_DATE5));
				listPosition.yobi_real1 = Float.parseFloat(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_REAL1)));
				listPosition.yobi_real2 = Float.parseFloat(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_REAL2)));
				listPosition.yobi_real3 = Float.parseFloat(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_REAL3)));
				listPosition.yobi_real4 = Float.parseFloat(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_REAL4)));
				listPosition.yobi_real5 = Float.parseFloat(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_REAL5)));
				listPosition.up_date = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_UP_DATE));
				listPosition.ad_date = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_AD_DATE));
				listPosition.opid = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_OPID));
				
				mainlist.add(listPosition);
				cursor.moveToNext();
			} while (!cursor.isAfterLast());
		}
		cursor.close();
		return mainlist;
	}
   
	//START
	/**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * getCustomerGroupList
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
	public List<CustomerGroup> getCustomerGroupList(String xWhere) {
		adapter.open();
		return getCustomerGroupList(adapter.getCustomerGroupList(" AND " + DatabaseAdapter.KEY_ISDELETED + " = 0 " + xWhere));
	}
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * getCustomerGroupList
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
	private List<CustomerGroup> getCustomerGroupList(Cursor cursor) {
		List<CustomerGroup> mainlist = new ArrayList<CustomerGroup>();
		CustomerGroup listPosition;
		if (cursor.getCount() >= 1) {
			cursor.moveToFirst();
			do {
				listPosition = new CustomerGroup();
				/** code chức danh */
				listPosition.code = Integer.parseInt(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_CODE)));
				/** tên chức danh */
				listPosition.name = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_NAME));
				/** tên tắt */
				listPosition.ryaku = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_RYAKU));
				/** ghi chú */
				listPosition.note = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_NOTE));
			
				listPosition.yobi_code1 = Integer.parseInt(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_CODE1)));
				listPosition.yobi_code2 = Integer.parseInt(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_CODE2)));
				listPosition.yobi_code3 = Integer.parseInt(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_CODE3)));
				listPosition.yobi_code4 = Integer.parseInt(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_CODE4)));
				listPosition.yobi_code5 = Integer.parseInt(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_CODE5)));
				listPosition.yobi_text1   = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_TEXT1));
				listPosition.yobi_text2 = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_TEXT2));
				listPosition.yobi_text3 = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_TEXT3));
				listPosition.yobi_text4 = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_TEXT4));
				listPosition.yobi_text5 = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_TEXT5));
				listPosition.yobi_date1   = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_DATE1));
				listPosition.yobi_date2 = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_DATE2));
				listPosition.yobi_date3 = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_DATE3));
				listPosition.yobi_date4 = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_DATE4));
				listPosition.yobi_date5 = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_DATE5));
				listPosition.yobi_real1 = Float.parseFloat(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_REAL1)));
				listPosition.yobi_real2 = Float.parseFloat(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_REAL2)));
				listPosition.yobi_real3 = Float.parseFloat(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_REAL3)));
				listPosition.yobi_real4 = Float.parseFloat(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_REAL4)));
				listPosition.yobi_real5 = Float.parseFloat(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_REAL5)));
				listPosition.up_date = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_UP_DATE));
				listPosition.ad_date = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_AD_DATE));
				listPosition.opid = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_OPID));
				
				
				mainlist.add(listPosition);
				cursor.moveToNext();
			} while (!cursor.isAfterLast());
		}
		cursor.close();
		adapter.close();
		
		return mainlist;
	}
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * getCustomerGroupByCode
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
	public CustomerGroup getCustomerGroupByCode(Integer code) {
		adapter.open();
		return getCustomerGroupByCode(adapter.getCustomerGroupByCode(code));
	}
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * getPositionGroupByCode
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
	private CustomerGroup getCustomerGroupByCode(Cursor cursor) {
		CustomerGroup listPosition=null;
		if (cursor.getCount() >= 1) {
			cursor.moveToFirst();
			do {
				listPosition = new CustomerGroup();
				/** code chức danh */
				listPosition.code = Integer.parseInt(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_CODE)));
				/** tên chức danh */
				listPosition.name = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_NAME));
				/** tên tắt */
				listPosition.ryaku = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_RYAKU));
				/** ghi chú */
				listPosition.note = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_NOTE));
			
				listPosition.yobi_code1 = Integer.parseInt(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_CODE1)));
				listPosition.yobi_code2 = Integer.parseInt(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_CODE2)));
				listPosition.yobi_code3 = Integer.parseInt(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_CODE3)));
				listPosition.yobi_code4 = Integer.parseInt(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_CODE4)));
				listPosition.yobi_code5 = Integer.parseInt(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_CODE5)));
				listPosition.yobi_text1   = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_TEXT1));
				listPosition.yobi_text2 = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_TEXT2));
				listPosition.yobi_text3 = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_TEXT3));
				listPosition.yobi_text4 = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_TEXT4));
				listPosition.yobi_text5 = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_TEXT5));
				listPosition.yobi_date1   = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_DATE1));
				listPosition.yobi_date2 = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_DATE2));
				listPosition.yobi_date3 = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_DATE3));
				listPosition.yobi_date4 = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_DATE4));
				listPosition.yobi_date5 = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_DATE5));
				listPosition.yobi_real1 = Float.parseFloat(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_REAL1)));
				listPosition.yobi_real2 = Float.parseFloat(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_REAL2)));
				listPosition.yobi_real3 = Float.parseFloat(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_REAL3)));
				listPosition.yobi_real4 = Float.parseFloat(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_REAL4)));
				listPosition.yobi_real5 = Float.parseFloat(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_REAL5)));
				listPosition.up_date = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_UP_DATE));
				listPosition.ad_date = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_AD_DATE));
				listPosition.opid = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_OPID));
				
				cursor.moveToNext();
			} while (!cursor.isAfterLast());
		}
		cursor.close();
		adapter.close();
		
		return listPosition;
	}
	/**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * getUSerCustomerGroupList
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
	public List<UserCustomerGroup> getUSerCustomerGroupList(int user_code) {
		adapter.open();
		List<UserCustomerGroup> mainlist= getUSerCustomerGroupList(adapter.getUserCustomerGroupList(user_code, ""));
		adapter.close();
		return mainlist;
	}
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * getPositionGroupList
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
	private List<UserCustomerGroup> getUSerCustomerGroupList(Cursor cursor) {
		List<UserCustomerGroup> mainlist = new ArrayList<UserCustomerGroup>();
		UserCustomerGroup listPosition;
		if (cursor.getCount() >= 1) {
			cursor.moveToFirst();
			do {
				listPosition = new UserCustomerGroup();
				/** code user nhóm chức danh */
				listPosition.code = Integer.parseInt(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_CODE)));
				/** code user */
				listPosition.user_code = Integer.parseInt(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_USER_CODE)));
				/** code nhóm chức danh */
				listPosition.customer_group_code = Integer.parseInt(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_CUSTOMER_GROUP_CODE)));
				/** tên nhóm chức danh */
				listPosition.name = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_NAME));
				/** tên tắt */
				listPosition.ryaku = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_RYAKU));
				/** ghi chú */
				listPosition.note = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_NOTE));
			
				listPosition.yobi_code1 = Integer.parseInt(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_CODE1)));
				listPosition.yobi_code2 = Integer.parseInt(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_CODE2)));
				listPosition.yobi_code3 = Integer.parseInt(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_CODE3)));
				listPosition.yobi_code4 = Integer.parseInt(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_CODE4)));
				listPosition.yobi_code5 = Integer.parseInt(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_CODE5)));
				listPosition.yobi_text1   = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_TEXT1));
				listPosition.yobi_text2 = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_TEXT2));
				listPosition.yobi_text3 = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_TEXT3));
				listPosition.yobi_text4 = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_TEXT4));
				listPosition.yobi_text5 = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_TEXT5));
				listPosition.yobi_date1   = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_DATE1));
				listPosition.yobi_date2 = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_DATE2));
				listPosition.yobi_date3 = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_DATE3));
				listPosition.yobi_date4 = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_DATE4));
				listPosition.yobi_date5 = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_DATE5));
				listPosition.yobi_real1 = Float.parseFloat(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_REAL1)));
				listPosition.yobi_real2 = Float.parseFloat(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_REAL2)));
				listPosition.yobi_real3 = Float.parseFloat(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_REAL3)));
				listPosition.yobi_real4 = Float.parseFloat(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_REAL4)));
				listPosition.yobi_real5 = Float.parseFloat(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_REAL5)));
				listPosition.up_date = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_UP_DATE));
				listPosition.ad_date = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_AD_DATE));
				listPosition.opid = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_OPID));
				
				mainlist.add(listPosition);
				cursor.moveToNext();
			} while (!cursor.isAfterLast());
		}
		cursor.close();
		return mainlist;
	}
	
	//END
	
	/**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * getUserList
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
	public List<User> getUserList(String xWhere ) {
		String xWhereDefault ="";
		String xOrderDefault="";
		GetSearchItemSetting searchItem = new GetSearchItemSetting(context);
		GetSortSetting sort = new GetSortSetting(context);
		
		xWhereDefault = searchItem.getWhereSQL();
		xOrderDefault = sort.getSortSQL();
		adapter.open();
		List<User> list =getUserList(adapter.getUserList( " 1=1 " + xWhere + xWhereDefault , xOrderDefault));
		adapter.close();
		return list;
	}
	
	/**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * getUserList
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
	@SuppressWarnings("static-access")
	public List<User> getAllUserList(String xWhere ) {
		String xWhereDefault ="";
		String xOrderDefault="";
		//GetSearchItemSetting searchItem = new GetSearchItemSetting(context);
		GetSortSetting sort = new GetSortSetting(context);
		
		//xWhereDefault = searchItem.getWhereSQL();
		xOrderDefault = sort.getSortSQL();
		adapter.open();
		List<User> list =getUserList(adapter.getUserList( " 1=1 " + xWhere  , xOrderDefault));
		adapter.close();
		return list;
	}
	
	/**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * getUserList
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
	public List<User> getUserList(String xWhere , boolean isUseSearchSetting , boolean isUseSortSetting) {
		String xWhereDefault ="";
		String xOrderDefault="";
		if(isUseSearchSetting){
			GetSearchItemSetting searchItem = new GetSearchItemSetting(context);
			xWhereDefault = searchItem.getWhereSQL();
		}else{
			xWhereDefault="";
		}
		if(isUseSortSetting){
			GetSortSetting sort = new GetSortSetting(context);
			xOrderDefault = sort.getSortSQL();
		}else{
			xOrderDefault="";
		}
		
		adapter.open();
		List<User> list =getUserList(adapter.getUserList( " 1=1 " + xWhere + xWhereDefault , xOrderDefault));
		adapter.close();
		return list;
	}
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * getUserList
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
	private List<User> getUserList(Cursor cursor) {
		List<User> mainlist = new ArrayList<User>();
		User listUser;
		if (cursor.getCount() >= 1) {
			cursor.moveToFirst();
			do {
				listUser = new User();
				//listUser.id =Integer.parseInt(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_ID)));
				/** code  user */
				listUser.code = Integer.parseInt(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_CODE)));
				listUser.google_id = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_GOOGLE_CONTACT_ID));
				listUser.first_name = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_FIRST_NAME));
				listUser.last_name = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_LAST_NAME));
				listUser.full_name= cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_FULL_NAME));
				listUser.sex = Integer.parseInt(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_SEX)));
				listUser.birthday = DateTimeUtil.formatDate2String( cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_BIRTHDAY)),MasterConstants.DATE_JP_FORMAT,MasterConstants.DATE_VN_FORMAT);
				listUser.married_date = DateTimeUtil.formatDate2String( cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_MARRIED_DATE)),MasterConstants.DATE_JP_FORMAT,MasterConstants.DATE_VN_FORMAT);
				listUser.address = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_ADDRESS));
				listUser.home_tel = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_HOME_TEL));
				listUser.mobile = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_MOBILE));
				listUser.fax = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_FAX));
				listUser.position = Integer.parseInt(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_POSITION)));
				listUser.position_name = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_POSITION_NAME));
				listUser.dept =Integer.parseInt( cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_DEPT)));
				listUser.dept_name = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_DEPT_NAME));
				listUser.team =Integer.parseInt( cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_TEAM)));
				listUser.team_name = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_TEAM_NAME));
				listUser.tant =Integer.parseInt( cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_TANT)));
				listUser.tant_name = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_TANT_NAME));
				listUser.training_date = DateTimeUtil.formatDate2String( cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_TRAINING_DATE)),MasterConstants.DATE_JP_FORMAT,MasterConstants.DATE_VN_FORMAT);
				listUser.training_dateEnd = DateTimeUtil.formatDate2String( cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_TRAINING_DATE_END)),MasterConstants.DATE_JP_FORMAT,MasterConstants.DATE_VN_FORMAT);
				
				listUser.learn_training_date = DateTimeUtil.formatDate2String( cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_LEARN_TRAINING_DATE)),MasterConstants.DATE_JP_FORMAT,MasterConstants.DATE_VN_FORMAT);
				listUser.learn_training_dateEnd = DateTimeUtil.formatDate2String( cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_LEARN_TRAINING_DATE_END)),MasterConstants.DATE_JP_FORMAT,MasterConstants.DATE_VN_FORMAT);
				
				listUser.in_date = DateTimeUtil.formatDate2String( cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_IN_DATE)),MasterConstants.DATE_JP_FORMAT,MasterConstants.DATE_VN_FORMAT);
				listUser.convert_keiken = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_CONVERT_KEIKEN));
				
				listUser.join_date = DateTimeUtil.formatDate2String( cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_JOIN_DATE)),MasterConstants.DATE_JP_FORMAT,MasterConstants.DATE_VN_FORMAT);
				listUser.out_date = DateTimeUtil.formatDate2String( cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_OUT_DATE)),MasterConstants.DATE_JP_FORMAT,MasterConstants.DATE_VN_FORMAT);
				listUser.note = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_NOTE));
				listUser.isdeleted =Integer.parseInt( cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_ISDELETED)));
				listUser.email = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_EMAIL));
				listUser.japanese = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_JAPANESE));
				listUser.allowance_business = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_ALLOWANCE_BUSINESS));
				listUser.allowance_bse = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_ALLOWANCE_BSE));
				listUser.allowance_room = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_ALLOWANCE_ROOM));
				listUser.married =Integer.parseInt( cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_MARRIED)));
				listUser.salary_notallowance =Float.parseFloat( cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_SALARY_NOTALOWANCE)));
				listUser.salary_allowance = Float.parseFloat( cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_SALARY_ALOWANCE)));
				listUser.estimate_point= Float.parseFloat( cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_ESTIMATE_POINT)));
				listUser.tag1 = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_TAG1));
				listUser.tag2 = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_TAG2));
				listUser.img_fullpath = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_IMG_FULLPATH));
				listUser.isLabour =Integer.parseInt( cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_ISLABOUR)));
				listUser.labour_join_date = DateTimeUtil.formatDate2String( cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_LABOUR_JOIN_DATE)),MasterConstants.DATE_JP_FORMAT,MasterConstants.DATE_VN_FORMAT);
				listUser.labour_out_date = DateTimeUtil.formatDate2String( cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_LABOUR_OUT_DATE)),MasterConstants.DATE_JP_FORMAT,MasterConstants.DATE_VN_FORMAT);
				listUser.business_kbn = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_BUSINESS_KBN));
				listUser.staff_kbn= cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_STAFF_KBN));

				listUser.program =Float.parseFloat( cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_PROGRAM)));
				listUser.basicdesign =Float.parseFloat( cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_BASIC_DESIGN)));
				listUser.detaildesign =Float.parseFloat( cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_DETAIL_DESIGN)));

				listUser.gpa=Float.parseFloat( cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_GPA)));
				listUser.gpa_text= cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_GPA_TEXT));
				listUser.collect_name= cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_COLLECT_NAME));
				listUser.interviewer1= cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_INTERVIEWER1));
				listUser.interviewer2= cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_INTERVIEWER2));
				listUser.interviewer3= cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_INTERVIEWER3));
				listUser.interview_kekka= cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_INTERVIEW_KEKKA));
				listUser.supporter1= cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_SUPPORTER1));
				listUser.supporter2= cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_SUPPORTER2));
				listUser.supporter3= cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_SUPPORTER3));

				listUser.yobi_code1 = Integer.parseInt(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_CODE1)));
				listUser.yobi_code2 = Integer.parseInt(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_CODE2)));
				listUser.yobi_code3 = Integer.parseInt(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_CODE3)));
				listUser.yobi_code4 = Integer.parseInt(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_CODE4)));
				listUser.yobi_code5 = Integer.parseInt(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_CODE5)));
				listUser.yobi_text1   = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_TEXT1));
				listUser.yobi_text2 = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_TEXT2));
				listUser.yobi_text3 = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_TEXT3));
				listUser.yobi_text4 = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_TEXT4));
				listUser.yobi_text5 = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_TEXT5));
				listUser.yobi_date1   = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_DATE1));
				listUser.yobi_date2 = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_DATE2));
				listUser.yobi_date3 = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_DATE3));
				listUser.yobi_date4 = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_DATE4));
				listUser.yobi_date5 = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_DATE5));
				listUser.yobi_real1 = Float.parseFloat(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_REAL1)));
				listUser.yobi_real2 = Float.parseFloat(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_REAL2)));
				listUser.yobi_real3 = Float.parseFloat(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_REAL3)));
				listUser.yobi_real4 = Float.parseFloat(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_REAL4)));
				listUser.yobi_real5 = Float.parseFloat(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_REAL5)));
				listUser.up_date = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_UP_DATE));
				listUser.ad_date = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_AD_DATE));
				listUser.opid = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_OPID));
				/* truong hop co ton tai cac item ngoai table M_USER thi su dung tam cac item du bi nhung chua su dung hien tai
				* CHU Y
				* */
				if (cursor.getColumnIndex("NEW_POSITION_CODE")>0){
					listUser.NEW_POSITION_CODE = cursor.getString(cursor.getColumnIndex("NEW_POSITION_CODE"));
				}
				if (cursor.getColumnIndex("NEW_POSITION_RYAKU")>0){
					listUser.NEW_POSITION_RYAKU = cursor.getString(cursor.getColumnIndex("NEW_POSITION_RYAKU"));
				}
				if (cursor.getColumnIndex("NEW_POSITION_LEVEL")>0){
					listUser.NEW_POSITION_LEVEL = cursor.getString(cursor.getColumnIndex("NEW_POSITION_LEVEL"));
				}
				/**END*/

				mainlist.add(listUser);
				cursor.moveToNext();
			} while (!cursor.isAfterLast());
		}
		cursor.close();
		
		return mainlist;
	}
	
	/**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * getUserListFromView
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
	public List<User> getUserListFromView(int ExpGroupKey , String xWhere , String ViewName) {
		String xWhereDefault ="";
		String xOrderDefault="";
		GetSearchItemSetting searchItem = new GetSearchItemSetting(context);
		GetSortSetting sort = new GetSortSetting(context);
		List<User> list;
		xWhereDefault = searchItem.getWhereSQL();
		xOrderDefault = sort.getSortSQL();
		
		adapter.open();		
		if(ExpGroupKey==IExpGroup.EXP_GROUP_YASUMI_YEARMONTH){
			xWhereDefault = xWhereDefault.replace(new GetSearchItemSetting(context).getYasumiSQLString(), " AND " + DatabaseAdapter.KEY_OUT_DATE + "<>''");
			//neu co chi dinh VIEW thi se co truong hop get data khong dung--do tren view cung 1 staff co the co nhieu dong
			list =getUserList(adapter.getUserListFromView( " 1=1 " + xWhere + xWhereDefault , xOrderDefault,""));
		}if(ExpGroupKey==IExpGroup.EXP_GROUP_STAFF_CURRENT_POSITION_NOT_SATIFIED){
			//so nhan vien co ngach khong phu hop voi tham nien
			list =getUserList(adapter.getUserListFromView( " 1=1 " + xWhere + xWhereDefault , xOrderDefault,DatabaseAdapter.VIEW_USER_LIST_CURRENT_POSITION_NOT_SATIFIED_SQL));
		}
		else{
			list =getUserList(adapter.getUserListFromView( " 1=1 " + xWhere + xWhereDefault , xOrderDefault,DatabaseAdapter.VIEW_M_USER_CUSTOMER_GROUP));
		}
		adapter.close();		
		
		return list;
	}
	
	/**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * getUserListFromViewYasumiYearMonth
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
	public List<User> getUserListFromViewYasumiYearMonth(String xWhere , String ViewName) {
		String xWhereDefault ="";
		String xOrderDefault="";
		GetSearchItemSetting searchItem = new GetSearchItemSetting(context);
		GetSortSetting sort = new GetSortSetting(context);
		
		xWhereDefault = searchItem.getWhereSQL();
		xOrderDefault = sort.getSortSQL();

		xWhereDefault = xWhereDefault.replace(new GetSearchItemSetting(context).getYasumiSQLString(), " AND " + DatabaseAdapter.KEY_OUT_DATE + "<>''");
		
		adapter.open();
		List<User> list =getUserList(adapter.getUserListFromView( " 1=1 " + xWhere + xWhereDefault , xOrderDefault,DatabaseAdapter.VIEW_M_USER_CUSTOMER_GROUP));
		adapter.close();
		return list;
	}
	
	/**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * getUserListFromView
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
	public List<User> getUserListFromView(String xWhere , boolean isUseSearchSetting , boolean isUseSortSetting , String ViewName) {
		String xWhereDefault ="";
		String xOrderDefault="";
		if(isUseSearchSetting){
			GetSearchItemSetting searchItem = new GetSearchItemSetting(context);
			xWhereDefault = searchItem.getWhereSQL();
		}else{
			xWhereDefault="";
		}
		if(isUseSortSetting){
			GetSortSetting sort = new GetSortSetting(context);
			xOrderDefault = sort.getSortSQL();
		}else{
			xOrderDefault="";
		}
		
		adapter.open();
		List<User> list =getUserList(adapter.getUserListFromView( " 1=1 " + xWhere + xWhereDefault , xOrderDefault,DatabaseAdapter.VIEW_M_USER_CUSTOMER_GROUP));
		adapter.close();
		return list;
	}
	
	/**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * getUserHisDeptList
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
	public List<UserHistory> getUserHisList(int hisType , String xWhere , String xOrderBy) {
		String xWhereDefault ="";
		String xOrderDefault="";
		List<UserHistory> list=null;
		adapter.open();
		switch(hisType){
			case MasterConstants.MASTER_MKBN_DEPT_HIS:
				list =getUserHisList(adapter.getUserHisDeptList( xWhere ,xOrderBy));
				break;
			
			case MasterConstants.MASTER_MKBN_TEAM_HIS:
				list =getUserHisList(adapter.getUserHisTeamList( xWhere ,xOrderBy));
				break;
				
			case MasterConstants.MASTER_MKBN_POSITION_HIS:
				list =getUserHisList(adapter.getUserHisPositionList( xWhere ,xOrderBy));
				break;
				
			case MasterConstants.MASTER_MKBN_JAPANESE_HIS:
				list =getUserHisList(adapter.getUserHisJapaneseList( xWhere ,xOrderBy));
				break;
				
			case MasterConstants.MASTER_MKBN_ALLOWANCE_BUSINESS_HIS:
				list =getUserHisList(adapter.getUserHisAllowance_BusinessList( xWhere ,xOrderBy));
				break;
			case MasterConstants.MASTER_MKBN_ALLOWANCE_BSE_HIS:
				list =getUserHisList(adapter.getUserHisAllowance_BSEList( xWhere ,xOrderBy));
				break;
			case MasterConstants.MASTER_MKBN_SALARY_HIS:
				list =getUserHisList(adapter.getUserHisSalaryList( xWhere ,xOrderBy));
				break;	
		}
		
		adapter.close();
		return list;
	}
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * getUserHistList
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
	private List<UserHistory> getUserHisList(Cursor cursor) {
		List<UserHistory> mainlist = new ArrayList<UserHistory>();
		UserHistory listUser;
		if (cursor.getCount() >= 1) {
			cursor.moveToFirst();
			do {
				listUser = new UserHistory();
				//listUser.id =Integer.parseInt(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_ID)));
				/** code  user */
				listUser.code = Integer.parseInt(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_CODE)));
				listUser.user_code = Integer.parseInt(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_USER_CODE)));
				listUser.mkbn= Integer.parseInt(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_MKBN)));
				listUser.full_name= cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_FULL_NAME));
				
				listUser.new_dept_code =Integer.parseInt( cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_NEW_DEPT_CODE)));
				listUser.new_dept_name=cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_DEPT_NAME));
				listUser.new_team_code=Integer.parseInt( cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_NEW_TEAM_CODE)));
				listUser.new_team_name=cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_TEAM_NAME));
				listUser.new_position_code=Integer.parseInt( cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_NEW_POSITION_CODE)));
				listUser.new_position_name=cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_POSITION_NAME));
				listUser.new_japanese=cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_NEW_JAPANESE));
				
				listUser.new_allowance_business=cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_NEW_ALLOWANCE_BUSINESS));
				listUser.new_allowance_bse=cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_NEW_ALLOWANCE_BSE));
				listUser.new_salary_standard= Float.parseFloat(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_SALARY_STANDARD)));
				listUser.new_salary_percent= Float.parseFloat(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_SALARY_PERCENT)));
				listUser.new_salary_actual_up= Float.parseFloat(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_SALARY_ACTUAL_UP)));
				listUser.new_salary_next_ym= cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_SALARY_NEXT_YM));
				listUser.new_salary= Float.parseFloat(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_NEW_SALARY)));
				
				listUser.date_from= DateTimeUtil.formatDate2String( cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_DATE_FROM)),MasterConstants.DATE_JP_FORMAT,MasterConstants.DATE_VN_FORMAT);
				listUser.date_to= DateTimeUtil.formatDate2String( cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_DATE_TO)),MasterConstants.DATE_JP_FORMAT,MasterConstants.DATE_VN_FORMAT);
				listUser.reason = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_REASON));
				listUser.note = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_NOTE));
				listUser.isdeleted =Integer.parseInt( cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_ISDELETED)));
				listUser.img_fullpath = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_IMG_FULLPATH));
								
				listUser.yobi_code1 = Integer.parseInt(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_CODE1)));
				listUser.yobi_code2 = Integer.parseInt(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_CODE2)));
				listUser.yobi_code3 = Integer.parseInt(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_CODE3)));
				listUser.yobi_code4 = Integer.parseInt(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_CODE4)));
				listUser.yobi_code5 = Integer.parseInt(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_CODE5)));
				listUser.yobi_text1   = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_TEXT1));
				listUser.yobi_text2 = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_TEXT2));
				listUser.yobi_text3 = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_TEXT3));
				listUser.yobi_text4 = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_TEXT4));
				listUser.yobi_text5 = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_TEXT5));
				listUser.yobi_date1   = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_DATE1));
				listUser.yobi_date2 = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_DATE2));
				listUser.yobi_date3 = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_DATE3));
				listUser.yobi_date4 = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_DATE4));
				listUser.yobi_date5 = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_DATE5));
				listUser.yobi_real1 = Float.parseFloat(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_REAL1)));
				listUser.yobi_real2 = Float.parseFloat(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_REAL2)));
				listUser.yobi_real3 = Float.parseFloat(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_REAL3)));
				listUser.yobi_real4 = Float.parseFloat(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_REAL4)));
				listUser.yobi_real5 = Float.parseFloat(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_REAL5)));
				listUser.up_date = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_UP_DATE));
				listUser.ad_date = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_AD_DATE));
				listUser.opid = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_OPID));
				
				mainlist.add(listUser);
				cursor.moveToNext();
			} while (!cursor.isAfterLast());
		}
		cursor.close();
		
		return mainlist;
	}
	
	/**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * getUserMessageStatusList
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
	public List<UserMessageStatus> getUserMessageStatusList(String xWhere) {
		adapter.open();
		return getUserMessageStatusList(adapter.getMessageList( " AND " + DatabaseAdapter.KEY_ISDELETED + " = 0 "+ xWhere) );
	}
	
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * getUserMessageStatusList
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
	private List<UserMessageStatus> getUserMessageStatusList(Cursor cursor) {
		List<UserMessageStatus> mainlist = new ArrayList<UserMessageStatus>();
		UserMessageStatus  listUserMessageStatus;
		if (cursor.getCount() >= 1) {
			cursor.moveToFirst();
			do {
				listUserMessageStatus = new UserMessageStatus();
				
				/** code  message */
				listUserMessageStatus.code = Integer.parseInt(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_CODE)));
				/** cod nhan vien*/
				listUserMessageStatus.message_emp_code = Integer.parseInt(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_MESSAGE_EMP_CODE)));
				/** code message template */
				listUserMessageStatus.message_template_code = Integer.parseInt(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_MESSAGE_TEMPLATE_CODE)));
				/** code  loai message */
				listUserMessageStatus.message_type = Integer.parseInt(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_MESSAGE_TYPE)));
				/** sender code*/
				listUserMessageStatus.sender_code= Integer.parseInt(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_SENDER_CODE)));
				/** sender phone*/
				listUserMessageStatus.sender_phone= (cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_SENDER_PHONE)));
				/** sender mail*/
				listUserMessageStatus.sender_mail= (cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_SENDER_MAIL)));
				/** receiver code*/
				listUserMessageStatus.receiver_code= Integer.parseInt(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_RECEIVER_CODE)));
				/** receiver phone*/
				listUserMessageStatus.receiver_phone= (cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_RECEIVER_PHONE)));
				/** receiver mail*/
				listUserMessageStatus.receiver_mail= (cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_RECEIVER_MAIL)));
				/** send package*/
				listUserMessageStatus.send_package= (cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_SEND_PACKAGE)));
				/** send date time*/
				listUserMessageStatus.send_datetime= (cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_SEND_DATETIME)));
				/** SMS?*/
				listUserMessageStatus.issms=Integer.parseInt( cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_ISSMS)));
				/** EMAIL?*/
				listUserMessageStatus.isemail =Integer.parseInt( cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_ISEMAIL)));
				/** ATIVE?*/
				listUserMessageStatus.isactive =Integer.parseInt( cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_ISACTIVE)));
								
				/** xoa chua*/
				listUserMessageStatus.isdeleted =Integer.parseInt( cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_ISDELETED)));
				/** dinh kem file */
				listUserMessageStatus.attach_file = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_ATTACH_FILE));
				/** send status*/
				listUserMessageStatus.send_status=Integer.parseInt( cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_SEND_STATUS)));
				/** schedule send datetime*/
				listUserMessageStatus.schedule_send_datetime=( cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_SCHEDULE_SEND_DATETIME)));
				/** schedule send loop*/
				listUserMessageStatus.schedule_send_loop=( cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_SCHEDULE_SEND_LOOP)));
				
				/** tên message */
				listUserMessageStatus.name = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_NAME)) 
								+ "(" + cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_TEXT1)) + ")";
				/** tên tắt */
				listUserMessageStatus.ryaku = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_RYAKU));
				/** yobi*/
				listUserMessageStatus.yobi_text1 = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_TEXT1));
				/** */
				listUserMessageStatus.yobi_text2= cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_TEXT2));
				/** ghi chú */
				listUserMessageStatus.note = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_NOTE));
			
				listUserMessageStatus.yobi_code1 = Integer.parseInt(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_CODE1)));
				listUserMessageStatus.yobi_code2 = Integer.parseInt(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_CODE2)));
				listUserMessageStatus.yobi_code3 = Integer.parseInt(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_CODE3)));
				listUserMessageStatus.yobi_code4 = Integer.parseInt(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_CODE4)));
				listUserMessageStatus.yobi_code5 = Integer.parseInt(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_CODE5)));
				listUserMessageStatus.yobi_text1   = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_TEXT1));
				listUserMessageStatus.yobi_text2 = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_TEXT2));
				listUserMessageStatus.yobi_text3 = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_TEXT3));
				listUserMessageStatus.yobi_text4 = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_TEXT4));
				listUserMessageStatus.yobi_text5 = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_TEXT5));
				listUserMessageStatus.yobi_date1   = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_DATE1));
				listUserMessageStatus.yobi_date2 = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_DATE2));
				listUserMessageStatus.yobi_date3 = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_DATE3));
				listUserMessageStatus.yobi_date4 = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_DATE4));
				listUserMessageStatus.yobi_date5 = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_DATE5));
				listUserMessageStatus.yobi_real1 = Float.parseFloat(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_REAL1)));
				listUserMessageStatus.yobi_real2 = Float.parseFloat(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_REAL2)));
				listUserMessageStatus.yobi_real3 = Float.parseFloat(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_REAL3)));
				listUserMessageStatus.yobi_real4 = Float.parseFloat(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_REAL4)));
				listUserMessageStatus.yobi_real5 = Float.parseFloat(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_REAL5)));
				listUserMessageStatus.up_date = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_UP_DATE));
				listUserMessageStatus.ad_date = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_AD_DATE));
				listUserMessageStatus.opid = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_OPID));
				
				mainlist.add(listUserMessageStatus);
				cursor.moveToNext();
			} while (!cursor.isAfterLast());
		}
		cursor.close();
		adapter.close();
		
		return mainlist;
	}
	
	/**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * getUserMessageItemList
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
	public List<MessageItem> getUserMessageItemList(Cursor cursor) {
		List<MessageItem> mainlist = new ArrayList<MessageItem>();
		MasterConstants.MESSAGE_TYPE msgType  ;
		MessageItem  listUserMessageStatus;
		if (cursor.getCount() >= 1) {
			cursor.moveToFirst();
			do {
				listUserMessageStatus = new MessageItem();
				
				User usr = null ;
				List<User> userList = getUserList( " AND " + DatabaseAdapter.KEY_CODE + "=" +  Integer.parseInt(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_MESSAGE_EMP_CODE)))
									, false, false);
				if(userList.size()>0){
					usr = userList.get(0);
				}
				//thong tin user
				listUserMessageStatus.setEmpInfo(usr);
				listUserMessageStatus.setMessageType( Integer.parseInt(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_MESSAGE_TYPE))));
				if(usr!=null){
					listUserMessageStatus.setEmpName(usr.full_name);	
				}
				listUserMessageStatus.setEmpCode(Integer.parseInt(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_MESSAGE_EMP_CODE))));
				listUserMessageStatus.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_CODE))));
				listUserMessageStatus.setSendDate((cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_SEND_DATETIME))));
				
				int status = Integer.parseInt(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_SEND_STATUS)));
				
				listUserMessageStatus.setMessageStatus(MESSAGE_STATUS.values()[status]);
				listUserMessageStatus.setMsgTemplateCode( Integer.parseInt(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_MESSAGE_TEMPLATE_CODE))));
				
				switch(listUserMessageStatus.getMessageType()){
				
					case MESSAGE_TYPE_CONST.BIRTHDAY:
						listUserMessageStatus.setIcon(context.getResources().getDrawable(R.drawable.cookie_128));		
						break;
					case MESSAGE_TYPE_CONST.SALARY:
						listUserMessageStatus.setIcon(context.getResources().getDrawable(R.drawable.currency_dollar_red));
						break;
					case MESSAGE_TYPE_CONST.YASUMI:
						listUserMessageStatus.setIcon(context.getResources().getDrawable(R.drawable.switch_user));
						break;
					case MESSAGE_TYPE_CONST.TRAIL:
						listUserMessageStatus.setIcon(context.getResources().getDrawable(R.drawable.contact_new));
						break;
						
				}
				
				mainlist.add(listUserMessageStatus);
				cursor.moveToNext();
			} while (!cursor.isAfterLast());
		}
		cursor.close();
		adapter.close();
		
		return mainlist;
	}
	/**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * getUserMessageItemList
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
	public List<MessageItem> getUserMessageItemList(String xWhere) {
		adapter.open();
		return getUserMessageItemList(adapter.getMessageList( " AND " + DatabaseAdapter.KEY_ISDELETED + " = 0 "+ xWhere) );
	}
	
	/**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * getMessageTemplateList
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
	public List<MessageTemplate> getMessageTemplateList(String xWhere) {
		adapter.open();
		return getMessageTemplateList(adapter.getMessageTemplateList(xWhere, null, false));
	}
	
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * getMessageTemplateList
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
	private List<MessageTemplate> getMessageTemplateList(Cursor cursor) {
		List<MessageTemplate> mainlist = new ArrayList<MessageTemplate>();
		MessageTemplate  listUserMessageStatus;
		if (cursor.getCount() >= 1) {
			cursor.moveToFirst();
			do {
				listUserMessageStatus = new MessageTemplate();
				
				/** code template*/
				listUserMessageStatus.code = Integer.parseInt(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_CODE)));
				/** mkbn*/
				listUserMessageStatus.mkbn = Integer.parseInt(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_MKBN)));
						
				/** xoa chua*/
				listUserMessageStatus.isdeleted =Integer.parseInt( cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_ISDELETED)));
				/** noi dung*/
				listUserMessageStatus.content = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_CONTENT));
				/** tel*/
				listUserMessageStatus.telephone= cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_TELEPHONE));
				/** email*/
				listUserMessageStatus.email= cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_EMAIL));
				/** tên template */
				listUserMessageStatus.name = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_NAME));
				/** tên tắt */
				listUserMessageStatus.ryaku = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_RYAKU));
				/** yobi*/
				listUserMessageStatus.yobi_text1 = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_TEXT1));
				/** */
				listUserMessageStatus.yobi_text2= cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_TEXT2));
				/** ghi chú */
				listUserMessageStatus.note = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_NOTE));
			
				listUserMessageStatus.yobi_code1 = Integer.parseInt(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_CODE1)));
				listUserMessageStatus.yobi_code2 = Integer.parseInt(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_CODE2)));
				listUserMessageStatus.yobi_code3 = Integer.parseInt(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_CODE3)));
				listUserMessageStatus.yobi_code4 = Integer.parseInt(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_CODE4)));
				listUserMessageStatus.yobi_code5 = Integer.parseInt(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_CODE5)));
				listUserMessageStatus.yobi_text1   = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_TEXT1));
				listUserMessageStatus.yobi_text2 = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_TEXT2));
				listUserMessageStatus.yobi_text3 = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_TEXT3));
				listUserMessageStatus.yobi_text4 = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_TEXT4));
				listUserMessageStatus.yobi_text5 = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_TEXT5));
				listUserMessageStatus.yobi_date1   = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_DATE1));
				listUserMessageStatus.yobi_date2 = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_DATE2));
				listUserMessageStatus.yobi_date3 = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_DATE3));
				listUserMessageStatus.yobi_date4 = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_DATE4));
				listUserMessageStatus.yobi_date5 = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_DATE5));
				listUserMessageStatus.yobi_real1 = Float.parseFloat(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_REAL1)));
				listUserMessageStatus.yobi_real2 = Float.parseFloat(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_REAL2)));
				listUserMessageStatus.yobi_real3 = Float.parseFloat(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_REAL3)));
				listUserMessageStatus.yobi_real4 = Float.parseFloat(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_REAL4)));
				listUserMessageStatus.yobi_real5 = Float.parseFloat(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_REAL5)));
				listUserMessageStatus.up_date = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_UP_DATE));
				listUserMessageStatus.ad_date = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_AD_DATE));
				listUserMessageStatus.opid = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_OPID));
				
				mainlist.add(listUserMessageStatus);
				cursor.moveToNext();
			} while (!cursor.isAfterLast());
		}
		cursor.close();
		adapter.close();
		
		return mainlist;
	}
	
	
}