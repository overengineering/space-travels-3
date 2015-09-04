package com.draga;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;

public class SpaceTravels3 extends ApplicationAdapter {
	private GameWorld gameWorld;
	
	@Override
	public void create () {
		gameWorld = new GameWorld("background4.jpg");
	}

	@Override
	public void render () {
		gameWorld.update(Gdx.graphics.getDeltaTime());
		gameWorld.draw();
	}
}
