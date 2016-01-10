package com.draga.spaceTravels3;

import com.badlogic.gdx.math.Vector2;
import com.draga.PooledVector2;
import com.draga.spaceTravels3.component.physicsComponent.PhysicsComponent;

public class PhysicsComponentBackgroundPositionController extends BackgroundPositionController
{
    private final PhysicsComponent physicsComponent;
    private       Vector2          lastPosition;

    public PhysicsComponentBackgroundPositionController(PhysicsComponent physicsComponent)
    {
        this.physicsComponent = physicsComponent;
        this.lastPosition = physicsComponent.getPosition().cpy();
    }

    @Override
    public PooledVector2 getMovement(float deltaTime)
    {
        PooledVector2 movement = PooledVector2.newVector2(this.physicsComponent.getPosition());

        movement.sub(this.lastPosition);

        this.lastPosition.set(this.physicsComponent.getPosition());

        return movement;
    }
}
