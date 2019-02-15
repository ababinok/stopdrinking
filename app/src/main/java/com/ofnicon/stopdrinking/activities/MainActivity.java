package com.ofnicon.stopdrinking.activities;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.ofnicon.stopdrinking.R;
import com.ofnicon.stopdrinking.core.Core;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String CHANNEL_ID = "stop_drinking";
    private static final String CHANNEL_NAME = "Уведомления приложения бросить пить";
    private static final String CHANNEL_DESC = "Основные уведомления";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initButtons();
        initNotificationChannel();
    }

    private void initButtons() {
        findViewById(R.id.yes_button).setOnClickListener(this);
        findViewById(R.id.no_button).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.yes_button:
                Core.startNotifications(this);
                finish();
                startActivity(new Intent(this, YesActivity.class));
                break;
            case R.id.no_button:
                Core.stopNotifications(this);
                finish();
                startActivity(new Intent(this, NoActivity.class));
                break;
        }
    }

    private void initNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription(CHANNEL_DESC);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }

}
