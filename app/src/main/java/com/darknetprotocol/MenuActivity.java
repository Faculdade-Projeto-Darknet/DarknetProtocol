package com.darknetprotocol;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MenuActivity extends AppCompatActivity {

    Button btnMissions;
    Button btnTerminal;
    Button btnProfile;

    BottomNavigationView bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_menu);

        bottomNavigation = findViewById(R.id.bottomNavigation);

        // MENU INFERIOR

        bottomNavigation.setSelectedItemId(R.id.nav_missions);;

        bottomNavigation.setOnItemSelectedListener(item -> {

            int id = item.getItemId();

            if (id == R.id.nav_missions) {

                startActivity(
                        new Intent(
                                MenuActivity.this,
                                MissionsActivity.class
                        )
                );

                return true;
            }

            if (id == R.id.nav_terminal) {

                startActivity(
                        new Intent(
                                MenuActivity.this,
                                TerminalActivity.class
                        )
                );

                return true;
            }

            if (id == R.id.nav_profile) {

                startActivity(
                        new Intent(
                                MenuActivity.this,
                                ProfileActivity.class
                        )
                );

                return true;
            }

            return false;
        });

    }
}