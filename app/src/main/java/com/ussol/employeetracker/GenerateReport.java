/**
 * Copyright (c) 2012 Fujinet Software Department 5 ( USSOL team)  
 * See the file license.txt for copying permission.
*/ 

package com.ussol.employeetracker;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfAction;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;
import com.ussol.employeetracker.helpers.ConvertCursorToListString;
import com.ussol.employeetracker.helpers.ExpGroupHelper;
import com.ussol.employeetracker.helpers.ExpParent;
import com.ussol.employeetracker.helpers.ExpParentChildInGroup;
import com.ussol.employeetracker.helpers.GroupPdfReport;
import com.ussol.employeetracker.helpers.SendMail;
import com.ussol.employeetracker.models.IExpGroup;
import com.ussol.employeetracker.models.MasterConstants;
import com.ussol.employeetracker.models.User;
import com.ussol.employeetracker.utils.DateTimeUtil;
import com.ussol.employeetracker.utils.StringProcessing;
import com.ussol.employeetracker.utils.Utils;


public class GenerateReport extends Activity implements OnClickListener {
	
	private Spinner period;
	private Spinner typeSpinner;
	
	private int mStartYear;
    private int mStartMonth;
    private int mStartDay;
    
    private int mEndYear;
    private int mEndMonth;
    private int mEndDay;
    
    private TextView customStartDateTextView;
    private TextView customEndDateTextView;
    private Button btnReportByNone , btnOutputPDFCancel , btnReportBySalary, btnReportbyDept, btnReportByTeam;
	private Button btnReportBySex , btnReportByBussinessKbn , btnReportByJapanese, btnReportByPosition;
	private Button btnReportByTrainingInYear , btnReportByContractInYear , btnReportByNotContractInYear, btnReportByYasumiInYear, btnReportByPositionNotSatified;
    private RadioGroup rdgExportType;	
    private AsyncTask<String, Void, String> exportPDF;
    private AsyncTask<String, Void, String> exportCSV;
    private AsyncTask<String, Void, String> exportEXCEL;
    
    private File fileLocation;
    
    private String dateRange;
	private List<User> mEntryList;
	private ArrayList<ExpParent> groupParent;
	private final int REQUEST_CODE = 1055;
	public static File fontFile = new File("assets/fonts/vuArial.ttf");
	
	public interface ExportType {
	    String PDF = "pdf";
	    String EXCEL = "xls";
		String CSV ="csv";
	}
	private String selectedExport;
	enum EXCEL_EXPORT_COLUMN {STT
							,EMP_CODE 
							,FULL_NAME
							,SEX
							,BIRTHDAY
							,TEL
							,ADDRESS
							,EMAIL
							,LEARNING_START
							,LEARNING_END
							,TRAINING_START
							,TRAINING_END
							,CONTRACT_DATE
							,KEIKEN_MONTH
							,ESTIMATE_POINT
							,KEIKEN_CONVERT
							,END_DATE
							,DEPT
							,TEAM
							,POSITION
							,JP
							,WORK_KBN
							,NOTE };
	private RadioButton radPDF,radEXCEL;
	
/**Xu ly activity START */
	@Override
	protected void onStart() {
		super.onStart();
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		//setResult(MasterConstants.RESULT_CLOSE_ALL);
		//this.finish();
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_user_generate_report);
		
		//set default end day values
		Calendar calendar = Calendar.getInstance();
		calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
		calendar.setFirstDayOfWeek(Calendar.MONDAY);
		mEndYear = calendar.get(Calendar.YEAR);
		mEndMonth = calendar.get(Calendar.MONTH);
		mEndDay = calendar.get(Calendar.DAY_OF_MONTH);
		
		getControl();
		settingListener();
		
