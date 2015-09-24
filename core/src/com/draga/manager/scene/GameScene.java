package com.draga.manager.scene;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.draga.GameWorld;
import com.draga.manager.level.LevelManager;

/**
 * Created by Administrator on 23/09/2015.
 */
public class GameScene extends Scene {
    private GameWorld world;

    public GameScene(String levelPath, SpriteBatch spriteBatch) {
        this.spriteBatch = spriteBatch;
        world = LevelManager.getLevelWorldFromFile(levelPath, spriteBatch);
    }

    @Override public void render(float delta) {
        world.update(delta);
        world.draw();
    }

    @Override public void dispose() {
        world.dispose();
    }

    @Override public void resize(int width, int height) {
        world.resize(width, height);
    }

    @Override public void pause() {

    }

    @Override public void resume() {

    }
}
