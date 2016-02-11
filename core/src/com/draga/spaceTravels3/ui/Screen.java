package com.draga.spaceTravels3.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.draga.spaceTravels3.SpaceTravels3;
import com.draga.spaceTravels3.manager.ScreenManager;
import com.draga.spaceTravels3.manager.UIManager;
import com.draga.spaceTravels3.manager.asset.AssMan;
import com.draga.spaceTravels3.screen.SettingsScreen;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class Screen implements com.badlogic.gdx.Screen
{
    private final boolean blockable;
    private final boolean blockParents;
    protected     Stage   stage;
    private HashMap<String, Image> asyncImages;

    public Screen(boolean blockable, boolean blockParents)
    {
        this.blockable = blockable;
        this.blockParents = blockParents;

        this.asyncImages = new HashMap<>();

        this.stage = new Stage(SpaceTravels3.menuViewport, SpaceTravels3.spriteBatch);
        this.stage.setDebugAll(true);
        //        this.stage.setDebugAll(SettingsManager.getDebugSettings().debugDraw);
    }

    public boolean isBlockable()
    {
        return this.blockable;
    }

    public boolean blockParents()
    {
        return this.blockParents;
    }

    protected Image loadTextureAsync(String filePath)
    {
        AssetManager assMan = AssMan.getAssMan();

        Image image = new Image();
        assMan.load(filePath, Texture.class);
        this.asyncImages.put(filePath, image);
        assMan.update();

        return image;
    }

    protected Actor getBackButton()
    {
        BeepingImageTextButton button = new BeepingImageTextButton("Back", UIManager.skin, "exit");
        button.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                ScreenManager.removeScreen(Screen.this);
            }
        });

        return button;
    }

    protected Button getSettingsButton(boolean useText)
    {
        Button button = useText
            ? new BeepingImageTextButton("Settings", UIManager.skin, "settings")
            : new BeepingImageButton(UIManager.skin, "settings");

        button.addListener(
            new ClickListener()
            {
                @Override
                public void clicked(InputEvent event, float x, float y)
                {
                    ScreenManager.addScreen(new SettingsScreen());
                }
            });

        return button;
    }

    public void onAdded()
    {

    }

    protected InputAdapter getBackInputAdapter()
    {
        return new InputAdapter()
        {
            @Override
            public boolean keyUp(int keycode)
            {
                switch (keycode)
                {
                    case Input.Keys.ESCAPE:
                    case Input.Keys.BACK:
                    {
                        ScreenManager.removeScreen(Screen.this);
                        return true;
                    }
                }
                return false;
            }
        };
    }

    @Override
    public void show()
    {

    }

    @Override
    public void render(float delta)
    {
        loadAsyncImages();

        this.stage.getViewport().apply();
        this.stage.act(delta);
        this.stage.draw();
    }

    protected void loadAsyncImages()
    {
        // Check if we need to load level icons and if they are loaded show them.
        if (!this.asyncImages.isEmpty())
        {
            AssetManager assMan = AssMan.getAssMan();

            ArrayList<String> texturePaths =
                new ArrayList<>(this.asyncImages.keySet());
            for (String texturePath : texturePaths)
            {
                if (assMan.update()
                    || assMan.isLoaded(texturePath))
                {
                    Texture texture = assMan.get(texturePath);
                    texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
                    this.asyncImages.get(texturePath)
                        .setDrawable(new TextureRegionDrawable(new TextureRegion(texture)));

                    this.asyncImages.remove(texturePath);
                }
            }
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
        this.stage.dispose();
    }
}
