package com.draga.entity.component;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.draga.entity.GameEntity;

public abstract class PhysicComponent extends Component {
    protected Body body;

    public PhysicComponent(
        float x,
        float y,
        BodyDef.BodyType bodyType,
        float angle,
        GameEntity gameEntity,
        float gravityScale,
        World box2dWorld) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = bodyType;
        bodyDef.position.set(x, y);
        bodyDef.gravityScale = gravityScale;

        body = box2dWorld.createBody(bodyDef);

        body.setUserData(gameEntity);

        body.setTransform(x, y, angle);
    }

    public float getMass() {
        return body.getMass();
    }

    public void setMass(float mass) {
        body.getMassData().mass = mass;
    }

    public Body getBody() {
        return body;
    }

    public void setBody(Body body) {
        this.body = body;
    }

    public abstract void update(float elapsed);

    public float getAngle() {
        return body.getAngle();
    }

    public void applyRotation(float angle) {
        body.setTransform(getX(), getY(), angle);
    }

    public abstract float getWidth();

    public abstract float getHeight();

    public float getX() {
        return body.getPosition().x;
    }

    public void setX(float x) {
        body.getPosition().x = x;
    }

    public float getY() {
        return body.getPosition().y;
    }

    public void setY(float y) {
        body.getPosition().y = y;
    }

    public void applyForce(float forceX, float forceY) {
        body.applyForceToCenter(forceX, forceY, true);
    }

    public void applyForce(Vector2 force) {
        applyForce(force.x, force.y);
    }

    public void applyYForce(float forceY) {
        applyForce(0, forceY);
    }

    public void applyXForce(float forceX) {
        applyForce(forceX, 0);
    }
}
