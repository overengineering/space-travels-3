package com.draga;

import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;

public class NullFileHandleResolver
{
    static final FileHandleResolver NULL_FILE_HANDLE_RESOLVER = new FileHandleResolver()
    {
        @Override
        public FileHandle resolve(String fileName)
        {
            return null;
        }
    };
}
