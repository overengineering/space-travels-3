package com.draga.entity.ship;

import com.badlogic.gdx.physics.box2d.Contact;
import com.draga.entity.Explosion;
import com.draga.entity.GameEntity;
import com.draga.entity.Planet;
import com.draga.entity.component.Box2dCollisionResolutionComponent;
import com.draga.manager.GameEntityManager;

/**
 * Created by qwasqaws on 01/10/15.
 */
public class ShipBox2dCollisionResolutionComponent extends Box2dCollisionResolutionComponent
{
    public ShipBox2dCollisionResolutionComponent(ShipPhysicComponent physicComponent)
    {
        super(physicComponent);
    }

    @Override
    public void Resolve(Contact contact)
    {
        super.Resolve(contact);

        GameEntity collidedEntity = (GameEntity)contact.getFixtureB().getBody().getUserData();

        if (collidedEntity instanceof Planet)
        {
            GameEntity explosion = new Explosion(
                    ownerPhysicComponent.getX(),
                    ownerPhysicComponent.getY());
            GameEntityManager.addGameEntityToCreate(explosion);
        }
    }
}
