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

public class PasswordMissionActivity extends AppCompatActivity {

    TextView txtStatusPassword;
    TextView txtStatsPassword;
    TextView txtLogPassword;

    EditText edtPasswordAnswer;

    Button btnCheckPassword;
    Button btnPasswordHint;
    Button btnResetPasswordMission;

    PlayerPrefs playerPrefs;

    String correctPassword = "ORION";

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

        edtPasswordAnswer = findViewById(R.id.edtPasswordAnswer);

        btnCheckPassword = findViewById(R.id.btnCheckPassword);
        btnPasswordHint = findViewById(R.id.btnPasswordHint);
        btnResetPasswordMission = findViewById(R.id.btnResetPasswordMission);

        if (playerPrefs.isMission2Completed()) {
            showCompletedMission();
        }

        btnCheckPassword.setOnClickListener(v -> checkPassword());
        btnPasswordHint.setOnClickListener(v -> showHint());
        btnResetPasswordMission.setOnClickListener(v -> resetMission());

        updateStats();
    }

    private void checkPassword() {
        if (playerPrefs.isMission2Completed()) {
            Toast.makeText(this, "Missão já concluída.", Toast.LENGTH_SHORT).show();
            return;
        }

        String answer = edtPasswordAnswer.getText().toString().trim().toUpperCase();

        if (answer.isEmpty()) {
            Toast.makeText(this, "Digite a senha primeiro.", Toast.LENGTH_SHORT).show();
            return;
        }

        SoundManager.playSound(this, R.raw.cyber_click);

        if (answer.equals(correctPassword)) {
            completeMission();
        } else {
            SoundManager.playSound(this, R.raw.cyber_error);

            attempts--;

            txtStatusPassword.setText("STATUS: SENHA INCORRETA");
            txtLogPassword.append("\n> Tentativa falhou: " + answer);
            txtLogPassword.append("\n> Assinatura inválida.");

            edtPasswordAnswer.setText("");
            updateStats();

            if (attempts <= 0) {
                gameOver();
            } else {
                Toast.makeText(
                        this,
                        "Senha errada. Tentativas restantes: " + attempts,
                        Toast.LENGTH_SHORT
                ).show();
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
            txtLogPassword.append("\n> Pista final: ORION.");
        } else {
            txtLogPassword.append("\n> Sem mais pistas. O sistema basicamente entregou a resposta numa bandeja.");
        }

        updateStats();
    }

    private void completeMission() {
        SoundManager.playSound(this, R.raw.cyber_success);

        txtStatusPassword.setText("STATUS: ACESSO LIBERADO");
        txtLogPassword.append("\n> Senha correta: ORION");
        txtLogPassword.append("\n> Cofre digital desbloqueado.");
        txtLogPassword.append("\n> MISSÃO CONCLUÍDA.");

        int xpReward = calculateXpReward();

        playerPrefs.addXp(xpReward);
        playerPrefs.setMission2Completed(true);

        new CloudSaveManager(playerPrefs).saveProgress();

        txtLogPassword.append("\n> XP recebido: +" + xpReward);
        txtLogPassword.append("\n> Rank da missão: " + getRank(xpReward));
        txtLogPassword.append("\n> Progresso sincronizado na nuvem.");

        disableAll();

        Toast.makeText(
                this,
                "Missão 2 concluída! +" + xpReward + " XP",
                Toast.LENGTH_LONG
        ).show();
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

        edtPasswordAnswer.setEnabled(false);
        btnCheckPassword.setEnabled(false);
        btnPasswordHint.setEnabled(false);

        btnCheckPassword.setAlpha(0.5f);
        btnPasswordHint.setAlpha(0.5f);

        Toast.makeText(
                this,
                "Missão falhou. Reinicie para tentar novamente.",
                Toast.LENGTH_LONG
        ).show();
    }

    private void resetMission() {
        if (playerPrefs.isMission2Completed()) {
            Toast.makeText(this, "Missão já concluída.", Toast.LENGTH_SHORT).show();
            return;
        }

        SoundManager.playSound(this, R.raw.cyber_error);

        attempts = 4;
        hintsUsed = 0;

        edtPasswordAnswer.setText("");
        edtPasswordAnswer.setEnabled(true);

        txtStatusPassword.setText("STATUS: AGUARDANDO TENTATIVA");
        txtLogPassword.setText("> Arquivo criptografado encontrado.\n> Dica 1: nome do projeto mencionado na missão 1.");

        btnCheckPassword.setEnabled(true);
        btnPasswordHint.setEnabled(true);
        btnResetPasswordMission.setEnabled(true);

        btnCheckPassword.setAlpha(1f);
        btnPasswordHint.setAlpha(1f);
        btnResetPasswordMission.setAlpha(1f);

        updateStats();

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
        txtLogPassword.setText("> Missão já finalizada.\n> Senha ORION validada.\n> XP já foi adicionado ao perfil.");
        edtPasswordAnswer.setText("ORION");
        disableAll();
    }

    private void disableAll() {
        edtPasswordAnswer.setEnabled(false);

        btnCheckPassword.setEnabled(false);
        btnPasswordHint.setEnabled(false);
        btnResetPasswordMission.setEnabled(false);

        btnCheckPassword.setAlpha(0.5f);
        btnPasswordHint.setAlpha(0.5f);
        btnResetPasswordMission.setAlpha(0.5f);
    }
}