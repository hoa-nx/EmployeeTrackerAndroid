package com.ussol.employeetracker.services;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import com.ussol.employeetracker.MainActivity;
import com.ussol.employeetracker.R;
import com.ussol.employeetracker.SwipeListViewActivity;
import com.ussol.employeetracker.helpers.ConvertCursorToListString;
import com.ussol.employeetracker.helpers.DatabaseAdapter;
import com.ussol.employeetracker.helpers.DatabaseAdapter.MESSAGE_STATUS;
import com.ussol.employeetracker.helpers.NotificationHelper;
import com.ussol.employeetracker.helpers.SystemConfigItemHelper;
import com.ussol.employeetracker.models.MasterConstants.MESSAGE_SMS_OR_EMAIL;
import com.ussol.employeetracker.models.MasterConstants.MESSAGE_TYPE;
import com.ussol.employeetracker.models.MasterConstants.MESSAGE_TYPE_CONST;
import com.ussol.employeetracker.models.MessageTemplate;
import com.ussol.employeetracker.models.User;
import com.ussol.employeetracker.models.UserMessageStatus;
import com.ussol.employeetracker.utils.BadgeAction;
import com.ussol.employeetracker.utils.Strings;
import com.ussol.employeetracker.utils.Utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

/**
 * JobService to be scheduled by the JobScheduler. Requests scheduled with
 * the JobScheduler ultimately land on this service's "onStartJob" method.
 * Currently all this does is write a log entry
 */

@SuppressLint("NewApi")
public class AlarmJobService  extends JobService{
	private static final String TAG = "SyncService";
	/** chuyển đổi Cursor thành List */
	private static ConvertCursorToListString mConvertCursorToListString;
	//DB adapter
	private static DatabaseAdapter mDatabaseAdapter;
	private static SystemConfigItemHelper systemConfig ;
	private static  Context _context;


	@Override
	public boolean onStartJob(JobParameters params) {
		// We don't do any real 'work' in this sample app. All we'll	
		// do is track which jobs have landed on our service, and
		// update the UI accordingly.
		Log.i(TAG, "on start job: " + params.getJobId());
		/** thuc thi job**/
		new JobTask(this, getApplicationContext()).execute(params);
		
		return true;
	}

	@Override
	public boolean onStopJob(JobParameters params) {
		Log.i(TAG, "on stop job: " + params.getJobId());
		return true;
	}

	public static void excuteTask(final Context ctx){
		_context= ctx;
		//danh sach nhan vien co ngay sinh nhat
		List<User>  userBirthdayList =getUserBirthdayList(ctx) ;
		//danh sach toi ngay nghi viec
		List<User>  userYasumiList =getUserYasumiList(ctx) ;
		//danh sach toi ky han gui danh gia nghi viec 
		List<User>  userTrainingList =getUserTrainingList(ctx) ;
		int countBadge =0;
		
		countBadge = userBirthdayList.size() + userYasumiList.size() + userTrainingList.size();
		
		if(countBadge!=0){
			/** Tao badge */
	        BadgeAction badge = new BadgeAction(ctx);
	        badge.setBadgeIcon(countBadge);
		}
		
		systemConfig = new SystemConfigItemHelper(ctx);
		int msgTemplateId = 0;
		String sim =  systemConfig.getSimSendSms();
		
		//danh sach nhan vien co sinh nhat
		if(userBirthdayList.size()>0)
		{
			msgTemplateId = Integer.parseInt(systemConfig.getBirthdayMsgCode());
			String msg = getMessageContent(ctx,msgTemplateId);
			sendSmsManager(ctx, userBirthdayList, msg,sim);

		}
		for ( User usr : userBirthdayList) {
			NotificationHelper.makeBirthdayNotification(_context,SwipeListViewActivity.class,usr,"","",0,true,true);
		}

		//danh sach nhan vien co sinh nhat
		if(userYasumiList.size()>0)
		{
			msgTemplateId = Integer.parseInt(systemConfig.getYasumiMsgCode());
			String msg = getMessageContent(ctx,msgTemplateId);
			sendSmsManager(ctx, userYasumiList, msg,sim);

		}
		
		//danh sach nhan vien co sinh nhat
		if(userTrainingList.size()>0)
		{
			msgTemplateId = Integer.parseInt(systemConfig.getTrainingMsgCode());
			String msg = getMessageContent(ctx,msgTemplateId);
			sendSmsManager(ctx, userTrainingList, msg,sim);
	    	        
		}
		
		
	}
	
