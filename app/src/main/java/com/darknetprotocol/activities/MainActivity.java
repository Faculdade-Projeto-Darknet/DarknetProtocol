package com.darknetprotocol.activities;

// Importa a classe Intent, usada para abrir outras telas do app
import android.content.Intent;

// Importa Bundle, usado no ciclo de vida da Activity
import android.os.Bundle;

// Importa Handler, usado para executar ações com atraso
import android.os.Handler;

// Importa View, usado para controlar visibilidade de componentes
import android.view.View;

// Importa componentes visuais da interface
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

// Importa recursos modernos para receber resultado de outra tela
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

// Classe base para criar uma tela no Android
import androidx.appcompat.app.AppCompatActivity;

// Importa recursos do projeto, como layout, sons e animações
import com.darknetprotocol.R;

// Importa classes auxiliares do projeto
import com.darknetprotocol.utils.SoundManager;
import com.darknetprotocol.utils.CloudSaveManager;
import com.darknetprotocol.utils.PlayerPrefs;

// Imports usados no login com Google
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

// Imports usados na autenticação com Firebase
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

// Activity principal do aplicativo
public class MainActivity extends AppCompatActivity {

    /*
     * =========================================================
     * FIREBASE / GOOGLE LOGIN
     * =========================================================
     */

    // ID do cliente web criado no Firebase Console
    // Ele é necessário para autenticar o login Google com Firebase
    private static final String WEB_CLIENT_ID =
            "279348768648-ahaecvncvmsm6cv7aqng95lvdnie5sld.apps.googleusercontent.com";

    // Objeto responsável pelo login/autenticação no Firebase
    private FirebaseAuth firebaseAuth;

    // Cliente responsável por iniciar o login com Google
    private GoogleSignInClient googleSignInClient;

    /*
     * =========================================================
     * COMPONENTES DA INTERFACE
     * =========================================================
     */

    // Botão que inicia o jogo
    private Button btnStart;

    // Botão oficial de login com Google
    private SignInButton btnGoogleLogin;

    // Texto usado para mostrar o status atual na tela
    private TextView txtStatus;

    // Barra de progresso exibida durante o carregamento inicial
    private ProgressBar progressBar;

    /*
     * =========================================================
     * SISTEMA DO JOGO
     * =========================================================
     */

    // Classe que controla dados salvos localmente do jogador
    private PlayerPrefs playerPrefs;

    /*
     * =========================================================
     * LOGIN RESULT
     * =========================================================
     */

    // Lançador usado para abrir a tela de login Google
    // e receber o resultado depois
    private ActivityResultLauncher<Intent> googleLoginLauncher;

    /*
     * =========================================================
     * CONTROLE
     * =========================================================
     */

    // Impede que o botão iniciar seja clicado várias vezes seguidas
    private boolean isStarting = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Define o layout XML desta tela
        setContentView(R.layout.activity_main);

        // Inicializa os componentes visuais
        initializeViews();

        // Inicializa Firebase e preferências do jogador
        initializeFirebase();

        // Configura o login com Google
        setupGoogleLogin();

        // Configura os cliques dos botões
        setupButtons();

