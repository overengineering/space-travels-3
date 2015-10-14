package com.draga.entity;

import com.badlogic.gdx.physics.box2d.Contact;

/**
 * Created by qwasqaws on 01/10/15.
 */
public class Box2dCollisionResolutionComponent
{
    protected GameEntity gameEntity = null;

    public Box2dCollisionResolutionComponent(GameEntity gameEntity)
    {
        this.gameEntity = gameEntity;
    }

    public void Resolve(Contact contact)
    {

    }
}
