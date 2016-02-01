package com.draga.spaceTravels3.screen;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.draga.spaceTravels3.level.Level;
import com.draga.spaceTravels3.manager.SettingsManager;
import com.draga.spaceTravels3.manager.UIManager;
import com.draga.spaceTravels3.manager.asset.AssMan;
import com.draga.spaceTravels3.ui.Screen;

public class LoseScreen extends IngameMenuScreen
{
    private final Sound sound;

    public LoseScreen(Level level, Screen gameScreen)
    {
        super(gameScreen, level);

        this.sound = AssMan.getGameAssMan().get(AssMan.getAssList().loseSound);
        this.sound.play(SettingsManager.getSettings().volumeFX);

        this.centreCell.setActor(new Label("You lose!", UIManager.skin));
    }

    @Override
    public void dispose()
    {
        this.sound.stop();
        this.sound.dispose();
        super.dispose();
    }
}
