package com.draga;

import com.draga.Planet.Planet;
import com.draga.Ship.Ship;

public class GameWorld extends World {
    public GameWorld(String backgroundTexturePath) {
        super(backgroundTexturePath);
        gameEntities.add(new Ship());
        gameEntities.add(new Planet(1, 64, 100, 100, "jupiter64.png"));
        gameEntities.add(new Planet(1, 48, 100, 300, "earth.png"));
        gameEntities.add(new Planet(1, 40, 200, 100, "mars.png"));
        gameEntities.add(new Planet(1, 60, 300, 100, "venus64.png"));
    }

}
