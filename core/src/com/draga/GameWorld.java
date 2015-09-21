package com.draga;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pools;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.draga.ship.Ship;

public class GameWorld {
    private static final String LOGGING_TAG = GameWorld.class.getSimpleName();
    private static final float GRAVITY_MULTIPLIER = 10f;
    public static World box2dWorld;
    private final Texture backgroundTexture;
    private final Box2DDebugRenderer box2DDebugRenderer;
    protected Array<GameEntity> gameEntities;
    SpriteBatch batch;
    private OrthographicCamera orthographicCamera;
    private ExtendViewport extendViewport;
    private Ship ship;
    private int width;
    private int height;

    public GameWorld(String backgroundTexturePath, SpriteBatch spriteBatch, int width, int height) {
        box2dWorld = new World(Pools.obtain(Vector2.class), true);
        FileHandle backgroundFileHandle = Gdx.files.internal(backgroundTexturePath);
        this.backgroundTexture = new Texture(backgroundFileHandle);
        this.width = width;
        this.height = height;
        gameEntities = new Array<GameEntity>();
        batch = spriteBatch;
        orthographicCamera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        extendViewport = new ExtendViewport(
            Constants.VIEWPORT_WIDTH,
            Constants.VIEWPORT_HEIGHT,
            width,
            height,
            orthographicCamera);

        if (Constants.IS_DEBUGGING) {
            createWalls();
        }

        box2DDebugRenderer = new Box2DDebugRenderer();
    }

    private void createWalls() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;

        Body body = GameWorld.box2dWorld.createBody(bodyDef);

        Vector2[] wallVertices = new Vector2[]{
            new Vector2(0, 0),
            new Vector2(width, 0),
            new Vector2(width, height),
            new Vector2(0, height),
            new Vector2(0, 0),
        };
        ChainShape chainShape = new ChainShape();
        chainShape.createChain(wallVertices);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = chainShape;

        body.createFixture(fixtureDef);

    }

    public void addShip(Ship ship) {
        this.ship = ship;
        addGameEntity(ship);
    }

    public void addGameEntity(GameEntity gameEntity) {
        gameEntities.add(gameEntity);
    }

    public void update(float elapsed) {
        Vector2 gravity = Pools.obtain(Vector2.class);
        switch (Gdx.input.getRotation()) {
            case 0:
                gravity.x = Gdx.input.getAccelerometerX();
                gravity.y = Gdx.input.getAccelerometerY();
                break;
            case 90:
                gravity.x = Gdx.input.getAccelerometerY();
                gravity.y = -Gdx.input.getAccelerometerX();
                break;
            case 180:
                gravity.x = -Gdx.input.getAccelerometerX();
                gravity.y = -Gdx.input.getAccelerometerY();
                break;
            case 270:
                gravity.x = -Gdx.input.getAccelerometerY();
                gravity.y = Gdx.input.getAccelerometerX();
                break;
            default:
                Gdx.app.error(LOGGING_TAG, "Orientation " + Gdx.input.getRotation() + " not implemented.");
        }
        gravity = gravity
            // Max the gravity by the Earth gravity to avoid excessive force being applied if the device is shaken
            .clamp(0, Constants.EARTH_GRAVITY)
            .scl(GRAVITY_MULTIPLIER);
        box2dWorld.setGravity(gravity);


        float halfWidth = orthographicCamera.viewportWidth / 2f;
        float halfHeight = orthographicCamera.viewportHeight / 2f;

        float cameraXPosition = MathUtils.clamp(
            ship.physicComponent.getX(),
            halfWidth,
            width - halfWidth);
        float cameraYPosition = MathUtils.clamp(
            ship.physicComponent.getY(),
            halfHeight,
            height - halfHeight);
        orthographicCamera.position.x = cameraXPosition;
        orthographicCamera.position.y = cameraYPosition;
        orthographicCamera.update();
        batch.setProjectionMatrix(orthographicCamera.combined);

        for (GameEntity gameEntity : gameEntities) {
            gameEntity.update(elapsed);
        }

        // max frame time to avoid spiral of death (on slow devices)
        float frameTime = Math.min(elapsed, 0.25f);
        box2dWorld.step(frameTime, 6, 2);
    }

    public void draw() {
        batch.begin();
        batch.draw(backgroundTexture, 0, 0, width, height);
        for (GameEntity gameEntity : gameEntities) {
            gameEntity.draw(batch);
        }
        batch.end();

        if (Constants.IS_DEBUGGING) {
            box2DDebugRenderer.render(box2dWorld, orthographicCamera.combined);
        }
    }

    public void resize(int width, int height) {
        extendViewport.update(width, height, true);
        orthographicCamera.update();
    }
}
