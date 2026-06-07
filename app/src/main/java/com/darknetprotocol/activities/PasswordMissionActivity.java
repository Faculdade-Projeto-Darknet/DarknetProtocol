package com.darknetprotocol.activities;

// Importa Bundle, utilizado no ciclo de vida da Activity
import android.os.Bundle;

// Importa componentes visuais utilizados na interface
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

// Classe base para Activities compatíveis com AppCompat
import androidx.appcompat.app.AppCompatActivity;

// Importa recursos do projeto (layouts, ids, sons, etc.)
import com.darknetprotocol.R;

// Classe responsável por reproduzir efeitos sonoros
import com.darknetprotocol.utils.SoundManager;

// Classe responsável por sincronizar progresso na nuvem
import com.darknetprotocol.utils.CloudSaveManager;

// Classe responsável por armazenar os dados do jogador
import com.darknetprotocol.utils.PlayerPrefs;

// Activity responsável pela Missão 2 (Quebra de Senha)
public class PasswordMissionActivity extends AppCompatActivity {

    // Exibe o status atual da missão
    TextView txtStatusPassword;

    // Exibe estatísticas da missão
    TextView txtStatsPassword;

    // Exibe o histórico de eventos da missão
    TextView txtLogPassword;

    // Campo onde o jogador digita a senha
    EditText edtPasswordAnswer;

    // Botão para verificar a senha
    Button btnCheckPassword;

    // Botão para solicitar dicas
    Button btnPasswordHint;

    // Botão para reiniciar a missão
    Button btnResetPasswordMission;

    // Gerenciador de progresso do jogador
    PlayerPrefs playerPrefs;

    // Senha correta da missão
    String correctPassword = "ORION";

    // Quantidade de tentativas disponíveis
    int attempts = 4;

    // Quantidade de dicas utilizadas
    int hintsUsed = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Define o layout da tela
        setContentView(R.layout.activity_password_mission);

        // Inicializa o sistema de preferências
        playerPrefs = new PlayerPrefs(this);

        // Liga os componentes do XML às variáveis Java
        txtStatusPassword = findViewById(R.id.txtStatusPassword);
        txtStatsPassword = findViewById(R.id.txtStatsPassword);
        txtLogPassword = findViewById(R.id.txtLogPassword);

        edtPasswordAnswer = findViewById(R.id.edtPasswordAnswer);

        btnCheckPassword = findViewById(R.id.btnCheckPassword);
        btnPasswordHint = findViewById(R.id.btnPasswordHint);
        btnResetPasswordMission = findViewById(R.id.btnResetPasswordMission);

        // Verifica se a missão já foi concluída anteriormente
        if (playerPrefs.isMission2Completed()) {
            showCompletedMission();
        }

        // Configura ações dos botões
        btnCheckPassword.setOnClickListener(v -> checkPassword());
        btnPasswordHint.setOnClickListener(v -> showHint());
        btnResetPasswordMission.setOnClickListener(v -> resetMission());