	/**
	 * lay danh sach template
	 * @param ctx
	 * @return
	 */
	public static List<MessageTemplate> getMessageTemplateList(Context ctx , int templateId){
		List<MessageTemplate>  allTemplateList ;
		/** chuyển đổi từ Cursor thành List */
		mConvertCursorToListString = new ConvertCursorToListString(ctx);
		allTemplateList= mConvertCursorToListString.getMessageTemplateList(" AND " + DatabaseAdapter.KEY_CODE + "=" + templateId);

		return allTemplateList;	
	}
	/**
	 * lay noi dung template
	 * @param ctx
	 * @return
	 */
	public static String getMessageContent(Context ctx , int templateId){
		List<MessageTemplate>  allTemplateList = getMessageTemplateList(ctx, templateId);
		
		String content ="" ;//Strings.getBirthdaySMSMessage();
		
		for(MessageTemplate tlp : allTemplateList){
			if(tlp!=null){
				content = tlp.content;
			}
			break;
		}
		
		return content;	
	}
	
	/**
	 * lay danh sach nhan vien co ngay sinh nhat la ngay hien tai
	 * @param ctx
	 * @return
	 */
	public static List<User> getUserBirthdayList(Context ctx){
		boolean isSuccess = false;
		List<User>  allUserList ;
		List<User> userBirthdayList = new ArrayList<User>();
		/** chuyển đổi từ Cursor thành List */
		mConvertCursorToListString = new ConvertCursorToListString(ctx);
		allUserList= mConvertCursorToListString.getUserList("");
		for(User usr : allUserList){
			if(Utils.isCurrentDateBirthday(usr.birthday)){
				//tao data cho bang message status
				isSuccess = insertMessageStatusTable(ctx, usr, MESSAGE_TYPE.BIRTHDAY, MESSAGE_SMS_OR_EMAIL.SMS);
				if(isSuccess){
					//neu la tao moi thi moi add vao list
					userBirthdayList.add(usr);
				}
			}
		}
		
		return userBirthdayList;
	}
	/** 
	 * Lay danh sach nghi viec
	 * @param ctx
	 * @return
	 */
	public static List<User> getUserYasumiList(Context ctx){
		boolean isSuccess = false;
		List<User>  allUserList ;
		List<User> userMatchList = new ArrayList<User>();
		/** chuyển đổi từ Cursor thành List */
		mConvertCursorToListString = new ConvertCursorToListString(ctx);
		allUserList= mConvertCursorToListString.getUserList("");
		for(User usr : allUserList){
			if(Utils.isDateMatch(usr.out_date)){
				//tao data cho bang message status
				insertMessageStatusTable(ctx, usr, MESSAGE_TYPE.YASUMI, MESSAGE_SMS_OR_EMAIL.SMS);
				if(isSuccess ){
					userMatchList.add(usr);
				}
			}
		}
		
		return userMatchList;
	}
	
	/** 
	 * Lay danh sach nghi viec
	 * @param ctx
	 * @return
	 */
	public static List<User> getUserTrainingList(Context ctx){
		boolean isSuccess = false;
		List<User>  allUserList ;
		List<User> userMatchList = new ArrayList<User>();
		/** chuyển đổi từ Cursor thành List */
		mConvertCursorToListString = new ConvertCursorToListString(ctx);
		allUserList= mConvertCursorToListString.getUserList("");
		for(User usr : allUserList){
			if(Utils.isDateMatch(usr.training_dateEnd)){
				//tao data cho bang message status
				insertMessageStatusTable(ctx, usr, MESSAGE_TYPE.TRAIL, MESSAGE_SMS_OR_EMAIL.SMS);
				if(isSuccess){
					userMatchList.add(usr);
				}
			}
		}
		
		return userMatchList;
	}
	
