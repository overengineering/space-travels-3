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
        public static final float PICKUP_WIDTH  = 5f;
        public static final float PICKUP_HEIGHT = 5f * 0.75f;

        // Explosion
        public static final float EXPLOSION_LIFETIME = 2f;

        // Ship
        public static final float SHIP_WIDTH  = 10f;
        public static final float SHIP_HEIGHT = 10f;

        // Thruster.
        public static final float   THRUSTER_MAX_WIDTH      = 5;
        public static final float   THRUSTER_MAX_HEIGHT     = 5;
        public static final float   THRUSTER_ANIMATION_TIME = 1f;
        public static final Vector2 THRUSTER_OFFSET         = new Vector2(-SHIP_WIDTH / 4f, 0);

        // Screen.
        public static final float SCREEN_FADE_DURATION = 3f;
        public static final Color SCREEN_FADE_COLOUR   = new Color(0, 0, 0, 0.7f);


        public static abstract class Background
        {
            public static final int   STAR_LAYER_COUNT        = 10;
            public static final int   STAR_COUNT              = 3000;
            public static final int   NEBULAE_LAYER_COUNT     = 0;
            public static final float STAR_MAX_DIAMETER_SCALE = 0.000005f;
        }


        public abstract static class UI
        {
            public static final float SQRT_PIXELS      =
                (float) Math.sqrt(Gdx.graphics.getWidth() * Gdx.graphics.getHeight());
            public static final float FONT_SCALE       = 0.05f;
            public static final float DEBUG_FONT_SCALE = 0.02f;
            public static final float BUTTON_PADDING   = SQRT_PIXELS * 0.01f;
        }


        public static class HUD
        {

            // HUD
            public static final float FORCE_INDICATOR_SCALE                  = 0.25f;
            public static final Color JOYSTICK_OVERLAY_COLOR                 =
                new Color(1f, 1f, 1f, 0.3f);
            public static final Color DESTINATION_PLANET_OVERLAY_LOSE_BORDER = Color.RED;
            public static final Color DESTINATION_PLANET_OVERLAY_LOSE_FILL   =
                new Color(1f, 0f, 0f, 0.2f);
            public static final Color DESTINATION_PLANET_OVERLAY_WIN_BORDER  = Color.GREEN;
            public static final Color DESTINATION_PLANET_OVERLAY_WIN_FILL    =
                new Color(0f, 1f, 0f, 0.2f);


            public abstract static class Minimap
            {
                public static final float PICKUP_RADIUS =
                    (PICKUP_WIDTH + PICKUP_HEIGHT) / 2f * 1.3f;
                public static final Color PICKUP_COLOR  = Color.GOLDENROD;
                
                public static final Color   BACKGROUND_COLOR      = new Color(0, 0.17f, 0, 0.5f);
                public static final Color   BORDER_COLOR          = new Color(0, 0.4f, 0, 1);
                public static final Color   SHIP_COLOUR           = Color.WHITE;
                public static final Vector2 SHIP_TRIANGLE_VERTEX1 = new Vector2(8, 0);
                public static final Vector2 SHIP_TRIANGLE_VERTEX2 = new Vector2(-5, -5);
                public static final Vector2 SHIP_TRIANGLE_VERTEX3 = new Vector2(-5, 5);
                public static final float   SCALE                 = 0.25f;

                // Planet
                public static final Color PLANET_DESTINATION_COLOUR = Color.BLUE;
                public static final Color PLANET_COLOUR             = Color.RED;
            }


            public abstract static class TrajectoryLine
            {
                public static final int   LINE_POINTS                   = 100;
                public static final float LINE_POINTS_TIME              = 0.05f;
                public static final float LINE_STEP_TIME                = 1f / 600f;
                public static final Color LINE_COLOR_NEUTRAL            =
                    new Color(0.5f, 0.5f, 0.5f, 1);
                public static final Color LINE_COLOR_PLANET_LOSE        =
                    new Color(0.8f, 0.2f, 0.2f, 1);
                public static final Color LINE_COLOR_PICKUP             = Color.GREEN;
                public static final Color LINE_COLOR_PLANET_DESTINATION =
                    new Color(0, 0.7f, 1, 1);
                public static final int   LINE_SECONDS                  = 5;
            }

        }
    }


    public abstract static class Game
    {
        public static final float COUNTDOWN_SECONDS = 1f;

        public static final float FUEL_PER_SECOND = 1f;

        public static final float FUEL_POINTS   = 1000f;
        public static final float TIME_POINTS   = 10f;
        public static final float PICKUP_POINTS = 1000f;

        public static final float MAX_DESTINATION_PLANET_APPROACH_SPEED = 30f;
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
