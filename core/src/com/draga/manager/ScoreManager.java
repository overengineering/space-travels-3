package com.draga.manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.draga.Constants;

import java.util.HashMap;

public abstract class ScoreManager
{
    private static final Json JSON = new Json();
    private static HashMap<String, Float> levelScores = getLevelScores();

    public static void updateScore(String levelName, float score)
    {
        levelScores.put(levelName, score);

        saveScores(levelScores);
    }

    private static HashMap<String, Float> getLevelScores()
    {
        FileHandle scoresFileHandle = getScoreFileHandle();
        if (scoresFileHandle.exists())
        {
            HashMap<String, Float> levelScores =
                JSON.fromJson(HashMap.class, scoresFileHandle.readString());
            return levelScores;
        }

        return new HashMap<>();
    }

    private static void saveScores(HashMap<String, Float> levelScores)
    {
        FileHandle scoresFileHandle = getScoreFileHandle();

        String scoresString = null;
        if (Constants.IS_DEBUGGING)
        {
            scoresString = JSON.prettyPrint(levelScores);
        }
        else
        {
            scoresString = JSON.toJson(levelScores);
        }

        scoresFileHandle.writeString(scoresString, false);
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
}
