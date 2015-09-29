package com.draga.manager;

import com.badlogic.gdx.Gdx;
import com.draga.Constants;
import com.draga.manager.scene.Scene;

/**
 * Created by Administrator on 23/09/2015.
 */
public class SceneManager
{
    private static final String LOGGING_TAG = SceneManager.class.getSimpleName();
    private static Scene activeScene;

    public static Scene getActiveScene()
    {
        return activeScene;
    }
    
    public static void setActiveScene(Scene scene)
    {
        if (Constants.IS_DEBUGGING)
        {
            Gdx.app.debug(LOGGING_TAG, "Set active scene " + scene.getClass().getSimpleName());
        }

        if (activeScene != null)
        {
            activeScene.pause();
            activeScene.dispose();
        }
        activeScene = scene;
    }
}
