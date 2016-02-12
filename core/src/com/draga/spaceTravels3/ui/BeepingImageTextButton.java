package com.draga.spaceTravels3.ui;

import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Scaling;
import com.draga.spaceTravels3.Constants;

/**
 * A a {@link ImageTextButton} with a {@link BeepingClickListener} attached to it.
 */
public class BeepingImageTextButton extends ImageTextButton
{
    public BeepingImageTextButton(String text, Skin skin, String styleName)
    {
        super(text, skin, styleName);
        init();
    }

    private void init()
    {
        this.addListener(BeepingClickListener.BEEPING_CLICK_LISTENER);
        getImage().setScaling(Scaling.fill);
        getImageCell().size(Constants.Visual.UI.BUTTON_IMAGE_SIZE);
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
}
