package com.draga.entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.draga.MiniMap;
import com.draga.entity.shape.Circle;
import com.draga.graphicComponent.StaticGraphicComponent;
import com.draga.physic.PhysicsComponent;

import java.util.ArrayList;
import java.util.List;

public class Planet extends GameEntity
{
    private final float radius;
    private final boolean isDestination;

    public Planet(
        float mass, float radius, float x, float y, String texturePath, boolean isDestination)
    {
        this.radius = radius;
        List<Class<? extends GameEntity>> collidesWith = new ArrayList<>();
        collidesWith.add(Ship.class);
        this.physicsComponent =
            new PhysicsComponent(x, y, mass, new Circle(radius), new GameEntityGroup(collidesWith));
        this.graphicComponent = new StaticGraphicComponent(texturePath,
            radius * 2f,
            radius * 2f,
            this.physicsComponent);

        this.isDestination = isDestination;
    }

    public boolean isDestination()
    {
        return isDestination;
    }

    @Override
    public void update(float deltaTime)
    {

    }

    @Override
    public void drawMiniMap()
    {
        MiniMap.getShapeRenderer().set(ShapeRenderer.ShapeType.Line);
        Color planetMinimapColour = isDestination
            ? Color.RED
            : Color.BLUE;
        MiniMap.getShapeRenderer().setColor(planetMinimapColour);
        MiniMap.getShapeRenderer()
            .circle(
                this.physicsComponent.getPosition().x,
                this.physicsComponent.getPosition().y,
                radius);
    }
}
