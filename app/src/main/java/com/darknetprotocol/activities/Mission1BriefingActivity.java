package com.darknetprotocol.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import com.darknetprotocol.utils.CloudSaveManager;

import androidx.appcompat.app.AppCompatActivity;

import com.darknetprotocol.R;
import com.darknetprotocol.SoundManager;

public class Mission1BriefingActivity extends AppCompatActivity {

    Button btnStartMission1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mission1_briefing);

        btnStartMission1 = findViewById(R.id.btnStartMission1);

        btnStartMission1.setOnClickListener(v -> {
            // 🔊 EFEITO SONORO: Clique ao iniciar a missão
            SoundManager.playSound(Mission1BriefingActivity.this, R.raw.cyber_click);

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