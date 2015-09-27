package com.draga.entity.component;

import com.badlogic.gdx.utils.Pool;

/**
 * Created by Administrator on 22/09/2015.
 */
public abstract class Component implements Pool.Poolable{
    public abstract void dispose();
}
