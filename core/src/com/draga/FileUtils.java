package com.draga;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.draga.spaceTravels3.Constants;

public abstract class FileUtils
{
    public static FileHandle getScoreFileHandle()
    {
        FileHandle fileHandle = getExternalStorageFileHandle(Constants.General.SCORES_FILENAME);

        return fileHandle;
    }

    private static FileHandle getExternalStorageFileHandle(String filename)
    {
        FileHandle folderFileHandle;
        if (Gdx.files.isExternalStorageAvailable())
        {
            folderFileHandle = Gdx.files.external(Constants.General.FOLDER);
        }
        else
        {
            throw new RuntimeException("No available storage!");
        }

        if (!folderFileHandle.exists())
        {
            folderFileHandle.mkdirs();
        }

        FileHandle fileHandle = folderFileHandle.child(filename);

        return fileHandle;
    }

    public static FileHandle getSettingsFileHandle()
    {
        FileHandle fileHandle = getExternalStorageFileHandle(Constants.General.SETTINGS_FILENAME);

        return fileHandle;
    }

    public static FileHandle getDebugSettingsFileHandle()
    {
        FileHandle fileHandle =
            getExternalStorageFileHandle(Constants.General.DEBUG_SETTINGS_FILENAME);

        return fileHandle;
    }
}
