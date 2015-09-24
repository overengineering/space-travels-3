package com.draga.manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Pools;
import com.draga.Constants;
import com.draga.GameWorld;
import com.draga.entity.planet.Planet;
import com.draga.entity.ship.Ship;

import java.util.List;

/**
 * Created by Administrator on 21/09/2015.
 */
public class GravityManager {
    private static final float ACCELEROMETER_GRAVITY_MULTIPLIER = 10f;
    private static final float PLANET_GRAVITY_MULTIPLIER = 3f;
    private static final String LOGGING_TAG = GravityManager.class.getSimpleName();

    public static void update(World box2dWorld, Ship ship, List<Planet> planets) {
        Vector2 planetForces = Pools.obtain(Vector2.class);
        for (Planet planet : planets) {
            Vector2 gravityForce = getGravityForce(ship.physicComponent.getBody(), planet.physicComponent.getBody());
            planetForces.add(gravityForce);
        }
        planetForces = planetForces.scl(PLANET_GRAVITY_MULTIPLIER);

        Vector2 accelerometerForce = getDeviceAccelerationForDeviceOrientation();
        accelerometerForce = accelerometerForce.scl(ACCELEROMETER_GRAVITY_MULTIPLIER);

        box2dWorld.setGravity(accelerometerForce.add(planetForces));
    }

    private static Vector2 getGravityForce(Body shipBody, Body planetBody) {
        Vector2 distance = planetBody.getPosition().sub(shipBody.getPosition());
        float distanceLen2 = distance.len2();
        distance = distance.nor();
        float forceMagnitude = shipBody.getMass() * planetBody.getMass() / distanceLen2;
        distance = distance.scl(forceMagnitude);
        return distance;
    }

    public static Vector2 getDeviceAccelerationForDeviceOrientation() {
        Vector2 gravity = Pools.obtain(Vector2.class);
        switch (Gdx.input.getRotation()) {
            case 0:
                gravity.x = Gdx.input.getAccelerometerX();
                gravity.y = Gdx.input.getAccelerometerY();
                break;
            case 90:
                gravity.x = Gdx.input.getAccelerometerY();
                gravity.y = -Gdx.input.getAccelerometerX();
                break;
            case 180:
                gravity.x = -Gdx.input.getAccelerometerX();
                gravity.y = -Gdx.input.getAccelerometerY();
                break;
            case 270:
                gravity.x = -Gdx.input.getAccelerometerY();
                gravity.y = Gdx.input.getAccelerometerX();
                break;
            default:
                Gdx.app.error(
                    LOGGING_TAG, "Orientation " + Gdx.input.getRotation() + " not implemented.");
        }
        // Max the gravity by the Earth gravity to avoid excessive force being applied if the device is shaken
        gravity = gravity.clamp(0, Constants.EARTH_GRAVITY);

        return gravity;
    }
}
