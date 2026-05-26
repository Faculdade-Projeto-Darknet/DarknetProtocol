package com.darknetprotocol.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.SwitchCompat;

import com.darknetprotocol.R;
import com.darknetprotocol.utils.CloudSaveManager;
import com.darknetprotocol.utils.PlayerPrefs;
import com.darknetprotocol.utils.SoundManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;

public class SettingsActivity extends AppCompatActivity {

    private static final String SETTINGS_PREFS = "settings_data";

    private TextView btnBack;

    private SeekBar seekSound;

    private SwitchCompat switchMusic;
    private SwitchCompat switchVibration;
    private SwitchCompat switchNotifications;

    private AppCompatButton btnThemeDark;
    private AppCompatButton btnThemeMatrix;
    private AppCompatButton btnSyncNow;
    private AppCompatButton btnLogout;

    private SharedPreferences settingsPrefs;
    private PlayerPrefs playerPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        settingsPrefs = getSharedPreferences(
                SETTINGS_PREFS,
                MODE_PRIVATE
        );

        playerPrefs = new PlayerPrefs(this);

        initializeViews();
        loadSettings();
        setupClicks();
    }

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

    private void loadSettings() {
        seekSound.setProgress(
                settingsPrefs.getInt("sound_volume", 70)
        );

        switchMusic.setChecked(
                settingsPrefs.getBoolean("music_enabled", true)
        );

        switchVibration.setChecked(
                settingsPrefs.getBoolean("vibration_enabled", true)
        );

        switchNotifications.setChecked(
                settingsPrefs.getBoolean("notifications_enabled", true)
        );
    }

    private void setupClicks() {
        btnBack.setOnClickListener(v -> {
            playClick();
            finish();
        });

        seekSound.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(
                            SeekBar seekBar,
                            int progress,
                            boolean fromUser
                    ) {
                        settingsPrefs.edit()
                                .putInt("sound_volume", progress)
                                .apply();
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        playClick();
                    }
                }
        );

        switchMusic.setOnCheckedChangeListener((buttonView, isChecked) -> {
            playClick();

            settingsPrefs.edit()
                    .putBoolean("music_enabled", isChecked)
                    .apply();
        });

        switchVibration.setOnCheckedChangeListener((buttonView, isChecked) -> {
            playClick();

            settingsPrefs.edit()
                    .putBoolean("vibration_enabled", isChecked)
                    .apply();
        });

        switchNotifications.setOnCheckedChangeListener((buttonView, isChecked) -> {
            playClick();

            settingsPrefs.edit()
                    .putBoolean("notifications_enabled", isChecked)
                    .apply();
        });

        btnThemeDark.setOnClickListener(v -> {
            playClick();

            settingsPrefs.edit()
                    .putString("theme_mode", "dark")
                    .apply();

            Toast.makeText(
                    this,
                    "Tema DARK selecionado.",
                    Toast.LENGTH_SHORT
            ).show();
        });

        btnThemeMatrix.setOnClickListener(v -> {
            playClick();

            settingsPrefs.edit()
                    .putString("theme_mode", "matrix")
                    .apply();

            Toast.makeText(
                    this,
                    "Tema MATRIX selecionado.",
                    Toast.LENGTH_SHORT
            ).show();
        });

        btnSyncNow.setOnClickListener(v -> syncNow());

        btnLogout.setOnClickListener(v -> showLogoutConfirmDialog());
    }

    private void syncNow() {
        playClick();

        btnSyncNow.setEnabled(false);
        btnSyncNow.setText("SINCRONIZANDO...");

        new CloudSaveManager(playerPrefs)
                .saveProgress(
                        () -> {
                            btnSyncNow.setEnabled(true);
                            btnSyncNow.setText("SINCRONIZAR AGORA");

                            Toast.makeText(
                                    this,
                                    "Progresso sincronizado.",
                                    Toast.LENGTH_SHORT
                            ).show();
                        },
                        () -> {
                            btnSyncNow.setEnabled(true);
                            btnSyncNow.setText("SINCRONIZAR AGORA");

                            Toast.makeText(
                                    this,
                                    "Erro ao sincronizar. Verifique o login.",
                                    Toast.LENGTH_SHORT
                            ).show();
                        }
                );
    }

    private void showLogoutConfirmDialog() {
        SoundManager.playSound(this, R.raw.cyber_error);

        new AlertDialog.Builder(this)
                .setTitle("Sair da conta?")
                .setMessage(
                        "Isso vai apagar o progresso dessa conta no banco e limpar os dados locais. Essa ação não tem Ctrl+Z, infelizmente."
                )
                .setPositiveButton(
                        "Apagar e sair",
                        (dialog, which) -> deleteCloudAndLogout()
                )
                .setNegativeButton(
                        "Cancelar",
                        (dialog, which) -> playClick()
                )
                .show();
    }

    private void deleteCloudAndLogout() {
        btnLogout.setEnabled(false);
        btnLogout.setText("APAGANDO...");

        new CloudSaveManager(playerPrefs)
                .deleteProgress(
                        this::clearLocalAndGoLogin,
                        () -> {
                            btnLogout.setEnabled(true);
                            btnLogout.setText("SAIR DA CONTA");

                            Toast.makeText(
                                    this,
                                    "Erro ao apagar no banco. Tente novamente.",
                                    Toast.LENGTH_LONG
                            ).show();
                        }
                );
    }

    private void clearLocalAndGoLogin() {
        playerPrefs.clearAll();

        FirebaseAuth.getInstance().signOut();

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

        Intent intent = new Intent(
                SettingsActivity.this,
                MainActivity.class
        );

        intent.addFlags(
                Intent.FLAG_ACTIVITY_NEW_TASK |
                        Intent.FLAG_ACTIVITY_CLEAR_TASK
        );

        startActivity(intent);
        finish();
    }

    private void playClick() {
        SoundManager.playSound(
                this,
                R.raw.cyber_click
        );
    }
}