package com.draga.entity.component;

import com.badlogic.gdx.physics.box2d.Contact;

/**
 * Created by qwasqaws on 01/10/15.
 */
public class Box2dCollisionResolutionComponent
{
    protected PhysicComponent ownerPhysicComponent = null;

    public Box2dCollisionResolutionComponent(PhysicComponent physicComponent)
    {
        this.ownerPhysicComponent = physicComponent;
    }

    public void Resolve(Contact contact)
    {

    }
}
