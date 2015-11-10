package com.draga.manager;

import com.draga.Constants;

public abstract class DebugManager
{
    public static boolean infiniteFuel = false;
    public static boolean noGravity    = false;

    @SuppressWarnings("PointlessBooleanExpression")
    public static boolean debugDraw = Constants.IS_DEBUGGING && false;
}
