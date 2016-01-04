package com.draga.spaceTravels3.component.miniMapGraphicComponent;

import com.badlogic.gdx.graphics.Color;
import com.draga.spaceTravels3.component.physicsComponent.PhysicsComponent;

public abstract class MiniMapGraphicComponent
{
    public static final MiniMapGraphicComponent NULL_MINI_MAP_GRAPHIC_COMPONENT =
        new MiniMapGraphicComponent(null, null)
        {
            @Override
            public void draw()
            {

            }

            @Override
            public void dispose()
            {

            }
        };
    protected PhysicsComponent physicsComponent;
    protected Color            colour;

    public MiniMapGraphicComponent(
        PhysicsComponent physicsComponent,
        Color colour)
    {
        this.physicsComponent = physicsComponent;
        this.colour = colour;
    }

    public abstract void draw();

    public abstract void dispose();
}
