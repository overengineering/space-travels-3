package com.draga.entity.component;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;

/**
 * Created by Administrator on 26/09/2015.
 */
public class AnimationGraphicComponent extends GraphicComponent
{
    public static final float TOTAL_ANIMATION_TIME = 1f;
    private Animation animation;
    private float stateTime;

    public AnimationGraphicComponent(
        String packFilePath, PhysicComponent physicComponent)
    {
        super(physicComponent);

        stateTime = 0f;

        TextureAtlas textureAtlas = new TextureAtlas(packFilePath);
        this.animation = new Animation(TOTAL_ANIMATION_TIME / textureAtlas.getRegions().size, textureAtlas.getRegions());
    }

    @Override public void draw(SpriteBatch spriteBatch)
    {
        float halfWidth = physicComponent.getWidth() / 2;
        float halfHeight = physicComponent.getHeight() / 2;
        stateTime += Gdx.graphics.getDeltaTime();
        TextureRegion textureRegion = animation.getKeyFrame(stateTime);
        spriteBatch.draw(
            textureRegion,
            physicComponent.getX() - halfWidth,
            physicComponent.getY() - halfHeight,
            halfWidth,
            halfHeight,
            physicComponent.getWidth(),
            physicComponent.getHeight(),
            1,
            1,
            physicComponent.getAngle() * MathUtils.radiansToDegrees);

    }

    @Override public void reset()
    {
        super.reset();
        animation = null;
    }

    @Override public void dispose()
    {

    }
}
