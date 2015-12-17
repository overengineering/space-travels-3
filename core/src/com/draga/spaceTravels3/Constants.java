package com.draga.spaceTravels3;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.google.common.eventbus.EventBus;

import java.text.DecimalFormat;

public abstract class Constants
{
    public abstract static class Visual
    {
        // GameScreen
        public static final float VIEWPORT_WIDTH  = 100f;
        public static final float VIEWPORT_HEIGHT = 100f;

        // Pickup
        public static final float PICKUP_WIDTH          = 5f;
        public static final float PICKUP_HEIGHT         = 5f * 0.75f;
        public static final float PICKUP_MINIMAP_RADIUS =
            (PICKUP_WIDTH + PICKUP_HEIGHT) / 2f * 1.3f;
        public static final Color PICKUP_MINIMAP_COLOR  = Color.GOLDENROD;

        // Planet
        public static final Color PLANET_MINIMAP_DESTINATION_COLOUR = Color.BLUE;
        public static final Color PLANET_MINIMAP_COLOUR             = Color.RED;

        // Explosion
        public static final float EXPLOSION_LIFETIME = 2f;

        // HUD
        public static final float MINIMAP_SCALE                     = 0.25f;
        public static final float HUD_FORCE_INDICATOR_SCALE         = 0.25f;
        public static final Color JOYSTICK_OVERLAY_COLOR            = new Color(1f, 1f, 1f, 0.3f);
        public static final Color HUD_TRAJECTORY_LINE_COLOR_NEUTRAL =
            new Color(0.5f, 0.5f, 0.5f, 1);
        public static final Color HUD_TRAJECTORY_LINE_COLOR_PLANET  =
            new Color(0.8f, 0.2f, 0.2f, 1);
        public static final int   HUD_TRAJECTORY_LINE_STEPS         = 100;
        public static final int   HUD_TRAJECTORY_LINE_SECONDS       = 5;

        // Ship
        public static final float   SHIP_WIDTH                    = 10f;
        public static final float   SHIP_HEIGHT                   = 10f;
        public static final Color   SHIP_MINIMAP_COLOUR           = Color.WHITE;
        public static final Vector2 SHIP_MINIMAP_TRIANGLE_VERTEX1 = new Vector2(8, 0);
        public static final Vector2 SHIP_MINIMAP_TRIANGLE_VERTEX2 = new Vector2(-5, -5);
        public static final Vector2 SHIP_MINIMAP_TRIANGLE_VERTEX3 = new Vector2(-5, 5);

        // Thruster.
        public static final float   THRUSTER_MAX_WIDTH      = 5;
        public static final float   THRUSTER_MAX_HEIGHT     = 5;
        public static final float   THRUSTER_ANIMATION_TIME = 1f;
        public static final Vector2 THRUSTER_OFFSET         = new Vector2(-SHIP_WIDTH / 4f, 0);

        // Screen.
        public static final float SCREEN_FADE_DURATION = 3f;
        public static final Color SCREEN_FADE_COLOUR   = new Color(0, 0, 0, 0.7f);

        // Minimap.
        public static final Color MINIMAP_BACKGROUND_COLOR      = new Color(0, 0.17f, 0, 0.5f);
        public static final Color MINIMAP_BORDER_COLOR          = new Color(0, 0.4f, 0, 1);
        public static final float HUD_TRAJECTORY_LINE_STEP_TIME = 0.05f;


        // Background.
        public static final int   BACKGROUND_STAR_LAYER_COUNT        = 10;
        public static final int   BACKGROUND_STAR_COUNT              = 3000;
        public static final int   BACKGROUND_NEBULAE_LAYER_COUNT     = 0;
        public static final float BACKGROUND_STAR_MAX_DIAMETER_SCALE = 0.000005f;
        public abstract static class UI
        {
            public static final float SQRT_PIXELS      =
                (float) Math.sqrt(Gdx.graphics.getWidth() * Gdx.graphics.getHeight());
            public static final float FONT_SCALE       = 0.05f;
            public static final float DEBUG_FONT_SCALE = 0.02f;
            public static final float BUTTON_PADDING   = SQRT_PIXELS * 0.01f;
        }
    }


    public abstract static class Game
    {
        public static final float COUNTDOWN_SECONDS = 1f;

        public static final float FUEL_PER_SECOND = 1f;

        public static final float FUEL_POINTS   = 1000f;
        public static final float TIME_POINTS   = 10f;
        public static final float PICKUP_POINTS = 1000f;

        public static final float MAX_DESTINATION_PLANET_APPROACH_SPEED = 60f;
        public static final float SHIP_COLLISION_RADIUS                 =
            (Constants.Visual.SHIP_WIDTH + Constants.Visual.SHIP_HEIGHT) / 2f
                * 0.8f
                / 2f;
        /**
         * Change tilt range. E.g. 1.0f = 90 degree max. 0.5f = 45 degrees max.
         */
        public static final float ACCELEROMETER_RANGE                   = 0.5f;
        public static final float DEAD_ZONE                             = 0.15f;

        public static float SHIP_ACCELERATION_PER_SECOND = 60f;

        public static int PHYSICS_STEPS = 10;
    }


    public static class General
    {
        public static final boolean       IS_DEBUGGING                        = true;
        public static final float         EARTH_GRAVITY                       = 9.80665f;
        public static final DecimalFormat COMMA_SEPARATED_THOUSANDS_FORMATTER =
            new DecimalFormat("#,###");
        public static final float         NANO                                = 0.000000001f;
        public static final EventBus      EVENT_BUS                           = new EventBus();

        public static final String FOLDER                  =
            SpaceTravels3.class.getSimpleName();
        public static final String SCORES_FILENAME         = "Scores.json";
        public static final String DEBUG_SETTINGS_FILENAME =
            "DebugSettings.json";
        public static final String SETTINGS_FILENAME       = "Settings.json";
    }
}
