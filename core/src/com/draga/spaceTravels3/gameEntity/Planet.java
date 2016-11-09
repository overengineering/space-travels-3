package com.draga.spaceTravels3.gameEntity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.draga.spaceTravels3.Constants;
import com.draga.spaceTravels3.component.graphicComponent.StaticGraphicComponent;
import com.draga.spaceTravels3.component.miniMapGraphicComponent.CircleMiniMapGraphicsComponent;
import com.draga.spaceTravels3.component.physicsComponent.PhysicsComponent;
import com.draga.spaceTravels3.component.physicsComponent.PhysicsComponentType;

import java.util.ArrayList;
import java.util.List;

public class Planet extends GameEntity
{
    public Planet(
        float mass,
        float radius,
        float x,
        float y,
        Texture texture,
        boolean isDestination)
    {
        List<Class<? extends GameEntity>> collidesWith = new ArrayList<>();
        collidesWith.add(Ship.class);
        this.physicsComponent = new PhysicsComponent(
            x,
            y,
            mass,
            radius,
            new GameEntityGroup(collidesWith),
            this.getClass(),
            false,
            PhysicsComponentType.STATIC);

        this.graphicComponent = new StaticGraphicComponent(
            texture,
            radius * 2f,
            radius * 2f,
            this.physicsComponent);

        Color miniMapColour = isDestination
            ? Constants.Visual.HUD.Minimap.PLANET_DESTINATION_COLOUR
            : Constants.Visual.HUD.Minimap.PLANET_COLOUR;
        this.miniMapGraphicComponent = new CircleMiniMapGraphicsComponent(
            this.physicsComponent,
            miniMapColour,
            radius);

    }

    @Override
    public void update(float deltaTime)
    {

    }
}
