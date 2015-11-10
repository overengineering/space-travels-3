package com.draga.entity.ship;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.utils.Pools;
import com.draga.Constants;
import com.draga.entity.*;
import com.draga.event.ShipPlanetCollisionEvent;
import com.draga.event.StarCollectedEvent;

public class ShipBox2dCollisionResolutionComponent extends Box2dCollisionResolutionComponent
{
    public ShipBox2dCollisionResolutionComponent(Ship ship)
    {
        super(ship);
    }

    @Override
    public void Resolve(Contact contact)
    {
        super.Resolve(contact);

        GameEntity collidedEntity = (GameEntity) contact.getFixtureB().getBody().getUserData();

        if (collidedEntity instanceof Planet)
        {
            ShipPlanetCollisionEvent shipPlanetCollisionEvent =
                Pools.obtain(ShipPlanetCollisionEvent.class);
            shipPlanetCollisionEvent.ship = (Ship) this.gameEntity;
            shipPlanetCollisionEvent.planet = (Planet) collidedEntity;

            Constants.EVENT_BUS.post(shipPlanetCollisionEvent);
            Pools.free(shipPlanetCollisionEvent);
        }
        else if (collidedEntity instanceof Star)
        {
            // Disable the contact so that it won't get computed by Box2d and the ship won't slow
            // down
            contact.setEnabled(false);

            StarCollectedEvent starCollectedEvent = Pools.obtain(StarCollectedEvent.class);
            starCollectedEvent.set((Star) collidedEntity);
            Constants.EVENT_BUS.post(starCollectedEvent);
            Pools.free(starCollectedEvent);
        }
    }
}