		/** xu ly hien thi message khong ton tai data */
		/**
		if(mEntryList.size() == 0) {
			new AlertDialog.Builder(this)
			.setTitle("Xuất PDF")
			.setCancelable(false)
			.setMessage("Không tồn tại dữ liệu")
			.setIcon(android.R.drawable.ic_dialog_alert)
			.setPositiveButton(getString(R.string.titleYes), new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					finish();
				}
			})
			.show();
		}
		*/
		
	}
	
    @Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		//setResult(MasterConstants.RESULT_CLOSE_ALL);
		//this.finish();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
	}

	/**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
    * get các control trên màn hình
    * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    private void getControl(){
    	btnReportbyDept = (Button)findViewById(R.id.btnReportByDept);
    	btnReportByTeam = (Button)findViewById(R.id.btnReportByTeam);
		btnReportByPosition = (Button)findViewById(R.id.btnReportByPosition);
		btnReportByBussinessKbn = (Button)findViewById(R.id.btnReportByBusinessKbn);
		btnReportBySex = (Button)findViewById(R.id.btnReportBySex);
		btnReportByJapanese = (Button)findViewById(R.id.btnReportByJapaneseLevel);
		btnReportByTrainingInYear = (Button)findViewById(R.id.btnReportByTrainingStaffInYear);
		btnReportByContractInYear = (Button)findViewById(R.id.btnReportByContractStaffInYear);
		btnReportByNotContractInYear = (Button)findViewById(R.id.btnReportByNotContractStaffInYear);
		btnReportByYasumiInYear = (Button)findViewById(R.id.btnReportByYasumiInYear);
		btnReportByPositionNotSatified = (Button)findViewById(R.id.btnReportByPositionNotSatified);
		btnReportByNone = (Button)findViewById(R.id.btnReportByNone);

    	btnReportBySalary = (Button)findViewById(R.id.btnReportBySalary);
    	btnOutputPDFCancel= (Button)findViewById(R.id.btnOutputPDFCancel);
    	rdgExportType=(RadioGroup) findViewById(R.id.radExportType);
    	radPDF = (RadioButton) findViewById(R.id.radPDF);
    	radEXCEL = (RadioButton) findViewById(R.id.radExcel);
    }
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
    * gán sự kiện cho các control
    * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    private void settingListener(){
    	btnReportbyDept.setOnClickListener(this);
    	btnReportByTeam.setOnClickListener(this);
		btnReportByPosition.setOnClickListener(this);
		btnReportByBussinessKbn.setOnClickListener(this);
		btnReportByJapanese.setOnClickListener(this);
		btnReportBySex.setOnClickListener(this);
    	btnReportByNone.setOnClickListener(this);
    	btnReportByTrainingInYear.setOnClickListener(this);
		btnReportByContractInYear.setOnClickListener(this);
		btnReportByNotContractInYear.setOnClickListener(this);
		btnReportBySalary.setOnClickListener(this);
		btnReportByPositionNotSatified.setOnClickListener(this);
		btnReportByYasumiInYear.setOnClickListener(this);

    	btnOutputPDFCancel.setOnClickListener(this);
    	/* Attach CheckedChangeListener to radio group */
    	rdgExportType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = (RadioButton) group.findViewById(checkedId);
                if(null!=rb && checkedId > -1){
                	switch(rb.getId()){
                	case R.id.radPDF:
                		selectedExport = ExportType.PDF;
                		break;
                		
                	case R.id.radExcel:
                		selectedExport = ExportType.EXCEL;
                		break;
                	}
                }

            }
        });
    }
    
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnReportByNone:
			/** get danh sach cac nhan vien de output report */
			mEntryList = new ConvertCursorToListString(this).getUserList("");
			if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
				if(mEntryList.size() <= 5000) {
					if(radPDF.isChecked()){
						exportToPDF(MasterConstants.REP_BY_USER_LIST);
					}else{
						exportToEXCEL(MasterConstants.REP_BY_USER_LIST);
					}
					
				} else {
					new AlertDialog.Builder(this)
					.setTitle("Xuất dữ liệu " + selectedExport)
					.setMessage("Quá nhiều dữ liệu, hãy chọn điều kiện để giới hạn dữ liệu.")
					.setIcon(android.R.drawable.ic_dialog_alert)
					.setPositiveButton(getString(R.string.titleYes), (DialogInterface.OnClickListener)null)
					.show();
				}
			} else {
				Toast.makeText(this, "SDcard không sẵn sàng", Toast.LENGTH_LONG).show();
			}
			break;
		case R.id.btnReportBySalary:
			groupParent = getDataOutput(IExpGroup.EXP_GROUP_SALARY_BASIC);
			/** output bang thong ke luong cua cac nhan vien */
			if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
				if(groupParent.size() <= 5000) {

					if(radPDF.isChecked()){
						GroupPdfReport report = new GroupPdfReport(this);
						report.exportToPDF();
					}else{
						//Excel
						exportToEXCEL(IExpGroup.EXP_GROUP_SALARY_BASIC);
					}

				} else {
					new AlertDialog.Builder(this)
							.setTitle("Xuất dữ liệu " + selectedExport)
							.setMessage("Quá nhiều dữ liệu, hãy chọn điều kiện để giới hạn dữ liệu.")
							.setIcon(android.R.drawable.ic_dialog_alert)
							.setPositiveButton(getString(R.string.titleYes), (DialogInterface.OnClickListener)null)
							.show();
				}
			} else {
				Toast.makeText(this, "SDcard không sẵn sàng", Toast.LENGTH_LONG).show();
			}
			break;	
		case R.id.btnReportByDept:
			groupParent = getDataOutput(IExpGroup.EXP_GROUP_DEPT);
			/** output bang thong ke luong cua cac nhan vien */
			if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
				if(groupParent.size() <= 5000) {

					if(radPDF.isChecked()){
						GroupPdfReport report = new GroupPdfReport(this);
						report.exportToPDF();
					}else{
						//Excel
						exportToEXCEL(IExpGroup.EXP_GROUP_DEPT);
					}
					
				} else {
					new AlertDialog.Builder(this)
					.setTitle("Xuất dữ liệu " + selectedExport)
					.setMessage("Quá nhiều dữ liệu, hãy chọn điều kiện để giới hạn dữ liệu.")
					.setIcon(android.R.drawable.ic_dialog_alert)
					.setPositiveButton(getString(R.string.titleYes), (DialogInterface.OnClickListener)null)
					.show();
				}
			} else {
				Toast.makeText(this, "SDcard không sẵn sàng", Toast.LENGTH_LONG).show();
			}
			break;
		case R.id.btnReportByTeam:
			groupParent = getDataOutput(IExpGroup.EXP_GROUP_TEAM);
			/** output bang thong ke luong cua cac nhan vien */
			if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
				if(groupParent.size() <= 5000) {

					if(radPDF.isChecked()){
						GroupPdfReport report = new GroupPdfReport(this);
						report.exportToPDF();
					}else{
						//Excel
						exportToEXCEL(IExpGroup.EXP_GROUP_TEAM);
					}

				} else {
					new AlertDialog.Builder(this)
							.setTitle("Xuất dữ liệu " + selectedExport)
							.setMessage("Quá nhiều dữ liệu, hãy chọn điều kiện để giới hạn dữ liệu.")
							.setIcon(android.R.drawable.ic_dialog_alert)
							.setPositiveButton(getString(R.string.titleYes), (DialogInterface.OnClickListener)null)
							.show();
				}
			} else {
				Toast.makeText(this, "SDcard không sẵn sàng", Toast.LENGTH_LONG).show();
			}
			break;
		case R.id.btnReportByPosition:
			groupParent = getDataOutput(IExpGroup.EXP_GROUP_POSITION);
			/** output bang thong ke luong cua cac nhan vien */
			if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
				if(groupParent.size() <= 5000) {

					if(radPDF.isChecked()){
						GroupPdfReport report = new GroupPdfReport(this);
						report.exportToPDF();
					}else{
						//Excel
						exportToEXCEL(IExpGroup.EXP_GROUP_POSITION);
					}

				} else {
					new AlertDialog.Builder(this)
							.setTitle("Xuất dữ liệu " + selectedExport)
							.setMessage("Quá nhiều dữ liệu, hãy chọn điều kiện để giới hạn dữ liệu.")
							.setIcon(android.R.drawable.ic_dialog_alert)
							.setPositiveButton(getString(R.string.titleYes), (DialogInterface.OnClickListener)null)
							.show();
				}
			} else {
				Toast.makeText(this, "SDcard không sẵn sàng", Toast.LENGTH_LONG).show();
			}
			break;
		case R.id.btnReportByBusinessKbn:
			groupParent = getDataOutput(IExpGroup.EXP_GROUP_BUSINESS_KBN);
			/** output bang thong ke luong cua cac nhan vien */
			if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
				if(groupParent.size() <= 5000) {

					if(radPDF.isChecked()){
						GroupPdfReport report = new GroupPdfReport(this);
						report.exportToPDF();
					}else{
						//Excel
						exportToEXCEL(IExpGroup.EXP_GROUP_BUSINESS_KBN);
					}

				} else {
					new AlertDialog.Builder(this)
							.setTitle("Xuất dữ liệu " + selectedExport)
							.setMessage("Quá nhiều dữ liệu, hãy chọn điều kiện để giới hạn dữ liệu.")
							.setIcon(android.R.drawable.ic_dialog_alert)
							.setPositiveButton(getString(R.string.titleYes), (DialogInterface.OnClickListener)null)
							.show();
				}
			} else {
				Toast.makeText(this, "SDcard không sẵn sàng", Toast.LENGTH_LONG).show();
			}
			break;
		case R.id.btnReportByJapaneseLevel:
			groupParent = getDataOutput(IExpGroup.EXP_GROUP_JAPANESE);
			/** output bang thong ke luong cua cac nhan vien */
			if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
				if(groupParent.size() <= 5000) {

					if(radPDF.isChecked()){
						GroupPdfReport report = new GroupPdfReport(this);
						report.exportToPDF();
					}else{
						//Excel
						exportToEXCEL(IExpGroup.EXP_GROUP_JAPANESE);
					}

				} else {
					new AlertDialog.Builder(this)
							.setTitle("Xuất dữ liệu " + selectedExport)
							.setMessage("Quá nhiều dữ liệu, hãy chọn điều kiện để giới hạn dữ liệu.")
							.setIcon(android.R.drawable.ic_dialog_alert)
							.setPositiveButton(getString(R.string.titleYes), (DialogInterface.OnClickListener)null)
							.show();
				}
			} else {
				Toast.makeText(this, "SDcard không sẵn sàng", Toast.LENGTH_LONG).show();
			}
			break;
		case R.id.btnReportBySex:
			groupParent = getDataOutput(IExpGroup.EXP_GROUP_SEX);
			/** output bang thong ke luong cua cac nhan vien */
			if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
				if(groupParent.size() <= 5000) {

					if(radPDF.isChecked()){
						GroupPdfReport report = new GroupPdfReport(this);
						report.exportToPDF();
					}else{
						//Excel
						exportToEXCEL(IExpGroup.EXP_GROUP_SEX);
					}

				} else {
					new AlertDialog.Builder(this)
							.setTitle("Xuất dữ liệu " + selectedExport)
							.setMessage("Quá nhiều dữ liệu, hãy chọn điều kiện để giới hạn dữ liệu.")
							.setIcon(android.R.drawable.ic_dialog_alert)
							.setPositiveButton(getString(R.string.titleYes), (DialogInterface.OnClickListener)null)
							.show();
				}
			} else {
				Toast.makeText(this, "SDcard không sẵn sàng", Toast.LENGTH_LONG).show();
			}
			break;
		case R.id.btnReportByTrainingStaffInYear:
			groupParent = getDataOutput(IExpGroup.EXP_GROUP_TRAINING_YEAR);
			/** output bang thong ke luong cua cac nhan vien */
			if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
				if(groupParent.size() <= 5000) {

					if(radPDF.isChecked()){
						GroupPdfReport report = new GroupPdfReport(this);
						report.exportToPDF();
					}else{
						//Excel
						exportToEXCEL(IExpGroup.EXP_GROUP_TRAINING_YEAR);
					}

				} else {
					new AlertDialog.Builder(this)
							.setTitle("Xuất dữ liệu " + selectedExport)
							.setMessage("Quá nhiều dữ liệu, hãy chọn điều kiện để giới hạn dữ liệu.")
							.setIcon(android.R.drawable.ic_dialog_alert)
							.setPositiveButton(getString(R.string.titleYes), (DialogInterface.OnClickListener)null)
							.show();
				}
			} else {
				Toast.makeText(this, "SDcard không sẵn sàng", Toast.LENGTH_LONG).show();
			}
			break;

		case R.id.btnReportByContractStaffInYear:
			groupParent = getDataOutput(IExpGroup.EXP_GROUP_CONTRACT_YEAR);
			/** output bang thong ke luong cua cac nhan vien */
			if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
				if(groupParent.size() <= 5000) {

					if(radPDF.isChecked()){
						GroupPdfReport report = new GroupPdfReport(this);
						report.exportToPDF();
					}else{
						//Excel
						exportToEXCEL(IExpGroup.EXP_GROUP_CONTRACT_YEAR);
					}

				} else {
					new AlertDialog.Builder(this)
							.setTitle("Xuất dữ liệu " + selectedExport)
							.setMessage("Quá nhiều dữ liệu, hãy chọn điều kiện để giới hạn dữ liệu.")
							.setIcon(android.R.drawable.ic_dialog_alert)
							.setPositiveButton(getString(R.string.titleYes), (DialogInterface.OnClickListener)null)
							.show();
				}
			} else {
				Toast.makeText(this, "SDcard không sẵn sàng", Toast.LENGTH_LONG).show();
			}
			break;
		case R.id.btnReportByNotContractStaffInYear:
			groupParent = getDataOutput(IExpGroup.EXP_GROUP_NOTCONTRACT_YEAR);
			/** output bang thong ke luong cua cac nhan vien */
			if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
				if(groupParent.size() <= 5000) {

					if(radPDF.isChecked()){
						GroupPdfReport report = new GroupPdfReport(this);
						report.exportToPDF();
					}else{
						//Excel
						exportToEXCEL(IExpGroup.EXP_GROUP_NOTCONTRACT_YEAR);
					}

				} else {
					new AlertDialog.Builder(this)
							.setTitle("Xuất dữ liệu " + selectedExport)
							.setMessage("Quá nhiều dữ liệu, hãy chọn điều kiện để giới hạn dữ liệu.")
							.setIcon(android.R.drawable.ic_dialog_alert)
							.setPositiveButton(getString(R.string.titleYes), (DialogInterface.OnClickListener)null)
							.show();
				}
			} else {
				Toast.makeText(this, "SDcard không sẵn sàng", Toast.LENGTH_LONG).show();
			}
			break;
		case R.id.btnReportByYasumiInYear:
			groupParent = getDataOutput(IExpGroup.EXP_GROUP_YASUMI_YEAR);
			/** output bang thong ke luong cua cac nhan vien */
			if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
				if(groupParent.size() <= 5000) {

					if(radPDF.isChecked()){
						GroupPdfReport report = new GroupPdfReport(this);
						report.exportToPDF();
					}else{
						//Excel
						exportToEXCEL(IExpGroup.EXP_GROUP_YASUMI_YEAR);
					}

				} else {
					new AlertDialog.Builder(this)
							.setTitle("Xuất dữ liệu " + selectedExport)
							.setMessage("Quá nhiều dữ liệu, hãy chọn điều kiện để giới hạn dữ liệu.")
							.setIcon(android.R.drawable.ic_dialog_alert)
							.setPositiveButton(getString(R.string.titleYes), (DialogInterface.OnClickListener)null)
							.show();
				}
			} else {
				Toast.makeText(this, "SDcard không sẵn sàng", Toast.LENGTH_LONG).show();
			}
			break;

		case R.id.btnReportByPositionNotSatified:
			groupParent = getDataOutput(IExpGroup.EXP_GROUP_STAFF_CURRENT_POSITION_NOT_SATIFIED);
			/** output bang thong ke luong cua cac nhan vien */
			if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
				if(groupParent.size() <= 5000) {

					if(radPDF.isChecked()){
						GroupPdfReport report = new GroupPdfReport(this);
						report.exportToPDF();
					}else{
						//Excel
						exportToEXCEL(IExpGroup.EXP_GROUP_STAFF_CURRENT_POSITION_NOT_SATIFIED);
					}

				} else {
					new AlertDialog.Builder(this)
							.setTitle("Xuất dữ liệu " + selectedExport)
							.setMessage("Quá nhiều dữ liệu, hãy chọn điều kiện để giới hạn dữ liệu.")
							.setIcon(android.R.drawable.ic_dialog_alert)
							.setPositiveButton(getString(R.string.titleYes), (DialogInterface.OnClickListener)null)
							.show();
				}
			} else {
				Toast.makeText(this, "SDcard không sẵn sàng", Toast.LENGTH_LONG).show();
			}
			break;
		case R.id.btnOutputPDFCancel:
			finish();
			break;
		default:
			break;
		}
	}
	
	/**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * copy Array
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    private String[] copyArray(String[] source){
    	String[] des;
    	des=new String[source.length];
    	for(int i=0 ; i<source.length; i++){
    		des[i] = source[i];
    	}
    	return des;
    }
    
	private ArrayList<ExpParent> getDataOutput(int reportType){
		List<User> list;
		ArrayList<ExpParent> arrayParents=new ArrayList<ExpParent>() ;
		ExpGroupHelper  grp =new ExpGroupHelper(getApplicationContext());
		String[] arrGroup=null,arrGroupTemp=null;

		//get data theo tung group
		arrayParents = ExpParentChildInGroup.getParentChildInGroup(reportType,getApplicationContext());
		//lay ve cac gia tri
		arrGroup = ExpParentChildInGroup.arrGroupTitle;
		grp = ExpParentChildInGroup.grpExp;
		list = ExpParentChildInGroup.listUser;

		/*switch (reportType){
			case MasterConstants.REP_BY_DEPT:
				arrGroup =grp.getGroup(IExpGroup.EXP_GROUP_DEPT);
				arrGroupTemp = copyArray(arrGroup);

				*//** here we set the parents and the children *//*
				for (int i = 0; i < arrGroup.length; i++){
					ArrayList<User> arrayChildren = new ArrayList<User>();
					*//** tạo Object để lưu trữ data tại node cha và con *//*
					ExpParent parent = new ExpParent();
					*//** insert data cho node cha *//*
					if (arrGroupTemp[i]==null){
						parent.setTitle("");
					}else{
						parent.setTitle(arrGroupTemp[i].toString());
					}
					*//** insert data cho node con *//*
					if (arrGroupTemp[i]==null){
						list = grp.getChildGroup(IExpGroup.EXP_GROUP_DEPT, "");
					}else{
						if( arrGroup[i]==null || arrGroup[i].equals("")){
							list = grp.getChildGroup(IExpGroup.EXP_GROUP_DEPT, "");
						}else{
							list = grp.getChildGroup(IExpGroup.EXP_GROUP_DEPT, arrGroup[i].toString());
						}

					}

					if (list !=null){
						for(User usr : list){
							arrayChildren.add(usr);
						}
						parent.setArrayChildren(arrayChildren);
						arrayParents.add(parent);
					}
				}
				break;

			case MasterConstants.REP_BY_TEAM:

				break;

		}*/
		return arrayParents;
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch(requestCode){
			case MasterConstants.CALL_SEARCH_ITEM_ACTIVITY_CODE:
				if (resultCode==RESULT_OK){
	    	       

				}
				break;
			case MasterConstants.CALL_SORT_ITEM_ACTIVITY_CODE:
				if (resultCode==RESULT_OK){
	    	        

				}
				break;
		}
	}
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.report_menu, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
        case R.id.menu_report_searchitem:
        	Intent intent = new Intent(this, SearchItemMainActivity.class);
			startActivityForResult(intent,MasterConstants.CALL_SEARCH_ITEM_ACTIVITY_CODE);
            return true;
        case R.id.menu_report_sortlist:
        	Intent intSort = new Intent(this, DragNDropListActivity.class);
			startActivityForResult(intSort , MasterConstants.CALL_SORT_ITEM_ACTIVITY_CODE);
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }
    
    @Override
	protected void onPause() {
		if(exportPDF != null && (exportPDF.getStatus().equals(AsyncTask.Status.RUNNING) || exportPDF.getStatus().equals(AsyncTask.Status.PENDING))) {
			exportPDF.cancel(true);
			Toast.makeText(this, "PDF Report Exporting Cancelled", Toast.LENGTH_LONG).show();
		}
		if(exportCSV != null && (exportCSV.getStatus().equals(AsyncTask.Status.RUNNING) || exportCSV.getStatus().equals(AsyncTask.Status.PENDING))) {
			exportCSV.cancel(true);
			Toast.makeText(this, "CSV Report Exporting Cancelled", Toast.LENGTH_LONG).show();
		}
		if(exportEXCEL != null && (exportEXCEL.getStatus().equals(AsyncTask.Status.RUNNING) || exportEXCEL.getStatus().equals(AsyncTask.Status.PENDING))) {
			exportEXCEL.cancel(true);
			Toast.makeText(this, "Excel Report Exporting Cancelled", Toast.LENGTH_LONG).show();
		}
		super.onPause();
	}
    
    private boolean checkStartEndDate(boolean isToShowToast) {
		if(customEndDateTextView.getText().toString().equals("") || customStartDateTextView.getText().toString().equals("")) {
			if(isToShowToast) {
				new AlertDialog.Builder(GenerateReport.this)
				.setTitle("Error")
				.setMessage("Set Start Date and End Date before exporting")
				.setIcon(android.R.drawable.ic_dialog_alert)
				//.setPositiveButton(getString(R.string.ok), (DialogInterface.OnClickListener)null)
				.show();
			}
			return false;
		}
		if(!isCombinationCorrect()) {
			new AlertDialog.Builder(GenerateReport.this)
			.setTitle("Error")
			.setMessage("End Date must be greater than Start Date")
			.setIcon(android.R.drawable.ic_dialog_alert)
			//.setPositiveButton(getString(R.string.ok), (DialogInterface.OnClickListener)null)
			.show();
			return false;
		}
		return true;
	}
	
	private boolean isCombinationCorrect() {
		if(mStartDay == mEndDay && mStartMonth == mEndMonth && mEndYear == mStartYear) {return true;}
		if(mStartYear > mEndYear) {return false;}
		if(mStartYear == mEndYear && mStartMonth > mEndMonth) {return false;}
		if(mStartYear == mEndYear && mStartMonth == mEndMonth && mStartDay > mEndDay) {return false;}
		return true;
	}

	public static byte[] getImage(String path) throws IOException {         
        java.io.InputStream is = new FileInputStream(path);               
        ByteArrayOutputStream img_bytes = new ByteArrayOutputStream(); 
        int b; 
        try { 
            while ((b = is.read()) != -1) { 
                img_bytes.write(b); 
            } 
        } finally { 
            is.close(); 
        } 
        return img_bytes.toByteArray(); 
    } 
        
    public static int getDPI() { 
                return 96 ;
    } 
	
    /**
    * Export ra PDF
    */
	private void exportToPDF(int outputReportType) {
		exportPDF = new ExportPDF().execute("pdf" ,String.valueOf(outputReportType));
	}
    
	/**
    * Export ra PDF
    */
	private void exportToEXCEL(int outputReportType) {
		exportEXCEL = new ExportEXCEL().execute("xlx",String.valueOf(outputReportType));
	}
		
    /**Xu ly activity END */
    
    /**Xu ly Export START */
	public abstract class Export extends AsyncTask<String, Void, String> {

		protected ProgressDialog progressDialog;
		protected Double totalAmount = 0.0;
		protected boolean isAmountNotEntered = false;
		protected boolean isRecordAdded = false;
		protected int totalNumberOfRecordsAdded;
		public final File fontFile = new File("assets/fonts/vuArial.ttf");
		/**
		 * Loai output report : theo nhan vien , theo nhom chuc vu....
		 * @return
		 */
		public int outputReportType;

		@Override
		protected void onPreExecute() {
			progressDialog = new ProgressDialog(GenerateReport.this);
			progressDialog.setCancelable(false);
			progressDialog.setTitle("Xuất dữ liệu");
			progressDialog.setMessage("Xin vui lòng chờ...");
			progressDialog.show();
		}
				
		@Override
		protected void onPostExecute(String result) {
			progressDialog.cancel();
			if(!isRecordAdded) {
				fileLocation.delete();
				new AlertDialog.Builder(GenerateReport.this)
				.setTitle("Xuất dữ liệu")
				.setMessage("Không tồn tại dữ liệu.")
				.setIcon(android.R.drawable.ic_dialog_alert)
				.setPositiveButton(getString(R.string.titleYes), (DialogInterface.OnClickListener)null)
				.show();
			} else {
				final PackageManager packageManager = getPackageManager();
				Intent intent = new Intent(Intent.ACTION_VIEW, Uri.fromFile(fileLocation));
				//get extenstion file 
				String fileExtension= MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(fileLocation).toString());
				if(fileExtension.equalsIgnoreCase("XLS" )){
					//chi dinh open EXCEL file
					intent.setDataAndType(Uri.fromFile(fileLocation), "application/vnd.ms-excel");
					//intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				}
				
				List<ResolveInfo> resolveInfo = packageManager.queryIntentActivities(intent,PackageManager.MATCH_DEFAULT_ONLY);
				if (resolveInfo.size() > 0) {
					Toast.makeText(GenerateReport.this, "Đường dẫn file - "+getShowLocation(), Toast.LENGTH_LONG).show();
					startActivityForResult(intent, REQUEST_CODE);
			    } else {
			    	new AlertDialog.Builder(GenerateReport.this)
			    	.setMessage(getType()+" Không tìm thấy Application Viewer, file được save tại "+getShowLocation())
			    	.setTitle("Xuất dữ liệu")
			    	.setPositiveButton(getString(R.string.titleYes), new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							finish();
						}
					})
			    	.setIcon(android.R.drawable.ic_dialog_info)
			    	.show();
			    }
				/** gui qua mail report */
				SharedPreferences prefs= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
				
				boolean isSendMail = prefs.getBoolean("config_ReportSendMail", false);
				if(isSendMail){
					if(SendMail.isNetworkConnected(getApplicationContext())){
						
						//SendMail.enableInternet(getApplicationContext(), true);
						/*
						String subject ="[QLNS]Report nhân viên";
						String emailContent ="Xin nhờ confirm report.";
						String recipents[] ;
						String recipent = prefs.getString("config_ReportSendMailTo", "xuanhoa97@gmail.com");
						recipents=recipent.split(";");
						//recipents[0]=prefs.getString("config_EmailAccount", "xuanhoa97@gmail.com");
						
						try {
							SendMail send= new SendMail(GenerateReport.this);
							//send.sendMail2(subject, emailContent,recipents, getFileLocation().getAbsolutePath());
							send.sendMail(subject, emailContent,recipents, getFileLocation().getAbsolutePath());

						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						*/
						new SendMailTask().execute(getFileLocation().getAbsolutePath());
						
					}else{
						/** bat 3G */
						//SendMail.enableInternet(GenerateReport.this, true);
						try {
							SendMail.setMobileNetworkfromLollipop(GenerateReport.this);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						new SendMailTask().execute(getFileLocation().getAbsolutePath());
						//SendMail.enableInternet(GenerateReport.this, false);
					}
				}
			}
		}
		
		protected void addFlurryEvent(int srNo) {
			Map<String, String> recordType = new HashMap<String, String>();
			recordType.put("Total Records", +srNo+"");
			recordType.put("Type Spinner", getType());
			recordType.put("Date Range",dateRange);
			recordType.put("Period Spinner", period.getSelectedItem()+"");
		}

		protected String getShowLocation(){
			return fileLocation.toString().replaceAll("/mnt", "");
		} 
		/** get path cua file PDF sau khi output */
		public File getFileLocation(){
			return fileLocation;
		}
		/** setting tri cho bien so isRecordAdded */
		public void setIsRecordAdded(boolean value ){
			isRecordAdded = value;
		}
		/** get danh sach nhan vien can output */
		public List<User> getDataList(){
			return mEntryList;
		}
		/** get danh sach nhan vien can output theo tung group */
		public ArrayList<ExpParent>  getGroupDataOutput(){
			return groupParent;
		}
		
		protected abstract String getType();

		protected void setFile(int report_type) {
			File dir = new File(Environment.getExternalStorageDirectory()+"/EmployeeTracker");
	        if(!dir.exists()) {dir.mkdirs();}
	        fileLocation = new File(dir, getFileName(report_type));
		}
		
		protected String getFileName(int report_type) {
			String filename ="Report_";
			switch(report_type){
				case IExpGroup.EXP_GROUP_DEPT:
					filename =filename + "phong ban";
					break;
				
				case IExpGroup.EXP_GROUP_TEAM:
					filename =filename + "nhom";
					break;
				case IExpGroup.EXP_GROUP_POSITION:
					filename =filename + "chu vu";
					break;
				case IExpGroup.EXP_GROUP_SEX:
					filename =filename + "gioi tinh";
					break;
				case IExpGroup.EXP_GROUP_JAPANESE:
					filename =filename + "tieng nhat";
					break;
				case IExpGroup.EXP_GROUP_BUSINESS_KBN:
					filename =filename + "chuyen mon";
					break;
				case MasterConstants.REP_BY_USER_DETAIL:
					filename =filename + "chi tiet nhan vien";
					break;
				case MasterConstants.REP_BY_USER_LIST:
					filename =filename + "danh sach nhan vien";
					break;
				case IExpGroup.EXP_GROUP_SALARY_BASIC:
					filename =filename + "muc luong";
					break;
				case IExpGroup.EXP_GROUP_TRAINING_YEAR:
					filename =filename + "thu viec trong nam";
					break;
				case IExpGroup.EXP_GROUP_CONTRACT_YEAR:
					filename =filename + "hop dong trong nam";
					break;
				case IExpGroup.EXP_GROUP_NOTCONTRACT_YEAR:
					filename =filename + "khong nhan sau thu viec";
					break;
				case IExpGroup.EXP_GROUP_YASUMI_YEAR:
					filename =filename + "nghi viec theo nam";
					break;
				case IExpGroup.EXP_GROUP_STAFF_CURRENT_POSITION_NOT_SATIFIED:
					filename =filename + "khong phu hop tham nien va ngach bac";
					break;

			}
			return filename;
		}
		
		protected boolean isDateValid(Long timeInMillis) {
			Calendar mCalendar = Calendar.getInstance();
	 		mCalendar.setTimeInMillis(timeInMillis);
	 		mCalendar.set(mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
	 		mCalendar.setFirstDayOfWeek(Calendar.MONDAY);
			Calendar startCalendar = Calendar.getInstance();
			startCalendar.set(mStartYear, mStartMonth, mStartDay, 0, 0, 0);
			startCalendar.setFirstDayOfWeek(Calendar.MONDAY);
			Calendar endCalendar = Calendar.getInstance();
			endCalendar.set(mEndYear, mEndMonth, mEndDay, 0, 0, 0);
			endCalendar.setFirstDayOfWeek(Calendar.MONDAY);
			if(mStartDay == mCalendar.get(Calendar.DAY_OF_MONTH) && mStartMonth == mCalendar.get(Calendar.MONTH) && mStartYear == mCalendar.get(Calendar.YEAR)) {return true;}
			if(mEndDay == mCalendar.get(Calendar.DAY_OF_MONTH) && mEndMonth == mCalendar.get(Calendar.MONTH) && mEndYear == mCalendar.get(Calendar.YEAR)) {return true;}
			if(mCalendar.after(startCalendar) && mCalendar.before(endCalendar)) {return true;}
			return false;
		}
		
		protected String getDescriptionIfNotPresent(String type) {
			/*if(type.equals(getString(R.string.unknown))) {
				return getString(R.string.unknown_entry);
			} else if(type.equals(getString(R.string.text))) {
				return getString(R.string.finished_textentry);
			} else if(type.equals(getString(R.string.voice))) {
				return getString(R.string.finished_voiceentry);
			} else if(type.equals(getString(R.string.camera))) {
				return getString(R.string.finished_cameraentry);
			}*/
			
			return "";
		}
		
		private class SendMailTask extends AsyncTask<String, Integer, Boolean> {
			ProgressDialog progressDialog;
			
			@Override
			protected Boolean doInBackground(String... params) {
				//fle attachment 
				String filename = params[0];
				// TODO Auto-generated method stub
				/** gui qua mail report */
				SharedPreferences prefs= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
				//SendMail.enableInternet(getApplicationContext(), true);
				String subject ="[QLNS]Report list nhân viên";
				String emailContent ="Xin nhờ confirm report.";
				String recipents[] ;
				String recipent = prefs.getString("config_ReportSendMailTo", "xuanhoa97@gmail.com");
				recipents=recipent.split(";");
				//recipents[0]=prefs.getString("config_EmailAccount", "xuanhoa97@gmail.com");
				
				try {
					SendMail send= new SendMail(GenerateReport.this);
					send.sendMail(subject, emailContent,recipents, filename);

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return false;
				}
				return true;
			}

			@Override
			protected void onPreExecute() {
			     //called before doInBackground() is started
				progressDialog = new ProgressDialog(GenerateReport.this);
				progressDialog.setCancelable(false);
				progressDialog.setTitle("Gửi mail");
				progressDialog.setMessage("Xin vui lòng chờ...");
				progressDialog.show();
			}
			
			protected void onPostExecute(Boolean result)
	        {
	        	progressDialog.cancel();
	        	String message="";
				if (result==true){
					message="Gửi thành công.";
				}else{
					message="Gửi thất bại.Hãy thử lại.";
				}
	        	AlertDialog.Builder b = new AlertDialog.Builder(GenerateReport.this);
	            b.setTitle("Gửi mail");
	
	                b.setMessage(message);
	             
	                b.setPositiveButton("OK",
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
	        
		}
	} 
	/**Xu ly Export END */
	
	
	
	/** phan output PDF chi tiet nhan vien START */
	private class ExportPDF extends Export {
		
		private Font catFont;
		private Font subFont;
		private Font tableHeader;
		private Font small;
		private PdfWriter writer;
		
		@Override
		protected String doInBackground(String... params) {
			try{
				//Loai file output
				String fileType = params[0];
				//Loai report se output
				int outputReportType = Integer.parseInt (params[1]);

				outputPDF_UserDetail();
			}catch(Exception e){
			
			}
        	return null;
		}
		
		@Override
		protected String getFileName(int report_type) {
			//return super.getFileName(report_type)+".pdf";
			//return super.getFileName(report_type)+"." + selectedExport;
			if(radPDF.isChecked()){
				return super.getFileName(report_type)+".pdf" ;
			}else{
				return super.getFileName(report_type)+".xls" ;
			}
		}
	    
		private void outputPDF_UserDetail(){
			//catFont = new Font();
			BaseFont bf = null;
			try {
				bf = BaseFont.createFont(super.fontFile.getAbsolutePath(), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
			} catch (DocumentException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			catFont = new Font(bf,15);
			subFont = new Font(bf,10);
	        tableHeader = new Font();
	        tableHeader.setStyle(Font.BOLD);
	        small = new Font();
	        Document document = new Document(PageSize.A4);
	        setFile(MasterConstants.REP_BY_USER_DETAIL);
	        fileLocation = getFileLocation();
	        try {
				writer = PdfWriter.getInstance(document, new FileOutputStream(fileLocation));
				writer.setPageEvent(new HeaderAndFooter());
				document.open();
				addMetaData(document);
				addContent_UserDetail(document);
				document.close();
				writer.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (DocumentException e) {
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}

	    private void addTable_UserDetail(Document document) throws DocumentException, IOException {
	    	/** chia thành 5 phần */
	 		PdfPTable table = new PdfPTable(5);
	 		table.setKeepTogether(true);
	 		table.setWidthPercentage(100);
	 		table.getDefaultCell().setPadding(2.0F);
			float totalWidth = ((table.getWidthPercentage()*writer.getPageSize().getWidth())/100);
			/** chia theo 5 phần trên */
			//float widths[] = {totalWidth/10,totalWidth/5,totalWidth/5,(3*totalWidth)/10,totalWidth/5};
			float widths[] = {(3*totalWidth)/10,(3*totalWidth)/10,(float) ((0.5*totalWidth)/10),(2*totalWidth)/10,(float)(1.5*totalWidth)/10};
			table.setWidths(widths);
			
			/*PdfPCell c1 = new PdfPCell(new Phrase("Ma nhan vien",tableHeader));
			c1.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(c1);

			c1 = new PdfPCell(new Phrase("Ho ten",tableHeader));
			c1.setHorizontalAlignment(Element.ALIGN_CENTER);
			c1.setRowspan(2);
			c1.setBackgroundColor(BaseColor.LIGHT_GRAY);
			table.addCell(c1);

			c1 = new PdfPCell(new Phrase("Hinh anh",tableHeader));
			c1.setHorizontalAlignment(Element.ALIGN_CENTER);
			c1.setRowspan(2);
			c1.setBackgroundColor(BaseColor.LIGHT_GRAY);
			table.addCell(c1);
			
			c1 = new PdfPCell(new Phrase("Gioi tinh",tableHeader));
			c1.setHorizontalAlignment(Element.ALIGN_CENTER);
			c1.setBackgroundColor(BaseColor.LIGHT_GRAY);
			table.addCell(c1);
			
			c1 = new PdfPCell(new Phrase("Ngay sinh",tableHeader));
			c1.setHorizontalAlignment(Element.ALIGN_CENTER);
			c1.setBackgroundColor(BaseColor.LIGHT_GRAY);
			table.addCell(c1);
			
			*//** đoạn 2 *//*
			PdfPCell c2 = new PdfPCell(new Phrase("",tableHeader));
			c2.setHorizontalAlignment(Element.ALIGN_CENTER);
			c2.setRowspan(2);
			table.addCell(c2);

			c2 = new PdfPCell(new Phrase("",tableHeader));
			c2.setHorizontalAlignment(Element.ALIGN_CENTER);
			c2.setRowspan(2);
			table.addCell(c2);

			c2 = new PdfPCell(new Phrase("",tableHeader));
			c2.setRowspan(2);
			c2.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(c2);
			
			PdfPCell c2 = new PdfPCell(new Phrase("ngay2 ",tableHeader));
			c2.setHorizontalAlignment(Element.ALIGN_CENTER);
			c2.setBackgroundColor(BaseColor.LIGHT_GRAY);
			table.addCell(c2);
			
			c2 = new PdfPCell(new Phrase("ngay 3",tableHeader));
			c2.setHorizontalAlignment(Element.ALIGN_CENTER);
			c2.setBackgroundColor(BaseColor.LIGHT_GRAY);
			table.addCell(c2);
			
			table.setHeaderRows(2);
			table.setSplitRows(false);
			table.setSplitLate(true);*/
			/** hien thi thong tin chi tiet len man hình */
			addDataToTable_UserDetail(table,document);
		}

		private void addTitle_UserDetail(Document document) throws DocumentException{
	 		Paragraph preface = new Paragraph();
			// We add one empty line
			addEmptyLine(preface, 1);
			// Lets write a big header
			preface.setAlignment(Element.ALIGN_CENTER);
			preface.add(new Paragraph("THÔNG TIN CHI TIẾT NHÂN VIÊN", catFont));
			addEmptyLine(preface, 1);
			document.add(preface);
		}
	    
		private void addContent_UserDetail(Document document) throws DocumentException, IOException {
	 		
	 		addDateRange_UserDetail(document);
			addTable_UserDetail(document);
		}

		private void addDateRange_UserDetail(Document document) throws DocumentException {
			Paragraph preface = new Paragraph();
			preface.setAlignment(Element.ALIGN_CENTER);
			preface.add(new Paragraph(dateRange, subFont));
			addEmptyLine(preface, 2);
			document.add(preface);
		}

		// add metadata to the PDF which can be viewed in your Adobe Reader
	 	// under File -> Properties
	 	private void addMetaData(Document document) {
	 		document.addTitle("Danh sach nhan vien");
			document.addSubject("Quan Ly Nhan Su");
			document.addKeywords("Android, PDF, Quan ly nhan su, Nhan su, Tracker, Employee Tracker");
			document.addAuthor("Hoa-NX");
			document.addCreator("Hoa-NX");
	 	}

		private void addDataToTable_UserDetail(PdfPTable table, Document document) throws DocumentException, IOException{
			int srNo = 0;
			for(int i=0 ; i < mEntryList.size() ; i++) {
				addTitle_UserDetail(document);
				
				User entry = mEntryList.get(i);
				PdfPCell c1;
				
				table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
				// Hinh anh nhan vien 
				if (entry.img_fullpath==null || entry.img_fullpath.equals("")) {
					c1 = new PdfPCell(new Phrase("",catFont));
					c1.setBorder(0);
					table.addCell(c1);
				}else{
					byte[] imageBytes = getImage(entry.img_fullpath); 
		            Image pdfImage = Image.getInstance(imageBytes);           

		            float factor = 72f * 50 / (getDPI() * 100); 
		            pdfImage.scaleAbsolute(200 * factor, 259 * factor); 
		            PdfPCell cell = new PdfPCell(pdfImage); 
		            cell.setBorder(0);
		            cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE); 
		            cell.setUseDescender(true); 
		            cell.setMinimumHeight(259 * factor);  
		            table.addCell(cell); 
				}
				addBlankCellNoBorder(table,4);
				// title ma nhan vien
				addCellNoBorder(table,"Mã nhân viên");
				// ma nhan vien
				addCellNoBorder(table,entry.code+"");
				addBlankCellNoBorder(table,3);
				
				// title ten nhan vien
				addCellNoBorder(table,"Họ và tên");
				// ten nhan vien
				//addCellNoBorder(table,VietnameseToNoSign.removeAccent( entry.full_name));
				addCellNoBorder(table,( entry.full_name));
				addBlankCellNoBorder(table,1);
				addCellNoBorder(table,"Giới tính");
				addCellNoBorder(table,String.valueOf(entry.sex).equals("0")?"Nữ":"Nam");
				
				addCellNoBorder(table,"Ngày tháng năm sinh");
				addCellNoBorder(table,String.valueOf(entry.birthday));
				addBlankCellNoBorder(table,1);
				addCellNoBorder(table,"Tuổi");
				addCellNoBorder(table,String.valueOf(DateTimeUtil.getAge(entry.birthday)));
				
				addCellNoBorder(table,"Địa chỉ");
				addCellNoBorder(table, String.valueOf(entry.address));
				addBlankCellNoBorder(table,3);
				
				addCellNoBorder(table,"Số điện thoại");
				addCellNoBorder(table, String.valueOf(entry.mobile));
				addBlankCellNoBorder(table,3);
				
				addCellNoBorder(table,"Email");
				addCellNoBorder(table, String.valueOf(entry.email));
				addBlankCellNoBorder(table,3);

				addCellNoBorder(table,"Phòng ban");
				addCellNoBorder(table, String.valueOf(entry.dept_name));
				addBlankCellNoBorder(table,3);

				addCellNoBorder(table,"Tổ");
				addCellNoBorder(table, String.valueOf(entry.team_name));
				addBlankCellNoBorder(table,3);
				
				addCellNoBorder(table,"Ngạch hiện tại");
				addCellNoBorder(table, String.valueOf(entry.position_name));
				addBlankCellNoBorder(table,3);
				

				addCellNoBorder(table,"Ngày thử việc");
				addCellNoBorder(table, String.valueOf(entry.training_date));
				addBlankCellNoBorder(table,3);

				addCellNoBorder(table,"Ngày kết thúc thử việc");
				addCellNoBorder(table, String.valueOf(entry.training_dateEnd));
				addBlankCellNoBorder(table,3);
				
				addCellNoBorder(table,"Ngày ký HĐLĐ");
				addCellNoBorder(table, String.valueOf(entry.in_date));
				/*
				String textOutput ="";
				textOutput= "Thâm niên" + String.valueOf(DateTime.getKeiken(entry.in_date) + "Tháng(" 
						+  String.valueOf(Utils.Round((DateTime.getKeiken(entry.in_date)/12.0), 1, RoundingMode.HALF_UP)) + "Năm)" );
				c1 = new PdfPCell(new Phrase(textOutput,catFont));
				c1.setColspan(3);
				
				c1.setBorder(0);
				table.addCell(c1);
				*/
				
				
				addBlankCellNoBorder(table,1);
				addCellNoBorder(table,"Thâm niên");
				addCellNoBorder(table,String.valueOf(DateTimeUtil.getKeiken(entry.in_date) + " tháng(" 
							+  String.valueOf(Utils.Round((DateTimeUtil.getKeiken(entry.in_date)/12.0), 1, RoundingMode.HALF_UP)) + " Năm)" ));
				
				
				addCellNoBorder(table,"Qui đổi thâm niên");
				String keikenOutput ="0"; 
				if(entry.convert_keiken.length()!=0){
					keikenOutput =entry.convert_keiken;
				}
				addCellNoBorder(table, keikenOutput + " tháng");
				addBlankCellNoBorder(table,3);
				
				addCellNoBorder(table,"Ngày vào nhóm labor");
				addCellNoBorder(table, String.valueOf(entry.join_date));
				addBlankCellNoBorder(table,3);

				addCellNoBorder(table,"Ngày kthúc labor");
				addCellNoBorder(table, String.valueOf(entry.labour_out_date));
				addBlankCellNoBorder(table,3);
				
				addCellNoBorder(table,"Ngày nghỉ việc");
				addCellNoBorder(table, String.valueOf(entry.out_date));
				addBlankCellNoBorder(table,3);
				

				addCellNoBorder(table,"Trình độ nhật ngữ");
				addCellNoBorder(table, String.valueOf(entry.japanese));
				addBlankCellNoBorder(table,3);

				addCellNoBorder(table,"Phụ cấp nghiệp vụ");
				addCellNoBorder(table, String.valueOf(entry.allowance_business));
				addBlankCellNoBorder(table,3);

				addCellNoBorder(table,"Phụ cấp phòng chuyên biệt");
				addCellNoBorder(table, String.valueOf(entry.allowance_room));
				addBlankCellNoBorder(table,3);
				
				addCellNoBorder(table,"Công việc");
				//addCellNoBorder(table, String.valueOf(entry.business_kbn).endsWith("0")?"Phien dich":"Lap trinh");
				//1: LTV ; 2  PD ; 3 : khac nhu tong vu ...
				String workType ="Chưa chỉ định";
				if(String.valueOf(entry.business_kbn).equals("1")){
					workType="LTV";
				}else if(String.valueOf(entry.business_kbn).equals("2")){
					workType="PD";
				} else if(String.valueOf(entry.business_kbn).equals("3")){
					workType="Khác...";
				}
				addCellNoBorder(table, workType);
				addBlankCellNoBorder(table,3);
				
				/*
				addCellNoBorder(table,"Năng suất TK");
				addCellNoBorder(table, String.valueOf(entry.detaildesign));
				addBlankCellNoBorder(table,3);

				addCellNoBorder(table,"Năng suất LT");
				addCellNoBorder(table, String.valueOf(entry.program));
				addBlankCellNoBorder(table,3);
				*/
				
				addCellNoBorder(table,"Lương cơ bản($)");
				addCellNoBorder(table, String.valueOf(entry.salary_notallowance));
				addBlankCellNoBorder(table,3);
				
				addCellNoBorder(table,"Lương+phụ cấp cố định($)");
				addCellNoBorder(table, String.valueOf(entry.salary_allowance));
				addBlankCellNoBorder(table,3);
				
				addCellNoBorder(table,"Ghi chú");
				addCellNoBorder(table, String.valueOf(entry.note));
				addBlankCellNoBorder(table,3);
				
				//table.addCell( VietnameseToNoSign.removeAccent( entry.full_name));
				/*if (entry.img_fullpath==null || entry.img_fullpath.equals("")) {
					c1 = new PdfPCell(new Phrase("",catFont));
					table.addCell(c1);
				}else{
					byte[] imageBytes = getImage(entry.img_fullpath); 
		            Image pdfImage = Image.getInstance(imageBytes);           

		            float factor = 72f * 50 / (getDPI() * 100); 
		            pdfImage.scaleAbsolute(200 * factor, 259 * factor); 
		            PdfPCell cell = new PdfPCell(pdfImage); 
		            
		            cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE); 
		            cell.setUseDescender(true); 
		            cell.setMinimumHeight(259 * factor);  
		            table.addCell(cell); 
				}
				
	            
	            // Adding date
				table.addCell( String.valueOf( entry.sex));
				
				table.addCell(  entry.birthday);
				
				table.addCell( String.valueOf( entry.in_date));
				
				table.addCell(  entry.join_date);
				*/
				/*
				// Adding location
				if(entry.location != null && !entry.location.equals("")) {
					table.addCell(entry.location);
				} else {
					table.addCell(getString(R.string.unknown_location));
				}
				
				// Adding description
				if(entry.description != null && !entry.description.equals("")) {
					table.addCell(entry.description);
				} else {
					table.addCell(getDescriptionIfNotPresent(entry.type));
				}
				
				// Adding Amount
				table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
				if(entry.amount != null && !entry.amount.equals("") && !entry.amount.contains("?")) {
					totalAmount = totalAmount + Double.parseDouble(entry.amount);
					table.addCell(new StringProcessing().getStringDoubleDecimal(entry.amount));
				} else {
					isAmountNotEntered = true;
					table.addCell("?");
				}
				table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
				*/
				isRecordAdded = true;
				setIsRecordAdded(true);
				document.add(table);
				table.flushContent();
				document.newPage();
				
				if((i+1) % 5 == 0) {
					/*document.add(table);
					table.flushContent();
					document.newPage();
					PdfPCell c2 = new PdfPCell(new Phrase("Stt",tableHeader));
					c2.setHorizontalAlignment(Element.ALIGN_CENTER);
					c2.setRowspan(2);
					c2.setBackgroundColor(BaseColor.LIGHT_GRAY);
					table.addCell(c2);

					c2 = new PdfPCell(new Phrase("Ho ten",tableHeader));
					c2.setHorizontalAlignment(Element.ALIGN_CENTER);
					c2.setRowspan(2);
					c2.setBackgroundColor(BaseColor.LIGHT_GRAY);
					table.addCell(c2);

					c2 = new PdfPCell(new Phrase("Hinh anh",tableHeader));
					c2.setHorizontalAlignment(Element.ALIGN_CENTER);
					c2.setRowspan(2);
					c2.setBackgroundColor(BaseColor.LIGHT_GRAY);
					table.addCell(c2);
					
					c2 = new PdfPCell(new Phrase("Gioi tinh",tableHeader));
					c2.setHorizontalAlignment(Element.ALIGN_CENTER);
					c2.setBackgroundColor(BaseColor.LIGHT_GRAY);
					table.addCell(c2);
					
					c2 = new PdfPCell(new Phrase("Ngay sinh",tableHeader));
					c2.setHorizontalAlignment(Element.ALIGN_CENTER);
					c2.setBackgroundColor(BaseColor.LIGHT_GRAY);
					table.addCell(c2);*/
					
					/** đoạn 2 */
					/*PdfPCell c2 = new PdfPCell(new Phrase("",tableHeader));
					c2.setHorizontalAlignment(Element.ALIGN_CENTER);
					c2.setRowspan(2);
					table.addCell(c2);

					c2 = new PdfPCell(new Phrase("",tableHeader));
					c2.setHorizontalAlignment(Element.ALIGN_CENTER);
					c2.setRowspan(2);
					table.addCell(c2);

					c2 = new PdfPCell(new Phrase("",tableHeader));
					c2.setRowspan(2);
					c2.setHorizontalAlignment(Element.ALIGN_CENTER);
					table.addCell(c2);*/
					
					/*PdfPCell c3 = new PdfPCell(new Phrase("ngay2 ",tableHeader));
					c3.setHorizontalAlignment(Element.ALIGN_CENTER);
					c3.setBackgroundColor(BaseColor.LIGHT_GRAY);
					table.addCell(c3);
					
					c3 = new PdfPCell(new Phrase("ngay 3",tableHeader));
					c3.setHorizontalAlignment(Element.ALIGN_CENTER);
					c3.setBackgroundColor(BaseColor.LIGHT_GRAY);
					table.addCell(c3);*/
					
				} 
			}
			totalNumberOfRecordsAdded = srNo;
			//addTotalAmountRow(table);
			/*document.add(table);
			table.flushContent();*/
		}

		private void addBlankCellNoBorder(PdfPTable table , int numCol){
			for(int i=1; i<=numCol;i++){
				PdfPCell c1 = new PdfPCell(new Phrase( "",catFont));
				c1.setBorder(0);
				table.addCell(c1);
			}
		}
		
		private void addCellNoBorder(PdfPTable table , String value){
			PdfPCell c1;
			if(value ==null || value.equals("null")){
				c1 = new PdfPCell(new Phrase("",catFont));
			}else{
				c1 = new PdfPCell(new Phrase( value,catFont));
			}
			c1.setBorder(0);
			table.addCell(c1);
		}
		
		private void addTotalAmountRow(PdfPTable table) {
			// Adding Serial Number
			table.addCell("");
			
			// Adding date
			table.addCell("");
			
			// Adding location
			table.addCell("");
			
			// Adding description
			table.addCell("Total Amount");
			
			// Adding Amount
			if(isAmountNotEntered) {
				table.addCell(new StringProcessing().getStringDoubleDecimal(totalAmount+"")+" ?");
			} else {
				table.addCell(new StringProcessing().getStringDoubleDecimal(totalAmount+"")+"");
			}
		}
		
		private void addEmptyLine(Paragraph paragraph, int number) {
			for (int i = 0; i < number; i++) {
				paragraph.add(new Paragraph(" "));
			}
		}
	 	
	 	public class HeaderAndFooter extends PdfPageEventHelper {

	 		protected PdfPTable footer;

	 		public HeaderAndFooter() {
	 			footer = new PdfPTable(1);
	 			footer.setTotalWidth(220);
	 			footer.getDefaultCell().setBorderWidth(0);
	 			footer.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	 			Chunk chunk = new Chunk(MasterConstants.REPORT_FOOTER_COMPANY_TEXT);
	 			chunk.setAction(new PdfAction(PdfAction.FIRSTPAGE));
	 			chunk.setFont(small);
	 			footer.addCell(new Phrase(chunk));
	 		}
	 		
	 		public void onEndPage(PdfWriter writer, Document document) {
	 	    	PdfContentByte cb = writer.getDirectContent();
	 	    	footer.writeSelectedRows(0, -1,(document.right() - document.left() - 200)+ document.leftMargin(), document.bottom() - 10, cb);
	 	    }

	 	}
	 	
	 	@Override
		protected String getType() {
			//return "PDF";
	 		if(radPDF.isChecked()){
	 			return "pdf";
			}else{
				return "xls";
			}
		}
		
	}
	/** phan output PDF chi tiet nhan vien END */
	
	/** phan output EXCEL chi tiet nhan vien START */
	private class ExportEXCEL extends Export {
		
		@Override
		protected String doInBackground(String... params) {
			try{
				//Loai file output
				String fileType = params[0];
				//Loai report se output
				int outputReportType = Integer.parseInt (params[1]);

				switch (outputReportType){
					case IExpGroup.EXP_GROUP_DEPT:
						outputEXCEL_UserByGroup(outputReportType);
						break;
					case IExpGroup.EXP_GROUP_TEAM:
						outputEXCEL_UserByGroup(outputReportType);
						break;
					case IExpGroup.EXP_GROUP_POSITION:
						outputEXCEL_UserByGroup(outputReportType);
						break;
					case IExpGroup.EXP_GROUP_SEX:
						outputEXCEL_UserByGroup(outputReportType);
						break;
					case IExpGroup.EXP_GROUP_JAPANESE:
						outputEXCEL_UserByGroup(outputReportType);
						break;
					case IExpGroup.EXP_GROUP_BUSINESS_KBN:
						outputEXCEL_UserByGroup(outputReportType);
						break;
					case IExpGroup.EXP_GROUP_SALARY_BASIC:
						outputEXCEL_UserByGroup(outputReportType);
						break;

					case IExpGroup.EXP_GROUP_TRAINING_YEAR:
						outputEXCEL_UserByGroup(outputReportType);
						break;

					case IExpGroup.EXP_GROUP_CONTRACT_YEAR:
						outputEXCEL_UserByGroup(outputReportType);
						break;

					case IExpGroup.EXP_GROUP_NOTCONTRACT_YEAR:
						outputEXCEL_UserByGroup(outputReportType);
						break;

					case IExpGroup.EXP_GROUP_YASUMI_YEAR:
						outputEXCEL_UserByGroup(outputReportType);
						break;

					case IExpGroup.EXP_GROUP_STAFF_CURRENT_POSITION_NOT_SATIFIED:
						outputEXCEL_UserByGroup(outputReportType);
						break;

					case MasterConstants.REP_BY_USER_LIST:
						outputEXCEL_UserDetail();
						break;


				}

				
			}catch(Exception e){
			
			}
        	return null;
		}
		
		@Override
		protected String getFileName(int report_type) {
			//return super.getFileName(report_type)+".pdf";
			String filename;
			if(radPDF.isChecked()){
				filename =super.getFileName(report_type)+".pdf";
			}else{
				filename =super.getFileName(report_type)+".xls";
			}
			return filename;
		}
	    
		private void outputEXCEL_UserDetail(){
			//file path
			setFile(MasterConstants.REP_BY_USER_DETAIL);
			fileLocation = getFileLocation();
			
			WorkbookSettings wbSettings = new WorkbookSettings();
			wbSettings.setLocale(new Locale("en", "EN"));		
			WritableWorkbook workbook;
			isRecordAdded = true;
			setIsRecordAdded(true);
			try {
				workbook = Workbook.createWorkbook(fileLocation, wbSettings);			
				//Excel sheet name. 0 represents first sheet
				WritableSheet sheet = workbook.createSheet("Staff List", 0);

				try {
					sheet.addCell(new Label(EXCEL_EXPORT_COLUMN.STT.ordinal(), 0, "STT")); // column and row
					sheet.addCell(new Label(EXCEL_EXPORT_COLUMN.EMP_CODE.ordinal(), 0, "Mã NV"));
					sheet.addCell(new Label(EXCEL_EXPORT_COLUMN.FULL_NAME.ordinal(), 0, "Họ và tên"));
					sheet.addCell(new Label(EXCEL_EXPORT_COLUMN.SEX.ordinal(), 0, "Giới tính"));
					sheet.addCell(new Label(EXCEL_EXPORT_COLUMN.BIRTHDAY.ordinal(), 0, "Ngày sinh"));
					sheet.addCell(new Label(EXCEL_EXPORT_COLUMN.TEL.ordinal(), 0, "Điện thoại"));
					sheet.addCell(new Label(EXCEL_EXPORT_COLUMN.ADDRESS.ordinal(), 0, "Địa chỉ"));
					sheet.addCell(new Label(EXCEL_EXPORT_COLUMN.EMAIL.ordinal(), 0, "Email"));
					sheet.addCell(new Label(EXCEL_EXPORT_COLUMN.LEARNING_START.ordinal(), 0, "Ngày bắt đầu học việc"));
					sheet.addCell(new Label(EXCEL_EXPORT_COLUMN.LEARNING_END.ordinal(), 0, "Ngày kết thúc học việc"));
					sheet.addCell(new Label(EXCEL_EXPORT_COLUMN.TRAINING_START.ordinal(), 0, "Ngày bắt đầu thử việc"));
					sheet.addCell(new Label(EXCEL_EXPORT_COLUMN.TRAINING_END.ordinal(), 0, "Ngày kết thúc thử việc"));
					sheet.addCell(new Label(EXCEL_EXPORT_COLUMN.CONTRACT_DATE.ordinal(), 0, "Ngày ký HĐ"));
					sheet.addCell(new Label(EXCEL_EXPORT_COLUMN.KEIKEN_MONTH.ordinal(), 0, "Thâm niên(Tháng)"));
					sheet.addCell(new Label(EXCEL_EXPORT_COLUMN.ESTIMATE_POINT.ordinal(), 0, "Hệ số đánh giá"));
					sheet.addCell(new Label(EXCEL_EXPORT_COLUMN.KEIKEN_CONVERT.ordinal(), 0, "Qui đổi KN(Tháng)"));
					sheet.addCell(new Label(EXCEL_EXPORT_COLUMN.END_DATE.ordinal(), 0, "Ngày nghỉ việc"));
					sheet.addCell(new Label(EXCEL_EXPORT_COLUMN.DEPT.ordinal(), 0, "Phòng ban"));
					sheet.addCell(new Label(EXCEL_EXPORT_COLUMN.TEAM.ordinal(), 0, "Nhóm"));
					sheet.addCell(new Label(EXCEL_EXPORT_COLUMN.POSITION.ordinal(), 0, "Ngạch/bậc"));
					sheet.addCell(new Label(EXCEL_EXPORT_COLUMN.JP.ordinal(), 0, "Tiếng Nhật"));
					sheet.addCell(new Label(EXCEL_EXPORT_COLUMN.WORK_KBN.ordinal(), 0, "Loại NV")); //LTV hay PD
					sheet.addCell(new Label(EXCEL_EXPORT_COLUMN.NOTE.ordinal(), 0, "Ghi chú"));
					
					for(int i=0 ; i < mEntryList.size() ; i++) {					
						User entry = mEntryList.get(i);
						
						sheet.addCell(new Label(EXCEL_EXPORT_COLUMN.STT.ordinal(), i+1, String.valueOf(i+1) ));
						sheet.addCell(new Label(EXCEL_EXPORT_COLUMN.EMP_CODE.ordinal(), i+1, String.valueOf(entry.code)));
						sheet.addCell(new Label(EXCEL_EXPORT_COLUMN.FULL_NAME.ordinal(), i+1, String.valueOf(entry.full_name)));
						if(entry.sex==0){
							sheet.addCell(new Label(EXCEL_EXPORT_COLUMN.SEX.ordinal(), i+1, "Nữ"));	
						}else{
							sheet.addCell(new Label(EXCEL_EXPORT_COLUMN.SEX.ordinal(), i+1, "Nam"));
						}
						
						//sheet.addCell(new DateTime(EXCEL_EXPORT_COLUMN.BIRTHDAY.ordinal(), i+1,DateTimeUtil.convertStringToDate(entry.birthday, MasterConstants.DATE_VN_FORMAT) ));
						sheet.addCell(new Label(EXCEL_EXPORT_COLUMN.BIRTHDAY.ordinal(), i+1,(entry.birthday)));
						sheet.addCell(new Label(EXCEL_EXPORT_COLUMN.TEL.ordinal(), i+1,(entry.mobile)));
						sheet.addCell(new Label(EXCEL_EXPORT_COLUMN.ADDRESS.ordinal(), i+1,(entry.address)));
						sheet.addCell(new Label(EXCEL_EXPORT_COLUMN.EMAIL.ordinal(), i+1,(entry.email)));
						sheet.addCell(new Label(EXCEL_EXPORT_COLUMN.LEARNING_START.ordinal(), i+1, String.valueOf(entry.learn_training_date)));
						sheet.addCell(new Label(EXCEL_EXPORT_COLUMN.LEARNING_END.ordinal(), i+1, String.valueOf(entry.learn_training_dateEnd)));
						sheet.addCell(new Label(EXCEL_EXPORT_COLUMN.TRAINING_START.ordinal(), i+1, String.valueOf(entry.training_date)));
						sheet.addCell(new Label(EXCEL_EXPORT_COLUMN.TRAINING_END.ordinal(), i+1, String.valueOf(entry.training_dateEnd)));
						sheet.addCell(new Label(EXCEL_EXPORT_COLUMN.CONTRACT_DATE.ordinal(), i+1, String.valueOf(entry.in_date)));
						
						/** lấy ngày vào công ty*/
				        Date datefrom=null;
				        if (entry.in_date!=null && entry.in_date!=""){
				        	if (DateTimeUtil.isDate(entry.in_date ,MasterConstants.DATE_VN_FORMAT)){
				        		datefrom = DateTimeUtil.convertStringToDate( entry.in_date ,MasterConstants.DATE_VN_FORMAT);
				        	}
				        }
				        /** lấy ngày hiện tại */
				        Date dateto =DateTimeUtil.getCurrentDate(MasterConstants.DATE_VN_FORMAT);
				        int keikenMonth =0;
				        				        			       
				        /** tính ra thâm niên từ lúc vào công ty chính thức */
				        if (datefrom==null){
				        }else{
				        	keikenMonth =DateTimeUtil.getFullMonthDiff(datefrom, dateto);
				        }
				        
						sheet.addCell(new Label(EXCEL_EXPORT_COLUMN.KEIKEN_MONTH.ordinal(), i+1, String.valueOf(keikenMonth)));
						sheet.addCell(new Label(EXCEL_EXPORT_COLUMN.ESTIMATE_POINT.ordinal(), i+1, String.valueOf(entry.estimate_point)));
						
						sheet.addCell(new Label(EXCEL_EXPORT_COLUMN.KEIKEN_CONVERT.ordinal(), i+1, String.valueOf(entry.convert_keiken)));
						sheet.addCell(new Label(EXCEL_EXPORT_COLUMN.END_DATE.ordinal(), i+1, String.valueOf(entry.out_date)));
												
						sheet.addCell(new Label(EXCEL_EXPORT_COLUMN.DEPT.ordinal(), i+1, String.valueOf(entry.dept_name)));
						sheet.addCell(new Label(EXCEL_EXPORT_COLUMN.TEAM.ordinal(), i+1, String.valueOf(entry.team_name)));
						sheet.addCell(new Label(EXCEL_EXPORT_COLUMN.POSITION.ordinal(), i+1, String.valueOf(entry.position_name)));
						sheet.addCell(new Label(EXCEL_EXPORT_COLUMN.JP.ordinal(), i+1, String.valueOf(entry.japanese)));
						if( entry.business_kbn.equals("1")){
							sheet.addCell(new Label(EXCEL_EXPORT_COLUMN.WORK_KBN.ordinal(), i+1, "LTV"));
						}else if (entry.business_kbn.equals("2")){
							sheet.addCell(new Label(EXCEL_EXPORT_COLUMN.WORK_KBN.ordinal(), i+1, "PD"));
						}else if (entry.business_kbn.equals("3")){
							sheet.addCell(new Label(EXCEL_EXPORT_COLUMN.WORK_KBN.ordinal(), i+1, "Loại khác(tổng vụ,QA...)"));
						}else{
							sheet.addCell(new Label(EXCEL_EXPORT_COLUMN.WORK_KBN.ordinal(), i+1, "Chưa chỉ định"));
						}
						
						sheet.addCell(new Label(EXCEL_EXPORT_COLUMN.NOTE.ordinal(), i+1, String.valueOf(entry.note)));
						
					}
					
				} catch (RowsExceededException e) {
					e.printStackTrace();
				} catch (WriteException e) {
					e.printStackTrace();
				}

				workbook.write();		
				try {
					workbook.close();
				} catch (WriteException e) {
					e.printStackTrace();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		/**
		 * Output EXCEL theo tung loai group chi dinh
		 * @param outputReportType : Loai report
         */
		private void outputEXCEL_UserByGroup(int outputReportType){
			//file path
			setFile(outputReportType);
			fileLocation = getFileLocation();

			WorkbookSettings wbSettings = new WorkbookSettings();
			wbSettings.setLocale(new Locale("en", "EN"));
			WritableWorkbook workbook;
			isRecordAdded = true;
			setIsRecordAdded(true);
			try {
				workbook = Workbook.createWorkbook(fileLocation, wbSettings);
				WritableSheet sheet =null;
				String sheetTitle ="Chua chi dinh";
				for(int dept=0 ; dept<groupParent.size();dept++){
					//xu ly cho tung dept
					//tao sheet cho tung dept
					if(groupParent.get(dept).getTitle().length()>0 ) {
						sheetTitle = groupParent.get(dept).getTitle();
					}else{
						sheetTitle ="Chua chi dinh" + dept; // de tranh trung nhau
					}

					sheet = workbook.createSheet(sheetTitle, dept);
					//chuyen tu ArrayLst thanh List
					mEntryList = groupParent.get(dept).getArrayChildren();

					try {
						sheet.addCell(new Label(EXCEL_EXPORT_COLUMN.STT.ordinal(), 0, "STT")); // column and row
						sheet.addCell(new Label(EXCEL_EXPORT_COLUMN.EMP_CODE.ordinal(), 0, "Mã NV"));
						sheet.addCell(new Label(EXCEL_EXPORT_COLUMN.FULL_NAME.ordinal(), 0, "Họ và tên"));
						sheet.addCell(new Label(EXCEL_EXPORT_COLUMN.SEX.ordinal(), 0, "Giới tính"));
						sheet.addCell(new Label(EXCEL_EXPORT_COLUMN.BIRTHDAY.ordinal(), 0, "Ngày sinh"));
						sheet.addCell(new Label(EXCEL_EXPORT_COLUMN.TEL.ordinal(), 0, "Điện thoại"));
						sheet.addCell(new Label(EXCEL_EXPORT_COLUMN.ADDRESS.ordinal(), 0, "Địa chỉ"));
						sheet.addCell(new Label(EXCEL_EXPORT_COLUMN.EMAIL.ordinal(), 0, "Email"));
						sheet.addCell(new Label(EXCEL_EXPORT_COLUMN.LEARNING_START.ordinal(), 0, "Ngày bắt đầu học việc"));
						sheet.addCell(new Label(EXCEL_EXPORT_COLUMN.LEARNING_END.ordinal(), 0, "Ngày kết thúc học việc"));
						sheet.addCell(new Label(EXCEL_EXPORT_COLUMN.TRAINING_START.ordinal(), 0, "Ngày bắt đầu thử việc"));
						sheet.addCell(new Label(EXCEL_EXPORT_COLUMN.TRAINING_END.ordinal(), 0, "Ngày kết thúc thử việc"));
						sheet.addCell(new Label(EXCEL_EXPORT_COLUMN.CONTRACT_DATE.ordinal(), 0, "Ngày ký HĐ"));
						sheet.addCell(new Label(EXCEL_EXPORT_COLUMN.KEIKEN_MONTH.ordinal(), 0, "Thâm niên(Tháng)"));
						sheet.addCell(new Label(EXCEL_EXPORT_COLUMN.ESTIMATE_POINT.ordinal(), 0, "Hệ số đánh giá"));
						sheet.addCell(new Label(EXCEL_EXPORT_COLUMN.KEIKEN_CONVERT.ordinal(), 0, "Qui đổi KN(Tháng)"));
						sheet.addCell(new Label(EXCEL_EXPORT_COLUMN.END_DATE.ordinal(), 0, "Ngày nghỉ việc"));
						sheet.addCell(new Label(EXCEL_EXPORT_COLUMN.DEPT.ordinal(), 0, "Phòng ban"));
						sheet.addCell(new Label(EXCEL_EXPORT_COLUMN.TEAM.ordinal(), 0, "Nhóm"));
						sheet.addCell(new Label(EXCEL_EXPORT_COLUMN.POSITION.ordinal(), 0, "Ngạch/bậc"));
						sheet.addCell(new Label(EXCEL_EXPORT_COLUMN.JP.ordinal(), 0, "Tiếng Nhật"));
						sheet.addCell(new Label(EXCEL_EXPORT_COLUMN.WORK_KBN.ordinal(), 0, "Loại NV")); //LTV hay PD hoac tong vu
						sheet.addCell(new Label(EXCEL_EXPORT_COLUMN.NOTE.ordinal(), 0, "Ghi chú"));

						for(int i=0 ; i < mEntryList.size() ; i++) {
							User entry = mEntryList.get(i);

							sheet.addCell(new Label(EXCEL_EXPORT_COLUMN.STT.ordinal(), i+1, String.valueOf(i+1) ));
							sheet.addCell(new Label(EXCEL_EXPORT_COLUMN.EMP_CODE.ordinal(), i+1, String.valueOf(entry.code)));
							sheet.addCell(new Label(EXCEL_EXPORT_COLUMN.FULL_NAME.ordinal(), i+1, String.valueOf(entry.full_name)));
							if(entry.sex==0){
								sheet.addCell(new Label(EXCEL_EXPORT_COLUMN.SEX.ordinal(), i+1, "Nữ"));
							}else{
								sheet.addCell(new Label(EXCEL_EXPORT_COLUMN.SEX.ordinal(), i+1, "Nam"));
							}

							//sheet.addCell(new DateTime(EXCEL_EXPORT_COLUMN.BIRTHDAY.ordinal(), i+1,DateTimeUtil.convertStringToDate(entry.birthday, MasterConstants.DATE_VN_FORMAT) ));
							sheet.addCell(new Label(EXCEL_EXPORT_COLUMN.BIRTHDAY.ordinal(), i+1,(entry.birthday)));
							sheet.addCell(new Label(EXCEL_EXPORT_COLUMN.TEL.ordinal(), i+1,(entry.mobile)));
							sheet.addCell(new Label(EXCEL_EXPORT_COLUMN.ADDRESS.ordinal(), i+1,(entry.address)));
							sheet.addCell(new Label(EXCEL_EXPORT_COLUMN.EMAIL.ordinal(), i+1,(entry.email)));
							sheet.addCell(new Label(EXCEL_EXPORT_COLUMN.LEARNING_START.ordinal(), i+1, String.valueOf(entry.learn_training_date)));
							sheet.addCell(new Label(EXCEL_EXPORT_COLUMN.LEARNING_END.ordinal(), i+1, String.valueOf(entry.learn_training_dateEnd)));
							sheet.addCell(new Label(EXCEL_EXPORT_COLUMN.TRAINING_START.ordinal(), i+1, String.valueOf(entry.training_date)));
							sheet.addCell(new Label(EXCEL_EXPORT_COLUMN.TRAINING_END.ordinal(), i+1, String.valueOf(entry.training_dateEnd)));
							sheet.addCell(new Label(EXCEL_EXPORT_COLUMN.CONTRACT_DATE.ordinal(), i+1, String.valueOf(entry.in_date)));

							/** lấy ngày vào công ty*/
							Date datefrom=null;
							if (entry.in_date!=null && entry.in_date!=""){
								if (DateTimeUtil.isDate(entry.in_date ,MasterConstants.DATE_VN_FORMAT)){
									datefrom = DateTimeUtil.convertStringToDate( entry.in_date ,MasterConstants.DATE_VN_FORMAT);
								}
							}
							/** lấy ngày hiện tại */
							Date dateto =DateTimeUtil.getCurrentDate(MasterConstants.DATE_VN_FORMAT);
							int keikenMonth =0;

							/** tính ra thâm niên từ lúc vào công ty chính thức */
							if (datefrom==null){
							}else{
								keikenMonth =DateTimeUtil.getFullMonthDiff(datefrom, dateto);
							}

							sheet.addCell(new Label(EXCEL_EXPORT_COLUMN.KEIKEN_MONTH.ordinal(), i+1, String.valueOf(keikenMonth)));
							sheet.addCell(new Label(EXCEL_EXPORT_COLUMN.ESTIMATE_POINT.ordinal(), i+1, String.valueOf(entry.estimate_point)));

							sheet.addCell(new Label(EXCEL_EXPORT_COLUMN.KEIKEN_CONVERT.ordinal(), i+1, String.valueOf(entry.convert_keiken)));
							sheet.addCell(new Label(EXCEL_EXPORT_COLUMN.END_DATE.ordinal(), i+1, String.valueOf(entry.out_date)));

							sheet.addCell(new Label(EXCEL_EXPORT_COLUMN.DEPT.ordinal(), i+1, String.valueOf(entry.dept_name)));
							sheet.addCell(new Label(EXCEL_EXPORT_COLUMN.TEAM.ordinal(), i+1, String.valueOf(entry.team_name)));
							sheet.addCell(new Label(EXCEL_EXPORT_COLUMN.POSITION.ordinal(), i+1, String.valueOf(entry.position_name)));
							sheet.addCell(new Label(EXCEL_EXPORT_COLUMN.JP.ordinal(), i+1, String.valueOf(entry.japanese)));
							if( entry.business_kbn.equals("1")){
								sheet.addCell(new Label(EXCEL_EXPORT_COLUMN.WORK_KBN.ordinal(), i+1, "LTV"));
							}else if (entry.business_kbn.equals("2")){
								sheet.addCell(new Label(EXCEL_EXPORT_COLUMN.WORK_KBN.ordinal(), i+1, "PD"));
							}else if (entry.business_kbn.equals("3")){
								sheet.addCell(new Label(EXCEL_EXPORT_COLUMN.WORK_KBN.ordinal(), i+1, "Loại khác(tổng vụ,QA...)"));
							}else{
								sheet.addCell(new Label(EXCEL_EXPORT_COLUMN.WORK_KBN.ordinal(), i+1, "Chưa chỉ định"));
							}

							sheet.addCell(new Label(EXCEL_EXPORT_COLUMN.NOTE.ordinal(), i+1, String.valueOf(entry.note)));

						}

					} catch (RowsExceededException e) {
						e.printStackTrace();
					} catch (WriteException e) {
						e.printStackTrace();
					}
				}
				workbook.write();
				try {
					workbook.close();
				} catch (WriteException e) {
					e.printStackTrace();
				}

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	 	@Override
		protected String getType() {
			//return "PDF";
	 		if(radPDF.isChecked()){
	 			return "pdf";
			}else{
				return "xls";
			}
		}
		
	}
	/** phan output EXCEL chi tiet nhan vien END */
}
