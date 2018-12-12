package com.ofnicon.stopdrinking.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.ofnicon.stopdrinking.R;

public class YesActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yes);

        findViewById(R.id.howitworks_button).setOnClickListener(this);
        findViewById(R.id.close_button).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.howitworks_button:
                startActivity(new Intent(this, HowItWorksActivity.class));
                break;
            case R.id.close_button:
                finish();
                break;
        }
    }

}
