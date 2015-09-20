package com.draga.ship;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.draga.component.InputComponent;

public class ShipInputComponent extends InputComponent {
    private final static String LOGGING_TAG = ShipInputComponent.class.getSimpleName();
    public static final int KEYBOARD_FORCE = 10;
    private ShipPhysicComponent shipPhysicComponent;

    public ShipInputComponent(ShipPhysicComponent shipPhysicComponent) {
        this.shipPhysicComponent = shipPhysicComponent;
    }

    @Override
    public void update(float elapsed) {
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
}
