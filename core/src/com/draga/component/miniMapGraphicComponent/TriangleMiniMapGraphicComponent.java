package com.draga.component.miniMapGraphicComponent;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.draga.SpaceTravels3;
import com.draga.component.PhysicsComponent;

public class TriangleMiniMapGraphicComponent extends MiniMapGraphicComponent
{
    private final Vector2 vertex1;
    private final Vector2 vertex2;
    private final Vector2 vertex3;

    public TriangleMiniMapGraphicComponent(
        PhysicsComponent physicsComponent,
        Color color,
        Vector2 vertex1,
        Vector2 vertex2,
        Vector2 vertex3)
    {
        this.vertex1 = vertex1;
        this.vertex2 = vertex2;
        this.vertex3 = vertex3;
        this.physicsComponent = physicsComponent;
        this.colour = color;
    }

    @Override
    public void draw()
    {
        SpaceTravels3.shapeRenderer.set(ShapeRenderer.ShapeType.Filled);
        SpaceTravels3.shapeRenderer.setColor(colour);

        Vector2 vertex1Rotated = vertex1.cpy().rotate(this.physicsComponent.getAngle());
        Vector2 vertex2Rotated = vertex2.cpy().rotate(this.physicsComponent.getAngle());
        Vector2 vertex3Rotated = vertex3.cpy().rotate(this.physicsComponent.getAngle());

        SpaceTravels3.shapeRenderer.triangle(
            vertex1Rotated.x + this.physicsComponent.getPosition().x,
            vertex1Rotated.y + this.physicsComponent.getPosition().y,
            vertex2Rotated.x + this.physicsComponent.getPosition().x,
            vertex2Rotated.y + this.physicsComponent.getPosition().y,
            vertex3Rotated.x + this.physicsComponent.getPosition().x,
            vertex3Rotated.y + this.physicsComponent.getPosition().y);
    }
}
