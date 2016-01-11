package com.draga.spaceTravels3.physic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.PixmapIO;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.draga.PooledVector2;
import com.draga.spaceTravels3.component.physicsComponent.PhysicsComponent;
import com.draga.spaceTravels3.component.physicsComponent.PhysicsComponentType;
import com.draga.spaceTravels3.gameEntity.GameEntity;
import com.draga.spaceTravels3.manager.GameEntityManager;
import com.draga.utils.FileUtils;
import com.google.common.base.Stopwatch;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class GravityCache
{
    private static final String LOGGING_TAG = GravityCache.class.getSimpleName();
    
    private static final float MIN_GRAVITY    = 1f;
    private static final int   MAX_CACHE_SIZE = 2;

    private final GravityCacheNode rootNode;
    
    private Rectangle bounds;

    private PhysicsComponent lastUsedPhysicsComponent;
    private GravityCacheNode lastUsedCacheNode;
    
    public GravityCache()
    {
        Stopwatch stopwatch = Stopwatch.createStarted();
        
        ArrayList<PhysicsComponent> staticPhysicsComponentsWithMass = new ArrayList<>();
        
        for (GameEntity gameEntity : GameEntityManager.getGameEntities())
        {
            if (gameEntity.physicsComponent.getPhysicsComponentType() == PhysicsComponentType.STATIC
                && gameEntity.physicsComponent.getMass() > 0)
            {
                staticPhysicsComponentsWithMass.add(gameEntity.physicsComponent);
            }
        }
        
        calculateBounds(staticPhysicsComponentsWithMass);
        this.rootNode = new GravityCacheNode(this.bounds, staticPhysicsComponentsWithMass);

        float elapsed = stopwatch.elapsed(TimeUnit.NANOSECONDS) * MathUtils.nanoToSec;
        Gdx.app.debug(LOGGING_TAG, "Caching gravity took " + elapsed + "s");
    }
    
    private void calculateBounds(ArrayList<PhysicsComponent> staticPhysicsComponentsWithMass)
    {
        try (PooledVector2 barycentre = PooledVector2.newVector2(0f, 0f))
        {
            float totalMass = 0f;
            for (PhysicsComponent otherPhysicsComponent : staticPhysicsComponentsWithMass)
            {
                totalMass += otherPhysicsComponent.getMass();
                try (PooledVector2 weightedPosition = otherPhysicsComponent.getPosition().cpy())
                {
                    weightedPosition.scl(otherPhysicsComponent.getMass());
                    barycentre.add(weightedPosition);
                }
            }
            barycentre.scl(1 / totalMass);
            Gdx.app.debug(
                LOGGING_TAG,
                "Barycentre calculated at x: " + barycentre.x + " y: " + barycentre.y);

            float radius = (float) Math.sqrt(totalMass / MIN_GRAVITY);

            this.bounds = new Rectangle(
                barycentre.x - radius,
                barycentre.y - radius,
                radius * 2,
                radius * 2);
        }
    }
    
    public PooledVector2 getCachedGravity(PhysicsComponent physicsComponent)
    {
        GravityCacheNode gravityCacheNode = getStartingNode(physicsComponent);

        // Move to the leaf node containing the physicsComponent
        gravityCacheNode = getLeafContaining(physicsComponent, gravityCacheNode);

        this.lastUsedCacheNode = gravityCacheNode;
        this.lastUsedPhysicsComponent = physicsComponent;

        PooledVector2 gravity = gravityCacheNode.getGravity(physicsComponent.getPosition());
        gravity.scl(physicsComponent.getMass());

        return gravity;
    }
    
    private GravityCacheNode getStartingNode(PhysicsComponent physicsComponent)
    {
        GravityCacheNode gravityCacheNode;

        if (this.lastUsedPhysicsComponent != null
            && this.lastUsedPhysicsComponent.equals(physicsComponent))
        {
            gravityCacheNode = this.lastUsedCacheNode;
        }
        else
        {
            if (!GravityCache.this.rootNode.contains(physicsComponent.getPosition()))
            {
                gravityCacheNode = GravityCacheNode.NULL_GRAVITY_CACHE_NODE;
            }
            else
            {
                gravityCacheNode = GravityCache.this.rootNode;
            }
        }

        // Go up until the physicsComponent is in the boundaries
        while (!gravityCacheNode.contains(physicsComponent.getPosition()))
        {
            gravityCacheNode = gravityCacheNode.getParentNode();
        }
        return gravityCacheNode;
    }
    
    private GravityCacheNode getLeafContaining(
        PhysicsComponent physicsComponent,
        GravityCacheNode gravityCacheNode)
    {
        while (gravityCacheNode.hasChildren())
        {
            if (physicsComponent.getPosition().x < gravityCacheNode.getCentre().x)
            {
                if (physicsComponent.getPosition().y < gravityCacheNode.getCentre().y)
                {
                    gravityCacheNode = gravityCacheNode.getBottomLeftNode();
                }
                else
                {
                    gravityCacheNode = gravityCacheNode.getTopLeftNode();
                }
            }
            else
            {
                if (physicsComponent.getPosition().y < gravityCacheNode.getCentre().y)
                {
                    gravityCacheNode = gravityCacheNode.getBottomRightNode();
                }
                else
                {
                    gravityCacheNode = gravityCacheNode.getTopRightNode();
                }
            }
        }
        return gravityCacheNode;
    }
    
    private void saveBitMap()
    {
        float scale = 10000f / this.bounds.width;
        int pixmapWidth = MathUtils.ceil(this.bounds.width * scale);
        int pixmapHeight = MathUtils.ceil(this.bounds.height * scale);
        
        Pixmap pixmap = new Pixmap(
            pixmapWidth,
            pixmapHeight,
            Pixmap.Format.RGBA8888);
        
        pixmap.setColor(Color.WHITE);
        
        drawGravityCacheNodeRecursive(pixmap, this.rootNode, -this.bounds.x, -this.bounds.y, scale);
        
        PixmapIO.writePNG(FileUtils.getFileHandle("gravityBitmap.png"), pixmap);
    }
    
    private void drawGravityCacheNodeRecursive(
        Pixmap pixmap,
        GravityCacheNode gravityCacheNode,
        float offsetX,
        float offsetY,
        float scale)
    {
        float scaledWidth = gravityCacheNode.getWidth() * scale;
        float scaledHeight = gravityCacheNode.getHeight() * scale;
        pixmap.drawRectangle(
            (int) ((gravityCacheNode.getX() + offsetX) * scale),
            (int) (((gravityCacheNode.getY() + offsetY) * scale) + scaledHeight),
            (int) scaledWidth,
            (int) scaledHeight);
        
        if (gravityCacheNode.getTopLeftNode() != null)
        {
            drawGravityCacheNodeRecursive(
                pixmap,
                gravityCacheNode.getTopLeftNode(),
                offsetX,
                offsetY,
                scale);
        }
        if (gravityCacheNode.getTopRightNode() != null)
        {
            drawGravityCacheNodeRecursive(
                pixmap,
                gravityCacheNode.getTopRightNode(),
                offsetX,
                offsetY,
                scale);
        }
        if (gravityCacheNode.getBottomLeftNode() != null)
        {
            drawGravityCacheNodeRecursive(
                pixmap,
                gravityCacheNode.getBottomLeftNode(),
                offsetX,
                offsetY,
                scale);
        }
        if (gravityCacheNode.getBottomRightNode() != null)
        {
            drawGravityCacheNodeRecursive(
                pixmap,
                gravityCacheNode.getBottomRightNode(),
                offsetX,
                offsetY,
                scale);
        }
    }
}
