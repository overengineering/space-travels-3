package com.draga.spaceTravels3.physic.collisionCache;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.draga.NullFileHandleResolver;
import com.draga.spaceTravels3.component.physicsComponent.PhysicsComponent;
import com.draga.spaceTravels3.level.Level;
import com.draga.spaceTravels3.manager.asset.AssMan;
import com.draga.spaceTravels3.physic.PhysicsEngine;
import com.draga.spaceTravels3.physic.gravityCache.GravityCache;
import com.draga.spaceTravels3.physic.gravityCache.GravityCacheParameters;

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
        Level level = manager.get("level", Level.class);
        PhysicsComponent physicsComponent = level.getShip().physicsComponent;
        this.collisionCache = new CollisionCache(physicsComponent);
        PhysicsEngine.addPhysicsComponentCollisions(physicsComponent, this.collisionCache);
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
