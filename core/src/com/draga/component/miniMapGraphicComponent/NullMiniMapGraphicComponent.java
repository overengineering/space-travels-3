package com.draga.component.miniMapGraphicComponent;

import com.badlogic.gdx.graphics.Color;
import com.draga.component.PhysicsComponent;

public class NullMiniMapGraphicComponent extends MiniMapGraphicComponent
{
    public NullMiniMapGraphicComponent(
        PhysicsComponent physicsComponent,
        Color colour)
    {
        super(physicsComponent, colour);
    }

    @Override
    public void draw()
    {

    }
}