        // Atualiza estatísticas iniciais
        updateStats();
    }

    // Verifica se a senha digitada está correta
    private void checkPassword() {

        // Impede jogar novamente após concluir a missão
        if (playerPrefs.isMission2Completed()) {
            Toast.makeText(this, "Missão já concluída.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Obtém a senha digitada pelo usuário
        String answer = edtPasswordAnswer
                .getText()
                .toString()
                .trim()
                .toUpperCase();

        // Verifica se o campo está vazio
        if (answer.isEmpty()) {
            Toast.makeText(this, "Digite a senha primeiro.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Reproduz som de clique
        SoundManager.playSound(this, R.raw.cyber_click);

        // Verifica se a senha está correta
        if (answer.equals(correctPassword)) {

            completeMission();

        } else {

            // Reproduz som de erro
            SoundManager.playSound(this, R.raw.cyber_error);

            // Reduz uma tentativa
            attempts--;

            // Atualiza informações da interface
            txtStatusPassword.setText("STATUS: SENHA INCORRETA");
            txtLogPassword.append("\n> Tentativa falhou: " + answer);
            txtLogPassword.append("\n> Assinatura inválida.");

            // Limpa o campo de senha
            edtPasswordAnswer.setText("");

            updateStats();

            // Verifica se o jogador perdeu
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

    // Exibe dicas para ajudar o jogador
    private void showHint() {

        if (playerPrefs.isMission2Completed()) {
            Toast.makeText(this, "Missão já concluída.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Reproduz som de clique
        SoundManager.playSound(this, R.raw.cyber_click);

        // Incrementa contador de dicas
        hintsUsed++;

        // Exibe uma dica diferente conforme a quantidade utilizada
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

    // Conclui a missão com sucesso
    private void completeMission() {

        // Reproduz som de sucesso
        SoundManager.playSound(this, R.raw.cyber_success);

        // Atualiza status e log
        txtStatusPassword.setText("STATUS: ACESSO LIBERADO");
        txtLogPassword.append("\n> Senha correta: ORION");
        txtLogPassword.append("\n> Cofre digital desbloqueado.");
        txtLogPassword.append("\n> MISSÃO CONCLUÍDA.");

        // Calcula recompensa
        int xpReward = calculateXpReward();

        // Adiciona XP ao jogador
        playerPrefs.addXp(xpReward);

        // Marca missão como concluída
        playerPrefs.setMission2Completed(true);

        // Salva progresso na nuvem
        new CloudSaveManager(playerPrefs).saveProgress();

        // Exibe recompensa
        txtLogPassword.append("\n> XP recebido: +" + xpReward);
        txtLogPassword.append("\n> Rank da missão: " + getRank(xpReward));
        txtLogPassword.append("\n> Progresso sincronizado na nuvem.");

        // Desativa controles
        disableAll();

        Toast.makeText(
                this,
                "Missão 2 concluída! +" + xpReward + " XP",
                Toast.LENGTH_LONG
        ).show();
    }

    // Calcula XP baseado no desempenho do jogador
    private int calculateXpReward() {

        if (attempts == 4 && hintsUsed == 0) return 250;

        if (attempts >= 3 && hintsUsed <= 1) return 200;

        if (attempts >= 2) return 150;

        return 100;
    }

    // Define rank conforme XP recebido
    private String getRank(int xpReward) {

        if (xpReward == 250) return "S+";

        if (xpReward == 200) return "A";

        if (xpReward == 150) return "B";

        return "C";
    }

    // Executa ações quando a missão falha
    private void gameOver() {

        txtStatusPassword.setText("STATUS: MISSÃO FALHOU");

        txtLogPassword.append("\n> Muitas tentativas incorretas.");
        txtLogPassword.append("\n> Sistema bloqueado temporariamente.");
        txtLogPassword.append("\n> Use REINICIAR MISSÃO para tentar de novo.");

        // Desativa controles da missão
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

    // Reinicia a missão
    private void resetMission() {

        if (playerPrefs.isMission2Completed()) {
            Toast.makeText(this, "Missão já concluída.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Reproduz som de reinicialização
        SoundManager.playSound(this, R.raw.cyber_error);

        // Restaura valores iniciais
        attempts = 4;
        hintsUsed = 0;

        edtPasswordAnswer.setText("");
        edtPasswordAnswer.setEnabled(true);

        txtStatusPassword.setText("STATUS: AGUARDANDO TENTATIVA");

        txtLogPassword.setText(
                "> Arquivo criptografado encontrado.\n" +
                        "> Dica 1: nome do projeto mencionado na missão 1."
        );

        // Reativa os botões
        btnCheckPassword.setEnabled(true);
        btnPasswordHint.setEnabled(true);
        btnResetPasswordMission.setEnabled(true);

        btnCheckPassword.setAlpha(1f);
        btnPasswordHint.setAlpha(1f);
        btnResetPasswordMission.setAlpha(1f);

        updateStats();

        Toast.makeText(this, "Missão reiniciada.", Toast.LENGTH_SHORT).show();
    }

    // Atualiza as estatísticas exibidas na tela
    private void updateStats() {

        txtStatsPassword.setText(
                "Tentativas: " + attempts +
                        " | Dicas usadas: " + hintsUsed +
                        " | XP: até +250"
        );
    }

    // Exibe a tela quando a missão já foi concluída
    private void showCompletedMission() {

        txtStatusPassword.setText("STATUS: MISSÃO JÁ CONCLUÍDA");

        txtStatsPassword.setText("Recompensa já recebida.");

        txtLogPassword.setText(
                "> Missão já finalizada.\n" +
                        "> Senha ORION validada.\n" +
                        "> XP já foi adicionado ao perfil."
        );

        edtPasswordAnswer.setText("ORION");

        disableAll();
    }

    // Desativa todos os controles da tela
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