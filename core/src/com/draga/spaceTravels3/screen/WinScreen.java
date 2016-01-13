package com.draga.spaceTravels3.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Pools;
import com.draga.spaceTravels3.Constants;
import com.draga.spaceTravels3.Level;
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

    private final Level  level;
    private       Screen gameScreen;
    private       Image  headerImage;

    public WinScreen(Level level, Screen gameScreen)
    {
        super(true, false);

        this.level = level;
        Score score = level.getScore();
        this.gameScreen = gameScreen;

        this.sound = AssMan.getGameAssMan().get(AssMan.getAssList().winSound);
        this.sound.play(SettingsManager.getSettings().volumeFX);

        this.stage = new Stage(SpaceTravels3.menuViewport, SpaceTravels3.overlaySpriteBath);

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

    public Actor getHeaderLabel()
    {
        Table table = new Table();

        Label label = new Label(this.level.getName() + " ", UIManager.skin, "large", Color.WHITE);
        table.add(label);

        if (AssMan.getMenuAssMan().update()
            || AssMan.getMenuAssMan().isLoaded(this.level.getIconPath()))
        {
            Texture texture =
                AssMan.getMenuAssMan().get(this.level.getIconPath(), Texture.class);
            this.headerImage = new Image(texture);
        }
        else
        {
            this.headerImage = new Image();
        }

        table
            .add(this.headerImage)
            .size(label.getHeight());

        table.add(new Label(" " + this.level.getDifficulty(), UIManager.skin));

        return table;
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
        ScreenManager.addScreen(new LoadingScreen(this.level.getId(), this.level.getDifficulty()));
    }

    @Override
    public void show()
    {
        Gdx.input.setInputProcessor(this.stage);
    }

    @Override
    public void render(float delta)
    {
        // If the image still needs showing.
        if (this.headerImage.getDrawable() == null
            && (
            AssMan.getMenuAssMan().update()
                || AssMan.getMenuAssMan().isLoaded(this.level.getIconPath())))
        {
            Texture texture = AssMan.getMenuAssMan().get(this.level.getIconPath());
            this.headerImage.setDrawable(new TextureRegionDrawable(new TextureRegion(texture)));
        }

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
