package com.ussol.employeetracker.helpers;

import java.util.List;

import com.ussol.employeetracker.R;
import com.ussol.employeetracker.models.Dept;
import com.ussol.employeetracker.models.Position;
import com.ussol.employeetracker.models.PositionGroup;
import com.ussol.employeetracker.models.Team;
import com.ussol.employeetracker.models.User;
import com.ussol.employeetracker.models.UserPositionGroup;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

public class PositionGroupDialogAdapter extends ArrayAdapter{
	private static final int RESOURCE = R.layout.dialog_master_list_row;
	private LayoutInflater inflater;
	private Context context;
	private PositionGroup[] data;
	private List<UserPositionGroup> checkedPositionGroupList;
	private boolean isFirstRun = true;
    static class ViewHolder {
        TextView nameTxVw;
        TextView userInfo;
        CheckBox chkPositionGroup;
    }
    
	public PositionGroupDialogAdapter(Context context, PositionGroup[] objects , List<UserPositionGroup> checkedItem)
	{
		super(context, RESOURCE, objects);
		inflater = LayoutInflater.from(context);
		context = context;
		/** copy array */
		data =objects;
		checkedPositionGroupList = checkedItem;
		/** phản ánh các item được check vào datasource */
		updateCheckStatusInit();
	}

	private void updateCheckStatusInit(){
		if (checkedPositionGroupList ==null){
			return;
		}
		if (checkedPositionGroupList.size()==0){
			return;
		}
		for(UserPositionGroup usergroup :checkedPositionGroupList ){
			for(int i=0 ; i<data.length;i++){
				if(data[i].code==usergroup.position_group_code){
					data[i].isselected =true;
				}
			}
		}
	}
	
	private PositionGroup[] copyArray(PositionGroup[] source){
		PositionGroup[] copy;
		
		copy = source;
		for(int i=0 ; i<source.length;i++){
			copy[i] = source[i];
		}
		return copy;
	}
	public PositionGroup[] getDataAdapter(){
		return data;
	}
	
	public PositionGroup[] getInitStatesDataAdapter(){
		PositionGroup[] datainit=setCheck(data, false);
		/** init thành trạng thái chưa gắn check */
		for(UserPositionGroup usergroup :checkedPositionGroupList ){
			for(int i=0 ; i<datainit.length;i++){
				datainit[i].isselected =false;
				if(datainit[i].code==usergroup.position_group_code){
					datainit[i].isselected =true;
				}
			}
		}
		return datainit;
	}

	private PositionGroup[] setCheck(PositionGroup[] data , boolean isChecked){
		PositionGroup[] datainit=data;
		for(int i=0 ; i<datainit.length;i++){
			datainit[i].isselected =isChecked;
		}
		return datainit;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		ViewHolder holder;

		if ( convertView == null ) {
			// inflate a new view and setup the view holder for future use
			convertView = inflater.inflate( RESOURCE, null );

			holder = new ViewHolder();
			holder.nameTxVw =(TextView) convertView.findViewById(R.id.txtListUserFullName);
			holder.userInfo =(TextView) convertView.findViewById(R.id.txtListUserInfor);
			holder.chkPositionGroup =(CheckBox) convertView.findViewById(R.id.chkListItem);
			
			convertView.setTag( holder );
		}  else {
			// view already defined, retrieve view holder
			holder = (ViewHolder) convertView.getTag();
			
		}

		PositionGroup cat =(PositionGroup) getItem( position );
		if ( cat == null ) {
			
		}
		holder.nameTxVw.setText( cat.name );
		holder.userInfo.setText( "" );
		holder.chkPositionGroup.setOnCheckedChangeListener(null);
		PositionGroup model =new PositionGroup();
		PositionGroup item =null;
		for(int i=0 ; i<data.length;i++){
			model = data[i];
			item = data[i];
			if (item.code == cat.code ){
				model.isselected = item.isselected;
				holder.chkPositionGroup.setChecked(item.isselected);
				break;
			}
		}
		model.isselected =holder.chkPositionGroup.isChecked();
		holder.chkPositionGroup.setTag(model);
		
		addClickHandlerToCheckBox(holder.chkPositionGroup,position );
		//holder.nameTxVw.setCompoundDrawables( cat.getImg(), null, null, null );
		//holder.nameTxVw.setCompoundDrawables(getImg( R.drawable.user_info), null, null, null );
		return convertView;
	}
	
	protected  void addClickHandlerToCheckBox(CheckBox checkbox,final int position )
    {
        checkbox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
	        public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
	            CheckBox checkbox = (CheckBox)arg0; 
	            boolean isChecked = checkbox.isChecked();
	            PositionGroup item =null;
	            // Store the boolean value somewhere durable
	            if(isChecked==true){
	            	PositionGroup model = (PositionGroup)checkbox.getTag();
	            	if(model.isselected==true){
	            		//checked
	            	}else{
	            		for(int i=0 ; i<data.length;i++){
	            			item = data[i];
	            			if (item.code == model.code ){
	            				item.isselected=true;
	            				break;
	            			}
	            		}
	            	}
	            }
	            else{
	            	//remove from array list
	            	PositionGroup model = (PositionGroup)checkbox.getTag();
	            		            	
	            	if(model.isselected==true){
	            		for(int i=0 ; i<data.length;i++){
	            			item = data[i];
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
	
	private Drawable getImg( int res )
	{
		Drawable img =context.getResources().getDrawable( res );
		img.setBounds( 0, 0, 75, 85 );
		return img;
	}
}
