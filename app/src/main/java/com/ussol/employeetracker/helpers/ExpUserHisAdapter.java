package com.ussol.employeetracker.helpers;

import java.util.ArrayList;

import com.ussol.employeetracker.R;
import com.ussol.employeetracker.models.MasterConstants;
import com.ussol.employeetracker.models.User;
import com.ussol.employeetracker.models.UserHistory;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * This is adapter for expandable list-view for constructing the group and child elements.
 */
public class ExpUserHisAdapter extends BaseExpandableListAdapter {

	private LayoutInflater inflater;
    private ArrayList<ExpParent> mParent;
    private ArrayList<ExpParent> mOriginalList;
    private Context ctx;
    private TextView txtEntry_count;
    static class ViewHolder {
    	/** Code nhan vien  */
	  	TextView txtUserCode;
	  	
        TextView nameTxVw;
        /** image nhân viên */
	  	ImageView imgUserImage;
	  	/** path chứa hình ảnh */
	  	TextView txtImgFullPath;
	  	/** bao gồm phòng ban - team - chức vụ ) */
	  	TextView txtUserInfo;
	    /** trình độ nhật ngữ */
		TextView txtJapanseLevel;  
		/** ngày vào công ty */
		TextView txtIn_date_count;
		/** ngày vào Ussol */
		TextView txtJoin_date_count;
		/** nang suat lap trinh */
		TextView txtProgram_count;
		/** nang suat thiet ke chi tiet */
		TextView txtDetailDesign_count;
		/** tro cap nghiep vu*/
		TextView txtListUserAllowance_Business;
		/** code cua dong doi tuong*/
		TextView txtCodeRecord;
    }
    
    public ExpUserHisAdapter(Context context, ArrayList<ExpParent> parent){
    	ctx = context;
        mParent = parent;
        inflater = LayoutInflater.from(context);
        this.mOriginalList = new ArrayList<ExpParent>();
        this.mOriginalList.addAll(parent);
    }
 
 
    @Override
    //counts the number of group/parent items so the list knows how many times calls getGroupView() method
    public int getGroupCount() {
        return mParent.size();
    }
 
    @Override
    //counts the number of children items so the list knows how many times calls getChildView() method
    public int getChildrenCount(int i) {
    	
        return mParent.get(i).getArrayChildrenUserHis().size();
    }
 
    @Override
    //gets the title of each parent/group
    public Object getGroup(int i) {
        return mParent.get(i).getTitle();
    }
 
    @Override
    //gets the name of each item
    public Object getChild(int i, int i1) {
        return mParent.get(i).getArrayChildrenUserHis().get(i1);
    }
 
    public void removeChild(int i, int i1){
    	mParent.get(i).removeChildrenUserHis(i1);
    }
    
