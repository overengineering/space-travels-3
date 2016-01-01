package com.draga.spaceTravels3.component.miniMapGraphicComponent;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Pools;
import com.draga.PooledVector2;
import com.draga.spaceTravels3.SpaceTravels3;
import com.draga.spaceTravels3.component.physicsComponent.PhysicsComponent;

public class TriangleMiniMapGraphicComponent extends MiniMapGraphicComponent
{
    private PooledVector2 vertex1;
    private PooledVector2 vertex2;
    private PooledVector2 vertex3;

    public TriangleMiniMapGraphicComponent(
        PhysicsComponent physicsComponent,
        Color color,
        PooledVector2 vertex1,
        PooledVector2 vertex2,
        PooledVector2 vertex3)
    {
        super(physicsComponent,color);

        this.vertex1 = vertex1;
        this.vertex2 = vertex2;
        this.vertex3 = vertex3;
    }

    @Override
    public void draw()
    {
        SpaceTravels3.shapeRenderer.set(ShapeRenderer.ShapeType.Filled);
        SpaceTravels3.shapeRenderer.setColor(this.colour);

        try (
            PooledVector2 vertex1Rotated = vertex1.cpy();
            PooledVector2 vertex2Rotated = vertex2.cpy();
            PooledVector2 vertex3Rotated = vertex3.cpy())
        {
            vertex1Rotated.rotate(this.physicsComponent.getAngle());
            vertex2Rotated.rotate(this.physicsComponent.getAngle());
            vertex3Rotated.rotate(this.physicsComponent.getAngle());

            float x = this.physicsComponent.getPosition().x;
            float y = this.physicsComponent.getPosition().y;
            SpaceTravels3.shapeRenderer.triangle(
                vertex1Rotated.x + x,
                vertex1Rotated.y + y,
                vertex2Rotated.x + x,
                vertex2Rotated.y + y,
                vertex3Rotated.x + x,
                vertex3Rotated.y + y);
        }
    }

    @Override
    public void dispose()
    {
        this.vertex1.close();
        this.vertex2.close();
        this.vertex3.close();
        this.vertex1 = null;
        this.vertex2 = null;
        this.vertex3 = null;
    }
}
