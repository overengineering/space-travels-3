package com.draga.spaceTravels3.manager.level;

import com.draga.spaceTravels3.manager.level.serialisableEntities.SerialisableLevel;

import java.util.ArrayList;

public class LevelPack
{
    private String                       name;
    private ArrayList<SerialisableLevel> serialisableLevels;
    private boolean                      free;
    private String googleSku;

    public LevelPack(String name, boolean free, String googleSku)
    {
        this.name = name;
        this.free = free;
        this.googleSku = googleSku;
        this.serialisableLevels = new ArrayList<>();
    }

    public String getGoogleSku()
    {
        return this.googleSku;
    }

    public void setGoogleSku(String googleSku)
    {
        this.googleSku = googleSku;
    }

    public boolean isFree()
    {
        return this.free;
    }

    public void setFree(boolean free)
    {
        this.free = free;
    }

    public String getName()
    {
        return this.name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public ArrayList<SerialisableLevel> getSerialisableLevels()
    {
        return this.serialisableLevels;
    }

    public void setSerialisableLevels(ArrayList<SerialisableLevel> serialisableLevels)
    {
        this.serialisableLevels = serialisableLevels;
    }
}
