/**
 * Copyright (c) 2012 Fujinet Software Department 5 ( USSOL team)  
 * See the file license.txt for copying permission.
*/  

package com.ussol.employeetracker.helpers;

import java.util.List;

import javax.crypto.spec.PSource;

import com.ussol.employeetracker.R;
import com.ussol.employeetracker.models.CustomerGroup;
import com.ussol.employeetracker.models.Dept;
import com.ussol.employeetracker.models.MasterConstants;
import com.ussol.employeetracker.models.Position;
import com.ussol.employeetracker.models.PositionGroup;
import com.ussol.employeetracker.models.Team;
import com.ussol.employeetracker.models.User;
import com.ussol.employeetracker.models.UserCustomerGroup;
import com.ussol.employeetracker.models.UserPositionGroup;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

/**
 * 
 * @author hoa-nx
 *
 */
public class AlertDialogRadio  extends DialogFragment{
		
	/** Declaring the interface, to invoke a callback function in the implementing activity class */
	AlertPositiveListener alertPositiveListener;
	String _title ="";
	String[] _data ;
	User[] _dataUser ;
	User[] _dataUserCheck ;
	Dept[] _dataDept ;
	Dept[] _dataDeptCheck ;
	Team[] _dataTeam ;
	Team[] _dataTeamCheck ;
	Position[] _dataPosition ;
	Position[] _dataPositionCheck ;
	PositionGroup[] _dataPositionGroup ;
	CustomerGroup[] _dataCustomerGroup ;
	List<UserPositionGroup> _userPositionGroupList;
	List<UserCustomerGroup> _userCustomerGroupList;
	
	List<Dept> _dataDeptList;
	List<Team> _dataTeamList;
	List<Position> _dataPositionList;
	List<User> _dataUserList;
	
	boolean _isMultiSelect=false;
	
	int _clickBtn=0;
	UserDialogAdapter adapter;
	DeptDialogAdapter adapterDept;
	TeamDialogAdapter adapterTeam;
	PositionDialogAdapter adapterPosition;
	PositionGroupDialogAdapter adapterPositionGroup;
	CustomerGroupDialogAdapter adapterCustomerGroup;
	DialogAdapterCheckBox<User> adapterUserCheck;
	DialogAdapterCheckBox<Dept> adapterDeptCheck;
	DialogAdapterCheckBox<Team> adapterTeamCheck;
	DialogAdapterCheckBox<Position> adapterPositionCheck;
	
	AlertDialog.Builder b;
	Bundle bundle=new Bundle();
	/** hàm khởi tạo trong trường hợp là common ( truyền vào mảng chuỗi) */
	public AlertDialogRadio(String title , String[] data , int clickBtn){
		_title = title;
		_data = data;
		_clickBtn = clickBtn;
		_isMultiSelect=false;
		_dataUser=null ;
		_dataDept=null ;
		_dataTeam=null ;
		_dataPosition=null ;
		_dataPositionGroup=null ;
		_dataCustomerGroup=null ;
		_dataDeptList=null;
		_dataDeptCheck=null;
	}
	/** hàm khởi tạo trong trường hợp là user */
	public AlertDialogRadio(String title , User[] data , int clickBtn){
		_title = title;
		_dataUser = data;
		_clickBtn = clickBtn;
		_isMultiSelect=false;
		_data=null;
		_dataDept=null ;
		_dataTeam=null ;
		_dataPosition=null ;
		_dataPositionGroup=null ;
		_dataCustomerGroup=null ;
		_dataDeptList=null;
		_dataDeptCheck=null;
	}
	/** hàm khởi tạo trong trường hợp là phòng ban */
	public AlertDialogRadio(String title , Dept[] data , int clickBtn){
		_title = title;
		_dataDept = data;
		_clickBtn = clickBtn;
		_isMultiSelect=false;
		_data=null;
		_dataUser=null ;
		_dataTeam=null ;
		_dataPosition=null ;
		_dataPositionGroup=null ;
		_dataCustomerGroup=null ;
		_dataDeptList=null;
		_dataDeptCheck=null;
	}
	/** hàm khởi tạo trong trường hợp là nhóm -tổ */
	public AlertDialogRadio(String title , Team[] data , int clickBtn){
		_title = title;
		_dataTeam = data;
		_clickBtn = clickBtn;
		_isMultiSelect=false;
		_data=null;
		_dataUser=null ;
		_dataDept=null ;
		_dataPosition=null ;
		_dataPositionGroup=null ;
		_dataCustomerGroup=null ;
		_dataDeptList=null;
		_dataDeptCheck=null;
	}
	/** hàm khởi tạo trong trường hợp là chức vụ */
	public AlertDialogRadio(String title , Position[] data , int clickBtn){
		_title = title;
		_dataPosition = data;
		_clickBtn = clickBtn;
		_isMultiSelect=false;
		_data=null;
		_dataUser=null ;
		_dataDept=null ;
		_dataTeam=null ;
		_dataPositionGroup=null ;
		_dataCustomerGroup=null ;
		_dataDeptList=null;
		_dataDeptCheck=null;
	}
	/** hàm khởi tạo trong trường hợp là nhóm chức danh */
	public AlertDialogRadio(String title , PositionGroup[] data , int clickBtn, List<UserPositionGroup> userPositionGroup , boolean isMultiSelect){
		_title = title;
		_dataPositionGroup = data;
		_clickBtn = clickBtn;
		_userPositionGroupList = userPositionGroup;
		_isMultiSelect = isMultiSelect;	/** cho phép chọn nhiều record */
		_data=null;
		_dataUser=null ;
		_dataDept=null ;
		_dataTeam=null ;
		_dataPosition=null ;
		_dataCustomerGroup=null ;
		_dataDeptList=null;
		_dataDeptCheck=null;
	}
	
