package com.darknetprotocol.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.darknetprotocol.R;
import com.darknetprotocol.SoundManager;
import com.darknetprotocol.utils.CloudSaveManager;
import com.darknetprotocol.utils.PlayerPrefs;

public class MissionDetailActivity extends AppCompatActivity {

    TextView txtStatus;
    TextView txtStats;
    TextView txtLog;

    ProgressBar progressMission;

    Button btnScan;
    Button btnProxy;
    Button btnBypass;
    Button btnExtract;
    Button btnReset;

    PlayerPrefs playerPrefs;

    int step = 0;
    int attempts = 3;
    int detection = 0;

    String[] correctSequence = {
            "SCAN",
            "PROXY",
            "BYPASS",
            "EXTRACT"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mission_detail);

        txtStatus = findViewById(R.id.txtStatus);
        txtStats = findViewById(R.id.txtStats);
        txtLog = findViewById(R.id.txtLog);

        progressMission = findViewById(R.id.progressMission);

        btnScan = findViewById(R.id.btnScan);
        btnProxy = findViewById(R.id.btnProxy);
        btnBypass = findViewById(R.id.btnBypass);
        btnExtract = findViewById(R.id.btnExtract);
        btnReset = findViewById(R.id.btnReset);

        playerPrefs = new PlayerPrefs(this);

        if (playerPrefs.isMission1Completed()) {
            showCompletedMission();
        }

        btnScan.setOnClickListener(v -> executeCommand("SCAN"));
        btnProxy.setOnClickListener(v -> executeCommand("PROXY"));
        btnBypass.setOnClickListener(v -> executeCommand("BYPASS"));
        btnExtract.setOnClickListener(v -> executeCommand("EXTRACT"));

        btnReset.setOnClickListener(v -> resetAttempt());

