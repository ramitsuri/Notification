package com.ramitsuri.notification;

/**
 * Created by 310247189 on 8/5/2016.
 */
public class NotificationRule {

    private String packageName;

    private String filterText;

    private NewNotification newNotification;

    public String getPackageName(){
        return this.packageName;
    }

    public void setPackageName(String packageName){
        this.packageName = packageName;
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

}
