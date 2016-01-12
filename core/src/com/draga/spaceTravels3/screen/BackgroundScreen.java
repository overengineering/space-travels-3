package com.draga.spaceTravels3.screen;

import com.badlogic.gdx.assets.AssetManager;
import com.draga.background.Background;
import com.draga.background.BackgroundLoader;
import com.draga.spaceTravels3.Constants;
import com.draga.spaceTravels3.RandomBackgroundPositionController;
import com.draga.spaceTravels3.SpaceTravels3;
import com.draga.spaceTravels3.manager.BackgroundPositionManager;
import com.draga.spaceTravels3.ui.Screen;

public class BackgroundScreen extends Screen
{
    private final AssetManager assetManager;
    private       Background   background;

    public BackgroundScreen()
    {
        super(false, false);
        this.assetManager = new AssetManager();
        this.assetManager.setLoader(Background.class, new BackgroundLoader());
        this.assetManager.load(Constants.Visual.Background.BACKGROUND_ASSET_DESCRIPTOR);
        this.assetManager.update();
    }

    @Override
    public void show()
    {

    }

    @Override
    public void render(float deltaTime)
    {
        if (this.background == null)
        {
            if (this.assetManager.update())
            {
                this.background =
                    this.assetManager.get(Constants.Visual.Background.BACKGROUND_ASSET_DESCRIPTOR);
                BackgroundPositionManager.create(this.background);
                BackgroundPositionManager.addBackgroundPositionController(new RandomBackgroundPositionController());
            }
        }
        else
        {
            BackgroundPositionManager.update(deltaTime);

            SpaceTravels3.gameViewport.apply();
            SpaceTravels3.spriteBatch.setProjectionMatrix(SpaceTravels3.gameViewport.getCamera().combined);
            SpaceTravels3.spriteBatch.begin();
            this.background.draw(SpaceTravels3.gameViewport.getCamera(), SpaceTravels3.spriteBatch);
            SpaceTravels3.spriteBatch.end();
        }
    }

    @Override
    public void resize(int width, int height)
    {

    }

    @Override
    public void pause()
    {

    }

    @Override
    public void resume()
    {

    }

    @Override
    public void hide()
    {

    }

    @Override
    public void dispose()
    {
        this.assetManager.dispose();
    }
}
