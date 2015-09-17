package com.draga.ship;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.draga.component.InputComponent;
import com.draga.component.PhysicComponent;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class ShipInputComponent extends InputComponent {
    private ShipPhysicComponent shipPhysicComponent;

    public ShipInputComponent(ShipPhysicComponent shipPhysicComponent) {
        this.shipPhysicComponent = shipPhysicComponent;
    }

    @Override
    public void update(float elapsed) {
        switch(Gdx.input.getRotation())
        {
            case 0:
                this.shipPhysicComponent.applyForce(
                    Gdx.input.getAccelerometerX(),
                    Gdx.input.getAccelerometerY());
                break;
            case 90:
                this.shipPhysicComponent.applyForce(
                    Gdx.input.getAccelerometerY(),
                    - Gdx.input.getAccelerometerX());
                this.shipPhysicComponent.rotateTo(
                    Gdx.input.getAccelerometerY(),
                    - Gdx.input.getAccelerometerX(),
                    elapsed);
                break;
            default:
                throw new NotImplementedException();
        }


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


        if(Gdx.input.isKeyPressed(Input.Keys.A)) {
            this.shipPhysicComponent.applyRotation(1);
        }

        if(Gdx.input.isKeyPressed(Input.Keys.D)) {
            this.shipPhysicComponent.applyRotation(-1);
        }
    }
}
