package com.draga.spaceTravels3.manager.asset;

import java.util.ArrayList;

public class AssList
{
    public String shipTexture;
    public String thrusterTextureAtlas;
    public String pickupTexture;
    public String pickupGreyTexture;
    public String explosionTextureAtlas;

    // Sound must be loopable.
    public String thrusterSound;
    public String explosionSound;
    public String pickupCollectSound;
    public String loseSound;
    public String winSound;
    public String buttonSound;

    public String font;
    public String debugFont;

    public ArrayList<String> musics;

    public String guideMinimapTexture;
    public String guideBelowLandingSpeedTexture;
    public String guideAboveLandingSpeedTexture;
    public String guidePickupTexture;
    
    public AssList()
    {
    }
}
