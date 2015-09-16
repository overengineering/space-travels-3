package com.draga;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.draga.planet.Planet;
import com.draga.ship.Ship;

public class GameWorld extends World {
    public GameWorld(String backgroundTexturePath, SpriteBatch spriteBatch, int width, int height) {
        super(backgroundTexturePath, spriteBatch, width, height);
    }

}
