package com.draga.entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.draga.physic.PhysicsComponent;

public class CircleMiniMapGraphicsComponent extends MiniMapGraphicComponent
{

    private final float radius;

    public CircleMiniMapGraphicsComponent(
        PhysicsComponent physicsComponent,
        Color miniMapColour,
        float radius)
    {
        this.physicsComponent = physicsComponent;
        this.colour = miniMapColour;
        this.radius = radius;
    }

    @Override
    public void draw(ShapeRenderer shapeRenderer)
    {
        shapeRenderer.set(ShapeRenderer.ShapeType.Line);

        shapeRenderer.setColor(this.colour);
        shapeRenderer.circle(
            this.physicsComponent.getPosition().x,
            this.physicsComponent.getPosition().y,
            radius);
    }
}
