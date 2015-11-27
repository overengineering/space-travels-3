package com.draga.manager.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pools;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.draga.*;
import com.draga.entity.*;
import com.draga.entity.shape.Circle;
import com.draga.event.CountdownFinishedEvent;
import com.draga.event.ScoreEvent;
import com.draga.event.ShipPlanetCollisionEvent;
import com.draga.event.PickupCollectedEvent;
import com.draga.manager.GameEntityManager;
import com.draga.manager.GameManager;
import com.draga.manager.SettingsManager;
import com.draga.manager.asset.AssMan;
import com.draga.physic.PhysicDebugDrawer;
import com.draga.physic.PhysicsEngine;
import com.google.common.eventbus.Subscribe;

import java.util.ArrayList;

public class GameScreen implements Screen
{
    private static final String LOGGING_TAG = GameScreen.class.getSimpleName();

    private PhysicDebugDrawer physicDebugDrawer;

    private int width;
    private int height;

    private String levelPath;

    private GameState gameState;
    private Screen    overlayScreen;

    private Hud hud;

    private Texture     backgroundTexture;
    private SpriteBatch spriteBatch;

    private OrthographicCamera orthographicCamera;
    private ExtendViewport     extendViewport;

    private Ship              ship;
    private ArrayList<Planet> planets;

    private GameScreenInputProcessor gameScreenInputProcessor;

    private int   pickupCollected;
    private float elapsedPlayTime;

    private Sound pickupCollectedSound;

    public GameScreen(
        String backgroundTexturePath,
        SpriteBatch spriteBatch,
        int width,
        int height,
        String levelPath)
    {
        this.backgroundTexture = AssMan.getAssMan().get(backgroundTexturePath, Texture.class);
        this.width = width;
        this.height = height;
        this.spriteBatch = spriteBatch;
        this.levelPath = levelPath;

        this.gameState = GameState.COUNTDOWN;
        this.overlayScreen = new CountdownScreen();

        MiniMap.setWorldSize(width, height);

        Constants.EVENT_BUS.register(this);

        gameScreenInputProcessor = new GameScreenInputProcessor();
        Gdx.input.setInputProcessor(gameScreenInputProcessor);

        planets = new ArrayList<>();

        orthographicCamera = new OrthographicCamera();
        extendViewport = new ExtendViewport(
            Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT, width, height, orthographicCamera);
        extendViewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        //
        //        if (Constants.IS_DEBUGGING)
        //        {
        //            createWalls();
        //        }
        if (SettingsManager.debugDraw)
        {
            physicDebugDrawer = new PhysicDebugDrawer();
        }

        hud = new Hud(orthographicCamera);

        pickupCollectedSound = AssMan.getAssMan().get(AssMan.getAssList().pickupCollectSound);
    }

    public void addShip(Ship ship)
    {
        this.ship = ship;
        hud.setShip(ship);

        orthographicCamera.position.x = ship.physicsComponent.getPosition().x;
        orthographicCamera.position.y = ship.physicsComponent.getPosition().y;

        updateCamera();

        Thruster thruster = new Thruster(ship);
        GameEntityManager.addGameEntity(thruster);
        GameEntityManager.addGameEntity(ship);
    }

    private void updateCamera()
    {
        // Soften camera movement.
        Vector2 cameraVec = new Vector2(ship.physicsComponent.getPosition().x, ship.physicsComponent.getPosition().y);
        Vector2 softCamera = cameraVec.cpy();
        Vector2 cameraOffset =
            cameraVec.sub(orthographicCamera.position.x, orthographicCamera.position.y);
        softCamera.sub(cameraOffset.scl(0.9f));

        orthographicCamera.position.x = softCamera.x;
        orthographicCamera.position.y = softCamera.y;
        orthographicCamera.update();

        spriteBatch.setProjectionMatrix(orthographicCamera.combined);
    }

    public void addPlanet(Planet planet)
    {
        this.planets.add(planet);
        GameEntityManager.addGameEntity(planet);
    }

    public void addPickup(Pickup pickup)
    {
        GameEntityManager.addGameEntity(pickup);
        hud.addPickup();
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

        if (gameState == GameState.PLAY)
        {
            elapsedPlayTime += deltaTime;
        }

        if (gameState != GameState.PAUSE
            && gameState != GameState.COUNTDOWN)
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
        if (Constants.IS_DEBUGGING)
        {
            checkDebugKeys();
        }


        for (GameEntity gameEntity : GameEntityManager.getGameEntities())
        {
            gameEntity.update(elapsed);
            gameEntity.graphicComponent.update(elapsed);
        }

        PhysicsEngine.update(elapsed);
        updateScore();
    }

