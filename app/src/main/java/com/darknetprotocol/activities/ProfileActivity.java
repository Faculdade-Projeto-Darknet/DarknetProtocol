package com.darknetprotocol.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.darknetprotocol.R;
import com.darknetprotocol.SoundManager;
import com.darknetprotocol.utils.PlayerPrefs;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    /*
     * =========================================================
     * COMPONENTES
     * =========================================================
     */

    private TextView txtNickname;
    private TextView txtRank;
    private TextView txtXp;
    private TextView txtMissions;
    private TextView txtAchievements;

    private ProgressBar progressXp;

    private CircleImageView imgProfile;

    private TextView btnResetProfile;

    private BottomNavigationView bottomNavigation;

    /*
     * =========================================================
     * SISTEMA
     * =========================================================
     */

    private PlayerPrefs playerPrefs;

    private ActivityResultLauncher<String[]>
            pickImageLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_profile);

        initializeViews();

        setupImagePicker();
        setupButtons();
        setupBottomNavigation();

        loadProfile();
    }

    /*
     * =========================================================
     * RESUME
     * =========================================================
     */

    @Override
    protected void onResume() {
        super.onResume();

        loadProfile();

        if (bottomNavigation != null) {
            bottomNavigation.setSelectedItemId(
                    R.id.nav_profile
            );
        }
    }

    /*
     * =========================================================
     * INICIALIZA COMPONENTES
     * =========================================================
     */

    private void initializeViews() {

        playerPrefs =
                new PlayerPrefs(this);

        /*
         * TEXTOS
         */

        txtNickname =
                findViewById(R.id.txtNickname);

        txtRank =
                findViewById(R.id.txtRank);

        txtXp =
                findViewById(R.id.txtXp);

        txtMissions =
                findViewById(R.id.txtMissions);

        txtAchievements =
                findViewById(R.id.txtAchievements);

        /*
         * PROGRESSO
         */

        progressXp =
                findViewById(R.id.progressXp);

        /*
         * IMAGEM
         */

        imgProfile =
                findViewById(R.id.imgProfile);

        /*
         * RESET
         */

        btnResetProfile =
                findViewById(R.id.btnResetProfile);

        /*
         * NAVEGAÇÃO
         */

        bottomNavigation =
                findViewById(R.id.bottomNavigation);
    }

    /*
     * =========================================================
     * BOTÕES
     * =========================================================
     */

    private void setupButtons() {

        /*
         * ALTERAR FOTO
         */

        imgProfile.setOnClickListener(v -> {

            playClick();

            pickImageLauncher.launch(
                    new String[]{"image/*"}
            );
        });

        /*
         * ALTERAR NICKNAME
         */

        txtNickname.setOnClickListener(v -> {

            playClick();

            showNickDialog();
        });

        /*
         * RESETAR PERFIL
         */

        btnResetProfile.setOnClickListener(v -> {

            SoundManager.playSound(
                    this,
                    R.raw.cyber_error
            );

            playerPrefs.clearAll();

            loadProfile();

            Toast.makeText(
                    this,
                    "Perfil resetado.",
                    Toast.LENGTH_SHORT
            ).show();
        });
    }

    /*
     * =========================================================
     * IMAGE PICKER
     * =========================================================
     */

    private void setupImagePicker() {

        pickImageLauncher =
                registerForActivityResult(
                        new ActivityResultContracts.OpenDocument(),
                        uri -> {

                            if (uri != null) {

                                try {

                                    getContentResolver()
                                            .takePersistableUriPermission(
                                                    uri,
                                                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                                            );

                                    imgProfile.setImageURI(uri);

                                    playerPrefs.setProfileImage(
                                            uri.toString()
                                    );

                                    SoundManager.playSound(
                                            this,
                                            R.raw.cyber_success
                                    );

                                } catch (Exception e) {

                                    imgProfile.setImageResource(
                                            R.drawable.default_avatar
                                    );

                                    SoundManager.playSound(
                                            this,
                                            R.raw.cyber_error
                                    );

                                    Toast.makeText(
                                            this,
                                            "Não foi possível salvar a imagem.",
                                            Toast.LENGTH_SHORT
                                    ).show();
                                }
                            }
                        }
                );
    }

    /*
     * =========================================================
     * DIALOG ALTERAR NICK
     * =========================================================
     */

    private void showNickDialog() {

        EditText editText =
                new EditText(this);

        editText.setHint("Novo nickname");

        editText.setText(
                txtNickname.getText().toString()
        );

        new AlertDialog.Builder(this)

                .setTitle("Alterar nickname")

                .setView(editText)

                .setPositiveButton(
                        "Salvar",
                        (dialog, which) -> {

                            SoundManager.playSound(
                                    this,
                                    R.raw.cyber_success
                            );

                            String newNick =
                                    editText
                                            .getText()
                                            .toString()
                                            .trim();

                            if (!newNick.isEmpty()) {

                                playerPrefs.setNickname(
                                        newNick
                                );

                                txtNickname.setText(
                                        newNick
                                );
                            }
                        }
                )

                .setNegativeButton(
                        "Cancelar",
                        (dialog, which) ->
                                playClick()
                )

                .show();
    }

    /*
     * =========================================================
     * CARREGA PERFIL
     * =========================================================
     */

    private void loadProfile() {

        int xp =
                playerPrefs.getXp();

        int completedMissions =
                playerPrefs.getCompletedMissionsCount();

        String nickname =
                playerPrefs.getNickname();

        String imageUri =
                playerPrefs.getProfileImage();

        /*
         * TEXTOS
         */

        txtNickname.setText(
                nickname
        );

        txtXp.setText(
                xp + " XP"
        );

        txtMissions.setText(
                completedMissions + "/4"
        );

        txtRank.setText(
                "RANK • " + getRank(xp)
        );

        /*
         * PROGRESSO
         */

        progressXp.setProgress(xp);

        /*
         * CONQUISTAS
         */

        txtAchievements.setText(
                getAchievements(xp)
        );

        /*
         * FOTO
         */

        loadProfileImage(imageUri);
    }

    /*
     * =========================================================
     * FOTO PERFIL
     * =========================================================
     */

    private void loadProfileImage(String imageUri) {

        if (imageUri != null) {

            try {

                imgProfile.setImageURI(
                        Uri.parse(imageUri)
                );

            } catch (Exception e) {

                imgProfile.setImageResource(
                        R.drawable.default_avatar
                );
            }

        } else {

            imgProfile.setImageResource(
                    R.drawable.default_avatar
            );
        }
    }

    /*
     * =========================================================
     * SISTEMA DE RANK
     * =========================================================
     */

    private String getRank(int xp) {

        if (xp >= 400) {
            return "OPERADOR ELITE";
        }

        if (xp >= 250) {
            return "INFILTRADOR AVANÇADO";
        }

        if (xp >= 150) {
            return "AGENTE DE REDE";
        }

        return "OPERADOR INICIANTE";
    }

    /*
     * =========================================================
     * CONQUISTAS
     * =========================================================
     */

    private String getAchievements(int xp) {

        StringBuilder achievements =
                new StringBuilder();

        achievements.append(
                "✓ PERFIL ATIVO\n"
        );

        if (playerPrefs.isMission1Completed()) {

            achievements.append(
                    "✓ PRIMEIRO ACESSO\n"
            );

            achievements.append(
                    "✓ FIREWALL MANIPULADO\n"
            );
        }

        if (playerPrefs.isMission2Completed()) {

            achievements.append(
                    "✓ SENHA DECIFRADA\n"
            );

            achievements.append(
                    "✓ COFRE DIGITAL ABERTO\n"
            );
        }

        if (playerPrefs.isMission3Completed()) {

            achievements.append(
                    "✓ ARQUIVO FANTASMA\n"
            );
        }

        if (playerPrefs.isMission4Completed()) {

            achievements.append(
                    "✓ NÓ INVASOR\n"
            );
        }

        if (xp >= 400) {

            achievements.append(
                    "✓ OPERADOR ELITE\n"
            );
        }

        return achievements.toString();
    }

    /*
     * =========================================================
     * NAVEGAÇÃO
     * =========================================================
     */

    private void setupBottomNavigation() {

        bottomNavigation.setSelectedItemId(
                R.id.nav_profile
        );

        bottomNavigation.setOnItemSelectedListener(item -> {

            int id = item.getItemId();

            /*
             * TELA ATUAL
             */

            if (id == R.id.nav_profile) {
                return true;
            }

            playClick();

            /*
             * MISSÕES
             */

            if (id == R.id.nav_missions) {

                openActivity(
                        MissionsActivity.class
                );

                return true;
            }

            /*
             * TERMINAL
             */

            if (id == R.id.nav_terminal) {

                openActivity(
                        TerminalActivity.class
                );

                return true;
            }

            return false;
        });
    }

    /*
     * =========================================================
     * ABRIR ACTIVITY
     * =========================================================
     */

    private void openActivity(
            Class<?> activityClass
    ) {

        Intent intent =
                new Intent(
                        ProfileActivity.this,
                        activityClass
                );

        intent.addFlags(
                Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
        );

        startActivity(intent);

        overridePendingTransition(
                R.anim.slide_in_left,
                R.anim.slide_out_right
        );
    }

    /*
     * =========================================================
     * SOM CLICK
     * =========================================================
     */

    private void playClick() {

        SoundManager.playSound(
                this,
                R.raw.cyber_click
        );
    }
}