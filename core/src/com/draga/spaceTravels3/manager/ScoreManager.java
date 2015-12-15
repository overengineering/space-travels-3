package com.draga.spaceTravels3.manager;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.draga.FileUtils;
import com.draga.spaceTravels3.Constants;

import java.util.HashMap;

public abstract class ScoreManager
{
    private static final Json JSON = new Json();

    private static final FileHandle               levelScoresFileHandle =
        FileUtils.getScoreFileHandle();
    private static final HashMap<String, Integer> levelScores           = getLevelScores();

    public static void setScore(String levelId, int score)
    {
        levelScores.put(levelId, score);

        saveLevelScores();
    }

    private static void saveLevelScores()
    {
        @SuppressWarnings("ConstantConditions")
        String levelScoresString = Constants.General.IS_DEBUGGING
            ? JSON.prettyPrint(levelScores)
            : JSON.toJson(levelScores);

        levelScoresFileHandle.writeString(levelScoresString, false);
    }

    private static HashMap<String, Integer> getLevelScores()
    {
        if (levelScoresFileHandle.exists())
        {
            HashMap<String, Integer> levelScores =
                JSON.fromJson(HashMap.class, levelScoresFileHandle.readString());

            // Check the type of the first value if any is present because they used to be saved in
            // float and for some reason they successfully make it into the HashMap but then
            // failing to retrieve it.
            if (!levelScores.isEmpty() && (levelScores.values().toArray()[0] instanceof Integer))
            {
                return levelScores;
            }
        }

        return new HashMap<>();
    }

    public static int getScore(String levelName)
    {
        int score = 0;
        if (levelScores.containsKey(levelName))
        {
            score = Math.round(levelScores.get(levelName));
        }

        return score;
    }
}
