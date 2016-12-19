package com.ussol.employeetracker.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Strings {
	public static final String EMPTY = "";
	public static final String SMS_NEW_LINE ="%0a";
	
	public static String InputStreamToString(InputStream inputStream){
    	InputStreamReader isr = new InputStreamReader(inputStream);  	// Input stream that translates bytes to characters
    	BufferedReader br = new BufferedReader(isr); 					// Buffered input character stream
    	String str;
    	StringBuilder output = new StringBuilder();
    	try {
			while((str = br.readLine())!= null){ output.append(str); }
		} catch (IOException e) {
			e.printStackTrace();
		}
    	return output.toString();
	}

    public static String toString( final Object o ) {
        return toString(o,"");
    }

    public static String toString( final Object o, final String def ) {
        return o==null ? def : o.toString();
    }

    public static boolean isEmpty( final String s ) {
        return s==null || s.length()==0;
    }

    public static boolean notEmpty( final String s ) {
        return s!=null && s.length()!=0;
    }

    public static boolean equal( final String a, final String b ) {
        return a==b || ( a!=null && b!=null && a.equals(b) );
    }
    /**
     * Nội dung SMS chúc mừng sinh nhật
     * @return
     */
    public static String getBirthdaySMSMessage()
    {
    	/*return "Thay mặt toàn bộ anh em trong Dept" 
    			+ SMS_NEW_LINE 
    			+ ",Chúc Bạn SINH NHẬT VUI VẺ & ẤM ÁP BÊN GIA ĐÌNH VÀ BẠN BÈ";*/
    	return "Thay mặt Dept,chúc bạn SINH NHẬT VUI VẺ & ẤM ÁP BÊN GIA ĐÌNH VÀ BẠN BÈ";
    }
}
