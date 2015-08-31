package com.draga;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class GameEntity {
    public InputComponent inputComponent = null;
    public PhysicComponent physicComponent = null;
    public GraphicComponent graphicComponent = null;

    public void update(float elapsed){
        if (inputComponent != null){
            inputComponent.update(elapsed);
        }

        if (physicComponent != null){
            physicComponent.update(elapsed);
        }
    }

    public void draw(SpriteBatch batch){
        if (graphicComponent != null){
            graphicComponent.draw(batch);
        }
    }
}
