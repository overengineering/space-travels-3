package com.draga.manager;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.draga.Constants;
import com.draga.Settings;

public abstract class SettingsManager
{
    private static final Json       JSON               = new Json();
    private static final FileHandle settingsFileHandle =
        FileManager.getSettingsFileHandle(Constants.FOLDER, Constants.SETTINGS_FILENAME);
    private static       Settings   settings           = getOrCreateSettings();

    public static Settings getSettings()
    {
        return settings;
    }

    private static Settings getOrCreateSettings()
    {
        if (settingsFileHandle.exists())
        {
            Settings settings =
                JSON.fromJson(Settings.class, settingsFileHandle.readString());
            return settings;
        }

        return new Settings();
    }

    public static void saveSettings()
    {
        String settingsString = Constants.IS_DEBUGGING
            ? JSON.prettyPrint(settings)
            : JSON.toJson(settings);

        settingsFileHandle.writeString(settingsString, false);
    }
}
