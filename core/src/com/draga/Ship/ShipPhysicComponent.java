package com.draga.ship;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pools;
import com.draga.Constants;
import com.draga.component.RectangularPhysicComponent;
import com.draga.event.GameEventBus;
import com.draga.event.GravityEvent;
import com.google.common.eventbus.Subscribe;

public class ShipPhysicComponent extends RectangularPhysicComponent
{
    private static final float MAX_SPEED = 100f;
    private static final float MAX_ACCELERATION_SECOND = MAX_SPEED * 2f;
    private static final int SHIP_WIDTH = 10;
    private static final int SHIP_HEIGHT = 10;
    private static final float ROTATION_PER_SECOND = 2000f;
    private Vector2 velocity;

    public ShipPhysicComponent()
    {
        super(0, 0, SHIP_WIDTH, SHIP_HEIGHT);
        this.velocity = new Vector2();
        this.mass = 1f;

        GameEventBus.GRAVITY_EVENT_BUS.register(this);
    }

    @Override
    public void update(float elapsed)
    {
        Vector2 timeScaledVelocity = new Vector2(velocity).scl(elapsed);
        applyForce(timeScaledVelocity);
    }

    public void applyAccelerometerForce(Vector2 accelerometerForce, float elapsed)
    {
        // If the force exceed the Earth's gravity then scale it down to it.
        accelerometerForce.clamp(0, Constants.EARTH_GRAVITY);

        Vector2 acceleration = new Vector2(accelerometerForce)
            .scl(elapsed / Constants.EARTH_GRAVITY * MAX_ACCELERATION_SECOND);
        velocity.add(acceleration);
        velocity.clamp(0, MAX_SPEED);

        rotateTo(accelerometerForce, elapsed);
    }

    /**
     * Rotate the ship towards the given vector smoothly
     *
     * @param accelerometerForce The force of gravity on the device, should be capped to the constant Earth gravity
     */
    private void rotateTo(Vector2 accelerometerForce, float elapsed)
    {
        float directionAngle = accelerometerForce.angle();

        // Scale the acceleration by the Earth's gravity. Moving with more strength produces faster turns.
        // This makes it more realistic but more importantly avoids the ship flickering left and right at low speeds.
        float scale = accelerometerForce.len() / Constants.EARTH_GRAVITY;

        double diffRotation = directionAngle - rotation;
        // Avoid the ship turning 360 when the rotation is close to 0 degrees.
        if (diffRotation < -180)
        {
            diffRotation += 360;
        }
        else if (diffRotation > 180)
        {
            diffRotation -= 360;
        }

        // Bring the rotation to the max if it's over it and scales it.
        float maxTurn = ROTATION_PER_SECOND * elapsed * scale;
        if (Math.abs(diffRotation) > maxTurn)
        {
            diffRotation = diffRotation > 0 ? maxTurn : -maxTurn;
        }
        rotation += diffRotation;

        // Brings the rotation between 0 and 360
        if (rotation > 360)
        {
            rotation %= 360;
        }
        else if (rotation < 0)
        {
            rotation += 360;
        }
    }

    @Subscribe
    public void gravity(GravityEvent gravityEvent)
    {
        Vector2 distance = new Vector2(gravityEvent.x - rectangle.x , gravityEvent.y - rectangle.y);
        float distanceLen2 = distance.len2();
        distance = distance.nor();
        float forceMagnitude = mass * gravityEvent.mass / distanceLen2 * gravityEvent.elapsed;
        Vector2 force = new Vector2(distance.scl(forceMagnitude));

        applyForce(force);
    }
}
