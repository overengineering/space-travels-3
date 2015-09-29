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
        Body bodyB = contact.getFixtureB().getBody();
        Vector2 explosionPosition = null;
        if (bodyA.getUserData() instanceof Ship && bodyB.getUserData() instanceof Planet)
        {
            explosionPosition = bodyA.getPosition();
        }
        if (bodyA.getUserData() instanceof Planet && bodyB.getUserData() instanceof Ship)
        {
            explosionPosition = bodyB.getPosition();
        }
        if (explosionPosition != null)
        {
            GameEntity explosion = new Explosion(
                explosionPosition.x, explosionPosition.y);
            GameEntityManager.addGameEntityToCreate(explosion);
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
