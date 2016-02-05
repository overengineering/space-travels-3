package com.draga.joystick;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.PixmapIO;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.draga.NullFileHandleResolver;
import com.draga.errorHandler.ErrorHandlerProvider;
import com.draga.utils.FileUtils;
import com.draga.utils.PixmapUtils;
import com.google.common.base.Stopwatch;

import java.util.concurrent.TimeUnit;

public class JoystickLoader
    extends AsynchronousAssetLoader<Joystick, JoystickParameters>
{
    private static final String LOGGING_TAG =
        JoystickLoader.class.getSimpleName();

    private Pixmap    pixmap;
    private Stopwatch stopwatch;

    public JoystickLoader()
    {
        super(NullFileHandleResolver.NULL_FILE_HANDLE_RESOLVER);
    }

    @Override
    public void loadAsync(
        AssetManager assetManager,
        String fileName,
        FileHandle fileHandle,
        JoystickParameters joystickParameters)
    {
        this.stopwatch = Stopwatch.createStarted();

        if (joystickParameters == null)
        {
            ErrorHandlerProvider.handle(LOGGING_TAG, "JoystickParameters can't be null");
        }

        fileName = "joystick-" + joystickParameters + ".png";

        fileHandle = FileUtils.getFileHandle(fileName);

        this.pixmap = !fileHandle.exists()
            ? createAndSaveJoystick(fileHandle, joystickParameters)
            : new Pixmap(fileHandle);

        this.stopwatch.stop();
    }

    private Pixmap createAndSaveJoystick(
        FileHandle fileHandle,
        JoystickParameters joystickParameters)
    {
        Pixmap pixmap =
            new Pixmap(
                (int) joystickParameters.size,
                (int) joystickParameters.size,
                Pixmap.Format.RGBA8888);
        pixmap.setColor(joystickParameters.color);

        int numOuterArcs = 8;
        float halfSmallestDimension = joystickParameters.size / 2f;

        PixmapUtils.dashedCircle(
            pixmap,
            halfSmallestDimension,
            halfSmallestDimension,
            halfSmallestDimension,
            numOuterArcs,
            15,
            360 / numOuterArcs / 2,
            100,
            joystickParameters.strokeSize);
        PixmapUtils.dashedCircle(
            pixmap,
            halfSmallestDimension,
            halfSmallestDimension,
            halfSmallestDimension * joystickParameters.deadZone,
            4,
            30,
            0,
            100,
            joystickParameters.strokeSize);

        PixmapIO.writePNG(fileHandle, pixmap);

        return pixmap;
    }

    @Override
    public Joystick loadSync(
        AssetManager assetManager,
        String fileName,
        FileHandle fileHandle,
        JoystickParameters joystickParameters)
    {
        this.stopwatch.start();

        Joystick joystick = new Joystick(this.pixmap);

        Gdx.app.debug(LOGGING_TAG, +this.stopwatch.elapsed(
            TimeUnit.NANOSECONDS) * MathUtils.nanoToSec + "s");

        return joystick;
    }

    @Override
    public Array<AssetDescriptor> getDependencies(
        String fileName, FileHandle fileHandle, JoystickParameters joystickParameters)
    {
        return null;
    }
}
