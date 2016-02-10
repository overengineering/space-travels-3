package com.draga.spaceTravels3;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.draga.background.Background;

public class RandomBackgroundPositionController extends BackgroundPositionController
{
    private static final float MAX_SPEED = 100f;

    private Vector2 velocity;

    public RandomBackgroundPositionController()
    {
        this.velocity = new Vector2(0f, 0f);
    }

    @Override
    public void move(Background background, float deltaTime)
    {
        float accelerationX = MathUtils.random(-100f, 100f) * deltaTime;
        float accelerationY = MathUtils.random(-100f, 100f) * deltaTime;

        this.velocity.add(accelerationX, accelerationY);

        this.velocity.limit(MAX_SPEED);

        background.getPosition().add(this.velocity.x * deltaTime, this.velocity.y * deltaTime);
    }
}
