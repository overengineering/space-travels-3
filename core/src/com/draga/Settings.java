package com.draga;

public class Settings
{
    public boolean infiniteFuel = false;
    public boolean noGravity    = false;
    @SuppressWarnings("PointlessBooleanExpression")
    public boolean debugDraw    = Constants.IS_DEBUGGING && false;
    public static TouchInputType touchInputType = TouchInputType.ACCELEROMETER;
}
