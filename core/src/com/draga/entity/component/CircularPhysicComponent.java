package com.draga.entity.component;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;

public class CircularPhysicComponent extends PhysicComponent
{
    private CircleShape circleShape;

    public CircularPhysicComponent(
        float mass, float radius, float x, float y, int categoryBits, int maskBits)
    {
        super(x, y, BodyDef.BodyType.DynamicBody, 0);
        circleShape = new CircleShape();
        circleShape.setRadius(radius);

        fixtureDef = new FixtureDef();
        fixtureDef.shape = circleShape;
        float area = radius * radius * MathUtils.PI;
        fixtureDef.density = mass / area;
        fixtureDef.friction = 1f;
        fixtureDef.restitution = 1f;
        fixtureDef.filter.categoryBits = (short) categoryBits;
        fixtureDef.filter.maskBits = (short) maskBits;
    }

    @Override public void update(float elapsed)
    {
    }


    @Override public float getWidth()
    {
        return circleShape.getRadius() * 2;
    }

    @Override public float getHeight()
    {
        return circleShape.getRadius() * 2;
    }

    public float getRadius()
    {
        return circleShape.getRadius();
    }

    @Override public void reset()
    {
        super.reset();
        dispose();
        circleShape = null;
        fixtureDef = null;
    }

    @Override public void dispose()
    {
        super.dispose();
        circleShape.dispose();
    }
}
