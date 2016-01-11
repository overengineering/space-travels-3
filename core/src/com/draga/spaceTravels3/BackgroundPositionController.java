package com.draga.spaceTravels3;

import com.draga.PooledVector2;

public abstract class BackgroundPositionController
{
    public abstract PooledVector2 getMovement(float deltaTime);
}
