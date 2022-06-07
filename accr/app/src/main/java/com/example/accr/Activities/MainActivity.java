package com.example.accr.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.LinearLayout;

import com.example.accr.AuxClasses.Animation;
import com.example.accr.R;

public class MainActivity extends AppCompatActivity {

    private LinearLayout projectInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        projectInfo = findViewById(R.id.project_info);
        projectInfo.setVisibility(View.INVISIBLE);
        Animation animation = new Animation();
        int time = 2000;
       animation.showHideElement(projectInfo, time);
        Handler handler = new Handler();
        handler.postDelayed(() -> {
            Intent intent = new Intent(MainActivity.this, LoggingActivity.class);
            startActivity(intent);
            finish();
        }, time *2);
    }

}