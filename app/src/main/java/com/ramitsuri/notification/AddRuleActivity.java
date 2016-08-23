package com.ramitsuri.notification;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.ramitsuri.notification.db.SQLHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class AddRuleActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    Spinner spinnerPackages;
    private ArrayList<Application> applications;
    private PackageManager packageManager;
    SQLHelper sqlHelper;
    Application selectedApplication;
    EditText editTextFilter;
    EditText editTextTitle;
    EditText editTextText;
    CheckBox checkBoxOriginalApp;
    SwitchCompat switchEnabled;
    ArrayAdapter<Application> spinnerAdapter;
    int startMode = 0;
    NotificationRule ruleFromClickEvent;
    public static String ACTION_CREATE = "action_create";
    public static String ACTION_EDIT = "action_edit";
    public static String RULE_TO_EDIT = "ruleToEdit";
    public static int MODE_CREATE = 0;
    public static int MODE_EDIT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_rule);

        spinnerPackages = (Spinner) findViewById(R.id.spinnerPackages);
        editTextFilter = (EditText)findViewById(R.id.editTextFilter);
        editTextTitle = (EditText)findViewById(R.id.editTextTitle);
        editTextText = (EditText)findViewById(R.id.editTextText);
        checkBoxOriginalApp = (CheckBox)findViewById(R.id.checkBoxOriginalApp);
        checkBoxOriginalApp.setChecked(true);
        switchEnabled = (SwitchCompat)findViewById(R.id.checkBox2);
        switchEnabled.setChecked(true);

        packageManager = getPackageManager();
        applications = getApplications();
        sqlHelper = SQLHelper.getInstance(this);
        spinnerAdapter = new ArrayAdapter<Application>(this, android.R.layout.simple_spinner_item, applications);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPackages.setAdapter(spinnerAdapter);
        spinnerPackages.setOnItemSelectedListener(this);
        setStartMode();
    }


    private void setStartMode() {
        Intent startIntent = getIntent();
        String action = startIntent.getAction();
        if(action.equals(ACTION_CREATE)) {
            startMode = MODE_CREATE;
            this.setTitle("Add new rule");
        }
        else if(action.equals(ACTION_EDIT)) {
            startMode = MODE_EDIT;
            this.setTitle("Edit rule");
            ruleFromClickEvent = getIntent().getParcelableExtra(RULE_TO_EDIT);
            configureViews();
        }

    }

    private void configureViews() {
        Application app = new Application("","");
        for (Application application:applications) {
            if(application.getPackageName().contentEquals(ruleFromClickEvent.getPackageName())) {
                app = application;
                break;
            }
        }
        if(!Objects.equals(app.getApplicationName(), ""))
            spinnerPackages.setSelection(spinnerAdapter.getPosition(app));

        editTextFilter.setText(ruleFromClickEvent.getFilterText());
        editTextText.setText(ruleFromClickEvent.getNewNotification().getText());
        editTextTitle.setText(ruleFromClickEvent.getNewNotification().getTitle());
        checkBoxOriginalApp.setChecked(ruleFromClickEvent.getNewNotification().getOpenOriginalApp());
        switchEnabled.setChecked(ruleFromClickEvent.getIsEnabled());
    }


    public ArrayList<Application> getApplications() {
        applications = new ArrayList<Application>();
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

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        //notificationHelper.selectedPackage = spinnerPackages.getItemAtPosition(i).toString();
        selectedApplication = (Application) spinnerPackages.getItemAtPosition(i);
        //Toast.makeText(this, spinnerPackages.getItemAtPosition(i).toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        if(startMode == MODE_CREATE) {

            inflater.inflate(R.menu.menu_add_rule, menu);

        }
        else if(startMode == MODE_EDIT){
            inflater.inflate(R.menu.menu_add_rule_edit, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_done: {
                addRule();
                this.finish();
                return true;
            }
            case R.id.action_delete:{
                sqlHelper.deleteRule(ruleFromClickEvent);
                this.finish();
                return true;
            }

            case R.id.action_done_after_edit:{
                editRule();
                this.finish();
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    private void editRule() {
        ruleFromClickEvent.setPackageName(selectedApplication.getPackageName());
        ruleFromClickEvent.setAppName(selectedApplication.getApplicationName());
        ruleFromClickEvent.setFilterText(editTextFilter.getText().toString());
        ruleFromClickEvent.setEnabled(switchEnabled.isChecked());
        ruleFromClickEvent.getNewNotification().setText(editTextText.getText().toString());
        ruleFromClickEvent.getNewNotification().setTitle(editTextTitle.getText().toString());
        ruleFromClickEvent.getNewNotification().setOpenOriginalApp(checkBoxOriginalApp.isChecked());
        sqlHelper.editRule(ruleFromClickEvent);
    }

    private void addRule() {
        NotificationRule rule = new NotificationRule();
        rule.setPackageName(selectedApplication.getPackageName());
        rule.setAppName(selectedApplication.getApplicationName());
        rule.setFilterText(editTextFilter.getText().toString());
        rule.setEnabled(switchEnabled.isChecked());
        NewNotification notification = new NewNotification();
        notification.setText(editTextText.getText().toString());
        notification.setTitle(editTextTitle.getText().toString());
        notification.setOpenOriginalApp(checkBoxOriginalApp.isChecked());
        rule.setNewNotification(notification);
        sqlHelper.createRule(rule);
    }
}
