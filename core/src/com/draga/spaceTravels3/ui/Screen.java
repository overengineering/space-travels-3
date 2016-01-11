package com.draga.spaceTravels3.ui;

public abstract class Screen implements com.badlogic.gdx.Screen
{
    private final boolean blockable;
    private final boolean blockParents;

    public Screen(boolean blockable, boolean blockParents)
    {
        this.blockable = blockable;
        this.blockParents = blockParents;
    }

    public boolean isBlockable()
    {
        return this.blockable;
    }

    public boolean blockParents()
    {
        return this.blockParents;
    }
}
