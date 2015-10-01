package com.draga.manager.scene;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Pools;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.draga.BodyMaskBit;
import com.draga.Constants;
import com.draga.entity.GameEntity;
import com.draga.entity.Planet;
import com.draga.entity.ship.Ship;
import com.draga.manager.GameContactListener;
import com.draga.manager.GameEntityManager;
import com.draga.manager.SceneManager;

import java.util.ArrayList;

public class GameScene extends Scene
{
    private static final String LOGGING_TAG = GameScene.class.getSimpleName();
    private final Texture backgroundTexture;
    private final Box2DDebugRenderer box2DDebugRenderer;
    private World box2dWorld;
    private SpriteBatch batch;
    private OrthographicCamera orthographicCamera;
    private ExtendViewport extendViewport;
    private Ship ship;
    private int width;
    private int height;
    private ArrayList<Planet> planets;

    public GameScene(String backgroundTexturePath, SpriteBatch spriteBatch, int width, int height)
    {
        box2dWorld = new World(Pools.obtain(Vector2.class), true);
        box2dWorld.setContactListener(new GameContactListener());
        planets = new ArrayList<>();
        FileHandle backgroundFileHandle = Gdx.files.internal(backgroundTexturePath);
        this.backgroundTexture = new Texture(backgroundFileHandle);
        this.width = width;
        this.height = height;
        batch = spriteBatch;
        orthographicCamera = new OrthographicCamera();
        extendViewport = new ExtendViewport(
            Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT, width, height, orthographicCamera);
        extendViewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        if (Constants.IS_DEBUGGING)
        {
            createWalls();
        }

        box2DDebugRenderer = new Box2DDebugRenderer();
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

        Vector2[] wallVertices =
            new Vector2[] {new Vector2(0, 0), new Vector2(width, 0), new Vector2(width, height),
                new Vector2(0, height), new Vector2(0, 0),};
        ChainShape chainShape = new ChainShape();
        chainShape.createChain(wallVertices);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = chainShape;
        fixtureDef.filter.categoryBits = BodyMaskBit.BOUNDARIES;
        fixtureDef.filter.maskBits = BodyMaskBit.SHIP;

        body.createFixture(fixtureDef);

    }

    public void addShip(Ship ship)
    {
        this.ship = ship;
        addGameEntity(ship);
    }

    public void addPlanet(Planet planet)
    {
        this.planets.add(planet);
        addGameEntity(planet);
    }

    private void addGameEntity(GameEntity gameEntity)
    {
        GameEntityManager.addGameEntity(gameEntity);
        BodyDef bodyDef = gameEntity.physicComponent.getBodyDef();
        Body body = box2dWorld.createBody(bodyDef);
        body.createFixture(gameEntity.physicComponent.getFixtureDef());
        gameEntity.physicComponent.setBody(body);
        body.setUserData(gameEntity);
    }

    public void update(float elapsed)
    {
        while (GameEntityManager.getGameEntitiesToCreate().size > 0)
        {
            addGameEntity(GameEntityManager.getGameEntitiesToCreate().pop());
        }
        while (GameEntityManager.getGameEntitiesToDestroy().size > 0)
        {
            removeGameEntity(GameEntityManager.getGameEntitiesToDestroy().pop());
        }

        updateCamera();

        for (GameEntity gameEntity : GameEntityManager.getGameEntities())
        {
            gameEntity.update(elapsed);
        }

        // max frame time to avoid spiral of death (on slow devices)
        float frameTime = Math.min(elapsed, 0.25f);
        box2dWorld.step(frameTime, 6, 2);
    }

    private void removeGameEntity(GameEntity gameEntity)
    {
        GameEntityManager.getGameEntities().removeValue(gameEntity, true);
        gameEntity.dispose();
    }

    private void updateCamera()
    {
        float halfWidth = orthographicCamera.viewportWidth / 2f;
        float halfHeight = orthographicCamera.viewportHeight / 2f;

        float cameraXPosition = MathUtils.clamp(
            ship.physicComponent.getX(), halfWidth, width - halfWidth);
        float cameraYPosition = MathUtils.clamp(
            ship.physicComponent.getY(), halfHeight, height - halfHeight);

        // Soften camera movement.
        Vector3 cameraVec = new Vector3(cameraXPosition, cameraYPosition, 0);
        Vector3 softCamera = cameraVec.cpy();
        Vector3 cameraOffset = cameraVec.sub(orthographicCamera.position);
        softCamera.sub(cameraOffset.scl(0.9f));

        orthographicCamera.position.set(softCamera);
        orthographicCamera.update();

        batch.setProjectionMatrix(orthographicCamera.combined);
    }

    public void draw()
    {
        batch.begin();
        batch.draw(backgroundTexture, 0, 0, width, height);
        for (GameEntity gameEntity : GameEntityManager.getGameEntities())
        {
            gameEntity.draw(batch);
        }
        batch.end();

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
        box2DDebugRenderer.dispose();
    }

    @Override public void render(float delta)
    {
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE))
        {
            SceneManager.setActiveScene(new MenuScene());
            return;
        }

        update(delta);
        draw();
    }

    @Override public void pause()
    {

    }

    @Override public void resume()
    {

    }
}
