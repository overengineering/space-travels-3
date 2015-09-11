package com.draga.ship;

import com.draga.GameEntity;

public class Ship extends GameEntity {
    public Ship(String texturePath) {
        physicComponent = new ShipPhysicComponent();
        inputComponent = new ShipInputComponent(physicComponent);
        graphicComponent = new ShipGraphicComponent(physicComponent, texturePath);
    }
}
