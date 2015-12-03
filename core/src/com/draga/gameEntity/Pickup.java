package com.draga.gameEntity;

import com.badlogic.gdx.math.MathUtils;
import com.draga.VisualStyle;
import com.draga.component.PhysicsComponent;
import com.draga.component.graphicComponent.StaticGraphicComponent;
import com.draga.component.miniMapGraphicComponent.StarMiniMapGraphicComponent;
import com.draga.physic.shape.Circle;

import java.util.ArrayList;
import java.util.List;

public class Pickup extends GameEntity
{
    public Pickup(float x, float y, String texturePath)
    {
        List<Class<? extends GameEntity>> collidesWith = new ArrayList<>();
        collidesWith.add(Ship.class);
        this.physicsComponent = new PhysicsComponent(
            x,
            y,
            0,
            new Circle((VisualStyle.PICKUP_WIDTH + VisualStyle.PICKUP_HEIGHT) / 4f),
            new GameEntityGroup(collidesWith),
            false);
        this.physicsComponent.setAngularVelocity(
            MathUtils.random(100, 300) * MathUtils.randomSign());

        this.graphicComponent =
            new StaticGraphicComponent(
                texturePath,
                VisualStyle.PICKUP_WIDTH,
                VisualStyle.PICKUP_HEIGHT,
                this.physicsComponent);

        this.miniMapGraphicComponent =
            new StarMiniMapGraphicComponent(
                physicsComponent,
                VisualStyle.PICKUP_MINIMAP_COLOR,
                VisualStyle.PICKUP_MINIMAP_RADIUS);
    }

    @Override
    public void update(float deltaTime)
    {

    }
}
