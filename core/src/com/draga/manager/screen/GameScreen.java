package com.draga.manager.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.draga.*;
import com.draga.event.CountdownFinishedEvent;
import com.draga.event.PickupCollectedEvent;
import com.draga.event.ShipPlanetCollisionEvent;
import com.draga.gameEntity.*;
import com.draga.manager.GameEntityManager;
import com.draga.manager.GameManager;
import com.draga.manager.InputManager;
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

    private Texture backgroundTexture;

    private OrthographicCamera orthographicCamera;
    private ExtendViewport     extendViewport;

    private Ship              ship;
    private Thruster          thruster;
    private ArrayList<Planet> planets;
    private Planet destinationPlanet;

    private GameScreenInputProcessor gameScreenInputProcessor;

    private int   pickupCollected;
    private float elapsedPlayTime;

    private Sound pickupCollectedSound;

    public GameScreen(
        String backgroundTexturePath,
        int width,
        int height,
        String levelPath)
    {
        this.backgroundTexture = AssMan.getAssMan().get(backgroundTexturePath);
        this.width = width;
        this.height = height;
        this.levelPath = levelPath;

        this.gameState = GameState.COUNTDOWN;
        this.overlayScreen = new CountdownScreen();

        Constants.General.EVENT_BUS.register(this);

        gameScreenInputProcessor = new GameScreenInputProcessor();
        Gdx.input.setInputProcessor(gameScreenInputProcessor);

        planets = new ArrayList<>();

        orthographicCamera = new OrthographicCamera();
        extendViewport = new ExtendViewport(
            Constants.Visual.VIEWPORT_WIDTH, Constants.Visual.VIEWPORT_HEIGHT, width, height, orthographicCamera);
        extendViewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        if (SettingsManager.getDebugSettings().debugDraw)
        {
            physicDebugDrawer = new PhysicDebugDrawer();
        }

        hud = new Hud(orthographicCamera, width, height);

        pickupCollectedSound = AssMan.getAssMan().get(AssMan.getAssList().pickupCollectSound);
    }

    public void addShip(Ship ship)
    {
        this.ship = ship;
        hud.setShip(ship);

        orthographicCamera.position.x = ship.physicsComponent.getPosition().x;
        orthographicCamera.position.y = ship.physicsComponent.getPosition().y;

        updateCamera();

        thruster = new Thruster(ship);

        // Adds the thruster first so that is drawn below the shipTexture.
        GameEntityManager.addGameEntity(thruster);
        GameEntityManager.addGameEntity(ship);
    }

    private void updateCamera()
    {
        // Soften camera movement.
        Vector2 cameraVec = ship.physicsComponent.getPosition();

        orthographicCamera.position.x = cameraVec.x;
        orthographicCamera.position.y = cameraVec.y;
        orthographicCamera.update();

        SpaceTravels3.spriteBatch.setProjectionMatrix(orthographicCamera.combined);
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
        if (Constants.General.IS_DEBUGGING)
        {
            checkDebugKeys();
        }

        InputManager.update();

        for (GameEntity gameEntity : GameEntityManager.getGameEntities())
        {
            gameEntity.update(elapsed);
        }

        PhysicsEngine.update(elapsed);
        hud.setScore(getScore());
    }

    public void draw()
    {
        updateCamera();

        SpaceTravels3.spriteBatch.begin();

        // Draw background at shipTexture and parallax 30%.
        SpaceTravels3.spriteBatch.draw(
            backgroundTexture,
            -(width / 2f - orthographicCamera.position.x) / 1.3f,
            -(height / 2f - orthographicCamera.position.y) / 1.3f,
            width,
            height);

        for (GameEntity gameEntity : GameEntityManager.getGameEntities())
        {
            gameEntity.graphicComponent.draw();
        }
        SpaceTravels3.spriteBatch.end();

        if (SettingsManager.getDebugSettings().debugDraw)
        {
            physicDebugDrawer.draw(orthographicCamera);
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
            ship.physicsComponent.getVelocity().set(0, 0);
            ship.physicsComponent.setAngularVelocity(0);
        }
    }

    private int getScore()
    {
        float pickupPoints = pickupCollected * Constants.Game.PICKUP_POINTS;
        float timePoints = elapsedPlayTime * Constants.Game.TIME_POINTS;
        // TODO: Max fuel can be all over the place. Percentage of remaining?
        float fuelPoints = ship.getCurrentFuel() * Constants.Game.FUEL_POINTS;

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
        Constants.General.EVENT_BUS.unregister(this);
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
        pickupCollectedSound.play(SettingsManager.getSettings().volume);
        GameEntityManager.removeGameEntity(pickupCollectedEvent.pickup);
    }

    @Subscribe
    public void shipPlanetCollision(ShipPlanetCollisionEvent shipPlanetCollisionEvent)
    {
        if (Constants.General.IS_DEBUGGING)
        {
            Gdx.app.debug(
                LOGGING_TAG,
                "Linear velocity on collision: " + ship.physicsComponent.getVelocity().len());
        }

        GameEntityManager.removeGameEntity(ship);
        GameEntityManager.removeGameEntity(thruster);

        // If wrong planet or too fast then lose.
        if (!(shipPlanetCollisionEvent.planet.equals(destinationPlanet))
            || ship.physicsComponent.getVelocity().len()
            > Constants.Game.MAX_DESTINATION_PLANET_APPROACH_SPEED)
        {
            gameState = GameState.LOSE;
            GameEntity explosion = new Explosion(
                shipPlanetCollisionEvent.ship.physicsComponent.getPosition().x,
                shipPlanetCollisionEvent.ship.physicsComponent.getPosition().y,
                shipPlanetCollisionEvent.ship.graphicComponent.getWidth(),
                shipPlanetCollisionEvent.ship.graphicComponent.getHeight()
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

    public void setDestinationPlanet(Planet destinationPlanet)
    {
        this.destinationPlanet = destinationPlanet;
    }
}
