package com.draga;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.draga.ship.Ship;

public class GameWorld
{
    private final Texture backgroundTexture;
    protected Array<GameEntity> gameEntities;
    SpriteBatch batch;
    private OrthographicCamera orthographicCamera;
    private Ship ship;
    private int width;
    private int height;

    public GameWorld(String backgroundTexturePath, SpriteBatch spriteBatch, int width, int height) {
        FileHandle backgroundFileHandle = Gdx.files.internal(backgroundTexturePath);
        this.backgroundTexture = new Texture(backgroundFileHandle);
        this.width = width;
        this.height = height;
        gameEntities = new Array<GameEntity>();
        batch = spriteBatch;
        orthographicCamera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
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
    }

    public void draw()
    {
        batch.begin();
        batch.draw(
            backgroundTexture,
            0,
            0,
            0,
            0,
            width,
            height,
            1,
            1,
            0,
            0,
            0,
            backgroundTexture.getWidth(),
            backgroundTexture.getHeight(),
            false,
            false);
        for (GameEntity gameEntity : gameEntities)
        {
            gameEntity.draw(batch);
        }
        batch.end();
    }

    public void resize(int width, int height)
    {
        orthographicCamera.viewportWidth = width;
        orthographicCamera.viewportHeight = height;
    }
}
