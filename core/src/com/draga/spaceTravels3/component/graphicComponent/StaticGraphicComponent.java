package com.draga.spaceTravels3.component.graphicComponent;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.draga.spaceTravels3.SpaceTravels3;
import com.draga.spaceTravels3.component.physicsComponent.PhysicsComponent;

public class StaticGraphicComponent extends GraphicComponent
{
    private Texture texture;

    public StaticGraphicComponent(
        Texture texture,
        float width,
        float height,
        PhysicsComponent physicsComponent)
    {
        super(physicsComponent, width, height);
        this.texture = texture;
    }

    @Override
    public void draw()
    {
        SpaceTravels3.spriteBatch.draw(
            this.texture,
            this.physicsComponent.getPosition().x - getHalfWidth(),
            this.physicsComponent.getPosition().y - getHalfHeight(),
            getHalfWidth(),
            getHalfHeight(),
            getWidth(),
            getHeight(),
            1,
            1,
            this.physicsComponent.getAngle(),
            0,
            0,
            this.texture.getWidth(),
            this.texture.getHeight(),
            false,
            false);
    }

    @Override
    public void dispose()
    {
        // Doesn't dispose texture.
    }

    @Override
    public TextureRegion getTexture()
    {
        TextureRegion textureRegion = new TextureRegion(this.texture);

        return textureRegion;
    }
}
