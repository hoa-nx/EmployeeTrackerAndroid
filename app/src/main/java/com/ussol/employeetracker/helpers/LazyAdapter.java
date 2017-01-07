/**
 * Copyright (c) 2012 Fujinet Software Department 5 ( USSOL team)  
 * See the file license.txt for copying permission.
*/  

package com.ussol.employeetracker.helpers;

import java.io.File;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.ussol.employeetracker.models.MasterConstants;
import com.ussol.employeetracker.models.User;
import com.ussol.employeetracker.utils.DateTimeUtil;
import com.ussol.employeetracker.utils.Utils;
import com.ussol.employeetracker.R;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
/**
 * @author Hoa-NX
 * Dùng để load data cho list nhân viên 
 */
public class LazyAdapter extends BaseAdapter {
    private Activity activity;
    /** danh sách nhân viên */
    private List<User> data;
    private List<User> originaldata;
    private UserFilter filter;
    /** layout từng dòng hiển thị*/
    private static LayoutInflater inflater=null;

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
    	  /** muc luong hien tai */
    	  TextView txtSalary;
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
    	  /** image sinh nhật */
    	  ImageView imgUserBirthday;
		  /** các thông tin khác */
		  TextView txtUserOtherInformation;
    	 }
    private String  txtUserInfo="";
    
    public LazyAdapter(Activity a, List<User> d) {
        activity = a;
        data=d;
        originaldata= d;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
     }
    
    public int getCount() {
        return data.size();
    }

    public Object getItem(int position) {
        return data.get(position);
    }

    public long getItemId(int position) {
        return position;
        
    }
    
    public List<User> getListViewData(){
    	return data;
    }
    
    public boolean remove(int position) {
    	try{
    		if (position<0 || position > getCount()) 
    			return true;
    		
    		data.remove(position );
    		
    	}catch(Exception e){
    		Log.v("remove", e.getMessage());
    	}
    	
        return true;
    }
    
    public boolean removeAllCheckedItem() {
    	try{
    		for(Iterator<User> iter = data.iterator();iter.hasNext();){
    			User usr = iter.next();
    			if(usr.isselected==true){
    				iter.remove();
    			}
    		}
    			
    	}catch(Exception e){
    		Log.v("remove", e.getMessage());
    	}
    	
        return true;
    }

    public Filter getFilter() {
    	if (filter == null){
    		filter  = new UserFilter();
    	}
    	return filter;
    }
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        ViewHolder holder;
        ImageView iv = null;
        String textDisplayOnImage ="";
        
        if(convertView==null){
            vi = inflater.inflate(R.layout.activity_user_list_row, null);
            holder = new ViewHolder();
            
            holder.txtListUserCode = (TextView) vi.findViewById(R.id.txtListUserCode);
            holder.txtUserFullName = (TextView) vi.findViewById(R.id.txtListUserFullName);
            holder.txtUserInfo = (TextView) vi.findViewById(R.id.txtListUserInfor);
            holder.txtImgFullPath= (TextView) vi.findViewById(R.id.txtUserImgFullPath);
            holder.imgUserImage = (ImageView) vi.findViewById(R.id.imgListUser);
            holder.txtJapanseLevel = (TextView) vi.findViewById(R.id.txtListUserJapaneseLevel);
            holder.rtbKeiken = (RatingBar)vi.findViewById(R.id.txtUserKeikenRate);
            holder.chkListUser =(CheckBox)vi.findViewById(R.id.chkListUserSelect);
            holder.txtIn_date_count =(TextView)vi.findViewById(R.id.txtIn_date_count);
            holder.txtSalary =(TextView)vi.findViewById(R.id.txtSalary);
            holder.txtProgram_count =(TextView)vi.findViewById(R.id.txtProgram_count);
            holder.txtDetailDesign_count =(TextView)vi.findViewById(R.id.txtDetailDesign_count);
            holder.txtMobile =(TextView)vi.findViewById(R.id.txtMobile);
            holder.txtEmail=(TextView)vi.findViewById(R.id.txtEmail);
            
			holder.txtYasumiDate=(TextView)vi.findViewById(R.id.txtListUserYasumiDate);
			
			holder.imgUserImageYasumi=(ImageView)vi.findViewById(R.id.imgListUserYasumi);
			holder.imgUserBirthday = (ImageView) vi.findViewById(R.id.imgListUserBirthday);
			holder.txtUserOtherInformation=(TextView)vi.findViewById(R.id.txtOtherInfomation);

            iv = (ImageView) vi.findViewById(R.id.imgListUser);
            vi.setTag(holder);
        }else
        {
        	 iv = (ImageView) vi.findViewById(R.id.imgListUser);
        	 holder = (ViewHolder) vi.getTag();
        	 /** load hình ảnh nhân viên */
        	 DecodeTask task =(DecodeTask)iv.getTag(R.id.imgListUser);
        	 if (task !=null){
        		 task.cancel(true);
        	 }
        }
        
        holder.txtListUserCode.setText(String.valueOf(data.get(position).code));
        if (data.get(position).full_name!=null){
        	holder.txtUserFullName.setText(data.get(position).full_name);
        }else{
        	holder.txtUserFullName.setText("");
        }
        /** phong ban */
        txtUserInfo="";
        if (data.get(position).dept_name!=null){
        	txtUserInfo = txtUserInfo + data.get(position).dept_name;
        }
        /** nhóm */
        if (data.get(position).team_name!=null){
        	txtUserInfo = txtUserInfo + " - " +  data.get(position).team_name;
        }
        /** chức vụ */
        if (data.get(position).position_name!=null){
        	txtUserInfo = txtUserInfo + " - " + data.get(position).position_name;
        }
        /** setting thông tin phòng ban ....*/
        holder.txtUserInfo.setText(txtUserInfo);
        /** image path */
        if (data.get(position).img_fullpath!=null){
        	holder.txtImgFullPath.setText(data.get(position).img_fullpath);
        }else{
        	holder.txtImgFullPath.setText("");
        }
        /** trình độ nhật ngữ */
        if (data.get(position).japanese!=null){
        	holder.txtJapanseLevel.setText(data.get(position).japanese.toString());
        }
        else{
        	holder.txtJapanseLevel.setText("");
        }
        /** lấy ngày sinh*/
        Date datebirthDay=null;
        if (data.get(position).birthday!=null && data.get(position).birthday!=""){
        	if (DateTimeUtil.isDate(data.get(position).birthday ,MasterConstants.DATE_VN_FORMAT)){
        		datebirthDay = DateTimeUtil.convertStringToDate( data.get(position).birthday ,MasterConstants.DATE_VN_FORMAT);
        	}
        	/*if (DateTime.isDate(data.get(position).in_date ,MasterConstants.DATE_JP_FORMAT )){
        		String changeFormat = DateTime.formatDate2String(data.get(position).in_date, MasterConstants.DATE_JP_FORMAT, MasterConstants.DATE_VN_FORMAT);
        		
        		datefrom = DateTime.convertStringToDate(changeFormat ,MasterConstants.DATE_VN_FORMAT);
        	}*/
        }
        /** lấy ngày vào công ty*/
        Date datefrom=null;
        if (data.get(position).in_date!=null && data.get(position).in_date!=""){
        	if (DateTimeUtil.isDate(data.get(position).in_date ,MasterConstants.DATE_VN_FORMAT)){
        		datefrom = DateTimeUtil.convertStringToDate( data.get(position).in_date ,MasterConstants.DATE_VN_FORMAT);
        	}
        	/*if (DateTime.isDate(data.get(position).in_date ,MasterConstants.DATE_JP_FORMAT )){
        		String changeFormat = DateTime.formatDate2String(data.get(position).in_date, MasterConstants.DATE_JP_FORMAT, MasterConstants.DATE_VN_FORMAT);
        		
        		datefrom = DateTime.convertStringToDate(changeFormat ,MasterConstants.DATE_VN_FORMAT);
        	}*/
        }
        if(datefrom==null){
        	textDisplayOnImage ="Trial";
        }
        /** lấy ngày vào nhóm labour */
        Date datefromUssol=null;
        if (data.get(position).join_date!=null && data.get(position).join_date!="" ){
        	if (DateTimeUtil.isDate(data.get(position).join_date ,MasterConstants.DATE_VN_FORMAT)){
        		datefromUssol = DateTimeUtil.convertStringToDate( data.get(position).join_date ,MasterConstants.DATE_VN_FORMAT);
        	}
        	/*if (DateTime.isDate(data.get(position).join_date ,MasterConstants.DATE_JP_FORMAT )){
        		String changeFormat = DateTime.formatDate2String(data.get(position).join_date, MasterConstants.DATE_JP_FORMAT, MasterConstants.DATE_VN_FORMAT);
        		
        		datefromUssol = DateTime.convertStringToDate(changeFormat ,MasterConstants.DATE_VN_FORMAT);
        	}*/
        	
        }
        /** lấy ngày hiện tại */
        //Date dateto =DateTime.getCurrentDate("dd/MM/yyyy");
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
        if (keikenMonth>24){
        	/*
        	MathContext NDecimals = new MathContext(2 , RoundingMode.FLOOR);
        	BigDecimal bdNumber = new BigDecimal(keikenMonth/12.0, NDecimals);
        	double keikenYear = bdNumber.doubleValue();
        	*/
        	holder.txtIn_date_count.setText(String.valueOf(Utils.Round((keikenMonth/12.0), 1, RoundingMode.HALF_UP) +"N"));
        }else{
        	holder.txtIn_date_count.setText(String.valueOf(keikenMonth)+"Th");
        }
        
        
        holder.rtbKeiken.setRating((float) (keikenMonth/12.0));

        /** tính ra thâm niên từ lúc vào USSOL chính thức */
        if (datefromUssol==null){
        }else{
        	/** nếu như đã ra khỏi nhóm labour thì sẽ tính ngày kết thúc là ngày đó chứ không phải ngày
        	 * hiện tại
        	 */
        	if (DateTimeUtil.isDate(data.get(position).labour_out_date,MasterConstants.DATE_VN_FORMAT)){
        		dateto = DateTimeUtil.convertStringToDate( data.get(position).labour_out_date ,MasterConstants.DATE_VN_FORMAT);
        	}
        	
        	/*if (DateTime.isDate(data.get(position).labour_out_date ,MasterConstants.DATE_JP_FORMAT )){
        		String changeFormat = DateTime.formatDate2String(data.get(position).labour_out_date, MasterConstants.DATE_JP_FORMAT, MasterConstants.DATE_VN_FORMAT);
        		
        		dateto = DateTime.convertStringToDate(changeFormat ,MasterConstants.DATE_VN_FORMAT);
        	}*/
        	
        	keikenMonthInUssol =DateTimeUtil.getFullMonthDiff(datefromUssol, dateto);
        }

        /*if (keikenMonthInUssol>24){
        	*//** hien thi so nam kinh nghiem USSOL*//*
        	*//*
        	MathContext NDecimals = new MathContext(2 , RoundingMode.FLOOR);
        	BigDecimal bdNumber = new BigDecimal(keikenMonthInUssol/12.0, NDecimals);
        	double keikenYearInUssol = bdNumber.doubleValue();
        	*//*
            holder.txtJoin_date_count.setText( String.valueOf(Utils.Round((keikenMonthInUssol/12.0), 1, RoundingMode.HALF_UP) + "N"));
        }else{
        	*//** hien thi so nam kinh nghiem USSOL*//*
            holder.txtJoin_date_count.setText( String.valueOf(keikenMonthInUssol)+"Th");	
        }*/

		/** Hien thi muc luong*/
		/** hien thi / khong hien thi muc luong thi do setting */
		holder.txtSalary.setText( String.valueOf(data.get(position).salary_allowance));

		/** hien thi len man hinh thong tin nang suat lap trinh*/
        //holder.txtProgram_count.setText( String.valueOf(data.get(position).program));
        holder.txtProgram_count.setText( String.valueOf(data.get(position).estimate_point));
                
        /** hien thi len man hinh thong tin nang suat thiet ke chi tiet*/
        holder.txtDetailDesign_count.setText( String.valueOf(data.get(position).detaildesign));
        //chuyen thang hien thi tuoi cua nhan vien
        holder.txtDetailDesign_count.setText(String.valueOf(ageStaff)+"");
        /** so dien thoai*/
        holder.txtMobile.setText(String.valueOf(data.get(position).mobile));    
        /** email*/
        holder.txtEmail.setText(String.valueOf(data.get(position).email));
        
        String txtKeiken =keikenMonth + " tháng【Labour : "+ keikenMonthInUssol +" tháng】";
        
        //holder.imgUserImage.setImageBitmap(getBitmap(data.get(position).img_fullpath));
        Log.i("getView", Integer.toString(position));
        /** hiển thị hình ảnh */
        /** nếu không có hình */
    	if (data.get(position).img_fullpath==null || data.get(position).img_fullpath.equals("")){
    		iv.setImageResource(R.drawable.user);
    	}else{
    		iv.setImageBitmap(null);
	        DecodeTask task = new DecodeTask(activity,data.get(position), iv);
	        task.execute(data.get(position).img_fullpath);
	        /** set tag */
	        iv.setTag(R.id.imgListUser, task);
    	}
		/** ngày nghỉ việc*/
		holder.txtYasumiDate.setText("");
		if (data.get(position).out_date!=null && !data.get(position).out_date.isEmpty()){
			Date dateYasumi=null;
			Date training_date=null;
			if (DateTimeUtil.isDate(data.get(position).out_date ,MasterConstants.DATE_VN_FORMAT)){
				dateYasumi = DateTimeUtil.convertStringToDate( data.get(position).out_date ,MasterConstants.DATE_VN_FORMAT);
			}
			if (data.get(position).training_date!=null && !data.get(position).training_date.isEmpty()){
				if (DateTimeUtil.isDate(data.get(position).training_date ,MasterConstants.DATE_VN_FORMAT)){
					training_date = DateTimeUtil.convertStringToDate( data.get(position).training_date ,MasterConstants.DATE_VN_FORMAT);
				}
			}
			if(dateYasumi==null){
	        	textDisplayOnImage ="nghỉ";
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
			holder.txtYasumiDate.setText(String.valueOf(data.get(position).out_date) + "(" + String.valueOf(yasumiMonth) +"Th)");
	        
		}else {
			holder.imgUserImageYasumi.setVisibility(View.INVISIBLE);
		}
		
		/** nếu là sinh nhật thì hiển thị image sinh nhật bên cạnh tuổi*/
		if(Utils.isCurrentDateBirthday(data.get(position).birthday)){
			holder.imgUserBirthday.setVisibility(View.VISIBLE);
		}else{
			holder.imgUserBirthday.setVisibility(View.INVISIBLE);
		}

		/** hien thi them thong tin ngach bac se duoc xem xet tuong ung voi tham nien*/
		holder.txtUserOtherInformation.setText("");
		if (data.get(position).in_date!=null && !data.get(position).in_date.isEmpty()){
			holder.txtUserOtherInformation.setText(data.get(position).in_date);
			iv.setAlpha(0.6f);
		}else{
			iv.setAlpha(1.0f);
		}

        /** xử lý liên quan đến check box */
    	/** hủy không gán sự kiện tại timing này để tránh trường hợp 
    	 * 	setting check ban đầu sẽ bị sai do gọi thêm event addClickHandlerToCheckBox*/
    	holder.chkListUser.setOnCheckedChangeListener(null);
		User model =new User();
		User item =null;
		for(User usr: data ){
			model = usr;
			item = usr;
			if (item.code == data.get(position).code ){
				model.isselected = item.isselected;
				holder.chkListUser.setChecked(item.isselected);
				break;
			}
		}
		
		model.isselected =holder.chkListUser.isChecked();
		holder.chkListUser.setTag(model);
		/** gán sự kiện cho checkbox*/
		addClickHandlerToCheckBox(holder.chkListUser,position );
		
        return vi;
    }
    
    private Bitmap getBitmap(String path)
    {
    	Bitmap disBitmap = BitmapFactory.decodeFile(path);
        int desiredImageWidth = 1000;  // pixels
		int desiredImageHeight = 1200; // pixels

		BitmapFactory.Options o = new BitmapFactory.Options();
		Bitmap newImage=Bitmap.createScaledBitmap(disBitmap, desiredImageWidth, desiredImageHeight, false);
		return newImage;
    }
    
    public void setCheckAll(boolean check){
    	for ( User usr : data){
    		usr.isselected=check;
    	}
    }
    
    protected  void addClickHandlerToCheckBox(CheckBox checkbox,final int position )
    {
        checkbox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
	        public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
	            CheckBox checkbox = (CheckBox)arg0; 
	            boolean isChecked = checkbox.isChecked();
	            User item =null;
	            // Store the boolean value somewhere durable
	            if(isChecked==true){
	            	User model = (User)checkbox.getTag();
	            	if(model.isselected==true){
	            		//checked
	            	}else{
	            		for ( User usr : data){
	            			item = usr;
	            			if (item.code == model.code ){
	            				item.isselected=true;
	            				break;
	            			}
	            		}
	            	}
	            }
	            else{
	            	//remove from array list
	            	User model = (User)checkbox.getTag();
	            		            	
	            	if(model.isselected==true){
	            		for ( User usr : data){
	            			item = usr;
	            			if (item.code == model.code ){
	            				item.isselected=false;
	            				break;
	            			}
	            		}
	            	}else{
	            		//unchecked
	            	}
	            }
	        }
        });
    }
    /** dùng để filter data trên list */
    private class UserFilter extends Filter{

		@Override
		protected FilterResults performFiltering(CharSequence constraint) {
			constraint = constraint.toString().toLowerCase();
			FilterResults result = new FilterResults();
			if(constraint != null && constraint.toString().length() > 0)
		    {
				List<User> filteredItems = new ArrayList<User>();
				for(User usr: originaldata){
					if(usr.full_name.toLowerCase().contains(constraint)){
						filteredItems.add(usr);
					}
				}
				result.count = filteredItems.size();
				result.values = filteredItems;
		    }else{
		    	synchronized (this) {
		    		result.values = originaldata;
		    	    result.count = originaldata.size();
				}
		    }
			return result;
		}

		@SuppressWarnings("unchecked")
		@Override
		protected void publishResults(CharSequence constraint,
				FilterResults results) {
			data = (List<User>) results.values;
			notifyDataSetChanged();
			/*clear();
			for(User usr :data ){
				add(usr);
			}
			notifyDataSetInvalidated();*/
		}
    }
}