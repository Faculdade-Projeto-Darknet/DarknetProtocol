package com.darknetprotocol.activities;

// Importa classes básicas do Android usadas nesta tela
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

// Importa a classe base para criar uma Activity
import androidx.appcompat.app.AppCompatActivity;

// Importa recursos do projeto, como layouts, ids e sons
import com.darknetprotocol.R;

// Importa classes auxiliares usadas para som, salvamento em nuvem e progresso do jogador
import com.darknetprotocol.utils.SoundManager;
import com.darknetprotocol.utils.CloudSaveManager;
import com.darknetprotocol.utils.PlayerPrefs;

// Activity responsável pela missão de identificação de IP suspeito
public class IpMissionActivity extends AppCompatActivity {

    // TextViews usados para mostrar status, estatísticas e logs da missão
    TextView txtIpStatus;
    TextView txtIpStats;
    TextView txtIpLog;

    // Botões com as opções de IP que o jogador pode escolher
    Button btnIp1;
    Button btnIp2;
    Button btnIp3;
    Button btnIp4;

    // Botão usado para reiniciar a missão
    Button btnResetIpMission;

    // Objeto usado para salvar e consultar o progresso do jogador
    PlayerPrefs playerPrefs;

    // Quantidade inicial de tentativas disponíveis
    int attempts = 3;

    // Nível de detecção do sistema
    int detection = 0;

    // IP correto que o jogador precisa identificar
    String correctIp = "185.221.77.13";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Define o layout XML que será usado nesta tela
        setContentView(R.layout.activity_ip_mission);

        // Inicializa o gerenciador de preferências do jogador
        playerPrefs = new PlayerPrefs(this);

        // Liga as variáveis Java aos componentes visuais do XML
        txtIpStatus = findViewById(R.id.txtIpStatus);
        txtIpStats = findViewById(R.id.txtIpStats);
        txtIpLog = findViewById(R.id.txtIpLog);

        btnIp1 = findViewById(R.id.btnIp1);
        btnIp2 = findViewById(R.id.btnIp2);
        btnIp3 = findViewById(R.id.btnIp3);
        btnIp4 = findViewById(R.id.btnIp4);
        btnResetIpMission = findViewById(R.id.btnResetIpMission);

        // Se a missão 4 já foi concluída, mostra a tela como finalizada
        if (playerPrefs.isMission4Completed()) {
            showCompletedMission();
        }

        // Define o IP que será testado quando cada botão for clicado
        btnIp1.setOnClickListener(v -> checkIp("192.168.0.12"));
        btnIp2.setOnClickListener(v -> checkIp("10.0.0.45"));
        btnIp3.setOnClickListener(v -> checkIp("172.16.4.91"));
        btnIp4.setOnClickListener(v -> checkIp("185.221.77.13"));

        // Define a ação do botão de reiniciar missão
        btnResetIpMission.setOnClickListener(v -> resetMission());

