package com.draga.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Pool;
import com.draga.entity.component.GraphicComponent;
import com.draga.entity.component.InputComponent;
import com.draga.entity.component.PhysicComponent;

public abstract class GameEntity implements Pool.Poolable {
    public InputComponent inputComponent = null;
    public PhysicComponent physicComponent = null;
    public GraphicComponent graphicComponent = null;

    public void update(float elapsed) {
        if (inputComponent != null) {
            inputComponent.update(elapsed);
        }
        if (physicComponent != null) {
            physicComponent.update(elapsed);
        }
    }

    public void draw(SpriteBatch batch) {
        if (graphicComponent != null) {
            graphicComponent.draw(batch);
        }
    }

    public void dispose() {
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

    @Override public void reset() {
        if (inputComponent != null) {
            inputComponent.reset();
        }
        if (physicComponent != null) {
            physicComponent.reset();
        }
        if (graphicComponent != null) {
            graphicComponent.reset();
        }
    }
}
