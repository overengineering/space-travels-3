package com.draga.spaceTravels3.screen;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.draga.spaceTravels3.Score;
import com.draga.spaceTravels3.level.Level;
import com.draga.spaceTravels3.manager.SettingsManager;
import com.draga.spaceTravels3.manager.UIManager;
import com.draga.spaceTravels3.manager.asset.AssMan;
import com.draga.spaceTravels3.ui.Screen;

public class WinScreen extends IngameMenuScreen
{
    protected final Sound sound;

    public WinScreen(Level level, Screen gameScreen, Score score, Integer previousBestScore)
    {
        super(gameScreen, level);

        this.sound = AssMan.getGameAssMan().get(AssMan.getAssList().winSound);
        this.sound.play(SettingsManager.getSettings().volumeFX);

        Table table = new Table(UIManager.skin);

        table.add("WIN", "large");
        table.row();

        // Best score.
        table.row();
        Label newBestScoreLabel = getBestScoreLabel(score.getTotalScore(), previousBestScore);
        table.add(newBestScoreLabel);

        // Current score.
        Table reportTable = getScoreReportTable(score);
        table.row();
        table.add(reportTable);

        this.centreCell.setActor(table);
    }

    private Label getBestScoreLabel(int score, Integer previousBestScore)
    {
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
