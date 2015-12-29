package com.draga.spaceTravels3.gameEntity;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.draga.Vector2;
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

    public Thruster(Ship ship)
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
            AssMan.getAssList().thrusterTextureAtlas,
            Constants.Visual.THRUSTER_ANIMATION_TIME,
            0,
            0,
            this.physicsComponent,
            Animation.PlayMode.LOOP);

        sound = AssMan.getAssMan().get(AssMan.getAssList().thrusterSound);

        // Sound must be loopable.
        soundInstance = sound.loop(0);
    }

    @Override
    public void update(float deltaTime)
    {
        Vector2 inputForce = InputManager.getInputForce();
        if (ship.getCurrentFuel() <= 0)
        {
            inputForce.setZero();
        }

        float thrusterScale = inputForce.len();
        this.graphicComponent.setWidth(Constants.Visual.THRUSTER_MAX_WIDTH * thrusterScale);
        this.graphicComponent.setHeight(Constants.Visual.THRUSTER_MAX_HEIGHT * thrusterScale);

        try (Vector2 thrusterOffsetPosition = Constants.Visual.THRUSTER_OFFSET.cpy())
        {
            thrusterOffsetPosition.sub(this.graphicComponent.getHalfWidth(), 0)
                .rotate(this.ship.physicsComponent.getAngle());

            try (Vector2 shipPosition = this.ship.physicsComponent.getPosition().cpy())
            {
                this.physicsComponent.getPosition()
                    .set(shipPosition
                        .add(thrusterOffsetPosition));
            }
        }
        this.physicsComponent.getVelocity()
            .set(this.ship.physicsComponent.getVelocity());

        this.physicsComponent.setAngle(this.ship.physicsComponent.getAngle());

        sound.setVolume(
            soundInstance,
            inputForce.len() * SettingsManager.getSettings().volume);
    }

    @Override
    public void dispose()
    {
        sound.stop();
    }
}
