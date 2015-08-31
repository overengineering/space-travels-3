package com.draga;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

/**
 * Created by Administrator on 31/08/2015.
 */
public class World {
    protected Array<GameEntity> gameEntities;SpriteBatch batch;

    public World() {
        gameEntities = new Array<GameEntity>();
        batch = new SpriteBatch();
    }

    public void update(float elapsed){
        for (GameEntity gameEntity: gameEntities){
            gameEntity.update(elapsed);
        }
    }

    public void draw(){
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        for (GameEntity gameEntity: gameEntities){
            gameEntity.draw(batch);
        }
        batch.end();
    }
}
