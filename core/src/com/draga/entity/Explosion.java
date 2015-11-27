package com.draga.entity;

import com.badlogic.gdx.audio.Sound;
import com.draga.graphicComponent.AnimatedGraphicComponent;
import com.draga.entity.shape.Circle;
import com.draga.manager.GameEntityManager;
import com.draga.manager.asset.AssMan;
import com.draga.physic.PhysicsComponent;

public class Explosion extends GameEntity
{

    private static final int   HEIGHT               = 10;
    private static final int   WIDTH                = 10;
    private static final float ANIMATION_TOTAL_TIME = 2f;
    private Sound     sound;

    public Explosion(
        float x, float y)
    {
        this.physicsComponent = new PhysicsComponent(
            x,
            y,
            0,
            new Circle((HEIGHT + WIDTH) / 2f),
            new GameEntityGroup(GameEntityGroup.GroupOverride.NONE));

        this.graphicComponent = new AnimatedGraphicComponent(AssMan.getAssList().explosion,
            ANIMATION_TOTAL_TIME,
            WIDTH,
            HEIGHT,
            this.physicsComponent);

        sound = AssMan.getAssMan().get(AssMan.getAssList().explosionSound);
        sound.play();
    }

    @Override
    public void update(float deltaTime)
    {
        // Can't get if the sound if still playing, can be done only with music.
        if (this.graphicComponent.isFinished())
        {
            GameEntityManager.removeGameEntity(this);
        }
    }

    @Override
    public void dispose()
    {
        super.dispose();
        sound.stop();
        sound.dispose();
    }
}
