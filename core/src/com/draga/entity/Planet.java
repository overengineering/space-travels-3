package com.draga.entity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.*;
import com.draga.MaskBits;
import com.draga.manager.AssMan;

public class Planet extends GameEntity
{
    private final float MASS_MULTIPLIER = 0.8f;
    private CircleShape circleShape;
    private FixtureDef  fixtureDef;
    private Fixture     fixture;
    private Texture     texture;

    public Planet(
        float mass, float radius, float x, float y, String texturePath)
    {
        circleShape = new CircleShape();
        circleShape.setRadius(radius);

        fixtureDef = new FixtureDef();
        fixtureDef.shape = circleShape;
        float area = radius * radius * MathUtils.PI;
        fixtureDef.density = (mass * MASS_MULTIPLIER) / area;
        fixtureDef.friction = 1f;
        fixtureDef.restitution = 0;
        fixtureDef.filter.categoryBits = MaskBits.PLANET;
        fixtureDef.filter.maskBits = MaskBits.SHIP;

        bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(x, y);
        bodyDef.angle = 0;


        this.texture = AssMan.getAssetManager().get(texturePath);
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
            getX() - fixture.getShape().getRadius(),
            getY() - fixture.getShape().getRadius(),
            fixture.getShape().getRadius(),
            fixture.getShape().getRadius(),
            fixture.getShape().getRadius() * 2,
            fixture.getShape().getRadius() * 2,
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
        circleShape.dispose();
        texture.dispose();
    }

    @Override
    public void createBody(World world)
    {
        body = world.createBody(bodyDef);
        body.setUserData(this);
        fixture = body.createFixture(fixtureDef);
    }
}