        // Atualiza as estatísticas iniciais na tela
        updateStats();
    }

    // Verifica se o IP escolhido pelo jogador é o correto
    private void checkIp(String selectedIp) {

        // Impede que a missão seja jogada novamente após ser concluída
        if (playerPrefs.isMission4Completed()) {
            Toast.makeText(this, "Missão já concluída.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Toca som de clique ao selecionar um IP
        SoundManager.playSound(this, R.raw.cyber_click);

        // Se não houver mais tentativas ou a detecção chegou ao limite, bloqueia a ação
        if (attempts <= 0 || detection >= 100) {
            txtIpLog.append("\n> Sistema bloqueado. Reinicie a missão.");
            return;
        }

        // Mostra no log qual IP foi analisado
        txtIpLog.append("\n> IP analisado: " + selectedIp);

        // Se o IP escolhido for o correto, conclui a missão
        if (selectedIp.equals(correctIp)) {
            completeMission();
        } else {

            // Se o IP estiver errado, toca som de erro
            SoundManager.playSound(this, R.raw.cyber_error);

            // Diminui uma tentativa
            attempts--;

            // Aumenta o nível de detecção do sistema
            detection += 35;

            // Atualiza o status e registra mensagens no log
            txtIpStatus.setText("STATUS: IP INCORRETO");
            txtIpLog.append("\n> Endereço pertence a uma rede local.");
            txtIpLog.append("\n> Detecção aumentou.");

            // Atualiza as estatísticas na tela
            updateStats();

            // Se acabaram as tentativas ou a detecção chegou a 100%, a missão falha
            if (attempts <= 0 || detection >= 100) {
                gameOver();
            } else {
                Toast.makeText(this, "IP errado. Tentativas restantes: " + attempts, Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Conclui a missão quando o jogador escolhe o IP correto
    private void completeMission() {

        // Toca som de sucesso
        SoundManager.playSound(this, R.raw.cyber_success);

        // Atualiza o status da missão
        txtIpStatus.setText("STATUS: NÓ INVASOR IDENTIFICADO");

        // Adiciona mensagens finais no log
        txtIpLog.append("\n> IP suspeito confirmado: 185.221.77.13");
        txtIpLog.append("\n> Origem fora da rede local.");
        txtIpLog.append("\n> Tráfego externo isolado.");
        txtIpLog.append("\n> MISSÃO CONCLUÍDA.");

        // Calcula a recompensa com base no desempenho do jogador
        int xpReward = calculateXpReward();

        // Adiciona o XP ao perfil do jogador
        playerPrefs.addXp(xpReward);

        // Marca a missão 4 como concluída
        playerPrefs.setMission4Completed(true);

        // Salva o progresso na nuvem
        new CloudSaveManager(playerPrefs).saveProgress();

        // Mostra XP e rank obtidos
        txtIpLog.append("\n> XP recebido: +" + xpReward);
        txtIpLog.append("\n> Rank da missão: " + getRank(xpReward));
        txtIpLog.append("\n> Progresso sincronizado na nuvem.");

        // Desativa todos os botões da missão
        disableAll();

        // Exibe mensagem de conclusão
        Toast.makeText(this, "Missão 4 concluída! +" + xpReward + " XP", Toast.LENGTH_LONG).show();
    }

    // Calcula o XP recebido de acordo com tentativas restantes e nível de detecção
    private int calculateXpReward() {
        if (attempts == 3 && detection == 0) return 350;
        if (attempts == 2) return 280;
        return 180;
    }

    // Retorna o rank da missão com base no XP recebido
    private String getRank(int xpReward) {
        if (xpReward == 350) return "S+";
        if (xpReward == 280) return "A";
        return "B";
    }

    // Executa quando o jogador perde a missão
    private void gameOver() {

        // Atualiza o status para indicar falha
        txtIpStatus.setText("STATUS: MISSÃO FALHOU");

        // Registra no log o motivo da falha
        txtIpLog.append("\n> Muitas escolhas incorretas.");
        txtIpLog.append("\n> Sistema entrou em alerta.");
        txtIpLog.append("\n> Use REINICIAR MISSÃO para tentar novamente.");

        // Desativa apenas os botões de IP
        disableIpButtons();

        // Mostra aviso para o jogador
        Toast.makeText(this, "Missão falhou. Reinicie para tentar novamente.", Toast.LENGTH_LONG).show();
    }

    // Reinicia a missão caso ela ainda não tenha sido concluída
    private void resetMission() {

        // Impede reiniciar uma missão já concluída
        if (playerPrefs.isMission4Completed()) {
            Toast.makeText(this, "Missão já concluída.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Toca som ao reiniciar a missão
        SoundManager.playSound(this, R.raw.cyber_error);

        // Restaura os valores iniciais da missão
        attempts = 3;
        detection = 0;

        // Restaura o status inicial
        txtIpStatus.setText("STATUS: AGUARDANDO VARREDURA");

        // Restaura o texto inicial do log
        txtIpLog.setText("> Analise as conexões ativas no roteamento fictício.\n> Encontre o terminal intruso oculto fora da sub-rede local.");

        // Reativa os botões de IP
        enableIpButtons();

        // Atualiza as estatísticas
        updateStats();

        // Mostra mensagem de reinício
        Toast.makeText(this, "Missão reiniciada.", Toast.LENGTH_SHORT).show();
    }

    // Atualiza as estatísticas exibidas na tela
    private void updateStats() {
        txtIpStats.setText("Tentativas: " + attempts + " | Detecção: " + detection + "% | XP: até +350");
    }

    // Mostra a tela no estado de missão já concluída
    private void showCompletedMission() {

        // Atualiza os textos informando que a missão já foi finalizada
        txtIpStatus.setText("STATUS: MISSÃO JÁ CONCLUÍDA");
        txtIpStats.setText("Recompensa já recebida.");
        txtIpLog.setText("> Missão já finalizada.\n> Endereço externo isolado.\n> Nó 185.221.77.13 neutralizado.");

        // Desativa todos os controles da missão
        disableAll();
    }

    // Desativa apenas os botões de escolha de IP
    private void disableIpButtons() {
        btnIp1.setEnabled(false);
        btnIp2.setEnabled(false);
        btnIp3.setEnabled(false);
        btnIp4.setEnabled(false);

        // Deixa os botões visualmente apagados
        btnIp1.setAlpha(0.5f);
        btnIp2.setAlpha(0.5f);
        btnIp3.setAlpha(0.5f);
        btnIp4.setAlpha(0.5f);
    }

    // Reativa os botões de escolha de IP
    private void enableIpButtons() {
        btnIp1.setEnabled(true);
        btnIp2.setEnabled(true);
        btnIp3.setEnabled(true);
        btnIp4.setEnabled(true);

        // Restaura a aparência normal dos botões
        btnIp1.setAlpha(1f);
        btnIp2.setAlpha(1f);
        btnIp3.setAlpha(1f);
        btnIp4.setAlpha(1f);
    }

    // Desativa todos os controles da missão
    private void disableAll() {

        // Primeiro desativa os botões de IP
        disableIpButtons();

        // Depois desativa o botão de reiniciar missão
        btnResetIpMission.setEnabled(false);
        btnResetIpMission.setAlpha(0.5f);
    }
}