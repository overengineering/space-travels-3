package com.draga.component;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.draga.GameEntity;

import java.awt.geom.Area;

/**
 * Created by Administrator on 03/09/2015.
 */
public class CircularPhysicComponent extends PhysicComponent {
    private final CircleShape circleShape;

    public CircularPhysicComponent(
        float mass, float radius, float x, float y, GameEntity gameEntity, float gravityScale) {
        super(x, y, BodyDef.BodyType.DynamicBody, 0, gameEntity, gravityScale);

        body.setGravityScale(0);

        circleShape = new CircleShape();
        circleShape.setRadius(radius);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = circleShape;
        float area = radius * radius * MathUtils.PI;
        fixtureDef.density = mass / area;
        fixtureDef.friction = 1f;
        fixtureDef.restitution = 1f;

        body.createFixture(fixtureDef);
    }

    @Override public void update(float elapsed) {
    }


    @Override public float getWidth() {
        return circleShape.getRadius() * 2;
    }

    @Override public float getHeight() {
        return circleShape.getRadius() * 2;
    }

    public float getRadius() {
        return circleShape.getRadius();
    }

    public void dispose() {
        circleShape.dispose();
    }
}
