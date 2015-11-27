package com.draga.component.miniMapGraphicComponent;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.draga.component.PhysicsComponent;

public abstract class MiniMapGraphicComponent
{
    protected PhysicsComponent physicsComponent;
    protected Color            colour;

    public abstract void draw(ShapeRenderer shapeRenderer);
}
