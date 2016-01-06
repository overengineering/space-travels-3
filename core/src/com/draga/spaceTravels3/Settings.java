package com.draga.spaceTravels3;

import com.draga.spaceTravels3.manager.MusicManager;

public class Settings
{
    public  InputType inputType          = InputType.ACCELEROMETER;
    public  float     volumeFX           = 1f;
    public  boolean   hudForceIndicators = true;
    private float     volumeMusic        = 1f;

    public float getVolumeMusic()
    {
        return this.volumeMusic;
    }

    public void setVolumeMusic(float volumeMusic)
    {
        this.volumeMusic = volumeMusic;
        MusicManager.changeVolume(volumeMusic);
    }
}
