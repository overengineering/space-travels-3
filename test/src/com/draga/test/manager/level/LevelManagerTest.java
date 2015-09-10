package com.draga.test.manager.level;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.draga.manager.level.LevelManager;
import com.draga.manager.level.serialisableEntities.SerialisableWorld;
import com.draga.test.gdxTestRunner.GdxTestRunner;
import com.draga.World;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.mockito.Mockito.mock;

/**
 * Created by Administrator on 05/09/2015.
 */
@RunWith(GdxTestRunner.class)
public class LevelManagerTest
{

    public static final String SERILISED_WORD = "{\n" +
        "  class: SerialisableWorld\n" +
        "  serialisedBackground: {\n" +
        "    texturePath: \"../android/assets/background4.jpg\"\n" +
        "  }\n" +
        "  serialisedShip: {\n" +
        "    texturePath: \"../android/assets/ship64.png\"\n" +
        "  }\n" +
        "  serialisedPlanets: [\n" +
        "    {\n" +
        "      texturePath: \"../android/assets/earth.png\"\n" +
        "      x: 100\n" +
        "      y: 100\n" +
        "      diameter: 100\n" +
        "    }\n" +
        "  ]\n" +
        "}\n";

    @Test
    public void testGetSerialisedWord() throws Exception
    {
        SerialisableWorld serialisableWorld = LevelManager.getSerialisedWord(SERILISED_WORD);

        Assert.assertNotNull(serialisableWorld);
        Assert.assertNotNull(serialisableWorld.serialisedPlanets);
        Assert.assertNotNull(serialisableWorld.serialisedBackground);
        Assert.assertNotNull(serialisableWorld.serialisedShip);
        Assert.assertEquals(serialisableWorld.serialisedPlanets.size(), 1);
    }

    @Test
    public void testGetLevelWorld() throws Exception
    {
        World world = LevelManager.getLevelWorld(SERILISED_WORD, mock(SpriteBatch.class));

        Assert.assertNotNull(world);
    }
}