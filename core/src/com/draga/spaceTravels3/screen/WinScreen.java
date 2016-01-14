package com.draga.spaceTravels3.screen;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Pools;
import com.draga.spaceTravels3.Constants;
import com.draga.spaceTravels3.Level;
import com.draga.spaceTravels3.Score;
import com.draga.spaceTravels3.manager.ScoreManager;
import com.draga.spaceTravels3.manager.SettingsManager;
import com.draga.spaceTravels3.manager.UIManager;
import com.draga.spaceTravels3.manager.asset.AssMan;
import com.draga.spaceTravels3.ui.Screen;

public class WinScreen extends IngameMenuScreen
{
    protected final Sound sound;

    public WinScreen(Level level, Screen gameScreen)
    {
        super(true, false, gameScreen, level);

        Score score = level.getScore();

        this.sound = AssMan.getGameAssMan().get(AssMan.getAssList().winSound);
        this.sound.play(SettingsManager.getSettings().volumeFX);

        Table table = UIManager.addDefaultTableToStage(this.stage);

        table.setBackground(UIManager.getTiledDrawable(Constants.Visual.SCREEN_FADE_COLOUR));
        table.addAction(Actions.sequence(
            Actions.fadeOut(0),
            Actions.fadeIn(Constants.Visual.SCREEN_FADE_DURATION, Interpolation.pow2In)));

        // Header label.
        Actor headerLabel = getHeaderLabel();
        table.add(headerLabel);
        table.row();

        // Gap between header and rest.
        table
            .add()
            .expand();
        table.row();

        table.add("You won!");
        table.row();

        // Best score.
        table.row();
        Label newBestScoreLabel = getBestScoreLabel(score.getTotalScore());
        table.add(newBestScoreLabel);

        // Current score.
        Table reportTable = getScoreReportTable(score);
        table.row();
        table.add(reportTable);

        // Retry button.
        table.row();
        TextButton retryButton = getRetryButton();
        table.add(retryButton);

        // Main menu button.
        TextButton mainMenuTextButton = getMainMenuTextButton();
        table.row();
        table.add(mainMenuTextButton);
        table.row();

        // Gap between the centre and the end of the screen.
        table
            .add()
            .expand();

        this.stage.setDebugAll(SettingsManager.getDebugSettings().debugDraw);

        Pools.free(score);
    }

    private Label getBestScoreLabel(int score)
    {
        Integer previousBestScore =
            ScoreManager.getScore(this.level.getId(), this.level.getDifficulty());

        Label.LabelStyle labelStyle = UIManager.skin.get(Label.LabelStyle.class);

        String text;
        if (previousBestScore == null)
        {
            text = "New score!";
        }
        else if (score > previousBestScore)
        {
            text = "New best score! It was: " + previousBestScore;
        }
        else
        {
            text = "Best score: " + previousBestScore;
        }

        Label newBestScoreLabel =
            new Label(text, labelStyle);

        return newBestScoreLabel;
    }

    private Table getScoreReportTable(Score score)
    {
        Table table = new Table();

        table
            .add(new Label("Pickup points: ", UIManager.skin))
            .right();
        table
            .add(new Label(String.valueOf(score.getPickupPoints()), UIManager.skin))
            .right();

        table.row();

        table
            .add(new Label("Fuel points: ", UIManager.skin))
            .right();
        table
            .add(new Label(String.valueOf(score.getFuelPoints()), UIManager.skin))
            .right();

        table.row();

        table
            .add(new Label("Time points: ", UIManager.skin))
            .right();
        table
            .add(new Label(String.valueOf(score.getTimePoints()), UIManager.skin))
            .right();

        table.row();

        table
            .add(new Label("Total score: ", UIManager.skin))
            .right();
        table
            .add(new Label(String.valueOf(score.getTotalScore()), UIManager.skin))
            .right();

        return table;
    }

    @Override
    public void dispose()
    {
        this.sound.stop();
        this.sound.dispose();
        super.dispose();
    }
}
