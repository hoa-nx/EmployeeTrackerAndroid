/**
 * Copyright (c) 2012 Fujinet Software Department 5 ( USSOL team)  
 * See the file license.txt for copying permission.
*/  

package com.ussol.employeetracker.helpers;

import java.io.File;

import javax.mail.Quota.Resource;

import com.ussol.employeetracker.R;
import com.ussol.employeetracker.models.User;
import com.ussol.employeetracker.utils.RoundImage;
import com.ussol.employeetracker.utils.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

public class DecodeTask extends AsyncTask<String, Void, Bitmap> {

private static int MaxTextureSize = 2048; /* True for most devices. */

public ImageView v;
public Context _ctx;
public String _textDraw;
public  User _usr ;

public DecodeTask(ImageView iv ) {
    v = iv;
}

public DecodeTask(Context ctx , User usr, ImageView iv ) {
    v = iv;
    _ctx= ctx;
    _usr = usr;
}

protected Bitmap doInBackground(String... params) {
    BitmapFactory.Options opt = new BitmapFactory.Options();
    opt.inPurgeable = true;
    opt.inPreferQualityOverSpeed = false;
    opt.inSampleSize = 1;

    Bitmap bitmap = null;
    if(isCancelled()) {
        return bitmap;
    }
    //kiem tra file xem có ton tai?
    File f = new File(params[0]);
    if(f.exists()){
    	opt.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(params[0], opt);
        while(opt.outHeight > MaxTextureSize || opt.outWidth > MaxTextureSize) {
            opt.inSampleSize++;
            BitmapFactory.decodeFile(params[0], opt);
        }
        opt.inJustDecodeBounds = false;

        bitmap = BitmapFactory.decodeFile(params[0], opt);

    }
    else
    {
    	bitmap=null;
    }
    return bitmap;
}

@Override
protected void onPostExecute(Bitmap result) {
	String textDraw ="";
    if(v != null && result!=null) {
    	/*if(_ctx!=null){
    		if(_usr.in_date!=null){
    			textDraw ="trial";
    		}
    		if(_usr.out_date!=null){
    			textDraw ="nghỉ";
    		}
    		result =Utils.drawTextToBitmap(_ctx, result, textDraw);	
    	}*/
    	
        v.setImageBitmap(result);
        RoundImage roundedImage = new RoundImage(result);
        v.setImageDrawable(roundedImage);
    }
}

}
