package com.draga.manager.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.draga.BeepingClickListener;
import com.draga.manager.GameManager;
import com.draga.manager.ScoreManager;
import com.draga.manager.SettingsManager;
import com.draga.manager.SkinManager;
import com.draga.manager.asset.AssMan;
import com.draga.manager.level.LevelManager;
import com.draga.manager.level.serialisableEntities.SerialisableLevel;

public class WinScreen implements Screen
{
    private static final float FADE_PER_SECOND = 0.7f;
    private final Stage stage;
    private final Color fadeToColour     = new Color(0, 0, 0, 0.7f);
    private final Color backgroundColour = new Color(0, 0, 0, 0);
    private final ShapeRenderer shapeRenderer;
    private final Sound         sound;
    private       String        levelName;

    public WinScreen(String levelName, int score)
    {
        this.levelName = levelName;
        this.stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        float previousBestScore = ScoreManager.getScore(levelName);

        Label headerLabel = getHeaderLabel();
        TextButton retryButton = getRetryButton();
        Label scoreLabel = getScoreLabel(score);

        Table table = new Table();
        table.setBackground(SkinManager.BasicSkin.newDrawable("background", fadeToColour));
        table.addAction(Actions.sequence(
            Actions.fadeOut(0),
            Actions.fadeIn(3, Interpolation.pow2In)));

        table.add(headerLabel);

        ScoreManager.updateScore(levelName, score);
        table.row();
        Label newBestScoreLabel = getBestScoreLabel(score, previousBestScore);
        table.add(newBestScoreLabel);

        table.row();
        table.add(scoreLabel);

        table.row();
        table.add(retryButton);

        SerialisableLevel nextLevel = LevelManager.getNextLevel(levelName);

        if (nextLevel != null)
        {
            String nextLevelName = nextLevel.name;
            TextButton nextTextButton = getNextButton(nextLevelName);
            table.row();
            table.add(nextTextButton);
        }

        table.setFillParent(true);
        stage.addActor(table);

        stage.setDebugAll(SettingsManager.getDebugSettings().debugDraw);
        shapeRenderer = new ShapeRenderer();

        sound = AssMan.getAssMan().get(AssMan.getAssList().winSound);
        sound.play();
    }

    public Label getHeaderLabel()
    {
        Label.LabelStyle labelStyle = SkinManager.BasicSkin.get(Label.LabelStyle.class);

        Label headerLabel = new Label("You won!", labelStyle);

        return headerLabel;
    }

    public TextButton getRetryButton()
    {
        TextButton retryButton = new TextButton("Try Again?", SkinManager.BasicSkin);

        retryButton.addListener(
            new BeepingClickListener()
            {
                @Override
                public void clicked(InputEvent event, float x, float y)
                {
                    super.clicked(event, x, y);
                    Retry();
                }
            });

        return retryButton;
    }

    private Label getScoreLabel(float score)
    {
        Label.LabelStyle labelStyle = SkinManager.BasicSkin.get(Label.LabelStyle.class);

        Label scoreLabel = new Label("Score: " + score, labelStyle);

        return scoreLabel;
    }

    private Label getBestScoreLabel(float score, float previousBestScore)
    {
        Label.LabelStyle labelStyle = SkinManager.BasicSkin.get(Label.LabelStyle.class);

        String text = score > previousBestScore
            ? "New best score! It was: " + previousBestScore
            : "Best score: " + previousBestScore;

        Label newBestScoreLabel =
            new Label(text, labelStyle);

        return newBestScoreLabel;
    }

    public TextButton getNextButton(final String levelName)
    {
        TextButton retryButton = new TextButton("Next level", SkinManager.BasicSkin);

        retryButton.addListener(
            new BeepingClickListener()
            {
                @Override
                public void clicked(InputEvent event, float x, float y)
                {
                    super.clicked(event, x, y);
                    GameManager.getGame().setScreen(new LoadingScreen(levelName));
                }
            });

        return retryButton;
    }

    private void Retry()
    {
        GameManager.getGame().setScreen(new LoadingScreen(levelName));
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
            GameManager.getGame().setScreen(new MenuScreen());
            return;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER))
        {
            Retry();
            return;
        }

        update(delta);
        draw(delta);
    }

    private void update(float delta)
    {
        stage.act(delta);
    }

    private void draw(float delta)
    {
        stage.draw();
    }

    @Override
    public void resize(int width, int height)
    {
        stage.getViewport().update(width, height);
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
        sound.stop();
        sound.dispose();
        shapeRenderer.dispose();
        stage.dispose();
    }
}
