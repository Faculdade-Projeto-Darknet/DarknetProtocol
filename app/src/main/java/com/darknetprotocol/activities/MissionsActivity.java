package com.darknetprotocol.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.darknetprotocol.R;
import com.darknetprotocol.SoundManager;
import com.darknetprotocol.utils.PlayerPrefs;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MissionsActivity extends AppCompatActivity {

    LinearLayout mission1, mission2, mission3, mission4;
    LinearLayout layoutAvailableMissions, layoutCompletedMissions;

    Button btnAvailable, btnCompleted;
    TextView txtCompletedList;

    BottomNavigationView bottomNavigation;
    PlayerPrefs playerPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_missions);

        playerPrefs = new PlayerPrefs(this);

        mission1 = findViewById(R.id.mission1);
        mission2 = findViewById(R.id.mission2);
        mission3 = findViewById(R.id.mission3);
        mission4 = findViewById(R.id.mission4);

        layoutAvailableMissions = findViewById(R.id.layoutAvailableMissions);
        layoutCompletedMissions = findViewById(R.id.layoutCompletedMissions);

        btnAvailable = findViewById(R.id.btnAvailable);
        btnCompleted = findViewById(R.id.btnCompleted);

        txtCompletedList = findViewById(R.id.txtCompletedList);
        bottomNavigation = findViewById(R.id.bottomNavigation);

        mission1.setOnClickListener(v -> {
            SoundManager.playSound(this, R.raw.cyber_click);
            openActivity(Mission1BriefingActivity.class);
        });
        mission2.setOnClickListener(v -> {
            SoundManager.playSound(this, R.raw.cyber_click);
            openActivity(Mission2BriefingActivity.class);
        });
        mission3.setOnClickListener(v -> {
            SoundManager.playSound(this, R.raw.cyber_click);
            openActivity(Mission3BriefingActivity.class);
        });
        mission4.setOnClickListener(v -> {
            SoundManager.playSound(this, R.raw.cyber_click);
            openActivity(Mission4BriefingActivity.class);
        });

        btnAvailable.setOnClickListener(v -> {
            SoundManager.playSound(this, R.raw.cyber_click);
            showAvailable();
        });
        btnCompleted.setOnClickListener(v -> {
            SoundManager.playSound(this, R.raw.cyber_click);
            showCompleted();
        });

        setupBottomNavigation();
        showAvailable();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (bottomNavigation != null) {
            bottomNavigation.setSelectedItemId(R.id.nav_missions);
        }
        updateAvailableMissions();
    }

    private void openActivity(Class<?> activityClass) {
        Intent intent = new Intent(MissionsActivity.this, activityClass);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    private void setupBottomNavigation() {
        bottomNavigation.setSelectedItemId(R.id.nav_missions);
        bottomNavigation.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_missions) {
                return true;
            }

            // 🔊 EFEITO SONORO: Navegação entre abas principais
            SoundManager.playSound(this, R.raw.cyber_click);

            if (id == R.id.nav_terminal) {
                openActivity(TerminalActivity.class);
                return true;
            }

            if (id == R.id.nav_profile) {
                openActivity(ProfileActivity.class);
                return true;
            }

            return false;
        });
    }

    private void showAvailable() {
        layoutAvailableMissions.setVisibility(View.VISIBLE);
        layoutCompletedMissions.setVisibility(View.GONE);

        btnAvailable.setTextColor(0xFFFFFFFF);
        btnCompleted.setTextColor(0xFFAAAAAA);

        btnAvailable.setBackgroundTintList(android.content.res.ColorStateList.valueOf(0xFF087A35));
        btnCompleted.setBackgroundTintList(android.content.res.ColorStateList.valueOf(0xFF101010));

        updateAvailableMissions();
    }

    private void showCompleted() {
        layoutAvailableMissions.setVisibility(View.GONE);
        layoutCompletedMissions.setVisibility(View.VISIBLE);

        btnAvailable.setTextColor(0xFFAAAAAA);
        btnCompleted.setTextColor(0xFFFFFFFF);

        btnAvailable.setBackgroundTintList(android.content.res.ColorStateList.valueOf(0xFF101010));
        btnCompleted.setBackgroundTintList(android.content.res.ColorStateList.valueOf(0xFF087A35));

        updateCompletedMissions();
    }

    private void updateAvailableMissions() {
        mission1.setVisibility(playerPrefs.isMission1Completed() ? View.GONE : View.VISIBLE);
        mission2.setVisibility(playerPrefs.isMission2Completed() ? View.GONE : View.VISIBLE);
        mission3.setVisibility(playerPrefs.isMission3Completed() ? View.GONE : View.VISIBLE);
        mission4.setVisibility(playerPrefs.isMission4Completed() ? View.GONE : View.VISIBLE);
    }

    private void updateCompletedMissions() {
        boolean mission1Completed = playerPrefs.isMission1Completed();
        boolean mission2Completed = playerPrefs.isMission2Completed();
        boolean mission3Completed = playerPrefs.isMission3Completed();
        boolean mission4Completed = playerPrefs.isMission4Completed();

        StringBuilder completed = new StringBuilder();

        if (mission1Completed) completed.append("✓ 01 - INVASÃO À REDE\n");
        if (mission2Completed) completed.append("✓ 02 - QUEBRA DE SENHA\n");
        if (mission3Completed) completed.append("✓ 03 - ARQUIVO FANTASMA\n");
        if (mission4Completed) completed.append("✓ 04 - NÓ INVASOR\n");

        if (!mission1Completed && !mission2Completed && !mission3Completed && !mission4Completed) {
            completed.append("Nenhuma missão concluída ainda.");
        }

        txtCompletedList.setText(completed.toString());
    }
}