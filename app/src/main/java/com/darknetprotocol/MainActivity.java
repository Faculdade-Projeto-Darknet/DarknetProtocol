package com.darknetprotocol;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

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

        btnStart.setOnClickListener(v -> {

            btnStart.setEnabled(false);

            Handler handler = new Handler();

            txtStatus.setText("CONECTANDO...");
            progressBar.setProgress(25);

            handler.postDelayed(() -> {

                txtStatus.setText("ACESSANDO SERVIDOR...");
                progressBar.setProgress(50);

            }, 1000);

            handler.postDelayed(() -> {

                txtStatus.setText("VALIDANDO CRIPTOGRAFIA...");
                progressBar.setProgress(75);

            }, 2000);

            handler.postDelayed(() -> {

                txtStatus.setText("ACESSO LIBERADO");
                progressBar.setProgress(100);

            }, 3000);

            handler.postDelayed(() -> {

                Intent intent = new Intent(
                        MainActivity.this,
                        MenuActivity.class
                );

                startActivity(intent);

            }, 4000);

        });

    }

}