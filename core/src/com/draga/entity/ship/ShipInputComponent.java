package com.draga.entity.ship;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.draga.entity.component.InputComponent;

public class ShipInputComponent extends InputComponent {
    public static final int KEYBOARD_FORCE = 10;
    private final static String LOGGING_TAG = ShipInputComponent.class.getSimpleName();
    private ShipPhysicComponent shipPhysicComponent;

    public ShipInputComponent(ShipPhysicComponent shipPhysicComponent) {
        this.shipPhysicComponent = shipPhysicComponent;
    }

    @Override public void update(float elapsed) {
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            this.shipPhysicComponent.applyXForce(-1);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            this.shipPhysicComponent.applyXForce(KEYBOARD_FORCE);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            this.shipPhysicComponent.applyYForce(KEYBOARD_FORCE);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            this.shipPhysicComponent.applyYForce(-KEYBOARD_FORCE);
        }


        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            this.shipPhysicComponent.applyRotation(1);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            this.shipPhysicComponent.applyRotation(-1);
        }
    }

    @Override public void dispose() {

    }

    @Override public void reset() {
        shipPhysicComponent = null;
    }
}
