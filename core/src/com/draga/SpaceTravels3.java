package com.draga;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.draga.manager.GameManager;
import com.draga.manager.SkinManager;
import com.draga.manager.asset.AssMan;
import com.draga.manager.screen.MenuScreen;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SpaceTravels3 extends Game
{
    private final static String LOGGING_TAG = SpaceTravels3.class.getSimpleName();
    private ScheduledExecutorService logOutputScheduler;

    @Override
    public void create()
    {
        Gdx.input.setCatchBackKey(true);
        AssMan.getAssList();
        SkinManager.create();
        GameManager.setGame(this);

        if (Constants.IS_DEBUGGING)
        {
            Gdx.app.setLogLevel(Application.LOG_DEBUG);

            launchPerformanceLoggerScheduler();
        }
        else
        {
            Gdx.app.setLogLevel(Application.LOG_ERROR);
        }

        this.setScreen(new MenuScreen());
    }

    private void launchPerformanceLoggerScheduler()
    {
        logOutputScheduler = Executors.newSingleThreadScheduledExecutor();
        logOutputScheduler.scheduleAtFixedRate(new PerformanceLogger(), 0, 1, TimeUnit.SECONDS);
    }

    @Override
    public void dispose()
    {
        Gdx.app.debug(LOGGING_TAG, "Dispose");
        if (Constants.IS_DEBUGGING)
        {
            logOutputScheduler.shutdown();
        }
        SkinManager.BasicSkin.dispose();
        super.dispose();
    }

    @Override
    public void pause()
    {
        Gdx.app.debug(LOGGING_TAG, "Pause");
        if (Constants.IS_DEBUGGING)
        {
            logOutputScheduler.shutdown();
        }
        super.pause();
    }

    @Override
    public void resume()
    {
        Gdx.app.debug(LOGGING_TAG, "Resume");
        launchPerformanceLoggerScheduler();
        super.resume();
    }

    @Override
    public void render()
    {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        super.render();
    }

    @Override
    public void resize(int width, int height)
    {
        String log = String.format("Resize to %4d width x %4d height", width, height);
        Gdx.app.debug(LOGGING_TAG, log);

        super.resize(width, height);
    }
}
