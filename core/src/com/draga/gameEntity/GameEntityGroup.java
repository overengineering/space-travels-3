package com.draga.gameEntity;

import java.util.ArrayList;
import java.util.List;

public class GameEntityGroup
{
    public  GroupOverride                     groupOverride = GroupOverride.ALL;
    private List<Class<? extends GameEntity>> gameEntities  = new ArrayList<>();

    public GameEntityGroup(List<Class<? extends GameEntity>> gameEntities)
    {
        this.groupOverride = GroupOverride.SELECTION;
        this.gameEntities = gameEntities;
    }

    public GameEntityGroup(GroupOverride groupOverride)
    {
        this.groupOverride = groupOverride;
    }

    public boolean contains(GameEntity gameEntity)
    {
        switch (groupOverride)
        {
            case ALL:
                return true;
            case NONE:
                return false;
            case SELECTION:
                return this.gameEntities.contains(gameEntity.getClass());
            default:
                throw new UnsupportedOperationException();
        }
    }

    public enum GroupOverride
    {
        NONE,
        SELECTION,
        ALL
    }

}
