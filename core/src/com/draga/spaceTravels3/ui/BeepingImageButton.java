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
        init();
    }

    private void init()
    {
        this.addListener(BeepingClickListener.BEEPING_CLICK_LISTENER);
        getImageCell().fill().expand();
    }

    public BeepingImageButton(Skin skin, String styleName)
    {
        super(skin, styleName);
        init();
    }

    public BeepingImageButton(ImageButtonStyle style)
    {
        super(style);
        init();
    }

    public BeepingImageButton(Drawable imageUp)
    {
        super(imageUp);
        init();
    }

    public BeepingImageButton(Drawable imageUp, Drawable imageDown)
    {
        super(imageUp, imageDown);
        init();
    }

    public BeepingImageButton(
        Drawable imageUp,
        Drawable imageDown,
        Drawable imageChecked)
    {
        super(imageUp, imageDown, imageChecked);
        init();
    }
}
