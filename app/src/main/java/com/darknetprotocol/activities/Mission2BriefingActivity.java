package com.darknetprotocol.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.darknetprotocol.R;

public class Mission2BriefingActivity extends AppCompatActivity {

    Button btnStartMission2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mission2_briefing);

        btnStartMission2 = findViewById(R.id.btnStartMission2);

        btnStartMission2.setOnClickListener(v -> {
            Intent intent = new Intent(
                    Mission2BriefingActivity.this,
                    PasswordMissionActivity.class
            );

            startActivity(intent);

            overridePendingTransition(
                    R.anim.slide_in_right,
                    R.anim.slide_out_left
            );
        });
    }
}