package com.draga.manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.draga.Constants;

import java.util.HashMap;

public abstract class ScoreManager
{
    private static final Json                     JSON                  = new Json();
    private static final FileHandle               levelScoresFileHandle = getScoreFileHandle();
    private static final HashMap<String, Integer> levelScores           = getLevelScores();

    public static void updateScore(String levelName, int score)
    {
        levelScores.put(levelName, score);

        saveLevelScores();
    }

    private static void saveLevelScores()
    {
        Runnable saveLevelScoreRunnable = new SaveLevelScoreRunnable();
        saveLevelScoreRunnable.run();
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

    private static FileHandle getScoreFileHandle()
    {
        FileHandle folderFileHandle;
        if (Gdx.files.isExternalStorageAvailable())
        {
            folderFileHandle = Gdx.files.external(Constants.SCORES_FOLDER);
        }
        else if (Gdx.files.isLocalStorageAvailable())
        {
            folderFileHandle = Gdx.files.local(Constants.SCORES_FOLDER);
        }
        else
        {
            throw new RuntimeException("No available storage available to save and load scores!");
        }

        if (!folderFileHandle.exists())
        {
            folderFileHandle.mkdirs();
        }

        FileHandle scoresFileHandle = folderFileHandle.child(Constants.SCORES_FILENAME);

        return scoresFileHandle;
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

    private static class SaveLevelScoreRunnable implements Runnable
    {
        @Override
        public void run()
        {
            String levelScoresString = Constants.IS_DEBUGGING
                ? JSON.prettyPrint(levelScores)
                : JSON.toJson(levelScores);

            levelScoresFileHandle.writeString(levelScoresString, false);
        }
    }
}
