package com.draga.spaceTravels3.input.inputModifier;

import com.draga.PooledVector2;

public class RangeInputModifier implements InputModifier
{
    private final float maxRawInput;

    public RangeInputModifier(float maxRawInput)
    {
        this.maxRawInput = maxRawInput;
    }

    @Override
    public void modify(PooledVector2 pooledVector2)
    {
        // Scale the input by the maxRawInput so that I'll be between 0 and 1.
        pooledVector2.scl(1 / maxRawInput);
    }
}
