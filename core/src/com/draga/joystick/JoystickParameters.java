package com.draga.joystick;

import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.graphics.Color;

public class JoystickParameters extends AssetLoaderParameters<Joystick>
{
    public Color color;
    public float size;
    public float strokeSize;
    public float deadZone;

    public JoystickParameters(Color color, float size, float strokeSize, float deadZone)
    {
        this.color = color;
        this.size = size;
        this.strokeSize = strokeSize;
        this.deadZone = deadZone;
    }

    @Override
    public String toString()
    {
        return String.format("%s-%s-%s-%s", this.color, this.size, this.strokeSize, this.deadZone);
    }
}
