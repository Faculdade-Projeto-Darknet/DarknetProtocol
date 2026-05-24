package com.darknetprotocol;

import android.content.Context;
import android.media.MediaPlayer;

public class SoundManager {


    public static void playSound(Context context, int soundResourceId) {
        try {
            MediaPlayer mediaPlayer = MediaPlayer.create(context, soundResourceId);
            if (mediaPlayer != null) {
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        // Libera a memória imediatamente após o som terminar
                        mp.release();
                    }
                });
                mediaPlayer.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}