package com.draga.gameEntity;

import com.badlogic.gdx.graphics.Color;
import com.draga.Constants;
import com.draga.component.PhysicsComponent;
import com.draga.component.graphicComponent.StaticGraphicComponent;
import com.draga.component.miniMapGraphicComponent.CircleMiniMapGraphicsComponent;
import com.draga.physic.shape.Circle;

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
            new Circle(radius),
            new GameEntityGroup(collidesWith),
            false);

        this.graphicComponent = new StaticGraphicComponent(
            texturePath,
            radius * 2f,
            radius * 2f,
            this.physicsComponent);

        Color miniMapColour = isDestination
            ? Constants.Visual.PLANET_MINIMAP_DESTINATION_COLOUR
            : Constants.Visual.PLANET_MINIMAP_COLOUR;
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
