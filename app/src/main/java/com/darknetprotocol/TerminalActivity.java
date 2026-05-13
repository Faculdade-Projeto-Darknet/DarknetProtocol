package com.darknetprotocol;

import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class TerminalActivity extends AppCompatActivity {

    TextView txtTerminal;
    Button btnHack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_terminal);

        txtTerminal = findViewById(R.id.txtTerminal);
        btnHack = findViewById(R.id.btnHack);

        btnHack.setOnClickListener(v -> {

            Handler handler = new Handler();

            handler.postDelayed(() -> {
                txtTerminal.append("\n> CONECTANDO AO SERVIDOR...");
            }, 1000);

            handler.postDelayed(() -> {
                txtTerminal.append("\n> FIREWALL DETECTADO...");
            }, 2000);

            handler.postDelayed(() -> {
                txtTerminal.append("\n> BYPASS INICIADO...");
            }, 3000);

            handler.postDelayed(() -> {
                txtTerminal.append("\n> ACESSO LIBERADO.");
            }, 4000);

        });

    }

}