	/** hàm khởi tạo trong trường hợp là khách hàng*/
	public AlertDialogRadio(String title , CustomerGroup[] data , int clickBtn, List<UserCustomerGroup> userCustomerGroup , boolean isMultiSelect){
		_title = title;
		_dataCustomerGroup = data;
		_clickBtn = clickBtn;
		_userCustomerGroupList = userCustomerGroup;
		_isMultiSelect = isMultiSelect;	/** cho phép chọn nhiều record */
		_data=null;
		_dataUser=null ;
		_dataDept=null ;
		_dataTeam=null ;
		_dataPosition=null ;
		_dataPositionGroup=null ;
		_dataDeptList=null;
		_dataDeptCheck=null;
	}
	/** hàm khởi tạo trong trường hợp là phòng ban-cho phép chọn nhiều record */
	public AlertDialogRadio(String title , Dept[] data , int clickBtn, List<Dept> deptList , boolean isMultiSelect){
		_title = title;
		_dataDeptCheck = data;
		_clickBtn = clickBtn;
		_dataDeptList = deptList;
		_isMultiSelect = isMultiSelect;	/** cho phép chọn nhiều record */
		_data=null;
		_dataUser=null ;
		_dataDept=null ;
		_dataTeam=null ;
		_dataPosition=null ;
		_dataPositionGroup =null;
		_dataCustomerGroup=null ;
	}
	
	/** hàm khởi tạo trong trường hợp là phòng ban-cho phép chọn nhiều record */
	public AlertDialogRadio(String title , Team[] data , int clickBtn, List<Team> teamList , boolean isMultiSelect){
		_title = title;
		_dataTeamCheck = data;
		_clickBtn = clickBtn;
		_dataTeamList = teamList;
		_isMultiSelect = isMultiSelect;	/** cho phép chọn nhiều record */
		_data=null;
		_dataUser=null ;
		_dataDept=null ;
		_dataTeam=null ;
		_dataPosition=null ;
		_dataPositionGroup =null;
		_dataDeptCheck=null;
		_dataPositionCheck=null;
		_dataCustomerGroup=null ;
	}
	
	/** hàm khởi tạo trong trường hợp là phòng ban-cho phép chọn nhiều record */
	public AlertDialogRadio(String title , Position[] data , int clickBtn, List<Position> positionList , boolean isMultiSelect){
		_title = title;
		_dataPositionCheck = data;
		_clickBtn = clickBtn;
		_dataPositionList = positionList;
		_isMultiSelect = isMultiSelect;	/** cho phép chọn nhiều record */
		_data=null;
		_dataUser=null ;
		_dataDept=null ;
		_dataTeam=null ;
		_dataPosition=null ;
		_dataPositionGroup =null;
		_dataDeptCheck=null;
		_dataTeamCheck=null;
		_dataCustomerGroup=null ;
	}
	
	/** hàm khởi tạo trong trường hợp là user phép chọn nhiều record */
	public AlertDialogRadio(String title , User[] data , int clickBtn, List<User> userList , boolean isMultiSelect){
		_title = title;
		_dataUserCheck = data;
		_clickBtn = clickBtn;
		_dataUserList = userList;
		_isMultiSelect = isMultiSelect;	/** cho phép chọn nhiều record */
		_data=null;
		_dataUser=null ;
		_dataDept=null ;
		_dataTeam=null ;
		_dataPosition=null ;
		_dataPositionGroup =null;
		_dataDeptCheck=null;
		_dataTeamCheck=null;
		_dataPositionCheck=null;
		_dataCustomerGroup=null ;
	}

	/** An interface to be implemented in the hosting activity for "OK" button click listener */
	public interface AlertPositiveListener {
		public void onPositiveClick(int position , int clickBtn , Bundle bundle);
	}

