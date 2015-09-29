package com.draga.entity;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Pool;
import com.draga.entity.component.AnimatedGraphicComponent;
import com.draga.entity.component.RectangularPhysicComponent;

/**
 * Created by Administrator on 26/09/2015.
 */
public class Explosion extends GameEntity{
    public Explosion(float x, float y, World box2dWorld) {
        this.physicComponent = new RectangularPhysicComponent(
            x, y, 50, 50, 0, BodyDef.BodyType.KinematicBody, this, box2dWorld);
        this.graphicComponent =
            new AnimatedGraphicComponent("explosion/explosion.atlas", physicComponent);
    }
}