    private void updateMiniMap()
    {
        Rectangle shipRect = new Rectangle(
            ship.physicsComponent.getPosition().x - ((Circle)ship.physicsComponent.getShape()).radius,
            ship.physicsComponent.getPosition().y - ((Circle)ship.physicsComponent.getShape()).radius,
            ((Circle)ship.physicsComponent.getShape()).radius * 2,
            ((Circle)ship.physicsComponent.getShape()).radius * 2);

        Rectangle worldRect = new Rectangle(0, 0, this.width, this.height);
        MiniMap.update(shipRect, worldRect);
    }

    public void draw()
    {
        updateCamera();

        spriteBatch.begin();
        spriteBatch.draw(backgroundTexture, 0, 0, width, height);
        spriteBatch.end();

        updateMiniMap();

        spriteBatch.begin();
        for (GameEntity gameEntity : GameEntityManager.getGameEntities())
        {
            gameEntity.graphicComponent.draw(spriteBatch);
        }
        spriteBatch.end();

        if (SettingsManager.debugDraw)
        {
            physicDebugDrawer.draw(orthographicCamera);
        }
    }

    private void checkDebugKeys()
    {
        if (Gdx.input.isKeyJustPressed(Input.Keys.F1))
        {
            SettingsManager.infiniteFuel = !SettingsManager.infiniteFuel;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.F2))
        {
            SettingsManager.noGravity = !SettingsManager.noGravity;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.F3))
        {
            ship.physicsComponent.getVelocity().set(0, 0);
            //ship.physicsComponent.setAngularVelocity(0);
        }
    }

    private void updateScore()
    {
        ScoreEvent scoreEvent = Pools.obtain(ScoreEvent.class);
        scoreEvent.setScore(getScore());
        Constants.EVENT_BUS.post(scoreEvent);
        Pools.free(scoreEvent);
    }

    private int getScore()
    {
        float pickupPoints = pickupCollected * Constants.PICKUP_POINTS;
        float timePoints = elapsedPlayTime * Constants.TIME_POINTS;
        float fuelPoints = ship.getFuel() * Constants.FUEL_POINTS;

        float score = pickupPoints;
        score -= timePoints;
        score += fuelPoints;
        return Math.round(score);
    }

    public void resize(int width, int height)
    {
        extendViewport.update(width, height);
    }

    @Override
    public void pause()
    {
        if (gameState == GameState.PLAY
            || gameState == GameState.COUNTDOWN)
        {
            gameState = GameState.PAUSE;
        }
    }

    @Override
    public void resume()
    {
        if (gameState == GameState.PAUSE)
        {
            gameState = GameState.COUNTDOWN;
            this.overlayScreen = new CountdownScreen();
        }
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

        if (SettingsManager.debugDraw)
        {
            physicDebugDrawer.dispose();
        }

        Constants.EVENT_BUS.unregister(this);
        hud.dispose();
        if (this.overlayScreen != null)
        {
            this.overlayScreen.dispose();
        }

        pickupCollectedSound.stop();
        pickupCollectedSound.dispose();

        AssMan.getAssMan().clear();
    }

    @Subscribe
    public void pickupCollected(PickupCollectedEvent pickupCollectedEvent)
    {
        pickupCollected++;
        pickupCollectedSound.play();
        GameEntityManager.removeGameEntity(pickupCollectedEvent.pickup);
    }

    @Subscribe
    public void shipPlanetCollision(ShipPlanetCollisionEvent shipPlanetCollisionEvent)
    {
        if (Constants.IS_DEBUGGING)
        {
            Gdx.app.debug(
                LOGGING_TAG,
                "Linear velocity on collision: " + ship.physicsComponent.getVelocity().len());
        }

        GameEntityManager.removeGameEntity(ship);

        // If wrong planet or too fast then lose.
        if (!shipPlanetCollisionEvent.planet.isDestination()
            || ship.physicsComponent.getVelocity().len()
            > Constants.MAX_DESTINATION_PLANET_APPROACH_SPEED)
        {
            gameState = GameState.LOSE;
            GameEntity explosion = new Explosion(
                shipPlanetCollisionEvent.ship.physicsComponent.getPosition().x,
                shipPlanetCollisionEvent.ship.physicsComponent.getPosition().y
            );
            GameEntityManager.addGameEntity(explosion);

            this.overlayScreen = new LoseScreen(levelPath);
        }
        else
        {
            int score = getScore();
            gameState = GameState.WIN;
            this.overlayScreen = new WinScreen(levelPath, score);
        }
    }

    public String getLevelPath()
    {
        return levelPath;
    }

    @Subscribe
    public void countdownFinished(CountdownFinishedEvent countdownFinishedEvent)
    {
        if (gameState == GameState.COUNTDOWN)
        {
            this.gameState = GameState.PLAY;
        }
        this.overlayScreen.dispose();
        this.overlayScreen = null;
    }
}
