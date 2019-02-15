package com.ofnicon.stopdrinking.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.ofnicon.stopdrinking.R;
import com.ofnicon.stopdrinking.core.Core;

public class NotificationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        Intent intent = getIntent();
        final String text = intent.getStringExtra("text");

        ((TextView) findViewById(R.id.notification_tv)).setText(text);
        findViewById(R.id.share_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Core.shareNotice(NotificationActivity.this, text);
            }
        });

    }
}
