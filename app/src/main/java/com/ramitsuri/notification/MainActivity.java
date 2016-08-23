package com.ramitsuri.notification;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.nanotasks.BackgroundWork;
import com.nanotasks.Completion;
import com.nanotasks.Tasks;
import com.ramitsuri.notification.db.SQLHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private PackageManager packageManager;
    public ArrayList<Application> applications;
    private RuleAdapter recyclerViewAdapter;
    private ArrayList<NotificationRule> notificationRules;
    private SQLHelper sqlHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FloatingActionButton fabAddRule = (FloatingActionButton) findViewById(R.id.fab);
        packageManager = getPackageManager();
        RecyclerView recyclerViewRules = (RecyclerView) findViewById(R.id.recyclerViewRules);
        RecyclerView.LayoutManager recyclerViewLManager = new LinearLayoutManager(this);
        sqlHelper = SQLHelper.getInstance(this);
        notificationRules = sqlHelper.getAllRules();
        recyclerViewAdapter = new RuleAdapter(notificationRules);
        NotificationListener.refreshRules(this);


        fabAddRule.setOnClickListener(this);
        recyclerViewRules.setHasFixedSize(true);
        recyclerViewRules.setLayoutManager(recyclerViewLManager);
        recyclerViewRules.setAdapter(recyclerViewAdapter);

        setText(isNotificationAccessEnabled());
        Tasks.executeInBackground(this, new BackgroundWork<ArrayList<Application>>() {
            @Override
            public  ArrayList<Application> doInBackground() throws Exception {
                return getApplications(); // expensive operation
            }
        }, new Completion<ArrayList<Application>>() {
            @Override
            public void onSuccess(Context context, ArrayList<Application> applications) {
                AddRuleActivity.applications = applications;
            }
            @Override
            public void onError(Context context, Exception e) {

            }
        });
    }

    @Override
    public void onResume(){
        super.onResume();
        setText(isNotificationAccessEnabled());
        notificationRules = sqlHelper.getAllRules();
        recyclerViewAdapter.updateDataSet(notificationRules);
        NotificationListener.refreshRules(this);
    }

    public ArrayList<Application> getApplications() {
        applications = new ArrayList<>();
        List<ApplicationInfo> packages = packageManager.getInstalledApplications(0);
        for (ApplicationInfo packageInfo : packages) {
            if( packageManager.getLaunchIntentForPackage(packageInfo.packageName) != null ){
                String appName = packageManager.getApplicationLabel(packageInfo).toString();
                applications.add(new Application(packageInfo.packageName, appName));
            }
        }
        Collections.sort(applications);
        return applications;
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
        Snackbar snackbar;
        if(status) {
            snackbar = Snackbar.make(findViewById(R.id.relativeLayout), getString(R.string.snackBar_access_granted), Snackbar.LENGTH_INDEFINITE)
                    .setAction(getString(R.string.snackBar_action_revoke), new View.OnClickListener(){

                        @Override
                        public void onClick(View view) {
                            startActivity(settingsIntent);
                        }
                    });

        }
        else{
            snackbar = Snackbar.make(findViewById(R.id.relativeLayout), getString(R.string.snackBar_grant_access), Snackbar.LENGTH_INDEFINITE)
                    .setAction(getString(R.string.snackBar_action_grant), new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            startActivity(settingsIntent);
                        }
                    });
        }
        snackbar.getView().setBackgroundColor(Color.parseColor("#222222"));
        snackbar.show();
    }



    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.fab) {
            Intent addRuleActivityIntent = new Intent(this, AddRuleActivity.class);
            addRuleActivityIntent.setAction(AddRuleActivity.ACTION_CREATE);
            MainActivity.this.startActivity(addRuleActivityIntent);
        }
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        //sqlHelper.deleteAllRules();
    }
}
