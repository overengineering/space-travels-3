package com.draga.spaceTravels3.physic.collisionCache;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.draga.NullFileHandleResolver;
import com.draga.spaceTravels3.Constants;
import com.draga.spaceTravels3.component.physicsComponent.PhysicsComponent;
import com.draga.spaceTravels3.level.Level;

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
        Level level = manager.get(Constants.Game.LEVEL_ASSET_FILENAME, Level.class);
        PhysicsComponent physicsComponent = level.getShip().physicsComponent;
        this.collisionCache = new CollisionCache(physicsComponent);
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
