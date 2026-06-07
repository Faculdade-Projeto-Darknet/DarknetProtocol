package com.darknetprotocol.activities;

// Importa a classe Intent, utilizada para abrir outras telas
import android.content.Intent;

// Importa ColorStateList, usada para alterar cores dinamicamente
import android.content.res.ColorStateList;

// Importa Bundle, utilizado no ciclo de vida da Activity
import android.os.Bundle;

// Importa View para controle de visibilidade dos componentes
import android.view.View;

// Importa componentes visuais utilizados na interface
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

// Classe base para Activities compatíveis com AppCompat
import androidx.appcompat.app.AppCompatActivity;

// Importa recursos do projeto (layouts, ids, animações, sons, etc.)
import com.darknetprotocol.R;

// Classe responsável por reproduzir efeitos sonoros
import com.darknetprotocol.utils.SoundManager;

// Classe responsável por armazenar e recuperar dados do jogador
import com.darknetprotocol.utils.PlayerPrefs;

// Componente de navegação inferior
import com.google.android.material.bottomnavigation.BottomNavigationView;

// Activity responsável por exibir e gerenciar as missões do jogo
public class MissionsActivity extends AppCompatActivity {

    /*
     * =========================================================
     * MISSÕES
     * =========================================================
     */

    // Layouts clicáveis das missões
    private LinearLayout mission1;
    private LinearLayout mission2;
    private LinearLayout mission3;
    private LinearLayout mission4;

    /*
     * =========================================================
     * LAYOUTS
     * =========================================================
     */

    // Área de missões disponíveis
    private LinearLayout layoutAvailableMissions;

    // Área de missões concluídas
    private LinearLayout layoutCompletedMissions;

    /*
     * =========================================================
     * BOTÕES
     * =========================================================
     */

    // Botão da aba "Disponíveis"
    private Button btnAvailable;

    // Botão da aba "Concluídas"
    private Button btnCompleted;

    /*
     * =========================================================
     * TEXTO
     * =========================================================
     */

    // Lista das missões já concluídas
    private TextView txtCompletedList;

    /*
     * =========================================================
     * SISTEMA
     * =========================================================
     */

    // Barra de navegação inferior
    private BottomNavigationView bottomNavigation;

    // Gerencia os dados salvos do jogador
    private PlayerPrefs playerPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Define o layout da tela
        setContentView(R.layout.activity_missions);

        // Inicializa os componentes da interface
        initializeViews();

        // Configura os botões das missões
        setupMissionButtons();

        // Configura os botões das abas
        setupTabButtons();

        // Configura o menu inferior
        setupBottomNavigation();

        // Exibe inicialmente as missões disponíveis
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

        // Mantém a aba de missões selecionada ao retornar para a tela
        if (bottomNavigation != null) {
            bottomNavigation.setSelectedItemId(
                    R.id.nav_missions
            );
        }

        // Atualiza a lista de missões disponíveis
        updateAvailableMissions();
    }

    /*
     * =========================================================
     * INICIALIZA COMPONENTES
     * =========================================================
     */

    private void initializeViews() {

        // Inicializa o sistema de preferências
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

        // Abre o briefing da missão 1
        mission1.setOnClickListener(v -> {
            playClick();
            openActivity(
                    Mission1BriefingActivity.class
            );
        });

        // Abre o briefing da missão 2
        mission2.setOnClickListener(v -> {
            playClick();
            openActivity(
                    Mission2BriefingActivity.class
            );
        });

        // Abre o briefing da missão 3
        mission3.setOnClickListener(v -> {
            playClick();
            openActivity(
                    Mission3BriefingActivity.class
            );
        });

        // Abre o briefing da missão 4
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

        // Exibe as missões disponíveis
        btnAvailable.setOnClickListener(v -> {
            playClick();
            showAvailable();
        });

        // Exibe as missões concluídas
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

        // Mantém a aba atual selecionada
        bottomNavigation.setSelectedItemId(
                R.id.nav_missions
        );

        // Monitora os cliques do menu inferior
        bottomNavigation.setOnItemSelectedListener(item -> {

            int id = item.getItemId();

            // Permanece na tela atual
            if (id == R.id.nav_missions) {
                return true;
            }

            playClick();

            // Abre a tela Terminal
            if (id == R.id.nav_terminal) {

                openActivity(
                        TerminalActivity.class
                );

                return true;
            }

            // Abre a tela Perfil
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
            completed.append("✓ 01 - INVASÃO À REDE\n");
        }

        if (mission2Completed) {
            completed.append("✓ 02 - QUEBRA DE SENHA\n");
        }

        if (mission3Completed) {
            completed.append("✓ 03 - ARQUIVO FANTASMA\n");
        }

        if (mission4Completed) {
            completed.append("✓ 04 - NÓ INVASOR\n");
        }

        // Caso nenhuma missão tenha sido concluída
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