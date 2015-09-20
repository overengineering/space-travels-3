package com.draga;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pools;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.draga.ship.Ship;

public class GameWorld
{
    private final Texture backgroundTexture;
    protected Array<GameEntity> gameEntities;
    SpriteBatch batch;
    private OrthographicCamera orthographicCamera;
    private ExtendViewport extendViewport;
    private Ship ship;
    private int width;
    private int height;
    private World box2dWorld;
    private final Box2DDebugRenderer box2DDebugRenderer;

    public GameWorld(String backgroundTexturePath, SpriteBatch spriteBatch, int width, int height)
    {
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
        box2DDebugRenderer = new Box2DDebugRenderer();
    }

    public void addShip(Ship ship)
    {
        this.ship = ship;
        addGameEntity(ship);
    }

    public void addGameEntity(GameEntity gameEntity)
    {
        gameEntities.add(gameEntity);
    }

    public void update(float elapsed)
    {
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

        for (GameEntity gameEntity : gameEntities)
        {
            gameEntity.update(elapsed);
        }

        // max frame time to avoid spiral of death (on slow devices)
        float frameTime = Math.min(elapsed, 0.25f);
        box2dWorld.step(frameTime, 6, 2);
    }

    public void draw()
    {
        batch.begin();
        batch.draw(backgroundTexture, 0, 0, width, height);
        for (GameEntity gameEntity : gameEntities)
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
        extendViewport.update(width, height, true);
        orthographicCamera.update();
    }
}
