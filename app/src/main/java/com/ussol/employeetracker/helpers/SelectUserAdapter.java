package com.ussol.employeetracker.helpers;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.Visibility;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import com.ussol.employeetracker.R;
import com.ussol.employeetracker.models.SelectUser;
import com.ussol.employeetracker.models.User;
import com.ussol.employeetracker.utils.RoundImage;

/**
 * Created by Trinity Tuts on 10-01-2015.
 */
public class SelectUserAdapter extends BaseAdapter {

    public List<SelectUser> _data;
    private ArrayList<SelectUser> arraylist;
    Context _c;
    ViewHolder v;
    RoundImage roundedImage;
    /** kết nối Database */
	DatabaseAdapter mDatabaseAdapter;
	/** chuyển đổi Cursor thành List */
	private ConvertCursorToListString mConvertCursorToListString;
	private List<User> listUser;
	
    public SelectUserAdapter(List<SelectUser> selectUsers, Context context) {
        _data = selectUsers;
        _c = context;
        this.arraylist = new ArrayList<SelectUser>();
        this.arraylist.addAll(_data);
        this.mDatabaseAdapter = new DatabaseAdapter(context);
        /** chuyển đổi từ Cursor thành List */
		mConvertCursorToListString = new ConvertCursorToListString(context);
		listUser = getListUser("");
		
    }

    @Override
    public int getCount() {
        return _data.size();
    }

    @Override
    public Object getItem(int i) {
        return _data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public List<SelectUser> getListViewData(){
    	return _data;
    }
    
    public boolean remove(int position) {
    	try{
    		if (position<0 || position > getCount()) 
    			return true;
    		
    		_data.remove(position );
    		
    	}catch(Exception e){
    		Log.v("remove", e.getMessage());
    	}
    	
        return true;
    }
    
    public boolean removeAllCheckedItem() {
    	try{
    		for(Iterator<SelectUser> iter = _data.iterator();iter.hasNext();){
    			SelectUser usr = iter.next();
    			if(usr.getCheckedBox()==true){
    				iter.remove();
    			}
    		}
    			
    	}catch(Exception e){
    		Log.v("remove", e.getMessage());
    	}
    	
        return true;
    }
    
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        View view = convertView;
        if (view == null) {
            LayoutInflater li = (LayoutInflater) _c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = li.inflate(R.layout.activity_contact_row, null);
            Log.e("Inside", "here--------------------------- In view1");
        } else {
            view = convertView;
            Log.e("Inside", "here--------------------------- In view2");
        }

        v = new ViewHolder();

        v.title = (TextView) view.findViewById(R.id.name);
        v.nickname = (TextView) view.findViewById(R.id.nickName);
        v.email = (TextView) view.findViewById(R.id.emailAddress);
        v.check = (CheckBox) view.findViewById(R.id.check);
        v.phone = (TextView) view.findViewById(R.id.no);
        v.imageView = (ImageView) view.findViewById(R.id.pic);
        v.contactlinked = (ImageView) view.findViewById(R.id.contactlinked);
        v.googleId = (TextView) view.findViewById(R.id.txtGoogleContactId);
        
        view.setTag(v); 
        
        final SelectUser data = (SelectUser) _data.get(i);
        v.title.setText(data.getName());
        v.nickname.setText(data.getNickname());
        v.email.setText(data.getEmail());
        v.check.setChecked(data.getCheckedBox());
        v.phone.setText(data.getPhone()); 
        v.googleId.setText(data.getGoogleId()); 
        
        //Kiem tra xem da co ton tai user tuong ung hay chua 
        if(isSonzaiByEmailOrPhone(data.getPhone(),data.getEmail())) {
        	//hien thi lien ket
        	v.contactlinked.setVisibility(View.VISIBLE);
        	//khong cho chon checkbox va set tri thanh khong check
        	v.check.setChecked(false);
        	v.check.setEnabled(false);
        	_data.get(i).setLinked(true);
        }else
        {
        	v.contactlinked.setVisibility(View.INVISIBLE);
            v.check.setChecked(false);
            v.check.setEnabled(true);
            _data.get(i).setLinked(false);
        }
        // Set image if exists
        try {

            if (data.getThumb() != null) {
                v.imageView.setImageBitmap(data.getThumb());
                roundedImage = new RoundImage(data.getThumb());
                v.imageView.setImageDrawable(roundedImage);
            } else {
                v.imageView.setImageResource(R.drawable.ic_app);
                
                Bitmap bm = BitmapFactory.decodeResource(view.getResources(), R.drawable.ic_app); // Load default image
                roundedImage = new RoundImage(bm);
                v.imageView.setImageDrawable(roundedImage);
            }
            // Seting round image
            //Bitmap bm = BitmapFactory.decodeResource(view.getResources(), R.drawable.ic_app); // Load default image
            //roundedImage = new RoundImage(bm);
            //v.imageView.setImageDrawable(roundedImage);
            
            /** xử lý liên quan đến check box */
        	/** hủy không gán sự kiện tại timing này để tránh trường hợp 
        	 * 	setting check ban đầu sẽ bị sai do gọi thêm event addClickHandlerToCheckBox*/
        	v.check.setOnCheckedChangeListener(null);
    		SelectUser model =new SelectUser();
    		SelectUser item =null;
    		for(SelectUser usr: _data){
    			model = usr;
    			item = usr;
    			if (item.getGoogleId() == _data.get(i).getGoogleId() ){
    				model.setCheckedBox(item.getCheckedBox());
    				v.check.setChecked(item.getCheckedBox());
    				break;
    			}
    		}
    		
    		model.setCheckedBox(v.check.isChecked());
    		v.check.setTag(model);
    		/** gán sự kiện cho checkbox*/
    		addClickHandlerToCheckBox(v.check,i );
    		
        } catch (OutOfMemoryError e) {
            // Add default picture
            v.imageView.setImageDrawable(this._c.getDrawable(R.drawable.ic_app));
            e.printStackTrace();
        }

        Log.e("Image Thumb", "--------------" + data.getThumb());

        // Set check box listener android
        /*
        v.check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckBox checkBox = (CheckBox) view;
                if(!checkBox.isEnabled()) return;
                if (checkBox.isChecked()) {
                    data.setCheckedBox(true);
                  } else {
                    data.setCheckedBox(false);
                }
            }
        });
		*/
        view.setTag(data);
        return view;
    }

