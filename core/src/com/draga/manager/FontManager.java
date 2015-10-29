package com.draga.manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;

public class FontManager
{
    private static final String LOGGING_TAG = FontManager.class.getSimpleName();
    public static final float FONT_SCALE = 0.00005f;
    public static AssetManager assetManager = new AssetManager();

    public static void create()
    {
        Gdx.app.debug(LOGGING_TAG, "Create");

        FileHandleResolver resolver = new InternalFileHandleResolver();
        assetManager.setLoader(
            FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader(resolver));
        assetManager.setLoader(BitmapFont.class, ".ttf", new FreetypeFontLoader(resolver));

        // Main label font
        FreetypeFontLoader.FreeTypeFontLoaderParameter size1Params =
            new FreetypeFontLoader.FreeTypeFontLoaderParameter();
        size1Params.fontFileName = "font/pdark.ttf";
        size1Params.fontParameters.size =
            (int) (Gdx.graphics.getWidth() * Gdx.graphics.getHeight() * FONT_SCALE);
        assetManager.load("bigFont.ttf", BitmapFont.class, size1Params);

        FontManager.assetManager.finishLoading();
    }

    public static void destroy()
    {
        Gdx.app.debug(LOGGING_TAG, "Destroy");

        Array<Disposable> allAssets = new Array<>();
        assetManager.getAll(Disposable.class, allAssets);

        for (Disposable asset : allAssets)
        {
            asset.dispose();
        }

        assetManager.clear();
        assetManager.dispose();
        assetManager = new AssetManager();
    }

    /**
     * Main big label font.
     */
    public static BitmapFont getBigFont()
    {
        return assetManager.get("bigFont.ttf", BitmapFont.class);
    }
}
