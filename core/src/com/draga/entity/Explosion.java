package com.draga.entity;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.draga.BodyMaskBit;
import com.draga.entity.component.AnimationGraphicComponent;
import com.draga.entity.component.RectangularPhysicComponent;
import com.draga.manager.GameEntityManager;

public class Explosion extends GameEntity
{

    private final AnimationGraphicComponent animationGraphicComponent;

    public Explosion(float x, float y)
    {
        this.physicComponent = new RectangularPhysicComponent(
            x, y, 10, 10, 0, BodyDef.BodyType.KinematicBody, BodyMaskBit.EXPLOSION, 0);
        animationGraphicComponent =
            new AnimationGraphicComponent("explosion/explosion.atlas", physicComponent);
        this.graphicComponent = animationGraphicComponent;
    }

    @Override public void update(float elapsed)
    {
        super.update(elapsed);
        if (animationGraphicComponent.isAnimationFinished())
        {
            GameEntityManager.addGameEntityToDestroy(this);
        }
    }
}
