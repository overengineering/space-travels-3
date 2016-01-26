package com.draga.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.draga.spaceTravels3.Constants;

public final class FileUtils
{
    private FileUtils()
    {
    }

    public static FileHandle getScoreFileHandle()
    {
        FileHandle fileHandle = getLocalStorageFileHandle(Constants.General.SCORES_FILENAME);

        return fileHandle;
    }

    private static FileHandle getLocalStorageFileHandle(String filename)
    {
        FileHandle folderFileHandle;
        if (Gdx.files.isLocalStorageAvailable())
        {
            folderFileHandle = Gdx.files.local(Constants.General.FOLDER);
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
        FileHandle fileHandle = getFileHandle(Constants.General.SETTINGS_FILENAME);

        return fileHandle;
    }

    public static FileHandle getFileHandle(String fileName)
    {
        FileHandle fileHandle =
            getLocalStorageFileHandle(fileName);

        return fileHandle;
    }

    public static FileHandle getDebugSettingsFileHandle()
    {
        FileHandle fileHandle =
            getFileHandle(Constants.General.DEBUG_SETTINGS_FILENAME);

        return fileHandle;
    }
}
