package com.darknetprotocol.activities;

// Importa classes utilizadas para diálogos, navegação e armazenamento local
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

// Importa componentes visuais da interface
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

// Classe base para Activities compatíveis com AppCompat
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.SwitchCompat;

// Recursos do projeto
import com.darknetprotocol.R;

// Classes utilitárias do projeto
import com.darknetprotocol.utils.CloudSaveManager;
import com.darknetprotocol.utils.PlayerPrefs;
import com.darknetprotocol.utils.SoundManager;

// Classes utilizadas para autenticação Google e Firebase
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;

// Activity responsável pelas configurações do aplicativo
public class SettingsActivity extends AppCompatActivity {

    // Nome do arquivo de preferências utilizado para salvar configurações
    private static final String SETTINGS_PREFS = "settings_data";

    // Botão de voltar
    private TextView btnBack;

    // Controle de volume dos efeitos sonoros
    private SeekBar seekSound;

    // Interruptores de configurações
    private SwitchCompat switchMusic;
    private SwitchCompat switchVibration;
    private SwitchCompat switchNotifications;

    // Botões de tema, sincronização e logout
    private AppCompatButton btnThemeDark;
    private AppCompatButton btnThemeMatrix;
    private AppCompatButton btnSyncNow;
    private AppCompatButton btnLogout;

    // Armazena as configurações do usuário
    private SharedPreferences settingsPrefs;

    // Gerencia os dados do jogador
    private PlayerPrefs playerPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Define o layout da tela
        setContentView(R.layout.activity_settings);

        // Inicializa o armazenamento das configurações
        settingsPrefs = getSharedPreferences(
                SETTINGS_PREFS,
                MODE_PRIVATE
        );

        // Inicializa o sistema de progresso do jogador
        playerPrefs = new PlayerPrefs(this);

        // Inicializa componentes visuais
        initializeViews();

        // Carrega configurações salvas
        loadSettings();

