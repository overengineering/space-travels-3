package com.draga.entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.draga.MaskBits;
import com.draga.MiniMap;
import com.draga.manager.asset.AssMan;

public class Star extends GameEntity
{

    private static final float WIDTH       = 5f;
    private static final float HEIGHT      = 5f;
    private static final float HALF_WIDTH  = WIDTH / 2f;
    private static final float HALF_HEIGHT = HEIGHT / 2f;
    private PolygonShape polygonShape;
    private FixtureDef   fixtureDef;
    private Texture      texture;
    private Fixture      fixture;

    public Star(float x, float y, String texturePath)
    {
        this.polygonShape = new PolygonShape();
        this.polygonShape.setAsBox(WIDTH / 2f, HEIGHT / 2f);

        this.fixtureDef = new FixtureDef();
        this.fixtureDef.shape = polygonShape;
        this.fixtureDef.density = 0;
        this.fixtureDef.friction = 0;
        this.fixtureDef.restitution = 0;
        this.fixtureDef.filter.categoryBits = MaskBits.STAR;
        this.fixtureDef.filter.maskBits = MaskBits.SHIP;

        this.bodyDef = new BodyDef();
        this.bodyDef.type = BodyDef.BodyType.DynamicBody;
        this.bodyDef.position.set(x, y);
        this.bodyDef.angle = 0;


        this.texture = AssMan.getAssMan().get(texturePath);
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

    @Override
    public void drawMiniMap()
    {
        MiniMap.getShapeRenderer().set(ShapeRenderer.ShapeType.Filled);
        MiniMap.getShapeRenderer().setColor(Color.GOLDENROD);

        Vector2 p1 = new Vector2(0, 7);
        Vector2 p2 = new Vector2(3, 0);
        Vector2 p3 = new Vector2(-3, 0);

        for (int i = 0; i < 5; i++)
        {
            p1.rotate(360 / 5);
            p2.rotate(360 / 5);
            p3.rotate(360 / 5);

            MiniMap.getShapeRenderer().triangle(
                p1.x + getX(), p1.y + getY(),
                p2.x + getX(), p2.y + getY(),
                p3.x + getX(), p3.y + getY());
        }
    }
}
