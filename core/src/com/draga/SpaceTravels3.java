package com.draga;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class SpaceTravels3 extends ApplicationAdapter {
	private GameWorld gameWorld;
	
	@Override
	public void create () {
		gameWorld = new GameWorld();
	}

	@Override
	public void render () {
		gameWorld.update(Gdx.graphics.getDeltaTime());
		gameWorld.draw();
	}
}
