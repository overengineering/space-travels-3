package com.draga.spaceTravels3.gameEntity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GameEntityGroup implements Serializable
{
    public  GroupOverride                     groupOverride;
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
        return contains(gameEntity.getClass());
    }

    public boolean contains(Class<? extends GameEntity> klass)
    {
        switch (groupOverride)
        {
            case ALL:
                return true;
            case NONE:
                return false;
            case SELECTION:
                return this.gameEntities.contains(klass);
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
