package com.ussol.employeetracker.helpers;

import java.util.List;

import com.ussol.employeetracker.R;
import com.ussol.employeetracker.models.Dept;
import com.ussol.employeetracker.models.Position;
import com.ussol.employeetracker.models.Team;
import com.ussol.employeetracker.models.User;

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

public class DialogAdapterCheckBox<T> extends ArrayAdapter<T>{
	private static final int RESOURCE = R.layout.dialog_master_list_row;
	private LayoutInflater inflater;
	private Context context;
	private T[] data;
	private List<T> checkedList;
    static class ViewHolder {
        TextView nameTxVw;
        TextView txtInfor;
        CheckBox chkItem;
    }
    
	public DialogAdapterCheckBox(Context context, T[] objects , List<T> checkedItem)
	{
		super(context, RESOURCE, objects);
		inflater = LayoutInflater.from(context);
		context = context;
		/** copy array */
		data =objects;
		checkedList = checkedItem;
		/** phản ánh các item được check vào datasource */
		updateCheckStatusInit();
	}

	private void updateCheckStatusInit(){
		if (checkedList ==null){
			return;
		}
		if (checkedList.size()==0){
			return;
		}
		for(T usergroup :checkedList ){
			for(int i=0 ; i<data.length;i++){
				if(usergroup  instanceof User){
					if(((User)data[i]).code==((User)usergroup).code){
						((User)data[i]).isselected =true;
					}
				}
				if(usergroup  instanceof Dept){
					if(((Dept)data[i]).code==((Dept)usergroup).code){
						((Dept)data[i]).isselected =true;
					}
				}
				if(usergroup  instanceof Team){
					if(((Team)data[i]).code==((Team)usergroup).code){
						((Team)data[i]).isselected =true;
					}
				}
				if(usergroup  instanceof Position){
					if(((Position)data[i]).code==((Position)usergroup).code){
						((Position)data[i]).isselected =true;
					}
				}
			}
		}
	}
	
	private T[] copyArray(T[] source){
		T[] copy;
		
		copy = source;
		for(int i=0 ; i<source.length;i++){
			copy[i] = source[i];
		}
		return copy;
	}
	public T[] getDataAdapter(){
		return data;
	}
		
	public T[] getInitStatesDataAdapter(){
		/** trả về list mà đã truyền vào khi tạo đối tượng */
		T[] datainit= setCheck(data, false);

		for(T usergroup :checkedList ){
			for(int i=0 ; i<datainit.length;i++){
				if(usergroup  instanceof User){
					((User)datainit[i]).isselected =false;
					/** gắn check cho item theo data đã truyền vào khi khởi tạo object*/
					if(((User)datainit[i]).code==((User)usergroup).code){
						((User)datainit[i]).isselected =true;
					}
				}
				if(usergroup  instanceof Dept){
					((Dept)datainit[i]).isselected =false;
					/** gắn check cho item theo data đã truyền vào khi khởi tạo object*/
					if(((Dept)datainit[i]).code==((Dept)usergroup).code){
						((Dept)datainit[i]).isselected =true;
					}
				}
				if(usergroup  instanceof Team){
					((Team)datainit[i]).isselected =false;
					/** gắn check cho item theo data đã truyền vào khi khởi tạo object*/
					if(((Team)datainit[i]).code==((Team)usergroup).code){
						((Team)datainit[i]).isselected =true;
					}
				}
				if(usergroup  instanceof Position){
					((Position)datainit[i]).isselected =false;
					/** gắn check cho item theo data đã truyền vào khi khởi tạo object*/
					if(((Position)datainit[i]).code==((Position)usergroup).code){
						((Position)datainit[i]).isselected =true;
					}
				}
			}
		}
		return datainit;
	}
	
