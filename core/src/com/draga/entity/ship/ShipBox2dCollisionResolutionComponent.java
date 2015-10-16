package com.draga.entity.ship;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.utils.Pools;
import com.draga.Constants;
import com.draga.event.StarCollectedEvent;
import com.draga.entity.*;
import com.draga.manager.GameEntityManager;

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
            GameEntity explosion = new Explosion(
                gameEntity.getX(), gameEntity.getY(), "explosion/explosion.atlas");
            GameEntityManager.addGameEntityToCreate(explosion);

            ((Ship) gameEntity).setIsDead(true);
        }
        else if (collidedEntity instanceof Star)
        {
            StarCollectedEvent starCollectedEvent= Pools.obtain(StarCollectedEvent.class);
            starCollectedEvent.set((Star) collidedEntity);
            Constants.EVENT_BUS.post(starCollectedEvent);
            Pools.free(starCollectedEvent);
        }
    }
}
