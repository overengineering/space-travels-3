package com.draga;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.draga.manager.GameManager;
import com.draga.manager.screen.MenuScreen;

public class GameScreenInputProcessor extends InputAdapter
{
    @Override
    public boolean keyUp(int keycode)
    {
        if (keycode == Input.Keys.BACK)
        {
            GameManager.getGame().setScreen(new MenuScreen());
            return true;
        }

        return false;
    }
}
