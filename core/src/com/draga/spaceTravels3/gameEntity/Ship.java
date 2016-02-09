package com.draga.spaceTravels3.gameEntity;

import com.badlogic.gdx.graphics.Texture;
import com.draga.PooledVector2;
import com.draga.spaceTravels3.Constants;
import com.draga.spaceTravels3.component.graphicComponent.StaticGraphicComponent;
import com.draga.spaceTravels3.component.miniMapGraphicComponent.TriangleMiniMapGraphicComponent;
import com.draga.spaceTravels3.component.physicsComponent.PhysicsComponent;
import com.draga.spaceTravels3.component.physicsComponent.PhysicsComponentType;
import com.draga.spaceTravels3.manager.InputManager;
import com.draga.spaceTravels3.manager.SettingsManager;

import java.util.ArrayList;
import java.util.List;

public class Ship extends GameEntity
{
    public static final String LOGGING_TAG = Ship.class.getSimpleName();

    // Physic.
    private static final float ROTATION_SCALE               = 5f;
    private static final float MAX_ROTATION_DEGREES_PER_SEC = 360f;

    // State.
    private float   maxFuel;
    private float   currentFuel;
    private boolean infiniteFuel;

    public Ship(
        float x,
        float y,
        float mass,
        Texture texture,
        float maxFuel,
        boolean infiniteFuel)
    {
        this.maxFuel = maxFuel;
        this.currentFuel = maxFuel;
        this.infiniteFuel = infiniteFuel;

        List<Class<? extends GameEntity>> collidesWith = new ArrayList<>();
        collidesWith.add(Planet.class);
        collidesWith.add(Pickup.class);

        this.physicsComponent =
            new PhysicsComponent(
                x,
                y,
                mass,
                Constants.Game.SHIP_COLLISION_RADIUS,
                new GameEntityGroup(collidesWith),
                this.getClass(),
                true,
                PhysicsComponentType.DYNAMIC);

        this.graphicComponent = new StaticGraphicComponent(
            texture,
            Constants.Visual.SHIP_WIDTH,
            Constants.Visual.SHIP_HEIGHT,
            this.physicsComponent);

        this.miniMapGraphicComponent = new TriangleMiniMapGraphicComponent(
            this.physicsComponent,
            Constants.Visual.HUD.Minimap.SHIP_COLOUR,
            Constants.Visual.HUD.Minimap.SHIP_TRIANGLE_VERTEX1.cpy(),
            Constants.Visual.HUD.Minimap.SHIP_TRIANGLE_VERTEX2.cpy(),
            Constants.Visual.HUD.Minimap.SHIP_TRIANGLE_VERTEX3.cpy());
    }

    public boolean isInfiniteFuel()
    {
        return this.infiniteFuel;
    }


    public float getMaxFuel()
    {
        return this.maxFuel;
    }

    @Override
    public void update(float deltaTime)
    {
        try (PooledVector2 inputForce = InputManager.getInputForce())
        {
            inputForce.scl(deltaTime);

            if (!this.infiniteFuel)
            {
                float fuelConsumption = inputForce.len() * Constants.Game.FUEL_PER_SECOND;

                // If the fuel is or is going to be completely consumed then only apply the input force
                // that the fuel can afford.
                if (fuelConsumption > this.currentFuel)
                {
                    inputForce.scl(this.currentFuel / fuelConsumption);
                    fuelConsumption = this.currentFuel;
                }

                this.currentFuel = SettingsManager.getDebugSettings().infiniteFuel
                    ? this.maxFuel
                    : this.currentFuel - fuelConsumption;
            }

            rotateTo(inputForce);

            this.physicsComponent.getVelocity()
                .add(inputForce.scl(Constants.Game.SHIP_ACCELERATION_PER_SECOND));
        }
    }

    /**
     * Rotate the ship towards the given vector smoothly
     *
     * @param inputForce The input force, should be long between 0 and 1.
     */
    private void rotateTo(PooledVector2 inputForce)
    {
        if (inputForce.len() == 0)
        {
            return;
        }

        float diffRotation = inputForce.angle() - this.physicsComponent.getAngle();

        // Avoid ship turning 360 when rotation close to 0 degrees
        if (diffRotation < -180)
        {
            diffRotation += 360;
        }
        else if (diffRotation > 180)
        {
            diffRotation -= 360;
        }

        // Scale the difference of rotation by the deltaTime time and input length.
        diffRotation *= inputForce.len() * ROTATION_SCALE;

        // bring the rotation to the max if it's over it
        if (Math.abs(diffRotation) > MAX_ROTATION_DEGREES_PER_SEC)
        {
            diffRotation = diffRotation > 0
                ? MAX_ROTATION_DEGREES_PER_SEC
                : -MAX_ROTATION_DEGREES_PER_SEC;
        }
        float finalRotation = this.physicsComponent.getAngle() + diffRotation;
        //Brings the finalRotation between 0 and 360
        if (finalRotation > 360)
        {
            finalRotation %= 360;
        }
        else if (finalRotation < 0)
        {
            finalRotation += 360;
        }

        this.physicsComponent.setAngle(finalRotation);
    }

    public float getCurrentFuel()
    {
        return this.currentFuel;
    }
}
