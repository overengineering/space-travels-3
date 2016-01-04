package com.draga.spaceTravels3.component.graphicComponent;

import com.badlogic.gdx.graphics.Texture;
import com.draga.spaceTravels3.SpaceTravels3;
import com.draga.spaceTravels3.component.physicsComponent.PhysicsComponent;
import com.draga.spaceTravels3.manager.asset.AssMan;

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
    public void draw()
    {
        SpaceTravels3.spriteBatch.draw(
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
}
