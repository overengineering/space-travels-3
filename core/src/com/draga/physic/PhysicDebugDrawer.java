package com.draga.physic;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.draga.gameEntity.GameEntity;
import com.draga.physic.shape.Circle;
import com.draga.manager.GameEntityManager;

/**
 * Draw all the game entities bounds
 */
public class PhysicDebugDrawer
{
    private static ShapeRenderer shapeRenderer;

    public PhysicDebugDrawer()
    {
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setColor(Color.RED);
    }

    /**
     * Draw all the game entities bounds. Uses ShapeRenderer.
     * @param camera The camera to use the projection from
     */
    public static void draw(Camera camera)
    {
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);

        for (GameEntity gameEntity : GameEntityManager.getGameEntities())
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
