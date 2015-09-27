package com.draga.entity.planet;

import com.badlogic.gdx.physics.box2d.World;
import com.draga.entity.GameEntity;
import com.draga.entity.component.CircularPhysicComponent;
import com.draga.entity.component.GraphicComponent;

/**
 * Created by Administrator on 03/09/2015.
 */
public class Planet extends GameEntity {
    public Planet(float mass, float radius, float x, float y, String texturePath, World box2dWorld) {
        physicComponent = new CircularPhysicComponent(mass, radius, x, y, this, 0, box2dWorld);
        graphicComponent = new GraphicComponent(texturePath, physicComponent);
    }
}
