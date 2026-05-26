package com.darknetprotocol.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.darknetprotocol.R;
import com.darknetprotocol.utils.SoundManager;

public class Mission3BriefingActivity extends AppCompatActivity {

    Button btnStartMission3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mission3_briefing);

        btnStartMission3 = findViewById(R.id.btnStartMission3);

        btnStartMission3.setOnClickListener(v -> {
            // 🔊 EFEITO SONORO: Clique ao iniciar a missão
            SoundManager.playSound(Mission3BriefingActivity.this, R.raw.cyber_click);

            Intent intent = new Intent(
                    Mission3BriefingActivity.this,
                    DecryptMissionActivity.class
            );

            startActivity(intent);

            overridePendingTransition(
                    R.anim.slide_in_right,
                    R.anim.slide_out_left
            );
        });
    }
}