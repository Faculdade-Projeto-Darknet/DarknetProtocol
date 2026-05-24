package com.darknetprotocol.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.darknetprotocol.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class TerminalActivity extends AppCompatActivity {

    TextView txtTerminal;
    TextView txtCommandDisplay;

    BottomNavigationView bottomNavigation;

    StringBuilder currentCommand =
            new StringBuilder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terminal);

        txtTerminal =
                findViewById(R.id.txtTerminal);

        txtCommandDisplay =
                findViewById(R.id.txtCommandDisplay);

        bottomNavigation =
                findViewById(R.id.bottomNavigation);

        setupKeyboard();
        setupControls();
        setupBottomNavigation();
    }

    private void setupKeyboard() {

        String keys = "QWERTYUIOPASDFGHJKLZXCVBNM";

        for (char key : keys.toCharArray()) {

            int id = getResources().getIdentifier(
                    "key" + key,
                    "id",
                    getPackageName()
            );

            Button button = findViewById(id);

            if (button != null) {

                button.setOnClickListener(v -> {

                    currentCommand.append(key);

                    updateCommandDisplay();

                });

            }
        }
    }

    private void setupControls() {

        Button btnDelete =
                findViewById(R.id.btnDelete);

        Button btnSpace =
                findViewById(R.id.btnSpace);

        Button btnExecute =
                findViewById(R.id.btnExecute);

        Button btnClearTerminal =
                findViewById(R.id.btnClearTerminal);

        btnDelete.setOnClickListener(v -> {

            if (currentCommand.length() > 0) {

                currentCommand.deleteCharAt(
                        currentCommand.length() - 1
                );

                updateCommandDisplay();
            }
        });

        btnSpace.setOnClickListener(v -> {

            currentCommand.append(" ");

            updateCommandDisplay();

        });

        btnExecute.setOnClickListener(v -> {

            executeCommand(
                    currentCommand.toString()
                            .trim()
                            .toUpperCase()
            );

        });

        btnClearTerminal.setOnClickListener(v -> {

            txtTerminal.setText(
                    "> Terminal limpo."
            );

        });
    }

    private void updateCommandDisplay() {

        txtCommandDisplay.setText(
                "> " + currentCommand
        );

    }

    private void executeCommand(String command) {

        if (command.isEmpty()) {
            return;
        }

        terminalOutput("> " + command);

        switch (command) {

            case "INFO":

                terminalOutput(
                        "> DARKNET TERMINAL v2.1\n" +
                                "> Simulador educacional de cibersegurança.\n" +
                                "> Objetivo: conscientização e treinamento ético.\n" +
                                "> Digite HELP para listar os comandos."
                );

                break;

            case "HELP":

                terminalOutput(
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

                break;

            case "ETHICS":

                terminalOutput(
                        "> HACKING ÉTICO ATIVADO\n" +
                                "> Conhecimento sem responsabilidade vira ameaça.\n" +
                                "> Segurança digital protege pessoas reais.\n" +
                                "> Use suas habilidades para defesa.\n" +
                                "> RESPEITE PRIVACIDADE."
                );

                break;

            case "PHISHING":

                terminalOutput(
                        "> ALERTA EDUCACIONAL\n" +
                                "> Nunca clique em links suspeitos.\n" +
                                "> Verifique remetentes.\n" +
                                "> Use autenticação em 2 fatores.\n" +
                                "> Golpes digitais exploram confiança humana."
                );

                break;

            case "PASSWORD":

                terminalOutput(
                        "> SEGURANÇA DE SENHAS\n" +
                                "> Não reutilize senhas.\n" +
                                "> Use combinações fortes.\n" +
                                "> Prefira gerenciadores de senha.\n" +
                                "> 123456 ainda existe. A humanidade falhou."
                );

                break;

            case "PRIVACY":

                terminalOutput(
                        "> PRIVACIDADE DIGITAL\n" +
                                "> Seus dados possuem valor.\n" +
                                "> Aplicativos coletam comportamento.\n" +
                                "> Revise permissões regularmente.\n" +
                                "> Segurança começa nos pequenos hábitos."
                );

                break;

            case "MALWARE":

                terminalOutput(
                        "> MALWARE DETECTADO\n" +
                                "> Vírus modernos roubam dados silenciosamente.\n" +
                                "> Nunca instale APKs desconhecidos.\n" +
                                "> Atualizações corrigem falhas críticas.\n" +
                                "> Pirataria pode custar seus dados."
                );

                break;

            case "SOCIAL":

                terminalOutput(
                        "> ENGENHARIA SOCIAL\n" +
                                "> O elo mais fraco quase sempre é humano.\n" +
                                "> Golpistas manipulam confiança.\n" +
                                "> Não compartilhe códigos ou senhas.\n" +
                                "> Desconfiança saudável salva contas."
                );

                break;

            case "LEAK":

                terminalOutput(
                        "> VAZAMENTO DE DADOS\n" +
                                "> Empresas sofrem ataques diariamente.\n" +
                                "> Emails e senhas vazados circulam online.\n" +
                                "> Monitore suas contas.\n" +
                                "> Segurança não é paranoia."
                );

                break;

            case "VPN":

                terminalOutput(
                        "> VPN INFO\n" +
                                "> VPN protege tráfego em redes públicas.\n" +
                                "> Cafeterias e aeroportos são arriscados.\n" +
                                "> Criptografia reduz exposição.\n" +
                                "> Segurança pública raramente é segura."
                );

                break;

            case "2FA":

                terminalOutput(
                        "> AUTENTICAÇÃO EM 2 FATORES\n" +
                                "> Uma senha sozinha não basta.\n" +
                                "> Ative 2FA sempre que possível.\n" +
                                "> Apps autenticadores são mais seguros.\n" +
                                "> SMS ainda é melhor que nada."
                );

                break;

            case "WI-FI":

                terminalOutput(
                        "> SEGURANÇA WI-FI\n" +
                                "> Redes abertas podem ser monitoradas.\n" +
                                "> Evite acessar bancos em Wi-Fi público.\n" +
                                "> Use WPA2 ou WPA3.\n" +
                                "> Troque senhas padrão do roteador."
                );

                break;

            case "SCAN":

                terminalOutput(
                        "> Escaneando portas...\n" +
                                "> Nenhuma ameaça encontrada.\n" +
                                "> Firewall ativo."
                );

                break;

            case "ANALYZE":

                terminalOutput(
                        "> Analisando tráfego de rede...\n" +
                                "> Pacotes criptografados detectados.\n" +
                                "> Nenhuma anomalia crítica."
                );

                break;

            case "REPORT":

                terminalOutput(
                        "> GERANDO RELATÓRIO...\n" +
                                "> Sistema estável.\n" +
                                "> Nenhum ataque detectado.\n" +
                                "> Logs salvos."
                );

                break;

            case "MATRIX":

                terminalOutput(
                        "> 010101010101010101\n" +
                                "> rastreando pacotes...\n" +
                                "> acesso negado.\n" +
                                "> brincadeira.\n" +
                                "> você não é o Neo."
                );

                break;

            case "CLEAR":

                txtTerminal.setText(
                        "> Terminal limpo."
                );

                break;

            default:

                terminalOutput(
                        "> COMANDO NÃO RECONHECIDO\n" +
                                "> Digite HELP."
                );

                break;
        }

        currentCommand.setLength(0);

        updateCommandDisplay();
    }

    private void terminalOutput(String text) {

        txtTerminal.append(
                "\n\n" + text
        );

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

    private void setupBottomNavigation() {

        bottomNavigation.setSelectedItemId(
                R.id.nav_terminal
        );

        bottomNavigation.setOnItemSelectedListener(item -> {

            int id = item.getItemId();

            if (id == R.id.nav_terminal) {
                return true;
            }

            if (id == R.id.nav_missions) {

                startActivity(
                        new Intent(
                                TerminalActivity.this,
                                MissionsActivity.class
                        )
                );

                overridePendingTransition(
                        R.anim.slide_in_left,
                        R.anim.slide_out_right
                );

                return true;
            }

            if (id == R.id.nav_profile) {

                startActivity(
                        new Intent(
                                TerminalActivity.this,
                                ProfileActivity.class
                        )
                );

                overridePendingTransition(
                        R.anim.slide_in_right,
                        R.anim.slide_out_left
                );

                return true;
            }

            return false;
        });
    }
}