package com.darknetprotocol.activities;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.darknetprotocol.R;
import com.darknetprotocol.utils.SoundManager;
import com.darknetprotocol.utils.PlayerPrefs;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MissionsActivity extends AppCompatActivity {

    /*
     * =========================================================
     * MISSÕES
     * =========================================================
     */

    private LinearLayout mission1;
    private LinearLayout mission2;
    private LinearLayout mission3;
    private LinearLayout mission4;

    /*
     * =========================================================
     * LAYOUTS
     * =========================================================
     */

    private LinearLayout layoutAvailableMissions;
    private LinearLayout layoutCompletedMissions;

    /*
     * =========================================================
     * BOTÕES
     * =========================================================
     */

    private Button btnAvailable;
    private Button btnCompleted;

    /*
     * =========================================================
     * TEXTO
     * =========================================================
     */

    private TextView txtCompletedList;

    /*
     * =========================================================
     * SISTEMA
     * =========================================================
     */

    private BottomNavigationView bottomNavigation;
    private PlayerPrefs playerPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_missions);

        initializeViews();
        setupMissionButtons();
        setupTabButtons();
        setupBottomNavigation();

        showAvailable();
    }

    /*
     * =========================================================
     * RESUME
     * =========================================================
     */

    @Override
    protected void onResume() {
        super.onResume();

        if (bottomNavigation != null) {
            bottomNavigation.setSelectedItemId(
                    R.id.nav_missions
            );
        }

        updateAvailableMissions();
    }

    /*
     * =========================================================
     * INICIALIZA COMPONENTES
     * =========================================================
     */

    private void initializeViews() {

        playerPrefs = new PlayerPrefs(this);

        /*
         * MISSÕES
         */

        mission1 = findViewById(R.id.mission1);
        mission2 = findViewById(R.id.mission2);
        mission3 = findViewById(R.id.mission3);
        mission4 = findViewById(R.id.mission4);

        /*
         * LAYOUTS
         */

        layoutAvailableMissions =
                findViewById(R.id.layoutAvailableMissions);

        layoutCompletedMissions =
                findViewById(R.id.layoutCompletedMissions);

        /*
         * BOTÕES
         */

        btnAvailable =
                findViewById(R.id.btnAvailable);

        btnCompleted =
                findViewById(R.id.btnCompleted);

        /*
         * TEXTO
         */

        txtCompletedList =
                findViewById(R.id.txtCompletedList);

        /*
         * NAVIGATION
         */

        bottomNavigation =
                findViewById(R.id.bottomNavigation);
    }

    /*
     * =========================================================
     * BOTÕES DAS MISSÕES
     * =========================================================
     */

    private void setupMissionButtons() {

        mission1.setOnClickListener(v -> {
            playClick();
            openActivity(
                    Mission1BriefingActivity.class
            );
        });

        mission2.setOnClickListener(v -> {
            playClick();
            openActivity(
                    Mission2BriefingActivity.class
            );
        });

        mission3.setOnClickListener(v -> {
            playClick();
            openActivity(
                    Mission3BriefingActivity.class
            );
        });

        mission4.setOnClickListener(v -> {
            playClick();
            openActivity(
                    Mission4BriefingActivity.class
            );
        });
    }

    /*
     * =========================================================
     * BOTÕES DAS ABAS
     * =========================================================
     */

    private void setupTabButtons() {

        btnAvailable.setOnClickListener(v -> {
            playClick();
            showAvailable();
        });

        btnCompleted.setOnClickListener(v -> {
            playClick();
            showCompleted();
        });
    }

    /*
     * =========================================================
     * NAVEGAÇÃO INFERIOR
     * =========================================================
     */

    private void setupBottomNavigation() {

        bottomNavigation.setSelectedItemId(
                R.id.nav_missions
        );

        bottomNavigation.setOnItemSelectedListener(item -> {

            int id = item.getItemId();

            /*
             * TELA ATUAL
             */

            if (id == R.id.nav_missions) {
                return true;
            }

            playClick();

            /*
             * TERMINAL
             */

            if (id == R.id.nav_terminal) {

                openActivity(
                        TerminalActivity.class
                );

                return true;
            }

            /*
             * PERFIL
             */

            if (id == R.id.nav_profile) {

                openActivity(
                        ProfileActivity.class
                );

                return true;
            }

            return false;
        });
    }

    /*
     * =========================================================
     * MOSTRA MISSÕES DISPONÍVEIS
     * =========================================================
     */

    private void showAvailable() {

        layoutAvailableMissions
                .setVisibility(View.VISIBLE);

        layoutCompletedMissions
                .setVisibility(View.GONE);

        setActiveTab(btnAvailable);
        setInactiveTab(btnCompleted);

        updateAvailableMissions();
    }

    /*
     * =========================================================
     * MOSTRA MISSÕES COMPLETADAS
     * =========================================================
     */

    private void showCompleted() {

        layoutAvailableMissions
                .setVisibility(View.GONE);

        layoutCompletedMissions
                .setVisibility(View.VISIBLE);

        setInactiveTab(btnAvailable);
        setActiveTab(btnCompleted);

        updateCompletedMissions();
    }

    /*
     * =========================================================
     * ATUALIZA MISSÕES DISPONÍVEIS
     * =========================================================
     */

    private void updateAvailableMissions() {

        mission1.setVisibility(
                playerPrefs.isMission1Completed()
                        ? View.GONE
                        : View.VISIBLE
        );

        mission2.setVisibility(
                playerPrefs.isMission2Completed()
                        ? View.GONE
                        : View.VISIBLE
        );

        mission3.setVisibility(
                playerPrefs.isMission3Completed()
                        ? View.GONE
                        : View.VISIBLE
        );

        mission4.setVisibility(
                playerPrefs.isMission4Completed()
                        ? View.GONE
                        : View.VISIBLE
        );
    }

    /*
     * =========================================================
     * ATUALIZA MISSÕES COMPLETADAS
     * =========================================================
     */

    private void updateCompletedMissions() {

        boolean mission1Completed =
                playerPrefs.isMission1Completed();

        boolean mission2Completed =
                playerPrefs.isMission2Completed();

        boolean mission3Completed =
                playerPrefs.isMission3Completed();

        boolean mission4Completed =
                playerPrefs.isMission4Completed();

        StringBuilder completed =
                new StringBuilder();

        if (mission1Completed) {
            completed.append(
                    "✓ 01 - INVASÃO À REDE\n"
            );
        }

        if (mission2Completed) {
            completed.append(
                    "✓ 02 - QUEBRA DE SENHA\n"
            );
        }

        if (mission3Completed) {
            completed.append(
                    "✓ 03 - ARQUIVO FANTASMA\n"
            );
        }

        if (mission4Completed) {
            completed.append(
                    "✓ 04 - NÓ INVASOR\n"
            );
        }

        /*
         * SEM MISSÕES COMPLETADAS
         */

        if (!mission1Completed &&
                !mission2Completed &&
                !mission3Completed &&
                !mission4Completed) {

            completed.append(
                    "Nenhuma missão concluída ainda."
            );
        }

        txtCompletedList.setText(
                completed.toString()
        );
    }

    /*
     * =========================================================
     * ESTILO ABA ATIVA
     * =========================================================
     */

    private void setActiveTab(Button button) {

        button.setTextColor(0xFF000000);

        button.setBackgroundTintList(
                ColorStateList.valueOf(
                        0xFF00FF66
                )
        );
    }

    /*
     * =========================================================
     * ESTILO ABA INATIVA
     * =========================================================
     */

    private void setInactiveTab(Button button) {

        button.setTextColor(0xFFFFFFFF);

        button.setBackgroundTintList(
                ColorStateList.valueOf(
                        0xFF101010
                )
        );
    }

    /*
     * =========================================================
     * ABRE ACTIVITY
     * =========================================================
     */

    private void openActivity(
            Class<?> activityClass
    ) {

        Intent intent =
                new Intent(
                        MissionsActivity.this,
                        activityClass
                );

        startActivity(intent);

        overridePendingTransition(
                R.anim.slide_in_right,
                R.anim.slide_out_left
        );
    }

    /*
     * =========================================================
     * SOM CLICK
     * =========================================================
     */

    private void playClick() {

        SoundManager.playSound(
                this,
                R.raw.cyber_click
        );
    }
}