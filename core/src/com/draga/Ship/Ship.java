package com.draga.Ship;

import com.draga.GameEntity;

public class Ship extends GameEntity {
    public Ship() {
        physicComponent = new ShipPhysicComponent();
        inputComponent = new ShipInputComponent(physicComponent);
        graphicComponent = new ShipGraphicComponent(physicComponent);
    }
}
