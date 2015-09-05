package com.draga.component;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class GraphicComponent {
    protected Texture texture;
    private PhysicComponent shipPhysicComponent;

    public GraphicComponent(String texturePath, PhysicComponent physicComponent)
    {
        FileHandle fileHandle = Gdx.files.internal(texturePath);

        this.texture = new Texture(fileHandle);
        this.shipPhysicComponent = physicComponent;
    }

    public void draw(SpriteBatch spriteBatch)
    {
        spriteBatch.draw(
            texture,
            shipPhysicComponent.getX(),
            shipPhysicComponent.getY(),
            shipPhysicComponent.getWith() / 2,
            shipPhysicComponent.getHeight() / 2,
            shipPhysicComponent.getWith(),
            shipPhysicComponent.getHeight(),
            1,
            1,
            shipPhysicComponent.getRotation(),
            0,
            0,
            texture.getWidth(),
            texture.getHeight(),
            false,
            false);
    }
}
