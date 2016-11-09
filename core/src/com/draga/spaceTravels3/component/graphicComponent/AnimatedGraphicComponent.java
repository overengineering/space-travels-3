package com.draga.spaceTravels3.component.graphicComponent;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.draga.spaceTravels3.SpaceTravels3;
import com.draga.spaceTravels3.component.physicsComponent.PhysicsComponent;
import com.google.common.base.Stopwatch;

import java.util.concurrent.TimeUnit;

public class AnimatedGraphicComponent extends GraphicComponent
{
    private Stopwatch    animationTime;
    private Animation    animation;
    private TextureAtlas textureAtlas;

    public AnimatedGraphicComponent(
        TextureAtlas textureAtlas,
        float animationTotalTime,
        float width,
        float height,
        PhysicsComponent physicsComponent,
        Animation.PlayMode playMode)
    {
        super(physicsComponent, width, height);

        this.animationTime = Stopwatch.createStarted();
        this.textureAtlas = textureAtlas;
        this.animation = new Animation(
            animationTotalTime / this.textureAtlas.getRegions().size,
            this.textureAtlas.getRegions(),
            playMode);
    }

    @Override
    public void draw()
    {
        TextureRegion textureRegion =
            this.animation.getKeyFrame(getAnimationTimeSeconds());

        SpaceTravels3.spriteBatch.draw(
            textureRegion,
            this.physicsComponent.getPosition().x - getHalfWidth(),
            this.physicsComponent.getPosition().y - getHalfHeight(),
            getHalfWidth(),
            getHalfHeight(),
            getWidth(),
            getHeight(),
            1f,
            1f,
            this.physicsComponent.getAngle());
    }

    private float getAnimationTimeSeconds()
    {
        return this.animationTime.elapsed(TimeUnit.NANOSECONDS)
            * MathUtils.nanoToSec;
    }

    @Override
    public void dispose()
    {
        this.animationTime.stop();
    }

    @Override
    public TextureRegion getTexture()
    {
        return this.animation.getKeyFrame(getAnimationTimeSeconds());
    }

    public boolean isFinished()
    {
        return this.animation.isAnimationFinished(getAnimationTimeSeconds());
    }
}
