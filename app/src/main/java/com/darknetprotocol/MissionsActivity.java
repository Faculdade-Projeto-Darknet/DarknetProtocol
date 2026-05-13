package com.darknetprotocol;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

public class MissionsActivity extends AppCompatActivity {

    LinearLayout mission1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_missions);

        mission1 = findViewById(R.id.mission1);

        mission1.setOnClickListener(v -> {

            Intent intent = new Intent(
                    MissionsActivity.this,
                    MissionDetailActivity.class
            );

            startActivity(intent);

        });

    }

}