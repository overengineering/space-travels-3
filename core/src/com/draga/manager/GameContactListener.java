package com.draga.manager;

import com.badlogic.gdx.physics.box2d.*;
import com.draga.entity.GameEntity;

public class GameContactListener implements ContactListener
{
    @Override
    public void beginContact(Contact contact)
    {
    }

    @Override
    public void endContact(Contact contact)
    {

    }

    /** Resolve the contact in preSolve to give a chance to the resolution component to disable
     * the contact */
    @Override
    public void preSolve(Contact contact, Manifold oldManifold)
    {
        Body bodyA = contact.getFixtureA().getBody();

        GameEntity entity = ((GameEntity) bodyA.getUserData());
        if (entity != null && entity.collisionResolutionComponent != null)
        {
            entity.collisionResolutionComponent.Resolve(contact);
        }
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse)
    {

    }
}
