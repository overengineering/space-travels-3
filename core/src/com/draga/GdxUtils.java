package com.draga;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

public abstract class GdxUtils
{
    // TODO: use only external? Use always the constants FOLDER?
    public static FileHandle getExternalOrLocalStorageFileHandle(String folder, String filename)
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
