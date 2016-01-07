package com.draga.spaceTravels3.physic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.PixmapIO;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Pool;
import com.draga.PooledVector2;
import com.draga.spaceTravels3.component.physicsComponent.PhysicsComponent;
import com.draga.spaceTravels3.component.physicsComponent.PhysicsComponentType;
import com.draga.utils.FileUtils;
import com.google.common.base.Stopwatch;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class CollisionCache implements Pool.Poolable
{
    public static final  float  GRANULARITY = 1f;
    private static final String LOGGING_TAG = CollisionCache.class.getSimpleName();
    private final int                             arrayHeight;
    private final int                             arrayWidth;
    private       ArrayList<PhysicsComponent>[][] collisions;
    private       PooledVector2                   offset;

    public CollisionCache(PhysicsComponent originalPhysicsComponent)
    {
        Stopwatch stopwatch = Stopwatch.createStarted();

        PhysicsComponent physicsComponent = new PhysicsComponent(originalPhysicsComponent);

        ArrayList<PhysicsComponent> collidablePhysicsComponents = new ArrayList<>();
        for (PhysicsComponent otherPhysicsComponent : PhysicsEngine.getAllPhysicsComponentsExcept(
            originalPhysicsComponent))
        {
            if (otherPhysicsComponent.getPhysicsComponentType() == PhysicsComponentType.STATIC
                && otherPhysicsComponent.getCollidesWith()
                .contains(physicsComponent.getOwnerClass())
                && physicsComponent.getCollidesWith()
                .contains(otherPhysicsComponent.getOwnerClass()))
            {
                collidablePhysicsComponents.add(otherPhysicsComponent);
            }
        }



        // Calculate the size and position of the grid of points to be checked
        float x1 = Float.MAX_VALUE;
        float x2 = Float.MIN_VALUE;
        float y1 = Float.MAX_VALUE;
        float y2 = Float.MIN_VALUE;
        for (PhysicsComponent otherPhysicsComponent : collidablePhysicsComponents)
        {
            if (x1
                > otherPhysicsComponent.getPosition().x
                - otherPhysicsComponent.getBoundsCircle().radius)
            {
                x1 = otherPhysicsComponent.getPosition().x
                    - otherPhysicsComponent.getBoundsCircle().radius;
            }
            if (x2
                < otherPhysicsComponent.getPosition().x
                + otherPhysicsComponent.getBoundsCircle().radius)
            {
                x2 = otherPhysicsComponent.getPosition().x
                    + otherPhysicsComponent.getBoundsCircle().radius;
            }
            if (y1
                > otherPhysicsComponent.getPosition().y
                - otherPhysicsComponent.getBoundsCircle().radius)
            {
                y1 = otherPhysicsComponent.getPosition().y
                    - otherPhysicsComponent.getBoundsCircle().radius;
            }
            if (y2
                < otherPhysicsComponent.getPosition().y
                + otherPhysicsComponent.getBoundsCircle().radius)
            {
                y2 = otherPhysicsComponent.getPosition().y
                    + otherPhysicsComponent.getBoundsCircle().radius;
            }
        }

        // Includes the size of the physicsComponent itself
        x1 -= physicsComponent.getBoundsCircle().radius;
        y1 -= physicsComponent.getBoundsCircle().radius;
        x2 += physicsComponent.getBoundsCircle().radius;
        y2 += physicsComponent.getBoundsCircle().radius;

        // The offset are the coordinates of the bottom left corner of the grid.
        this.offset = PooledVector2.newVector2(x1, y1);

        float width = x2 - x1;
        float height = y2 - y1;

        this.arrayWidth = MathUtils.ceil(width / CollisionCache.GRANULARITY);
        this.arrayHeight = MathUtils.ceil(height / CollisionCache.GRANULARITY);

        //noinspection unchecked
        this.collisions = new ArrayList[this.arrayWidth][this.arrayHeight];

        for (int x = 0; x < this.arrayWidth; x++)
        {
            for (int y = 0; y < this.arrayHeight; y++)
            {
                physicsComponent.getPosition()
                    .set(
                        CollisionCache.GRANULARITY * x + this.offset.x,
                        CollisionCache.GRANULARITY * y + this.offset.y);
                for (PhysicsComponent collidablePhysicsComponent : collidablePhysicsComponents)
                {
                    if (PhysicsEngine.areColliding(physicsComponent, collidablePhysicsComponent))
                    {
                        if (this.collisions[x][y] == null)
                        {
                            this.collisions[x][y] = new ArrayList<>();
                        }
                        this.collisions[x][y].add(collidablePhysicsComponent);
                    }
                }

            }
        }

        Gdx.app.debug(
            LOGGING_TAG,
            "Cache collision for "
                + physicsComponent.getOwnerClass()
                + " took "
                + stopwatch.elapsed(TimeUnit.NANOSECONDS) * MathUtils.nanoToSec
                + "s");
    }

    private void saveBitMap()
    {
        Pixmap pixmap = new Pixmap(this.arrayWidth, this.arrayHeight, Pixmap.Format.RGBA8888);

        for (int x = 0; x < this.arrayWidth; x++)
        {
            for (int y = 0; y < this.arrayHeight; y++)
            {
                if (this.collisions[x][y] != null)
                {
                    switch (this.collisions[x][y].size())
                    {
                        case 0:
                            pixmap.setColor(Color.CLEAR);
                            break;
                        case 1:
                            pixmap.setColor(Color.RED);
                            break;
                        case 2:
                            pixmap.setColor(Color.GREEN);
                            break;
                        case 3:
                            pixmap.setColor(Color.BLUE);
                            break;
                        default:
                            pixmap.setColor(Color.WHITE);
                            break;
                    }
                    pixmap.drawPixel(x, y);
                }
            }
        }

        PixmapIO.writePNG(FileUtils.getFileHandle("collisionBitmap.png"), pixmap);
    }

    public ArrayList<PhysicsComponent> getPossibleCollidingPhysicsComponents(float x, float y)
    {
        ArrayList<PhysicsComponent> pointCollisions = new ArrayList<>();

        int floorX = MathUtils.floor(x - this.offset.x);
        int floorY = MathUtils.floor(y - this.offset.y);
        int ceilX = MathUtils.ceil(x - this.offset.x);
        int ceilY = MathUtils.ceil(y - this.offset.y);

        boolean floorXInArray = floorX >= 0 && floorX < this.arrayWidth;
        boolean floorYInArray = floorY >= 0 && floorY < this.arrayHeight;
        boolean ceilXInArray = ceilX >= 0 && ceilX < this.arrayWidth;
        boolean ceilYInArray = ceilY >= 0 && ceilY < this.arrayHeight;

        if (floorXInArray)
        {
            if (floorYInArray)
            {
                addCollisions(pointCollisions, this.collisions[floorX][floorY]);
            }
            if (ceilYInArray)
            {
                addCollisions(pointCollisions, this.collisions[floorX][ceilY]);
            }
        }
        if (ceilXInArray)
        {
            if (floorYInArray)
            {
                addCollisions(pointCollisions, this.collisions[ceilX][floorY]);
            }
            if (ceilYInArray)
            {
                addCollisions(pointCollisions, this.collisions[ceilX][ceilY]);
            }
        }

        return pointCollisions;
    }

    private void addCollisions(ArrayList<PhysicsComponent> to, ArrayList<PhysicsComponent> from)
    {
        if (from != null)
        {
            for (PhysicsComponent physicsComponent : from)
            {
                if (!to.contains(physicsComponent))
                {
                    to.add(physicsComponent);
                }
            }
        }
    }

    @Override
    public void reset()
    {
        this.offset.close();
        this.offset= null;
        this.collisions = null;
    }
}
