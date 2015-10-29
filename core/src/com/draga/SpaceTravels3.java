package com.draga;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.draga.manager.AssMan;
import com.draga.manager.FontManager;
import com.draga.manager.ScreenManager;
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
        AssMan.getAssList();
        FontManager.create();
        ScreenManager.setGame(this);
        ScreenManager.setActiveScreen(new MenuScreen());

        if (Constants.IS_DEBUGGING)
        {
            Gdx.app.setLogLevel(Application.LOG_DEBUG);

            launchPerformanceLoggerScheduler();
        } else
        {
            Gdx.app.setLogLevel(Application.LOG_ERROR);
        }

        Box2D.init();
    }

    private void launchPerformanceLoggerScheduler()
    {
        logOutputScheduler = Executors.newSingleThreadScheduledExecutor();
        logOutputScheduler.scheduleAtFixedRate(new PerformanceLogger(), 0, 1, TimeUnit.SECONDS);
    }

    @Override
    public void render()
    {
        float deltaTime = Gdx.graphics.getDeltaTime();

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        ScreenManager.getActiveScreen().render(deltaTime);
    }

    @Override
    public void dispose()
    {
        Gdx.app.debug(LOGGING_TAG, "Dispose");
        ScreenManager.getActiveScreen().dispose();
        if (Constants.IS_DEBUGGING)
        {
            logOutputScheduler.shutdown();
        }
        FontManager.destroy();
        super.dispose();
    }

    @Override
    public void pause()
    {
        Gdx.app.debug(LOGGING_TAG, "Pause");
        ScreenManager.getActiveScreen().pause();
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
        ScreenManager.getActiveScreen().resume();
        launchPerformanceLoggerScheduler();
        super.resume();
    }

    @Override
    public void resize(int width, int height)
    {
        String log = String.format("Resize to %4d width x %4d height", width, height);
        Gdx.app.debug(LOGGING_TAG, log);

        ScreenManager.getActiveScreen().resize(width, height);

        super.resize(width, height);
    }
}
