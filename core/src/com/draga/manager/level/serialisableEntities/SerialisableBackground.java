package com.draga.manager.level.serialisableEntities;

/**
 * Created by Administrator on 05/09/2015.
 */
public class SerialisableBackground {
    private String texturePath;

    public SerialisableBackground() {
    }

    public SerialisableBackground(String texturePath) {
        this.texturePath = texturePath;
    }

    public String getTexturePath() {
        return texturePath;
    }

    public void setTexturePath(String texturePath) {
        this.texturePath = texturePath;
    }
}
