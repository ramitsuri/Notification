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

    public void generateNewNotification(Context context, Notification notification, String notificationTitle, String notificationText) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder notificationCompat = new NotificationCompat.Builder(context);
        notificationCompat.setContentTitle(notificationTitle)
                    .setContentText(notificationText)
                    .setSmallIcon(android.R.drawable.ic_notification_clear_all)
                    .setContentIntent(notification.contentIntent);
        notificationManager.notify((int)System.currentTimeMillis(),notificationCompat.build());
    }

    public boolean isMonitoredNotification(StatusBarNotification sbn, NotificationRule rule) {
        if(sbn.getNotification().extras.getString("android.text").toLowerCase().contains(rule.getFilterText().toLowerCase())
                || sbn.getNotification().extras.getString("android.title").toLowerCase().contains(rule.getFilterText().toLowerCase()))
            return true;
        return false;
    }
}
