package com.draga.spaceTravels3.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.utils.Pools;
import com.draga.spaceTravels3.*;
import com.draga.spaceTravels3.event.LoseEvent;
import com.draga.spaceTravels3.event.WinEvent;
import com.draga.spaceTravels3.gameEntity.GameEntity;
import com.draga.spaceTravels3.manager.*;
import com.draga.spaceTravels3.manager.asset.AssMan;
import com.draga.spaceTravels3.physic.PhysicDebugDrawer;
import com.draga.spaceTravels3.physic.PhysicsEngine;
import com.draga.spaceTravels3.physic.Projection;
import com.draga.spaceTravels3.physic.ProjectionPoint;
import com.draga.spaceTravels3.ui.Screen;
import com.google.common.eventbus.Subscribe;

import java.util.ArrayList;

public class GameScreen extends Screen
{
    private static final String LOGGING_TAG = GameScreen.class.getSimpleName();

    private final PhysicsComponentBackgroundPositionController shipBackgroundPositionController;

    private Hud hud;

    private Level level;

    private Projection shipProjection;

    public GameScreen(Level level)
    {
        super(true, true);

        this.level = level;

        this.shipBackgroundPositionController =
            new PhysicsComponentBackgroundPositionController(level.getShip().physicsComponent);

        PhysicsEngine.create();
        PhysicsEngine.cachePhysicsComponentCollisions(level.getShip().physicsComponent);
        PhysicsEngine.cacheGravity();

        Constants.General.EVENT_BUS.register(this);

        this.hud = new Hud(this.level);

        // Run a frame to do things like generate a Projection.
        update(0);
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

    @Override
    public void show()
    {
        BackgroundPositionManager.setBackgroundPositionController(this.shipBackgroundPositionController);
        ScreenManager.addScreen(new CountdownScreen());

        Gdx.input.setInputProcessor(new InputAdapter()
        {
            @Override
            public boolean keyUp(int keycode)
            {
                if (keycode == Input.Keys.BACK
                    || keycode == Input.Keys.ESCAPE)
                {
                    ScreenManager.removeScreen(GameScreen.this);
                    return true;
                }

                return false;
            }
        });
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
        SpaceTravels3.gameViewport.apply();

        if (this.shipProjection != null)
        {
            SpaceTravels3.shapeRenderer.setProjectionMatrix(SpaceTravels3.gameViewport.getCamera().combined);
            SpaceTravels3.shapeRenderer.begin();
            this.shipProjection.draw();
            SpaceTravels3.shapeRenderer.end();
        }

        SpaceTravels3.spriteBatch.setProjectionMatrix(SpaceTravels3.gameViewport.getCamera().combined);
        SpaceTravels3.spriteBatch.begin();

        for (GameEntity gameEntity : GameEntityManager.getGameEntities())
        {
            gameEntity.graphicComponent.draw();
        }

        SpaceTravels3.spriteBatch.end();

        if (SettingsManager.getDebugSettings().debugDraw)
        {
            PhysicDebugDrawer.draw();
        }
    }

    private void updateCamera()
    {
        if (!GameEntityManager.getGameEntities().contains(this.level.getShip()))
        {
            return;
        }
        Camera camera = SpaceTravels3.gameViewport.getCamera();

        camera.position.set(
            this.level.getShip().physicsComponent.getPosition().x,
            this.level.getShip().physicsComponent.getPosition().y,
            0f);
        camera.update();

        SpaceTravels3.spriteBatch.setProjectionMatrix(camera.combined);
    }

    public void resize(int width, int height)
    {
        SpaceTravels3.gameViewport.update(width, height);
    }

    @Override
    public void pause()
    {
        this.level.pause();
    }

    @Override
    public void resume()
    {
        this.level.resume();
    }

    @Override
    public void hide()
    {
    }

    @Override
    public void dispose()
    {
        BackgroundPositionManager.setBackgroundPositionController(new RandomBackgroundPositionController());
        GameEntityManager.dispose();
        Constants.General.EVENT_BUS.unregister(this);
        this.hud.dispose();

        this.level.dispose();

        AssMan.getGameAssMan().clear();

        PhysicsEngine.dispose();

        if (this.shipProjection != null)
        {
            Pools.free(this.shipProjection);
        }
    }

    @Subscribe
    public void Lose(LoseEvent loseEvent)
    {
        ScreenManager.addScreen(new LoseScreen(this.level, this));
    }

    @Subscribe
    public void Win(WinEvent winEvent)
    {
        ScreenManager.addScreen(new WinScreen(this.level, this));

        Score score = this.level.getScore();
        ScoreManager.saveHighScore(
            this.level.getId(),
            this.level.getDifficulty(),
            score.getTotalScore());
        Pools.free(score);
    }
}
