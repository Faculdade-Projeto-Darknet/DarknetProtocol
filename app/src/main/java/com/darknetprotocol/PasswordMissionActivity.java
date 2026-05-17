package com.darknetprotocol;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class PasswordMissionActivity extends AppCompatActivity {

    TextView txtStatusPassword, txtStatsPassword, txtLogPassword;
    EditText edtPassword;
    Button btnCheckPassword, btnPasswordHint, btnResetPasswordMission;

    SharedPreferences preferences;

    String correctPassword = "ORION";

    int attempts = 4;
    int hintsUsed = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_mission);

        txtStatusPassword = findViewById(R.id.txtStatusPassword);
        txtStatsPassword = findViewById(R.id.txtStatsPassword);
        txtLogPassword = findViewById(R.id.txtLogPassword);
        edtPassword = findViewById(R.id.edtPassword);

        btnCheckPassword = findViewById(R.id.btnCheckPassword);
        btnPasswordHint = findViewById(R.id.btnPasswordHint);
        btnResetPasswordMission = findViewById(R.id.btnResetPasswordMission);

        preferences = getSharedPreferences("player_data", MODE_PRIVATE);

        if (preferences.getBoolean("mission2_completed", false)) {
            showCompletedMission();
        }

        btnCheckPassword.setOnClickListener(v -> checkPassword());
        btnPasswordHint.setOnClickListener(v -> showHint());
        btnResetPasswordMission.setOnClickListener(v -> resetMission());
    }

    private void checkPassword() {

        if (preferences.getBoolean("mission2_completed", false)) {
            Toast.makeText(this, "Missão já concluída.", Toast.LENGTH_SHORT).show();
            return;
        }

        String input = edtPassword.getText().toString().trim().toUpperCase();

        if (input.isEmpty()) {
            Toast.makeText(this, "Digite uma senha primeiro, gênio do teclado.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (input.equals(correctPassword)) {
            completeMission();
        } else {
            attempts--;

            txtStatusPassword.setText("STATUS: SENHA INCORRETA");
            txtLogPassword.append("\n> Tentativa falhou: " + input);
            txtLogPassword.append("\n> Assinatura inválida.");

            updateStats();

            if (attempts <= 0) {
                gameOver();
            } else {
                Toast.makeText(this, "Senha errada. Tentativas restantes: " + attempts, Toast.LENGTH_SHORT).show();
            }
        }

        edtPassword.setText("");
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
            txtLogPassword.append("\n> Dica 4: O _ I O N");
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

        btnCheckPassword.setEnabled(false);
        btnPasswordHint.setEnabled(false);

        btnCheckPassword.setAlpha(0.5f);
        btnPasswordHint.setAlpha(0.5f);

        Toast.makeText(this, "Missão falhou. Reinicie para tentar novamente.", Toast.LENGTH_LONG).show();
    }

    private void resetMission() {

        if (preferences.getBoolean("mission2_completed", false)) {
            Toast.makeText(this, "Missão já concluída.", Toast.LENGTH_SHORT).show();
            return;
        }

        attempts = 4;
        hintsUsed = 0;

        edtPassword.setText("");
        txtStatusPassword.setText("STATUS: AGUARDANDO TENTATIVA");

        txtLogPassword.setText(
                "> Arquivo criptografado encontrado." +
                        "\n> Dica 1: nome do projeto mencionado na missão 1."
        );

        btnCheckPassword.setEnabled(true);
        btnPasswordHint.setEnabled(true);

        btnCheckPassword.setAlpha(1f);
        btnPasswordHint.setAlpha(1f);

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

        txtLogPassword.setText(
                "> Missão já finalizada." +
                        "\n> Senha ORION validada." +
                        "\n> XP já foi adicionado ao perfil."
        );

        disableAll();
    }

    private void disableAll() {

        edtPassword.setEnabled(false);

        btnCheckPassword.setEnabled(false);
        btnPasswordHint.setEnabled(false);
        btnResetPasswordMission.setEnabled(false);

        btnCheckPassword.setAlpha(0.5f);
        btnPasswordHint.setAlpha(0.5f);
        btnResetPasswordMission.setAlpha(0.5f);
    }
}