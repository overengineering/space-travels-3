package com.draga.joystick;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;

public class Joystick extends Texture
{
    private static final String LOGGING_TAG = Joystick.class.getSimpleName();

    public Joystick(Pixmap pixmap)
    {
        super(pixmap);
    }
}
