package com.darknetprotocol.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.darknetprotocol.R;
import com.darknetprotocol.SoundManager;
import com.darknetprotocol.utils.CloudSaveManager;
import com.darknetprotocol.utils.PlayerPrefs;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class MainActivity extends AppCompatActivity {

    /*
     * =========================================================
     * FIREBASE / GOOGLE LOGIN
     * =========================================================
     */

    private static final String WEB_CLIENT_ID =
            "279348768648-ahaecvncvmsm6cv7aqng95lvdnie5sld.apps.googleusercontent.com";

    private FirebaseAuth firebaseAuth;
    private GoogleSignInClient googleSignInClient;

    /*
     * =========================================================
     * COMPONENTES DA INTERFACE
     * =========================================================
     */

    private Button btnStart;
    private SignInButton btnGoogleLogin;

    private TextView txtStatus;
    private ProgressBar progressBar;

    /*
     * =========================================================
     * SISTEMA DO JOGO
     * =========================================================
     */

    private PlayerPrefs playerPrefs;

    /*
     * =========================================================
     * LOGIN RESULT
     * =========================================================
     */

    private ActivityResultLauncher<Intent> googleLoginLauncher;

    /*
     * =========================================================
     * CONTROLE
     * =========================================================
     */

    private boolean isStarting = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        initializeViews();
        initializeFirebase();
        setupGoogleLogin();
        setupButtons();
        checkCurrentUser();
    }

    /*
     * =========================================================
     * INICIALIZAÇÃO
     * =========================================================
     */

    private void initializeViews() {

        btnStart = findViewById(R.id.btnStart);

        btnGoogleLogin =
                findViewById(R.id.btnGoogleLogin);

        txtStatus =
                findViewById(R.id.txtStatus);

        progressBar =
                findViewById(R.id.progressBar);
    }

    private void initializeFirebase() {

        firebaseAuth =
                FirebaseAuth.getInstance();

        playerPrefs =
                new PlayerPrefs(this);
    }

    /*
     * =========================================================
     * VERIFICA LOGIN ATUAL
     * =========================================================
     */

    private void checkCurrentUser() {

        FirebaseUser currentUser =
                firebaseAuth.getCurrentUser();

        if (currentUser != null) {

            txtStatus.setText(
                    "LOGIN: " +
                            currentUser.getDisplayName()
            );

            loadCloudProgress();
        }
    }

    /*
     * =========================================================
     * BOTÕES
     * =========================================================
     */

    private void setupButtons() {

        btnStart.setOnClickListener(v ->
                startProtocol()
        );

        btnGoogleLogin.setOnClickListener(v -> {

            SoundManager.playSound(
                    this,
                    R.raw.cyber_click
            );

            Intent signInIntent =
                    googleSignInClient.getSignInIntent();

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

        GoogleSignInOptions googleSignInOptions =
                new GoogleSignInOptions.Builder(
                        GoogleSignInOptions.DEFAULT_SIGN_IN
                )
                        .requestIdToken(WEB_CLIENT_ID)
                        .requestEmail()
                        .build();

        googleSignInClient =
                GoogleSignIn.getClient(
                        this,
                        googleSignInOptions
                );

        googleLoginLauncher =
                registerForActivityResult(
                        new ActivityResultContracts.StartActivityForResult(),
                        result -> {

                            Intent data =
                                    result.getData();

                            Task<GoogleSignInAccount> task =
                                    GoogleSignIn
                                            .getSignedInAccountFromIntent(data);

                            try {

                                GoogleSignInAccount account =
                                        task.getResult(ApiException.class);

                                firebaseLoginWithGoogle(account);

                            } catch (ApiException e) {

                                SoundManager.playSound(
                                        this,
                                        R.raw.cyber_error
                                );

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

        AuthCredential credential =
                GoogleAuthProvider.getCredential(
                        account.getIdToken(),
                        null
                );

        firebaseAuth
                .signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {

                    if (task.isSuccessful()) {

                        FirebaseUser user =
                                firebaseAuth.getCurrentUser();

                        SoundManager.playSound(
                                this,
                                R.raw.cyber_success
                        );

                        if (user != null) {

                            txtStatus.setText(
                                    "LOGIN: " +
                                            user.getDisplayName()
                            );

                        } else {

                            txtStatus.setText(
                                    "LOGIN GOOGLE CONCLUÍDO"
                            );
                        }

                        Toast.makeText(
                                this,
                                "Login realizado com sucesso.",
                                Toast.LENGTH_SHORT
                        ).show();

                        loadCloudProgress();

                    } else {

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

        txtStatus.setText(
                "SINCRONIZANDO PROGRESSO..."
        );

        new CloudSaveManager(playerPrefs)
                .loadProgress(() -> {

                    txtStatus.setText(
                            "PROGRESSO SINCRONIZADO"
                    );

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

        if (isStarting) {
            return;
        }

        isStarting = true;

        SoundManager.playSound(
                this,
                R.raw.cyber_click
        );

        btnStart.setEnabled(false);
        btnGoogleLogin.setEnabled(false);

        progressBar.setVisibility(View.VISIBLE);
        progressBar.setAlpha(0f);

        Handler handler = new Handler();

        /*
         * ETAPA 1
         */

        txtStatus.setText("CONECTANDO...");
        animateProgress(25);

        /*
         * ETAPA 2
         */

        handler.postDelayed(() -> {

            SoundManager.playSound(
                    this,
                    R.raw.cyber_click
            );

            txtStatus.setText(
                    "ACESSANDO SERVIDOR..."
            );

            animateProgress(50);

        }, 1000);

        /*
         * ETAPA 3
         */

        handler.postDelayed(() -> {

            SoundManager.playSound(
                    this,
                    R.raw.cyber_click
            );

            txtStatus.setText(
                    "VALIDANDO CRIPTOGRAFIA..."
            );

            animateProgress(75);

        }, 2000);

        /*
         * ETAPA 4
         */

        handler.postDelayed(() -> {

            SoundManager.playSound(
                    this,
                    R.raw.cyber_success
            );

            txtStatus.setText(
                    "ACESSO LIBERADO"
            );

            animateProgress(100);

        }, 3000);

        /*
         * ABRE TELA DE MISSÕES
         */

        handler.postDelayed(() -> {

            Intent intent =
                    new Intent(
                            MainActivity.this,
                            MissionsActivity.class
                    );

            startActivity(intent);

            overridePendingTransition(
                    R.anim.slide_in_right,
                    R.anim.slide_out_left
            );

            finish();

        }, 4000);
    }

    /*
     * =========================================================
     * ANIMAÇÃO DA BARRA
     * =========================================================
     */

    private void animateProgress(int progress) {

        progressBar.animate()
                .alpha(1f)
                .setDuration(250)
                .start();

        progressBar.setProgress(
                progress,
                true
        );
    }
}