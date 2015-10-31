package com.draga.manager.screen;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.draga.manager.ScreenManager;

public class GameScreenInputProcessor extends InputAdapter
{
    @Override
    public boolean keyUp(int keycode)
    {
        if (keycode == Input.Keys.BACK)
        {
            ScreenManager.setActiveScreen(new MenuScreen());
            return true;
        }

        return false;
    }
}
