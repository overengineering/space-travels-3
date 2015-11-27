package com.draga.manager;

import com.draga.Constants;
import com.draga.TouchInputType;

public abstract class SettingsManager
{
    public static boolean infiniteFuel = false;
    public static boolean noGravity    = false;
    public static TouchInputType touchInputType = TouchInputType.TOUCH;

    @SuppressWarnings("PointlessBooleanExpression")
    public static boolean debugDraw = Constants.IS_DEBUGGING && false;
}
