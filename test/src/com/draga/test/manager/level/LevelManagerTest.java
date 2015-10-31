package com.draga.test.manager.level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.draga.manager.level.LevelManager;
import com.draga.manager.level.serialisableEntities.SerialisableLevel;
import com.draga.manager.screen.GameScreen;
import com.draga.test.gdxTestRunner.GdxTestRunner;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.mockito.Mockito.mock;

@RunWith(GdxTestRunner.class)
public class LevelManagerTest
{
    @Test
    public void testGetSerialisedLevelFromString() throws Exception
    {
        String testLevelJson = getTestLevelJson();
        SerialisableLevel serialisableLevel = LevelManager.getSerialisedLevelFromString(
            testLevelJson);

        Assert.assertNotNull(serialisableLevel);
        Assert.assertNotNull(serialisableLevel.serialisedPlanets);
        Assert.assertNotNull(serialisableLevel.serialisedBackground);
        Assert.assertNotNull(serialisableLevel.serialisedShip);
        Assert.assertNotEquals(serialisableLevel.serialisedPlanets.size(), 0);
    }

    @Test
    public void testGetLevelGameScreenFromString() throws Exception
    {
        String testLevelJson = getTestLevelJson();
        GameScreen gameScreen = LevelManager.getLevelGameScreenFromString(
            testLevelJson, mock(SpriteBatch.class));

        Assert.assertNotNull(gameScreen);
    }

    private String getTestLevelJson()
    {
        FileHandle testLevelFileHandle = Gdx.files.internal("level/level1.json");
        String testLevelJson = testLevelFileHandle.readString();

        return testLevelJson;
    }
}
