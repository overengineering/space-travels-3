package com.draga.background;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.draga.ErrorHandlerProvider;
import com.draga.NullFileHandleResolver;

public class BackgroundLoader
    extends AsynchronousAssetLoader<Background, BackgroundParameters>
{
    private static final String LOGGING_TAG =
        BackgroundLoader.class.getSimpleName();

    private Background background;

    public BackgroundLoader()
    {
        super(NullFileHandleResolver.NULL_FILE_HANDLE_RESOLVER);
    }

    @Override
    public void loadAsync(
        AssetManager assetManager,
        String fileName,
        FileHandle fileHandle,
        BackgroundParameters backgroundParameters)
    {
        if (backgroundParameters == null)
        {
            ErrorHandlerProvider.handle(LOGGING_TAG, "BackgroundParameters can't be null");
        }

        this.background = new Background();
        this.background.generateStarLayersPixmap(
            backgroundParameters.layerCount,
            backgroundParameters.starsCount,
            backgroundParameters.minParallax,
            backgroundParameters.maxParallax,
            backgroundParameters.starMaxDiameterScale);
    }

    @Override
    public Background loadSync(
        AssetManager assetManager,
        String fileName,
        FileHandle fileHandle,
        BackgroundParameters backgroundParameters)
    {
        this.background.loadLayersFromPixmaps();

        return this.background;
    }

    @Override
    public Array<AssetDescriptor> getDependencies(
        String fileName, FileHandle fileHandle, BackgroundParameters backgroundParameters)
    {
        return null;
    }
}
