package com.draga.spaceTravels3.physic;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.draga.spaceTravels3.SpaceTravels3;
import com.draga.spaceTravels3.gameEntity.GameEntity;
import com.draga.shape.Circle;
import com.draga.spaceTravels3.manager.GameEntityManager;

/**
 * Draw all the game entities bounds
 */
public class PhysicDebugDrawer
{
    public PhysicDebugDrawer()
    {
    }

    /**
     * Draw all the game entities bounds. Uses ShapeRenderer.
     * @param camera The camera to use the projection from
     */
    public static void draw(Camera camera)
    {
        SpaceTravels3.shapeRenderer.setColor(Color.RED);
        SpaceTravels3.shapeRenderer.setProjectionMatrix(camera.combined);
        SpaceTravels3.shapeRenderer.begin(ShapeRenderer.ShapeType.Line);

        for (GameEntity gameEntity : GameEntityManager.getGameEntities())
        {
            if (gameEntity.physicsComponent.getShape() instanceof Circle)
            {
                SpaceTravels3.shapeRenderer.circle(
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

        SpaceTravels3.shapeRenderer.end();
    }
}
