package com.ramitsuri.notification;

/**
 * Created by ramitsuri on 8/14/16.
 */
public class Application implements Comparable<Application>{

    public Application(String packageName, String applicationName){
        this.applicationName = applicationName;
        this.packageName = packageName;
    }
    private String applicationName;
    private String packageName;

    public String getApplicationName(){
        return this.applicationName;
    }

    public void setApplicationName(String applicationName){
        this.applicationName = applicationName;
    }

    public String getPackageName(){
        return this.packageName;
    }

    public void setPackageName(String packageName){
        this.packageName = packageName;
    }

    @Override
    public String toString(){
        return this.applicationName;
    }

    @Override
    public int compareTo(Application application) {
            return this.applicationName.compareTo(application.applicationName);
    }
}
