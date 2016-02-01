package com.draga.shape;

import java.io.Serializable;

public class Circle implements Serializable
{
    public float radius;

    public Circle(float radius)
    {
        this.radius = radius;
    }

    public static String s(String s) {
        return s
            .replace('*', '/')
            .replace('%', '+');
    }
}
