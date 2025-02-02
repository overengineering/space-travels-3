package com.draga.spaceTravels3.component.miniMapGraphicComponent;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.draga.spaceTravels3.SpaceTravels3;
import com.draga.spaceTravels3.component.physicsComponent.PhysicsComponent;

public class CircleMiniMapGraphicsComponent extends MiniMapGraphicComponent
{
    private final float radius;

    public CircleMiniMapGraphicsComponent(
        PhysicsComponent physicsComponent,
        Color colour,
        float radius)
    {
        super(physicsComponent,colour);

        this.radius = radius;
    }

    @Override
    public void draw()
    {
        SpaceTravels3.shapeRenderer.set(ShapeRenderer.ShapeType.Line);
        SpaceTravels3.shapeRenderer.setColor(this.colour);

        SpaceTravels3.shapeRenderer.circle(
            this.physicsComponent.getPosition().x,
            this.physicsComponent.getPosition().y,
            radius);
    }

    @Override
    public void dispose()
    {

    }
}
