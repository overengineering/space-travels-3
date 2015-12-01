package com.draga.manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.draga.Constants;

public abstract class FileManager
{
    public static FileHandle getSettingsFileHandle(String folder, String filename)
    {
        FileHandle folderFileHandle;
        if (Gdx.files.isExternalStorageAvailable())
        {
            folderFileHandle = Gdx.files.external(folder);
        }
        else if (Gdx.files.isLocalStorageAvailable())
        {
            folderFileHandle = Gdx.files.local(Constants.FOLDER);
        }
        else
        {
            throw new RuntimeException("No available storage!");
        }

        if (!folderFileHandle.exists())
        {
            folderFileHandle.mkdirs();
        }

        FileHandle scoresFileHandle = folderFileHandle.child(filename);

        return scoresFileHandle;
    }
}
