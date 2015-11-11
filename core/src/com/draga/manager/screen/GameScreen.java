package com.draga.manager.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.draga.*;
import com.draga.entity.*;
import com.draga.event.CountdownFinishedEvent;
import com.draga.event.ShipPlanetCollisionEvent;
import com.draga.event.StarCollectedEvent;
import com.draga.manager.DebugManager;
import com.draga.manager.GameContactListener;
import com.draga.manager.GameEntityManager;
import com.draga.manager.GameManager;
import com.draga.manager.asset.AssMan;
import com.google.common.eventbus.Subscribe;

import java.util.ArrayList;

public class GameScreen implements Screen
{
    private static final String LOGGING_TAG = GameScreen.class.getSimpleName();

    private int width;
    private int height;

    private String levelPath;

    private GameState gameState;
    private Screen    overlayScreen;

    private Hud hud;

    private Texture     backgroundTexture;
    private SpriteBatch spriteBatch;

    private Box2DDebugRenderer box2DDebugRenderer;
    private World              box2dWorld;

    private OrthographicCamera orthographicCamera;
    private ExtendViewport     extendViewport;

    private Ship              ship;
    private ArrayList<Planet> planets;
    private Planet            destinationPlanet;

    private GameScreenInputProcessor gameScreenInputProcessor;

    private int   starsCollected;
    private float elapsedPlayTime;

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

        Constants.EVENT_BUS.register(this);

        gameScreenInputProcessor = new GameScreenInputProcessor();
        Gdx.input.setInputProcessor(gameScreenInputProcessor);

        box2dWorld = new World(new Vector2(), true);
        box2dWorld.setContactListener(new GameContactListener());

        planets = new ArrayList<>();

        orthographicCamera = new OrthographicCamera();
        extendViewport = new ExtendViewport(
            Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT, width, height, orthographicCamera);
        extendViewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        if (Constants.IS_DEBUGGING)
        {
            createWalls();
        }
        if (Constants.DEBUG_DRAW)
        {
            box2DDebugRenderer = new Box2DDebugRenderer();
        }

