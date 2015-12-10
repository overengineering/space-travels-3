package com.draga.component.miniMapGraphicComponent;

import com.badlogic.gdx.graphics.Color;
import com.draga.component.PhysicsComponent;

public abstract class MiniMapGraphicComponent
{
    public static final MiniMapGraphicComponent NULL_MINI_MAP_GRAPHIC_COMPONENT =
        new MiniMapGraphicComponent(null, null)
        {
            @Override
            public void draw()
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

    public void dispose()
    {

    }
}