        updateStats();
    }

    private void executeCommand(String command) {

        if (playerPrefs.isMission1Completed()) {
            Toast.makeText(this, "Missão já concluída.", Toast.LENGTH_SHORT).show();
            return;
        }

        SoundManager.playSound(this, R.raw.cyber_click);

        if (attempts <= 0 || detection >= 100) {
            txtLog.append("\n> Sistema bloqueado. Use reiniciar tentativa.");
            return;
        }

        if (command.equals(correctSequence[step])) {

            step++;

            txtLog.append("\n> " + command + " executado com sucesso.");

            updateMissionProgress();

            if (step == correctSequence.length) {
                completeMission();
            }

        } else {

            failCommand(command);
        }
    }

    private void updateMissionProgress() {

        if (step == 1) {
            txtStatus.setText("STATUS: REDE MAPEADA");
            progressMission.setProgress(25);
            txtLog.append("\n> Portas fictícias analisadas.");
        }

        if (step == 2) {
            txtStatus.setText("STATUS: PROXY ATIVADO");
            progressMission.setProgress(50);
            txtLog.append("\n> Rota segura simulada.");
        }

        if (step == 3) {
            txtStatus.setText("STATUS: FIREWALL CONTORNADO");
            progressMission.setProgress(75);
            txtLog.append("\n> Camada de defesa fictícia neutralizada.");
        }

        updateStats();
    }

    private void failCommand(String command) {

        SoundManager.playSound(this, R.raw.cyber_error);

        attempts--;
        detection += 35;
        step = 0;

        progressMission.setProgress(0);

        txtStatus.setText("STATUS: ERRO NA SEQUÊNCIA");

        txtLog.append("\n> Comando inválido: " + command);
        txtLog.append("\n> Sequência reiniciada.");
        txtLog.append("\n> Detecção aumentou.");

        updateStats();

        if (attempts <= 0 || detection >= 100) {

            gameOver();

        } else {

            Toast.makeText(
                    this,
                    "Sequência errada. Tentativas restantes: " + attempts,
                    Toast.LENGTH_SHORT
            ).show();
        }
    }

    private void completeMission() {

        SoundManager.playSound(this, R.raw.cyber_success);

        progressMission.setProgress(100);

        txtStatus.setText("STATUS: MISSÃO CONCLUÍDA");

        txtLog.append("\n> EXTRACT concluído.");
        txtLog.append("\n> Arquivos do Projeto Orion recuperados.");
        txtLog.append("\n> Rastros apagados.");
        txtLog.append("\n> MISSÃO FINALIZADA.");

        int xpReward = calculateXpReward();

        playerPrefs.addXp(xpReward);
        playerPrefs.setMission1Completed(true);

        new CloudSaveManager(playerPrefs)
                .saveProgress();

        String rank = getRank(xpReward);

        txtLog.append("\n> XP recebido: +" + xpReward);
        txtLog.append("\n> Rank da missão: " + rank);
        txtLog.append("\n> Progresso sincronizado na nuvem.");

        disableAllButtons();

        Toast.makeText(
                this,
                "Missão concluída! +" + xpReward + " XP",
                Toast.LENGTH_LONG
        ).show();
    }

    private int calculateXpReward() {

        if (attempts == 3 && detection == 0) {
            return 200;
        }

        if (attempts == 2) {
            return 150;
        }

        return 100;
    }

    private String getRank(int xpReward) {

        if (xpReward == 200) {
            return "S+";
        }

        if (xpReward == 150) {
            return "A";
        }

        return "B";
    }

    private void gameOver() {

        txtStatus.setText("STATUS: MISSÃO FALHOU");

        txtLog.append("\n> ALERTA MÁXIMO.");
        txtLog.append("\n> Sistema bloqueado.");
        txtLog.append("\n> Use REINICIAR TENTATIVA para tentar novamente.");

        disableCommandButtons();

        Toast.makeText(
                this,
                "Missão falhou. Reinicie a tentativa.",
                Toast.LENGTH_LONG
        ).show();
    }

    private void resetAttempt() {

        if (playerPrefs.isMission1Completed()) {

            Toast.makeText(
                    this,
                    "Missão já concluída.",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        SoundManager.playSound(this, R.raw.cyber_error);

        step = 0;
        attempts = 3;
        detection = 0;

        progressMission.setProgress(0);

        txtStatus.setText("STATUS: AGUARDANDO COMANDO");

        txtLog.setText(
                "> Sistema reiniciado.\n" +
                        "> Descubra a sequência correta de infiltração.\n" +
                        "> Dica: mapeie a rede antes de esconder o tráfego."
        );

        updateStats();

        enableCommandButtons();

        Toast.makeText(
                this,
                "Tentativa reiniciada.",
                Toast.LENGTH_SHORT
        ).show();
    }

    private void updateStats() {

        txtStats.setText(
                "Tentativas: " + attempts +
                        " | Detecção: " + detection + "%" +
                        " | XP: até +200"
        );
    }

    private void showCompletedMission() {

        txtStatus.setText("STATUS: MISSÃO JÁ CONCLUÍDA");

        txtStats.setText("Recompensa já recebida.");

        progressMission.setProgress(100);

        txtLog.setText(
                "> Missão já finalizada.\n" +
                        "> Arquivos do Projeto Orion recuperados.\n" +
                        "> XP já foi adicionado ao perfil."
        );

        disableAllButtons();
    }

    private void disableAllButtons() {

        btnScan.setEnabled(false);
        btnProxy.setEnabled(false);
        btnBypass.setEnabled(false);
        btnExtract.setEnabled(false);
        btnReset.setEnabled(false);

        btnScan.setAlpha(0.5f);
        btnProxy.setAlpha(0.5f);
        btnBypass.setAlpha(0.5f);
        btnExtract.setAlpha(0.5f);
        btnReset.setAlpha(0.5f);
    }

    private void disableCommandButtons() {

        btnScan.setEnabled(false);
        btnProxy.setEnabled(false);
        btnBypass.setEnabled(false);
        btnExtract.setEnabled(false);

        btnScan.setAlpha(0.5f);
        btnProxy.setAlpha(0.5f);
        btnBypass.setAlpha(0.5f);
        btnExtract.setAlpha(0.5f);

        btnReset.setEnabled(true);
        btnReset.setAlpha(1f);
    }

    private void enableCommandButtons() {

        btnScan.setEnabled(true);
        btnProxy.setEnabled(true);
        btnBypass.setEnabled(true);
        btnExtract.setEnabled(true);
        btnReset.setEnabled(true);

        btnScan.setAlpha(1f);
        btnProxy.setAlpha(1f);
        btnBypass.setAlpha(1f);
        btnExtract.setAlpha(1f);
        btnReset.setAlpha(1f);
    }
}