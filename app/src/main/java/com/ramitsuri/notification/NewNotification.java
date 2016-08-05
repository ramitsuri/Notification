package com.ramitsuri.notification;

/**
 * Created by 310247189 on 8/5/2016.
 */
public class NewNotification {

    private String title;

    private String text;

    private long[] vibratePattern;

    private boolean openOriginalApp;

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
}
