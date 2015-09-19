package com.draga.ship;

import com.badlogic.gdx.math.Vector2;
import com.draga.Constants;
import com.draga.TestHelper;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by Administrator on 17/09/2015.
 */
public class ShipPhysicComponentTest
{
    @Test
    public void testRotateTo() throws Exception
    {
        ShipPhysicComponent shipPhysicComponent = new ShipPhysicComponent();
        Vector2 accelerometerPointingLeft = new Vector2(-Constants.EARTH_GRAVITY, 0);
        Class[] argClasses = {Vector2.class, float.class};
        Object[] args = {accelerometerPointingLeft, 1f / 60f};

        while (shipPhysicComponent.getRotation() != accelerometerPointingLeft.angle())
        {
            TestHelper.invokeMethod(ShipPhysicComponent.class, "rotateTo", shipPhysicComponent, argClasses, args);
        }

        Assert.assertEquals(180f, shipPhysicComponent.getRotation(), 0);
    }

    @Test
    public void testGravity() throws Exception
    {

    }
}