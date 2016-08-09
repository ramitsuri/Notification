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

public class MainActivity extends AppCompatActivity implements View.OnClickListener{



    private NotificationHelper notificationHelper;
    private FloatingActionButton fabAddRule;
    private RecyclerView recyclerViewRules;
    private RuleAdapter recyclerViewAdapter;
    private RecyclerView.LayoutManager recyclerViewLManager;
    private ArrayList<NotificationRule> notificationRules;
    private SQLHelper sqlHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        notificationHelper = NotificationHelper.getInstance();
        fabAddRule = (FloatingActionButton)findViewById(R.id.fab);

        recyclerViewRules = (RecyclerView) findViewById(R.id.recyclerViewRules);
        recyclerViewLManager = new LinearLayoutManager(this);
        sqlHelper = SQLHelper.getInstance(this);
        //addDummyData();
        notificationRules = sqlHelper.getAllRules();
        recyclerViewAdapter = new RuleAdapter(notificationRules);
        NotificationListener.refreshRules(this);


        fabAddRule.setOnClickListener(this);
        recyclerViewRules.setHasFixedSize(true);
        recyclerViewRules.setLayoutManager(recyclerViewLManager);
        recyclerViewRules.setAdapter(recyclerViewAdapter);

        setText(isNotificationAccessEnabled());
    }

    private void addDummyData() {
        for(int i=0;i<5;i++) {
            NotificationRule rule = new NotificationRule();
            rule.setPackageName("com.whatsapp");
            rule.setEnabled(true);
            rule.setFilterText("ramit");
            NewNotification not = new NewNotification();
            not.setOpenOriginalApp(true);
            not.setText("Your package has been delivered");
            not.setTitle("Amazon Delivery");
            rule.setNewNotification(not);
            sqlHelper.createRule(rule);
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        setText(isNotificationAccessEnabled());
        notificationRules = sqlHelper.getAllRules();
        recyclerViewAdapter.updateDataSet(notificationRules);
        NotificationListener.refreshRules(this);
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



    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.fab) {
            Intent addRuleActivityIntent = new Intent(this, AddRuleActivity.class);
            MainActivity.this.startActivity(addRuleActivityIntent);
        }
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        //sqlHelper.deleteAllRules();
    }
}
