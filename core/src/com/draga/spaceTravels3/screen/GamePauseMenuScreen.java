package com.draga.spaceTravels3.screen;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.draga.spaceTravels3.Level;
import com.draga.spaceTravels3.manager.ScreenManager;
import com.draga.spaceTravels3.manager.UIManager;
import com.draga.spaceTravels3.ui.BeepingImageTextButton;
import com.draga.spaceTravels3.ui.Screen;

public class GamePauseMenuScreen extends IngameMenuScreen
{
    public GamePauseMenuScreen(Level level, Screen gameScreen)
    {
        super(gameScreen, level);

        BeepingImageTextButton
            button = new BeepingImageTextButton("Resume", UIManager.skin, "play");
        button.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                ScreenManager.removeScreen(GamePauseMenuScreen.this);
            }
        });

        this.centreCell.setActor(button);
    }
}