        // Verifica se já existe algum usuário logado
        checkCurrentUser();
    }

    /*
     * =========================================================
     * INICIALIZAÇÃO
     * =========================================================
     */

    private void initializeViews() {

        // Liga o botão iniciar do XML à variável Java
        btnStart = findViewById(R.id.btnStart);

        // Liga o botão de login Google do XML à variável Java
        btnGoogleLogin =
                findViewById(R.id.btnGoogleLogin);

        // Liga o texto de status do XML à variável Java
        txtStatus =
                findViewById(R.id.txtStatus);

        // Liga a barra de progresso do XML à variável Java
        progressBar =
                findViewById(R.id.progressBar);
    }

    private void initializeFirebase() {

        // Obtém a instância principal do Firebase Authentication
        firebaseAuth =
                FirebaseAuth.getInstance();

        // Inicializa o sistema de preferências/salvamento local
        playerPrefs =
                new PlayerPrefs(this);
    }

    /*
     * =========================================================
     * VERIFICA LOGIN ATUAL
     * =========================================================
     */

    private void checkCurrentUser() {

        // Verifica se já existe um usuário logado no Firebase
        FirebaseUser currentUser =
                firebaseAuth.getCurrentUser();

        // Se existir usuário logado, mostra o nome dele na tela
        if (currentUser != null) {

            txtStatus.setText(
                    "LOGIN: " +
                            currentUser.getDisplayName()
            );

            // Depois carrega o progresso salvo na nuvem
            loadCloudProgress();
        }
    }

    /*
     * =========================================================
     * BOTÕES
     * =========================================================
     */

    private void setupButtons() {

        // Quando clicar em iniciar, chama o método que começa o protocolo
        btnStart.setOnClickListener(v ->
                startProtocol()
        );

        // Quando clicar no botão Google, começa o processo de login
        btnGoogleLogin.setOnClickListener(v -> {

            // Toca som de clique
            SoundManager.playSound(
                    this,
                    R.raw.cyber_click
            );

            // Cria a intenção de abrir a tela de login Google
            Intent signInIntent =
                    googleSignInClient.getSignInIntent();

            // Abre a tela de login Google e espera o resultado
            googleLoginLauncher.launch(
                    signInIntent
            );
        });
    }

    /*
     * =========================================================
     * CONFIGURA LOGIN GOOGLE
     * =========================================================
     */

    private void setupGoogleLogin() {

        // Define as opções do login Google
        GoogleSignInOptions googleSignInOptions =
                new GoogleSignInOptions.Builder(
                        GoogleSignInOptions.DEFAULT_SIGN_IN
                )
                        // Solicita o token necessário para autenticar no Firebase
                        .requestIdToken(WEB_CLIENT_ID)

                        // Solicita o email da conta Google
                        .requestEmail()
                        .build();

        // Cria o cliente de login Google com as opções definidas
        googleSignInClient =
                GoogleSignIn.getClient(
                        this,
                        googleSignInOptions
                );

        // Registra o retorno da tela de login Google
        googleLoginLauncher =
                registerForActivityResult(
                        new ActivityResultContracts.StartActivityForResult(),
                        result -> {

                            // Pega os dados retornados pela tela de login
                            Intent data =
                                    result.getData();

                            // Tenta recuperar a conta Google escolhida pelo usuário
                            Task<GoogleSignInAccount> task =
                                    GoogleSignIn
                                            .getSignedInAccountFromIntent(data);

                            try {

                                // Obtém a conta Google autenticada
                                GoogleSignInAccount account =
                                        task.getResult(ApiException.class);

                                // Usa essa conta para autenticar no Firebase
                                firebaseLoginWithGoogle(account);

                            } catch (ApiException e) {

                                // Se o login falhar, toca som de erro
                                SoundManager.playSound(
                                        this,
                                        R.raw.cyber_error
                                );

                                // Mostra mensagem de falha
                                Toast.makeText(
                                        this,
                                        "Falha no login Google.",
                                        Toast.LENGTH_SHORT
                                ).show();
                            }
                        }
                );
    }

    /*
     * =========================================================
     * LOGIN FIREBASE
     * =========================================================
     */

    private void firebaseLoginWithGoogle(
            GoogleSignInAccount account
    ) {

        // Cria uma credencial do Firebase usando o token da conta Google
        AuthCredential credential =
                GoogleAuthProvider.getCredential(
                        account.getIdToken(),
                        null
                );

        // Faz login no Firebase usando a credencial criada
        firebaseAuth
                .signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {

                    // Verifica se o login foi realizado com sucesso
                    if (task.isSuccessful()) {

                        // Recupera o usuário logado
                        FirebaseUser user =
                                firebaseAuth.getCurrentUser();

                        // Toca som de sucesso
                        SoundManager.playSound(
                                this,
                                R.raw.cyber_success
                        );

                        // Se o usuário existe, mostra o nome dele
                        if (user != null) {

                            txtStatus.setText(
                                    "LOGIN: " +
                                            user.getDisplayName()
                            );

                        } else {

                            // Caso não consiga pegar o nome, mostra mensagem genérica
                            txtStatus.setText(
                                    "LOGIN GOOGLE CONCLUÍDO"
                            );
                        }

                        // Mostra mensagem confirmando login
                        Toast.makeText(
                                this,
                                "Login realizado com sucesso.",
                                Toast.LENGTH_SHORT
                        ).show();

                        // Após logar, carrega o progresso salvo na nuvem
                        loadCloudProgress();

                    } else {

                        // Caso o Firebase não consiga autenticar
                        SoundManager.playSound(
                                this,
                                R.raw.cyber_error
                        );

                        Toast.makeText(
                                this,
                                "Erro ao autenticar com Firebase.",
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                });
    }

    /*
     * =========================================================
     * CARREGA PROGRESSO DO FIREBASE
     * =========================================================
     */

    private void loadCloudProgress() {

        // Mostra que o app está buscando o progresso online
        txtStatus.setText(
                "SINCRONIZANDO PROGRESSO..."
        );

        // Carrega o progresso da nuvem usando o CloudSaveManager
        new CloudSaveManager(playerPrefs)
                .loadProgress(() -> {

                    // Quando terminar de carregar, atualiza o status
                    txtStatus.setText(
                            "PROGRESSO SINCRONIZADO"
                    );

                    // Mostra confirmação para o jogador
                    Toast.makeText(
                            this,
                            "Progresso carregado.",
                            Toast.LENGTH_SHORT
                    ).show();
                });
    }

    /*
     * =========================================================
     * INICIA JOGO
     * =========================================================
     */

    private void startProtocol() {

        // Se o protocolo já está iniciando, não faz nada
        // Isso evita vários cliques abrindo várias telas
        if (isStarting) {
            return;
        }

        // Marca que o protocolo começou
        isStarting = true;

        // Toca som de clique
        SoundManager.playSound(
                this,
                R.raw.cyber_click
        );

        // Desativa os botões durante o carregamento
        btnStart.setEnabled(false);
        btnGoogleLogin.setEnabled(false);

        // Mostra a barra de progresso
        progressBar.setVisibility(View.VISIBLE);

        // Deixa a barra invisível no começo para aparecer com animação
        progressBar.setAlpha(0f);

        // Handler permite executar comandos depois de alguns segundos
        Handler handler = new Handler();

        /*
         * ETAPA 1
         */

        // Primeira mensagem do carregamento
        txtStatus.setText("CONECTANDO...");

        // Atualiza barra para 25%
        animateProgress(25);

        /*
         * ETAPA 2
         */

        // Executa esta parte depois de 1 segundo
        handler.postDelayed(() -> {

            // Toca som de clique
            SoundManager.playSound(
                    this,
                    R.raw.cyber_click
            );

            // Atualiza mensagem da tela
            txtStatus.setText(
                    "ACESSANDO SERVIDOR..."
            );

            // Atualiza barra para 50%
            animateProgress(50);

        }, 1000);

        /*
         * ETAPA 3
         */

        // Executa esta parte depois de 2 segundos
        handler.postDelayed(() -> {

            // Toca som de clique
            SoundManager.playSound(
                    this,
                    R.raw.cyber_click
            );

            // Atualiza mensagem da tela
            txtStatus.setText(
                    "VALIDANDO CRIPTOGRAFIA..."
            );

            // Atualiza barra para 75%
            animateProgress(75);

        }, 2000);

        /*
         * ETAPA 4
         */

        // Executa esta parte depois de 3 segundos
        handler.postDelayed(() -> {

            // Toca som de sucesso
            SoundManager.playSound(
                    this,
                    R.raw.cyber_success
            );

            // Mostra mensagem final do carregamento
            txtStatus.setText(
                    "ACESSO LIBERADO"
            );

            // Atualiza barra para 100%
            animateProgress(100);

        }, 3000);

        /*
         * ABRE TELA DE MISSÕES
         */

        // Executa depois de 4 segundos
        handler.postDelayed(() -> {

            // Cria intenção para abrir a tela de missões
            Intent intent =
                    new Intent(
                            MainActivity.this,
                            MissionsActivity.class
                    );

            // Abre a tela de missões
            startActivity(intent);

            // Aplica animação de entrada e saída entre telas
            overridePendingTransition(
                    R.anim.slide_in_right,
                    R.anim.slide_out_left
            );

            // Fecha a MainActivity para o usuário não voltar para ela pelo botão voltar
            finish();

        }, 4000);
    }

    /*
     * =========================================================
     * ANIMAÇÃO DA BARRA
     * =========================================================
     */

    private void animateProgress(int progress) {

        // Faz a barra aparecer suavemente
        progressBar.animate()
                .alpha(1f)
                .setDuration(250)
                .start();

        // Atualiza o valor da barra com animação
        progressBar.setProgress(
                progress,
                true
        );
    }
}