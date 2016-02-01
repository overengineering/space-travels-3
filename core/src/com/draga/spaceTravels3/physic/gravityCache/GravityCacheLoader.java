package com.draga.spaceTravels3.physic.gravityCache;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.draga.NullFileHandleResolver;
import com.google.common.base.Stopwatch;

import java.util.concurrent.TimeUnit;

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
        Stopwatch stopwatch = Stopwatch.createStarted();

        this.gravityCache = new GravityCache();

        Gdx.app.debug(LOGGING_TAG, +stopwatch.elapsed(
            TimeUnit.NANOSECONDS) * MathUtils.nanoToSec + "s");
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
