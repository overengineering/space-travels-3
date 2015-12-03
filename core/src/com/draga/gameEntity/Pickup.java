package com.draga.gameEntity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.draga.Sizes;
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
            new Circle((Sizes.PICKUP_WIDTH + Sizes.PICKUP_HEIGHT) / 4f),
            new GameEntityGroup(collidesWith),
            false);
        this.physicsComponent.setAngularVelocity(MathUtils.random(100, 300)
            * MathUtils.randomSign());

        this.graphicComponent =
            new StaticGraphicComponent(
                texturePath,
                Sizes.PICKUP_WIDTH,
                Sizes.PICKUP_HEIGHT,
                this.physicsComponent);

        this.miniMapGraphicComponent =
            new StarMiniMapGraphicComponent(
                physicsComponent,
                Color.GOLDENROD,
                Sizes.PICKUP_WIDTH * 1.3f);
    }

    @Override
    public void update(float deltaTime)
    {

    }
}
