package com.draga.Ship;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.draga.GraphicComponent;
import com.draga.PhysicComponent;

public class ShipGraphicComponent extends GraphicComponent {
    private PhysicComponent shipPhysicComponent;

    public ShipGraphicComponent(PhysicComponent shipPhysicComponent) {
        this.shipPhysicComponent = shipPhysicComponent;
        texture = new Texture(Gdx.files.internal("badlogic.jpg"));
    }

    @Override
    public void draw(SpriteBatch spriteBatch){
        spriteBatch.draw(texture, shipPhysicComponent.getX(), shipPhysicComponent.getY());
    }
}
