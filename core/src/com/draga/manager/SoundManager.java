package com.draga.manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.draga.manager.asset.AssMan;

public abstract class SoundManager
{
    public static Sound buttonSound;

    public static void create()
    {
        buttonSound =
            Gdx.audio.newSound(Gdx.files.internal(AssMan.getAssList().buttonSound));
    }

    public static void dispose()
    {
        buttonSound.dispose();
    }
}
