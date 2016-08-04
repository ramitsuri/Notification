package com.ramitsuri.notification;

import android.service.notification.StatusBarNotification;

public class NotificationListenerService extends android.service.notification.NotificationListenerService {

    NotificationHelper notificationHelper;

    public NotificationListenerService() {
        notificationHelper = NotificationHelper.getInstance();
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        if(notificationHelper.isMonitoredNotification(sbn, "")){
            NotificationListenerService.this.cancelNotification(sbn.getKey());
            notificationHelper.generateNewNotification(getApplicationContext(), sbn.getNotification(), "", "");
        }
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {

    }
}
