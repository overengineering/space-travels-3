package com.draga.entity;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.draga.BodyMaskBit;
import com.draga.entity.component.AnimationGraphicComponent;
import com.draga.entity.component.RectangularPhysicComponent;

public class Explosion extends GameEntity
{
    public Explosion(float x, float y)
    {
        this.physicComponent = new RectangularPhysicComponent(
            x, y, 10, 10, 0, BodyDef.BodyType.KinematicBody, BodyMaskBit.EXPLOSION, (short) 0);
        this.graphicComponent =
            new AnimationGraphicComponent("explosion/explosion.atlas", physicComponent);
    }
}
