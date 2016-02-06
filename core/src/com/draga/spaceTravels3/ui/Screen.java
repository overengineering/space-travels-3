package com.draga.spaceTravels3.ui;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.draga.spaceTravels3.manager.ScreenManager;
import com.draga.spaceTravels3.manager.UIManager;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class Screen implements com.badlogic.gdx.Screen
{
    private final boolean blockable;
    private final boolean blockParents;

    private HashMap<String, Image> asyncImages;

    public Screen(boolean blockable, boolean blockParents)
    {
        this.blockable = blockable;
        this.blockParents = blockParents;

        this.asyncImages = new HashMap<>();
    }

    public boolean isBlockable()
    {
        return this.blockable;
    }

    public boolean blockParents()
    {
        return this.blockParents;
    }

    protected void loadAsyncImages(AssetManager assMan)
    {
        // Check if we need to load level icons and if they are loaded show them.
        if (!this.asyncImages.isEmpty())
        {
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

    protected Image loadTextureAsync(String filePath, AssetManager assMan)
    {
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
}
