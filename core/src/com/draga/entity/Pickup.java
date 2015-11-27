package com.draga.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.draga.entity.shape.Circle;
import com.draga.graphicComponent.StaticGraphicComponent;
import com.draga.physic.PhysicsComponent;

import java.util.ArrayList;
import java.util.List;

public class Pickup extends GameEntity
{

    private static final float WIDTH       = 5f;
    private static final float HEIGHT      = 5f*0.75f;
    private static final float HALF_WIDTH  = WIDTH / 2f;
    private static final float HALF_HEIGHT = HEIGHT / 2f;

    public Pickup(float x, float y, String texturePath)
    {
        List<Class<? extends GameEntity>> collidesWith = new ArrayList<>();
        collidesWith.add(Ship.class);
        this.physicsComponent = new PhysicsComponent(
            x,
            y,
            0,
            new Circle(HALF_WIDTH),
            new GameEntityGroup(collidesWith));
        this.physicsComponent.setAngularVelocity(MathUtils.random(-300, 300));

        this.graphicComponent =
            new StaticGraphicComponent(texturePath, WIDTH, HEIGHT, this.physicsComponent);

        this.miniMapGraphicComponent =
            new StarMiniMapGraphicComponent(physicsComponent, Color.GOLDENROD, WIDTH * 1.3f);
    }

    @Override
    public void update(float deltaTime)
    {

    }
}
