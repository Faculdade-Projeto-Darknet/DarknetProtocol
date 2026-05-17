package com.darknetprotocol;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity {

    TextView txtNickname, txtRank, txtXp, txtMissions, txtAchievements;
    ProgressBar progressXp;
    Button btnResetProfile;

    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        txtNickname = findViewById(R.id.txtNickname);
        txtRank = findViewById(R.id.txtRank);
        txtXp = findViewById(R.id.txtXp);
        txtMissions = findViewById(R.id.txtMissions);
        txtAchievements = findViewById(R.id.txtAchievements);
        progressXp = findViewById(R.id.progressXp);
        btnResetProfile = findViewById(R.id.btnResetProfile);

        preferences = getSharedPreferences("player_data", MODE_PRIVATE);

        loadProfile();

        btnResetProfile.setOnClickListener(v -> {
            SharedPreferences.Editor editor = preferences.edit();
            editor.clear();
            editor.apply();

            loadProfile();

            Toast.makeText(this, "Progresso resetado.", Toast.LENGTH_SHORT).show();
        });
    }

    private void loadProfile() {

        int xp = preferences.getInt("xp_total", 0);

        boolean mission1 = preferences.getBoolean("mission1_completed", false);
        boolean mission2 = preferences.getBoolean("mission2_completed", false);

        int completedMissions = 0;

        if (mission1) {
            completedMissions++;
        }

        if (mission2) {
            completedMissions++;
        }

        txtNickname.setText("Ghost_404");
        txtXp.setText("XP Total: " + xp);
        txtMissions.setText(completedMissions + "/2");

        progressXp.setProgress(xp);

        txtRank.setText("Rank: " + getRank(xp));
        txtAchievements.setText(getAchievements(mission1, mission2, completedMissions, xp));
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

    private String getAchievements(boolean mission1, boolean mission2, int completedMissions, int xp) {

        StringBuilder achievements = new StringBuilder();

        achievements.append("CONQUISTAS\n\n");

        if (!mission1 && !mission2) {
            achievements.append("Nenhuma conquista desbloqueada ainda.");
            return achievements.toString();
        }

        if (mission1) {
            achievements.append("✓ Primeiro Acesso\n");
            achievements.append("✓ Firewall Manipulado\n");
        }

        if (mission2) {
            achievements.append("✓ Senha Decifrada\n");
            achievements.append("✓ Cofre Digital Aberto\n");
        }

        if (completedMissions == 2) {
            achievements.append("✓ Protocolo Darknet Concluído\n");
        }

        if (xp >= 400) {
            achievements.append("✓ Operador Elite\n");
        }

        return achievements.toString();
    }
}