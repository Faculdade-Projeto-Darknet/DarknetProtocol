package com.darknetprotocol.utils;

// Classe utilizada para registrar mensagens de depuração e erros
import android.util.Log;

// Classes de autenticação do Firebase
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

// Classe responsável pelo banco Firestore
import com.google.firebase.firestore.FirebaseFirestore;

// Estruturas utilizadas para armazenar dados em formato chave/valor
import java.util.HashMap;
import java.util.Map;

// Classe responsável por sincronizar o progresso do jogador com o Firebase
public class CloudSaveManager {

    // Tag utilizada para identificar mensagens no Logcat
    private static final String TAG = "CloudSave";

    // Referência ao banco Firestore
    private final FirebaseFirestore db;

    // Referência ao sistema de dados locais do jogador
    private final PlayerPrefs playerPrefs;

    // Construtor da classe
    public CloudSaveManager(PlayerPrefs playerPrefs) {

        // Obtém instância do Firestore
        this.db = FirebaseFirestore.getInstance();

        // Armazena referência das preferências locais
        this.playerPrefs = playerPrefs;
    }

    // Método simplificado para salvar progresso sem callbacks
    public void saveProgress() {
        saveProgress(null, null);
    }

    // Salva o progresso do jogador no Firestore
    public void saveProgress(
            Runnable onSuccess,
            Runnable onFailure
    ) {

        // Obtém usuário autenticado atualmente
        FirebaseUser user =
                FirebaseAuth.getInstance()
                        .getCurrentUser();

        // Verifica se existe usuário logado
        if (user == null) {

            Log.e(
                    TAG,
                    "Usuário não logado."
            );

            if (onFailure != null) {
                onFailure.run();
            }

            return;
        }

        // Estrutura que armazenará os dados enviados ao Firestore
        Map<String, Object> data =
                new HashMap<>();

        /*
         * =========================================================
         * DADOS DO JOGADOR
         * =========================================================
         */

        data.put(
                "xp",
                playerPrefs.getXp()
        );

        data.put(
                "nickname",
                playerPrefs.getNickname()
        );

        data.put(
                "profile_image",
                playerPrefs.getProfileImage()
        );

        /*
         * =========================================================
         * MISSÕES
         * =========================================================
         */

        data.put(
                "mission1_completed",
                playerPrefs.isMission1Completed()
        );

        data.put(
                "mission2_completed",
                playerPrefs.isMission2Completed()
        );

        data.put(
                "mission3_completed",
                playerPrefs.isMission3Completed()
        );

        data.put(
                "mission4_completed",
                playerPrefs.isMission4Completed()
        );

        /*
         * =========================================================
         * ENVIO PARA O FIRESTORE
         * =========================================================
         */

        db.collection("players")
                .document(user.getUid())
                .set(data)

                // Sucesso ao salvar
                .addOnSuccessListener(unused -> {

                    Log.d(
                            TAG,
                            "Progresso salvo."
                    );

                    if (onSuccess != null) {
                        onSuccess.run();
                    }
                })

                // Erro ao salvar
                .addOnFailureListener(e -> {

                    Log.e(
                            TAG,
                            "Erro ao salvar: "
                                    + e.getMessage()
                    );

                    if (onFailure != null) {
                        onFailure.run();
                    }
                });
    }

    // Carrega o progresso salvo no Firestore
    public void loadProgress(Runnable onFinish) {

        // Obtém usuário autenticado
        FirebaseUser user =
                FirebaseAuth.getInstance()
                        .getCurrentUser();

        // Verifica se existe usuário logado
        if (user == null) {

            Log.e(
                    TAG,
                    "Usuário não logado."
            );

            if (onFinish != null) {
                onFinish.run();
            }

            return;
        }

        /*
         * =========================================================
         * BUSCA DOCUMENTO DO JOGADOR
         * =========================================================
         */

        db.collection("players")
                .document(user.getUid())
                .get()

                // Documento carregado com sucesso
                .addOnSuccessListener(document -> {

                    // Verifica se existe save salvo
                    if (document.exists()) {

                        /*
                         * =========================================================
                         * RECUPERA DADOS
                         * =========================================================
                         */

                        Long xp =
                                document.getLong("xp");

                        String nickname =
                                document.getString("nickname");

                        String profileImage =
                                document.getString("profile_image");

                        Boolean mission1 =
                                document.getBoolean("mission1_completed");

                        Boolean mission2 =
                                document.getBoolean("mission2_completed");

                        Boolean mission3 =
                                document.getBoolean("mission3_completed");

                        Boolean mission4 =
                                document.getBoolean("mission4_completed");

                        /*
                         * =========================================================
                         * SALVA LOCALMENTE
                         * =========================================================
                         */

                        if (xp != null) {
                            playerPrefs.setXp(
                                    xp.intValue()
                            );
                        }

                        if (nickname != null) {
                            playerPrefs.setNickname(
                                    nickname
                            );
                        }

                        if (profileImage != null) {
                            playerPrefs.setProfileImage(
                                    profileImage
                            );
                        }

                        if (mission1 != null) {
                            playerPrefs.setMission1Completed(
                                    mission1
                            );
                        }

                        if (mission2 != null) {
                            playerPrefs.setMission2Completed(
                                    mission2
                            );
                        }

                        if (mission3 != null) {
                            playerPrefs.setMission3Completed(
                                    mission3
                            );
                        }

                        if (mission4 != null) {
                            playerPrefs.setMission4Completed(
                                    mission4
                            );
                        }

                        Log.d(
                                TAG,
                                "Progresso carregado."
                        );

                    } else {

                        Log.d(
                                TAG,
                                "Nenhum save encontrado."
                        );
                    }

                    // Finaliza processo
                    if (onFinish != null) {
                        onFinish.run();
                    }
                })

                // Erro ao carregar
                .addOnFailureListener(e -> {

                    Log.e(
                            TAG,
                            "Erro ao carregar: "
                                    + e.getMessage()
                    );

                    if (onFinish != null) {
                        onFinish.run();
                    }
                });
    }

    // Remove completamente o progresso salvo na nuvem
    public void deleteProgress(
            Runnable onSuccess,
            Runnable onFailure
    ) {

        // Obtém usuário autenticado
        FirebaseUser user =
                FirebaseAuth.getInstance()
                        .getCurrentUser();

        // Verifica se existe usuário logado
        if (user == null) {

            Log.e(
                    TAG,
                    "Usuário não logado."
            );

            if (onFailure != null) {
                onFailure.run();
            }

            return;
        }

        /*
         * =========================================================
         * REMOVE DOCUMENTO DO FIRESTORE
         * =========================================================
         */

        db.collection("players")
                .document(user.getUid())
                .delete()

                // Exclusão realizada com sucesso
                .addOnSuccessListener(unused -> {

                    Log.d(
                            TAG,
                            "Progresso removido do banco."
                    );

                    if (onSuccess != null) {
                        onSuccess.run();
                    }
                })

                // Erro ao apagar
                .addOnFailureListener(e -> {

                    Log.e(
                            TAG,
                            "Erro ao apagar progresso: "
                                    + e.getMessage()
                    );

                    if (onFailure != null) {
                        onFailure.run();
                    }
                });
    }
}