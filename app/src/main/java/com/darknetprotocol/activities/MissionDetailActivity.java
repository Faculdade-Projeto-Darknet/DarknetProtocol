package com.darknetprotocol.activities;

// Importa Bundle, utilizado no ciclo de vida da Activity
import android.os.Bundle;

// Importa componentes visuais utilizados na interface
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

// Classe base para Activities compatíveis com AppCompat
import androidx.appcompat.app.AppCompatActivity;

// Importa recursos do projeto (layouts, ids, sons, etc.)
import com.darknetprotocol.R;

// Classe responsável por reproduzir efeitos sonoros
import com.darknetprotocol.utils.SoundManager;

// Classe responsável por salvar e carregar progresso na nuvem
import com.darknetprotocol.utils.CloudSaveManager;

// Classe responsável pelo armazenamento local dos dados do jogador
import com.darknetprotocol.utils.PlayerPrefs;

// Activity responsável pela primeira missão prática do jogo
public class MissionDetailActivity extends AppCompatActivity {

    // Exibe o status atual da missão
    TextView txtStatus;

    // Exibe estatísticas da missão
    TextView txtStats;

    // Exibe mensagens e eventos ocorridos durante a missão
    TextView txtLog;

    // Barra de progresso da missão
    ProgressBar progressMission;

    // Botões dos comandos disponíveis
    Button btnScan;
    Button btnProxy;
    Button btnBypass;
    Button btnExtract;

    // Botão para reiniciar a tentativa
    Button btnReset;

    // Gerencia dados salvos do jogador
    PlayerPrefs playerPrefs;

    // Controla a etapa atual da sequência
    int step = 0;

    // Quantidade de tentativas restantes
    int attempts = 3;

    // Nível de detecção acumulado
    int detection = 0;

    // Sequência correta para concluir a missão
    String[] correctSequence = {
            "SCAN",
            "PROXY",
            "BYPASS",
            "EXTRACT"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Define o layout da Activity
        setContentView(R.layout.activity_mission_detail);

        // Liga os componentes visuais às variáveis Java
        txtStatus = findViewById(R.id.txtStatus);
        txtStats = findViewById(R.id.txtStats);
        txtLog = findViewById(R.id.txtLog);

        progressMission = findViewById(R.id.progressMission);

        btnScan = findViewById(R.id.btnScan);
        btnProxy = findViewById(R.id.btnProxy);
        btnBypass = findViewById(R.id.btnBypass);
        btnExtract = findViewById(R.id.btnExtract);
        btnReset = findViewById(R.id.btnReset);

        // Inicializa o sistema de preferências do jogador
        playerPrefs = new PlayerPrefs(this);

        // Verifica se a missão já foi concluída anteriormente
        if (playerPrefs.isMission1Completed()) {
            showCompletedMission();
        }

        // Define ações para os botões de comando
        btnScan.setOnClickListener(v -> executeCommand("SCAN"));
        btnProxy.setOnClickListener(v -> executeCommand("PROXY"));
        btnBypass.setOnClickListener(v -> executeCommand("BYPASS"));
        btnExtract.setOnClickListener(v -> executeCommand("EXTRACT"));

        // Define ação para reiniciar a missão
        btnReset.setOnClickListener(v -> resetAttempt());

        // Atualiza as estatísticas iniciais
        updateStats();
    }

