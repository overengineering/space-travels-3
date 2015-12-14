package com.draga.spaceTravels3.manager.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.draga.spaceTravels3.*;
import com.draga.spaceTravels3.event.CountdownFinishedEvent;
import com.draga.spaceTravels3.event.LoseEvent;
import com.draga.spaceTravels3.event.WinEvent;
import com.draga.spaceTravels3.gameEntity.GameEntity;
import com.draga.spaceTravels3.manager.GameEntityManager;
import com.draga.spaceTravels3.manager.InputManager;
import com.draga.spaceTravels3.manager.SettingsManager;
import com.draga.spaceTravels3.manager.asset.AssMan;
import com.draga.spaceTravels3.physic.PhysicsEngine;
import com.google.common.eventbus.Subscribe;

public class GameScreen implements Screen
{
    private static final String LOGGING_TAG = GameScreen.class.getSimpleName();

    private Screen overlayScreen;
    private Hud hud;
    private ExtendViewport extendViewport;
    private Level level;

    public GameScreen(Level level)
    {
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

        extendViewport.getCamera().position.x = level.getShip().physicsComponent.getPosition().x;
        extendViewport.getCamera().position.y = level.getShip().physicsComponent.getPosition().y;
        updateCamera();

    }

    private void updateCamera()
    {
        // Soften camera movement.
        Vector2 cameraVec = level.getShip().physicsComponent.getPosition();

        Camera camera = extendViewport.getCamera();
        camera.position.x = cameraVec.x;
        camera.position.y = cameraVec.y;
        camera.update();

        SpaceTravels3.spriteBatch.setProjectionMatrix(camera.combined);
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
            SpaceTravels3.getGame().setScreen(new MenuScreen());
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

        // Draw background at shipTexture and parallax 30%.
        /*SpaceTravels3.spriteBatch.draw(
            backgroundTexture,
            -(width / 2f - orthographicCamera.position.x) / 1.3f,
            -(height / 2f - orthographicCamera.position.y) / 1.3f,
            width,
            height);*/

        for (GameEntity gameEntity : GameEntityManager.getGameEntities())
        {
            gameEntity.graphicComponent.draw();
        }
        SpaceTravels3.spriteBatch.end();
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
