package com.darknetprotocol.activities;

// Importa a classe Intent, utilizada para abrir outras telas
import android.content.Intent;

// Importa Bundle, usado no ciclo de vida da Activity
import android.os.Bundle;

// Importa o componente Button utilizado na interface
import android.widget.Button;

// Classe base para Activities compatíveis com AppCompat
import androidx.appcompat.app.AppCompatActivity;

// Importa recursos do projeto (layouts, ids, animações e sons)
import com.darknetprotocol.R;

// Classe responsável por reproduzir efeitos sonoros
import com.darknetprotocol.utils.SoundManager;

// Activity responsável por exibir o briefing da Missão 4
public class Mission4BriefingActivity extends AppCompatActivity {

    // Botão utilizado para iniciar a missão
    Button btnStartMission4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Define o layout que será exibido nesta tela
        setContentView(R.layout.activity_mission4_briefing);

        // Liga a variável Java ao botão presente no XML
        btnStartMission4 = findViewById(R.id.btnStartMission4);

        // Define a ação executada quando o botão for clicado
        btnStartMission4.setOnClickListener(v -> {

            // Reproduz o som de clique ao iniciar a missão
            SoundManager.playSound(
                    Mission4BriefingActivity.this,
                    R.raw.cyber_click
            );

            // Cria uma Intent para abrir a tela da Missão 4
            Intent intent = new Intent(
                    Mission4BriefingActivity.this,
                    IpMissionActivity.class
            );

            // Inicia a Activity da missão
            startActivity(intent);

            // Aplica animação de transição entre as telas
            overridePendingTransition(
                    R.anim.slide_in_right,
                    R.anim.slide_out_left
            );
        });
    }
}