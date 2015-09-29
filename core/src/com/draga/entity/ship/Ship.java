package com.draga.entity.ship;

import com.badlogic.gdx.physics.box2d.World;
import com.draga.entity.GameEntity;

public class Ship extends GameEntity
{
    public Ship(String texturePath, int x, int y)
    {
        ShipPhysicComponent shipPhysicComponent = new ShipPhysicComponent(x, y);
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
