package com.draga.manager.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.draga.manager.ScreenManager;
import com.draga.manager.level.LevelManager;

public class LoseScreen implements Screen
{
    private final Stage      stage;
    private final BitmapFont pDark24Font;
    private final GameScreen parentScreen;
    private final SpriteBatch batch        = new SpriteBatch();
    private final Color       fadeToColour = new Color(0, 0, 0, 0.7f);
    private final Color backgroundColour = new Color(0, 0, 0, 0);
    private final ShapeRenderer shapeRenderer;

    public LoseScreen(GameScreen parentScreen)
    {
        this.parentScreen = parentScreen;
        this.stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        FreeTypeFontGenerator freeTypeFontGenerator =
            new FreeTypeFontGenerator(Gdx.files.internal("font/pdark.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter =
            new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 64;
        pDark24Font = freeTypeFontGenerator.generateFont(parameter);

        freeTypeFontGenerator.dispose();

        stage.addActor(getLoseLabel());

        shapeRenderer = new ShapeRenderer();
    }

    @Override public void show()
    {

    }

    @Override public void render(float delta)
    {
        if (Gdx.input.isKeyJustPressed(Input.Keys.BACK) || Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE))
        {
            parentScreen.pause();
            parentScreen.dispose();
            ScreenManager.setActiveScreen(new MenuScreen());
            return;
        }

        if (Gdx.input.isTouched() || Gdx.input.isKeyJustPressed(Input.Keys.ENTER))
        {
            parentScreen.pause();
            parentScreen.dispose();
            ScreenManager.setActiveScreen(
                LevelManager.getLevelWorldFromFile(
                    "level1.json", new SpriteBatch()));
            return;
        }

        parentScreen.render(delta);
        update(delta);
        draw(delta);
    }

    private void draw(float delta)
    {
        final float fadePerSecond = 0.7f;

        backgroundColour.lerp(fadeToColour, fadePerSecond * delta);

        if (fadeToColour.equals(backgroundColour))
        {
            parentScreen.pause();
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

    public Actor getLoseLabel()
    {
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = pDark24Font;

        Label loseLabel = new Label("Try Again?", labelStyle);

        loseLabel.setWidth(stage.getWidth());
        loseLabel.setHeight(stage.getHeight());
        loseLabel.setAlignment(Align.center);

        loseLabel.setColor(new Color(1, 1, 1, 0));
        loseLabel.addAction(Actions.color(new Color(1, 1, 1, 1), 5, Interpolation.pow2In));

        return loseLabel;
    }
}
