package com.draga.scene;

public abstract class Scene
{

    public abstract void render(float deltaTime);

    public abstract void dispose();

    public abstract void pause();

    public abstract void resize(int width, int height);

    public abstract void resume();
}
