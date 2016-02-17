package com.draga.spaceTravels3.manager.asset;

import java.util.ArrayList;
import java.util.LinkedHashMap;

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

    public String iconSettings;
    public String iconLeaderboard;
    public String iconAchievement;
    public String iconCredits;
    public String iconRate;
    public String iconGuide;
    public String iconTutorial;
    public String iconLeft;
    public String iconRight;
    public String iconShare;
    public String iconRetry;
    public String iconTouch;
    public String iconPlay;
    public String iconAccelerometer;
    public String iconAccelerometerChecked;
    public String iconTouchChecked;
    public String iconUnlock;
    public String iconUnlockOverlay;
    public String iconFaceUp;

    public LinkedHashMap<String, String> iconDifficulties;

    public AssList()
    {
    }
}
