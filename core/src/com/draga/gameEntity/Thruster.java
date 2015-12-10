package com.draga.gameEntity;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Vector2;
import com.draga.Constants;
import com.draga.component.PhysicsComponent;
import com.draga.component.graphicComponent.AnimatedGraphicComponent;
import com.draga.manager.InputManager;
import com.draga.manager.SettingsManager;
import com.draga.manager.asset.AssMan;
import com.draga.physic.shape.Circle;

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
            0,
            new Circle(0),
            new GameEntityGroup(GameEntityGroup.GroupOverride.NONE),
            false);

        this.graphicComponent = new AnimatedGraphicComponent(
            AssMan.getAssList().thrusterTexture,
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

        Vector2 thrusterOffsetPosition = Constants.Visual.THRUSTER_OFFSET
            .cpy()
            .sub(this.graphicComponent.getHalfWidth(), 0)
            .rotate(this.ship.physicsComponent.getAngle());

        this.physicsComponent.getPosition()
            .set(this.ship.physicsComponent.getPosition()
                .cpy()
                .add(thrusterOffsetPosition));
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
