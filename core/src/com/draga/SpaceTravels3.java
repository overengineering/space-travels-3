package com.draga;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.draga.manager.SceneManager;
import com.draga.manager.scene.MenuScene;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SpaceTravels3 extends ApplicationAdapter
{
    private final static String LOGGING_TAG = SpaceTravels3.class.getSimpleName();
    private final float timeBetweenDebugInfoUpdate = 1f;
    private float timeUntilDebugInfoUpdate = timeBetweenDebugInfoUpdate;

    @Override
    public void create()
    {
        SceneManager.setActiveScene(new MenuScene());

        if (Constants.IS_DEBUGGING)
        {
            Gdx.app.setLogLevel(Application.LOG_DEBUG);

            ScheduledExecutorService logOutputScheduler = Executors.newSingleThreadScheduledExecutor();
            logOutputScheduler.scheduleAtFixedRate(
                    new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            String formattedJavaHeap = Constants.COMMA_SEPARATED_THOUSANDS_FORMATTER.format(
                                    Gdx.app.getJavaHeap());
                            String formattedNativeHeap =
                                    Constants.COMMA_SEPARATED_THOUSANDS_FORMATTER.format(Gdx.app.getNativeHeap());
                            String log = String.format(
                                    "%-23s | FPS : %3d | Java heap : %12s | Java native heap : %12s",
                                    new Timestamp(new Date().getTime()).toString(),
                                    Gdx.graphics.getFramesPerSecond(),
                                    formattedJavaHeap,
                                    formattedNativeHeap);
                            Gdx.app.log(LOGGING_TAG, log);
                        }
                    },
                    0,
                    1,
                    TimeUnit.SECONDS);
        } else
        {
            Gdx.app.setLogLevel(Application.LOG_ERROR);
        }

        Box2D.init();
    }

    @Override
    public void render()
    {
        float deltaTime = Gdx.graphics.getDeltaTime();

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        SceneManager.getActiveScene().render(deltaTime);
    }

    @Override
    public void dispose()
    {
        Gdx.app.debug(LOGGING_TAG, "Dispose");
        SceneManager.getActiveScene().dispose();
        super.dispose();
    }

    @Override
    public void pause()
    {
        Gdx.app.debug(LOGGING_TAG, "Pause");
        SceneManager.getActiveScene().pause();
        super.pause();
    }

    @Override
    public void resize(int width, int height)
    {
        String log = String.format("Resize to %4d width x %4d height", width, height);
        Gdx.app.debug(LOGGING_TAG, log);

        SceneManager.getActiveScene().resize(width, height);

        super.resize(width, height);
    }

    @Override
    public void resume()
    {
        Gdx.app.debug(LOGGING_TAG, "Resume");
        SceneManager.getActiveScene().resume();
        super.resume();
    }
}
