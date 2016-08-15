package com.ramitsuri.notification;

/**
 * Created by 310247189 on 8/5/2016.
 */
public class NotificationRule {

    private int id;

    private String packageName;

    private String appName;

    private String filterText;

    private NewNotification newNotification;

    private boolean isEnabled;

    public int getId(){
        return this.id;
    }

    public void setId(int id){
        this.id = id;
    }

    public String getPackageName(){
        return this.packageName;
    }

    public void setPackageName(String packageName){
        this.packageName = packageName;
    }

    public String getAppName(){
        return this.appName;
    }

    public void setAppName(String appName){
        this.appName = appName;
    }

    public String getFilterText(){
        return this.filterText;
    }

    public void setFilterText(String filterText){
        this.filterText = filterText;
    }

    public NewNotification getNewNotification(){
        return this.newNotification;
    }

    public void setNewNotification(NewNotification newNotification){
        this.newNotification = newNotification;
    }

    public boolean getIsEnabled(){
        return this.isEnabled;
    }

    public void setEnabled(boolean enabledStatus){
        this.isEnabled = enabledStatus;
    }

}
