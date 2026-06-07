package com.darknetprotocol.activities;

// Importa classes básicas do Android usadas na tela
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

// Importa a classe base para criar uma Activity com suporte ao AppCompat
import androidx.appcompat.app.AppCompatActivity;

// Importa os recursos do projeto, como layouts, ids e sons
import com.darknetprotocol.R;

// Importa classes auxiliares do projeto
import com.darknetprotocol.utils.SoundManager;
import com.darknetprotocol.utils.CloudSaveManager;
import com.darknetprotocol.utils.PlayerPrefs;

// Activity responsável pela missão de descriptografia
public class DecryptMissionActivity extends AppCompatActivity {

    // TextViews usados para mostrar status, estatísticas e logs da missão
    TextView txtDecryptStatus;
    TextView txtDecryptStats;
    TextView txtDecryptLog;

    // Campo onde o jogador digita a resposta
    EditText edtDecryptAnswer;

    // Botões da tela
    Button btnCheckDecrypt;
    Button btnDecryptHint;
    Button btnResetDecrypt;

    // Objeto usado para salvar e consultar dados do jogador
    PlayerPrefs playerPrefs;

    // Quantidade inicial de tentativas disponíveis
    int attempts = 3;

    // Contador de dicas usadas pelo jogador
    int hintsUsed = 0;

    // Resposta correta da missão
    String correctAnswer = "ORION";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Define qual layout XML será usado nesta tela
        setContentView(R.layout.activity_decrypt_mission);

        // Inicializa o gerenciador de preferências do jogador
        playerPrefs = new PlayerPrefs(this);

        // Liga as variáveis Java aos componentes visuais do XML
        txtDecryptStatus = findViewById(R.id.txtDecryptStatus);
        txtDecryptStats = findViewById(R.id.txtDecryptStats);
        txtDecryptLog = findViewById(R.id.txtDecryptLog);

        edtDecryptAnswer = findViewById(R.id.edtDecryptAnswer);

        btnCheckDecrypt = findViewById(R.id.btnCheckDecrypt);
        btnDecryptHint = findViewById(R.id.btnDecryptHint);
        btnResetDecrypt = findViewById(R.id.btnResetDecrypt);

        // Se a missão já foi concluída antes, mostra a tela como finalizada
        if (playerPrefs.isMission3Completed()) {
            showCompletedMission();
        }

        // Define o que acontece quando o jogador clica em cada botão
        btnCheckDecrypt.setOnClickListener(v -> checkAnswer());
        btnDecryptHint.setOnClickListener(v -> showHint());
        btnResetDecrypt.setOnClickListener(v -> resetMission());

