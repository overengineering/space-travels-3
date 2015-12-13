package com.draga.spaceTravels3.manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.draga.spaceTravels3.manager.asset.AssMan;

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
