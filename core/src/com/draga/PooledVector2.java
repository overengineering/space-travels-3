package com.draga;

import com.badlogic.gdx.Gdx;

import java.util.Stack;

public class PooledVector2 extends com.badlogic.gdx.math.Vector2 implements AutoCloseable
{
    private static final String LOGGING_TAG = PooledVector2.class.getSimpleName();

    private static final float GROWTH = 1.2f;

    private final static Stack<PooledVector2> freeObjects = new Stack<>();

    private PooledVector2()
    {
    }

    @Override
    public PooledVector2 cpy()
    {
        return newVector2(this.x, this.y);
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
    public void close()
    {
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
