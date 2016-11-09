package com.draga.spaceTravels3.manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.MathUtils;
import com.draga.errorHandler.ErrorHandlerProvider;
import com.draga.spaceTravels3.manager.asset.AssMan;

import java.util.ArrayList;

public abstract class MusicManager
{
    private static final String LOGGING_TAG = MusicManager.class.getSimpleName();

    private static ArrayList<FileHandle> musicFileHandles;
    private static Music                 currentMusic;
    private static FileHandle            lastPlayedMusicFileHandle;

    public static final Music.OnCompletionListener ON_COMPLETION_LISTENER_PLAY_RANDOM_MUSIC =
        new Music.OnCompletionListener()
        {
            @Override
            public void onCompletion(Music music)
            {
                playRandomMusic();
            }
        };

    private MusicManager()
    {
    }

    public static void create()
    {
        ArrayList<String> musicPaths = AssMan.getAssList().musics;

        // If less than 2 music files found it won't be possible to play a random music excluding
        // the last one played.
        if (musicPaths.size() < 2)
        {
            ErrorHandlerProvider.handle(LOGGING_TAG, "Less than 2 music files!");
        }

        musicFileHandles = new ArrayList<>(musicPaths.size());

        for (String musicPath : musicPaths)
        {
            FileHandle musicFileHandle = Gdx.files.internal(musicPath);
            musicFileHandles.add(musicFileHandle);
        }
    }

    public static void dispose()
    {
        if (currentMusic != null)
        {
            currentMusic.stop();
            currentMusic.dispose();
        }
    }

    public static void playRandomMusic()
    {
        // Find the next random file handle, excluding the last played if any.
        FileHandle nextMusicFileHandle;
        do
        {
            int randomMusicIndex = MathUtils.random(musicFileHandles.size() - 1);
            nextMusicFileHandle = musicFileHandles.get(randomMusicIndex);
        } while (lastPlayedMusicFileHandle != null
            && !lastPlayedMusicFileHandle.equals(nextMusicFileHandle));
        lastPlayedMusicFileHandle = nextMusicFileHandle;

        if (currentMusic != null && currentMusic.isPlaying())
        {
            currentMusic.stop();
            currentMusic.dispose();
        }

        currentMusic = Gdx.audio.newMusic(nextMusicFileHandle);
        currentMusic.setVolume(SettingsManager.getSettings().getVolumeMusic());
        currentMusic.play();
        currentMusic.setOnCompletionListener(ON_COMPLETION_LISTENER_PLAY_RANDOM_MUSIC);
    }

    public static void changeVolume(float volume)
    {
        if (currentMusic != null)
        {
            currentMusic.setVolume(volume);
        }
    }
}
