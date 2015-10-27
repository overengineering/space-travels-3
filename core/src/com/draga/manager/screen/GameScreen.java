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
import com.draga.Constants;
import com.draga.Hud;
import com.draga.MaskBits;
import com.draga.entity.*;
import com.draga.event.ShipPlanetCollisionEvent;
import com.draga.event.StarCollectedEvent;
import com.draga.manager.*;
import com.google.common.eventbus.Subscribe;

import java.util.ArrayList;

public class GameScreen implements Screen
{
    private static final String LOGGING_TAG = GameScreen.class.getSimpleName();
    private Hud                hud;
    private Texture            backgroundTexture;
    private Box2DDebugRenderer box2DDebugRenderer;
    private World              box2dWorld;
    private SpriteBatch        spriteBatch;
    private OrthographicCamera orthographicCamera;
    private ExtendViewport     extendViewport;
    private Ship               ship;
    private int                width;
    private int                height;
    private ArrayList<Planet>  planets;
    private Planet             destinationPlanet;
    private boolean doUpdate = true;

    public GameScreen(String backgroundTexturePath, SpriteBatch spriteBatch, int width, int height)
    {
        Constants.EVENT_BUS.register(this);
        box2dWorld = new World(new Vector2(), true);
        box2dWorld.setContactListener(new GameContactListener());
        planets = new ArrayList<>();
        this.backgroundTexture = AssMan.getAssMan().get(backgroundTexturePath, Texture.class);
        this.width = width;
        this.height = height;
        this.spriteBatch = spriteBatch;
        orthographicCamera = new OrthographicCamera();
        extendViewport = new ExtendViewport(
            Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT, width, height, orthographicCamera);
        extendViewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        if (Constants.IS_DEBUGGING)
        {
            createWalls();
            box2DDebugRenderer = new Box2DDebugRenderer();
        }

        hud = new Hud();
    }

    public Planet getDestinationPlanet()
    {
        return destinationPlanet;
    }

    public void setDestinationPlanet(Planet destinationPlanet)
    {
        this.destinationPlanet = destinationPlanet;
    }

    public World getBox2dWorld()
    {
        return box2dWorld;
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
        addGameEntity(ship);

        orthographicCamera.position.x = ship.getX();
        orthographicCamera.position.y = ship.getY();
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

    private void addGameEntity(GameEntity gameEntity)
    {
        GameEntityManager.addGameEntity(gameEntity);
        gameEntity.createBody(getBox2dWorld());
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

        // On death
        if (ship.isDead())
        {
            ship.setIsDead(false);

            removeGameEntity(ship);
            ScreenManager.setActiveScreen(new LoseScreen(this), false);
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

    public void draw()
    {
        spriteBatch.begin();
        spriteBatch.draw(backgroundTexture, 0, 0, width, height);
        for (GameEntity gameEntity : GameEntityManager.getGameEntities())
        {
            gameEntity.draw(spriteBatch);
        }
        spriteBatch.end();

        if (Constants.IS_DEBUGGING)
        {
            box2DDebugRenderer.render(box2dWorld, orthographicCamera.combined);
        }
    }

    public void resize(int width, int height)
    {
        extendViewport.update(width, height);
    }

    public void dispose()
    {
        for (GameEntity gameEntity : GameEntityManager.getGameEntities())
        {
            gameEntity.dispose();
        }

        GameEntityManager.dispose();
        box2dWorld.dispose();
        backgroundTexture.dispose();
        if (Constants.IS_DEBUGGING)
        {
            box2DDebugRenderer.dispose();
        }
        Constants.EVENT_BUS.unregister(this);
        hud.dispose();
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
            ScreenManager.setActiveScreen(new MenuScreen());
            return;
        }

        if (doUpdate)
        {
            update(deltaTime);
        }
        draw();

        hud.render(deltaTime);
    }

    @Override
    public void pause()
    {

    }

    @Override
    public void resume()
    {

    }

    @Override
    public void hide()
    {

    }

    public void setDoUpdate(boolean doUpdate)
    {
        this.doUpdate = doUpdate;
    }

    @Subscribe
    public void starCollected(StarCollectedEvent starCollectedEvent)
    {
        GameEntityManager.addGameEntityToDestroy(starCollectedEvent.star);
    }

    @Subscribe
    public void shipPlanetCollision(ShipPlanetCollisionEvent shipPlanetCollisionEvent)
    {
        Gdx.app.log(LOGGING_TAG, "Linear velocity on collision: " + ship.getBody().getLinearVelocity().len());

        GameEntity explosion = new Explosion(
            shipPlanetCollisionEvent.ship.getX(), shipPlanetCollisionEvent.ship.getY(), "explosion/explosion.atlas");
        GameEntityManager.addGameEntityToCreate(explosion);

        if (getDestinationPlanet() != shipPlanetCollisionEvent.planet)
        {
            shipPlanetCollisionEvent.ship.setIsDead(true);
        }
        else if (ship.getBody().getLinearVelocity().len() < 15)
        {
            ship.setIsDead(true);
        }
    }
}
