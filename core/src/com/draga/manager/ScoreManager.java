package com.draga.manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.draga.Constants;

import java.util.HashMap;

public abstract class ScoreManager
{
    private static final Json                   JSON                  = new Json();
    private static final FileHandle             levelScoresFileHandle = getScoreFileHandle();
    private static final HashMap<String, Float> levelScores           = getLevelScores();

    public static void updateScore(String levelName, float score)
    {
        levelScores.put(levelName, score);

        saveLevelScores(levelScores);
    }

    private static void saveLevelScores(final HashMap<String, Float> levelScores)
    {
        Runnable saveLevelScoreRunnable = new SaveLevelScoreRunnable();
        saveLevelScoreRunnable.run();
    }

    private static HashMap<String, Float> getLevelScores()
    {
        if (levelScoresFileHandle.exists())
        {
            HashMap<String, Float> levelScores =
                JSON.fromJson(HashMap.class, levelScoresFileHandle.readString());
            return levelScores;
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

    public static float getScore(String levelName)
    {
        float score = 0;
        if (levelScores.containsKey(levelName))
        {
            score = levelScores.get(levelName);
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
