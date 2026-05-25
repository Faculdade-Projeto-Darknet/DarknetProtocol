package com.darknetprotocol.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.darknetprotocol.R;
import com.darknetprotocol.SoundManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.HashMap;
import java.util.Map;

public class TerminalActivity extends AppCompatActivity {

    /*
     * =========================================================
     * COMPONENTES
     * =========================================================
     */

    private TextView txtTerminal;
    private TextView txtCommandDisplay;

    private BottomNavigationView bottomNavigation;

    /*
     * =========================================================
     * TERMINAL
     * =========================================================
     */

    private final StringBuilder currentCommand =
            new StringBuilder();

    private final Map<String, String> commands =
            new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_terminal);

        initializeViews();

        setupCommands();
        setupKeyboard();
        setupControls();
        setupBottomNavigation();
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
                    R.id.nav_terminal
            );
        }
    }

    /*
     * =========================================================
     * INICIALIZA COMPONENTES
     * =========================================================
     */

    private void initializeViews() {

        txtTerminal =
                findViewById(R.id.txtTerminal);

        txtCommandDisplay =
                findViewById(R.id.txtCommandDisplay);

        bottomNavigation =
                findViewById(R.id.bottomNavigation);
    }

    /*
     * =========================================================
     * COMANDOS TERMINAL
     * =========================================================
     */

    private void setupCommands() {

        commands.put(
                "INFO",
                "> DARKNET TERMINAL v2.1\n" +
                        "> Simulador educacional de cibersegurança.\n" +
                        "> Objetivo: conscientização e treinamento ético.\n" +
                        "> Digite HELP para listar os comandos."
        );

        commands.put(
                "HELP",
                "> COMANDOS DISPONÍVEIS:\n\n" +
                        "INFO\n" +
                        "HELP\n" +
                        "ETHICS\n" +
                        "PHISHING\n" +
                        "PASSWORD\n" +
                        "PRIVACY\n" +
                        "MALWARE\n" +
                        "SOCIAL\n" +
                        "LEAK\n" +
                        "VPN\n" +
                        "2FA\n" +
                        "WI-FI\n" +
                        "SCAN\n" +
                        "ANALYZE\n" +
                        "REPORT\n" +
                        "MATRIX\n" +
                        "CLEAR"
        );

        commands.put(
                "ETHICS",
                "> HACKING ÉTICO ATIVADO\n" +
                        "> Conhecimento sem responsabilidade vira ameaça.\n" +
                        "> Segurança digital protege pessoas reais.\n" +
                        "> Use suas habilidades para defesa."
        );

        commands.put(
                "PASSWORD",
                "> SEGURANÇA DE SENHAS\n" +
                        "> Não reutilize senhas.\n" +
                        "> Use combinações fortes.\n" +
                        "> Prefira gerenciadores de senha.\n" +
                        "> 123456 ainda existe. A humanidade falhou."
        );

        commands.put(
                "MATRIX",
                "> 010101010101010101\n" +
                        "> rastreando pacotes...\n" +
                        "> acesso negado.\n" +
                        "> brincadeira.\n" +
                        "> você não é o Neo."
        );

        commands.put(
                "SCAN",
                "> Escaneando portas...\n" +
                        "> Nenhuma ameaça encontrada.\n" +
                        "> Firewall ativo."
        );

        commands.put(
                "REPORT",
                "> GERANDO RELATÓRIO...\n" +
                        "> Sistema estável.\n" +
                        "> Nenhum ataque detectado."
        );

        commands.put(
                "VPN",
                "> VPN INFO\n" +
                        "> VPN protege tráfego em redes públicas.\n" +
                        "> Redes públicas podem ser monitoradas."
        );

        commands.put(
                "2FA",
                "> AUTENTICAÇÃO EM 2 FATORES\n" +
                        "> Uma senha sozinha não basta.\n" +
                        "> Ative 2FA sempre que possível."
        );

        commands.put(
                "CLEAR",
                "> Terminal limpo."
        );
    }

    /*
     * =========================================================
     * TECLADO
     * =========================================================
     */

    private void setupKeyboard() {

        String keys =
                "QWERTYUIOPASDFGHJKLZXCVBNM";

        for (char key : keys.toCharArray()) {

            int id =
                    getResources().getIdentifier(
                            "key" + key,
                            "id",
                            getPackageName()
                    );

            Button button =
                    findViewById(id);

            if (button != null) {

                button.setOnClickListener(v -> {

                    playClick();

                    currentCommand.append(key);

                    updateCommandDisplay();
                });
            }
        }
    }

    /*
     * =========================================================
     * CONTROLES TERMINAL
     * =========================================================
     */

    private void setupControls() {

        Button btnDelete =
                findViewById(R.id.btnDelete);

        Button btnSpace =
                findViewById(R.id.btnSpace);

        Button btnExecute =
                findViewById(R.id.btnExecute);

        Button btnClearTerminal =
                findViewById(R.id.btnClearTerminal);

        /*
         * DELETE
         */

        btnDelete.setOnClickListener(v -> {

            if (currentCommand.length() > 0) {

                playClick();

                currentCommand.deleteCharAt(
                        currentCommand.length() - 1
                );

                updateCommandDisplay();
            }
        });

        /*
         * SPACE
         */

        btnSpace.setOnClickListener(v -> {

            playClick();

            currentCommand.append(" ");

            updateCommandDisplay();
        });

        /*
         * EXECUTAR
         */

        btnExecute.setOnClickListener(v -> {

            executeCommand(
                    currentCommand
                            .toString()
                            .trim()
                            .toUpperCase()
            );
        });

        /*
         * LIMPAR TERMINAL
         */

        btnClearTerminal.setOnClickListener(v -> {

            SoundManager.playSound(
                    this,
                    R.raw.cyber_error
            );

            txtTerminal.setText(
                    "> Terminal limpo."
            );
        });
    }

    /*
     * =========================================================
     * EXECUTA COMANDO
     * =========================================================
     */

    private void executeCommand(String command) {

        if (command.isEmpty()) {
            return;
        }

        terminalOutput("> " + command);

        /*
         * CLEAR
         */

        if (command.equals("CLEAR")) {

            txtTerminal.setText(
                    "> Terminal limpo."
            );

            playSuccess();

            resetCommand();

            return;
        }

        /*
         * COMANDO EXISTE
         */

        if (commands.containsKey(command)) {

            terminalOutput(
                    commands.get(command)
            );

            playSuccess();

        } else {

            terminalOutput(
                    "> COMANDO NÃO RECONHECIDO\n" +
                            "> Digite HELP."
            );

            playError();
        }

        resetCommand();
    }

    /*
     * =========================================================
     * DISPLAY COMANDO
     * =========================================================
     */

    private void updateCommandDisplay() {

        txtCommandDisplay.setText(
                "> " + currentCommand
        );
    }

    /*
     * =========================================================
     * RESET COMANDO
     * =========================================================
     */

    private void resetCommand() {

        currentCommand.setLength(0);

        updateCommandDisplay();
    }

    /*
     * =========================================================
     * OUTPUT TERMINAL
     * =========================================================
     */

    private void terminalOutput(String text) {

        txtTerminal.append("\n\n" + text);

        txtTerminal.post(() -> {

            int scrollAmount =
                    txtTerminal.getLayout().getLineTop(
                            txtTerminal.getLineCount()
                    ) - txtTerminal.getHeight();

            if (scrollAmount > 0) {

                txtTerminal.scrollTo(
                        0,
                        scrollAmount
                );

            } else {

                txtTerminal.scrollTo(
                        0,
                        0
                );
            }
        });
    }

    /*
     * =========================================================
     * NAVEGAÇÃO
     * =========================================================
     */

    private void setupBottomNavigation() {

        bottomNavigation.setSelectedItemId(
                R.id.nav_terminal
        );

        bottomNavigation.setOnItemSelectedListener(item -> {

            int id = item.getItemId();

            /*
             * TELA ATUAL
             */

            if (id == R.id.nav_terminal) {
                return true;
            }

            playClick();

            /*
             * MISSÕES
             */

            if (id == R.id.nav_missions) {

                openActivity(
                        MissionsActivity.class,
                        true
                );

                return true;
            }

            /*
             * PERFIL
             */

            if (id == R.id.nav_profile) {

                openActivity(
                        ProfileActivity.class,
                        false
                );

                return true;
            }

            return false;
        });
    }

    /*
     * =========================================================
     * ABRIR ACTIVITY
     * =========================================================
     */

    private void openActivity(
            Class<?> activityClass,
            boolean leftAnimation
    ) {

        Intent intent =
                new Intent(
                        TerminalActivity.this,
                        activityClass
                );

        intent.addFlags(
                Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
        );

        startActivity(intent);

        if (leftAnimation) {

            overridePendingTransition(
                    R.anim.slide_in_left,
                    R.anim.slide_out_right
            );

        } else {

            overridePendingTransition(
                    R.anim.slide_in_right,
                    R.anim.slide_out_left
            );
        }
    }

    /*
     * =========================================================
     * SONS
     * =========================================================
     */

    private void playClick() {

        SoundManager.playSound(
                this,
                R.raw.cyber_click
        );
    }

    private void playSuccess() {

        SoundManager.playSound(
                this,
                R.raw.cyber_success
        );
    }

    private void playError() {

        SoundManager.playSound(
                this,
                R.raw.cyber_error
        );
    }
}