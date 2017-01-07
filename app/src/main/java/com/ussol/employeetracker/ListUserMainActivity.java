/**
 * Copyright (c) 2012 Fujinet Software Department 5 ( USSOL team)  
 * See the file license.txt for copying permission.
 */
package com.ussol.employeetracker;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.nightonke.boommenu.BoomButtons.ButtonPlaceEnum;
import com.nightonke.boommenu.BoomButtons.OnBMClickListener;
import com.nightonke.boommenu.BoomButtons.SimpleCircleButton;
import com.nightonke.boommenu.BoomMenuButton;
import com.nightonke.boommenu.ButtonEnum;
import com.nightonke.boommenu.Piece.PiecePlaceEnum;
import com.nightonke.boommenu.Util;
import com.ussol.employeetracker.helpers.BadgeDrawableHelper;
import com.ussol.employeetracker.helpers.ConvertCursorToListString;
import com.ussol.employeetracker.helpers.DatabaseAdapter;
import com.ussol.employeetracker.helpers.GetSearchItemSetting;
import com.ussol.employeetracker.helpers.IconContextMenu;
import com.ussol.employeetracker.helpers.InitDatabase;
import com.ussol.employeetracker.helpers.LazyAdapter;
import com.ussol.employeetracker.models.*;
import com.ussol.employeetracker.passcodelock.PasscodeManagePasswordActivity;
import com.ussol.employeetracker.utils.ShowAlertDialog;
import com.ussol.employeetracker.widget.PullToRefreshListView;
import com.ussol.employeetracker.widget.PullToRefreshListView.OnRefreshDoneListener;

import android.R.integer;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

/**
 *
 * @author hoa-nx
 *
 */
public class ListUserMainActivity extends AppCompatActivity  implements OnClickListener, SearchView.OnQueryTextListener, SearchView.OnCloseListener {
	//tag
	private final static String TAG = ListUserMainActivity.class.getName();
	private static final int MENU_ITEM_ADDCOPY_ACTION = 1;
	private static final int MENU_ITEM_DETAIL_ACTION = 2;
	private static final int MENU_ITEM_EDIT_ACTION = 3;
	private static final int MENU_ITEM_DELETE_ACTION = 4;
	private static final int MENU_ITEM_HISTORY_ACTION = 5;
	private static final int MENU_ITEM_CALL_ACTION = 6;
	private static final int CONTEXT_MENU_ID = 410;
	public static final String KEY_TYPE = "type";
	//DB adapter
	DatabaseAdapter mDatabaseAdapter;
	ConvertCursorToListString mConvertCursorToListString;
	/** List  data  */
	private List<User> list;
	private ListView listview;
	private LazyAdapter adapter = null;
	private IconContextMenu iconContextMenu = null;
	private User info = null;
	private int positionClicked;
	ProgressDialog progressBar;
	private ImageButton btnSelectAll, btnDeselectAll, btnDelete, btnUnDelete, btnSendMail, btnSendSms;
	private ImageButton btnSearchItem, btnSortList, btnSortAsc, btnSortDesc, btnSearch, btnSearchCancel;
	private TextView txtUserTitle;
	private EditText txtFilterText ;
	//private ImageView btnSortAsc , btnSortDesc;
	private SharedPreferences settings;
	private SearchView search;
	private LinearLayout layoutSearch, layoutMenu;
	private PullToRefreshListView mListView;
	private View viewLayout = null;
	private static int[] imageResources = new int[]{R.drawable.ic_account_card_details_black_24dp, R.drawable.ic_sort_black_24dp,
			R.drawable.ic_settings_black_24dp};
	private BoomMenuButton bmb;

	private FloatingActionMenu materialDesignFAM;
	private FloatingActionButton fab_menu_list_user_send_sms, fab_menu_list_user_send_email, fab_menu_list_user_checkall;
	private FloatingActionButton fab_menu_list_user_uncheckall, fab_menu_list_user_delete, fab_menu_list_user_undelete;

	private  LayerDrawable iconBadgeSearchSetting;
	/**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
	 * onCreate
	 *
	 ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
	@Override
	public void onCreate(Bundle savedInstanceState) {
		try {
			super.onCreate(savedInstanceState);
			requestWindowFeature(Window.FEATURE_NO_TITLE);
    		 	/*Boolean customTitleSupported = requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);*/
			setContentView(R.layout.activity_user_list);

			//boom menu
			//bmbMainMenuConfig();
			//FAM
			materialFAM();
			/*if (customTitleSupported) {
				getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,R.layout.user_cus_title);
			}*/
			//create DB
			mDatabaseAdapter = new DatabaseAdapter(this);

			SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
			search = (SearchView) findViewById(R.id.search);
			//layoutMenu = (LinearLayout) findViewById(R.id.fdLinearLayoutMenu);
			layoutSearch = (LinearLayout) findViewById(R.id.fdLinearLayoutSearch);
			search.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
			search.setIconifiedByDefault(false);
			search.setOnQueryTextListener(this);
			search.setOnCloseListener(this);

			info = new User();
			/** get cac control tren man hinh */
			getControl();
			/** gán các sự kiện cho các control */
			settingListener();
			/** get list user */
			list = getListUser("");
			/** hiển thị data*/
			bindData();

			/** pull down to refresh */
			/**
			 mListView = (PullToRefreshListView) getListView();
			 mListView.setupPullDownRefresher(new Runnable() {
			@Override public void run() {
			try {
			Thread.sleep(2000);
			} catch (InterruptedException e) {
			}
			//mListItems.addFirst("Added after refresh..."
			//        + System.currentTimeMillis());
			}
			});
			 mListView.setOnRefreshDoneListener(new OnRefreshDoneListener() {
			@Override public void onRefreshDone() {
			bindData();
			mListView.invalidateViews();
			}
			});
			 bindData();
			 */
			/**init the menu*/
			Resources res = getResources();
			iconContextMenu = new IconContextMenu(this, CONTEXT_MENU_ID);
			iconContextMenu.addItem(res, R.string.maddcopy, R.drawable.addcopy, MENU_ITEM_ADDCOPY_ACTION);
			//iconContextMenu.addItem(res, R.string.mdetail, R.drawable.detail, MENU_ITEM_DETAIL_ACTION);
			iconContextMenu.addItem(res, R.string.medit, R.drawable.edit, MENU_ITEM_EDIT_ACTION);
			iconContextMenu.addItem(res, R.string.mshowhistory, R.drawable.history_position, MENU_ITEM_HISTORY_ACTION);
			iconContextMenu.addItem(res, R.string.mdelete, R.drawable.delete, MENU_ITEM_DELETE_ACTION);
			iconContextMenu.addItem(res, R.string.mcall, R.drawable.call, MENU_ITEM_CALL_ACTION);

