package com.draga.spaceTravels3.screen;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.draga.spaceTravels3.event.LoseEvent;
import com.draga.spaceTravels3.level.Level;
import com.draga.spaceTravels3.manager.SettingsManager;
import com.draga.spaceTravels3.manager.UIManager;
import com.draga.spaceTravels3.manager.asset.AssMan;
import com.draga.spaceTravels3.ui.Screen;

public class LoseScreen extends IngameMenuScreen
{
    private final Sound sound;

    public LoseScreen(Level level, Screen gameScreen, LoseEvent loseEvent)
    {
        super(gameScreen, level);

        this.sound = AssMan.getGameAssMan().get(AssMan.getAssList().loseSound);
        this.sound.play(SettingsManager.getSettings().volumeFX);

        Table table = UIManager.getDefaultTable();

        table
            .add("LOST", "large")
            .center()
            .row();

        String message = loseEvent.isPlanetIsDestination()
            ? "You landed too fast!"
            : "You landed on the wrong planet!";
        table
            .add(message)
            .center();

        this.centreCell.setActor(table);
    }

    @Override
    public void dispose()
    {
        this.sound.stop();
        super.dispose();
    }
}