        // Configura eventos dos componentes
        setupClicks();
    }

    // Inicializa todos os componentes da interface
    private void initializeViews() {

        btnBack = findViewById(R.id.btnBackSettings);

        seekSound = findViewById(R.id.seekSound);

        switchMusic = findViewById(R.id.switchMusic);
        switchVibration = findViewById(R.id.switchVibration);
        switchNotifications = findViewById(R.id.switchNotifications);

        btnThemeDark = findViewById(R.id.btnThemeDark);
        btnThemeMatrix = findViewById(R.id.btnThemeMatrix);

        btnSyncNow = findViewById(R.id.btnSyncNow);
        btnLogout = findViewById(R.id.btnLogoutAccount);
    }

    // Carrega todas as configurações salvas localmente
    private void loadSettings() {

        seekSound.setProgress(
                settingsPrefs.getInt(
                        "sound_volume",
                        70
                )
        );

        switchMusic.setChecked(
                settingsPrefs.getBoolean(
                        "music_enabled",
                        true
                )
        );

        switchVibration.setChecked(
                settingsPrefs.getBoolean(
                        "vibration_enabled",
                        true
                )
        );

        switchNotifications.setChecked(
                settingsPrefs.getBoolean(
                        "notifications_enabled",
                        true
                )
        );
    }

    // Configura todos os eventos da tela
    private void setupClicks() {

        // Fecha a tela atual
        btnBack.setOnClickListener(v -> {
            playClick();
            finish();
        });

        // Salva o volume escolhido pelo usuário
        seekSound.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {

                    @Override
                    public void onProgressChanged(
                            SeekBar seekBar,
                            int progress,
                            boolean fromUser
                    ) {

                        settingsPrefs.edit()
                                .putInt(
                                        "sound_volume",
                                        progress
                                )
                                .apply();
                    }

                    @Override
                    public void onStartTrackingTouch(
                            SeekBar seekBar
                    ) {
                    }

                    @Override
                    public void onStopTrackingTouch(
                            SeekBar seekBar
                    ) {

                        playClick();
                    }
                }
        );

        // Ativa ou desativa música
        switchMusic.setOnCheckedChangeListener(
                (buttonView, isChecked) -> {

                    playClick();

                    settingsPrefs.edit()
                            .putBoolean(
                                    "music_enabled",
                                    isChecked
                            )
                            .apply();
                }
        );

        // Ativa ou desativa vibração
        switchVibration.setOnCheckedChangeListener(
                (buttonView, isChecked) -> {

                    playClick();

                    settingsPrefs.edit()
                            .putBoolean(
                                    "vibration_enabled",
                                    isChecked
                            )
                            .apply();
                }
        );

        // Ativa ou desativa notificações
        switchNotifications.setOnCheckedChangeListener(
                (buttonView, isChecked) -> {

                    playClick();

                    settingsPrefs.edit()
                            .putBoolean(
                                    "notifications_enabled",
                                    isChecked
                            )
                            .apply();
                }
        );

        // Seleciona o tema Dark
        btnThemeDark.setOnClickListener(v -> {

            playClick();

            settingsPrefs.edit()
                    .putString(
                            "theme_mode",
                            "dark"
                    )
                    .apply();

            Toast.makeText(
                    this,
                    "Tema DARK selecionado.",
                    Toast.LENGTH_SHORT
            ).show();
        });

        // Seleciona o tema Matrix
        btnThemeMatrix.setOnClickListener(v -> {

            playClick();

            settingsPrefs.edit()
                    .putString(
                            "theme_mode",
                            "matrix"
                    )
                    .apply();

            Toast.makeText(
                    this,
                    "Tema MATRIX selecionado.",
                    Toast.LENGTH_SHORT
            ).show();
        });

        // Força sincronização manual
        btnSyncNow.setOnClickListener(
                v -> syncNow()
        );

        // Inicia processo de logout
        btnLogout.setOnClickListener(
                v -> showLogoutConfirmDialog()
        );
    }

    // Sincroniza manualmente o progresso com a nuvem
    private void syncNow() {

        playClick();

        btnSyncNow.setEnabled(false);
        btnSyncNow.setText("SINCRONIZANDO...");

        new CloudSaveManager(playerPrefs)
                .saveProgress(

                        // Sucesso
                        () -> {

                            btnSyncNow.setEnabled(true);
                            btnSyncNow.setText(
                                    "SINCRONIZAR AGORA"
                            );

                            Toast.makeText(
                                    this,
                                    "Progresso sincronizado.",
                                    Toast.LENGTH_SHORT
                            ).show();
                        },

                        // Erro
                        () -> {

                            btnSyncNow.setEnabled(true);
                            btnSyncNow.setText(
                                    "SINCRONIZAR AGORA"
                            );

                            Toast.makeText(
                                    this,
                                    "Erro ao sincronizar. Verifique o login.",
                                    Toast.LENGTH_SHORT
                            ).show();
                        }
                );
    }

    // Exibe confirmação antes de apagar dados e sair da conta
    private void showLogoutConfirmDialog() {

        SoundManager.playSound(
                this,
                R.raw.cyber_error
        );

        new AlertDialog.Builder(this)
                .setTitle("Sair da conta?")
                .setMessage(
                        "Isso vai apagar o progresso dessa conta no banco e limpar os dados locais. Essa ação não tem Ctrl+Z, infelizmente."
                )
                .setPositiveButton(
                        "Apagar e sair",
                        (dialog, which) ->
                                deleteCloudAndLogout()
                )
                .setNegativeButton(
                        "Cancelar",
                        (dialog, which) ->
                                playClick()
                )
                .show();
    }

    // Remove dados da nuvem antes de desconectar a conta
    private void deleteCloudAndLogout() {

        btnLogout.setEnabled(false);
        btnLogout.setText("APAGANDO...");

        new CloudSaveManager(playerPrefs)
                .deleteProgress(

                        // Sucesso
                        this::clearLocalAndGoLogin,

                        // Erro
                        () -> {

                            btnLogout.setEnabled(true);
                            btnLogout.setText(
                                    "SAIR DA CONTA"
                            );

                            Toast.makeText(
                                    this,
                                    "Erro ao apagar no banco. Tente novamente.",
                                    Toast.LENGTH_LONG
                            ).show();
                        }
                );
    }

    // Limpa dados locais e retorna para tela inicial
    private void clearLocalAndGoLogin() {

        // Remove dados do jogador
        playerPrefs.clearAll();

        // Faz logout do Firebase
        FirebaseAuth.getInstance().signOut();

        // Faz logout da conta Google
        GoogleSignIn.getClient(
                this,
                new GoogleSignInOptions.Builder(
                        GoogleSignInOptions.DEFAULT_SIGN_IN
                ).build()
        ).signOut();

        Toast.makeText(
                this,
                "Conta desconectada e progresso apagado.",
                Toast.LENGTH_LONG
        ).show();

        // Retorna para a tela inicial
        Intent intent = new Intent(
                SettingsActivity.this,
                MainActivity.class
        );

        // Limpa a pilha de Activities
        intent.addFlags(
                Intent.FLAG_ACTIVITY_NEW_TASK |
                        Intent.FLAG_ACTIVITY_CLEAR_TASK
        );

        startActivity(intent);

        finish();
    }

    // Reproduz o som padrão de clique
    private void playClick() {

        SoundManager.playSound(
                this,
                R.raw.cyber_click
        );
    }
}