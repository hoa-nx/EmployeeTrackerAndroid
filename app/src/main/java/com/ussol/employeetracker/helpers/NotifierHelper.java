/**
 * Copyright (c) 2012 Fujinet Software Department 5 ( USSOL team)  
 * See the file license.txt for copying permission.
*/  

package com.ussol.employeetracker.helpers;

import com.ussol.employeetracker.R;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Color;
/**
 * 
 * @author Hoa-NX
 *
 */
public class NotifierHelper {
	private static final int NOTIFICATION_ID = 0x1001;
	/**
	 * Send notification to status bar
	 * @param context
	 * @param activityToLaunch
	 * @param title
	 * @param message
	 * @param numberOfEvents
	 * @param flashLed
	 * @param vibratesendNotification
	 */

	public static void sendNotification(Context context, Class<?> activityToLaunch, String title, String message, int numberOfEvents, boolean flashLed, boolean vibrate) {
		/*
        NotificationManager notifier = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        CharSequence contentTitle = title;
		CharSequence contentText = message;
		
        final Notification notify = new Notification(R.drawable.user,contentTitle, System.currentTimeMillis());
		   
        notify.icon = R.drawable.user;
        notify.tickerText = title;
        notify.when = System.currentTimeMillis();
        notify.number = numberOfEvents;
        notify.flags |= Notification.FLAG_AUTO_CANCEL;
        notify.defaults|=Notification.DEFAULT_SOUND;
        if (flashLed) {
        	// add lights
            notify.flags |= Notification.FLAG_SHOW_LIGHTS;
            notify.ledARGB = Color.MAGENTA;
            notify.ledOnMS = 1000;
            notify.ledOffMS = 1000;
        }

        if (vibrate) {
            notify.vibrate = new long[] {100, 200, 200, 200, 200, 200, 1000, 200, 200, 200, 1000, 200};
        }

        Intent toLaunch = new Intent(context, activityToLaunch);
        PendingIntent intentBack = PendingIntent.getActivity(context, 0, toLaunch, 0);

        notify.setLatestEventInfo(context, contentTitle, contentText, intentBack);
        notifier.notify(NOTIFICATION_ID, notify);*/
		
		
		Intent notificationIntent = new Intent(context,activityToLaunch);
		PendingIntent contentIntent = PendingIntent.getActivity(context,
				NOTIFICATION_ID, notificationIntent,
		        PendingIntent.FLAG_CANCEL_CURRENT);

		NotificationManager nm = (NotificationManager) context
		        .getSystemService(Context.NOTIFICATION_SERVICE);

		Resources res = context.getResources();
		Notification.Builder builder = new Notification.Builder(context);

		builder.setContentIntent(contentIntent)
		            .setSmallIcon(R.drawable.user)
		            .setLargeIcon(BitmapFactory.decodeResource(res, R.drawable.user))
		            .setTicker(title)
		            .setWhen(System.currentTimeMillis())
		            .setAutoCancel(true)
		            .setContentTitle(title)
		            .setContentText(message);
		Notification n = builder.getNotification();

		nm.notify(NOTIFICATION_ID, n);
		
    }

}
