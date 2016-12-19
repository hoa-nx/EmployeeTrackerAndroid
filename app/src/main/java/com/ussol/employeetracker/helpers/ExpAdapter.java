package com.ussol.employeetracker.helpers;

import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.ussol.employeetracker.R;
import com.ussol.employeetracker.helpers.UserDialogAdapter.ViewHolder;
import com.ussol.employeetracker.models.MasterConstants;
import com.ussol.employeetracker.models.User;
import com.ussol.employeetracker.utils.DateTimeUtil;
import com.ussol.employeetracker.utils.Utils;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

/**
 * This is adapter for expandable list-view for constructing the group and child elements.
 */
public class ExpAdapter extends BaseExpandableListAdapter {
	private Activity activity;
	private LayoutInflater inflater;
    private ArrayList<ExpParent> mParent;
    private ArrayList<ExpParent> mOriginalList;
    private ArrayList<User> mOriginalUserList;
    private Context ctx;
    private TextView txtEntry_count;
    
    static class ViewHolder {
  	  /** tên nhân viên */
  	  TextView txtUserFullName;
  	  /** bao gồm phòng ban - team - chức vụ ) */
  	  TextView txtUserInfo;
  	  /** code nhân viên */
  	  TextView txtListUserCode;
  	  /** image nhân viên */
  	  ImageView imgUserImage;
  	  /** path chứa hình ảnh */
  	  TextView txtImgFullPath;
  	  /** trình độ nhật ngữ */
  	  TextView txtJapanseLevel;  
  	  /** check box */
  	  CheckBox chkListUser;
  	  /** ngày vào công ty */
  	  TextView txtIn_date_count;
  	  /** ngày vào Ussol */
  	  TextView txtJoin_date_count;
  	  /** nang suat lap trinh */
  	  TextView txtProgram_count;
  	  /** nang suat thiet ke chi tiet */
  	  TextView txtDetailDesign_count;
  	  /** so dien thoai */
  	  TextView txtMobile;
  	  /** email */
  	  TextView txtEmail;
  	  /** Danh gia chung */
  	  RatingBar rtbKeiken;
	  /** image nhân viên da nghi viec*/
	  ImageView imgUserImageYasumi;
	  /** Ngay nghi viec*/
	  TextView txtYasumiDate;
      /** các thông tin khác */
      TextView txtUserOtherInformation;
  	 }
    
    public ExpAdapter(Context context, ArrayList<ExpParent> parent){
    	ctx = context;
        mParent = parent;
        this.mOriginalList = new ArrayList<ExpParent>();
        this.mOriginalList.addAll(parent);
        inflater = LayoutInflater.from(context);
    }
 
 
    @Override
    //counts the number of group/parent items so the list knows how many times calls getGroupView() method
    public int getGroupCount() {
        return mParent.size();
    }
 
    @Override
    //counts the number of children items so the list knows how many times calls getChildView() method
    public int getChildrenCount(int i) {
    	
        return mParent.get(i).getArrayChildren().size();
    }
    /** get toan bo so record */
    public int getAllChildCount(){
    	int cnt=0;
    	for (int i =0 ; i< getGroupCount();i++){
    		cnt += getChildrenCount(i);
    	}
    	return cnt;
    }
    @Override
    //gets the title of each parent/group
    public Object getGroup(int i) {
        return mParent.get(i).getTitle();
    }
 
    @Override
    //gets the name of each item
    public Object getChild(int i, int i1) {
        return mParent.get(i).getArrayChildren().get(i1);
    }
 
