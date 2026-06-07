package com.darknetprotocol.activities;

// Importa classes utilizadas para navegação entre telas
import android.content.Intent;

// Importa Bundle, utilizado no ciclo de vida da Activity
import android.os.Bundle;

// Importa componentes visuais utilizados na interface
import android.widget.Button;
import android.widget.TextView;

// Classe base para Activities compatíveis com AppCompat
import androidx.appcompat.app.AppCompatActivity;

// Recursos do projeto (layouts, ids, sons e animações)
import com.darknetprotocol.R;

// Classe responsável pelos efeitos sonoros
import com.darknetprotocol.utils.SoundManager;

// Componente de navegação inferior
import com.google.android.material.bottomnavigation.BottomNavigationView;

// Estruturas utilizadas para armazenar os comandos do terminal
import java.util.HashMap;
import java.util.Map;

// Activity responsável pelo terminal interativo do jogo
public class TerminalActivity extends AppCompatActivity {

    /*
     * =========================================================
     * COMPONENTES DA INTERFACE
     * =========================================================
     */

    // Área principal onde as mensagens do terminal são exibidas
    private TextView txtTerminal;

    // Exibe o comando que o usuário está digitando
    private TextView txtCommandDisplay;

    // Menu de navegação inferior
    private BottomNavigationView bottomNavigation;

    /*
     * =========================================================
     * SISTEMA DO TERMINAL
     * =========================================================
     */

    // Armazena o comando digitado pelo jogador
    private final StringBuilder currentCommand =
            new StringBuilder();

    // Lista de comandos disponíveis e suas respostas
    private final Map<String, String> commands =
            new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Define o layout da tela
        setContentView(R.layout.activity_terminal);

        // Inicializa os componentes visuais
        initializeViews();

        // Carrega os comandos disponíveis
        setupCommands();

        // Configura o teclado virtual
        setupKeyboard();

        // Configura botões de controle
        setupControls();

        // Configura navegação inferior
        setupBottomNavigation();
    }

    /*
     * =========================================================
     * AO RETORNAR PARA A TELA
     * =========================================================
     */

    @Override
    protected void onResume() {
        super.onResume();

        // Mantém a aba Terminal selecionada
        if (bottomNavigation != null) {
            bottomNavigation.setSelectedItemId(
                    R.id.nav_terminal
            );
        }
    }

    /*
     * =========================================================
     * INICIALIZAÇÃO DOS COMPONENTES
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
     * CADASTRA COMANDOS DO TERMINAL
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
     * CONFIGURA TECLADO VIRTUAL
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

                    // Reproduz som de clique
                    playClick();

                    // Adiciona letra ao comando atual
                    currentCommand.append(key);

                    // Atualiza exibição do comando
                    updateCommandDisplay();
                });
            }
        }
    }

    /*
     * =========================================================
     * CONFIGURA BOTÕES DE CONTROLE
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

        // Remove último caractere digitado
        btnDelete.setOnClickListener(v -> {

            if (currentCommand.length() > 0) {

                playClick();

                currentCommand.deleteCharAt(
                        currentCommand.length() - 1
                );

                updateCommandDisplay();
            }
        });

        // Adiciona espaço ao comando
        btnSpace.setOnClickListener(v -> {

            playClick();

            currentCommand.append(" ");

            updateCommandDisplay();
        });

        // Executa o comando digitado
        btnExecute.setOnClickListener(v -> {

            executeCommand(
                    currentCommand
                            .toString()
                            .trim()
                            .toUpperCase()
            );
        });

        // Limpa completamente a saída do terminal
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

        // Ignora comandos vazios
        if (command.isEmpty()) {
            return;
        }

        // Exibe o comando digitado
        terminalOutput("> " + command);

        // Comando responsável por limpar o terminal
        if (command.equals("CLEAR")) {

            txtTerminal.setText(
                    "> Terminal limpo."
            );

            playSuccess();

            resetCommand();

            return;
        }

        // Verifica se o comando existe
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
     * ATUALIZA EXIBIÇÃO DO COMANDO
     * =========================================================
     */

    private void updateCommandDisplay() {

        txtCommandDisplay.setText(
                "> " + currentCommand
        );
    }

    /*
     * =========================================================
     * LIMPA COMANDO ATUAL
     * =========================================================
     */

    private void resetCommand() {

        currentCommand.setLength(0);

        updateCommandDisplay();
    }

    /*
     * =========================================================
     * EXIBE TEXTO NO TERMINAL
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
     * NAVEGAÇÃO ENTRE TELAS
     * =========================================================
     */

    private void setupBottomNavigation() {

        bottomNavigation.setSelectedItemId(
                R.id.nav_terminal
        );

        bottomNavigation.setOnItemSelectedListener(item -> {

            int id = item.getItemId();

            // Permanece na tela atual
            if (id == R.id.nav_terminal) {
                return true;
            }

            playClick();

            // Abre tela de missões
            if (id == R.id.nav_missions) {

                openActivity(
                        MissionsActivity.class,
                        true
                );

                return true;
            }

            // Abre tela de perfil
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
     * ABRE NOVA ACTIVITY
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
     * EFEITOS SONOROS
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