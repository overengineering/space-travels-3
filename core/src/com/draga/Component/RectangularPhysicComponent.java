package com.draga.component;

import com.badlogic.gdx.math.Rectangle;

/**
 * Created by Administrator on 03/09/2015.
 */
public class RectangularPhysicComponent extends PhysicComponent
{
    protected Rectangle rectangle;

    public RectangularPhysicComponent() {
        rectangle = new Rectangle();
    }

    public float getX() {
        return rectangle.x;
    }

    public float getY() {
        return rectangle.y;
    }

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

    public float getHeight() {
        return rectangle.height;
    }

    public float getWith() {
        return rectangle.width;
    }
    @Override
    public void update(float elapsed)
    {
    }
}
