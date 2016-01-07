package com.draga.spaceTravels3.manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.MathUtils;
import com.draga.spaceTravels3.manager.asset.AssMan;

import java.util.ArrayList;

public abstract class MusicManager
{
    private static ArrayList<FileHandle> musicFileHandles;
    private static Music                 currentMusic;

    private MusicManager()
    {
    }

    public static void create()
    {
        ArrayList<String> musicPaths = AssMan.getAssList().musics;

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
        int randomMusicIndex = MathUtils.random(musicFileHandles.size() - 1);

        if (currentMusic != null && currentMusic.isPlaying())
        {
            currentMusic.stop();
            currentMusic.dispose();
        }

        currentMusic = Gdx.audio.newMusic(musicFileHandles.get(randomMusicIndex));
        currentMusic.setVolume(SettingsManager.getSettings().getVolumeMusic());
        currentMusic.play();
        currentMusic.setOnCompletionListener(new Music.OnCompletionListener()
        {
            @Override
            public void onCompletion(Music music)
            {
                // TODO: avoid playing last music.
                playRandomMusic();
            }
        });
    }

    public static void changeVolume(float volume)
    {
        if (currentMusic != null)
        {
            currentMusic.setVolume(volume);
        }
    }
}
