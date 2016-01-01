/*
package com.draga;


import com.badlogic.gdx.Gdx;

import java.lang.reflect.Constructor;
import java.util.Stack;

public class Pool<T>
{
    private static final String LOGGING_TAG = Pool.class.getSimpleName();

    private static final int INITIAL_CAPACITY = 16;
    private static final float GROWTH = 1.2f;

    private final Stack<T>       freeObjects;
    private       Constructor<T> constructor;

    public Pool(Class<T> klass)
    {
        this.freeObjects = new Stack<>();
        this.freeObjects.ensureCapacity(INITIAL_CAPACITY);
        try
        {
            this.constructor = klass.getDeclaredConstructor();
        } catch (NoSuchMethodException e)
        {
            Gdx.app.error(LOGGING_TAG, "Could not find an empty constructor", e);
        }
        this.constructor.setAccessible(true);
    }

    public T obtain()
    {
        if (this.freeObjects.empty())
        {
            try
            {
                return constructor.newInstance();
            } catch (Exception e)
            {
                Gdx.app.error(LOGGING_TAG, "Can't instantiate", e);
            }
        }
        return this.freeObjects.pop();
    }

    public void free(T object)
    {
        if (this.freeObjects.size() == this.freeObjects.capacity())
        {
            this.freeObjects.ensureCapacity((int) (this.freeObjects.size() * GROWTH));
            Gdx.app.debug(LOGGING_TAG, constructor.getClass().getName() + " pool size increased to " + this.freeObjects.capacity());
        }
        this.freeObjects.add(object);
    }
}
*/
