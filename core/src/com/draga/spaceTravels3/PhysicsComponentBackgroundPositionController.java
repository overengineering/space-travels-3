package com.draga.spaceTravels3;

import com.badlogic.gdx.math.Vector2;
import com.draga.background.Background;
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
    public void move(Background background, float deltaTime)
    {
        Vector2 newPosition = this.physicsComponent.getPosition();

        background.getPosition().add(
            (newPosition.x - this.lastPosition.x),
            (newPosition.y - this.lastPosition.y));

        this.lastPosition.set(newPosition);
    }
}
