package com.draga.component;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class GraphicComponent {
    protected Texture texture;
    private PhysicComponent physicComponent;

    public GraphicComponent(String texturePath, PhysicComponent physicComponent)
    {
        FileHandle fileHandle = Gdx.files.internal(texturePath);

        this.texture = new Texture(fileHandle);
        this.physicComponent = physicComponent;
    }

    public void draw(SpriteBatch spriteBatch)
    {
        spriteBatch.draw(
            texture,
            physicComponent.getX() - physicComponent.getWidth() / 2,
            physicComponent.getY() - physicComponent.getHeight() / 2,
            0,
            0,
            physicComponent.getWidth(),
            physicComponent.getHeight(),
            1,
            1,
            physicComponent.getRotation(),
            0,
            0,
            texture.getWidth(),
            texture.getHeight(),
            false,
            false);
    }
}
