package com.darknetprotocol;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class PasswordMissionActivity extends AppCompatActivity {

    TextView txtStatusPassword, txtStatsPassword, txtLogPassword, txtPasswordDisplay;

    Button btnCheckPassword, btnPasswordHint, btnResetPasswordMission, btnDeleteKey;
    Button btnKeyO, btnKeyR, btnKeyI, btnKeyN, btnKeyA, btnKeyB, btnKeyC, btnKeyX, btnKeyZ;

    SharedPreferences preferences;

    String correctPassword = "ORION";
    String currentInput = "";

    int attempts = 4;
    int hintsUsed = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_mission);

        txtStatusPassword = findViewById(R.id.txtStatusPassword);
        txtStatsPassword = findViewById(R.id.txtStatsPassword);
        txtLogPassword = findViewById(R.id.txtLogPassword);
        txtPasswordDisplay = findViewById(R.id.txtPasswordDisplay);

        btnCheckPassword = findViewById(R.id.btnCheckPassword);
        btnPasswordHint = findViewById(R.id.btnPasswordHint);
        btnResetPasswordMission = findViewById(R.id.btnResetPasswordMission);
        btnDeleteKey = findViewById(R.id.btnDeleteKey);

        btnKeyO = findViewById(R.id.btnKeyO);
        btnKeyR = findViewById(R.id.btnKeyR);
        btnKeyI = findViewById(R.id.btnKeyI);
        btnKeyN = findViewById(R.id.btnKeyN);
        btnKeyA = findViewById(R.id.btnKeyA);
        btnKeyB = findViewById(R.id.btnKeyB);
        btnKeyC = findViewById(R.id.btnKeyC);
        btnKeyX = findViewById(R.id.btnKeyX);
        btnKeyZ = findViewById(R.id.btnKeyZ);

        preferences = getSharedPreferences("player_data", MODE_PRIVATE);

        if (preferences.getBoolean("mission2_completed", false)) {
            showCompletedMission();
        }

        btnKeyO.setOnClickListener(v -> addLetter("O"));
        btnKeyR.setOnClickListener(v -> addLetter("R"));
        btnKeyI.setOnClickListener(v -> addLetter("I"));
        btnKeyN.setOnClickListener(v -> addLetter("N"));
        btnKeyA.setOnClickListener(v -> addLetter("A"));
        btnKeyB.setOnClickListener(v -> addLetter("B"));
        btnKeyC.setOnClickListener(v -> addLetter("C"));
        btnKeyX.setOnClickListener(v -> addLetter("X"));
        btnKeyZ.setOnClickListener(v -> addLetter("Z"));

        btnDeleteKey.setOnClickListener(v -> deleteLetter());
        btnCheckPassword.setOnClickListener(v -> checkPassword());
        btnPasswordHint.setOnClickListener(v -> showHint());
        btnResetPasswordMission.setOnClickListener(v -> resetMission());

        updatePasswordDisplay();
    }

    private void addLetter(String letter) {

        if (preferences.getBoolean("mission2_completed", false)) {
            return;
        }

        if (currentInput.length() >= 5) {
            Toast.makeText(this, "A senha tem só 5 letras, Sherlock.", Toast.LENGTH_SHORT).show();
            return;
        }

        currentInput += letter;
        updatePasswordDisplay();
    }

    private void deleteLetter() {

        if (currentInput.length() > 0) {
            currentInput = currentInput.substring(0, currentInput.length() - 1);
            updatePasswordDisplay();
        }
    }

    private void updatePasswordDisplay() {

        StringBuilder display = new StringBuilder();

        for (int i = 0; i < 5; i++) {
            if (i < currentInput.length()) {
                display.append(currentInput.charAt(i));
            } else {
                display.append("_");
            }

            if (i < 4) {
                display.append(" ");
            }
        }

        txtPasswordDisplay.setText(display.toString());
    }

    private void checkPassword() {

        if (preferences.getBoolean("mission2_completed", false)) {
            Toast.makeText(this, "Missão já concluída.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (currentInput.length() < 5) {
            Toast.makeText(this, "Complete as 5 letras da senha.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (currentInput.equals(correctPassword)) {
            completeMission();
        } else {
            attempts--;

            txtStatusPassword.setText("STATUS: SENHA INCORRETA");
            txtLogPassword.append("\n> Tentativa falhou: " + currentInput);
            txtLogPassword.append("\n> Assinatura inválida.");

            currentInput = "";
            updatePasswordDisplay();
            updateStats();

            if (attempts <= 0) {
                gameOver();
            } else {
                Toast.makeText(this, "Senha errada. Tentativas restantes: " + attempts, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void showHint() {

        if (preferences.getBoolean("mission2_completed", false)) {
            Toast.makeText(this, "Missão já concluída.", Toast.LENGTH_SHORT).show();
            return;
        }

        hintsUsed++;

        if (hintsUsed == 1) {
            txtLogPassword.append("\n> Dica 2: a senha tem 5 letras.");
        } else if (hintsUsed == 2) {
            txtLogPassword.append("\n> Dica 3: começa com O.");
        } else if (hintsUsed == 3) {
            txtLogPassword.append("\n> Dica 4: O R I O N.");
        } else {
            txtLogPassword.append("\n> Sem mais dicas. Agora é contigo, operador.");
        }

        updateStats();
    }

    private void completeMission() {

        txtStatusPassword.setText("STATUS: ACESSO LIBERADO");

        txtLogPassword.append("\n> Senha correta: ORION");
        txtLogPassword.append("\n> Cofre digital desbloqueado.");
        txtLogPassword.append("\n> Hash fictício quebrado.");
        txtLogPassword.append("\n> MISSÃO CONCLUÍDA.");

        int xpReward = calculateXpReward();

        int currentXp = preferences.getInt("xp_total", 0);
        int newXp = currentXp + xpReward;

        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("xp_total", newXp);
        editor.putBoolean("mission2_completed", true);
        editor.apply();

        txtLogPassword.append("\n> XP recebido: +" + xpReward);
        txtLogPassword.append("\n> Rank da missão: " + getRank(xpReward));

        disableAll();

        Toast.makeText(this, "Missão 2 concluída! +" + xpReward + " XP", Toast.LENGTH_LONG).show();
    }

    private int calculateXpReward() {

        if (attempts == 4 && hintsUsed == 0) {
            return 250;
        }

        if (attempts >= 3 && hintsUsed <= 1) {
            return 200;
        }

        if (attempts >= 2) {
            return 150;
        }

        return 100;
    }

    private String getRank(int xpReward) {

        if (xpReward == 250) {
            return "S+";
        }

        if (xpReward == 200) {
            return "A";
        }

        if (xpReward == 150) {
            return "B";
        }

        return "C";
    }

    private void gameOver() {

        txtStatusPassword.setText("STATUS: MISSÃO FALHOU");
        txtLogPassword.append("\n> Muitas tentativas incorretas.");
        txtLogPassword.append("\n> Sistema bloqueado temporariamente.");
        txtLogPassword.append("\n> Use REINICIAR MISSÃO para tentar de novo.");

        disableKeyboardOnly();

        Toast.makeText(this, "Missão falhou. Reinicie para tentar novamente.", Toast.LENGTH_LONG).show();
    }

    private void resetMission() {

        if (preferences.getBoolean("mission2_completed", false)) {
            Toast.makeText(this, "Missão já concluída.", Toast.LENGTH_SHORT).show();
            return;
        }

        attempts = 4;
        hintsUsed = 0;
        currentInput = "";

        txtStatusPassword.setText("STATUS: AGUARDANDO TENTATIVA");

        txtLogPassword.setText(
                "> Arquivo criptografado encontrado." +
                        "\n> Dica 1: nome do projeto mencionado na missão 1."
        );

        updatePasswordDisplay();
        updateStats();
        enableAll();

        Toast.makeText(this, "Missão reiniciada.", Toast.LENGTH_SHORT).show();
    }

    private void updateStats() {
        txtStatsPassword.setText(
                "Tentativas: " + attempts +
                        " | Dicas usadas: " + hintsUsed +
                        " | XP: até +250"
        );
    }

    private void showCompletedMission() {

        txtStatusPassword.setText("STATUS: MISSÃO JÁ CONCLUÍDA");
        txtStatsPassword.setText("Recompensa já recebida.");

        txtLogPassword.setText(
                "> Missão já finalizada." +
                        "\n> Senha ORION validada." +
                        "\n> XP já foi adicionado ao perfil."
        );

        currentInput = "ORION";
        updatePasswordDisplay();

        disableAll();
    }

    private void disableKeyboardOnly() {
        btnKeyO.setEnabled(false);
        btnKeyR.setEnabled(false);
        btnKeyI.setEnabled(false);
        btnKeyN.setEnabled(false);
        btnKeyA.setEnabled(false);
        btnKeyB.setEnabled(false);
        btnKeyC.setEnabled(false);
        btnKeyX.setEnabled(false);
        btnKeyZ.setEnabled(false);

        btnDeleteKey.setEnabled(false);
        btnCheckPassword.setEnabled(false);
        btnPasswordHint.setEnabled(false);

        btnResetPasswordMission.setEnabled(true);
    }

    private void disableAll() {
        btnKeyO.setEnabled(false);
        btnKeyR.setEnabled(false);
        btnKeyI.setEnabled(false);
        btnKeyN.setEnabled(false);
        btnKeyA.setEnabled(false);
        btnKeyB.setEnabled(false);
        btnKeyC.setEnabled(false);
        btnKeyX.setEnabled(false);
        btnKeyZ.setEnabled(false);

        btnDeleteKey.setEnabled(false);
        btnCheckPassword.setEnabled(false);
        btnPasswordHint.setEnabled(false);
        btnResetPasswordMission.setEnabled(false);
    }

    private void enableAll() {
        btnKeyO.setEnabled(true);
        btnKeyR.setEnabled(true);
        btnKeyI.setEnabled(true);
        btnKeyN.setEnabled(true);
        btnKeyA.setEnabled(true);
        btnKeyB.setEnabled(true);
        btnKeyC.setEnabled(true);
        btnKeyX.setEnabled(true);
        btnKeyZ.setEnabled(true);

        btnDeleteKey.setEnabled(true);
        btnCheckPassword.setEnabled(true);
        btnPasswordHint.setEnabled(true);
        btnResetPasswordMission.setEnabled(true);
    }
}