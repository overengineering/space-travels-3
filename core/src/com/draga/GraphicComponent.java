package com.draga;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class GraphicComponent {
    protected Texture texture;
    abstract public void draw(SpriteBatch batch);
}
