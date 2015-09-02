package com.draga;

import com.badlogic.gdx.math.Vector2;

import java.awt.*;

public abstract class PhysicComponent {
    protected Rectangle rectangle;

    private float rotation = 0;

    public PhysicComponent() {
        rectangle = new Rectangle();
    }

    public float getX() {
        return rectangle.x;
    }

    public float getY() {
        return rectangle.y;
    }

    public abstract void update(float elapsed);

    public void applyForce(float forceX, float forceY){
        rectangle.x += forceX;
        rectangle.y += forceY;
    }

    public void applyYForce(float forceY) {
        this.rectangle.y += forceY;
    }

    public void applyXForce(float forceX) {
        this.rectangle.x += forceX;
    }

    public int getHeight() {
        return rectangle.height;
    }

    public int getWith() {
        return rectangle.width;
    }

    public float getRotation() {
        return rotation;
    }
}
