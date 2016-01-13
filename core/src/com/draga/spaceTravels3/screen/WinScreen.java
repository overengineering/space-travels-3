package com.draga.spaceTravels3.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
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
import com.draga.spaceTravels3.manager.ScreenManager;
import com.draga.spaceTravels3.manager.SettingsManager;
import com.draga.spaceTravels3.manager.UIManager;
import com.draga.spaceTravels3.manager.asset.AssMan;
import com.draga.spaceTravels3.ui.BeepingTextButton;
import com.draga.spaceTravels3.ui.Screen;

public class WinScreen extends Screen
{
    private final Stage stage;

    private final Sound sound;

    private final String levelId;
    private final String difficulty;
    private       Screen gameScreen;

    public WinScreen(String levelId, String difficulty, Score score, Screen gameScreen)
    {
        super(true, false);

        this.gameScreen = gameScreen;

        this.sound = AssMan.getGameAssMan().get(AssMan.getAssList().winSound);
        this.sound.play(SettingsManager.getSettings().volumeFX);

        this.levelId = levelId;
        this.difficulty = difficulty;

        this.stage = new Stage(SpaceTravels3.menuViewport, SpaceTravels3.overlaySpriteBath);

        Table table = UIManager.addDefaultTableToStage(this.stage);

        table.setBackground(UIManager.getTiledDrawable(Constants.Visual.SCREEN_FADE_COLOUR));
        table.addAction(Actions.sequence(
            Actions.fadeOut(0),
            Actions.fadeIn(Constants.Visual.SCREEN_FADE_DURATION, Interpolation.pow2In)));

        // Header label.
        Label headerLabel = getHeaderLabel();
        table.add(headerLabel);

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

        this.stage.setDebugAll(SettingsManager.getDebugSettings().debugDraw);
    }

    public Label getHeaderLabel()
    {
        Label.LabelStyle labelStyle = UIManager.skin.get(Label.LabelStyle.class);

        Label headerLabel = new Label("You won!", labelStyle);

        return headerLabel;
    }

    private Label getBestScoreLabel(int score)
    {
        Integer previousBestScore = ScoreManager.getScore(this.levelId, this.difficulty);

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

    private TextButton getMainMenuTextButton()
    {
        TextButton mainMenuTextButton = new BeepingTextButton("Main menu", UIManager.skin);
        mainMenuTextButton.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                ScreenManager.removeScreen(WinScreen.this);
                ScreenManager.removeScreen(WinScreen.this.gameScreen);
            }
        });

        return mainMenuTextButton;
    }

    private void Retry()
    {
        ScreenManager.removeScreen(this);
        ScreenManager.removeScreen(this.gameScreen);
        ScreenManager.addScreen(new LoadingScreen(this.levelId, this.difficulty));
    }

    @Override
    public void show()
    {
        Gdx.input.setInputProcessor(this.stage);
    }

    @Override
    public void render(float delta)
    {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)
            || Gdx.input.isKeyJustPressed(Input.Keys.BACK))
        {
            ScreenManager.removeScreen(WinScreen.this);
            ScreenManager.removeScreen(WinScreen.this.gameScreen);
            return;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER))
        {
            Retry();
            return;
        }

        this.stage.getViewport().apply();

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

    }

    @Override
    public void dispose()
    {
        this.sound.stop();
        this.sound.dispose();
        this.stage.dispose();
    }
}
