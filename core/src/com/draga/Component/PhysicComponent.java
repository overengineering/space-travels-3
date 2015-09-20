package com.draga.component;

import com.badlogic.gdx.physics.box2d.Body;

public abstract class PhysicComponent {
    protected float mass;
    protected float rotation = 0;
    protected Body body;

    public float getMass() {
        return mass;
    }

    public void setMass(float mass) {
        this.mass = mass;
    }

    public Body getBody() {
        return body;
    }

    public void setBody(Body body) {
        this.body = body;
    }

    public abstract void update(float elapsed);

    public float getRotation() {
        return rotation;
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
    }

    public void applyRotation(float i) {
        rotation += i;
    }

    public abstract float getX();

    public abstract void setX(float x);

    public abstract float getY();

    public abstract void setY(float y);

    public abstract float getWidth();

    public abstract float getHeight();

    public abstract void applyYForce(float forceY);

    public abstract void applyXForce(float forceX);

    public abstract void applyForce(float forceX, float forceY);
}
