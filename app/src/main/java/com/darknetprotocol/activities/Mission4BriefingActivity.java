package com.darknetprotocol.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import com.darknetprotocol.utils.CloudSaveManager;

import androidx.appcompat.app.AppCompatActivity;

import com.darknetprotocol.R;
import com.darknetprotocol.SoundManager;

public class Mission4BriefingActivity extends AppCompatActivity {

    Button btnStartMission4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mission4_briefing);

        btnStartMission4 = findViewById(R.id.btnStartMission4);

        btnStartMission4.setOnClickListener(v -> {
            // 🔊 EFEITO SONORO: Clique ao iniciar a missão
            SoundManager.playSound(Mission4BriefingActivity.this, R.raw.cyber_click);

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