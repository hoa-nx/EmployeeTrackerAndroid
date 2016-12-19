package com.ussol.employeetracker.utils;

import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import com.ussol.employeetracker.models.ChartItem;
import com.ussol.employeetracker.models.MasterConstants;

import android.content.Context;

public class DateTimeUtil {

    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * @param  dateFrom : Ngày start 
     *         dateTo : ngày end 
     * @return Trả về số tháng giữa ngày bắt đầu và ngày kết thúc
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
	private static int Months_Between(Date dateFrom, Date dateTo){
		Calendar firstDate = new GregorianCalendar(dateTo.getYear(), dateTo.getMonth(), dateTo.getDay());
		
		Calendar secondDate = new GregorianCalendar(dateFrom.getYear(), dateFrom.getMonth(), dateFrom.getDay());
		int year = (firstDate.get(Calendar.YEAR) - secondDate.get(Calendar.YEAR));
		int months  = (firstDate.get(Calendar.MONTH)- secondDate.get(Calendar.MONTH)) ;
		int days = (firstDate.get(Calendar.DAY_OF_MONTH) >= secondDate.get(Calendar.DAY_OF_MONTH)? 0: -1);
		
		return year*12 + months + days;
	}
	
	public static double DifferenceInMonths(Date date1, Date date2)
    {
		return DifferenceInYears(date1, date2) * 12;
    }
 
    public static double DifferenceInYears(Date date1, Date date2)
    {
		double days = DifferenceInDays(date1, date2);
		return  days / 365.2425;
    }
 
    public static double DifferenceInDays(Date date1, Date date2)
    {
    	return DifferenceInHours(date1, date2) / 24.0;
    }
 
    public static double DifferenceInHours(Date date1, Date date2)
    {
    	return DifferenceInMinutes(date1, date2) / 60.0;
    }
 
    public static double DifferenceInMinutes(Date date1, Date date2)
    {
    	return DifferenceInSeconds(date1, date2) / 60.0;
    }
 
    public static double DifferenceInSeconds(Date date1, Date date2)
    {
    	return DifferenceInMilliseconds(date1, date2) / 1000.0;
    }
 
    private static double DifferenceInMilliseconds(Date date1, Date date2)
    {
    	return Math.abs(GetTimeInMilliseconds(date1) - GetTimeInMilliseconds(date2));
    }
 
    private static long GetTimeInMilliseconds(Date date)
    {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.getTimeInMillis() + cal.getTimeZone().getOffset(cal.getTimeInMillis());
    }
    
