package com.draga.entity.ship;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.draga.entity.GameEntity;
import com.draga.entity.component.RectangularPhysicComponent;
import com.draga.manager.GravityManager;

public class ShipPhysicComponent extends RectangularPhysicComponent
{
    public static final int ROTATION_FORCE = 1500;
    private static final int SHIP_WIDTH = 10;
    private static final int SHIP_HEIGHT = 10;
    private static final float BODY_GRAVITY_MULTIPLIER = 3f;

    public ShipPhysicComponent(int x, int y)
    {
        super(x, y, SHIP_WIDTH, SHIP_HEIGHT, 1f, BodyDef.BodyType.DynamicBody);
    }

    @Override public void update(float elapsed)
    {
        Vector2 gravityForce = GravityManager.getForceActingOn(body);
        gravityForce.scl(BODY_GRAVITY_MULTIPLIER);
        applyForce(gravityForce);
    }

    /**
     * Rotate the ship towards the given vector smoothly
     *
     * @param accelerometerForce The force of gravity on the device, should be capped to the Earth gravity
     */
    public void rotateTo(Vector2 accelerometerForce, float elapsed)
    {
        // Ref. http://www.iforce2d.net/b2dtut/rotate-to-angle
        float nextAngle = body.getAngle() + body.getAngularVelocity();
        float directionAngle = accelerometerForce.angleRad();

        float diffRotation = directionAngle - nextAngle;

        // Brings the desired rotation between -180 and 180 degrees to find the closest way to turn.
        // E.g.: rotates - 10 degrees instead of 350.
        while (diffRotation < -180 * MathUtils.degreesToRadians)
        {
            diffRotation += 360 * MathUtils.degreesToRadians;
        }
        while (diffRotation > 180 * MathUtils.degreesToRadians)
        {
            diffRotation -= 360 * MathUtils.degreesToRadians;
        }

        body.applyAngularImpulse(diffRotation * ROTATION_FORCE * elapsed, true);
    }
}
