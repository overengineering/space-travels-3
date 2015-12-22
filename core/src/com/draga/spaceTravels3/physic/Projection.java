package com.draga.spaceTravels3.physic;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Frustum;
import com.badlogic.gdx.math.Vector2;
import com.draga.spaceTravels3.Constants;
import com.draga.spaceTravels3.Level;
import com.draga.spaceTravels3.SpaceTravels3;
import com.draga.spaceTravels3.Vertex;
import com.draga.spaceTravels3.component.PhysicsComponent;
import com.draga.utils.GraphicsUtils;

import java.util.ArrayList;

public class Projection
{
    private final ArrayList<Vertex> vertices;

    public Projection(ArrayList<Vertex> vertices)
    {
        this.vertices = vertices;
    }

    public void draw()
    {
        GraphicsUtils.enableBlending();
        SpaceTravels3.shapeRenderer.set(ShapeRenderer.ShapeType.Line);

        for (int i = 1; i < vertices.size(); i += 2)
        {
            Vertex vertexA = vertices.get(i);
            Vertex vertexB = vertices.get(i - 1);
            Color color = vertexA.getColor().cpy().lerp(vertexB.getColor(), 0.5f);
            SpaceTravels3.shapeRenderer.setColor(color);

            Vector2 projectionPointA = vertexA.getPosition();
            Vector2 projectionPointB = vertexB.getPosition();

            SpaceTravels3.shapeRenderer.line(
                projectionPointA.x, projectionPointA.y,
                projectionPointB.x, projectionPointB.y);
        }

        GraphicsUtils.disableBlending();
    }
}
