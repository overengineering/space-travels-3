package com.draga.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.*;
import com.draga.MaskBits;

public class Planet extends GameEntity
{

    private CircleShape circleShape;
    private FixtureDef  fixtureDef;
    private Fixture     fixture;
    private Texture     texture;

    public Planet(float mass, float radius, float x, float y, String texturePath)
    {
        circleShape = new CircleShape();
        circleShape.setRadius(radius);

        fixtureDef = new FixtureDef();
        fixtureDef.shape = circleShape;
        float area = radius * radius * MathUtils.PI;
        fixtureDef.density = mass / area;
        fixtureDef.friction = 1f;
        fixtureDef.restitution = 1f;
        fixtureDef.filter.categoryBits = MaskBits.PLANET;
        fixtureDef.filter.maskBits = MaskBits.SHIP;

        bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(x, y);
        bodyDef.angle = 0;


        FileHandle fileHandle = Gdx.files.internal(texturePath);
        texture = new Texture(fileHandle);
    }


    @Override public void update(float deltaTime)
    {

    }

    @Override public void draw(SpriteBatch spriteBatch)
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

    @Override public void dispose()
    {
        circleShape.dispose();
    }

    @Override public void createBody(World world)
    {
        body = world.createBody(bodyDef);
        body.setUserData(this);
        fixture = body.createFixture(fixtureDef);
    }
}