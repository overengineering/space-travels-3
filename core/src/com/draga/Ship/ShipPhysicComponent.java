package com.draga.ship;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.draga.Constants;
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
    private Vector2 velocity;

    public ShipPhysicComponent(int x, int y) {
        super(x, y, SHIP_WIDTH, SHIP_HEIGHT, 1f, BodyDef.BodyType.DynamicBody);
        this.velocity = new Vector2();

        GameEventBus.GRAVITY_EVENT_BUS.register(this);
    }

    @Override
    public void update(float elapsed) {
//        Vector2 timeScaledVelocity = new Vector2(velocity).scl(elapsed);
//        applyForce(timeScaledVelocity);
        rotateTo(GameWorld.box2dWorld.getGravity(), elapsed);
    }

    public void applyAccelerometerForce(Vector2 accelerometerForce, float elapsed) {
        // If the force exceed the Earth's gravity then scale it down to it.
        accelerometerForce.clamp(0, Constants.EARTH_GRAVITY);

        GameWorld.box2dWorld.setGravity(accelerometerForce);
//        Vector2 acceleration = new Vector2(accelerometerForce)
//            .scl(elapsed / Constants.EARTH_GRAVITY * MAX_ACCELERATION_SECOND);
//        velocity.add(acceleration);
//        velocity.clamp(0, MAX_SPEED);

    }

    /**
     * Rotate the ship towards the given vector smoothly
     *
     * @param accelerometerForce The force of gravity on the device, should be capped to the constant Earth gravity
     */
    private void rotateTo(Vector2 accelerometerForce, float elapsed) {
        float directionAngle = accelerometerForce.angleRad();

        // Scale the acceleration by the Earth's gravity. Moving with more strength produces faster turns.
        // This makes it more realistic but more importantly avoids the ship flickering left and right at low speeds.
        float scale = accelerometerForce.len() / Constants.EARTH_GRAVITY;

        float diffRotation = directionAngle - getAngle();
        // Avoid the ship turning 360 when the rotation is close to 0 degrees.
        if (diffRotation < -MathUtils.PI) {
            diffRotation += 2 * MathUtils.PI;
        } else if (diffRotation > MathUtils.PI) {
            diffRotation -= 2 * MathUtils.PI;
        }

        // Bring the rotation to the max if it's over it and scales it.
        float maxTurn = ROTATION_PER_SECOND * elapsed * scale;
        if (Math.abs(diffRotation) > maxTurn) {
            diffRotation = diffRotation > 0 ? maxTurn : -maxTurn;
        }
        applyRotation(diffRotation);

        // Brings the rotation between 0 and 360
//        if (rotation > 360) {
//            rotation %= 360;
//        } else if (rotation < 0) {
//            rotation += 360;
//        }
    }

    @Subscribe
    public void gravity(GravityEvent gravityEvent) {
        Vector2 distance = new Vector2(gravityEvent.x - getX(), gravityEvent.y - getY());
        float distanceLen2 = distance.len2();
        distance = distance.nor();
        float forceMagnitude = getMass() * gravityEvent.mass / distanceLen2 * gravityEvent.elapsed;
        Vector2 force = new Vector2(distance.scl(forceMagnitude));

        applyForce(force);
    }
}
