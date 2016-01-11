package com.draga.spaceTravels3;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.draga.PooledVector2;

public class RandomBackgroundPositionController extends BackgroundPositionController
{
    private static final float MAX_SPEED = 100f;

    private Vector2 velocity;
    private Vector2 acceleration;

    public RandomBackgroundPositionController()
    {
        this.velocity = new Vector2(MathUtils.random(-100f, 100f), MathUtils.random(-100f, 100f));
        this.acceleration =
            new Vector2(MathUtils.random(-100f, 100f), MathUtils.random(-100f, 100f));
    }

    @Override
    public PooledVector2 getMovement(float deltaTime)
    {
        try (PooledVector2 newFrameAcceleration = PooledVector2.newVector2(
            MathUtils.random(-100f, 100f),
            MathUtils.random(-100f, 100f)))
        {
            newFrameAcceleration.scl(deltaTime);
            this.acceleration.add(newFrameAcceleration);
        }

        try (PooledVector2 frameAcceleration = PooledVector2.newVector2(this.acceleration))
        {
            frameAcceleration.scl(deltaTime);
            this.velocity.add(frameAcceleration);
        }

        this.velocity.limit(MAX_SPEED);

        PooledVector2 movement = PooledVector2.newVector2(this.velocity);

        movement.scl(deltaTime);

        return movement;
    }
}
