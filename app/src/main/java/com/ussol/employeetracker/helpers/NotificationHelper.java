package com.ussol.employeetracker.helpers;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.transition.Visibility;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.ussol.employeetracker.R;
import com.ussol.employeetracker.models.MasterConstants;
import com.ussol.employeetracker.models.User;
import com.ussol.employeetracker.utils.DateTimeUtil;

import java.util.Date;

/**
 * Created by HOA-NX on 2017/01/02.
 */

public class NotificationHelper {
    private static Context _context;
    private static int NOTIFICATION_ID = 100;
    private static Class<?> _activityToLaunch;
    private static User _user;
    public static final String ACTION_1 = "action_1";

    public static void makeBirthdayNotification(Context context, Class<?> activityToLaunch, User user, String title, String message, int numberOfEvents, boolean flashLed, boolean vibrate) {
        _context = context;
        _activityToLaunch = activityToLaunch;
        _user = user;

        Intent intent = new Intent(context, activityToLaunch);

        PendingIntent pendingIntent = PendingIntent.getActivity(_context,
                NOTIFICATION_ID, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent action1Intent = new Intent(context, NotificationActionService.class)
                .setAction(ACTION_1);
        PendingIntent action1PendingIntent = PendingIntent.getService(context, 0,
                action1Intent, PendingIntent.FLAG_ONE_SHOT);

        // The stack builder object will contain an artificial back stack for
        // the
        // started Activity.
        // This ensures that navigating backward from the Activity leads out of
        // your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(_context);
        // Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(_activityToLaunch);
        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(intent);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(_context)
                // Set Icon
                .setSmallIcon(R.drawable.ic_app)
                // Set Ticker Message
                .setTicker("FJS Sinh nhật")
                // Dismiss Notification
                .setAutoCancel(true)
                // Set PendingIntent into Notification
                .setContentIntent(pendingIntent)
                // Set RemoteViews into Notification
                .setCustomBigContentView(getComplexNotificationView())
                .setDefaults(Notification.DEFAULT_ALL)
                //.setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
                //.addAction (R.drawable.ic_stat_dismiss,getString(R.string.dismiss), piDismiss)
                //.addAction (R.drawable.ic_stat_snooze,getString(R.string.snooze), piSnooze)
                .addAction(new NotificationCompat.Action(R.id.btnSendSMS, "Action 1", action1PendingIntent));


        //notification.bigContentView = notificationView;

        NotificationManager notificationManager = (NotificationManager) _context
                .getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }

    protected static NotificationCompat.Builder buildNotification() {
        Intent intent = new Intent(_context, _activityToLaunch);
        // Open NotificationView.java Activity
        PendingIntent pIntent = PendingIntent.getActivity(
                _context,
                NOTIFICATION_ID,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(_context)
                // Set Icon
                .setSmallIcon(R.drawable.ic_launcher)
                // Set Ticker Message
                //.setTicker(_context.getString(R.string.customnotificationticker))
                .setTicker("Hôm nay là sinh nhật của")
                // Dismiss Notification
                .setAutoCancel(true)
                // Set PendingIntent into Notification
                .setContentIntent(pIntent);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            // build a complex notification, with buttons and such
            //
            //builder = builder.setContent(getComplexNotificationView());
            builder = builder.setCustomBigContentView(getComplexNotificationView());
        } else {
            // Build a simpler notification, without buttons
            //
            builder = builder.setContentTitle("Title")
                    .setContentText("Content")
                    .setSmallIcon(R.drawable.ic_app);
        }
        return builder;
    }

    private static RemoteViews getComplexNotificationView() {
        // Using RemoteViews to bind custom layouts into Notification
        RemoteViews notificationView = new RemoteViews(
                _context.getPackageName(),
                R.layout.user_event_notification
        );

        // Locate and set the Image into customnotificationtext.xml ImageViews
        notificationView.setImageViewBitmap(R.id.imgListUser, getBitmap(_user.img_fullpath));
        // Locate and set the Text into customnotificationtext.xml TextViews
        /*Date datefrom=null;
        if (_user.in_date!=null && _user.in_date!=""){
            if (DateTimeUtil.isDate(_user.in_date , MasterConstants.DATE_VN_FORMAT)){
                datefrom = DateTimeUtil.convertStringToDate( _user.in_date ,MasterConstants.DATE_VN_FORMAT);
            }
            notificationView.setTextViewText(R.id.txtListUserInfor, _user.in_date);
        }else {
            notificationView.setTextViewText(R.id.txtListUserInfor, "");
        }*/
        String content = "";
        content = _user.dept_name + " " + _user.team_name + " " + _user.position_name + " (" + _user.birthday + ")";
        notificationView.setTextViewText(R.id.txtListUserInfor, content);

        notificationView.setTextViewText(R.id.txtListUserFullName, _user.full_name);
        notificationView.setViewVisibility(R.id.imgListUserYasumi, View.INVISIBLE);
        notificationView.setViewVisibility(R.id.txtListUserYasumiDate, View.INVISIBLE);
        return notificationView;
    }


    private static Bitmap getBitmap(String path) {
        Bitmap disBitmap = BitmapFactory.decodeFile(path);
        int desiredImageWidth = 1000;  // pixels
        int desiredImageHeight = 1200; // pixels

        BitmapFactory.Options o = new BitmapFactory.Options();
        Bitmap newImage = Bitmap.createScaledBitmap(disBitmap, desiredImageWidth, desiredImageHeight, false);
        return newImage;
    }

    public static class NotificationActionService extends IntentService {
        public NotificationActionService() {
            super(NotificationActionService.class.getSimpleName());
        }

        @Override
        protected void onHandleIntent(Intent intent) {
            String action = intent.getAction();
            //DebugUtils.log("Received notification action: " + action);
            if (ACTION_1.equals(action)) {
                // TODO: handle action 1.
                // If you want to cancel the notification: NotificationManagerCompat.from(this).cancel(NOTIFICATION_ID);
                Toast.makeText(_context, "send sms not implement", Toast.LENGTH_SHORT);
            }
        }
    }
}
