package com.draga;

public class Vector2 extends com.badlogic.gdx.math.Vector2 implements AutoCloseable
{
    private static final com.draga.Pool<Vector2> POOL = new com.draga.Pool<>(Vector2.class);

    private Vector2()
    {
    }

    @Override
    public Vector2 cpy()
    {
        return newVector2(this.x, this.y);
    }

    public static Vector2 newVector2(float x, float y)
    {
        Vector2 vector2 = POOL.obtain();
        vector2.set(x, y);

        return vector2;
    }

    @Override
    public void close()
    {
        POOL.free(this);
    }
}
