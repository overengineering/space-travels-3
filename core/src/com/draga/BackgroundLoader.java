package com.draga;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.draga.spaceTravels3.Constants;

public class BackgroundLoader
    extends AsynchronousAssetLoader<Background, BackgroundLoader.BackgroundParameter>
{
    private static final FileHandleResolver NULL_FILE_HANDLE_RESOLVER = new FileHandleResolver()
    {
        @Override
        public FileHandle resolve(String fileName)
        {
            return null;
        }
    };
    private Background background;

    public BackgroundLoader()
    {
        super(NULL_FILE_HANDLE_RESOLVER);
    }

    @Override
    public void loadAsync(
        AssetManager manager,
        String fileName,
        FileHandle file,
        BackgroundParameter parameter)
    {
        if (parameter == null)
        {
            parameter = new BackgroundParameter();
        }

        this.background = new Background();
        for (int i = 0; i < parameter.layers; i++)
        {
            this.background.addStarLayerPixmap(
                parameter.starsCount / parameter.layers,
                parameter.minParallax,
                parameter.maxParallax);
        }
    }

    @Override
    public Background loadSync(
        AssetManager manager,
        String fileName,
        FileHandle file,
        BackgroundParameter parameter)
    {
        this.background.loadLayersFromPixmaps();

        return this.background;
    }

    @Override
    public Array<AssetDescriptor> getDependencies(
        String fileName, FileHandle file, BackgroundParameter parameter)
    {
        return null;
    }

    public class BackgroundParameter extends AssetLoaderParameters<Background>
    {
        public int   layers      = Constants.Visual.Background.LAYER_COUNT;
        public int   starsCount  = Constants.Visual.Background.STAR_COUNT;
        public float minParallax = Constants.Visual.Background.MIN_PARALLAX;
        public float maxParallax = Constants.Visual.Background.MAX_PARALLAX;
    }
}
