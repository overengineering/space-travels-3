package com.draga.background;

import com.badlogic.gdx.assets.AssetLoaderParameters;

public class BackgroundParameters extends AssetLoaderParameters<Background>
{
    public int   layerCount;
    public int   starsCount;
    public float minParallax;
    public float maxParallax;
    public float starMaxDiameterScale;

    public BackgroundParameters(
        int layerCount,
        int starsCount,
        float minParallax,
        float maxParallax,
        float starMaxDiameterScale)
    {
        this.layerCount = layerCount;
        this.starsCount = starsCount;
        this.minParallax = minParallax;
        this.maxParallax = maxParallax;
        this.starMaxDiameterScale = starMaxDiameterScale;
    }
}
