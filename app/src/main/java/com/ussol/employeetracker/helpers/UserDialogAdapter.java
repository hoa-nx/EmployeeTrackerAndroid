package com.ussol.employeetracker.helpers;

import java.util.Date;

import com.ussol.employeetracker.R;
import com.ussol.employeetracker.helpers.LazyAdapter.ViewHolder;
import com.ussol.employeetracker.models.Dept;
import com.ussol.employeetracker.models.User;
import com.ussol.employeetracker.utils.DateTimeUtil;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class UserDialogAdapter extends ArrayAdapter{
	private static final int RESOURCE = R.layout.dialog_user_list_row;
	private LayoutInflater inflater;
	private Context context;
    static class ViewHolder {
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
    }

	public UserDialogAdapter(Context context, User[] objects)
	{
		super(context, RESOURCE, objects);
		inflater = LayoutInflater.from(context);
		context = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		ViewHolder holder;
		ImageView iv = null;
		
		if ( convertView == null ) {
			// inflate a new view and setup the view holder for future use
			convertView = inflater.inflate( RESOURCE, null );

			holder = new ViewHolder();
			holder.nameTxVw =(TextView) convertView.findViewById(R.id.txtListUserFullName);
			holder.txtImgFullPath= (TextView) convertView.findViewById(R.id.txtUserImgFullPath);
			holder.txtUserInfo = (TextView) convertView.findViewById(R.id.txtListUserInfor);
			holder.txtJapanseLevel = (TextView) convertView.findViewById(R.id.txtListUserJapaneseLevel);
            holder.txtIn_date_count =(TextView)convertView.findViewById(R.id.txtIn_date_count);
            holder.txtJoin_date_count =(TextView)convertView.findViewById(R.id.txtJoin_date_count);
            holder.txtProgram_count =(TextView)convertView.findViewById(R.id.txtProgram_count);
            holder.txtDetailDesign_count =(TextView)convertView.findViewById(R.id.txtDetailDesign_count);
            holder.txtListUserAllowance_Business =(TextView)convertView.findViewById(R.id.txtListUserAllowance_Business);
            
			iv = (ImageView) convertView.findViewById(R.id.imgListUser);
			
			convertView.setTag( holder );
			
		}  else {
			// view already defined, retrieve view holder
			 iv = (ImageView) convertView.findViewById(R.id.imgListUser);
			 holder = (ViewHolder) convertView.getTag();
        	 DecodeTask task =(DecodeTask)iv.getTag(R.id.imgListUser);
        	 if (task !=null){
        		 task.cancel(true);
        	 }
		}

		User cat =(User) getItem( position );
		if ( cat == null ) {
			
		}
		/** hien thi thong tin */
        
		holder.nameTxVw.setText( cat.full_name );
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
        /** trình độ nhật ngữ */
        if (cat.japanese!=null){
        	holder.txtJapanseLevel.setText(cat.japanese.toString());
        }
        else{
        	holder.txtJapanseLevel.setText("");
        }
        /** phụ cấp nghiệp vụ*/
        if (cat.allowance_business!=null){
        	/** tro cap nghiep vu*/
            holder.txtListUserAllowance_Business.setText( String.valueOf(cat.allowance_business));
        }
        else{
        	holder.txtListUserAllowance_Business.setText("");
        }
        /** lấy ngày vào công ty*/
        Date datefrom=null;
        if (cat.in_date!=null && cat.in_date!=""){
        	if (DateTimeUtil.isDate(cat.in_date ,"dd/MM/yyyy")){
        		datefrom = DateTimeUtil.convertStringToDate( cat.in_date ,"dd/MM/yyyy");
        	}
        	
        }
        /** lấy ngày vào nhóm labour */
        Date datefromUssol=null;
        if (cat.join_date!=null && cat.join_date!="" ){
        	if (DateTimeUtil.isDate(cat.join_date ,"dd/MM/yyyy")){
        		datefromUssol = DateTimeUtil.convertStringToDate(cat.join_date ,"dd/MM/yyyy");
        	}
        	
        }
        /** lấy ngày hiện tại */
        Date dateto =DateTimeUtil.getCurrentDate("dd/MM/yyyy");
        int keikenMonth =0;
        int keikenMonthInUssol =0;
        
        /** tính ra thâm niên từ lúc vào công ty chính thức */
        if (datefrom==null){
        }else{
        	keikenMonth =DateTimeUtil.getFullMonthDiff(datefrom, dateto);
        }
        
        /** hien thi len man hinh*/
        holder.txtIn_date_count.setText(String.valueOf(keikenMonth));

        /** tính ra thâm niên từ lúc vào USSOL chính thức */
        if (datefromUssol==null){
        }else{
        	/** nếu như đã ra khỏi nhóm labour thì sẽ tính ngày kết thúc là ngày đó chứ không phải ngày
        	 * hiện tại
        	 */
        	if (DateTimeUtil.isDate(cat.labour_out_date, "dd/MM/yyyy")){
        		dateto = DateTimeUtil.convertStringToDate(cat.labour_out_date ,"dd/MM/yyyy");
        	}
        	keikenMonthInUssol =DateTimeUtil.getFullMonthDiff(datefromUssol, dateto);
        }
        /** hien thi so nam kinh nghiem USSOL*/
        holder.txtJoin_date_count.setText( String.valueOf(keikenMonthInUssol));
        
        /** hien thi len man hinh thong tin nang suat lap trinh*/
        holder.txtProgram_count.setText( String.valueOf(cat.program));
        
        /** hien thi len man hinh thong tin nang suat thiet ke chi tiet*/
        holder.txtDetailDesign_count.setText( String.valueOf(cat.detaildesign));
        
		/** hiển thị hình ảnh */
    	if (cat.img_fullpath==null || cat.img_fullpath.equals("")){
    		/** nếu không có hình thì hiển thị hình mặc định*/
    		iv.setImageResource(R.drawable.user);
    	}else{
    		iv.setImageBitmap(null);
	        DecodeTask task = new DecodeTask(iv);
	        /** load image */
	        task.execute(cat.img_fullpath);
	        /** set tag */
	        iv.setTag(R.id.imgListUser, task);
    	}
    	
		//holder.nameTxVw.setCompoundDrawables( cat.getImg(), null, null, null );
		//holder.nameTxVw.setCompoundDrawables(getImg( R.drawable.user_info), null, null, null );
		return convertView;
	}
	private Drawable getImg( int res )
	{
		Drawable img =context.getResources().getDrawable( res );
		img.setBounds( 0, 0, 75, 85 );
		return img;
	}
}
