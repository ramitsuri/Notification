package com.ramitsuri.notification;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.provider.CalendarContract;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    Button buttonNotificationAccess;
    TextView textViewNotificationAccess;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        buttonNotificationAccess = (Button)findViewById(R.id.buttonNotificationAccess);
        buttonNotificationAccess.setOnClickListener(this);
        textViewNotificationAccess = (TextView)findViewById(R.id.textViewNotificationAccess);
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
}
