package com.darknetprotocol.utils;

// Importa Context para acessar recursos do aplicativo
import android.content.Context;

// Classe responsável por reproduzir sons
import android.media.MediaPlayer;

// Classe utilitária para tocar efeitos sonoros do jogo
public class SoundManager {

    // Método estático utilizado para reproduzir qualquer som do projeto
    public static void playSound(
            Context context,
            int soundResourceId
    ) {

        try {

            // Cria um MediaPlayer utilizando o arquivo de áudio informado
            MediaPlayer mediaPlayer =
                    MediaPlayer.create(
                            context,
                            soundResourceId
                    );

            // Verifica se o áudio foi carregado corretamente
            if (mediaPlayer != null) {

                // Evento executado quando o som termina
                mediaPlayer.setOnCompletionListener(
                        new MediaPlayer.OnCompletionListener() {

                            @Override
                            public void onCompletion(
                                    MediaPlayer mp
                            ) {

                                // Libera a memória utilizada pelo áudio
                                mp.release();
                            }
                        }
                );

                // Inicia a reprodução do som
                mediaPlayer.start();
            }

        } catch (Exception e) {

            // Exibe erro no Logcat caso algo falhe
            e.printStackTrace();
        }
    }
}