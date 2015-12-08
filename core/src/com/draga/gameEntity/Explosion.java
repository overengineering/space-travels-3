package com.draga.gameEntity;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.draga.Constants;
import com.draga.component.PhysicsComponent;
import com.draga.component.graphicComponent.AnimatedGraphicComponent;
import com.draga.manager.GameEntityManager;
import com.draga.manager.SettingsManager;
import com.draga.manager.asset.AssMan;
import com.draga.physic.shape.Circle;

public class Explosion extends GameEntity
{
    private Sound sound;

    public Explosion(float x, float y, float width, float height)
    {
        this.physicsComponent = new PhysicsComponent(
            x,
            y,
            0f,
            new Circle((height + width) / 4f),
            new GameEntityGroup(GameEntityGroup.GroupOverride.NONE),
            false);

        this.graphicComponent = new AnimatedGraphicComponent(
            AssMan.getAssList().explosion,
            Constants.Visual.EXPLOSION_LIFETIME,
            width,
            height,
            this.physicsComponent,
            Animation.PlayMode.NORMAL);

        sound = AssMan.getAssMan().get(AssMan.getAssList().explosionSound);
        sound.play(SettingsManager.getSettings().volume);
    }

    @Override
    public void update(float deltaTime)
    {
        // Can't get whether the sound is still playing, can be done only with music.
        // Sound must be shorter than animation.
        if (((AnimatedGraphicComponent) this.graphicComponent).isFinished())
        {
            GameEntityManager.removeGameEntity(this);
        }
    }

    @Override
    public void dispose()
    {
        sound.stop();
        sound.dispose();
        super.dispose();
    }
}
