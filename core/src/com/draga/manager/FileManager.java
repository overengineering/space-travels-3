package com.draga.manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.draga.Constants;

// TODO: better name? Doesn't manage anything, just convenience
public abstract class FileManager
{
    public static FileHandle getFileHandle(String folder, String filename)
    {
        FileHandle folderFileHandle;
        if (Gdx.files.isExternalStorageAvailable())
        {
            folderFileHandle = Gdx.files.external(folder);
        }
        else if (Gdx.files.isLocalStorageAvailable())
        {
            folderFileHandle = Gdx.files.local(folder);
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
}
