package com.draga;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.draga.manager.SettingsManager;
import com.draga.manager.SoundManager;

/**
 * Provides a {@link ClickListener} that beeps when clicked. Do not instantiate,
 * use the constant provided instead.
 */
public final class BeepingClickListener extends ClickListener
{
    /**
     * A {@link ClickListener} that beeps.
     */
    public static final ClickListener BEEPING_CLICK_LISTENER = new ClickListener()
    {
        @Override
        public void clicked(InputEvent event, float x, float y)
        {
            SoundManager.buttonSound.play(SettingsManager.getSettings().volume);
        }
    };

    private BeepingClickListener()
    {
    }
}
