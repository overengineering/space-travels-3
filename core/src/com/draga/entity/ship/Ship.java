package com.draga.entity.ship;

import com.draga.entity.GameEntity;

public class Ship extends GameEntity {
    public ShipPhysicComponent physicComponent = null;

    public Ship(String texturePath, int x, int y) {
        physicComponent = new ShipPhysicComponent(x, y, this, 1);
        inputComponent = new ShipInputComponent(physicComponent);
        graphicComponent = new ShipGraphicComponent(physicComponent, texturePath);
    }

    @Override
    public void update(float elapsed) {
        inputComponent.update(elapsed);
        physicComponent.update(elapsed);
    }
}
