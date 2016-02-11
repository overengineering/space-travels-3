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
        init();
    }

    public BeepingImageTextButton(
        String text,
        ImageTextButtonStyle style)
    {
        super(text, style);
        init();
    }

    public BeepingImageTextButton(String text, Skin skin)
    {
        super(text, skin);
        init();
    }

    private void init()
    {
        this.addListener(BeepingClickListener.BEEPING_CLICK_LISTENER);
        getImageCell().fill().expand();
    }
}
