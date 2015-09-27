package com.draga.entity.ship;

import com.badlogic.gdx.physics.box2d.World;
import com.draga.entity.GameEntity;

public class Ship extends GameEntity {
    public ShipPhysicComponent physicComponent = null;

    public Ship(String texturePath, int x, int y, World box2dWorld) {
        physicComponent = new ShipPhysicComponent(x, y, this, 1, box2dWorld);
        inputComponent = new ShipInputComponent(physicComponent);
        graphicComponent = new ShipGraphicComponent(physicComponent, texturePath);
    }
}
