package com.draga.entity.ship;

import com.badlogic.gdx.math.Vector2;
import com.draga.entity.component.InputComponent;
import com.draga.manager.InputManager;

public class ShipInputComponent extends InputComponent
{
    private static final float INPUT_GRAVITY_MULTIPLIER = 100f;
    private final static String LOGGING_TAG = ShipInputComponent.class.getSimpleName();
    private ShipPhysicComponent shipPhysicComponent;

    public ShipInputComponent(ShipPhysicComponent shipPhysicComponent)
    {
        this.shipPhysicComponent = shipPhysicComponent;
    }

    @Override public void update(float elapsed)
    {
        Vector2 inputForce = InputManager.getInputForce();
        inputForce.scl(INPUT_GRAVITY_MULTIPLIER);
        shipPhysicComponent.rotateTo(inputForce, elapsed);
        shipPhysicComponent.applyForce(inputForce);
    }

    @Override public void dispose()
    {

    }

    @Override public void reset() {
        shipPhysicComponent = null;
    }
}
