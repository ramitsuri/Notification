package com.ramitsuri.notification;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class AddRuleActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    Spinner spinnerPackages;
    private ArrayList<String> applications;
    private PackageManager packageManager;
    SQLHelper sqlHelper;
    String packageName;
    EditText editText1;
    EditText editText2;
    EditText editText3;
    CheckBox checkBox1;
    CheckBox checkBox2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_rule);
        spinnerPackages = (Spinner) findViewById(R.id.spinnerPackages);
        editText1 = (EditText)findViewById(R.id.editText1);
        editText2 = (EditText)findViewById(R.id.editText2);
        editText3 = (EditText)findViewById(R.id.editText3);
        checkBox1 = (CheckBox)findViewById(R.id.checkBox1);
        checkBox2 = (CheckBox)findViewById(R.id.checkBox2);


        packageManager = getPackageManager();
        applications = getApplications();
        sqlHelper = SQLHelper.getInstance(this);
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, applications);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPackages.setAdapter(spinnerAdapter);
        spinnerPackages.setOnItemSelectedListener(this);
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
        packageName = spinnerPackages.getItemAtPosition(i).toString();
        Toast.makeText(this, spinnerPackages.getItemAtPosition(i).toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_add_rule, menu);
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
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    private void addRule() {
        NotificationRule rule = new NotificationRule();
        rule.setPackageName(packageName);
        rule.setFilterText(editText1.getText().toString());
        rule.setEnabled(checkBox2.isChecked());
        NewNotification notification = new NewNotification();
        notification.setText(editText3.getText().toString());
        notification.setTitle(editText2.getText().toString());
        notification.setOpenOriginalApp(checkBox1.isChecked());
        rule.setNewNotification(notification);
        sqlHelper.createRule(rule);
    }
}
