package com.draga.spaceTravels3.gameEntity;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.draga.shape.Circle;
import com.draga.spaceTravels3.Constants;
import com.draga.spaceTravels3.component.PhysicsComponent;
import com.draga.spaceTravels3.component.graphicComponent.AnimatedGraphicComponent;
import com.draga.spaceTravels3.manager.GameEntityManager;
import com.draga.spaceTravels3.manager.SettingsManager;
import com.draga.spaceTravels3.manager.asset.AssMan;

public class Explosion extends GameEntity
{
    private Sound sound;

    public Explosion(float x, float y, float width, float height)
    {
        this.physicsComponent = new PhysicsComponent(
            x,
            y,
            0f,
            (height + width) / 4f,
            new GameEntityGroup(GameEntityGroup.GroupOverride.NONE),
            this.getClass(),
            false);

        this.graphicComponent = new AnimatedGraphicComponent(
            AssMan.getAssList().explosionTextureAtlas,
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
