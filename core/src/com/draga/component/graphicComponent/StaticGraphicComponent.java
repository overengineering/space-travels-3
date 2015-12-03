package com.draga.component.graphicComponent;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.draga.manager.asset.AssMan;
import com.draga.component.PhysicsComponent;

public class StaticGraphicComponent extends GraphicComponent
{
    private Texture texture;

    public StaticGraphicComponent(
        String texturePath,
        float width,
        float height,
        PhysicsComponent physicsComponent)
    {
        super(physicsComponent, height, width);
        this.texture = AssMan.getAssMan().get(texturePath);
    }

    @Override
    public void update(float deltaTime)
    {

    }

    @Override
    public void draw(SpriteBatch spriteBatch)
    {
        spriteBatch.draw(
            texture,
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
            texture.getWidth(),
            texture.getHeight(),
            false,
            false);

    }

    @Override
    public void dispose()
    {
        // Doesn't dispose texture.
    }

    @Override
    public boolean isFinished()
    {
        return false;
    }
}
