package com.draga.entity.component;

import com.badlogic.gdx.utils.Pool;

public abstract class Component implements Pool.Poolable
{
    public abstract void dispose();
}