			//set onclick listener for context menu
			iconContextMenu.setOnClickListener(new IconContextMenu.IconContextMenuOnClickListener() {
				@Override
				public void onClick(int menuId) {
					switch (menuId) {
						case MENU_ITEM_ADDCOPY_ACTION:
							/** tạo mới từ user có sẵn */
							/**  chỉnh sửa */
							Intent intentCopy = new Intent(getApplicationContext(), EditUserMainActivity.class);
							Bundle bundleCopy = new Bundle();
							/**lấy code của user*/
							info.code = 0;
							info.img_fullpath = "";
							info.full_name = "";
							info.mobile = "";
							info.email = "";
							info.address = "";
							bundleCopy.putInt(DatabaseAdapter.KEY_CODE, info.code);
							bundleCopy.putParcelable(MasterConstants.TAB_USER_TAG, info);
							/**gán vào bundle để gửi cùng với intent */
							intentCopy.putExtras(bundleCopy);

							/**khởi tạo activity dùng để edit  */
							startActivityForResult(intentCopy, MasterConstants.CALL_USER_ACTIVITY_CODE);

							//Toast.makeText(getApplicationContext(), "You've clicked on menu item 1", 1000).show();
							break;
						case MENU_ITEM_DETAIL_ACTION:

							//Toast.makeText(getApplicationContext(), "You've clicked on menu item 2", 1000).show();
							break;
						case MENU_ITEM_EDIT_ACTION:
							/**  chỉnh sửa */
							Intent intent = new Intent(getApplicationContext(), EditUserMainActivity.class);
							Bundle bundle = new Bundle();
							/**lấy code của user*/
							bundle.putInt(DatabaseAdapter.KEY_CODE, info.code);
							bundle.putParcelable(MasterConstants.TAB_USER_TAG, info);
							bundle.putString(MasterConstants.LISTVIEW_CURRENT_POSITION, String.valueOf(positionClicked));
							/**gán vào bundle để gửi cùng với intent */
							intent.putExtras(bundle);

							/**khởi tạo activity dùng để edit  */
							startActivityForResult(intent, MasterConstants.CALL_USER_ACTIVITY_CODE);

							//Toast.makeText(getApplicationContext(), "You've clicked on menu item 3", 1000).show();
							break;
						case MENU_ITEM_HISTORY_ACTION:
							/** xem thong tin lich su */
							Intent intentHis = new Intent(getApplicationContext(), ExpandableListUserHisActivity.class);
							Bundle bundleHis = new Bundle();
							/**lấy code của user*/
							bundleHis.putInt(DatabaseAdapter.KEY_CODE, info.code);
							bundleHis.putParcelable(MasterConstants.TAB_USER_TAG, info);
							/**gán vào bundle để gửi cùng với intent */
							intentHis.putExtras(bundleHis);

							/**khởi tạo activity dùng để edit  */
							startActivity(intentHis);

							break;

						case MENU_ITEM_DELETE_ACTION:
							/** xóa*/
							deleteUser(info.code);
							setUserTitleBar(String.valueOf(adapter.getCount()));
							setBadgeCount(getApplicationContext(), iconBadgeSearchSetting, String.valueOf(adapter.getCount()));
							//Toast.makeText(getApplicationContext(), "You've clicked on menu item 4", 1000).show();
							break;
						case MENU_ITEM_CALL_ACTION:
							if (info != null) {
								if (info.mobile != null && !info.mobile.equals("")) {
									Intent dialIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + info.mobile));
									dialIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
									if (ActivityCompat.checkSelfPermission(ListUserMainActivity.this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
										// TODO: Consider calling
										//    ActivityCompat#requestPermissions
										// here to request the missing permissions, and then overriding
										//   public void onRequestPermissionsResult(int requestCode, String[] permissions,
										//                                          int[] grantResults)
										// to handle the case where the user grants the permission. See the documentation
										// for ActivityCompat#requestPermissions for more details.
										return;
									}
									startActivity(dialIntent);
								}
    	                	}
    	                	 
    	                	break;
    	                }
    	            }
    	        });

			/** XU LY CHO TEXT SEARCH */
			txtFilterText.addTextChangedListener(filterTextWatcher);
			/** xu ly kbi click vao list thi se an keyboarc*/
			txtFilterText.setOnClickListener(filterTextClickListener);

			listview.setOnTouchListener(new View.OnTouchListener() {
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					setHideKeyboard();
					txtFilterText.setCursorVisible(false);
					return false;
				}
			});
			txtFilterText.setCursorVisible(false);
			setHideKeyboard();


		}catch (Exception e){
    		Log.v(TAG,e.getMessage());
    	}

    }
    
    @Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		//Neu de 2 dong ben duoi thi khi tra ket qua tu man hinh edit user se dong activity va tro ve login
		//setResult(MasterConstants.RESULT_CLOSE_ALL);
		//this.finish();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		setBadgeCount(this, iconBadgeSearchSetting, String.valueOf(adapter.getCount()));
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		//Neu de 2 dong ben duoi thi khi tra ket qua tu man hinh edit user se dong activity va tro ve login
		//setResult(MasterConstants.RESULT_CLOSE_ALL);
		//this.finish();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		txtFilterText.setText("");
	}
    @Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();

		/*if(!EmployeeTrackerApplicationLifecycleCallbacks.isApplicationInForeground())
		{
			Intent passCodeInt = new Intent(getBaseContext(), PasscodeManagePasswordActivity.class);
			Bundle bundlePassCode = new Bundle();
			bundlePassCode.putInt(KEY_TYPE, 0);
			passCodeInt.putExtras(bundlePassCode);
			
			startActivity(passCodeInt);
		}*/
	}
	/** xu ly chuc nang search */
	private TextWatcher filterTextWatcher = new TextWatcher() {

		public void afterTextChanged(Editable s) {

		}

		public void beforeTextChanged(CharSequence s, int start, int count,
									  int after) {

		}

		public void onTextChanged(CharSequence s, int start, int before,
								  int count) {
			adapter.getFilter().filter(s);
		}

	};

	private OnClickListener filterTextClickListener = new OnClickListener()
	{
		public void onClick(View v)
		{
			if (v.getId() == txtFilterText.getId())
			{
				txtFilterText.setCursorVisible(true);
			}
		}
	};

	/**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
	 * onCreateOptionsMenu
	 *
	 ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.list_user_menu, menu);

		MenuItem itemFilterSetting = menu.findItem(R.id.menu_list_user_item_settings);
		//iconBadgeSearchSetting = (LayerDrawable) itemFilterSetting.getIcon(); //truong hop icon la dang drawable
		BitmapDrawable iconBitmap = (BitmapDrawable) itemFilterSetting.getIcon(); //truong hop khong phai nhu tren
		iconBadgeSearchSetting = new LayerDrawable(new Drawable [] { iconBitmap });

		setBadgeCount(this, iconBadgeSearchSetting, String.valueOf(adapter.getCount()));
		/*
		MenuItem searchItem = menu.findItem(R.id.menu_list_user_item_filter);
		SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
		//*** setOnQueryTextFocusChangeListener ***
		searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {

			}
		});

		searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

			@Override
			public boolean onQueryTextSubmit(String query) {

				return false;
			}

			@Override
			public boolean onQueryTextChange(String searchQuery) {
				adapter.getFilter().filter(searchQuery);
				//listview.invalidate();
				return true;
			}
		});

		MenuItemCompat.setOnActionExpandListener(searchItem, new MenuItemCompat.OnActionExpandListener() {
			@Override
			public boolean onMenuItemActionCollapse(MenuItem item) {
				// Do something when collapsed
				return true;  // Return true to collapse action view
			}

			@Override
			public boolean onMenuItemActionExpand(MenuItem item) {
				// Do something when expanded
				return true;  // Return true to expand action view
			}
		});
		*/
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
			case R.id.menu_list_user_item_settings:
				Intent intent = new Intent(this, SearchItemMainActivity.class);
				startActivityForResult(intent,MasterConstants.CALL_SEARCH_ITEM_ACTIVITY_CODE);
				return true;
			case R.id.menu_list_user_item_setting_sort:
				Intent intSort = new Intent(this, DragNDropListActivity.class);
				startActivityForResult(intSort , MasterConstants.CALL_SORT_ITEM_ACTIVITY_CODE);
				return true;
			case R.id.menu_list_user_item_config:
				Intent intConfig = new Intent(this, SystemConfigPreferencesActivity.class);
				startActivityForResult(intConfig , MasterConstants.CALL_CONFIG_ITEM_ACTIVITY_CODE);
				return true;

			case R.id.menu_list_user_item_sort_asc:
				/** đọc thông tin lưu trữ tại xml */
				settings = getSharedPreferences(MasterConstants.PRE_SORT_FILE, Context.MODE_PRIVATE);
				storeCurrentSortByInSharedPreferences(settings, " ASC ");
				/** get list user */
				list = getListUser("");
				/** hiển thị data*/
				bindData();
				return true;
			case R.id.menu_list_user_item_sort_des:
				/** đọc thông tin lưu trữ tại xml */
				settings = getSharedPreferences(MasterConstants.PRE_SORT_FILE, Context.MODE_PRIVATE);
				storeCurrentSortByInSharedPreferences(settings, " DESC ");
				/** get list user */
				list = getListUser("");
				/** hiển thị data*/
				bindData();
				return true;

			default:
				return super.onOptionsItemSelected(item);
		}
	}

	/**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * onActivityResult
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch(requestCode){
			case MasterConstants.CALL_USER_ACTIVITY_CODE:
				if (resultCode==RESULT_OK){
					txtFilterText.setText("");
	    	        /** get list user */
	    			list = getListUser("");
	    			/** hiển thị data*/
	    			bindData();
	    			/** di chuyen toi vi tri cu **/
	    			//listview.smoothScrollToPosition(Integer.valueOf(positionClicked));
	 	    			
	    			/*
	    			String currentPosition = data.getStringExtra(MasterConstants.LISTVIEW_CURRENT_POSITION);
	    			if(currentPosition.equals("")){
	    				
	    			}else{
	    				listview.smoothScrollToPosition(Integer.valueOf(positionClicked));
	    			}
	    			*/
	    			/** di chuyen toi item cu**/
	    			//final ListView listview = (ListView) findViewById(R.id.list);
	    	        //int h1 = listview.getHeight();
	    			//int h2 = viewLayout.getHeight();
	    			
	    			//listview.smoothScrollToPositionFromTop(positionClicked, h1/2 - h2/2, 100);
					if(positionClicked>list.size()){
						listview.setSelection(list.size());
					}else{
						listview.setSelection(positionClicked);
					}

				}
			case MasterConstants.CALL_SEARCH_ITEM_ACTIVITY_CODE:
				if (resultCode==RESULT_OK){

	    	        /** get list user */
	    			list = getListUser("");
	    			/** hiển thị data*/
	    			bindData();
	    			/** di chuyen toi item cu**/
	    			//final ListView listview = (ListView) findViewById(R.id.list);
	    	        //int h1 = listview.getHeight();
	    			//int h2 = viewLayout.getHeight();
	    			
	    			//listview.smoothScrollToPositionFromTop(positionClicked, h1/2 - h2/2, 100);
					if(positionClicked>list.size()){
						listview.setSelection(list.size());
					}else{
						listview.setSelection(positionClicked);
					}
				}
				break;
			case MasterConstants.CALL_SORT_ITEM_ACTIVITY_CODE:
				if (resultCode==RESULT_OK){
	    	        /** get list user */
	    			list = getListUser("");
	    			/** hiển thị data*/
	    			bindData();
	    			/** di chuyen toi item cu**/
	    			//final ListView listview = (ListView) findViewById(R.id.list);
	    	        //int h1 = listview.getHeight();
	    			//int h2 = viewLayout.getHeight();
	    			
	    			//listview.smoothScrollToPositionFromTop(positionClicked, h1/2 - h2/2, 100);
					if(positionClicked>list.size()){
						listview.setSelection(list.size());
					}else{
						listview.setSelection(positionClicked);
					}
				}
				break;
			case MasterConstants.CALL_CONFIG_ITEM_ACTIVITY_CODE:
				if (resultCode==RESULT_OK){
					/** get list user */
					list = getListUser("");
					/** hiển thị data*/
					bindData();
					/** di chuyen toi item cu**/
					//final ListView listview = (ListView) findViewById(R.id.list);
					//int h1 = listview.getHeight();
					//int h2 = viewLayout.getHeight();

					//listview.smoothScrollToPositionFromTop(positionClicked, h1/2 - h2/2, 100);
					if(positionClicked>list.size()){
						listview.setSelection(list.size());
					}else{
						listview.setSelection(positionClicked);
					}
				}
				break;
		}
	}	
    /**
     * create context menu
     */
    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == CONTEXT_MENU_ID) {
            return iconContextMenu.createMenu("");
        }
        return super.onCreateDialog(id);
    }
    /**
     * list item long click handler
     * used to show the context menu
     */
    private OnItemLongClickListener itemLongClickHandler = new OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView parent, View view,
                    int position, long id) {
        	/** lưu vị trí đã chọn */
        	positionClicked = position;
        	/** get data tương ứng với item đã chọn */
        	View v = view;
        	viewLayout = view;
        	TextView _id = (TextView)v.findViewById(R.id.txtListUserCode);
        	List<User> listUser;
        	LazyAdapter adt =(LazyAdapter)parent.getAdapter();
        	/** lưu đối tượng user */
        	User tmpUser = (User) adt.getItem(position);
        	
        	info= tmpUser.clone();
        
            showDialog(CONTEXT_MENU_ID);
            return true;
        }
    };
    
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * binding data cho list
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
	public void bindData(){
		final ListView listview = (ListView) findViewById(R.id.list);
        listview.setScrollingCacheEnabled(true);
        
        adapter = new LazyAdapter(this, list);
        
        listview.setAdapter(adapter);
        //ADD 2015.08.29
        //listview.setFastScrollEnabled(true);
        
        listview.setOnItemLongClickListener(itemLongClickHandler);
        /** hiển thị số nhân viên trong lis */
        setUserTitleBar(String.valueOf(adapter.getCount()));
		setBadgeCount(this, iconBadgeSearchSetting, String.valueOf(adapter.getCount()));
        //listview = listview;
        
        /** di chuyen toi item cu**/
        /*int h1 = listview.getHeight();
		int h2 = viewLayout.getHeight();
		
		listview.smoothScrollToPositionFromTop(positionClicked, h1/2 - h2/2, 0);*/
		
	}
	
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * get data cho list
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
	public List<User> getListUser(String xWhere){
		/** chuyển đổi từ Cursor thành List */
		mConvertCursorToListString = new ConvertCursorToListString(this);
		list = mConvertCursorToListString.getUserList(xWhere);
		return list;
	}
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * xóa user dựa vào code user
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
	public void deleteUser(final int code){
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
		/** Setting Dialog Title */
        alertDialog.setTitle(getResources().getString(R.string.titleDelete));

        /** Setting Dialog Message */
        alertDialog.setMessage("Bạn có muốn xóa nhân viên này không?");
        
        /** Setting Icon to Dialog*/
        alertDialog.setIcon(R.drawable.ic_button_delete);
        /** Setting Positive "Yes" Button */
        alertDialog.setPositiveButton(getResources().getText(R.string.titleYes), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
            	/** xóa user đã chọn */
            	mDatabaseAdapter.open();
            	/** kiểm tra xem trạng thái của delete user setting tại màn hình setting điều kiện*/
            	if (GetSearchItemSetting.getIsDeleted()){
            		/** xoa khỏi db*/
            		mDatabaseAdapter.deletePernamentUserByCode(code);
            	}else{
            		mDatabaseAdapter.deleteUserByCode(code);
            	}
        		mDatabaseAdapter.close();
        		/** remove ra khỏi list hiện tại */
            	adapter.remove(positionClicked);
            	adapter.notifyDataSetChanged();
            	 /** hiển thị số nhân viên trong lis */
                setUserTitleBar(String.valueOf(adapter.getCount()));
				setBadgeCount(getApplicationContext(), iconBadgeSearchSetting, String.valueOf(adapter.getCount()));
            }
        });
 
        /** Setting Negative "NO" Button */
        alertDialog.setNegativeButton(getResources().getText(R.string.titleNo), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            dialog.cancel();
            }
        });
 
        /** Showing Alert Message */
        alertDialog.show();
        
	}
	/**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * xóa user dựa vào code user
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
	public void deleteUser(final String listCode){
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
		/** Setting Dialog Title */
        alertDialog.setTitle(getResources().getString(R.string.titleDelete));

        /** Setting Dialog Message */
        alertDialog.setMessage("Bạn có muốn xóa các nhân viên này không?");
        
        /** Setting Icon to Dialog*/
        alertDialog.setIcon(R.drawable.ic_button_delete);
        /** Setting Positive "Yes" Button */
        alertDialog.setPositiveButton(getResources().getText(R.string.titleYes), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
            	/** xóa user đã chọn */
            	mDatabaseAdapter.open();
            	/** kiểm tra xem trạng thái của delete user setting tại màn hình setting điều kiện*/
            	if (GetSearchItemSetting.getIsDeleted()){
            		/** xoa khỏi db*/
            		mDatabaseAdapter.deletePernamentUserByCode(listCode);
            	}else{
            		mDatabaseAdapter.deleteUserByCode(listCode);
            	}
        		mDatabaseAdapter.close();
        		/** remove ra khỏi list hiện tại */
            	adapter.removeAllCheckedItem();
            	adapter.notifyDataSetChanged();
            }
        });
 
        /** Setting Negative "NO" Button */
        alertDialog.setNegativeButton(getResources().getText(R.string.titleNo), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            dialog.cancel();
            }
        });
 
        /** Showing Alert Message */
        alertDialog.show();
        
	}
	
	/**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * xóa user dựa vào code user
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
	public void unDeleteUser(final String listCode){
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
		/** Setting Dialog Title */
        alertDialog.setTitle(getResources().getString(R.string.titleUnDelete));

        /** Setting Dialog Message */
        alertDialog.setMessage("Bạn có muốn phục hồi (xóa→chưa xóa)nhân viên này không?");
        
        /** Setting Icon to Dialog*/
        alertDialog.setIcon(R.drawable.undelete);
        /** Setting Positive "Yes" Button */
        alertDialog.setPositiveButton(getResources().getText(R.string.titleYes), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
            	/** xóa user đã chọn */
            	mDatabaseAdapter.open();
        		mDatabaseAdapter.unDeleteUserByCode(listCode);
        		mDatabaseAdapter.close();
        		/** remove ra khỏi list hiện tại */
            	adapter.removeAllCheckedItem();
            	adapter.notifyDataSetChanged();
            	 /** hiển thị số nhân viên trong lis */
                setUserTitleBar(String.valueOf(adapter.getCount()));
				setBadgeCount(getApplicationContext(), iconBadgeSearchSetting, String.valueOf(adapter.getCount()));
            }
        });
 
        /** Setting Negative "NO" Button */
        alertDialog.setNegativeButton(getResources().getText(R.string.titleNo), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            dialog.cancel();
            }
        });
 
        /** Showing Alert Message */
        alertDialog.show();
        
	}
	/**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
	 * onClick
	 * 
	 ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
	@Override
	public void onClick(View v) {
		/** phân nhánh xử lý theo từng button*/
		switch (v.getId()) {
			case R.id.btnSearchItem:
				Intent intent = new Intent(this, SearchItemMainActivity.class);
				startActivityForResult(intent,MasterConstants.CALL_SEARCH_ITEM_ACTIVITY_CODE);
				break;
			
			case R.id.btnSortList:
				Intent intSort = new Intent(this, DragNDropListActivity.class);
				startActivityForResult(intSort , MasterConstants.CALL_SORT_ITEM_ACTIVITY_CODE);
				break;
			case R.id.btnSortAsc:
				/** đọc thông tin lưu trữ tại xml */
				settings = getSharedPreferences(MasterConstants.PRE_SORT_FILE, Context.MODE_PRIVATE);
				storeCurrentSortByInSharedPreferences(settings, " ASC ");
				/** get list user */
    			list = getListUser("");
    			/** hiển thị data*/
    			bindData();
				break;
				
			case R.id.btnSortDesc:
				/** đọc thông tin lưu trữ tại xml */
				settings = getSharedPreferences(MasterConstants.PRE_SORT_FILE, Context.MODE_PRIVATE);
				storeCurrentSortByInSharedPreferences(settings, " DESC ");
				/** get list user */
    			list = getListUser("");
    			/** hiển thị data*/
    			bindData();
				break;
			case R.id.btnSearch:
				setMenuShowHide(layoutMenu, View.GONE);
				setSearchViewShowHide(layoutSearch, View.VISIBLE);
				break;
			case R.id.btnSearchCancel:
				adapter.getFilter().filter("");
				setSearchViewShowHide(layoutSearch, View.GONE);
				setMenuShowHide(layoutMenu, View.VISIBLE);
				setHideKeyboard();
				break;
			case R.id.btnSelectAll:
				setCheckAll();
				break;
			case R.id.btnSchedule_button:
				setUnCheckAll();
				break;
			case R.id.btnCancel_button:
				/** get các item được gán check */
				List<User> listUser =adapter.getListViewData();
				int index=0;
				String listCode="";
				for(User usr : listUser){
					if(usr.isselected){
						if(index==0){
							listCode= String.valueOf(usr.code);
						}else{
							listCode= listCode + "," + String.valueOf(usr.code);
						}
						index++;
					}
				}
				if(listCode.trim().isEmpty()){
					/** không có dòng nào được chọn*/
					ShowAlertDialog.showTitleAndMessageDialog(this, "Xóa nhân viên", getResources().getString(R.string.titleNoItemSelected));
					break;
				}
				/** Xóa các nhân viên đã chọn */
				deleteUser(listCode);
				/** setting so nhan vien */
				setUserTitleBar(String.valueOf(adapter.getCount()));
				setBadgeCount(this, iconBadgeSearchSetting, String.valueOf(adapter.getCount()));
				break;
			case R.id.btnUnDeleteAll:
            	/** kiểm tra xem trạng thái của delete user setting tại màn hình setting điều kiện*/
            	if (GetSearchItemSetting.getIsDeleted()){
            	}else{
            		ShowAlertDialog.showTitleAndMessageDialog(this
							, getResources().getString(R.string.titleUnDelete)
							,"Vì các nhân viên đang hiển thị là chưa bị xóa nên không thể phục hồi.");
            		break;

            	}
				/** get các item được gán check */
				List<User> listUserUndelete =adapter.getListViewData();
				int indexUndelete=0;
				String listCodeUndelete="";
				for(User usr : listUserUndelete){
					if(usr.isselected){
						if(indexUndelete==0){
							listCodeUndelete= String.valueOf(usr.code);
						}else{
							listCodeUndelete= listCodeUndelete + "," + String.valueOf(usr.code);
						}
						indexUndelete++;
					}
				}
				if(listCodeUndelete.trim().isEmpty()){
					/** không có dòng nào được chọn*/
					ShowAlertDialog.showTitleAndMessageDialog(this, getResources().getString(R.string.titleUnDelete), getResources().getString(R.string.titleNoItemSelected));
					break;
				}
				/** Xóa các nhân viên đã chọn */
				unDeleteUser(listCodeUndelete);
				/** setting so nhan vien */
				setUserTitleBar(String.valueOf(adapter.getCount()));
				setBadgeCount(this, iconBadgeSearchSetting, String.valueOf(adapter.getCount()));
				break;
			case R.id.btnBack:
				/** get các item được gán check */
				List<User> listUserSendSms =adapter.getListViewData();
				ArrayList<User> listUserChecked=new ArrayList<User>();
				User[] arrUserChecked;
				int indexSendSms=0;
				String listCodeSendSms="";
				for(User usr : listUserSendSms){
					if(usr.isselected){
						listUserChecked.add(usr);
						if(indexSendSms==0){
							listCodeSendSms= String.valueOf(usr.code);
						}else{
							listCodeSendSms= listCodeSendSms + "," + String.valueOf(usr.code);
						}
						indexSendSms++;
					}
				}
				if(listCodeSendSms.trim().isEmpty()){
					/** không có dòng nào được chọn*/
					ShowAlertDialog.showTitleAndMessageDialog(this, getResources().getString(R.string.send_sms_titleInfo), getResources().getString(R.string.titleNoItemSelected));
					break;
				}
				arrUserChecked=new User[listUserChecked.size()];
				
				for(int i=0;i<listUserChecked.size();i++){
					arrUserChecked[i]= listUserChecked.get(i);
				}
				//listUserChecked.toArray(arrUserChecked);
				/**  chỉnh sửa */
				Intent intentSendSms = new Intent(getApplicationContext(), SendSmsActivity.class);
				Bundle bundle = new Bundle();
				/**lấy code của user*/
				//bundle.putParcelableArray(MasterConstants.SEND_SMS_TAG, arrUserChecked);
				bundle.putParcelableArrayList(MasterConstants.SEND_SMS_TAG, listUserChecked);
				/**gán vào bundle để gửi cùng với intent */
				intentSendSms.putExtras(bundle);
				
				/**khởi tạo activity dùng để edit  */
				startActivity(intentSendSms);
				
				break;
			case R.id.btnSendMail:
				
				break;
		}
	}
	
	/**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * get các control trên màn hình
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    private void getControl(){
		btnSelectAll= (ImageButton)findViewById(R.id.btnSelectAll);
		btnDeselectAll= (ImageButton)findViewById(R.id.btnSchedule_button);
		btnDelete= (ImageButton)findViewById(R.id.btnCancel_button);
		btnUnDelete= (ImageButton)findViewById(R.id.btnUnDeleteAll);
		btnSendMail= (ImageButton)findViewById(R.id.btnSendMail);
		btnSendSms= (ImageButton)findViewById(R.id.btnBack);
		
		txtUserTitle = (TextView) findViewById(R.id.txtUserTitle);
		txtFilterText = (EditText) findViewById(R.id.txtFilterText);
		btnSortAsc =(ImageButton)findViewById(R.id.btnSortAsc);
		btnSortDesc=(ImageButton)findViewById(R.id.btnSortDesc);
		
		btnSearchItem = (ImageButton) findViewById(R.id.btnSearchItem);
		btnSortList = (ImageButton) findViewById(R.id.btnSortList);
		btnSearch= (ImageButton) findViewById(R.id.btnSearch);
		btnSearchCancel= (ImageButton) findViewById(R.id.btnSearchCancel);
		listview = (ListView) findViewById(R.id.list);
		
    }

    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * gán sự kiện cho các control
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    private void settingListener(){

    	btnSelectAll.setOnClickListener(this);
    	btnDeselectAll.setOnClickListener(this);
    	btnDelete.setOnClickListener(this);
    	btnUnDelete.setOnClickListener(this);
    	btnSendMail.setOnClickListener(this);
    	btnSendSms.setOnClickListener(this);
    	
    	btnSearchItem.setOnClickListener(this);
    	btnSortList.setOnClickListener(this);
    	
    	btnSortAsc.setOnClickListener(this);
    	btnSortDesc.setOnClickListener(this);
    	btnSearch.setOnClickListener(this);
    	btnSearchCancel.setOnClickListener(this);
    }
    
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * luu thông tin trình tự sort 
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/   
    public void storeCurrentSortByInSharedPreferences(SharedPreferences pre, String xSortAscOrDesc){
    	Editor edit = pre.edit();
    	edit.putString("pre_SortBy",xSortAscOrDesc );
    	edit.commit();
    }
    
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * 
     * set tri cho title bar cua man hinh dang ky user
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    public void setUserTitleBar(String value){
    	if(value.equals("") || value==null){
    		txtUserTitle.setText(getResources().getString(R.string.userTitleList) + "(0)");
    	}else{
    		txtUserTitle.setText(getResources().getString(R.string.userTitleList) + "(" + value + ")");
    	}
		//title for activity
		if(value.equals("") || value==null){
			setTitle(getResources().getString(R.string.userTitleList) + "(0)");
		}else{
			setTitle(getResources().getString(R.string.userTitleList) + "(" + value + ")");
		}

    }
    
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * 
     * gan check cho cac item tren list
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
	private void setCheckAll(){

		for(int i=0; i < listview.getChildCount(); i++){
		    RelativeLayout itemLayout = (RelativeLayout)listview.getChildAt(i);
		    CheckBox cb = (CheckBox)itemLayout.findViewById(R.id.chkListUserSelect);
		    cb.setChecked(true);
		}
	
		adapter.setCheckAll(true);

	}
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * 
     *  bo check cho cac item tren list
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
	private void setUnCheckAll(){

		for(int i=0; i < listview.getChildCount(); i++){
		    RelativeLayout itemLayout = (RelativeLayout)listview.getChildAt(i);
		    CheckBox cb = (CheckBox)itemLayout.findViewById(R.id.chkListUserSelect);
		    cb.setChecked(false);
		}

		adapter.setCheckAll(false);
	}

	/**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * ẩn/hiển thị search view
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
	private void setSearchViewShowHide(View v , int showHide) {
		layoutSearch.setVisibility(showHide);
	}
	/**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * ẩn/hiển thị menu
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
	private void setMenuShowHide(View v , int showHide) {
		layoutMenu.setVisibility(showHide);
	}
	/**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * ẩn bàn phím
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
	private void setHideKeyboard(){
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(txtFilterText.getWindowToken(), 0);
	}
	
	/**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * nhận về trạng thái của SearchView
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
	private int getSearchViewShowHide(View v ) {
		return layoutSearch.getVisibility();
	}
	/**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * nhận về trạng thái của menu
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
	private int getMenuShowHide(View v ) {
		return layoutMenu.getVisibility();
	}
	
	@Override
	public boolean onClose() {
		
		return false;
	}
	@Override
	public boolean onQueryTextChange(String newText) {
		// TODO Auto-generated method stub
		adapter.getFilter().filter(newText);
		return false;
	}
	@Override
	public boolean onQueryTextSubmit(String query) {
		// TODO Auto-generated method stub
		return false;
	}

	/*protected void bmbMainMenuConfig(){
		bmb = (BoomMenuButton) findViewById(R.id.bmb);
		assert bmb != null;
		bmb.setButtonEnum(ButtonEnum.SimpleCircle);
		bmb.setPiecePlaceEnum(PiecePlaceEnum.DOT_3_1);
		bmb.setButtonPlaceEnum(ButtonPlaceEnum.SC_3_1);

		for (int i = 0; i < bmb.getPiecePlaceEnum().pieceNumber(); i++)
		{
			SimpleCircleButton.Builder builder = new SimpleCircleButton.Builder()
					.normalImageRes(getImageResource())
					.listener(new OnBMClickListener() {
						@Override
						public void onBoomButtonClick(int index) {
							// When the boom-button corresponding this builder is clicked.
							switch (index) {
								case 0:
									Intent intent = new Intent(ListUserMainActivity.this, SearchItemMainActivity.class);
									startActivityForResult(intent, MasterConstants.CALL_SEARCH_ITEM_ACTIVITY_CODE);
									break;

								case 1:
									Intent intSort = new Intent(ListUserMainActivity.this, DragNDropListActivity.class);
									startActivityForResult(intSort, MasterConstants.CALL_SORT_ITEM_ACTIVITY_CODE);
									break;
								case 2:
									Intent intConfig = new Intent(ListUserMainActivity.this, SystemConfigPreferencesActivity.class);
									startActivityForResult(intConfig , MasterConstants.CALL_CONFIG_ITEM_ACTIVITY_CODE);
									break;
							}
						}
					})
					// Whether the image-view should rotate.
					.rotateImage(false)

					// Whether the boom-button should have a shadow effect.
					.shadowEffect(false)

					// Set the horizontal shadow-offset of the boom-button.
					.shadowOffsetX(20)

					// Set the vertical shadow-offset of the boom-button.
					.shadowOffsetY(0)

					// Set the radius of shadow of the boom-button.
					.shadowRadius(Util.dp2px(20))

					// Set the color of the shadow of boom-button.
					//.shadowColor(Color.parseColor("#c5e26d"))

					// Set the image resource when boom-button is at normal-state.
					//.normalImageRes(R.drawable.jellyfish)

					// Set the image drawable when boom-button is at normal-state.
					//.normalImageDrawable(getResources().getDrawable(R.drawable.jellyfish, null))

					// Set the image resource when boom-button is at highlighted-state.
					//.highlightedImageRes(R.drawable.bat)

					// Set the image drawable when boom-button is at highlighted-state.
					//.highlightedImageDrawable(getResources().getDrawable(R.drawable.bat, null))

					// Set the image resource when boom-button is at unable-state.
					//.unableImageRes(R.drawable.butterfly)

					// Set the image drawable when boom-button is at unable-state.
					//.unableImageDrawable(getResources().getDrawable(R.drawable.butterfly, null))

					// Set the rect of image.
					// By this method, you can set the position and size of the image-view in boom-button.
					// For example, builder.imageRect(new Rect(0, 50, 100, 100)) will make the
					// image-view's size to be 100 * 50 and margin-top to be 50 pixel.
					//.imageRect(new Rect(Util.dp2px(10), Util.dp2px(10), Util.dp2px(70), Util.dp2px(70)))

					// Set the padding of image.
					// By this method, you can control the padding in the image-view.
					// For instance, builder.imagePadding(new Rect(10, 10, 10, 10)) will make the
					// image-view content 10-pixel padding to itself.
					//.imagePadding(new Rect(0, 0, 0, 0))

					// Whether the boom-button should have a ripple effect.
					.rippleEffect(true)

					// The color of boom-button when it is at normal-state.
					//.normalColor(Color.RED)

					// The color of boom-button when it is at highlighted-state.
					//.highlightedColor(Color.BLUE)

					// The color of boom-button when it is at unable-state.
					//.unableColor(Color.BLACK)

					// Whether the boom-button is unable, default value is false.
					.unable(false)

					// The radius of boom-button, in pixel.
					.buttonRadius(Util.dp2px(40));

			bmb.addBuilder(builder);
		}
	}
*/
	private static int imageResourceIndex = 0;

	static int getImageResource() {
		if (imageResourceIndex >= imageResources.length) imageResourceIndex = 0;
		return imageResources[imageResourceIndex++];
	}

	/**
	 * An / Hien FAM
	 * @param isVisible
     */
	private void setFAMVisible(boolean isVisible){
		if(isVisible){
			materialDesignFAM.setVisibility(View.VISIBLE);
		}else{
			materialDesignFAM.setVisibility(View.INVISIBLE);
		}
	}
	/**
	 * Material design floatin action button menu
	 */
	protected void materialFAM(){
		materialDesignFAM = (FloatingActionMenu) findViewById(R.id.material_design_android_floating_action_menu);
		fab_menu_list_user_checkall = (FloatingActionButton) findViewById(R.id.fab_menu_list_user_check_all);
		fab_menu_list_user_uncheckall = (FloatingActionButton) findViewById(R.id.fab_menu_list_user_uncheck_all);
		fab_menu_list_user_delete = (FloatingActionButton) findViewById(R.id.fab_menu_list_user_delete);
		fab_menu_list_user_undelete= (FloatingActionButton) findViewById(R.id.fab_menu_list_user_undelete);
		fab_menu_list_user_send_email = (FloatingActionButton) findViewById(R.id.fab_menu_list_user_send_email);
		fab_menu_list_user_send_sms = (FloatingActionButton) findViewById(R.id.fab_menu_list_user_send_sms);

		fab_menu_list_user_checkall.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				//check all
				setCheckAll();
			}
		});
		fab_menu_list_user_uncheckall.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				//uncheck all
				setUnCheckAll();
			}
		});
		fab_menu_list_user_delete.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				//delete user
				deleteUser();
			}
		});
		fab_menu_list_user_undelete.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				//phuc hoi user da xoa
				unDeleteUser();

			}
		});
		fab_menu_list_user_send_email.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				//TODO something when floating action menu third item clicked

			}
		});
		fab_menu_list_user_send_sms.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				//send sms
				sendSMS();
			}
		});
	}

	/**
	 * Xoa cac user duoc chon
	 */
	private void deleteUser(){
		/** get các item được gán check */
		List<User> listUser =adapter.getListViewData();
		int index=0;
		String listCode="";
		for(User usr : listUser){
			if(usr.isselected){
				if(index==0){
					listCode= String.valueOf(usr.code);
				}else{
					listCode= listCode + "," + String.valueOf(usr.code);
				}
				index++;
			}
		}
		if(listCode.trim().isEmpty()){
			/** không có dòng nào được chọn*/
			ShowAlertDialog.showTitleAndMessageDialog(this, "Xóa nhân viên", getResources().getString(R.string.titleNoItemSelected));
			return;
		}
		/** Xóa các nhân viên đã chọn */
		deleteUser(listCode);
		/** setting so nhan vien */
		setUserTitleBar(String.valueOf(adapter.getCount()));
		setBadgeCount(this, iconBadgeSearchSetting, String.valueOf(adapter.getCount()));
	}

	/**
	 * Phuc hoi cac user da xoa
	 */
	private void unDeleteUser(){
		/** kiểm tra xem trạng thái của delete user setting tại màn hình setting điều kiện*/
		if (GetSearchItemSetting.getIsDeleted()){
		}else{
			ShowAlertDialog.showTitleAndMessageDialog(this
					, getResources().getString(R.string.titleUnDelete)
					,"Vì các nhân viên đang hiển thị là chưa bị xóa nên không thể phục hồi.");
			return;

		}
		/** get các item được gán check */
		List<User> listUserUndelete =adapter.getListViewData();
		int indexUndelete=0;
		String listCodeUndelete="";
		for(User usr : listUserUndelete){
			if(usr.isselected){
				if(indexUndelete==0){
					listCodeUndelete= String.valueOf(usr.code);
				}else{
					listCodeUndelete= listCodeUndelete + "," + String.valueOf(usr.code);
				}
				indexUndelete++;
			}
		}
		if(listCodeUndelete.trim().isEmpty()){
			/** không có dòng nào được chọn*/
			ShowAlertDialog.showTitleAndMessageDialog(this, getResources().getString(R.string.titleUnDelete), getResources().getString(R.string.titleNoItemSelected));
			return;
		}
		/** Xóa các nhân viên đã chọn */
		unDeleteUser(listCodeUndelete);
		/** setting so nhan vien */
		setUserTitleBar(String.valueOf(adapter.getCount()));
		setBadgeCount(this, iconBadgeSearchSetting, String.valueOf(adapter.getCount()));
	}

	/**
	 * Gui SMS toi cac user duoc chon(se ton phi)
	 */
	private  void sendSMS(){
		//TODO something when floating action menu third item clicked
		/** get các item được gán check */
		List<User> listUserSendSms =adapter.getListViewData();
		ArrayList<User> listUserChecked=new ArrayList<User>();
		User[] arrUserChecked;
		int indexSendSms=0;
		String listCodeSendSms="";
		for(User usr : listUserSendSms){
			if(usr.isselected){
				listUserChecked.add(usr);
				if(indexSendSms==0){
					listCodeSendSms= String.valueOf(usr.code);
				}else{
					listCodeSendSms= listCodeSendSms + "," + String.valueOf(usr.code);
				}
				indexSendSms++;
			}
		}
		if(listCodeSendSms.trim().isEmpty()){
			/** không có dòng nào được chọn*/
			ShowAlertDialog.showTitleAndMessageDialog(this, getResources().getString(R.string.send_sms_titleInfo), getResources().getString(R.string.titleNoItemSelected));
			return;
		}
		arrUserChecked=new User[listUserChecked.size()];

		for(int i=0;i<listUserChecked.size();i++){
			arrUserChecked[i]= listUserChecked.get(i);
		}
		//listUserChecked.toArray(arrUserChecked);
		/**  chỉnh sửa */
		Intent intentSendSms = new Intent(getApplicationContext(), SendSmsActivity.class);
		Bundle bundle = new Bundle();
		/**lấy code của user*/
		//bundle.putParcelableArray(MasterConstants.SEND_SMS_TAG, arrUserChecked);
		bundle.putParcelableArrayList(MasterConstants.SEND_SMS_TAG, listUserChecked);
		/**gán vào bundle để gửi cùng với intent */
		intentSendSms.putExtras(bundle);

		/**khởi tạo activity dùng để edit  */
		startActivity(intentSendSms);

	}

	public void setBadgeCount(Context context, LayerDrawable icon, String count) {

		BadgeDrawableHelper badge;

		if(icon==null) return;
		// Reuse drawable if possible
		Drawable reuse = icon.findDrawableByLayerId(R.id.ic_badge);
		if(reuse==null) {
			reuse = geSingleDrawable(iconBadgeSearchSetting);
		}

		if (reuse != null && reuse instanceof BadgeDrawableHelper) {
			badge = (BadgeDrawableHelper) reuse;
		} else {
			badge = new BadgeDrawableHelper(context);
		}

		badge.setCount(count);
		icon.mutate();
		icon.setDrawableByLayerId(R.id.ic_badge, badge);
	}
	/**
	 * Hides the soft keyboard
	 */
	public void hideSoftKeyboard() {
		if(getCurrentFocus()!=null) {
			InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
			inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
		}
	}

	/**
	 * Shows the soft keyboard
	 */
	public void showSoftKeyboard(View view) {
		InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
		view.requestFocus();
		inputMethodManager.showSoftInput(view, 0);
	}

	/***
	 * Get Drawable from LayerDrawable
	 * @param layerDrawable
     * @return
     */
	public Drawable geSingleDrawable(LayerDrawable layerDrawable){

		int resourceBitmapHeight = 136, resourceBitmapWidth = 153;

		float widthInInches = 0.9f;

		int widthInPixels = (int)(widthInInches * getResources().getDisplayMetrics().densityDpi);
		int heightInPixels = (int)(widthInPixels * resourceBitmapHeight / resourceBitmapWidth);

		int insetLeft = 10, insetTop = 10, insetRight = 10, insetBottom = 10;

		layerDrawable.setLayerInset(1, insetLeft, insetTop, insetRight, insetBottom);

		Bitmap bitmap = Bitmap.createBitmap(widthInPixels, heightInPixels, Bitmap.Config.ARGB_8888);

		Canvas canvas = new Canvas(bitmap);
		layerDrawable.setBounds(0, 0, widthInPixels, heightInPixels);
		layerDrawable.draw(canvas);

		BitmapDrawable bitmapDrawable = new BitmapDrawable(getResources(), bitmap);
		bitmapDrawable.setBounds(0, 0, widthInPixels, heightInPixels);

		return bitmapDrawable;
	}

}
