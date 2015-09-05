package com.draga.ship;

import com.draga.component.RectangularPhysicComponent;

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
