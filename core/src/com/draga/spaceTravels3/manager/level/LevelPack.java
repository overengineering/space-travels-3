package com.draga.spaceTravels3.manager.level;

import com.draga.spaceTravels3.manager.level.serialisableEntities.SerialisableLevel;

import java.util.ArrayList;

public class LevelPack
{
    private String                       name;
    private ArrayList<SerialisableLevel> serialisableLevels;

    public LevelPack(String name)
    {
        this.name = name;
        this.serialisableLevels = new ArrayList<>();
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
