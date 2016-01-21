package com.draga.spaceTravels3.gameEntity;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.draga.spaceTravels3.Constants;
import com.draga.spaceTravels3.component.graphicComponent.AnimatedGraphicComponent;
import com.draga.spaceTravels3.component.physicsComponent.PhysicsComponent;
import com.draga.spaceTravels3.component.physicsComponent.PhysicsComponentType;
import com.draga.spaceTravels3.manager.GameEntityManager;
import com.draga.spaceTravels3.manager.SettingsManager;
import com.draga.spaceTravels3.manager.asset.AssMan;

public class Explosion extends GameEntity
{
    private Sound sound;

    public Explosion(float x, float y, float width, float height, TextureAtlas textureAtlas)
    {
        this.physicsComponent = new PhysicsComponent(
            x,
            y,
            0f,
            (height + width) / 4f,
            new GameEntityGroup(GameEntityGroup.GroupOverride.NONE),
            this.getClass(),
            false,
            PhysicsComponentType.DYNAMIC);

        this.graphicComponent = new AnimatedGraphicComponent(
            textureAtlas,
            Constants.Visual.EXPLOSION_LIFETIME,
            width,
            height,
            this.physicsComponent,
            Animation.PlayMode.NORMAL);

        this.sound = AssMan.getGameAssMan().get(AssMan.getAssList().explosionSound);
        this.sound.play(SettingsManager.getSettings().volumeFX);
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
        this.sound.stop();
        this.sound.dispose();
        super.dispose();
    }
}
