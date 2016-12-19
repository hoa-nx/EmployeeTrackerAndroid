package com.ussol.employeetracker.helpers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import android.os.AsyncTask;
import android.text.Layout.Alignment;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
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
import com.ussol.employeetracker.GenerateReport;
import com.ussol.employeetracker.models.MasterConstants;
import com.ussol.employeetracker.models.User;
import com.ussol.employeetracker.utils.VietnameseToNoSign;

public class SalaryReport {
	private File fileLocation;
	private String dateRange;
	private List<User> mEntryList;
	private AsyncTask<String, Void, String> exportPDF;
	private GenerateReport report;
	/** phan output chi tiet luong cua cac nhan vien START */
	public SalaryReport(GenerateReport rpx){
		report = rpx;
	}
	public void exportToPDF() {
		exportPDF = new ExportPDF(report).execute();
	}
			
	private class ExportPDF extends GenerateReport.Export {
		
		private Font catFont;
		private Font subFont;
		private Font tableHeader;
		private Font small;
		private PdfWriter writer;
		
		ExportPDF(GenerateReport outer){
			outer.super();
		}
		@Override
		protected String doInBackground(String... params) {
			try{
				outputPDF_UserDetail();
			}catch(Exception e){
			
			}
        	return null;
		}
		
		@Override
		protected String getFileName(int report_type) {
			return super.getFileName(report_type)+".pdf";
		}
		
	    /**
	     * Ouput thông tin chi tiết
	     */
		private void outputPDF_UserDetail(){       
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
	        
	        setFile(MasterConstants.REP_BY_USER_SALARY);
	        fileLocation = getFileLocation();
	        mEntryList = getDataList();
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
	 		table.getDefaultCell().setPadding(5.0F);
			float totalWidth = ((table.getWidthPercentage()*writer.getPageSize().getWidth())/100);
			/** chia theo 5 phần trên */
			//float widths[] = {totalWidth/10,totalWidth/5,totalWidth/5,(3*totalWidth)/10,totalWidth/5};
			float widths[] = {(float)(0.5*totalWidth)/10,(3*totalWidth)/10,(float) ((1.0*totalWidth)/10),(2*totalWidth)/10,(float)(2*totalWidth)/10};
			table.setWidths(widths);
			/** hien thi thong tin chi tiet len man hình */
			addDataToTable_UserDetail(table,document);
		}

		private void addTitle_UserDetail(Document document) throws DocumentException{
	 		Paragraph preface = new Paragraph();
			// We add one empty line
			addEmptyLine(preface, 1);
			// Lets write a big header
			preface.setAlignment(Element.ALIGN_CENTER);
			preface.add(new Paragraph("BẢNG LƯƠNG NHÂN VIÊN", catFont));
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
	 		/*document.addTitle("Detail Report using Employee Tracker (USSOL)");
	 		document.addSubject("PDF created using android app \"Employee Tracker (USSOL)\"");
	 		document.addKeywords("Android, PDF, Quan ly nhan su, Nhan su, Tracker, Employee Tracker");
	 		document.addAuthor("Hoa-NX");
	 		document.addCreator("Hoa-NX");*/
	 	}

		private void addDataToTable_UserDetail(PdfPTable table, Document document) throws DocumentException, IOException{
			int srNo = 0;
			float salaryBasic =0, salaryWithAllowance=0;
			addTitle_UserDetail(document);
			PdfPCell c;
			table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
			table.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
			// No
			addCellNoBorder(table,"STT",Element.ALIGN_CENTER);
			// Ho va ten
			addCellNoBorder(table,"Họ và tên",Element.ALIGN_CENTER);
			// Tham nien
			addCellNoBorder(table,"Thâm niên(năm)",Element.ALIGN_CENTER);
			// Luong co ban
			addCellNoBorder(table,"Lương cơ bản(USD)",Element.ALIGN_CENTER);
			// Luong tinh phu cap nghiep vu va tieng nhat
			addCellNoBorder(table,"Lương+PC(USD)",Element.ALIGN_CENTER);
			
			for(int i=0 ; i < mEntryList.size() ; i++) {
				User entry = mEntryList.get(i);
				PdfPCell c1;
				
				table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
				table.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
				// STT
				addCellNoBorder(table,String.valueOf(i+1),Element.ALIGN_LEFT);
				// Ten nhan vien
				addCellNoBorder(table, entry.full_name,Element.ALIGN_LEFT);
				//tham nien 
				addCellNoBorder(table,String.valueOf(entry.yobi_real1),Element.ALIGN_RIGHT);
				//Luong co ban 
				addCellNoBorder(table,String.valueOf(entry.salary_notallowance),Element.ALIGN_RIGHT);
				salaryBasic+= entry.salary_notallowance;
				//Luong co ban +  phu cap 
				addCellNoBorder(table,String.valueOf(entry.salary_allowance),Element.ALIGN_RIGHT);
				salaryWithAllowance+= entry.salary_allowance;
				
				isRecordAdded = true;
				setIsRecordAdded(true);
				document.add(table);
				table.flushContent();
				//document.newPage();
				
				if((i+1) % 5 == 0) {
									
				} 
			}
			totalNumberOfRecordsAdded = srNo;
			
			addTotalAmountRow(document, table, salaryBasic, salaryWithAllowance);
			/*
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
		
		private void addCellNoBorder(PdfPTable table , String value, int align){
			PdfPCell c1;
			if(value ==null || value.equals("null")){
				c1 = new PdfPCell(new Phrase("",catFont));
			}else{
				c1 = new PdfPCell(new Phrase( value,catFont));
			}
			c1.setBorder(0);
			c1.setHorizontalAlignment(align);
			table.addCell(c1);
		}
		
		private void addTotalAmountRow(Document document ,PdfPTable table , float salaryBasicTotal, float salaryWithAllowanceTotal) {
			PdfPCell c1;
			// Adding Serial Number
			c1 = new PdfPCell(new Phrase( "",catFont));
			c1.setBorder(0);
			c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
			table.addCell(c1);
			// 
			c1 = new PdfPCell(new Phrase( "Tổng cộng",catFont));
			c1.setBorder(0);
			c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
			table.addCell(c1);
			// 
			c1 = new PdfPCell(new Phrase( "",catFont));
			c1.setBorder(0);
			c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
			table.addCell(c1);
			// 
			c1 = new PdfPCell(new Phrase(String.valueOf(salaryBasicTotal),catFont));
			c1.setBorder(0);
			c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
			table.addCell(c1);
			// 
			c1 = new PdfPCell(new Phrase( String.valueOf(salaryWithAllowanceTotal),catFont));
			c1.setBorder(0);
			c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
			table.addCell(c1);			
		
			try {
				document.add(table);
			} catch (DocumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
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
	 			Chunk chunk = new Chunk("Report Generated Using - Employee Tracker(FJN)");
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
			return "PDF";
		}
		
	}
	
	/** phan output chi tiet luong cua cac nhan vien END */
}
