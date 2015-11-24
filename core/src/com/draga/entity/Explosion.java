package com.draga.entity;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.draga.entity.shape.Circle;
import com.draga.manager.GameEntityManager;
import com.draga.manager.asset.AssMan;
import com.draga.physic.PhysicsComponent;
import com.draga.physic.PhysicsEngine;

public class Explosion extends GameEntity
{

    private static final int   HEIGHT               = 10;
    private static final int   WIDTH                = 10;
    private static final float ANIMATION_TOTAL_TIME = 2f;
    private float     stateTime;
    private Animation animation;
    private Sound     sound;

    public Explosion(
        float x, float y, String textureAtlasPath)
    {
        this.physicsComponent = new PhysicsComponent(x, y, 0, new Circle((HEIGHT + WIDTH) / 2f));

        stateTime = 0f;
        TextureAtlas textureAtlas = AssMan.getAssMan().get(textureAtlasPath);
        animation = new Animation(
            ANIMATION_TOTAL_TIME / textureAtlas.getRegions().size, textureAtlas.getRegions());

        sound = AssMan.getAssMan().get(AssMan.getAssList().explosionSound);
        sound.play();
    }

    @Override
    public void update(float deltaTime)
    {
        stateTime += deltaTime;
        // Can't get if the sound if still playing, can be done only with music.
        if (animation.isAnimationFinished(stateTime))
        {
            GameEntityManager.removeGameEntity(this);
        }
    }

    @Override
    public void draw(SpriteBatch spriteBatch)
    {
        TextureRegion textureRegion = animation.getKeyFrame(stateTime);
        spriteBatch.draw(
            textureRegion,
            this.physicsComponent.getPosition().x - WIDTH / 2f,
            this.physicsComponent.getPosition().y - HEIGHT / 2f,
            WIDTH / 2f,
            HEIGHT / 2f,
            WIDTH,
            HEIGHT,
            1,
            1,
            this.physicsComponent.getAngle() * MathUtils.radiansToDegrees);
    }

    @Override
    public void dispose()
    {
        sound.stop();
        sound.dispose();
    }

    @Override
    public void drawMiniMap()
    {

    }
}
