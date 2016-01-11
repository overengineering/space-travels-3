package com.draga.spaceTravels3.physic;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.draga.spaceTravels3.SpaceTravels3;
import com.draga.spaceTravels3.gameEntity.GameEntity;
import com.draga.spaceTravels3.manager.GameEntityManager;

/**
 * Draw all the game entities bounds
 */
public class PhysicDebugDrawer
{
    /**
     * Draw all the game entities bounds. Uses ShapeRenderer.
     */
    public static void draw()
    {
        SpaceTravels3.shapeRenderer.setColor(Color.RED);
        SpaceTravels3.shapeRenderer.setProjectionMatrix(SpaceTravels3.gameViewport.getCamera().combined);
        SpaceTravels3.shapeRenderer.begin(ShapeRenderer.ShapeType.Line);

        for (GameEntity gameEntity : GameEntityManager.getGameEntities())
        {
            SpaceTravels3.shapeRenderer.circle(
                gameEntity.physicsComponent.getPosition().x,
                gameEntity.physicsComponent.getPosition().y,
                gameEntity.physicsComponent.getBoundsCircle().radius);
        }

        SpaceTravels3.shapeRenderer.end();
    }
}
