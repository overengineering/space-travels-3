package com.draga.spaceTravels3.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

/**
 * A a {@link TextButton} with a {@link BeepingClickListener} attached to it
 */
public class BeepingTextButton extends TextButton
{
    public BeepingTextButton(String text, Skin skin, String styleName)
    {
        super(text, skin, styleName);
        this.addListener(BeepingClickListener.BEEPING_CLICK_LISTENER);
    }

    public BeepingTextButton(
        String text,
        TextButtonStyle style)
    {
        super(text, style);
        this.addListener(BeepingClickListener.BEEPING_CLICK_LISTENER);
    }

    public BeepingTextButton(String text, Skin skin)
    {
        super(text, skin);
        this.addListener(BeepingClickListener.BEEPING_CLICK_LISTENER);
    }
}
