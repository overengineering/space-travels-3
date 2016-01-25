package com.draga.spaceTravels3.ui;

import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

/**
 * A a {@link ImageButton} with a {@link BeepingClickListener} attached to it
 */
public class BeepingImageButton extends ImageButton
{
    public BeepingImageButton(Skin skin)
    {
        super(skin);
        this.addListener(BeepingClickListener.BEEPING_CLICK_LISTENER);
    }

    public BeepingImageButton(Skin skin, String styleName)
    {
        super(skin, styleName);
        this.addListener(BeepingClickListener.BEEPING_CLICK_LISTENER);
    }

    public BeepingImageButton(ImageButtonStyle style)
    {
        super(style);
        this.addListener(BeepingClickListener.BEEPING_CLICK_LISTENER);
    }

    public BeepingImageButton(Drawable imageUp)
    {
        super(imageUp);
        this.addListener(BeepingClickListener.BEEPING_CLICK_LISTENER);
    }

    public BeepingImageButton(Drawable imageUp, Drawable imageDown)
    {
        super(imageUp, imageDown);
        this.addListener(BeepingClickListener.BEEPING_CLICK_LISTENER);
    }

    public BeepingImageButton(
        Drawable imageUp,
        Drawable imageDown,
        Drawable imageChecked)
    {
        super(imageUp, imageDown, imageChecked);
        this.addListener(BeepingClickListener.BEEPING_CLICK_LISTENER);
    }
}
