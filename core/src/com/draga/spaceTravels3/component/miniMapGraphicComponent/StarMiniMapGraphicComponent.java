package com.draga.spaceTravels3.component.miniMapGraphicComponent;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.draga.spaceTravels3.SpaceTravels3;
import com.draga.spaceTravels3.component.PhysicsComponent;

public class StarMiniMapGraphicComponent extends MiniMapGraphicComponent
{
    public static final int POINTS        = 5;
    public static final int POINT_DEGREES = 360 / POINTS;

    // Vertex of the triangle of a point, they will then get rotated and drawn again.
    private Vector2 vertex1;
    private Vector2 vertex2;
    private Vector2 vertex3;

    public StarMiniMapGraphicComponent(PhysicsComponent physicsComponent, Color colour, float radius)
    {
        super(physicsComponent, colour);

        vertex1 = new Vector2(0, radius);
        vertex2 = new Vector2(radius / 2, 0);
        vertex3 = new Vector2(-radius / 2, 0);
    }

    @Override
    public void draw()
    {
        SpaceTravels3.shapeRenderer.set(ShapeRenderer.ShapeType.Filled);
        SpaceTravels3.shapeRenderer.setColor(this.colour);

        Vector2 vertex1Rotated = vertex1.cpy();
        Vector2 vertex2Rotated = vertex2.cpy();
        Vector2 vertex3Rotated = vertex3.cpy();

        for (int i = 0; i < POINTS; i++)
        {
            vertex1Rotated.rotate(POINT_DEGREES);
            vertex2Rotated.rotate(POINT_DEGREES);
            vertex3Rotated.rotate(POINT_DEGREES);

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
}
