package com.ramitsuri.notification;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by 310247189 on 8/5/2016.
 */
public class NotificationRule implements Parcelable{

    private int id;

    private String packageName;

    private String appName;

    private String filterText;

    private NewNotification newNotification;

    private boolean isEnabled;

    public NotificationRule(){

    }

    protected NotificationRule(Parcel in) {
        id = in.readInt();
        packageName = in.readString();
        appName = in.readString();
        filterText = in.readString();
        newNotification = in.readParcelable(NewNotification.class.getClassLoader());
        isEnabled = in.readByte() != 0;
    }

    public static final Creator<NotificationRule> CREATOR = new Creator<NotificationRule>() {
        @Override
        public NotificationRule createFromParcel(Parcel in) {
            return new NotificationRule(in);
        }

        @Override
        public NotificationRule[] newArray(int size) {
            return new NotificationRule[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(packageName);
        parcel.writeString(appName);
        parcel.writeString(filterText);
        parcel.writeParcelable(newNotification, i);
        parcel.writeByte((byte) (isEnabled ? 1 : 0));
    }
}
