package com.draga.manager;

import com.draga.gdxTestRunner.GdxTestRunner;
import com.draga.World;
import com.draga.manager.level.LevelManager;
import com.draga.manager.level.serialisableEntities.SerialisableWorld;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Created by Administrator on 05/09/2015.
 */
@RunWith(GdxTestRunner.class)
public class LevelManagerTest
{

    public static final String SERILISED_WORD = "{\n" +
        "  class: SerialisableWorld\n" +
        "  serialisedBackground: {\n" +
        "    texturePath: background4.jpg\n" +
        "  }\n" +
        "  serialisedShip: {\n" +
        "    texturePath: ship64.png\n" +
        "  }\n" +
        "  serialisedPlanets: [\n" +
        "    {\n" +
        "      texturePath: earth.png\n" +
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
        World world = LevelManager.getLevelWorld(SERILISED_WORD);

        Assert.assertNotNull(world);
    }
}