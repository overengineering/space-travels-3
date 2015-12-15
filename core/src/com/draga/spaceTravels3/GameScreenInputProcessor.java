package com.draga.spaceTravels3;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.draga.spaceTravels3.screen.MenuScreen;

public class GameScreenInputProcessor extends InputAdapter
{
    @Override
    public boolean keyUp(int keycode)
    {
        if (keycode == Input.Keys.BACK)
        {
            SpaceTravels3.getGame().setScreen(new MenuScreen());
            return true;
        }

        return false;
    }
}
