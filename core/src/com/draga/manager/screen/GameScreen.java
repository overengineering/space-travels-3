package com.draga.manager.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.draga.*;
import com.draga.event.CountdownFinishedEvent;
import com.draga.event.LoseEvent;
import com.draga.event.WinEvent;
import com.draga.gameEntity.GameEntity;
import com.draga.gameEntity.Ship;
import com.draga.manager.GameEntityManager;
import com.draga.manager.GameManager;
import com.draga.manager.InputManager;
import com.draga.manager.SettingsManager;
import com.draga.manager.asset.AssMan;
import com.draga.physic.PhysicDebugDrawer;
import com.draga.physic.PhysicsEngine;
import com.google.common.eventbus.Subscribe;

public class GameScreen implements Screen
{
    private static final String LOGGING_TAG = GameScreen.class.getSimpleName();

    private final Background background;
    private Screen overlayScreen;

    private Hud hud;

    private ExtendViewport extendViewport;

    private Level level;

    public GameScreen(Level level)
    {
        this.background = new Background();
        this.level = level;

        this.overlayScreen = new CountdownScreen();

        Constants.General.EVENT_BUS.register(this);

        GameScreenInputProcessor gameScreenInputProcessor = new GameScreenInputProcessor();
        Gdx.input.setInputProcessor(gameScreenInputProcessor);

        extendViewport = new ExtendViewport(
            Constants.Visual.VIEWPORT_WIDTH,
            Constants.Visual.VIEWPORT_HEIGHT,
            level.getWidth(),
            level.getHeight());
        extendViewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        hud = new Hud(extendViewport.getCamera(), level);
    }

    private void updateCamera()
    {
        // Soften camera movement.
        Vector2 cameraVec = level.getShip().physicsComponent.getPosition();

        Camera camera = extendViewport.getCamera();
        camera.position.x = cameraVec.x;
        camera.position.y = cameraVec.y;
        camera.update();

        SpaceTravels3.spriteBatch.setProjectionMatrix(extendViewport.getCamera().combined);
    }

    @Override
    public void show()
    {

    }

    @Override
    public void render(float deltaTime)
    {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE))
        {
            GameManager.getGame().setScreen(new MenuScreen());
            return;
        }

        if (level.getGameState() != GameState.PAUSE
            && level.getGameState() != GameState.COUNTDOWN)
        {
            update(deltaTime);
        }

        draw();

        hud.render(deltaTime);

        if (overlayScreen != null)
        {
            overlayScreen.render(deltaTime);
        }
    }

    public void update(float elapsed)
    {
        if (Constants.General.IS_DEBUGGING)
        {
            checkDebugKeys();
        }

        InputManager.update();

        PhysicsEngine.update(elapsed);

        for (GameEntity gameEntity : GameEntityManager.getGameEntities())
        {
            gameEntity.update(elapsed);
        }

        hud.update();
    }

    public void draw()
    {
        updateCamera();

        SpaceTravels3.spriteBatch.begin();

        background.draw(extendViewport.getCamera());

        for (GameEntity gameEntity : GameEntityManager.getGameEntities())
        {
            gameEntity.graphicComponent.draw();
        }
        SpaceTravels3.spriteBatch.end();

        if (SettingsManager.getDebugSettings().debugDraw)
        {
            PhysicDebugDrawer.draw(extendViewport.getCamera());
        }
    }

    private void checkDebugKeys()
    {
        if (Gdx.input.isKeyJustPressed(Input.Keys.F1))
        {
            SettingsManager.getDebugSettings().infiniteFuel =
                !SettingsManager.getDebugSettings().infiniteFuel;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.F2))
        {
            SettingsManager.getDebugSettings().noGravity =
                !SettingsManager.getDebugSettings().noGravity;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.F3))
        {
            level.getShip().physicsComponent.getVelocity().set(0, 0);
            level.getShip().physicsComponent.setAngularVelocity(0);
        }
    }

    public void resize(int width, int height)
    {
        extendViewport.update(width, height);
    }

    @Override
    public void pause()
    {
        level.pause();
    }

    @Override
    public void resume()
    {
        if (level.getGameState() == GameState.PAUSE)
        {
            this.overlayScreen = new CountdownScreen();
        }

        level.resume();
    }

    @Override
    public void hide()
    {
        this.dispose();
    }

    @Override
    public void dispose()
    {
        GameEntityManager.dispose();
        Constants.General.EVENT_BUS.unregister(this);
        hud.dispose();
        if (this.overlayScreen != null)
        {
            this.overlayScreen.dispose();
        }

        level.dispose();

        background.dispose();

        AssMan.getAssMan().clear();
    }

    @Subscribe
    public void Lose(LoseEvent loseEvent)
    {
        this.overlayScreen = new LoseScreen(level.getId());
    }

    @Subscribe
    public void Win(WinEvent winEvent)
    {
        this.overlayScreen = new WinScreen(level.getId(), level.getScore());
    }

    @Subscribe
    public void countdownFinished(CountdownFinishedEvent countdownFinishedEvent)
    {
        this.overlayScreen.dispose();
        this.overlayScreen = null;
    }
}
