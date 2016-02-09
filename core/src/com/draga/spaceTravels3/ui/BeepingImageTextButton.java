package com.draga.spaceTravels3.ui;

import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

/**
 * A a {@link ImageTextButton} with a {@link BeepingClickListener} attached to it
 */
public class BeepingImageTextButton extends ImageTextButton
{
    public BeepingImageTextButton(String text, Skin skin, String styleName)
    {
        super(text, skin, styleName);
        this.addListener(BeepingClickListener.BEEPING_CLICK_LISTENER);
    }

    public BeepingImageTextButton(
        String text,
        ImageTextButtonStyle style)
    {
        super(text, style);
        this.addListener(BeepingClickListener.BEEPING_CLICK_LISTENER);
    }

    public BeepingImageTextButton(String text, Skin skin)
    {
        super(text, skin);
        this.addListener(BeepingClickListener.BEEPING_CLICK_LISTENER);
    }
}
