package com.darknetprotocol.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.darknetprotocol.R;
import com.darknetprotocol.SoundManager;
import com.darknetprotocol.utils.PlayerPrefs;

public class IpMissionActivity extends AppCompatActivity {

    TextView txtIpStatus;
    TextView txtIpStats;
    TextView txtIpLog;

    Button btnIp1;
    Button btnIp2;
    Button btnIp3;
    Button btnIp4;
    Button btnResetIpMission;

    PlayerPrefs playerPrefs;

    int attempts = 3;
    int detection = 0;

    String correctIp = "185.221.77.13";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ip_mission);

        playerPrefs = new PlayerPrefs(this);

        txtIpStatus = findViewById(R.id.txtIpStatus);
        txtIpStats = findViewById(R.id.txtIpStats);
        txtIpLog = findViewById(R.id.txtIpLog);

        btnIp1 = findViewById(R.id.btnIp1);
        btnIp2 = findViewById(R.id.btnIp2);
        btnIp3 = findViewById(R.id.btnIp3);
        btnIp4 = findViewById(R.id.btnIp4);
        btnResetIpMission = findViewById(R.id.btnResetIpMission);

        if (playerPrefs.isMission4Completed()) {
            showCompletedMission();
        }

        btnIp1.setOnClickListener(v -> checkIp("192.168.0.12"));
        btnIp2.setOnClickListener(v -> checkIp("10.0.0.45"));
        btnIp3.setOnClickListener(v -> checkIp("172.16.4.91"));
        btnIp4.setOnClickListener(v -> checkIp("185.221.77.13"));

        btnResetIpMission.setOnClickListener(v -> resetMission());

        updateStats();
    }

    private void checkIp(String selectedIp) {
        if (playerPrefs.isMission4Completed()) {
            Toast.makeText(this, "Missão já concluída.", Toast.LENGTH_SHORT).show();
            return;
        }

        // 🔊 SOM DE CLIQUE: Varredura de IP iniciada
        SoundManager.playSound(this, R.raw.cyber_click);

        if (attempts <= 0 || detection >= 100) {
            txtIpLog.append("\n> Sistema bloqueado. Reinicie a missão.");
            return;
        }

        txtIpLog.append("\n> IP analisado: " + selectedIp);

        if (selectedIp.equals(correctIp)) {
            completeMission();
        } else {
            // 🔊 SOM DE ERRO: Alvo inofensivo interno selecionado
            SoundManager.playSound(this, R.raw.cyber_error);

            attempts--;
            detection += 35;

            txtIpStatus.setText("STATUS: IP INCORRETO");
            txtIpLog.append("\n> Endereço pertence a uma rede local.");
            txtIpLog.append("\n> Detecção aumentou.");

            updateStats();

            if (attempts <= 0 || detection >= 100) {
                gameOver();
            } else {
                Toast.makeText(this, "IP errado. Tentativas restantes: " + attempts, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void completeMission() {
        // 🔊 SOM DE SUCESSO: Invasor detectado e isolado com sucesso! Juego ganho!
        SoundManager.playSound(this, R.raw.cyber_success);

        txtIpStatus.setText("STATUS: NÓ INVASOR IDENTIFICADO");
        txtIpLog.append("\n> IP suspeito confirmado: 185.221.77.13\n> Origem fora da rede local.\n> Tráfego externo isolado.\n> MISSÃO CONCLUÍDA.");

        int xpReward = calculateXpReward();
        playerPrefs.addXp(xpReward);
        playerPrefs.setMission4Completed(true);

        txtIpLog.append("\n> XP recebido: +" + xpReward);
        txtIpLog.append("\n> Rank da missão: " + getRank(xpReward));

        disableAll();
        Toast.makeText(this, "Missão 4 concluída! +" + xpReward + " XP", Toast.LENGTH_LONG).show();
    }

    private int calculateXpReward() {
        if (attempts == 3 && detection == 0) return 350;
        if (attempts == 2) return 280;
        return 180;
    }

    private String getRank(int xpReward) {
        if (xpReward == 350) return "S+";
        if (xpReward == 280) return "A";
        return "B";
    }

    private void gameOver() {
        txtIpStatus.setText("STATUS: MISSÃO FALHOU");
        txtIpLog.append("\n> Muitas escolhas incorretas.\n> Sistema entrou em alerta.\n> Use REINICIAR MISSÃO para tentar novamente.");
        disableIpButtons();
        Toast.makeText(this, "Missão falhou. Reinicie para tentar novamente.", Toast.LENGTH_LONG).show();
    }

    private void resetMission() {
        if (playerPrefs.isMission4Completed()) {
            Toast.makeText(this, "Missão já concluída.", Toast.LENGTH_SHORT).show();
            return;
        }

        // 🔊 SOM DE ERRO: Firewall restaurado para o padrão
        SoundManager.playSound(this, R.raw.cyber_error);

        attempts = 3;
        detection = 0;

        txtIpStatus.setText("STATUS: AGUARDANDO VARREDURA");
        txtIpLog.setText("> Analise as conexões ativas no roteamento fictício.\n> Encontre o terminal intruso oculto fora da sub-rede local.");

        enableIpButtons();
        updateStats();
        Toast.makeText(this, "Missão reiniciada.", Toast.LENGTH_SHORT).show();
    }

    private void updateStats() {
        txtIpStats.setText("Tentativas: " + attempts + " | Detecção: " + detection + "%" + " | XP: até +350");
    }

    private void showCompletedMission() {
        txtIpStatus.setText("STATUS: MISSÃO JÁ CONCLUÍDA");
        txtIpStats.setText("Recompensa já recebida.");
        txtIpLog.setText("> Missão já finalizada.\n> Endereço externo isolado.\n> Nó 185.221.77.13 neutralizado.");
        disableAll();
    }

    private void disableIpButtons() {
        btnIp1.setEnabled(false); btnIp2.setEnabled(false); btnIp3.setEnabled(false); btnIp4.setEnabled(false);
        btnIp1.setAlpha(0.5f); btnIp2.setAlpha(0.5f); btnIp3.setAlpha(0.5f); btnIp4.setAlpha(0.5f);
    }

    private void enableIpButtons() {
        btnIp1.setEnabled(true); btnIp2.setEnabled(true); btnIp3.setEnabled(true); btnIp4.setEnabled(true);
        btnIp1.setAlpha(1f); btnIp2.setAlpha(1f); btnIp3.setAlpha(1f); btnIp4.setAlpha(1f);
    }

    private void disableAll() {
        disableIpButtons();
        btnResetIpMission.setEnabled(false);
        btnResetIpMission.setAlpha(0.5f);
    }
}