package com.draga.spaceTravels3.physic.gravityCache;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.draga.NullFileHandleResolver;

public class GravityCacheLoader
    extends AsynchronousAssetLoader<GravityCache, GravityCacheParameters>
{
    private static final String LOGGING_TAG =
        GravityCacheLoader.class.getSimpleName();
    private GravityCache gravityCache;

    public GravityCacheLoader()
    {
        super(NullFileHandleResolver.NULL_FILE_HANDLE_RESOLVER);
    }

    @Override
    public void loadAsync(
        AssetManager manager, String fileName, FileHandle file, GravityCacheParameters parameter)
    {
        this.gravityCache = new GravityCache();
    }

    @Override
    public GravityCache loadSync(
        AssetManager manager, String fileName, FileHandle file, GravityCacheParameters parameter)
    {
        return this.gravityCache;
    }

    @Override
    public Array<AssetDescriptor> getDependencies(
        String fileName, FileHandle file, GravityCacheParameters parameter)
    {
        return parameter.dependencies;
    }
}
