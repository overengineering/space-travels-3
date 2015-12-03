package com.draga;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.draga.manager.GameManager;
import com.draga.manager.SettingsManager;
import com.draga.manager.SoundManager;
import com.draga.manager.UIManager;
import com.draga.manager.asset.AssMan;
import com.draga.manager.screen.MenuScreen;
import com.draga.physic.PhysicsEngine;

public class SpaceTravels3 extends Game
{
    private final static String LOGGING_TAG = SpaceTravels3.class.getSimpleName();
    private DebugOverlay debugOverlay;

    @Override
    public void create()
    {
        Gdx.input.setCatchBackKey(true);
        UIManager.create();
        AssMan.create();
        SoundManager.create();
        AssMan.getAssList();
        GameManager.setGame(this);
        PhysicsEngine.create();

        if (Constants.IS_DEBUGGING)
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
        if (Constants.IS_DEBUGGING)
        {
            this.debugOverlay.dispose();
        }
        UIManager.dispose();
        AssMan.dispose();
        SoundManager.dispose();
        PhysicsEngine.dispose();
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

        this.debugOverlay.render(Gdx.graphics.getDeltaTime());
    }

    @Override
    public void resize(int width, int height)
    {
        String log = String.format("Resize to %4d width x %4d height", width, height);
        Gdx.app.debug(LOGGING_TAG, log);

        super.resize(width, height);

        this.debugOverlay.resize(width, height);
    }
}
