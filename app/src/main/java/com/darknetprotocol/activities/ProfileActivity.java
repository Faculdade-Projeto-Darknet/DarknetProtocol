package com.darknetprotocol.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.darknetprotocol.R;
import com.darknetprotocol.utils.PlayerPrefs;
import com.darknetprotocol.utils.SoundManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ProfileActivity extends AppCompatActivity {

    private ImageView imgProfile;

    private TextView txtNickname;
    private TextView txtRank;
    private TextView txtXp;
    private TextView txtMissions;
    private TextView txtAchievements;

    private ProgressBar progressXp;

    private AppCompatButton btnResetProfile;
    private AppCompatButton btnOpenSettings;

    private BottomNavigationView bottomNavigation;

    private PlayerPrefs playerPrefs;

    private ActivityResultLauncher<String[]> pickImageLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        playerPrefs = new PlayerPrefs(this);

        setupImagePicker();
        initializeViews();
        setupBottomNavigation();
        setupButtons();

        loadProfile();
    }

    private void initializeViews() {
        imgProfile = findViewById(R.id.imgProfile);

        txtNickname = findViewById(R.id.txtNickname);
        txtRank = findViewById(R.id.txtRank);
        txtXp = findViewById(R.id.txtXp);
        txtMissions = findViewById(R.id.txtMissions);
        txtAchievements = findViewById(R.id.txtAchievements);

        progressXp = findViewById(R.id.progressXp);

        btnResetProfile = findViewById(R.id.btnResetProfile);
        btnOpenSettings = findViewById(R.id.btnOpenSettings);

        bottomNavigation = findViewById(R.id.bottomNavigation);
    }

    private void setupImagePicker() {
        pickImageLauncher = registerForActivityResult(
                new ActivityResultContracts.OpenDocument(),
                uri -> {
                    if (uri == null) {
                        return;
                    }

                    try {
                        getContentResolver().takePersistableUriPermission(
                                uri,
                                Intent.FLAG_GRANT_READ_URI_PERMISSION
                        );

                        playerPrefs.setProfileImage(uri.toString());
                        imgProfile.setImageURI(uri);

                        playClick();

                    } catch (Exception e) {
                        imgProfile.setImageResource(R.drawable.default_avatar);
                    }
                }
        );
    }

    private void loadProfile() {
        String nickname = playerPrefs.getNickname();

        if (nickname == null || nickname.trim().isEmpty()) {
            nickname = "Ghost_404";
        }

        int xp = playerPrefs.getXp();

        int completed = 0;

        if (playerPrefs.isMission1Completed()) completed++;
        if (playerPrefs.isMission2Completed()) completed++;
        if (playerPrefs.isMission3Completed()) completed++;
        if (playerPrefs.isMission4Completed()) completed++;

        txtNickname.setText(nickname);
        txtXp.setText(xp + " XP");
        txtMissions.setText(completed + "/4");

        progressXp.setMax(450);
        progressXp.setProgress(Math.min(xp, 450));

        if (xp >= 350) {
            txtRank.setText("RANK • ELITE");
        } else if (xp >= 200) {
            txtRank.setText("RANK • ANALISTA");
        } else {
            txtRank.setText("RANK • OPERADOR INICIANTE");
        }

        if (completed == 0) {
            txtAchievements.setText("Nenhuma conquista desbloqueada ainda.");
        } else if (completed < 4) {
            txtAchievements.setText(
                    "Operações concluídas: " + completed +
                            "\nAgente em progresso."
            );
        } else {
            txtAchievements.setText(
                    "Projeto Orion concluído.\nTodos os sistemas invadidos."
            );
        }

        loadProfileImage();
    }

    private void loadProfileImage() {
        String imageUri = playerPrefs.getProfileImage();

        if (imageUri != null && !imageUri.trim().isEmpty()) {
            try {
                imgProfile.setImageURI(Uri.parse(imageUri));
            } catch (Exception e) {
                imgProfile.setImageResource(R.drawable.default_avatar);
            }
        } else {
            imgProfile.setImageResource(R.drawable.default_avatar);
        }
    }

    private void setupButtons() {
        btnOpenSettings.setOnClickListener(v -> {
            playClick();

            Intent intent = new Intent(
                    ProfileActivity.this,
                    SettingsActivity.class
            );

            startActivity(intent);

            overridePendingTransition(
                    R.anim.slide_in_right,
                    R.anim.slide_out_left
            );
        });

        imgProfile.setOnClickListener(v -> {
            playClick();

            pickImageLauncher.launch(
                    new String[]{"image/*"}
            );
        });

        txtNickname.setOnClickListener(v -> {
            playClick();
            showNicknameDialog();
        });

        btnResetProfile.setOnClickListener(v -> {
            SoundManager.playSound(this, R.raw.cyber_error);

            new AlertDialog.Builder(this)
                    .setTitle("Resetar progresso?")
                    .setMessage("Todo o progresso local será apagado.")
                    .setPositiveButton(
                            "Resetar",
                            (dialog, which) -> {
                                playerPrefs.clearAll();
                                loadProfile();
                                playClick();
                            }
                    )
                    .setNegativeButton(
                            "Cancelar",
                            (dialog, which) -> playClick()
                    )
                    .show();
        });
    }

    private void showNicknameDialog() {
        EditText editText = new EditText(this);

        editText.setText(txtNickname.getText().toString());
        editText.setSingleLine(true);
        editText.setSelection(editText.getText().length());

        new AlertDialog.Builder(this)
                .setTitle("Alterar nickname")
                .setView(editText)
                .setPositiveButton(
                        "Salvar",
                        (dialog, which) -> {
                            String newNickname = editText
                                    .getText()
                                    .toString()
                                    .trim();

                            if (!newNickname.isEmpty()) {
                                playerPrefs.setNickname(newNickname);
                                txtNickname.setText(newNickname);
                                playClick();
                            }
                        }
                )
                .setNegativeButton(
                        "Cancelar",
                        (dialog, which) -> playClick()
                )
                .show();
    }

    private void setupBottomNavigation() {
        bottomNavigation.setSelectedItemId(R.id.nav_profile);

        bottomNavigation.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.nav_profile) {
                return true;
            }

            playClick();

            if (itemId == R.id.nav_terminal) {
                startActivity(
                        new Intent(this, TerminalActivity.class)
                );

                overridePendingTransition(
                        R.anim.slide_in_left,
                        R.anim.slide_out_right
                );

                finish();

                return true;
            }

            if (itemId == R.id.nav_missions) {
                startActivity(
                        new Intent(this, MissionsActivity.class)
                );

                overridePendingTransition(
                        R.anim.slide_in_left,
                        R.anim.slide_out_right
                );

                finish();

                return true;
            }

            return false;
        });
    }

    private void playClick() {
        SoundManager.playSound(
                this,
                R.raw.cyber_click
        );
    }
}