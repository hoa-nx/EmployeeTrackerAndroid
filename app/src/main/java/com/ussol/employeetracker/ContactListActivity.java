package com.ussol.employeetracker;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Event;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ussol.employeetracker.helpers.ContactToUser;
import com.ussol.employeetracker.helpers.SelectUserAdapter;
import com.ussol.employeetracker.models.SelectUser;

public class ContactListActivity extends Activity  implements OnClickListener  {

	// ArrayList
    ArrayList<SelectUser> selectUsers;
    List<SelectUser> temp;
    // Contact List
    ListView listView;
    // Cursor to load contacts list
    Cursor phones, email;

    // Pop up
    ContentResolver resolver;
    SearchView search;
    SelectUserAdapter adapter;

    private ImageButton btnCreateUser , btnSelectAll , btnDeSelectAll,btnBack ;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);

        selectUsers = new ArrayList<SelectUser>();
        resolver = this.getContentResolver();
        listView = (ListView) findViewById(R.id.contacts_list);

        phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");
        LoadContact loadContact = new LoadContact();
        loadContact.execute();

        search = (SearchView) findViewById(R.id.searchView);

        //*** setOnQueryTextListener ***
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                // TODO Auto-generated method stub

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // TODO Auto-generated method stub
                adapter.filter(newText);
                return false;
            }
        });
        
        
        /** get cac control tren man hinh */
        getControl();
        /** gán các sự kiện cho các control */
        settingListener();
        
        
    }

    // Load data on background
    class LoadContact extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @SuppressWarnings("unchecked")
		@Override
        protected Void doInBackground(Void... voids) {
            // Get Contact list from Phone

            if (phones != null) {
                Log.e("count", "" + phones.getCount());
                if (phones.getCount() == 0) {
                    Toast.makeText(ContactListActivity.this, "No contacts in your contact list.", Toast.LENGTH_LONG).show();
                }

                while (phones.moveToNext()) {
                	
                	Uri dataUri = ContactsContract.Data.CONTENT_URI;
                	long contactId = phones.getLong(phones.getColumnIndex("_ID"));
                	contactId=phones.getLong(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID));
                	
                	// Querying the table ContactsContract.Data to retrieve individual items like
                    // home phone, mobile phone, work email etc corresponding to each contact
                    Cursor dataCursor = getContentResolver().query(dataUri, null,
                                        ContactsContract.Data.CONTACT_ID + "=" + contactId,
                                        null, null);
                    String displayName="";
                    String nickName="";
                    String homePhone="";
                    String mobilePhone="";
                    String workPhone="";
                    String homeEmail="";
                    String workEmail="";
                    String companyName="";
                    String title="";
                    String birthday="";
                    String googleContactId="";
                    
                    if(dataCursor.moveToFirst()){
                        // Getting Display Name
                        displayName = dataCursor.getString(dataCursor.getColumnIndex(ContactsContract.Data.DISPLAY_NAME ));
                        do{
 
                            // Getting NickName
                            if(dataCursor.getString(dataCursor.getColumnIndex("mimetype")).equals(ContactsContract.CommonDataKinds.Nickname.CONTENT_ITEM_TYPE))
                                nickName = dataCursor.getString(dataCursor.getColumnIndex("data1"));

                            // Getting Phone numbers
                            if(dataCursor.getString(dataCursor.getColumnIndex("mimetype")).equals(ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)){
                                switch(dataCursor.getInt(dataCursor.getColumnIndex("data2"))){
                                    case ContactsContract.CommonDataKinds.Phone.TYPE_HOME :
                                        homePhone = dataCursor.getString(dataCursor.getColumnIndex("data1"));
                                        break;
                                    case ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE :
                                        mobilePhone = dataCursor.getString(dataCursor.getColumnIndex("data1"));
                                        break;
                                    case ContactsContract.CommonDataKinds.Phone.TYPE_WORK :
                                        workPhone = dataCursor.getString(dataCursor.getColumnIndex("data1"));
                                        break;
                                }
                            }
 
                            // Getting EMails
                            if(dataCursor.getString(dataCursor.getColumnIndex("mimetype")).equals(ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE ) ) {
                                switch(dataCursor.getInt(dataCursor.getColumnIndex("data2"))){
                                    case ContactsContract.CommonDataKinds.Email.TYPE_HOME :
                                        homeEmail = dataCursor.getString(dataCursor.getColumnIndex("data1"));
                                        break;
                                    case ContactsContract.CommonDataKinds.Email.TYPE_WORK :
                                        workEmail = dataCursor.getString(dataCursor.getColumnIndex("data1"));
                                        break;
                                }
                            }
                         // Getting birthday
                            if(dataCursor.getString(dataCursor.getColumnIndex("mimetype")).equals(ContactsContract.CommonDataKinds.Event.CONTENT_ITEM_TYPE ) ) {
                                switch(dataCursor.getInt(dataCursor.getColumnIndex(ContactsContract.CommonDataKinds.Event.TYPE))){
                                    case ContactsContract.CommonDataKinds.Event.TYPE_BIRTHDAY :
                                        birthday = dataCursor.getString(dataCursor.getColumnIndex(ContactsContract.CommonDataKinds.Event.START_DATE));
                                        break;
                                }
                            }
 
                            // Getting Organization details
                            if(dataCursor.getString(dataCursor.getColumnIndex("mimetype")).equals(ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE)){
                                companyName = dataCursor.getString(dataCursor.getColumnIndex("data1"));
                                title = dataCursor.getString(dataCursor.getColumnIndex("data4"));
                            }

                        }while(dataCursor.moveToNext());
                    }
                    dataCursor.close();
                    
                    Bitmap bit_thumb = null;
                    String id = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID));
                    String name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));

                    String note = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Note.DATA1));
                    String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    //String emailAddress = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA2));
                    String image_thumb = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_THUMBNAIL_URI));
                    try {
                        if (image_thumb != null) {
                            bit_thumb = MediaStore.Images.Media.getBitmap(resolver, Uri.parse(image_thumb));
                        } else {
                            Log.e("No Image Thumb", "--------------");
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    SelectUser selectUser = new SelectUser();
                    
                    selectUser.setThumb(bit_thumb);
                    selectUser.setName(name);
                    selectUser.setNickname(nickName);
                    selectUser.setPhone(phoneNumber);
                    selectUser.setEmail(workEmail);
                    selectUser.setBirthDay(birthday);
                    selectUser.setGoogleId(String.valueOf(contactId));
                    
                    selectUser.setCheckedBox(false);
                    
                    selectUsers.add(selectUser);
                }
            } else {
                Log.e("Cursor close 1", "----------------");
            }
            //phones.close();
            EmployeeTrackerApplication.googleContactList = (ArrayList<SelectUser>) selectUsers.clone();
            
            return null;
        }

        public List<SelectUser> cloneList(List<SelectUser> list) {
            List<SelectUser> clone = new ArrayList<SelectUser>(list.size());
            for(SelectUser item: list) 
            	{
            		clone.add(item);
            	}
            return clone;
        }
        
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            adapter = new SelectUserAdapter(selectUsers, ContactListActivity.this);
            listView.setAdapter(adapter);

            // Select item on listclick
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    Log.e("search", "here---------------- listener");

                    SelectUser data = selectUsers.get(i);
                }
            });

            listView.setFastScrollEnabled(true);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        phones.close();
    }
    
    @Override
    public  void onBackPressed()
    {
    	super.onBackPressed();
    	this.finish();
    }
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
	 * onClick
	 * 
	 ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
	@Override
	public void onClick(View v) {
		/** phân nhánh xử lý theo từng button*/
		switch (v.getId()) {
			case R.id.btnSelectAll:
				setCheckAll();
				break;
			case R.id.btnSchedule_button:
				setUnCheckAll();
				break;
			case R.id.btnCancel_button:
				/** tao danh sach user tu list cotact dang  chon */
				createUserToDbEntryPoint();
				//saveUserToDb();
				break;

			case R.id.btnBack:
				finish();
				break;
		}
	}

	/**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * 
     * gan check cho cac item tren list
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
	private void setCheckAll(){

		for(int i=0; i < listView.getChildCount(); i++){
		    RelativeLayout itemLayout = (RelativeLayout)listView.getChildAt(i);
		    CheckBox cb = (CheckBox)itemLayout.findViewById(R.id.check);
		    if(cb.isEnabled()){
		    	cb.setChecked(true);
		    }
		    
		}
	
		adapter.setCheckAll(true);

	}
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * 
     *  bo check cho cac item tren list
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
	private void setUnCheckAll(){

		for(int i=0; i < listView.getChildCount(); i++){
		    RelativeLayout itemLayout = (RelativeLayout)listView.getChildAt(i);
		    CheckBox cb = (CheckBox)itemLayout.findViewById(R.id.check);
		    if(cb.isEnabled()){
		    	cb.setChecked(false);
		    }
		}

		adapter.setCheckAll(false);
	}
	
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * get các control trên màn hình
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    private void getControl(){
    	btnCreateUser= (ImageButton)findViewById(R.id.btnCancel_button);
    	btnSelectAll= (ImageButton)findViewById(R.id.btnSelectAll);
    	btnDeSelectAll= (ImageButton)findViewById(R.id.btnSchedule_button);
    	btnBack= (ImageButton)findViewById(R.id.btnBack);
    }
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * gán sự kiện cho các control
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    private void settingListener(){
    	btnCreateUser.setOnClickListener(this);
    	btnSelectAll.setOnClickListener(this);
    	btnDeSelectAll.setOnClickListener(this);
		btnBack.setOnClickListener(this);
		
    }
    
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * save thong tin user da input 
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
	public void createUserToDbEntryPoint(){
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
		/** Setting Dialog Title */
        alertDialog.setTitle(getResources().getString(R.string.titleSave));

        /** Setting Dialog Message */
        alertDialog.setMessage("Bạn có tạo thông tin nhân viên này không?");
        
        /** Setting Icon to Dialog*/
        alertDialog.setIcon(R.drawable.save_back);
        /** Setting Positive "Yes" Button */
        alertDialog.setPositiveButton(getResources().getText(R.string.titleYes), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
            	//saveUserToDbTask();
            	/** Lưu vào DB */
				saveUserToDb();
            }
        });
 
        /** Setting Negative "NO" Button */
        alertDialog.setNegativeButton(getResources().getText(R.string.titleNo), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            dialog.cancel();
            }
        });
 
        /** Showing Alert Message */
        alertDialog.create().show();
        
	}
	
	private void saveUserToDbTask(){
		new AsyncTask<Void, Void, Boolean>()
	    {
	        ProgressDialog progressDialog;
	
	        @Override
			protected void onPreExecute() {
				
				progressDialog = new ProgressDialog(getApplicationContext());
				progressDialog.setCancelable(false);
				progressDialog.setTitle("Đang tạo dữ liệu");
				progressDialog.setMessage("Xin vui lòng chờ...");
				progressDialog.show();
				
			}
	        
	        @Override
	        protected Boolean doInBackground(Void... params)
	        {
	            try
	            {
	            	
	            	/** Lưu vào DB */
					saveUserToDb();

	            }
	            catch (Exception e)
	            {
	            	return false;
	            }
	            return true;
	        }
	
	        @Override
	        protected void onPostExecute(Boolean result)
	        {
	        	
	        	progressDialog.cancel();
	        	String message="";
				if (result==true){
					message="Tạo thành công.";
				}else{
					message="Tạo thất bại.";
				}
	        	AlertDialog.Builder b = new AlertDialog.Builder(getApplicationContext());
	            b.setTitle("Lưu dữ liệu");
	
	                b.setMessage(message);
	             
	                b.setPositiveButton(getString(android.R.string.ok),
	                        new DialogInterface.OnClickListener()
	                        {
	
	                            @Override
	                            public void onClick(DialogInterface dlg, int arg1)
	                            {
	                                dlg.dismiss();
	                            }
	                        });
	                b.create().show();
	            }
	        }.execute();
	        
	        
	}
	
	public void saveUserToDb()
	{
		List<SelectUser> listUser =adapter._data;
		ContactToUser update = new ContactToUser(this);
		for(SelectUser contact : listUser){
			if(contact.getCheckedBox()){
				try {
					update.InsertContactToDB(contact);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	
	}

	
}
