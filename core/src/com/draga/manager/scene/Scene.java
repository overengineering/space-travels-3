package com.draga.manager.scene;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Created by Administrator on 23/09/2015.
 */
public abstract class Scene {

    public abstract void render(float deltaTime);

    public abstract void dispose();

    public abstract void pause();

    public abstract void resize(int width, int height);

    public abstract void resume();
}
