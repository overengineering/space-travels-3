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
        AssMan.getAssMan().load(Constants.Visual.Background.BACKGROUND_ASSET_DESCRIPTOR);
        AssMan.getAssMan().update();
    }

    @Override
    public void render(float deltaTime)
    {
        if (this.background == null)
        {
            if (AssMan.getAssMan().update()
                || AssMan.getAssMan()
                .isLoaded(
                    Constants.Visual.Background.BACKGROUND_ASSET_DESCRIPTOR.fileName,
                    Constants.Visual.Background.BACKGROUND_ASSET_DESCRIPTOR.type))
            {
                this.background =
                    AssMan.getAssMan()
                        .get(Constants.Visual.Background.BACKGROUND_ASSET_DESCRIPTOR);
                BackgroundPositionManager.create(
                    this.background,
                    new RandomBackgroundPositionController());
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
}
