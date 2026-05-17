package com.darknetprotocol;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MenuActivity extends AppCompatActivity {

    Button btnMissions;
    Button btnTerminal;
    Button btnProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        btnMissions = findViewById(R.id.btnMissions);
        btnTerminal = findViewById(R.id.btnTerminal);
        btnProfile = findViewById(R.id.btnProfile);

        btnMissions.setOnClickListener(v -> {
            Intent intent = new Intent(MenuActivity.this, MissionsActivity.class);
            startActivity(intent);
        });

        btnTerminal.setOnClickListener(v -> {
            Intent intent = new Intent(MenuActivity.this, TerminalActivity.class);
            startActivity(intent);
        });

        btnProfile.setOnClickListener(v -> {
            Intent intent = new Intent(MenuActivity.this, ProfileActivity.class);
            startActivity(intent);
        });
    }
}