package com.darknetprotocol.utils;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class CloudSaveManager {

    private static final String TAG = "CloudSave";

    private final FirebaseFirestore db;
    private final PlayerPrefs playerPrefs;

    public CloudSaveManager(PlayerPrefs playerPrefs) {
        this.db = FirebaseFirestore.getInstance();
        this.playerPrefs = playerPrefs;
    }

    public void saveProgress() {
        saveProgress(null, null);
    }

    public void saveProgress(Runnable onSuccess, Runnable onFailure) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user == null) {
            Log.e(TAG, "Usuário não logado.");

            if (onFailure != null) {
                onFailure.run();
            }

            return;
        }

        Map<String, Object> data = new HashMap<>();

        data.put("xp", playerPrefs.getXp());
        data.put("nickname", playerPrefs.getNickname());
        data.put("profile_image", playerPrefs.getProfileImage());

        data.put("mission1_completed", playerPrefs.isMission1Completed());
        data.put("mission2_completed", playerPrefs.isMission2Completed());
        data.put("mission3_completed", playerPrefs.isMission3Completed());
        data.put("mission4_completed", playerPrefs.isMission4Completed());

        db.collection("players")
                .document(user.getUid())
                .set(data)
                .addOnSuccessListener(unused -> {
                    Log.d(TAG, "Progresso salvo.");

                    if (onSuccess != null) {
                        onSuccess.run();
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Erro ao salvar: " + e.getMessage());

                    if (onFailure != null) {
                        onFailure.run();
                    }
                });
    }

    public void loadProgress(Runnable onFinish) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user == null) {
            Log.e(TAG, "Usuário não logado.");

            if (onFinish != null) {
                onFinish.run();
            }

            return;
        }

        db.collection("players")
                .document(user.getUid())
                .get()
                .addOnSuccessListener(document -> {

                    if (document.exists()) {
                        Long xp = document.getLong("xp");
                        String nickname = document.getString("nickname");
                        String profileImage = document.getString("profile_image");

                        Boolean mission1 = document.getBoolean("mission1_completed");
                        Boolean mission2 = document.getBoolean("mission2_completed");
                        Boolean mission3 = document.getBoolean("mission3_completed");
                        Boolean mission4 = document.getBoolean("mission4_completed");

                        if (xp != null) {
                            playerPrefs.setXp(xp.intValue());
                        }

                        if (nickname != null) {
                            playerPrefs.setNickname(nickname);
                        }

                        if (profileImage != null) {
                            playerPrefs.setProfileImage(profileImage);
                        }

                        if (mission1 != null) {
                            playerPrefs.setMission1Completed(mission1);
                        }

                        if (mission2 != null) {
                            playerPrefs.setMission2Completed(mission2);
                        }

                        if (mission3 != null) {
                            playerPrefs.setMission3Completed(mission3);
                        }

                        if (mission4 != null) {
                            playerPrefs.setMission4Completed(mission4);
                        }

                        Log.d(TAG, "Progresso carregado.");
                    } else {
                        Log.d(TAG, "Nenhum save encontrado.");
                    }

                    if (onFinish != null) {
                        onFinish.run();
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Erro ao carregar: " + e.getMessage());

                    if (onFinish != null) {
                        onFinish.run();
                    }
                });
    }

    public void deleteProgress(Runnable onSuccess, Runnable onFailure) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user == null) {
            Log.e(TAG, "Usuário não logado.");

            if (onFailure != null) {
                onFailure.run();
            }

            return;
        }

        db.collection("players")
                .document(user.getUid())
                .delete()
                .addOnSuccessListener(unused -> {
                    Log.d(TAG, "Progresso removido do banco.");

                    if (onSuccess != null) {
                        onSuccess.run();
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Erro ao apagar progresso: " + e.getMessage());

                    if (onFailure != null) {
                        onFailure.run();
                    }
                });
    }
}