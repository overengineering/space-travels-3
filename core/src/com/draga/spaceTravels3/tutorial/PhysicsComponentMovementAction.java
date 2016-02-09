package com.draga.spaceTravels3.tutorial;

import com.badlogic.gdx.math.Vector2;
import com.draga.spaceTravels3.component.physicsComponent.PhysicsComponent;

public abstract class PhysicsComponentMovementAction extends TutorialAction
{
    private final float            distanceToTrigger;
    private final PhysicsComponent physicsComponent;
    private       Vector2          initialPosition;

    public PhysicsComponentMovementAction(
        PhysicsComponent physicsComponent,
        float distanceToTrigger)
    {
        this.physicsComponent = physicsComponent;
        this.distanceToTrigger = distanceToTrigger;
        this.initialPosition = new Vector2(physicsComponent.getPosition());
    }

    @Override
    protected boolean isTriggered()
    {
        float distance = this.physicsComponent.getPosition().dst(this.initialPosition);

        return distance >= this.distanceToTrigger;
    }
}
