package com.draga.spaceTravels3.input.inputProvider;

import com.badlogic.gdx.math.Vector2;
import com.draga.spaceTravels3.input.inputModifier.InputModifier;

import java.util.ArrayList;

/**
 * Provides a standardised input between 0 and 1 in length.
 */
public abstract class InputProvider
{
    private ArrayList<InputModifier> inputModifiers = new ArrayList<>();

    /**
     * Applies all the input modifiers to the raw input and the max the vector to 1.
     * @return a standardised input between 0 and 1.
     */
    public Vector2 getInput()
    {
        Vector2 rawInput = getRawInput();

        for (InputModifier inputModifier : inputModifiers)
        {
            inputModifier.modify(rawInput);
        }

        rawInput.limit(1f);

        return rawInput;
    }

    protected abstract Vector2 getRawInput();

    protected void addInputModifier(InputModifier inputModifier)
    {
        inputModifiers.add(inputModifier);
    }
}