	/**
	 * Tao moi message status
	 * @param usr
	 * @param type
	 */
	public static boolean insertMessageStatusTable(Context ctx , User usr , MESSAGE_TYPE type, MESSAGE_SMS_OR_EMAIL smsOrEmail){
		boolean isSuccess = false;

		UserMessageStatus msg = new UserMessageStatus();
		//get current date
		Calendar c = Calendar.getInstance();
		SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
		String formattedDate = df.format(c.getTime());
				
		msg.message_emp_code = usr.code;
		msg.message_type =type.ordinal();
		msg.send_status = MESSAGE_STATUS.NOT_SENT.ordinal();
		msg.sender_code = 0;
		msg.sender_mail = "";
		msg.sender_phone = "0";
		msg.receiver_code = usr.code;
		msg.receiver_phone = usr.mobile;
		msg.receiver_mail = usr.email;

		msg.isdeleted=0;
		msg.isactive=1;
		if(smsOrEmail.ordinal()==MESSAGE_SMS_OR_EMAIL.SMS.ordinal()){
			msg.issms=1;
			msg.isemail=0;
		}else{
			msg.issms=0;
		}
		if(smsOrEmail.ordinal()==MESSAGE_SMS_OR_EMAIL.EMAIL.ordinal()){
			msg.issms=0;
			msg.isemail=1;
		}else{
			msg.isemail=0;
		}
		

		systemConfig = new SystemConfigItemHelper(ctx);
		int msgTemplateId = 0;
		if(type == MESSAGE_TYPE.BIRTHDAY){
			msgTemplateId = Integer.parseInt(systemConfig.getBirthdayMsgCode());	
		}
		if(type == MESSAGE_TYPE.YASUMI){
			msgTemplateId = Integer.parseInt(systemConfig.getYasumiMsgCode());
		}
		if(type == MESSAGE_TYPE.TRAIL){
			msgTemplateId = Integer.parseInt(systemConfig.getTrainingMsgCode());
		}
		
		msg.message_template_code=msgTemplateId;

		msg.send_datetime = formattedDate;
		msg.name=type.name();
		
		msg.yobi_text1 = Utils.getLastName(usr.full_name);
		if(msg.yobi_text1.indexOf("(")>0){
			msg.yobi_text1 = msg.yobi_text1.substring(0, msg.yobi_text1.indexOf("("));
		}
		//create DB
        mDatabaseAdapter = new DatabaseAdapter(ctx);
        mDatabaseAdapter.open();

        if(!mDatabaseAdapter.isSonzaiUserMessageStatus(msg.message_emp_code, formattedDate, type.ordinal())){
        	mDatabaseAdapter.insertToMessageStatusTable(msg);
			isSuccess = true;

        }
        
        mDatabaseAdapter.close();
        return isSuccess;
	}

	/**
	 * cập nhật trạng thái của message
	 * @param ctx : context
	 * @param msg_code : meesage code
	 * @param status : trạng thái
     */
	public static void updateMessageStatus(Context ctx , int  msg_code, MESSAGE_STATUS status){
		UserMessageStatus msg = new UserMessageStatus();
		/** chuyển đổi từ Cursor thành List */
		mConvertCursorToListString = new ConvertCursorToListString(ctx);
		String xWhere = " AND " + DatabaseAdapter.KEY_CODE + "=" + msg_code;
		List<UserMessageStatus> listMsg = mConvertCursorToListString.getUserMessageStatusList(xWhere);
		if(listMsg.size()>0){
			msg = listMsg.get(0);
			
			msg.send_status = status.ordinal();

		}
		
		//create DB
        mDatabaseAdapter = new DatabaseAdapter(ctx);
        mDatabaseAdapter.open();
        mDatabaseAdapter.editMessageStatusTable(msg);
        mDatabaseAdapter.close();
        
	}
	/**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
	 * Gửi message sử dụng SmSManager
	 * 
	 ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
	public static void sendSmsManager(Context ctx ,List<User> userArr , String message , String sendFromPhone){
		//get current date
		Calendar c = Calendar.getInstance();
		SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
		String formattedDate = df.format(c.getTime());
		/** chuyển đổi từ Cursor thành List */
		mConvertCursorToListString = new ConvertCursorToListString(ctx);
		String xWhere = " AND " + DatabaseAdapter.KEY_SEND_DATETIME + "='" + formattedDate + "'";
		//chi get cac MSG chua duoc gui
		xWhere += " AND " + DatabaseAdapter.KEY_SEND_STATUS + "=" + MESSAGE_STATUS.NOT_SENT.ordinal() ;
		
		List<UserMessageStatus> listMsg = mConvertCursorToListString.getUserMessageStatusList(xWhere);
		
