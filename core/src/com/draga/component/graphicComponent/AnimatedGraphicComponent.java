package com.draga.component.graphicComponent;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.draga.manager.asset.AssMan;
import com.draga.component.PhysicsComponent;

public class AnimatedGraphicComponent extends GraphicComponent
{
    private float animationTotalTime = 2f;
    private float        animationCurrentTime;
    private Animation    animation;
    private boolean      isFinished;
    private TextureAtlas textureAtlas;

    public AnimatedGraphicComponent(
        String textureAtlasPath,
        float animationTotalTime,
        float width,
        float height,
        PhysicsComponent physicsComponent)
    {
        super(physicsComponent, width, height);

        animationCurrentTime = 0f;
        this.animationTotalTime = animationTotalTime;
        textureAtlas = AssMan.getAssMan().get(textureAtlasPath);
        animation = new Animation(
            animationTotalTime / textureAtlas.getRegions().size, textureAtlas.getRegions());
    }

    @Override
    public void update(float deltaTime)
    {
        // Avoid overflow.
        animationCurrentTime += deltaTime;
        if (animationCurrentTime > animationTotalTime)
        {
            isFinished = true;
            animationCurrentTime %= animationTotalTime;
        }
    }

    @Override
    public void draw(SpriteBatch spriteBatch)
    {
        TextureRegion textureRegion = animation.getKeyFrame(animationCurrentTime);
        spriteBatch.draw(
            textureRegion,
            this.physicsComponent.getPosition().x - width / 2f,
            this.physicsComponent.getPosition().y - height / 2f,
            width / 2f,
            height / 2f,
            width,
            height,
            1,
            1,
            this.physicsComponent.getAngle());

    }

    @Override
    public void dispose()
    {
        // Doesn't dispose texture atlas.
    }

    @Override
    public boolean isFinished()
    {
        return isFinished;
    }
}
