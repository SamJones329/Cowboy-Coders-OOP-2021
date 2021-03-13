package com.example.medication_reminder_android_app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class SettingsActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_menu);
    }

    @Override
    public void onStart() {
        super.onStart();

        findViewById(R.id.settings_back_button).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onResume() {
        //TODO
        super.onResume();
    }

    @Override
    public void onPause() {
        //TODO
        super.onPause();
    }

    @Override
    public void onStop() {
        //TODO
        super.onStop();
    }

    @Override
    public void onDestroy() {
        //TODO
        super.onDestroy();
    }
}
