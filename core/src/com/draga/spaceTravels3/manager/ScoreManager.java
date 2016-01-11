package com.draga.spaceTravels3.manager;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.draga.ErrorHandlerProvider;
import com.draga.spaceTravels3.Constants;
import com.draga.utils.FileUtils;

import java.util.HashMap;

public abstract class ScoreManager
{
    private static final String LOGGING_TAG = ScoreManager.class.getSimpleName();

    private static final Json JSON = new Json();

    private static final FileHandle                                SCORE_FILE_HANDLE       =
        FileUtils.getScoreFileHandle();
    private static final HashMap<String, HashMap<String, Integer>> LEVEL_DIFFICULTY_SCORES =
        getLevelScores();

    /**
     * Saves the score if highest for this level.
     */
    public static void saveHighScore(String levelId, String difficulty, int score)
    {
        HashMap<String, Integer> difficultyScores;
        if (!LEVEL_DIFFICULTY_SCORES.containsKey(levelId))
        {
            difficultyScores = new HashMap<>();
            LEVEL_DIFFICULTY_SCORES.put(levelId, difficultyScores);
        }
        else
        {
            difficultyScores = LEVEL_DIFFICULTY_SCORES.get(levelId);
        }

        difficultyScores.put(difficulty, score);

        saveLevelScores();
    }

    private static void saveLevelScores()
    {
        @SuppressWarnings("ConstantConditions")
        String levelScoresString = Constants.General.IS_DEBUGGING
            ? JSON.prettyPrint(LEVEL_DIFFICULTY_SCORES)
            : JSON.toJson(LEVEL_DIFFICULTY_SCORES);

        SCORE_FILE_HANDLE.writeString(levelScoresString, false);
    }

    private static HashMap<String, HashMap<String, Integer>> getLevelScores()
    {
        try
        {
            if (SCORE_FILE_HANDLE.exists())
            {
                HashMap<String, HashMap<String, Integer>> levelScores =
                    JSON.fromJson(HashMap.class, SCORE_FILE_HANDLE);

                // Ensure HashMap generic type
                if (!levelScores.isEmpty()
                    && levelScores.keySet().toArray()[0] instanceof String)
                {
                    Object firstValue = levelScores.values().toArray()[0];
                    if (firstValue instanceof HashMap)
                    {
                        HashMap castedFirstValue = (HashMap) firstValue;
                        if (!castedFirstValue.isEmpty()
                            && castedFirstValue.keySet()
                            .toArray()[0] instanceof String
                            && castedFirstValue.values()
                            .toArray()[0] instanceof HashMap)
                        {
                            return levelScores;
                        }
                    }
                }
            }
        } catch (Exception exception)
        {
            ErrorHandlerProvider.handle(LOGGING_TAG, "Couldn't load scores", exception);
        }

        return new HashMap<>();
    }

    public static int getScore(String levelId, String difficulty)
    {
        int score = 0;
        if (LEVEL_DIFFICULTY_SCORES.containsKey(levelId))
        {
            HashMap<String, Integer> difficultyScores =
                ScoreManager.LEVEL_DIFFICULTY_SCORES.get(levelId);
            if (difficultyScores.containsKey(difficulty))
            {
                score = difficultyScores.get(difficulty);
            }
        }

        return score;
    }
}
