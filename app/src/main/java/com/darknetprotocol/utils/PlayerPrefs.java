package com.darknetprotocol.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class PlayerPrefs {

    private static final String PREF_NAME = "player_data";

    private static final String KEY_XP = "xp_total";

    private static final String KEY_MISSION_1 = "mission1_completed";
    private static final String KEY_MISSION_2 = "mission2_completed";
    private static final String KEY_MISSION_3 = "mission3_completed";
    private static final String KEY_MISSION_4 = "mission4_completed";

    private static final String KEY_NICKNAME = "nickname";
    private static final String KEY_PROFILE_IMAGE = "profile_image";

    private final SharedPreferences preferences;

    public PlayerPrefs(Context context) {
        preferences = context.getSharedPreferences(
                PREF_NAME,
                Context.MODE_PRIVATE
        );
    }

    public int getXp() {
        return preferences.getInt(
                KEY_XP,
                0
        );
    }

    public void setXp(int xp) {
        preferences.edit()
                .putInt(
                        KEY_XP,
                        Math.max(xp, 0)
                )
                .apply();
    }

    public void addXp(int amount) {
        int currentXp = getXp();

        preferences.edit()
                .putInt(
                        KEY_XP,
                        Math.max(currentXp + amount, 0)
                )
                .apply();
    }

    public boolean isMission1Completed() {
        return preferences.getBoolean(
                KEY_MISSION_1,
                false
        );
    }

    public void setMission1Completed(boolean completed) {
        preferences.edit()
                .putBoolean(
                        KEY_MISSION_1,
                        completed
                )
                .apply();
    }

    public boolean isMission2Completed() {
        return preferences.getBoolean(
                KEY_MISSION_2,
                false
        );
    }

    public void setMission2Completed(boolean completed) {
        preferences.edit()
                .putBoolean(
                        KEY_MISSION_2,
                        completed
                )
                .apply();
    }

    public boolean isMission3Completed() {
        return preferences.getBoolean(
                KEY_MISSION_3,
                false
        );
    }

    public void setMission3Completed(boolean completed) {
        preferences.edit()
                .putBoolean(
                        KEY_MISSION_3,
                        completed
                )
                .apply();
    }

    public boolean isMission4Completed() {
        return preferences.getBoolean(
                KEY_MISSION_4,
                false
        );
    }

    public void setMission4Completed(boolean completed) {
        preferences.edit()
                .putBoolean(
                        KEY_MISSION_4,
                        completed
                )
                .apply();
    }

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

    public String getNickname() {
        return preferences.getString(
                KEY_NICKNAME,
                "Ghost_404"
        );
    }

    public void setNickname(String nickname) {
        preferences.edit()
                .putString(
                        KEY_NICKNAME,
                        nickname
                )
                .apply();
    }

    public String getProfileImage() {
        return preferences.getString(
                KEY_PROFILE_IMAGE,
                null
        );
    }

    public void setProfileImage(String imageUri) {
        preferences.edit()
                .putString(
                        KEY_PROFILE_IMAGE,
                        imageUri
                )
                .apply();
    }

    public void resetProgressOnly() {
        preferences.edit()
                .putInt(KEY_XP, 0)
                .putBoolean(KEY_MISSION_1, false)
                .putBoolean(KEY_MISSION_2, false)
                .putBoolean(KEY_MISSION_3, false)
                .putBoolean(KEY_MISSION_4, false)
                .apply();
    }

    public void clearAll() {
        preferences.edit()
                .clear()
                .apply();
    }
}