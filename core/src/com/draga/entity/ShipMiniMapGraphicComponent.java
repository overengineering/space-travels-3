package com.draga.entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.draga.MiniMap;
import com.draga.physic.PhysicsComponent;

public class ShipMiniMapGraphicComponent implements MiniMapGraphicComponent
{
    public static final Color COLOR = Color.WHITE;
    private final PhysicsComponent physicsComponent;

    public ShipMiniMapGraphicComponent(PhysicsComponent physicsComponent)
    {
        this.physicsComponent = physicsComponent;
    }

    @Override
    public void draw()
    {
        MiniMap.getShapeRenderer().set(ShapeRenderer.ShapeType.Filled);
        MiniMap.getShapeRenderer().setColor(COLOR);


        Vector2 p1 = new Vector2(8, 0);
        Vector2 p2 = new Vector2(-5, -5);
        Vector2 p3 = new Vector2(-5, 5);
        p1.rotate(this.physicsComponent.getAngle());
        p2.rotate(this.physicsComponent.getAngle());
        p3.rotate(this.physicsComponent.getAngle());

        MiniMap.getShapeRenderer().triangle(
            p1.x + this.physicsComponent.getPosition().x,
            p1.y + this.physicsComponent.getPosition().y,
            p2.x + this.physicsComponent.getPosition().x,
            p2.y + this.physicsComponent.getPosition().y,
            p3.x + this.physicsComponent.getPosition().x,
            p3.y + this.physicsComponent.getPosition().y);
    }
}
