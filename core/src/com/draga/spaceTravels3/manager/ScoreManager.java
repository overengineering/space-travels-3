package com.draga.spaceTravels3.manager;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import com.draga.errorHandler.ErrorHandlerProvider;
import com.draga.spaceTravels3.Constants;
import com.draga.utils.FileUtils;

import java.util.HashMap;

public abstract class ScoreManager
{
    private static final String LOGGING_TAG = ScoreManager.class.getSimpleName();

    private static final Json JSON = new Json(JsonWriter.OutputType.json);

    private static final FileHandle                                SCORE_FILE_HANDLE       =
        FileUtils.getScoreFileHandle();
    private static final HashMap<String, HashMap<String, Integer>> LEVEL_DIFFICULTY_SCORES =
        getLevelScores();

    /**
     * Saves the score if highest for this level.
     */
    public static void saveHighScore(String levelId, String difficulty, int score)
    {
        // Ensure the inner map of difficulty->score exists.
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

        // Save the score if higher.
        if (!difficultyScores.containsKey(difficulty)
            || difficultyScores.get(difficulty) < score)
        {
            difficultyScores.put(difficulty, score);
            saveLevelScores();
        }
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
                @SuppressWarnings("unchecked")
                HashMap<String, HashMap<String, Integer>> levelScores =
                    JSON.fromJson(HashMap.class, HashMap.class, SCORE_FILE_HANDLE);

                // TODO: serialisable objects instead of HashMap?
                // Ensure HashMap generic types
                if (!levelScores.isEmpty()
                    && levelScores.keySet().toArray()[0] instanceof String)
                {
                    @SuppressWarnings("unchecked")
                    HashMap<String, Integer> firstValue =
                        (HashMap<String, Integer>) levelScores.values().toArray()[0];
                    if (!firstValue.isEmpty()
                        && firstValue.keySet()
                        .toArray()[0] instanceof String
                        && firstValue.values()
                        .toArray()[0] instanceof Integer)
                    {
                        return levelScores;
                    }
                }
            }
        } catch (Exception exception)
        {
            ErrorHandlerProvider.handle(LOGGING_TAG, "Couldn't load scores", exception);
        }

        return new HashMap<>();
    }

    public static Integer getScore(String levelId, String difficulty)
    {
        if (!hasScore(levelId, difficulty))
        {
            return null;
        }

        int score = LEVEL_DIFFICULTY_SCORES.get(levelId).get(difficulty);

        return score;
    }

    public static boolean hasScore(String levelId, String difficulty)
    {
        if (LEVEL_DIFFICULTY_SCORES.containsKey(levelId))
        {
            HashMap<String, Integer> difficultyScores = LEVEL_DIFFICULTY_SCORES.get(levelId);
            if (difficultyScores != null && difficultyScores.containsKey(difficulty))
            {
                return true;
            }
        }

        return false;
    }
}
