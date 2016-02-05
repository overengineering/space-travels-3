package com.draga.spaceTravels3.physic;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pools;
import com.draga.PooledVector2;
import com.draga.spaceTravels3.SpaceTravels3;
import com.draga.spaceTravels3.Vertex;
import com.draga.utils.GraphicsUtils;

import java.util.ArrayList;

public class Projection implements Pool.Poolable
{
    private ArrayList<Vertex> vertices;

    public void set(ArrayList<Vertex> vertices)
    {
        this.vertices = vertices;
    }

    public void draw()
    {
        GraphicsUtils.enableBlending();
        SpaceTravels3.shapeRenderer.set(ShapeRenderer.ShapeType.Line);

        for (int i = 1; i < this.vertices.size(); i += 2)
        {
            Vertex vertexA = this.vertices.get(i);
            Vertex vertexB = this.vertices.get(i - 1);
            Color color = vertexA.getColor().cpy().lerp(vertexB.getColor(), 0.5f);
            SpaceTravels3.shapeRenderer.setColor(color);

            PooledVector2 projectionPointA = vertexA.getPosition();
            PooledVector2 projectionPointB = vertexB.getPosition();

            SpaceTravels3.shapeRenderer.line(
                projectionPointA.x, projectionPointA.y,
                projectionPointB.x, projectionPointB.y);
        }

        GraphicsUtils.disableBlending();
    }

    @Override
    public void reset()
    {
        Pool<Vertex> vertexPool = Pools.get(Vertex.class);
        for (Vertex vertex : this.vertices)
        {
            vertexPool.free(vertex);
        }
        this.vertices = null;
    }
}
