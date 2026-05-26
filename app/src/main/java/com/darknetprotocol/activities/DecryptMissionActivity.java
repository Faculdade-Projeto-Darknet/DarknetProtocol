package com.darknetprotocol.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.darknetprotocol.R;
import com.darknetprotocol.utils.SoundManager;
import com.darknetprotocol.utils.CloudSaveManager;
import com.darknetprotocol.utils.PlayerPrefs;

public class DecryptMissionActivity extends AppCompatActivity {

    TextView txtDecryptStatus;
    TextView txtDecryptStats;
    TextView txtDecryptLog;

    EditText edtDecryptAnswer;

    Button btnCheckDecrypt;
    Button btnDecryptHint;
    Button btnResetDecrypt;

    PlayerPrefs playerPrefs;

    int attempts = 3;
    int hintsUsed = 0;

    String correctAnswer = "ORION";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_decrypt_mission);

        playerPrefs = new PlayerPrefs(this);

        txtDecryptStatus = findViewById(R.id.txtDecryptStatus);
        txtDecryptStats = findViewById(R.id.txtDecryptStats);
        txtDecryptLog = findViewById(R.id.txtDecryptLog);

        edtDecryptAnswer = findViewById(R.id.edtDecryptAnswer);

        btnCheckDecrypt = findViewById(R.id.btnCheckDecrypt);
        btnDecryptHint = findViewById(R.id.btnDecryptHint);
        btnResetDecrypt = findViewById(R.id.btnResetDecrypt);

        if (playerPrefs.isMission3Completed()) {
            showCompletedMission();
        }

        btnCheckDecrypt.setOnClickListener(v -> checkAnswer());
        btnDecryptHint.setOnClickListener(v -> showHint());
        btnResetDecrypt.setOnClickListener(v -> resetMission());

        updateStats();
    }

    private void checkAnswer() {
        if (playerPrefs.isMission3Completed()) {
            Toast.makeText(this, "Missão já concluída.", Toast.LENGTH_SHORT).show();
            return;
        }

        SoundManager.playSound(this, R.raw.cyber_click);

        String answer = edtDecryptAnswer.getText().toString().trim().toUpperCase();

        if (answer.isEmpty()) {
            Toast.makeText(this, "Digite a resposta primeiro, operador.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (answer.equals(correctAnswer)) {
            completeMission();
        } else {
            SoundManager.playSound(this, R.raw.cyber_error);

            attempts--;
            txtDecryptStatus.setText("STATUS: FALHA NA DESCRIPTOGRAFIA");
            txtDecryptLog.append("\n> Resposta inválida: " + answer);
            txtDecryptLog.append("\n> Chave rejeitada.");

            edtDecryptAnswer.setText("");
            updateStats();

            if (attempts <= 0) {
                gameOver();
            } else {
                Toast.makeText(this, "Errado. Tentativas restantes: " + attempts, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void showHint() {
        if (playerPrefs.isMission3Completed()) {
            Toast.makeText(this, "Missão já concluída.", Toast.LENGTH_SHORT).show();
            return;
        }

        SoundManager.playSound(this, R.raw.cyber_click);

        hintsUsed++;

        if (hintsUsed == 1) {
            txtDecryptLog.append("\n> Dica 2: cada letra foi deslocada 3 posições para frente.");
        } else if (hintsUsed == 2) {
            txtDecryptLog.append("\n> Dica 3: R vira O.");
        } else if (hintsUsed == 3) {
            txtDecryptLog.append("\n> Dica final: O R I O N.");
        } else {
            txtDecryptLog.append("\n> Sem mais dicas. O sistema já abriu a porta e te entregou café.");
        }

        updateStats();
    }

    private void completeMission() {
        SoundManager.playSound(this, R.raw.cyber_success);

        txtDecryptStatus.setText("STATUS: ARQUIVO DESCRIPTOGRAFADO");
        txtDecryptLog.append("\n> Palavra correta: ORION");
        txtDecryptLog.append("\n> Arquivo ghost_file.enc aberto.");
        txtDecryptLog.append("\n> Dados do Projeto Orion recuperados.");
        txtDecryptLog.append("\n> MISSÃO CONCLUÍDA.");

        int xpReward = calculateXpReward();

        playerPrefs.addXp(xpReward);
        playerPrefs.setMission3Completed(true);

        new CloudSaveManager(playerPrefs).saveProgress();

        txtDecryptLog.append("\n> XP recebido: +" + xpReward);
        txtDecryptLog.append("\n> Rank da missão: " + getRank(xpReward));
        txtDecryptLog.append("\n> Progresso sincronizado na nuvem.");

        disableAll();

        Toast.makeText(this, "Missão 3 concluída! +" + xpReward + " XP", Toast.LENGTH_LONG).show();
    }

    private int calculateXpReward() {
        if (attempts == 3 && hintsUsed == 0) return 300;
        if (attempts >= 2 && hintsUsed <= 1) return 250;
        if (attempts >= 1) return 180;
        return 100;
    }

    private String getRank(int xpReward) {
        if (xpReward == 300) return "S+";
        if (xpReward == 250) return "A";
        if (xpReward == 180) return "B";
        return "C";
    }

    private void gameOver() {
        txtDecryptStatus.setText("STATUS: MISSÃO FALHOU");
        txtDecryptLog.append("\n> Muitas tentativas inválidas.");
        txtDecryptLog.append("\n> Arquivo bloqueado temporariamente.");
        txtDecryptLog.append("\n> Use REINICIAR MISSÃO para tentar novamente.");

        btnCheckDecrypt.setEnabled(false);
        btnDecryptHint.setEnabled(false);
        btnCheckDecrypt.setAlpha(0.5f);
        btnDecryptHint.setAlpha(0.5f);

        Toast.makeText(this, "Missão falhou. Reinicie para tentar novamente.", Toast.LENGTH_LONG).show();
    }

    private void resetMission() {
        if (playerPrefs.isMission3Completed()) {
            Toast.makeText(this, "Missão já concluída.", Toast.LENGTH_SHORT).show();
            return;
        }

        SoundManager.playSound(this, R.raw.cyber_error);

        attempts = 3;
        hintsUsed = 0;
        edtDecryptAnswer.setText("");

        txtDecryptStatus.setText("STATUS: ARQUIVO CRIPTOGRAFADO");
        txtDecryptLog.setText("> Arquivo encontrado: ghost_file.enc\n> Conteúdo criptografado: RULRQ\n> Dica: Cifra de César -3");

        btnCheckDecrypt.setEnabled(true);
        btnDecryptHint.setEnabled(true);
        btnCheckDecrypt.setAlpha(1f);
        btnDecryptHint.setAlpha(1f);

        updateStats();

        Toast.makeText(this, "Missão reiniciada.", Toast.LENGTH_SHORT).show();
    }

    private void updateStats() {
        txtDecryptStats.setText("Tentativas: " + attempts + " | Dicas usadas: " + hintsUsed + " | XP: até +300");
    }

    private void showCompletedMission() {
        txtDecryptStatus.setText("STATUS: MISSÃO JÁ CONCLUÍDA");
        txtDecryptStats.setText("Recompensa já recebida.");
        txtDecryptLog.setText("> Missão já finalizada.\n> Arquivo ghost_file.enc descriptografado.\n> Palavra encontrada: ORION.\n> XP já foi adicionado ao perfil.");
        edtDecryptAnswer.setText("ORION");
        disableAll();
    }

    private void disableAll() {
        edtDecryptAnswer.setEnabled(false);
        btnCheckDecrypt.setEnabled(false);
        btnDecryptHint.setEnabled(false);
        btnResetDecrypt.setEnabled(false);

        btnCheckDecrypt.setAlpha(0.5f);
        btnDecryptHint.setAlpha(0.5f);
        btnResetDecrypt.setAlpha(0.5f);
    }
}