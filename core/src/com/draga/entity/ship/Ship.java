package com.draga.entity.ship;

import com.draga.entity.GameEntity;

public class Ship extends GameEntity
{
    public Ship(String texturePath, int x, int y)
    {
        ShipPhysicComponent shipPhysicComponent = new ShipPhysicComponent(x, y);
        shipPhysicComponent.collisionResolutionComponent = new ShipBox2dCollisionResolutionComponent(shipPhysicComponent);

        physicComponent = shipPhysicComponent;
        inputComponent = new ShipInputComponent(shipPhysicComponent);
        graphicComponent = new ShipGraphicComponent(physicComponent, texturePath);
    }
    @Override
    public void update(float elapsed) {
        inputComponent.update(elapsed);
        physicComponent.update(elapsed);
    }
}
