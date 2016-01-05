package com.draga;

public class BackgroundAsyncLoader
{
    private final int        starLayersCount;
    private final Background background;

    public BackgroundAsyncLoader(final int starLayersCount, final int starCount)
    {
        this.starLayersCount = starLayersCount;
        this.background = new Background();

        final int starsPerLayer = starCount
            / starLayersCount;

        Runnable backgroundLoader = new Runnable()
        {
            @Override
            public void run()
            {
                for (int i = 0; i < starLayersCount; i++)
                {
                    BackgroundAsyncLoader.this.background.addStarLayer(starsPerLayer);
                }
            }
        };
        backgroundLoader.run();
    }

    public boolean doneLoading()
    {
        return this.background.getLayerCount() == this.starLayersCount;
    }

    public int layersDone()
    {
        return this.background.getLayerCount();
    }

    public int totalLayers()
    {
        return this.starLayersCount;
    }

    public Background getBackground()
    {
        return this.background;
    }
}
