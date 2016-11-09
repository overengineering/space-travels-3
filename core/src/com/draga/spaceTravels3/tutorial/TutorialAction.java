package com.draga.spaceTravels3.tutorial;

import com.badlogic.gdx.scenes.scene2d.Action;

public abstract class TutorialAction extends Action
{
    @Override
    public boolean act(float delta)
    {
        if (isTriggered())
        {
            onTriggered();
            return true;
        }
        else
        {
            return false;
        }
    }

    protected abstract boolean isTriggered();

    protected abstract void onTriggered();
}
