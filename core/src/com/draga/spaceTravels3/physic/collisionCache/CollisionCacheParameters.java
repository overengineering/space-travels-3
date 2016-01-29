package com.draga.spaceTravels3.physic.collisionCache;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.utils.Array;
import com.draga.spaceTravels3.component.physicsComponent.PhysicsComponent;
import com.draga.spaceTravels3.physic.gravityCache.GravityCache;

public class CollisionCacheParameters extends AssetLoaderParameters<CollisionCache>
{
    public Array<AssetDescriptor> dependencies = new Array<>();
}
