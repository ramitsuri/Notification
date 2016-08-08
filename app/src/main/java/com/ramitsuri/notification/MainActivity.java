package com.ramitsuri.notification;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.ramitsuri.notification.db.SQLHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener{

    //Spinner spinnerPackages;
    private PackageManager packageManager;
    private ArrayList<String> applications;
    private NotificationHelper notificationHelper;
    private FloatingActionButton fabAddRule;
    private RecyclerView recyclerViewRules;
    private RecyclerView.Adapter recyclerViewAdapter;
    private RecyclerView.LayoutManager recyclerViewLManager;
    private ArrayList<NotificationRule> notificationRules;
    private SQLHelper sqlHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //spinnerPackages = (Spinner) findViewById(R.id.spinnerPackages);
        packageManager = getPackageManager();
        applications = getApplications();
        notificationHelper = NotificationHelper.getInstance();
        fabAddRule = (FloatingActionButton)findViewById(R.id.fab);
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, applications);
        recyclerViewRules = (RecyclerView) findViewById(R.id.recyclerViewRules);
        recyclerViewLManager = new LinearLayoutManager(this);
        sqlHelper = SQLHelper.getInstance(this);
        notificationRules = sqlHelper.getAllRules();
        recyclerViewAdapter = new RuleAdapter(notificationRules);
        NotificationListener.refreshRules(this);

        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //spinnerPackages.setAdapter(spinnerAdapter);
        //spinnerPackages.setOnItemSelectedListener(this);
        fabAddRule.setOnClickListener(this);
        recyclerViewRules.setHasFixedSize(true);
        recyclerViewRules.setLayoutManager(recyclerViewLManager);
        recyclerViewRules.setAdapter(recyclerViewAdapter);

        setText(isNotificationAccessEnabled());
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
            if (NotificationListener.class.getName().equals(service.service.getClassName())) {
                isNotificationAccessEnabled = true;
            }
        }
        return isNotificationAccessEnabled;
    }

    public void setText(boolean status) {
        final Intent settingsIntent = new Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS);
        if(status) {
            Snackbar.make(findViewById(R.id.relativeLayout), getString(R.string.snackBar_access_granted), Snackbar.LENGTH_LONG)
                    .setAction(getString(R.string.snackBar_action_revoke), new View.OnClickListener(){

                        @Override
                        public void onClick(View view) {
                            startActivity(settingsIntent);
                        }
                    }).show();
        }
        else{
            Snackbar.make(findViewById(R.id.relativeLayout), getString(R.string.snackBar_grant_access), Snackbar.LENGTH_INDEFINITE)
                    .setAction(getString(R.string.snackBar_action_grant), new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            startActivity(settingsIntent);
                        }
                    }).show();
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
        //notificationHelper.selectedPackage = spinnerPackages.getItemAtPosition(i).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.fab) {
            Intent addRuleActivityIntent = new Intent(this, AddRuleActivity.class);
            startActivity(addRuleActivityIntent);
        }
    }
}
