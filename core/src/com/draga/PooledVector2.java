package com.draga;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

import java.util.Stack;

public class PooledVector2 extends com.badlogic.gdx.math.Vector2 implements AutoCloseable
{
    private static final String LOGGING_TAG = PooledVector2.class.getSimpleName();

    private static final float GROWTH = 1.2f;

    private static final Stack<PooledVector2> freeObjects = new Stack<>();

    private PooledVector2()
    {
    }

    public static PooledVector2 newVector2(Vector2 vector2)
    {
        return newVector2(vector2.x, vector2.y);
    }

    public static PooledVector2 newVector2(float x, float y)
    {
        PooledVector2 pooledVector2;

        pooledVector2 = freeObjects.empty()
            ? new PooledVector2()
            : PooledVector2.freeObjects.pop();

        pooledVector2.set(x, y);

        return pooledVector2;
    }

    @Override
    public PooledVector2 cpy()
    {
        return newVector2(this.x, this.y);
    }

    @Override
    public void close()
    {
        // TODO: let java handle size?
        if (freeObjects.size() == freeObjects.capacity())
        {
            freeObjects.ensureCapacity((int) (freeObjects.size() * GROWTH));
            Gdx.app.debug(
                LOGGING_TAG,
                "Pool size increased to "
                    + freeObjects.capacity());
        }

        freeObjects.push(this);
    }
}
