package com.draga.manager.scene;

/**
 * Created by Administrator on 23/09/2015.
 */
public abstract class Scene
{

    public abstract void render(float deltaTime);

    public abstract void dispose();

    public abstract void pause();

    public abstract void resize(int width, int height);

    public abstract void resume();
}