		for(UserMessageStatus msg : listMsg){
			try {
				String phoneNo =  msg.receiver_phone;
				int msgTemplateId = msg.message_template_code;
				message = getMessageContent(ctx,msgTemplateId);
				message = message.replace("{0}", msg.yobi_text1);
				
				if(phoneNo==null || phoneNo.equals("")){
					//khong xu ly
				}else{
					switch (msg.message_type){
						case MESSAGE_TYPE_CONST.BIRTHDAY:
							// truong hop ma cho phep gui sms tu dong thi se gui
							if(systemConfig.getEnableSendSmsBirthdayConfiguration()){
								//gui SMS
								if(phoneNo.length() > 0 && message.equals("") == false)
								{
									send(ctx, msg.code,phoneNo,message,sendFromPhone);
									//cap nhat lai trang thai cua data message
									//updateMessageStatus(ctx, msg.code, MESSAGE_STATUS.SENT);
								}
							}
							break;
						
						case MESSAGE_TYPE_CONST.YASUMI:
							// truong hop ma cho phep gui sms tu dong thi se gui
							if(systemConfig.getEnableSendSmsYasumiConfiguration()){
								//gui SMS
								if(phoneNo.length() > 0 && message.equals("") == false)
								{
									send(ctx, msg.code,phoneNo,message,sendFromPhone);
									//cap nhat lai trang thai cua data message
									//updateMessageStatus(ctx, msg.code, MESSAGE_STATUS.SENT);
								}
							}
							break;
							
						case MESSAGE_TYPE_CONST.TRAIL:
							// truong hop ma cho phep gui sms tu dong thi se gui
							if(systemConfig.getEnableSendSmsTrainingConfiguration()){
								//gui SMS
								if(phoneNo.length() > 0 && message.equals("") == false)
								{
									send(ctx, msg.code,phoneNo,message,sendFromPhone);
									//cap nhat lai trang thai cua data message
									//updateMessageStatus(ctx, msg.code, MESSAGE_STATUS.SENT);
								}
							}
							break;
						case MESSAGE_TYPE_CONST.SALARY:
							// truong hop ma cho phep gui sms tu dong thi se gui
							/*if(systemConfig.getEnableSendSmsTrainingConfiguration()){
								//gui SMS
								if(phoneNo.length() > 0 && message.equals("") == false)
								{
									send(ctx, msg.code,phoneNo,message,sendFromPhone);
								}
							}*/
							break;
					}
				}
				
			  } catch (Exception e) {
				  //cap nhat rang thai loi
				  updateMessageStatus(ctx, msg.code, MESSAGE_STATUS.FAIL);
			  }
		}
		/*
    	for(int i=0 ; i<userArr.size();i++){
			try {
				String phoneNo =  userArr.get(i).mobile;
				if(phoneNo==null || phoneNo.equals("")){
					//khong xu ly
				}else{
					send(phoneNo,ctx,Strings.getBirthdaySMSMessage(),sendFromPhone);
				}
				
			  } catch (Exception e) {
				  //cap nhat rang thai loi
				  
			  }
		}
		 */
		
	}

	/**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
	 * Gửi message sử dụng sendSmsMessage
	 * 
	 ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
	public static void sendSmsMessage(Context ctx ,int msgId , String message , String sendFromPhone){
		//get current date
		Calendar c = Calendar.getInstance();
		SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
		String formattedDate = df.format(c.getTime());
		/** chuyển đổi từ Cursor thành List */
		mConvertCursorToListString = new ConvertCursorToListString(ctx);
		String xWhere = "";
		//xWhere += " AND " + DatabaseAdapter.KEY_SEND_DATETIME + "='" + formattedDate + "'";
		xWhere += " AND " + DatabaseAdapter.KEY_CODE + "=" + msgId ;
		
		List<UserMessageStatus> listMsg = mConvertCursorToListString.getUserMessageStatusList(xWhere);
		
		for(UserMessageStatus msg : listMsg){
			try {
				String phoneNo =  msg.receiver_phone;
				if(phoneNo==null || phoneNo.equals("")){
					//khong xu ly
				}else{
					String msgContent = getMessageContent(ctx, msg.message_template_code);
					msgContent = msgContent.replace("{0}", msg.yobi_text1);
					/*systemConfig = new SystemConfigItemHelper(ctx);
					int msgTemplateId = 1;
					
					msgTemplateId = Integer.parseInt(systemConfig.getBirthdayMsgCode());
					msgTemplateId = Integer.parseInt(systemConfig.getYasumiMsgCode());
					msgTemplateId = Integer.parseInt(systemConfig.getTrainingMsgCode());*/
					
					//gui SMS
					send(ctx, msg.code,phoneNo,msgContent ,sendFromPhone);
					//cap nhat lai trang thai cua data message
					updateMessageStatus(ctx, msg.code, MESSAGE_STATUS.SENT);
				}
				
			  } catch (Exception e) {
				  //cap nhat rang thai loi
				  updateMessageStatus(ctx, msg.code, MESSAGE_STATUS.FAIL);
			  }
		}
		
	}
	
	private static  void send(final Context ctx , final int msgId, String incomingNumber,String stext, String sendFromPhone){
		  Log.d(TAG,"in contact specific");
		  Log.d(incomingNumber,stext);
		  String SENT="SMS_SENT";
		  String DELIVERED="SMS_DELIVERED";
		  PendingIntent sentIntent=PendingIntent.getBroadcast(ctx,0,new Intent(SENT),0);
		  PendingIntent deliveryIntent=PendingIntent.getBroadcast(ctx,0,new Intent(DELIVERED),0);
		  
		  //---when the SMS has been sent---
	        ctx.registerReceiver(new BroadcastReceiver(){
	            @Override
	            public void onReceive(Context arg0, Intent arg1) {
	                switch (getResultCode())
	                {
	                    case Activity.RESULT_OK:
	                    	//cap nhat rang thai loi
	      				  	updateMessageStatus(ctx, msgId, MESSAGE_STATUS.SENT);
	                        break;
	                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
	                    	updateMessageStatus(ctx, msgId, MESSAGE_STATUS.FAIL);
	                        break;
	                    case SmsManager.RESULT_ERROR_NO_SERVICE:
	                    	updateMessageStatus(ctx, msgId, MESSAGE_STATUS.FAIL);
	                        break;
	                    case SmsManager.RESULT_ERROR_NULL_PDU:
	                    	updateMessageStatus(ctx, msgId, MESSAGE_STATUS.FAIL);
	                        break;
	                    case SmsManager.RESULT_ERROR_RADIO_OFF:
	                    	updateMessageStatus(ctx, msgId, MESSAGE_STATUS.FAIL);
	                        break;
	                }
	            }
	        }, new IntentFilter(SENT));
	
	        //---when the SMS has been delivered---
	        ctx.registerReceiver(new BroadcastReceiver(){
	            @Override
	            public void onReceive(Context arg0, Intent arg1) {
	                switch (getResultCode())
	                {
	                    case Activity.RESULT_OK:
	                    	updateMessageStatus(ctx, msgId, MESSAGE_STATUS.SENT);
	                        break;
	                    case Activity.RESULT_CANCELED:
	                    	updateMessageStatus(ctx, msgId, MESSAGE_STATUS.FAIL);
	                        break;                        
	                }
	            }
	        }, new IntentFilter(DELIVERED));    
	        
		  try {
			  SmsManager smsManager=SmsManager.getDefault();
			  ArrayList<String> parts =smsManager.divideMessage(stext);
			  int numParts = parts.size();
			  
			  ArrayList<PendingIntent> sentIntents = new ArrayList<PendingIntent>();
			  ArrayList<PendingIntent> deliveryIntents = new ArrayList<PendingIntent>();

			  for (int i = 0; i < numParts; i++) {
				  sentIntents.add(PendingIntent.getBroadcast(ctx, 0, new Intent(SENT), 0));
				  deliveryIntents.add(PendingIntent.getBroadcast(ctx, 0, new Intent(DELIVERED), 0));
			  }

			  smsManager.sendMultipartTextMessage(incomingNumber,null, parts, sentIntents, deliveryIntents);

			  //cap nhat lai trang thai cua data message
			  updateMessageStatus(ctx, msgId, MESSAGE_STATUS.SENT);
		  }
		 catch (  Exception e) {
			//cap nhat rang thai loi
			  updateMessageStatus(ctx, msgId, MESSAGE_STATUS.FAIL);
		    //Toast.makeText(context,"SMS faild, please try again later!",Toast.LENGTH_LONG).show();
		    e.printStackTrace();
		  }
	}
	
	
	MainActivity mActivity;
	private final LinkedList<JobParameters> jobParamsMap = new LinkedList<JobParameters>();

	public void setUiCallback(MainActivity activity) {
		mActivity = activity;
	}

	private static class JobTask extends AsyncTask<JobParameters, Void, JobParameters> {
        private final JobService jobService;
        private final Context ctx;
        
        public JobTask(JobService jobService, Context applicationContext) {
            this.jobService = jobService;
            this.ctx = applicationContext;
        }

        @Override
        protected JobParameters doInBackground(JobParameters... params) {
            
        	excuteTask(ctx);
            return params[0];
        }

        @Override
        protected void onPostExecute(JobParameters jobParameters) {
            jobService.jobFinished(jobParameters, false);
        }
    }


} 