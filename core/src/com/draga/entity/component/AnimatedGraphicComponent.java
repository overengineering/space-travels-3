package com.draga.entity.component;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Created by Administrator on 26/09/2015.
 */
public class AnimatedGraphicComponent extends GraphicComponent {
    private Animation animation;
    public AnimatedGraphicComponent(
        String packFilePath, PhysicComponent physicComponent) {
        super(packFilePath, physicComponent);
        TextureAtlas textureAtlas = new TextureAtlas(packFilePath);
        this.animation = new Animation(0.1f, textureAtlas.getRegions());
    }

    @Override public void reset() {
        super.reset();
        animation = null;
    }
}
