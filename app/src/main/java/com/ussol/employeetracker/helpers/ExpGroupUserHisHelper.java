package com.ussol.employeetracker.helpers;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.widget.Switch;

import com.ussol.employeetracker.models.IExpGroup;
import com.ussol.employeetracker.models.MasterConstants;
import com.ussol.employeetracker.models.User;
import com.ussol.employeetracker.models.UserHistory;

public class ExpGroupUserHisHelper implements IExpGroup<UserHistory> {
	private String mTitle;
    private Context ctx ;
    private DatabaseAdapter mDatabaseAdapter; 
    /** chuyển đổi Cursor thành string array */
	ConvertCursorToArrayString mConvertCursorToArrayString;
	ConvertCursorToListString mConvertCursorToListString;
	
    public ExpGroupUserHisHelper(Context context ){
    	ctx = context;
    	mDatabaseAdapter = new DatabaseAdapter(ctx);
    	mConvertCursorToArrayString = new ConvertCursorToArrayString(ctx);
    	mConvertCursorToListString = new ConvertCursorToListString(ctx);
    }

	@Override
	public String[] getGroup(int group) {
    	ArrayList<String> list;
    	String[] returnArr=null ;
    	try{
    		
			switch (group){
				case IExpGroup.EXP_GROUP_HISTORY:
					/** hien thi thong tin lich su cac nhan vien*/
					list= new ArrayList<String>();
					list.add(MasterConstants.HIS_DEPT_NAME);
					list.add(MasterConstants.HIS_TEAM_NAME);
					list.add(MasterConstants.HIS_POSITION_NAME);
					list.add(MasterConstants.HIS_JAPANESE_NAME);
					list.add(MasterConstants.HIS_ALLOWANCE_BUSINESS_NAME);
					list.add(MasterConstants.HIS_SALARY_NAME);
					list.add(MasterConstants.HIS_ALLOWANCE_BSE_NAME);
					returnArr = new String[list.size()];
					returnArr = list.toArray(returnArr);
					break;
			}
    	}catch (Exception e){
    		mDatabaseAdapter.close();
    	}

    	return returnArr;
	}
	
	@Override
	public List<UserHistory> getChildUserHisGroup(int group ,String groupValue) {
		String xWhere="";
		String xOrderBy="";
		List<UserHistory>  list=null;
		try{
    		mDatabaseAdapter.open();
    		xWhere = " AND " + DatabaseAdapter.KEY_USER_CODE + " = " + groupValue;
    		xOrderBy = DatabaseAdapter.KEY_DATE_FROM + " ASC ";
			switch (group){
				case MasterConstants.MASTER_MKBN_DEPT_HIS:
					list = mConvertCursorToListString.getUserHisList(MasterConstants.MASTER_MKBN_DEPT_HIS, xWhere,xOrderBy);
					break;
				case  MasterConstants.MASTER_MKBN_TEAM_HIS:
					list = mConvertCursorToListString.getUserHisList(MasterConstants.MASTER_MKBN_TEAM_HIS, xWhere,xOrderBy);
					break;
				case  MasterConstants.MASTER_MKBN_POSITION_HIS:
					list = mConvertCursorToListString.getUserHisList(MasterConstants.MASTER_MKBN_POSITION_HIS, xWhere,xOrderBy);
					break;
				case  MasterConstants.MASTER_MKBN_JAPANESE_HIS:
					list = mConvertCursorToListString.getUserHisList(MasterConstants.MASTER_MKBN_JAPANESE_HIS, xWhere,xOrderBy);
					break;
				case  MasterConstants.MASTER_MKBN_ALLOWANCE_BUSINESS_HIS:
					list = mConvertCursorToListString.getUserHisList(MasterConstants.MASTER_MKBN_ALLOWANCE_BUSINESS_HIS, xWhere,xOrderBy);
					break;
				case  MasterConstants.MASTER_MKBN_ALLOWANCE_BSE_HIS:
					list = mConvertCursorToListString.getUserHisList(MasterConstants.MASTER_MKBN_ALLOWANCE_BSE_HIS, xWhere,xOrderBy);
					break;
				case  MasterConstants.MASTER_MKBN_SALARY_HIS:
					list = mConvertCursorToListString.getUserHisList(MasterConstants.MASTER_MKBN_SALARY_HIS, xWhere,xOrderBy);
					break;
			}
	
		mDatabaseAdapter.close();
		
    	}catch (Exception e){
    		mDatabaseAdapter.close();
    	}
				
		return list;
	}

	@Override
	public List<UserHistory> getChildGroup(int group) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<UserHistory> getChildGroup(int group, String groupValue) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<?> getGroupArrayList(int group) {
		// TODO Auto-generated method stub
		return null;
	}
}
