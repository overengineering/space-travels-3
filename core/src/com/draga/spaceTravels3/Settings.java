package com.draga.spaceTravels3;

import com.draga.spaceTravels3.event.InputTypeChangedEvent;
import com.draga.spaceTravels3.manager.MusicManager;

public class Settings
{
    public  float     volumeFX             = 1f;
    public  boolean   disableFaceUpWarning = false;
    private InputType inputType            = InputType.ACCELEROMETER;
    private float     volumeMusic          = 1f;

    public float getVolumeMusic()
    {
        return this.volumeMusic;
    }

    public void setVolumeMusic(float volumeMusic)
    {
        this.volumeMusic = volumeMusic;
        MusicManager.changeVolume(volumeMusic);
    }

    public InputType getInputType()
    {
        return this.inputType;
    }

    public void setInputType(InputType inputType)
    {
        this.inputType = inputType;

        Constants.General.EVENT_BUS.post(new InputTypeChangedEvent(inputType));
    }
}
