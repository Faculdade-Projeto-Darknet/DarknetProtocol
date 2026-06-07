package com.darknetprotocol.activities;

// Importa a classe Intent, utilizada para abrir outras telas do aplicativo
import android.content.Intent;

// Importa Bundle, usado no ciclo de vida da Activity
import android.os.Bundle;

// Classe base para Activities compatíveis com versões antigas do Android
import androidx.appcompat.app.AppCompatActivity;

// Importa recursos do projeto (layouts, ids, sons, etc.)
import com.darknetprotocol.R;

// Classe responsável por reproduzir efeitos sonoros
import com.darknetprotocol.utils.SoundManager;

// Componente de navegação inferior do Material Design
import com.google.android.material.bottomnavigation.BottomNavigationView;

// Activity responsável pelo menu principal do sistema
public class MenuActivity extends AppCompatActivity {

    // Barra de navegação inferior
    BottomNavigationView bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Define o layout XML utilizado nesta tela
        setContentView(R.layout.activity_menu);

        // Liga a variável Java ao componente do XML
        bottomNavigation = findViewById(R.id.bottomNavigation);

        // Define que a aba de missões estará selecionada por padrão
        bottomNavigation.setSelectedItemId(R.id.nav_missions);

        // Escuta os cliques nos itens do menu inferior
        bottomNavigation.setOnItemSelectedListener(item -> {

            // Obtém o ID do item selecionado
            int id = item.getItemId();

            // Reproduz um efeito sonoro sempre que o usuário navegar pelo menu
            SoundManager.playSound(this, R.raw.cyber_click);

            // Se o usuário clicar em "Missões"
            if (id == R.id.nav_missions) {

                // Abre a tela de missões
                startActivity(
                        new Intent(
                                MenuActivity.this,
                                MissionsActivity.class
                        )
                );

                // Fecha a tela atual para evitar empilhamento de Activities
                finish();

                return true;
            }

            // Se o usuário clicar em "Terminal"
            if (id == R.id.nav_terminal) {

                // Abre a tela de terminal
                startActivity(
                        new Intent(
                                MenuActivity.this,
                                TerminalActivity.class
                        )
                );

                // Fecha a tela atual
                finish();

                return true;
            }

            // Se o usuário clicar em "Perfil"
            if (id == R.id.nav_profile) {

                // Abre a tela de perfil do jogador
                startActivity(
                        new Intent(
                                MenuActivity.this,
                                ProfileActivity.class
                        )
                );

                // Fecha a tela atual
                finish();

                return true;
            }

            // Caso nenhum item válido seja selecionado
            return false;
        });
    }
}