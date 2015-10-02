package com.draga.manager;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.draga.entity.Explosion;
import com.draga.entity.GameEntity;
import com.draga.entity.Planet;
import com.draga.entity.ship.Ship;

public class GameContactListener implements ContactListener
{
    @Override public void beginContact(Contact contact)
    {
        Body bodyA = contact.getFixtureA().getBody();

        GameEntity entity = ((GameEntity)bodyA.getUserData());
        if (entity != null
            && entity.physicComponent != null
            && entity.physicComponent.collisionResolutionComponent != null)
        {
            entity.physicComponent.collisionResolutionComponent.Resolve(contact);
        }
    }

    @Override public void endContact(Contact contact)
    {

    }

    @Override public void preSolve(Contact contact, Manifold oldManifold)
    {

    }

    @Override public void postSolve(Contact contact, ContactImpulse impulse)
    {

    }
}