    public void removeChild(int i, int i1){
    	mParent.get(i).removeChildrenUser(i1);
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
    		/** refresh lại list */
    		notifyDataSetChanged();
    		return;
    	}else{
    		for(ExpParent par : mOriginalList){
    			ArrayList<User> userList = par.getArrayChildren();
    			ArrayList<User> userNewList = new ArrayList<User>();
    			for(User  usr : userList){
    				if(String.valueOf(usr.code).toLowerCase().contains(query)
    					|| usr.full_name.toLowerCase().contains(query)
    					|| usr.email.toLowerCase().contains(query)
    					|| usr.mobile.toLowerCase().contains(query)
    					|| usr.dept_name.toLowerCase().contains(query)
    					|| usr.team_name.toLowerCase().contains(query)
    					|| usr.position_name.toLowerCase().contains(query)
    				  ){
    					/** nếu thỏa mãn điều kiện thì sẽ add vào list */
    					userNewList.add(usr);
    				}
    			}
    			if(userNewList.size() > 0){
    				ExpParent nExpParent = new ExpParent(par.getTitle(), userNewList);
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
        //Tinh % cua từng loai 
        
        
        float totalCount =getAllChildCount()*1.0f;
        float percent =0 ;
        if (totalCount==0) {
        	percent=0;
        }else{
        	//int percent = (int)((n * 100.0f) / v);
        	percent=(int)Utils.Round((getChildrenCount(i)/ totalCount)*100.0f,0,RoundingMode.HALF_UP);
        }
                
        //"i" is the position of the parent/group in the list
        if(getGroup(i).equals("") || getGroup(i)==null){
        	
        	textView.setText(ctx.getString(R.string.titleNoDefine) + "(" + String.valueOf(percent) + "%)");//không chỉ định
        	
        }else{
        	textView.setText(getGroup(i).toString() + "(" + String.valueOf(percent) + "%)");
        }
        txtEntry_count.setText(String.valueOf( getChildrenCount(i)));
        //return the entire view
        return view;
    }
 
    @Override
    //in this method you must set the text to see the children on the list
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
    	View vi=view;
    	ViewHolder holder;
		ImageView iv = null;
		
        if (view == null) {
            view = inflater.inflate(R.layout.expandable_list_view_user_child_row, viewGroup,false);
            holder = new ViewHolder();
            holder.txtListUserCode = (TextView) view.findViewById(R.id.txtListUserCode);
            holder.txtUserFullName = (TextView) view.findViewById(R.id.txtListUserFullName);
            holder.txtUserInfo = (TextView) view.findViewById(R.id.txtListUserInfor);
            holder.txtImgFullPath= (TextView) view.findViewById(R.id.txtUserImgFullPath);
            holder.imgUserImage = (ImageView) view.findViewById(R.id.imgListUser);
            holder.txtJapanseLevel = (TextView) view.findViewById(R.id.txtListUserJapaneseLevel);
            holder.rtbKeiken = (RatingBar)view.findViewById(R.id.txtUserKeikenRate);
            holder.chkListUser =(CheckBox)view.findViewById(R.id.chkListUserSelect);
            holder.txtIn_date_count =(TextView)view.findViewById(R.id.txtIn_date_count);
            holder.txtJoin_date_count =(TextView)view.findViewById(R.id.txtJoin_date_count);
            holder.txtProgram_count =(TextView)view.findViewById(R.id.txtProgram_count);
            holder.txtDetailDesign_count =(TextView)view.findViewById(R.id.txtDetailDesign_count);
            holder.txtMobile =(TextView)view.findViewById(R.id.txtMobile);
            holder.txtEmail=(TextView)view.findViewById(R.id.txtEmail);
            
			holder.txtYasumiDate=(TextView)view.findViewById(R.id.txtListUserYasumiDate);
			
			holder.imgUserImageYasumi=(ImageView)view.findViewById(R.id.imgListUserYasumi);
            holder.txtUserOtherInformation=(TextView)view.findViewById(R.id.txtOtherInfomation);

            iv = (ImageView) view.findViewById(R.id.imgListUser);
            view.setTag(holder);
        } else {
    		iv = (ImageView) view.findViewById(R.id.imgListUser);
			holder = (ViewHolder) view.getTag();
			/** load hình ảnh nhân viên */
			DecodeTask task =(DecodeTask)iv.getTag(R.id.imgListUser);
			if (task !=null){
				task.cancel(true);
			}
		}
        holder.chkListUser.setVisibility(View.INVISIBLE);
        User cat =(User) mParent.get(i).getArrayChildren().get(i1);
        /** hien thi thong tin */
        holder.txtListUserCode.setText(String.valueOf(cat.code));
        if (cat.full_name!=null){
        	holder.txtUserFullName.setText(cat.full_name);
        }else{
        	holder.txtUserFullName.setText("");
        }

		/** phong ban */
        String txtUserInfo="";
        if (cat.dept_name!=null){
        	txtUserInfo = txtUserInfo + cat.dept_name;
        }
        /** nhóm */
        if (cat.team_name!=null){
        	txtUserInfo = txtUserInfo + " - " +  cat.team_name;
        }
        /** chức vụ */
        if (cat.position_name!=null){
        	txtUserInfo = txtUserInfo + " - " + cat.position_name;
        }
        /** setting thông tin phòng ban ....*/
        holder.txtUserInfo.setText(txtUserInfo);
        
        /** image path */
        if (cat.img_fullpath!=null){
        	holder.txtImgFullPath.setText(cat.img_fullpath);
        }else{
        	holder.txtImgFullPath.setText("");
        }
        
        /** trình độ nhật ngữ */
        if (cat.japanese!=null){
        	holder.txtJapanseLevel.setText(cat.japanese.toString());
        }
        else{
        	holder.txtJapanseLevel.setText("");
        }
        /** phụ cấp nghiệp vụ*/
        /*if (cat.allowance_business!=null){
        	*//** tro cap nghiep vu*//*
            holder.txtListUserAllowance_Business.setText( String.valueOf(cat.allowance_business));
        }
        else{
        	holder.txtListUserAllowance_Business.setText("");
        }*/
        
        /** lấy ngày sinh*/
        Date datebirthDay=null;
        if (cat.birthday!=null && cat.birthday!=""){
        	if (DateTimeUtil.isDate(cat.birthday ,MasterConstants.DATE_VN_FORMAT)){
        		datebirthDay = DateTimeUtil.convertStringToDate( cat.birthday ,MasterConstants.DATE_VN_FORMAT);
        	}
        }
        /** lấy ngày vào công ty*/
        Date datefrom=null;
        if (cat.in_date!=null && cat.in_date!=""){
        	if (DateTimeUtil.isDate(cat.in_date ,MasterConstants.DATE_VN_FORMAT)){
        		datefrom = DateTimeUtil.convertStringToDate( cat.in_date ,MasterConstants.DATE_VN_FORMAT);
        	}
        	        	
        }
        /** lấy ngày vào nhóm labour */
        Date datefromUssol=null;
        if (cat.join_date!=null && cat.join_date!="" ){
        	if (DateTimeUtil.isDate(cat.join_date ,MasterConstants.DATE_VN_FORMAT)){
        		datefromUssol = DateTimeUtil.convertStringToDate( cat.join_date ,MasterConstants.DATE_VN_FORMAT);
        	}
        	
        }
        /** lấy ngày hiện tại */
        Date dateto =DateTimeUtil.getCurrentDate(MasterConstants.DATE_VN_FORMAT);
        int keikenMonth =0;
        int keikenMonthInUssol =0;
        double ageStaff =0;
        
        /** tính ra tuoi cua nhan vien */
        if (datebirthDay==null){
        	ageStaff=0;
        }else{
        	ageStaff =DateTimeUtil.getFullMonthDiff(datebirthDay, dateto);
        	ageStaff = Utils.Round((ageStaff/12.0), 0, RoundingMode.HALF_DOWN);
        }
        
        /** tính ra thâm niên từ lúc vào công ty chính thức */
        if (datefrom==null){
        }else{
        	keikenMonth =DateTimeUtil.getFullMonthDiff(datefrom, dateto);
        }
        
        /** hien thi len man hinh*/
        holder.txtIn_date_count.setText(String.valueOf(keikenMonth));
        holder.rtbKeiken.setRating((float) (keikenMonth/12.0));
        
        /** tính ra thâm niên từ lúc vào USSOL chính thức */
        if (datefromUssol==null){
        }else{
        	/** nếu như đã ra khỏi nhóm labour thì sẽ tính ngày kết thúc là ngày đó chứ không phải ngày
        	 * hiện tại
        	 */
        	if (DateTimeUtil.isDate(cat.labour_out_date,MasterConstants.DATE_VN_FORMAT)){
        		dateto = DateTimeUtil.convertStringToDate( cat.labour_out_date,MasterConstants.DATE_VN_FORMAT);
        	}
        	keikenMonthInUssol =DateTimeUtil.getFullMonthDiff(datefromUssol, dateto);
        }
        /** hien thi so nam kinh nghiem USSOL*/
        holder.txtJoin_date_count.setText( String.valueOf(keikenMonthInUssol));
        
        /** hien thi len man hinh thong tin nang suat lap trinh*/
        holder.txtProgram_count.setText( String.valueOf(cat.program));
        
        /** hien thi len man hinh thong tin nang suat thiet ke chi tiet*/
        holder.txtDetailDesign_count.setText( String.valueOf(cat.detaildesign));
        //chuyen thang hien thi tuoi cua nhan vien
        holder.txtDetailDesign_count.setText(String.valueOf(ageStaff));
        
        /** so dien thoai*/
        holder.txtMobile.setText(String.valueOf(cat.mobile));    
        /** email*/
        holder.txtEmail.setText(String.valueOf(cat.email));
        
        String txtKeiken =keikenMonth + " tháng【Labour : "+ keikenMonthInUssol +" tháng】";
        
		/** hiển thị hình ảnh */
    	if (cat.img_fullpath==null || cat.img_fullpath.equals("")){
    		/** nếu không có hình thì hiển thị hình mặc định*/
    		iv.setImageResource(R.drawable.user);
    	}else{
    		iv.setImageBitmap(null);
	        DecodeTask task = new DecodeTask(iv);
	        task.execute(cat.img_fullpath);
	        /** set tag */
	        iv.setTag(R.id.imgListUser, task);
    	}
    	/** ngày nghỉ việc*/
		holder.txtYasumiDate.setText("");
		if (cat.out_date!=null && !cat.out_date.isEmpty()){
			Date dateYasumi=null;
			Date training_date=null;
			if (DateTimeUtil.isDate(cat.out_date ,MasterConstants.DATE_VN_FORMAT)){
				dateYasumi = DateTimeUtil.convertStringToDate( cat.out_date ,MasterConstants.DATE_VN_FORMAT);
			}
			if (cat.training_date!=null && !cat.training_date.isEmpty()){
				if (DateTimeUtil.isDate(cat.training_date ,MasterConstants.DATE_VN_FORMAT)){
					training_date = DateTimeUtil.convertStringToDate( cat.training_date ,MasterConstants.DATE_VN_FORMAT);
				}
			}
			/** tính ra thâm niên từ lúc vào công ty chính thức cho toi khi nghi viec*/
			Integer yasumiMonth=0;
			if (dateYasumi==null){
	        }else{
	        	if(datefrom!=null){
	        		yasumiMonth =DateTimeUtil.getFullMonthDiff(datefrom, dateYasumi);
	        	}else if (training_date !=null){
	        		yasumiMonth =DateTimeUtil.getFullMonthDiff(training_date, dateYasumi);
	        	}else{
	        		yasumiMonth=0;
	        	}
	        	
	        }
			holder.imgUserImageYasumi.setVisibility(View.VISIBLE);
			holder.txtYasumiDate.setText(String.valueOf(cat.out_date) + "(" + String.valueOf(yasumiMonth) +"Th)");
	        
		}else {
			holder.imgUserImageYasumi.setVisibility(View.INVISIBLE);
		}
        /** hien thi them thong tin ngach bac se duoc xem xet tuong ung voi tham nien*/
        holder.txtUserOtherInformation.setText("");
        if (cat.NEW_POSITION_RYAKU!=null && !cat.NEW_POSITION_RYAKU.isEmpty()){
            holder.txtUserOtherInformation.setText("↑"+cat.NEW_POSITION_RYAKU.toString());
            iv.setAlpha(0.6f);
        }else{
            iv.setAlpha(1.0f);
        }
        /*TextView textView = (TextView) view.findViewById(R.id.tvPlayerName);
        //"i" is the position of the parent/group in the list and 
        //"i1" is the position of the child
        textView.setText(((User) mParent.get(i).getArrayChildren().get(i1)).full_name);*/
 
        //return the entire view
        return view;
    }
 
    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }
}