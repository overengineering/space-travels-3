package com.draga.spaceTravels3.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.utils.Pools;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.draga.background.Background;
import com.draga.spaceTravels3.*;
import com.draga.spaceTravels3.event.CountdownFinishedEvent;
import com.draga.spaceTravels3.event.LoseEvent;
import com.draga.spaceTravels3.event.WinEvent;
import com.draga.spaceTravels3.gameEntity.GameEntity;
import com.draga.spaceTravels3.manager.GameEntityManager;
import com.draga.spaceTravels3.manager.InputManager;
import com.draga.spaceTravels3.manager.SettingsManager;
import com.draga.spaceTravels3.manager.asset.AssMan;
import com.draga.spaceTravels3.physic.PhysicDebugDrawer;
import com.draga.spaceTravels3.physic.PhysicsEngine;
import com.draga.spaceTravels3.physic.Projection;
import com.draga.spaceTravels3.physic.ProjectionPoint;
import com.google.common.eventbus.Subscribe;

import java.util.ArrayList;

public class GameScreen implements Screen
{
    private static final String LOGGING_TAG = GameScreen.class.getSimpleName();

    private final Background background;
    private       Screen     overlayScreen;

    private Hud hud;

    private ExtendViewport extendViewport;

    private Level level;

    private Projection shipProjection;

    public GameScreen(Level level)
    {
        this.background =
            AssMan.getAssMan().get(Constants.Visual.Background.BACKGROUND_ASSET_DESCRIPTOR);
        this.level = level;

        PhysicsEngine.create();
        PhysicsEngine.cachePhysicsComponentCollisions(level.getShip().physicsComponent);
        PhysicsEngine.cacheGravity();
    }

    @Override
    public void show()
    {
        Constants.General.EVENT_BUS.register(this);

        GameScreenInputProcessor gameScreenInputProcessor = new GameScreenInputProcessor();
        Gdx.input.setInputProcessor(gameScreenInputProcessor);

        this.extendViewport = new ExtendViewport(
            Constants.Visual.VIEWPORT_WIDTH,
            Constants.Visual.VIEWPORT_HEIGHT);
        this.extendViewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        this.hud = new Hud(this.extendViewport.getCamera(), this.level);

        // Run a frame to do things like generate a Projection.
        update(0);

        this.overlayScreen = new CountdownScreen();
    }

    @Override
    public void render(float deltaTime)
    {
        if (Constants.General.IS_DEBUGGING)
        {
            checkDebugKeys();
        }

        if (this.level.getGameState() != GameState.PAUSE
            && this.level.getGameState() != GameState.COUNTDOWN)
        {
            update(deltaTime);
        }

        draw();

        this.hud.render(deltaTime);

        if (this.overlayScreen != null)
        {
            this.overlayScreen.render(deltaTime);
        }

        if (SettingsManager.getDebugSettings().debugDraw)
        {
            PhysicDebugDrawer.draw(this.extendViewport.getCamera());
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
            this.level.getShip().physicsComponent.getVelocity().set(0, 0);
            this.level.getShip().physicsComponent.setAngularVelocity(0);
        }
    }

    public void draw()
    {
        updateCamera();

        if (this.shipProjection != null)
        {
            SpaceTravels3.shapeRenderer.setProjectionMatrix(this.extendViewport.getCamera().combined);
            SpaceTravels3.shapeRenderer.begin();
            this.shipProjection.draw();
            SpaceTravels3.shapeRenderer.end();
        }

        SpaceTravels3.spriteBatch.begin();
        this.background.draw(this.extendViewport.getCamera(), SpaceTravels3.spriteBatch);

        for (GameEntity gameEntity : GameEntityManager.getGameEntities())
        {
            gameEntity.graphicComponent.draw();
        }

        SpaceTravels3.spriteBatch.end();

        if (SettingsManager.getDebugSettings().debugDraw)
        {
            PhysicDebugDrawer.draw(this.extendViewport.getCamera());
        }
    }

    private void updateCamera()
    {
        if (!GameEntityManager.getGameEntities().contains(this.level.getShip()))
        {
            return;
        }
        Camera camera = this.extendViewport.getCamera();

        camera.position.set(
            this.level.getShip().physicsComponent.getPosition().x,
            this.level.getShip().physicsComponent.getPosition().y,
            0f);
        camera.update();

        SpaceTravels3.spriteBatch.setProjectionMatrix(this.extendViewport.getCamera().combined);
    }

    public void resize(int width, int height)
    {
        this.extendViewport.update(width, height);
    }

    @Override
    public void pause()
    {
        this.level.pause();
    }

    @Override
    public void resume()
    {
        if (this.level.getGameState() == GameState.PAUSE)
        {
            this.overlayScreen = new CountdownScreen();
        }

        this.level.resume();
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
        this.hud.dispose();
        if (this.overlayScreen != null)
        {
            this.overlayScreen.dispose();
        }

        this.level.dispose();

        this.background.dispose();

        AssMan.getAssMan().clear();

        PhysicsEngine.dispose();

        if (this.shipProjection != null)
        {
            Pools.free(this.shipProjection);
        }
    }

    public void update(float deltaTime)
    {
        InputManager.update();

        PhysicsEngine.update(deltaTime);

        for (GameEntity gameEntity : GameEntityManager.getGameEntities())
        {
            gameEntity.update(deltaTime);
        }

        updateShipProjection();
        this.hud.getMiniMap().setShipProjection(this.shipProjection);
    }

    private void updateShipProjection()
    {
        if (this.shipProjection != null)
        {
            Pools.free(this.shipProjection);
        }

        if (this.level.getGameState() == GameState.PLAY
            || this.level.getGameState() == GameState.COUNTDOWN
            || this.level.getGameState() == GameState.PAUSE
            )
        {
            ArrayList<ProjectionPoint> projectionPoints = PhysicsEngine.gravityProjection(
                this.level.getShip().physicsComponent,
                this.level.getTrajectorySeconds(),
                Constants.Visual.HUD.TrajectoryLine.POINTS_TIME);

            this.shipProjection = this.level.processProjection(projectionPoints);
        }
        else
        {
            this.shipProjection = null;
        }
    }

    @Subscribe
    public void Lose(LoseEvent loseEvent)
    {
        this.overlayScreen = new LoseScreen(this.level.getId(), this.level.getDifficulty());
    }

    @Subscribe
    public void Win(WinEvent winEvent)
    {
        Score score = this.level.getScore();
        this.overlayScreen = new WinScreen(this.level.getId(), this.level.getDifficulty(), score);
        Pools.free(score);
    }

    @Subscribe
    public void countdownFinished(CountdownFinishedEvent countdownFinishedEvent)
    {
        this.overlayScreen.dispose();
        this.overlayScreen = null;
    }
}
