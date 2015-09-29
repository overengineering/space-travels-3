package com.draga.entity;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.draga.entity.component.AnimationGraphicComponent;
import com.draga.entity.component.RectangularPhysicComponent;

/**
 * Created by Administrator on 26/09/2015.
 */
public class Explosion extends GameEntity{
    public Explosion(float x, float y) {
        this.physicComponent = new RectangularPhysicComponent(
            x, y, 10, 10, 0, BodyDef.BodyType.KinematicBody);
        this.graphicComponent =
            new AnimationGraphicComponent("explosion/explosion.atlas", physicComponent);
    }
}
