package com.draga.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.draga.entity.component.GraphicComponent;
import com.draga.entity.component.InputComponent;
import com.draga.entity.component.PhysicComponent;

public abstract class GameEntity {
    public InputComponent inputComponent = null;
    public PhysicComponent physicComponent = null;
    public GraphicComponent graphicComponent = null;

    public abstract void update(float elapsed);

    public void draw(SpriteBatch batch) {
        if (graphicComponent != null) {
            graphicComponent.draw(batch);
        }
    }

    public void dispose(){
        if (inputComponent != null) {
            inputComponent.dispose();
        }
        if (physicComponent != null) {
            physicComponent.dispose();
        }
        if (graphicComponent != null) {
            graphicComponent.dispose();
        }
    }
}
