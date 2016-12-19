package com.ussol.employeetracker;

import com.ussol.employeetracker.models.MasterConstants;
import com.ussol.employeetracker.services.AlarmJobService;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class AlarmJobServiceActivity  extends Activity {
	  public static final int MSG_UNCOLOUR_START = 0;
	  public static final int MSG_UNCOLOUR_STOP = 1;
	  public static final int MSG_SERVICE_OBJ = 2;

	  @Override
	  public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_alarm_job_service);
	    Resources res = getResources();
	    /*
	    defaultColor = res.getColor(R.color.none_received);
	    startJobColor = res.getColor(R.color.start_received);
	    stopJobColor = res.getColor(R.color.stop_received);
	     */
	    mDelayEditText = (EditText) findViewById(R.id.delay_time);
	    mDeadlineEditText = (EditText) findViewById(R.id.deadline_time);
	    mWiFiConnectivityRadioButton = (RadioButton) findViewById(R.id.checkbox_unmetered);
	    mAnyConnectivityRadioButton = (RadioButton) findViewById(R.id.checkbox_any);
	    mRequiresChargingCheckBox = (CheckBox) findViewById(R.id.checkbox_charging);
	    mRequiresIdleCheckbox = (CheckBox) findViewById(R.id.checkbox_idle);
	    mPeriodic= (EditText) findViewById(R.id.periodic);
	    
	    mServiceComponent = new ComponentName(this, AlarmJobService.class);
	    
	  }

	  // UI fields.
	  int defaultColor;
	  int startJobColor;
	  int stopJobColor;
	  private EditText mDelayEditText;
	  private EditText mDeadlineEditText , mPeriodic;
	  private RadioButton mWiFiConnectivityRadioButton;
	  private RadioButton mAnyConnectivityRadioButton;
	  private CheckBox mRequiresChargingCheckBox;
	  private CheckBox mRequiresIdleCheckbox;
	  ComponentName mServiceComponent;
	  
	/** Service object to interact scheduled jobs. */

	  AlarmJobService mTestService;
	  private static int kJobId = 0;

	  
	/**
	   * UI onclick listener to schedule a new job. 
	   */

	@SuppressLint("NewApi")
	public void scheduleJob(View v) {
	    JobInfo.Builder builder = new JobInfo.Builder(kJobId++,mServiceComponent);
	    String delay = mDelayEditText.getText().toString();
	    if (delay != null && !TextUtils.isEmpty(delay)) {
	      builder.setMinimumLatency(Long.valueOf(delay) * 1000);
	    }
	    String deadline = mDeadlineEditText.getText().toString();
	    if (deadline != null && !TextUtils.isEmpty(deadline)) {
	      builder.setOverrideDeadline(Long.valueOf(deadline) * 1000);
	    }
	    String periodic = mPeriodic.getText().toString();
	    if (periodic != null && !TextUtils.isEmpty(periodic)) {
	      builder.setPeriodic(Long.valueOf(periodic) * 1000);
	    }
	    
	    boolean requiresUnmetered = mWiFiConnectivityRadioButton.isChecked();
	    
	    //builder.setOverrideDeadline(10*1000); // 3600000 the job will be executed anyway after one hour
	    boolean requiresAnyConnectivity = mAnyConnectivityRadioButton
	        .isChecked();
	    if (requiresUnmetered) {
	      builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED);
	    } else if (requiresAnyConnectivity) {
	      builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY);
	    }
	    builder.setRequiresDeviceIdle(mRequiresIdleCheckbox.isChecked());
	    builder.setRequiresCharging(mRequiresChargingCheckBox.isChecked());
	    JobScheduler jobScheduler =
	            (JobScheduler) getApplication().getSystemService(Context.JOB_SCHEDULER_SERVICE);
	    
	    jobScheduler.schedule(builder.build());
	    /** trả về kết quả*/
		Intent intent = new Intent();
		Bundle bundle = new Bundle();
		bundle.putInt("CANCEL_JOB", 1);
		intent.putExtras(bundle);
		this.setResult(this.RESULT_OK, intent);
	    this.finish();
	  }

	  public void cancelAllJobs(View v) {
	    JobScheduler tm = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
	    tm.cancelAll();
	    //this.finish();
	    //tra trang thai cua phan setting thanh OFF
	    /** trả về kết quả*/
		Intent intent = new Intent();
		Bundle bundle = new Bundle();
		bundle.putInt("CANCEL_JOB", 1);
		intent.putExtras(bundle);
		this.setResult(this.RESULT_CANCELED, intent);
		this.finish();
	  }


	} 
