package com.draga;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.draga.manager.level.LevelManager;

public class SpaceTravels3 extends ApplicationAdapter {
	private World world;
	
	@Override
	public void create () {
		world = LevelManager.getLevelWorldFromFile("level1.json", new SpriteBatch());
	}

	@Override
	public void render () {
		world.update(Gdx.graphics.getDeltaTime());
		world.draw();
	}
}
