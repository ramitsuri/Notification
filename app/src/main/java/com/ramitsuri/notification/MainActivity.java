package com.ramitsuri.notification;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.provider.CalendarContract;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSpinner;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener{

    Button buttonNotificationAccess;
    TextView textViewNotificationAccess;
    Spinner spinnerPackages;
    PackageManager packageManager;
    ArrayList<String> applications;
    NotificationHelper notificationHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        buttonNotificationAccess = (Button)findViewById(R.id.buttonNotificationAccess);
        buttonNotificationAccess.setOnClickListener(this);
        textViewNotificationAccess = (TextView)findViewById(R.id.textViewNotificationAccess);
        spinnerPackages = (Spinner) findViewById(R.id.spinnerPackages);
        packageManager = getPackageManager();
        applications = getApplications();
        notificationHelper = NotificationHelper.getInstance();
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, applications);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPackages.setAdapter(spinnerAdapter);
        spinnerPackages.setOnItemSelectedListener(this);
        setText(isNotificationAccessEnabled());
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.buttonNotificationAccess){
            Intent settingsIntent = new Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS);
            startActivity(settingsIntent);
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        setText(isNotificationAccessEnabled());
    }

    public boolean isNotificationAccessEnabled() {
        boolean isNotificationAccessEnabled = false;
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (NotificationListenerService.class.getName().equals(service.service.getClassName())) {
                isNotificationAccessEnabled = true;
            }
        }
        return isNotificationAccessEnabled;
    }

    public void setText(boolean status) {
        if(status) {
            textViewNotificationAccess.setText("Access granted!");
            textViewNotificationAccess.setTextColor(Color.GREEN);
        }
        else{
            textViewNotificationAccess.setText("Please grant notification access");
            textViewNotificationAccess.setTextColor(Color.RED);
        }

    }

    public ArrayList<String> getApplications() {
        applications = new ArrayList<String>();
        List<ApplicationInfo> packages = packageManager.getInstalledApplications(0);
        for (ApplicationInfo packageInfo : packages) {
            if( packageManager.getLaunchIntentForPackage(packageInfo.packageName) != null ){
                applications.add(packageInfo.packageName);
            }
        }
        Collections.sort(applications);
        return applications;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        notificationHelper.selectedPackage = spinnerPackages.getItemAtPosition(i).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
