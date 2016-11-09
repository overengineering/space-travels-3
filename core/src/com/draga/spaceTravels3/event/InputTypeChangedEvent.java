package com.draga.spaceTravels3.event;

import com.draga.spaceTravels3.InputType;

public class InputTypeChangedEvent
{
    public InputType newInputType;

    public InputTypeChangedEvent(InputType newInputType)
    {
        this.newInputType = newInputType;
    }
}
