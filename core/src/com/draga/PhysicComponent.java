package com.draga;

public abstract class PhysicComponent {
    private float x;
    private float y;

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public abstract void update(float elapsed);

    public void applyYForce(float yForce) {
        this.y += yForce;
    }

    public void applyXForce(float xForce) {
        this.x += xForce;
    }
}
