package com.draga.entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.draga.MiniMap;
import com.draga.entity.shape.Circle;
import com.draga.graphicComponent.StaticGraphicComponent;
import com.draga.manager.asset.AssMan;
import com.draga.physic.PhysicsComponent;

import java.util.ArrayList;
import java.util.List;

public class Star extends GameEntity
{

    private static final float WIDTH       = 5f;
    private static final float HEIGHT      = 5f;
    private static final float HALF_WIDTH  = WIDTH / 2f;
    private static final float HALF_HEIGHT = HEIGHT / 2f;
    private Texture texture;

    public Star(float x, float y, String texturePath)
    {
        List<Class<? extends GameEntity>> collidesWith = new ArrayList<>();
        collidesWith.add(Ship.class);
        this.physicsComponent = new PhysicsComponent(
            x,
            y,
            0,
            new Circle(HALF_WIDTH),
            new GameEntityGroup(collidesWith));
        this.graphicComponent =
            new StaticGraphicComponent(texturePath, WIDTH, HEIGHT, this.physicsComponent);
        this.texture = AssMan.getAssMan().get(texturePath);
    }

    @Override
    public void update(float deltaTime)
    {

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
                p1.x + this.physicsComponent.getPosition().x,
                p1.y + this.physicsComponent.getPosition().y,
                p2.x + this.physicsComponent.getPosition().x,
                p2.y + this.physicsComponent.getPosition().y,
                p3.x + this.physicsComponent.getPosition().x,
                p3.y + this.physicsComponent.getPosition().y);
        }
    }
}
