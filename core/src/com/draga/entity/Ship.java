package com.draga.entity;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pools;
import com.draga.Constants;
import com.draga.MiniMap;
import com.draga.entity.shape.Circle;
import com.draga.event.FuelChangeEvent;
import com.draga.graphicComponent.StaticGraphicComponent;
import com.draga.manager.InputManager;
import com.draga.manager.SettingsManager;
import com.draga.manager.asset.AssMan;
import com.draga.physic.PhysicsComponent;
import com.draga.physic.PhysicsEngine;

import java.util.ArrayList;
import java.util.List;

public class Ship extends GameEntity
{
    public static final String LOGGING_TAG = Ship.class.getSimpleName();

    // Fuel.
    public static final float MAX_FUEL        = 1f;
    public static final float FUEL_PER_SECOND = 0.3f;

    // Size.
    private static final float SHIP_WIDTH       = 10;
    private static final float SHIP_HEIGHT      = 10;

    // Physic.
    private static final float ROTATION_SCALE = 5f;
    private static final float SHIP_MASS      = 1f;

    private static final float MAX_ROTATION_DEGREES_PER_SEC = 360f;
    private Sound        thrusterSound;
    private long         thrusterSoundInstance;
    // State.
    private float        fuel;

    public Ship(float x, float y, String shipTexturePath, String thrusterTextureAtlasPath)
    {
        thrusterSound = AssMan.getAssMan().get(AssMan.getAssList().thrusterSound);
        // TODO: check if this sound is loopable.
        thrusterSoundInstance = thrusterSound.loop(0);

        fuel = MAX_FUEL;

        List<Class<? extends GameEntity>> collidesWith = new ArrayList<>();
        collidesWith.add(Planet.class);
        collidesWith.add(Star.class);
        this.physicsComponent =
            new PhysicsComponent(x, y, SHIP_MASS, new Circle(4), new GameEntityGroup(collidesWith));

        this.graphicComponent = new StaticGraphicComponent(shipTexturePath,
            SHIP_WIDTH,
            SHIP_HEIGHT,
            this.physicsComponent);
    }
    
    @Override
    public void update(float deltaTime)
    {
        Vector2 gravityForce;
        if (SettingsManager.noGravity)
        {
            gravityForce = new Vector2();
        }
        else
        {
            gravityForce = PhysicsEngine.getForceActingOn(this);
        }
        this.physicsComponent.getVelocity().add(gravityForce.scl(deltaTime));

        Vector2 inputForce = InputManager.getInputForce();

        // TODO: apply the last part of acceleration properly and maybe then elapsed updating the
        // thrusters?
        if (fuel <= 0)
        {
            inputForce.setZero();
        }

        thrusterSound.setVolume(thrusterSoundInstance, inputForce.len());
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

    @Override
    public void drawMiniMap()
    {
        MiniMap.getShapeRenderer().set(ShapeRenderer.ShapeType.Filled);
        MiniMap.getShapeRenderer().setColor(Color.WHITE);

        Vector2 p1 = new Vector2(8, 0);
        Vector2 p2 = new Vector2(-5, -5);
        Vector2 p3 = new Vector2(-5, 5);
        p1.rotate(this.physicsComponent.getAngle());
        p2.rotate(this.physicsComponent.getAngle());
        p3.rotate(this.physicsComponent.getAngle());

        MiniMap.getShapeRenderer().triangle(
            p1.x + this.physicsComponent.getPosition().x,
            p1.y + this.physicsComponent.getPosition().y,
            p2.x + this.physicsComponent.getPosition().x,
            p2.y + this.physicsComponent.getPosition().y,
            p3.x + this.physicsComponent.getPosition().x,
            p3.y + this.physicsComponent.getPosition().y);
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
        float oldFuel = fuel;

        if (SettingsManager.infiniteFuel)
        {
            fuel = MAX_FUEL;
        }
        else
        {
            fuel -= inputForce.len() * FUEL_PER_SECOND * deltaTime;
        }

        FuelChangeEvent fuelChangeEvent = Pools.obtain(FuelChangeEvent.class);
        fuelChangeEvent.set(oldFuel, fuel, MAX_FUEL);
        Constants.EVENT_BUS.post(fuelChangeEvent);
        Pools.free(fuelChangeEvent);
    }

    public float getFuel()
    {
        return fuel;
    }
}