    public static String convertDateToString(Date date, String format) {
    	
	    SimpleDateFormat mySimpleDateFormat = new SimpleDateFormat(format);
	
	    return mySimpleDateFormat.format(date);
	
	}
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * @param  dateStr : chuỗi muốn chuyển đổi
     *         format : định dạng
     * @return Trả về ngày được format theo định dạng truyền vào.
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
	public static Date convertStringToDate(String dateStr, String format) {
		
		DateFormat  mySimpleDateFormat = new SimpleDateFormat(format);
		Date date=null;
		try {
			date = mySimpleDateFormat.parse( dateStr);
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		//Calendar cal = Calendar.getInstance();
		Calendar cal = new GregorianCalendar();
		cal.setTime(date);
	   
        return cal.getTime();
	
	   
	}
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     *  @param format : định dạng format
     *         
     * @return Trả về ngày hiện tại theo format chỉ định
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
	public static Date getCurrentDate(String format){
		Calendar currentDate = Calendar.getInstance();
		SimpleDateFormat formatter=new SimpleDateFormat(format);
		String dateNow = formatter.format(currentDate.getTime());
		try {
			return formatter.parse(dateNow);
		} catch (ParseException e) {
			 return null;
		}
	}
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * @param format : định dạng format
     *         
     * @return Trả về ngày giờ hiện tại theo format chỉ định
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
	public static Date getCurrentDateTime(String format){
		Calendar currentDate = Calendar.getInstance();
		SimpleDateFormat formatter=new SimpleDateFormat(format);
		String dateNow = formatter.format(currentDate.getTime());
		try {
				return formatter.parse(dateNow);
		} catch (ParseException e) {
				 return null;
		}
	}
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * @param dateToValidate : Date muốn check
     *        dateFromat : format  
     * @return Trả về true nếu là Date ngược lại là false
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
	public static boolean isDate(String dateToValidate, String dateFromat){
		if(dateToValidate == null || dateToValidate == ""){
			return false;
		}
 
		SimpleDateFormat sdf = new SimpleDateFormat(dateFromat);
		sdf.setLenient(false);
 
		try {
 
			//if not valid, it will throw ParseException
			Date date = sdf.parse(dateToValidate);
			//System.out.println(date);
 
		} catch (ParseException e) {
 
			//e.printStackTrace();
			return false;
		}
 
		return true;
	}
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * @param format : định dạng format
     *         
     * @return Trả về chuỗi date
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
	public static String formatDate2String(String value, String oldFormat , String newFormat){
		if(isDate(value, oldFormat)){
			SimpleDateFormat sdf1 = new SimpleDateFormat(oldFormat);
		    SimpleDateFormat sdf2 = new SimpleDateFormat(newFormat);
		    try {
		    	return sdf2.format(sdf1.parse(value));
		    } catch (ParseException e) {
		    	return "";
		    }
		}else{
			return "";
		}
	}
	
	public static String formatDateTime(Context context, String timeToFormat) {

	    String finalDateTime = "";          

	    SimpleDateFormat iso8601Format = new SimpleDateFormat(MasterConstants.DATETIME_JP_FORMAT);

	    Date date = null;
	    if (timeToFormat != null) {
	        try {
	            date = iso8601Format.parse(timeToFormat);
	        } catch (ParseException e) {
	            date = null;
	        }

	        if (date != null) {
	            long when = date.getTime();
	            int flags = 0;
	            flags |= android.text.format.DateUtils.FORMAT_SHOW_TIME;
	            flags |= android.text.format.DateUtils.FORMAT_SHOW_DATE;
	            flags |= android.text.format.DateUtils.FORMAT_ABBREV_MONTH;
	            flags |= android.text.format.DateUtils.FORMAT_SHOW_YEAR;

	            finalDateTime = android.text.format.DateUtils.formatDateTime(context,
	            when + TimeZone.getDefault().getOffset(when), flags);               
	        }
	    }
	    return finalDateTime;
	}
	/** tinh so thang giua 2 date */
	@SuppressWarnings("deprecation")
	public static int getFullMonthDiff(Date dateStart, Date dateEnd) {
		GregorianCalendar d1 = new GregorianCalendar();
		GregorianCalendar d2 = new GregorianCalendar();
		d1.set(dateStart.getYear(),dateStart.getMonth(),dateStart.getDate(),0,0,0);
		d2.set(dateEnd.getYear(),dateEnd.getMonth(),dateEnd.getDate(),0,0,0);
		return getFullMonthDiff(d1,d2);
	}
	/** tinh so thang giua 2 date */
	public static int getFullMonthDiff(Calendar dt1, Calendar dt2) {
	      // Both dates are truncated to the beginning of the day
	      // dt1 and dt2 are damaged
	      // 30-Oct-2002 to 30-Nov-2002: 1 month
	      // 31-Oct-2002 to 30-Nov-2002: 1 month
	      // 30-Oct-2002 to 29-Nov-2002: < 1 month
	      Calendar a, b;
	      if (dt1.before(dt2)) {
	      	 a = dt1;
	      	 b = dt2;
	      } else {
	         a = dt2;
	         b = dt1;
	      }
	      
	      // work on the years
	      int y1 = a.get(Calendar.YEAR);
	      int y2 = b.get(Calendar.YEAR);
	      int m1 = a.get(Calendar.MONTH);
	      int m2 = b.get(Calendar.MONTH);
	      int l = y2 - y1;
	      if (m2<m1) l--; 
	      int diff = 0;
	      if (l>0) {
	         diff += l*12;
	         a.add(Calendar.YEAR,l);
	      }
	      
	      // work on the months
	      y1 = a.get(Calendar.YEAR);
	      y2 = b.get(Calendar.YEAR);
	      m1 = a.get(Calendar.MONTH);
	      m2 = b.get(Calendar.MONTH);
	      if (m2<m1) {
	      	 l = m2 + 12 - m1;
	      } else {
	         l = m2 - m1;
	      }
	      
	      if (l>0) {
	      	 diff += l;
	         a.add(Calendar.MONTH,1);
	      }
	      
	      // date adjustment
	      int d1 = a.get(Calendar.DATE);
	      int d2 = b.get(Calendar.DATE);
	      if (d2<d1) diff--;
	      
	      return diff;
	   }
	 
	/**
	 * Tinh ra tuoi cua nhan vien dua vao ngay thang nam sinh
	 * @param birthday
	 * @return
	 */
	public static double getAge(String birthday) {
		Date dateto =DateTimeUtil.getCurrentDate(MasterConstants.DATE_VN_FORMAT);
		double ageStaff =0;
		/** lấy ngày sinh*/
        Date datebirthDay=null;
        if (birthday!=null && !birthday.equals("")){
        	if (DateTimeUtil.isDate(birthday ,MasterConstants.DATE_VN_FORMAT)){
        		datebirthDay = DateTimeUtil.convertStringToDate( birthday,MasterConstants.DATE_VN_FORMAT);
        	}
        }else
        {
        	return ageStaff;
        }
        
		ageStaff =DateTimeUtil.getFullMonthDiff(datebirthDay, dateto);
    	ageStaff = Utils.Round((ageStaff/12.0), 0, RoundingMode.HALF_DOWN);
    	return ageStaff;
	}
	
	/**
	 * Tinh ra tham nien cua nhan vien ( thang )
	 * Nếu muốn tính ra năm thì String.valueOf(Utils.Round((keikenMonth/12.0), 1, RoundingMode.HALF_UP))
	 * @param contractSignDate
	 * @return
	 */
	public static int getKeiken(String contractSignDate) {
        /** lấy ngày hiện tại */
        Date dateto =DateTimeUtil.getCurrentDate(MasterConstants.DATE_VN_FORMAT);
        int keikenMonth =0;
		/** lấy ngày vào công ty*/
        Date datefrom=null;
        if (contractSignDate!=null && contractSignDate!=""){
        	if (DateTimeUtil.isDate(contractSignDate,MasterConstants.DATE_VN_FORMAT)){
        		datefrom = DateTimeUtil.convertStringToDate(contractSignDate,MasterConstants.DATE_VN_FORMAT);
        	}
        }else
        {
        	return keikenMonth;
        }

        /** tính ra thâm niên từ lúc vào công ty chính thức */
        if (datefrom==null){
        	
        }else{
        	keikenMonth =DateTimeUtil.getFullMonthDiff(datefrom, dateto);
        }
        return keikenMonth;
                       
	}
	
	/**
	 * Lây 12 tháng của năm tương ứng
	 * @param currentYear
	 * Năm
	 * @return
	 * Mảng chứa trị của 12 tháng của năm currentYear ( eg:2014-01,2014-02)
	 */
	public static ArrayList<ChartItem> get12MonthFromYear(int currentYear){
		/** Tao array gom 12 thang cua nam hien tai */
        //int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        ArrayList<ChartItem> listCurrentYearMonth = new ArrayList<ChartItem>();
        ChartItem item ;
        //Tao moi date voi 1/1
        DateTime firstMonthCurrentYear = new DateTime(currentYear, 1, 1, 0, 0);
        DateTime firstMonthCurrentYearFix = new DateTime(currentYear, 1, 1, 0, 0);
        for(int month=0;month<12;month++){
        	firstMonthCurrentYear =firstMonthCurrentYearFix.plusMonths(month);
        	DateTimeFormatter fmt = ISODateTimeFormat.yearMonth();
        	item = new ChartItem(fmt.print(firstMonthCurrentYear), "0");
        	listCurrentYearMonth.add(item);
        }
        return listCurrentYearMonth;
	}
	
	
}
