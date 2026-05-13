package com.darknetprotocol;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MenuActivity extends AppCompatActivity {

    Button btnMissions;
    Button btnTerminal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_menu);

        // BOTÃO MISSÕES

        btnMissions = findViewById(R.id.btnMissions);

        btnMissions.setOnClickListener(v -> {

            Intent intent = new Intent(
                    MenuActivity.this,
                    MissionsActivity.class
            );

            startActivity(intent);

        });

        // BOTÃO TERMINAL

        btnTerminal = findViewById(R.id.btnTerminal);

        btnTerminal.setOnClickListener(v -> {

            Intent intent = new Intent(
                    MenuActivity.this,
                    TerminalActivity.class
            );

            startActivity(intent);

        });

    }

}