	private T[] setCheck(T[] data , boolean isChecked){
		T[] datainit=data;
		for(int i=0 ; i<datainit.length;i++){
			if(datainit[i]  instanceof User){
				((User)datainit[i]).isselected =isChecked;
			}
			if(datainit[i]  instanceof Dept){
				((Dept)datainit[i]).isselected =isChecked;
			}
			if(datainit[i]  instanceof Team){
				((Team)datainit[i]).isselected =isChecked;
			}
			if(datainit[i]  instanceof Position){
				((Position)datainit[i]).isselected =isChecked;
			}
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
			holder.chkItem =(CheckBox) convertView.findViewById(R.id.chkListItem);
			
			convertView.setTag( holder );
		}  else {
			// view already defined, retrieve view holder
			holder = (ViewHolder) convertView.getTag();
			
		}

		T cat =(T) getItem( position );
		if ( cat == null ) {
			
		}
		
		holder.chkItem.setOnCheckedChangeListener(null);
		/** phòng ban */
		if (cat  instanceof Dept){
			Dept cast=(Dept)cat; 
			holder.nameTxVw.setText( cast.name );
			Dept model =new Dept();
			Dept item =null;
			for(int i=0 ; i<data.length;i++){
				model =(Dept) data[i];
				item =(Dept)data[i];
				if (item.code == cast.code ){
					model.isselected = item.isselected;
					holder.chkItem.setChecked(item.isselected);
					break;
				}
			}
			model.isselected =holder.chkItem.isChecked();
			holder.chkItem.setTag(model);
		}
		
		/** nhóm */
		if (cat  instanceof Team){
			Team cast=(Team)cat; 
			holder.nameTxVw.setText( cast.name );
			Team model =new Team();
			Team item =null;
			for(int i=0 ; i<data.length;i++){
				model =(Team) data[i];
				item =(Team)data[i];
				if (item.code == cast.code ){
					model.isselected = item.isselected;
					holder.chkItem.setChecked(item.isselected);
					break;
				}
			}
			model.isselected =holder.chkItem.isChecked();
			holder.chkItem.setTag(model);
		}
		
		/** chức vụ */
		if (cat  instanceof Position){
			Position cast=(Position)cat; 
			holder.nameTxVw.setText( cast.name );
			Position model =new Position();
			Position item =null;
			for(int i=0 ; i<data.length;i++){
				model =(Position) data[i];
				item =(Position)data[i];
				if (item.code == cast.code ){
					model.isselected = item.isselected;
					holder.chkItem.setChecked(item.isselected);
					break;
				}
			}
			model.isselected =holder.chkItem.isChecked();
			holder.chkItem.setTag(model);
		}
		
		/** danh sach user*/
		if (cat  instanceof User){
			User cast=(User)cat; 
			holder.nameTxVw.setText( cast.full_name );
			User model =new User();
			User item =null;
			for(int i=0 ; i<data.length;i++){
				model =(User) data[i];
				item =(User)data[i];
				if (item.code == cast.code ){
					model.isselected = item.isselected;
					holder.chkItem.setChecked(item.isselected);
					break;
				}
			}
			model.isselected =holder.chkItem.isChecked();
			holder.chkItem.setTag(model);
		}
		
		addClickHandlerToCheckBox(holder.chkItem,position );
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
	            T item =null;
	            // Store the boolean value somewhere durable
	            if(isChecked==true){
	            	T model = (T)checkbox.getTag();
	            	/** user */
	            	if (model  instanceof User){
	            		User cast= (User) model;
	            		if(cast.isselected==true){
	            		//checked
		            	}else{
		            		for(int i=0 ; i<data.length;i++){
		            			item = data[i];
		            			if (((User)item).code == ((User)model).code ){
		            				((User)item).isselected=true;
		            				break;
		            			}
		            			
		            		}
		            	}
	            	}
	            	/** phòng ban */
	            	if (model  instanceof Dept){
	            		Dept cast= (Dept) model;
	            		if(cast.isselected==true){
	            		//checked
		            	}else{
		            		for(int i=0 ; i<data.length;i++){
		            			item = data[i];
		            			if (((Dept)item).code == ((Dept)model).code ){
		            				((Dept)item).isselected=true;
		            				break;
		            			}
		            			
		            		}
		            	}
	            	}
	            	/** nhóm */
	            	if (model  instanceof Team){
	            		Team cast= (Team) model;
	            		if(cast.isselected==true){
	            		//checked
		            	}else{
		            		for(int i=0 ; i<data.length;i++){
		            			item = data[i];
		            			if (((Team)item).code == ((Team)model).code ){
		            				((Team)item).isselected=true;
		            				break;
		            			}
		            			
		            		}
		            	}
	            	}
	            	/** chức vụ */
	            	if (model  instanceof Position){
	            		Position cast= (Position) model;
	            		if(cast.isselected==true){
	            		//checked
		            	}else{
		            		for(int i=0 ; i<data.length;i++){
		            			item = data[i];
		            			if (((Position)item).code == ((Position)model).code ){
		            				((Position)item).isselected=true;
		            				break;
		            			}
		            			
		            		}
		            	}
	            	}
	            	
	            }
	            else{
	            	//remove from array list
	            	T model = (T)checkbox.getTag();
	            	/** user */
	            	if(model  instanceof User){
	            		if(((User)model).isselected==true){
	            		for(int i=0 ; i<data.length;i++){
	            			item = data[i];
	            			if (((User)item).code == ((User)model).code ){
	            				((User)item).isselected=false;
	            				break;
	            			}
	            		}
	            	}else{
	            		//unchecked
	            		}
	            	}
	            	/** phòng ban */
	            	if(model  instanceof Dept){
	            		if(((Dept)model).isselected==true){
	            		for(int i=0 ; i<data.length;i++){
	            			item = data[i];
	            			if (((Dept)item).code == ((Dept)model).code ){
	            				((Dept)item).isselected=false;
	            				break;
	            			}
	            		}
	            	}else{
	            		//unchecked
	            		}
	            	}
	            	/**nhóm */
	            	if(model  instanceof Team){
	            		if(((Team)model).isselected==true){
	            		for(int i=0 ; i<data.length;i++){
	            			item = data[i];
	            			if (((Team)item).code == ((Team)model).code ){
	            				((Team)item).isselected=false;
	            				break;
	            			}
	            		}
	            	}else{
	            		//unchecked
	            		}
	            	}
	            	/** chức vụ */
	            	if(model  instanceof Position){
	            		if(((Position)model).isselected==true){
	            		for(int i=0 ; i<data.length;i++){
	            			item = data[i];
	            			if (((Position)item).code == ((Position)model).code ){
	            				((Position)item).isselected=false;
	            				break;
	            			}
	            		}
	            	}else{
	            		//unchecked
	            		}
	            	}
	            	
	            }
	        }
        });
    }
	
	public class Generic<T>
	{
	    private Class<T> clazz;

	    public Generic(Class<T> clazz)
	    {
	        this.clazz = clazz;
	    }

	    public T buildOne() throws InstantiationException,
	        IllegalAccessException
	    {
	        return clazz.newInstance();
	    }
	}

	
	private Drawable getImg( int res )
	{
		Drawable img =context.getResources().getDrawable( res );
		img.setBounds( 0, 0, 75, 85 );
		return img;
	}
}