	/** This is a callback method executed when this fragment is attached to an activity. 
	 *  This function ensures that, the hosting activity implements the interface AlertPositiveListener 
	 * */
	public void onAttach(android.app.Activity activity) {
		super.onAttach(activity);
		try{
			alertPositiveListener = (AlertPositiveListener) activity;
		}catch(ClassCastException e){
			// The hosting activity does not implemented the interface AlertPositiveListener
			throw new ClassCastException(activity.toString() + " must implement AlertPositiveListener");
		}
	}
	
	
	/** This is the OK button listener for the alert dialog, 
	 *  which in turn invokes the method onPositiveClick(position) 
	 *  of the hosting activity which is supposed to implement it
	 */	
	OnClickListener positiveListener = new OnClickListener() {		
		@Override
		public void onClick(DialogInterface dialog, int which) {
			AlertDialog alert = (AlertDialog)dialog;
			int position = 0;
			if(_isMultiSelect==true){
				/** xác định những item được chọn trên list */
				switch(_clickBtn){
					case MasterConstants.BTN_DIALOG_USER_LIST:
						User[] checkedUserItem= adapterUserCheck.getDataAdapter();
						bundle.putParcelableArray(MasterConstants.TAB_DIALOG_BUNDLE, checkedUserItem);
						break;
					case R.id.tbtUserPositionGroup:
						PositionGroup[] checkedItem= adapterPositionGroup.getDataAdapter();
						bundle.putParcelableArray(MasterConstants.TAB_DIALOG_BUNDLE, checkedItem);
						break;
					case R.id.tbtUserCustomerGroup:
						CustomerGroup[] checkedCustomer= adapterCustomerGroup.getDataAdapter();
						bundle.putParcelableArray(MasterConstants.TAB_DIALOG_BUNDLE, checkedCustomer);
						break;
						
					case R.id.tbtSearchItemDept:
						Dept[] checkedDept= adapterDeptCheck.getDataAdapter();
						bundle.putParcelableArray(MasterConstants.TAB_DIALOG_BUNDLE, checkedDept);
						break;
					case R.id.tbtSearchItemTeam:
						/** xác định những item được chọn trên list */
						Team[] checkedTeam= adapterTeamCheck.getDataAdapter();
						bundle.putParcelableArray(MasterConstants.TAB_DIALOG_BUNDLE, checkedTeam);
						break;
					case R.id.tbtSearchItemPosition:
						/** xác định những item được chọn trên list */
						Position[] checkedPosition= adapterPositionCheck.getDataAdapter();
						bundle.putParcelableArray(MasterConstants.TAB_DIALOG_BUNDLE, checkedPosition);
						break;
				}
				
			}else{
				 position = alert.getListView().getCheckedItemPosition();
			}
		
			alertPositiveListener.onPositiveClick(position , _clickBtn, bundle);			
		}
	};
	
	/** This is the OK button listener for the alert dialog, 
	 *  which in turn invokes the method onPositiveClick(position) 
	 *  of the hosting activity which is supposed to implement it
	 */	
	OnClickListener negativeListener = new OnClickListener() {		
		@Override
		public void onClick(DialogInterface dialog, int which) {
			if(_isMultiSelect==true){
				switch(_clickBtn){
					case MasterConstants.BTN_DIALOG_USER_LIST:
						User[] checkedUserItem= adapterUserCheck.getInitStatesDataAdapter();
						bundle.putParcelableArray(MasterConstants.TAB_DIALOG_BUNDLE, checkedUserItem);
						break;
					case R.id.tbtUserPositionGroup:
						/** xác định những item được chọ trên list */
						PositionGroup[] checkedItem= adapterPositionGroup.getDataAdapter();
						bundle.putParcelableArray(MasterConstants.TAB_DIALOG_BUNDLE, checkedItem);
						
						break;
						
					case R.id.tbtUserCustomerGroup:
						/** xác định những item được chọ trên list */
						CustomerGroup[] checkedCustomer= adapterCustomerGroup.getDataAdapter();
						bundle.putParcelableArray(MasterConstants.TAB_DIALOG_BUNDLE, checkedCustomer);
						
						break;
						
					case R.id.tbtSearchItemDept:
						/** xác định những item được chọ trên list */
						Dept[] checkedDept= adapterDeptCheck.getInitStatesDataAdapter();
						bundle.putParcelableArray(MasterConstants.TAB_DIALOG_BUNDLE, checkedDept);
						break;
					case R.id.tbtSearchItemTeam:
						/** xác định những item được chọ trên list */
						Team[] checkedTeam= adapterTeamCheck.getInitStatesDataAdapter();
						bundle.putParcelableArray(MasterConstants.TAB_DIALOG_BUNDLE, checkedTeam);
						break;
					case R.id.tbtSearchItemPosition:
						/** xác định những item được chọ trên list */
						Position[] checkedPosition= adapterPositionCheck.getInitStatesDataAdapter();
						bundle.putParcelableArray(MasterConstants.TAB_DIALOG_BUNDLE, checkedPosition);
						break;
				}
				
			}
		
			alertPositiveListener.onPositiveClick(0 , _clickBtn, bundle);			
		}
	};
	
