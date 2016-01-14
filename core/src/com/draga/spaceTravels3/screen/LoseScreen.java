package com.draga.spaceTravels3.screen;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.draga.spaceTravels3.Constants;
import com.draga.spaceTravels3.Level;
import com.draga.spaceTravels3.manager.SettingsManager;
import com.draga.spaceTravels3.manager.UIManager;
import com.draga.spaceTravels3.manager.asset.AssMan;
import com.draga.spaceTravels3.ui.Screen;

public class LoseScreen extends IngameMenuScreen
{
    private final Sound sound;
    private final Level level;
    private       Image headerImage;

    public LoseScreen(Level level, Screen gameScreen)
    {
        super(true, false, gameScreen, level);

        this.level = level;

        this.sound = AssMan.getGameAssMan().get(AssMan.getAssList().loseSound);
        this.sound.play(SettingsManager.getSettings().volumeFX);

        Table table = UIManager.addDefaultTableToStage(this.stage);

        table.setBackground(UIManager.getTiledDrawable(Constants.Visual.SCREEN_FADE_COLOUR));
        table.addAction(Actions.sequence(
            Actions.fadeOut(0),
            Actions.fadeIn(Constants.Visual.SCREEN_FADE_DURATION, Interpolation.pow2In)));

        table.add(getHeaderLabel());
        table.row();

        table
            .add()
            .expand();
        table.row();

        table.add("You lost!");
        table.row();

        // Retry button.
        TextButton retryButton = getRetryButton();
        table
            .add(retryButton);
        table.row();

        // Main menu button.
        TextButton mainMenuTextButton = getMainMenuTextButton();
        table.add(mainMenuTextButton);
        table.row();

        table
            .add()
            .expand();

        this.stage.setDebugAll(SettingsManager.getDebugSettings().debugDraw);
    }

    @Override
    public void dispose()
    {
        this.sound.stop();
        this.sound.dispose();
        super.dispose();
    }
}
