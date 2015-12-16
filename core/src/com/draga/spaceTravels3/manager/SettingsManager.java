package com.draga.spaceTravels3.manager;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.draga.FileUtils;
import com.draga.spaceTravels3.Constants;
import com.draga.spaceTravels3.DebugSettings;
import com.draga.spaceTravels3.Settings;

public abstract class SettingsManager
{
    private static final Json JSON = new Json();

    private static final FileHandle debugSettingsFileHandle =
        FileUtils.getDebugSettingsFileHandle();
    private static final FileHandle settingsFileHandle      =
        FileUtils.getSettingsFileHandle();

    private static DebugSettings debugSettings = getOrCreateDebugSettings();
    private static Settings      settings      = getOrCreateSettings();

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
        @SuppressWarnings("ConstantConditions")
        String debugSettingsString = Constants.General.IS_DEBUGGING
            ? JSON.prettyPrint(debugSettings)
            : JSON.toJson(debugSettings);

        debugSettingsFileHandle.writeString(debugSettingsString, false);

        @SuppressWarnings("ConstantConditions")
        String settingsString = Constants.General.IS_DEBUGGING
            ? JSON.prettyPrint(settings)
            : JSON.toJson(settings);

        settingsFileHandle.writeString(settingsString, false);
    }
}
