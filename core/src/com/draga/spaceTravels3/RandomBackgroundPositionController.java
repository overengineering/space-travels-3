package com.draga.spaceTravels3;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.draga.PooledVector2;

public class RandomBackgroundPositionController extends BackgroundPositionController
{
    private static final float MAX_SPEED = 100f;

    private Vector2 velocity;

    public RandomBackgroundPositionController()
    {
        this.velocity = new Vector2(0f, 0f);
    }

    @Override
    public PooledVector2 getMovement(float deltaTime)
    {
        float accelerationX = MathUtils.random(-100f, 100f) * deltaTime;
        float accelerationY = MathUtils.random(-100f, 100f) * deltaTime;

        this.velocity.add(accelerationX, accelerationY);

        this.velocity.limit(MAX_SPEED);

        PooledVector2 movement = PooledVector2.newVector2(this.velocity);

        movement.scl(deltaTime);

        return movement;
    }
}
