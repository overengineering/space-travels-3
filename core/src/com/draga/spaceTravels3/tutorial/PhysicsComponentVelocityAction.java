package com.draga.spaceTravels3.tutorial;

import com.draga.spaceTravels3.component.physicsComponent.PhysicsComponent;

public abstract class PhysicsComponentVelocityAction extends TutorialAction
{
    private final float            velocityToTrigger;
    private final boolean          triggerIfFaster;
    private final PhysicsComponent physicsComponent;

    public PhysicsComponentVelocityAction(
        PhysicsComponent physicsComponent,
        float velocityToTrigger,
        boolean triggerIfFaster)
    {
        this.physicsComponent = physicsComponent;
        this.velocityToTrigger = velocityToTrigger;
        this.triggerIfFaster = triggerIfFaster;
    }

    @Override
    protected boolean isTriggered()
    {
        return this.physicsComponent.getVelocity().len() > this.velocityToTrigger
            == this.triggerIfFaster;
    }
}
