package com.draga.test.manager.level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.draga.manager.level.LevelManager;
import com.draga.manager.level.serialisableEntities.SerialisableWorld;
import com.draga.manager.screen.GameScreen;
import com.draga.test.gdxTestRunner.GdxTestRunner;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.mockito.Mockito.mock;

@RunWith(GdxTestRunner.class) public class LevelManagerTest
{
    @Test public void testGetSerialisedWord() throws Exception
    {
        String testLevelJson = getTestLevelJson();
        SerialisableWorld serialisableWorld = LevelManager.getSerialisedGameSceneFromString(
            testLevelJson);

        Assert.assertNotNull(serialisableWorld);
        Assert.assertNotNull(serialisableWorld.serialisedPlanets);
        Assert.assertNotNull(serialisableWorld.serialisedBackground);
        Assert.assertNotNull(serialisableWorld.serialisedShip);
        Assert.assertEquals(serialisableWorld.serialisedPlanets.size(), 1);
    }

    @Test public void testGetLevelWorld() throws Exception
    {
        String testLevelJson = getTestLevelJson();
        GameScreen gameScreen = LevelManager.getLevelGameSceneFromString(
            testLevelJson, mock(SpriteBatch.class));

        Assert.assertNotNull(gameScreen);
    }

    private String getTestLevelJson()
    {
        FileHandle testLevelFileHandle = Gdx.files.internal("../android/assets/testLevel.json");
        String testLevelJson = testLevelFileHandle.readString();

        return testLevelJson;
    }
}
