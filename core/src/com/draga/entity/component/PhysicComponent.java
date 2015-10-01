package com.draga.entity.component;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

public abstract class PhysicComponent extends Component
{
    protected Body body;
    private BodyDef bodyDef;
    protected FixtureDef fixtureDef;
    public Box2dCollisionResolutionComponent collisionResolutionComponent = null;

    public PhysicComponent(float x, float y, BodyDef.BodyType bodyType, float angle)
    {
        bodyDef = new BodyDef();
        bodyDef.type = bodyType;
        bodyDef.position.set(x, y);
        bodyDef.angle = angle;
    }

    public BodyDef getBodyDef()
    {
        return bodyDef;
    }

    public void setBodyDef(BodyDef bodyDef)
    {
        this.bodyDef = bodyDef;
    }

    public float getMass()
    {
        return body.getMass();
    }

    public void setMass(float mass)
    {
        body.getMassData().mass = mass;
    }

    public Body getBody()
    {
        return body;
    }

    public void setBody(Body body)
    {
        this.body = body;
    }

    public abstract void update(float elapsed);

    public float getAngle()
    {
        return body.getAngle();
    }

    public void applyRotation(float angle)
    {
        body.setTransform(getX(), getY(), angle);
    }

    public abstract float getWidth();

    public abstract float getHeight();

    public float getX()
    {
        return body.getPosition().x;
    }

    public void setX(float x)
    {
        body.getPosition().x = x;
    }

    public float getY()
    {
        return body.getPosition().y;
    }

    public void setY(float y)
    {
        body.getPosition().y = y;
    }

    public void applyForce(float forceX, float forceY)
    {
        body.applyForceToCenter(forceX, forceY, true);
    }

    public void applyForce(Vector2 force)
    {
        applyForce(force.x, force.y);
    }

    public void applyYForce(float forceY)
    {
        applyForce(0, forceY);
    }

    public void applyXForce(float forceX)
    {
        applyForce(forceX, 0);
    }

    @Override public void reset()
    {
        dispose();
        body = null;
        bodyDef = null;
    }

    @Override public void dispose()
    {
        World box2dWorld = body.getWorld();
        box2dWorld.destroyBody(body);
    }

    public FixtureDef getFixtureDef()
    {
        return fixtureDef;
    }

    public void setFixtureDef(FixtureDef fixtureDef)
    {
        this.fixtureDef = fixtureDef;
    }
}
