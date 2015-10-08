package com.draga.manager.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.draga.manager.ScreenManager;

public class LoseScreen implements Screen
{
    Screen      parentScreen = null;
    SpriteBatch batch        = new SpriteBatch();
    Color       fadeToColour = new Color(0, 0, 0, 0.7f);
    Color       backgroundColour = new Color(0, 0, 0, 0);

    public LoseScreen(Screen parentScreen)
    {
        this.parentScreen = parentScreen;
    }

    @Override public void show()
    {

    }

    @Override public void render(float delta)
    {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE))
        {
            ScreenManager.setActiveScreen(new MenuScreen());
            return;
        }

        parentScreen.render(delta);
        update(delta);
        draw();
    }

    private void draw()
    {
        backgroundColour.lerp(fadeToColour, 0.02f);

        if (fadeToColour.equals(backgroundColour))
        {
            parentScreen.pause();
        }

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        ShapeRenderer shapeRenderer = new ShapeRenderer();
        shapeRenderer.setColor(backgroundColour);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.rect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        shapeRenderer.end();

        Gdx.gl.glDisable(GL20.GL_BLEND);
    }

    private void update(float delta)
    {

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

    }
}
