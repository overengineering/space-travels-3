package com.draga.shape;

import java.io.Serializable;

public class Circle implements Serializable
{
    public static String s =
        "zvvovWnaoTXDUXVt9J0ondrsnnbpnd8nzvvopTxpndrnHKuPoxwj8eelEzSB68Obzz1TkjDZtqR1L";
    public float radius;


    public Circle(float radius)
    {
        this.radius = radius;
    }

    public static String s(String s)
    {
        return s
            .replace('*', '/')
            .replace('%', '+');
    }
}
