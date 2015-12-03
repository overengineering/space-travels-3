package com.draga.gameEntity;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pools;
import com.draga.Constants;
import com.draga.component.PhysicsComponent;
import com.draga.component.graphicComponent.StaticGraphicComponent;
import com.draga.component.miniMapGraphicComponent.TriangleMiniMapGraphicComponent;
import com.draga.manager.InputManager;
import com.draga.manager.SettingsManager;
import com.draga.manager.asset.AssMan;
import com.draga.physic.shape.Circle;

import java.util.ArrayList;
import java.util.List;

public class Ship extends GameEntity
{
    public static final String LOGGING_TAG = Ship.class.getSimpleName();

    // Fuel.
    public static final float MAX_FUEL        = 1f;
    public static final float FUEL_PER_SECOND = 0.3f;

    // Size.
    private static final float SHIP_WIDTH  = 10;
    private static final float SHIP_HEIGHT = 10;

    // Physic.
    private static final float ROTATION_SCALE = 5f;
    private static final float SHIP_MASS      = 1f;

    private static final float MAX_ROTATION_DEGREES_PER_SEC = 360f;
    private Sound thrusterSound;
    private long  thrusterSoundInstance;
    // State.
    private float fuel;

    public Ship(float x, float y, String shipTexturePath, String thrusterTextureAtlasPath)
    {
        thrusterSound = AssMan.getAssMan().get(AssMan.getAssList().thrusterSound);
        // TODO: check if this sound is loopable.
        thrusterSoundInstance = thrusterSound.loop(0);

        fuel = MAX_FUEL;

        List<Class<? extends GameEntity>> collidesWith = new ArrayList<>();
        collidesWith.add(Planet.class);
        collidesWith.add(Pickup.class);
        this.physicsComponent =
            new PhysicsComponent(
                x,
                y,
                SHIP_MASS,
                new Circle(4),
                new GameEntityGroup(collidesWith),
                true);

        this.graphicComponent = new StaticGraphicComponent(
            shipTexturePath,
            SHIP_WIDTH,
            SHIP_HEIGHT,
            this.physicsComponent);

        this.miniMapGraphicComponent = new TriangleMiniMapGraphicComponent(
            this.physicsComponent,
            Color.WHITE,
            new Vector2(8, 0),
            new Vector2(-5, -5),
            new Vector2(-5, 5));
    }
    
    @Override
    public void update(float deltaTime)
    {
        Vector2 inputForce = InputManager.getInputForce();

        // TODO: apply the last part of acceleration properly and maybe then elapsed updating the
        // thrusters?
        if (fuel <= 0)
        {
            inputForce.setZero();
        }

        thrusterSound.setVolume(
            thrusterSoundInstance,
            inputForce.len() * SettingsManager.getSettings().volume);
        rotateTo(inputForce, deltaTime);
        updateFuel(inputForce, deltaTime);

        this.physicsComponent.getVelocity().add(inputForce);
    }

    @Override
    public void dispose()
    {
        super.dispose();
        thrusterSound.stop();
        thrusterSound.dispose();
    }

    /**
     * Rotate the ship towards the given vector smoothly
     *
     * @param inputForce The input force, should be long between 0 and 1.
     */
    private void rotateTo(Vector2 inputForce, float elapsed)
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

        // Scale the difference of rotation by the elapsed time and input length.
        diffRotation *= inputForce.len() * elapsed * ROTATION_SCALE;

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

    private void updateFuel(Vector2 inputForce, float deltaTime)
    {
        if (SettingsManager.getDebugSettings().infiniteFuel)
        {
            fuel = MAX_FUEL;
        }
        else
        {
            fuel -= inputForce.len() * FUEL_PER_SECOND * deltaTime;
        }
    }

    public float getFuel()
    {
        return fuel;
    }
}
