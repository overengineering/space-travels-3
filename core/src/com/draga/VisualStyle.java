package com.draga;

import com.badlogic.gdx.graphics.Color;

public abstract class VisualStyle
{
    // GameScreen
    public static final float VIEWPORT_WIDTH  = 100f;
    public static final float VIEWPORT_HEIGHT = 100f;

    // Pickup
    public static final float PICKUP_WIDTH          = 5f;
    public static final float PICKUP_HEIGHT         = 5f * 0.75f;
    public static final float PICKUP_MINIMAP_RADIUS = (PICKUP_WIDTH + PICKUP_HEIGHT) / 2f * 1.3f;
    public static final Color PICKUP_MINIMAP_COLOR  = Color.GOLDENROD;

    // Planet
    public static final Color PLANET_MINIMAP_DESTINATION_COLOUR = Color.BLUE;
    public static final Color PLANET_MINIMAP_COLOUR             = Color.RED;

    // Explosion
    public static final float EXPLOSION_LIFETIME = 2f;

    // HUD
    public static final float MINIMAP_SCALE             = 0.25f;
    public static final float HUD_FORCE_INDICATOR_SCALE = 0.25f;
}
