package com.draga.entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.draga.MiniMap;
import com.draga.entity.shape.Circle;
import com.draga.manager.asset.AssMan;
import com.draga.physic.PhysicsComponent;

public class Planet extends GameEntity
{
    private final float radius;
    private final boolean isDestination;
    private       Texture texture;

    public Planet(
        float mass, float radius, float x, float y, String texturePath, boolean isDestination)
    {
        this.radius = radius;
        this.physicsComponent = new PhysicsComponent(x, y, mass, new Circle(radius));
        this.texture = AssMan.getAssMan().get(texturePath);

        this.isDestination = isDestination;
    }

    public boolean isDestination()
    {
        return isDestination;
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
            this.physicsComponent.getPosition().x - this.radius,
            this.physicsComponent.getPosition().y - this.radius,
            this.radius,
            this.radius,
            this.radius * 2,
            this.radius * 2,
            1,
            1,
            this.physicsComponent.getAngle() * MathUtils.radiansToDegrees,
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
        texture.dispose();
    }

    @Override
    public void drawMiniMap()
    {
        MiniMap.getShapeRenderer().set(ShapeRenderer.ShapeType.Line);
        Color planetMinimapColour = isDestination
            ? Color.RED
            : Color.BLUE;
        MiniMap.getShapeRenderer().setColor(planetMinimapColour);
        MiniMap.getShapeRenderer()
            .circle(this.physicsComponent.getPosition().x,
                this.physicsComponent.getPosition().y,
                radius);
    }
}
