package com.draga.physic;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.draga.entity.GameEntity;
import com.draga.entity.shape.Circle;

public class PhysicDebugDrawer
{
    private static ShapeRenderer shapeRenderer;

    public PhysicDebugDrawer()
    {
        shapeRenderer = new ShapeRenderer();
    }

    public static void draw(Camera camera)
    {
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);

        for (GameEntity gameEntity : PhysicsEngine.getGameEntities())
        {
            if (gameEntity.physicsComponent.getShape() instanceof Circle)
            {
                shapeRenderer.circle(
                    gameEntity.physicsComponent.getPosition().x,
                    gameEntity.physicsComponent.getPosition().y,
                    ((Circle) gameEntity.physicsComponent.getShape()).radius);
            }
            else
            {
                throw new IllegalArgumentException("Illegal shape of type "
                    + gameEntity.physicsComponent.getShape().getClass().getSimpleName());
            }
        }

        shapeRenderer.end();
    }

    public static void dispose()
    {
        shapeRenderer.dispose();
    }
}
