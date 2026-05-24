package com.darknetprotocol.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.darknetprotocol.R;
import com.darknetprotocol.SoundManager;

public class MainActivity extends AppCompatActivity {

    Button btnStart;
    TextView txtStatus;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnStart = findViewById(R.id.btnStart);
        txtStatus = findViewById(R.id.txtStatus);
        progressBar = findViewById(R.id.progressBar);

        btnStart.setOnClickListener(v -> startProtocol());
    }

    private void startProtocol() {
        // 🔊 EFEITO SONORO: Clique inicial para disparar o protocolo
        SoundManager.playSound(this, R.raw.cyber_click);

        btnStart.setEnabled(false);
        progressBar.setVisibility(View.VISIBLE);

        Handler handler = new Handler();

        txtStatus.setText("CONECTANDO...");
        progressBar.setProgress(25);

        handler.postDelayed(() -> {
            // 🔊 EFEITO SONORO: Clique de processamento
            SoundManager.playSound(this, R.raw.cyber_click);
            txtStatus.setText("ACESSANDO SERVIDOR...");
            progressBar.setProgress(50);
        }, 1000);

        handler.postDelayed(() -> {
            // 🔊 EFEITO SONORO: Clique de processamento
            SoundManager.playSound(this, R.raw.cyber_click);
            txtStatus.setText("VALIDANDO CRIPTOGRAFIA...");
            progressBar.setProgress(75);
        }, 2000);

        handler.postDelayed(() -> {
            // 🔊 EFEITO SONORO: Sucesso na liberação do túnel seguro
            SoundManager.playSound(this, R.raw.cyber_success);
            txtStatus.setText("ACESSO LIBERADO");
            progressBar.setProgress(100);
        }, 3000);

        handler.postDelayed(() -> {
            Intent intent = new Intent(
                    MainActivity.this,
                    MissionsActivity.class
            );

            startActivity(intent);

            overridePendingTransition(
                    R.anim.slide_in_right,
                    R.anim.slide_out_left
            );

            finish();

        }, 4000);
    }
}