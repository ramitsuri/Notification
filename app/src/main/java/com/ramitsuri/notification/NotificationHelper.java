package com.ramitsuri.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.service.notification.StatusBarNotification;
import android.support.v4.app.NotificationCompat;

/**
 * Created by 310247189 on 8/4/2016.
 */
public class NotificationHelper {

    private static NotificationHelper helper;

    public String selectedPackage;

    public static NotificationHelper getInstance(){
        if(helper == null)
            helper = new NotificationHelper();
        return helper;
    }

    public void generateNewNotification(Context context, StatusBarNotification statusBarNotification, NotificationRule rule) {
        Notification notification = statusBarNotification.getNotification();
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder notificationCompat = new NotificationCompat.Builder(context);
        notificationCompat.setContentTitle(rule.getNewNotification().getTitle())
                    .setContentText(rule.getNewNotification().getText())
                    .setSmallIcon(R.drawable.ic_stat_notification_notification)
                    .setVibrate(new long[]{10,400})
                    .setAutoCancel(true);
        if(rule.getNewNotification().getOpenOriginalApp())
                    notificationCompat.setContentIntent(notification.contentIntent);

        notificationManager.notify(statusBarNotification.getId(), notificationCompat.build());
    }

    public boolean isMonitoredNotification(StatusBarNotification sbn, NotificationRule rule) {
        if(sbn.getNotification().extras.getString("android.text").toLowerCase().contains(rule.getFilterText().toLowerCase())
                || sbn.getNotification().extras.getString("android.title").toLowerCase().contains(rule.getFilterText().toLowerCase()))
            return true;
        return false;
    }
}
