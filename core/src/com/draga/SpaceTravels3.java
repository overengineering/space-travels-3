package com.draga;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.draga.manager.level.LevelManager;
import com.draga.utilities.JavaUtilities;

import java.sql.Timestamp;
import java.util.Date;

public class SpaceTravels3 extends ApplicationAdapter
{
    private final float timeBetweenDebugInfoUpdate = 1f;
    private final String loggingTag = "Space Travels 3";
    private final boolean debugMode = JavaUtilities.isDebugMode();
    private World world;
    private float timeUntilDebugInfoUpdate = timeBetweenDebugInfoUpdate;

    @Override
    public void create()
    {
        world = LevelManager.getLevelWorldFromFile("level1.json", new SpriteBatch());

        if (debugMode)
        {
            Gdx.app.setLogLevel(Application.LOG_DEBUG);
        }
        else
        {
            Gdx.app.setLogLevel(Application.LOG_ERROR);
        }
    }

    @Override
    public void render()
    {
        float deltaTime = Gdx.graphics.getDeltaTime();

        if (debugMode)
        {
            timeUntilDebugInfoUpdate -= deltaTime;
            if (timeUntilDebugInfoUpdate <= 0f)
            {
                timeUntilDebugInfoUpdate = timeBetweenDebugInfoUpdate;
                String log = String.format(
                    "%23s | FPS : %3d | Java heap : %10d | Java native heap : %10d",
                    new Timestamp(new Date().getTime()).toString(),
                    Gdx.graphics.getFramesPerSecond(),
                    Gdx.app.getJavaHeap(),
                    Gdx.app.getNativeHeap());
                Gdx.app.log(loggingTag, log);
            }
        }

        world.update(deltaTime);
        world.draw();
    }

    @Override
    public void dispose()
    {
        Gdx.app.debug(loggingTag, "Dispose");
        super.dispose();
    }

    @Override
    public void pause()
    {
        Gdx.app.debug(loggingTag, "Pause");
        super.pause();
    }

    @Override
    public void resize(int width, int height)
    {
        String log = String.format("Resize to %4d width x %4d height", width, height);
        Gdx.app.debug(loggingTag, log);
        super.resize(width, height);
    }

    @Override
    public void resume()
    {
        Gdx.app.debug(loggingTag, "Resume");
        super.resume();
    }
}
