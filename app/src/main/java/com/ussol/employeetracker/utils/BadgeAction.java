package com.ussol.employeetracker.utils;

import com.ussol.employeetracker.MainActivity;

import android.content.Context;
import android.util.Log;
/**
 * SamsungBadger
 * https://github.com/shafty023/SamsungBadger
 * @author shafty023
 *
 */
public class BadgeAction {
	Context ctx;
	
	public BadgeAction(Context context)
	{
		ctx=context;
	}
	/**
	 * get Badge 
	 */
	public void getBadge(){
		if (Badge.isBadgingSupported(ctx)) {
		    Badge badge = Badge.getBadge(ctx);

		    // This indicates there is no badge record for your app yet
		    if (badge == null) {
		        return;
		    } else {
		        //Log.d("Badge", badge.toString());
		    }
		}
	}
	/**
	 * gan thong bao 
	 * @param count
	 * So thong bao
	 */
	public void setBadgeIcon(int count){
		if (Badge.isBadgingSupported(ctx)) {
		    Badge badge = new Badge();
		    badge.mPackage = ctx.getPackageName();
		    //badge.mClass = getClass().getName(); // This should point to Activity declared as android.intent.action.MAIN
		    badge.mClass = MainActivity.class.getName();
		    badge.mBadgeCount = count;
		    badge.save(ctx);
		}
	}
	/**
	 * clear badge icon instance
	 */
	public void clearBadgeIcon(){
		if (Badge.isBadgingSupported(ctx)) {
		    Badge badge = Badge.getBadge(ctx);
		    if (badge != null) {
		        badge.mBadgeCount = 0;
		        badge.update(ctx);
		    } else {
		        // Nothing to do as this means you don't have a badge record with the BadgeProvider
		        // Thus you shouldn't even have a badge count on your icon
		    }
		}
	}
	/**
	 * Xoa cac badge
	 * @param deleteAllBadge
	 * Neu deleteAllBadge la true thi xoa toan bo.Nguoc lai thi xoa 1 
	 */
	public void deleteBagde(boolean deleteAllBadge){
		if(deleteAllBadge){
			if (Badge.isBadgingSupported(ctx)) {
			    if (Badge.deleteAllBadges(ctx)) {
			        Log.d("Badge", "Successfully deleted all badge records");
			    } else {
			        Log.d("Badge", "Failed to delete badge records");
			    }
			}
		}else{
			if (Badge.isBadgingSupported(ctx)) {
			    Badge badge = Badge.getBadge(ctx);
			    if (badge != null) {
			        if (badge.delete(ctx)) {
			            Log.d("Badge", "Successfully deleted badge record");
			        } else {
			            Log.d("Badge", "Failed to delete badge record");
			        }
			    }
			}
		}
	}
}
