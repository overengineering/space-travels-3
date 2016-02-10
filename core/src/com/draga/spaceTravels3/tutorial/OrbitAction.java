package com.draga.spaceTravels3.tutorial;

import com.draga.PooledVector2;
import com.draga.spaceTravels3.component.physicsComponent.PhysicsComponent;

public abstract class OrbitAction extends TutorialAction
{
    private final PhysicsComponent rotatingPhysicsComponent;
    private final PhysicsComponent staticPhysicsComponent;
    private final int              orbits;
    private       float            lastAngle;
    private       float            currentOrbits;

    public OrbitAction(
        PhysicsComponent rotatingPhysicsComponent,
        PhysicsComponent staticPhysicsComponent,
        int orbits)
    {
        this.rotatingPhysicsComponent = rotatingPhysicsComponent;
        this.staticPhysicsComponent = staticPhysicsComponent;
        this.orbits = orbits;

        this.lastAngle = getAngle();
    }

    private float getAngle()
    {
        try (PooledVector2 distance = PooledVector2.newVector2(this.staticPhysicsComponent.getPosition()))
        {
            distance.sub(this.rotatingPhysicsComponent.getPosition());
            float angle = distance.angle();
            return angle;
        }
    }

    @Override
    public boolean act(float delta)
    {
        float newAngle = getAngle();
        float angleDifference = newAngle - this.lastAngle;
        this.lastAngle = newAngle;

        // If the two angles are between 360/0 adjust the angle difference.
        if (angleDifference > 180)
        {
            angleDifference -= 360;
        }
        else if (angleDifference < -180)
        {
            angleDifference += 360;
        }

        this.currentOrbits += angleDifference / 360;

        return super.act(delta);
    }

    @Override
    protected boolean isTriggered()
    {
        return Math.abs(this.currentOrbits) > this.orbits;
    }
}
