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
        Label newBestScoreLabel = getBestScoreLabel(score.getTotalScore(), previousBestScore);
        table.add(newBestScoreLabel);
        table.row();

        // Current score.
        Table reportTable = getScoreReportTable(score);
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
        Table table = UIManager.getDefaultTable();
        table.pad(0);

        table.add("pickup");
        table.add("fuel");
        table.add("time");
        table.add("total");
        table.row();

        table.add(String.valueOf(score.getPickupPoints()));
        table.add(String.valueOf(score.getFuelPoints()));
        table.add(String.valueOf(score.getTimePoints()));
        table.add(String.valueOf(score.getTotalScore()));

        return table;
    }

    @Override
    public void dispose()
    {
        this.sound.stop();
        super.dispose();
    }
}
