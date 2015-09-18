package com.draga.ship;

import com.badlogic.gdx.math.Vector2;
import com.draga.Constants;
import com.draga.component.RectangularPhysicComponent;

public class ShipPhysicComponent extends RectangularPhysicComponent
{
    private static final float MAX_SPEED = 10f;
    private final float rotationPerSecond = 2000f;
    private Vector2 velocity;

    public ShipPhysicComponent()
    {
        super();
        this.rectangle.width = 10;
        this.rectangle.height = 10;
        this.velocity = new Vector2();
    }

    @Override
    public void update(float elapsed)
    {
        applyForce(velocity);
    }

    public void applyAccelerometerForce(Vector2 accelerometerForce, float elapsed)
    {
        // If the force exceed the Earth's gravity then scale it down to it.
        accelerometerForce = accelerometerForce.clamp(0, Constants.EARTH_GRAVITY);

        velocity.add(accelerometerForce);
        velocity = velocity.clamp(0, MAX_SPEED);

        rotateTo(accelerometerForce, elapsed);
    }

    public void rotateTo(Vector2 accelerometerForce, float elapsed)
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
        float maxTurn = rotationPerSecond * elapsed * scale;
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
}
