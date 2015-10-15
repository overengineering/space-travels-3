package com.draga.entity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.*;
import com.draga.MaskBits;
import com.draga.manager.AssMan;

public class Star extends GameEntity
{

    private static final float WIDTH       = 5f;
    private static final float HEIGHT      = 5f;
    private static final float HALF_WIDTH  = WIDTH / 2f;
    private static final float HALF_HEIGHT = HEIGHT / 2f;
    private PolygonShape polygonShape;
    private FixtureDef   fixtureDef;
    private Texture      texture;
    private Fixture fixture;

    public Star(float x, float y, String texturePath)
    {
        polygonShape = new PolygonShape();
        polygonShape.setAsBox(WIDTH / 2f, HEIGHT / 2f);

        fixtureDef = new FixtureDef();
        fixtureDef.shape = polygonShape;
        fixtureDef.density = 0;
        fixtureDef.friction = 1f;
        fixtureDef.restitution = 0;
        fixtureDef.filter.categoryBits = MaskBits.STAR;
        fixtureDef.filter.maskBits = 0;

        bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(x, y);
        bodyDef.angle = 0;


        texture = AssMan.getAssetManager().get(texturePath);
    }

    @Override
    public void update(float deltaTime)
    {

    }

    @Override
    public void draw(SpriteBatch spriteBatch)
    {
        spriteBatch.draw(
            texture,
            getX() - HALF_WIDTH,
            getY() - HALF_HEIGHT,
            HALF_WIDTH,
            HALF_HEIGHT,
            WIDTH,
            HEIGHT,
            1,
            1,
            body.getAngle() * MathUtils.radiansToDegrees,
            0,
            0,
            texture.getWidth(),
            texture.getHeight(),
            false,
            false);
    }

    @Override
    public void dispose()
    {

    }

    @Override
    public void createBody(World world)
    {
        body = world.createBody(bodyDef);
        body.setUserData(this);
        fixture = body.createFixture(fixtureDef);
    }
}
