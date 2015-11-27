package com.draga.entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.draga.physic.PhysicsComponent;

public abstract class MiniMapGraphicComponent
{
    protected PhysicsComponent physicsComponent;
    protected Color            colour;

    public abstract void draw(ShapeRenderer shapeRenderer);
}
