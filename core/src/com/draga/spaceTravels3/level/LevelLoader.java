package com.draga.spaceTravels3.level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.draga.NullFileHandleResolver;
import com.draga.spaceTravels3.manager.level.LevelManager;
import com.google.common.base.Stopwatch;

import java.util.concurrent.TimeUnit;

public class LevelLoader
    extends AsynchronousAssetLoader<Level, LevelParameters>
{
    private static final String LOGGING_TAG =
        LevelLoader.class.getSimpleName();

    public LevelLoader()
    {
        super(NullFileHandleResolver.NULL_FILE_HANDLE_RESOLVER);
    }

    @Override
    public void loadAsync(
        AssetManager manager, String fileName, FileHandle file, LevelParameters levelParameters)
    {
    }

    @Override
    public Level loadSync(
        AssetManager manager, String fileName, FileHandle file, LevelParameters levelParameters)
    {
        Stopwatch stopwatch = Stopwatch.createStarted();

        Level level =
            LevelManager.getLevel(levelParameters.serialisableLevel, levelParameters.difficulty);

        Gdx.app.debug(LOGGING_TAG, +stopwatch.elapsed(
            TimeUnit.NANOSECONDS) * MathUtils.nanoToSec + "s");

        return level;
    }

    @Override
    public Array<AssetDescriptor> getDependencies(
        String fileName, FileHandle file, LevelParameters parameter)
    {
        return parameter.dependencies;
    }
}
