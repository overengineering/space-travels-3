package com.draga;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class GraphicComponent {
    protected Texture texture;
    abstract public void draw(SpriteBatch batch);

    public GraphicComponent(String texturePath) {
        this.texture = new Texture(Gdx.files.internal(texturePath));
    }
}
