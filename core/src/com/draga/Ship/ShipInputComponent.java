package com.draga.Ship;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.draga.InputComponent;
import com.draga.PhysicComponent;

public class ShipInputComponent extends InputComponent {
    private PhysicComponent shipPhysicComponent;

    public ShipInputComponent(PhysicComponent shipPhysicComponent) {
        this.shipPhysicComponent = shipPhysicComponent;
    }

    @Override
    public void update(float elapsed) {
        this.shipPhysicComponent.applyXForce(Gdx.input.getAccelerometerX());
        this.shipPhysicComponent.applyYForce(Gdx.input.getAccelerometerY());

        if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            this.shipPhysicComponent.applyXForce(-1);
        }

        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            this.shipPhysicComponent.applyXForce(1);
        }

        if(Gdx.input.isKeyPressed(Input.Keys.UP)) {
            this.shipPhysicComponent.applyYForce(1);
        }

        if(Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            this.shipPhysicComponent.applyYForce(-1);
        }
    }
}
