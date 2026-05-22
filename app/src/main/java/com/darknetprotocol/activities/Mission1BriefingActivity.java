package com.darknetprotocol.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.darknetprotocol.R;

public class Mission1BriefingActivity extends AppCompatActivity {

    Button btnStartMission1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mission1_briefing);

        btnStartMission1 = findViewById(R.id.btnStartMission1);

        btnStartMission1.setOnClickListener(v -> {
            Intent intent = new Intent(
                    Mission1BriefingActivity.this,
                    MissionDetailActivity.class
            );

            startActivity(intent);

            overridePendingTransition(
                    R.anim.slide_in_right,
                    R.anim.slide_out_left
            );
        });
    }
}