    protected  void addClickHandlerToCheckBox(CheckBox checkbox,final int position )
    {
        checkbox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
	        public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
	            CheckBox checkbox = (CheckBox)arg0; 
	            boolean isChecked = checkbox.isChecked();
	            SelectUser item =null;
	            // Store the boolean value somewhere durable
	            if(isChecked==true){
	            	SelectUser model = (SelectUser)checkbox.getTag();
	            	if(model.getCheckedBox()==true){
	            		//checked
	            	}else{
	            		for ( SelectUser usr : _data){
	            			item = usr;
	            			if (item.getGoogleId() == model.getGoogleId() ){
	            				item.setCheckedBox(true);
	            				break;
	            			}
	            		}
	            	}
	            }
	            else{
	            	//remove from array list
	            	SelectUser model = (SelectUser)checkbox.getTag();
	            		            	
	            	if(model.getCheckedBox()==true){
	            		for ( SelectUser usr : _data){
	            			item = usr;
	            			if (item.getGoogleId() == model.getGoogleId() ){
	            				item.setCheckedBox(false);
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
    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        _data.clear();
        if (charText.length() == 0) {
            _data.addAll(arraylist);
        } else {
            for (SelectUser wp : arraylist) {
                if (wp.getName().toLowerCase(Locale.getDefault())
                        .contains(charText)) {
                    _data.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }
    static class ViewHolder {
        ImageView imageView,contactlinked;
        TextView title, phone,nickname,email,googleId;
        CheckBox check;
    }
    
    public void setCheckAll(boolean check){
    	for ( SelectUser usr : _data){
    		if(!usr.getLinked()){
    			usr.setCheckedBox(check);
    		}
    	}
    }
    
    private boolean isSonzaiByEmailOrPhone(String phone,String email){
    	for(User usr : listUser){
    		if(!usr.email.isEmpty() && usr.email.equalsIgnoreCase(email)){
    			return true;
    		}
    		if(!usr.mobile.isEmpty() &&  usr.mobile.equalsIgnoreCase(phone)){
    			return true;
    		}
    	}
    	return false;
    }
    private boolean isSonzaiByPhone(String phone){
    	List<User> lstUser;
    	lstUser=mConvertCursorToListString.getUserList(" AND " + mDatabaseAdapter.KEY_MOBILE + "='" + phone + "'");
    	if(lstUser.size() >0){
    		return true;
    	}else{
    		return false;
    	}
    }
    
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * get user list
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
	public List<User> getListUser(String xWhere){
		List<User> list;
		/** chuyển đổi từ Cursor thành List */
		mConvertCursorToListString = new ConvertCursorToListString(_c);
		list = mConvertCursorToListString.getAllUserList(xWhere);
		return list;
	}
	
    
    
}
