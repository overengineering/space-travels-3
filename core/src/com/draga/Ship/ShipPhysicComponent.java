package com.draga.Ship;

import com.draga.Component.RectangularPhysicComponent;

public class ShipPhysicComponent extends RectangularPhysicComponent
{
    public ShipPhysicComponent()
    {
        super();
        this.rectangle.width = 64;
        this.rectangle.height = 64;
    }

    @Override
    public void update(float elapsed) {

    }
}
