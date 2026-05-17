package com.darknetprotocol;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class TerminalActivity extends AppCompatActivity {

    TextView txtTerminal, txtCommandDisplay;
    String currentCommand = "";

    BottomNavigationView bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terminal);

        txtTerminal = findViewById(R.id.txtTerminal);
        txtCommandDisplay = findViewById(R.id.txtCommandDisplay);
        bottomNavigation = findViewById(R.id.bottomNavigation);

        setupKeyboard();
        setupBottomNavigation();
    }

    private void setupKeyboard() {

        bindKey(R.id.keyQ, "Q");
        bindKey(R.id.keyW, "W");
        bindKey(R.id.keyE, "E");
        bindKey(R.id.keyR, "R");
        bindKey(R.id.keyT, "T");
        bindKey(R.id.keyY, "Y");
        bindKey(R.id.keyU, "U");
        bindKey(R.id.keyI, "I");
        bindKey(R.id.keyO, "O");
        bindKey(R.id.keyP, "P");

        bindKey(R.id.keyA, "A");
        bindKey(R.id.keyS, "S");
        bindKey(R.id.keyD, "D");
        bindKey(R.id.keyF, "F");
        bindKey(R.id.keyG, "G");
        bindKey(R.id.keyH, "H");
        bindKey(R.id.keyJ, "J");
        bindKey(R.id.keyK, "K");
        bindKey(R.id.keyL, "L");

        bindKey(R.id.keyZ, "Z");
        bindKey(R.id.keyX, "X");
        bindKey(R.id.keyC, "C");
        bindKey(R.id.keyV, "V");
        bindKey(R.id.keyB, "B");
        bindKey(R.id.keyN, "N");
        bindKey(R.id.keyM, "M");

        Button btnDelete = findViewById(R.id.btnDelete);
        Button btnSpace = findViewById(R.id.btnSpace);
        Button btnExecute = findViewById(R.id.btnExecute);
        Button btnClearTerminal = findViewById(R.id.btnClearTerminal);

        btnDelete.setOnClickListener(v -> deleteLetter());
        btnSpace.setOnClickListener(v -> addLetter(" "));
        btnExecute.setOnClickListener(v -> executeCommand());

        btnClearTerminal.setOnClickListener(v -> {
            txtTerminal.setText("> Terminal limpo.\n> Digite INFO para ver os comandos.");
            currentCommand = "";
            updateCommandDisplay();
        });
    }

    private void bindKey(int buttonId, String letter) {
        Button button = findViewById(buttonId);
        button.setOnClickListener(v -> addLetter(letter));
    }

    private void addLetter(String letter) {
        currentCommand += letter;
        updateCommandDisplay();
    }

    private void deleteLetter() {
        if (currentCommand.length() > 0) {
            currentCommand = currentCommand.substring(0, currentCommand.length() - 1);
            updateCommandDisplay();
        }
    }

    private void updateCommandDisplay() {
        txtCommandDisplay.setText("> " + currentCommand);
    }

    private void executeCommand() {

        String command = currentCommand.trim().toLowerCase();

        if (command.isEmpty()) {
            txtTerminal.append("\n> Nenhum comando digitado.");
            return;
        }

        txtTerminal.append("\n\n> " + command.toUpperCase());

        switch (command) {

            case "info":
                txtTerminal.append(
                        "\nComandos disponíveis:" +
                                "\nINFO - Mostra todos os comandos" +
                                "\nHELP - Ajuda rápida" +
                                "\nETHICS - Código white hat" +
                                "\nSCAN - Simula uma análise autorizada" +
                                "\nANALYZE - Analisa risco fictício" +
                                "\nREPORT - Gera relatório responsável" +
                                "\nCLEAR - Limpa o terminal"
                );
                break;

            case "help":
                txtTerminal.append(
                        "\nUse INFO para ver todos os comandos." +
                                "\nEste terminal simula práticas éticas de segurança."
                );
                break;

            case "ethics":
                txtTerminal.append(
                        "\nWhite hat hacker trabalha com autorização." +
                                "\nObjetivo: proteger sistemas, documentar falhas" +
                                "\ne reportar vulnerabilidades com responsabilidade."
                );
                break;

            case "scan":
                txtTerminal.append(
                        "\nEscaneamento fictício iniciado..." +
                                "\nNenhuma ação real foi executada." +
                                "\nResultado: ambiente de teste seguro."
                );
                break;

            case "analyze":
                txtTerminal.append(
                        "\nAnálise fictícia concluída." +
                                "\nRisco: baixo." +
                                "\nRecomendação: manter boas práticas de segurança."
                );
                break;

            case "report":
                txtTerminal.append(
                        "\nRelatório gerado:" +
                                "\n- Falha simulada documentada" +
                                "\n- Nenhum sistema real afetado" +
                                "\n- Conduta ética mantida"
                );
                break;

            case "clear":
                txtTerminal.setText("> Terminal limpo.\n> Digite INFO para ver os comandos.");
                break;

            default:
                txtTerminal.append(
                        "\nComando não reconhecido." +
                                "\nDigite INFO para ver os comandos disponíveis."
                );
                break;
        }

        currentCommand = "";
        updateCommandDisplay();
    }

    private void setupBottomNavigation() {

        bottomNavigation.setSelectedItemId(R.id.nav_terminal);

        bottomNavigation.setOnItemSelectedListener(item -> {

            int id = item.getItemId();

            if (id == R.id.nav_missions) {
                startActivity(new Intent(TerminalActivity.this, MissionsActivity.class));
                return true;
            }

            if (id == R.id.nav_terminal) {
                return true;
            }

            if (id == R.id.nav_profile) {
                startActivity(new Intent(TerminalActivity.this, ProfileActivity.class));
                return true;
            }

            return false;
        });
    }
}