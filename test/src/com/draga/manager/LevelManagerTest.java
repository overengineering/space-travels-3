package com.draga.manager;

import com.draga.GdxTestRunner;
import com.draga.manager.serialisableEntities.SerialisableWorld;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Created by Administrator on 05/09/2015.
 */
@RunWith(GdxTestRunner.class)
public class LevelManagerTest
{
    @Test
    public void testGetSerialisedWord() throws Exception
    {
        String serilisedWord = "{\n" +
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
        SerialisableWorld serialisableWorld = LevelManager.getSerialisedWord(serilisedWord);

        Assert.assertNotNull(serialisableWorld);
    }
}