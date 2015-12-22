package com.draga.spaceTravels3.gameEntity;

import com.badlogic.gdx.graphics.Color;
import com.draga.spaceTravels3.Constants;
import com.draga.spaceTravels3.component.PhysicsComponent;
import com.draga.spaceTravels3.component.graphicComponent.StaticGraphicComponent;
import com.draga.spaceTravels3.component.miniMapGraphicComponent.CircleMiniMapGraphicsComponent;

import java.util.ArrayList;
import java.util.List;

public class Planet extends GameEntity
{
    public Planet(
        float mass,
        float radius,
        float x,
        float y,
        String texturePath,
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
            false);

        this.graphicComponent = new StaticGraphicComponent(
            texturePath,
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
