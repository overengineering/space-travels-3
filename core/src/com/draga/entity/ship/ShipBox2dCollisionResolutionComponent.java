package com.draga.entity.ship;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.physics.box2d.Contact;
import com.draga.entity.Box2dCollisionResolutionComponent;
import com.draga.entity.Explosion;
import com.draga.entity.GameEntity;
import com.draga.entity.Planet;
import com.draga.manager.GameEntityManager;

/**
 * Created by qwasqaws on 01/10/15.
 */
public class ShipBox2dCollisionResolutionComponent extends Box2dCollisionResolutionComponent
{
    private AssetManager assetManager;

    public ShipBox2dCollisionResolutionComponent(
        com.draga.entity.Ship ship,
        AssetManager assetManager)
    {
        super(ship);
        this.assetManager = assetManager;
    }

    @Override public void Resolve(Contact contact)
    {
        super.Resolve(contact);

        GameEntity collidedEntity = (GameEntity) contact.getFixtureB().getBody().getUserData();

        if (collidedEntity instanceof Planet)
        {
            GameEntity explosion = new Explosion(
                gameEntity.getX(), gameEntity.getY(), "explosion/explosion.atlas", assetManager);
            GameEntityManager.addGameEntityToCreate(explosion);
        }
    }
}
