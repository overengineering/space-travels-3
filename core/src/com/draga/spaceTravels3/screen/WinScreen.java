package com.draga.spaceTravels3.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.draga.spaceTravels3.Constants;
import com.draga.spaceTravels3.Score;
import com.draga.spaceTravels3.SpaceTravels3;
import com.draga.spaceTravels3.manager.ScoreManager;
import com.draga.spaceTravels3.manager.SettingsManager;
import com.draga.spaceTravels3.manager.UIManager;
import com.draga.spaceTravels3.manager.asset.AssMan;
import com.draga.spaceTravels3.manager.level.LevelManager;
import com.draga.spaceTravels3.manager.level.serialisableEntities.SerialisableLevel;
import com.draga.spaceTravels3.ui.BeepingTextButton;

public class WinScreen implements Screen
{
    private final Stage  stage;
    private final Sound  sound;
    private final String levelId;

    public WinScreen(String levelId, Score score)
    {
        this.sound = AssMan.getAssMan().get(AssMan.getAssList().winSound);
        this.sound.play(SettingsManager.getSettings().volumeFX);

        this.levelId = levelId;
        this.stage = new Stage();
        Gdx.input.setInputProcessor(this.stage);

        int previousBestScore = ScoreManager.getScore(levelId);

        Table table = UIManager.addDefaultTableToStage(this.stage);
        table.setBackground(UIManager.skin.newDrawable(
            "background",
            Constants.Visual.SCREEN_FADE_COLOUR));
        table.addAction(Actions.sequence(
            Actions.fadeOut(0),
            Actions.fadeIn(Constants.Visual.SCREEN_FADE_DURATION, Interpolation.pow2In)));

        // Header label.
        Label headerLabel = getHeaderLabel();
        table.add(headerLabel);

        // Best score.
        ScoreManager.saveHighScore(levelId, score.getTotalScore());
        table.row();
        Label newBestScoreLabel = getBestScoreLabel(score.getTotalScore(), previousBestScore);
        table.add(newBestScoreLabel);

        // Current score.
        Table reportTable = getScoreReportTable(score);
        table.row();
        table.add(reportTable);

        // Retry button.
        table.row();
        TextButton retryButton = getRetryButton();
        table.add(retryButton);

        // Next level button.
        SerialisableLevel nextLevel = LevelManager.getNextLevel(levelId);
        if (nextLevel != null)
        {
            String nextLevelId = nextLevel.id;
            TextButton nextTextButton = getNextButton(nextLevelId);
            table.row();
            table.add(nextTextButton);
        }

        // Main menu button.
        TextButton mainMenuTextButton = getMainMenuTextButton();
        table.row();
        table.add(mainMenuTextButton);

        this.stage.setDebugAll(SettingsManager.getDebugSettings().debugDraw);
    }

    public Label getHeaderLabel()
    {
        Label.LabelStyle labelStyle = UIManager.skin.get(Label.LabelStyle.class);

        Label headerLabel = new Label("You won!", labelStyle);

        return headerLabel;
    }

    private Label getBestScoreLabel(int score, int previousBestScore)
    {
        Label.LabelStyle labelStyle = UIManager.skin.get(Label.LabelStyle.class);

        String text = score > previousBestScore
            ? "New best score! It was: " + previousBestScore
            : "Best score: " + previousBestScore;

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

    public TextButton getRetryButton()
    {
        TextButton retryButton = new BeepingTextButton("Try Again?", UIManager.skin);

        retryButton.addListener(
            new ClickListener()
            {
                @Override
                public void clicked(InputEvent event, float x, float y)
                {
                    Retry();
                }
            });

        return retryButton;
    }

    public TextButton getNextButton(final String levelId)
    {
        TextButton retryButton = new BeepingTextButton("Next level", UIManager.skin);

        retryButton.addListener(
            new ClickListener()
            {
                @Override
                public void clicked(InputEvent event, float x, float y)
                {
                    SpaceTravels3.getGame().setScreen(new LoadingScreen(levelId));
                }
            });

        return retryButton;
    }

    private TextButton getMainMenuTextButton()
    {
        TextButton mainMenuTextButton = new BeepingTextButton("Main menu", UIManager.skin);
        mainMenuTextButton.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                SpaceTravels3.getGame().setScreen(new MenuScreen());
            }
        });

        return mainMenuTextButton;
    }

    private void Retry()
    {
        SpaceTravels3.getGame().setScreen(new LoadingScreen(this.levelId));
    }

    @Override
    public void show()
    {

    }

    @Override
    public void render(float delta)
    {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)
            || Gdx.input.isKeyJustPressed(Input.Keys.BACK))
        {
            SpaceTravels3.getGame().setScreen(new MenuScreen());
            return;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER))
        {
            Retry();
            return;
        }

        this.stage.act(delta);
        this.stage.draw();
    }

    @Override
    public void resize(int width, int height)
    {
        this.stage.getViewport().update(width, height);
    }

    @Override
    public void pause()
    {


    }

    @Override
    public void resume()
    {

    }

    @Override
    public void hide()
    {
        this.dispose();
    }

    @Override
    public void dispose()
    {
        this.sound.stop();
        this.sound.dispose();
        this.stage.dispose();
    }
}
