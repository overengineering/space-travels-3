package com.draga;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.draga.manager.level.LevelManager;

import java.sql.Timestamp;
import java.util.Date;

public class SpaceTravels3 extends ApplicationAdapter {
	private World world;
	private final float timeBetweenDebugInfoUpdate = 1f;
	private float timeUntilDebugInfoUpdate = timeBetweenDebugInfoUpdate;
	private final String loggingTag = "Space Travels 3";
	private final boolean debugMode = isDebugMode();

	@Override
	public void create () {
		world = LevelManager.getLevelWorldFromFile("level1.json", new SpriteBatch());
	}

	@Override
	public void render () {
		float deltaTime = Gdx.graphics.getDeltaTime();

		if(debugMode)
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
        if (debugMode)
        {
            Gdx.app.log(loggingTag, "Dispose");
        }
        super.dispose();
    }

    @Override
    public void pause()
    {
        if (debugMode)
        {
            Gdx.app.log(loggingTag, "Pause");
        }
        super.pause();
    }

    @Override
    public void resize(int width, int height)
    {
        if (debugMode)
        {
            String log = String.format("Resize to %4d width x %4d height", width, height);
            Gdx.app.log(loggingTag, log);
        }
        super.resize(width, height);
    }

    @Override
    public void resume()
    {
        if (debugMode)
        {
            Gdx.app.log(loggingTag, "Resume");
        }
        super.resume();
    }

    /**
     * https://stackoverflow.com/questions/3776204/how-to-find-out-if-debug-mode-is-enabled
     * @return If the application is being debugged
     */
	public static boolean isDebugMode()
	{
		boolean isDebug = java.lang.management.ManagementFactory.getRuntimeMXBean().
			getInputArguments().toString().indexOf("-agentlib:jdwp") > 0;
		return isDebug;
	}
}
