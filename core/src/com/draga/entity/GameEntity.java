package com.draga.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class GameEntity
{
    public PhysicsComponent physicsComponent;

    public abstract void update(float deltaTime);

    public abstract void draw(SpriteBatch spriteBatch);

    public abstract void dispose();

    public abstract void drawMiniMap();
}
