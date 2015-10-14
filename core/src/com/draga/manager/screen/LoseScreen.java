package com.draga.manager.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.draga.manager.FontManager;
import com.draga.manager.ScreenManager;
import com.draga.manager.level.LevelManager;

public class LoseScreen implements Screen
{
    private final Stage         stage;
    private final GameScreen    parentGameScreen;
    private final Color         fadeToColour     = new Color(0, 0, 0, 0.7f);
    private final Color         backgroundColour = new Color(0, 0, 0, 0);
    private final ShapeRenderer shapeRenderer;

    public LoseScreen(GameScreen parentScreen)
    {
        this.parentGameScreen = parentScreen;
        this.stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        Actor retryButton = getRetryButton();

        Table table = new Table();
        table
            .add(retryButton)
            .size(retryButton.getWidth() * 2, retryButton.getHeight() * 3);
        table.setFillParent(true);
        stage.addActor(table);

        shapeRenderer = new ShapeRenderer();
    }

    @Override public void show()
    {

    }

    @Override public void render(float delta)
    {
        if (Gdx.input.isKeyJustPressed(Input.Keys.BACK)
            || Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE))
        {
            parentGameScreen.pause();
            parentGameScreen.dispose();
            ScreenManager.setActiveScreen(new MenuScreen());
            return;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER))
        {
            Retry();
            return;
        }

        parentGameScreen.render(delta);
        update(delta);
        draw(delta);
    }

    private void draw(float delta)
    {
        final float fadePerSecond = 0.7f;

        backgroundColour.lerp(fadeToColour, fadePerSecond * delta);

        if (fadeToColour.equals(backgroundColour))
        {
            parentGameScreen.setDoUpdate(false);
        }

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        shapeRenderer.setColor(backgroundColour);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.rect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        shapeRenderer.end();

        Gdx.gl.glDisable(GL20.GL_BLEND);

        stage.draw();
    }

    private void update(float delta)
    {
        stage.act(delta);
    }

    @Override public void resize(int width, int height)
    {

    }

    @Override public void pause()
    {

    }

    @Override public void resume()
    {

    }

    @Override public void hide()
    {

    }

    @Override public void dispose()
    {
        Gdx.input.setInputProcessor(null);
        shapeRenderer.dispose();
        stage.dispose();
    }

    public Actor getRetryButton()
    {
        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = FontManager.getBigFont();

        TextButton retryButton = new TextButton("Try Again?", buttonStyle);

        retryButton.setColor(new Color(1, 1, 1, 0));
        retryButton.addAction(Actions.color(new Color(1, 1, 1, 1), 5, Interpolation.pow2In));
        retryButton.addListener(
            new ClickListener()
            {
                @Override public void clicked(InputEvent event, float x, float y)
                {
                    Retry();
                    super.clicked(event, x, y);
                }
            });

        return retryButton;
    }

    private void Retry()
    {
        parentGameScreen.pause();
        parentGameScreen.dispose();
        ScreenManager.setActiveScreen(
            LevelManager.getLevelWorldFromFile(
                "level1.json", new SpriteBatch()));
    }
}
