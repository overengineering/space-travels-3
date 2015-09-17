package com.draga.ship;

import com.draga.component.RectangularPhysicComponent;

public class ShipPhysicComponent extends RectangularPhysicComponent
{
    public ShipPhysicComponent()
    {
        super();
        this.rectangle.width = 10;
        this.rectangle.height = 10;
    }

    @Override
    public void update(float elapsed) {

    }
}
