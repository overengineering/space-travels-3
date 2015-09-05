package com.draga.component;

public abstract class PhysicComponent {

    private float rotation = 0;
    public float mass;

    public abstract void update(float elapsed);

    public float getRotation() {
        return rotation;
    }

    public void applyRotation(float i)
    {
        rotation += i;
    }

    public abstract float getX();

    public abstract float getY();

    public abstract float getWith();

    public abstract float getHeight();

    public abstract void applyYForce(float forceY);

    public abstract void applyXForce(float forceX);

    public abstract void applyForce(float forceX, float forceY);
}
