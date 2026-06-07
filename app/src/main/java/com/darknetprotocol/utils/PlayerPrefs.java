package com.darknetprotocol.utils;

// Importa Context para acessar SharedPreferences
import android.content.Context;

// Importa SharedPreferences para armazenar dados localmente
import android.content.SharedPreferences;

// Classe responsável por gerenciar os dados do jogador
public class PlayerPrefs {

    /*
     * =========================================================
     * CONFIGURAÇÕES DO ARQUIVO
     * =========================================================
     */

    // Nome do arquivo SharedPreferences
    private static final String PREF_NAME = "player_data";

    /*
     * =========================================================
     * CHAVES DE XP
     * =========================================================
     */

    // Chave utilizada para armazenar o XP total do jogador
    private static final String KEY_XP = "xp_total";

    /*
     * =========================================================
     * CHAVES DAS MISSÕES
     * =========================================================
     */

    private static final String KEY_MISSION_1 = "mission1_completed";
    private static final String KEY_MISSION_2 = "mission2_completed";
    private static final String KEY_MISSION_3 = "mission3_completed";
    private static final String KEY_MISSION_4 = "mission4_completed";

    /*
     * =========================================================
     * CHAVES DO PERFIL
     * =========================================================
     */

    // Nickname do jogador
    private static final String KEY_NICKNAME = "nickname";

    // URI da imagem de perfil
    private static final String KEY_PROFILE_IMAGE = "profile_image";

    /*
     * =========================================================
     * SHARED PREFERENCES
     * =========================================================
     */

    // Referência ao armazenamento local
    private final SharedPreferences preferences;

    // Construtor da classe
    public PlayerPrefs(Context context) {

        // Obtém o arquivo SharedPreferences do aplicativo
        preferences = context.getSharedPreferences(
                PREF_NAME,
                Context.MODE_PRIVATE
        );
    }

    /*
     * =========================================================
     * XP DO JOGADOR
     * =========================================================
     */

    // Retorna o XP atual
    public int getXp() {

        return preferences.getInt(
                KEY_XP,
                0
        );
    }

    // Define um valor de XP
    public void setXp(int xp) {

        preferences.edit()
                .putInt(
                        KEY_XP,
                        Math.max(xp, 0)
                )
                .apply();
    }

    // Adiciona XP ao valor atual
    public void addXp(int amount) {

        int currentXp = getXp();

        preferences.edit()
                .putInt(
                        KEY_XP,
                        Math.max(
                                currentXp + amount,
                                0
                        )
                )
                .apply();
    }

    /*
     * =========================================================
     * MISSÃO 1
     * =========================================================
     */

    // Verifica se a missão 1 foi concluída
    public boolean isMission1Completed() {

        return preferences.getBoolean(
                KEY_MISSION_1,
                false
        );
    }

    // Marca a missão 1 como concluída ou não
    public void setMission1Completed(boolean completed) {

        preferences.edit()
                .putBoolean(
                        KEY_MISSION_1,
                        completed
                )
                .apply();
    }

    /*
     * =========================================================
     * MISSÃO 2
     * =========================================================
     */

    // Verifica se a missão 2 foi concluída
    public boolean isMission2Completed() {

        return preferences.getBoolean(
                KEY_MISSION_2,
                false
        );
    }

    // Define o status da missão 2
    public void setMission2Completed(boolean completed) {

        preferences.edit()
                .putBoolean(
                        KEY_MISSION_2,
                        completed
                )
                .apply();
    }

    /*
     * =========================================================
     * MISSÃO 3
     * =========================================================
     */

    // Verifica se a missão 3 foi concluída
    public boolean isMission3Completed() {

        return preferences.getBoolean(
                KEY_MISSION_3,
                false
        );
    }

    // Define o status da missão 3
    public void setMission3Completed(boolean completed) {

        preferences.edit()
                .putBoolean(
                        KEY_MISSION_3,
                        completed
                )
                .apply();
    }

    /*
     * =========================================================
     * MISSÃO 4
     * =========================================================
     */

    // Verifica se a missão 4 foi concluída
    public boolean isMission4Completed() {

        return preferences.getBoolean(
                KEY_MISSION_4,
                false
        );
    }

    // Define o status da missão 4
    public void setMission4Completed(boolean completed) {

        preferences.edit()
                .putBoolean(
                        KEY_MISSION_4,
                        completed
                )
                .apply();
    }

    /*
     * =========================================================
     * CONTADOR DE MISSÕES
     * =========================================================
     */

    // Retorna quantas missões já foram concluídas
    public int getCompletedMissionsCount() {

        int count = 0;

        if (isMission1Completed()) {
            count++;
        }

        if (isMission2Completed()) {
            count++;
        }

        if (isMission3Completed()) {
            count++;
        }

        if (isMission4Completed()) {
            count++;
        }

        return count;
    }

    /*
     * =========================================================
     * NICKNAME
     * =========================================================
     */

    // Retorna o nickname do jogador
    public String getNickname() {

        return preferences.getString(
                KEY_NICKNAME,
                "Ghost_404"
        );
    }

    // Salva um novo nickname
    public void setNickname(String nickname) {

        preferences.edit()
                .putString(
                        KEY_NICKNAME,
                        nickname
                )
                .apply();
    }

    /*
     * =========================================================
     * FOTO DE PERFIL
     * =========================================================
     */

    // Retorna a URI da foto de perfil
    public String getProfileImage() {

        return preferences.getString(
                KEY_PROFILE_IMAGE,
                null
        );
    }

    // Salva a URI da foto de perfil
    public void setProfileImage(String imageUri) {

        preferences.edit()
                .putString(
                        KEY_PROFILE_IMAGE,
                        imageUri
                )
                .apply();
    }

    /*
     * =========================================================
     * RESETAR APENAS PROGRESSO
     * =========================================================
     */

    // Remove XP e progresso das missões
    public void resetProgressOnly() {

        preferences.edit()
                .putInt(KEY_XP, 0)
                .putBoolean(KEY_MISSION_1, false)
                .putBoolean(KEY_MISSION_2, false)
                .putBoolean(KEY_MISSION_3, false)
                .putBoolean(KEY_MISSION_4, false)
                .apply();
    }

    /*
     * =========================================================
     * LIMPAR TODOS OS DADOS
     * =========================================================
     */

    // Remove absolutamente todos os dados salvos
    public void clearAll() {

        preferences.edit()
                .clear()
                .apply();
    }
}