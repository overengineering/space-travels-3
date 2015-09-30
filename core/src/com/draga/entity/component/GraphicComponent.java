package com.draga.entity.component;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class GraphicComponent extends Component
{
    protected PhysicComponent physicComponent;

    public GraphicComponent(PhysicComponent physicComponent)
    {
        this.physicComponent = physicComponent;
    }

    public abstract void draw(SpriteBatch spriteBatch);

    @Override public void reset()
    {
        physicComponent = null;
    }
}
