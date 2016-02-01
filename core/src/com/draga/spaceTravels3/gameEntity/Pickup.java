package com.draga.spaceTravels3.gameEntity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.draga.spaceTravels3.Constants;
import com.draga.spaceTravels3.component.graphicComponent.StaticGraphicComponent;
import com.draga.spaceTravels3.component.miniMapGraphicComponent.StarMiniMapGraphicComponent;
import com.draga.spaceTravels3.component.physicsComponent.PhysicsComponent;
import com.draga.spaceTravels3.component.physicsComponent.PhysicsComponentType;

import java.util.ArrayList;
import java.util.List;

public class Pickup extends GameEntity
{
    public Pickup(float x, float y, Texture texture)
    {
        List<Class<? extends GameEntity>> collidesWith = new ArrayList<>();
        collidesWith.add(Ship.class);
        this.physicsComponent = new PhysicsComponent(
            x,
            y,
            0,
            (Constants.Visual.PICKUP_WIDTH + Constants.Visual.PICKUP_HEIGHT) / 4f,
            new GameEntityGroup(collidesWith),
            this.getClass(),
            false,
            PhysicsComponentType.STATIC);
        this.physicsComponent.setAngularVelocity(
            MathUtils.random(100, 300) * MathUtils.randomSign());

        this.graphicComponent =
            new StaticGraphicComponent(
                texture,
                Constants.Visual.PICKUP_WIDTH,
                Constants.Visual.PICKUP_HEIGHT,
                this.physicsComponent);

        this.miniMapGraphicComponent =
            new StarMiniMapGraphicComponent(
                this.physicsComponent,
                Constants.Visual.HUD.Minimap.PICKUP_COLOR,
                Constants.Game.PICKUP_RADIUS);
    }

    @Override
    public void update(float deltaTime)
    {

    }
}
