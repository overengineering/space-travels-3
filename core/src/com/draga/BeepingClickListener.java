package com.draga;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.draga.manager.SettingsManager;
import com.draga.manager.SoundManager;

public class BeepingClickListener extends ClickListener
{
    @Override
    public void clicked(InputEvent event, float x, float y)
    {
        SoundManager.buttonSound.play(SettingsManager.getSettings().volume);
        super.clicked(event, x, y);
    }
}
