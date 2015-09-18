package com.draga;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.draga.component.GraphicComponent;
import com.draga.component.InputComponent;
import com.draga.component.PhysicComponent;

public abstract class GameEntity
{
    public InputComponent inputComponent = null;
    public PhysicComponent physicComponent = null;
    public GraphicComponent graphicComponent = null;

    public abstract void update(float elapsed);

    public void draw(SpriteBatch batch)
    {
        if (graphicComponent != null) {
            graphicComponent.draw(batch);
        }
    }
}
