package com.draga.planet;

import com.draga.GameEntity;
import com.draga.component.CircularPhysicComponent;
import com.draga.component.GraphicComponent;

/**
 * Created by Administrator on 03/09/2015.
 */
public class Planet extends GameEntity {
    public Planet(float mass, float radius, float x, float y, String texturePath) {
        physicComponent = new CircularPhysicComponent(mass, radius, x, y);
        graphicComponent = new GraphicComponent(texturePath, physicComponent);
    }

    @Override
    public void update(float elapsed) {
        physicComponent.update(elapsed);
    }
}
