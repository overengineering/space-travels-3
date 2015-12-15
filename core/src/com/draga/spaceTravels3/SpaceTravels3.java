package com.draga.spaceTravels3;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.draga.spaceTravels3.manager.InputManager;
import com.draga.spaceTravels3.manager.SettingsManager;
import com.draga.spaceTravels3.manager.SoundManager;
import com.draga.spaceTravels3.manager.UIManager;
import com.draga.spaceTravels3.manager.asset.AssMan;
import com.draga.spaceTravels3.screen.MenuScreen;
import com.draga.spaceTravels3.physic.PhysicsEngine;

public class SpaceTravels3 extends Game
{
    private final static String LOGGING_TAG = SpaceTravels3.class.getSimpleName();
    public static SpriteBatch   spriteBatch;
    public static ShapeRenderer shapeRenderer;
    private static Game game;
    private DebugOverlay debugOverlay;

    public static Game getGame()
    {
        return SpaceTravels3.game;
    }

    @Override
    public void create()
    {
        game = this;
        spriteBatch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setAutoShapeType(true);

        Gdx.input.setCatchBackKey(true);
        UIManager.create();
        AssMan.create();
        SoundManager.create();
        AssMan.getAssList();
        PhysicsEngine.create();
        InputManager.create();

        if (Constants.General.IS_DEBUGGING)
        {
            Gdx.app.setLogLevel(Application.LOG_DEBUG);

            this.debugOverlay = new DebugOverlay();
        }
        else
        {
            Gdx.app.setLogLevel(Application.LOG_ERROR);
        }

        this.setScreen(new MenuScreen());
    }

    @Override
    public void dispose()
    {
        Gdx.app.debug(LOGGING_TAG, "Dispose");
        if (Constants.General.IS_DEBUGGING)
        {
            this.debugOverlay.dispose();
        }
        UIManager.dispose();
        AssMan.dispose();
        SoundManager.dispose();
        PhysicsEngine.dispose();
        spriteBatch.dispose();
        shapeRenderer.dispose();
        super.dispose();
    }

    @Override
    public void pause()
    {
        Gdx.app.debug(LOGGING_TAG, "Pause");
        SettingsManager.saveSettings();
        super.pause();
    }

    @Override
    public void resume()
    {
        Gdx.app.debug(LOGGING_TAG, "Resume");
        super.resume();
    }

    @Override
    public void render()
    {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        super.render();

        if (Constants.General.IS_DEBUGGING)
        {
            this.debugOverlay.render(Gdx.graphics.getDeltaTime());
        }
    }

    @Override
    public void resize(int width, int height)
    {
        String log = String.format("Resize to %4d width x %4d height", width, height);
        Gdx.app.debug(LOGGING_TAG, log);

        super.resize(width, height);

        if (Constants.General.IS_DEBUGGING)
        {
            this.debugOverlay.resize(width, height);
        }
    }
}