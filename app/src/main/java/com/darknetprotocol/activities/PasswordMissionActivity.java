package com.darknetprotocol.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.darknetprotocol.R;
import com.darknetprotocol.SoundManager;
import com.darknetprotocol.utils.CloudSaveManager;
import com.darknetprotocol.utils.PlayerPrefs;

public class PasswordMissionActivity extends AppCompatActivity {

    TextView txtStatusPassword;
    TextView txtStatsPassword;
    TextView txtLogPassword;
    TextView txtPasswordDisplay;

    Button btnCheckPassword, btnPasswordHint, btnResetPasswordMission, btnDeleteKey;
    Button btnKeyO, btnKeyR, btnKeyI, btnKeyN, btnKeyA, btnKeyB, btnKeyC, btnKeyX, btnKeyZ;

    PlayerPrefs playerPrefs;

    String correctPassword = "ORION";
    String currentInput = "";

    int attempts = 4;
    int hintsUsed = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_mission);

        playerPrefs = new PlayerPrefs(this);

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

        if (playerPrefs.isMission2Completed()) {
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
        updateStats();
    }

    private void addLetter(String letter) {
        if (playerPrefs.isMission2Completed()) return;

        if (currentInput.length() >= 5) {
            Toast.makeText(this, "A senha tem só 5 letras, Sherlock.", Toast.LENGTH_SHORT).show();
            return;
        }

        SoundManager.playSound(this, R.raw.cyber_click);
        currentInput += letter;
        updatePasswordDisplay();
    }

    private void deleteLetter() {
        if (currentInput.length() > 0) {
            SoundManager.playSound(this, R.raw.cyber_click);
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
        if (playerPrefs.isMission2Completed()) {
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
            SoundManager.playSound(this, R.raw.cyber_error);

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
        if (playerPrefs.isMission2Completed()) {
            Toast.makeText(this, "Missão já concluída.", Toast.LENGTH_SHORT).show();
            return;
        }

        SoundManager.playSound(this, R.raw.cyber_click);

        hintsUsed++;

        if (hintsUsed == 1) {
            txtLogPassword.append("\n> Pista 2: a senha é o nome do projeto extraído na Missão 1.");
        } else if (hintsUsed == 2) {
            txtLogPassword.append("\n> Pista 3: a senha possui 5 letras.");
        } else if (hintsUsed == 3) {
            txtLogPassword.append("\n> Pista 4: começa com O e termina com N.");
        } else if (hintsUsed == 4) {
            txtLogPassword.append("\n> Pista final: O R I O N.");
        } else {
            txtLogPassword.append("\n> Sem mais pistas. O sistema praticamente resolveu isso pra você.");
        }

        updateStats();
    }

    private void completeMission() {
        SoundManager.playSound(this, R.raw.cyber_success);

        txtStatusPassword.setText("STATUS: ACESSO LIBERADO");
        txtLogPassword.append("\n> Senha correta: ORION");
        txtLogPassword.append("\n> Cofre digital desbloqueado.");
        txtLogPassword.append("\n> Hash fictício quebrado.");
        txtLogPassword.append("\n> MISSÃO CONCLUÍDA.");

        int xpReward = calculateXpReward();

        playerPrefs.addXp(xpReward);
        playerPrefs.setMission2Completed(true);

        new CloudSaveManager(playerPrefs).saveProgress();

        txtLogPassword.append("\n> XP recebido: +" + xpReward);
        txtLogPassword.append("\n> Rank da missão: " + getRank(xpReward));
        txtLogPassword.append("\n> Progresso sincronizado na nuvem.");

        disableAll();

        Toast.makeText(this, "Missão 2 concluída! +" + xpReward + " XP", Toast.LENGTH_LONG).show();
    }

    private int calculateXpReward() {
        if (attempts == 4 && hintsUsed == 0) return 250;
        if (attempts >= 3 && hintsUsed <= 1) return 200;
        if (attempts >= 2) return 150;
        return 100;
    }

    private String getRank(int xpReward) {
        if (xpReward == 250) return "S+";
        if (xpReward == 200) return "A";
        if (xpReward == 150) return "B";
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
        if (playerPrefs.isMission2Completed()) {
            Toast.makeText(this, "Missão já concluída.", Toast.LENGTH_SHORT).show();
            return;
        }

        SoundManager.playSound(this, R.raw.cyber_error);

        attempts = 4;
        hintsUsed = 0;
        currentInput = "";

        txtStatusPassword.setText("STATUS: AGUARDANDO TENTATIVA");
        txtLogPassword.setText("> Arquivo criptografado encontrado.\n> Pista 1: o arquivo roubado na Missão 1 se chamava Projeto Orion.");

        updatePasswordDisplay();
        updateStats();
        enableAll();

        Toast.makeText(this, "Missão reiniciada.", Toast.LENGTH_SHORT).show();
    }

    private void updateStats() {
        txtStatsPassword.setText("Tentativas: " + attempts + " | Dicas usadas: " + hintsUsed + " | XP: até +250");
    }

    private void showCompletedMission() {
        txtStatusPassword.setText("STATUS: MISSÃO JÁ CONCLUÍDA");
        txtStatsPassword.setText("Recompensa já recebida.");
        txtLogPassword.setText("> Missão já finalizada.\n> Senha ORION validada.\n> XP já foi adicionado ao perfil.");
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
        disableKeyboardOnly();
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