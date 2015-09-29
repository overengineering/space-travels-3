package com.draga.entity.component;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

/**
 * Created by Administrator on 03/09/2015.
 */
public class RectangularPhysicComponent extends PhysicComponent
{
    private float width;
    private float height;
    private PolygonShape polygonShape;

    public RectangularPhysicComponent(
        float x, float y, int width, int height, float mass, BodyDef.BodyType bodyType)
    {
        super(x, y, bodyType, 0);

        this.width = width;
        this.height = height;

        polygonShape = new PolygonShape();
        polygonShape.setAsBox(width / 2f, height / 2f);

        fixtureDef = new FixtureDef();
        fixtureDef.shape = polygonShape;
        float area = width * height;
        fixtureDef.density = mass / area;
        fixtureDef.friction = 1f;
        fixtureDef.restitution = 1f;
    }

    public FixtureDef getFixtureDef()
    {
        return fixtureDef;
    }

    public void setFixtureDef(FixtureDef fixtureDef)
    {
        this.fixtureDef = fixtureDef;
    }

    @Override public void reset()
    {
        super.reset();
        width = 0;
        height = 0;
        polygonShape = null;
        fixtureDef = null;
    }

    @Override public void dispose()
    {
        super.dispose();
        polygonShape.dispose();
    }

    public float getHeight()
    {
        return this.height;
    }

    public float getWidth()
    {
        return this.width;
    }

    @Override public void update(float elapsed)
    {
    }
}
