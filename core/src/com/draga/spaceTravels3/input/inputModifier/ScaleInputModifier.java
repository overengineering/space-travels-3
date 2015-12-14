package com.draga.spaceTravels3.input.inputModifier;

import com.badlogic.gdx.math.Vector2;

public class ScaleInputModifier implements InputModifier
{
    private final float maxRawInput;

    public ScaleInputModifier(float maxRawInput)
    {
        this.maxRawInput = maxRawInput;
    }

    @Override
    public void modify(Vector2 vector2)
    {
        // Scale the input by the maxRawInput so that I'll be between 0 and 1.
        vector2.scl(1 / maxRawInput);
    }
}
