package com.darknetprotocol.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.darknetprotocol.R;
import com.darknetprotocol.utils.PlayerPrefs;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MissionsActivity extends AppCompatActivity {

    LinearLayout mission1;
    LinearLayout mission2;

    LinearLayout layoutAvailableMissions;
    LinearLayout layoutCompletedMissions;

    Button btnAvailable;
    Button btnCompleted;

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

        layoutAvailableMissions = findViewById(R.id.layoutAvailableMissions);
        layoutCompletedMissions = findViewById(R.id.layoutCompletedMissions);

        btnAvailable = findViewById(R.id.btnAvailable);
        btnCompleted = findViewById(R.id.btnCompleted);

        txtCompletedList = findViewById(R.id.txtCompletedList);
        bottomNavigation = findViewById(R.id.bottomNavigation);

        mission1.setOnClickListener(v -> {
            Intent intent = new Intent(
                    MissionsActivity.this,
                    MissionDetailActivity.class
            );

            startActivity(intent);

            overridePendingTransition(
                    R.anim.slide_in_right,
                    R.anim.slide_out_left
            );
        });

        mission2.setOnClickListener(v -> {
            Intent intent = new Intent(
                    MissionsActivity.this,
                    PasswordMissionActivity.class
            );

            startActivity(intent);

            overridePendingTransition(
                    R.anim.slide_in_right,
                    R.anim.slide_out_left
            );
        });

        btnAvailable.setOnClickListener(v -> showAvailable());
        btnCompleted.setOnClickListener(v -> showCompleted());

        setupBottomNavigation();

        showAvailable();
    }

    private void setupBottomNavigation() {
        bottomNavigation.setSelectedItemId(R.id.nav_missions);

        bottomNavigation.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_missions) {
                return true;
            }

            if (id == R.id.nav_terminal) {
                Intent intent = new Intent(
                        MissionsActivity.this,
                        TerminalActivity.class
                );

                startActivity(intent);

                overridePendingTransition(
                        R.anim.slide_in_right,
                        R.anim.slide_out_left
                );

                return true;
            }

            if (id == R.id.nav_profile) {
                Intent intent = new Intent(
                        MissionsActivity.this,
                        ProfileActivity.class
                );

                startActivity(intent);

                overridePendingTransition(
                        R.anim.slide_in_right,
                        R.anim.slide_out_left
                );

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

        btnAvailable.setBackgroundTintList(
                android.content.res.ColorStateList.valueOf(0xFF087A35)
        );

        btnCompleted.setBackgroundTintList(
                android.content.res.ColorStateList.valueOf(0xFF101010)
        );
    }

    private void showCompleted() {
        layoutAvailableMissions.setVisibility(View.GONE);
        layoutCompletedMissions.setVisibility(View.VISIBLE);

        btnAvailable.setTextColor(0xFFAAAAAA);
        btnCompleted.setTextColor(0xFFFFFFFF);

        btnAvailable.setBackgroundTintList(
                android.content.res.ColorStateList.valueOf(0xFF101010)
        );

        btnCompleted.setBackgroundTintList(
                android.content.res.ColorStateList.valueOf(0xFF087A35)
        );

        boolean mission1Completed = playerPrefs.isMission1Completed();
        boolean mission2Completed = playerPrefs.isMission2Completed();

        StringBuilder completed = new StringBuilder();

        if (mission1Completed) {
            completed.append("✓ 01 - INVASÃO À REDE\n");
        }

        if (mission2Completed) {
            completed.append("✓ 02 - QUEBRA DE SENHA\n");
        }

        if (!mission1Completed && !mission2Completed) {
            completed.append("Nenhuma missão concluída ainda.");
        }

        txtCompletedList.setText(completed.toString());
    }
}