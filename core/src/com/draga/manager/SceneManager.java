package com.draga.manager;

import com.draga.manager.scene.GameScene;
import com.draga.manager.scene.Scene;

/**
 * Created by Administrator on 23/09/2015.
 */
public class SceneManager {
    private static Scene activeScene;

    public static Scene getActiveScene(){
        return activeScene;
    }
    
    public static void setActiveScene(GameScene scene) {
        if (activeScene != null){
            activeScene.pause();
            activeScene.dispose();
        }
        activeScene = scene;
    }
}
