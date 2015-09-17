package com.draga.ship;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.draga.component.RectangularPhysicComponent;

public class ShipPhysicComponent extends RectangularPhysicComponent
{
    private final float rotationPerSecond = 720;

    public ShipPhysicComponent()
    {
        super();
        this.rectangle.width = 10;
        this.rectangle.height = 10;
    }

    @Override
    public void update(float elapsed)
    {

    }
    public void rotateTo(float x, float y, float elapsed)
    {
        // increase the angle by 90 degrees to compensate the angle of Vector2 being calculated on the x axis
        float forceAngle = new Vector2(y, - x).angle();

        double diffRotation = forceAngle - rotation;
        // Avoid ship turning 360 when rotation close to 0 degrees
        if (diffRotation < -180)
        {
            diffRotation += 360;
        }
        else if (diffRotation > 180)
        {
            diffRotation -= 360;
        }
        // bring the rotation to the max if it's over it
        float maxTurn = rotationPerSecond * elapsed;
        if (Math.abs(diffRotation) > maxTurn)
        {
            diffRotation = diffRotation > 0 ? maxTurn : -maxTurn;
        }
        rotation += diffRotation;
        //Brings the finalRotation between 0 and 360
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