    // Executa um comando selecionado pelo jogador
    private void executeCommand(String command) {

        // Impede jogar novamente após concluir a missão
        if (playerPrefs.isMission1Completed()) {
            Toast.makeText(this, "Missão já concluída.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Reproduz som de clique
        SoundManager.playSound(this, R.raw.cyber_click);

        // Impede execução caso a missão esteja bloqueada
        if (attempts <= 0 || detection >= 100) {
            txtLog.append("\n> Sistema bloqueado. Use reiniciar tentativa.");
            return;
        }

        // Verifica se o comando corresponde à etapa atual da sequência
        if (command.equals(correctSequence[step])) {

            // Avança para a próxima etapa
            step++;

            // Registra sucesso no log
            txtLog.append("\n> " + command + " executado com sucesso.");

            // Atualiza o progresso da missão
            updateMissionProgress();

            // Se todas as etapas foram concluídas
            if (step == correctSequence.length) {
                completeMission();
            }

        } else {

            // Trata erro de sequência
            failCommand(command);
        }
    }

    // Atualiza o progresso da missão conforme o avanço do jogador
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

    // Executa ações quando o jogador escolhe um comando incorreto
    private void failCommand(String command) {

        // Reproduz som de erro
        SoundManager.playSound(this, R.raw.cyber_error);

        // Reduz tentativas
        attempts--;

        // Aumenta o nível de detecção
        detection += 35;

        // Reinicia a sequência
        step = 0;

        // Zera a barra de progresso
        progressMission.setProgress(0);

        // Atualiza status
        txtStatus.setText("STATUS: ERRO NA SEQUÊNCIA");

        // Registra informações no log
        txtLog.append("\n> Comando inválido: " + command);
        txtLog.append("\n> Sequência reiniciada.");
        txtLog.append("\n> Detecção aumentou.");

        updateStats();

        // Verifica condição de derrota
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

    // Finaliza a missão com sucesso
    private void completeMission() {

        // Reproduz som de sucesso
        SoundManager.playSound(this, R.raw.cyber_success);

        // Completa a barra de progresso
        progressMission.setProgress(100);

        // Atualiza status
        txtStatus.setText("STATUS: MISSÃO CONCLUÍDA");

        // Adiciona mensagens finais ao log
        txtLog.append("\n> EXTRACT concluído.");
        txtLog.append("\n> Arquivos do Projeto Orion recuperados.");
        txtLog.append("\n> Rastros apagados.");
        txtLog.append("\n> MISSÃO FINALIZADA.");

        // Calcula recompensa de XP
        int xpReward = calculateXpReward();

        // Adiciona XP ao perfil
        playerPrefs.addXp(xpReward);

        // Marca missão como concluída
        playerPrefs.setMission1Completed(true);

        // Salva progresso na nuvem
        new CloudSaveManager(playerPrefs)
                .saveProgress();

        // Obtém rank da missão
        String rank = getRank(xpReward);

        // Exibe recompensa recebida
        txtLog.append("\n> XP recebido: +" + xpReward);
        txtLog.append("\n> Rank da missão: " + rank);
        txtLog.append("\n> Progresso sincronizado na nuvem.");

        // Desativa todos os botões
        disableAllButtons();

        Toast.makeText(
                this,
                "Missão concluída! +" + xpReward + " XP",
                Toast.LENGTH_LONG
        ).show();
    }

    // Calcula XP com base no desempenho do jogador
    private int calculateXpReward() {

        if (attempts == 3 && detection == 0) {
            return 200;
        }

        if (attempts == 2) {
            return 150;
        }

        return 100;
    }

    // Define o rank de acordo com a recompensa recebida
    private String getRank(int xpReward) {

        if (xpReward == 200) {
            return "S+";
        }

        if (xpReward == 150) {
            return "A";
        }

        return "B";
    }

    // Executa ações quando a missão falha
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

    // Reinicia a missão para uma nova tentativa
    private void resetAttempt() {

        if (playerPrefs.isMission1Completed()) {

            Toast.makeText(
                    this,
                    "Missão já concluída.",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        // Reproduz som ao reiniciar
        SoundManager.playSound(this, R.raw.cyber_error);

        // Restaura valores iniciais
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

        // Reativa os comandos
        enableCommandButtons();

        Toast.makeText(
                this,
                "Tentativa reiniciada.",
                Toast.LENGTH_SHORT
        ).show();
    }

    // Atualiza informações exibidas na área de estatísticas
    private void updateStats() {

        txtStats.setText(
                "Tentativas: " + attempts +
                        " | Detecção: " + detection + "%" +
                        " | XP: até +200"
        );
    }

    // Exibe a tela de missão já concluída
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

    // Desativa todos os botões da tela
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

    // Desativa apenas os botões de comando
    private void disableCommandButtons() {

        btnScan.setEnabled(false);
        btnProxy.setEnabled(false);
        btnBypass.setEnabled(false);
        btnExtract.setEnabled(false);

        btnScan.setAlpha(0.5f);
        btnProxy.setAlpha(0.5f);
        btnBypass.setAlpha(0.5f);
        btnExtract.setAlpha(0.5f);

        // Mantém o botão de reset ativo
        btnReset.setEnabled(true);
        btnReset.setAlpha(1f);
    }

    // Reativa todos os comandos da missão
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