package com.draga.manager;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pools;

public abstract class GravityManager
{
    public static Vector2 getForceActingOn(Body body)
    {
        World world = body.getWorld();

        Vector2 totalForce = Pools.obtain(Vector2.class);

        Array<Body> gravitationBodies = new Array<>();
        world.getBodies(gravitationBodies);
        for (Body actingBody : gravitationBodies)
        {
            if (body != actingBody)
            {
                Vector2 force = getGravityForce(body, actingBody);
                totalForce.add(force);
            }
        }

        return totalForce;
    }

    private static Vector2 getGravityForce(Body bodyA, Body bodyB)
    {
        Vector2 distance = bodyB.getPosition().sub(bodyA.getPosition());
        float distanceLen2 = distance.len2();
        distance = distance.nor();
        float forceMagnitude = bodyA.getMass() * bodyB.getMass() / distanceLen2;
        distance = distance.scl(forceMagnitude);
        return distance;
    }
}
