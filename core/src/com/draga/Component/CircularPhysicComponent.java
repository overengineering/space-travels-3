package com.draga.component;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.utils.Pools;
import com.draga.event.GameEventBus;
import com.draga.event.GravityEvent;

/**
 * Created by Administrator on 03/09/2015.
 */
public class CircularPhysicComponent extends PhysicComponent {

    private final CircleShape circleShape;

    public CircularPhysicComponent(float mass, float radius, float x, float y) {
        super(x, y, BodyDef.BodyType.StaticBody, mass, 0);

        circleShape = new CircleShape();
        circleShape.setRadius(radius);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = circleShape;
        fixtureDef.density = 1f;
        fixtureDef.friction = 1f;

        body.createFixture(fixtureDef);
    }

    @Override
    public void update(float elapsed) {
        GravityEvent gravityEvent = Pools.obtain(GravityEvent.class);
        gravityEvent.set(getX(), getY(), getMass(), elapsed);
        GameEventBus.GRAVITY_EVENT_BUS.post(gravityEvent);
        Pools.free(gravityEvent);
    }

    @Override
    public float getWidth() {
        return circleShape.getRadius() * 2;
    }

    @Override
    public float getHeight() {
        return circleShape.getRadius() * 2;
    }

    public float getRadius() {
        return circleShape.getRadius();
    }

    public void dispose() {
        circleShape.dispose();
    }
}
