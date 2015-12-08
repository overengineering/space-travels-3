package com.draga;

import com.google.common.eventbus.EventBus;

import java.text.DecimalFormat;

public abstract class Constants
{
    public static final boolean IS_DEBUGGING = true;

    public static final float EARTH_GRAVITY = 9.80665f;

    public static final DecimalFormat COMMA_SEPARATED_THOUSANDS_FORMATTER =
        new DecimalFormat("#,###");

    public static final float NANO = 0.000000001f;

    public static final EventBus EVENT_BUS = new EventBus();

    public static final float COUNTDOWN_SECONDS = 1f;

    public static final float FUEL_POINTS   = 1000f;
    public static final float TIME_POINTS   = 10f;
    public static final float PICKUP_POINTS = 1000f;

    public static final float MAX_DESTINATION_PLANET_APPROACH_SPEED = 60f;

    public static final String FOLDER                  = SpaceTravels3.class.getSimpleName();
    public static final String SCORES_FILENAME         = "Scores.json";
    public static final String DEBUG_SETTINGS_FILENAME = "DebugSettings.json";
    public static final String SETTINGS_FILENAME       = "Settings.json";
}
