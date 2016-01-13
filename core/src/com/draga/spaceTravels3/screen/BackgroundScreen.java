package com.draga.spaceTravels3.screen;

import com.draga.background.Background;
import com.draga.spaceTravels3.Constants;
import com.draga.spaceTravels3.RandomBackgroundPositionController;
import com.draga.spaceTravels3.SpaceTravels3;
import com.draga.spaceTravels3.manager.BackgroundPositionManager;
import com.draga.spaceTravels3.manager.asset.AssMan;
import com.draga.spaceTravels3.ui.Screen;

public class BackgroundScreen extends Screen
{
    private Background background;

    public BackgroundScreen()
    {
        super(false, false);
        AssMan.getMenuAssMan().load(Constants.Visual.Background.BACKGROUND_ASSET_DESCRIPTOR);
        AssMan.getMenuAssMan().update();
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
            if (AssMan.getMenuAssMan().update()
                || AssMan.getMenuAssMan()
                .isLoaded(
                    Constants.Visual.Background.BACKGROUND_ASSET_DESCRIPTOR.fileName,
                    Constants.Visual.Background.BACKGROUND_ASSET_DESCRIPTOR.type))
            {
                ;
            }
            {
                this.background =
                    AssMan.getMenuAssMan()
                        .get(Constants.Visual.Background.BACKGROUND_ASSET_DESCRIPTOR);
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

    }
}
