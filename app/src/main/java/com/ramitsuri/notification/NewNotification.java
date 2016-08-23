package com.ramitsuri.notification;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by 310247189 on 8/5/2016.
 */
public class NewNotification implements Parcelable {

    private String title;

    private String text;

    private long[] vibratePattern;

    private boolean openOriginalApp;

    public NewNotification(){

    }
    protected NewNotification(Parcel in) {
        title = in.readString();
        text = in.readString();
        vibratePattern = in.createLongArray();
        openOriginalApp = in.readByte() != 0;
    }

    public String getTitle(){
        return this.title;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public String getText(){
        return this.text;
    }

    public void setText(String text){
        this.text = text;
    }

    public long[] getVibratePattern(){
        return this.vibratePattern;
    }

    public void setVibratePattern(long[] pattern){
        this.vibratePattern = pattern;
    }

    public boolean getOpenOriginalApp(){
        return this.openOriginalApp;
    }

    public void setOpenOriginalApp(boolean openOriginalApp) {
        this.openOriginalApp = openOriginalApp;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeString(text);
        parcel.writeLongArray(vibratePattern);
        parcel.writeByte((byte) (openOriginalApp ? 1 : 0));
    }

    public static final Creator<NewNotification> CREATOR = new Creator<NewNotification>() {
        @Override
        public NewNotification createFromParcel(Parcel in) {
            return new NewNotification(in);
        }

        @Override
        public NewNotification[] newArray(int size) {
            return new NewNotification[size];
        }
    };
}
