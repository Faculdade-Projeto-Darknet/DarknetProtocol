package com.darknetprotocol.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.darknetprotocol.R;
import com.darknetprotocol.SoundManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MenuActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        bottomNavigation = findViewById(R.id.bottomNavigation);
        bottomNavigation.setSelectedItemId(R.id.nav_missions);

        bottomNavigation.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            // 🔊 EFEITO SONORO: Clique de navegação do menu
            SoundManager.playSound(this, R.raw.cyber_click);

            if (id == R.id.nav_missions) {
                startActivity(new Intent(MenuActivity.this, MissionsActivity.class));
                finish();
                return true;
            }

            if (id == R.id.nav_terminal) {
                startActivity(new Intent(MenuActivity.this, TerminalActivity.class));
                finish();
                return true;
            }

            if (id == R.id.nav_profile) {
                startActivity(new Intent(MenuActivity.this, ProfileActivity.class));
                finish();
                return true;
            }

            return false;
        });
    }
}