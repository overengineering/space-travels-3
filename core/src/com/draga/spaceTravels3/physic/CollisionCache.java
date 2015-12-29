package com.draga.spaceTravels3.physic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.PixmapIO;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.draga.spaceTravels3.Constants;
import com.draga.spaceTravels3.component.physicsComponent.PhysicsComponent;
import com.draga.spaceTravels3.component.physicsComponent.PhysicsComponentType;
import com.draga.utils.FileUtils;
import com.google.common.base.Stopwatch;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class CollisionCache
{
    private static final String LOGGING_TAG = CollisionCache.class.getSimpleName();

    public static final float GRANULARITY = 1f;

    private final ArrayList<PhysicsComponent>[][] collisions;

    private final Vector2 offset;
    private final int     arrayHeight;
    private final int     arrayWidth;

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
        this.offset = new Vector2(x1, y1);

        float width = x2 - x1;
        float height = y2 - y1;

        this.arrayWidth = MathUtils.ceil(width / CollisionCache.GRANULARITY);
        this.arrayHeight = MathUtils.ceil(height / CollisionCache.GRANULARITY);

        collisions = new ArrayList[arrayWidth][arrayHeight];

        for (int x = 0; x < arrayWidth; x++)
        {
            for (int y = 0; y < arrayHeight; y++)
            {
                physicsComponent.getPosition()
                    .set(
                        CollisionCache.GRANULARITY * x + offset.x,
                        CollisionCache.GRANULARITY * y + offset.y);
                for (PhysicsComponent collidablePhysicsComponent : collidablePhysicsComponents)
                {
                    if (PhysicsEngine.areColliding(physicsComponent, collidablePhysicsComponent))
                    {
                        if (collisions[x][y] == null)
                        {
                            collisions[x][y] = new ArrayList<>();
                        }
                        collisions[x][y].add(collidablePhysicsComponent);
                    }
                }

            }
        }

        Gdx.app.debug(
            LOGGING_TAG,
            "Cache collision for "
                + physicsComponent.getOwnerClass()
                + " took "
                + stopwatch.elapsed(TimeUnit.NANOSECONDS) * Constants.General.NANO
                + "s");
    }

    private void saveBitMap()
    {
        Pixmap pixmap = new Pixmap(arrayWidth, arrayHeight, Pixmap.Format.RGBA8888);

        for (int x = 0; x < arrayWidth; x++)
        {
            for (int y = 0; y < arrayHeight; y++)
            {
                if (collisions[x][y] != null)
                {
                    switch (collisions[x][y].size())
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

        int floorX = MathUtils.floor(x - offset.x);
        int floorY = MathUtils.floor(y - offset.y);
        int ceilX = MathUtils.ceil(x - offset.x);
        int ceilY = MathUtils.ceil(y - offset.y);

        boolean floorXInArray = floorX >= 0 && floorX < arrayWidth;
        boolean floorYInArray = floorY >= 0 && floorY < arrayHeight;
        boolean ceilXInArray = ceilX >= 0 && ceilX < arrayWidth;
        boolean ceilYInArray = ceilY >= 0 && ceilY < arrayHeight;

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
}
