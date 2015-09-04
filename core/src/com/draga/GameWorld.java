package com.draga;

import com.draga.Planet.Planet;
import com.draga.Ship.Ship;

public class GameWorld extends World {
    public GameWorld() {
        super();
        gameEntities.add(new Ship());
        gameEntities.add(new Planet(1, 100, "jupiter64.png"));
    }

}