        // Atualiza a área de estatísticas da missão
        updateStats();
    }

    // Verifica se a resposta digitada pelo jogador está correta
    private void checkAnswer() {

        // Impede que o jogador refaça a missão depois de concluída
        if (playerPrefs.isMission3Completed()) {
            Toast.makeText(this, "Missão já concluída.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Toca som de clique
        SoundManager.playSound(this, R.raw.cyber_click);

        // Pega o texto digitado, remove espaços e converte para maiúsculo
        String answer = edtDecryptAnswer.getText().toString().trim().toUpperCase();

        // Verifica se o jogador deixou o campo vazio
        if (answer.isEmpty()) {
            Toast.makeText(this, "Digite a resposta primeiro, operador.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Se a resposta estiver correta, conclui a missão
        if (answer.equals(correctAnswer)) {
            completeMission();
        } else {

            // Se estiver errada, toca som de erro
            SoundManager.playSound(this, R.raw.cyber_error);

            // Diminui uma tentativa
            attempts--;

            // Atualiza o status e adiciona mensagens no log
            txtDecryptStatus.setText("STATUS: FALHA NA DESCRIPTOGRAFIA");
            txtDecryptLog.append("\n> Resposta inválida: " + answer);
            txtDecryptLog.append("\n> Chave rejeitada.");

            // Limpa o campo de resposta
            edtDecryptAnswer.setText("");

            // Atualiza as estatísticas na tela
            updateStats();

            // Se acabaram as tentativas, chama o game over
            if (attempts <= 0) {
                gameOver();
            } else {
                Toast.makeText(this, "Errado. Tentativas restantes: " + attempts, Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Mostra dicas para ajudar o jogador
    private void showHint() {

        // Impede o uso de dicas após a missão já estar concluída
        if (playerPrefs.isMission3Completed()) {
            Toast.makeText(this, "Missão já concluída.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Toca som de clique
        SoundManager.playSound(this, R.raw.cyber_click);

        // Aumenta o contador de dicas usadas
        hintsUsed++;

        // Mostra uma dica diferente dependendo da quantidade já usada
        if (hintsUsed == 1) {
            txtDecryptLog.append("\n> Dica 2: cada letra foi deslocada 3 posições para frente.");
        } else if (hintsUsed == 2) {
            txtDecryptLog.append("\n> Dica 3: R vira O.");
        } else if (hintsUsed == 3) {
            txtDecryptLog.append("\n> Dica final: O R I O N.");
        } else {
            txtDecryptLog.append("\n> Sem mais dicas. O sistema já abriu a porta e te entregou café.");
        }

        // Atualiza as estatísticas depois de usar uma dica
        updateStats();
    }

    // Finaliza a missão quando o jogador acerta a resposta
    private void completeMission() {

        // Toca som de sucesso
        SoundManager.playSound(this, R.raw.cyber_success);

        // Atualiza o status e adiciona mensagens finais no log
        txtDecryptStatus.setText("STATUS: ARQUIVO DESCRIPTOGRAFADO");
        txtDecryptLog.append("\n> Palavra correta: ORION");
        txtDecryptLog.append("\n> Arquivo ghost_file.enc aberto.");
        txtDecryptLog.append("\n> Dados do Projeto Orion recuperados.");
        txtDecryptLog.append("\n> MISSÃO CONCLUÍDA.");

        // Calcula o XP com base nas tentativas e dicas usadas
        int xpReward = calculateXpReward();

        // Adiciona XP ao jogador
        playerPrefs.addXp(xpReward);

        // Marca a missão 3 como concluída
        playerPrefs.setMission3Completed(true);

        // Salva o progresso na nuvem
        new CloudSaveManager(playerPrefs).saveProgress();

        // Mostra a recompensa e o rank no log
        txtDecryptLog.append("\n> XP recebido: +" + xpReward);
        txtDecryptLog.append("\n> Rank da missão: " + getRank(xpReward));
        txtDecryptLog.append("\n> Progresso sincronizado na nuvem.");

        // Desativa os campos e botões da missão
        disableAll();

        // Mostra mensagem de conclusão
        Toast.makeText(this, "Missão 3 concluída! +" + xpReward + " XP", Toast.LENGTH_LONG).show();
    }

    // Calcula a recompensa de XP de acordo com o desempenho do jogador
    private int calculateXpReward() {
        if (attempts == 3 && hintsUsed == 0) return 300;
        if (attempts >= 2 && hintsUsed <= 1) return 250;
        if (attempts >= 1) return 180;
        return 100;
    }

    // Retorna o rank da missão com base no XP recebido
    private String getRank(int xpReward) {
        if (xpReward == 300) return "S+";
        if (xpReward == 250) return "A";
        if (xpReward == 180) return "B";
        return "C";
    }

    // Executa quando o jogador perde todas as tentativas
    private void gameOver() {

        // Atualiza o status e informa que a missão falhou
        txtDecryptStatus.setText("STATUS: MISSÃO FALHOU");
        txtDecryptLog.append("\n> Muitas tentativas inválidas.");
        txtDecryptLog.append("\n> Arquivo bloqueado temporariamente.");
        txtDecryptLog.append("\n> Use REINICIAR MISSÃO para tentar novamente.");

        // Desativa os botões de resposta e dica
        btnCheckDecrypt.setEnabled(false);
        btnDecryptHint.setEnabled(false);

        // Deixa os botões visualmente apagados
        btnCheckDecrypt.setAlpha(0.5f);
        btnDecryptHint.setAlpha(0.5f);

        // Mostra aviso para o jogador
        Toast.makeText(this, "Missão falhou. Reinicie para tentar novamente.", Toast.LENGTH_LONG).show();
    }

    // Reinicia a missão caso ela ainda não tenha sido concluída
    private void resetMission() {

        // Não permite reiniciar uma missão já concluída
        if (playerPrefs.isMission3Completed()) {
            Toast.makeText(this, "Missão já concluída.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Toca som de erro/reinício
        SoundManager.playSound(this, R.raw.cyber_error);

        // Restaura tentativas e dicas para o valor inicial
        attempts = 3;
        hintsUsed = 0;

        // Limpa o campo de resposta
        edtDecryptAnswer.setText("");

        // Restaura o status e o log inicial da missão
        txtDecryptStatus.setText("STATUS: ARQUIVO CRIPTOGRAFADO");
        txtDecryptLog.setText("> Arquivo encontrado: ghost_file.enc\n> Conteúdo criptografado: RULRQ\n> Dica: Cifra de César -3");

        // Reativa os botões
        btnCheckDecrypt.setEnabled(true);
        btnDecryptHint.setEnabled(true);

        // Volta a aparência normal dos botões
        btnCheckDecrypt.setAlpha(1f);
        btnDecryptHint.setAlpha(1f);

        // Atualiza as estatísticas
        updateStats();

        // Mostra mensagem de reinício
        Toast.makeText(this, "Missão reiniciada.", Toast.LENGTH_SHORT).show();
    }

    // Atualiza o texto que mostra tentativas, dicas usadas e XP máximo
    private void updateStats() {
        txtDecryptStats.setText("Tentativas: " + attempts + " | Dicas usadas: " + hintsUsed + " | XP: até +300");
    }

    // Mostra a tela no estado de missão já concluída
    private void showCompletedMission() {

        // Atualiza os textos informando que a missão já foi finalizada
        txtDecryptStatus.setText("STATUS: MISSÃO JÁ CONCLUÍDA");
        txtDecryptStats.setText("Recompensa já recebida.");
        txtDecryptLog.setText("> Missão já finalizada.\n> Arquivo ghost_file.enc descriptografado.\n> Palavra encontrada: ORION.\n> XP já foi adicionado ao perfil.");

        // Preenche o campo com a resposta correta
        edtDecryptAnswer.setText("ORION");

        // Desativa os controles da tela
        disableAll();
    }

    // Desativa todos os campos e botões da missão
    private void disableAll() {
        edtDecryptAnswer.setEnabled(false);
        btnCheckDecrypt.setEnabled(false);
        btnDecryptHint.setEnabled(false);
        btnResetDecrypt.setEnabled(false);

        // Deixa os botões com aparência apagada
        btnCheckDecrypt.setAlpha(0.5f);
        btnDecryptHint.setAlpha(0.5f);
        btnResetDecrypt.setAlpha(0.5f);
    }
}