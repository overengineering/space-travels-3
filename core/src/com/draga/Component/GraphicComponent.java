package com.draga.component;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;

public class GraphicComponent extends Component {
    protected Texture texture;
    private PhysicComponent physicComponent;

    public GraphicComponent(String texturePath, PhysicComponent physicComponent) {
        FileHandle fileHandle = Gdx.files.internal(texturePath);

        this.texture = new Texture(fileHandle);
        this.physicComponent = physicComponent;
    }

    public void draw(SpriteBatch spriteBatch) {
        float halfWidth = physicComponent.getWidth() / 2;
        float halfHeight = physicComponent.getHeight() / 2;
        spriteBatch.draw(
            texture,
            physicComponent.getX() - halfWidth,
            physicComponent.getY() - halfHeight,
            halfWidth,
            halfHeight,
            physicComponent.getWidth(),
            physicComponent.getHeight(),
            1,
            1,
            physicComponent.getAngle() * MathUtils.radiansToDegrees,
            0,
            0,
            texture.getWidth(),
            texture.getHeight(),
            false,
            false);
    }

    @Override public void dispose() {
        texture.dispose();
    }
}
