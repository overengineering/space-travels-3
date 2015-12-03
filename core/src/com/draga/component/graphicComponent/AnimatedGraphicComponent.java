package com.draga.component.graphicComponent;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.draga.Constants;
import com.draga.SpaceTravels3;
import com.draga.component.PhysicsComponent;
import com.draga.manager.asset.AssMan;
import com.google.common.base.Stopwatch;

import java.util.concurrent.TimeUnit;

public class AnimatedGraphicComponent extends GraphicComponent
{
    private Stopwatch    animationTime;
    private Animation    animation;
    private TextureAtlas textureAtlas;

    public AnimatedGraphicComponent(
        String textureAtlasPath,
        float animationTotalTime,
        float width,
        float height,
        PhysicsComponent physicsComponent,
        Animation.PlayMode playMode)
    {
        super(physicsComponent, width, height);

        animationTime = Stopwatch.createStarted();
        textureAtlas = AssMan.getAssMan().get(textureAtlasPath);
        animation = new Animation(
            animationTotalTime / textureAtlas.getRegions().size,
            textureAtlas.getRegions(),
            playMode);
    }

    @Override
    public void draw()
    {
        TextureRegion textureRegion =
            animation.getKeyFrame(animationTime.elapsed(TimeUnit.NANOSECONDS) * Constants.NANO);

        SpaceTravels3.spriteBatch.draw(
            textureRegion,
            physicsComponent.getPosition().x - getHalfWidth(),
            physicsComponent.getPosition().y - getHalfHeight(),
            getHalfWidth(),
            getHalfHeight(),
            getWidth(),
            getHeight(),
            1,
            1,
            physicsComponent.getAngle());
    }

    @Override
    public void dispose()
    {
        // Doesn't dispose texture atlas.
    }

    public boolean isFinished()
    {
        return animation.isAnimationFinished(animationTime.elapsed(TimeUnit.NANOSECONDS)
            * Constants.NANO);
    }
}
