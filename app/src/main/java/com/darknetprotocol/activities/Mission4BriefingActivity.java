package com.darknetprotocol.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.darknetprotocol.R;

public class Mission4BriefingActivity extends AppCompatActivity {

    Button btnStartMission4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mission4_briefing);

        btnStartMission4 = findViewById(R.id.btnStartMission4);

        btnStartMission4.setOnClickListener(v -> {
            Intent intent = new Intent(
                    Mission4BriefingActivity.this,
                    IpMissionActivity.class
            );

            startActivity(intent);

            overridePendingTransition(
                    R.anim.slide_in_right,
                    R.anim.slide_out_left
            );
        });
    }
}