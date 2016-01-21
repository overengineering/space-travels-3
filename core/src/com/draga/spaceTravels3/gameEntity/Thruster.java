package com.draga.spaceTravels3.gameEntity;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.draga.PooledVector2;
import com.draga.spaceTravels3.Constants;
import com.draga.spaceTravels3.component.graphicComponent.AnimatedGraphicComponent;
import com.draga.spaceTravels3.component.physicsComponent.PhysicsComponent;
import com.draga.spaceTravels3.component.physicsComponent.PhysicsComponentType;
import com.draga.spaceTravels3.manager.InputManager;
import com.draga.spaceTravels3.manager.SettingsManager;
import com.draga.spaceTravels3.manager.asset.AssMan;

public class Thruster extends GameEntity
{
    private final Ship ship;

    // Sound
    private Sound sound;
    private long  soundInstance;

    public Thruster(Ship ship, TextureAtlas textureAtlas)
    {
        this.ship = ship;

        this.physicsComponent = new PhysicsComponent(
            ship.physicsComponent.getPosition().x + Constants.Visual.THRUSTER_OFFSET.x,
            ship.physicsComponent.getPosition().y + Constants.Visual.THRUSTER_OFFSET.y,
            0f,
            0f,
            new GameEntityGroup(GameEntityGroup.GroupOverride.NONE),
            this.getClass(),
            false,
            PhysicsComponentType.DYNAMIC);

        this.graphicComponent = new AnimatedGraphicComponent(
            textureAtlas,
            Constants.Visual.THRUSTER_ANIMATION_TIME,
            0,
            0,
            this.physicsComponent,
            Animation.PlayMode.LOOP);

        this.sound = AssMan.getGameAssMan().get(AssMan.getAssList().thrusterSound);

        // Sound must be loopable.
        this.soundInstance = this.sound.loop(0);
    }

    @Override
    public void update(float deltaTime)
    {
        PooledVector2 inputForce = InputManager.getInputForce();
        if (!this.ship.isInfiniteFuel() && this.ship.getCurrentFuel() <= 0)
        {
            inputForce.setZero();
        }

        float thrusterScale = inputForce.len();
        this.graphicComponent.setWidth(Constants.Visual.THRUSTER_MAX_WIDTH * thrusterScale);
        this.graphicComponent.setHeight(Constants.Visual.THRUSTER_MAX_HEIGHT * thrusterScale);

        try (PooledVector2 thrusterOffsetPosition = Constants.Visual.THRUSTER_OFFSET.cpy())
        {
            thrusterOffsetPosition.sub(this.graphicComponent.getHalfWidth(), 0)
                .rotate(this.ship.physicsComponent.getAngle());

            try (PooledVector2 shipPosition = PooledVector2.newVector2(this.ship.physicsComponent.getPosition()
                .cpy()))
            {
                this.physicsComponent.getPosition()
                    .set(shipPosition
                        .add(thrusterOffsetPosition));
            }
        }
        this.physicsComponent.getVelocity()
            .set(this.ship.physicsComponent.getVelocity());

        this.physicsComponent.setAngle(this.ship.physicsComponent.getAngle());

        this.sound.setVolume(
            this.soundInstance,
            inputForce.len() * SettingsManager.getSettings().volumeFX);
    }

    @Override
    public void dispose()
    {
        this.sound.stop();
    }
}
