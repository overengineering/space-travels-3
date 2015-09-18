package com.draga.ship;

import com.badlogic.gdx.math.Vector2;
import com.draga.Constants;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

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

        while (shipPhysicComponent.getRotation() != accelerometerPointingLeft.angle())
        {
            shipPhysicComponent.rotateTo(accelerometerPointingLeft, 1f / 60f);
        }

        Assert.assertEquals(180f, shipPhysicComponent.getRotation(), 0);
    }
}