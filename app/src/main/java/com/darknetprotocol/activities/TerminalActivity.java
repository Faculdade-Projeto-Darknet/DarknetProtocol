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

        txtTerminal.setText(
                "> DARKNET TERMINAL ONLINE\n" +
                        "> Digite INFO para acessar os comandos.\n"
        );
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

            txtTerminal.setText(
                    "> Terminal limpo.\n" +
                            "> Digite INFO para ver os comandos."
            );

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

            currentCommand = currentCommand.substring(
                    0,
                    currentCommand.length() - 1
            );

            updateCommandDisplay();

        }

    }

    private void updateCommandDisplay() {

        txtCommandDisplay.setText(
                "> " + currentCommand
        );

    }

    private void executeCommand() {

        String command =
                currentCommand.trim().toLowerCase();

        if (command.isEmpty()) {

            txtTerminal.append(
                    "\n> Nenhum comando digitado."
            );

            return;

        }

        txtTerminal.append(
                "\n\n> " + command.toUpperCase()
        );

        switch (command) {

            case "info":

                txtTerminal.append(
                        "\n\nCOMANDOS DISPONÍVEIS" +

                                "\n\nINFO" +
                                "\nMostra todos os comandos." +

                                "\n\nHELP" +
                                "\nAjuda rápida do terminal." +

                                "\n\nETHICS" +
                                "\nExplica o conceito white hat." +

                                "\n\nSCAN" +
                                "\nExecuta escaneamento fictício." +

                                "\n\nANALYZE" +
                                "\nAnalisa vulnerabilidade simulada." +

                                "\n\nREPORT" +
                                "\nGera relatório ético." +

                                "\n\nCLEAR" +
                                "\nLimpa o terminal." +

                                "\n\nMATRIX" +
                                "\n???"
                );

                break;

            case "help":

                txtTerminal.append(
                        "\n\nUse INFO para visualizar todos os comandos." +
                                "\nEste terminal é apenas uma simulação educacional."
                );

                break;

            case "ethics":

                txtTerminal.append(
                        "\n\nWhite hat hackers atuam com autorização." +
                                "\nObjetivo: encontrar falhas e proteger sistemas." +
                                "\nSegurança ofensiva responsável salva empresas" +
                                "\nde perder dados por causa de senha123."
                );

                break;

            case "scan":

                txtTerminal.append(
                        "\n\nIniciando escaneamento..." +
                                "\nPorta 443: OPEN" +
                                "\nPorta 8080: FILTERED" +
                                "\nNenhum alvo real afetado." +
                                "\nAmbiente fictício seguro."
                );

                break;

            case "analyze":

                txtTerminal.append(
                        "\n\nAnalisando vulnerabilidades..." +
                                "\nRisco encontrado: baixo." +
                                "\nRecomendação:" +
                                "\n- Atualizar senhas" +
                                "\n- Ativar autenticação 2FA" +
                                "\n- Não clicar em e-mail escrito" +
                                "\npor um príncipe bilionário."
                );

                break;

            case "report":

                txtTerminal.append(
                        "\n\nRELATÓRIO GERADO" +
                                "\n- Vulnerabilidade fictícia documentada" +
                                "\n- Nenhum sistema real afetado" +
                                "\n- Conduta ética mantida" +
                                "\n- Operador autorizado"
                );

                break;

            case "matrix":

                txtTerminal.append(
                        "\n\nWake up, operator..." +
                                "\nThe protocol has you."
                );

                break;

            case "clear":

                txtTerminal.setText(
                        "> Terminal limpo.\n" +
                                "> Digite INFO para ver os comandos."
                );

                break;

            default:

                txtTerminal.append(
                        "\n\nComando não reconhecido." +
                                "\nDigite INFO para acessar os comandos."
                );

                break;
        }

        currentCommand = "";

        updateCommandDisplay();

    }

    private void setupBottomNavigation() {

        bottomNavigation.setSelectedItemId(
                R.id.nav_terminal
        );

        bottomNavigation.setOnItemSelectedListener(item -> {

            int id = item.getItemId();

            if (id == R.id.nav_missions) {

                Intent intent = new Intent(
                        TerminalActivity.this,
                        MissionsActivity.class
                );

                startActivity(intent);

                overridePendingTransition(
                        R.anim.slide_in_right,
                        R.anim.slide_out_left
                );

                return true;
            }

            if (id == R.id.nav_terminal) {
                return true;
            }

            if (id == R.id.nav_profile) {

                Intent intent = new Intent(
                        TerminalActivity.this,
                        ProfileActivity.class
                );

                startActivity(intent);

                overridePendingTransition(
                        R.anim.slide_in_right,
                        R.anim.slide_out_left
                );

                return true;
            }

            return false;

        });

    }
    @Override
    protected void onResume() {
        super.onResume();

        if (bottomNavigation != null) {
            bottomNavigation.setSelectedItemId(R.id.nav_terminal);
        }
    }
}