package com.draga;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.Array;

/**
 * Created by Administrator on 31/08/2015.
 */
public class World {
    private final Texture backgroundTexture;
    protected Array<GameEntity> gameEntities;SpriteBatch batch;

    public World(String backgroundTexturePath, SpriteBatch spriteBatch) {
        FileHandle backgroundFileHandle = Gdx.files.internal(backgroundTexturePath);
        this.backgroundTexture = new Texture(backgroundFileHandle);
        gameEntities = new Array<GameEntity>();
        batch = spriteBatch;
    }

    public void addGameEntity(GameEntity gameEntity)
    {
        gameEntities.add(gameEntity);
    }

    public void update(float elapsed){
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
            800,
            480,
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