    @Override
    public long getGroupId(int i) {
        return i;
    }
 
    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }
 
    @Override
    public boolean hasStableIds() {
        return true;
    }
    
    /** xử lý filter data của list*/
    public void filterData(String query){
    	/** vì không xem xét chữ thường hay chữ Hoa nên cho là chữ thường */
    	query = query.toLowerCase();
    	/** xóa data */
		mParent.clear();
    	if(query.isEmpty()|| query==null || query.equals("")){
    		/** trường hợp mà không có nhập filter thì gán toàn bộ data ban đầu */
    		mParent.addAll(mOriginalList);
    		return;
    	}else{
    		
    		for(ExpParent par : mOriginalList){
    			ArrayList<UserHistory> userList = par.getArrayChildrenUserHis();
    			ArrayList<UserHistory> userNewList = new ArrayList<UserHistory>();
    			for(UserHistory  usr : userList){
    				if(String.valueOf(usr.code).toLowerCase().contains(query)
    					|| usr.full_name.toLowerCase().contains(query)
    					|| usr.new_dept_name.toLowerCase().contains(query)
    					|| usr.new_team_name.toLowerCase().contains(query)
    					|| usr.new_position_name.toLowerCase().contains(query)
    					|| usr.new_japanese.toLowerCase().contains(query)
    					|| usr.new_allowance_business.toLowerCase().contains(query)
						|| usr.new_allowance_bse.toLowerCase().contains(query)
    					|| String.valueOf(usr.yobi_real1).toLowerCase().contains(query)
    				  ){
    					/** nếu thỏa mãn điều kiện thì sẽ add vào list */
    					userNewList.add(usr);
    				}
    			}
    			if(userNewList.size() > 0){
    				ExpParent nExpParent = new ExpParent(par.getTitle(), userNewList,"");
    				mParent.add(nExpParent);
    			}
    		}
    		/** refresh lại list */
    		notifyDataSetChanged();
    	}
    }
    @Override
    //in this method you must set the text to see the parent/group on the list
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
         if (view == null) {
            view = inflater.inflate(R.layout.expandable_list_view_user_group_row, viewGroup,false);
            
        }
         TextView textView = (TextView) view.findViewById(R.id.tvGroupName);
        txtEntry_count = (TextView) view.findViewById(R.id.txtEntry_count);
        
        //"i" is the position of the parent/group in the list
        if(getGroup(i).equals("") || getGroup(i)==null){
        	textView.setText(R.string.titleNoDefine);
        }else{
        	textView.setText(getGroup(i).toString());
        }
        txtEntry_count.setText(String.valueOf( getChildrenCount(i)));
        //return the entire view
        return view;
    }
 
    @Override
    //in this method you must set the text to see the children on the list
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
    	ViewHolder holder;
		ImageView iv = null;
		
        if (view == null) {
            view = inflater.inflate(R.layout.expandable_list_view_user_his_child_row, viewGroup,false);
            holder = new ViewHolder();
			holder.nameTxVw =(TextView) view.findViewById(R.id.txtListUserFullName);
			holder.txtImgFullPath= (TextView) view.findViewById(R.id.txtUserImgFullPath);
			holder.txtUserInfo = (TextView) view.findViewById(R.id.txtListUserInfor);
			holder.txtCodeRecord =(TextView) view.findViewById(R.id.txtCodeRecord);
			holder.txtUserCode=(TextView) view.findViewById(R.id.txtListUserCode);
			/*holder.txtJapanseLevel = (TextView) view.findViewById(R.id.txtListUserJapaneseLevel);
            holder.txtIn_date_count =(TextView)view.findViewById(R.id.txtIn_date_count);
            holder.txtJoin_date_count =(TextView)view.findViewById(R.id.txtJoin_date_count);
            holder.txtProgram_count =(TextView)view.findViewById(R.id.txtProgram_count);
            holder.txtDetailDesign_count =(TextView)view.findViewById(R.id.txtDetailDesign_count);
            holder.txtListUserAllowance_Business =(TextView)view.findViewById(R.id.txtListUserAllowance_Business);*/
            
			iv = (ImageView) view.findViewById(R.id.imgListUser);
			
			view.setTag( holder );
        } else {
			 /**view already defined, retrieve view holder */
			 iv = (ImageView) view.findViewById(R.id.imgListUser);
			 
			 holder = (ViewHolder) view.getTag();
		}

        UserHistory cat =(UserHistory) mParent.get(i).getArrayChildrenUserHis().get(i1);
        /** hien thi thong tin */
     	
     	String txtUserInfo="";
		switch((i*10)+MasterConstants.MASTER_MKBN_DEPT_HIS){
	    	case MasterConstants.MASTER_MKBN_DEPT_HIS:
	    		holder.nameTxVw.setText( cat.new_dept_name );		
	            txtUserInfo =  cat.date_from + " ～ " + cat.date_to ;
	    		break;
	    	case MasterConstants.MASTER_MKBN_TEAM_HIS:
	    		holder.nameTxVw.setText( cat.new_team_name);
	    		txtUserInfo =  cat.date_from + " ～ " + cat.date_to ;
	        	break;
	    	case MasterConstants.MASTER_MKBN_POSITION_HIS:
	    		holder.nameTxVw.setText( cat.new_position_name );
	    		txtUserInfo =  cat.date_from + " ～ " + cat.date_to ;
	        	break;
	    	case MasterConstants.MASTER_MKBN_JAPANESE_HIS:
	    		holder.nameTxVw.setText( cat.new_japanese );
	    		txtUserInfo =  cat.date_from + " ～ " + cat.date_to ;
	        	break;
	    	case MasterConstants.MASTER_MKBN_ALLOWANCE_BUSINESS_HIS:
	    		holder.nameTxVw.setText( cat.new_allowance_business);
	    		txtUserInfo =  cat.date_from + " ～ " + cat.date_to ;
	        	break;
			case MasterConstants.MASTER_MKBN_ALLOWANCE_BSE_HIS:
				holder.nameTxVw.setText( cat.new_allowance_bse);
				txtUserInfo =  cat.date_from + " ～ " + cat.date_to ;
				break;
	    	case MasterConstants.MASTER_MKBN_SALARY_HIS:
	    		holder.nameTxVw.setText(String.valueOf( cat.yobi_real1));
	    		txtUserInfo =  cat.date_from + " ～ " + cat.date_to ;
	        	break;
	    }
		
        /** setting thông tin phòng ban ....*/
        holder.txtUserInfo.setText(txtUserInfo);
        
        //return the entire view
        return view;
    }
 
    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }
}