        hud = new Hud(orthographicCamera);
    }

    private void createWalls()
    {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;

        Body body = box2dWorld.createBody(bodyDef);

        Vector2[] wallVertices = new Vector2[] {
            new Vector2(0, 0),
            new Vector2(width, 0),
            new Vector2(width, height),
            new Vector2(0, height),
            new Vector2(0, 0),};
        ChainShape chainShape = new ChainShape();
        chainShape.createChain(wallVertices);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = chainShape;
        fixtureDef.restitution = 0;
        fixtureDef.filter.categoryBits = MaskBits.BOUNDARIES;
        fixtureDef.filter.maskBits = MaskBits.SHIP;

        body.createFixture(fixtureDef);

    }

    public void addShip(Ship ship)
    {
        this.ship = ship;
        hud.setShip(ship);
        addGameEntity(ship);

        orthographicCamera.position.x = ship.getX();
        orthographicCamera.position.y = ship.getY();

        updateCamera();
    }

    private void addGameEntity(GameEntity gameEntity)
    {
        GameEntityManager.getGameEntities().add(gameEntity);
        gameEntity.createBody(getBox2dWorld());
    }

    private void updateCamera()
    {
        float halfWidth = orthographicCamera.viewportWidth / 2f;
        float halfHeight = orthographicCamera.viewportHeight / 2f;

        float cameraXPosition = MathUtils.clamp(
            ship.getX(), halfWidth, width - halfWidth);
        float cameraYPosition = MathUtils.clamp(
            ship.getY(), halfHeight, height - halfHeight);

        // Soften camera movement.
        Vector2 cameraVec = new Vector2(cameraXPosition, cameraYPosition);
        Vector2 softCamera = cameraVec.cpy();
        Vector2 cameraOffset =
            cameraVec.sub(orthographicCamera.position.x, orthographicCamera.position.y);
        softCamera.sub(cameraOffset.scl(0.9f));

        orthographicCamera.position.x = softCamera.x;
        orthographicCamera.position.y = softCamera.y;
        orthographicCamera.update();

        spriteBatch.setProjectionMatrix(orthographicCamera.combined);
    }

    public World getBox2dWorld()
    {
        return box2dWorld;
    }

    public void addPlanet(Planet planet)
    {
        this.planets.add(planet);
        addGameEntity(planet);
    }

    public void addStar(Star star)
    {
        addGameEntity(star);
        hud.addStar();
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

        while (!GameEntityManager.getGameEntitiesToCreate().isEmpty())
        {
            addGameEntity(GameEntityManager.getGameEntitiesToCreate().poll());
        }
        while (!GameEntityManager.getGameEntitiesToDestroy().isEmpty())
        {
            removeGameEntity(GameEntityManager.getGameEntitiesToDestroy().poll());
        }

        updateCamera();

        for (GameEntity gameEntity : GameEntityManager.getGameEntities())
        {
            gameEntity.update(elapsed);
        }

        // TODO: investigate this
        // max frame time to avoid spiral of death (on slow devices)
        float frameTime = Math.min(elapsed, 0.25f);
        box2dWorld.step(frameTime, 6, 2);
    }

    public void draw()
    {
        MiniMap.getShapeRenderer().begin();

        spriteBatch.begin();
        spriteBatch.draw(backgroundTexture, 0, 0, width, height);
        for (GameEntity gameEntity : GameEntityManager.getGameEntities())
        {
            gameEntity.draw(spriteBatch);
        }
        spriteBatch.end();

        MiniMap.getShapeRenderer().end();

        if (Constants.DEBUG_DRAW)
        {
            box2DDebugRenderer.render(box2dWorld, orthographicCamera.combined);
        }
    }

    private void checkDebugKeys()
    {
        if (Gdx.input.isKeyJustPressed(Input.Keys.F1))
        {
            DebugManager.infiniteFuel = !DebugManager.infiniteFuel;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.F2))
        {
            DebugManager.noGravity = !DebugManager.noGravity;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.F3))
        {
            ship.getBody().setLinearVelocity(0, 0);
            ship.getBody().setAngularVelocity(0);
        }
    }

    private void removeGameEntity(GameEntity gameEntity)
    {
        box2dWorld.destroyBody(gameEntity.getBody());
        GameEntityManager.getGameEntities().remove(gameEntity);
        gameEntity.dispose();
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
        for (GameEntity gameEntity : GameEntityManager.getGameEntities())
        {
            gameEntity.dispose();
        }

        GameEntityManager.dispose();
        box2dWorld.dispose();
        backgroundTexture.dispose();
        if (Constants.DEBUG_DRAW)
        {
            box2DDebugRenderer.dispose();
        }
        Constants.EVENT_BUS.unregister(this);
        hud.dispose();
        if (this.overlayScreen != null)
        {
            this.overlayScreen.dispose();
        }
    }

    @Subscribe
    public void starCollected(StarCollectedEvent starCollectedEvent)
    {
        starsCollected++;
        GameEntityManager.getGameEntitiesToDestroy().add(starCollectedEvent.star);
    }

    @Subscribe
    public void shipPlanetCollision(ShipPlanetCollisionEvent shipPlanetCollisionEvent)
    {
        if (Constants.IS_DEBUGGING)
        {
            Gdx.app.debug(
                LOGGING_TAG,
                "Linear velocity on collision: " + ship.getBody().getLinearVelocity().len());
        }

        GameEntityManager.getGameEntitiesToDestroy().add(ship);

        // If wrong planet or too fast then lose.
        if (getDestinationPlanet() != shipPlanetCollisionEvent.planet
            || ship.getBody().getLinearVelocity().len()
            > Constants.MAX_DESTINATION_PLANET_APPROACH_SPEED)
        {
            gameState = GameState.LOSE;
            GameEntity explosion = new Explosion(
                shipPlanetCollisionEvent.ship.getX(),
                shipPlanetCollisionEvent.ship.getY(),
                AssMan.getAssList().explosion);
            GameEntityManager.getGameEntitiesToCreate().add(explosion);

            this.overlayScreen = new LoseScreen(levelPath);
        }
        else
        {
            float score = getScore();
            gameState = GameState.WIN;
            this.overlayScreen = new WinScreen(levelPath, score);
        }
    }

    public Planet getDestinationPlanet()
    {
        return destinationPlanet;
    }

    public void setDestinationPlanet(Planet destinationPlanet)
    {
        this.destinationPlanet = destinationPlanet;
    }

    private float getScore()
    {
        float starPoints = starsCollected * Constants.STAR_POINTS;
        float timePoints = elapsedPlayTime * Constants.TIME_POINTS;
        float fuelPoints = ship.getFuel() * Constants.FUEL_POINTS;

        String message = String.format(
            "Points summary: %f stars - %f time + %f fuel",
            starPoints,
            timePoints,
            fuelPoints);
        Gdx.app.log(LOGGING_TAG, message);

        float score = starPoints;
        score -= timePoints;
        score += fuelPoints;
        return score;
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
