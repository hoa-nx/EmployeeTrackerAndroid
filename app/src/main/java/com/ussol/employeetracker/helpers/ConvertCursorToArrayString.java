/**
 * Copyright (c) 2012 Fujinet Software Department 5 ( USSOL team)  
 * See the file license.txt for copying permission.
*/  

package com.ussol.employeetracker.helpers;

import java.util.ArrayList;
import java.util.List;

import com.ussol.employeetracker.models.ChartItem;
import com.ussol.employeetracker.models.Dept;
import com.ussol.employeetracker.models.MasterConstants;
import com.ussol.employeetracker.models.Position;
import com.ussol.employeetracker.models.Team;

import android.content.Context;
import android.database.Cursor;

/**
 * @author hoa-nx
 *
 */
public class ConvertCursorToArrayString {

	private Context context;
	private DatabaseAdapter adapter;
	private String xWhereDefault ="";
	private String xOrderDefault="";
		
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * ConvertCursorToListString
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
	public ConvertCursorToArrayString(Context context) {
		this.context = context;
		adapter = new DatabaseAdapter(context);
		
	}
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * getDeptList
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
	public String[] getDeptList() {
		adapter.open();
		ArrayList<String> list = getDeptList(adapter.getDeptList( " AND " + DatabaseAdapter.KEY_ISDELETED + " = 0"));
		String[] returnArr = new String[list.size()];
		returnArr = list.toArray(returnArr);
		adapter.close();
		return returnArr;
	}
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * getDeptList
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
	private ArrayList<String> getDeptList(Cursor cursor) {
		Dept listDept;
		ArrayList<String> list = new ArrayList<String>();
		if (cursor.getCount() >= 1) {
			cursor.moveToFirst();
			do {
				listDept = new Dept();

				/** code  phòng ban */
				//listDept.code = Integer.parseInt(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_CODE)));
				/** tên phòng ban */
				listDept.name = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_NAME)) 
								+ " ( " + cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_TEXT1)) + " )";
				list.add(listDept.name);
				cursor.moveToNext();
			} while (!cursor.isAfterLast());
		}
		cursor.close();
		
		return list;
	}
	/**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * getTeamList
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
	public String[] getTeamList() {
		adapter.open();
		ArrayList<String> list = getTeamList(adapter.getTeamList( " AND " + DatabaseAdapter.KEY_ISDELETED + " = 0"));
		String[] returnArr = new String[list.size()];
		returnArr = list.toArray(returnArr);
		adapter.close();
		return returnArr;
	}
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * getTeamList
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
	private ArrayList<String> getTeamList(Cursor cursor) {
		Team listTeam;
		ArrayList<String> list = new ArrayList<String>();
		if (cursor.getCount() >= 1) {
			cursor.moveToFirst();
			do {
				listTeam = new Team();

				/** code  nhom */
				//listTeam.code = Integer.parseInt(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_CODE)));
				/** tên nhóm */
				listTeam.name = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_NAME)) 
								+ " ( " + cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_TEXT1)) + " )";
				list.add(listTeam.name);
				cursor.moveToNext();
			} while (!cursor.isAfterLast());
		}
		
		cursor.close();
				
		return list;
	}
	/**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * getTeamList
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
	public String[] getPositionList() {
		adapter.open();
		ArrayList<String> list = getPositionList(adapter.getPositionList( " AND " + DatabaseAdapter.KEY_ISDELETED + " = 0"));
		String[] returnArr = new String[list.size()];
		returnArr = list.toArray(returnArr);
		adapter.close();
		return returnArr;
	}
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * getTeamList
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
	private ArrayList<String> getPositionList(Cursor cursor) {
		Position listPosition;
		ArrayList<String> list = new ArrayList<String>();
		if (cursor.getCount() >= 1) {
			cursor.moveToFirst();
			do {
				listPosition = new Position();

				/** code  nhóm */
				//listPosition.code = Integer.parseInt(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_CODE)));
				/** tên nhóm */
				listPosition.name = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_NAME)) 
								+ " ( " + cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YOBI_TEXT1)) + " )";
				list.add(listPosition.name);
				cursor.moveToNext();
			} while (!cursor.isAfterLast());
		}
		
		cursor.close();
		
		return list;
	}
	
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * getUserGroup 
     * @param <T> : chỉ định kiểu cần chuyển đổi
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲
     */
	public <T> ArrayList<T> getUserGroup(String xKeyGroup) {
		int addNullOrEmptyCount=0;
		GetSearchItemSetting searchItem = new GetSearchItemSetting(context);
		GetSortSetting sort = new GetSortSetting(context);
		
		xWhereDefault = searchItem.getWhereSQL();
		xOrderDefault = sort.getSortSQL();
		//open ket noi
		adapter.open();
		//get data tu database
		Cursor cursor = adapter.getUserGroupByWithWhere(xKeyGroup,xWhereDefault);
		ArrayList<T> list = new ArrayList<T>();
		//loop qua ket qua tra ve 
		if (cursor.getCount() >= 1) {
			//di chuyen toi dong dau tien
			cursor.moveToFirst();
			do {

				if(cursor.getString(cursor.getColumnIndex("GRP"))==null 
						|| cursor.getString(cursor.getColumnIndex("GRP")).equals("")){ 
					if(addNullOrEmptyCount==0){
						/** tên item group*/
						list.add((T) cursor.getString(cursor.getColumnIndex("GRP")));
					}
					addNullOrEmptyCount++;
							
				}else{
					/** tên item group*/
					list.add((T) cursor.getString(cursor.getColumnIndex("GRP")));
				}
				cursor.moveToNext();
			} while (!cursor.isAfterLast());
		}
		
		adapter.close();
		cursor.close();
		
		return list;
	}
	/**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * getUserGroup 
     * @param <T> : chỉ định kiểu cần chuyển đổi
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲
     */
	public <T> ArrayList<T> getUserGroup(String xKeyGroup, String columnGet) {
		GetSearchItemSetting searchItem = new GetSearchItemSetting(context);
		GetSortSetting sort = new GetSortSetting(context);
		
		xWhereDefault = searchItem.getWhereSQL();
		xOrderDefault = sort.getSortSQL();
		adapter.open();
		Cursor cursor = adapter.getUserGroupBy(xKeyGroup,columnGet,xWhereDefault);
		ArrayList<T> list = new ArrayList<T>();
		if (cursor.getCount() >= 1) {
			cursor.moveToFirst();
			do {
				/** tên item goup*/
				list.add((T) cursor.getString(cursor.getColumnIndex("GRP")));
				cursor.moveToNext();
			} while (!cursor.isAfterLast());
		}
		
		adapter.close();
		cursor.close();
		
		return list;
	}
	/**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * getUserGroupFromView 
     * @param <T> : chỉ định kiểu cần chuyển đổi
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲
     */
	public <T> ArrayList<T> getUserGroupFromView(String xKeyGroup,String ViewName) {
		int addNullOrEmptyCount=0;
		GetSearchItemSetting searchItem = new GetSearchItemSetting(context);
		GetSortSetting sort = new GetSortSetting(context);
		
		xWhereDefault = searchItem.getWhereSQL();
		xOrderDefault = sort.getSortSQL();
		//open ket noi
		adapter.open();
		//get data tu database
		Cursor cursor = adapter.getUserGroupByFromViewWithWhere(xKeyGroup,ViewName,xWhereDefault);
		ArrayList<T> list = new ArrayList<T>();
		//loop qua ket qua tra ve 
		if (cursor.getCount() >= 1) {
			//di chuyen toi dong dau tien
			cursor.moveToFirst();
			do {
				if(cursor.getString(cursor.getColumnIndex("GRP"))==null 
						|| cursor.getString(cursor.getColumnIndex("GRP")).equals("")){ 
					if(addNullOrEmptyCount==0){
						/** tên item group*/
						list.add((T) cursor.getString(cursor.getColumnIndex("GRP")));
					}
					addNullOrEmptyCount++;
							
				}else{
					/** tên item group*/
					list.add((T) cursor.getString(cursor.getColumnIndex("GRP")));
				}
				
				cursor.moveToNext();
			} while (!cursor.isAfterLast());
		}
		
		adapter.close();
		cursor.close();
		
		return list;
	}
	
	/**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * getUserGroupFromViewChartItem 
     * @param <T> : chỉ định kiểu cần chuyển đổi
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲
     */
	public  ArrayList<ChartItem> getUserGroupFromViewChartItem(String xKeyGroup,String ViewName) {
		int addNullOrEmptyCount=0;
		GetSearchItemSetting searchItem = new GetSearchItemSetting(context);
		GetSortSetting sort = new GetSortSetting(context);
		
		xWhereDefault = searchItem.getWhereSQL();
		xOrderDefault = sort.getSortSQL();
		//open ket noi
		adapter.open();
		//get data tu database
		Cursor cursor = adapter.getUserGroupByFromViewWithWhere(xKeyGroup,ViewName,xWhereDefault);
		ArrayList<ChartItem> list = new ArrayList<ChartItem>();
		//loop qua ket qua tra ve 
		if (cursor.getCount() >= 1) {
			//di chuyen toi dong dau tien
			cursor.moveToFirst();
			ChartItem chartItem;
			do {
				if(cursor.getString(cursor.getColumnIndex("GRP"))==null 
						|| cursor.getString(cursor.getColumnIndex("GRP")).equals("")){ 
					if(addNullOrEmptyCount==0){
						/** tên item group*/
						chartItem = new ChartItem(cursor.getString(cursor.getColumnIndex("GRP")), cursor.getString(cursor.getColumnIndex("CNT"))); 
						list.add(chartItem);
					}
					addNullOrEmptyCount++;
							
				}else{
					/** tên item group*/
					chartItem = new ChartItem(cursor.getString(cursor.getColumnIndex("GRP")), cursor.getString(cursor.getColumnIndex("CNT"))); 
					list.add(chartItem);
				}
				
				cursor.moveToNext();
			} while (!cursor.isAfterLast());
		}
		
		adapter.close();
		cursor.close();
		
		return list;
	}
	/**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * getDevWorkingByTreeYearMonthChartItem 
     * @param <T> : chỉ định kiểu cần chuyển đổi
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲
     */
	public  ArrayList<ChartItem> getDevWorkingByTreeYearMonthChartItem(int year) {
		int addNullOrEmptyCount=0;
		GetSearchItemSetting searchItem = new GetSearchItemSetting(context);
		GetSortSetting sort = new GetSortSetting(context);
		
		xWhereDefault = searchItem.getWhereSQL();
		xOrderDefault = sort.getSortSQL();
		//open ket noi
		adapter.open();
		//get data tu database
		Cursor cursor = adapter.getDevWorkingByTreeYearMonth(year, xWhereDefault);
		ArrayList<ChartItem> list = new ArrayList<ChartItem>();
		//loop qua ket qua tra ve 
		if (cursor.getCount() >= 1) {
			//di chuyen toi dong dau tien
			cursor.moveToFirst();
			ChartItem chartItem;
			do {
				if(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YEARMONTH))==null 
						|| cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YEARMONTH)).equals("")){ 
					if(addNullOrEmptyCount==0){
						/** tên item group*/
						chartItem = new ChartItem(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YEARMONTH)), cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_CNT))); 
						list.add(chartItem);
					}
					addNullOrEmptyCount++;
							
				}else{
					/** tên item group*/
					chartItem = new ChartItem(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YEARMONTH)), cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_CNT))); 
					list.add(chartItem);
				}
				
				cursor.moveToNext();
			} while (!cursor.isAfterLast());
		}
		
		adapter.close();
		cursor.close();
		
		return list;
	}
	/** 2016.12.10
	 * Lấy số nhân viên thử việc trong các năm
	 * @param year
	 * @return
	 */
	public  ArrayList<ChartItem> getDevTrainingByThreeYearMonthChartItem(int year) {
		int addNullOrEmptyCount=0;
		GetSearchItemSetting searchItem = new GetSearchItemSetting(context);
		GetSortSetting sort = new GetSortSetting(context);
		
		xWhereDefault = searchItem.getWhereSQL();
		xOrderDefault = sort.getSortSQL();
		//open ket noi
		adapter.open();
		//get data tu database
		Cursor cursor = adapter.getDevTrainingByThreeYearMonth(year, xWhereDefault);
		ArrayList<ChartItem> list = new ArrayList<ChartItem>();
		//loop qua ket qua tra ve 
		if (cursor.getCount() >= 1) {
			//di chuyen toi dong dau tien
			cursor.moveToFirst();
			ChartItem chartItem;
			do {
				if(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YEARMONTH))==null 
						|| cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YEARMONTH)).equals("")){ 
					if(addNullOrEmptyCount==0){
						/** tên item group*/
						chartItem = new ChartItem(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YEARMONTH)), cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_CNT))); 
						list.add(chartItem);
					}
					addNullOrEmptyCount++;
							
				}else{
					/** tên item group*/
					chartItem = new ChartItem(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YEARMONTH)), cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_CNT))); 
					list.add(chartItem);
				}
				
				cursor.moveToNext();
			} while (!cursor.isAfterLast());
		}
		
		adapter.close();
		cursor.close();
		
		return list;
	}
	
	/** 2016.12.10
	 * Lấy số nhân viên nhận chính thức trong các năm
	 * @param year
	 * @return
	 */
	public  ArrayList<ChartItem> getDevContractByThreeYearMonthChartItem(int year) {
		int addNullOrEmptyCount=0;
		GetSearchItemSetting searchItem = new GetSearchItemSetting(context);
		GetSortSetting sort = new GetSortSetting(context);
		
		xWhereDefault = searchItem.getWhereSQL();
		xOrderDefault = sort.getSortSQL();
		//open ket noi
		adapter.open();
		//get data tu database
		Cursor cursor = adapter.getDevContractByThreeYearMonth(year, xWhereDefault);
		ArrayList<ChartItem> list = new ArrayList<ChartItem>();
		//loop qua ket qua tra ve 
		if (cursor.getCount() >= 1) {
			//di chuyen toi dong dau tien
			cursor.moveToFirst();
			ChartItem chartItem;
			do {
				if(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YEARMONTH))==null 
						|| cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YEARMONTH)).equals("")){ 
					if(addNullOrEmptyCount==0){
						/** tên item group*/
						chartItem = new ChartItem(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YEARMONTH)), cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_CNT))); 
						//lay so nhan vien da thu viec tu nam truoc nhung dau nam nay moi nhan chinh thuc
						
						list.add(chartItem);
					}
					addNullOrEmptyCount++;
							
				}else{
					/** tên item group*/
					chartItem = new ChartItem(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YEARMONTH)), cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_CNT))); 
					list.add(chartItem);
				}
				
				cursor.moveToNext();
			} while (!cursor.isAfterLast());
		}
		
		adapter.close();
		cursor.close();
		
		return list;
	}
	
	/**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * getUserGroupFromView 
     * @param <T> : chỉ định kiểu cần chuyển đổi
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲
     */
	public <T> ArrayList<T> getUserGroupFromView(String xKeyGroup, String columnGet,String ViewName) {
		GetSearchItemSetting searchItem = new GetSearchItemSetting(context);
		GetSortSetting sort = new GetSortSetting(context);
		
		xWhereDefault = searchItem.getWhereSQL();
		xOrderDefault = sort.getSortSQL();
		
		adapter.open();
		Cursor cursor = adapter.getUserGroupByFromView(xKeyGroup,columnGet,ViewName,xWhereDefault);
		ArrayList<T> list = new ArrayList<T>();
		if (cursor.getCount() >= 1) {
			cursor.moveToFirst();
			do {
				/** tên item goup*/
				list.add((T) cursor.getString(cursor.getColumnIndex("GRP")));
				cursor.moveToNext();
			} while (!cursor.isAfterLast());
		}
		
		adapter.close();
		cursor.close();
		
		return list;
	}
	
	/**
	 * Lay so nhan vien PD chinh thuc
	 * @param KeyGroup
	 * @param ViewName
	 * @return
	 */
	public  ArrayList<ChartItem> getPDWorkingFromViewChartItem(int year,String xWhere) {
		int addNullOrEmptyCount=0;
		GetSearchItemSetting searchItem = new GetSearchItemSetting(context);
		GetSortSetting sort = new GetSortSetting(context);
		
		xWhereDefault = searchItem.getWhereSQL();
		xOrderDefault = sort.getSortSQL();
		//open ket noi
		adapter.open();
		//get data tu database
		Cursor cursor = adapter.getPDWorkingByYearMonth(year, xWhereDefault);
		ArrayList<ChartItem> list = new ArrayList<ChartItem>();
		//loop qua ket qua tra ve 
		if (cursor.getCount() >= 1) {
			//di chuyen toi dong dau tien
			cursor.moveToFirst();
			ChartItem chartItem;
			do {
				if(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YEARMONTH))==null 
						|| cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YEARMONTH)).equals("")){ 
					if(addNullOrEmptyCount==0){
						/** tên item group*/
						chartItem = new ChartItem(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YEARMONTH)), cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_CNT))); 
						list.add(chartItem);
					}
					addNullOrEmptyCount++;
							
				}else{
					/** tên item group*/
					chartItem = new ChartItem(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YEARMONTH)), cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_CNT))); 
					list.add(chartItem);
				}
				
				cursor.moveToNext();
			} while (!cursor.isAfterLast());
		}
		
		adapter.close();
		cursor.close();
		
		return list;
	}
	
	/**
	 * Lay so nhan vien lap trinh chinh thuc
	 * @param KeyGroup
	 * @param ViewName
	 * @return
	 */
	public  ArrayList<ChartItem> getDevWorkingFromViewChartItem(int year,String xWhere) {
		int addNullOrEmptyCount=0;
		GetSearchItemSetting searchItem = new GetSearchItemSetting(context);
		GetSortSetting sort = new GetSortSetting(context);
		
		xWhereDefault = searchItem.getWhereSQL();
		xOrderDefault = sort.getSortSQL();
		//open ket noi
		adapter.open();
		//get data tu database
		Cursor cursor = adapter.getDevWorkingByYearMonth(year, xWhereDefault);
		ArrayList<ChartItem> list = new ArrayList<ChartItem>();
		//loop qua ket qua tra ve 
		if (cursor.getCount() >= 1) {
			//di chuyen toi dong dau tien
			cursor.moveToFirst();
			ChartItem chartItem;
			do {
				if(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YEARMONTH))==null 
						|| cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YEARMONTH)).equals("")){ 
					if(addNullOrEmptyCount==0){
						/** tên item group*/
						chartItem = new ChartItem(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YEARMONTH)), cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_CNT))); 
						list.add(chartItem);
					}
					addNullOrEmptyCount++;
							
				}else{
					/** tên item group*/
					chartItem = new ChartItem(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YEARMONTH)), cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_CNT))); 
					list.add(chartItem);
				}
				
				cursor.moveToNext();
			} while (!cursor.isAfterLast());
		}
		
		adapter.close();
		cursor.close();
		
		return list;
	}
	
	/**
	 * Lay so nhan vien lap trinh training
	 * @param KeyGroup
	 * @param ViewName
	 * @return
	 */
	public  ArrayList<ChartItem> getDevTrainingFromViewChartItem(int year) {
		int addNullOrEmptyCount=0;
		GetSearchItemSetting searchItem = new GetSearchItemSetting(context);
		GetSortSetting sort = new GetSortSetting(context);
		
		xWhereDefault = searchItem.getWhereSQL();
		xOrderDefault = sort.getSortSQL();
		//open ket noi
		adapter.open();
		//get data tu database
		Cursor cursor = adapter.getDevTrainingByYearMonth(year,xWhereDefault);
		ArrayList<ChartItem> list = new ArrayList<ChartItem>();
		//loop qua ket qua tra ve 
		if (cursor.getCount() >= 1) {
			//di chuyen toi dong dau tien
			cursor.moveToFirst();
			ChartItem chartItem;
			do {
				if(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YEARMONTH))==null 
						|| cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YEARMONTH)).equals("")){ 
					if(addNullOrEmptyCount==0){
						/** tên item group*/
						chartItem = new ChartItem(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YEARMONTH)), cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_CNT))); 
						list.add(chartItem);
					}
					addNullOrEmptyCount++;
							
				}else{
					/** tên item group*/
					chartItem = new ChartItem(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YEARMONTH)), cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_CNT))); 
					list.add(chartItem);
				}
				
				cursor.moveToNext();
			} while (!cursor.isAfterLast());
		}
		
		adapter.close();
		cursor.close();
		
		return list;
	}
	
	/**
	 * Lay so nhan vien Phien Dich training
	 * @param KeyGroup
	 * @param ViewName
	 * @return
	 */
	public  ArrayList<ChartItem> getPDTrainingFromViewChartItem(int year ) {
		int addNullOrEmptyCount=0;
		GetSearchItemSetting searchItem = new GetSearchItemSetting(context);
		GetSortSetting sort = new GetSortSetting(context);
		
		xWhereDefault = searchItem.getWhereSQL();
		xOrderDefault = sort.getSortSQL();
		//open ket noi
		adapter.open();
		//get data tu database
		Cursor cursor = adapter.getPDTrainingByYearMonth(year,xWhereDefault);
		ArrayList<ChartItem> list = new ArrayList<ChartItem>();
		//loop qua ket qua tra ve 
		if (cursor.getCount() >= 1) {
			//di chuyen toi dong dau tien
			cursor.moveToFirst();
			ChartItem chartItem;
			do {
				if(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YEARMONTH))==null 
						|| cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YEARMONTH)).equals("")){ 
					if(addNullOrEmptyCount==0){
						/** tên item group*/
						chartItem = new ChartItem(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YEARMONTH)), cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_CNT))); 
						list.add(chartItem);
					}
					addNullOrEmptyCount++;
							
				}else{
					/** tên item group*/
					chartItem = new ChartItem(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YEARMONTH)), cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_CNT))); 
					list.add(chartItem);
				}
				
				cursor.moveToNext();
			} while (!cursor.isAfterLast());
		}
		
		adapter.close();
		cursor.close();
		
		return list;
	}
	
	/**
	 * Lay so nhan vien lap trinh nghi viec theo tung thang nam
	 * @param KeyGroup
	 * @param ViewName
	 * @return
	 */
	public  ArrayList<ChartItem> getDevYasumiFromViewChartItem() {
		int addNullOrEmptyCount=0;
		GetSearchItemSetting searchItem = new GetSearchItemSetting(context);
		GetSortSetting sort = new GetSortSetting(context);
		
		xWhereDefault = searchItem.getWhereSQL();
		xOrderDefault = sort.getSortSQL();
		//open ket noi
		adapter.open();
		//get data tu database
		Cursor cursor = adapter.getDevYasumiByYearMonth(xWhereDefault);
		ArrayList<ChartItem> list = new ArrayList<ChartItem>();
		//loop qua ket qua tra ve 
		if (cursor.getCount() >= 1) {
			//di chuyen toi dong dau tien
			cursor.moveToFirst();
			ChartItem chartItem;
			do {
				if(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YEARMONTH))==null 
						|| cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YEARMONTH)).equals("")){ 
					if(addNullOrEmptyCount==0){
						/** tên item group*/
						chartItem = new ChartItem(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YEARMONTH)), cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_CNT))); 
						list.add(chartItem);
					}
					addNullOrEmptyCount++;
							
				}else{
					/** tên item group*/
					chartItem = new ChartItem(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YEARMONTH)), cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_CNT))); 
					list.add(chartItem);
				}
				
				cursor.moveToNext();
			} while (!cursor.isAfterLast());
		}
		
		adapter.close();
		cursor.close();
		
		return list;
	}
	
	/**
	 * Lay so nhan vien phien dich nghi viec theo tung tham nam
	 * @param KeyGroup
	 * @param ViewName
	 * @return
	 */
	public  ArrayList<ChartItem> getPDYasumiFromViewChartItem() {
		int addNullOrEmptyCount=0;
		GetSearchItemSetting searchItem = new GetSearchItemSetting(context);
		GetSortSetting sort = new GetSortSetting(context);
		
		xWhereDefault = searchItem.getWhereSQL();
		xOrderDefault = sort.getSortSQL();
		//open ket noi
		adapter.open();
		//get data tu database
		Cursor cursor = adapter.getPDYasumiByYearMonth(xWhereDefault);
		ArrayList<ChartItem> list = new ArrayList<ChartItem>();
		//loop qua ket qua tra ve 
		if (cursor.getCount() >= 1) {
			//di chuyen toi dong dau tien
			cursor.moveToFirst();
			ChartItem chartItem;
			do {
				if(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YEARMONTH))==null 
						|| cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YEARMONTH)).equals("")){ 
					if(addNullOrEmptyCount==0){
						/** tên item group*/
						chartItem = new ChartItem(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YEARMONTH)), cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_CNT))); 
						list.add(chartItem);
					}
					addNullOrEmptyCount++;
							
				}else{
					/** tên item group*/
					chartItem = new ChartItem(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_YEARMONTH)), cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_CNT))); 
					list.add(chartItem);
				}
				
				cursor.moveToNext();
			} while (!cursor.isAfterLast());
		}
		
		adapter.close();
		cursor.close();
		
		return list;
	}
}