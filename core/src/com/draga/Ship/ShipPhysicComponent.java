package com.draga.ship;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.draga.GameWorld;
import com.draga.component.RectangularPhysicComponent;
import com.draga.event.GameEventBus;
import com.draga.event.GravityEvent;
import com.google.common.eventbus.Subscribe;

public class ShipPhysicComponent extends RectangularPhysicComponent {
    private static final float MAX_SPEED = 100f;
    private static final float MAX_ACCELERATION_SECOND = MAX_SPEED * 2f;
    private static final int SHIP_WIDTH = 10;
    private static final int SHIP_HEIGHT = 10;
    private static final float ROTATION_PER_SECOND = 1000f;

    public ShipPhysicComponent(int x, int y) {
        super(x, y, SHIP_WIDTH, SHIP_HEIGHT, 1f, BodyDef.BodyType.DynamicBody);

        GameEventBus.GRAVITY_EVENT_BUS.register(this);
    }

    @Override public void update(float elapsed) {
        rotateTo(GameWorld.box2dWorld.getGravity(), elapsed);
    }

    /**
     * Rotate the ship towards the given vector smoothly
     *
     * @param accelerometerForce The force of gravity on the device, should be capped to the constant Earth gravity
     */
    private void rotateTo(Vector2 accelerometerForce, float elapsed) {
        float directionAngle = accelerometerForce.angleRad();

        float diffRotation = directionAngle - getAngle();
        while (diffRotation < -180 * MathUtils.degreesToRadians)
            diffRotation += 360 * MathUtils.degreesToRadians;
        while (diffRotation > 180 * MathUtils.degreesToRadians)
            diffRotation -= 360 * MathUtils.degreesToRadians;
        body.applyTorque(diffRotation * 100000 * elapsed, true);
    }

    @Subscribe public void gravity(GravityEvent gravityEvent) {
        Vector2 distance = new Vector2(gravityEvent.x - getX(), gravityEvent.y - getY());
        float distanceLen2 = distance.len2();
        distance = distance.nor();
        float forceMagnitude = getMass() * gravityEvent.mass / distanceLen2 * gravityEvent.elapsed;
        Vector2 force = new Vector2(distance.scl(forceMagnitude));

        applyForce(force);
    }
}
