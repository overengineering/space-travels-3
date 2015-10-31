package com.draga.entity;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.*;
import com.draga.MaskBits;
import com.draga.manager.asset.AssMan;
import com.draga.manager.GameEntityManager;

public class Explosion extends GameEntity
{

    private static final int   HEIGHT               = 10;
    private static final int   WIDTH                = 10;
    private static final float ANIMATION_TOTAL_TIME = 2f;
    private PolygonShape polygonShape;
    private FixtureDef   fixtureDef;
    private float        stateTime;
    private Animation    animation;
    private Fixture      fixture;

    public Explosion(
        float x, float y, String textureAtlasPath)
    {
        polygonShape = new PolygonShape();
        polygonShape.setAsBox(WIDTH / 2f, HEIGHT / 2f);

        fixtureDef = new FixtureDef();
        fixtureDef.shape = polygonShape;
        fixtureDef.density = 0;
        fixtureDef.friction = 1f;
        fixtureDef.restitution = 0;
        fixtureDef.filter.categoryBits = MaskBits.EXPLOSION;
        fixtureDef.filter.maskBits = 0;

        bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(x, y);
        bodyDef.angle = 0;

        stateTime = 0f;
        TextureAtlas textureAtlas = AssMan.getAssMan().get(textureAtlasPath);
        animation = new Animation(
            ANIMATION_TOTAL_TIME / textureAtlas.getRegions().size, textureAtlas.getRegions());
    }

    @Override
    public void update(float deltaTime)
    {
        stateTime += deltaTime;
        if (animation.isAnimationFinished(stateTime))
        {
            GameEntityManager.getGameEntitiesToDestroy().add(this);
        }
    }

    @Override
    public void draw(SpriteBatch spriteBatch)
    {
        TextureRegion textureRegion = animation.getKeyFrame(stateTime);
        spriteBatch.draw(
            textureRegion,
            getX() - WIDTH / 2f,
            getY() - HEIGHT / 2f,
            WIDTH / 2f,
            HEIGHT / 2f,
            WIDTH,
            HEIGHT,
            1,
            1,
            body.getAngle() * MathUtils.radiansToDegrees);
    }

    @Override
    public void dispose()
    {
        polygonShape.dispose();
    }

    @Override
    public void createBody(World world)
    {
        body = world.createBody(bodyDef);
        body.setUserData(this);
        fixture = body.createFixture(fixtureDef);
    }
}
