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
import com.darknetprotocol.utils.PlayerPrefs;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    TextView txtNickname;
    TextView txtRank;
    TextView txtXp;
    TextView txtMissions;
    TextView txtAchievements;

    ProgressBar progressXp;

    CircleImageView imgProfile;

    TextView btnResetProfile;

    BottomNavigationView bottomNavigation;

    PlayerPrefs playerPrefs;

    ActivityResultLauncher<String[]> pickImageLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        playerPrefs = new PlayerPrefs(this);

        txtNickname = findViewById(R.id.txtNickname);
        txtRank = findViewById(R.id.txtRank);
        txtXp = findViewById(R.id.txtXp);
        txtMissions = findViewById(R.id.txtMissions);
        txtAchievements = findViewById(R.id.txtAchievements);

        progressXp = findViewById(R.id.progressXp);

        imgProfile = findViewById(R.id.imgProfile);

        btnResetProfile = findViewById(R.id.btnResetProfile);

        bottomNavigation = findViewById(R.id.bottomNavigation);

        setupImagePicker();
        setupBottomNavigation();
        loadProfile();

        imgProfile.setOnClickListener(v -> {
            pickImageLauncher.launch(new String[]{"image/*"});
        });

        txtNickname.setOnClickListener(v -> {
            showNickDialog();
        });

        btnResetProfile.setOnClickListener(v -> {

            playerPrefs.clearAll();

            loadProfile();

            Toast.makeText(
                    this,
                    "Perfil resetado.",
                    Toast.LENGTH_SHORT
            ).show();

        });
    }

    private void setupImagePicker() {

        pickImageLauncher = registerForActivityResult(
                new ActivityResultContracts.OpenDocument(),
                uri -> {

                    if (uri != null) {

                        try {

                            getContentResolver().takePersistableUriPermission(
                                    uri,
                                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                            );

                            imgProfile.setImageURI(uri);

                            playerPrefs.setProfileImage(
                                    uri.toString()
                            );

                        } catch (Exception e) {

                            imgProfile.setImageResource(
                                    R.mipmap.ic_launcher
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

    private void showNickDialog() {

        EditText editText = new EditText(this);

        editText.setHint("Novo nickname");

        editText.setText(
                txtNickname.getText().toString()
        );

        new AlertDialog.Builder(this)

                .setTitle("Alterar nickname")

                .setView(editText)

                .setPositiveButton("Salvar", (dialog, which) -> {

                    String newNick = editText
                            .getText()
                            .toString()
                            .trim();

                    if (!newNick.isEmpty()) {

                        playerPrefs.setNickname(newNick);

                        txtNickname.setText(newNick);

                    }

                })

                .setNegativeButton("Cancelar", null)

                .show();

    }

    private void loadProfile() {

        int xp = playerPrefs.getXp();

        boolean mission1 =
                playerPrefs.isMission1Completed();

        boolean mission2 =
                playerPrefs.isMission2Completed();

        int completedMissions =
                playerPrefs.getCompletedMissionsCount();

        String nickname =
                playerPrefs.getNickname();

        String imageUri =
                playerPrefs.getProfileImage();

        txtNickname.setText(nickname);

        txtXp.setText(
                "XP Total: " + xp
        );

        txtMissions.setText(
                completedMissions + "/2"
        );

        progressXp.setProgress(xp);

        txtRank.setText(
                "Rank: " + getRank(xp)
        );

        txtAchievements.setText(
                getAchievements(
                        mission1,
                        mission2,
                        completedMissions,
                        xp
                )
        );

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

    private String getRank(int xp) {

        if (xp >= 400) {
            return "Operador Elite";
        }

        if (xp >= 250) {
            return "Infiltrador Avançado";
        }

        if (xp >= 150) {
            return "Agente de Rede";
        }

        return "Operador Iniciante";
    }

    private String getAchievements(
            boolean mission1,
            boolean mission2,
            int completedMissions,
            int xp
    ) {

        StringBuilder achievements =
                new StringBuilder();

        achievements.append("CONQUISTAS\n\n");

        if (!mission1 && !mission2) {

            achievements.append(
                    "Nenhuma conquista desbloqueada ainda."
            );

            return achievements.toString();

        }

        if (mission1) {

            achievements.append(
                    "✓ Primeiro Acesso\n"
            );

            achievements.append(
                    "✓ Firewall Manipulado\n"
            );

        }

        if (mission2) {

            achievements.append(
                    "✓ Senha Decifrada\n"
            );

            achievements.append(
                    "✓ Cofre Digital Aberto\n"
            );

        }

        if (completedMissions == 2) {

            achievements.append(
                    "✓ Protocolo Darknet Concluído\n"
            );

        }

        if (xp >= 400) {

            achievements.append(
                    "✓ Operador Elite\n"
            );

        }

        return achievements.toString();

    }

    private void setupBottomNavigation() {

        bottomNavigation.setSelectedItemId(
                R.id.nav_profile
        );

        bottomNavigation.setOnItemSelectedListener(item -> {

            int id = item.getItemId();

            if (id == R.id.nav_profile) {
                return true;
            }

            if (id == R.id.nav_missions) {

                Intent intent = new Intent(
                        ProfileActivity.this,
                        MissionsActivity.class
                );

                intent.addFlags(
                        Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
                );

                startActivity(intent);

                overridePendingTransition(
                        R.anim.slide_in_left,
                        R.anim.slide_out_right
                );

                return true;
            }

            if (id == R.id.nav_terminal) {

                Intent intent = new Intent(
                        ProfileActivity.this,
                        TerminalActivity.class
                );

                intent.addFlags(
                        Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
                );

                startActivity(intent);

                overridePendingTransition(
                        R.anim.slide_in_left,
                        R.anim.slide_out_right
                );

                return true;
            }

            return false;
        });
    }
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
}