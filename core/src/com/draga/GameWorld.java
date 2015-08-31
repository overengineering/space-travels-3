package com.draga;

import com.draga.Ship.Ship;

public class GameWorld extends World {
    public GameWorld() {
        super();
        gameEntities.add(new Ship());
    }

}
