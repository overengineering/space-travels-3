package com.draga.ship;

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
        while (shipPhysicComponent.getRotation() != 180)
        {
            shipPhysicComponent.rotateTo(0, -Constants.EARTH_GRAVITY, 1f / 60f);
        }

        Assert.assertEquals(180f, shipPhysicComponent.getRotation(), 0);
    }
}