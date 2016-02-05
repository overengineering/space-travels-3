package com.draga.spaceTravels3.physic.collisionCache;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.draga.NullFileHandleResolver;
import com.draga.spaceTravels3.Constants;
import com.draga.spaceTravels3.component.physicsComponent.PhysicsComponent;
import com.draga.spaceTravels3.level.Level;
import com.google.common.base.Stopwatch;

import java.util.concurrent.TimeUnit;

public class CollisionCacheLoader
    extends AsynchronousAssetLoader<CollisionCache, CollisionCacheParameters>
{
    private static final String LOGGING_TAG =
        CollisionCacheLoader.class.getSimpleName();

    private CollisionCache collisionCache;

    public CollisionCacheLoader()
    {
        super(NullFileHandleResolver.NULL_FILE_HANDLE_RESOLVER);
    }

    @Override
    public void loadAsync(
        AssetManager manager, String fileName, FileHandle file, CollisionCacheParameters parameter)
    {
        Stopwatch stopwatch = Stopwatch.createStarted();

        Level level = manager.get(Constants.Game.LEVEL_ASSET_FILENAME, Level.class);
        PhysicsComponent physicsComponent = level.getShip().physicsComponent;
        this.collisionCache = new CollisionCache(physicsComponent);

        Gdx.app.debug(LOGGING_TAG, +stopwatch.elapsed(
            TimeUnit.NANOSECONDS) * MathUtils.nanoToSec + "s");
    }

    @Override
    public CollisionCache loadSync(
        AssetManager manager, String fileName, FileHandle file, CollisionCacheParameters parameter)
    {
        return this.collisionCache;
    }

    @Override
    public Array<AssetDescriptor> getDependencies(
        String fileName, FileHandle file, CollisionCacheParameters parameter)
    {
        return null;
    }
}