	/** This is a callback method which will be executed 
	 *  on creating this fragment
	 */
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		
		/** Getting the arguments passed to this fragment */
		Bundle bundle = getArguments();		
		int position = bundle.getInt("position");
		
		
		/** Creating a builder for the alert dialog window */
		b = new AlertDialog.Builder(getActivity());
		
		
		/** Setting a title for the window */
		b.setTitle(_title);		

		/** Setting items to the alert dialog */
		//b.setSingleChoiceItems(_data, position, null);
		//b.setSingleChoiceItems(data, position, "", null);
		// define the list adapter with the choices
		//ListAdapter adapter = new DialogAdapter( getActivity(),_users);
		if (_data!=null){
			b.setSingleChoiceItems(_data, position, null);
		}else if(_dataUser!=null){
			/** trường hợp là data user */
			adapter = new UserDialogAdapter(getActivity(), _dataUser);
			b.setSingleChoiceItems(adapter, 0 ,mOnClickListener);
		}else if(_dataDept!=null){
			/** trường hợp là data phòng ban */
			adapterDept = new DeptDialogAdapter(getActivity(), _dataDept);
			b.setSingleChoiceItems(adapterDept, 0 , mOnClickListener);
		}else if(_dataTeam!=null){
			/** trường hợp là data nhóm  */
			adapterTeam = new TeamDialogAdapter(getActivity(), _dataTeam);
			b.setSingleChoiceItems(adapterTeam, 0 , mOnClickListener);
		}else if(_dataPosition!=null){
			/** trường hợp là data chức vụ */
			adapterPosition = new PositionDialogAdapter(getActivity(), _dataPosition);
			b.setSingleChoiceItems(adapterPosition, 0 , mOnClickListener);
		}else if(_dataPositionGroup!=null){
			/** trường hợp là data nhóm chức danh */
			adapterPositionGroup = new PositionGroupDialogAdapter(getActivity(), _dataPositionGroup,_userPositionGroupList);
			b.setAdapter(adapterPositionGroup, null);
		}else if(_dataCustomerGroup!=null){
			/** trường hợp là data nhóm chức danh */
			adapterCustomerGroup = new CustomerGroupDialogAdapter(getActivity(), _dataCustomerGroup,_userCustomerGroupList);
			b.setAdapter(adapterCustomerGroup, null);
		}else if(_dataDeptCheck!=null){
			/** trường hợp là data nhóm chức danh */
			adapterDeptCheck = new DialogAdapterCheckBox<Dept>(getActivity(), _dataDeptCheck,_dataDeptList);
			b.setAdapter(adapterDeptCheck, null);
		}else if(_dataTeamCheck!=null){
			/** trường hợp là data nhóm chức danh */
			adapterTeamCheck = new DialogAdapterCheckBox<Team>(getActivity(), _dataTeamCheck,_dataTeamList);
			b.setAdapter(adapterTeamCheck, null);
		}else if(_dataPositionCheck!=null){
			/** trường hợp là data nhóm chức danh */
			adapterPositionCheck = new DialogAdapterCheckBox<Position>(getActivity(), _dataPositionCheck,_dataPositionList);
			b.setAdapter(adapterPositionCheck, null);
		}else if(_dataUserCheck!=null){
			/** trường hợp là list user */
			adapterUserCheck = new DialogAdapterCheckBox<User>(getActivity(), _dataUserCheck,_dataUserList);
			b.setAdapter(adapterUserCheck, null);
		}
		
		//b.setSingleChoiceItems(adapter, 0, null);
		/** Setting a positive button and its listener */
		b.setPositiveButton("OK",positiveListener);
		
		/** Setting a positive button and its listener */
		if(_isMultiSelect==true){
			b.setNegativeButton("Cancel", negativeListener);
		}else{
			b.setNegativeButton("Cancel", null);
		}
				
		/** Creating the alert dialog window using the builder class */
		AlertDialog d = b.create();
		
		/** Return the alert dialog window */
		return d;
	}
		
	private OnClickListener mOnClickListener = new OnClickListener() {
		
		@Override
		public void onClick(DialogInterface dialog, int which) {
			// TODO Auto-generated method stub
			if(_isMultiSelect==false){
				/** trường hợp không cho chọn nhiều iteam */
				AlertDialog alert = (AlertDialog)dialog;
				int position = alert.getListView().getCheckedItemPosition();			
				alertPositiveListener.onPositiveClick(position , _clickBtn, bundle);		
				dialog.dismiss();
			}else{
				
			}
			
		}
	};
}
