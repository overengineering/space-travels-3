package com.draga;

public abstract class MaskBits
{
    public static final int SHIP       = 1 << 0;
    public static final int PLANET     = 1 << 1;
    public static final int EXPLOSION  = 1 << 2;
    public static final int BOUNDARIES = 1 << 3;
    public static final int THRUSTER   = 1 << 4;
    public static final int PICKUP     = 1 << 5;
}
