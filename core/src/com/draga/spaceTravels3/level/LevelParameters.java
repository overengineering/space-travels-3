package com.draga.spaceTravels3.level;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.utils.Array;
import com.draga.spaceTravels3.manager.level.serialisableEntities.SerialisableLevel;

public class LevelParameters extends AssetLoaderParameters<Level>
{
    public boolean           tutorial;
    public SerialisableLevel serialisableLevel;
    public String            difficulty;
    public Array<AssetDescriptor> dependencies = new Array<>();

    public LevelParameters(SerialisableLevel serialisableLevel, String difficulty, boolean tutorial)
    {
        this.serialisableLevel = serialisableLevel;
        this.difficulty = difficulty;
        this.tutorial = tutorial;
    }
}
