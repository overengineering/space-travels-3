package com.draga.spaceTravels3.manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.Array;
import com.draga.spaceTravels3.manager.asset.AssMan;

/**
 * App sounds that can stay in memory.
 */
public abstract class SoundManager
{
    public static Sound buttonSound;

    public static void create()
    {
        buttonSound =
            Gdx.audio.newSound(Gdx.files.internal(AssMan.getAssList().buttonSound));
    }

    public static void pauseGameSound()
    {
        for (Sound sound : AssMan.getGameAssMan().getAll(Sound.class, new Array<Sound>()))
        {
            sound.pause();
        }
    }

    public static void resumeGameSound()
    {
        for (Sound sound : AssMan.getGameAssMan().getAll(Sound.class, new Array<Sound>()))
        {
            sound.resume();
        }
    }

    public static void dispose()
    {
        buttonSound.dispose();
    }
}
