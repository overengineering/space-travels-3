package com.draga.spaceTravels3;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.draga.ErrorHandlerProvider;
import com.draga.GdxErrorHandler;
import com.draga.spaceTravels3.manager.*;
import com.draga.spaceTravels3.manager.asset.AssMan;
import com.draga.spaceTravels3.manager.level.LevelManager;
import com.draga.spaceTravels3.manager.level.serialisableEntities.SerialisableLevel;
import com.draga.spaceTravels3.screen.BackgroundScreen;
import com.draga.spaceTravels3.screen.MenuScreen;

public class SpaceTravels3 implements ApplicationListener
{
    private static final String LOGGING_TAG = SpaceTravels3.class.getSimpleName();

    public static SpriteBatch     spriteBatch;
    public static SpriteBatch overlaySpriteBath;
    public static ShapeRenderer   shapeRenderer;
    public static ScalingViewport menuViewport;
    public static ExtendViewport  gameViewport;

    private DebugOverlay debugOverlay;

    @Override
    public void create()
    {
        MathUtils.random.setSeed(System.currentTimeMillis());
        ErrorHandlerProvider.addErrorHandler(new GdxErrorHandler());

        spriteBatch = new SpriteBatch();
        overlaySpriteBath = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setAutoShapeType(true);
        menuViewport = new ScalingViewport(
            Scaling.stretch,
            Gdx.graphics.getWidth(),
            Gdx.graphics.getHeight(),
            new OrthographicCamera());
        gameViewport =
            new ExtendViewport(Constants.Visual.VIEWPORT_WIDTH, Constants.Visual.VIEWPORT_HEIGHT);
        gameViewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        Gdx.input.setCatchBackKey(true);

        AssMan.create();
        for (SerialisableLevel serialisableLevel : LevelManager.getSerialisableLevels())
        {
            AssMan.getMenuAssMan().load(serialisableLevel.iconPath, Texture.class);
        }
        AssMan.getMenuAssMan().update();


        ScreenManager.create();
        UIManager.create();
        SoundManager.create();
        InputManager.create();
        MusicManager.create();

        MusicManager.playRandomMusic();

        if (Constants.General.IS_DEBUGGING)
        {
            Gdx.app.setLogLevel(Application.LOG_DEBUG);

            this.debugOverlay = new DebugOverlay();
        }
        else
        {
            Gdx.app.setLogLevel(Application.LOG_ERROR);
        }

        BackgroundScreen backgroundScreen = new BackgroundScreen();
        ScreenManager.addScreen(backgroundScreen);

        MenuScreen menuScreen = new MenuScreen();
        ScreenManager.addScreen(menuScreen);
    }

    @Override
    public void resize(int width, int height)
    {
        String log = String.format("Resize to %4d x %4d height", width, height);
        Gdx.app.debug(LOGGING_TAG, log);

        if (Constants.General.IS_DEBUGGING)
        {
            this.debugOverlay.resize(width, height);
        }
    }

    @Override
    public void render()
    {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        ScreenManager.render(Gdx.graphics.getDeltaTime());

        if (Constants.General.IS_DEBUGGING)
        {
            this.debugOverlay.render(Gdx.graphics.getRawDeltaTime());
        }
    }

    @Override
    public void pause()
    {
        Gdx.app.debug(LOGGING_TAG, "Pause");
        SettingsManager.saveSettings();
    }

    @Override
    public void resume()
    {
        Gdx.app.debug(LOGGING_TAG, "Resume");
    }

    @Override
    public void dispose()
    {
        Gdx.app.debug(LOGGING_TAG, "Dispose");
        if (Constants.General.IS_DEBUGGING)
        {
            this.debugOverlay.dispose();
        }

        ScreenManager.dispose();
        UIManager.dispose();
        AssMan.dispose();
        SoundManager.dispose();
        InputManager.dispose();
        MusicManager.dispose();

        spriteBatch.dispose();
        shapeRenderer.dispose();
    }
}
