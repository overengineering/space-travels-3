package com.draga;

import java.text.DecimalFormat;

/**
 * Created by Administrator on 16/09/2015.
 */
public abstract class Constants
{
    public static final boolean IS_DEBUGGING = true;

    public static final float VIEWPORT_WIDTH = 100f;
    public static final float VIEWPORT_HEIGHT = 100f;

    public static final float EARTH_GRAVITY = 9.80665f;

    public static final DecimalFormat COMMA_SEPARATED_THOUSANDS_FORMATTER =
        new DecimalFormat("#,###");
}
