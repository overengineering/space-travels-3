package com.draga;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.draga.ship.Ship;

/**
 * Created by Administrator on 31/08/2015.
 */
public class World {
    private final Texture backgroundTexture;
    protected Array<GameEntity> gameEntities;SpriteBatch batch;
    private OrthographicCamera orthographicCamera;
    private Ship ship;
    private float width;
    private float height;

    public World(String backgroundTexturePath, SpriteBatch spriteBatch, float width, float height) {
        FileHandle backgroundFileHandle = Gdx.files.internal(backgroundTexturePath);
        this.backgroundTexture = new Texture(backgroundFileHandle);
        this.width = width;
        this.height = height;
        gameEntities = new Array<GameEntity>();
        batch = spriteBatch;
        orthographicCamera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    public void addShip(Ship ship){
        this.ship = ship;
        addGameEntity(ship);
    }

    public void addGameEntity(GameEntity gameEntity)
    {
        gameEntities.add(gameEntity);
    }

    public void update(float elapsed){
        float cameraXPosition = MathUtils.clamp(
            ship.physicComponent.getX(),
            orthographicCamera.viewportWidth / 2f,
            width - (orthographicCamera.viewportWidth / 2f));
        float cameraYPosition = MathUtils.clamp(
            ship.physicComponent.getY(),
            orthographicCamera.viewportHeight / 2f,
            height - (orthographicCamera.viewportHeight / 2f));
        orthographicCamera.position.x = cameraXPosition;
        orthographicCamera.position.y = cameraYPosition;
        orthographicCamera.update();
        batch.setProjectionMatrix(orthographicCamera.combined);

        for (GameEntity gameEntity: gameEntities){
            gameEntity.update(elapsed);
        }
    }

    public void draw(){
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
        for (GameEntity gameEntity: gameEntities){
            gameEntity.draw(batch);
        }
        batch.end();
    }
}
