package com.draga.entity.component;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.draga.entity.GameEntity;

/**
 * Created by Administrator on 03/09/2015.
 */
public class RectangularPhysicComponent extends PhysicComponent {
    private final float width;
    private final float height;

    public RectangularPhysicComponent(
        float x,
        float y,
        int width,
        int height,
        float mass,
        BodyDef.BodyType bodyType,
        GameEntity gameEntity,
        float gravityScale, World box2dWorld) {
        super(x, y, bodyType, 0, gameEntity, gravityScale, box2dWorld);

        this.width = width;
        this.height = height;

        PolygonShape polygonShape = new PolygonShape();
        polygonShape.setAsBox(width / 2f, height / 2f);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = polygonShape;
        float area = width * height;
        fixtureDef.density = mass / area;
        fixtureDef.friction = 1f;
        fixtureDef.restitution = 1f;

        body.createFixture(fixtureDef);

        polygonShape.dispose();
    }

    public float getHeight() {
        return this.height;
    }

    public float getWidth() {
        return this.width;
    }

    @Override public void update(float elapsed) {
    }
}
