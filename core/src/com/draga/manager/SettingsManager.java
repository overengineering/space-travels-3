package com.draga.manager;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.draga.Constants;
import com.draga.DebugSettings;
import com.draga.Settings;

public abstract class SettingsManager
{
    private static final Json JSON = new Json();

    private static final FileHandle    debugSettingsFileHandle =
        FileManager.getFileHandle(Constants.General.FOLDER, Constants.General.DEBUG_SETTINGS_FILENAME);
    private static final FileHandle settingsFileHandle =
        FileManager.getFileHandle(Constants.General.FOLDER, Constants.General.SETTINGS_FILENAME);

    private static       DebugSettings debugSettings           = getOrCreateDebugSettings();
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

    public static DebugSettings getDebugSettings()
    {
        return debugSettings;
    }

    private static DebugSettings getOrCreateDebugSettings()
    {
        if (debugSettingsFileHandle.exists())
        {
            DebugSettings settings =
                JSON.fromJson(DebugSettings.class, debugSettingsFileHandle.readString());
            return settings;
        }

        return new DebugSettings();
    }

    public static void saveSettings()
    {
        String debugSettingsString = Constants.General.IS_DEBUGGING
            ? JSON.prettyPrint(debugSettings)
            : JSON.toJson(debugSettings);

        debugSettingsFileHandle.writeString(debugSettingsString, false);

        String settingsString = Constants.General.IS_DEBUGGING
            ? JSON.prettyPrint(settings)
            : JSON.toJson(settings);

        settingsFileHandle.writeString(settingsString, false);
    }
}
