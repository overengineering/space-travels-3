package com.draga;

import com.google.common.eventbus.EventBus;

import java.text.DecimalFormat;

public abstract class Constants
{
    public static final boolean IS_DEBUGGING = true;

    public static final float VIEWPORT_WIDTH  = 100f;
    public static final float VIEWPORT_HEIGHT = 100f;

    public static final float EARTH_GRAVITY = 9.80665f;

    public static final DecimalFormat COMMA_SEPARATED_THOUSANDS_FORMATTER =
        new DecimalFormat("#,###");

    public static final float NANO = 0.000000001f;

    public static final float MAX_FUEL        = 1f;
    public static final float FUEL_PER_SECOND = 0.3f;

    public static final EventBus EVENT_BUS = new EventBus();
}
