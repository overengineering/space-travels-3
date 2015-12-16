package com.draga.spaceTravels3.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
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
import com.draga.spaceTravels3.physic.PhysicDebugDrawer;
import com.draga.spaceTravels3.physic.PhysicsEngine;
import com.google.common.eventbus.Subscribe;

import java.util.ArrayList;

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
    }

    @Override
    public void show()
    {
        this.overlayScreen = new CountdownScreen();

        Constants.General.EVENT_BUS.register(this);

        GameScreenInputProcessor gameScreenInputProcessor = new GameScreenInputProcessor();
        Gdx.input.setInputProcessor(gameScreenInputProcessor);

        extendViewport = new ExtendViewport(
            Constants.Visual.VIEWPORT_WIDTH,
            Constants.Visual.VIEWPORT_HEIGHT);
        extendViewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        updateCamera();

        hud = new Hud(extendViewport.getCamera(), level);
    }

    private void updateCamera()
    {
        Camera camera = extendViewport.getCamera();

        camera.position.set(
            level.getShip().physicsComponent.getPosition().x,
            level.getShip().physicsComponent.getPosition().y,
            0f);
        camera.update();

        SpaceTravels3.spriteBatch.setProjectionMatrix(camera.combined);
    }

    @Override
    public void render(float deltaTime)
    {
        if (Constants.General.IS_DEBUGGING)
        {
            checkDebugKeys();
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

    public void update(float deltaTime)
    {
        InputManager.update();

        PhysicsEngine.update(deltaTime);

        for (GameEntity gameEntity : GameEntityManager.getGameEntities())
        {
            gameEntity.update(deltaTime);
        }
    }

    public void draw()
    {
        updateCamera();
        drawTrajectoryLine();

        SpaceTravels3.spriteBatch.begin();

        for (GameEntity gameEntity : GameEntityManager.getGameEntities())
        {
            gameEntity.graphicComponent.draw();
        }

        SpaceTravels3.spriteBatch.end();

    }

    private void drawTrajectoryLine()
    {
        // TODO: Refactor out when projection points are a class.
        int steps = Constants.Visual.HUD_TRAJECTORY_LINE_STEPS;

        ArrayList<Vector2> projectionPoints =
            PhysicsEngine.projectGravity(
                level.getShip(),
                Constants.Visual.HUD_TRAJECTORY_LINE_STEPS,
                Constants.Visual.HUD_TRAJECTORY_LINE_STEP_TIME);

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        SpaceTravels3.shapeRenderer.setProjectionMatrix(this.extendViewport.getCamera().combined);
        SpaceTravels3.shapeRenderer.begin(ShapeRenderer.ShapeType.Point);
        Color color = projectionPoints.size() == steps
            ? Constants.Visual.HUD_TRAJECTORY_LINE_COLOR_NEUTRAL
            : Constants.Visual.HUD_TRAJECTORY_LINE_COLOR_PLANET;

        for (int i = 1; i < projectionPoints.size(); i += 2)
        {
            float alpha = 1 - ((float) i / steps);
            SpaceTravels3.shapeRenderer.setColor(color.r, color.g, color.b, alpha);

            Vector2 projectionPointA = projectionPoints.get(i);
            Vector2 projectionPointB = projectionPoints.get(i - 1);

            SpaceTravels3.shapeRenderer.line(
                projectionPointA.x, projectionPointA.y,
                projectionPointB.x, projectionPointB.y);
        }
        SpaceTravels3.shapeRenderer.end();

        Gdx.gl.glDisable(GL20.GL_BLEND);
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
