package com.darknetprotocol.activities;

// Importa classes utilizadas para diálogos, navegação e manipulação de arquivos
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

// Importa componentes visuais da interface
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

// Classes base do AndroidX
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

// Recursos do projeto
import com.darknetprotocol.R;

// Classes utilitárias do projeto
import com.darknetprotocol.utils.PlayerPrefs;
import com.darknetprotocol.utils.SoundManager;

// Componente de navegação inferior
import com.google.android.material.bottomnavigation.BottomNavigationView;

// Activity responsável pelo perfil do jogador
public class ProfileActivity extends AppCompatActivity {

    // Imagem de perfil do usuário
    private ImageView imgProfile;

    // Informações exibidas no perfil
    private TextView txtNickname;
    private TextView txtRank;
    private TextView txtXp;
    private TextView txtMissions;
    private TextView txtAchievements;

    // Barra de progresso do XP
    private ProgressBar progressXp;

    // Botões da tela
    private AppCompatButton btnResetProfile;
    private AppCompatButton btnOpenSettings;

    // Menu inferior
    private BottomNavigationView bottomNavigation;

    // Sistema de armazenamento local
    private PlayerPrefs playerPrefs;

    // Responsável por selecionar imagens da galeria
    private ActivityResultLauncher<String[]> pickImageLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Define o layout da tela
        setContentView(R.layout.activity_profile);

        // Inicializa o sistema de preferências
        playerPrefs = new PlayerPrefs(this);

        // Configura seleção de imagem
        setupImagePicker();

        // Inicializa componentes visuais
        initializeViews();

        // Configura navegação inferior
        setupBottomNavigation();

        // Configura eventos dos botões
        setupButtons();

        // Carrega os dados do perfil
        loadProfile();
    }

    // Inicializa todos os componentes da interface
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

    // Configura o seletor de imagens da galeria
    private void setupImagePicker() {

        pickImageLauncher = registerForActivityResult(
                new ActivityResultContracts.OpenDocument(),
                uri -> {

                    // Se o usuário cancelar a seleção
                    if (uri == null) {
                        return;
                    }

                    try {

                        // Mantém permissão permanente para acessar a imagem
                        getContentResolver().takePersistableUriPermission(
                                uri,
                                Intent.FLAG_GRANT_READ_URI_PERMISSION
                        );

                        // Salva o caminho da imagem
                        playerPrefs.setProfileImage(uri.toString());

                        // Atualiza a imagem na tela
                        imgProfile.setImageURI(uri);

                        playClick();

                    } catch (Exception e) {

                        // Caso ocorra erro, usa avatar padrão
                        imgProfile.setImageResource(
                                R.drawable.default_avatar
                        );
                    }
                }
        );
    }

    // Carrega todas as informações do perfil
    private void loadProfile() {

        String nickname = playerPrefs.getNickname();

        // Define nickname padrão caso não exista
        if (nickname == null || nickname.trim().isEmpty()) {
            nickname = "Ghost_404";
        }

        // Obtém XP atual
        int xp = playerPrefs.getXp();

        // Conta quantas missões foram concluídas
        int completed = 0;

        if (playerPrefs.isMission1Completed()) completed++;
        if (playerPrefs.isMission2Completed()) completed++;
        if (playerPrefs.isMission3Completed()) completed++;
        if (playerPrefs.isMission4Completed()) completed++;

        // Atualiza informações da tela
        txtNickname.setText(nickname);
        txtXp.setText(xp + " XP");
        txtMissions.setText(completed + "/4");

        // Configura barra de XP
        progressXp.setMax(450);
        progressXp.setProgress(Math.min(xp, 450));

        // Define rank baseado no XP
        if (xp >= 350) {

            txtRank.setText("RANK • ELITE");

        } else if (xp >= 200) {

            txtRank.setText("RANK • ANALISTA");

        } else {

            txtRank.setText("RANK • OPERADOR INICIANTE");
        }

        // Define conquistas exibidas
        if (completed == 0) {

            txtAchievements.setText(
                    "Nenhuma conquista desbloqueada ainda."
            );

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

        // Carrega imagem de perfil
        loadProfileImage();
    }

    // Carrega a imagem salva do perfil
    private void loadProfileImage() {

        String imageUri = playerPrefs.getProfileImage();

        if (imageUri != null && !imageUri.trim().isEmpty()) {

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

    // Configura eventos dos botões da tela
    private void setupButtons() {

        // Abre tela de configurações
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

        // Permite alterar foto de perfil
        imgProfile.setOnClickListener(v -> {

            playClick();

            pickImageLauncher.launch(
                    new String[]{"image/*"}
            );
        });

        // Permite alterar nickname
        txtNickname.setOnClickListener(v -> {

            playClick();

            showNicknameDialog();
        });

        // Reseta progresso do jogador
        btnResetProfile.setOnClickListener(v -> {

            SoundManager.playSound(
                    this,
                    R.raw.cyber_error
            );

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

    // Exibe janela para alterar nickname
    private void showNicknameDialog() {

        EditText editText = new EditText(this);

        editText.setText(
                txtNickname.getText().toString()
        );

        editText.setSingleLine(true);

        editText.setSelection(
                editText.getText().length()
        );

        new AlertDialog.Builder(this)
                .setTitle("Alterar nickname")
                .setView(editText)
                .setPositiveButton(
                        "Salvar",
                        (dialog, which) -> {

                            String newNickname =
                                    editText.getText()
                                            .toString()
                                            .trim();

                            if (!newNickname.isEmpty()) {

                                playerPrefs.setNickname(
                                        newNickname
                                );

                                txtNickname.setText(
                                        newNickname
                                );

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

    // Configura navegação inferior
    private void setupBottomNavigation() {

        bottomNavigation.setSelectedItemId(
                R.id.nav_profile
        );

        bottomNavigation.setOnItemSelectedListener(item -> {

            int itemId = item.getItemId();

            if (itemId == R.id.nav_profile) {
                return true;
            }

            playClick();

            if (itemId == R.id.nav_terminal) {

                startActivity(
                        new Intent(
                                this,
                                TerminalActivity.class
                        )
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
                        new Intent(
                                this,
                                MissionsActivity.class
                        )
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

    // Reproduz som de clique padrão
    private void playClick() {

        SoundManager.playSound(
                this,
                R.raw.cyber_click
        );
    }
}