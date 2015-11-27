package com.draga.component.miniMapGraphicComponent;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.draga.component.PhysicsComponent;

public class StarMiniMapGraphicComponent extends MiniMapGraphicComponent
{
    private final float   radius;
    private       Vector2 vertex1;
    private       Vector2 vertex2;
    private       Vector2 vertex3;

    public StarMiniMapGraphicComponent(PhysicsComponent physicsComponent, Color color, float radius)
    {

        this.physicsComponent = physicsComponent;
        this.colour = color;
        this.radius = radius;

        vertex1 = new Vector2(0, radius);
        vertex2 = new Vector2(radius / 2, 0);
        vertex3 = new Vector2(-radius / 2, 0);
    }

    @Override
    public void draw(ShapeRenderer shapeRenderer)
    {
        shapeRenderer.set(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.GOLDENROD);

        Vector2 vertex1Rotated = vertex1.cpy();
        Vector2 vertex2Rotated = vertex2.cpy();
        Vector2 vertex3Rotated = vertex3.cpy();

        for (int i = 0; i < 5; i++)
        {
            vertex1Rotated.rotate(360 / 5);
            vertex2Rotated.rotate(360 / 5);
            vertex3Rotated.rotate(360 / 5);

            shapeRenderer.triangle(
                vertex1Rotated.x + this.physicsComponent.getPosition().x,
                vertex1Rotated.y + this.physicsComponent.getPosition().y,
                vertex2Rotated.x + this.physicsComponent.getPosition().x,
                vertex2Rotated.y + this.physicsComponent.getPosition().y,
                vertex3Rotated.x + this.physicsComponent.getPosition().x,
                vertex3Rotated.y + this.physicsComponent.getPosition().y);
        }
    }
}
