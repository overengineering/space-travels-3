package com.draga.gameEntity;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Vector2;
import com.draga.physic.shape.Circle;
import com.draga.component.graphicComponent.AnimatedGraphicComponent;
import com.draga.manager.InputManager;
import com.draga.manager.asset.AssMan;
import com.draga.component.PhysicsComponent;

public class Thruster extends GameEntity
{
    // Thruster.
    private static final float MAX_WIDTH      = 5;
    private static final float MAX_HEIGHT     = 5;
    private static final float ANIMATION_TIME = 1f;
    private final Ship    ship;
    private       Vector2 offset;
    private       Circle  shape;

    public Thruster(Ship ship)
    {
        this.ship = ship;
        this.offset =
            new Vector2(-(ship.graphicComponent.height / 2f) / 2f, 0);
        shape = new Circle(0);
        this.physicsComponent = new PhysicsComponent(
            ship.physicsComponent.getPosition().x + this.offset.x,
            ship.physicsComponent.getPosition().y + this.offset.y,
            0,
            shape,
            new GameEntityGroup(GameEntityGroup.GroupOverride.NONE));
        this.graphicComponent = new AnimatedGraphicComponent(
            AssMan.getAssList().thruster,
            ANIMATION_TIME,
            0,
            0,
            this.physicsComponent,
            Animation.PlayMode.LOOP);
    }

    @Override
    public void update(float deltaTime)
    {
        Vector2 inputForce = InputManager.getInputForce();
        if (ship.getFuel() <= 0)
        {
            inputForce.setZero();
        }

        float thrusterScale = inputForce.len();
        this.graphicComponent.width = MAX_WIDTH * thrusterScale;
        this.graphicComponent.height = MAX_HEIGHT * thrusterScale;
        shape.radius = MAX_WIDTH / 2f * thrusterScale;

        Vector2 thrusterOffsetFromCentre = offset
            .cpy()
            .sub(this.graphicComponent.width / 2f, 0);
        this.physicsComponent.getPosition().set(this.ship.physicsComponent.getPosition().cpy());
        Vector2 thrusterRotateOffset =
            new Vector2(thrusterOffsetFromCentre).rotate(this.ship.physicsComponent.getAngle());
        this.physicsComponent.getPosition().add(thrusterRotateOffset);

        this.physicsComponent.getVelocity().set(this.ship.physicsComponent.getVelocity());

        this.physicsComponent.setAngle(this.ship.physicsComponent.getAngle());
    }

    /*@Override
    public void drawMiniMap()
    {

    }*/
}
