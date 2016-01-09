package com.draga.spaceTravels3.screen;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.draga.background.Background;
import com.draga.background.BackgroundLoader;
import com.draga.spaceTravels3.Constants;
import com.draga.spaceTravels3.SpaceTravels3;
import com.draga.spaceTravels3.ui.Screen;

public class BackgroundScreen extends Screen
{
    private final AssetManager assetManager;
    private final Stage        stage;
    private       Background   background;

    public BackgroundScreen()
    {
        super(false, false);
        this.assetManager = new AssetManager();
        this.assetManager.setLoader(Background.class, new BackgroundLoader());
        this.assetManager.load(Constants.Visual.Background.BACKGROUND_ASSET_DESCRIPTOR);
        this.assetManager.update();

        this.stage = new Stage(SpaceTravels3.menuViewport, SpaceTravels3.spriteBatch);
    }

    @Override
    public void show()
    {

    }

    @Override
    public void render(float delta)
    {
        if (this.background == null)
        {
            if (this.assetManager.update())
            {
                this.background =
                    this.assetManager.get(Constants.Visual.Background.BACKGROUND_ASSET_DESCRIPTOR);
            }
        }
        else
        {
            this.stage.getViewport().apply();
            this.stage.getBatch()
                .setProjectionMatrix(this.stage.getViewport().getCamera().combined);
            this.stage.getBatch().begin();
            this.background.draw(this.stage.getCamera(), this.stage.getBatch());
            this.stage.getBatch().end();
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
