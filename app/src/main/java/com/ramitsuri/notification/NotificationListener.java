package com.ramitsuri.notification;

import android.content.Context;
import android.service.notification.StatusBarNotification;

import com.ramitsuri.notification.db.SQLHelper;

import java.util.ArrayList;

public class NotificationListener extends android.service.notification.NotificationListenerService {

    NotificationHelper notificationHelper;
    static Context context;

    public static ArrayList<NotificationRule> rules;
    private static SQLHelper sqlHelper;

    public static void refreshRules(Context c){
        context = c;
        sqlHelper = SQLHelper.getInstance(context);
        rules = sqlHelper.getAllRules();

    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public NotificationListener() {
        notificationHelper = NotificationHelper.getInstance();
        //sqlHelper = SQLHelper.getInstance(getApplicationContext());
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        for (NotificationRule rule: rules) {
            if(rule.getIsEnabled()){
                if (sbn.getPackageName().equalsIgnoreCase(rule.getPackageName())) {
                    if (notificationHelper.isMonitoredNotification(sbn, rule)) {
                        NotificationListener.this.cancelNotification(sbn.getKey());
                        notificationHelper.generateNewNotification(getApplicationContext(), sbn.getNotification(), rule);
                        break;
                    }
                }
            }
        }
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {

    }
}
