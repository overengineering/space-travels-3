package com.draga.gameEntity;

import com.badlogic.gdx.math.Vector2;
import com.draga.Constants;
import com.draga.component.PhysicsComponent;
import com.draga.component.graphicComponent.StaticGraphicComponent;
import com.draga.component.miniMapGraphicComponent.TriangleMiniMapGraphicComponent;
import com.draga.manager.InputManager;
import com.draga.manager.SettingsManager;
import com.draga.physic.shape.Circle;

import java.util.ArrayList;
import java.util.List;

public class Ship extends GameEntity
{
    public static final String LOGGING_TAG = Ship.class.getSimpleName();

    // Physic.
    private static final float ROTATION_SCALE               = 5f;
    private static final float MAX_ROTATION_DEGREES_PER_SEC = 360f;

    // State.
    private float maxFuel;
    private float currentFuel;

    public Ship(
        float x,
        float y,
        float mass,
        String texturePath,
        float maxFuel)
    {
        this.maxFuel = maxFuel;
        this.currentFuel = maxFuel;

        List<Class<? extends GameEntity>> collidesWith = new ArrayList<>();
        collidesWith.add(Planet.class);
        collidesWith.add(Pickup.class);

        this.physicsComponent =
            new PhysicsComponent(
                x,
                y,
                mass,
                new Circle(Constants.Game.SHIP_COLLISION_RADIUS),
                new GameEntityGroup(collidesWith),
                true);

        this.graphicComponent = new StaticGraphicComponent(
            texturePath,
            Constants.Visual.SHIP_WIDTH,
            Constants.Visual.SHIP_HEIGHT,
            this.physicsComponent);

        this.miniMapGraphicComponent = new TriangleMiniMapGraphicComponent(
            this.physicsComponent,
            Constants.Visual.SHIP_MINIMAP_COLOUR,
            Constants.Visual.SHIP_MINIMAP_TRIANGLE_VERTEX1,
            Constants.Visual.SHIP_MINIMAP_TRIANGLE_VERTEX2,
            Constants.Visual.SHIP_MINIMAP_TRIANGLE_VERTEX3);
    }

    public float getMaxFuel()
    {
        return maxFuel;
    }
    
    @Override
    public void update(float deltaTime)
    {
        Vector2 inputForce = InputManager.getInputForce();

        float fuelConsumption = inputForce.len() * Constants.Game.FUEL_PER_SECOND * deltaTime;

        // If the fuel is or is going to be completely consumed then only apply the input force
        // that the fuel can afford.
        if (fuelConsumption > currentFuel)
        {
            inputForce.scl(currentFuel / fuelConsumption);
            fuelConsumption = currentFuel;
        }

        currentFuel = SettingsManager.getDebugSettings().infiniteFuel
            ? maxFuel
            : currentFuel - fuelConsumption;

        this.physicsComponent.getVelocity()
            .add(inputForce.cpy().scl(deltaTime * Constants.Game.SHIP_ACCELERATION_PER_SECOND));

        rotateTo(inputForce, deltaTime);
    }

    /**
     * Rotate the ship towards the given vector smoothly
     *
     * @param inputForce The input force, should be long between 0 and 1.
     */
    private void rotateTo(Vector2 inputForce, float deltaTime)
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
        diffRotation *= inputForce.len() * deltaTime * ROTATION_SCALE;

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
        return currentFuel;
    }
}
